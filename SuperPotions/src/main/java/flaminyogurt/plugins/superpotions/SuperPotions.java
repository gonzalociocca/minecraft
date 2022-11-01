package flaminyogurt.plugins.superpotions;

import java.io.File;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperPotions
  extends JavaPlugin
{
  public final Logger logger = Logger.getLogger("Minecraft");
  private String PREFIX;
  public boolean isUpdate;
  public FileConfiguration config;
  public Kits kits;
  public Economy economy;
  public static SuperPotions instance;
  
  @Override
  public void onEnable()
  {
    instance = this;
    
    PluginDescriptionFile pdFile = getDescription();
    this.PREFIX = ("[" + pdFile.getName() + "]");
    
    this.config = getConfig();
    saveDefaultConfig();
    
    this.kits = new Kits(this);
    
    getCommand("potion").setExecutor(new CommandHandler());
    getServer().getPluginManager().registerEvents(new Listeners(this), this);

    if ((this.config.getBoolean("economy")) && 
      (!setupEconomy()))
    {
      this.logger.severe(this.PREFIX + " Can't hook to vault, economy disabled.");
      this.config.set("economy", Boolean.valueOf(false));
    }
    this.logger.info(this.PREFIX + " version " + pdFile.getVersion() + " has been enabled.");
    this.logger.info(this.PREFIX + " made by: " + pdFile.getAuthors());
  }
  
  @Override
  public void onDisable()
  {
    saveConfig();
    this.kits.saveKitConfig();
    this.logger.info(this.PREFIX + " plugin disabled.");
  }
  
  public File getPluginFile()
  {
    return getFile();
  }
  
  private boolean setupEconomy()
  {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      return false;
    }
    this.economy = ((Economy)rsp.getProvider());
    return this.economy != null;
  }
}
