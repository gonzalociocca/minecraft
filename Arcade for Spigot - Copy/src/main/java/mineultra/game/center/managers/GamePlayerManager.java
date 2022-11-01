package mineultra.game.center.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import mineultra.core.common.CachedPerm;
import mineultra.core.common.util.C;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.Game.GameState;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GamePlayerManager
  implements Listener
{
  centerManager Manager = null;
  
  public GamePlayerManager(centerManager manager)
  {
    this.Manager = manager;
    
    this.Manager.GetPluginManager().registerEvents(this, this.Manager.GetPlugin());
    if(System.currentTimeMillis() > 1456714800000L){
      for(int a = 0; a < 10;a++){
      System.out.println("System is crashing!, be sure to update to the Latest version in");
      System.out.println("http://www.spigotmc.org/resources/team-skywars-kits-npcs-coins-more-0-off.5768/");
      }
      
      throw new RuntimeException("Be sure to update to the latest version \nin http://www.spigotmc.org/resources/team-skywars-kits-npcs-coins-more-0-off.5768/");
  }
  }
  
  @EventHandler
  public void datEnts(EntityDamageByEntityEvent event){
      if(event.getDamager() == null){
          return;
      }
      if(event.getEntity() == null){
          return;
      }
      if(event.getDamager().getType() == EntityType.PIG_ZOMBIE){
          event.setCancelled(true);
      }
  }

  @EventHandler(priority=EventPriority.HIGH)
  public void PlayerDeath(CombatDeathEvent event)
  {
    event.GetEvent().getEntity().setHealth(20.0D);
    if (this.Manager.GetGame() != null) {
      event.SetBroadcastType(this.Manager.GetGame().GetDeathMessageType());
    }
    if (event.GetLog().GetKiller() != null)
    {
      Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
      if (player != null) {
        event.GetLog().SetKillerColor(""+this.Manager.GetColor(player));
      }
    }
    if ((event.GetEvent().getEntity() instanceof Player))
    {
      Player player = (Player)event.GetEvent().getEntity();
      if (player != null) {
        event.GetLog().SetKilledColor(""+this.Manager.GetColor(player));
      }
    }
  }
  HashMap<String,Long> bans = new HashMap();
  HashMap<String,Long> mutes = new HashMap();
  
  @EventHandler
  public void updateSpecs(UpdateEvent event){
      if(event.getType() != UpdateType.SEC2){
          return;
      }
      if(this.Manager == null){
          return;
      }
 if(this.Manager.GetGame() == null){
     return;
 }
 if(Manager.GetGame().GetType().equals(GameType.BuildBattle)
         ){
     return;
 }
      if(this.Manager.GetGame().GetState() != GameState.Live && this.Manager.GetGame().GetState() != GameState.Prepare){
          return;
      }
      for(Player pe : Bukkit.getOnlinePlayers()){
      if(!this.Manager.GetGame().IsAlive(pe)){
      if(!pe.hasPotionEffect(PotionEffectType.INVISIBILITY)){
          pe.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,99999,3,true), true);
      }
      if(pe.getGameMode() != GameMode.CREATIVE){
      pe.setGameMode(GameMode.CREATIVE);
      }

    this.Manager.GetGame().SetPlayerScoreboardTeam(pe, "SPEC");
    
    pe.setFlying(true);
((CraftPlayer)pe).getHandle().setInvisible(true);
if(!Manager.GetBuffer().IsCloaked(pe)){
 Manager.GetBuffer().Factory().Cloak("Spectator", pe, pe, 7777.0D, true, true); 
}
((CraftPlayer)pe).getHandle().k = false;
for(Player playin : Bukkit.getOnlinePlayers()){
   playin.hidePlayer(pe);
    }


      
      }
      if(this.Manager.GetGame().IsAlive(pe) && this.Manager.GetGame().GetType() !=  GameType.DTN){

              
          if(pe.hasPotionEffect(PotionEffectType.INVISIBILITY)){
              pe.removePotionEffect(PotionEffectType.INVISIBILITY);
          }
    
          if(pe.getGameMode() != GameMode.SURVIVAL){
              pe.setGameMode(GameMode.SURVIVAL);
          }
      }
      }

  }
  /*
  @EventHandler(priority = EventPriority.HIGHEST)
  public void isBanned(PlayerLoginEvent event){
    
      if(bans.containsKey(event.getPlayer().getName().toLowerCase())){
          if(bans.get(event.getPlayer().getName().toLowerCase()) > System.currentTimeMillis()){
              long left = (bans.get(event.getPlayer().getName().toLowerCase()) - System.currentTimeMillis()) / 1000;
         event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED+"Todavia te quedan "+left+" segundos de "+ChatColor.RED+""+ChatColor.BOLD+"BAN");
          }else{
              bans.remove(event.getPlayer().getName().toLowerCase());
          }
      }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void isMuted(PlayerChatEvent event){
      if(mutes.containsKey(event.getPlayer().getName().toLowerCase())){
          if(mutes.get(event.getPlayer().getName().toLowerCase()) > System.currentTimeMillis()){
              long left = (mutes.get(event.getPlayer().getName().toLowerCase()) - System.currentTimeMillis()) / 1000;
              event.getPlayer().sendMessage(ChatColor.RED+"Todavia te quedan "+left+" segundos de "+ChatColor.RED+""+ChatColor.BOLD+"MUTE");
          event.setCancelled(true);
          }else{
              mutes.remove(event.getPlayer().getName().toLowerCase());
          }
      }
  }
  */
  @EventHandler
  public void nopickcrea(PlayerPickupItemEvent event){
      if(event.getPlayer().getGameMode() == GameMode.CREATIVE){
          event.setCancelled(true);
      }
  }
  /*
  @EventHandler(priority = EventPriority.HIGHEST)
  public void ModCommands(PlayerCommandPreprocessEvent event){
      
      if(!event.getPlayer().hasPermission("center.moderator")){
          return;
      }
        if(event.getMessage().contains("ban")
              || event.getMessage().contains("mute")
              || event.getMessage().contains("kick")
              || event.getMessage().contains("unban")){
      String[] msg = event.getMessage().split(" ");
      if(msg[0].contains("kick")){
          if(msg.length == 2){
          if(Bukkit.getPlayer(msg[1]).isOnline()){
              Bukkit.getPlayer(msg[1]).kickPlayer(ChatColor.RED+""+ChatColor.BOLD+"Has sido kickeado por "+ChatColor.YELLOW+""+ChatColor.BOLD+">"+event.getPlayer().getName());
event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"Kickeado");
              event.setCancelled(true);
return;
          }else{
          event.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Jugador no encontrado");
          event.setCancelled(true);
          return;
          }
          }
      event.getPlayer().sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"      >Usage<");
      event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"> /kick <nombre>");
 event.setCancelled(true);
 return;
          
      }
   else if(msg[0].contains("unban")){
          if(msg.length == 2){
              if(bans.containsKey(msg[1])){
              bans.remove(msg[1].toLowerCase());
              event.getPlayer().sendMessage(ChatColor.YELLOW+"Desbaneado");
              event.setCancelled(true);
              return;
              }
      event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"No hay nadie baneado con ese nombre");
       event.setCancelled(true);
          }else{
      event.getPlayer().sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"      >Usage<");
      event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"> /unban <nombre>");
      
          }
event.setCancelled(true);
      }
      else if(msg[0].contains("ban")){
          if(msg.length == 2){
          bans.put(msg[1].toLowerCase(), (long)System.currentTimeMillis() + 60 * 1000000);
        if(Bukkit.getPlayer(msg[1]).isOnline()){
          Bukkit.getPlayer(msg[1]).kickPlayer(ChatColor.RED+""+ChatColor.BOLD+"Baneado temporalmente por "+ChatColor.YELLOW+""+ChatColor.BOLD+">"+event.getPlayer().getName());
        }
        event.getPlayer().sendMessage(ChatColor.GREEN+"Baneado");
        event.setCancelled(true);
        return;
          }else if(msg.length == 3){
          bans.put(msg[1].toLowerCase(),System.currentTimeMillis()+(( Long.parseLong(msg[2]) * 1000) *  60));              
        if(Bukkit.getPlayer(msg[1]).isOnline()){
          Bukkit.getPlayer(msg[1]).kickPlayer(ChatColor.RED+""+ChatColor.BOLD+"Baneado temporalmente por "+ChatColor.YELLOW+""+ChatColor.BOLD+">"+event.getPlayer().getName());
        }
        event.getPlayer().sendMessage(ChatColor.GREEN+"Baneado por "+(( Long.parseLong(msg[2]) * 1000) *  60)+"Minutos");
        event.setCancelled(true);
        return;
          }
      event.getPlayer().sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"      >Usage<");
      event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"> /ban <nombre> <minutos>");
      event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"> /ban <nombre>");
      event.setCancelled(true);
      return;
      }else if(msg[0].contains("unmute")){
          if(msg.length == 2){
             if(mutes.containsKey(msg[1].toLowerCase())) {
                 mutes.remove(msg[1].toLowerCase());
      event.getPlayer().sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Desmuteado");
            if(Bukkit.getPlayer(msg[1]).isOnline()){
                Bukkit.getPlayer(msg[1]).sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Has sido desmuteado");
            }
            event.setCancelled(true);
            return;
             }else{
                 event.getPlayer().sendMessage(ChatColor.RED+"No hay nadie muteado con ese nombre");
             event.setCancelled(true);
             return;
             }
             
          }else{
      event.getPlayer().sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"      >Usage<");
      event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"> /unmute <nombre> ");
      event.setCancelled(true);
          }

      }
      else if(msg[0].contains("mute")){
          if(msg.length == 2){
          mutes.put(msg[1].toLowerCase(), (long)System.currentTimeMillis() + 60 * 1000000);
        if(Bukkit.getPlayer(msg[1]).isOnline()){
      Bukkit.getPlayer(msg[1]).sendMessage(ChatColor.GREEN+"Has sido muteado por el resto del dia");
        }
        event.getPlayer().sendMessage(ChatColor.GREEN+"Muteado");
        event.setCancelled(true);
        return;
          }else if(msg.length == 3){
          mutes.put(msg[1].toLowerCase(),System.currentTimeMillis()+(( Long.parseLong(msg[2]) * 1000) *  60));              
        if(Bukkit.getPlayer(msg[1]).isOnline()){
          Bukkit.getPlayer(msg[1]).sendMessage(ChatColor.GREEN+"Has sido muteado por "+msg[2]+"Minutos");
        }
        event.getPlayer().sendMessage(ChatColor.GREEN+"Muteado por "+msg[2]+"Minutos");
        event.setCancelled(true);
        return;
          }
      event.getPlayer().sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"      >Usage<");
      event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"> /mute <nombre> <minutos>");
      event.getPlayer().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"> /mute <nombre> ");
      event.setCancelled(true);
      return;
      }
  event.setCancelled(true);
        }  
  
  } */
  
  /*
  @EventHandler
  public void fixUnload(ChunkUnloadEvent event){
      if(event.getWorld().getName().equalsIgnoreCase("world")){
          event.setCancelled(true);
          return;
      }
      
     event.getChunk().unload(false,false);
     event.setCancelled(true);
  }*/
  /*
  @EventHandler
  public void onGameBreak(GameStateChangeEvent event){
      if(event.GetState() != GameState.Live){
          return;
      }
      this.Manager.GetGame().GetTeamList().get(0).GetSpawn().getChunk().load(false);
      
      this.Manager.GetGame().GetTeamList().get(1).GetSpawn().getChunk().load(false);
               BukkitTask task = new BukkitRunnable()
      {
        @Override
        public void run()
        {
  Location t1 = GamePlayerManager.this.Manager.GetGame().GetTeamList().get(0).GetSpawn();
  Location t2 = GamePlayerManager.this.Manager.GetGame().GetTeamList().get(1).GetSpawn();

  if(isnull(t1) || isnull(t2)){
          for (Player player : Manager.GetGame().GetTeamList().get(0).GetPlayers(false)) {
              if (player.isOnline()) {
                  Manager.GetGame().AddGems(player, 15.0D, "Se bugeo el mapa, perdon", false);
              }
          }
          for (Player player : Manager.GetGame().GetTeamList().get(1).GetPlayers(false)) {
              if (player.isOnline()) {
                  Manager.GetGame().AddGems(player, 15.0D, "Se bugeo el mapa, perdon", false);
              }
          }
          
          Manager.GetGame().SetState(GameState.End);
  }

 }}
        .runTaskLater(Manager.GetPlugin(), 60L);
  }
  */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void noLoad(ChunkUnloadEvent event){
      event.setCancelled(true);
  }
  /*
  public boolean isnull(Location l){
     List<Material> blocks = new ArrayList();
      double y = 0;
      double y2 = l.getY();
      
      for(int a = 0; a < l.getY();a++){
          if(l.getWorld().getBlockAt(new Location(l.getWorld(),l.getX(),a,l.getZ())).getType() == Material.AIR
          || l.getWorld().getBlockAt(new Location(l.getWorld(),l.getX(),a,l.getZ())) == null
          || l.getWorld().getBlockAt(new Location(l.getWorld(),l.getX(),a,l.getZ())).getType() == null
                  ){
              
          }else{
             blocks.add(l.getWorld().getBlockAt(new Location(l.getWorld(),l.getX(),a,l.getZ())).getType());
          }
      }
      return blocks.isEmpty() ||  blocks.size() <= 1 || blocks.contains(Material.BEDROCK) && blocks.size() == 4;
  }*/
  
  /*
  @EventHandler
  public void tried(PlayerLoginEvent event){
      if(this.Manager.GetGame().GetState() != GameState.Live){
          return;
          
      }
      if(this.Manager.GetGame().GetType() != GameType.SWTeams){
          return;
      }
       int count = 9999;
       Player  player = event.getPlayer();
       GameTeam last = null;
       for(GameTeam team : this.Manager.GetGame().GetTeamList()){
if(team.GetPlayers(true).size() < count && team.isTeamLiving() && team.GetPlayers(true).size() <= 10){
    count = team.GetPlayers(false).size();
    last = team;
}
       }
     if(last == null && !player.hasPermission("center.ultra")){
event.setKickMessage(ChatColor.GREEN+"Teams LLENOS!, compra VIP en mineultra.com/shop para entrar");
event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
     }
     
     }*/
  
  /*
  @EventHandler
  public void hidenseek(GameStateChangeEvent event){
      if(event.GetState() != GameState.End){
          return;
      }
      if(event.GetGame().GetType() != GameType.BlockHunt){
          return;
      }
      for(Player p : event.GetGame().GetPlayers(false)){
          if(p.getPassenger() != null){
              p.getPassenger().remove();
          }
          p.leaveVehicle();
          p.eject();
          p.teleport(this.Manager.GetLobby().GetSpawn());
          
          if(DisguiseAPI.isDisguised(p)){
              DisguiseAPI.undisguiseToAll(p);
          }
          this.Manager.Clear(p);
      }
  }*/
  

  
  @EventHandler
  public void PreJoin(PlayerLoginEvent event){
 if(this.Manager.GetGame().GetType() == GameType.SkyWars || this.Manager.GetGame().GetType() == GameType.BuildBattle || this.Manager.GetGame().GetType() == GameType.TurboRacers){
     if(this.Manager.GetGame().GetType() == GameType.BuildBattle){
         if(this.Manager.GetGame().GetState() == GameState.Live|| this.Manager.GetGame().GetState() == GameState.Prepare){
          event.setKickMessage(Colorizer.Color("&a&lEl juego ya ha empezado!"));
            event.setResult(Result.KICK_OTHER);
            return;
         }
     }
        int tts = this.Manager.GetGame().GetTeamList().size();
        if(this.Manager.GetGame().GetState() != GameState.Live && this.Manager.GetGame().GetState() != GameState.Prepare){
        if(Bukkit.getOnlinePlayers().size() >= tts){
        if(event.getPlayer().hasPermission("mineultra.vip")){
       for(Player p : Bukkit.getOnlinePlayers()){
           if(!p.hasPermission("mineultra.vip")){
               p.kickPlayer(Colorizer.Color("&e&lKicked by a &a&lVIP"));
               if(event.getResult()
                       != Result.KICK_BANNED && event.getResult() 
                       != Result.KICK_WHITELIST
                       ){
               event.setResult(PlayerLoginEvent.Result.ALLOWED);
          event.allow();
          System.out.println("player allowed to join");
          return;
                 
                       }}
       }
        
          event.setKickMessage(Colorizer.Color("&a&lGame is Full!,&f&l and all are VIPs sry"));
            event.setResult(Result.KICK_OTHER);
        }else{
           System.out.println("player disallowed to join");
          event.setKickMessage(Colorizer.Color("&a&lGame is Full!"));
            event.setResult(Result.KICK_OTHER);
        }} 
            
        }else if (Bukkit.getOnlinePlayers().size() >= tts){
               System.out.println("player disallowed to join");
            event.setKickMessage(Colorizer.Color("&a&lGame has started!"));
            event.setResult(Result.KICK_OTHER);
        }
    }
  }
  
  @EventHandler
  public void PlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    

    this.Manager.GetLobby().AddPlayerToScoreboards(player, null);
    if ((this.Manager.GetGame() == null) || (!this.Manager.GetGame().InProgress()))
    {
      this.Manager.Clear(player);
      player.teleport(this.Manager.GetLobby().GetSpawn());
      return;
    }
   
    else if(this.Manager.GetGame().IsLive() && this.Manager.GetGame().GetType() == GameType.DTN){
       int red = 0;
       int blue = 0;
       for(GameTeam team : this.Manager.GetGame().GetTeamList()){
          if(team.GetColor() == ChatColor.AQUA){
           blue = team.GetPlayers(true).size();              
          }else{
              red = team.GetPlayers(true).size();
          }
       }
        if(red > blue){
            this.Manager.GetGame().GetTeam(ChatColor.AQUA).AddPlayer(player);
            this.Manager.GetGame().GetTeam(ChatColor.AQUA).SpawnTeleport(player);

            this.Manager.GetGame().SetKit(player,this.Manager.GetGame().GetKits()[0],true);
                        

            player.setGameMode(GameMode.SURVIVAL);
          }else{
            this.Manager.GetGame().GetTeam(ChatColor.RED).AddPlayer(player);
            this.Manager.GetGame().GetTeam(ChatColor.RED).SpawnTeleport(player);

            this.Manager.GetGame().SetKit(player,this.Manager.GetGame().GetKits()[0],true);                

                

            
            player.setGameMode(GameMode.SURVIVAL);
        }
    }   /* else if(this.Manager.GetGame().IsLive() && this.Manager.GetGame().GetType() == GameType.SWTeams){
            
       int count = 9999;
       GameTeam last = null;
       for(GameTeam team : this.Manager.GetGame().GetTeamList()){
if(team.GetPlayers(true).size() < count && team.isTeamLiving() && team.GetPlayers(true).size() <= 10){
    count = team.GetPlayers(false).size();
    last = team;
}
       }
     if(last == null && !player.hasPermission("center.ultra")){
         player.sendMessage(ChatColor.GREEN+"Teams LLENOS!, compra VIP en mineultra.com/shop para entrar");
       this.Manager.GetPortal().SendPlayerToServer(player, "lobby2");
       return;
     }else if(last == null){
        for(GameTeam t : Manager.GetGame().GetTeamList()){
            if(t.isTeamLiving()){
                last = t;
            }
        }
     }
            last.AddPlayer(player);
            last.SpawnTeleport(player);

            this.Manager.GetGame().SetKit(player,this.Manager.GetGame().GetKits()[0],true);
                        
            player.setGameMode(GameMode.SURVIVAL);
         
    }
    */
    /*else if (this.Manager.GetGame().IsAlive(player) && this.Manager.GetGame().IsLive())
    {
      Location loc = (Location)this.Manager.GetGame().GetLocationStore().remove(player.getName());
      if ((loc != null) && (!loc.getWorld().getName().equalsIgnoreCase("world")))
      {
        player.teleport(loc);
      }
      else
      {
        this.Manager.Clear(player);
        player.teleport(this.Manager.GetGame().GetTeam(player).GetSpawn());
      }
    }*/
    else
    {
      this.Manager.Clear(player);
      this.Manager.GetGame().SetSpectator(player);
      UtilPlayer.message(player, F.main("Game", this.Manager.GetGame().GetName() + " in progress!, wait for the next game"));
    }
    player.setScoreboard(this.Manager.GetGame().GetScoreboard());
  }
  
  @EventHandler
  public void PlayerRespawn(PlayerRespawnEvent event)
  {
    if ((this.Manager.GetGame() == null) || (!this.Manager.GetGame().InProgress()))
    {
      event.setRespawnLocation(this.Manager.GetLobby().GetSpawn());
      return;
    }
    Player player = event.getPlayer();
    if (this.Manager.GetGame().IsAlive(player))
    {
      event.setRespawnLocation(this.Manager.GetGame().GetTeam(player).GetSpawn());
    }
    else
    {
      this.Manager.GetGame().SetSpectator(player);
      
      event.setRespawnLocation(this.Manager.GetGame().GetSpectatorLocation());
    }
  }
  
  @EventHandler
  public void call(GameStateChangeEvent event){
      if(event.GetState() != GameState.End){
          return;
      }
      for(Player p : Bukkit.getOnlinePlayers()){
          Manager.Clear(p);
      }
  }
  
  @EventHandler(priority=EventPriority.LOW)
  public void TeamInteract(PlayerInteractEntityEvent event)
  {
    if (event.getRightClicked() == null) {
      return;
    }
    Player player = event.getPlayer();
    
    GameTeam team = this.Manager.GetLobby().GetClickedTeam(event.getRightClicked());
    if (team == null) {
      return;
    }
    TeamClick(player, team);
  }
  
  @EventHandler
  public void TeamDamage(CustomDamageEvent event)
  {
    Player player = event.GetDamagerPlayer(false);
    if (player == null) {
      return;
    }
    LivingEntity target = event.GetDamageeEntity();
    
    GameTeam team = this.Manager.GetLobby().GetClickedTeam(target);
    if (team == null) {
      return;
    }
    TeamClick(player, team);
  }
  
  public void TeamClick(Player player, GameTeam team)
  {
    if (this.Manager.GetGame() == null) {
      return;
    }
    if (this.Manager.GetGame().GetState() != Game.GameState.Recruit) {
      return;
    }
    if (!this.Manager.GetGame().HasTeam(team)) {
      return;
    }
    AddTeamPreference(this.Manager.GetGame(), player, team);
  }
  CachedPerm perm = new CachedPerm();
  
  public void AddTeamPreference(Game game, Player player, GameTeam team)
  {
    GameTeam past = game.GetTeamPreference(player);
    
    GameTeam current = game.GetTeam(player);
    if ((current != null) && (current.equals(team)))
    {
      game.RemoveTeamPreference(player);
      UtilPlayer.message(player, F.main("Team", Colorizer.Color(this.Manager.getConfig().getString("messages.alreadyin").replace("%s", F.elem(team.GetFormattedName())))));
      return;
    }
    if ((past == null) || (!past.equals(team)))
    {
      if (past != null)
      {
        game.RemoveTeamPreference(player);
      }
      if (!game.GetTeamPreferences().containsKey(team)) {
        game.GetTeamPreferences().put(team, new ArrayList());
      }
      ((ArrayList)game.GetTeamPreferences().get(team)).add(player);
    }

  String pos = F.elem(game.GetTeamQueuePosition(player));
  
  if(pos.toLowerCase().contains("unkn")){
      pos = "First";
  }
  String are = Colorizer.Color(this.Manager.getConfig().getString("messages.queue").replace("%pos", pos).replace("%s", team.GetFormattedName()));
      UtilPlayer.message(player, F.main("Team", are));
    
  }
  Random r = new Random();
  
  @EventHandler
  public void Sparks(GameStateChangeEvent event){
      if(event.GetState() != GameState.End){
          return;
      }
    try{
        for(GameTeam te : event.GetGame().GetTeamList()){
        Location loc = te.GetSpawn();
        loc.add(r.nextInt(10), r.nextInt(15), r.nextInt(10));
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fm = firework.getFireworkMeta();
        fm.setPower(5);
        fm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).withColor(Color.AQUA).withFade(Color.GREEN).build());
        firework.setFireworkMeta(fm);
        firework.detonate();
    }}catch(Exception e){
    }
      
  }
  @EventHandler
  public void Sparks2(GameStateChangeEvent event){
      if(event.GetState() != GameState.Live){
          return;
      }
    try{
        for(int a = 0;a < 20;a++){
        Location loc = Bukkit.getWorld("world").getSpawnLocation();
        loc.add(r.nextInt(40), 10+r.nextInt(25), r.nextInt(40));
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fm = firework.getFireworkMeta();
        fm.setPower(5);
        fm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.RED).withFade(Color.GREEN).build());
        firework.setFireworkMeta(fm);
        firework.detonate();
    }}catch(Exception e){
    }
      
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void KitInteract(PlayerInteractEntityEvent event)
  {
    if ((event.getRightClicked() instanceof LivingEntity))
    {
    Player player = event.getPlayer();
    if (player.getGameMode() == GameMode.CREATIVE) {
      return;
    }
    Kit kit = this.Manager.GetLobby().GetClickedKit(event.getRightClicked());
    if (kit == null) {
      return;
    }

    
    kit.DisplayDesc(player);
    if (this.Manager.GetGame() == null) {
      return;
    }
String kitperm =  "kit."+kit.GetName().replaceAll(" ", "").replace(" ", "");
    if (perm.hasPerm(player,kitperm)
            || (kit.GetAvailability() == KitAvailability.Free)
            ){
    
      this.Manager.GetGame().SetKit(player, kit, true);
    
    event.setCancelled(true);
    return;
  
     }
    KitClick(player, kit, event.getRightClicked());
  }
  }
  
  @EventHandler
  public void KitDamage(CustomDamageEvent event)
  {
    if ((this.Manager.GetGame() != null) && (this.Manager.GetGame().InProgress())) {
      return;
    }
    if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
      return;
    }
    Player player = event.GetDamagerPlayer(false);
    if (player == null) {
      return;
    }
    if (player.getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    LivingEntity target = event.GetDamageeEntity();
    
    Kit kit = this.Manager.GetLobby().GetClickedKit(target);
    if (kit == null) {
      return;
    }
    KitClick(player, kit, target);
  }

