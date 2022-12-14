//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraft.server.v1_9_R1;

import co.aikar.timings.TimingHistory;
import co.aikar.timings.WorldTimingsHandler;
import com.destroystokyo.paper.PaperWorldConfig;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import net.minecraft.server.v1_9_R1.BlockPosition.PooledBlockPosition;
import net.minecraft.server.v1_9_R1.Chunk.EnumTileEntityState;
import net.minecraft.server.v1_9_R1.EnumDirection.EnumDirectionLimit;
import net.minecraft.server.v1_9_R1.Explosion.CacheKey;
import net.minecraft.server.v1_9_R1.MovingObjectPosition.EnumMovingObjectType;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_9_R1.WorldMap.WorldMapHumanTracker;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_9_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.generator.ChunkGenerator;
import org.spigotmc.ActivationRange;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.SpigotWorldConfig;
import org.spigotmc.TickLimiter;

public abstract class World implements IBlockAccess {
    private int a = 63;
    protected boolean d;
    public final List<Entity> entityList = new ArrayList() {
        public Entity remove(int index) {
            this.guard();
            return (Entity)super.remove(index);
        }

        public boolean remove(Object o) {
            this.guard();
            return super.remove(o);
        }

        private void guard() {
            if(World.this.guardEntityList) {
                throw new ConcurrentModificationException();
            }
        }
    };
    protected final Set<Entity> f = Sets.newHashSet();
    public final List<TileEntity> tileEntityListTick = Lists.newArrayList();
    private final List<TileEntity> b = Lists.newArrayList();
    private final Set<TileEntity> tileEntityListUnload = Sets.newHashSet();
    public final List<EntityHuman> players = Lists.newArrayList();
    public final List<Entity> j = Lists.newArrayList();
    protected final IntHashMap<Entity> entitiesById = new IntHashMap();
    private long I = 16777215L;
    private int J;
    protected int l = (new Random()).nextInt();
    protected final int m = 1013904223;
    protected float n;
    protected float o;
    protected float p;
    protected float q;
    private int K;
    public final Random random = new Random();
    public WorldProvider worldProvider;
    protected NavigationListener t = new NavigationListener();
    protected List<IWorldAccess> u;
    protected IChunkProvider chunkProvider;
    protected final IDataManager dataManager;
    public WorldData worldData;
    protected boolean isLoading;
    public PersistentCollection worldMaps;
    protected PersistentVillage villages;
    protected LootTableRegistry B;
    public final MethodProfiler methodProfiler;
    private final Calendar L;
    public Scoreboard scoreboard;
    public final boolean isClientSide;
    public boolean allowMonsters;
    public boolean allowAnimals;
    private boolean M;
    private final WorldBorder N;
    int[] H;
    private final CraftWorld world;
    public boolean pvpMode;
    public boolean keepSpawnInMemory = true;
    public ChunkGenerator generator;
    public boolean captureBlockStates = false;
    public boolean captureTreeGeneration = false;
    public ArrayList<BlockState> capturedBlockStates = new ArrayList() {
        public boolean add(BlockState blockState) {
            Iterator blockStateIterator = this.iterator();

            BlockState blockState1;
            do {
                if(!blockStateIterator.hasNext()) {
                    return super.add(blockState);
                }

                blockState1 = (BlockState)blockStateIterator.next();
            } while(!blockState1.getLocation().equals(blockState.getLocation()));

            return false;
        }
    };
    public long ticksPerAnimalSpawns;
    public long ticksPerMonsterSpawns;
    public boolean populating;
    private int tickPosition;
    public final SpigotWorldConfig spigotConfig;
    public final PaperWorldConfig paperConfig;
    public final WorldTimingsHandler timings;
    private boolean guardEntityList;
    public static boolean haveWeSilencedAPhysicsCrash;
    public static String blockLocation;
    private TickLimiter entityLimiter;
    private TickLimiter tileLimiter;
    private int tileTickPosition;
    public final Map<CacheKey, Float> explosionDensityCache = new HashMap();
    public Map<BlockPosition, TileEntity> capturedTileEntities = Maps.newHashMap();

    public CraftWorld getWorld() {
        return this.world;
    }

    public CraftServer getServer() {
        return (CraftServer)Bukkit.getServer();
    }

    public Chunk getChunkIfLoaded(BlockPosition blockposition) {
        return this.chunkProvider.getLoadedChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    public Chunk getChunkIfLoaded(int x, int z) {
        return ((ChunkProviderServer)this.chunkProvider).getLoadedChunkAtWithoutMarkingActive(x, z);
    }

    public Chunk getChunkIfActive(int x, int z) {
        return ((ChunkProviderServer)this.chunkProvider).getChunkIfActive(x, z);
    }

    protected World(IDataManager idatamanager, WorldData worlddata, WorldProvider worldprovider, MethodProfiler methodprofiler, boolean flag, ChunkGenerator gen, Environment env) {
        this.spigotConfig = new SpigotWorldConfig(worlddata.getName());
        this.paperConfig = new PaperWorldConfig(worlddata.getName(), this.spigotConfig);
        this.generator = gen;
        this.world = new CraftWorld((WorldServer)this, gen, env);
        this.ticksPerAnimalSpawns = (long)this.getServer().getTicksPerAnimalSpawns();
        this.ticksPerMonsterSpawns = (long)this.getServer().getTicksPerMonsterSpawns();
        this.u = Lists.newArrayList(new IWorldAccess[]{this.t});
        this.L = Calendar.getInstance();
        this.scoreboard = new Scoreboard();
        this.allowMonsters = true;
        this.allowAnimals = true;
        this.H = new int['???'];
        this.dataManager = idatamanager;
        this.methodProfiler = methodprofiler;
        this.worldData = worlddata;
        this.worldProvider = worldprovider;
        this.isClientSide = flag;
        this.N = worldprovider.getWorldBorder();
        this.getWorldBorder().world = (WorldServer)this;
        this.getWorldBorder().a(new IWorldBorderListener() {
            public void a(WorldBorder worldborder, double d0) {
                World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, EnumWorldBorderAction.SET_SIZE), worldborder.world);
            }

            public void a(WorldBorder worldborder, double d0, double d1, long i) {
                World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, EnumWorldBorderAction.LERP_SIZE), worldborder.world);
            }

