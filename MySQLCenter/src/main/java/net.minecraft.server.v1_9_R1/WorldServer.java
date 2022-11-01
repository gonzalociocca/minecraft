//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraft.server.v1_9_R1;

import co.aikar.timings.Timing;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import net.minecraft.server.v1_9_R1.BiomeBase.BiomeMeta;
import net.minecraft.server.v1_9_R1.BlockJukeBox.TileEntityRecordPlayer;
import net.minecraft.server.v1_9_R1.BlockPosition.MutableBlockPosition;
import net.minecraft.server.v1_9_R1.BlockPosition.PooledBlockPosition;
import net.minecraft.server.v1_9_R1.WorldSettings.EnumGamemode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_9_R1.CraftTravelAgent;
import org.bukkit.craftbukkit.v1_9_R1.generator.CustomChunkGenerator;
import org.bukkit.craftbukkit.v1_9_R1.generator.NetherChunkGenerator;
import org.bukkit.craftbukkit.v1_9_R1.generator.NormalChunkGenerator;
import org.bukkit.craftbukkit.v1_9_R1.generator.SkyLandsChunkGenerator;
import org.bukkit.craftbukkit.v1_9_R1.util.HashTreeSet;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.generator.ChunkGenerator;

public class WorldServer extends World implements IAsyncTaskHandler {
    private static final Logger a = LogManager.getLogger();
    boolean stopPhysicsEvent = false;
    private final MinecraftServer server;
    public EntityTracker tracker;
    private final PlayerChunkMap manager;
    private final HashTreeSet<NextTickListEntry> nextTickList = new HashTreeSet();
    private final Map<UUID, Entity> entitiesByUUID = Maps.newHashMap();
    public boolean savingDisabled;
    private boolean O;
    private int emptyTime;
    private final PortalTravelAgent portalTravelAgent;
    private final SpawnerCreature spawnerCreature = new SpawnerCreature();
    protected final VillageSiege siegeManager = new VillageSiege(this);
    private WorldServer.BlockActionDataList[] S = new WorldServer.BlockActionDataList[]{new WorldServer.BlockActionDataList((Object)null), new WorldServer.BlockActionDataList((Object)null)};
    private int T;
    private List<NextTickListEntry> U = Lists.newArrayList();
    public final int dimension;

