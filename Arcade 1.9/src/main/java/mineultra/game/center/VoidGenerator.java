package mineultra.game.center;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class VoidGenerator extends ChunkGenerator
{
    center plugin;

    public VoidGenerator(center plugin)
    {
        this.plugin = plugin;
    }

    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid)
    {
        byte[][] result = new byte[world.getMaxHeight() / 16][];

        return result;
    }

    private void setBlock(byte[][] result, int x, int y, int z, byte blkid)
    {
        if (result[(y >> 4)] == null) {
            result[(y >> 4)] = new byte['?'];
        }
        result[(y >> 4)][((y & 0xF) << 8 | z << 4 | x)] = blkid;
    }
}
