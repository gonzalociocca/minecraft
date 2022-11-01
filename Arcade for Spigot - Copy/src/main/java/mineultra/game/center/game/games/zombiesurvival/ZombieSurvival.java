package mineultra.game.center.game.games.zombiesurvival;



import java.util.HashMap;
import java.util.Iterator;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.GameType;
import mineultra.game.center.centerManager;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.game.SoloGame;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.NullKit;
import net.minecraft.server.v1_8_R2.EntityCreature;
import net.minecraft.server.v1_8_R2.Navigation;
import net.minecraft.server.v1_8_R2.NavigationAbstract;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ZombieSurvival
  extends SoloGame
{
  private GameTeam _survivors;
  private GameTeam _undead;
  private final HashMap<Creature, ZombieData> _mobs = new HashMap();
  
  public ZombieSurvival(centerManager manager)
  {
    super(manager, GameType.ZombieSurvival, new Kit[] {new KitSurvivorKnight(manager), new KitSurvivorRogue(manager), new KitSurvivorArcher(manager), new NullKit(manager), new KitUndeadAlpha(manager), new KitUndeadZombie(manager) }, new String[] {"Los Undeads estan atacando!", "Corre, pelea, o ocultate para sobrevivir!", "Si mueres, te vuelves un Undead", "El ultimo sobreviviente Gana!" });
    

    this.DeathOut = false;
    this.HungerSet = 20;
    
    this.CompassEnabled = true;
  }
  
  @Override
  public void RestrictKits()
  {
    for (Kit kit : GetKits()) {
      for (GameTeam team : GetTeamList()) {
        if (team.GetColor() == ChatColor.RED)
        {
          if (kit.GetName().contains("Survivor")) {
            team.GetRestrictedKits().add(kit);
          }
        }
        else if (kit.GetName().contains("Undead")) {
          team.GetRestrictedKits().add(kit);
        }
      }
    }
  }
  
  @EventHandler
  @Override
  public void CustomTeamGeneration(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Recruit) {
      return;
    }
    this._survivors = ((GameTeam)GetTeamList().get(0));
    this._survivors.SetName("Survivors");
    

    this._undead = new GameTeam(this,"Undead", ChatColor.RED, this.WorldData.GetDataLocs("RED"));
    GetTeamList().add(this._undead);
    
    RestrictKits();
  }
  
  @Override
  public GameTeam ChooseTeam(Player player)
  {
    return this._survivors;
  }
  
  @EventHandler
  public void UpdateChasers(UpdateEvent event)
  {
    if (!IsLive()) {
      return;
    }
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    int req = 1 + this._survivors.GetPlayers(true).size() / 20;
    while ((this._undead.GetPlayers(true).size() < req) && (this._survivors.GetPlayers(true).size() > 0))
    {
      Player player = (Player)this._survivors.GetPlayers(true).get(UtilMath.r(this._survivors.GetPlayers(true).size()));
      SetChaser(player, true);
    }
  }
  
  @EventHandler
  public void PlayerDeath(PlayerDeathEvent event)
  {
    if (this._survivors.HasPlayer(event.getEntity())) {
      SetChaser(event.getEntity(), false);
    }
  }
  
  public void SetChaser(Player player, boolean forced)
  {
    if (!GetPlaces().contains(player)) {
      GetPlaces().add(0, player);
    }
    SetPlayerTeam(player, this._undead);
    

    Kit newKit = GetKits()[4];
    if (forced) {
      newKit = GetKits()[5];
    }
    SetKit(player, newKit, true);
    newKit.ApplyKit(player);
    for (Player other : UtilServer.getPlayers())
    {
      other.hidePlayer(player);
      other.showPlayer(player);
    }
    if (forced)
    {
      player.eject();
      player.teleport(this._undead.GetSpawn());
      
      AddGems(player, 10.0D, "Forced Undead", false);
      
      Announce(F.main("Game", F.elem(new StringBuilder().append(this._survivors.GetColor()).append(player.getName()).toString()) + " has become an " + 
        F.elem(new StringBuilder().append(this._undead.GetColor()).append("Alpha Zombie").toString()) + "."));
      
      player.getWorld().strikeLightningEffect(player.getLocation());
    }
    UtilPlayer.message(player, C.cRed + C.Bold + "Has sido Zombificado! Braaaaaiiiinnnssss!");
  }
  
  @Override
  public void RespawnPlayer(final Player player)
  {
    this.Manager.Clear(player);
    if (this._undead.HasPlayer(player))
    {
      player.eject();
      player.teleport(this._undead.GetSpawn());
    }
    this.Manager.GetPlugin().getServer().getScheduler().scheduleSyncDelayedTask(this.Manager.GetPlugin(), new Runnable()
    {
      @Override
      public void run()
      {
        ZombieSurvival.this.GetKit(player).ApplyKit(player);
        for (Player other : UtilServer.getPlayers())
        {
          other.hidePlayer(player);
          other.showPlayer(player);
        }
      }
    }, 0L);
  }
  
  @EventHandler
  public void UndeadUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    if (!InProgress()) {
      return;
    }
    Iterator<Creature> mobIterator = this._mobs.keySet().iterator();
    while (mobIterator.hasNext())
    {
      Creature mob = (Creature)mobIterator.next();
      if (!mob.isValid())
      {
        mob.remove();
        mobIterator.remove();
      }
    }
    if (this._mobs.size() < 50)
    {
      this.CreatureAllowOverride = true;
      Zombie zombie = (Zombie)this._undead.GetSpawn().getWorld().spawn(this._undead.GetSpawn(), Zombie.class);
      this._mobs.put(zombie, new ZombieData(GetTargetLocation()));
      this.CreatureAllowOverride = false;
    }
    mobIterator = this._mobs.keySet().iterator();
    while (mobIterator.hasNext())
    {
      Creature mob = (Creature)mobIterator.next();
      this.Manager.GetBuffer().Factory().Speed("Zombie Speed", mob, mob, 1.9D, 1, false, false, true);
      
      ZombieData data = (ZombieData)this._mobs.get(mob);
      if ((UtilMath.offset(mob.getLocation(), data.Target) < 10.0D) || 
        (UtilMath.offset2d(mob.getLocation(), data.Target) < 6.0D) || 
        (UtilTime.elapsed(data.Time, 30000L)))
      {
        data.SetTarget(GetTargetLocation());
      }
      else if (mob.getTarget() != null)
      {
        if (UtilMath.offset2d(mob, mob.getTarget()) > 10.0D) {
          mob.setTarget(null);
        } else if (((mob.getTarget() instanceof Player)) && 
          (this._undead.HasPlayer((Player)mob.getTarget()))) {
          mob.setTarget(null);
        }
      }
      else
      {
        EntityCreature ec = ((CraftCreature)mob).getHandle();
        NavigationAbstract nav = ec.getNavigation();
        if (UtilMath.offset(mob.getLocation(), data.Target) > 20.0D)
        {
          Location target = mob.getLocation();
          
          target.add(UtilAlg.getTrajectory(mob.getLocation(), data.Target).multiply(20));
          
          nav.a(target.getX(), target.getY(), target.getZ(), 1.200000047683716D);
        }
        else
        {
          nav.a(data.Target.getX(), data.Target.getY(), data.Target.getZ(), 1.200000047683716D);
        }
      }
    }
  }
  
  public Location GetTargetLocation()
  {
    if (this._survivors.GetPlayers(true).isEmpty()) {
      return this._survivors.GetSpawn();
    }
    return ((Player)this._survivors.GetPlayers(true).get(UtilMath.r(this._survivors.GetPlayers(true).size()))).getLocation();
  }
  
  @EventHandler
  public void UndeadTarget(EntityTargetEvent event)
  {
    if (((event.getTarget() instanceof Player)) && 
      (this._undead.HasPlayer((Player)event.getTarget()))) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void UndeadCombust(EntityCombustEvent event)
  {
    event.setCancelled(true);
  }
  
  @Override
  public void EndCheck()
  {
    if (!IsLive()) {
      return;
    }
    if (this._survivors.GetPlayers(true).size() <= 1)
    {
      if (this._survivors.GetPlayers(true).size() == 1) {
        GetPlaces().add(0, (Player)GetPlayers(true).get(0));
      }
      if (GetPlaces().size() >= 1) {
        AddGems((Player)GetPlaces().get(0), 15.0D, "1er Lugar", false);
      }
      if (GetPlaces().size() >= 2) {
        AddGems((Player)GetPlaces().get(1), 10.0D, "2do Lugar", false);
      }
      if (GetPlaces().size() >= 3) {
        AddGems((Player)GetPlaces().get(2), 5.0D, "3er Lugar", false);
      }
      for (Player player : GetPlayers(false)) {
        if (player.isOnline()) {
          AddGems(player, 10.0D, "Participacion", false);
        }
      }
      SetState(Game.GameState.End);
      AnnounceEnd(GetPlaces());
    }
  }
  
  @EventHandler
  @Override
  public void ScoreboardUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    if ((this._survivors == null) || (this._undead == null)) {
      return;
    }
    GetObjectiveSide().getScore(Bukkit.getOfflinePlayer(this._survivors.GetColor() + this._survivors.GetName())).setScore(this._survivors.GetPlayers(true).size());
    GetObjectiveSide().getScore(Bukkit.getOfflinePlayer(this._undead.GetColor() + this._undead.GetName())).setScore(this._undead.GetPlayers(true).size());
  }
  
  @Override
  public boolean CanJoinTeam(GameTeam team)
  {
    if (team.GetColor() == ChatColor.RED) {
      return team.GetSize() < 1 + UtilServer.getPlayers().length / 25;
    }
    return true;
  }
  
}
