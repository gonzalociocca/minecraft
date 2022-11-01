package server.instance.core.combat;

import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import server.common.Code;
import server.instance.GameServer;
import server.instance.core.combat.event.ClearCombatEvent;
import server.instance.core.combat.event.CombatDeathEvent;
import server.instance.core.damage.CustomDamageEvent;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.util.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class GameCombat {
    private final HashMap<Player, CombatLog> _active;
    private final HashMap<String, ClientCombat> _combatClients;
    private final HashSet<Player> _removeList;
    protected long ExpireTime;

    public GameCombat() {
        this._active = new HashMap<>();
        this._combatClients = new HashMap<>();
        this._removeList = new HashSet<>();
        this.ExpireTime = 15000L;
    }

    public void cleanAll() {
        _active.clear();
        _combatClients.clear();
        _removeList.clear();
    }

    public HashMap<Player, CombatLog> getActiveMap() {
        return _active;
    }

    public HashSet<Player> getRemoveSet() {
        return _removeList;
    }

    public ClientCombat get(final String name) {
        if (!this._combatClients.containsKey(name)) {
            this._combatClients.put(name, new ClientCombat());
        }
        return this._combatClients.get(name);
    }

    public void addAttack(final CustomDamageEvent event) {
        if (event.getDamageePlayer() == null) {
            return;
        }
        if (event.getDamagerEntity(true) != null) {
            String reason = event.getReason();
            if (reason == null) {
                if (event.getDamagerPlayer(false) != null) {
                    final Player damager = event.getDamagerPlayer(false);
                    reason = "Fists";
                    if (damager.getItemInHand() != null) {
                        final ItemStack itemStack = CraftItemStack.asNMSCopy(damager.getItemInHand());
                        if (itemStack != null) {
                            reason = CraftItemStack.asNMSCopy(damager.getItemInHand()).getName();
                        }
                    }
                } else if (event.getProjectile() != null) {
                    if (event.getProjectile() instanceof Arrow) {
                        reason = "Archery";
                    } else if (event.getProjectile() instanceof Fireball) {
                        reason = "Fireball";
                    }
                }
            }
            if (event.getDamagerEntity(true) instanceof Player) {
                this.get((Player) event.getDamagerEntity(true)).SetLastCombat(System.currentTimeMillis());
            }
            this.get(event.getDamageePlayer()).Attacked(UtilEnt.getName(event.getDamagerEntity(true)), (int) event.getDamage(), event.getDamagerEntity(true), reason);
        } else {
            final EntityDamageEvent.DamageCause cause = event.getCause();
            String source = "?";
            String reason2 = "-";
            if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                source = "Explosion";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.CONTACT) {
                source = "Cactus";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.CUSTOM) {
                source = "Custom";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
                source = "Water";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                source = "Entity";
                reason2 = "Attack";
            } else if (cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                source = "Explosion";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.FALL) {
                source = "Fall";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.FALLING_BLOCK) {
                source = "Falling Block";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.FIRE) {
                source = "Fire";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
                source = "Fire";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.LAVA) {
                source = "Lava";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
                source = "Lightning";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.MAGIC) {
                source = "Magic";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.MELTING) {
                source = "Melting";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.POISON) {
                source = "Poison";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.PROJECTILE) {
                source = "Projectile";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.STARVATION) {
                source = "Starvation";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
                source = "Suffocation";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.SUICIDE) {
                source = "Suicide";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.VOID) {
                source = "Void";
                reason2 = "-";
            } else if (cause == EntityDamageEvent.DamageCause.WITHER) {
                source = "Wither";
                reason2 = "-";
            }
            if (event.getReason() != null) {
                reason2 = event.getReason();
            }
            get(event.getDamageePlayer()).Attacked(source, (int) event.getDamage(), null, reason2);
        }
    }

    public void add(final Player player) {
        this._active.put(player, new CombatLog(player, 15000L));
    }

    public CombatLog get(final Player player) {
        if (!this._active.containsKey(player)) {
            this.add(player);
        }
        return this._active.get(player);
    }

    public long getExpireTime() {
        return this.ExpireTime;
    }

    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {
            for (CombatLog log : getActiveMap().values()) {
                log.ExpireOld();
            }
        } else if (event.getType() == UpdateType.MIN_02) {
            final Iterator<Player> removeIterator = getRemoveSet().iterator();
            while (removeIterator.hasNext()) {
                final Player player = removeIterator.next();
                if (!player.isOnline()) {
                    getActiveMap().remove(player);
                }
                removeIterator.remove();
            }
            final Iterator<Player> iterator = getActiveMap().keySet().iterator();
            while (iterator.hasNext()) {
                final Player player = iterator.next();
                if (!player.isOnline()) {
                    getRemoveSet().add(player);
                }
            }
        }
    }

    public void onEntityDamage(EntityDamageEvent event) {

        if (!event.isCancelled()) {
            if (event.getEntity() != null && (event.getEntity() instanceof Player)) {
                final Player damagee = (Player) event.getEntity();

                final LivingEntity damagerEnt = UtilEvent.GetDamagerEntity(event, true);
                if (damagerEnt != null) {
                    if (damagerEnt instanceof Player) {
                        get((Player) damagerEnt).SetLastCombat(System.currentTimeMillis());
                    }
                    get(damagee).Attacked(UtilEnt.getName(damagerEnt), event.getDamage(), damagerEnt, new StringBuilder().append(event.getCause()).toString());
                } else {
                    final EntityDamageEvent.DamageCause cause = event.getCause();
                    String source = "?";
                    String reason = "-";
                    if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                        source = "Explosion";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.CONTACT) {
                        source = "Cactus";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.CUSTOM) {
                        source = "Custom";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
                        source = "Water";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        source = "Entity";
                        reason = "Attack";
                    } else if (cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                        source = "Explosion";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.FALL) {
                        source = "Fall";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.FALLING_BLOCK) {
                        source = "Falling Block";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.FIRE) {
                        source = "Fire";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
                        source = "Fire";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.LAVA) {
                        source = "Lava";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
                        source = "Lightning";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.MAGIC) {
                        source = "Magic";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.MELTING) {
                        source = "Melting";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.POISON) {
                        source = "Poison";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.PROJECTILE) {
                        source = "Projectile";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.STARVATION) {
                        source = "Starvation";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
                        source = "Suffocation";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.SUICIDE) {
                        source = "Suicide";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.VOID) {
                        source = "Void";
                        reason = "-";
                    } else if (cause == EntityDamageEvent.DamageCause.WITHER) {
                        source = "Wither";
                        reason = "-";
                    }
                    get(damagee).Attacked(source, event.getDamage(), null, reason);
                }
            }
        }
    }

    public void onPlayerDeath(GameServer game, PlayerDeathEvent event) {

        event.setDeathMessage(null);
        if (getActiveMap().containsKey(event.getEntity())) {
            final CombatLog log = getActiveMap().remove(event.getEntity());
            log.SetDeathTime(System.currentTimeMillis());
            get(event.getEntity().getName()).GetDeaths().addFirst(log);
            int assists = 0;
            for (int i = 0; i < log.GetAttackers().size(); ++i) {
                if (log.GetAttackers().get(i).IsPlayer()) {
                    if (!UtilTime.elapsed(log.GetAttackers().get(i).GetLastDamage(), getExpireTime())) {
                        if (log.GetKiller() == null) {
                            log.SetKiller(log.GetAttackers().get(i));
                            final ClientCombat killerClient = get(log.GetAttackers().get(i).GetName());
                            if (killerClient != null) {
                                killerClient.GetKills().addFirst(log);
                            }
                        } else {
                            ++assists;
                            final ClientCombat assistClient = get(log.GetAttackers().get(i).GetName());
                            if (assistClient != null) {
                                assistClient.GetAssists().addFirst(log);
                            }
                        }
                    }
                }
            }
            log.SetAssists(assists);

            final CombatDeathEvent deathEvent = new CombatDeathEvent(game, event, get(event.getEntity().getName()), log);
            Bukkit.getServer().getPluginManager().callEvent(deathEvent);
            if (deathEvent.getBroadcastType() == DeathMessageType.Detailed || deathEvent.getBroadcastType() == DeathMessageType.Absolute) {
                for (final Player cur : game.getPlayers(false)) {
                    final String killedColor = log.GetKilledColor();
                    final String deadPlayer = String.valueOf(killedColor) + event.getEntity().getName();
                    if (log.GetKiller() != null) {
                        final String killerColor = log.GetKillerColor();
                        String killPlayer = String.valueOf(killerColor) + log.GetKiller().GetName();
                        if (log.GetAssists() > 0) {
                            killPlayer = String.valueOf(killPlayer) + " + " + log.GetAssists();
                        }
                        final String weapon = log.GetKiller().GetLastDamageSource();
                        UtilPlayer.message(cur, F.main("Death", String.valueOf(deadPlayer) + UtilMsg.Gray + " killed by " + killPlayer + UtilMsg.Gray + " with " + F.item(weapon) + "."));
                    } else if (log.GetAttackers().isEmpty()) {
                        UtilPlayer.message(cur, F.main("Death", String.valueOf(deadPlayer) + UtilMsg.Gray + " has died."));
                    } else if (log.GetLastDamager() != null && log.GetLastDamager().GetReason() != null && log.GetLastDamager().GetReason().length() > 1) {
                        UtilPlayer.message(cur, String.valueOf(F.main("Death", new StringBuilder(String.valueOf(deadPlayer)).append(UtilMsg.Gray).append(" killed by ").append(F.name(log.GetLastDamager().GetReason())).toString())) + UtilMsg.Gray + ".");
                    } else {
                        UtilPlayer.message(cur, String.valueOf(F.main("Death", new StringBuilder(String.valueOf(deadPlayer)).append(UtilMsg.Gray).append(" killed by ").append(F.name(log.GetAttackers().getFirst().GetName())).toString())) + UtilMsg.Gray + ".");
                    }
                }
                if (deathEvent.getBroadcastType() == DeathMessageType.Absolute) {
                    UtilPlayer.message(event.getEntity(), log.DisplayAbsolute());
                } else {
                    UtilPlayer.message(event.getEntity(), log.Display());
                }
            } else if (deathEvent.getBroadcastType() == DeathMessageType.Simple) {
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
                    UtilPlayer.message(killer, F.main("Death", "You killed " + F.elem(deadPlayer) + " with " + F.item(weapon2) + "."));
                    UtilPlayer.message(event.getEntity(), F.main("Death", String.valueOf(killPlayer2) + UtilMsg.Gray + " killed you with " + F.item(weapon2) + "."));
                } else if (log.GetAttackers().isEmpty()) {
                    UtilPlayer.message(event.getEntity(), F.main("Death", "You have died."));
                } else {
                    UtilPlayer.message(event.getEntity(), String.valueOf(F.main("Death", new StringBuilder("You were killed by ").append(F.name(log.GetAttackers().getFirst().GetName())).toString())) + UtilMsg.Gray + ".");
                }
            }

        }
    }

    public void checkTitleSend(CombatDeathEvent event) {
        LivingEntity death = event.getEvent().getEntity();
        CombatComponent killer = event.getLog().GetKiller();
        if(death != null && death instanceof Player && killer != null && killer.GetName() != null){
            UtilDisplay.sendTitle((Player)death, Code.Color("&a&lKilled by"), Code.Color("&c&l" + killer.GetName()));
        }
    }

    public void checkDeathMessage(GameServer game, CombatDeathEvent event) {
        if (game != null) {
            event.setBroadcastType(game.GetDeathMessageType());

            LivingEntity death = event.getEvent().getEntity();
            CombatComponent killer = event.getLog().GetKiller();

            death.setHealth(20.0D);

            if (killer != null && killer.GetName() != null) {
                Player player = UtilPlayer.searchExact(event.getLog().GetKiller().GetName());
                if (player != null) {
                    event.getLog().SetKillerColor("" + game.getLogin().getColor(player));
                }
            }
            if (death != null && death instanceof Player) {
                Player player = (Player) event.getEvent().getEntity();
                event.getLog().SetKilledColor("" + game.getLogin().getColor(player));
            }
        }
    }

    public void onCombatDeath(GameServer game, CombatDeathEvent event){
        checkDeathMessage(game, event);
        checkTitleSend(event);
    }

    public void onClearCombat(ClearCombatEvent event){
        getActiveMap().remove(event.getPlayer());
    }
}
