package mineultra.game.center.game.games.survivalgames;

        import java.util.ArrayList;
        import org.bukkit.Chunk;
        import org.bukkit.Location;

public class ChunkChange
{
    public Chunk Chunk;
    public long Time;
    public ArrayList<BlockChange> Changes;
    public short[] DirtyBlocks;
    public short DirtyCount;

    public ChunkChange(Location loc, int id, byte data)
    {
        this.DirtyBlocks = new short[64];
        this.DirtyCount = 0;
        this.Chunk = loc.getChunk();
        this.Changes = new ArrayList();
        AddChange(loc, id, data);
        this.Time = System.currentTimeMillis();
    }

    public void AddChange(Location loc, int id, byte data)
    {
        this.Changes.add(new BlockChange(loc, id, data));
        if (this.DirtyCount < 63)
        {
            short short1 = (short)((loc.getBlockX() & 0xF) << 12 | (loc.getBlockZ() & 0xF) << 8 | loc.getBlockY());
            for (int l = 0; l < this.DirtyCount; l++) {
                if (this.DirtyBlocks[l] == short1) {
                    return;
                }
            }
            short[] dirtyBlocks = this.DirtyBlocks;
            short dirtyCount = this.DirtyCount;
            this.DirtyCount = ((short)(dirtyCount + 1));
            dirtyBlocks[dirtyCount] = short1;
        }
        else
        {
            this.DirtyCount = ((short)(this.DirtyCount + 1));
        }
    }
}
