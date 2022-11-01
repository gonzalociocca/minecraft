import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: Q
public class class_100 implements Listener {

    private Enums.HackType Ξ;

    public class_100() {
        this.field_177 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (entitydamagebyentityevent.isCancelled() || !class_137.method_666(player, this.field_177, entity) || class_137.method_665(player, 3.8D) > 12 || !class_120.method_572("KillAura.check_rotations")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D) {
                    double d1 = class_67.method_357(player, entity);
                    double d2 = class_67.HHΞ(player, entity);
                    double d3 = class_67.method_356(player, entity);

                    if (class_39.method_169(player, this.field_177.toString() + "=rotations=hit")) {
                        if (d1 >= 1.25D) {
                            class_39.method_170(player, this.field_177.toString() + "=rotations=hit", 14);
                        }
                    } else if (d1 < 0.1D && d2 >= 0.999D && d3 <= 3.0D) {
                        class_92.method_493(player);
                        class_38.method_159(player, this.field_177, "t: rotations, d: " + d1 + ", r: " + d3 + ", a: " + d2);
                        if (class_123.method_583(player, this.field_177)) {
                            entitydamagebyentityevent.setCancelled(true);
                        }
                    }
                }
            }
        }

    }
}
