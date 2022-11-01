import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;

// $FF: renamed from: T
public class class_106 implements Listener {

    private Enums.HackType Ξ;

    public class_106() {
        this.field_182 = Enums.HackType.Exploits;
    }

    @EventHandler
    private void Ξ(EntityToggleGlideEvent entitytoggleglideevent) {
        if (entitytoggleglideevent.getEntity() instanceof Player) {
            Player player = (Player) entitytoggleglideevent.getEntity();
            ItemStack itemstack = player.getInventory().getChestplate();

            if (entitytoggleglideevent.isGliding() && (itemstack == null || itemstack.getType() != Material.ELYTRA)) {
                class_38.method_159(player, this.field_182, "t: illegal elytra packet");
            }
        }

    }
}
