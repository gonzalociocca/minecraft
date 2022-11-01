package flaminyogurt.plugins.superpotions;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Kits
{
  private final SuperPotions plugin;
  private FileConfiguration kitConfig = null;
  private File kitConfigFile = null;
  
  public Kits(SuperPotions instance)
  {
    this.plugin = instance;
    getKitConfig();
  }
  
  private void reloadKitConfig()
  {
    if (this.kitConfigFile == null) {
      this.kitConfigFile = new File(this.plugin.getDataFolder(), "kits.yml");
    }
    if (!this.kitConfigFile.exists()) {
      this.plugin.saveResource("kits.yml", false);
    }
    this.kitConfig = YamlConfiguration.loadConfiguration(this.kitConfigFile);
  }
  
  public FileConfiguration getKitConfig()
  {
    if (this.kitConfig == null) {
      reloadKitConfig();
    }
    return this.kitConfig;
  }
  
  public void saveKitConfig()
  {
    if ((this.kitConfig == null) || (this.kitConfigFile == null)) {
      return;
    }
    try
    {
      this.kitConfig.save(this.kitConfigFile);
    }
    catch (IOException ex)
    {
      this.plugin.logger.log(Level.SEVERE, "Could not save config to " + this.kitConfigFile, ex);
    }
  }
}
