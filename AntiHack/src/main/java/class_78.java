import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: F
public class class_78 implements Listener {

    private Enums.HackType Ξ;

    public class_78() {
        this.field_156 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_156, entity) || class_137.method_665(player, 6.0D) > 12 || !class_120.method_572("KillAura.check_direction")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D) {
                    double d1 = class_67.method_357(player, entity);
                    double d2 = class_67.method_356(player, entity);
                    double d3 = class_67.HHΞ(player, entity);

                    if (d1 >= 2.75D && d2 >= 75.0D && d3 <= 0.5D) {
                        boolean flag = false;

                        if (class_137.method_663(player)) {
                            flag = true;
                        } else {
                            class_36.method_135(player, this.field_156.toString() + "=direction", 140);
                            if (class_36.method_133(player, this.field_156.toString() + "=direction") >= class_41.method_179(3)) {
                                flag = true;
                            }
                        }

                        if (flag) {
                            class_92.method_493(player);
                            class_38.method_159(player, this.field_156, "t: direction, d: " + d1 + ", r: " + d2 + ", a: " + d3);
                            if (class_123.method_583(player, this.field_156)) {
                                entitydamagebyentityevent.setDamage(0.0D);
                            }
                        }
                    }
                }
            }
        }

    }
}
