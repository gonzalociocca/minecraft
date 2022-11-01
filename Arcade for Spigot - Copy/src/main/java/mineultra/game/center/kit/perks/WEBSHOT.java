package mineultra.game.center.kit.perks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.projectile.IThrown;
import mineultra.core.projectile.ProjectileUser;
import mineultra.core.recharge.Recharge;
import mineultra.game.center.kit.Perk;

public class WEBSHOT extends Perk implements IThrown
{
    public WEBSHOT() {
        super("Web Shot", new String[] { String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Web Shot" });
    }
    
    @EventHandler
    public void ShootWeb(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }
        if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE")) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, this.GetName(), 3000L, true, true)) {
            return;
        }
        event.setCancelled(true);
        UtilInv.remove(player, Material.WEB, (byte)0, 1);
        UtilInv.Update((Entity)player);
        final Item ent = player.getWorld().dropItem(player.getEyeLocation(), ItemStackFactory.Instance.CreateStack(Material.WEB));
        UtilAction.velocity((Entity)ent, player.getLocation().getDirection(), 1.0, false, 0.0, 0.2, 10.0, false);
        this.Manager.GetProjectile().AddThrow((Entity)ent, (LivingEntity)player, this, -1L, true, true, true, false, 2.0);
        UtilPlayer.message((Entity)player, F.main("Game", "You used " + F.skill(this.GetName()) + "."));
        player.getWorld().playSound(player.getLocation(), Sound.SPIDER_IDLE, 1.0f, 0.6f);
    }
    
    @Override
    public void Collide(final LivingEntity target, final Block block, final ProjectileUser data) {
        if (target != null) {
            data.GetThrown().remove();
            this.Manager.GetBlockRestore().Add(target.getLocation().getBlock(), 30, (byte)0, 2500L);
            return;
        }
        this.Web(data);
    }
    
    @Override
    public void Idle(final ProjectileUser data) {
        this.Web(data);
    }
    
    @Override
    public void Expire(final ProjectileUser data) {
        this.Web(data);
    }
    
    public void Web(final ProjectileUser data) {
        final Location loc = data.GetThrown().getLocation();
        data.GetThrown().remove();
        this.Manager.GetBlockRestore().Add(loc.getBlock(), 30, (byte)0, 2500L);
    }
}
