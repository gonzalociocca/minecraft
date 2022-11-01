package server.instance.core.projectile;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import server.instance.GameServer;
import server.instance.core.damage.CustomDamageEvent;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.util.UtilParticle;

import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Created by noname on 11/4/2017.
 */
public class GameProjectiles {

    private WeakHashMap<Entity, ProjectileUser> _thrown = new WeakHashMap();

    public void cleanAll(){
        _thrown.clear();
    }

    public WeakHashMap<Entity, ProjectileUser> getThrownProjectiles(){
        return _thrown;
    }


    public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback,
                         long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, float hitboxGrow)
    {
        _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback,
                expireTime, hitPlayer, hitBlock, idle, false,
                null, 1f, 1f, null, 0, null, null, 0F, 0F, 0F, 0F, 1, hitboxGrow));
    }

    public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback,
                         long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, boolean pickup, float hitboxGrow)
    {
        _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback,
                expireTime, hitPlayer, hitBlock, idle, pickup,
                null, 1f, 1f, null, 0, null, null, 0F, 0F, 0F, 0F, 1, hitboxGrow));
    }

    public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback,
                         long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle,
                         Sound sound, float soundVolume, float soundPitch, Effect effect, int effectData, UpdateType effectRate , float hitboxGrow)
    {
        _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback,
                expireTime, hitPlayer, hitBlock, idle, false,
                sound, soundVolume, soundPitch, effect, effectData, effectRate, null, 0F, 0F, 0F, 0F, 1, hitboxGrow));
    }

    public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback,
                         long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle,
                         Sound sound, float soundVolume, float soundPitch, UtilParticle.ParticleType particle, Effect effect, int effectData, UpdateType effectRate, float hitboxGrow)
    {
        _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback,
                expireTime, hitPlayer, hitBlock, idle, false,
                sound, soundVolume, soundPitch, effect, effectData, effectRate, particle, 0F, 0F, 0F, 0F, 1, hitboxGrow));
    }

    public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback,
                         long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle,
                         Sound sound, float soundVolume, float soundPitch, UtilParticle.ParticleType particle, UpdateType effectRate, float hitboxMult)
    {
        _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback,
                expireTime, hitPlayer, hitBlock, idle, false,
                sound, soundVolume, soundPitch, null, 0, effectRate, particle, 0F, 0F, 0F, 0F, 1, hitboxMult));
    }

    public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback,
                         long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle,
                         Sound sound, float soundVolume, float soundPitch, UtilParticle.ParticleType particle, float pX, float pY, float pZ, float pS, int pC, UpdateType effectRate, float hitboxMult)
    {
        _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback,
                expireTime, hitPlayer, hitBlock, idle, false,
                sound, soundVolume, soundPitch, null, 0, effectRate, particle, pX, pY, pZ, pS, pC, hitboxMult));
    }

    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (!event.isCancelled() && event.getItem() != null && event.getItem().getLocation() != null) {
            if (getThrownProjectiles().containsKey(event.getItem())) {
                event.setCancelled(true);
            }
        }
    }

    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (!event.isCancelled()) {
            if (getThrownProjectiles().containsKey(event.getItem()) && !getThrownProjectiles().get(event.getItem()).CanPickup(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

    public void onUpdate(GameServer game, UpdateEvent event) {

        if (event.getType() == UpdateType.TICK) {
                if(!getThrownProjectiles().isEmpty()) {
                    for (Iterator<Entity> it = getThrownProjectiles().keySet().iterator(); it.hasNext(); ) {
                        Entity cur = it.next();
                        if ((getThrownProjectiles().get(cur)).Collision(game)) {
                            it.remove();
                        } else if ((cur.isDead()) || (!cur.isValid())) {
                            it.remove();
                        }
                    }

                    for (ProjectileUser cur2 : getThrownProjectiles().values()) {
                        cur2.Effect(game, event);
                    }
                }
        }
    }

    public void onCustomDamage(CustomDamageEvent event) {
        Projectile projectile = event.getProjectile();
        if(projectile != null && projectile instanceof Arrow){
            event.addMod("Del", "Arrow Fix", -event.getDamageInitial(), false);
            event.addMod("Add", "Arrow Fix", projectile.getVelocity().length() * 3.0D, false);
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (projectile != null) {
                projectile.remove();
            }
        }
    }
}
