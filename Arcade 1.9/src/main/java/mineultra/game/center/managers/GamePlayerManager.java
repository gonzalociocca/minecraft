package mineultra.game.center.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.gonzalociocca.mineultra.DBManager;
import me.gonzalociocca.mineultra.Rank;
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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GamePlayerManager
  implements Listener
{
  centerManager Manager = null;
  DBManager db = null;
  public GamePlayerManager(centerManager manager)
  {
    this.Manager = manager;
      db = manager.getDB();
    
    this.Manager.GetPluginManager().registerEvents(this, this.Manager.GetPlugin());

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
          db.getPlayerData(player).addKills(1);
        event.GetLog().SetKillerColor(""+this.Manager.GetColor(player));
      }
    }
    if ((event.GetEvent().getEntity() instanceof Player))
    {
      Player player = (Player)event.GetEvent().getEntity();
      if (player != null) {
      db.getPlayerData(player).addDeaths(1);
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
//WAT again((CraftPlayer)pe).getHandle().k = false;
((CraftPlayer)pe).getHandle().setSneaking(false);
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

  @EventHandler
  public void nopickcrea(PlayerPickupItemEvent event){
      if(event.getPlayer().getGameMode() == GameMode.CREATIVE){
          event.setCancelled(true);
      }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void noLoad(ChunkUnloadEvent event){
      event.setCancelled(true);
  }
  
  @EventHandler
  public void PlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(30);

    this.Manager.GetLobby().AddPlayerToScoreboards(player, null);
    if ((this.Manager.GetGame() == null) || (!this.Manager.GetGame().InProgress()))
    {
      this.Manager.Clear(player);
      player.teleport(this.Manager.GetLobby().GetSpawn());
      return;
    }
      //      a< b ? a : b // a < b = a  // a > b = b

    else if(Manager.GetGame().IsLive() && Manager.GetGame().canJoinAfterStart
            || Manager.GetGame().GetState()==GameState.Prepare && Manager.GetGame().canJoinAfterStart){

       int size = 1000;
       GameTeam tm = null;
       for(GameTeam team : this.Manager.GetGame().GetTeamList()){
           if(team.GetPlayers(true).size() < size){
               tm = team;
               size = team.GetPlayers(true).size();
           }
       }

            this.Manager.GetGame().SetPlayerTeam(player,tm);
            tm.SpawnTeleport(player);
            this.Manager.GetGame().SetKit(player,this.Manager.GetGame().GetKits()[0],true);
            player.setGameMode(GameMode.SURVIVAL);

    }
    else
    {
      this.Manager.Clear(player);
      this.Manager.GetGame().SetSpectator(player);
      UtilPlayer.message(player, F.main("Game", this.Manager.GetGame().GetName() + " en progreso!, espera al siguiente juego"));
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
  
  @EventHandler(ignoreCancelled = true)
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
    if (db.getPlayerData(player).hasTransaction(kitperm)
            || (kit.GetAvailability() == KitAvailability.Free)
            ){
    
      this.Manager.GetGame().SetKit(player, kit, true);
    
    event.setCancelled(true);
    return;
  
     }
    KitClick(player, kit);
  }
  }

    @EventHandler(ignoreCancelled = true)
    public void KitInventoryClick(InventoryClickEvent event){
        if(event.getCurrentItem() == null){
            return;
        }
        if(!event.getCurrentItem().hasItemMeta()){
            return;
        }
        if(!event.getCurrentItem().getItemMeta().hasDisplayName()){
            return;
        }
        if(!event.getCurrentItem().getItemMeta().hasLore()){
            return;
        }
        if(!event.getCurrentItem().getItemMeta().getDisplayName().contains("Kit")){
            return;
        }
        try{
        ItemStack it = event.getCurrentItem();
        String name = it.getItemMeta().getDisplayName();
        name = ChatColor.stripColor(name).replace("Kit","").replace(" ","");
        for(Kit kit : this.Manager.GetGame().GetKits()){
            if(kit==null){
                continue;
            }
            if(kit._kitName.equalsIgnoreCase(name)){
                this.KitClick((Player)event.getWhoClicked(),kit);
                return;
            }
        }}
        catch(Exception e){
            System.out.println("Exception on kit at GamePlayerManager.KitInventoryClick");
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
    KitClick(player, kit);
  }

@EventHandler
public void resetShop(GameStateChangeEvent event){
    this.shop.clear();
}

HashMap<String, Kit> shop = new HashMap();

    public boolean hasKit(Player player,String permkit1, String permkit2){
        if(db.getPlayerData(player).hasTransaction(permkit1)){
            return true;
        }else if(db.getPlayerData(player).hasTransaction(permkit2)){
            return true;
        }else{
            return false;
        }
    }

  public void KitClick(final Player player, final Kit kit) {

      {
          if(this.Manager.GetGame()==null) {
               return;
          }
          String permkit = "kit." + kit.GetName().replace(" ", "").replace(" ", "");
          String permkit2 = "kit." +Manager.GetGame().GetType().GetName() +"."+ kit.GetName().replace(" ", "").replace(" ", "");

          player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 2.0F, 0.5F);
          if (kit.GetAvailability() == KitAvailability.Blue && !db.getPlayerData(player).getRank().isAtLeast(Rank.VIP)) {
              UtilPlayer.message(player, F.main("Kit", "Este kit requiere " + F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("VIP").toString()) + "."));
              UtilPlayer.message(player, F.main("Kit", "Compra el acceso en " + F.elem(new StringBuilder(String.valueOf(C.cYellow)).append(this.Manager.getConfig().getString("GameConfig.shop")).toString())));
          } else if (db.getPlayerData(player).getCoins() < kit.GetCost() && !this.hasKit(player,permkit,permkit2) ) {
              String m1 = Colorizer.Color(this.Manager.getConfig().getString("messages.insuf").replace("%s", "" + kit.GetCost()));
              String m2 = Colorizer.Color(this.Manager.getConfig().getString("messages.insuf2"));
              UtilPlayer.message(player, F.main("Kit", m1));
              UtilPlayer.message(player, F.main("Kit", m2));
          } else if (db.getPlayerData(player).getCoins() >= kit.GetCost() && !this.hasKit(player,permkit,permkit2)) {
              if (this.shop.containsKey(player.getName())) {
                  if (this.shop.get(player.getName()) == kit) {
                      db.getPlayerData(player).removeCoins(kit.GetCost());
db.getPlayerData(player).addTransaction(permkit2,9999);
                      player.sendMessage(F.main("Kit", Colorizer.Color(this.Manager.getConfig().getString("messages.buyed").replace("%kit", kit.GetName()).replace("_", "").replace("%s", "" + kit.GetCost()))));
                      return;
                  } else {
                      this.shop.put(player.getName(), kit);
                  }
              } else {
                  this.shop.put(player.getName(), kit);
                  player.sendMessage(F.main("Kit", Colorizer.Color(this.Manager.getConfig().getString("messages.prebuy").replace("%s", "" + kit.GetCost()))));
              }
          } else if (this.hasKit(player,permkit,permkit2)) {
              this.Manager.GetGame().SetKit(player,kit,true);
              return;
          } else {
              UtilPlayer.message(player, F.main("Kit", "Error"));
          }
      }
  }

}
