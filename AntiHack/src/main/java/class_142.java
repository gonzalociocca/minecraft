import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: y
public class class_142 implements Listener {

    private Enums.HackType Ξ;

    public class_142() {
        this.field_240 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && entitydamagebyentityevent.getEntity() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Player player1 = (Player) entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_240, player1) || !class_120.method_572("KillAura.check_accuracy")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                long i = class_140.method_676(player, false);
                long j = class_140.method_676(player1, false);
                double d0 = class_136.method_651(player);

                if (d0 >= 90.0D && i >= 1L && i <= 550L && j >= 1L && j <= 550L && (class_71.method_397(player) || class_71.method_397(player1) && class_71.method_398(player))) {
                    class_92.method_493(player);
                    class_136.method_650(player);
                    class_38.method_159(player, this.field_240, "t: accuracy, v: " + d0);
                    if (class_123.method_583(player, this.field_240)) {
                        entitydamagebyentityevent.setCancelled(true);
                    }
                }
            }
        }

    }
}
