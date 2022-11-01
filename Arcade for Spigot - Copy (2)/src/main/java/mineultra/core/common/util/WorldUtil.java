package mineultra.core.common.util;

import java.io.File;
import mineultra.game.center.world.WorldData;
import net.minecraft.server.v1_8_R3.Convertable;
import net.minecraft.server.v1_8_R3.EntityTracker;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.IDataManager;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.MethodProfiler;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import net.minecraft.server.v1_8_R3.ServerNBTManager;
import net.minecraft.server.v1_8_R3.WorldLoaderServer;
import net.minecraft.server.v1_8_R3.WorldManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
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
    Convertable converter = new WorldLoaderServer(server.getWorldContainer());
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
 
}