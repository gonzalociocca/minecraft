import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

// $FF: renamed from: r
public class class_135 implements Listener {

    private static Enums.HackType Ξ;
    private static HashMap<Player, Location> Ξ;
    private static HashMap<Player, Double> Π;
    private static int Ξ;

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_135.field_225.remove(player);
        class_135.field_226.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        Player player = playerteleportevent.getPlayer();

        class_135.field_225.remove(player);
        class_135.field_226.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerDeathEvent playerdeathevent) {
        Player player = playerdeathevent.getEntity();

        class_38.method_160(player, class_135.field_224, 10);
    }

    @EventHandler
    private void Ξ(PlayerRespawnEvent playerrespawnevent) {
        Player player = playerrespawnevent.getPlayer();

        class_38.method_160(player, class_135.field_224, 10);
    }

    public static void Ξ(Player player) {
        if (class_39.method_168(player, class_135.field_224.toString() + "=scheduler") == 1 && method_648(player) && class_135.field_225.containsKey(player)) {
            Location location = player.getLocation();
            Location location1 = (Location) class_135.field_225.get(player);

            if (location.getWorld() == location1.getWorld()) {
                double d0 = class_89.method_475(location, location1);
                double d1 = location.getY() - location1.getY();
                double d2 = location.distance(location1);
                double d3 = d1 + d0;
                double d4;

                if (d2 >= 1.0D && class_39.method_169(player, class_135.field_224.toString() + "=knockback")) {
                    d4 = d0 + Math.abs(d1);
                    double d5 = Math.abs(d2 - d4);

                    if (d5 >= 0.9D) {
                        class_38.method_159(player, class_135.field_224, "type: calculated, diff: " + d5);
                    }
                }

                if (d2 <= 0.3D && d1 != 0.0D) {
                    class_36.method_135(player, class_135.field_224.toString() + "=distance=sensitive", 77);
                    if (class_36.method_133(player, class_135.field_224.toString() + "=distance=sensitive") >= 3) {
                        class_38.method_159(player, class_135.field_224, "t: distance(sensitive), d: " + d2);
                    }
                }

                if (d3 == d2) {
                    class_36.method_135(player, class_135.field_224.toString() + "=combined", 66);
                    if (class_36.method_133(player, class_135.field_224.toString() + "=combined") >= 3) {
                        class_38.method_159(player, class_135.field_224, "t: combined, d: " + d2 + ", v: " + d1);
                    }
                }

                if (d2 <= 0.15D) {
                    class_36.method_135(player, class_135.field_224.toString() + "=distance=hard", 66);
                    if (class_36.method_133(player, class_135.field_224.toString() + "=distance=hard") >= 3) {
                        class_38.method_159(player, class_135.field_224, "t: distance(hard), d: " + d2 + ", v: " + d1);
                    }
                }

                if (d1 > 0.0D && d1 <= 0.5D) {
                    if (class_135.field_226.containsKey(player)) {
                        d4 = Math.abs(d1 - ((Double) class_135.field_226.get(player)).doubleValue());
                        if (d4 <= 0.01D) {
                            class_36.method_135(player, class_135.field_224.toString() + "=vertical", 77);
                            if (class_36.method_133(player, class_135.field_224.toString() + "=vertical") >= 3) {
                                class_38.method_159(player, class_135.field_224, "t: vertical, v: " + d1 + ", diff: " + d4);
                            }
                        }
                    }

                    class_135.field_226.put(player, Double.valueOf(d1));
                }

                if (d1 > 0.0D && d0 == 0.0D) {
                    class_36.method_135(player, class_135.field_224.toString() + "=vertical", 100);
                    if (class_36.method_133(player, class_135.field_224.toString() + "=vertical") >= 2) {
                        class_38.method_159(player, class_135.field_224, "t: horizontal, ver: " + d1 + ", hor: " + d0);
                    }
                }
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getEntity() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getEntity();
            Entity entity = entitydamagebyentityevent.getDamager();

            if (!method_648(player) || !class_39.method_169(player, class_135.field_224.toString() + "=scheduler")) {
                return;
            }

            DamageCause damagecause = entitydamagebyentityevent.getCause();
            boolean flag = damagecause == DamageCause.PROJECTILE && entity instanceof Arrow;

            if (damagecause == DamageCause.ENTITY_ATTACK || flag) {
                class_135.field_225.put(player, player.getLocation());
                class_39.method_170(player, class_135.field_224.toString() + "=damage", 2);
                class_39.method_170(player, class_135.field_224.toString() + "=scheduler", class_135.field_227);
                if (flag) {
                    class_39.method_170(player, class_135.field_224.toString() + "=knockback", class_135.field_227);
                } else if (entity instanceof Player) {
                    Player player1 = (Player) entitydamagebyentityevent.getDamager();

                    if (player1.getItemInHand().containsEnchantment(Enchantment.KNOCKBACK) || player1.isFlying()) {
                        class_39.method_170(player, class_135.field_224.toString() + "=knockback", class_135.field_227);
                    }
                }
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerVelocityEvent playervelocityevent) {
        Player player = playervelocityevent.getPlayer();

        if (playervelocityevent.isCancelled() && !class_39.method_169(player, class_135.field_224.toString() + "=damage")) {
            class_39.method_171(player, class_135.field_224.toString() + "=scheduler");
        }

    }

    private static boolean Ξ(Player player) {
        if (player == null) {
            return false;
        } else {
            for (int i = 0; i <= 2; ++i) {
                if (!class_66.HHΞ(player, 1.0D, (double) i, 1.0D) || class_66.method_350(player.getLocation().add(0.0D, (double) i, 0.0D), Material.WEB, 1.0D)) {
                    return false;
                }
            }

            return !class_38.method_157(player, class_135.field_224, true) && !class_71.method_400(player) && !class_71.HHHΞ(player) && !class_69.method_374(player, -1.0D) && !class_67.method_364(player) && class_137.method_665(player, 6.0D) <= 3 && !class_71.PPΠ(player) && !class_71.PPPΠ(player);
        }
    }

    static {
        class_135.field_224 = Enums.HackType.Velocity;
        class_135.field_225 = new HashMap();
        class_135.field_226 = new HashMap();
        class_135.field_227 = 5;
    }
}
