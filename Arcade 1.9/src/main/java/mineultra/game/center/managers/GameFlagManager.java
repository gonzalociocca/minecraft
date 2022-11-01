package mineultra.game.center.managers;

import java.util.Iterator;

import mineultra.core.common.util.C;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.events.PlayerDeathOutEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameTeam;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GameFlagManager
  implements Listener
{
  centerManager Manager = null;
  
  public GameFlagManager(centerManager manager)
  {
    this.Manager = manager;
    this.Manager.GetBuffer().Disable();
    
    this.Manager.GetPluginManager().registerEvents(this, this.Manager.GetPlugin());
  }
  
  @EventHandler(priority=EventPriority.LOW)
  public void DamageEvent(CustomDamageEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null)
    {
      event.SetCancelled("Game Null");
      return;
    }
    LivingEntity damagee = event.GetDamageeEntity();
    LivingEntity damager = event.GetDamagerEntity(true);
    if ((damagee != null) && (damagee.getWorld().getName().equals("world")))
    {
      event.SetCancelled("In Lobby");
      if (event.GetCause() == EntityDamageEvent.DamageCause.VOID) {
        damagee.teleport(this.Manager.GetLobby().GetSpawn());
      }
      return;
    }
    if (!game.Damage)
    {
      event.SetCancelled("Damage Disabled");
      return;
    }
    if (game.GetState() != Game.GameState.Live)
    {
      event.SetCancelled("Game not Live");
      return;
    }
    if ((damagee != null) && ((damagee instanceof Player)) && (!game.IsAlive((Player)damagee)))
    {
      event.SetCancelled("Damagee Not Playing");
      return;
    }
    if ((damager != null) && ((damager instanceof Player)) && (!game.IsAlive((Player)damager)))
    {
      event.SetCancelled("Damager Not Playing");
      return;
    }
    if ((damagee != null) && (damager != null)) {
      if (((damagee instanceof Player)) && ((damager instanceof Player)))
      {
        if (!this.Manager.CanHurt((Player)damagee, (Player)damager)) {
          event.SetCancelled("Damage Rules");
        }
      }
      else if ((damager instanceof Player))
      {
        if (!game.DamagePvE) {
          event.SetCancelled("PvE Disabled");
        }
      }
      else if ((damagee instanceof Player)) {
        if (!game.DamageEvP)
        {
          event.SetCancelled("EvP Disabled");
          return;
        }
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  public void DamageExplosion(CustomDamageEvent event)
  {
    if (event.IsCancelled()) {
      return;
    }
    if ((event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) && (event.GetCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
      return;
    }
    Player damagee = event.GetDamageePlayer();
    if (damagee == null) {
      return;
    }
    Player damager = event.GetDamagerPlayer(true);
    if (damager == null) {
      return;
    }
    if (this.Manager.CanHurt(damagee, damager)) {
      return;
    }
    event.SetCancelled("Allied Explosion");
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void ItemPickupEvent(PlayerPickupItemEvent event)
  {
    Player player = event.getPlayer();
    
    Game game = this.Manager.GetGame();
    if ((game == null) || (!game.IsAlive(player)) || (game.GetState() != Game.GameState.Live))
    {
      event.setCancelled(true);
      return;
    }
    if (game.ItemPickup)
    {
      if (game.ItemPickupDeny.contains(Integer.valueOf(event.getItem().getItemStack().getTypeId()))) {
        event.setCancelled(true);
      }
    }
    else if (!game.ItemPickupAllow.contains(Integer.valueOf(event.getItem().getItemStack().getTypeId()))) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void ItemDropEvent(PlayerDropItemEvent event)
  {
    Player player = event.getPlayer();
    
    Game game = this.Manager.GetGame();
    if ((game == null) || (!game.IsAlive(player)) || (game.GetState() != Game.GameState.Live))
    {
      if ((!player.isOp()) || (player.getGameMode() != GameMode.CREATIVE)) {
        event.setCancelled(true);
      }
      return;
    }
    if (game.ItemDrop)
    {
      if (game.ItemDropDeny.contains(Integer.valueOf(event.getItemDrop().getItemStack().getTypeId()))) {
        event.setCancelled(true);
      }
    }
    else if (!game.ItemDropAllow.contains(Integer.valueOf(event.getItemDrop().getItemStack().getTypeId()))) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void InventoryOpen(PlayerInteractEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (!game.InProgress()) {
      return;
    }
    if (game.InventoryOpen) {
      return;
    }
    if (event.getClickedBlock() == null) {
      return;
    }
    if (!UtilBlock.usable(event.getClickedBlock())) {
      return;
    }
    event.setCancelled(true);
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void BlockPlaceEvent(BlockPlaceEvent event)
  {
    Player player = event.getPlayer();
    
    Game game = this.Manager.GetGame();
    if (game == null)
    {
      if ((!player.isOp()) || (player.getGameMode() != GameMode.CREATIVE)) {
        event.setCancelled(true);
      }
    }
    else if (!game.IsAlive(player))
    {
      if ((!player.isOp()) || (player.getGameMode() != GameMode.CREATIVE)) {
        event.setCancelled(true);
      }
    }
    else if (game.BlockPlace)
    {
      if (game.BlockPlaceDeny.contains(Integer.valueOf(event.getBlock().getTypeId()))) {
        event.setCancelled(true);
      }
    }
    else if (!game.BlockPlaceAllow.contains(Integer.valueOf(event.getBlock().getTypeId()))) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void BlockBreakEvent(BlockBreakEvent event)
  {
    Player player = event.getPlayer();
    
    Game game = this.Manager.GetGame();
    if (game == null)
    {
      if ((!player.isOp()) || (player.getGameMode() != GameMode.CREATIVE)) {
        event.setCancelled(true);
      }
    }
    else if (game.GetState() == Game.GameState.Live)
    {
      if (!game.IsAlive(player)) {
        event.setCancelled(true);
      } else if (game.BlockBreak)
      {
        if (game.BlockBreakDeny.contains(Integer.valueOf(event.getBlock().getTypeId()))) {
          event.setCancelled(true);
        }
      }
      else if (!game.BlockBreakAllow.contains(Integer.valueOf(event.getBlock().getTypeId()))) {
        event.setCancelled(true);
      }
    }
    else {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority=EventPriority.MONITOR)
  public void PrivateBlockPlace(BlockPlaceEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (!game.PrivateBlocks) {
      return;
    }
    if (event.isCancelled()) {
      return;
    }
    if (!UtilBlock.usable(event.getBlockPlaced())) {
      return;
    }
    if ((event.getBlockPlaced().getType() != Material.CHEST) && 
      (event.getBlockPlaced().getType() != Material.FURNACE) && 
      (event.getBlockPlaced().getType() != Material.BURNING_FURNACE) && 
      (event.getBlockPlaced().getType() != Material.WORKBENCH)) {
      return;
    }
    String privateKey = event.getPlayer().getName();
    if (!game.PrivateBlockCount.containsKey(privateKey)) {
      game.PrivateBlockCount.put(privateKey, Integer.valueOf(0));
    }
    if (((Integer)game.PrivateBlockCount.get(privateKey)).intValue() == 4) {
      return;
    }
    if (((Integer)game.PrivateBlockCount.get(privateKey)).intValue() > 1) {
return;
    }
    game.PrivateBlockMap.put(event.getBlockPlaced().getLocation(), event.getPlayer());
    game.PrivateBlockCount.put(event.getPlayer().getName(), Integer.valueOf(((Integer)game.PrivateBlockCount.get(event.getPlayer().getName())).intValue() + 1));
    event.getPlayer().sendMessage(F.main(game.GetName(), "Can't touch this. Na na nana!"));
    if (((Integer)game.PrivateBlockCount.get(privateKey)).intValue() == 4) {
      event.getPlayer().sendMessage(F.main(game.GetName(), "Protected block limit reached.  Stay classy Ultra ranker ;)"));
    } else if (((Integer)game.PrivateBlockCount.get(privateKey)).intValue() == 2) {

        event.getPlayer().sendMessage(F.main(game.GetName(), "Protected block limit reached. Thieves are scary"));
    }
  }
  
  @EventHandler(priority=EventPriority.LOW)
  public void PrivateBlockPlaceCancel(BlockPlaceEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (!game.PrivateBlocks) {
      return;
    }
    if (event.isCancelled()) {
      return;
    }
    Block block = event.getBlockPlaced();
    if (block.getType() != Material.CHEST) {
      return;
    }
    Player player = event.getPlayer();
    
    BlockFace[] faces = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    BlockFace[] arrayOfBlockFace1;
    int j = (arrayOfBlockFace1 = faces).length;
    for (int i = 0; i < j; i++)
    {
      BlockFace face = arrayOfBlockFace1[i];
      
      Block other = block.getRelative(face);
      if (other.getType() == Material.CHEST) {
        if (game.PrivateBlockMap.containsKey(other.getLocation()))
        {
          Player owner = (Player)game.PrivateBlockMap.get(other.getLocation());
          if (!player.equals(owner))
          {
            GameTeam ownerTeam = game.GetTeam(owner);
            GameTeam playerTeam = game.GetTeam(player);
            if ((ownerTeam == null) || (playerTeam == null) || (ownerTeam.equals(playerTeam)))
            {
              UtilPlayer.message(event.getPlayer(), F.main("Game", 
                "You cannot combine " + 
                F.elem(new StringBuilder(String.valueOf(C.cPurple)).append(ItemStackFactory.Instance.GetName(event.getBlock(), false)).toString()) + 
                " with " + F.elem(new StringBuilder().append(this.Manager.GetColor(owner)).append(owner.getName()).append(".").toString())));
              
              event.setCancelled(true);
              return;
            }
          }
        }
      }
    }
  }
  
  @EventHandler(priority=EventPriority.NORMAL)
  public void PrivateBlockBreak(BlockBreakEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (!game.PrivateBlocks) {
      return;
    }
    if (event.isCancelled()) {
      return;
    }
    if (!game.PrivateBlockMap.containsKey(event.getBlock().getLocation())) {
      return;
    }
    Player owner = (Player)game.PrivateBlockMap.get(event.getBlock().getLocation());
    Player player = event.getPlayer();
    if (owner.equals(player))
    {
      game.PrivateBlockMap.remove(event.getBlock().getLocation());
    }
    else
    {
      GameTeam ownerTeam = game.GetTeam(owner);
      GameTeam playerTeam = game.GetTeam(player);
      if ((ownerTeam != null) && (playerTeam != null) && (!ownerTeam.equals(playerTeam))) {
        return;
      }
      UtilPlayer.message(event.getPlayer(), F.main("Game", 
        F.elem(new StringBuilder(String.valueOf(C.cPurple)).append(ItemStackFactory.Instance.GetName(event.getBlock(), false)).toString()) + 
        " belongs to " + F.elem(new StringBuilder().append(this.Manager.GetColor(owner)).append(owner.getName()).append(".").toString())));
      
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.LOW)
  public void PrivateBlockUse(PlayerInteractEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (!game.PrivateBlocks) {
      return;
    }
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (!UtilBlock.usable(event.getClickedBlock())) {
      return;
    }
    if ((event.getClickedBlock().getType() != Material.CHEST) && 
      (event.getClickedBlock().getType() != Material.FURNACE) && 
      (event.getClickedBlock().getType() != Material.BURNING_FURNACE)) {
      return;
    }
    if (!game.PrivateBlockMap.containsKey(event.getClickedBlock().getLocation())) {
      return;
    }
    Player owner = (Player)game.PrivateBlockMap.get(event.getClickedBlock().getLocation());
    Player player = event.getPlayer();
    if (owner.equals(player)) {
      return;
    }
    GameTeam ownerTeam = game.GetTeam(owner);
    GameTeam playerTeam = game.GetTeam(player);
    if ((ownerTeam != null) && (playerTeam != null) && (!ownerTeam.equals(playerTeam))) {
      return;
    }
    UtilPlayer.message(event.getPlayer(), F.main("Game", 
      F.elem(new StringBuilder(String.valueOf(C.cPurple)).append(ItemStackFactory.Instance.GetName(event.getClickedBlock(), false)).toString()) + 
      " belongs to " + F.elem(new StringBuilder().append(this.Manager.GetColor(owner)).append(owner.getName()).append(".").toString())));
    
    event.setCancelled(true);
  }
  
  @EventHandler
  public void onDrop(PlayerDropItemEvent event){
      if(event.getPlayer().getGameMode() == GameMode.CREATIVE){
          event.setCancelled(true);
      }
  }
  @EventHandler
  public void onPlace(BlockPlaceEvent event){
      if(event.getPlayer().getGameMode() == GameMode.CREATIVE){
          event.setCancelled(true);
      }
  }
  @EventHandler
  public void onBreak(BlockBreakEvent event){
      if(event.getPlayer().getGameMode() == GameMode.CREATIVE){
          event.setCancelled(true);
      }
  }
  @EventHandler
  public void onTouch(PlayerInteractEvent event){
      if(event.getPlayer().getGameMode() == GameMode.CREATIVE){
          event.setCancelled(true);
      }
  }
  @EventHandler
  public void onTouch(EntityDamageByEntityEvent event){
      if(event.getDamager() instanceof Player){
      Player p = (Player)event.getDamager();
      if(p.getGameMode() == GameMode.CREATIVE){
          event.setCancelled(true);
      }
      
      }
  }
  @EventHandler
  public void onCustom(CustomDamageEvent event){
      if(event.GetDamagerPlayer(false) != null)
              {
     if(event.GetDamagerPlayer(false).getGameMode() == GameMode.CREATIVE){
         event.SetCancelled("You are a spectator now!");
     }
              }}
  
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void PlayerDeath(PlayerDeathEvent event)
  {
    final Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    final Player player = event.getEntity();
    

    this.Manager.GetBuffer().Factory().Blind("Ghost", player, player, 1.5D, 0, false, false, false);
    
    player.setFireTicks(0);
    player.setFallDistance(0.0F);
    if (game.DeathDropItems) {
      for (ItemStack stack : event.getDrops()) {
        player.getWorld().dropItem(player.getLocation(), stack);
      }
    }
    event.getDrops().clear();
    if ((game.GetState() == Game.GameState.Live) && (game.DeathOut))
    {
      PlayerDeathOutEvent outEvent = new PlayerDeathOutEvent(game, player);
      UtilServer.getServer().getPluginManager().callEvent(outEvent);
      if (!outEvent.isCancelled()) {
        game.SetPlayerState(player, GameTeam.PlayerState.OUT);
      }
    }
    if ((game.DeathSpectateSecs <= 0.0D) && ((game.GetTeam(player) == null) || (game.GetTeam(player).GetRespawnTime() <= 0.0D)))
    {
      if (game.IsAlive(player)) {
        game.RespawnPlayer(player);
      } else {
        game.SetSpectator(player);
      }
      this.Manager.GetPlugin().getServer().getScheduler().scheduleSyncDelayedTask(this.Manager.GetPlugin(), new Runnable()
      {
        @Override
        public void run()
        {
          player.setFireTicks(0);
          player.setVelocity(new Vector(0, 0, 0));
        }
      }, 0L);
    }
    else
    {
      double time = game.DeathSpectateSecs;
      if ((game.GetTeam(player) != null) && 
        (game.GetTeam(player).GetRespawnTime() > time)) {
        time = game.GetTeam(player).GetRespawnTime();
      }
      UtilInv.Clear(player);
      this.Manager.GetBuffer().Factory().Blind("Ghost", player, player, 1.5D, 0, false, false, false);
      this.Manager.GetBuffer().Factory().Cloak("Ghost", player, player, time, false, false);
      player.setGameMode(GameMode.CREATIVE);
      player.setFlying(true);
      player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) (time * 20)+20,1));
      ((CraftPlayer)player).getHandle().setInvisible(true);
      ((CraftPlayer)player).getHandle().setSneaking(false);
      for (int i = 0; i < 9; i++) {
        player.getInventory().setItem(i, new ItemStack(Material.SKULL));
      }
      UtilAction.velocity(player, new Vector(0, 0, 0), 1.0D, true, 0.4D, 0.0D, 1.0D, true);
      
      UtilPlayer.message(player, Colorizer.Color(this.Manager.getConfig().getString("messages.resp").replace("%s", ""+ time)));
      
      this.Manager.GetPlugin().getServer().getScheduler().scheduleSyncDelayedTask(this.Manager.GetPlugin(), new Runnable()
      {
        @Override
        public void run()
        {
          if (game.IsAlive(player)) {
            game.RespawnPlayer(player);
          } else {
            game.SetSpectator(player);
          }
          player.setFireTicks(0);
          player.setVelocity(new Vector(0, 0, 0));
        }
      }, (int)(time * 20.0D));
    }
  }
  
  @EventHandler
  public void PlayerQuit(PlayerQuitEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    game.RemoveTeamPreference(event.getPlayer());
    game.GetPlayerKits().remove(event.getPlayer());
    if (!game.QuitOut) {
      return;
    }
    GameTeam team = game.GetTeam(event.getPlayer());
    if (team != null) {
      if (game.InProgress()) {
        team.SetPlayerState(event.getPlayer(), GameTeam.PlayerState.OUT);
      } else {
        team.RemovePlayer(event.getPlayer());
      }
    }
  }
  /*
  @EventHandler
  public void PlayerMoveCancel(PlayerMoveEvent event)
  {
    Game game = this.Manager.GetGame();
    if ((game == null) || (game.GetState() != Game.GameState.Prepare)) {
      return;
    }
    if (!game.PrepareFreeze) {
      return;
    }
    if (UtilMath.offset2d(event.getFrom(), event.getTo()) <= 0.0D) {
      return;
    }
    event.getFrom().setPitch(event.getTo().getPitch());
    event.getFrom().setYaw(event.getTo().getYaw());
    
    event.setTo(event.getFrom());
  }
  */
  
  @EventHandler
  public void PlayerHealthFoodUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    Game game = this.Manager.GetGame();
    Player[] arrayOfPlayer;
    int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
    for (int i = 0; i < j; i++)
    {
      Player player = arrayOfPlayer[i];
      if ((game == null) || (game.GetState() == Game.GameState.Recruit) || (!game.IsAlive(player)))
      {
         player.setMaxHealth(20.0D);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
      
      }
    }
    if ((game == null) || (!game.IsLive())) {
      return;
    }
    if (game.HungerSet != -1) {
      for (Player player : game.GetPlayers(true)) {
        player.setFoodLevel(game.HungerSet);
      }
    }
    if (game.HealthSet != -1) {
      for (Player player : game.GetPlayers(true)) {
        player.setHealth(game.HealthSet);
      }
    }
  }
  

  @EventHandler(priority=EventPriority.LOWEST)
  public void PlayerBoundaryCheck(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    Game game = this.Manager.GetGame();
    if ((game == null) || (game.GetState() != Game.GameState.Live)) {
      return;
    }
    Player[] arrayOfPlayer;
    int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
    for (int i = 0; i < j; i++)
    {
      Player player = arrayOfPlayer[i];
      if ((player.getLocation().getX() > game.WorldData.MaxX) ||
        (player.getLocation().getX() < game.WorldData.MinX) || 
        (player.getLocation().getZ() > game.WorldData.MaxZ) || 
        (player.getLocation().getZ() < game.WorldData.MinZ) || (
        ((player.getLocation().getY() > game.WorldData.MaxY) || 
        (player.getLocation().getY() < game.WorldData.MinY)) && (game.IsAlive(player)))) {
        if (!this.Manager.IsAlive(player))
        {
          player.teleport(game.GetSpectatorLocation());
        }
        else if (!game.WorldBoundaryKill)
        {
          UtilPlayer.message(player, C.cRed + C.Bold + "WARNING: " + C.cWhite + C.Bold + "RETURN TO PLAYABLE AREA!");

          this.Manager.GetDamage().NewDamageEvent(player, null, null, 
            EntityDamageEvent.DamageCause.VOID, 4.0D, false, false, false, 
            "Void", "Void Damage");

          player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 2.0F, 1.0F);
        }
        else
        {
          this.Manager.GetDamage().NewDamageEvent(player, null, null, 
            EntityDamageEvent.DamageCause.VOID, 9001.0D, false, false, false, 
            "Void", "Void Damage");
        }
      }
    }
  }
  
  @EventHandler(priority=EventPriority.LOW)
  public void WorldCreature(CreatureSpawnEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (!game.CreatureAllow && !game.CreatureAllowOverride) {
      if (game.WorldData != null) {
        if (game.WorldData.World != null) {
          if (event.getLocation().getWorld().equals(game.WorldData.World)) {
            event.setCancelled(true);
          }
        }
      }
    }
  }
  
  @EventHandler
  public void WorldTime(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (game.WorldTimeSet != -1) {
      if (game.WorldData != null) {
        if (game.WorldData.World != null) {
          game.WorldData.World.setTime(game.WorldTimeSet);
        }
      }
    }
  }
  
  @EventHandler
  public void WorldWeather(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (!game.WorldWeatherEnabled) {
      if (game.WorldData != null) {
        if (game.WorldData.World != null)
        {
          game.WorldData.World.setStorm(false);
          game.WorldData.World.setThundering(false);
        }
      }
    }
  }
  
  @EventHandler
  public void WorldWaterDamage(UpdateEvent event)
  {
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (game.WorldWaterDamage <= 0) {
      return;
    }
    if (!game.IsLive()) {
      return;
    }
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    Iterator localIterator2;
    for (Iterator localIterator1 = game.GetTeamList().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
    {
      GameTeam team = (GameTeam)localIterator1.next();
      localIterator2 = team.GetPlayers(true).iterator(); 
      Player player = (Player)localIterator2.next();
      if ((player.getLocation().getBlock().getTypeId() == 8) || (player.getLocation().getBlock().getTypeId() == 9))
      {
        this.Manager.GetDamage().NewDamageEvent(player, null, null, 
          EntityDamageEvent.DamageCause.DROWNING, 4.0D, true, false, false, 
          "Water", "Water Damage");
        
        player.getWorld().playSound(player.getLocation(), 
          Sound.ENTITY_SPLASH_POTION_BREAK, 0.8F, 
          1.0F + (float)Math.random() / 2.0F);
      }
    }
  }
  
  @EventHandler
  public void SpectatorMessage(UpdateEvent event)
  {
    if (this.Manager.GetGame() == null) {
      return;
    }
    if (!this.Manager.GetGame().AnnounceStay) {
      return;
    }
    if (!this.Manager.GetGame().IsLive()) {
      return;
    }
    if (event.getType() != UpdateType.SLOWER) {
      return;
    }
    Player[] arrayOfPlayer;
    int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
    for (int i = 0; i < j; i++)
    {
      Player player = arrayOfPlayer[i];
      if (!this.Manager.IsAlive(player))
      {
        UtilPlayer.message(player, " ");
        UtilPlayer.message(player, Colorizer.Color(this.Manager.getConfig().getString("messages.spec")));
        UtilPlayer.message(player, Colorizer.Color(this.Manager.getConfig().getString("messages.spec2")));
      }
    }
  }
}
