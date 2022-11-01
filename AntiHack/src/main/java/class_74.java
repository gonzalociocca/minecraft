import java.util.HashMap;
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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: D
public class class_74 implements Listener {

    private static Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;

    public class_74() {
        this.field_149 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_149.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_149.remove(player);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, class_74.field_148, entity) || !class_120.method_572("KillAura.check_combined")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D && !class_67.method_358(player, entity)) {
                    double d1 = class_67.method_357(player, entity);
                    double d2 = class_67.method_356(player, entity);
                    double d3 = class_67.HHΞ(player, entity);
                    long i = class_140.method_676(player, false);

                    if (this.field_149.containsKey(player) && i != 0L) {
                        Location location = class_67.method_360(player, entity);
                        double d4 = location == null ? 0.0D : Math.abs(location.getX());
                        double d5 = location == null ? 0.0D : Math.abs(location.getZ());
                        double d6 = class_38.method_154(player, class_74.field_148) >= 2 ? 6.0D : 7.0D;

                        if (d4 >= d6 || d5 >= d6) {
                            double d7 = class_89.method_482(d1, ((Double) this.field_149.get(player)).doubleValue());

                            if (d7 <= 0.2D && d3 >= 0.99D && d2 <= 12.0D && i <= 350L) {
                                class_92.method_493(player);
                                byte b0 = 0;
                                boolean flag = entity instanceof Player;

                                if (class_137.method_660(player)) {
                                    b0 = 1;
                                } else if (class_71.method_397(player) && (flag && class_71.method_398((Player) entity) || !flag)) {
                                    b0 = 2;
                                } else if (class_137.method_663(player)) {
                                    b0 = 3;
                                } else if (flag && class_71.method_397((Player) entity) && class_71.method_398((Player) entity)) {
                                    b0 = 4;
                                } else if (class_137.method_661(player)) {
                                    b0 = 5;
                                } else if (class_137.HHΞ(player)) {
                                    b0 = 6;
                                } else if (class_137.method_662(player)) {
                                    b0 = 7;
                                } else if (!flag && class_137.method_664(entity)) {
                                    b0 = 8;
                                }

                                if (b0 != 0) {
                                    boolean flag1 = false;

                                    if (b0 == 3) {
                                        flag1 = true;
                                    } else {
                                        class_36.method_135(player, class_74.field_148.toString() + "=combined", 100);
                                        if (class_36.method_133(player, class_74.field_148.toString() + "=combined") >= 2) {
                                            flag1 = true;
                                        }
                                    }

                                    if (flag1) {
                                        class_38.method_159(player, class_74.field_148, "t: combined, diff: " + d7 + ", a: " + d3 + ", r: " + d2 + ", x: " + d4 + ", z: " + d5 + ", t_b_c: " + i + ", c: " + b0);
                                        if (class_123.method_583(player, class_74.field_148)) {
                                            entitydamagebyentityevent.setDamage(0.0D);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    this.field_149.put(player, Double.valueOf(d1));
                }
            }
        }

    }

    static {
        class_74.field_148 = Enums.HackType.KillAura;
    }
}
