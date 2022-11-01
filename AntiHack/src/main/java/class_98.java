import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: P
public class class_98 implements Listener {

    private Enums.HackType Ξ;

    public class_98() {
        this.field_176 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (class_67.method_354(entity, entitydamagebyentityevent.getDamage()) || !class_137.method_666(player, this.field_176, entity) || class_34.HHΞ(player) || !class_120.method_572("KillAura.check_rapid_hits")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                if (!class_39.method_169(player, this.field_176.toString() + "=rapid_hits=cooldown")) {
                    class_36.method_135(player, this.field_176.toString() + "=rapid_hits", 100);
                    if (class_36.method_133(player, this.field_176.toString() + "=rapid_hits") >= 6) {
                        class_92.method_493(player);
                        class_38.method_159(player, this.field_176, "t: rapid hits");
                        if (class_123.method_583(player, this.field_176)) {
                            entitydamagebyentityevent.setCancelled(true);
                        }
                    }
                } else {
                    class_39.method_170(player, this.field_176.toString() + "=rapid_hits=cooldown", 2);
                }
            }
        }

    }
}
