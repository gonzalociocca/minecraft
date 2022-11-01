package mineultra.game.center.addons;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import mineultra.game.center.centerManager;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilGear;
import mineultra.core.common.util.UtilPlayer;
import mineultra.minecraft.game.core.Buffer.Buffer;

public class SoupAddon extends MiniPlugin
{
    public centerManager Manager;
    
    public SoupAddon(final JavaPlugin plugin, final centerManager manager) {
        super("Soup Addon", plugin);
        this.Manager = manager;
    }
    
    @EventHandler
    public void EatSoup(final PlayerInteractEvent event) {
        if (this.Manager.GetGame() == null) {
            return;
        }
        if (!this.Manager.GetGame().IsLive()) {
            return;
        }
        if (!this.Manager.GetGame().SoupEnabled) {
            return;
        }
        final Player player = event.getPlayer();
        if (!UtilGear.isMat(player.getItemInHand(), Material.MUSHROOM_SOUP)) {
            return;
        }
        if (UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 2.0f, 1.0f);
        player.getWorld().playEffect(player.getEyeLocation(), Effect.STEP_SOUND, 39);
        player.getWorld().playEffect(player.getEyeLocation(), Effect.STEP_SOUND, 40);
        this.Manager.GetBuffer().Factory().Custom("Mushroom Soup", (LivingEntity)player, (LivingEntity)player, Buffer.BufferType.REGENERATION, 4.0, 1, false, Material.MUSHROOM_SOUP,(byte) 0, true);
        UtilPlayer.hunger(player, 3);
        event.setCancelled(true);
        player.setItemInHand((ItemStack)null);
    }
}