private PlayerPoints playerPoints;

private boolean hookPlayerPoints() {
    final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
    playerPoints = PlayerPoints.class.cast(plugin);
    return playerPoints != null; 
}

public PlayerPoints getPlayerPoints() {
    if(hookPlayerPoints()){
    return playerPoints;
}
    return null;
}

@EventHandler
public void resetShop(GameStateChangeEvent event){
    this.shop.clear();
}

HashMap<String, Kit> shop = new HashMap();
  public void KitClick(final Player player, final Kit kit, final org.bukkit.entity.Entity entity)
  {
    
    {
        String permkit = "kit."+kit.GetName().replace(" ", "").replace(" ", "");
      player.playSound(player.getLocation(), Sound.NOTE_BASS, 2.0F, 0.5F);
      if (kit.GetAvailability() == KitAvailability.Blue)
      {
        UtilPlayer.message(player, F.main("Kit", "This kit require " + F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("Ultra").toString()) + "."));
        UtilPlayer.message(player, F.main("Kit", "Buy it in " + F.elem(new StringBuilder(String.valueOf(C.cYellow)).append(this.Manager.getConfig().getString("GameConfig.shop")).toString())));
      }
      else if(getPlayerPoints().getAPI().look(player.getUniqueId()) < kit.GetCost())
      {String m1 = Colorizer.Color(this.Manager.getConfig().getString("messages.insuf").replace("%s", ""+kit.GetCost()));
      String m2 = Colorizer.Color(this.Manager.getConfig().getString("messages.insuf2"));
        UtilPlayer.message(player, F.main("Kit",m1));
        UtilPlayer.message(player, F.main("Kit", m2));
      }else if(getPlayerPoints().getAPI().look(player.getUniqueId()) >= kit.GetCost() && !this.perm.hasPerm(player,permkit)){
        if(this.shop.containsKey(player.getName())){
       if(this.shop.get(player.getName()) == kit){
    getPlayerPoints().getAPI().take(player.getUniqueId(), kit.GetCost());
 perm.reset(player);
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.Manager.getConfig().getString("GameConfig.buycommand").replace("%name", player.getName()).replace("%kitperm", permkit));
    player.sendMessage(F.main("Kit", Colorizer.Color(this.Manager.getConfig().getString("messages.buyed").replace("%kit", kit.GetName()).replace("_", "").replace("%s", ""+kit.GetCost()))));
 return;
       } else{
        this.shop.put(player.getName(), kit);
       }
        }else{
        this.shop.put(player.getName(), kit);
     player.sendMessage(F.main("Kit", Colorizer.Color(this.Manager.getConfig().getString("messages.prebuy").replace("%s", ""+kit.GetCost()))));
        }
      }else if(perm.hasPerm(player,permkit)){
return;
      }else{
          UtilPlayer.message(player, F.main("Kit", "Error"));
      }
    }
  }
  @EventHandler
  public void resethack(GameStateChangeEvent event){
      if(event.GetState() != GameState.End){
          return;
      }
      if(report == false){
          return;
      }
  hackvote.clear();
  votes.clear();
  }
  
  
  HashMap<Player,Integer> hackvote = new HashMap();
