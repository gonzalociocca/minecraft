import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerVelocityEvent;

// $FF: renamed from: bB
public class class_28 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageEvent entitydamageevent) {
        if (!entitydamageevent.isCancelled() && entitydamageevent.getEntity() instanceof Player && entitydamageevent.getCause() == DamageCause.ENTITY_ATTACK) {
            Player player = (Player) entitydamageevent.getEntity();

            class_39.method_170(player, "no-hit-delay", 2);
        }

    }

    @EventHandler
    private void Ξ(PlayerVelocityEvent playervelocityevent) {
        Player player = playervelocityevent.getPlayer();

        if (!class_39.method_169(player, "no-hit-delay") && player.getNoDamageTicks() < 20) {
            class_39.method_170(player, "no-hit-delay=protection", 40);
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "no-hit-delay=protection");
    }
}
