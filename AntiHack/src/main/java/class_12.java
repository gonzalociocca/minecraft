import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: aM
public class class_12 implements Listener {

    private static HashMap<Player, Location> Ξ;

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_12.field_17.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerMoveEvent playermoveevent) {
        Player player = playermoveevent.getPlayer();

        if (class_12.field_17.containsKey(player)) {
            Location location = player.getLocation();
            Location location1 = (Location) class_12.field_17.get(player);

            if (location.getWorld() != location1.getWorld() || location.distance(location1) >= 0.7D) {
                class_12.field_17.remove(player);
            }
        }

    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() == TeleportCause.ENDER_PEARL) {
            Player player = playerteleportevent.getPlayer();

            if (!method_50(player)) {
                class_12.field_17.put(player, player.getLocation());
            }
        }

    }

    public static boolean Ξ(Player player) {
        return player == null ? false : class_12.field_17.containsKey(player);
    }

    private static boolean Π(Player player) {
        if (player != null) {
            for (int i = 0; i <= 1; ++i) {
                if (!class_66.HHΞ(player, 0.299D, (double) i, 0.299D)) {
                    return false;
                }
            }
        }

        return true;
    }

    static {
        class_12.field_17 = new HashMap();
    }
}
