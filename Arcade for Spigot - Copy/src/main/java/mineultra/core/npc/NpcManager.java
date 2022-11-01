package mineultra.core.npc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import mineultra.core.MiniPlugin;
import mineultra.core.common.Rank;
import mineultra.core.common.util.F;
import mineultra.core.common.util.NautHashMap;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilWorld;
import mineultra.core.creature.event.CreatureKillEntitiesEvent;
import net.minecraft.server.v1_8_R2.EntityAgeable;
import net.minecraft.server.v1_8_R2.EntityInsentient;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftAgeable;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NpcManager extends MiniPlugin
{
  private mineultra.core.creature.Creature _creature;
  private NautHashMap<String, NpcEntry> _npcs;
  private NautHashMap<String, Integer> _failedAttempts;
  private NautHashMap<String, NpcEntry> _addTempList;
  private HashSet<String> _delTempList;

  public NpcManager(JavaPlugin plugin, mineultra.core.creature.Creature creature)
  {
    super("NpcManager", plugin);

    _creature = creature;
    _npcs = new NautHashMap();
    _failedAttempts = new NautHashMap();
    _addTempList = new NautHashMap();
    _delTempList = new HashSet();

    _plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, new Runnable()
    {
      @Override
      public void run()
      {
        NpcManager.this.UpdateNpcLocations();
      }
    }
    , 0L, 5L);

    _plugin.getServer().getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
    {
      public void run()
      {
        ReattachNpcs();
      }
    }
    , 100L);

    _plugin.getServer().getPluginManager().registerEvents(this, _plugin);

    LoadNpcs();
  }

  public void Help(Player caller, String message)
  {

  }

  public void Help(Player caller)
  {
    Help(caller, null);
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void OnEntityDamage(EntityDamageEvent event)
  {
    if (_npcs.containsKey(event.getEntity().getUniqueId().toString()))
    {
      event.setCancelled(true);
      return;
    }
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void OnCreatureKillEntities(CreatureKillEntitiesEvent event)
  {
    Iterator entityIterator = event.GetEntities().iterator();

    while (entityIterator.hasNext())
    {
      if (_npcs.containsKey(((Entity)entityIterator.next()).getUniqueId().toString()))
      {
        entityIterator.remove();
      }
    }
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void OnEntityTarget(EntityTargetEvent event)
  {
    if (_npcs.containsKey(event.getEntity().getUniqueId().toString()))
    {
      event.setCancelled(true);
      return;
    }
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void OnEntityCombust(EntityCombustEvent event)
  {
    if (_npcs.containsKey(event.getEntity().getUniqueId().toString()))
    {
      event.setCancelled(true);
      return;
    }
  }

  @EventHandler(priority=EventPriority.MONITOR)
  public void OnChunkLoad(ChunkLoadEvent event)
  {
    for (Entity entity : event.getChunk().getEntities())
    {
      if (_npcs.containsKey(entity.getUniqueId().toString()))
      {
        ((NpcEntry)_npcs.get(entity.getUniqueId().toString())).Name = ((LivingEntity)entity).getCustomName();
        ((NpcEntry)_npcs.get(entity.getUniqueId().toString())).Entity = entity;
        UtilEnt.silence(entity, true);
        UtilEnt.ghost(entity, true, false);

        if (((NpcEntry)_npcs.get(entity.getUniqueId().toString())).Radius == 0)
        {
          UtilEnt.Vegetate(entity);
        }
      }
    }
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
  {
    if ((event.getRightClicked() instanceof LivingEntity))
    {
      if (_addTempList.containsKey(event.getPlayer().getName()))
      {
        if (event.getRightClicked().getType() == EntityType.PLAYER)
        {
          event.getPlayer().sendMessage(F.main(GetName(), "Failed to add npc.  Can't attach to player."));
        }
        else
        {
          LivingEntity npc = (LivingEntity)event.getRightClicked();

          if (((NpcEntry)_addTempList.get(event.getPlayer().getName())).Name != null)
          {
            npc.setCustomName(((NpcEntry)_addTempList.get(event.getPlayer().getName())).Name);
            npc.setCustomNameVisible(true);
          }

          npc.getEquipment().setArmorContents(event.getPlayer().getInventory().getArmorContents());
          npc.getEquipment().setItemInHand(event.getPlayer().getItemInHand());
          npc.setCanPickupItems(false);
          ((EntityInsentient)((CraftLivingEntity)npc).getHandle()).persistent = true;

          if ((npc instanceof mineultra.core.creature.Creature))
          {
            ((org.bukkit.entity.Creature)npc).setTarget(null);
          }

          AddNpc(npc, (NpcEntry)_addTempList.get(event.getPlayer().getName()), true);
          event.getPlayer().sendMessage(F.main(GetName(), "Added npc"));
        }

        _addTempList.remove(event.getPlayer().getName());
      }
      else if (_delTempList.contains(event.getPlayer().getName()))
      {
        if (DeleteNpc(event.getRightClicked()))
        {
          event.getPlayer().sendMessage(F.main(GetName(), "Deleted npc."));
        }
        else
        {
          event.getPlayer().sendMessage(F.main(GetName(), "Failed to delete npc.  That one isn't in the list."));
        }

        _delTempList.remove(event.getPlayer().getName());
      }

      if (_npcs.containsKey(event.getRightClicked().getUniqueId().toString()))
      {
        event.setCancelled(true);
        return;
      }
    }
  }

  public void SetNpcInfo(Player admin, int radius, String name, Location location)
  {
    _addTempList.put(admin.getName(), new NpcEntry(name, null, radius, location));
  }

  public Entity AddNpc(EntityType entityType, int radius, String name, Location location)
  {
    LivingEntity entity = (LivingEntity)_creature.SpawnEntity(location, entityType);

    entity.setCustomName(name);
    entity.setCustomNameVisible(true);

    entity.setCanPickupItems(false);
    entity.setRemoveWhenFarAway(false);
    ((EntityInsentient)((CraftLivingEntity)entity).getHandle()).persistent = true;

    if ((((CraftLivingEntity)entity).getHandle() instanceof EntityAgeable))
    {
      ((CraftAgeable)entity).getHandle().ageLocked = true;
    }

    if ((entity instanceof mineultra.core.creature.Creature))
    {
      ((org.bukkit.entity.Creature)entity).setTarget(null);
    }

    return AddNpc(entity, new NpcEntry(name, null, radius, location), true);
  }

  public Entity AddNpc(LivingEntity entity, NpcEntry entry, boolean save)
  {
    entry.Entity = entity;
    _npcs.put(entity.getUniqueId().toString(), entry);

    if (entry.Radius == 0)
    {
      UtilEnt.Vegetate(entry.Entity);
      UtilEnt.silence(entry.Entity, true);
    }

    if (save) {
      SaveNpcs();
    }
    return entity;
  }

  public boolean DeleteNpc(Entity entity)
  {
    if ((entity instanceof LivingEntity))
    {
      if (_npcs.containsKey(entity.getUniqueId().toString()))
      {
        entity.remove();
        _npcs.remove(entity.getUniqueId().toString());

        return true;
      }
    }

    return false;
  }

  public void PrepDeleteNpc(Player admin)
  {
    _delTempList.add(admin.getName());
  }

  public void ClearNpcs()
  {
    Iterator npcIterator = _npcs.keySet().iterator();

    while (npcIterator.hasNext())
    {
      String id = (String)npcIterator.next();

      if (((NpcEntry)_npcs.get(id)).Entity != null) {
        ((NpcEntry)_npcs.get(id)).Entity.remove();
      }
      npcIterator.remove();
    }

    SaveNpcs();
  }

  private void UpdateNpcLocations()
  {
    for (NpcEntry npc : _npcs.values())
    {
      if (npc.Entity != null)
      {
        npc.Entity.setTicksLived(1);
        ((EntityInsentient)((CraftLivingEntity)npc.Entity).getHandle()).persistent = true;
        UtilEnt.silence(npc.Entity, true);

        if ((IsNpcChunkLoaded(npc.Entity)) && ((npc.Entity instanceof CraftCreature)))
        {
          if ((!npc.Entity.isDead()) && (npc.Entity.isValid()))
          {
            String uuid = npc.Entity.getUniqueId().toString();

            ((LivingEntity)npc.Entity).getEquipment().getArmorContents()[0].setDurability((short)0);
            ((LivingEntity)npc.Entity).getEquipment().getArmorContents()[1].setDurability((short)0);
            ((LivingEntity)npc.Entity).getEquipment().getArmorContents()[2].setDurability((short)0);
            ((LivingEntity)npc.Entity).getEquipment().getArmorContents()[3].setDurability((short)0);

            if (!_failedAttempts.containsKey(uuid)) {
              _failedAttempts.put(uuid, Integer.valueOf(0));
            }
            if (((Integer)_failedAttempts.get(uuid)).intValue() >= 10)
            {
              npc.Entity.teleport(npc.Location);
              _failedAttempts.put(uuid, Integer.valueOf(0));
            }
            else if (!npc.IsInRadius())
            {
              npc.ReturnToPost();
              _failedAttempts.put(uuid, Integer.valueOf(((Integer)_failedAttempts.get(uuid)).intValue() + 1));
            }
            else
            {
              if (npc.Returning())
              {
                npc.ClearGoals();
              }

              _failedAttempts.put(uuid, Integer.valueOf(0));
            }
          }
        }
      }
    }
  }

  public void TeleportNpcsHome() { for (NpcEntry npc : _npcs.values())
    {
      if (npc.Entity != null)
      {
        if (IsNpcChunkLoaded(npc.Entity))
        {
          if ((!npc.Entity.isDead()) && (npc.Entity.isValid()))
          {
            npc.Entity.teleport(npc.Location);
            _failedAttempts.put(npc.Entity.getUniqueId().toString(), Integer.valueOf(0));
          }
        }
      }
    } }

  public void ReattachNpcs() {
    for (Entity entity : UtilWorld.getWorldType(World.Environment.NORMAL).getEntities())
    {
      if (_npcs.containsKey(entity.getUniqueId().toString()))
      {
        ((NpcEntry)_npcs.get(entity.getUniqueId().toString())).Name = ((LivingEntity)entity).getCustomName();
        ((NpcEntry)_npcs.get(entity.getUniqueId().toString())).Entity = entity;
      }
    }
  }

  public boolean IsNpcChunkLoaded(Entity entity)
  {
    return entity.getWorld().isChunkLoaded(entity.getLocation().getBlockX() >> 4, entity.getLocation().getBlockZ() >> 4);
  }

  public void LoadNpcs()
  {
    FileInputStream fstream = null;
    BufferedReader br = null;
    try
    {
      File npcFile = new File("npcs.dat");

      if (npcFile.exists())
      {
        fstream = new FileInputStream(npcFile);
        br = new BufferedReader(new InputStreamReader(fstream));

        String line = br.readLine();

        while (line != null)
        {
          UUID uuid = UUID.fromString(line.split(" ")[0]);
          String location = line.split(" ")[1];
          Integer radius = Integer.valueOf(Integer.parseInt(line.split(" ")[2]));

          _npcs.put(uuid.toString(), new NpcEntry(null, null, radius.intValue(), UtilWorld.strToLoc(location)));

          line = br.readLine();
        }
      }
    }
    catch (Exception e)
    {
      System.out.println(F.main(GetName(), "Error parsing npc file."));

      if (br != null)
      {
        try
        {
          br.close();
        }
        catch (IOException ed)
        {
          ed.printStackTrace();
        }
      }

      if (fstream != null)
      {
        try
        {
          fstream.close();
        }
        catch (IOException ef)
        {
          ef.printStackTrace();
        }
      }
    }
    finally
    {
      if (br != null)
      {
        try
        {
          br.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }

      if (fstream != null)
      {
        try
        {
          fstream.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  public void SaveNpcs()
  {
    FileWriter fstream = null;
    BufferedWriter out = null;
    try
    {
      fstream = new FileWriter("npcs.dat");
      out = new BufferedWriter(fstream);

      for (String key : _npcs.keySet())
      {
        out.write(key + " " + UtilWorld.locToStr(((NpcEntry)_npcs.get(key)).Location) + " " + ((NpcEntry)_npcs.get(key)).Radius);
        out.newLine();
      }

      out.close();
    }
    catch (Exception e)
    {
      System.err.println("Npc Save Error: " + e.getMessage());

      if (out != null)
      {
        try
        {
          out.close();
        }
        catch (IOException eg)
        {
          eg.printStackTrace();
        }
      }

      if (fstream != null)
      {
        try
        {
          fstream.close();
        }
        catch (IOException eg)
        {
          eg.printStackTrace();
        }
      }
    }
    finally
    {
      if (out != null)
      {
        try
        {
          out.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }

      if (fstream != null)
      {
        try
        {
          fstream.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  public NpcEntry GetNpcByUUID(UUID uniqueId)
  {
    return (NpcEntry)_npcs.get(uniqueId.toString());
  }
}