package me.hugmanrique.bpr.utils;

import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public class ConfigManager {

    public static Configuration loadConfig(Plugin plugin, String fileName){
        if (!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        File configFile = getConfigFile(plugin, fileName);

        if (!configFile.exists()){
            createConfigFile(plugin, configFile, fileName);
        }

        try {
            return getYamlProvider().load(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error loading the config file:", e);
        }

        return null;
    }

    private static boolean createConfigFile(Plugin plugin, File file, String fileName){
        try (InputStream in = plugin.getResourceAsStream(fileName)){
            Files.copy(in, file.toPath());

            return true;
        } catch (IOException e){
            return false;
        }
    }

    public static boolean saveConfig(Plugin plugin, Configuration config, String fileName){
        File file = getConfigFile(plugin, fileName);

        try {
            getYamlProvider().save(config, file);
            return true;
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Error saving the config file:", e);
        }

        return false;
    }

    private static File getConfigFile(Plugin plugin, String fileName){
        return new File(plugin.getDataFolder(), fileName);
    }

    private static ConfigurationProvider getYamlProvider(){
        return ConfigurationProvider.getProvider(YamlConfiguration.class);
    }
}
