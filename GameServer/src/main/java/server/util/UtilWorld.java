package server.util;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

/**
 * Created by noname on 14/5/2017.
 */
public class UtilWorld {

    public static void load(String worldname, ChunkGenerator generator) {
        World newGameWorld = new WorldCreator(worldname).generator(generator).environment(World.Environment.NORMAL).type(WorldType.CUSTOMIZED).generateStructures(false).seed(0).createWorld();
        newGameWorld.setAutoSave(false);
        newGameWorld.setKeepSpawnInMemory(false);
        newGameWorld.setTicksPerAnimalSpawns(0);
        newGameWorld.setTicksPerMonsterSpawns(0);
        newGameWorld.setPVP(true);
    }


}
