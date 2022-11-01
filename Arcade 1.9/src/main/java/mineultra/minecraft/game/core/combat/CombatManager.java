package mineultra.minecraft.game.core.combat;

import java.util.HashSet;
import java.util.Iterator;
import mineultra.core.common.util.C;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.F;
import mineultra.core.common.util.NautHashMap;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilEvent;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.combat.event.ClearCombatEvent;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import net.minecraft.server.v1_9_R1.ItemStack;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class CombatManager extends MiniPlugin
{
    private final NautHashMap<Player, CombatLog> _active;
    private final NautHashMap<String, ClientCombat> _combatClients;
    private final HashSet<Player> _removeList;
    protected long ExpireTime;
    
    public CombatManager(final JavaPlugin plugin) {
        super("Combat", plugin);
        this._active = new NautHashMap<>();
        this._combatClients = new NautHashMap<>();
        this._removeList = new HashSet<>();
        this.ExpireTime = 15000L;
    }
    
    public ClientCombat Get(final String name) {
        if (!this._combatClients.containsKey(name)) {
            this._combatClients.put(name, new ClientCombat());
        }
        return this._combatClients.get(name);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void AddAttack(final EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() == null || !(event.getEntity() instanceof Player)) {
            return;
        }
        final Player damagee = (Player)event.getEntity();
        final LivingEntity damagerEnt = UtilEvent.GetDamagerEntity(event, true);
        if (damagerEnt != null) {
            if (damagerEnt instanceof Player) {
                this.Get((Player)damagerEnt).SetLastCombat(System.currentTimeMillis());
            }
            this.Get(damagee).Attacked(UtilEnt.getName((Entity)damagerEnt), event.getDamage(), damagerEnt, new StringBuilder().append(event.getCause()).toString());
        }
        else {
            final EntityDamageEvent.DamageCause cause = event.getCause();
            String source = "?";
            String reason = "-";
            if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                source = "Explosion";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.CONTACT) {
                source = "Cactus";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.CUSTOM) {
                source = "Custom";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
                source = "Water";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                source = "Entity";
                reason = "Attack";
            }
            else if (cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                source = "Explosion";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.FALL) {
                source = "Fall";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.FALLING_BLOCK) {
                source = "Falling Block";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.FIRE) {
                source = "Fire";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
                source = "Fire";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.LAVA) {
                source = "Lava";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
                source = "Lightning";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.MAGIC) {
                source = "Magic";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.MELTING) {
                source = "Melting";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.POISON) {
                source = "Poison";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.PROJECTILE) {
                source = "Projectile";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.STARVATION) {
                source = "Starvation";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
                source = "Suffocation";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.SUICIDE) {
                source = "Suicide";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.VOID) {
                source = "Void";
                reason = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.WITHER) {
                source = "Wither";
                reason = "-";
            }
            this.Get(damagee).Attacked(source, event.getDamage(), null, reason);
        }
    }
    
    public void AddAttack(final CustomDamageEvent event) {
        if (event.GetDamageePlayer() == null) {
            return;
        }
        if (event.GetDamagerEntity(true) != null) {
            String reason = event.GetReason();
            if (reason == null) {
                if (event.GetDamagerPlayer(false) != null) {
                    final Player damager = event.GetDamagerPlayer(false);
                    reason = "Fists";
                    if (damager.getItemInHand() != null) {
                        final ItemStack itemStack = CraftItemStack.asNMSCopy(damager.getItemInHand());
                        if (itemStack != null) {
                            reason = CraftItemStack.asNMSCopy(damager.getItemInHand()).getName();
                        }
                    }
                }
                else if (event.GetProjectile() != null) {
                    if (event.GetProjectile() instanceof Arrow) {
                        reason = "Archery";
                    }
                    else if (event.GetProjectile() instanceof Fireball) {
                        reason = "Fireball";
                    }
                }
            }
            if (event.GetDamagerEntity(true) instanceof Player) {
                this.Get((Player)event.GetDamagerEntity(true)).SetLastCombat(System.currentTimeMillis());
            }
            this.Get(event.GetDamageePlayer()).Attacked(UtilEnt.getName((Entity)event.GetDamagerEntity(true)), (int)event.GetDamage(), event.GetDamagerEntity(true), reason);
        }
        else {
            final EntityDamageEvent.DamageCause cause = event.GetCause();
            String source = "?";
            String reason2 = "-";
            if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                source = "Explosion";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.CONTACT) {
                source = "Cactus";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.CUSTOM) {
                source = "Custom";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
                source = "Water";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                source = "Entity";
                reason2 = "Attack";
            }
            else if (cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                source = "Explosion";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.FALL) {
                source = "Fall";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.FALLING_BLOCK) {
                source = "Falling Block";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.FIRE) {
                source = "Fire";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
                source = "Fire";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.LAVA) {
                source = "Lava";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
                source = "Lightning";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.MAGIC) {
                source = "Magic";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.MELTING) {
                source = "Melting";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.POISON) {
                source = "Poison";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.PROJECTILE) {
                source = "Projectile";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.STARVATION) {
                source = "Starvation";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
                source = "Suffocation";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.SUICIDE) {
                source = "Suicide";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.VOID) {
                source = "Void";
                reason2 = "-";
            }
            else if (cause == EntityDamageEvent.DamageCause.WITHER) {
                source = "Wither";
                reason2 = "-";
            }
            if (event.GetReason() != null) {
                reason2 = event.GetReason();
            }
            this.Get(event.GetDamageePlayer()).Attacked(source, (int)event.GetDamage(), null, reason2);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void HandleDeath(final PlayerDeathEvent event) {
        event.setDeathMessage((String)null);
        if (!this._active.containsKey(event.getEntity())) {
            return;
        }
        final CombatLog log = this._active.remove(event.getEntity());
        log.SetDeathTime(System.currentTimeMillis());
        this.Get(event.getEntity().getName()).GetDeaths().addFirst(log);
        int assists = 0;
        for (int i = 0; i < log.GetAttackers().size(); ++i) {
            if (log.GetAttackers().get(i).IsPlayer()) {
                if (!UtilTime.elapsed(log.GetAttackers().get(i).GetLastDamage(), this.ExpireTime)) {
                    if (log.GetKiller() == null) {
                        log.SetKiller(log.GetAttackers().get(i));
                        final ClientCombat killerClient = this.Get(log.GetAttackers().get(i).GetName());
                        if (killerClient != null) {
                            killerClient.GetKills().addFirst(log);
                        }
                    }
                    else {
                        ++assists;
                        final ClientCombat assistClient = this.Get(log.GetAttackers().get(i).GetName());
                        if (assistClient != null) {
                            assistClient.GetAssists().addFirst(log);
                        }
                    }
                }
            }
        }
        log.SetAssists(assists);
        final CombatDeathEvent deathEvent = new CombatDeathEvent((EntityDeathEvent)event, this.Get(event.getEntity().getName()), log);
        UtilServer.getServer().getPluginManager().callEvent((Event)deathEvent);
        if (deathEvent.GetBroadcastType() == DeathMessageType.Detailed || deathEvent.GetBroadcastType() == DeathMessageType.Absolute) {
            for (final Player cur : event.getEntity().getWorld().getPlayers()) {
                final String killedColor = log.GetKilledColor();
                final String deadPlayer = String.valueOf(killedColor) + event.getEntity().getName();
                if (log.GetKiller() != null) {
                    final String killerColor = log.GetKillerColor();
                    String killPlayer = String.valueOf(killerColor) + log.GetKiller().GetName();
                    if (log.GetAssists() > 0) {
                        killPlayer = String.valueOf(killPlayer) + " + " + log.GetAssists();
                    }
                    final String weapon = log.GetKiller().GetLastDamageSource();
                    UtilPlayer.message((Entity)cur, F.main("Death", String.valueOf(deadPlayer) + C.cGray + " killed by " + killPlayer + C.cGray + " with " + F.item(weapon) + "."));
                }
                else if (log.GetAttackers().isEmpty()) {
                    UtilPlayer.message((Entity)cur, F.main("Death", String.valueOf(deadPlayer) + C.cGray + " has died."));
                }
                else if (log.GetLastDamager() != null && log.GetLastDamager().GetReason() != null && log.GetLastDamager().GetReason().length() > 1) {
                    UtilPlayer.message((Entity)cur, String.valueOf(F.main("Death", new StringBuilder(String.valueOf(deadPlayer)).append(C.cGray).append(" killed by ").append(F.name(log.GetLastDamager().GetReason())).toString())) + C.cGray + ".");
                }
                else {
                    UtilPlayer.message((Entity)cur, String.valueOf(F.main("Death", new StringBuilder(String.valueOf(deadPlayer)).append(C.cGray).append(" killed by ").append(F.name(log.GetAttackers().getFirst().GetName())).toString())) + C.cGray + ".");
                }
            }
            if (deathEvent.GetBroadcastType() == DeathMessageType.Absolute) {
                UtilPlayer.message((Entity)event.getEntity(), log.DisplayAbsolute());
            }
            else {
                UtilPlayer.message((Entity)event.getEntity(), log.Display());
            }
        }
        else if (deathEvent.GetBroadcastType() == DeathMessageType.Simple) {
            if (log.GetKiller() != null) {
                final String killerColor2 = log.GetKillerColor();
                String killPlayer2 = String.valueOf(killerColor2) + log.GetKiller().GetName();
                final String killedColor = log.GetKilledColor();
                final String deadPlayer = String.valueOf(killedColor) + event.getEntity().getName();
                if (log.GetAssists() > 0) {
                    killPlayer2 = String.valueOf(killPlayer2) + " + " + log.GetAssists();
                }
                final String weapon2 = log.GetKiller().GetLastDamageSource();
                final Player killer = UtilPlayer.searchExact(log.GetKiller().GetName());
                UtilPlayer.message((Entity)killer, F.main("Death", "You killed " + F.elem(deadPlayer) + " with " + F.item(weapon2) + "."));
                UtilPlayer.message((Entity)event.getEntity(), F.main("Death", String.valueOf(killPlayer2) + C.cGray + " killed you with " + F.item(weapon2) + "."));
            }
            else if (log.GetAttackers().isEmpty()) {
                UtilPlayer.message((Entity)event.getEntity(), F.main("Death", "You have died."));
            }
            else {
                UtilPlayer.message((Entity)event.getEntity(), String.valueOf(F.main("Death", new StringBuilder("You were killed by ").append(F.name(log.GetAttackers().getFirst().GetName())).toString())) + C.cGray + ".");
            }
        }
    }
    
    @EventHandler
    public void ExpireOld(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        for (final CombatLog log : this._active.values()) {
            log.ExpireOld();
        }
    }
    
    public void Add(final Player player) {
        this._active.put(player, new CombatLog(player, 15000L));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void Clear(final ClearCombatEvent event) {
        this._active.remove(event.GetPlayer());
    }
    
    public CombatLog Get(final Player player) {
        if (!this._active.containsKey(player)) {
            this.Add(player);
        }
        return this._active.get(player);
    }
    
    public long GetExpireTime() {
        return this.ExpireTime;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void ClearInactives(final UpdateEvent event) {
        if (event.getType() == UpdateType.MIN_02) {
            final Iterator<Player> removeIterator = this._removeList.iterator();
            while (removeIterator.hasNext()) {
                final Player player = removeIterator.next();
                if (!player.isOnline()) {
                    this._active.remove(player);
                }
                removeIterator.remove();
            }
            final Iterator<Player> iterator = this._active.keySet().iterator();
            while (iterator.hasNext()) {
                final Player player = iterator.next();
                if (!player.isOnline()) {
                    this._removeList.add(player);
                }
            }
        }
    }
    
    public void DebugInfo(final Player player) {
        final StringBuilder nameBuilder = new StringBuilder();
        for (final Player combats : this._active.keySet()) {
            if (!combats.isOnline()) {
                if (nameBuilder.length() != 0) {
                    nameBuilder.append(", ");
                }
                nameBuilder.append(combats.getName());
            }
        }
        player.sendMessage(F.main(this.GetName(), nameBuilder.toString()));
    }
}
