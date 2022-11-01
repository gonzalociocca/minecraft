package me.hugmanrique.bpr.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import me.hugmanrique.bpr.BPRestarter;
import me.hugmanrique.bpr.data.events.PlayerRestartJoinEvent;
import me.hugmanrique.bpr.data.events.PlayerRestartQuitEvent;
import me.hugmanrique.bpr.utils.ReflectionUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public class PluginRestart {
    private static final long DEFAULT_WAIT_TIME = 5000L;

    private Plugin plugin;

    private RestartOption[] options;

    private long waitTime;
    private long startTime;

    public PluginRestart(Plugin plugin, long waitTime, RestartOption... options) {
        this.plugin = plugin;
        this.options = options;
        this.waitTime = waitTime;

        Preconditions.checkNotNull(plugin);
    }

    public PluginRestart(String pluginName, long waitTime, RestartOption... options){
        this(ProxyServer.getInstance().getPluginManager().getPlugin(pluginName), waitTime, options);
    }

    public PluginRestart(Plugin plugin, RestartOption... options){
        this(plugin, DEFAULT_WAIT_TIME, options);
    }

    public PluginRestart(String pluginName, RestartOption... options){
        this(pluginName, DEFAULT_WAIT_TIME, options);
    }

    public boolean run(){
        this.startTime = System.currentTimeMillis();

        URL jarUrl = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();

        unloadPlugin();

        // Check for plugin.yml or bungee.yml
        PluginDescription description = null;
        File jarFile = null;

        try {
            jarFile = new File(jarUrl.toURI());
            description = loadPluginDescription(jarFile);
        } catch (Exception ignored){}

        if (jarFile == null || description == null){
            log(Level.SEVERE, "Error while loading PluginDescription from plugin's JAR file");
            return false;
        }

        description.setFile(jarFile);

        // Check for dependencies
        PluginManager manager = getProxy().getPluginManager();

        for (String dependency : description.getDepends()){
            if (manager.getPlugin(dependency) == null){
                log(Level.WARNING, "{0} (required by {1}) is unavailable", description.getName(), dependency);
                return false;
            }
        }

        // Load the actual plugin
        return loadPlugin(description, manager);
    }

    private void unloadPlugin(){
        PluginManager manager = getProxy().getPluginManager();
        ClassLoader pluginClassLoader = plugin.getClass().getClassLoader();

        String pluginName = plugin.getDescription().getName();

        if (hasOption(RestartOption.FIRE_QUIT_EVENT)){
            for (ProxiedPlayer player : getProxy().getPlayers()){
                PlayerRestartQuitEvent event = new PlayerRestartQuitEvent(plugin, player);

                getProxy().getPluginManager().callEvent(event);
            }
        }

        // Call onDisable
        plugin.onDisable();

        // Remove all the info the plugin has in the Proxy
        for (Handler handler : plugin.getLogger().getHandlers()){
            handler.close();
        }

        manager.unregisterListeners(plugin);
        manager.unregisterCommands(plugin);

        getProxy().getScheduler().cancel(plugin);

        plugin.getExecutorService().shutdownNow();

        for (Thread thread : Thread.getAllStackTraces().keySet()){
            if (!thread.getClass().getClassLoader().equals(pluginClassLoader)){
                continue;
            }

            try {
                thread.interrupt();
                thread.join(2000L);

                if (thread.isAlive()){
                    thread.stop();
                }
            } catch (Throwable t){
                log(Level.SEVERE, "Failed to stop thread that belong to plugin", t, pluginName);
            }
        }

        // Remove other commands
        Map<String, Command> commandMap = ReflectionUtils.getFieldValue(manager, "commandMap");

        Iterator<Map.Entry<String, Command>> iterator = commandMap.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, Command> entry = iterator.next();

            if (entry.getValue().getClass().getClassLoader().equals(pluginClassLoader)){
                iterator.remove();
            }
        }

        // Remove listeners and commands maps from plugin references
        try {
            Map<String, Plugin> pluginMap = ReflectionUtils.getFieldValue(manager, "plugins");
            pluginMap.values().remove(plugin);

            Multimap<Plugin, Command> commands = ReflectionUtils.getFieldValue(manager, "commandsByPlugin");
            commands.removeAll(plugin);

            Multimap<Plugin, Listener> listeners = ReflectionUtils.getFieldValue(manager, "listenersByPlugin");
            listeners.removeAll(plugin);
        } catch (Exception e){
            log(Level.SEVERE, "Error while cleaning up Bungee internal maps from plugin references", e);
        }

        if (pluginClassLoader instanceof URLClassLoader){
            try {
                ((URLClassLoader) pluginClassLoader).close();
            } catch (IOException e){
                log(Level.SEVERE, "Failed to close ClassLoader from plugin", e, pluginName);
            }
        }

        // Remove the ClassLoader from the PluginClassloader static references
        Set<PluginClassloader> loaders = ReflectionUtils.getStaticValue(PluginClassloader.class, "allLoaders");
        loaders.remove(pluginClassLoader);
    }

    private boolean loadPlugin(PluginDescription description, PluginManager manager){
        try {
            URLClassLoader loader = new PluginClassloader(new URL[]{
                    plugin.getFile().toURI().toURL()
            });

            Class<?> main = loader.loadClass(description.getMain());
            Plugin clazz = (Plugin) main.getDeclaredConstructor().newInstance();

            // Some reflection needed
            ReflectionUtils.invokeMethod(clazz, "init", getProxy(), description);

            Map<String, Plugin> pluginMap = ReflectionUtils.getFieldValue(manager, "plugins");
            pluginMap.put(description.getName(), clazz);

            // Fire onLoad and onEnable
            clazz.onLoad();
            clazz.onEnable();

            getProxy().getLogger().log(Level.INFO, "Restarted plugin {0} version {1} by {2} in {3}ms", new Object[]{
                    description.getName(), description.getVersion(), description.getAuthor(), System.currentTimeMillis() - startTime
            });

            // Redefine plugin variable
            this.plugin = clazz;

            // Fire PlayerJoin events if necessary
            runPostLoad();

            return true;
        } catch (Throwable t){
            getProxy().getLogger().log(Level.WARNING, "Error restarting plugin " + description.getName(), t);
        }

        return false;
    }

    private void runPostLoad(){
        getProxy().getScheduler().schedule(BPRestarter.getPlugin(), () -> {
            if (hasOption(RestartOption.FIRE_JOIN_EVENT)){
                for (ProxiedPlayer player : getProxy().getPlayers()){
                    PlayerRestartJoinEvent event = new PlayerRestartJoinEvent(plugin, player);

                    getProxy().getPluginManager().callEvent(event);
                }
            }
        }, waitTime, TimeUnit.MILLISECONDS);
    }

    private PluginDescription loadPluginDescription(File file){
        try (JarFile jar = new JarFile(file)){
            JarEntry pdf = jar.getJarEntry("bungee.yml");

            if (pdf == null){
                pdf = jar.getJarEntry("plugin.yml");
            }

            try (InputStream in = jar.getInputStream(pdf)){
                PluginDescription desc = new Yaml().loadAs(in, PluginDescription.class);
                return desc;
            }

        } catch (Exception e){
            log(Level.SEVERE, "Error while loading plugin.yml from plugin", e, file.getName());
        }

        return null;
    }

    private boolean hasOption(RestartOption option){
        for (RestartOption opt : options){
            if (opt.equals(option)){
                return true;
            }
        }

        return false;
    }

    private ProxyServer getProxy(){
        return ProxyServer.getInstance();
    }

    private void log(Level level, String msg, Object... objs){
        getProxy().getLogger().log(level, msg, objs);
    }
}
