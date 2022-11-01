package mineultra.minecraft.game.core.Buffer;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.Map;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import mineultra.core.common.util.C;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Entity;
import java.util.HashSet;
import java.util.LinkedList;
import org.bukkit.entity.LivingEntity;
import java.util.WeakHashMap;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.Buffer.events.BufferApplyEvent;

public class BufferManager extends MiniPlugin
{
    private BufferFactory _factory;
    private BufferApplicator _applicator;
    protected BufferEffect Effect;
    private WeakHashMap<LivingEntity, LinkedList<Buffer>> _Buffers;
    private WeakHashMap<LivingEntity, LinkedList<BufferActive>> _activeBuffers;
    private HashSet<Entity> _items;
    
    public BufferManager(final JavaPlugin plugin) {
        super("Buffer Manager", plugin);
        this._Buffers = new WeakHashMap<LivingEntity, LinkedList<Buffer>>();
        this._activeBuffers = new WeakHashMap<LivingEntity, LinkedList<BufferActive>>();
        this._items = new HashSet<Entity>();
        this.Factory();
        this.Applicator();
        this.Effect();
    }
    
    public BufferFactory Factory() {
        if (this._factory == null) {
            this._factory = new BufferFactory(this);
        }
        return this._factory;
    }
    
    public BufferApplicator Applicator() {
        if (this._applicator == null) {
            this._applicator = new BufferApplicator();
        }
        return this._applicator;
    }
    
    public BufferEffect Effect() {
        if (this.Effect == null) {
            this.Effect = new BufferEffect(this);
        }
        return this.Effect;
    }
    
    public Buffer AddBuffer(final Buffer newCon) {
        final BufferApplyEvent condEvent = new BufferApplyEvent(newCon);
        this.GetPlugin().getServer().getPluginManager().callEvent((Event)condEvent);
        if (condEvent.isCancelled()) {
            return null;
        }
        if (!this._Buffers.containsKey(newCon.GetEnt())) {
            this._Buffers.put(newCon.GetEnt(), new LinkedList<Buffer>());
        }
        this._Buffers.get(newCon.GetEnt()).add(newCon);
        newCon.OnBufferAdd();
        this.HandleIndicator(newCon);
        return newCon;
    }
    
    public void HandleIndicator(final Buffer newCon) {
        final BufferActive ind = this.GetIndicatorType(newCon);
        if (ind == null) {
            this.AddIndicator(newCon);
        }
        else {
            this.UpdateActive(ind, newCon);
        }
    }
    
    public BufferActive GetIndicatorType(final Buffer newCon) {
        if (!this._activeBuffers.containsKey(newCon.GetEnt())) {
            this._activeBuffers.put(newCon.GetEnt(), new LinkedList<BufferActive>());
        }
        for (final BufferActive ind : this._activeBuffers.get(newCon.GetEnt())) {
            if (ind.GetBuffer().GetType() == newCon.GetType()) {
                return ind;
            }
        }
        return null;
    }
    
    public void AddIndicator(final Buffer newCon) {
        final BufferActive newInd = new BufferActive(newCon);
        if (!this._activeBuffers.containsKey(newCon.GetEnt())) {
            this._activeBuffers.put(newCon.GetEnt(), new LinkedList<BufferActive>());
        }
        final LinkedList<BufferActive> entInds = this._activeBuffers.get(newCon.GetEnt());
        entInds.addFirst(newInd);
        if (newCon.GetInformOn() != null) {
            UtilPlayer.message((Entity)newCon.GetEnt(), F.main("Buffer", newCon.GetInformOn()));
        }
    }
    
