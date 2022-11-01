import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: aZ
public class class_25 implements Listener {

    @EventHandler
    private void Ξ(PlayerLoginEvent playerloginevent) {
        Player player = playerloginevent.getPlayer();

        class_39.method_170(player, "invisible-block", 40);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getTo().getWorld() == playerteleportevent.getFrom().getWorld() && playerteleportevent.getCause() == TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (class_39.method_169(player, "invisible-block")) {
                double d0 = playerteleportevent.getTo().distance(playerteleportevent.getFrom());

                if (d0 <= 0.5D && !class_49.method_246(player) && class_71.method_416(player.getLocation())) {
                    Block block = player.getLocation().getBlock();
                    Block block1 = player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock();
                    Block block2 = player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock();

                    if (block.getType() != Material.AIR) {
                        player.sendBlockChange(block.getLocation(), block.getType().getId(), block.getData());
                    }

                    if (block1.getType() != Material.AIR) {
                        player.sendBlockChange(block1.getLocation(), block1.getType().getId(), block1.getData());
                    }

                    if (block2.getType() != Material.AIR) {
                        player.sendBlockChange(block2.getLocation(), block2.getType().getId(), block2.getData());
                    }
                }

            }
        }
    }
}
