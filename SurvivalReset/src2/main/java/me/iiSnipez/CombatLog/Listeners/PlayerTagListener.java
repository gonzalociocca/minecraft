package me.iiSnipez.CombatLog.Listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import java.util.Iterator;
import me.iiSnipez.CombatLog.CombatLog;
import me.iiSnipez.CombatLog.Events.PlayerTagEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerTagListener implements Listener {

    CombatLog plugin;
    Faction faction;

    public PlayerTagListener(CombatLog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTagEvent(PlayerTagEvent event) {
        Player damager = event.getDamager();
        Player damagee = event.getDamagee();

        this.tagDamager(damager, damagee);
        this.tagDamagee(damager, damagee);
    }

    private void tagDamager(Entity damager, Entity damagee) {
        if (damager instanceof Player) {
            Player p = (Player) damager;

            if (!p.hasPermission("combatlog.bypass")) {
                Location l = p.getLocation();

                if (!this.plugin.disableWorldNames.contains(p.getWorld().getName())) {
                    if (this.plugin.usesFactions) {
                        if (this.plugin.useNewFaction) {
                            this.faction = Board.getFactionAt(l);
                            if (this.faction.getTag().equalsIgnoreCase("SafeZone")) {
                                return;
                            }
                        }

                        if (this.plugin.useOldFaction && Board.getFactionAt(l).getTag().equalsIgnoreCase("SafeZone")) {
                            return;
                        }

                        if (this.plugin.useOldOldFaction && Board.getFactionAt(new FLocation(l)).getTag().equalsIgnoreCase("SafeZone")) {
                            return;
                        }
                    }

                    if (!this.plugin.taggedPlayers.containsKey(p.getName())) {
                        this.plugin.taggedPlayers.put(p.getName(), Long.valueOf(this.plugin.getCurrentTime()));
                        if (this.plugin.taggedMessageEnabled && damagee instanceof Player) {
                            p.sendMessage(this.plugin.translateText(this.plugin.taggerMessage.replaceAll("<name>", ((Player) damagee).getName())));
                        }

                        if (this.plugin.usesLibsDisguise && this.plugin.removeDisguiseEnabled) {
                            this.plugin.removeDisguise(p);
                        }

                        if (this.plugin.removeFlyEnabled) {
                            this.plugin.removeFly(p);
                        }

                        if (this.plugin.removeInvisPotion) {
                            this.removePotion(p);
                            this.removePotion((Player) damagee);
                        }
                    } else {
                        this.plugin.taggedPlayers.remove(p.getName());
                        this.plugin.taggedPlayers.put(p.getName(), Long.valueOf(this.plugin.getCurrentTime()));
                        if (this.plugin.removeDisguiseEnabled) {
                            this.plugin.removeDisguise(p);
                        }

                        if (this.plugin.removeFlyEnabled) {
                            this.plugin.removeFly(p);
                        }

                        if (this.plugin.removeInvisPotion) {
                            this.removePotion(p);
                            this.removePotion((Player) damagee);
                        }
                    }
                }
            }
        }

    }

    private void tagDamagee(Entity damager, Entity damagee) {
        if (damagee instanceof Player) {
            Player p = (Player) damagee;

            if (!p.hasPermission("combatlog.bypass")) {
                Location l = p.getLocation();

                if (!this.plugin.disableWorldNames.contains(p.getWorld().getName())) {
                    if (this.plugin.usesFactions) {
                        if (this.plugin.useNewFaction) {
                            this.faction = Board.getFactionAt(l);
                            if (this.faction.getTag().equalsIgnoreCase("SafeZone")) {
                                return;
                            }
                        }

                        if (this.plugin.useOldFaction && Board.getFactionAt(l).getTag().equalsIgnoreCase("SafeZone")) {
                            return;
                        }

                        if (this.plugin.useOldOldFaction && Board.getFactionAt(new FLocation(l)).getTag().equalsIgnoreCase("SafeZone")) {
                            return;
                        }
                    }

                    if (!this.plugin.taggedPlayers.containsKey(p.getName())) {
                        this.plugin.taggedPlayers.put(p.getName(), Long.valueOf(this.plugin.getCurrentTime()));
                        if (this.plugin.taggerMessageEnabled && damager instanceof Player) {
                            p.sendMessage(this.plugin.translateText(this.plugin.taggedMessage.replaceAll("<name>", ((Player) damager).getName())));
                        }

                        if (this.plugin.usesLibsDisguise && this.plugin.removeDisguiseEnabled) {
                            this.plugin.removeDisguise(p);
                        }

                        if (this.plugin.removeFlyEnabled) {
                            this.plugin.removeFly(p);
                        }

                        if (this.plugin.removeInvisPotion) {
                            this.removePotion(p);
                            this.removePotion((Player) damager);
                        }
                    } else {
                        this.plugin.taggedPlayers.remove(p.getName());
                        this.plugin.taggedPlayers.put(p.getName(), Long.valueOf(this.plugin.getCurrentTime()));
                        if (this.plugin.removeDisguiseEnabled) {
                            this.plugin.removeDisguise(p);
                        }

                        if (this.plugin.removeFlyEnabled) {
                            this.plugin.removeFly(p);
                        }

                        if (this.plugin.removeInvisPotion) {
                            this.removePotion(p);
                            this.removePotion((Player) damager);
                        }
                    }
                }
            }
        }

    }

    private void removePotion(Player player) {
        Iterator iterator = player.getActivePotionEffects().iterator();

        while (iterator.hasNext()) {
            PotionEffect potion = (PotionEffect) iterator.next();

            if (potion.getType().equals(PotionEffectType.INVISIBILITY)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                if (this.plugin.removeInvisMessageEnabled) {
                    player.sendMessage(this.plugin.translateText(this.plugin.removeInvisMessage));
                }
            }
        }

    }
}