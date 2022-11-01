import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: E
public class class_76 implements Listener {

    private Enums.HackType Ξ;
    private double Ξ;
    private double Π;
    private double HHΞ;
    private static HashMap<Player, HashMap<UUID, Double>> Ξ;

    public class_76() {
        this.field_150 = Enums.HackType.KillAura;
        this.field_151 = 0.1D;
        this.field_152 = 0.3D;
        this.HHΞ = 0.45D;
    }

    public static void Ξ(Player player) {
        if (class_55.method_297(player)) {
            Iterator iterator = player.getNearbyEntities(6.0D, 6.0D, 6.0D).iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (class_67.method_353(entity)) {
                    double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                    if (d0 >= 1.5D) {
                        method_432(player, entity);
                    } else {
                        method_433(player, entity);
                    }
                }
            }
        } else {
            class_76.field_153.remove(player);
        }

    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_76.field_153.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerMoveEvent playermoveevent) {
        Player player = playermoveevent.getPlayer();

        method_425(player);
    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        method_425(player);
    }

    @EventHandler
    private void Ξ(PlayerAnimationEvent playeranimationevent) {
        if (playeranimationevent.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            Player player = playeranimationevent.getPlayer();

            method_425(player);
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_150, entity) || !class_120.method_572("KillAura.check_comparison")) {
                if (class_76.field_153.containsKey(player)) {
                    ((HashMap) class_76.field_153.get(player)).remove(entity.getUniqueId());
                }

                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D) {
                    boolean flag = entity instanceof Player;
                    boolean flag1 = class_67.method_358(player, entity);
                    double d1 = (!flag || flag1) && class_71.method_414(player, 6.0D) <= 5 ? (flag ? this.field_152 : this.field_151) : this.HHΞ;
                    double d2 = class_67.method_357(player, entity);
                    double d3 = method_431(player, entity);
                    double d4 = Math.abs(d2 - d3);
                    long i = class_140.method_676(player, false);

                    if (d3 != 0.0D && d4 != 0.0D && i > 0L && i <= 550L) {
                        boolean flag2 = false;
                        Location location = class_67.method_360(player, entity);
                        double d5 = location == null ? 0.0D : Math.abs(location.getX());
                        double d6 = location == null ? 0.0D : Math.abs(location.getZ());
                        int j = class_38.method_154(player, this.field_150);

                        if (d4 >= 2.0D) {
                            class_38.method_159(player, this.field_150, "t: comparison(instant), r: " + d4);
                            flag2 = true;
                        } else {
                            if (d0 <= 3.5D || !class_69.HHΞ(player)) {
                                if (d4 >= 0.01D) {
                                    double d7 = j >= 2 ? 6.0D : 7.0D;

                                    if (d5 >= d7 || d6 >= d7) {
                                        class_36.method_135(player, this.field_150.toString() + "=comparison=sensitive", 100);
                                        if (class_36.method_133(player, this.field_150.toString() + "=comparison=sensitive") >= 3) {
                                            class_38.method_159(player, this.field_150, "t: comparison(sensitive), r: " + d4 + ", l: " + d7);
                                            flag2 = true;
                                        }
                                    }
                                }

                                if (d4 >= 0.1D && (d5 >= 6.0D || d6 >= 6.0D)) {
                                    class_36.method_135(player, this.field_150.toString() + "=comparison=hard", 100);
                                    if (class_36.method_133(player, this.field_150.toString() + "=comparison=hard") >= 2) {
                                        class_38.method_159(player, this.field_150, "t: comparison(hard), r: " + d4);
                                        flag2 = true;
                                    }
                                }
                            }

                            if (d4 >= d1) {
                                class_36.method_135(player, this.field_150.toString() + "=comparison=normal", 100);
                                if (class_36.method_133(player, this.field_150.toString() + "=comparison=normal") >= 2) {
                                    class_38.method_159(player, this.field_150, "t: comparison(normal), r: " + d4 + ", m: " + d1);
                                    flag2 = true;
                                }
                            }

                            if (d4 > 0.0D && flag1) {
                                class_36.method_135(player, this.field_150.toString() + "=comparison=constant", 100);
                                if (class_36.method_133(player, this.field_150.toString() + "=comparison=constant") >= 8) {
                                    class_38.method_159(player, this.field_150, "t: comparison(constant), r: " + d4 + ", m: " + d1);
                                    flag2 = true;
                                }
                            }
                        }

                        if (flag2) {
                            class_92.method_493(player);
                            if (class_123.method_583(player, this.field_150)) {
                                entitydamagebyentityevent.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }

    }

    private static double Ξ(Player player, Entity entity) {
        return player != null && entity != null && class_67.method_353(entity) ? (class_76.field_153.containsKey(player) && ((HashMap) class_76.field_153.get(player)).containsKey(entity.getUniqueId()) ? ((Double) ((HashMap) class_76.field_153.get(player)).get(entity.getUniqueId())).doubleValue() : 0.0D) : 0.0D;
    }

    private static void Ξ(Player player, Entity entity) {
        if (player != null && entity != null && class_67.method_353(entity)) {
            if (!class_76.field_153.containsKey(player)) {
                class_76.field_153.put(player, new HashMap());
            }

            if (class_76.field_153.containsKey(player)) {
                HashMap hashmap = (HashMap) class_76.field_153.get(player);

                hashmap.put(entity.getUniqueId(), Double.valueOf(class_67.method_357(player, entity)));
                class_76.field_153.put(player, hashmap);
            }

        }
    }

    private static void Π(Player player, Entity entity) {
        if (player != null && entity != null && class_76.field_153.containsKey(player)) {
            ((HashMap) class_76.field_153.get(player)).remove(entity.getUniqueId());
        }
    }

    static {
        class_76.field_153 = new HashMap();
    }
}
