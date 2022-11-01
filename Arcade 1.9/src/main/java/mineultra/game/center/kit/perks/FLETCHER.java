package mineultra.game.center.kit.perks;

import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Iterator;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.GameMode;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.entity.Entity;
import java.util.HashSet;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;

public class FLETCHER extends Perk
{
    private HashSet<Entity> _fletchArrows;
    private int _max;
    private int _time;
    private boolean _remove;
    
    public FLETCHER(final int time, final int max, final boolean remove) {
        super("Fletcher", new String[] { String.valueOf(C.cGray) + "Receive 1 Arrow every " + time + " seconds. Maximum of " + max + "." });
        this._fletchArrows = new HashSet<Entity>();
        this._max = 0;
        this._time = 0;
        this._time = time;
        this._max = max;
        this._remove = remove;
    }
    
    @EventHandler
    public void FletchShootBow(final EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntity();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        for (int i = 0; i <= 8; ++i) {
            if (player.getInventory().getItem(i) != null && UtilInv.IsItem(player.getInventory().getItem(i), Material.ARROW, (byte)1)) {
                this._fletchArrows.add(event.getProjectile());
                return;
            }
        }
    }
    
    @EventHandler
    public void FletchProjectileHit(final ProjectileHitEvent event) {
        if (this._remove && this._fletchArrows.remove(event.getEntity())) {
            event.getEntity().remove();
        }
    }
    
    @EventHandler
    public void Fletch(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player cur = players[i];
            if (cur.getGameMode() == GameMode.SURVIVAL) {
                if (this.Kit.HasKit(cur)) {
                    if (this.Manager.GetGame().IsAlive(cur)) {
                        if (Recharge.Instance.use(cur, this.GetName(), this._time * 1000, false, false)) {
                            if (!UtilInv.contains(cur, Material.ARROW, (byte)1, this._max)) {
                                cur.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(262, (byte)1, 1, F.item("Fletched Arrow")) });
                                cur.playSound(cur.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2.0f, 1.0f);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void FletchDrop(final PlayerDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!UtilInv.IsItem(event.getItemDrop().getItemStack(), Material.ARROW, (byte)1)) {
            return;
        }
        event.setCancelled(true);
        UtilPlayer.message((Entity)event.getPlayer(), F.main(this.GetName(), "You cannot drop " + F.item("Fletched Arrow") + "."));
    }
    
    @EventHandler
    public void FletchDeathRemove(final PlayerDeathEvent event) {
        final HashSet<ItemStack> remove = new HashSet<ItemStack>();
        for (final ItemStack item : event.getDrops()) {
            if (UtilInv.IsItem(item, Material.ARROW, (byte)1)) {
                remove.add(item);
            }
        }
        for (final ItemStack item : remove) {
            event.getDrops().remove(item);
        }
    }
    
    @EventHandler
    public void FletchInvClick(final InventoryClickEvent event) {
        UtilInv.DisallowMovementOf(event, "Fletched Arrow", Material.ARROW, (byte)1, true);
    }
    
    @EventHandler
    public void FletchClean(final UpdateEvent event) {
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        final Iterator<Entity> arrowIterator = this._fletchArrows.iterator();
        while (arrowIterator.hasNext()) {
            final Entity arrow = arrowIterator.next();
            if (arrow.isDead() || !arrow.isValid()) {
                arrowIterator.remove();
            }
        }
    }
}