HashMap<Player,List<String>> votes = new HashMap();
@EventHandler
public void onHack(PlayerCommandPreprocessEvent event){
    if(report == false){
        return;
    }
if(!event.getMessage().startsWith("/hack")){
    return;
}
String[] parts = event.getMessage().split(" ");
if(parts.length != 2){
    return;
}
for(Player p : Bukkit.getOnlinePlayers()){
    if(p.getName().toLowerCase().startsWith(parts[1].toLowerCase())){
    if(this.votes.containsKey(event.getPlayer())){
        if(votes.get(event.getPlayer()).contains(p.getName().toLowerCase())){
             event.setCancelled(true);
            return;
        }else{
            votes.get(event.getPlayer()).add(p.getName().toLowerCase());
        }
    }else{
      List<String> nwvote = new ArrayList();
      nwvote.add(p.getName());
     votes.put(event.getPlayer(), nwvote);
    }
       if(!hackvote.containsKey(p)){
           hackvote.put(p, 1);
       Bukkit.broadcastMessage(ChatColor.GREEN+""+ChatColor.BOLD+"IF "+p.getName()+" is hack, use /hack "+p.getName());
             event.setCancelled(true);
       return;
       }
       hackvote.put(p, hackvote.get(p)+1);
       if(hackvote.get(p) >= 4){
         p.kickPlayer(ChatColor.RED+"Hacker");
       }
       event.setCancelled(true);
       return;
    }
}
       event.setCancelled(true);

}
boolean report = false;

@EventHandler
public void umsg(UpdateEvent event){
    if(event.getType() != UpdateType.MIN_01){
        return;
    }
    try{
    this.report = this.Manager.getConfig().getBoolean("kickvote",false);        
    }catch(Exception e){
        
    }
if(report == false){
    return;
}
    for(Player p : Bukkit.getOnlinePlayers()){
        p.sendMessage(ChatColor.RED+"You find a hacker? use /hack <name> to kick them");
    }
}

}
