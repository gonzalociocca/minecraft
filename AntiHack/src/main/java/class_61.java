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

// $FF: renamed from: av
public class class_61 implements Listener {

    private Enums.HackType Ξ;

    public class_61() {
        this.field_126 = Enums.HackType.FastBreak;
    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (this.method_320(player)) {
            Block block = playerinteractevent.getClickedBlock();

            if (playerinteractevent.getAction() == Action.LEFT_CLICK_BLOCK && block != null) {
                class_45.method_211(player, this.field_126.toString() + "=" + block);
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

            if (this.method_320(player) && !class_66.PPΞ(block.getLocation())) {
                if (!class_39.method_169(player, this.field_126.toString() + "=cooldown")) {
                    if (class_123.method_583(player, this.field_126)) {
                        blockbreakevent.setCancelled(true);
                    }
                } else if (class_45.method_209(player, this.field_126.toString() + "=" + block)) {
                    long i = class_45.method_210(player, this.field_126.toString() + "=" + block);
                    long j = 300L;

                    if (player.getItemInHand() != null) {
                        if (player.getItemInHand().getType() != Material.WOOD_AXE && player.getItemInHand().getType() != Material.WOOD_PICKAXE && player.getItemInHand().getType() != Material.WOOD_SPADE) {
                            if (player.getItemInHand().getType() != Material.STONE_AXE && player.getItemInHand().getType() != Material.STONE_PICKAXE && player.getItemInHand().getType() != Material.STONE_SPADE) {
                                if (player.getItemInHand().getType() != Material.IRON_AXE && player.getItemInHand().getType() != Material.IRON_PICKAXE && player.getItemInHand().getType() != Material.IRON_SPADE) {
                                    if (player.getItemInHand().getType() != Material.DIAMOND_AXE && player.getItemInHand().getType() != Material.DIAMOND_PICKAXE && player.getItemInHand().getType() != Material.DIAMOND_SPADE) {
                                        if (player.getItemInHand().getType() != Material.GOLD_AXE && player.getItemInHand().getType() != Material.GOLD_PICKAXE && player.getItemInHand().getType() != Material.GOLD_SPADE) {
                                            if (player.getItemInHand().getType() == Material.SHEARS) {
                                                j /= 15L;
                                            }
                                        } else {
                                            j /= 12L;
                                        }
                                    } else {
                                        j /= 8L;
                                    }
                                } else {
                                    j /= 6L;
                                }
                            } else {
                                j /= 4L;
                            }
                        } else {
                            j /= 2L;
                        }
                    }

                    j = class_41.method_180(j);
                    if (i > 5L && j > 5L && i < j) {
                        boolean flag = false;

                        if (player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) <= 0 && player.getItemInHand().getType() != Material.SHEARS) {
                            flag = true;
                        } else {
                            class_36.method_135(player, this.field_126.toString() + "=attempts", 20);
                            if (class_36.method_133(player, this.field_126.toString() + "=attempts") >= 2) {
                                flag = true;
                            }
                        }

                        if (flag) {
                            class_38.method_159(player, this.field_126, "ms: " + i + ", l: " + j + ", b: " + class_66.method_343(block));
                            if (class_123.method_583(player, this.field_126)) {
                                int k = class_120.method_571("FastBreak.cancel_seconds");

                                k = k > 60 ? 60 : k;
                                blockbreakevent.setCancelled(true);
                                class_39.method_170(player, this.field_126.toString() + "=cooldown", k * 20);
                            }
                        }
                    }
                }

            }
        }
    }

    private boolean Ξ(Player player) {
        return player == null ? false : !class_38.method_157(player, this.field_126, true) && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) && !player.hasPotionEffect(PotionEffectType.FAST_DIGGING) && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) <= 4;
    }
}
