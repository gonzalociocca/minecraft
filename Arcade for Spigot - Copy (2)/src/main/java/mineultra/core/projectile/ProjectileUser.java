package mineultra.core.projectile;

import java.util.Iterator;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilParticle;
import mineultra.core.common.util.UtilParticle.PCType;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MovingObjectPosition;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ProjectileUser
{
  public ProjectileManager Throw;
  private org.bukkit.entity.Entity _thrown;
  private LivingEntity _thrower;
  private IThrown _callback;
  private long _expireTime;
  private boolean _hitPlayer = false;
  private boolean _hitBlock = false;
  private boolean _idle = false;
  private boolean _pickup = false;

  private Sound _sound = null;
  private float _soundVolume = 1.0F;
  private float _soundPitch = 1.0F;
  private PCType _particle = null;
  private Effect _effect = null;
  private int _effectData = 0;
  private UpdateType _effectRate = UpdateType.TICK;

  public ProjectileUser(ProjectileManager throwInput, org.bukkit.entity.Entity thrown, LivingEntity thrower, IThrown callback, long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, boolean pickup, Sound sound, float soundVolume, float soundPitch, Effect effect, int effectData, UpdateType effectRate, PCType particle, double hitboxMult)
  {
    Throw = throwInput;

    _thrown = thrown;
    _thrower = thrower;
    _callback = callback;

    _expireTime = expireTime;
    _hitPlayer = hitPlayer;
    _hitBlock = hitBlock;
    _idle = idle;
    _pickup = pickup;

    _sound = sound;
    _soundVolume = soundVolume;
    _soundPitch = soundPitch;
    _particle = particle;
    _effect = effect;
    _effectData = effectData;
    _effectRate = effectRate;
  }

  public void Effect(UpdateEvent event)
  {
    if (event.getType() != _effectRate) {
      return;
    }
    if (_sound != null) {
      _thrown.getWorld().playSound(_thrown.getLocation(), _sound, _soundVolume, _soundPitch);
    }
    if (_effect != null) {
      _thrown.getWorld().playEffect(_thrown.getLocation(), _effect, _effectData);
    }
    if (_particle != null)
      UtilParticle.PlayParticle(_particle, _thrown.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
  }

  public boolean Collision()
  {
    /*if ((_expireTime != -1L) && (System.currentTimeMillis() > _expireTime))
    {
      _callback.Expire(this);
      return true;
    }

    double distanceToEntity = 0.0D;
    LivingEntity victim = null;

    net.minecraft.server.v1_8_R2.Entity nmsEntity = ((CraftEntity)_thrown).getHandle();
    Vec3D vec3d = Vec3D.a(nmsEntity.locX, nmsEntity.locY, nmsEntity.locZ);
    Vec3D vec3d1 = Vec3D.a(nmsEntity.locX + nmsEntity.motX, nmsEntity.locY + nmsEntity.motY, nmsEntity.locZ + nmsEntity.motZ);

    MovingObjectPosition finalObjectPosition = nmsEntity.world.rayTrace(vec3d, vec3d1, false, true, false);
    vec3d = Vec3D.a(nmsEntity.locX, nmsEntity.locY, nmsEntity.locZ);
    vec3d1 = Vec3D.a(nmsEntity.locX + nmsEntity.motX, nmsEntity.locY + nmsEntity.motY, nmsEntity.locZ + nmsEntity.motZ);

    if (finalObjectPosition != null)
    {
      vec3d1 = Vec3D.a(finalObjectPosition.pos.a, finalObjectPosition.pos.b, finalObjectPosition.pos.c);
    }

    for (Iterator localIterator = ((CraftWorld)_thrown.getWorld()).getHandle().getEntities(((CraftEntity)_thrown).getHandle(), ((CraftEntity)_thrown).getHandle().boundingBox.a(((CraftEntity)_thrown).getHandle().motX, ((CraftEntity)_thrown).getHandle().motY, ((CraftEntity)_thrown).getHandle().motZ).grow(1.0D, 1.0D, 1.0D)).iterator(); localIterator.hasNext(); ) { Object entity = localIterator.next();

      if ((entity instanceof net.minecraft.server.v1_8_R2.Entity))
      {
        org.bukkit.entity.Entity bukkitEntity = ((net.minecraft.server.v1_8_R2.Entity)entity).getBukkitEntity();

        if ((bukkitEntity instanceof LivingEntity))
        {
          LivingEntity ent = (LivingEntity)bukkitEntity;

          if (!ent.equals(_thrower))
          {
            if ((!(ent instanceof Player)) || 
              (((Player)ent).getGameMode() != GameMode.CREATIVE))
            {
              float f1 = (float)(nmsEntity.getBoundingBox().a() * 0.6D);
              AxisAlignedBB axisalignedbb1 = ((CraftEntity)ent).getHandle().getBoundingBox().grow(f1, f1, f1);
              MovingObjectPosition entityCollisionPosition = axisalignedbb1.a(vec3d, vec3d1);

              if (entityCollisionPosition != null)
              {
                double d1 = vec3d.distanceSquared(entityCollisionPosition.pos);
                if ((d1 < distanceToEntity) || (distanceToEntity == 0.0D))
                {
                  victim = ent;
                  distanceToEntity = d1;
                }
              }
            }
          }
        }
      } }
    if (victim != null)
    {
      finalObjectPosition = new MovingObjectPosition(((CraftLivingEntity)victim).getHandle());

      _callback.Collide(victim, null, this);
      return true;
    }

    if (finalObjectPosition != null)
    {
      if (_hitBlock)
      {
        Block block = _thrown.getWorld().getBlockAt(finalObjectPosition.b, finalObjectPosition.c, finalObjectPosition.d);
        if ((!UtilBlock.airFoliage(block)) && (!block.isLiquid()))
        {
          nmsEntity.motX = ((float)(finalObjectPosition.pos.a - nmsEntity.locX));
          nmsEntity.motY = ((float)(finalObjectPosition.pos.b - nmsEntity.locY));
          nmsEntity.motZ = ((float)(finalObjectPosition.pos.c - nmsEntity.locZ));
          float f2 = MathHelper.sqrt(nmsEntity.motX * nmsEntity.motX + nmsEntity.motY * nmsEntity.motY + nmsEntity.motZ * nmsEntity.motZ);
          nmsEntity.locX -= nmsEntity.motX / f2 * 0.0500000007450581D;
          nmsEntity.locY -= nmsEntity.motY / f2 * 0.0500000007450581D;
          nmsEntity.locZ -= nmsEntity.motZ / f2 * 0.0500000007450581D;

          _callback.Collide(null, block, this);
          return true;
        }
      }

    }

    try
    {
      if (_idle)
      {
        if ((_thrown.getVelocity().length() < 0.2D) && 
          (!UtilBlock.airFoliage(_thrown.getLocation().getBlock().getRelative(BlockFace.DOWN))))
        {
          _callback.Idle(this);
          return true;
        }
      }
    }
    catch (Exception ex)
    {
      if (_hitBlock)
      {
        return true;
      }

      if (_idle)
      {
        return true;
      }
    }
*/
    return false;
  }

  public LivingEntity GetThrower()
  {
    return _thrower;
  }

  public org.bukkit.entity.Entity GetThrown()
  {
    return _thrown;
  }

  public boolean CanPickup(LivingEntity thrower)
  {
    if (!thrower.equals(_thrower)) {
      return false;
    }
    return _pickup;
  }
}