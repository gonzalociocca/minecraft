import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: K
public class class_88 implements Listener {

    private Enums.HackType Ξ;

    public class_88() {
        this.field_162 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (!class_137.method_666(player, this.field_162, (Entity) null) || !class_120.method_572("KillAura.check_hits_per_second")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                class_36.method_135(player, this.field_162.toString() + "=max_hits_per_second", 20);
                int i = class_36.method_133(player, this.field_162.toString() + "=max_hits_per_second");

                if (i >= 15) {
                    class_92.method_493(player);
                    class_38.method_159(player, this.field_162, "t: max-hits-per-second, h: " + i);
                    if (class_123.method_583(player, this.field_162)) {
                        entitydamagebyentityevent.setCancelled(true);
                    }
                }
            }
        }

    }
}
