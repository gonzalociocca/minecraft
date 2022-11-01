import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: I
public class class_84 implements Listener {

    private Enums.HackType Ξ;

    public class_84() {
        this.field_159 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_159, entity)) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                if (class_45.method_209(player, this.field_159.toString() + "=hit_consistency")) {
                    long i = class_45.method_210(player, this.field_159.toString() + "=hit_consistency");

                    if (i >= 450L && i <= 550L) {
                        long j = class_140.method_677(player);
                        long k = Math.abs(i - j);

                        if (k <= 3L) {
                            class_36.method_135(player, this.field_159.toString() + "=hit_consistency", 100);
                            if (class_36.method_133(player, this.field_159.toString() + "=hit_consistency") >= 3) {
                                class_92.method_493(player);
                                class_38.method_159(player, this.field_159, "t: hit-consistency, ms: " + i + ", cac: " + j + ", diff: " + k);
                                if (class_123.method_583(player, this.field_159)) {
                                    entitydamagebyentityevent.setCancelled(true);
                                }
                            }
                        }
                    }
                }

                class_45.method_211(player, this.field_159.toString() + "=hit_consistency");
            }
        }

    }
}