            public void a(WorldBorder worldborder, double d0, double d1) {
                World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, EnumWorldBorderAction.SET_CENTER), worldborder.world);
            }

            public void a(WorldBorder worldborder, int i) {
                World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, EnumWorldBorderAction.SET_WARNING_TIME), worldborder.world);
            }

            public void b(WorldBorder worldborder, int i) {
                World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, EnumWorldBorderAction.SET_WARNING_BLOCKS), worldborder.world);
            }

            public void b(WorldBorder worldborder, double d0) {
            }

            public void c(WorldBorder worldborder, double d0) {
            }
        });
        this.getServer().addWorld(this.world);
        this.timings = new WorldTimingsHandler(this);
        this.keepSpawnInMemory = this.paperConfig.keepSpawnInMemory;
        this.entityLimiter = new TickLimiter(this.spigotConfig.entityMaxTickTime);
        this.tileLimiter = new TickLimiter(this.spigotConfig.tileMaxTickTime);
    }

    public World b() {
        return this;
    }

    public BiomeBase getBiome(final BlockPosition blockposition) {
        if(this.isLoaded(blockposition)) {
            Chunk chunk = this.getChunkAtWorldCoords(blockposition);

            try {
                return chunk.getBiome(blockposition, this.worldProvider.k());
            } catch (Throwable var6) {
                CrashReport crashreport = CrashReport.a(var6, "Getting biome");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Coordinates of biome request");
                crashreportsystemdetails.a("Location", new Callable() {
                    public String a() throws Exception {
                        return CrashReportSystemDetails.a(blockposition);
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        } else {
            return this.worldProvider.k().getBiome(blockposition, Biomes.c);
        }
    }

    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.k();
    }

    protected abstract IChunkProvider n();

    public void a(WorldSettings worldsettings) {
        this.worldData.d(true);
    }

    public MinecraftServer getMinecraftServer() {
        return null;
    }

    public IBlockData c(BlockPosition blockposition) {
        BlockPosition blockposition1;
        for(blockposition1 = new BlockPosition(blockposition.getX(), this.K(), blockposition.getZ()); !this.isEmpty(blockposition1.up()); blockposition1 = blockposition1.up()) {
            ;
        }

        return this.getType(blockposition1);
    }

    private static boolean isValidLocation(BlockPosition blockposition) {
        return blockposition.isValidLocation();
    }

    public boolean isEmpty(BlockPosition blockposition) {
        return this.getType(blockposition).getMaterial() == Material.AIR;
    }

    public boolean isLoaded(BlockPosition blockposition) {
        return this.a(blockposition, true);
    }

    public boolean a(BlockPosition blockposition, boolean flag) {
        return !blockposition.isValidLocation()?false:this.isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4, flag);
    }

    public boolean areChunksLoaded(BlockPosition blockposition, int i) {
        return this.areChunksLoaded(blockposition, i, true);
    }

    public boolean areChunksLoaded(BlockPosition blockposition, int i, boolean flag) {
        return this.isAreaLoaded(blockposition.getX() - i, blockposition.getY() - i, blockposition.getZ() - i, blockposition.getX() + i, blockposition.getY() + i, blockposition.getZ() + i, flag);
    }

    public boolean areChunksLoadedBetween(BlockPosition blockposition, BlockPosition blockposition1) {
        return this.areChunksLoadedBetween(blockposition, blockposition1, true);
    }

    public boolean areChunksLoadedBetween(BlockPosition blockposition, BlockPosition blockposition1, boolean flag) {
        return this.isAreaLoaded(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), flag);
    }

    public boolean a(StructureBoundingBox structureboundingbox) {
        return this.b(structureboundingbox, true);
    }

    public boolean b(StructureBoundingBox structureboundingbox, boolean flag) {
        return this.isAreaLoaded(structureboundingbox.a, structureboundingbox.b, structureboundingbox.c, structureboundingbox.d, structureboundingbox.e, structureboundingbox.f, flag);
    }

    private boolean isAreaLoaded(int i, int j, int k, int l, int i1, int j1, boolean flag) {
        if(i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;

            for(int k1 = i; k1 <= l; ++k1) {
                for(int l1 = k; l1 <= j1; ++l1) {
                    if(!this.isChunkLoaded(k1, l1, flag)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected abstract boolean isChunkLoaded(int var1, int var2, boolean var3);

    public Chunk getChunkAtWorldCoords(BlockPosition blockposition) {
        return this.getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    public Chunk getChunkAt(int i, int j) {
        return this.chunkProvider.getOrCreateChunkFast(i, j);
    }

    public boolean setTypeAndData(final BlockPosition blockposition, IBlockData iblockdata, int i) {
        if(this.captureTreeGeneration) {
            Object chunk1 = null;
            Iterator block1 = this.capturedBlockStates.iterator();

            while(block1.hasNext()) {
                BlockState blockstate1 = (BlockState)block1.next();
                if(blockstate1.getX() == blockposition.getX() && blockstate1.getY() == blockposition.getY() && blockstate1.getZ() == blockposition.getZ()) {
                    chunk1 = blockstate1;
                    block1.remove();
                    break;
                }
            }

            if(chunk1 == null) {
                chunk1 = CraftBlockState.getBlockState(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), i);
            }

            ((BlockState)chunk1).setTypeId(CraftMagicNumbers.getId(iblockdata.getBlock()));
            ((BlockState)chunk1).setRawData((byte)iblockdata.getBlock().toLegacyData(iblockdata));
            this.capturedBlockStates.add((BlockState)chunk1);
            return true;
        } else if(!blockposition.isValidLocation()) {
            return false;
        } else if(!this.isClientSide && this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        } else {
            final Chunk chunk = this.getChunkAtWorldCoords(blockposition);
            Block block = iblockdata.getBlock();
            CraftBlockState blockstate = null;
            if(this.captureBlockStates) {
                blockstate = CraftBlockState.getBlockState(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), i);
                this.capturedBlockStates.add(blockstate);
            }

            IBlockData iblockdata1 = chunk.a(blockposition, iblockdata);
            if(iblockdata1 == null) {
                if(this.captureBlockStates) {
                    this.capturedBlockStates.remove(blockstate);
                }

                return false;
            } else {
                if(iblockdata.c() != iblockdata1.c() || iblockdata.d() != iblockdata1.d()) {
                    this.methodProfiler.a("checkLight");
                    if(!this.paperConfig.queueLightUpdates) {
                        this.w(blockposition);
                    } else {
                        ++chunk.lightUpdates;
                        this.getMinecraftServer().lightingQueue.add(new Runnable(){
                            @Override
                            public void run() {
                                w(blockposition);
                                --chunk.lightUpdates;
                            }
                        });
                    }

                    this.methodProfiler.b();
                }

                if(!this.captureBlockStates) {
                    this.notifyAndUpdatePhysics(blockposition, chunk, iblockdata1, iblockdata, i);
                }

                return true;
            }
        }
    }

    public void notifyAndUpdatePhysics(BlockPosition blockposition, Chunk chunk, IBlockData oldBlock, IBlockData newBlock, int flag) {
        if((flag & 2) != 0 && (chunk == null || chunk.isReady())) {
            this.notify(blockposition, oldBlock, newBlock, flag);
        }

        if(!this.isClientSide && (flag & 1) != 0) {
            this.update(blockposition, oldBlock.getBlock());
            if(newBlock.n()) {
                this.updateAdjacentComparators(blockposition, newBlock.getBlock());
            }
        }

    }

    public boolean setAir(BlockPosition blockposition) {
        return this.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
    }

    public boolean setAir(BlockPosition blockposition, boolean flag) {
        IBlockData iblockdata = this.getType(blockposition);
        Block block = iblockdata.getBlock();
        if(iblockdata.getMaterial() == Material.AIR) {
            return false;
        } else {
            this.triggerEffect(2001, blockposition, Block.getCombinedId(iblockdata));
            if(flag) {
                block.b(this, blockposition, iblockdata, 0);
            }

            return this.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
        }
    }

    public boolean setTypeUpdate(BlockPosition blockposition, IBlockData iblockdata) {
        return this.setTypeAndData(blockposition, iblockdata, 3);
    }

    public void notify(BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1, int i) {
        for(int j = 0; j < this.u.size(); ++j) {
            ((IWorldAccess)this.u.get(j)).a(this, blockposition, iblockdata, iblockdata1, i);
        }

    }

    public void update(BlockPosition blockposition, Block block) {
        if(this.worldData.getType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
            if(this.populating) {
                return;
            }

            this.applyPhysics(blockposition, block);
        }

    }

    public void a(int i, int j, int k, int l) {
        int i1;
        if(k > l) {
            i1 = l;
            l = k;
            k = i1;
        }

        if(!this.worldProvider.m()) {
            for(i1 = k; i1 <= l; ++i1) {
                this.c(EnumSkyBlock.SKY, new BlockPosition(i, i1, j));
            }
        }

        this.b(i, k, j, i, l, j);
    }

    public void b(BlockPosition blockposition, BlockPosition blockposition1) {
        this.b(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
    }

    public void b(int i, int j, int k, int l, int i1, int j1) {
        for(int k1 = 0; k1 < this.u.size(); ++k1) {
            ((IWorldAccess)this.u.get(k1)).a(i, j, k, l, i1, j1);
        }

    }

    public void applyPhysics(BlockPosition blockposition, Block block) {
        if(!this.captureBlockStates) {
            this.e(blockposition.west(), block);
            this.e(blockposition.east(), block);
            this.e(blockposition.down(), block);
            this.e(blockposition.up(), block);
            this.e(blockposition.north(), block);
            this.e(blockposition.south(), block);
        }
    }

    public void a(BlockPosition blockposition, Block block, EnumDirection enumdirection) {
        if(enumdirection != EnumDirection.WEST) {
            this.e(blockposition.west(), block);
        }

        if(enumdirection != EnumDirection.EAST) {
            this.e(blockposition.east(), block);
        }

        if(enumdirection != EnumDirection.DOWN) {
            this.e(blockposition.down(), block);
        }

        if(enumdirection != EnumDirection.UP) {
            this.e(blockposition.up(), block);
        }

        if(enumdirection != EnumDirection.NORTH) {
            this.e(blockposition.north(), block);
        }

        if(enumdirection != EnumDirection.SOUTH) {
            this.e(blockposition.south(), block);
        }

    }

    public void e(BlockPosition blockposition, final Block block) {
        if(!this.isClientSide) {
            IBlockData iblockdata = this.getType(blockposition);

            try {
                CraftWorld throwable = ((WorldServer)this).getWorld();
                if(throwable != null && !((WorldServer)this).stopPhysicsEvent) {
                    BlockPhysicsEvent crashreport1 = new BlockPhysicsEvent(throwable.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftMagicNumbers.getId(block));
                    this.getServer().getPluginManager().callEvent(crashreport1);
                    if(crashreport1.isCancelled()) {
                        return;
                    }
                }

                iblockdata.getBlock().doPhysics(this, blockposition, iblockdata, block);
            } catch (StackOverflowError var7) {
                haveWeSilencedAPhysicsCrash = true;
                blockLocation = blockposition.getX() + ", " + blockposition.getY() + ", " + blockposition.getZ();
            } catch (Throwable var8) {
                CrashReport crashreport = CrashReport.a(var8, "Exception while updating neighbours");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being updated");
                crashreportsystemdetails.a("Source block type", new Callable() {
                    public String a() throws Exception {
                        try {
                            return String.format("ID #%d (%s // %s)", new Object[]{Integer.valueOf(Block.getId(block)), block.a(), block.getClass().getCanonicalName()});
                        } catch (Throwable var2) {
                            return "ID #" + Block.getId(block);
                        }
                    }

                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                CrashReportSystemDetails.a(crashreportsystemdetails, blockposition, iblockdata);
                throw new ReportedException(crashreport);
            }
        }

    }

    public boolean a(BlockPosition blockposition, Block block) {
        return false;
    }

    public boolean h(BlockPosition blockposition) {
        return this.getChunkAtWorldCoords(blockposition).c(blockposition);
    }

    public boolean i(BlockPosition blockposition) {
        if(blockposition.getY() >= this.K()) {
            return this.h(blockposition);
        } else {
            BlockPosition blockposition1 = new BlockPosition(blockposition.getX(), this.K(), blockposition.getZ());
            if(!this.h(blockposition1)) {
                return false;
            } else {
                for(blockposition1 = blockposition1.down(); blockposition1.getY() > blockposition.getY(); blockposition1 = blockposition1.down()) {
                    IBlockData iblockdata = this.getType(blockposition1);
                    if(iblockdata.c() > 0 && !iblockdata.getMaterial().isLiquid()) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public int j(BlockPosition blockposition) {
        if(blockposition.getY() < 0) {
            return 0;
        } else {
            if(blockposition.getY() >= 256) {
                blockposition = new BlockPosition(blockposition.getX(), 255, blockposition.getZ());
            }

            return this.getChunkAtWorldCoords(blockposition).a(blockposition, 0);
        }
    }

    public boolean isLightLevel(BlockPosition blockposition, int level) {
        if(blockposition.isValidLocation()) {
            if(this.getType(blockposition).f()) {
                return this.c(blockposition.up(), false) >= level?true:(this.c(blockposition.east(), false) >= level?true:(this.c(blockposition.west(), false) >= level?true:(this.c(blockposition.south(), false) >= level?true:this.c(blockposition.north(), false) >= level)));
            } else {
                if(blockposition.getY() >= 256) {
                    blockposition = new BlockPosition(blockposition.getX(), 255, blockposition.getZ());
                }

                Chunk chunk = this.getChunkAtWorldCoords(blockposition);
                return chunk.a(blockposition, this.J) >= level;
            }
        } else {
            return true;
        }
    }

    public int getLightLevel(BlockPosition blockposition) {
        return this.c(blockposition, true);
    }

    public int c(BlockPosition blockposition, boolean flag) {
        if(blockposition.getX() >= -30000000 && blockposition.getZ() >= -30000000 && blockposition.getX() < 30000000 && blockposition.getZ() < 30000000) {
            if(flag && this.getType(blockposition).f()) {
                int chunk1 = this.c(blockposition.up(), false);
                int j = this.c(blockposition.east(), false);
                int k = this.c(blockposition.west(), false);
                int l = this.c(blockposition.south(), false);
                int i1 = this.c(blockposition.north(), false);
                if(j > chunk1) {
                    chunk1 = j;
                }

                if(k > chunk1) {
                    chunk1 = k;
                }

                if(l > chunk1) {
                    chunk1 = l;
                }

                if(i1 > chunk1) {
                    chunk1 = i1;
                }

                return chunk1;
            } else if(blockposition.getY() < 0) {
                return 0;
            } else {
                if(blockposition.getY() >= 256) {
                    blockposition = new BlockPosition(blockposition.getX(), 255, blockposition.getZ());
                }

                if(!this.isLoaded(blockposition)) {
                    return 0;
                } else {
                    Chunk chunk = this.getChunkAtWorldCoords(blockposition);
                    return chunk.a(blockposition, this.J);
                }
            }
        } else {
            return 15;
        }
    }

    public BlockPosition getHighestBlockYAt(BlockPosition blockposition) {
        int i;
        if(blockposition.getX() >= -30000000 && blockposition.getZ() >= -30000000 && blockposition.getX() < 30000000 && blockposition.getZ() < 30000000) {
            if(this.isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4, true)) {
                i = this.getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4).b(blockposition.getX() & 15, blockposition.getZ() & 15);
            } else {
                i = 0;
            }
        } else {
            i = this.K() + 1;
        }

        return new BlockPosition(blockposition.getX(), i, blockposition.getZ());
    }

    public int b(int i, int j) {
        if(i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if(!this.isChunkLoaded(i >> 4, j >> 4, true)) {
                return 0;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, j >> 4);
                return chunk.w();
            }
        } else {
            return this.K() + 1;
        }
    }

    public int b(EnumSkyBlock enumskyblock, BlockPosition blockposition) {
        if(blockposition.getY() < 0) {
            blockposition = new BlockPosition(blockposition.getX(), 0, blockposition.getZ());
        }

        if(!blockposition.isValidLocation()) {
            return enumskyblock.c;
        } else if(!this.isLoaded(blockposition)) {
            return enumskyblock.c;
        } else {
            Chunk chunk = this.getChunkAtWorldCoords(blockposition);
            return chunk.getBrightness(enumskyblock, blockposition);
        }
    }

    public void a(EnumSkyBlock enumskyblock, BlockPosition blockposition, int i) {
        if(blockposition.isValidLocation() && this.isLoaded(blockposition)) {
            Chunk chunk = this.getChunkAtWorldCoords(blockposition);
            chunk.a(enumskyblock, blockposition, i);
            this.m(blockposition);
        }

    }

    public void m(BlockPosition blockposition) {
        for(int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess)this.u.get(i)).a(blockposition);
        }

    }

    public float n(BlockPosition blockposition) {
        return this.worldProvider.n()[this.getLightLevel(blockposition)];
    }

    public IBlockData getTypeIfLoaded(BlockPosition blockposition) {
        if(this.captureTreeGeneration) {
            Iterator chunk = this.capturedBlockStates.iterator();

            while(chunk.hasNext()) {
                BlockState previous = (BlockState)chunk.next();
                if(previous.getX() == blockposition.getX() && previous.getY() == blockposition.getY() && previous.getZ() == blockposition.getZ()) {
                    return CraftMagicNumbers.getBlock(previous.getTypeId()).fromLegacyData(previous.getRawData());
                }
            }
        }

        Chunk chunk1 = this.getChunkIfLoaded(blockposition);
        return chunk1 != null?(blockposition.isValidLocation()?chunk1.getBlockData(blockposition):Blocks.AIR.getBlockData()):null;
    }

    public IBlockData getType(BlockPosition blockposition) {
        if(this.captureTreeGeneration) {
            Iterator chunk = this.capturedBlockStates.iterator();

            while(chunk.hasNext()) {
                BlockState previous = (BlockState)chunk.next();
                if(previous.getX() == blockposition.getX() && previous.getY() == blockposition.getY() && previous.getZ() == blockposition.getZ()) {
                    return CraftMagicNumbers.getBlock(previous.getTypeId()).fromLegacyData(previous.getRawData());
                }
            }
        }

        if(!blockposition.isValidLocation()) {
            return Blocks.AIR.getBlockData();
        } else {
            Chunk chunk1 = this.getChunkAtWorldCoords(blockposition);
            return chunk1.getBlockData(blockposition);
        }
    }

    public boolean B() {
        return this.J < 4;
    }

    public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1) {
        return this.rayTrace(vec3d, vec3d1, false, false, false);
    }

    public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1, boolean flag) {
        return this.rayTrace(vec3d, vec3d1, flag, false, false);
    }

    public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1, boolean flag, boolean flag1, boolean flag2) {
        if(!Double.isNaN(vec3d.x) && !Double.isNaN(vec3d.y) && !Double.isNaN(vec3d.z)) {
            if(!Double.isNaN(vec3d1.x) && !Double.isNaN(vec3d1.y) && !Double.isNaN(vec3d1.z)) {
                int i = MathHelper.floor(vec3d1.x);
                int j = MathHelper.floor(vec3d1.y);
                int k = MathHelper.floor(vec3d1.z);
                int l = MathHelper.floor(vec3d.x);
                int i1 = MathHelper.floor(vec3d.y);
                int j1 = MathHelper.floor(vec3d.z);
                BlockPosition blockposition = new BlockPosition(l, i1, j1);
                IBlockData iblockdata = this.getType(blockposition);
                Block block = iblockdata.getBlock();
                MovingObjectPosition movingobjectposition1;
                if((!flag1 || iblockdata.d(this, blockposition) != Block.k) && block.a(iblockdata, flag)) {
                    movingobjectposition1 = iblockdata.a(this, blockposition, vec3d, vec3d1);
                    if(movingobjectposition1 != null) {
                        return movingobjectposition1;
                    }
                }

                movingobjectposition1 = null;
                int k1 = 200;

                while(k1-- >= 0) {
                    if(Double.isNaN(vec3d.x) || Double.isNaN(vec3d.y) || Double.isNaN(vec3d.z)) {
                        return null;
                    }

                    if(l == i && i1 == j && j1 == k) {
                        return flag2?movingobjectposition1:null;
                    }

                    boolean flag3 = true;
                    boolean flag4 = true;
                    boolean flag5 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;
                    if(i > l) {
                        d0 = (double)l + 1.0D;
                    } else if(i < l) {
                        d0 = (double)l + 0.0D;
                    } else {
                        flag3 = false;
                    }

                    if(j > i1) {
                        d1 = (double)i1 + 1.0D;
                    } else if(j < i1) {
                        d1 = (double)i1 + 0.0D;
                    } else {
                        flag4 = false;
                    }

                    if(k > j1) {
                        d2 = (double)j1 + 1.0D;
                    } else if(k < j1) {
                        d2 = (double)j1 + 0.0D;
                    } else {
                        flag5 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec3d1.x - vec3d.x;
                    double d7 = vec3d1.y - vec3d.y;
                    double d8 = vec3d1.z - vec3d.z;
                    if(flag3) {
                        d3 = (d0 - vec3d.x) / d6;
                    }

                    if(flag4) {
                        d4 = (d1 - vec3d.y) / d7;
                    }

                    if(flag5) {
                        d5 = (d2 - vec3d.z) / d8;
                    }

                    if(d3 == -0.0D) {
                        d3 = -1.0E-4D;
                    }

                    if(d4 == -0.0D) {
                        d4 = -1.0E-4D;
                    }

                    if(d5 == -0.0D) {
                        d5 = -1.0E-4D;
                    }

                    EnumDirection enumdirection;
                    if(d3 < d4 && d3 < d5) {
                        enumdirection = i > l?EnumDirection.WEST:EnumDirection.EAST;
                        vec3d = new Vec3D(d0, vec3d.y + d7 * d3, vec3d.z + d8 * d3);
                    } else if(d4 < d5) {
                        enumdirection = j > i1?EnumDirection.DOWN:EnumDirection.UP;
                        vec3d = new Vec3D(vec3d.x + d6 * d4, d1, vec3d.z + d8 * d4);
                    } else {
                        enumdirection = k > j1?EnumDirection.NORTH:EnumDirection.SOUTH;
                        vec3d = new Vec3D(vec3d.x + d6 * d5, vec3d.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec3d.x) - (enumdirection == EnumDirection.EAST?1:0);
                    i1 = MathHelper.floor(vec3d.y) - (enumdirection == EnumDirection.UP?1:0);
                    j1 = MathHelper.floor(vec3d.z) - (enumdirection == EnumDirection.SOUTH?1:0);
                    blockposition = new BlockPosition(l, i1, j1);
                    IBlockData iblockdata1 = this.getType(blockposition);
                    Block block1 = iblockdata1.getBlock();
                    if(!flag1 || iblockdata1.getMaterial() == Material.PORTAL || iblockdata1.d(this, blockposition) != Block.k) {
                        if(block1.a(iblockdata1, flag)) {
                            MovingObjectPosition movingobjectposition2 = iblockdata1.a(this, blockposition, vec3d, vec3d1);
                            if(movingobjectposition2 != null) {
                                return movingobjectposition2;
                            }
                        } else {
                            movingobjectposition1 = new MovingObjectPosition(EnumMovingObjectType.MISS, vec3d, enumdirection, blockposition);
                        }
                    }
                }

                return flag2?movingobjectposition1:null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void a(EntityHuman entityhuman, BlockPosition blockposition, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
        this.a(entityhuman, (double)blockposition.getX() + 0.5D, (double)blockposition.getY() + 0.5D, (double)blockposition.getZ() + 0.5D, soundeffect, soundcategory, f, f1);
    }

    public void a(EntityHuman entityhuman, double d0, double d1, double d2, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
        for(int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess)this.u.get(i)).a(entityhuman, soundeffect, soundcategory, d0, d1, d2, f, f1);
        }

    }

    public void a(double d0, double d1, double d2, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1, boolean flag) {
    }

    public void a(BlockPosition blockposition, SoundEffect soundeffect) {
        for(int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess)this.u.get(i)).a(soundeffect, blockposition);
        }

    }

    public void addParticle(EnumParticle enumparticle, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
        this.a(enumparticle.c(), enumparticle.e(), d0, d1, d2, d3, d4, d5, aint);
    }

    private void a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {
        for(int j = 0; j < this.u.size(); ++j) {
            ((IWorldAccess)this.u.get(j)).a(i, flag, d0, d1, d2, d3, d4, d5, aint);
        }

    }

    public boolean strikeLightning(Entity entity) {
        this.j.add(entity);
        return true;
    }

    public boolean addEntity(Entity entity) {
        return this.addEntity(entity, SpawnReason.DEFAULT);
    }

    public boolean addEntity(Entity entity, SpawnReason spawnReason) {
        AsyncCatcher.catchOp("entity add");
        if(entity == null) {
            return false;
        } else {
            int i = MathHelper.floor(entity.locX / 16.0D);
            int j = MathHelper.floor(entity.locZ / 16.0D);
            boolean flag = entity.attachedToPlayer;
            if(entity.origin == null) {
                entity.origin = entity.getBukkitEntity().getLocation();
            }

            if(entity instanceof EntityHuman) {
                flag = true;
            }

            Object event = null;
            if(entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
                boolean entityhuman1 = entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal || entity instanceof EntityGolem;
                boolean isMonster = entity instanceof EntityMonster || entity instanceof EntityGhast || entity instanceof EntitySlime;
                if(spawnReason != SpawnReason.CUSTOM && (entityhuman1 && !this.allowAnimals || isMonster && !this.allowMonsters)) {
                    entity.dead = true;
                    return false;
                }

                event = CraftEventFactory.callCreatureSpawnEvent((EntityLiving)entity, spawnReason);
            } else if(entity instanceof EntityItem) {
                event = CraftEventFactory.callItemSpawnEvent((EntityItem)entity);
            } else if(entity.getBukkitEntity() instanceof Projectile) {
                event = CraftEventFactory.callProjectileLaunchEvent(entity);
            } else if(entity instanceof EntityExperienceOrb) {
                EntityExperienceOrb entityhuman = (EntityExperienceOrb)entity;
                double radius = this.spigotConfig.expMerge;
                if(radius > 0.0D) {
                    List entities = this.getEntities(entity, entity.getBoundingBox().grow(radius, radius, radius));
                    Iterator var12 = entities.iterator();

                    while(var12.hasNext()) {
                        Entity e = (Entity)var12.next();
                        if(e instanceof EntityExperienceOrb) {
                            EntityExperienceOrb loopItem = (EntityExperienceOrb)e;
                            if(!loopItem.dead) {
                                entityhuman.value += loopItem.value;
                                loopItem.die();
                            }
                        }
                    }
                }
            }

            if(event != null && (((Cancellable)event).isCancelled() || entity.dead)) {
                entity.dead = true;
                return false;
            } else if(!flag && !this.isChunkLoaded(i, j, false)) {
                return false;
            } else {
                if(entity instanceof EntityHuman) {
                    EntityHuman entityhuman2 = (EntityHuman)entity;
                    this.players.add(entityhuman2);
                    this.everyoneSleeping();
                }

                this.getChunkAt(i, j).a(entity);
                this.entityList.add(entity);
                this.b(entity);
                return true;
            }
        }
    }

    protected void b(Entity entity) {
        for(int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess)this.u.get(i)).a(entity);
        }

        entity.valid = true;
        (new EntityAddToWorldEvent(entity.getBukkitEntity())).callEvent();
    }

    protected void c(Entity entity) {
        for(int i = 0; i < this.u.size(); ++i) {
            ((IWorldAccess)this.u.get(i)).b(entity);
        }

        (new EntityRemoveFromWorldEvent(entity.getBukkitEntity())).callEvent();
        entity.valid = false;
    }

    public void kill(Entity entity) {
        if(entity.isVehicle()) {
            entity.az();
        }

        if(entity.isPassenger()) {
            entity.stopRiding();
        }

        entity.die();
        if(entity instanceof EntityHuman) {
            this.players.remove(entity);
            Iterator var2 = this.worldMaps.c.iterator();

            while(true) {
                Object o;
                do {
                    if(!var2.hasNext()) {
                        this.everyoneSleeping();
                        this.c(entity);
                        return;
                    }

                    o = var2.next();
                } while(!(o instanceof WorldMap));

                WorldMap map = (WorldMap)o;
                map.j.remove(entity);
                Iterator iter = map.h.iterator();

                while(iter.hasNext()) {
                    if(((WorldMapHumanTracker)iter.next()).trackee == entity) {
                        map.decorations.remove(entity.getUniqueID());
                        iter.remove();
                    }
                }
            }
        }
    }

    public void removeEntity(Entity entity) {
        AsyncCatcher.catchOp("entity remove");
        entity.b(false);
        entity.die();
        if(entity instanceof EntityHuman) {
            this.players.remove(entity);
            this.everyoneSleeping();
        }

        if(!this.guardEntityList) {
            int i = entity.ab;
            int j = entity.ad;
            if(entity.aa && this.isChunkLoaded(i, j, true)) {
                this.getChunkAt(i, j).b(entity);
            }

            int index = this.entityList.indexOf(entity);
            if(index != -1) {
                if(index <= this.tickPosition) {
                    --this.tickPosition;
                }

                this.entityList.remove(index);
            }
        }

        this.c(entity);
    }

    public NavigationListener C() {
        return this.t;
    }

    public void addIWorldAccess(IWorldAccess iworldaccess) {
        this.u.add(iworldaccess);
    }

    public List<AxisAlignedBB> getCubes(Entity entity, AxisAlignedBB axisalignedbb) {
        ArrayList arraylist = Lists.newArrayList();
        int i = MathHelper.floor(axisalignedbb.a) - 1;
        int j = MathHelper.f(axisalignedbb.d) + 1;
        int k = MathHelper.floor(axisalignedbb.b) - 1;
        int l = MathHelper.f(axisalignedbb.e) + 1;
        int i1 = MathHelper.floor(axisalignedbb.c) - 1;
        int j1 = MathHelper.f(axisalignedbb.f) + 1;
        WorldBorder worldborder = this.getWorldBorder();
        boolean flag = entity != null && entity.bo();
        boolean flag1 = entity != null && this.a(worldborder, entity);
        IBlockData iblockdata = Blocks.STONE.getBlockData();
        PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();

        int k1;
        for(int list = i; list < j; ++list) {
            for(k1 = i1; k1 < j1; ++k1) {
                int entity1 = (list != i && list != j - 1?0:1) + (k1 != i1 && k1 != j1 - 1?0:1);
                if(entity1 != 2 && this.isLoaded(blockposition_pooledblockposition.d(list, 64, k1))) {
                    for(int axisalignedbb1 = k; axisalignedbb1 < l; ++axisalignedbb1) {
                        if(entity1 <= 0 || axisalignedbb1 != k && axisalignedbb1 != l - 1) {
                            blockposition_pooledblockposition.d(list, axisalignedbb1, k1);
                            if(entity != null) {
                                if(flag && flag1) {
                                    entity.j(false);
                                } else if(!flag && !flag1) {
                                    entity.j(true);
                                }
                            }

                            IBlockData iblockdata1 = iblockdata;
                            if(worldborder.a(blockposition_pooledblockposition) || !flag1) {
                                iblockdata1 = this.getType(blockposition_pooledblockposition);
                            }

                            iblockdata1.a(this, blockposition_pooledblockposition, axisalignedbb, arraylist, entity);
                        }
                    }
                }
            }
        }

        blockposition_pooledblockposition.t();
        if(entity != null) {
            List var20 = this.getEntities(entity, axisalignedbb.g(0.25D));

            for(k1 = 0; k1 < var20.size(); ++k1) {
                Entity var21 = (Entity)var20.get(k1);
                if(!entity.x(var21)) {
                    AxisAlignedBB var22 = var21.af();
                    if(var22 != null && var22.b(axisalignedbb)) {
                        arraylist.add(var22);
                    }

                    var22 = entity.j(var21);
                    if(var22 != null && var22.b(axisalignedbb)) {
                        arraylist.add(var22);
                    }
                }
            }
        }

        return arraylist;
    }

    public boolean a(WorldBorder worldborder, Entity entity) {
        double d0 = worldborder.b();
        double d1 = worldborder.c();
        double d2 = worldborder.d();
        double d3 = worldborder.e();
        if(entity.bo()) {
            ++d0;
            ++d1;
            --d2;
            --d3;
        } else {
            --d0;
            --d1;
            ++d2;
            ++d3;
        }

        return entity.locX > d0 && entity.locX < d2 && entity.locZ > d1 && entity.locZ < d3;
    }

    public List<AxisAlignedBB> a(AxisAlignedBB axisalignedbb) {
        ArrayList arraylist = Lists.newArrayList();
        int i = MathHelper.floor(axisalignedbb.a) - 1;
        int j = MathHelper.f(axisalignedbb.d) + 1;
        int k = MathHelper.floor(axisalignedbb.b) - 1;
        int l = MathHelper.f(axisalignedbb.e) + 1;
        int i1 = MathHelper.floor(axisalignedbb.c) - 1;
        int j1 = MathHelper.f(axisalignedbb.f) + 1;
        PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = i1; l1 < j1; ++l1) {
                int i2 = (k1 != i && k1 != j - 1?0:1) + (l1 != i1 && l1 != j1 - 1?0:1);
                if(i2 != 2 && this.isLoaded(blockposition_pooledblockposition.d(k1, 64, l1))) {
                    for(int j2 = k; j2 < l; ++j2) {
                        if(i2 <= 0 || j2 != k && j2 != l - 1) {
                            blockposition_pooledblockposition.d(k1, j2, l1);
                            IBlockData iblockdata;
                            if(k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000) {
                                iblockdata = this.getType(blockposition_pooledblockposition);
                            } else {
                                iblockdata = Blocks.BEDROCK.getBlockData();
                            }

                            iblockdata.a(this, blockposition_pooledblockposition, axisalignedbb, arraylist, (Entity)null);
                        }
                    }
                }
            }
        }

        blockposition_pooledblockposition.t();
        return arraylist;
    }

    public boolean b(AxisAlignedBB axisalignedbb) {
        ArrayList arraylist = Lists.newArrayList();
        int i = MathHelper.floor(axisalignedbb.a) - 1;
        int j = MathHelper.f(axisalignedbb.d) + 1;
        int k = MathHelper.floor(axisalignedbb.b) - 1;
        int l = MathHelper.f(axisalignedbb.e) + 1;
        int i1 = MathHelper.floor(axisalignedbb.c) - 1;
        int j1 = MathHelper.f(axisalignedbb.f) + 1;
        PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();

        try {
            for(int k1 = i; k1 < j; ++k1) {
                for(int l1 = i1; l1 < j1; ++l1) {
                    int i2 = (k1 != i && k1 != j - 1?0:1) + (l1 != i1 && l1 != j1 - 1?0:1);
                    if(i2 != 2 && this.isLoaded(blockposition_pooledblockposition.d(k1, 64, l1))) {
                        for(int j2 = k; j2 < l; ++j2) {
                            if(i2 <= 0 || j2 != k && j2 != l - 1) {
                                blockposition_pooledblockposition.d(k1, j2, l1);
                                boolean flag1;
                                if(k1 < -30000000 || k1 >= 30000000 || l1 < -30000000 || l1 >= 30000000) {
                                    boolean var21 = true;
                                    flag1 = var21;
                                    return flag1;
                                }

                                IBlockData iblockdata = this.getType(blockposition_pooledblockposition);
                                iblockdata.a(this, blockposition_pooledblockposition, axisalignedbb, arraylist, (Entity)null);
                                if(!arraylist.isEmpty()) {
                                    flag1 = true;
                                    boolean var16 = flag1;
                                    return var16;
                                }
                            }
                        }
                    }
                }
            }

            boolean var20 = false;
            return var20;
        } finally {
            blockposition_pooledblockposition.t();
        }
    }

    public int a(float f) {
        float f1 = this.c(f);
        float f2 = 1.0F - (MathHelper.cos(f1 * 6.2831855F) * 2.0F + 0.5F);
        f2 = MathHelper.a(f2, 0.0F, 1.0F);
        f2 = 1.0F - f2;
        f2 = (float)((double)f2 * (1.0D - (double)(this.j(f) * 5.0F) / 16.0D));
        f2 = (float)((double)f2 * (1.0D - (double)(this.h(f) * 5.0F) / 16.0D));
        f2 = 1.0F - f2;
        return (int)(f2 * 11.0F);
    }

    public float c(float f) {
        return this.worldProvider.a(this.worldData.getDayTime(), f);
    }

    public float E() {
        return WorldProvider.a[this.worldProvider.a(this.worldData.getDayTime())];
    }

    public float d(float f) {
        float f1 = this.c(f);
        return f1 * 6.2831855F;
    }

    public BlockPosition p(BlockPosition blockposition) {
        return this.getChunkAtWorldCoords(blockposition).f(blockposition);
    }

    public BlockPosition q(BlockPosition blockposition) {
        Chunk chunk = this.getChunkAtWorldCoords(blockposition);

        BlockPosition blockposition1;
        BlockPosition blockposition2;
        for(blockposition1 = new BlockPosition(blockposition.getX(), chunk.g() + 16, blockposition.getZ()); blockposition1.getY() >= 0; blockposition1 = blockposition2) {
            blockposition2 = blockposition1.down();
            Material material = chunk.getBlockData(blockposition2).getMaterial();
            if(material.isSolid() && material != Material.LEAVES) {
                break;
            }
        }

        return blockposition1;
    }

    public boolean b(BlockPosition blockposition, Block block) {
        return true;
    }

    public void a(BlockPosition blockposition, Block block, int i) {
    }

    public void a(BlockPosition blockposition, Block block, int i, int j) {
    }

    public void b(BlockPosition blockposition, Block block, int i, int j) {
    }

    int nexttick = 0;
    public void tickEntities() {
        nexttick++;
        if(nexttick >= 20){
            nexttick = 0;
        }
        this.methodProfiler.a("entities");
        this.methodProfiler.a("global");

        Entity entity;
        for(int i = 0; i < this.j.size(); ++i) {
            entity = (Entity)this.j.get(i);
            if(entity != null) {
                try {
                    ++entity.ticksLived;
                    entity.m();
                } catch (Throwable var17) {
                    CrashReport crashreport = CrashReport.a(var17, "Ticking entity");
                    CrashReportSystemDetails e = crashreport.a("Entity being ticked");
                    if(entity == null) {
                        e.a("Entity", "~~NULL~~");
                    } else {
                        entity.appendEntityCrashDetails(e);
                    }

                    throw new ReportedException(crashreport);
                }

                if(entity.dead) {
                    this.j.remove(i--);
                }
            }
        }

        this.methodProfiler.c("remove");
        this.timings.entityRemoval.startTiming();
        this.entityList.removeAll(this.f);
        Iterator var21 = this.f.iterator();

        int j;
        Entity var22;
        while(var21.hasNext()) {
            var22 = (Entity)var21.next();
            j = var22.getChunkX();
            int entitiesThisCycle = var22.getChunkZ();
            if(var22.aa && this.isChunkLoaded(entitiesThisCycle, j, true)) {
                this.getChunkAt(entitiesThisCycle, j).b(var22);
            }
        }

        var21 = this.f.iterator();

        while(var21.hasNext()) {
            var22 = (Entity)var21.next();
            this.c(var22);
        }

        this.f.clear();
        this.l();
        this.timings.entityRemoval.stopTiming();
        this.methodProfiler.c("regular");
        ActivationRange.activateEntities(this);
        this.timings.entityTick.startTiming();
        this.guardEntityList = true;
        TimingHistory.entityTicks += (long)this.entityList.size();
        boolean var23 = false;

        int i1;
        for(this.tickPosition = 0; this.tickPosition < this.entityList.size(); ++this.tickPosition) {
            this.tickPosition = this.tickPosition < this.entityList.size()?this.tickPosition:0;
            entity = (Entity)this.entityList.get(this.tickPosition);

            if(nexttick != 0){
                if(entity.getName().equals("entity.ItemFrame.name")
                        || entity.getName().equals("entity.EnderCrystal.name")
                        || entity.getName().equals("Painting")){
                    continue;
                }}
            if(random.nextBoolean()){
                if(entity instanceof EntityAnimal){
                    if(entity instanceof EntityChicken){
                        ((EntityChicken)entity).bB-=4;
                    }
                    ((EntityAnimal)entity).maxNoDamageTicks = 1;
                    ((EntityAnimal) entity).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.28D);
                }else if(entity instanceof EntityMonster){
                    ((EntityMonster)entity).maxNoDamageTicks = 1;
                    ((EntityMonster) entity).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.28D);
                }
                continue;
            }

            Entity tilesThisCycle = entity.by();
            if(tilesThisCycle != null) {
                if(!tilesThisCycle.dead && tilesThisCycle.w(entity)) {
                    continue;
                }

                entity.stopRiding();
            }

            this.methodProfiler.a("tick");
            if(!entity.dead && !(entity instanceof EntityPlayer)) {
                try {
                    entity.tickTimer.startTiming();
                    this.g(entity);
                    entity.tickTimer.stopTiming();
                } catch (Throwable var20) {
                    entity.tickTimer.stopTiming();
                    String tileentity1 = "Entity threw exception at " + entity.world.getWorld().getName() + ":" + entity.locX + "," + entity.locY + "," + entity.locZ;
                    System.err.println(tileentity1);
                    var20.printStackTrace();
                    this.getServer().getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(tileentity1, var20)));
                    entity.dead = true;
                    continue;
                }
            }

            this.methodProfiler.b();
            this.methodProfiler.a("remove");
            if(entity.dead) {
                j = entity.ab;
                i1 = entity.ad;
                if(entity.aa && this.isChunkLoaded(j, i1, true)) {
                    this.getChunkAt(j, i1).b(entity);
                }

                this.guardEntityList = false;
                this.entityList.remove(this.tickPosition--);
                this.guardEntityList = true;
                this.c(entity);
            }

            this.methodProfiler.b();
        }

        this.guardEntityList = false;
        this.timings.entityTick.stopTiming();
        this.methodProfiler.c("blockEntities");
        this.M = true;
        if(!this.tileEntityListUnload.isEmpty()) {
            this.tileEntityListTick.removeAll(this.tileEntityListUnload);
            this.tileEntityListUnload.clear();
        }

        int var24 = 0;

        for(this.tileTickPosition = 0; this.tileTickPosition < this.tileEntityListTick.size(); ++this.tileTickPosition) {
            this.tileTickPosition = this.tileTickPosition < this.tileEntityListTick.size()?this.tileTickPosition:0;
            TileEntity var25 = (TileEntity)this.tileEntityListTick.get(this.tileTickPosition);
            if(var25 == null) {
                this.getServer().getLogger().severe("Spigot has detected a null entity and has removed it, preventing a crash");
                --var24;
                this.tileEntityListTick.remove(this.tileTickPosition--);
            } else {
                if(!var25.x() && var25.t()) {
                    BlockPosition var26 = var25.getPosition();
                    if(this.isLoaded(var26) && this.N.a(var26)) {
                        try {
                            this.methodProfiler.a("");
                            var25.tickTimer.startTiming();
                            ((ITickable)var25).c();
                            this.methodProfiler.b();
                        } catch (Throwable var18) {
                            String iblockdata = "TileEntity threw exception at " + var25.world.getWorld().getName() + ":" + var25.position.getX() + "," + var25.position.getY() + "," + var25.position.getZ();
                            System.err.println(iblockdata);
                            var18.printStackTrace();
                            this.getServer().getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(iblockdata, var18)));
                            --var24;
                            this.tileEntityListTick.remove(this.tileTickPosition--);
                            continue;
                        } finally {
                            var25.tickTimer.stopTiming();
                        }
                    }
                }

                if(var25.x()) {
                    --var24;
                    this.tileEntityListTick.remove(this.tileTickPosition--);
                    if(this.isLoaded(var25.getPosition())) {
                        this.getChunkAtWorldCoords(var25.getPosition()).d(var25.getPosition());
                    }
                }
            }
        }

        this.timings.tileEntityTick.stopTiming();
        this.timings.tileEntityPending.startTiming();
        this.M = false;
        this.methodProfiler.c("pendingBlockEntities");
        if(!this.b.isEmpty()) {
            for(i1 = 0; i1 < this.b.size(); ++i1) {
                TileEntity var27 = (TileEntity)this.b.get(i1);
                if(!var27.x() && this.isLoaded(var27.getPosition())) {
                    Chunk chunk = this.getChunkAtWorldCoords(var27.getPosition());
                    IBlockData var28 = chunk.getBlockData(var27.getPosition());
                    chunk.a(var27.getPosition(), var27);
                    this.notify(var27.getPosition(), var28, var28, 3);
                    this.a(var27);
                }
            }

            this.b.clear();
        }

        this.timings.tileEntityPending.stopTiming();
        TimingHistory.tileEntityTicks += (long)this.tileEntityListTick.size();
        this.methodProfiler.b();
        this.methodProfiler.b();
    }

    protected void l() {
    }

    public boolean a(TileEntity tileentity) {
        boolean flag = true;
        if(flag && tileentity instanceof ITickable && !this.tileEntityListTick.contains(tileentity)) {
            this.tileEntityListTick.add(tileentity);
        }

        return flag;
    }

    public void b(Collection<TileEntity> collection) {
        if(this.M) {
            this.b.addAll(collection);
        } else {
            Iterator iterator = collection.iterator();

            while(iterator.hasNext()) {
                TileEntity tileentity = (TileEntity)iterator.next();
                this.a(tileentity);
            }
        }

    }

    public void g(Entity entity) {
        this.entityJoinedWorld(entity, true);
    }

    public void entityJoinedWorld(Entity entity, boolean flag) {
        int i = MathHelper.floor(entity.locX);
        int j = MathHelper.floor(entity.locZ);
        boolean b0 = true;
        if(flag && !ActivationRange.checkIfActive(entity)) {
            ++entity.ticksLived;
            entity.inactiveTick();
        } else {
            entity.M = entity.locX;
            entity.N = entity.locY;
            entity.O = entity.locZ;
            entity.lastYaw = entity.yaw;
            entity.lastPitch = entity.pitch;
            if(flag && entity.aa) {
                ++entity.ticksLived;
                ++TimingHistory.activatedEntityTicks;
                if(entity.isPassenger()) {
                    entity.aw();
                } else {
                    entity.m();
                }
            }

            this.methodProfiler.a("chunkCheck");
            if(Double.isNaN(entity.locX) || Double.isInfinite(entity.locX)) {
                entity.locX = entity.M;
            }

            if(Double.isNaN(entity.locY) || Double.isInfinite(entity.locY)) {
                entity.locY = entity.N;
            }

            if(Double.isNaN(entity.locZ) || Double.isInfinite(entity.locZ)) {
                entity.locZ = entity.O;
            }

            if(Double.isNaN((double)entity.pitch) || Double.isInfinite((double)entity.pitch)) {
                entity.pitch = entity.lastPitch;
            }

            if(Double.isNaN((double)entity.yaw) || Double.isInfinite((double)entity.yaw)) {
                entity.yaw = entity.lastYaw;
            }

            int k = MathHelper.floor(entity.locX / 16.0D);
            int l = Math.min(15, Math.max(0, MathHelper.floor(entity.locY / 16.0D)));
            int i1 = MathHelper.floor(entity.locZ / 16.0D);
            if(!entity.aa || entity.ab != k || entity.ac != l || entity.ad != i1) {
                if(entity.aa && this.isChunkLoaded(entity.ab, entity.ad, true)) {
                    this.getChunkAt(entity.ab, entity.ad).a(entity, entity.ac);
                }

                if(!entity.bs() && !this.isChunkLoaded(k, i1, true)) {
                    entity.aa = false;
                } else {
                    this.getChunkAt(k, i1).a(entity);
                }
            }

            this.methodProfiler.b();
            if(flag && entity.aa) {
                Iterator iterator = entity.bu().iterator();

                while(true) {
                    while(iterator.hasNext()) {
                        Entity entity1 = (Entity)iterator.next();
                        if(!entity1.dead && entity1.by() == entity) {
                            this.g(entity1);
                        } else {
                            entity1.stopRiding();
                        }
                    }

                    return;
                }
            }
        }

    }

    public boolean c(AxisAlignedBB axisalignedbb) {
        return this.a(axisalignedbb, (Entity)null);
    }

    public boolean a(AxisAlignedBB axisalignedbb, Entity entity) {
        List list = this.getEntities((Entity)null, axisalignedbb);

        for(int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity)list.get(i);
            if((!(entity instanceof EntityPlayer) || !(entity1 instanceof EntityPlayer) || ((EntityPlayer)entity).getBukkitEntity().canSee(((EntityPlayer)entity1).getBukkitEntity())) && !entity1.dead && entity1.i && entity1 != entity && (entity == null || entity1.x(entity))) {
                return false;
            }
        }

        return true;
    }

    public boolean d(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.f(axisalignedbb.e);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    IBlockData iblockdata = this.getType(blockposition_pooledblockposition.d(k1, l1, i2));
                    if(iblockdata.getMaterial() != Material.AIR) {
                        blockposition_pooledblockposition.t();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.t();
        return false;
    }

    public boolean containsLiquid(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.f(axisalignedbb.e);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    IBlockData iblockdata = this.getType(blockposition_pooledblockposition.d(k1, l1, i2));
                    if(iblockdata.getMaterial().isLiquid()) {
                        blockposition_pooledblockposition.t();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.t();
        return false;
    }

    public boolean f(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.f(axisalignedbb.e);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        if(this.isAreaLoaded(i, k, i1, j, l, j1, true)) {
            PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();
            int k1 = i;

            while(true) {
                if(k1 >= j) {
                    blockposition_pooledblockposition.t();
                    break;
                }

                for(int l1 = k; l1 < l; ++l1) {
                    for(int i2 = i1; i2 < j1; ++i2) {
                        Block block = this.getType(blockposition_pooledblockposition.d(k1, l1, i2)).getBlock();
                        if(block == Blocks.FIRE || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA) {
                            blockposition_pooledblockposition.t();
                            return true;
                        }
                    }
                }

                ++k1;
            }
        }

        return false;
    }

    public boolean a(AxisAlignedBB axisalignedbb, Material material, Entity entity) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.f(axisalignedbb.e);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        if(!this.isAreaLoaded(i, k, i1, j, l, j1, true)) {
            return false;
        } else {
            boolean flag = false;
            Vec3D vec3d = Vec3D.a;
            PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();

            for(int k1 = i; k1 < j; ++k1) {
                for(int l1 = k; l1 < l; ++l1) {
                    for(int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.d(k1, l1, i2);
                        IBlockData iblockdata = this.getType(blockposition_pooledblockposition);
                        Block block = iblockdata.getBlock();
                        if(iblockdata.getMaterial() == material) {
                            double d0 = (double)((float)(l1 + 1) - BlockFluids.e(((Integer)iblockdata.get(BlockFluids.LEVEL)).intValue()));
                            if((double)l >= d0) {
                                flag = true;
                                vec3d = block.a(this, blockposition_pooledblockposition, entity, vec3d);
                            }
                        }
                    }
                }
            }

            blockposition_pooledblockposition.t();
            if(vec3d.b() > 0.0D && entity.bd()) {
                vec3d = vec3d.a();
                double d1 = 0.014D;
                entity.motX += vec3d.x * d1;
                entity.motY += vec3d.y * d1;
                entity.motZ += vec3d.z * d1;
            }

            return flag;
        }
    }

    public boolean a(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.f(axisalignedbb.e);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    if(this.getType(blockposition_pooledblockposition.d(k1, l1, i2)).getMaterial() == material) {
                        blockposition_pooledblockposition.t();
                        return true;
                    }
                }
            }
        }

        blockposition_pooledblockposition.t();
        return false;
    }

    public boolean b(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.f(axisalignedbb.e);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    IBlockData iblockdata = this.getType(blockposition_pooledblockposition.d(k1, l1, i2));
                    if(iblockdata.getMaterial() == material) {
                        int j2 = ((Integer)iblockdata.get(BlockFluids.LEVEL)).intValue();
                        double d0 = (double)(l1 + 1);
                        if(j2 < 8) {
                            d0 = (double)(l1 + 1) - (double)j2 / 8.0D;
                        }

                        if(d0 >= axisalignedbb.b) {
                            blockposition_pooledblockposition.t();
                            return true;
                        }
                    }
                }
            }
        }

        blockposition_pooledblockposition.t();
        return false;
    }

    public Explosion explode(Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        return this.createExplosion(entity, d0, d1, d2, f, false, flag);
    }

    public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        Explosion explosion = new Explosion(this, entity, d0, d1, d2, f, flag, flag1);
        explosion.a();
        explosion.a(true);
        return explosion;
    }

    public float a(Vec3D vec3d, AxisAlignedBB axisalignedbb) {
        double d0 = 1.0D / ((axisalignedbb.d - axisalignedbb.a) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.e - axisalignedbb.b) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.f - axisalignedbb.c) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if(d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
            int i = 0;
            int j = 0;

            for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
                for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
                    for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                        double d5 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double)f;
                        double d6 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double)f1;
                        double d7 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double)f2;
                        if(this.rayTrace(new Vec3D(d5 + d3, d6, d7 + d4), vec3d) == null) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    public boolean douseFire(EntityHuman entityhuman, BlockPosition blockposition, EnumDirection enumdirection) {
        blockposition = blockposition.shift(enumdirection);
        if(this.getType(blockposition).getBlock() == Blocks.FIRE) {
            this.a(entityhuman, 1009, blockposition, 0);
            this.setAir(blockposition);
            return true;
        } else {
            return false;
        }
    }

    public TileEntity getTileEntity(BlockPosition blockposition) {
        if(!blockposition.isValidLocation()) {
            return null;
        } else if(this.capturedTileEntities.containsKey(blockposition)) {
            return (TileEntity)this.capturedTileEntities.get(blockposition);
        } else {
            TileEntity tileentity = null;
            int i;
            TileEntity tileentity1;
            if(this.M) {
                for(i = 0; i < this.b.size(); ++i) {
                    tileentity1 = (TileEntity)this.b.get(i);
                    if(!tileentity1.x() && tileentity1.getPosition().equals(blockposition)) {
                        tileentity = tileentity1;
                        break;
                    }
                }
            }

            if(tileentity == null) {
                tileentity = this.getChunkAtWorldCoords(blockposition).a(blockposition, EnumTileEntityState.IMMEDIATE);
            }

            if(tileentity == null) {
                for(i = 0; i < this.b.size(); ++i) {
                    tileentity1 = (TileEntity)this.b.get(i);
                    if(!tileentity1.x() && tileentity1.getPosition().equals(blockposition)) {
                        tileentity = tileentity1;
                        break;
                    }
                }
            }

            return tileentity;
        }
    }

    public void setTileEntity(BlockPosition blockposition, TileEntity tileentity) {
        if(tileentity != null && !tileentity.x()) {
            if(this.captureBlockStates) {
                tileentity.a(this);
                tileentity.a(blockposition);
                this.capturedTileEntities.put(blockposition, tileentity);
                return;
            }

            if(this.M) {
                tileentity.a(blockposition);
                Iterator iterator = this.b.iterator();

                while(iterator.hasNext()) {
                    TileEntity tileentity1 = (TileEntity)iterator.next();
                    if(tileentity1.getPosition().equals(blockposition)) {
                        tileentity1.y();
                        iterator.remove();
                    }
                }

                tileentity.a(this);
                this.b.add(tileentity);
            } else {
                this.a(tileentity);
                this.getChunkAtWorldCoords(blockposition).a(blockposition, tileentity);
            }
        }

    }

    public void s(BlockPosition blockposition) {
        TileEntity tileentity = this.getTileEntity(blockposition);
        if(tileentity != null && this.M) {
            tileentity.y();
            this.b.remove(tileentity);
        } else {
            if(tileentity != null) {
                this.b.remove(tileentity);
                this.tileEntityListTick.remove(tileentity);
            }

            this.getChunkAtWorldCoords(blockposition).d(blockposition);
        }

    }

    public void b(TileEntity tileentity) {
        this.tileEntityListUnload.add(tileentity);
    }

    public boolean t(BlockPosition blockposition) {
        AxisAlignedBB axisalignedbb = this.getType(blockposition).d(this, blockposition);
        return axisalignedbb != Block.k && axisalignedbb.a() >= 1.0D;
    }

    public boolean d(BlockPosition blockposition, boolean flag) {
        if(!blockposition.isValidLocation()) {
            return flag;
        } else {
            Chunk chunk = this.chunkProvider.getLoadedChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
            if(chunk != null && !chunk.isEmpty()) {
                IBlockData iblockdata = this.getType(blockposition);
                return iblockdata.getMaterial().k() && iblockdata.h();
            } else {
                return flag;
            }
        }
    }

    public void H() {
        int i = this.a(1.0F);
        if(i != this.J) {
            this.J = i;
        }

    }

    public void setSpawnFlags(boolean flag, boolean flag1) {
        this.allowMonsters = flag;
        this.allowAnimals = flag1;
    }

    public void doTick() {
        this.t();
    }

    protected void I() {
        if(this.worldData.hasStorm()) {
            this.o = 1.0F;
            if(this.worldData.isThundering()) {
                this.q = 1.0F;
            }
        }

    }

    protected void t() {
        if(!this.worldProvider.m() && !this.isClientSide) {
            int i = this.worldData.z();
            if(i > 0) {
                --i;
                this.worldData.i(i);
                this.worldData.setThunderDuration(this.worldData.isThundering()?1:2);
                this.worldData.setWeatherDuration(this.worldData.hasStorm()?1:2);
            }

            int j = this.worldData.getThunderDuration();
            if(j <= 0) {
                if(this.worldData.isThundering()) {
                    this.worldData.setThunderDuration(this.random.nextInt(12000) + 3600);
                } else {
                    this.worldData.setThunderDuration(this.random.nextInt(168000) + 12000);
                }
            } else {
                --j;
                this.worldData.setThunderDuration(j);
                if(j <= 0) {
                    this.worldData.setThundering(!this.worldData.isThundering());
                }
            }

            this.p = this.q;
            if(this.worldData.isThundering()) {
                this.q = (float)((double)this.q + 0.01D);
            } else {
                this.q = (float)((double)this.q - 0.01D);
            }

            this.q = MathHelper.a(this.q, 0.0F, 1.0F);
            int k = this.worldData.getWeatherDuration();
            if(k <= 0) {
                if(this.worldData.hasStorm()) {
                    this.worldData.setWeatherDuration(this.random.nextInt(12000) + 12000);
                } else {
                    this.worldData.setWeatherDuration(this.random.nextInt(168000) + 12000);
                }
            } else {
                --k;
                this.worldData.setWeatherDuration(k);
                if(k <= 0) {
                    this.worldData.setStorm(!this.worldData.hasStorm());
                }
            }

            this.n = this.o;
            if(this.worldData.hasStorm()) {
                this.o = (float)((double)this.o + 0.01D);
            } else {
                this.o = (float)((double)this.o - 0.01D);
            }

            this.o = MathHelper.a(this.o, 0.0F, 1.0F);

            for(int idx = 0; idx < this.players.size(); ++idx) {
                if(((EntityPlayer)this.players.get(idx)).world == this) {
                    ((EntityPlayer)this.players.get(idx)).tickWeather();
                }
            }
        }

    }

    protected void j() {
    }

    public void a(Block block, BlockPosition blockposition, Random random) {
        this.d = true;
        block.b(this, blockposition, this.getType(blockposition), random);
        this.d = false;
    }

    public boolean u(BlockPosition blockposition) {
        return this.e(blockposition, false);
    }

    public boolean v(BlockPosition blockposition) {
        return this.e(blockposition, true);
    }

    public boolean e(BlockPosition blockposition, boolean flag) {
        BiomeBase biomebase = this.getBiome(blockposition);
        float f = biomebase.a(blockposition);
        if(f > 0.15F) {
            return false;
        } else {
            if(blockposition.getY() >= 0 && blockposition.getY() < 256 && this.b(EnumSkyBlock.BLOCK, blockposition) < 10) {
                IBlockData iblockdata = this.getType(blockposition);
                Block block = iblockdata.getBlock();
                if((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && ((Integer)iblockdata.get(BlockFluids.LEVEL)).intValue() == 0) {
                    if(!flag) {
                        return true;
                    }

                    boolean flag1 = this.E(blockposition.west()) && this.E(blockposition.east()) && this.E(blockposition.north()) && this.E(blockposition.south());
                    if(!flag1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private boolean E(BlockPosition blockposition) {
        return this.getType(blockposition).getMaterial() == Material.WATER;
    }

    public boolean f(BlockPosition blockposition, boolean flag) {
        BiomeBase biomebase = this.getBiome(blockposition);
        float f = biomebase.a(blockposition);
        if(f > 0.15F) {
            return false;
        } else if(!flag) {
            return true;
        } else {
            if(blockposition.getY() >= 0 && blockposition.getY() < 256 && this.b(EnumSkyBlock.BLOCK, blockposition) < 10) {
                IBlockData iblockdata = this.getType(blockposition);
                if(iblockdata.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlace(this, blockposition)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean w(BlockPosition blockposition) {
        boolean flag = false;
        if(!this.worldProvider.m()) {
            flag |= this.c(EnumSkyBlock.SKY, blockposition);
        }

        flag |= this.c(EnumSkyBlock.BLOCK, blockposition);
        return flag;
    }

    private int a(BlockPosition blockposition, EnumSkyBlock enumskyblock) {
        if(enumskyblock == EnumSkyBlock.SKY && this.h(blockposition)) {
            return 15;
        } else {
            IBlockData iblockdata = this.getType(blockposition);
            int i = enumskyblock == EnumSkyBlock.SKY?0:iblockdata.d();
            int j = iblockdata.c();
            if(j >= 15 && iblockdata.d() > 0) {
                j = 1;
            }

            if(j < 1) {
                j = 1;
            }

            if(j >= 15) {
                return 0;
            } else if(i >= 14) {
                return i;
            } else {
                PooledBlockPosition blockposition_pooledblockposition = PooledBlockPosition.s();
                EnumDirection[] aenumdirection = EnumDirection.values();
                int k = aenumdirection.length;

                for(int l = 0; l < k; ++l) {
                    EnumDirection enumdirection = aenumdirection[l];
                    blockposition_pooledblockposition.h(blockposition).c(enumdirection);
                    int i1 = this.b((EnumSkyBlock)enumskyblock, (BlockPosition)blockposition_pooledblockposition) - j;
                    if(i1 > i) {
                        i = i1;
                    }

                    if(i >= 14) {
                        return i;
                    }
                }

                blockposition_pooledblockposition.t();
                return i;
            }
        }
    }

    public boolean c(EnumSkyBlock enumskyblock, BlockPosition blockposition) {
        Chunk chunk = this.getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
        if(chunk != null && chunk.areNeighborsLoaded(1)) {
            int i = 0;
            int j = 0;
            this.methodProfiler.a("getBrightness");
            int k = this.b(enumskyblock, blockposition);
            int l = this.a(blockposition, enumskyblock);
            int i1 = blockposition.getX();
            int j1 = blockposition.getY();
            int k1 = blockposition.getZ();
            int l1;
            int i2;
            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;
            if(l > k) {
                this.H[j++] = 133152;
            } else if(l < k) {
                this.H[j++] = 133152 | k << 18;

                label92:
                while(true) {
                    int blockposition2;
                    do {
                        do {
                            BlockPosition k5;
                            do {
                                if(i >= j) {
                                    i = 0;
                                    break label92;
                                }

                                l1 = this.H[i++];
                                i2 = (l1 & 63) - 32 + i1;
                                j2 = (l1 >> 6 & 63) - 32 + j1;
                                k2 = (l1 >> 12 & 63) - 32 + k1;
                                blockposition2 = l1 >> 18 & 15;
                                k5 = new BlockPosition(i2, j2, k2);
                                l2 = this.b(enumskyblock, k5);
                            } while(l2 != blockposition2);

                            this.a((EnumSkyBlock)enumskyblock, (BlockPosition)k5, 0);
                        } while(blockposition2 <= 0);

                        i3 = MathHelper.a(i2 - i1);
                        j3 = MathHelper.a(j2 - j1);
                        k3 = MathHelper.a(k2 - k1);
                    } while(i3 + j3 + k3 >= 17);

                    PooledBlockPosition flag = PooledBlockPosition.s();
                    EnumDirection[] aenumdirection = EnumDirection.values();
                    int i4 = aenumdirection.length;

                    for(int j4 = 0; j4 < i4; ++j4) {
                        EnumDirection enumdirection = aenumdirection[j4];
                        int k4 = i2 + enumdirection.getAdjacentX();
                        int l4 = j2 + enumdirection.getAdjacentY();
                        int i5 = k2 + enumdirection.getAdjacentZ();
                        flag.d(k4, l4, i5);
                        int j5 = Math.max(1, this.getType(flag).c());
                        l2 = this.b((EnumSkyBlock)enumskyblock, (BlockPosition)flag);
                        if(l2 == blockposition2 - j5 && j < this.H.length) {
                            this.H[j++] = k4 - i1 + 32 | l4 - j1 + 32 << 6 | i5 - k1 + 32 << 12 | blockposition2 - j5 << 18;
                        }
                    }

                    flag.t();
                }
            }

            this.methodProfiler.b();
            this.methodProfiler.a("checkedPosition < toCheckCount");

            while(i < j) {
                l1 = this.H[i++];
                i2 = (l1 & 63) - 32 + i1;
                j2 = (l1 >> 6 & 63) - 32 + j1;
                k2 = (l1 >> 12 & 63) - 32 + k1;
                BlockPosition var30 = new BlockPosition(i2, j2, k2);
                int var31 = this.b(enumskyblock, var30);
                l2 = this.a(var30, enumskyblock);
                if(l2 != var31) {
                    this.a(enumskyblock, var30, l2);
                    if(l2 > var31) {
                        i3 = Math.abs(i2 - i1);
                        j3 = Math.abs(j2 - j1);
                        k3 = Math.abs(k2 - k1);
                        boolean var32 = j < this.H.length - 6;
                        if(i3 + j3 + k3 < 17 && var32) {
                            if(this.b(enumskyblock, var30.west()) < l2) {
                                this.H[j++] = i2 - 1 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if(this.b(enumskyblock, var30.east()) < l2) {
                                this.H[j++] = i2 + 1 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if(this.b(enumskyblock, var30.down()) < l2) {
                                this.H[j++] = i2 - i1 + 32 + (j2 - 1 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if(this.b(enumskyblock, var30.up()) < l2) {
                                this.H[j++] = i2 - i1 + 32 + (j2 + 1 - j1 + 32 << 6) + (k2 - k1 + 32 << 12);
                            }

                            if(this.b(enumskyblock, var30.north()) < l2) {
                                this.H[j++] = i2 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - 1 - k1 + 32 << 12);
                            }

                            if(this.b(enumskyblock, var30.south()) < l2) {
                                this.H[j++] = i2 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 + 1 - k1 + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.methodProfiler.b();
            return true;
        } else {
            return false;
        }
    }

    public boolean a(boolean flag) {
        return false;
    }

    public List<NextTickListEntry> a(Chunk chunk, boolean flag) {
        return null;
    }

    public List<NextTickListEntry> a(StructureBoundingBox structureboundingbox, boolean flag) {
        return null;
    }

    public List<Entity> getEntities(Entity entity, AxisAlignedBB axisalignedbb) {
        return this.a(entity, axisalignedbb, IEntitySelector.e);
    }

    public List<Entity> a(Entity entity, AxisAlignedBB axisalignedbb, Predicate<? super Entity> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);

        for(int i1 = i; i1 <= j; ++i1) {
            for(int j1 = k; j1 <= l; ++j1) {
                if(this.isChunkLoaded(i1, j1, true)) {
                    this.getChunkAt(i1, j1).a(entity, axisalignedbb, arraylist, predicate);
                }
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> a(Class<? extends T> oclass, Predicate<? super T> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.entityList.iterator();

        while(iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if(oclass.isAssignableFrom(entity.getClass()) && predicate.apply((T)entity)) {
                arraylist.add(entity);
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> b(Class<? extends T> oclass, Predicate<? super T> predicate) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.players.iterator();

        while(iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if(oclass.isAssignableFrom(entity.getClass()) && predicate.apply((T)entity)) {
                arraylist.add(entity);
            }
        }

        return arraylist;
    }

    public <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb) {
        return this.a(oclass, axisalignedbb, IEntitySelector.e);
    }

    public <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, Predicate<? super T> predicate) {
        int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.f((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.f((axisalignedbb.f + 2.0D) / 16.0D);
        ArrayList arraylist = Lists.newArrayList();

        for(int i1 = i; i1 < j; ++i1) {
            for(int j1 = k; j1 < l; ++j1) {
                if(this.isChunkLoaded(i1, j1, true)) {
                    this.getChunkAt(i1, j1).a(oclass, axisalignedbb, arraylist, predicate);
                }
            }
        }

        return arraylist;
    }

    public <T extends Entity> T a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, T t0) {
        List list = this.a(oclass, axisalignedbb);
        Entity entity = null;
        double d0 = 1.7976931348623157E308D;

        for(int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity)list.get(i);
            if(entity1 != t0 && IEntitySelector.e.apply(entity1)) {
                double d1 = t0.h(entity1);
                if(d1 <= d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }

        return (T)entity;
    }

    public Entity getEntity(int i) {
        return (Entity)this.entitiesById.get(i);
    }

    public void b(BlockPosition blockposition, TileEntity tileentity) {
        if(this.isLoaded(blockposition)) {
            this.getChunkAtWorldCoords(blockposition).e();
        }

    }

    public int a(Class<?> oclass) {
        int i = 0;
        Iterator iterator = this.entityList.iterator();

        while(true) {
            Entity entity;
            EntityInsentient entityinsentient;
            do {
                if(!iterator.hasNext()) {
                    return i;
                }

                entity = (Entity)iterator.next();
                if(!(entity instanceof EntityInsentient)) {
                    break;
                }

                entityinsentient = (EntityInsentient)entity;
            } while(entityinsentient.isTypeNotPersistent() && entityinsentient.isPersistent());

            if(oclass.isAssignableFrom(entity.getClass())) {
                ++i;
            }
        }
    }

    public void a(Collection<Entity> collection) {
        AsyncCatcher.catchOp("entity world add");
        Iterator iterator = collection.iterator();

        while(iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if(entity != null) {
                this.entityList.add(entity);
                this.b(entity);
            }
        }

    }

    public void c(Collection<Entity> collection) {
        this.f.addAll(collection);
    }

    public boolean a(Block block, BlockPosition blockposition, boolean flag, EnumDirection enumdirection, Entity entity, ItemStack itemstack) {
        IBlockData iblockdata = this.getType(blockposition);
        AxisAlignedBB axisalignedbb = flag?null:block.getBlockData().d(this, blockposition);
        boolean defaultReturn = axisalignedbb != Block.k && !this.a(axisalignedbb.a(blockposition), entity)?false:(iblockdata.getMaterial() == Material.ORIENTABLE && block == Blocks.ANVIL?true:iblockdata.getMaterial().isReplaceable() && block.canPlace(this, blockposition, enumdirection, itemstack));
        BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftMagicNumbers.getId(block), defaultReturn);
        this.getServer().getPluginManager().callEvent(event);
        return event.isBuildable();
    }

    public int K() {
        return this.a;
    }

    public void b(int i) {
        this.a = i;
    }

    public int getBlockPower(BlockPosition blockposition, EnumDirection enumdirection) {
        return this.getType(blockposition).b(this, blockposition, enumdirection);
    }

    public WorldType L() {
        return this.worldData.getType();
    }

    public int getBlockPower(BlockPosition blockposition) {
        byte b0 = 0;
        int i = Math.max(b0, this.getBlockPower(blockposition.down(), EnumDirection.DOWN));
        if(i >= 15) {
            return i;
        } else {
            i = Math.max(i, this.getBlockPower(blockposition.up(), EnumDirection.UP));
            if(i >= 15) {
                return i;
            } else {
                i = Math.max(i, this.getBlockPower(blockposition.north(), EnumDirection.NORTH));
                if(i >= 15) {
                    return i;
                } else {
                    i = Math.max(i, this.getBlockPower(blockposition.south(), EnumDirection.SOUTH));
                    if(i >= 15) {
                        return i;
                    } else {
                        i = Math.max(i, this.getBlockPower(blockposition.west(), EnumDirection.WEST));
                        if(i >= 15) {
                            return i;
                        } else {
                            i = Math.max(i, this.getBlockPower(blockposition.east(), EnumDirection.EAST));
                            return i >= 15?i:i;
                        }
                    }
                }
            }
        }
    }

    public boolean isBlockFacePowered(BlockPosition blockposition, EnumDirection enumdirection) {
        return this.getBlockFacePower(blockposition, enumdirection) > 0;
    }

    public int getBlockFacePower(BlockPosition blockposition, EnumDirection enumdirection) {
        IBlockData iblockdata = this.getType(blockposition);
        return iblockdata.l()?this.getBlockPower(blockposition):iblockdata.a(this, blockposition, enumdirection);
    }

    public boolean isBlockIndirectlyPowered(BlockPosition blockposition) {
        return this.getBlockFacePower(blockposition.down(), EnumDirection.DOWN) > 0?true:(this.getBlockFacePower(blockposition.up(), EnumDirection.UP) > 0?true:(this.getBlockFacePower(blockposition.north(), EnumDirection.NORTH) > 0?true:(this.getBlockFacePower(blockposition.south(), EnumDirection.SOUTH) > 0?true:(this.getBlockFacePower(blockposition.west(), EnumDirection.WEST) > 0?true:this.getBlockFacePower(blockposition.east(), EnumDirection.EAST) > 0))));
    }

    public int z(BlockPosition blockposition) {
        int i = 0;
        EnumDirection[] aenumdirection = EnumDirection.values();
        int j = aenumdirection.length;

        for(int k = 0; k < j; ++k) {
            EnumDirection enumdirection = aenumdirection[k];
            int l = this.getBlockFacePower(blockposition.shift(enumdirection), enumdirection);
            if(l >= 15) {
                return 15;
            }

            if(l > i) {
                i = l;
            }
        }

        return i;
    }

    public EntityHuman findNearbyPlayer(Entity entity, double d0) {
        return this.findNearbyPlayer(entity, d0, Predicates.<EntityHuman>alwaysTrue());
    }

    public EntityHuman findNearbyPlayer(Entity entity, double d0, Predicate<EntityHuman> filter) {
        return this.findNearbyPlayer(entity.locX, entity.locY, entity.locZ, d0, false, filter);
    }

    public EntityHuman b(Entity entity, double d0) {
        return this.findNearbyPlayerNotInCreativeMode(entity, d0, Predicates.<EntityHuman>alwaysTrue());
    }

    public EntityHuman findNearbyPlayerNotInCreativeMode(Entity entity, double d0, Predicate<EntityHuman> filter) {
        return this.findNearbyPlayer(entity.locX, entity.locY, entity.locZ, d0, true, filter);
    }

    public EntityHuman a(double d0, double d1, double d2, double d3, boolean flag) {
        return this.findNearbyPlayer(d0, d1, d2, d3, flag,  Predicates.<EntityHuman>alwaysTrue());
    }

    public EntityHuman findNearbyPlayer(double d0, double d1, double d2, double d3, boolean flag, Predicate<EntityHuman> filter) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;

        for(int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman1 = (EntityHuman)this.players.get(i);
            if(entityhuman1 != null && !entityhuman1.dead && filter.apply(entityhuman1) && (IEntitySelector.d.apply(entityhuman1) || !flag) && (IEntitySelector.e.apply(entityhuman1) || flag)) {
                double d5 = entityhuman1.e(d0, d1, d2);
                if((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    public boolean isPlayerNearby(double d0, double d1, double d2, double d3) {
        return this.isPlayerNearby(d0, d1, d2, d3, Predicates.<EntityHuman>alwaysTrue());
    }

    public boolean isPlayerNearby(double d0, double d1, double d2, double d3, Predicate<EntityHuman> filter) {
        for(int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman = (EntityHuman)this.players.get(i);
            if(filter.apply(entityhuman) && IEntitySelector.e.apply(entityhuman)) {
                double d4 = entityhuman.e(d0, d1, d2);
                if(d3 < 0.0D || d4 < d3 * d3) {
                    return true;
                }
            }
        }

        return false;
    }

    public EntityHuman a(Entity entity, double d0, double d1) {
        return this.a(entity.locX, entity.locY, entity.locZ, d0, d1, (Function)null, (Predicate)null);
    }

    public EntityHuman a(BlockPosition blockposition, double d0, double d1) {
        return this.a((double)((float)blockposition.getX() + 0.5F), (double)((float)blockposition.getY() + 0.5F), (double)((float)blockposition.getZ() + 0.5F), d0, d1, (Function)null, (Predicate)null);
    }

    public EntityHuman a(double d0, double d1, double d2, double d3, double d4, Function<EntityHuman, Double> function, Predicate<EntityHuman> predicate) {
        double d5 = -1.0D;
        EntityHuman entityhuman = null;

        for(int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman1 = (EntityHuman)this.players.get(i);
            if(!entityhuman1.abilities.isInvulnerable && entityhuman1.isAlive() && !entityhuman1.isSpectator() && (predicate == null || predicate.apply(entityhuman1))) {
                double d6 = entityhuman1.e(d0, entityhuman1.locY, d2);
                double d7 = d3;
                if(entityhuman1.isSneaking()) {
                    d7 = d3 * 0.800000011920929D;
                }

                if(entityhuman1.isInvisible()) {
                    float f = entityhuman1.cG();
                    if(f < 0.1F) {
                        f = 0.1F;
                    }

                    d7 *= (double)(0.7F * f);
                }

                if(function != null) {
                    d7 *= ((Double)Objects.firstNonNull(function.apply(entityhuman1), Double.valueOf(1.0D))).doubleValue();
                }

                if((d4 < 0.0D || Math.abs(entityhuman1.locY - d1) < d4 * d4) && (d3 < 0.0D || d6 < d7 * d7) && (d5 == -1.0D || d6 < d5)) {
                    d5 = d6;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    public EntityHuman a(String s) {
        for(int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman = (EntityHuman)this.players.get(i);
            if(s.equals(entityhuman.getName())) {
                return entityhuman;
            }
        }

        return null;
    }

    public EntityHuman b(UUID uuid) {
        for(int i = 0; i < this.players.size(); ++i) {
            EntityHuman entityhuman = (EntityHuman)this.players.get(i);
            if(uuid.equals(entityhuman.getUniqueID())) {
                return entityhuman;
            }
        }

        return null;
    }

    public void checkSession() throws ExceptionWorldConflict {
        this.dataManager.checkSession();
    }

    public long getSeed() {
        return this.worldData.getSeed();
    }

    public long getTime() {
        return this.worldData.getTime();
    }

    public long getDayTime() {
        return this.worldData.getDayTime();
    }

    public void setDayTime(long i) {
        this.worldData.setDayTime(i);
    }

    public BlockPosition getSpawn() {
        BlockPosition blockposition = new BlockPosition(this.worldData.b(), this.worldData.c(), this.worldData.d());
        if(!this.getWorldBorder().a(blockposition)) {
            blockposition = this.getHighestBlockYAt(new BlockPosition(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
        }

        return blockposition;
    }

    public void A(BlockPosition blockposition) {
        this.worldData.setSpawn(blockposition);
    }

    public boolean a(EntityHuman entityhuman, BlockPosition blockposition) {
        return true;
    }

    public void broadcastEntityEffect(Entity entity, byte b0) {
    }

    public IChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }

    public void playBlockAction(BlockPosition blockposition, Block block, int i, int j) {
        block.a(this, blockposition, this.getType(blockposition), i, j);
    }

    public IDataManager getDataManager() {
        return this.dataManager;
    }

    public WorldData getWorldData() {
        return this.worldData;
    }

    public GameRules getGameRules() {
        return this.worldData.w();
    }

    public void everyoneSleeping() {
    }

    public void checkSleepStatus() {
        if(!this.isClientSide) {
            this.everyoneSleeping();
        }

    }

    public float h(float f) {
        return (this.p + (this.q - this.p) * f) * this.j(f);
    }

    public float j(float f) {
        return this.n + (this.o - this.n) * f;
    }

    public boolean V() {
        return (double)this.h(1.0F) > 0.9D;
    }

    public boolean W() {
        return (double)this.j(1.0F) > 0.2D;
    }

    public boolean isRainingAt(BlockPosition blockposition) {
        if(!this.W()) {
            return false;
        } else if(!this.h(blockposition)) {
            return false;
        } else if(this.p(blockposition).getY() > blockposition.getY()) {
            return false;
        } else {
            BiomeBase biomebase = this.getBiome(blockposition);
            return biomebase.c()?false:(this.f(blockposition, false)?false:biomebase.d());
        }
    }

    public boolean C(BlockPosition blockposition) {
        BiomeBase biomebase = this.getBiome(blockposition);
        return biomebase.e();
    }

    public PersistentCollection X() {
        return this.worldMaps;
    }

    public void a(String s, PersistentBase persistentbase) {
        this.worldMaps.a(s, persistentbase);
    }

    public PersistentBase a(Class<? extends PersistentBase> oclass, String s) {
        return this.worldMaps.get(oclass, s);
    }

    public int b(String s) {
        return this.worldMaps.a(s);
    }

    public void a(int i, BlockPosition blockposition, int j) {
        for(int k = 0; k < this.u.size(); ++k) {
            ((IWorldAccess)this.u.get(k)).a(i, blockposition, j);
        }

    }

    public void triggerEffect(int i, BlockPosition blockposition, int j) {
        this.a((EntityHuman)null, i, blockposition, j);
    }

    public void a(EntityHuman entityhuman, int i, BlockPosition blockposition, int j) {
        try {
            for(int throwable = 0; throwable < this.u.size(); ++throwable) {
                ((IWorldAccess)this.u.get(throwable)).a(entityhuman, i, blockposition, j);
            }

        } catch (Throwable var8) {
            CrashReport crashreport = CrashReport.a(var8, "Playing level event");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Level event being played");
            crashreportsystemdetails.a("Block coordinates", CrashReportSystemDetails.a(blockposition));
            crashreportsystemdetails.a("Event source", entityhuman);
            crashreportsystemdetails.a("Event type", Integer.valueOf(i));
            crashreportsystemdetails.a("Event data", Integer.valueOf(j));
            throw new ReportedException(crashreport);
        }
    }

    public int getHeight() {
        return 256;
    }

    public int Z() {
        return this.worldProvider.m()?128:256;
    }

    public Random a(int i, int j, int k) {
        long l = (long)i * 341873128712L + (long)j * 132897987541L + this.getWorldData().getSeed() + (long)k;
        this.random.setSeed(l);
        return this.random;
    }

    public CrashReportSystemDetails a(CrashReport crashreport) {
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Affected level", 1);
        crashreportsystemdetails.a("Level name", this.worldData == null?"????":this.worldData.getName());
        crashreportsystemdetails.a("All players", new Callable() {
            public String a() {
                return World.this.players.size() + " total; " + World.this.players.toString();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.a("Chunk stats", new Callable() {
            public String a() {
                return World.this.chunkProvider.getName();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });

        try {
            this.worldData.a(crashreportsystemdetails);
        } catch (Throwable var4) {
            crashreportsystemdetails.a("Level Data Unobtainable", var4);
        }

        return crashreportsystemdetails;
    }

    public void c(int i, BlockPosition blockposition, int j) {
        for(int k = 0; k < this.u.size(); ++k) {
            IWorldAccess iworldaccess = (IWorldAccess)this.u.get(k);
            iworldaccess.b(i, blockposition, j);
        }

    }

    public Calendar ac() {
        if(this.getTime() % 600L == 0L) {
            this.L.setTimeInMillis(MinecraftServer.av());
        }

        return this.L;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public void updateAdjacentComparators(BlockPosition blockposition, Block block) {
        Iterator iterator = EnumDirectionLimit.HORIZONTAL.iterator();

        while(iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection)iterator.next();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);
            if(this.isLoaded(blockposition1)) {
                IBlockData iblockdata = this.getType(blockposition1);
                if(Blocks.UNPOWERED_COMPARATOR.C(iblockdata)) {
                    iblockdata.getBlock().doPhysics(this, blockposition1, iblockdata, block);
                } else if(iblockdata.l()) {
                    blockposition1 = blockposition1.shift(enumdirection);
                    iblockdata = this.getType(blockposition1);
                    if(Blocks.UNPOWERED_COMPARATOR.C(iblockdata)) {
                        iblockdata.getBlock().doPhysics(this, blockposition1, iblockdata, block);
                    }
                }
            }
        }

    }

    public DifficultyDamageScaler D(BlockPosition blockposition) {
        long i = 0L;
        float f = 0.0F;
        if(this.isLoaded(blockposition)) {
            f = this.E();
            i = this.getChunkAtWorldCoords(blockposition).x();
        }

        return new DifficultyDamageScaler(this.getDifficulty(), this.getDayTime(), i, f);
    }

    public EnumDifficulty getDifficulty() {
        return this.getWorldData().getDifficulty();
    }

    public int af() {
        return this.J;
    }

    public void c(int i) {
        this.J = i;
    }

    public void d(int i) {
        this.K = i;
    }

    public PersistentVillage ai() {
        return this.villages;
    }

    public WorldBorder getWorldBorder() {
        return this.N;
    }

    public boolean shouldStayLoaded(int i, int j) {
        return this.c(i, j);
    }

    public boolean c(int i, int j) {
        BlockPosition blockposition = this.getSpawn();
        int k = i * 16 + 8 - blockposition.getX();
        int l = j * 16 + 8 - blockposition.getZ();
        short short0 = this.paperConfig.keepLoadedRange;
        return k >= -short0 && k <= short0 && l >= -short0 && l <= short0 && this.keepSpawnInMemory;
    }

    public void a(Packet<?> packet) {
        throw new UnsupportedOperationException("Can\'t send packets to server unless you\'re on the client.");
    }

    public LootTableRegistry ak() {
        return this.B;
    }
}
