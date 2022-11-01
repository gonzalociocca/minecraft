package server.instance.core.buffer;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;
import server.instance.GameServer;
import server.instance.core.buffer.events.BufferApplyEvent;
import server.instance.core.damage.CustomDamageEvent;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.util.F;
import server.util.UtilEnt;
import server.util.UtilPlayer;
import server.util.UtilTime;

import java.util.*;

public class GameBuffer {
    private BufferFactory _factory;
    private BufferApplicator _applicator;
    protected BufferEffect Effect;
    private WeakHashMap<LivingEntity, LinkedList<Buffer>> _Buffers;
    private WeakHashMap<LivingEntity, LinkedList<BufferActive>> _activeBuffers;
    private HashSet<Entity> _items;

    public GameBuffer() {

        this._Buffers = new WeakHashMap<LivingEntity, LinkedList<Buffer>>();
        this._activeBuffers = new WeakHashMap<LivingEntity, LinkedList<BufferActive>>();
        this._items = new HashSet<Entity>();
        this.factory();
        this.applicator();
        this.effect();
    }

    public WeakHashMap<LivingEntity, LinkedList<Buffer>> getBuffers() {
        return _Buffers;
    }

    public WeakHashMap<LivingEntity, LinkedList<BufferActive>> getActiveBuffers() {
        return _activeBuffers;
    }

    public HashSet<Entity> getItems() {
        return _items;
    }

    public BufferFactory factory() {
        if (this._factory == null) {
            this._factory = new BufferFactory(this);
        }
        return this._factory;
    }

    public BufferApplicator applicator() {
        if (this._applicator == null) {
            this._applicator = new BufferApplicator();
        }
        return this._applicator;
    }

    public BufferEffect effect() {
        if (this.Effect == null) {
            this.Effect = new BufferEffect(this);
        }
        return this.Effect;
    }

    public Buffer addBuffer(GameServer game, final Buffer newCon) {
        final BufferApplyEvent condEvent = new BufferApplyEvent(newCon);
        Bukkit.getServer().getPluginManager().callEvent(condEvent);
        if (condEvent.isCancelled()) {
            return null;
        }
        if (!this._Buffers.containsKey(newCon.GetEnt())) {
            this._Buffers.put(newCon.GetEnt(), new LinkedList<Buffer>());
        }
        this._Buffers.get(newCon.GetEnt()).add(newCon);
        newCon.OnBufferAdd();
        this.handleIndicator(game, newCon);
        return newCon;
    }

    public void handleIndicator(GameServer game, final Buffer newCon) {
        final BufferActive ind = this.getIndicatorType(newCon);
        if (ind == null) {
            this.addIndicator(game, newCon);
        } else {
            this.updateActive(game, ind, newCon);
        }
    }

