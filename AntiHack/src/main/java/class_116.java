import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;

// $FF: renamed from: Y
public class class_116 implements Listener {

    private Enums.HackType Ξ;

    public class_116() {
        this.field_192 = Enums.HackType.ImpossibleInventory;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(InventoryClickEvent inventoryclickevent) {
        Player player = (Player) inventoryclickevent.getWhoClicked();
        ClickType clicktype = inventoryclickevent.getClick();
        boolean flag = class_71.PPΞ(player);

        if (!inventoryclickevent.isCancelled() && !class_38.method_157(player, this.field_192, true) && inventoryclickevent.getCurrentItem() != null && this.method_554(player) && !clicktype.isCreativeAction() && !clicktype.isKeyboardClick() && !class_71.method_400(player) && (!clicktype.isShiftClick() || !flag) && !class_66.method_332(player.getLocation().add(0.0D, -1.0D, 0.0D)) && !class_66.method_332(player.getLocation().add(1.0D, -1.0D, 0.0D)) && !class_66.method_332(player.getLocation().add(-1.0D, -1.0D, 0.0D)) && !class_66.method_332(player.getLocation().add(0.0D, -1.0D, 1.0D)) && !class_66.method_332(player.getLocation().add(0.0D, -1.0D, -1.0D)) && !class_66.method_332(player.getLocation().add(1.0D, -1.0D, 1.0D)) && !class_66.method_332(player.getLocation().add(-1.0D, -1.0D, -1.0D)) && !class_66.method_332(player.getLocation().add(-1.0D, -1.0D, 1.0D)) && !class_66.method_332(player.getLocation().add(1.0D, -1.0D, -1.0D))) {
            class_38.method_159(player, this.field_192, "t: closed inventory");
            if (class_123.method_583(player, this.field_192)) {
                inventoryclickevent.setCancelled(true);
                player.closeInventory();
            }

        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Π(InventoryClickEvent inventoryclickevent) {
        Player player = (Player) inventoryclickevent.getWhoClicked();

        if (!inventoryclickevent.isCancelled() && !class_38.method_157(player, this.field_192, true) && inventoryclickevent.getCurrentItem() != null) {
            if (player.getLocation().getBlock().getType() == Material.PORTAL || player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.PORTAL) {
                class_38.method_159(player, this.field_192, "t: portal inventory");
                if (class_123.method_583(player, this.field_192)) {
                    inventoryclickevent.setCancelled(true);
                    player.closeInventory();
                }
            }

        }
    }

    @EventHandler
    private void Ξ(PlayerMoveEvent playermoveevent) {
        Player player = playermoveevent.getPlayer();

        if (!playermoveevent.isCancelled() && !class_38.method_157(player, this.field_192, true) && this.method_554(player)) {
            if (class_71.OOOΠ(player)) {
                class_38.method_159(player, this.field_192, "t: cursor usage");
                if (class_123.method_583(player, this.field_192)) {
                    playermoveevent.setCancelled(true);
                    player.closeInventory();
                }
            }

        }
    }

    private boolean Ξ(Player player) {
        if (player == null) {
            return false;
        } else {
            boolean flag = class_120.method_572("ImpossibleInventory.check_sneaking");
            boolean flag1 = class_120.method_572("ImpossibleInventory.check_sprinting");
            boolean flag2 = class_120.method_572("ImpossibleInventory.check_sleeping");
            boolean flag3 = class_120.method_572("ImpossibleInventory.check_walking");
            boolean flag4 = class_120.method_572("ImpossibleInventory.check_dead");
            boolean flag5 = class_120.method_572("ImpossibleInventory.check_sprint_jumping");
            boolean flag6 = class_120.method_572("ImpossibleInventory.check_walk_jumping");

            return player.isSneaking() && flag || player.isDead() && flag4 || player.isSleeping() && flag2 || class_71.method_407(player) && flag3 || (player.isSprinting() || class_71.method_408(player)) && flag1 || class_71.HHΠ(player) && flag5 || class_71.BBBΠ(player) && flag6;
        }
    }
}