    public void UpdateActive(final BufferActive active, final Buffer newCon) {
        if (!active.GetBuffer().IsExpired() && active.GetBuffer().IsBetterOrEqual(newCon, newCon.IsAdd())) {
            return;
        }
        active.SetBuffer(newCon);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void ExpireBuffers(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        for (final LivingEntity ent : this._Buffers.keySet()) {
            final Iterator<Buffer> BufferIterator = this._Buffers.get(ent).iterator();
            while (BufferIterator.hasNext()) {
                final Buffer cond = BufferIterator.next();
                if (cond.Tick()) {
                    BufferIterator.remove();
                }
            }
        }
        for (final LivingEntity ent : this._activeBuffers.keySet()) {
            final Iterator<BufferActive> BufferIndicatorIterator = this._activeBuffers.get(ent).iterator();
            while (BufferIndicatorIterator.hasNext()) {
                final BufferActive BufferIndicator = BufferIndicatorIterator.next();
                if (BufferIndicator.GetBuffer().IsExpired()) {
                    final Buffer replacement = this.GetBestBuffer(ent, BufferIndicator.GetBuffer().GetType());
                    if (replacement == null) {
                        BufferIndicatorIterator.remove();
                        if (BufferIndicator.GetBuffer().GetInformOff() == null) {
                            continue;
                        }
                        UtilPlayer.message((Entity)BufferIndicator.GetBuffer().GetEnt(), F.main("Buffer", BufferIndicator.GetBuffer().GetInformOff()));
                    }
                    else {
                        this.UpdateActive(BufferIndicator, replacement);
                    }
                }
            }
        }
    }
    
    public Buffer GetBestBuffer(final LivingEntity ent, final Buffer.BufferType type) {
        if (!this._Buffers.containsKey(ent)) {
            return null;
        }
        Buffer best = null;
        for (final Buffer con : this._Buffers.get(ent)) {
            if (con.GetType() != type) {
                continue;
            }
            if (con.IsExpired()) {
                continue;
            }
            if (best == null) {
                best = con;
            }
            else {
                if (!con.IsBetterOrEqual(best, false)) {
                    continue;
                }
                best = con;
            }
        }
        return best;
    }
    
    public mineultra.minecraft.game.core.Buffer.Buffer GetActiveBuffer(final LivingEntity ent, final Buffer.BufferType type) {
        if (!this._activeBuffers.containsKey(ent)) {
            return null;
        }
        for (final BufferActive ind : this._activeBuffers.get(ent)) {
            if (ind.GetBuffer().GetType() != type) {
                continue;
            }
            if (ind.GetBuffer().IsExpired()) {
                continue;
            }
            return ind.GetBuffer();
        }
        return null;
    }
    
    @EventHandler
    public void Remove(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final HashSet<Entity> expired = new HashSet<Entity>();
        for (final Entity cur : this._items) {
            if (UtilEnt.isGrounded(cur) || cur.isDead() || !cur.isValid()) {
                expired.add(cur);
            }
        }
        for (final Entity cur : expired) {
            this._items.remove(cur);
            cur.remove();
        }
    }
    
    @EventHandler
    public void Respawn(final PlayerRespawnEvent event) {
        this.Clean((LivingEntity)event.getPlayer());
    }
    
    @EventHandler
    public void Quit(final PlayerQuitEvent event) {
        this.Clean((LivingEntity)event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void Death(final EntityDeathEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().getHealth() > 0.0) {
            return;
        }
        this.Clean(event.getEntity());
    }
    
    public void Clean(final LivingEntity ent) {
        this._Buffers.remove(ent);
        this._activeBuffers.remove(ent);
    }
    
    @EventHandler
    public void Debug(final UpdateEvent event) {
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        for (final LivingEntity ent : this._activeBuffers.keySet()) {
            if (!(ent instanceof Player)) {
                continue;
            }
            final Player player = (Player)ent;
            if (player.getItemInHand() == null) {
                continue;
            }
            if (player.getItemInHand().getType() != Material.PAPER) {
                continue;
            }
            if (!player.isOp()) {
                continue;
            }
            UtilPlayer.message((Entity)player, String.valueOf(C.cGray) + this._activeBuffers.get(ent).size() + " Indicators ----------- " + this._Buffers.get(ent).size() + " Buffers");
            for (final BufferActive ind : this._activeBuffers.get(ent)) {
                UtilPlayer.message((Entity)player, String.valueOf(F.elem(new StringBuilder().append(ind.GetBuffer().GetType()).append(" ").append(ind.GetBuffer().GetMult() + 1).toString())) + " for " + F.time(UtilTime.convertString(ind.GetBuffer().GetTicks() * 50L, 1, UtilTime.TimeUnit.FIT)) + " via " + F.skill(ind.GetBuffer().GetReason()) + " from " + F.name(UtilEnt.getName((Entity)ind.GetBuffer().GetSource())) + ".");
            }
        }
    }
    
    @EventHandler
    public void Pickup(final PlayerPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (this._items.contains(event.getItem())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void HopperPickup(final InventoryPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (this._items.contains(event.getItem())) {
            event.setCancelled(true);
        }
    }
    
    public void EndBuffer(final LivingEntity target, final Buffer.BufferType type, final String reason) {
        if (!this._Buffers.containsKey(target)) {
            return;
        }
        for (final Buffer cond : this._Buffers.get(target)) {
            if ((reason == null || cond.GetReason().equals(reason)) && (type == null || cond.GetType() == type)) {
                cond.Expire();
                final Buffer best = this.GetBestBuffer(target, cond.GetType());
                if (best == null) {
                    continue;
                }
                best.Apply();
            }
        }
    }
    
    public boolean HasBuffer(final LivingEntity target, final Buffer.BufferType type, final String reason) {
        if (!this._Buffers.containsKey(target)) {
            return false;
        }
        for (final Buffer cond : this._Buffers.get(target)) {
            if ((reason == null || cond.GetReason().equals(reason)) && (type == null || cond.GetType() == type)) {
                return true;
            }
        }
        return false;
    }
    
    public WeakHashMap<LivingEntity, LinkedList<BufferActive>> GetActiveBuffers() {
        return this._activeBuffers;
    }
    
    public boolean IsSilenced(final LivingEntity ent, final String ability) {
        if (!this._activeBuffers.containsKey(ent)) {
            return false;
        }
        for (final BufferActive ind : this._activeBuffers.get(ent)) {
            if (ind.GetBuffer().GetType() == Buffer.BufferType.SILENCE) {
                if (ability != null && ent instanceof Player && Recharge.Instance.use((Player)ent, "Silence Feedback", 200L, false, false)) {
                    UtilPlayer.message((Entity)ent, F.main("Buffer", "Cannot use " + F.skill(ability) + " while silenced."));
                    ((Player)ent).playSound(ent.getLocation(), Sound.ENTITY_BAT_HURT, 0.8f, 0.8f);
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean IsInvulnerable(final LivingEntity ent) {
        if (!this._activeBuffers.containsKey(ent)) {
            return false;
        }
        for (final BufferActive ind : this._activeBuffers.get(ent)) {
            if (ind.GetBuffer().GetType() == Buffer.BufferType.INVULNERABLE) {
                return true;
            }
        }
        return false;
    }
    
    public boolean IsCloaked(final LivingEntity ent) {
        if (!this._activeBuffers.containsKey(ent)) {
            return false;
        }
        for (final BufferActive ind : this._activeBuffers.get(ent)) {
            if (ind.GetBuffer().GetType() == Buffer.BufferType.CLOAK) {
                return true;
            }
        }
        return false;
    }
    
    @EventHandler
    public void CleanUpdate(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        final Iterator<Map.Entry<LivingEntity, LinkedList<BufferActive>>> BufferIndIterator = this._activeBuffers.entrySet().iterator();
        while (BufferIndIterator.hasNext()) {
            final Map.Entry<LivingEntity, LinkedList<BufferActive>> entry = BufferIndIterator.next();
            final LivingEntity ent = entry.getKey();
            if (ent.isDead() || !ent.isValid() || (ent instanceof Player && !((Player)ent).isOnline())) {
                ent.remove();
                BufferIndIterator.remove();
            }
        }
        final Iterator<Map.Entry<LivingEntity, LinkedList<Buffer>>> BufferIterator = this._Buffers.entrySet().iterator();
        while (BufferIterator.hasNext()) {
            final Map.Entry<LivingEntity, LinkedList<Buffer>> entry2 = BufferIterator.next();
            final LivingEntity ent2 = entry2.getKey();
            if (ent2.isDead() || !ent2.isValid() || (ent2 instanceof Player && !((Player)ent2).isOnline())) {
                ent2.remove();
                BufferIterator.remove();
            }
        }
    }
}
