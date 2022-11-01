import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

// $FF: renamed from: bz
public class class_115 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerCommandPreprocessEvent playercommandpreprocessevent) {
        if (!playercommandpreprocessevent.isCancelled()) {
            if (class_93.method_494("essentials")) {
                Player player = playercommandpreprocessevent.getPlayer();

                if (player.hasPermission("essentials.break")) {
                    String s = playercommandpreprocessevent.getMessage().toLowerCase();

                    if (s.startsWith("/break ") || s.equalsIgnoreCase("/break")) {
                        class_38.method_160(player, Enums.HackType.NoSwing, 10);
                        class_38.method_160(player, Enums.HackType.GhostHand, 10);
                    }
                }
            }

        }
    }
}
