import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: ax
public class class_63 implements Listener {

    private static Enums.HackType Ξ;

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        if (!playerinteractevent.isCancelled()) {
            Player player = playerinteractevent.getPlayer();
            Block block = playerinteractevent.getClickedBlock();

            if (this.method_324(player, block) && class_120.method_572("GhostHand.check_player_interactions")) {
                if (playerinteractevent.getAction() == Action.RIGHT_CLICK_BLOCK && (class_66.OOOΠ(block.getLocation()) || player.getItemInHand().getType() == Material.getMaterial(383))) {
                    Block block1 = class_66.method_345(player, block.getLocation());

                    if (block1 != null) {
                        class_38.method_159(player, class_63.field_128, "t: interact, c: " + class_66.method_343(block) + ", r: " + class_66.method_343(block1));
                        if (class_123.method_583(player, class_63.field_128)) {
                            playerinteractevent.setCancelled(true);
                        }
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
            Block block = blockbreakevent.getBlock();

            if (this.method_324(player, block) && class_120.method_572("GhostHand.check_block_breaking") && !player.hasPotionEffect(PotionEffectType.FAST_DIGGING) && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) <= 5 && (!class_7.method_31(player) || class_66.OOOΠ(block.getLocation()))) {
                Block block1 = class_66.method_345(player, block.getLocation());

                if (class_123.method_583(player, class_63.field_128) && !class_39.method_169(player, class_63.field_128.toString() + "=break=cooldown")) {
                    blockbreakevent.setCancelled(true);
                } else if (block1 != null) {
                    class_38.method_159(player, class_63.field_128, "t: break, c: " + class_66.method_343(block) + ", r: " + class_66.method_343(block1));
                    if (class_123.method_583(player, class_63.field_128)) {
                        blockbreakevent.setCancelled(true);
                        class_39.method_170(player, class_63.field_128.toString() + "=break=cooldown", 10);
                    }
                }

            }
        }
    }

    private boolean Ξ(Player player, Block block) {
        return player != null && block != null ? !class_38.method_157(player, class_63.field_128, true) && !player.isFlying() && block.getWorld() == player.getWorld() && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) : false;
    }

    static {
        class_63.field_128 = Enums.HackType.GhostHand;
    }
}
