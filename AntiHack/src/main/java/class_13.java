import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

// $FF: renamed from: aN
public class class_13 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageEvent entitydamageevent) {
        if (entitydamageevent.getEntity() instanceof Player && !entitydamageevent.isCancelled()) {
            Player player = (Player) entitydamageevent.getEntity();
            DamageCause damagecause = entitydamageevent.getCause();

            if (damagecause == DamageCause.BLOCK_EXPLOSION || damagecause == DamageCause.ENTITY_EXPLOSION) {
                method_54(player, 80);
                class_10.method_42(player, 0);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (class_71.method_410(player) <= 12) {
                class_39.method_171(player, "explosion=protection=falling");
            } else {
                double d0 = playermoveevent.getTo().getY() - playermoveevent.getFrom().getY();

                if (d0 < 0.0D && method_53(player)) {
                    class_39.method_170(player, "explosion=protection=falling", 80);
                }
            }

        }
    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "explosion=protection");
    }

    public static void Ξ(Player player, int i) {
        class_39.method_170(player, "explosion=protection", i);
    }

    public static boolean Π(Player player) {
        return !class_39.method_169(player, "explosion=protection=falling");
    }
}