    public BufferActive getIndicatorType(final Buffer newCon) {
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

    public void addIndicator(GameServer game, final Buffer newCon) {
        final BufferActive newInd = new BufferActive(game, newCon);
        if (!this._activeBuffers.containsKey(newCon.GetEnt())) {
            this._activeBuffers.put(newCon.GetEnt(), new LinkedList<BufferActive>());
        }
        final LinkedList<BufferActive> entInds = this._activeBuffers.get(newCon.GetEnt());
        entInds.addFirst(newInd);
        if (newCon.GetInformOn() != null) {
            UtilPlayer.message(newCon.GetEnt(), F.main("buffer", newCon.GetInformOn()));
        }
    }

    public void updateActive(GameServer game, final BufferActive active, final Buffer newCon) {
        if (!active.GetBuffer().IsExpired() && active.GetBuffer().IsBetterOrEqual(newCon, newCon.IsAdd())) {
            return;
        }
        active.SetBuffer(game, newCon);
    }

    public Buffer getBestBuffer(final LivingEntity ent, final Buffer.BufferType type) {
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
            } else {
                if (!con.IsBetterOrEqual(best, false)) {
                    continue;
                }
                best = con;
            }
        }
        return best;
    }

    public Buffer getActiveBuffer(final LivingEntity ent, final Buffer.BufferType type) {
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

    public void clean(final LivingEntity ent) {
        if (ent != null) {
            LinkedList<Buffer> list1 = _Buffers.get(ent);
            LinkedList<BufferActive> list2 = _activeBuffers.get(ent);

            if (list1 != null && !list1.isEmpty())
                for (Buffer buffer : list1) {
                    buffer.Expire();
                }

            if (list2 != null && !list2.isEmpty())
                for (BufferActive bufferActive : list2) {
                    bufferActive.GetBuffer().Expire();
                }

            this._Buffers.remove(ent);
            this._activeBuffers.remove(ent);
        }
    }

    public void endBuffer(GameServer game, final LivingEntity target, final Buffer.BufferType type, final String reason) {
        if (!this._Buffers.containsKey(target)) {
            return;
        }
        for (final Buffer cond : this._Buffers.get(target)) {
            if ((reason == null || cond.GetReason().equals(reason)) && (type == null || cond.GetType() == type)) {
                cond.Expire();
                final Buffer best = this.getBestBuffer(target, cond.GetType());
                if (best == null) {
                    continue;
                }
                best.Apply(game);
            }
        }
    }

    public boolean hasBuffer(final LivingEntity target, final Buffer.BufferType type, final String reason) {
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

    public boolean isSilenced(final LivingEntity ent, final String ability) {
        if (!this._activeBuffers.containsKey(ent)) {
            return false;
        }

        return false;
    }

    public boolean isInvulnerable(final LivingEntity ent) {
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

    public boolean isCloaked(final LivingEntity ent) {
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

    public void cleanAll() {
        for (LinkedList<BufferActive> list : _activeBuffers.values()) {
            for (BufferActive active : list) {
                active.GetBuffer().Expire();
            }
        }
        for (LinkedList<Buffer> list : _Buffers.values()) {
            for (Buffer buff : list) {
                buff.Expire();
            }
        }
        _activeBuffers.clear();
        _Buffers.clear();
        _items.clear();
    }

    public void onUpdate(GameServer game, UpdateEvent event) {
        if (event.getType() == UpdateType.TICK) {
            if (!getBuffers().isEmpty()) {
                for (Iterator<Map.Entry<LivingEntity, LinkedList<Buffer>>> it = getBuffers().entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<LivingEntity, LinkedList<Buffer>> entry = it.next();
                    LinkedList<Buffer> value = entry.getValue();
                    if (!value.isEmpty()) {
                        for (Iterator<Buffer> it2 = value.iterator(); it2.hasNext(); ) {
                            Buffer buff = it2.next();
                            if (buff.Tick()) {
                                it2.remove();
                            }
                        }
                    }
                }
            }
            if (!getActiveBuffers().isEmpty()) {
                for (Iterator<Map.Entry<LivingEntity, LinkedList<BufferActive>>> it = getActiveBuffers().entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<LivingEntity, LinkedList<BufferActive>> entry = it.next();
                    LivingEntity ent = entry.getKey();
                    LinkedList<BufferActive> value = entry.getValue();
                    if (!value.isEmpty()) {
                        for (Iterator<BufferActive> it2 = value.iterator(); it2.hasNext(); ) {
                            BufferActive buff = it2.next();
                            if (buff.GetBuffer().IsExpired()) {
                                Buffer replacement = getBestBuffer(ent, buff.GetBuffer().GetType());
                                if (replacement == null) {
                                    it2.remove();
                                    if (buff.GetBuffer().GetInformOff() != null) {
                                        UtilPlayer.message(buff.GetBuffer().GetEnt(), F.main("buffer", buff.GetBuffer().GetInformOff()));
                                    }
                                } else {
                                    updateActive(game, buff, replacement);
                                }
                            }
                            if (buff.GetBuffer().GetType() == Buffer.BufferType.SHOCK) {
                                ent.playEffect(EntityEffect.HURT);
                            }
                        }
                    }
                }
            }
            if (!getItems().isEmpty()) {
                for (Iterator<Entity> it = getItems().iterator(); it.hasNext(); ) {
                    Entity next = it.next();
                    if (UtilEnt.isGrounded(next) || next.isDead() || !next.isValid()) {
                        it.remove();
                    }
                }
            }
        } else if (event.getType() == UpdateType.FAST) {

            if (!getActiveBuffers().isEmpty()) {
                for (Iterator<Map.Entry<LivingEntity, LinkedList<BufferActive>>> it = getActiveBuffers().entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<LivingEntity, LinkedList<BufferActive>> entry = it.next();
                    LivingEntity ent = entry.getKey();
                    LinkedList<BufferActive> value = entry.getValue();

                    if (ent.isDead() || !ent.isValid() || (ent instanceof Player && !((Player) ent).isOnline())) {
                        ent.remove();
                        it.remove();
                        continue;
                    }
                    boolean hasCloack = false;
                    boolean isGrounded = UtilEnt.isGrounded(ent);
                    if (!value.isEmpty()) {
                        for (BufferActive active : value) {
                            Buffer buff = active.GetBuffer();
                            if (buff.GetType() == Buffer.BufferType.CLOAK) {
                                hasCloack = true;
                            }
                            if (isGrounded) {
                                if (buff.GetType() == Buffer.BufferType.FALLING) {
                                    if (buff != null) {
                                        if (UtilTime.elapsed(buff.GetTime(), 250L)) {
                                            endBuffer(game, ent, Buffer.BufferType.FALLING, null);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (ent.getFireTicks() <= 0) {
                        endBuffer(game, ent, Buffer.BufferType.BURNING, null);
                    }
                    if (!hasCloack && !ent.isDead() && ent.hasPotionEffect(PotionEffectType.WITHER)) {
                        ent.getWorld().playEffect(ent.getLocation(), org.bukkit.Effect.SMOKE, 1);
                        ent.getWorld().playEffect(ent.getLocation(), org.bukkit.Effect.SMOKE, 3);
                        ent.getWorld().playEffect(ent.getLocation(), org.bukkit.Effect.SMOKE, 5);
                        ent.getWorld().playEffect(ent.getLocation(), org.bukkit.Effect.SMOKE, 7);
                    }
                }
            }

            if (!getBuffers().isEmpty()) {
                for (Iterator<Map.Entry<LivingEntity, LinkedList<Buffer>>> it = getBuffers().entrySet().iterator(); it.hasNext(); ) {
                    {
                        Map.Entry<LivingEntity, LinkedList<Buffer>> entry2 = it.next();
                        LivingEntity ent2 = entry2.getKey();
                        if (ent2.isDead() || !ent2.isValid() || (ent2 instanceof Player && !((Player) ent2).isOnline())) {
                            ent2.remove();
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {
        clean(event.getPlayer());
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        clean(event.getPlayer());
    }

    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() == null || event.getEntity() instanceof Player && event.getEntity().getHealth() > 0.0) {
            return;
        }
        clean(event.getEntity());
    }

    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (!event.isCancelled()) {
            if (getItems().contains(event.getItem())) {
                event.setCancelled(true);
            }
        }
    }

    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (!event.isCancelled()) {
            if (getItems().contains(event.getItem())) {
                event.setCancelled(true);
            }
        }
    }

    public void onEntityTarget(EntityTargetEvent event){
        if (!event.isCancelled() && event.getTarget() instanceof Player) {
            if (hasBuffer((LivingEntity) event.getTarget(), Buffer.BufferType.CLOAK, null)) {
                event.setCancelled(true);
            }
        }
    }

    public void onCustomDamage(CustomDamageEvent event){
        if (event.isCancelled()) {
            return;
        }
        final LivingEntity ent = event.getDamageeEntity();
        final Player damagee = event.getDamageePlayer();
        final Player damager = event.getDamagerPlayer(false);

        if (ent != null && isInvulnerable(ent)) {
            event.setCancelled("Invulnerable");
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.WITHER) {
            event.setCancelled("Vulnerability Wither");
        } else if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (ent != null) {
                if (isCloaked(ent)) {
                    event.setCancelled("Cloak");
                }
            }
            if (damagee != null) {
                if (damagee.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                    Buffer buff = getActiveBuffer(damagee, Buffer.BufferType.DAMAGE_RESISTANCE);
                    if (buff != null) {
                        event.addMod(UtilEnt.getName(buff.GetSource()), buff.GetReason(), -1 * (buff.GetMult() + 1), false);
                    }
                }
                if (damagee.hasPotionEffect(PotionEffectType.WITHER)) {
                    Buffer buff = getActiveBuffer(damagee, Buffer.BufferType.WITHER);
                    if (buff != null) {
                        event.addMod(UtilEnt.getName(buff.GetSource()), buff.GetReason(), buff.GetMult() + 1, false);
                    }
                }
            }
            if (damager != null) {
                if (damager.hasPotionEffect(PotionEffectType.WITHER)) {
                    Buffer buff = getActiveBuffer(damager, Buffer.BufferType.WITHER);
                    if (buff != null) {
                        event.addMod(UtilEnt.getName(buff.GetSource()), buff.GetReason(), -1 * (buff.GetMult() + 1), false);
                    }
                }
                if (damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                    Buffer buff = getActiveBuffer(damager, Buffer.BufferType.INCREASE_DAMAGE);
                    if (buff != null) {
                        event.addMod(damager.getName(), buff.GetReason(), buff.GetMult() + 1, true);
                    }
                }
            }
        } else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            if (ent != null) {
                if (ent.getFireTicks() > 160) {
                    ent.setFireTicks(160);
                }
                Buffer buff = getActiveBuffer(ent, Buffer.BufferType.BURNING);
                if (buff != null) {
                    event.setDamager(buff.GetSource());
                    event.addMod(UtilEnt.getName(buff.GetSource()), buff.GetReason(), 0.0, true);
                    event.setIgnoreArmor(true);
                    event.setKnockback(false);
                }
            }
        } else if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            if (ent != null) {
                Buffer buff = getActiveBuffer(ent, Buffer.BufferType.EXPLOSION);
                if (buff != null) {
                    event.setDamager(buff.GetSource());
                    event.addMod("Negate", buff.GetReason(), -event.getDamageInitial(), false);
                    event.addMod(UtilEnt.getName(buff.GetSource()), buff.GetReason(), Math.min(event.getDamageInitial(), buff.GetMult()), true);
                    event.setKnockback(false);
                }
            }
        } else if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            if (ent != null) {
                Buffer buff = getActiveBuffer(ent, Buffer.BufferType.LIGHTNING);
                if (buff != null) {
                    event.setDamager(buff.GetSource());
                    event.addMod(UtilEnt.getName(buff.GetSource()), buff.GetReason(), 0.0, true);
                    if (buff.GetMult() != 0) {
                        event.addMod("Lightning Modifier", UtilEnt.getName(buff.GetSource()), buff.GetMult(), false);
                    }
                    event.setKnockback(false);
                }
            }
        } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (ent != null) {
                Buffer buff = getActiveBuffer(ent, Buffer.BufferType.FALLING);
                if (buff != null) {
                    event.setDamager(buff.GetSource());
                    event.addMod(UtilEnt.getName(buff.GetSource()), buff.GetReason(), 0.0, true);
                    event.setIgnoreArmor(true);
                    event.setKnockback(false);
                }
            }
        } else if (event.getCause() == EntityDamageEvent.DamageCause.POISON) {
            if (ent != null) {
                Buffer buff = getActiveBuffer(ent, Buffer.BufferType.POISON);
                if (buff != null) {
                    event.setDamager(buff.GetSource());
                    event.addMod(UtilEnt.getName(buff.GetSource()), buff.GetReason(), 0.0, true);
                    event.setIgnoreArmor(true);
                    event.setKnockback(false);
                }
            }
        }
    }

}
