import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

// $FF: renamed from: aR
public class class_17 implements Listener {

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "piston=protection=has");
    }

    public static boolean Π(Player player) {
        return !class_39.method_169(player, "piston=protection=had");
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockPistonExtendEvent blockpistonextendevent) {
        if (!blockpistonextendevent.isCancelled()) {
            Location location = blockpistonextendevent.getBlock().getLocation();
            Iterator iterator = Bukkit.getOnlinePlayers().iterator();

            while (iterator.hasNext()) {
                Player player = (Player) iterator.next();
                Location location1 = player.getLocation();
                double d0 = !class_20.method_75(player) && !class_66.method_352(player, 2) ? 2.5D : 3.5D;

                if (class_69.method_391(location1, location) <= d0 || class_69.method_391(location1.clone().add(0.0D, 1.0D, 0.0D), location) <= d0) {
                    class_39.method_170(player, "piston=protection=has", 30);
                    class_39.method_170(player, "piston=protection=had", 60);
                    return;
                }

                Iterator iterator1 = blockpistonextendevent.getBlocks().iterator();

                while (iterator1.hasNext()) {
                    Block block = (Block) iterator1.next();
                    Location location2 = block.getLocation();

                    if (class_69.method_391(location1, location2) <= d0 || class_69.method_391(location1.clone().add(0.0D, 1.0D, 0.0D), location2) <= d0) {
                        class_39.method_170(player, "piston=protection=has", 30);
                        class_39.method_170(player, "piston=protection=had", 60);
                    }
                }
            }
        }

    }
}
