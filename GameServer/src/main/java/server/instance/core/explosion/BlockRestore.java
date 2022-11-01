package server.instance.core.explosion;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import server.util.UtilBlock;

import java.util.HashMap;

public class BlockRestore {
    private HashMap<Block, BlockRestoreData> _blocks = new HashMap();

    public void cleanAll() {
        _blocks.clear();
    }

    public void Restore(Block block) {
        if (!containsBlock(block)) {
            return;
        }
        _blocks.remove(block).restore();
    }

    public void Add(Block block, int toID, byte toData, long expireTime) {
        if (!containsBlock(block)) getBlocks().put(block, new BlockRestoreData(block, toID, toData, expireTime, 0L));
        else
            getData(block).update(toID, toData, expireTime);
    }

    public void Snow(Block block, byte heightAdd, byte heightMax, long expireTime, long meltDelay, int heightJumps) {
        if (((block.getTypeId() == 78) && (block.getData() >= 7)) || ((block.getTypeId() == 80) && (getData(block) != null))) {
            getData(block).update(78, heightAdd, expireTime, meltDelay);

            if (heightJumps > 0)
                Snow(block.getRelative(BlockFace.UP), heightAdd, heightMax, expireTime, meltDelay, heightJumps - 1);
            if (heightJumps == -1)
                Snow(block.getRelative(BlockFace.UP), heightAdd, heightMax, expireTime, meltDelay, -1);

            return;
        }

        if ((!UtilBlock.solid(block.getRelative(BlockFace.DOWN))) && (block.getRelative(BlockFace.DOWN).getTypeId() != 78)) {
            return;
        }

        if ((block.getRelative(BlockFace.DOWN).getTypeId() == 78) && (block.getRelative(BlockFace.DOWN).getData() < 7)) {
            return;
        }

        if (block.getRelative(BlockFace.DOWN).getTypeId() == 79) {
            return;
        }

        if ((block.getRelative(BlockFace.DOWN).getTypeId() == 44) || (block.getRelative(BlockFace.DOWN).getTypeId() == 126)) {
            return;
        }

        if (block.getRelative(BlockFace.DOWN).getType().toString().contains("STAIRS")) {
            return;
        }

        if ((block.getRelative(BlockFace.DOWN).getTypeId() == 85) ||
                (block.getRelative(BlockFace.DOWN).getTypeId() == 139)) {
            return;
        }

        if ((!UtilBlock.airFoliage(block)) && (block.getTypeId() != 78) && (block.getType() != Material.CARPET)) {
            return;
        }

        if ((block.getTypeId() == 78) &&
                (block.getData() >= (byte) (heightMax - 1))) {
            heightAdd = 0;
        }

        if (!containsBlock(block))
            getBlocks().put(block, new BlockRestoreData(block, 78, (byte) Math.max(0, heightAdd - 1), expireTime, meltDelay));
        else
            getData(block).update(78, heightAdd, expireTime, meltDelay);
    }

    public boolean containsBlock(Block block) {
        return getBlocks().containsKey(block);
    }

    public BlockRestoreData getData(Block block) {
        return _blocks.get(block);
    }

    public HashMap<Block, BlockRestoreData> getBlocks() {
        return _blocks;
    }
}