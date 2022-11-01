package server.util;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class UtilEnt
{
  private static final HashMap<org.bukkit.entity.Entity, String> _nameMap = new HashMap();
  private static final HashMap<String, EntityType> creatureMap = new HashMap();
  private static Field _goalSelector;
  private static Field _targetSelector;
  private static Field _bsRestrictionGoal;

  public static HashMap<org.bukkit.entity.Entity, String> GetEntityNames()
  {
    return _nameMap;
  }
  
  public static void silence(org.bukkit.entity.Entity entity, boolean silence)
  {

  }

  public static void ghost(org.bukkit.entity.Entity entity, boolean ghost, boolean invisible)
  {
  
    if ((entity instanceof Player))
    {
      Player p = (Player)entity;
WorldServer ghostWorld = ((CraftWorld) p.getWorld()).getHandle();
 
ghostWorld.getScoreboard().getTeam("SPEC").setCanSeeFriendlyInvisibles(true);
    }

    ((CraftEntity)entity).getHandle().setInvisible(invisible);
  }

  public static void Vegetate(org.bukkit.entity.Entity entity)
  {
    Vegetate(entity, false);
  }

  public static void Vegetate(org.bukkit.entity.Entity entity, boolean mute)
  {
      if(entity == null){
    return;
}
    try
    {
      if (_goalSelector == null)
      {
        _goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
        _goalSelector.setAccessible(true);
      }

      if (_targetSelector == null)
      {
        _targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
        _targetSelector.setAccessible(true);
      }

      if ((entity instanceof CraftCreature))
      {
        EntityCreature creature = ((CraftCreature)entity).getHandle();

        if (_bsRestrictionGoal == null)
        {
          _bsRestrictionGoal = EntityCreature.class.getDeclaredField("c");
          _bsRestrictionGoal.setAccessible(true);
        }

        _bsRestrictionGoal.set(creature, new PathfinderGoalMoveTowardsRestriction(creature, 0.0D));
      }

      if ((((CraftEntity)entity).getHandle() instanceof EntityInsentient))
      {
        EntityInsentient creature = (EntityInsentient)((CraftEntity)entity).getHandle();

        PathfinderGoalSelector goalSelector = new PathfinderGoalSelector(((CraftWorld)entity.getWorld()).getHandle().methodProfiler);

        goalSelector.a(7, new PathfinderGoalLookAtPlayer(creature, EntityHuman.class, 6.0F));
        goalSelector.a(7, new PathfinderGoalRandomLookaround(creature));

        _goalSelector.set(creature, goalSelector);
        _targetSelector.set(creature, new PathfinderGoalSelector(((CraftWorld)entity.getWorld()).getHandle().methodProfiler));
      }

      if ((((CraftEntity)entity).getHandle() instanceof EntityBat))
      {
      }

      if ((((CraftEntity)entity).getHandle() instanceof EntityEnderDragon))
      {
        EntityEnderDragon creature = (EntityEnderDragon)((CraftEntity)entity).getHandle();

      }
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchFieldException e)
    {
      e.printStackTrace();
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
  }

  public static String getName(org.bukkit.entity.Entity ent)
  {
    if (ent == null) {
      return "Null";
    }
    if (ent.getType() == EntityType.PLAYER) {
      return ent.getName();
    }
    if (GetEntityNames().containsKey(ent)) {
      return GetEntityNames().get(ent);
    }
    if ((ent instanceof LivingEntity))
    {
      LivingEntity le = (LivingEntity)ent;
      if (le.getCustomName() != null) {
        return le.getCustomName();
      }
    }
    return getName(ent.getType());
  }

  public static String getName(EntityType type)
  {
    for (String cur : creatureMap.keySet()) {
      if (creatureMap.get(cur) == type)
        return cur;
    }
    return type.getName();
  }

  public static boolean isGrounded(org.bukkit.entity.Entity ent)
  {
    if ((ent instanceof CraftEntity)) {
      return ((CraftEntity)ent).getHandle().onGround;
    }
    return UtilBlock.solid(ent.getLocation().getBlock().getRelative(BlockFace.DOWN));
  }

  public static void PlayDamageSound(LivingEntity damagee)
  {
    Sound sound = Sound.HURT_FLESH;

    if (damagee.getType() == EntityType.BAT) sound = Sound.BAT_HURT;
    else if (damagee.getType() == EntityType.BLAZE) sound = Sound.BLAZE_HIT;
    else if (damagee.getType() == EntityType.CAVE_SPIDER) sound = Sound.SPIDER_IDLE;
    else if (damagee.getType() == EntityType.CHICKEN) sound = Sound.CHICKEN_HURT;
    else if (damagee.getType() == EntityType.COW) sound = Sound.COW_HURT;
    else if (damagee.getType() == EntityType.CREEPER) sound = Sound.CREEPER_HISS;
    else if (damagee.getType() == EntityType.ENDER_DRAGON) sound = Sound.ENDERDRAGON_GROWL;
    else if (damagee.getType() == EntityType.ENDERMAN) sound = Sound.ENDERMAN_HIT;
    else if (damagee.getType() == EntityType.GHAST) sound = Sound.GHAST_SCREAM;
    else if (damagee.getType() == EntityType.GIANT) sound = Sound.ZOMBIE_HURT;
    else if (damagee.getType() == EntityType.IRON_GOLEM) sound = Sound.IRONGOLEM_HIT;
    else if (damagee.getType() == EntityType.MAGMA_CUBE) sound = Sound.MAGMACUBE_JUMP;
    else if (damagee.getType() == EntityType.MUSHROOM_COW) sound = Sound.COW_HURT;
    else if (damagee.getType() == EntityType.OCELOT) sound = Sound.CAT_MEOW;
    else if (damagee.getType() == EntityType.PIG) sound = Sound.PIG_IDLE;
    else if (damagee.getType() == EntityType.PIG_ZOMBIE) sound = Sound.ZOMBIE_HURT;
    else if (damagee.getType() == EntityType.SHEEP) sound = Sound.SHEEP_IDLE;
    else if (damagee.getType() == EntityType.SILVERFISH) sound = Sound.SILVERFISH_HIT;
    else if (damagee.getType() == EntityType.SKELETON) sound = Sound.SKELETON_HURT;
    else if (damagee.getType() == EntityType.SLIME) sound = Sound.SLIME_ATTACK;
    else if (damagee.getType() == EntityType.SNOWMAN) sound = Sound.STEP_SNOW;
    else if (damagee.getType() == EntityType.SPIDER) sound = Sound.SPIDER_IDLE;
    else if (damagee.getType() == EntityType.WITHER) sound = Sound.WITHER_HURT;
    else if (damagee.getType() == EntityType.WOLF) sound = Sound.WOLF_HURT;
    else if (damagee.getType() == EntityType.ZOMBIE) sound = Sound.ZOMBIE_HURT;

    damagee.getWorld().playSound(damagee.getLocation(), sound, 1.5F + (float)(0.5D * Math.random()), 0.8F + (float)(0.4000000059604645D * Math.random()));
  }
}