import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: B
public class class_70 implements Listener {

    private Enums.HackType Ξ;

    public class_70() {
        this.field_141 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_141, entity) || class_137.method_665(player, 6.0D) > 12 || !class_120.method_572("KillAura.check_angle")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D) {
                    double d1 = class_67.HHΞ(player, entity);
                    double d2 = class_67.method_359(player, entity);
                    byte b0 = 0;

                    if (!class_89.method_474(d1, 0.4D / d0)) {
                        b0 = 1;
                    } else if (!class_89.method_474(d1, 0.2D)) {
                        b0 = 2;
                    } else if (d2 > 0.6D) {
                        b0 = 3;
                    }

                    if (b0 != 0) {
                        class_92.method_493(player);
                        boolean flag = false;

                        if (class_137.method_663(player)) {
                            flag = true;
                        } else {
                            class_36.method_135(player, this.field_141.toString() + "=angle", 100);
                            if (class_36.method_133(player, this.field_141.toString() + "=angle") >= class_41.method_179(4)) {
                                flag = true;
                            }
                        }

                        if (flag) {
                            class_38.method_159(player, this.field_141, "t: angle, c: " + b0 + ", a1: " + d1 + ", a2: " + d2);
                            if (class_123.method_583(player, this.field_141)) {
                                entitydamagebyentityevent.setDamage(0.0D);
                            }
                        }
                    }
                }
            }
        }

    }
}
