import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

// $FF: renamed from: Z
public class class_117 implements Listener {

    private Enums.HackType Ξ;

    public class_117() {
        this.field_193 = Enums.HackType.InventoryClicks;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(InventoryClickEvent inventoryclickevent) {
        Player player = (Player) inventoryclickevent.getWhoClicked();
        ClickType clicktype = inventoryclickevent.getClick();
        ItemStack itemstack = inventoryclickevent.getCurrentItem();

        if (!inventoryclickevent.isCancelled() && !class_38.method_157(player, this.field_193, true) && !clicktype.isCreativeAction() && !clicktype.isKeyboardClick() && itemstack != null && itemstack.getItemMeta() != null && itemstack.getType() != null && class_71.PPΞ(player)) {
            boolean flag = false;
            boolean flag1 = clicktype.isShiftClick();

            if (flag1 && !class_71.OOOΠ(player)) {
                class_36.method_135(player, this.field_193.toString() + "=shift", 5);
                int i = class_36.method_133(player, this.field_193.toString() + "=shift");

                if (i > class_41.method_179(5)) {
                    class_38.method_159(player, this.field_193, "t: shift-click");
                    flag = true;
                }
            } else if (!flag1 && class_45.method_209(player, this.field_193.toString() + "=time")) {
                long j = class_45.method_210(player, this.field_193.toString() + "=time");

                if (j < class_41.method_180(50L)) {
                    class_36.method_135(player, this.field_193.toString() + "=fast", 5);
                    if (class_36.method_133(player, this.field_193.toString() + "=fast") >= 2) {
                        class_38.method_159(player, this.field_193, "t: normal, speed: fast, ms: " + j);
                        flag = true;
                    }
                } else if (j < class_41.method_180(100L)) {
                    class_36.method_135(player, this.field_193.toString() + "=average", 5);
                    if (class_36.method_133(player, this.field_193.toString() + "=average") >= 4) {
                        class_38.method_159(player, this.field_193, "t: normal, speed: average, ms: " + j);
                        flag = true;
                    }
                } else if (j < class_41.method_180(150L)) {
                    class_36.method_135(player, this.field_193.toString() + "=slow", 10);
                    if (class_36.method_133(player, this.field_193.toString() + "=slow") >= 8) {
                        class_38.method_159(player, this.field_193, "t: normal, speed: slow, ms: " + j);
                        flag = true;
                    }
                }
            }

            class_45.method_211(player, this.field_193.toString() + "=time");
            if (flag && class_123.method_583(player, this.field_193)) {
                inventoryclickevent.setCancelled(true);
                player.closeInventory();
            }

        }
    }
}
