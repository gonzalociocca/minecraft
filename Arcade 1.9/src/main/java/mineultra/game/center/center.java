package mineultra.game.center;

import java.io.File;

import me.gonzalociocca.mineultra.DBManager;
import mineultra.core.ImineultraPlugin;
import mineultra.core.common.Blood;
import mineultra.core.common.util.FileUtil;
import mineultra.core.common.util.UtilServer;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.npc.NpcManager;
import mineultra.core.portal.Portal;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.Updater;
import mineultra.minecraft.game.core.combat.CombatManager;
import mineultra.minecraft.game.core.damage.DamageManager;
import mineultra.game.center.game.GameServerConfig;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import mineultra.core.creature.Creature;

public class center extends JavaPlugin
  implements ImineultraPlugin
{
  private DamageManager _damageManager;
  private centerManager _gameManager;
    private DBManager dbmanager;
   Updater upd;

    VoidGenerator generator = new VoidGenerator(this);

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
    {
        return this.generator;
    }

  @Override
  public void onEnable()
  {
    this.delayEnable();
  }

    public void delayEnable(){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                try{
                Enable();}catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Failed to start.");
                    Bukkit.getServer().shutdown();
                }
            }
        }, 50L);
    }

    public void Enable(){
        DeleteFolders();
        this.saveDefaultConfig();
        dbmanager = new DBManager(this);

        ItemStackFactory.Initialize(this, false);
        Recharge.Initialize(this);

        Creature creature = new Creature(this);



        _damageManager = new DamageManager(this,this._gameManager, new CombatManager(this), new NpcManager(this, creature));

        Portal portal = new Portal(this);

        _gameManager = new centerManager(this,this, dbmanager,ReadServerConfig(), _damageManager, creature, new Blood(this), portal);

        System.out.println("Plugin/Game Author: Gonzalo Ciocca");
        System.out.println("My email: gonza_12_09@hotmail.com");
        upd = new Updater(this);
        //strange why two or infinite times?    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Updater(this), 1L, 1L);

    }

  @Override
  public void onDisable()
  {
    for (Player player : UtilServer.getPlayers()) {
      player.kickPlayer("Server Shutdown");
    }
    if ((_gameManager.GetGame() != null) && 
      (_gameManager.GetGame().WorldData != null))
      _gameManager.GetGame().WorldData.Uninitialize();
  }

  public GameServerConfig ReadServerConfig()
  {
    GameServerConfig config = new GameServerConfig();

            
          config.ServerType = "Minigames";
          config.MaxPlayers = this.GetPlugin().getConfig().getInt("GameConfig.maxplayers");
          config.MinPlayers = this.GetPlugin().getConfig().getInt("GameConfig.minplayers");
  
       if(!this.GetPlugin().getConfig().getStringList("GameConfig.games").isEmpty())
  {
   for(String esse : this.GetPlugin().getConfig().getStringList("GameConfig.games")){
       if(esse.equalsIgnoreCase("SWTeams")){
         config.GameList.add(GameType.SWTeams);
       }else if(esse.equalsIgnoreCase("Nexus")){
           config.GameList.add(GameType.DTN);
       }else if(esse.equalsIgnoreCase("Smash")){
           config.GameList.add(GameType.Smash);
       }else if(esse.equalsIgnoreCase("ZombieSurvival")){
           config.GameList.add(GameType.ZombieSurvival);
       }else if(esse.equalsIgnoreCase("BlockHunt")){
           config.GameList.add(GameType.BlockHunt);
       }else if(esse.equalsIgnoreCase("Spleef")){
           config.GameList.add(GameType.Spleef);
       }else if(esse.equalsIgnoreCase("Quiver")){
           config.GameList.add(GameType.Quiver);
       }else if(esse.equalsIgnoreCase("SkyWars")){
           config.GameList.add(GameType.SkyWars);
       }else if(esse.equalsIgnoreCase("BuildBattle")){
           config.GameList.add(GameType.BuildBattle);
   }else if(esse.equalsIgnoreCase("TurboRacers")){
           config.GameList.add(GameType.TurboRacers);
       }else if(esse.equalsIgnoreCase("SurvivalGames")){
           config.GameList.add(GameType.SurvivalGames);
       }
   }
  
  
  }else{
          config = this.GetDefaultConfig();
           }
  
       
  return config;
  }

  
  public GameServerConfig GetDefaultConfig()
  {
    GameServerConfig config = new GameServerConfig();

    config.ServerType = "Minigames";
    config.MinPlayers = 2;
    config.MaxPlayers = 16;

    return config;
  }
  


  private void DeleteFolders()
  {
    File curDir = new File(".");

    File[] filesList = curDir.listFiles();
    for (File file : filesList)
    {
      if (file.isDirectory())
      {
        if (file.getName().length() >= 4)
        {
          if (file.getName().substring(0, 4).equalsIgnoreCase("Game"))
          {
            FileUtil.DeleteFolder(file);

            System.out.println("Deleted latest game: " + file.getName());
          }
        }
      }
    }
  }

  @Override
  public JavaPlugin GetPlugin() { return this; }

  @Override
  public Server GetRealServer()
  {
    return getServer();
  }

  @Override
  public PluginManager GetPluginManager()
  {
    return GetRealServer().getPluginManager();
  }
}