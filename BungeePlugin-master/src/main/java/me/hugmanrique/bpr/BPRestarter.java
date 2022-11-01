package me.hugmanrique.bpr;

import me.hugmanrique.bpr.data.commands.RestartPluginCmd;
import me.hugmanrique.bpr.utils.ConfigManager;
import me.hugmanrique.bpr.utils.Language;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.util.logging.Level;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public class BPRestarter extends Plugin {
    private static BPRestarter instance;

    private Configuration config;

    private Language language;

    @Override
    public void onEnable() {
        instance = this;
        config = ConfigManager.loadConfig(this, "config.yml");

        if (config == null){
            getLogger().log(Level.INFO, "Plugin disabled due to an error while loading the config");
        }

        language = new Language(this);

        // Register restart cmd
        getProxy().getPluginManager().registerCommand(this, new RestartPluginCmd(this));
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public Configuration getConfig() {
        return config;
    }

    public Language getLanguage() {
        return language;
    }

    public static BPRestarter getPlugin(){
        return instance;
    }
}
