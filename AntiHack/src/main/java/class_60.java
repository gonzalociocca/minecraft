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

// $FF: renamed from: au
public class class_60 implements Listener {

    private double Ξ;
    private Enums.HackType Ξ;

    public class_60() {
        this.field_124 = 7.5D;
        this.field_125 = Enums.HackType.BlockReach;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockBreakEvent blockbreakevent) {
        if (!blockbreakevent.isCancelled()) {
            Player player = blockbreakevent.getPlayer();
            Block block = blockbreakevent.getBlock();

            if (!class_38.method_157(player, this.field_125, true) && !class_30.method_106(block) && (player.getItemInHand() == null || player.getItemInHand().getType() != Material.FLINT_AND_STEEL)) {
                double d0 = class_69.method_391(player.getLocation(), block.getLocation());

                if (d0 >= class_41.method_178(this.field_124)) {
                    class_38.method_159(player, this.field_125, "t: break, d: " + d0);
                    if (class_123.method_583(player, this.field_125)) {
                        blockbreakevent.setCancelled(true);
                    }
                }

            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockPlaceEvent blockplaceevent) {
        if (!blockplaceevent.isCancelled()) {
            Player player = blockplaceevent.getPlayer();
            Block block = blockplaceevent.getBlockAgainst();

            if (!class_38.method_157(player, this.field_125, true) && class_66.method_340(block.getLocation(), true) && (player.getItemInHand() == null || player.getItemInHand().getType() != Material.getMaterial(351))) {
                Block block1 = blockplaceevent.getBlock();
                double d0 = block1.getLocation().getY() - player.getLocation().getY();
                double d1 = class_69.method_391(player.getLocation(), block1.getLocation());
                double d2 = class_69.method_391(player.getLocation(), block.getLocation()) + 0.36D;

                if (d1 >= 1.3D && d1 > d2 && d0 <= 0.5D) {
                    class_38.method_159(player, this.field_125, "t: unusual, d: " + d1 + ", ab_d: " + d2 + ", ypos: " + d0);
                    if (class_123.method_583(player, this.field_125)) {
                        blockplaceevent.setCancelled(true);
                    }

                } else {
                    if (d1 >= class_41.method_178(this.field_124)) {
                        class_38.method_159(player, this.field_125, "t: illegal, d: " + d1);
                        if (!class_123.method_587(this.field_125)) {
                            blockplaceevent.setCancelled(true);
                        }
                    }

                }
            }
        }
    }
}
