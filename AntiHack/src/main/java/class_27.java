import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

// $FF: renamed from: aa
public class class_27 implements Listener {

    private Enums.HackType Ξ;

    public class_27() {
        this.field_22 = Enums.HackType.ItemDrops;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerDropItemEvent playerdropitemevent) {
        Player player = playerdropitemevent.getPlayer();

        if (!playerdropitemevent.isCancelled() && this.method_95(player)) {
            if (!class_39.method_169(player, this.field_22.toString() + "=cooldown")) {
                if (class_123.method_583(player, this.field_22)) {
                    playerdropitemevent.setCancelled(true);
                    player.closeInventory();
                }
            } else if (class_45.method_209(player, this.field_22.toString() + "=time")) {
                long i = class_45.method_210(player, this.field_22.toString() + "=time");

                if (i <= class_41.method_180(50L)) {
                    class_36.method_135(player, this.field_22.toString() + "=attempts", 10);
                    if (class_36.method_133(player, this.field_22.toString() + "=attempts") >= 8) {
                        class_38.method_159(player, this.field_22, "ms: " + i);
                        if (class_123.method_583(player, this.field_22)) {
                            playerdropitemevent.setCancelled(true);
                            player.closeInventory();
                            class_39.method_170(player, this.field_22.toString() + "=cooldown", 10);
                        }
                    }
                }
            }

            class_45.method_211(player, this.field_22.toString() + "=time");
        }
    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();
        Block block = playerinteractevent.getClickedBlock();

        if (playerinteractevent.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.STATIONARY_WATER && player.getItemInHand() != null && player.getItemInHand().getType() == Material.GLASS_BOTTLE) {
            class_38.method_160(player, this.field_22, 10);
        }

    }

    private boolean Ξ(Player player) {
        return player == null ? false : !class_38.method_157(player, this.field_22, true) && !class_71.PPPΠ(player);
    }
}
