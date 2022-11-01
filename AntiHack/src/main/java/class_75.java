import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

// $FF: renamed from: bf
public class class_75 implements Listener {

    public static void Ξ(Player player) {
        if (class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_251) {
            ItemStack itemstack = player.getInventory().getChestplate();

            if (itemstack != null && itemstack.getDurability() < 432 && itemstack.getType() == Material.ELYTRA) {
                class_39.method_170(player, "elytra=has", 2);
            }
        }

    }

    @EventHandler
    private void Ξ(EntityToggleGlideEvent entitytoggleglideevent) {
        if (entitytoggleglideevent.getEntity() instanceof Player) {
            Player player = (Player) entitytoggleglideevent.getEntity();

            if (!class_39.method_169(player, "elytra=has")) {
                boolean flag = entitytoggleglideevent.isGliding();

                if (flag && class_71.method_410(player) > 0) {
                    class_36.method_134(player, "elytra", 1);
                } else if (!flag) {
                    class_39.method_170(player, "elytra", class_36.method_133(player, "elytra=firework") == 1 ? 120 : 60);
                    class_36.method_138(player, "elytra=firework");
                    class_36.method_138(player, "elytra");
                    class_71.method_412(player);
                }
            }
        }

    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        if (playerinteractevent.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = playerinteractevent.getPlayer();

            if (player.getItemInHand().getType() == Material.FIREWORK) {
                class_36.method_134(player, "elytra=firework", 1);
            }
        }

    }
}
