import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

// $FF: renamed from: aX
public class class_23 implements Listener {

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if ((class_101.method_512() == class_101.field_250 || class_101.method_512() == class_101.field_254) && playerinteractevent.getAction() == Action.RIGHT_CLICK_BLOCK && playerinteractevent.getClickedBlock().getType() == Material.FENCE && player.getItemInHand() != null && player.getItemInHand().getType() != null && !player.getItemInHand().getType().isBlock()) {
            playerinteractevent.setCancelled(true);
        }

    }
}
