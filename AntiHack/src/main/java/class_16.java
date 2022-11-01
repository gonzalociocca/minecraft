import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

// $FF: renamed from: aQ
public class class_16 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerGameModeChangeEvent playergamemodechangeevent) {
        if (!playergamemodechangeevent.isCancelled()) {
            Player player = playergamemodechangeevent.getPlayer();

            class_39.method_170(player, "gamemode=protection", 60);
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "gamemode=protection");
    }
}
