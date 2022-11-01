package me.gonzalociocca.minigame.misc;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * Created by noname on 28/3/2017.
 */
public class VoidGenerator extends ChunkGenerator {


    public VoidGenerator(){};

    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid)
    {
        byte[][] result = new byte[world.getMaxHeight() / 16][];

        return result;
    }

    public Location getFixedSpawnLocation(World world, Random random)
    {
        return new Location(world, 0,1,0);
    }

    private void setBlock(byte[][] result, int x, int y, int z, byte blkid)
    {
        if (result[(y >> 4)] == null) {
            result[(y >> 4)] = new byte['?'];
        }
        result[(y >> 4)][((y & 0xF) << 8 | z << 4 | x)] = blkid;
    }
}