    public WorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, WorldData worlddata, int i, MethodProfiler methodprofiler, Environment env, ChunkGenerator gen) {
        super(idatamanager, worlddata, DimensionManager.a(env.getId()).d(), methodprofiler, false, gen, env);
        this.dimension = i;
        this.pvpMode = minecraftserver.getPVP();
        worlddata.world = this;
        this.server = minecraftserver;
        this.tracker = new EntityTracker(this);
        this.manager = new PlayerChunkMap(this, this.spigotConfig.viewDistance);
        this.worldProvider.a(this);
        this.chunkProvider = this.n();
        this.portalTravelAgent = new CraftTravelAgent(this);
        this.H();
        this.I();
        this.getWorldBorder().a(minecraftserver.aD());
    }

    public World b() {
        this.worldMaps = new PersistentCollection(this.dataManager);
        String s = PersistentVillage.a(this.worldProvider);
        PersistentVillage persistentvillage = (PersistentVillage)this.worldMaps.get(PersistentVillage.class, s);
        if(persistentvillage == null) {
            this.villages = new PersistentVillage(this);
            this.worldMaps.a(s, this.villages);
        } else {
            this.villages = persistentvillage;
            this.villages.a(this);
        }

        if(this.getServer().getScoreboardManager() == null) {
            this.scoreboard = new ScoreboardServer(this.server);
            PersistentScoreboard persistentscoreboard = (PersistentScoreboard)this.worldMaps.get(PersistentScoreboard.class, "scoreboard");
            if(persistentscoreboard == null) {
                persistentscoreboard = new PersistentScoreboard();
                this.worldMaps.a("scoreboard", persistentscoreboard);
            }

            persistentscoreboard.a(this.scoreboard);
            ((ScoreboardServer)this.scoreboard).a(new RunnableSaveScoreboard(persistentscoreboard));
        } else {
            this.scoreboard = this.getServer().getScoreboardManager().getMainScoreboard().getHandle();
        }

        this.B = new LootTableRegistry(new File(new File(this.dataManager.getDirectory(), "data"), "loot_tables"));
        this.getWorldBorder().setCenter(this.worldData.B(), this.worldData.C());
        this.getWorldBorder().setDamageAmount(this.worldData.H());
        this.getWorldBorder().setDamageBuffer(this.worldData.G());
        this.getWorldBorder().setWarningDistance(this.worldData.I());
        this.getWorldBorder().setWarningTime(this.worldData.J());
        if(this.worldData.E() > 0L) {
            this.getWorldBorder().transitionSizeBetween(this.worldData.D(), this.worldData.F(), this.worldData.E());
        } else {
            this.getWorldBorder().setSize(this.worldData.D());
        }

        if(this.generator != null) {
            this.getWorld().getPopulators().addAll(this.generator.getDefaultPopulators(this.getWorld()));
        }

        return this;
    }

    public TileEntity getTileEntity(BlockPosition pos) {
        TileEntity result = super.getTileEntity(pos);
        Block type = this.getType(pos).getBlock();
        if(type != Blocks.CHEST && type != Blocks.TRAPPED_CHEST) {
            if(type == Blocks.FURNACE) {
                if(!(result instanceof TileEntityFurnace)) {
                    result = this.fixTileEntity(pos, type, result);
                }
            } else if(type == Blocks.DROPPER) {
                if(!(result instanceof TileEntityDropper)) {
                    result = this.fixTileEntity(pos, type, result);
                }
            } else if(type == Blocks.DISPENSER) {
                if(!(result instanceof TileEntityDispenser)) {
                    result = this.fixTileEntity(pos, type, result);
                }
            } else if(type == Blocks.JUKEBOX) {
                if(!(result instanceof TileEntityRecordPlayer)) {
                    result = this.fixTileEntity(pos, type, result);
                }
            } else if(type == Blocks.NOTEBLOCK) {
                if(!(result instanceof TileEntityNote)) {
                    result = this.fixTileEntity(pos, type, result);
                }
            } else if(type == Blocks.MOB_SPAWNER) {
                if(!(result instanceof TileEntityMobSpawner)) {
                    result = this.fixTileEntity(pos, type, result);
                }
            } else if(type != Blocks.STANDING_SIGN && type != Blocks.WALL_SIGN) {
                if(type == Blocks.ENDER_CHEST) {
                    if(!(result instanceof TileEntityEnderChest)) {
                        result = this.fixTileEntity(pos, type, result);
                    }
                } else if(type == Blocks.BREWING_STAND) {
                    if(!(result instanceof TileEntityBrewingStand)) {
                        result = this.fixTileEntity(pos, type, result);
                    }
                } else if(type == Blocks.BEACON) {
                    if(!(result instanceof TileEntityBeacon)) {
                        result = this.fixTileEntity(pos, type, result);
                    }
                } else if(type == Blocks.HOPPER && !(result instanceof TileEntityHopper)) {
                    result = this.fixTileEntity(pos, type, result);
                }
            } else if(!(result instanceof TileEntitySign)) {
                result = this.fixTileEntity(pos, type, result);
            }
        } else if(!(result instanceof TileEntityChest)) {
            result = this.fixTileEntity(pos, type, result);
        }

        return result;
    }

    private TileEntity fixTileEntity(BlockPosition pos, Block type, TileEntity found) {
        this.getServer().getLogger().log(Level.SEVERE, "Block at {0},{1},{2} is {3} but has {4}. Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.", new Object[]{Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()), Material.getMaterial(Block.getId(type)).toString(), found});
        if(type instanceof ITileEntity) {
            TileEntity replacement = ((ITileEntity)type).a(this, type.toLegacyData(this.getType(pos)));
            replacement.world = this;
            this.setTileEntity(pos, replacement);
            return replacement;
        } else {
            this.getServer().getLogger().severe("Don\'t know how to fix for this type... Can\'t do anything! :(");
            return found;
        }
    }

    private boolean canSpawn(int x, int z) {
        return this.generator != null?this.generator.canSpawn(this.getWorld(), x, z):this.worldProvider.canSpawn(x, z);
    }

    public void doTick() {
        super.doTick();
        if(this.getWorldData().isHardcore() && this.getDifficulty() != EnumDifficulty.HARD) {
            this.getWorldData().setDifficulty(EnumDifficulty.HARD);
        }

        this.worldProvider.k().b();
        long time;
        if(this.everyoneDeeplySleeping()) {
            if(this.getGameRules().getBoolean("doDaylightCycle")) {
                time = this.worldData.getDayTime() + 24000L;
                this.worldData.setDayTime(time - time % 24000L);
            }

            this.f();
        }

        time = this.worldData.getTime();
        if(this.tick%30==0) {
            this.timings.mobSpawn.startTiming();
            if (this.getGameRules().getBoolean("doMobSpawning") && this.worldData.getType() != WorldType.DEBUG_ALL_BLOCK_STATES && (this.allowMonsters || this.allowAnimals) && this.players.size() > 0) {
                //     this.spawnerCreature.a(this, this.allowMonsters && this.ticksPerMonsterSpawns != 0L && time % this.ticksPerMonsterSpawns == 0L, this.allowAnimals && this.ticksPerAnimalSpawns != 0L && time % this.ticksPerAnimalSpawns == 0L, this.worldData.getTime() % 400L == 0L);
                int maxsize = this.players.size()*30;
                if(players.size() > 100){
                    maxsize = 100*30;
                }
                this.spawnMobs(maxsize > this.entityList.size() && this.allowMonsters && this.ticksPerMonsterSpawns != 0L, maxsize > this.entityList.size() && this.allowAnimals && this.ticksPerAnimalSpawns != 0);
            }
            this.timings.mobSpawn.stopTiming();
        }

        this.timings.doChunkUnload.startTiming();
        this.methodProfiler.c("chunkSource");
        this.chunkProvider.unloadChunks();
        int j = this.a(1.0F);
        if(j != this.af()) {
            this.c(j);
        }

        this.worldData.setTime(this.worldData.getTime() + 1L);
        if(this.getGameRules().getBoolean("doDaylightCycle")) {
            this.worldData.setDayTime(this.worldData.getDayTime() + 1L);
        }

        this.timings.doChunkUnload.stopTiming();
        this.methodProfiler.c("tickPending");
        this.timings.scheduledBlocks.startTiming();
        this.a(false);
        this.timings.scheduledBlocks.stopTiming();
        this.methodProfiler.c("tickBlocks");
        this.timings.chunkTicks.startTiming();
        this.j();
        this.timings.chunkTicks.stopTiming();
        this.methodProfiler.c("chunkMap");
        this.timings.doChunkMap.startTiming();
        this.manager.flush();
        this.timings.doChunkMap.stopTiming();
        this.methodProfiler.c("village");
        this.timings.doVillages.startTiming();
        this.villages.tick();
        this.siegeManager.a();
        this.timings.doVillages.stopTiming();
        this.methodProfiler.c("portalForcer");
        this.timings.doPortalForcer.startTiming();
        this.portalTravelAgent.a(this.getTime());
        this.timings.doPortalForcer.stopTiming();
        this.methodProfiler.b();
        this.timings.doSounds.startTiming();
        this.ao();
        this.timings.doSounds.stopTiming();
        this.timings.doChunkGC.startTiming();
        this.getWorld().processChunkGC();
        this.timings.doChunkGC.stopTiming();
    }

    public void spawnMobs(boolean monsters, boolean animals){
        if(players.size() <= 0){
            return;
        }

        for(EntityHuman player : this.players){
            if(random.nextBoolean()){
                continue;
            }
            int chunkx = player.getChunkX();
            int chunkz = player.getChunkZ();
            BlockPosition loc = spawnerCreature.getRandomPosition(this, chunkx, chunkz);

            if(random.nextInt(3)==0){
                loc = this.getHighestBlockYAt(loc);
            }

            boolean day = true;
            Long time = this.getWorld().getTime();
            if(time > 12300 && time < 23850){
                day = false;
            }

            if(worldProvider instanceof WorldProviderHell && loc.getY() > 128){
                continue;
            }if(monsters || animals){
                Class<?extends org.bukkit.entity.Entity> rencl =  this.getRandomEnt(day,this.worldProvider,monsters,animals);
                if(rencl == null){
                    continue;
                }
                net.minecraft.server.v1_9_R1.Material mat = this.getType(loc).getMaterial();
                if (!mat.isSolid() && !mat.isLiquid()) {
                    this.getWorld().spawn(new Location(Bukkit.getWorld(this.getWorld().getName()), loc.getX(), loc.getY(), loc.getZ()),rencl, SpawnReason.NATURAL);
                }}



        }
    }


    public Class<?extends org.bukkit.entity.Entity> getRandomEnt(boolean day, WorldProvider wp,boolean monsters, boolean animals){
        if(wp instanceof WorldProviderNormal ) {
            if (day == false && monsters) {
                int ran = random.nextInt(6);
                if (ran == 0) {
                    return org.bukkit.entity.Zombie.class;
                }
                if (ran == 1) {
                    return org.bukkit.entity.Spider.class;
                }
                if (ran == 2) {
                    return org.bukkit.entity.Skeleton.class;
                }
                if (ran == 3) {
                    return org.bukkit.entity.Creeper.class;
                }
                if (ran == 4) {
                    return org.bukkit.entity.Witch.class;
                }
                if (ran == 5) {
                    return org.bukkit.entity.Slime.class;
                }
            }
            else if(day == true && animals && random.nextInt(10)==1){
                int ran = random.nextInt(7);
                if (ran == 0) {
                    return org.bukkit.entity.Pig.class;
                }
                if (ran == 1) {
                    return org.bukkit.entity.Horse.class;
                }
                if (ran == 2) {
                    return org.bukkit.entity.Sheep.class;
                }
                if (ran == 3) {
                    return org.bukkit.entity.Cow.class;
                }
                if (ran == 4) {
                    return org.bukkit.entity.Wolf.class;
                }
                if (ran == 5) {
                    return org.bukkit.entity.Rabbit.class;
                }
                if (ran == 6) {
                    return org.bukkit.entity.Enderman.class;
                }
            }}
        else if(wp instanceof WorldProviderHell && monsters) {
            int ran = random.nextInt(6);
            if (ran == 0) {
                return org.bukkit.entity.Blaze.class;
            }
            if (ran == 1) {
                return org.bukkit.entity.MagmaCube.class;
            }
            if (ran == 2) {
                return org.bukkit.entity.Ghast.class;
            }
            if (ran == 3) {
                return org.bukkit.entity.WitherSkull.class;
            }
            if (ran == 4) {
                return org.bukkit.entity.Skeleton.class;
            }
            if (ran == 5) {
                return org.bukkit.entity.PigZombie.class;
            }
        }
        else if(wp instanceof WorldProviderTheEnd && monsters) {
            int ran = random.nextInt(3);
            if (ran == 0) {
                return org.bukkit.entity.Enderman.class;
            }
            if (ran == 1) {
                return org.bukkit.entity.Endermite.class;
            }
        }

        return null;
    }

    public BiomeMeta a(EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
        List list = this.getChunkProviderServer().a(enumcreaturetype, blockposition);
        return list != null && !list.isEmpty()?(BiomeMeta)WeightedRandom.a(this.random, list):null;
    }

    public boolean a(EnumCreatureType enumcreaturetype, BiomeMeta biomebase_biomemeta, BlockPosition blockposition) {
        List list = this.getChunkProviderServer().a(enumcreaturetype, blockposition);
        return list != null && !list.isEmpty()?list.contains(biomebase_biomemeta):false;
    }

    public void everyoneSleeping() {
        this.O = false;
        if(!this.players.isEmpty()) {
            int i = 0;
            int j = 0;
            Iterator iterator = this.players.iterator();

            while(true) {
                while(iterator.hasNext()) {
                    EntityHuman entityhuman = (EntityHuman)iterator.next();
                    if(entityhuman.isSpectator()) {
                        ++i;
                    } else if(entityhuman.isSleeping() || entityhuman.fauxSleeping) {
                        ++j;
                    }
                }

                this.O = j > 0 && j >= this.players.size() - i;
                break;
            }
        }

    }

    protected void f() {
        this.O = false;
        Iterator iterator = this.players.iterator();

        while(iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman)iterator.next();
            if(entityhuman.isSleeping()) {
                entityhuman.a(false, false, true);
            }
        }

        this.c();
    }

    private void c() {
        this.worldData.setStorm(false);
        if(!this.worldData.hasStorm()) {
            this.worldData.setWeatherDuration(0);
        }

        this.worldData.setThundering(false);
        if(!this.worldData.isThundering()) {
            this.worldData.setThunderDuration(0);
        }

    }

    public boolean everyoneDeeplySleeping() {
        if(this.O && !this.isClientSide) {
            Iterator iterator = this.players.iterator();
            boolean foundActualSleepers = false;

            EntityHuman entityhuman;
            do {
                if(!iterator.hasNext()) {
                    return foundActualSleepers;
                }

                entityhuman = (EntityHuman)iterator.next();
                if(entityhuman.isDeeplySleeping()) {
                    foundActualSleepers = true;
                }
            } while(!entityhuman.isSpectator() || entityhuman.isDeeplySleeping() || entityhuman.fauxSleeping);

            return false;
        } else {
            return false;
        }
    }

    protected boolean isChunkLoaded(int i, int j, boolean flag) {
        return this.getChunkProviderServer().e(i, j);
    }

    protected void i() {
        this.methodProfiler.a("playerCheckLight");
        if(this.spigotConfig.randomLightUpdates && !this.players.isEmpty()) {
            int i = this.random.nextInt(this.players.size());
            EntityHuman entityhuman = (EntityHuman)this.players.get(i);
            int j = MathHelper.floor(entityhuman.locX) + this.random.nextInt(11) - 5;
            int k = MathHelper.floor(entityhuman.locY) + this.random.nextInt(11) - 5;
            int l = MathHelper.floor(entityhuman.locZ) + this.random.nextInt(11) - 5;
            this.w(new BlockPosition(j, k, l));
        }

        this.methodProfiler.b();
    }

    int tick = (20*90)-1;
    protected void j() {
        tick++;
        if(tick == 20*90){
            tick = 0;
        }
        this.i();
        if(this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            Iterator i = this.manager.b();

            while(i.hasNext()) {
                ((Chunk)i.next()).b(false);
            }
        } else {
            int var23 = this.getGameRules().c("randomTickSpeed");
            boolean flag = this.W();
            boolean flag1 = this.V();
            this.methodProfiler.a("pollingChunks");

            for(Iterator iterator1 = this.manager.b(); iterator1.hasNext(); this.methodProfiler.b()) {
                this.methodProfiler.a("getChunk");
                Chunk chunk = (Chunk)iterator1.next();
                int j = chunk.locX * 16;
                int k = chunk.locZ * 16;
                this.methodProfiler.c("checkNextLight");
                chunk.n();
                this.methodProfiler.c("tickChunk");
                chunk.b(false);
                if(chunk.areNeighborsLoaded(1)) {
                    this.methodProfiler.c("thunder");
                    int l;
                    BlockPosition blockposition;
                    if(!this.paperConfig.disableThunder && flag && flag1 && this.random.nextInt(100000) == 0) {
                        this.l = this.l * 3 + 1013904223;
                        l = this.l >> 2;
                        blockposition = this.a(new BlockPosition(j + (l & 15), 0, k + (l >> 8 & 15)));
                        if(this.isRainingAt(blockposition)) {
                            DifficultyDamageScaler achunksection = this.D(blockposition);
                            double chance = this.paperConfig.skeleHorseSpawnChance == -1.0D?(double)achunksection.b() * 0.05D:this.paperConfig.skeleHorseSpawnChance;
                            if(this.random.nextDouble() < chance) {
                                EntityHorse chunksection = new EntityHorse(this);
                                chunksection.setType(EnumHorseType.SKELETON);
                                chunksection.x(true);
                                chunksection.setAgeRaw(0);
                                chunksection.setPosition((double)blockposition.getX(), (double)blockposition.getY(), (double)blockposition.getZ());
                                this.addEntity(chunksection, SpawnReason.LIGHTNING);
                                this.strikeLightning(new EntityLightning(this, (double)blockposition.getX(), (double)blockposition.getY(), (double)blockposition.getZ(), true));
                            } else {
                                this.strikeLightning(new EntityLightning(this, (double)blockposition.getX(), (double)blockposition.getY(), (double)blockposition.getZ(), false));
                            }
                        }
                    }

                    this.methodProfiler.c("iceandsnow");
                    if(!this.paperConfig.disableIceAndSnow && this.random.nextInt(16) == 0) {
                        this.l = this.l * 3 + 1013904223;
                        l = this.l >> 2;
                        blockposition = this.p(new BlockPosition(j + (l & 15), 0, k + (l >> 8 & 15)));
                        BlockPosition var24 = blockposition.down();
                        BlockState i1;
                        BlockFormEvent j1;
                        if(this.v(var24)) {
                            i1 = this.getWorld().getBlockAt(var24.getX(), var24.getY(), var24.getZ()).getState();
                            i1.setTypeId(Block.getId(Blocks.ICE));
                            j1 = new BlockFormEvent(i1.getBlock(), i1);
                            this.getServer().getPluginManager().callEvent(j1);
                            if(!j1.isCancelled()) {
                                i1.update(true);
                            }
                        }

                        if(flag && this.f(blockposition, true)) {
                            i1 = this.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()).getState();
                            i1.setTypeId(Block.getId(Blocks.SNOW_LAYER));
                            j1 = new BlockFormEvent(i1.getBlock(), i1);
                            this.getServer().getPluginManager().callEvent(j1);
                            if(!j1.isCancelled()) {
                                i1.update(true);
                            }
                        }

                        if(flag && this.getBiome(var24).d()) {
                            this.getType(var24).getBlock().h(this, var24);
                        }
                    }

                    this.timings.chunkTicksBlocks.startTiming();
                    if(var23 > 0 && tick == 0) {
                        ChunkSection[] chunksecs = chunk.getSections();
                        int j1 = chunksecs.length;
                        for (int k1 = 0; k1 < j1; k1++) {
                            ChunkSection chunksection = chunksecs[k1];
                            if ((chunksection != Chunk.a) && (chunksection.shouldTick()) && random.nextBoolean()) {
                                chunksection.recalcTickedBlocks();

                                for(BlockPosition block : chunksection.tickedblocks) {
                                    if(random.nextBoolean()){
                                        continue;
                                    }
                                    IBlockData face = chunksection.getType(block.getX(),block.getY(),block.getZ());
                                    Block update = face.getBlock();
                                    this.methodProfiler.a("randomTick");
                                    if (update.isTicking() && update!=null && update !=Blocks.AIR) {
                                        if(update.material.isLiquid()){
                                            continue;
                                        }
                                        IBlockData iblock = null;
                                        BlockPosition worldposition = new BlockPosition(block.getX()+j,block.getY()+chunksection.getYPosition(),block.getZ()+k);

                                        try{
                                            if(this.isAgeable(update.getName())){
                                                iblock = face.set(AGE,15);
                                            }}catch(Exception e){
                                            noageable.add(update.getName());

                                        }
                                        if(iblock == null){
                                            iblock = face;
                                        }
                                        update.b(this,worldposition,iblock,random);
                                    }
                                    this.methodProfiler.b();
                                }
                            }
                        }
                    }

                    this.timings.chunkTicksBlocks.stopTiming();
                }
            }

            this.methodProfiler.b();
        }

    }
    BlockStateInteger AGE = BlockStateInteger.of("age", 0, 15);

    List<String> noageable = new ArrayList();
    public Boolean isAgeable(String str){
        return !(noageable.contains(str));}

    protected BlockPosition a(BlockPosition blockposition) {
        BlockPosition blockposition1 = this.p(blockposition);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockposition1, new BlockPosition(blockposition1.getX(), this.getHeight(), blockposition1.getZ()))).g(3.0D);
        List list = this.a(EntityLiving.class, axisalignedbb, new Predicate() {
            public boolean a(EntityLiving entityliving) {
                return entityliving != null && entityliving.isAlive() && WorldServer.this.h(entityliving.getChunkCoordinates());
            }

            public boolean apply(Object object) {
                return this.a((EntityLiving)object);
            }
        });
        if(!list.isEmpty()) {
            return ((EntityLiving)list.get(this.random.nextInt(list.size()))).getChunkCoordinates();
        } else {
            if(blockposition1.getY() == -1) {
                blockposition1 = blockposition1.up(2);
            }

            return blockposition1;
        }
    }

    public boolean a(BlockPosition blockposition, Block block) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
        return this.U.contains(nextticklistentry);
    }

    public boolean b(BlockPosition blockposition, Block block) {
        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
        return this.nextTickList.contains(nextticklistentry);
    }

    public void a(BlockPosition blockposition, Block block, int i) {
        this.a(blockposition, block, i, 0);
    }

    public void a(BlockPosition blockposition, Block block, int i, int j) {
        if(blockposition instanceof MutableBlockPosition || blockposition instanceof PooledBlockPosition) {
            blockposition = new BlockPosition(blockposition);
            LogManager.getLogger().warn("Tried to assign a mutable BlockPos to tick data...", new Error(blockposition.getClass().toString()));
        }

        net.minecraft.server.v1_9_R1.Material material = block.getBlockData().getMaterial();
        if(this.d && material != net.minecraft.server.v1_9_R1.Material.AIR) {
            if(block.s()) {
                if(this.areChunksLoadedBetween(blockposition.a(-8, -8, -8), blockposition.a(8, 8, 8))) {
                    IBlockData nextticklistentry1 = this.getType(blockposition);
                    if(nextticklistentry1.getMaterial() != net.minecraft.server.v1_9_R1.Material.AIR && nextticklistentry1.getBlock() == block) {
                        nextticklistentry1.getBlock().b(this, blockposition, nextticklistentry1, this.random);
                    }
                }

                return;
            }

            i = 1;
        }

        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
        if(this.isLoaded(blockposition)) {
            if(material != net.minecraft.server.v1_9_R1.Material.AIR) {
                nextticklistentry.a((long)i + this.worldData.getTime());
                nextticklistentry.a(j);
            }

            if(!this.nextTickList.contains(nextticklistentry)) {
                this.nextTickList.add(nextticklistentry);
            }
        }

    }

    public void b(BlockPosition blockposition, Block block, int i, int j) {
        if(blockposition instanceof MutableBlockPosition || blockposition instanceof PooledBlockPosition) {
            blockposition = new BlockPosition(blockposition);
            LogManager.getLogger().warn("Tried to assign a mutable BlockPos to tick data...", new Error(blockposition.getClass().toString()));
        }

        NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
        nextticklistentry.a(j);
        net.minecraft.server.v1_9_R1.Material material = block.getBlockData().getMaterial();
        if(material != net.minecraft.server.v1_9_R1.Material.AIR) {
            nextticklistentry.a((long)i + this.worldData.getTime());
        }

        if(!this.nextTickList.contains(nextticklistentry)) {
            this.nextTickList.add(nextticklistentry);
        }

    }

    public void tickEntities() {
        this.m();
        this.worldProvider.r();
        super.tickEntities();
        this.spigotConfig.currentPrimedTnt = 0;
    }

    protected void l() {
        super.l();
        this.methodProfiler.c("players");

        for(int i = 0; i < this.players.size(); ++i) {
            Entity entity = (Entity)this.players.get(i);
            Entity entity1 = entity.by();
            if(entity1 != null) {
                if(!entity1.dead && entity1.w(entity)) {
                    continue;
                }

                entity.stopRiding();
            }

            this.methodProfiler.a("tick");
            if(!entity.dead) {
                try {
                    this.g(entity);
                } catch (Throwable var7) {
                    CrashReport k = CrashReport.a(var7, "Ticking player");
                    CrashReportSystemDetails crashreportsystemdetails = k.a("Player being ticked");
                    entity.appendEntityCrashDetails(crashreportsystemdetails);
                    throw new ReportedException(k);
                }
            }

            this.methodProfiler.b();
            this.methodProfiler.a("remove");
            if(entity.dead) {
                int j = entity.ab;
                int var8 = entity.ad;
                if(entity.aa && this.isChunkLoaded(j, var8, true)) {
                    this.getChunkAt(j, var8).b(entity);
                }

                this.entityList.remove(entity);
                this.c(entity);
            }

            this.methodProfiler.b();
        }

    }

    public void m() {
        this.emptyTime = 0;
    }

    public boolean a(boolean flag) {
        if(this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        } else {
            int i = this.nextTickList.size();
            if(i > 10){
                i = i / 3;
            }
            if(i > 65536) {
                if(i > 1310720) {
                    i /= 20;
                } else {
                    i = 65536;
                }
            }


            this.methodProfiler.a("cleaning");
            this.timings.scheduledBlocksCleanup.startTiming();

            NextTickListEntry nextticklistentry;
            for(int iterator = 0; iterator < i; ++iterator) {
                nextticklistentry = (NextTickListEntry)this.nextTickList.first();
                if(!flag && nextticklistentry.b > this.worldData.getTime()) {
                    break;
                }

                this.nextTickList.remove(nextticklistentry);
                this.U.add(nextticklistentry);
            }

            this.timings.scheduledBlocksCleanup.stopTiming();
            this.methodProfiler.b();
            this.methodProfiler.a("ticking");
            this.timings.scheduledBlocksTicking.startTiming();
            Iterator var16 = this.U.iterator();

            while(true) {
                while(var16.hasNext()) {
                    nextticklistentry = (NextTickListEntry)var16.next();
                    var16.remove();
                    byte b0 = 0;
                    if(this.areChunksLoadedBetween(nextticklistentry.a.a(-b0, -b0, -b0), nextticklistentry.a.a(b0, b0, b0))) {
                        IBlockData iblockdata = this.getType(nextticklistentry.a);
                        Timing timing = iblockdata.getBlock().getTiming();
                        timing.startTiming();
                        if(iblockdata.getMaterial() != net.minecraft.server.v1_9_R1.Material.AIR && Block.a(iblockdata.getBlock(), nextticklistentry.a())) {
                            try {
                                this.stopPhysicsEvent = !this.paperConfig.firePhysicsEventForRedstone && (iblockdata.getBlock() instanceof BlockDiodeAbstract || iblockdata.getBlock() instanceof BlockRedstoneTorch);
                                iblockdata.getBlock().b(this, nextticklistentry.a, iblockdata, this.random);
                            } catch (Throwable var14) {
                                CrashReport crashreport = CrashReport.a(var14, "Exception while ticking a block");
                                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being ticked");
                                CrashReportSystemDetails.a(crashreportsystemdetails, nextticklistentry.a, iblockdata);
                                throw new ReportedException(crashreport);
                            } finally {
                                this.stopPhysicsEvent = false;
                            }
                        }

                        timing.stopTiming();
                    } else {
                        this.a(nextticklistentry.a, nextticklistentry.a(), 0);
                    }
                }

                this.timings.scheduledBlocksTicking.stopTiming();
                this.methodProfiler.b();
                this.U.clear();
                return !this.nextTickList.isEmpty();
            }
        }
    }

    public List<NextTickListEntry> a(Chunk chunk, boolean flag) {
        ChunkCoordIntPair chunkcoordintpair = chunk.k();
        int i = (chunkcoordintpair.x << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkcoordintpair.z << 4) - 2;
        int l = k + 16 + 2;
        return this.a(new StructureBoundingBox(i, 0, k, j, 256, l), flag);
    }

    public List<NextTickListEntry> a(StructureBoundingBox structureboundingbox, boolean flag) {
        ArrayList arraylist = null;

        for(int i = 0; i < 2; ++i) {
            Iterator iterator;
            if(i == 0) {
                iterator = this.nextTickList.iterator();
            } else {
                iterator = this.U.iterator();
            }

            while(iterator.hasNext()) {
                NextTickListEntry nextticklistentry = (NextTickListEntry)iterator.next();
                BlockPosition blockposition = nextticklistentry.a;
                if(blockposition.getX() >= structureboundingbox.a && blockposition.getX() < structureboundingbox.d && blockposition.getZ() >= structureboundingbox.c && blockposition.getZ() < structureboundingbox.f) {
                    if(flag) {
                        if(i == 0) {
                            ;
                        }

                        iterator.remove();
                    }

                    if(arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(nextticklistentry);
                }
            }
        }

        return arraylist;
    }

    private boolean getSpawnNPCs() {
        return this.server.getSpawnNPCs();
    }

    private boolean getSpawnAnimals() {
        return this.server.getSpawnAnimals();
    }

    protected IChunkProvider n() {
        IChunkLoader ichunkloader = this.dataManager.createChunkLoader(this.worldProvider);
        Object gen;
        if(this.generator != null) {
            gen = new CustomChunkGenerator(this, this.getSeed(), this.generator);
        } else if(this.worldProvider instanceof WorldProviderHell) {
            gen = new NetherChunkGenerator(this, this.getSeed());
        } else if(this.worldProvider instanceof WorldProviderTheEnd) {
            gen = new SkyLandsChunkGenerator(this, this.getSeed());
        } else {
            gen = new NormalChunkGenerator(this, this.getSeed());
        }

        return new ChunkProviderServer(this, ichunkloader, (net.minecraft.server.v1_9_R1.ChunkGenerator)gen);
    }

    public List<TileEntity> getTileEntities(int i, int j, int k, int l, int i1, int j1) {
        ArrayList arraylist = Lists.newArrayList();

        for(int chunkX = i >> 4; chunkX <= l - 1 >> 4; ++chunkX) {
            for(int chunkZ = k >> 4; chunkZ <= j1 - 1 >> 4; ++chunkZ) {
                Chunk chunk = this.getChunkAt(chunkX, chunkZ);
                if(chunk != null) {
                    Iterator var11 = chunk.tileEntities.values().iterator();

                    while(var11.hasNext()) {
                        Object te = var11.next();
                        TileEntity tileentity = (TileEntity)te;
                        if(tileentity.position.getX() >= i && tileentity.position.getY() >= j && tileentity.position.getZ() >= k && tileentity.position.getX() < l && tileentity.position.getY() < i1 && tileentity.position.getZ() < j1) {
                            arraylist.add(tileentity);
                        }
                    }
                }
            }
        }

        return arraylist;
    }

    public boolean a(EntityHuman entityhuman, BlockPosition blockposition) {
        return !this.server.a(this, blockposition, entityhuman) && this.getWorldBorder().a(blockposition);
    }

    public void a(WorldSettings worldsettings) {
        if(!this.worldData.v()) {
            try {
                this.b(worldsettings);
                if(this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
                    this.an();
                }

                super.a(worldsettings);
            } catch (Throwable var6) {
                CrashReport crashreport = CrashReport.a(var6, "Exception initializing level");

                try {
                    this.a((CrashReport)crashreport);
                } catch (Throwable var5) {
                    ;
                }

                throw new ReportedException(crashreport);
            }

            this.worldData.d(true);
        }

    }

    private void an() {
        this.worldData.f(false);
        this.worldData.c(true);
        this.worldData.setStorm(false);
        this.worldData.setThundering(false);
        this.worldData.i(1000000000);
        this.worldData.setDayTime(6000L);
        this.worldData.setGameType(EnumGamemode.SPECTATOR);
        this.worldData.g(false);
        this.worldData.setDifficulty(EnumDifficulty.PEACEFUL);
        this.worldData.e(true);
        this.getGameRules().set("doDaylightCycle", "false");
    }

    private void b(WorldSettings worldsettings) {
        if(!this.worldProvider.e()) {
            this.worldData.setSpawn(BlockPosition.ZERO.up(this.worldProvider.getSeaLevel()));
        } else if(this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            this.worldData.setSpawn(BlockPosition.ZERO.up());
        } else {
            this.isLoading = true;
            WorldChunkManager worldchunkmanager = this.worldProvider.k();
            List list = worldchunkmanager.a();
            Random random = new Random(this.getSeed());
            BlockPosition blockposition = worldchunkmanager.a(0, 0, 256, list, random);
            int i = 8;
            int j = this.worldProvider.getSeaLevel();
            int k = 8;
            if(this.generator != null) {
                Random l = new Random(this.getSeed());
                Location spawn = this.generator.getFixedSpawnLocation(this.getWorld(), l);
                if(spawn != null) {
                    if(spawn.getWorld() != this.getWorld()) {
                        throw new IllegalStateException("Cannot set spawn point for " + this.worldData.getName() + " to be in another world (" + spawn.getWorld().getName() + ")");
                    }

                    this.worldData.setSpawn(new BlockPosition(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()));
                    this.isLoading = false;
                    return;
                }
            }

            if(blockposition != null) {
                i = blockposition.getX();
                k = blockposition.getZ();
            } else {
                a.warn("Unable to find spawn biome");
            }

            int var11 = 0;

            while(!this.canSpawn(i, k)) {
                i += random.nextInt(64) - random.nextInt(64);
                k += random.nextInt(64) - random.nextInt(64);
                ++var11;
                if(var11 == 1000) {
                    break;
                }
            }

            this.worldData.setSpawn(new BlockPosition(i, j, k));
            this.isLoading = false;
            if(worldsettings.c()) {
                this.o();
            }
        }

    }

    protected void o() {
        WorldGenBonusChest worldgenbonuschest = new WorldGenBonusChest();

        for(int i = 0; i < 10; ++i) {
            int j = this.worldData.b() + this.random.nextInt(6) - this.random.nextInt(6);
            int k = this.worldData.d() + this.random.nextInt(6) - this.random.nextInt(6);
            BlockPosition blockposition = this.q(new BlockPosition(j, 0, k)).up();
            if(worldgenbonuschest.generate(this, this.random, blockposition)) {
                break;
            }
        }

    }

    public BlockPosition getDimensionSpawn() {
        return this.worldProvider.h();
    }

    public void save(boolean flag, IProgressUpdate iprogressupdate) throws ExceptionWorldConflict {
        ChunkProviderServer chunkproviderserver = this.getChunkProviderServer();
        if(chunkproviderserver.e()) {
            Bukkit.getPluginManager().callEvent(new WorldSaveEvent(this.getWorld()));
            if(iprogressupdate != null) {
                iprogressupdate.a("Saving level");
            }

            this.a();
            if(iprogressupdate != null) {
                iprogressupdate.c("Saving chunks");
            }

            chunkproviderserver.a(flag);
            Collection arraylist = chunkproviderserver.a();
            Iterator iterator = arraylist.iterator();

            while(iterator.hasNext()) {
                Chunk chunk = (Chunk)iterator.next();
                if(chunk != null && !this.manager.a(chunk.locX, chunk.locZ)) {
                    chunkproviderserver.queueUnload(chunk.locX, chunk.locZ);
                }
            }
        }

    }

    public void flushSave() {
        ChunkProviderServer chunkproviderserver = this.getChunkProviderServer();
        if(chunkproviderserver.e()) {
            chunkproviderserver.c();
        }

    }

    protected void a() throws ExceptionWorldConflict {
        this.checkSession();
        WorldServer[] aworldserver = this.server.worldServer;
        int i = aworldserver.length;

        for(int j = 0; j < i; ++j) {
            WorldServer worldserver = aworldserver[j];
            if(worldserver instanceof SecondaryWorldServer) {
                ((SecondaryWorldServer)worldserver).c();
            }
        }

        if(this instanceof SecondaryWorldServer) {
            ((SecondaryWorldServer)this).c();
        }

        this.worldData.a(this.getWorldBorder().getSize());
        this.worldData.d(this.getWorldBorder().getCenterX());
        this.worldData.c(this.getWorldBorder().getCenterZ());
        this.worldData.e(this.getWorldBorder().getDamageBuffer());
        this.worldData.f(this.getWorldBorder().getDamageAmount());
        this.worldData.j(this.getWorldBorder().getWarningDistance());
        this.worldData.k(this.getWorldBorder().getWarningTime());
        this.worldData.b(this.getWorldBorder().j());
        this.worldData.e(this.getWorldBorder().i());
        if(!(this instanceof SecondaryWorldServer)) {
            this.worldMaps.a();
        }

        this.dataManager.saveWorldData(this.worldData, this.server.getPlayerList().t());
    }

    public boolean addEntity(Entity entity) {
        return this.i(entity)?super.addEntity(entity):false;
    }

    public void a(Collection<Entity> collection) {
        ArrayList arraylist = Lists.newArrayList(collection);
        Iterator iterator = arraylist.iterator();

        while(iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if(this.i(entity)) {
                this.entityList.add(entity);
                this.b(entity);
            }
        }

    }

    private boolean i(Entity entity) {
        if(entity.dead) {
            return false;
        } else {
            UUID uuid = entity.getUniqueID();
            if(this.entitiesByUUID.containsKey(uuid)) {
                Entity entity1 = (Entity)this.entitiesByUUID.get(uuid);
                if(this.f.contains(entity1)) {
                    this.f.remove(entity1);
                } else {
                    if(!(entity instanceof EntityHuman)) {
                        return false;
                    }

                    a.warn("Force-added player with duplicate UUID " + uuid.toString());
                }

                this.removeEntity(entity1);
            }

            return true;
        }
    }

    protected void b(Entity entity) {
        super.b(entity);
        this.entitiesById.a(entity.getId(), entity);
        this.entitiesByUUID.put(entity.getUniqueID(), entity);
        Entity[] aentity = entity.aR();
        if(aentity != null) {
            for(int i = 0; i < aentity.length; ++i) {
                this.entitiesById.a(aentity[i].getId(), aentity[i]);
            }
        }

    }

    protected void c(Entity entity) {
        super.c(entity);
        this.entitiesById.d(entity.getId());
        this.entitiesByUUID.remove(entity.getUniqueID());
        Entity[] aentity = entity.aR();
        if(aentity != null) {
            for(int i = 0; i < aentity.length; ++i) {
                this.entitiesById.d(aentity[i].getId());
            }
        }

    }

    public boolean strikeLightning(Entity entity) {
        LightningStrikeEvent lightning = new LightningStrikeEvent(this.getWorld(), (LightningStrike)entity.getBukkitEntity());
        this.getServer().getPluginManager().callEvent(lightning);
        if(lightning.isCancelled()) {
            return false;
        } else if(super.strikeLightning(entity)) {
            this.server.getPlayerList().sendPacketNearby((EntityHuman)null, entity.locX, entity.locY, entity.locZ, 512.0D, this.dimension, new PacketPlayOutSpawnEntityWeather(entity));
            return true;
        } else {
            return false;
        }
    }

    public void broadcastEntityEffect(Entity entity, byte b0) {
        this.getTracker().sendPacketToEntity(entity, new PacketPlayOutEntityStatus(entity, b0));
    }

    public ChunkProviderServer getChunkProviderServer() {
        return (ChunkProviderServer)super.getChunkProvider();
    }

    public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        Explosion explosion = super.createExplosion(entity, d0, d1, d2, f, flag, flag1);
        if(explosion.wasCanceled) {
            return explosion;
        } else {
            if(!flag1) {
                explosion.clearBlocks();
            }

            Iterator iterator = this.players.iterator();

            while(iterator.hasNext()) {
                EntityHuman entityhuman = (EntityHuman)iterator.next();
                if(entityhuman.e(d0, d1, d2) < 4096.0D) {
                    ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutExplosion(d0, d1, d2, f, explosion.getBlocks(), (Vec3D)explosion.b().get(entityhuman)));
                }
            }

            return explosion;
        }
    }

    public void playBlockAction(BlockPosition blockposition, Block block, int i, int j) {
        BlockActionData blockactiondata = new BlockActionData(blockposition, block, i, j);
        Iterator iterator = this.S[this.T].iterator();

        while(iterator.hasNext()) {
            BlockActionData blockactiondata1 = (BlockActionData)iterator.next();
            if(blockactiondata1.equals(blockactiondata)) {
                return;
            }
        }

        this.S[this.T].add(blockactiondata);
    }

    private void ao() {
        while(!this.S[this.T].isEmpty()) {
            int i = this.T;
            this.T ^= 1;
            Iterator iterator = this.S[i].iterator();

            while(iterator.hasNext()) {
                BlockActionData blockactiondata = (BlockActionData)iterator.next();
                if(this.a(blockactiondata)) {
                    this.server.getPlayerList().sendPacketNearby((EntityHuman)null, (double)blockactiondata.a().getX(), (double)blockactiondata.a().getY(), (double)blockactiondata.a().getZ(), 64.0D, this.dimension, new PacketPlayOutBlockAction(blockactiondata.a(), blockactiondata.d(), blockactiondata.b(), blockactiondata.c()));
                }
            }

            this.S[i].clear();
        }

    }

    private boolean a(BlockActionData blockactiondata) {
        IBlockData iblockdata = this.getType(blockactiondata.a());
        return iblockdata.getBlock() == blockactiondata.d()?iblockdata.getBlock().a(this, blockactiondata.a(), iblockdata, blockactiondata.b(), blockactiondata.c()):false;
    }

    public void saveLevel() {
        this.dataManager.a();
    }

    protected void t() {
        boolean flag = this.W();
        super.t();
        int i;
        if(flag != this.W()) {
            for(i = 0; i < this.players.size(); ++i) {
                if(((EntityPlayer)this.players.get(i)).world == this) {
                    ((EntityPlayer)this.players.get(i)).setPlayerWeather(!flag?WeatherType.DOWNFALL:WeatherType.CLEAR, false);
                }
            }
        }

        for(i = 0; i < this.players.size(); ++i) {
            if(((EntityPlayer)this.players.get(i)).world == this) {
                ((EntityPlayer)this.players.get(i)).updateWeather(this.n, this.o, this.p, this.q);
            }
        }

    }

    public MinecraftServer getMinecraftServer() {
        return this.server;
    }

    public EntityTracker getTracker() {
        return this.tracker;
    }

    public PlayerChunkMap getPlayerChunkMap() {
        return this.manager;
    }

    public PortalTravelAgent getTravelAgent() {
        return this.portalTravelAgent;
    }

    public DefinedStructureManager y() {
        return this.dataManager.h();
    }

    public void a(EnumParticle enumparticle, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        this.a(enumparticle, false, d0, d1, d2, i, d3, d4, d5, d6, aint);
    }

    public void a(EnumParticle enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        this.sendParticles((EntityPlayer)null, enumparticle, flag, d0, d1, d2, i, d3, d4, d5, d6, aint);
    }

    public void sendParticles(EntityPlayer sender, EnumParticle enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(enumparticle, flag, (float)d0, (float)d1, (float)d2, (float)d3, (float)d4, (float)d5, (float)d6, i, aint);

        for(int j = 0; j < this.players.size(); ++j) {
            EntityPlayer entityplayer = (EntityPlayer)this.players.get(j);
            if(sender == null || entityplayer.getBukkitEntity().canSee(sender.getBukkitEntity())) {
                BlockPosition blockposition = entityplayer.getChunkCoordinates();
                blockposition.distanceSquared(d0, d1, d2);
                this.a(entityplayer, flag, d0, d1, d2, packetplayoutworldparticles);
            }
        }

    }

    public void a(EntityPlayer entityplayer, EnumParticle enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint) {
        PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(enumparticle, flag, (float)d0, (float)d1, (float)d2, (float)d3, (float)d4, (float)d5, (float)d6, i, aint);
        this.a(entityplayer, flag, d0, d1, d2, packetplayoutworldparticles);
    }

    private void a(EntityPlayer entityplayer, boolean flag, double d0, double d1, double d2, Packet<?> packet) {
        BlockPosition blockposition = entityplayer.getChunkCoordinates();
        double d3 = blockposition.distanceSquared(d0, d1, d2);
        if(d3 <= 1024.0D || flag && d3 <= 262144.0D) {
            entityplayer.playerConnection.sendPacket(packet);
        }

    }

    public Entity getEntity(UUID uuid) {
        return (Entity)this.entitiesByUUID.get(uuid);
    }

    public ListenableFuture<Object> postToMainThread(Runnable runnable) {
        return this.server.postToMainThread(runnable);
    }

    public boolean isMainThread() {
        return this.server.isMainThread();
    }

    public IChunkProvider getChunkProvider() {
        return this.getChunkProviderServer();
    }

    static class BlockActionDataList extends ArrayList<BlockActionData> {
        private BlockActionDataList() {
        }

        BlockActionDataList(Object object) {
            this();
        }
    }
}
