import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;

// $FF: renamed from: o
public class class_132 implements Listener {

    private Enums.HackType Ξ;

    public class_132() {
        this.field_220 = Enums.HackType.FastBow;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityShootBowEvent entityshootbowevent) {
        if (entityshootbowevent.getEntity() instanceof Player) {
            Player player = (Player) entityshootbowevent.getEntity();

            if (class_38.method_157(player, this.field_220, true) || entityshootbowevent.isCancelled() && class_39.method_169(player, this.field_220.toString() + "=time")) {
                return;
            }

            boolean flag = this.method_633(player);
            boolean flag1 = this.method_632(player, entityshootbowevent.getForce());

            if (class_39.method_169(player, this.field_220.toString() + "=time")) {
                class_39.method_170(player, this.field_220.toString() + "=time", 9);
                if (flag || flag1) {
                    entityshootbowevent.setCancelled(true);
                }
            } else if (class_123.method_583(player, this.field_220)) {
                entityshootbowevent.setCancelled(true);
            }
        }

    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (!class_38.method_157(player, this.field_220, true)) {
            if (playerinteractevent.getAction() == Action.RIGHT_CLICK_AIR && player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOW && player.getInventory().contains(Material.ARROW)) {
                class_45.method_211(player, this.field_220.toString() + "=interact");
            }

        }
    }

    private boolean Ξ(Player player, float f) {
        if (player != null && class_120.method_572("FastBow.check_bow_force")) {
            boolean flag = false;

            if (f == 1.0F && class_45.method_209(player, this.field_220.toString() + "=interact")) {
                long i = class_45.method_210(player, this.field_220.toString() + "=interact");

                if (i <= class_41.method_180(500L)) {
                    class_38.method_159(player, this.field_220, "t: bow force, ms: " + i);
                    if (class_123.method_583(player, this.field_220)) {
                        flag = true;
                    }
                }
            }

            return flag;
        } else {
            return false;
        }
    }

    private boolean Ξ(Player player) {
        if (player != null && class_120.method_572("FastBow.check_bow_shots")) {
            boolean flag = false;
            String s = null;

            class_36.method_135(player, this.field_220.toString() + "=fast", 10);
            class_36.method_135(player, this.field_220.toString() + "=medium", 20);
            class_36.method_135(player, this.field_220.toString() + "=slow", 30);
            if (class_36.method_133(player, this.field_220.toString() + "=fast") == class_41.method_179(6)) {
                class_36.method_138(player, this.field_220.toString() + "=fast");
                s = "fast";
            } else if (class_36.method_133(player, this.field_220.toString() + "=medium") == class_41.method_179(7)) {
                class_36.method_138(player, this.field_220.toString() + "=medium");
                s = "medium";
            } else if (class_36.method_133(player, this.field_220.toString() + "=slow") == class_41.method_179(9)) {
                class_36.method_138(player, this.field_220.toString() + "=slow");
                s = "slow";
            }

            if (s != null) {
                class_38.method_159(player, this.field_220, "t: bow shots, r: " + s);
                if (class_123.method_583(player, this.field_220)) {
                    flag = true;
                }
            }

            return flag;
        } else {
            return false;
        }
    }
}
