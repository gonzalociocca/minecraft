package mineultra.core.common.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.Chunk;
import net.minecraft.server.v1_9_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_9_R1.ChunkProviderServer;
import net.minecraft.server.v1_9_R1.ChunkSection;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumSkyBlock;
import net.minecraft.server.v1_9_R1.IBlockData;
import net.minecraft.server.v1_9_R1.MinecraftServer;
import net.minecraft.server.v1_9_R1.PacketPlayOutMultiBlockChange;
import net.minecraft.server.v1_9_R1.PlayerConnection;
import net.minecraft.server.v1_9_R1.RegionFile;
import net.minecraft.server.v1_9_R1.RegionFileCache;
import net.minecraft.server.v1_9_R1.WorldProvider;
import net.minecraft.server.v1_9_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.util.LongHashSet;
import org.bukkit.craftbukkit.v1_9_R1.util.LongObjectHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class MapUtil
{
    
  public static void ReplaceOreInChunk(org.bukkit.Chunk chunk, Material replacee, Material replacer)
  {
    net.minecraft.server.v1_9_R1.Chunk c = ((CraftChunk)chunk).getHandle();

    for (int x = 0; x < 16; x++)
    {
      for (int z = 0; z < 16; z++)
      {
        for (int y = 0; y < 18; y++)
        {
          int bX = c.locX << 4 | x & 0xF;
          int bY = y & 0xFF;
          int bZ = c.locZ << 4 | z & 0xF;

       /*  not updated. if (c.getBlockData(new BlockPosition(bX & 0xF, bY, bZ & 0xF))== replacee.getId())
          {
            c.a(new BlockPosition(bX & 0xF, bY, bZ & 0xF), replacer.getId());
          }*/
        }
      }
    }

    c.initLighting();
  }

  public static void QuickChangeBlockAt(Location location, Material setTo)
  {
    QuickChangeBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), setTo);
  }

  public static void QuickChangeBlockAt(Location location, int id, byte data)
  {
    QuickChangeBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), id, data);
  }

  public static void QuickChangeBlockAt(org.bukkit.World world, int x, int y, int z, Material setTo)
  {
    QuickChangeBlockAt(world, x, y, z, setTo, 0);
  }

  public static void QuickChangeBlockAt(org.bukkit.World world, int x, int y, int z, Material setTo, int data)
  {
    QuickChangeBlockAt(world, x, y, z, setTo.getId(), data);
  }

  public static void QuickChangeBlockAt(org.bukkit.World world, int x, int y, int z, int id, int data)
  {
    org.bukkit.Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
    net.minecraft.server.v1_9_R1.Chunk c = ((CraftChunk)chunk).getHandle();


    c.a(new BlockPosition(x & 0xF, y, z & 0xF), net.minecraft.server.v1_9_R1.Block.getById(id).getBlockData());

  //  ((CraftWorld)world).getHandle().notify(x, y, z);
  }
        

  public static int GetHighestBlockInCircleAt(org.bukkit.World world, int bx, int bz, int radius)
  {
    int count = 0;
    int totalHeight = 0;

    double invRadiusX = 1 / radius;
    double invRadiusZ = 1 / radius;

    int ceilRadiusX = (int)Math.ceil(radius);
    int ceilRadiusZ = (int)Math.ceil(radius);

    double nextXn = 0.0D;
    for (int x = 0; x <= ceilRadiusX; x++)
    {
      double xn = nextXn;
      nextXn = (x + 1) * invRadiusX;
      double nextZn = 0.0D;
      for (int z = 0; z <= ceilRadiusZ; z++)
      {
        double zn = nextZn;
        nextZn = (z + 1) * invRadiusZ;

        double distanceSq = xn * xn + zn * zn;
        if (distanceSq > 1.0D)
        {
          if (z != 0) break;

        }

        totalHeight += world.getHighestBlockAt(bx + x, bz + z).getY();
        count++;
      }
    }

    label155: return totalHeight / count;
  }

  public static void ResendChunksForNearbyPlayers(Collection<net.minecraft.server.v1_9_R1.Chunk> chunks)
  {
    int j;
 int i = 0;
      Player[] arrayOfPlayer;
    j = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()])).length;
    for (Iterator localIterator = chunks.iterator(); i < j; localIterator.hasNext() )
    {
        if(localIterator.next() == null){
            break;
        }
            
      net.minecraft.server.v1_9_R1.Chunk c = (net.minecraft.server.v1_9_R1.Chunk)localIterator.next();

      Player player = arrayOfPlayer[i];

      Vector pV = player.getLocation().toVector();
      int xDist = Math.abs((pV.getBlockX() >> 4) - c.locX);
      int zDist = Math.abs((pV.getBlockZ() >> 4) - c.locZ);

      if (xDist + zDist <= 12)
      {
        SendChunkForPlayer(c, player);
      }
      i=i+1;
    }
  }
  

  /*public static net.minecraft.server.v1_9_R1.Chunk ChunkBlockChange(Location location, int id, byte data)
  {
    return ChunkBlockChange(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), id, data);
  }

  public static net.minecraft.server.v1_9_R1.Chunk ChunkBlockChange(org.bukkit.World world, int x, int y, int z, int id, byte data)
  {
    net.minecraft.server.v1_9_R1.Chunk c = ((CraftChunk)world.getChunkAt(x >> 4, z >> 4)).getHandle();
    net.minecraft.server.v1_9_R1.Block block = net.minecraft.server.v1_9_R1.Block.getById(id);

    x &= 15;
    z &= 15;

    int l1 = bloc.k(c.getBlockData(new BlockPosition(x,y,z)));
    int i2 = c.a(new BlockPosition(x, y, z),0);


    return c;
  }*/

  public static void SendChunkForPlayer(net.minecraft.server.v1_9_R1.Chunk chunk, Player player)
  {
    SendChunkForPlayer(chunk.locX, chunk.locZ, player);
  }

  public static void SendChunkForPlayer(int x, int z, Player player)
  {
    //removed in minecraft 1.9 ((CraftPlayer)player).getHandle().chunkcoordintpairetc.add(new ChunkCoordIntPair(x, z));
  }

  public static void SendMultiBlockForPlayer(int x, int z, short[] dirtyBlocks, int dirtyCount, org.bukkit.World world, Player player)
  {
    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutMultiBlockChange(dirtyCount, dirtyBlocks, ((CraftWorld)world).getHandle().getChunkAt(x, z)));
  }

  public static void UnloadWorld(JavaPlugin plugin, org.bukkit.World world)
  {
      if(world == null){
          return;
      }
    world.setAutoSave(false);

    for (Entity entity : world.getEntities())
    {
      entity.remove();
    }

    CraftServer server = (CraftServer)plugin.getServer();
    CraftWorld craftWorld = (CraftWorld)world;

    Bukkit.getPluginManager().callEvent(new WorldUnloadEvent(((CraftWorld)world).getHandle().getWorld()));

      for (net.minecraft.server.v1_9_R1.Chunk chunk : ((CraftWorld)world).getHandle().getChunkProviderServer().chunks.values()) {
          chunk.removeEntities();
      }

    ((CraftWorld)world).getHandle().getChunkProviderServer().chunks.clear();
    ((CraftWorld)world).getHandle().getChunkProviderServer().unloadQueue.clear();
    try
    {
      Field f = server.getClass().getDeclaredField("worlds");
      f.setAccessible(true);

      Map worlds = (Map)f.get(server);
      worlds.remove(world.getName().toLowerCase());
      f.setAccessible(false);
    }
    catch (IllegalAccessException | NoSuchFieldException ex)
    {
      System.out.println("Error removing world from bukkit master list: " + ex.getMessage());
    }

    MinecraftServer ms = null;
    try
    {
      Field f = server.getClass().getDeclaredField("console");
      f.setAccessible(true);
      ms = (MinecraftServer)f.get(server);
      f.setAccessible(false);
    }
    catch (IllegalAccessException | NoSuchFieldException ex)
    {
      System.out.println("Error getting minecraftserver variable: " + ex.getMessage());
    }
if(ms != null){
    ms.worlds.remove(ms.worlds.indexOf(craftWorld.getHandle()));    
}

  }

  public static boolean ClearWorldReferences(String worldName)
  {
    HashMap regionfiles = null;
    Field rafField = null;
    try
    {
      Field a = RegionFileCache.class.getDeclaredField("a");
      a.setAccessible(true);
      regionfiles = (HashMap)a.get(null);
      rafField = RegionFile.class.getDeclaredField("c");
      rafField.setAccessible(true);
    }
    catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException t)
    {
      System.out.println("Error binding to region file cache.");
    }

    if (regionfiles == null) {
    System.out.println("region files is null at MapUtil");
    return false;
    }
    
    if (rafField == null){
        System.out.println("Random acces field is null at MapUtil");
        return false;
    }

    ArrayList removedKeys = new ArrayList();
    try
    {
      for (Iterator localIterator = regionfiles.entrySet().iterator(); localIterator.hasNext(); )
      { Object o = localIterator.next();

        Map.Entry e = (Map.Entry)o;
        File f = (File)e.getKey();

        if (f.toString().startsWith("." + File.separator + worldName))
        {
          RegionFile file = (RegionFile)e.getValue();
          try
          {
            RandomAccessFile raf = (RandomAccessFile)rafField.get(file);
            raf.close();
            removedKeys.add(f);
          }
          catch (IllegalArgumentException | IllegalAccessException | IOException ex)
          {
          }
        }
      }
    }
    catch (Exception ex)
    {
      System.out.println("Exception while removing world reference for '" + worldName + "'!");
    }

    for (Iterator localIterator = removedKeys.iterator(); localIterator.hasNext(); ) { Object key = localIterator.next();
      regionfiles.remove(key);
    }
    return true;
 
  }
}