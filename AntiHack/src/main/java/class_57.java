import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: ar
public class class_57 implements Listener {

    private Enums.HackType Ξ;
    private long Ξ;
    private int Ξ;

    public class_57() {
        this.field_116 = Enums.HackType.FastEat;
        this.field_117 = 1000L;
        this.field_118 = 25;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(FoodLevelChangeEvent foodlevelchangeevent) {
        if (foodlevelchangeevent.getEntity() instanceof Player && !foodlevelchangeevent.isCancelled()) {
            Player player = (Player) foodlevelchangeevent.getEntity();

            if (!this.HHΞ(player)) {
                return;
            }

            if (foodlevelchangeevent.getFoodLevel() > player.getFoodLevel()) {
                boolean flag = false;

                if (this.method_303(player) || this.method_304(player)) {
                    flag = true;
                    foodlevelchangeevent.setCancelled(true);
                }

                if (flag) {
                    int i = foodlevelchangeevent.getFoodLevel() - player.getFoodLevel();

                    player.setFoodLevel(player.getFoodLevel() - i);
                    if (!class_39.method_169(player, this.field_116.toString() + "=regeneration")) {
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                    }

                    if (!class_39.method_169(player, this.field_116.toString() + "=absorption")) {
                        player.removePotionEffect(PotionEffectType.ABSORPTION);
                    }

                    if (!class_39.method_169(player, this.field_116.toString() + "=resistance")) {
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    }
                }
            }
        }

    }

    private boolean Ξ(Player player) {
        if (player == null) {
            return false;
        } else {
            boolean flag = false;

            if (class_45.method_209(player, this.field_116.toString() + "=time")) {
                long i = class_45.method_210(player, this.field_116.toString() + "=time");

                if (i <= class_41.method_180(this.field_117)) {
                    class_38.method_159(player, this.field_116, "t: eat, ms: " + i);
                    if (class_123.method_583(player, this.field_116)) {
                        flag = true;
                    }
                }
            }

            class_45.method_211(player, this.field_116.toString() + "=time");
            return flag;
        }
    }

    private boolean Π(Player player) {
        if (player == null) {
            return false;
        } else {
            boolean flag = false;

            if (class_45.method_209(player, this.field_116.toString() + "=food")) {
                long i = class_45.method_210(player, this.field_116.toString() + "=food");

                if (i <= class_41.method_180(this.field_117)) {
                    class_38.method_159(player, this.field_116, "t: interact, ms: " + i);
                    if (class_123.method_583(player, this.field_116)) {
                        flag = true;
                    }
                }
            }

            return flag;
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();
        Block block = playerinteractevent.getClickedBlock();

        if (this.HHΞ(player)) {
            if (playerinteractevent.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (block.getType() == Material.CAKE_BLOCK) {
                    class_38.method_160(player, this.field_116, 10);
                }
            } else if (playerinteractevent.getAction() == Action.RIGHT_CLICK_AIR && class_71.BBΞ(player)) {
                this.method_306(player);
                class_45.method_211(player, this.field_116.toString() + "=food");
            }

        }
    }

    private void Ξ(Player player) {
        if (player != null) {
            if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.GOLDEN_APPLE) {
                if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                    class_39.method_170(player, this.field_116.toString() + "=regeneration", this.field_118);
                }

                if (!player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                    class_39.method_170(player, this.field_116.toString() + "=absorption", this.field_118);
                }

                if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                    class_39.method_170(player, this.field_116.toString() + "=resistance", this.field_118);
                }
            }

        }
    }

    private boolean HHΞ(Player player) {
        return player == null ? false : !class_38.method_157(player, this.field_116, true) && player.getWorld().getDifficulty() != Difficulty.PEACEFUL && !player.hasPotionEffect(PotionEffectType.SATURATION);
    }
}
