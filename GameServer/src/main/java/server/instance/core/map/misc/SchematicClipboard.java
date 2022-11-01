package server.instance.core.map.misc;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.*;
import com.sk89q.worldedit.LocalEntity;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by noname on 29/3/2017.
 */
public class SchematicClipboard {
    private BaseBlock[][][] data;
    private Vector size;
    private List<SchematicClipboard.CopiedEntity> entities = new ArrayList();

    public SchematicClipboard(Vector size) {
        Preconditions.checkNotNull(size);
        this.size = size;
        this.data = new BaseBlock[size.getBlockX()][size.getBlockY()][size.getBlockZ()];
    }

    public int getWidth() {
        return this.size.getBlockX();
    }

    public int getLength() {
        return this.size.getBlockZ();
    }

    public int getHeight() {
        return this.size.getBlockY();
    }

    public boolean runQueue(World paste, BlockPosition ori, int maxBlocksPer) throws MaxChangedBlocksException {
        return place(paste, new BlockPosition(ori.getX(), ori.getY(), ori.getZ()), maxBlocksPer);
    }

    int queueX = 0;
    int queueY = 0;
    int queueZ = 0;

    public boolean isQueueFinished() {
        return queueX == size.getBlockX() && queueY == size.getBlockY() && queueZ == size.getBlockZ();
    }

    private Method nbtCreateTagMethod;

