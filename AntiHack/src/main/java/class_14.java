import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

// $FF: renamed from: aO
public class class_14 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerFishEvent playerfishevent) {
        if (playerfishevent.getCaught() instanceof Player && playerfishevent.getState() == State.CAUGHT_ENTITY) {
            Player player = playerfishevent.getPlayer();
            Player player1 = (Player) playerfishevent.getCaught();

            if (!player1.equals(player)) {
                class_10.method_42(player1, 20);
                class_39.method_170(player1, "hook=protection=has", 40);
                class_39.method_170(player1, "hook=protection=had", 80);
            }
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "hook=protection=has");
    }

    public static boolean Π(Player player) {
        return !class_39.method_169(player, "hook=protection=had");
    }
}
