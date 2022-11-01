import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

// $FF: renamed from: aW
public class class_22 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerVelocityEvent playervelocityevent) {
        Player player = playervelocityevent.getPlayer();

        if (playervelocityevent.isCancelled()) {
            byte b0 = 0;

            method_83(player, b0);
            class_10.method_42(player, b0);
            class_18.method_68(player, b0);
            class_13.method_54(player, b0);
            class_8.method_33(player, b0);
            class_15.method_62(player, b0);
        } else if (class_39.method_169(player, "velocity=protection=disallow")) {
            method_83(player, 80);
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageEvent entitydamageevent) {
        if (entitydamageevent.getEntity() instanceof Player && !entitydamageevent.isCancelled()) {
            Player player = (Player) entitydamageevent.getEntity();

            method_84(player, 2);
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (!playermoveevent.isCancelled()) {
            Player player = playermoveevent.getPlayer();

            if (!class_55.method_297(player) || class_9.method_35()) {
                Location location = playermoveevent.getFrom();
                double d0 = class_69.method_391(playermoveevent.getTo(), location);

                if (d0 >= 8.0D) {
                    class_21.method_78(player);
                    class_69.method_386(player, location);
                }
            }
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "velocity=protection");
    }

    public static void Ξ(Player player, int i) {
        class_39.method_170(player, "velocity=protection", i);
    }

    public static void Π(Player player, int i) {
        class_39.method_170(player, "velocity=protection=disallow", i);
    }
}
