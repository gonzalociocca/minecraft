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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: aA
public class class_0 implements Listener {

    private Enums.HackType Ξ;

    public class_0() {
        this.field_0 = Enums.HackType.Nuker;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockBreakEvent blockbreakevent) {
        if (!blockbreakevent.isCancelled()) {
            Player player = blockbreakevent.getPlayer();
            Block block = blockbreakevent.getBlock();

            if (this.method_3(player) && !class_30.method_106(block)) {
                if (!class_39.method_169(player, this.field_0.toString() + "=cooldown")) {
                    if (class_123.method_583(player, this.field_0)) {
                        blockbreakevent.setCancelled(true);
                    }
                } else {
                    boolean flag = false;

                    if (this.method_1(player, block)) {
                        flag = true;
                    } else if (this.method_2(player, block)) {
                        flag = true;
                    }

                    if (flag && class_123.method_583(player, this.field_0)) {
                        int i = class_120.method_571("Nuker.cancel_seconds");

                        i = i > 60 ? 60 : i;
                        blockbreakevent.setCancelled(true);
                        class_39.method_170(player, this.field_0.toString() + "=cooldown", i * 20);
                    }
                }

            }
        }
    }

    private boolean Ξ(Player player, Block block) {
        if (player != null && block != null && !class_66.PPΞ(block.getLocation()) && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) <= 5) {
            long i = 50L;
            byte b0 = 1;
            byte b1 = 1;
            boolean flag = false;

            if (player.getGameMode() == GameMode.CREATIVE || player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) == 5) {
                i = 40L;
                b0 = 4;
                b1 = 3;
            }

            i = class_41.method_180(i);
            if (class_45.method_209(player, this.field_0.toString() + "=time")) {
                long j = class_45.method_210(player, this.field_0.toString() + "=time");

                if (j <= i) {
                    class_36.method_135(player, this.field_0.toString() + "=attempts", b0);
                    if (class_36.method_133(player, this.field_0.toString() + "=attempts") >= b1) {
                        class_38.method_159(player, this.field_0, "t: delay, ms: " + j + ", l: " + i + ", b: " + class_66.method_343(block));
                        flag = true;
                    }
                }
            }

            class_45.method_211(player, this.field_0.toString() + "=time");
            return flag;
        } else {
            return false;
        }
    }

    private boolean Π(Player player, Block block) {
        if (player != null && block != null) {
            class_36.method_135(player, this.field_0.toString() + "=max_breaks_per_second", 20);
            int i = class_36.method_133(player, this.field_0.toString() + "=max_breaks_per_second");
            int j = class_120.method_571("Nuker.max_breaks_per_second");

            j = j < 10 ? 10 : j;
            if (i >= j) {
                class_38.method_159(player, this.field_0, "t: max-breaks-per-sec, b: " + class_66.method_343(block) + ", a: " + i);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean Ξ(Player player) {
        if (player == null) {
            return false;
        } else {
            if (player.getItemInHand().getType() != null) {
                if (player.getItemInHand().getType() == Material.SHEARS || player.getItemInHand().getType() == Material.DIAMOND_SPADE || player.getItemInHand().getType() == Material.IRON_SPADE || player.getItemInHand().getType() == Material.GOLD_SPADE || player.getItemInHand().getType() == Material.STONE_SPADE || player.getItemInHand().getType() == Material.WOOD_SPADE) {
                    return false;
                }

                if ((player.getItemInHand().getType() == Material.DIAMOND_AXE || player.getItemInHand().getType() == Material.IRON_AXE || player.getItemInHand().getType() == Material.GOLD_AXE || player.getItemInHand().getType() == Material.STONE_AXE || player.getItemInHand().getType() == Material.WOOD_AXE) && player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) >= 4) {
                    return false;
                }
            }

            return !class_38.method_157(player, this.field_0, true) && !player.hasPotionEffect(PotionEffectType.FAST_DIGGING) && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.CREATIVE);
        }
    }
}
