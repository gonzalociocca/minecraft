import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

// $FF: renamed from: aH
public class class_7 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        if (!playerinteractevent.isCancelled()) {
            Player player = playerinteractevent.getPlayer();
            Block block = playerinteractevent.getClickedBlock();

            if (playerinteractevent.getAction() == Action.LEFT_CLICK_BLOCK && block != null) {
                class_45.method_211(player, "block-break=click");
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockBreakEvent blockbreakevent) {
        if (!blockbreakevent.isCancelled()) {
            Player player = blockbreakevent.getPlayer();
            boolean flag = false;
            long i;

            if (class_45.method_209(player, "block-break=click")) {
                i = class_45.method_210(player, "block-break=click");
                if (i <= 200L) {
                    flag = true;
                }
            }

            if (class_45.method_209(player, "block-break=repeat")) {
                i = class_45.method_210(player, "block-break=repeat");
                if (i <= 200L) {
                    flag = true;
                }
            }

            class_45.method_211(player, "block-break=repeat");
            if (flag) {
                class_39.method_170(player, "block-break=protection", 40);
            }
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "block-break=protection");
    }
}
