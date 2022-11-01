import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: q
public class class_134 implements Listener {

    private static Enums.HackType Ξ;

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (entitydamagebyentityevent.isCancelled() || player.getGameMode() != GameMode.ADVENTURE && player.getGameMode() != GameMode.SURVIVAL) {
                return;
            }

            Entity entity = entitydamagebyentityevent.getEntity();

            if (class_38.method_157(player, class_134.field_223, true) || class_67.method_354(entity, entitydamagebyentityevent.getDamage()) || !class_67.method_353(entity) || class_69.HHΞ(player)) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_476(player.getLocation(), entity.getLocation());
                boolean flag = false;

                if (d0 > 4.5D) {
                    class_38.method_159(player, class_134.field_223, "t: vertical, d: " + d0);
                    flag = true;
                } else {
                    boolean flag1 = class_101.method_512() == class_101.field_254 || class_101.method_512() == class_101.field_250;
                    double d1 = class_89.method_475(player.getLocation(), entity.getLocation());
                    double d2 = (class_38.method_154(player, class_134.field_223) < 3 || flag1) && class_69.method_373(player) > 0.1D ? (flag1 ? 5.0D : 4.0D) : 2.0D;
                    double d3 = class_120.method_573("HitReach.distance_to_check");

                    d3 = class_41.method_178(d3 < 3.6D ? 3.6D : d3);
                    d3 = d3 > 6.0D ? 6.0D : d3;
                    if (d1 >= d3) {
                        class_36.method_135(player, class_134.field_223.toString() + "=horizontal", 100);
                        if ((double) class_36.method_133(player, class_134.field_223.toString() + "=horizontal") >= d2) {
                            class_38.method_159(player, class_134.field_223, "t: horizontal, d: " + d1 + ", l: " + d3);
                            flag = true;
                        }
                    }
                }

                if (flag && class_123.method_583(player, class_134.field_223)) {
                    entitydamagebyentityevent.setCancelled(true);
                }
            }
        }

    }

    static {
        class_134.field_223 = Enums.HackType.HitReach;
    }
}
