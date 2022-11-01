package server.instance.core.projectile;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public interface IThrown
{
  void Collide(LivingEntity paramLivingEntity, Block paramBlock, ProjectileUser paramProjectileUser);

  void Idle(ProjectileUser paramProjectileUser);

  void Expire(ProjectileUser paramProjectileUser);
}