import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

// $FF: renamed from: aL
public class class_11 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        if (playerinteractevent.isCancelled()) {
            Player player = playerinteractevent.getPlayer();
            Block block = playerinteractevent.getClickedBlock();

            if (block == null) {
                return;
            }

            double d0 = class_69.method_391(player.getLocation(), block.getLocation());

            if (playerinteractevent.getAction() == Action.RIGHT_CLICK_BLOCK && player.getLocation().getY() >= block.getLocation().getY() + 0.5D && d0 >= 0.5D && d0 <= 3.0D) {
                boolean flag = false;

                if (player.getItemInHand().getItemMeta() != null && player.getItemInHand().getType().isBlock()) {
                    flag = true;
                } else if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251 && player.getInventory().getItemInOffHand().getItemMeta() != null && player.getInventory().getItemInOffHand().getType().isBlock()) {
                    flag = true;
                }

                if (flag) {
                    Location location = player.getLocation();
                    Location location1 = block.getLocation();
                    Location location2 = location1.clone().add(0.0D, 1.0D, 0.0D);

                    if (location.getBlockX() == location1.getBlockX() && location.getBlockZ() == location1.getBlockZ() && !class_66.BBBΠ(location2) && (player.isSneaking() || !class_66.OOOΠ(location1)) && d0 > 2.0D && d0 < 2.5D) {
                        class_39.method_170(player, "disallowed_building=protection", 60);
                    }
                }
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockPlaceEvent blockplaceevent) {
        Player player = blockplaceevent.getPlayer();
        Location location = blockplaceevent.getBlock().getLocation();
        Location location1 = player.getLocation();
        Location location2 = location1.clone().add(0.0D, -1.0D, 0.0D);
        byte b0 = 0;

        if (!class_71.method_416(location1)) {
            if (location2.getBlockX() == location.getBlockX() && location2.getBlockY() == location.getBlockY() && location2.getBlockZ() == location.getBlockZ()) {
                b0 = 1;
            } else if (!class_66.HHΞ(player, 0.3D, -1.0D, 0.3D)) {
                b0 = 2;
            }
        }

        if (b0 == 1 && blockplaceevent.isCancelled()) {
            class_21.method_78(player);
            class_69.HHΞ(player);
            location1.setPitch(0.0F);
            player.teleport(location1);
            class_39.method_171(player, "disallowed_building=protection");
        } else if (b0 != 0) {
            class_39.method_170(player, "disallowed_building=protection", 60);
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "disallowed_building=protection");
    }
}
