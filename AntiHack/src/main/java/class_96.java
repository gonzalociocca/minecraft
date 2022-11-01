import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: O
public class class_96 implements Listener {

    private Enums.HackType Ξ;

    public class_96() {
        this.field_170 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_170, entity) || class_137.method_665(player, 6.0D) > 12 || !class_120.method_572("KillAura.check_pitch_movement")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D) {
                    double d1 = class_67.HHΞ(player, entity);
                    double d2 = class_67.method_356(player, entity);
                    float f = class_138.method_670(player);
                    boolean flag = false;

                    if (d1 >= 0.999D && d2 <= 6.0D && class_137.HHΞ(player)) {
                        class_38.method_159(player, this.field_170, "t: pitch movement(normal), a: " + d1 + ", r: " + d2 + ", p: " + f);
                        flag = true;
                    } else if (d1 >= 0.999D && d2 <= 6.0D && f >= 30.0F) {
                        class_38.method_159(player, this.field_170, "t: pitch movement(instant), a: " + d1 + ", r: " + d2 + ", p: " + f);
                        flag = true;
                    }

                    if (flag) {
                        class_92.method_493(player);
                        if (class_123.method_583(player, this.field_170)) {
                            entitydamagebyentityevent.setCancelled(true);
                        }
                    }
                }
            }
        }

    }
}
