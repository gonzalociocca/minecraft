import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

// $FF: renamed from: aY
public class class_24 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        if (!playerinteractevent.isCancelled() && playerinteractevent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = playerinteractevent.getClickedBlock();

            if (block != null) {
                Player player = playerinteractevent.getPlayer();

                if (class_66.OOOΠ(block.getLocation())) {
                    class_36.method_135(player, "InteractSpammer", 20);
                    if (class_36.method_133(player, "InteractSpammer") >= 15) {
                        playerinteractevent.setCancelled(true);
                    }
                }
            }
        }

    }
}
