package mineultra.core.common.util;

import java.io.File;
import mineultra.game.center.world.WorldData;
import net.minecraft.server.v1_9_R1.Convertable;
import net.minecraft.server.v1_9_R1.EntityTracker;
import net.minecraft.server.v1_9_R1.EnumDifficulty;
import net.minecraft.server.v1_9_R1.IDataManager;
import net.minecraft.server.v1_9_R1.IProgressUpdate;
import net.minecraft.server.v1_9_R1.MethodProfiler;
import net.minecraft.server.v1_9_R1.WorldSettings.EnumGamemode;
import net.minecraft.server.v1_9_R1.ServerNBTManager;
import net.minecraft.server.v1_9_R1.WorldLoaderServer;
import net.minecraft.server.v1_9_R1.WorldManager;
import net.minecraft.server.v1_9_R1.WorldServer;
import net.minecraft.server.v1_9_R1.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;

public class WorldUtil
{
    public static World LoadWorld(WorldCreator creator)
  {
      
    CraftServer server = (CraftServer)Bukkit.getServer();
    if (creator == null)
    {
      throw new IllegalArgumentException("Creator may not be null");
    }

    String name = creator.name();
    System.out.println("Loading world '" + name + "'");
    ChunkGenerator generator = creator.generator();
    File folder = new File(server.getWorldContainer(), name);
    World world2 = server.getWorld(name);
  
    if (world2 != null)
    {
      return world2;
    }
System.out.println("Step: 1");
    
    if ((folder.exists()) && (!folder.isDirectory()))
    {
      throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
    }
    System.out.println("Step: 2");

    if (generator == null)
    {
      generator = server.getGenerator(name);
    }
System.out.println("Step: 3");

    Convertable converter = new WorldLoaderServer(server.getWorldContainer(),server.getServer().getDataConverterManager());
   try{ if (converter.isConvertable(name))
    {
      server.getLogger().info("Converting world '" + name + "'");
     /* converter.convert(name,converter.;
     */
    }}catch(Exception en){
      en.printStackTrace();
  }
System.out.println("Step: 4");
    int dimension = server.getServer().worlds.size() + 1;
    boolean used = false;
    do
    {
        System.out.println("Step: 5");
      for (WorldServer worldServer : server.getServer().worlds)
      {
        used = worldServer.dimension == dimension;
        if (used)
        {
     System.out.println("Step: 6");
            dimension++;
          break;
        }
      }
    }
    while (used);
    boolean hardcore = false;

    World world = Bukkit.createWorld(new WorldCreator(name));
    
    System.out.println("Step: 11");

System.out.println("Step: 12");
    System.out.println("Created world with dimension : " + dimension);



  
        return world;


  }
    
    /*
  public static World LoadWorld(WorldCreator creator)
  {
      
    CraftServer server = (CraftServer)Bukkit.getServer();
    if (creator == null)
    {
      throw new IllegalArgumentException("Creator may not be null");
    }

    String name = creator.name();
    System.out.println("Loading world '" + name + "'");
    ChunkGenerator generator = creator.generator();
    File folder = new File(server.getWorldContainer(), name);
    World world = server.getWorld(name);
    net.minecraft.server.v1_8_R2.WorldType type = net.minecraft.server.v1_8_R2.WorldType.getType(creator.type().getName());
    boolean generateStructures = creator.generateStructures();

    if (world != null)
    {
      return world;
    }
System.out.println("Step: 1");
    
    if ((folder.exists()) && (!folder.isDirectory()))
    {
      throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
    }
    System.out.println("Step: 2");

    if (generator == null)
    {
      generator = server.getGenerator(name);
    }
System.out.println("Step: 3");
    Convertable converter = new WorldLoaderServer(server.getWorldContainer());
   try{ if (converter.isConvertable(name))
    {
      server.getLogger().info("Converting world '" + name + "'");
     /* converter.convert(name,converter.;
     
    }}catch(Exception en){
      en.printStackTrace();
  }
System.out.println("Step: 4");
    int dimension = server.getServer().worlds.size() + 1;
    boolean used = false;
    do
    {
        System.out.println("Step: 5");
      for (WorldServer worldServer : server.getServer().worlds)
      {
        used = worldServer.dimension == dimension;
        if (used)
        {
     System.out.println("Step: 6");
            dimension++;
          break;
        }
      }
    }
    while (used);
    boolean hardcore = false;
System.out.println("Step: 7");
    IDataManager idt = converter.a(name, true);


System.out.println("Step: 8");
      
    WorldServer internal = new WorldServer(
            server.getServer(),
            idt,
            idt.getWorldData(),
            server.getWorlds().size()+1,
            server.getServer().methodProfiler,
            creator.environment(),
            generator);
    System.out.println("Step: 9");
        internal.worldMaps = ((WorldServer)server.getServer().worlds.get(0)).worldMaps;
    internal.addIWorldAccess(new WorldManager(server.getServer(), internal));
        internal.scoreboard = server.getScoreboardManager().getMainScoreboard().getHandle();
    internal.tracker = new EntityTracker(internal);
    System.out.println("Step: 10");
   internal.savingDisabled = true;
   internal.setSpawnFlags(true, true);
    server.getServer().worlds.add(internal);
    boolean containsWorld = false;
    System.out.println("Step: 11");
    
    for (World otherWorld : server.getWorlds())
    {
      if (otherWorld.getName().equalsIgnoreCase(name.toLowerCase()))
      {
        containsWorld = true;
        break;
      }
    }
System.out.println("Step: 12");
    if (!containsWorld) {
  /*    return null;
    }
    System.out.println("Created world with dimension : " + dimension);



    if (generator != null)
    {
      internal.getWorld().getPopulators().addAll(generator.getDefaultPopulators(internal.getWorld()));
    }

    server.getPluginManager().callEvent(new WorldInitEvent(internal.getWorld()));
    server.getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));
if(internal == null){
    System.out.println("internal null");
}else{
    System.out.println(internal);
}
if(internal.getWorld() == null){
    System.out.println("world null");
}else{
    System.out.println(internal.getWorld());
}
    return internal.getWorld();
  }*/
}