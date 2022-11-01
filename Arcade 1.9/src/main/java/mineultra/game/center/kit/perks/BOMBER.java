package mineultra.game.center.kit.perks;

import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BOMBER extends Perk
{
    private HashMap<Entity, Player> _tntMap;
    private int _spawnRate;
    private int _max;
    private int _fuse;
    
    public BOMBER(final int spawnRate, final int max, final int fuse) {
        super("Bomber", new String[] { String.valueOf(C.cGray) + "Receive 1 TNT every " + spawnRate + " seconds. Maximum of " + max + ".", String.valueOf(C.cYellow) + "Click" + C.cGray + " with TNT to " + C.cGreen + "Throw TNT" });
        this._tntMap = new HashMap<Entity, Player>();
        this._spawnRate = spawnRate;
        this._max = max;
        this._fuse = fuse;
    }
    
    @Override
    public void Apply(final Player player) {
        Recharge.Instance.use(player, this.GetName(), this._spawnRate * 1000, false, false);
    }
    
    @EventHandler
    public void TNTSpawn(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player cur = players[i];
            if (this.Kit.HasKit(cur)) {
                if (this.Manager.GetGame().IsAlive(cur)) {
                    if (Recharge.Instance.use(cur, this.GetName(), this._spawnRate * 1000, false, false)) {
                        if (!UtilInv.contains(cur, Material.TNT, (byte)0, this._max)) {
                            cur.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.TNT, (byte)0, (byte)1, F.item("Throwing TNT")) });
                            cur.playSound(cur.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2.0f, 1.0f);
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void TNTDrop(final PlayerDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!UtilInv.IsItem(event.getItemDrop().getItemStack(), Material.TNT, (byte)0)) {
            return;
        }
        event.setCancelled(true);
        UtilPlayer.message((Entity)event.getPlayer(), F.main(this.GetName(), "You cannot drop " + F.item("Throwing TNT") + "."));
    }
    
    @EventHandler
    public void TNTDeathRemove(final PlayerDeathEvent event) {
        final HashSet<ItemStack> remove = new HashSet<ItemStack>();
        for (final ItemStack item : event.getDrops()) {
            if (UtilInv.IsItem(item, Material.TNT, (byte)0)) {
                remove.add(item);
            }
        }
        for (final ItemStack item : remove) {
            event.getDrops().remove(item);
        }
    }
    
    @EventHandler
    public void TNTInvClick(final InventoryClickEvent event) {
        UtilInv.DisallowMovementOf(event, "Throwing TNT", Material.TNT, (byte)0, true);
    }
    
    @EventHandler
    public void TNTThrow(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        final Player player = event.getPlayer();
        if (!UtilInv.IsItem(player.getItemInHand(), Material.TNT, (byte)0)) {
            return;
        }
        if (!this.Kit.HasKit(player)) {
            return;
        }
        event.setCancelled(true);
        if (!this.Manager.GetGame().CanThrowTNT(player.getLocation())) {
            UtilPlayer.message((Entity)event.getPlayer(), F.main(this.GetName(), "You cannot use " + F.item("Throwing TNT") + " here."));
            return;
        }
        UtilInv.remove(player, Material.TNT, (byte)0, (byte)1);
        UtilInv.Update((Entity)player);
        final TNTPrimed tnt = (TNTPrimed)player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), (Class)TNTPrimed.class);
        if (this._fuse != -1) {
            tnt.setFuseTicks(this._fuse);
        }
        UtilAction.velocity((Entity)tnt, player.getLocation().getDirection(), 0.5, false, 0.0, 0.1, 10.0, false);
        this._tntMap.put((Entity)tnt, player);
    }
    
    @EventHandler
    public void ExplosionPrime(final ExplosionPrimeEvent event) {
        if (!this._tntMap.containsKey(event.getEntity())) {
            return;
        }
        final Player player = this._tntMap.remove(event.getEntity());
        for (final Player other : UtilPlayer.getNearby(event.getEntity().getLocation(), 14.0)) {
            this.Manager.GetBuffer().Factory().Explosion("Throwing TNT", (LivingEntity)other, (LivingEntity)player, 50, 0.1, false, false);
        }
    }
}