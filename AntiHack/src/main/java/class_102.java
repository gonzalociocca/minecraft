import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: R
public class class_102 implements Listener {

    private Enums.HackType Ξ;

    public class_102() {
        this.field_179 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_179, entity) || !class_120.method_572("KillAura.check_yaw_movement") || class_71.method_414(player, 6.0D) > 5) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.0D) {
                    double d1 = class_67.HHΞ(player, entity);
                    double d2 = class_67.method_356(player, entity);
                    float f = class_141.method_681(player);
                    boolean flag = false;

                    if (d1 >= 0.999D && d2 <= 3.0D && f >= 75.0F) {
                        class_38.method_159(player, this.field_179, "t: yaw movement(normal), a: " + d1 + ", r: " + d2 + ", y: " + f);
                        flag = true;
                    } else if (class_137.method_661(player) && f >= 75.0F && class_137.method_665(player, 6.0D) <= 3) {
                        class_36.method_135(player, this.field_179.toString() + "=yaw_movement=constant", 25);
                        if (class_36.method_133(player, this.field_179.toString() + "=yaw_movement=constant") >= 2) {
                            class_38.method_159(player, this.field_179, "t: yaw movement(constant), a: " + d1 + ", r: " + d2 + ", y: " + f);
                            flag = true;
                        }
                    }

                    if (flag) {
                        class_92.method_493(player);
                        if (class_123.method_583(player, this.field_179)) {
                            entitydamagebyentityevent.setCancelled(true);
                        }
                    }
                }
            }
        }

    }
}
