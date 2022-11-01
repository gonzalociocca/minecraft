import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: C
public class class_72 implements Listener {

    private Enums.HackType Ξ;

    public class_72() {
        this.field_142 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_142, entity) || class_71.method_414(player, 6.0D) > 12 || !class_120.method_572("KillAura.check_block_raytrace")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                String s = class_66.method_344(player, entity.getLocation());

                if (s != null) {
                    class_92.method_493(player);
                    class_38.method_159(player, this.field_142, "t: block raytrace, " + s);
                    if (class_123.method_583(player, this.field_142)) {
                        entitydamagebyentityevent.setCancelled(true);
                    }
                }
            }
        }

    }
}
