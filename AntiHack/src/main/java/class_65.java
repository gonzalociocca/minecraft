import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

// $FF: renamed from: az
public class class_65 implements Listener {

    private Enums.HackType Ξ;

    public class_65() {
        this.field_130 = Enums.HackType.Liquids;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockPlaceEvent blockplaceevent) {
        if (!blockplaceevent.isCancelled()) {
            Player player = blockplaceevent.getPlayer();
            Block block = blockplaceevent.getBlock();
            Material material = block.getType();

            if (!class_38.method_157(player, this.field_130, false) && !class_66.method_338(block.getLocation()) && class_120.method_572("Liquids.check_block_placing") && material != Material.WATER_LILY) {
                if (blockplaceevent.getBlockAgainst().isLiquid()) {
                    class_38.method_159(player, this.field_130, "t: place, b: " + class_66.method_343(block));
                    if (class_123.method_583(player, this.field_130)) {
                        blockplaceevent.setCancelled(true);
                    }
                }

            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockBreakEvent blockbreakevent) {
        if (!blockbreakevent.isCancelled()) {
            Player player = blockbreakevent.getPlayer();

            if (!class_38.method_157(player, this.field_130, false) && class_120.method_572("Liquids.check_block_breaking")) {
                Block block = blockbreakevent.getBlock();

                if (block.isLiquid()) {
                    class_38.method_159(player, this.field_130, "t: break, b: " + class_66.method_343(block));
                    if (class_123.method_583(player, this.field_130)) {
                        blockbreakevent.setCancelled(true);
                    }
                }

            }
        }
    }
}
