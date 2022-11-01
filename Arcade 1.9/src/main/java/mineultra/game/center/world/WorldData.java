package mineultra.game.center.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import mineultra.core.common.util.FileUtil;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.WorldUtil;
import mineultra.core.common.util.ZipUtil;
import mineultra.game.center.centerManager;
import mineultra.game.center.game.Game;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldData
{
  public Game Host;
  public int Id = -1;
  public centerManager p;
  public String File = null;
  public String Folder = null;
  public World World;
  public int MinX = 0;
  public int MinZ = 0;
  public int MaxX = 0;
  public int MaxZ = 0;
  public int CurX = 0;
  public int CurZ = 0;

  public int MinY = -1;
  public int MaxY = 256;

  public String MapName = "Null";
  public String MapAuthor = "Null";

  public HashMap<String, ArrayList<Location>> SpawnLocs = new HashMap();
  private final HashMap<String, ArrayList<Location>> DataLocs = new HashMap();
  private final HashMap<String, ArrayList<Location>> CustomLocs = new HashMap();
  boolean isUnzip = false;
  public WorldData(centerManager plug,Game game)
  {
    this.p = plug;
    this.config = plug.getConfig();
    Host = game;
    
    Initialize();

  }

  public void Initialize()
  {
    
final WorldData worldData = this;

    UtilServer.getServer().getScheduler().runTask(Host.Manager.GetPlugin(), new Runnable()
    {
      @Override
      public void run()
      {
    if(worldData.UnzipWorld()){
            
            World = WorldUtil.LoadWorld(new WorldCreator(GetFolder()));
            World.setDifficulty(Difficulty.HARD);
            World.setAutoSave(false);
            World.setGameRuleValue("doMobSpawning","false");
        for(Entity et : World.getEntities()){
            if(!(et instanceof Player)){
                et.remove();
            }
        }
            worldData.LoadWorldConfig();
            Id = GetNewId();

      }
      }
    });
  }

  protected String GetFile()
  {
    if (File == null)
    {
        Random r = new Random(System.currentTimeMillis());

      File = ((String)Host.GetFiles().get(r.nextInt(Host.GetFiles().size())));

      if (Host.GetFiles().size() > 1)
      {
        while (File.equals(Host.Manager.GetGameCreationManager().GetLastMap()))
        {
          File = ((String)Host.GetFiles().get(r.nextInt(Host.GetFiles().size())));
        }
      }
    }

    Host.Manager.GetGameCreationManager().SetLastMap(File);

    return File;
  }

  protected String GetFolder()
  {
    if (Folder == null) {
      Folder = ("Game" + Id + "_" + Host.GetName() + "_" + GetFile());
    }
    return Folder;
  }
FileConfiguration config;

  protected Boolean UnzipWorld()
  {
    String folder = GetFolder();
    new File(folder).mkdir();
    new File(folder + "/" + "region").mkdir();
    new File(folder + "/" + "data").mkdir();
    return ZipUtil.UnzipToDirectory(config.getString("mapconfig.folder").replace("%game", this.Host.GetName())+ "/" + GetFile() + ".zip", folder);
  }

  public void LoadWorldConfig()
  {
    String line = null;
    try
    {
      FileInputStream fstream = new FileInputStream(GetFolder() + "/" + "WorldConfig.dat");
        try (DataInputStream in = new DataInputStream(fstream)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            ArrayList currentTeam = null;
            ArrayList currentData = null;
            
            int currentDirection = 0;
            
            while ((line = br.readLine()) != null)
            {
                String[] tokens = line.split(":");
                
                if (tokens.length >= 2)
                {
                    if (tokens[0].length() != 0)
                    {
                        if (tokens[0].equalsIgnoreCase("MAP_NAME"))
                        {
                            MapName = tokens[1];
                        }
                        else if (tokens[0].equalsIgnoreCase("MAP_AUTHOR"))
                        {
                            MapAuthor = tokens[1];
                        }
                        else if (tokens[0].equalsIgnoreCase("TEAM_NAME"))
                        {
                            SpawnLocs.put(tokens[1], new ArrayList());
                            currentTeam = (ArrayList)SpawnLocs.get(tokens[1]);
                            currentDirection = 0;
                        }
                        else if (tokens[0].equalsIgnoreCase("TEAM_DIRECTION"))
                        {
                            currentDirection = Integer.parseInt(tokens[1]);
                        }
                        else if (tokens[0].equalsIgnoreCase("TEAM_SPAWNS"))
                        {
                            for (int i = 1; i < tokens.length; i++)
                            {
                                Location loc = StrToLoc(tokens[i]);
                                if (loc != null)
                                {
                                    loc.setYaw(currentDirection);
                                    
                                    currentTeam.add(loc);
                                }
                            }
                            
                        }
                        else if (tokens[0].equalsIgnoreCase("DATA_NAME"))
                        {
                            DataLocs.put(tokens[1], new ArrayList());
                            currentData = (ArrayList)DataLocs.get(tokens[1]);
                        }
                        else if (tokens[0].equalsIgnoreCase("DATA_LOCS"))
                        {
                            for (int i = 1; i < tokens.length; i++)
                            {
                                Location loc = StrToLoc(tokens[i]);
                                if (loc != null)
                                {
                                    currentData.add(loc);
                                }
                            }
                            
                        }
                        else if (tokens[0].equalsIgnoreCase("CUSTOM_NAME"))
                        {
                            CustomLocs.put(tokens[1], new ArrayList());
                            currentData = (ArrayList)CustomLocs.get(tokens[1]);
                        }
                        else if (tokens[0].equalsIgnoreCase("CUSTOM_LOCS"))
                        {
                            for (int i = 1; i < tokens.length; i++)
                            {
                                Location loc = StrToLoc(tokens[i]);
                                if (loc != null)
                                {
                                    currentData.add(loc);
                                }
                            }
                            
                        }
                        else if (tokens[0].equalsIgnoreCase("MIN_X"))
                        {
                            try
                            {
                                MinX = Integer.parseInt(tokens[1]);
                                CurX = MinX;
                            }
                            catch (Exception e)
                            {
                                System.out.println("World Data Read Error: Invalid MinX [" + tokens[1] + "]");
                            }
                            
                        }
                        else if (tokens[0].equalsIgnoreCase("MAX_X"))
                        {
                            try
                            {
                                MaxX = Integer.parseInt(tokens[1]);
                            }
                            catch (Exception e)
                            {
                                System.out.println("World Data Read Error: Invalid MaxX [" + tokens[1] + "]");
                            }
                        }
                        else if (tokens[0].equalsIgnoreCase("MIN_Z"))
                        {
                            try
                            {
                                MinZ = Integer.parseInt(tokens[1]);
                                CurZ = MinZ;
                            }
                            catch (Exception e)
                            {
                                System.out.println("World Data Read Error: Invalid MinZ [" + tokens[1] + "]");
                            }
                        }
                        else if (tokens[0].equalsIgnoreCase("MAX_Z"))
                        {
                            try
                            {
                                MaxZ = Integer.parseInt(tokens[1]);
                            }
                            catch (Exception e)
                            {
                                System.out.println("World Data Read Error: Invalid MaxZ [" + tokens[1] + "]");
                            }
                        }
                        else if (tokens[0].equalsIgnoreCase("MIN_Y"))
                        {
                            try
                            {
                                MinY = Integer.parseInt(tokens[1]);
                            }
                            catch (Exception e)
                            {
                                System.out.println("World Data Read Error: Invalid MinY [" + tokens[1] + "]");
                            }
                        }
                        else if (tokens[0].equalsIgnoreCase("MAX_Y"))
                        {
                            try
                            {
                                MaxY = Integer.parseInt(tokens[1]);
                            }
                            catch (Exception e)
                            {
                                System.out.println("World Data Read Error: Invalid MaxY [" + tokens[1] + "]");
                            }
                        }
                        else if (tokens[0].equalsIgnoreCase("MAX_PLAYERS"))
                        {
                            try
                            {
                                this.p.GetServerConfig().setMaxPlayers(Integer.parseInt(tokens[1]));
                            }
                            catch (Exception e)
                            {
                                System.out.println("World Data Read Error: Invalid MaxPlayers [" + tokens[1] + "]");
                            }
                        }
                        else if (tokens[0].equalsIgnoreCase("MIN_PLAYERS"))
                        {
                            try
                            {
                                this.p.GetServerConfig().setMinPlayers(Integer.parseInt(tokens[1]));
                            }
                            catch (Exception e)
                            {
                                System.out.println("World Data Read Error: Invalid MinPlayers [" + tokens[1] + "]");
                            }
                        }
                    }
                }
            } }

      Host.Manager.GetGameWorldManager().RegisterWorld(this);
    }
    catch (IOException | NumberFormatException e)
    {
      System.err.println("Line: " + line);
    }
  }

  protected Location StrToLoc(String loc)
  {
    String[] coords = loc.split(",");
    try
    {
      return new Location(World, Integer.valueOf(coords[0]) + 0.5D, Integer.valueOf(coords[1]), Integer.valueOf(coords[2]) + 0.5D);
    }
    catch (Exception e)
    {
      System.out.println("World Data Read Error: Invalid Location String [" + loc + "]");
    }

    return null;
  }

  public boolean LoadChunks(long maxMilliseconds)
  {

    long startTime = System.currentTimeMillis();

    for (; CurX <= MaxX; CurX += 16)
    {
      for (; CurZ <= MaxZ; CurZ += 16)
      {
        if (System.currentTimeMillis() - startTime >= maxMilliseconds) {
          return false;
        }
        if(!World.getChunkAt(new Location(World, CurX, 0.0D, CurZ)).isLoaded()){
        World.getChunkAt(new Location(World, CurX, 0.0D, CurZ)).load(false);            
        }

      }

      CurZ = MinZ;
    }

    return true;
  }

  public void Uninitialize()
  {
    if (World == null) {
      return;
    }
    for(Chunk c : World.getLoadedChunks()){
        for(Entity ete : c.getEntities()){
            ete.remove();
        }
    }
    System.gc();
    
String name = String.valueOf(World.getName());

 Bukkit.getServer().unloadWorld(World, false);
 
 FileUtil.DeleteFolder(new File(name));
if(Bukkit.getWorlds().contains(World)){
    Bukkit.getWorlds().remove(World);
}
    World = null;
  }
  
  public int GetNewId()
  {
    File file = new File("GameId.dat");

    if (!file.exists())
    {
      try
      {
        FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);

        out.write("0");

        out.close();
      }
      catch (Exception e)
      {
        System.out.println("Error: Game World GetId Write Exception");
      }
    }

    int id = 0;
    try
    {
      FileInputStream fstream = new FileInputStream(file);
        try (DataInputStream in = new DataInputStream(fstream)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            
            id = Integer.parseInt(line);
        }
    }
    catch (IOException | NumberFormatException e)
    {
      System.out.println("Error: Game World GetId Read Exception");
      id = 0;
    }

    try
    {
      FileWriter fstream = new FileWriter(file);
        try (BufferedWriter out = new BufferedWriter(fstream)) {
            out.write(id + 1);
        }
    }
    catch (Exception e)
    {
      System.out.println("Error: Game World GetId Re-Write Exception");
    }

    return id;
  }

  public ArrayList<Location> GetDataLocs(String data)
  {
    if (!DataLocs.containsKey(data)) {
      return new ArrayList();
    }
    return (ArrayList)DataLocs.get(data);
  }

  public ArrayList<Location> GetCustomLocs(String id)
  {
    if (!CustomLocs.containsKey(id)) {
      return new ArrayList();
    }
    return (ArrayList)CustomLocs.get(id);
  }

  public HashMap<String, ArrayList<Location>> GetAllCustomLocs()
  {
    return CustomLocs;
  }

  public Location GetRandomXZ()
  {
    Location loc = new Location(World, 0.0D, 250.0D, 0.0D);

    int xVar = MaxX - MinX;
    int zVar = MaxZ - MinZ;

    loc.setX(MinX + UtilMath.r(xVar));
    loc.setZ(MinZ + UtilMath.r(zVar));

    return loc;
  }
}