    private NBTBase fromNative(Tag foreign) {
        if (nbtCreateTagMethod == null) {
            try {
                this.nbtCreateTagMethod = NBTBase.class.getDeclaredMethod("createTag", Byte.TYPE);
                this.nbtCreateTagMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (foreign == null) {
            return null;
        } else if (foreign instanceof CompoundTag) {
            NBTTagCompound e1 = new NBTTagCompound();
            Iterator foreignList1 = ((CompoundTag) foreign).getValue().entrySet().iterator();

            while (foreignList1.hasNext()) {
                Map.Entry i$1 = (Map.Entry) foreignList1.next();
                e1.set((String) i$1.getKey(), this.fromNative((Tag) i$1.getValue()));
            }

            return e1;
        } else if (foreign instanceof ByteTag) {
            return new NBTTagByte(((ByteTag) foreign).getValue().byteValue());
        } else if (foreign instanceof ByteArrayTag) {
            return new NBTTagByteArray(((ByteArrayTag) foreign).getValue());
        } else if (foreign instanceof DoubleTag) {
            return new NBTTagDouble(((DoubleTag) foreign).getValue().doubleValue());
        } else if (foreign instanceof FloatTag) {
            return new NBTTagFloat(((FloatTag) foreign).getValue().floatValue());
        } else if (foreign instanceof IntTag) {
            return new NBTTagInt(((IntTag) foreign).getValue().intValue());
        } else if (foreign instanceof IntArrayTag) {
            return new NBTTagIntArray(((IntArrayTag) foreign).getValue());
        } else if (!(foreign instanceof ListTag)) {
            if (foreign instanceof LongTag) {
                return new NBTTagLong(((LongTag) foreign).getValue().longValue());
            } else if (foreign instanceof ShortTag) {
                return new NBTTagShort(((ShortTag) foreign).getValue().shortValue());
            } else if (foreign instanceof StringTag) {
                return new NBTTagString(((StringTag) foreign).getValue());
            } else if (foreign instanceof EndTag) {
                try {
                    return (NBTBase) this.nbtCreateTagMethod.invoke(null, Byte.valueOf((byte) 0));
                } catch (Exception var6) {
                    return null;
                }
            } else {
                throw new IllegalArgumentException("Don\'t know how to make NMS " + foreign.getClass().getCanonicalName());
            }
        } else {
            NBTTagList e = new NBTTagList();
            ListTag foreignList = (ListTag) foreign;
            Iterator i$ = foreignList.getValue().iterator();

            while (i$.hasNext()) {
                Tag t = (Tag) i$.next();
                e.add(this.fromNative(t));
            }

            return e;
        }
    }

    public void setNMSBlock(CraftWorld craftWorld, BlockPosition pos, BaseBlock block, boolean notifyAndLight) {

        CompoundTag nativeTag = block.getNbtData();
        boolean nbtNotNull = nativeTag != null;
        int data = block.getData();
        int id = block.getId();

        craftWorld.getHandle().setTypeAndData(pos, CraftMagicNumbers.getBlock(id).fromLegacyData(data), 3);

        if (nbtNotNull) {
            TileEntity tileEntity = craftWorld.getHandle().getTileEntity(pos);
            if (tileEntity != null) {
                NBTTagCompound tag = (NBTTagCompound) this.fromNative(nativeTag);
                tag.set("x", new NBTTagInt(pos.getX()));
                tag.set("y", new NBTTagInt(pos.getY()));
                tag.set("z", new NBTTagInt(pos.getZ()));
                tileEntity.a(tag);// read tag into tileentity
            }
        }

    }

    List<BlockPosition> lastBlocks = new ArrayList();

    long start = System.currentTimeMillis();

    public boolean place(org.bukkit.World paste, BlockPosition ori, int maxBlocksPer) throws MaxChangedBlocksException {
        boolean reLoadY = false;
        boolean reLoadZ = false;
        CraftWorld world = (CraftWorld)paste;

        label:
        for (int x = queueX; x < size.getBlockX(); ++x) {
            for (int y = 0; y < size.getBlockY(); ++y) {
                if (!reLoadY) {
                    y = queueY;
                    reLoadY = true;
                }
                for (int z = 0; z < size.getBlockZ(); ++z) {
                    if (!reLoadZ) {
                        z = queueZ;
                        reLoadZ = true;
                    }
                    maxBlocksPer--;
                    if (maxBlocksPer < 0) {
                        queueX = x;
                        queueY = y;
                        queueZ = z;
                        return true;
                    }
                    BaseBlock block = this.data[x][y][z];
                    if (block != null) {

                        BlockPosition pos = new BlockPosition(ori.getX() + x, ori.getY() + y, ori.getZ() + z);
                        net.minecraft.server.v1_8_R3.Chunk chunk = world.getHandle().getChunkAtWorldCoords(pos);
                        int height = pos.getY() >> 4;
                        ChunkSection section = chunk.getSections()[height];
                        if(section == null){
                            section = new ChunkSection(height << 4, true);
                            chunk.getSections()[height] = section;
                        }
                        section.b(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, 9);
                        section.a(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, 9);
                        if (!block.isAir()) {
                            int id = block.getId();
                            if (id == 61 || id == 62 || id == 130 || id == 65 || id == 68 || id == 69 || id == 63 || id == 54 || id == 52 || id == 33 || id == 34 || id == 51 || id == 50 || id == 76 || id == 75 || id == 78 || id == 81 || id == 83 || id == 84 || id == 90 || id == 132 || id == 138 || id == 143 || id == 146 || id == 169 || id == 89) {
                                lastBlocks.add(pos);
                            } else {
                                setNMSBlock(world, pos, block, false);
                            }
                        }

                    }

                }
            }
        }

        for (BlockPosition pos : lastBlocks) {
            int x = pos.getX() - ori.getX();
            int y = pos.getY() - ori.getY();
            int z = pos.getZ() - ori.getZ();

            BaseBlock block = data[x][y][z];
            setNMSBlock(world, new BlockPosition(ori.getX() + x, ori.getY() + y, ori.getZ() + z), block, true);
        }
        System.out.println("Took "+(System.currentTimeMillis() - start)+" ms to build");
        lastBlocks.clear();

        queueX = size.getBlockX();
        queueY = size.getBlockY();
        queueZ = size.getBlockZ();
        return true;
    }

    public LocalEntity[] pasteEntities(Vector newOrigin) {
        LocalEntity[] entities = new LocalEntity[this.entities.size()];

        for (int i = 0; i < this.entities.size(); ++i) {
            SchematicClipboard.CopiedEntity copied = this.entities.get(i);
            if (copied.entity.spawn(copied.entity.getPosition().setPosition(copied.relativePosition.add(newOrigin)))) {
                entities[i] = copied.entity;
            }
        }

        return entities;
    }

    public void storeEntity(LocalEntity entity) {
        this.entities.add(new SchematicClipboard.CopiedEntity(entity));
    }

    /**
     * @deprecated
     */
    @Deprecated
    public BaseBlock getPoint(Vector position) throws ArrayIndexOutOfBoundsException {
        BaseBlock block = this.getBlock(position);
        return block == null ? new BaseBlock(0) : block;
    }

    public BaseBlock getBlock(Vector position) throws ArrayIndexOutOfBoundsException {
        return this.data[position.getBlockX()][position.getBlockY()][position.getBlockZ()];
    }

    public BaseBlock getBlock(int x, int y, int z) throws ArrayIndexOutOfBoundsException {
        return this.data[x][y][z];
    }

    public void setBlock(Vector position, BaseBlock block) {
        this.data[position.getBlockX()][position.getBlockY()][position.getBlockZ()] = block;
    }

    public Vector getSize() {
        return this.size;
    }

    private class CopiedEntity {
        private final LocalEntity entity;
        private final Vector relativePosition;

        private CopiedEntity(LocalEntity entity) {
            this.entity = entity;
            this.relativePosition = entity.getPosition().getPosition();
        }
    }

}
