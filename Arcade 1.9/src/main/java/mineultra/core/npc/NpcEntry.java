package mineultra.core.npc;

import net.minecraft.server.v1_9_R1.EntityCreature;
import net.minecraft.server.v1_9_R1.Navigation;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftCreature;
import org.bukkit.entity.Entity;

public class NpcEntry
{
  public String Name;
  public Entity Entity;
  public int Radius;
  public Location Location;
  private boolean _returning = false;

  public NpcEntry(String name, Entity entity, int radius, Location location)
  {
    Name = name;
    Entity = entity;
    Radius = radius;
    Location = location;
  }

  public void ReturnToPost()
  {
    EntityCreature ec = ((CraftCreature)Entity).getHandle();
    ec.getNavigation().a(Location.getX(), Location.getY(), Location.getZ(), 0.800000011920929D);

    _returning = true;
  }

  public boolean IsInRadius()
  {
    Location entityLocation = Entity.getLocation();
    return Math.abs(entityLocation.getBlockX() - Location.getBlockX()) + Math.abs(entityLocation.getBlockY() - Location.getBlockY()) + Math.abs(entityLocation.getBlockZ() - Location.getBlockZ()) <= Radius;
  }

  public boolean Returning()
  {
    return _returning;
  }

  public void ClearGoals()
  {
    _returning = false;

    Location entityLocation = Entity.getLocation();
    EntityCreature ec = ((CraftCreature)Entity).getHandle();
    ec.getNavigation().a(entityLocation.getX(), entityLocation.getY(), entityLocation.getZ(), 0.800000011920929D);
  }
}