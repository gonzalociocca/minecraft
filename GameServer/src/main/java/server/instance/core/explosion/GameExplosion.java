package server.instance.core.explosion;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.util.Vector;
import server.ServerPlugin;
import server.instance.GameServer;
import server.common.event.ExplosionEvent;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.util.UtilAction;
import server.util.UtilAlg;
import server.util.UtilBlock;
import server.util.UtilMath;

import java.util.*;

public class GameExplosion {
    @Expose private boolean _regenerateGround = true;
    @Expose private boolean _temporaryDebris = true;
    @Expose private boolean _enableDebris = false;
    @Expose private boolean _tntSpread = true;
    private HashSet<FallingBlock> _explosionBlocks = new HashSet();
    private BlockRestore _blockRestore = new BlockRestore();

    public void cleanAll() {
        _explosionBlocks.clear();
        _blockRestore.cleanAll();
    }

    public BlockRestore getBlockRestore() {
        return _blockRestore;
    }

    public boolean canRegenerateGround() {
        return _regenerateGround;
    }

    public boolean canTemporaryDebris() {
        return _temporaryDebris;
    }

    public boolean canDebris() {
        return _enableDebris;
    }

    public boolean canSpreadTnT() {
        return _tntSpread;
    }

    public void setRegenerateGround(boolean regenerate) {
        _regenerateGround = regenerate;
    }

    public void setDebris(boolean value) {
        _enableDebris = value;
    }

    public void setSpreadTnT(boolean value) {
        _tntSpread = value;
    }

    public void setTemporaryDebris(boolean value) {
        _temporaryDebris = value;
    }

    public HashSet<FallingBlock> getExplosionBlocks() {
        return _explosionBlocks;
    }

    public void BlockExplosion(Collection<Block> blockSet, Location mid, boolean onlyAbove) {
        if (blockSet.isEmpty()) {
            return;
        }

        final HashMap blocks = new HashMap();

        for (Block cur : blockSet) {
            if (cur.getTypeId() != 0) {
                if ((!onlyAbove) || (cur.getY() >= mid.getY())) {
                    blocks.put(cur, new AbstractMap.SimpleEntry(cur.getTypeId(), Byte.valueOf(cur.getData())));

                    cur.setType(Material.AIR);
                }
            }
        }
        final Location fLoc = mid;
        Bukkit.getServer().getScheduler().runTaskLater(ServerPlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Block cur : (List<Block>) blocks.keySet()) {
                            if ((((Integer) ((Map.Entry) blocks.get(cur)).getKey()) != 98) || (
                                    (((Byte) ((Map.Entry) blocks.get(cur)).getValue()) != 0) && (((Byte) ((Map.Entry) blocks.get(cur)).getValue()) != 3))) {
                                double chance = 0.2D + _explosionBlocks.size() / 120.0D;
                                if (Math.random() > Math.min(0.85D, chance)) {
                                    FallingBlock fall = cur.getWorld().spawnFallingBlock(cur.getLocation().add(0.5D, 0.5D, 0.5D), ((Integer) ((Map.Entry) blocks.get(cur)).getKey()).intValue(), ((Byte) ((Map.Entry) blocks.get(cur)).getValue()).byteValue());

                                    Vector vec = UtilAlg.getTrajectory(fLoc, fall.getLocation());
                                    if (vec.getY() < 0.0D) vec.setY(vec.getY() * -1.0D);

                                    UtilAction.velocity(fall, vec, 0.5D + 0.25D * Math.random(), false, 0.0D, 0.4D + 0.2D * Math.random(), 10.0D, false);

                                    _explosionBlocks.add(fall);
                                }
                            }
                        }
                    }
                }
                , 1L);
    }

    public void onItemSpawn(ItemSpawnEvent event) {
        for (FallingBlock block : getExplosionBlocks()) {
            if (UtilMath.offset(event.getEntity().getLocation(), block.getLocation()) < 1.0D) {
                event.setCancelled(true);
            }
        }
    }

    public void onEntityExplode(GameServer game, EntityExplodeEvent event) {
        if (event.getEntity() == null) {
            event.blockList().clear();
        }
        if (!event.isCancelled() && !event.blockList().isEmpty()) {
            if (event.getEntityType() == EntityType.CREEPER) {
                event.blockList().clear();
            }
            if (event.getEntityType() == EntityType.WITHER_SKULL) {
                event.blockList().clear();
            }

            ExplosionEvent explodeEvent = new ExplosionEvent(game, event.blockList());

            Bukkit.getServer().getPluginManager().callEvent(explodeEvent);

            event.setYield(0.0F);

            final HashMap blocks = new HashMap();

            for (Block cur : event.blockList()) {
                if ((cur.getTypeId() != 0) && (!cur.isLiquid())) {
                    if ((cur.getType() == Material.CHEST) ||
                            (cur.getType() == Material.IRON_ORE) ||
                            (cur.getType() == Material.COAL_ORE) ||
                            (cur.getType() == Material.GOLD_ORE) ||
                            (cur.getType() == Material.DIAMOND_ORE)) {
                        cur.breakNaturally();
                    } else {
                        blocks.put(cur, new AbstractMap.SimpleEntry(Integer.valueOf(cur.getTypeId()), Byte.valueOf(cur.getData())));

                        if (!canRegenerateGround()) {
                            if ((cur.getTypeId() != 98) || ((cur.getData() != 0) && (cur.getData() != 3))) {
                                cur.setTypeId(0);
                            }
                        } else {
                            int heightDiff = cur.getLocation().getBlockY() - event.getEntity().getLocation().getBlockY();
                            getBlockRestore().Add(cur, 0, (byte) 0, (long) (20000 + heightDiff * 3000 + Math.random() * 2900.0D));
                        }
                    }
                }
            }
            event.blockList().clear();

            final Entity fEnt = event.getEntity();
            final Location fLoc = event.getLocation();
            Bukkit.getServer().getScheduler().runTaskLater(ServerPlugin.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            for (Iterator it = blocks.keySet().iterator(); it.hasNext(); ) {
                                Block cur = (Block) it.next();
                                if ((((Integer) ((Map.Entry) blocks.get(cur)).getKey()) != 98) || (
                                        (((Byte) ((Map.Entry) blocks.get(cur)).getValue()) != 0) && (((Byte) ((Map.Entry) blocks.get(cur)).getValue()) != 3))) {
                                    if ((canSpreadTnT()) && (((Integer) ((Map.Entry) blocks.get(cur)).getKey()) == 46)) {
                                        TNTPrimed ent = cur.getWorld().spawn(cur.getLocation().add(0.5D, 0.5D, 0.5D), TNTPrimed.class);
                                        org.bukkit.util.Vector vec = UtilAlg.getTrajectory(fEnt, ent);
                                        if (vec.getY() < 0.0D) vec.setY(vec.getY() * -1.0D);

                                        UtilAction.velocity(ent, vec, 1.0D, false, 0.0D, 0.6D, 10.0D, false);

                                        ent.setFuseTicks(10);
                                    } else {
                                        double chance = 0.85D + getExplosionBlocks().size() / 500.0D;
                                        if (Math.random() > Math.min(0.975D, chance)) {
                                            FallingBlock fall = cur.getWorld().spawnFallingBlock(cur.getLocation().add(0.5D, 0.5D, 0.5D), ((Integer) ((Map.Entry) blocks.get(cur)).getKey()).intValue(), ((Byte) ((Map.Entry) blocks.get(cur)).getValue()).byteValue());

                                            org.bukkit.util.Vector vec = UtilAlg.getTrajectory(fEnt, fall);
                                            if (vec.getY() < 0.0D) vec.setY(vec.getY() * -1.0D);

                                            UtilAction.velocity(fall, vec, 0.5D + 0.25D * Math.random(), false, 0.0D, 0.4D + 0.2D * Math.random(), 10.0D, false);

                                            getExplosionBlocks().add(fall);
                                        }

                                    }

                                }
                            }

                            for (Block cur : UtilBlock.getInRadius(fLoc, 4.0D).keySet())
                                if ((cur.getTypeId() == 98) && (
                                        (cur.getData() == 0) || (cur.getData() == 3)))
                                    cur.setTypeIdAndData(98, (byte) 2, true);
                        }
                    }
                    , 1L);
        }
    }

    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (getBlockRestore().containsBlock(event.getBlock())) {
                event.setCancelled(true);
            }
        }
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            if (getBlockRestore().containsBlock(event.getBlockPlaced())) {
                event.setCancelled(true);
            }
        }
    }

    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.TICK) {
            if (!getBlockRestore().getBlocks().isEmpty()) {
                for (Iterator<BlockRestoreData> it = getBlockRestore().getBlocks().values().iterator(); it.hasNext(); ) {
                    BlockRestoreData data = it.next();
                    if (data.expire()) {
                        it.remove();
                    }
                }
            }

            Iterator fallingIterator = getExplosionBlocks().iterator();

            while (fallingIterator.hasNext()) {
                FallingBlock cur = (FallingBlock) fallingIterator.next();

                if ((cur.isDead()) || (!cur.isValid()) || (cur.getTicksLived() > 400) || (!cur.getWorld().isChunkLoaded(cur.getLocation().getBlockX() >> 4, cur.getLocation().getBlockZ() >> 4))) {
                    fallingIterator.remove();

                    if ((cur.getTicksLived() > 400) || (!cur.getWorld().isChunkLoaded(cur.getLocation().getBlockX() >> 4, cur.getLocation().getBlockZ() >> 4))) {
                        cur.remove();
                        continue;
                    }

                    Block block = cur.getLocation().getBlock();
                    block.setTypeIdAndData(0, (byte) 0, true);

                    if (canDebris()) {
                        if (canTemporaryDebris()) {
                            getBlockRestore().Add(block, cur.getBlockId(), cur.getBlockData(), 10000L);
                        } else {
                            block.setTypeIdAndData(cur.getBlockId(), cur.getBlockData(), true);
                        }
                    } else {
                        cur.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, cur.getBlockId());
                    }

                    cur.remove();
                }
            }
        }
    }

    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.getRadius() < 5.0F) {
            for (Block block : UtilBlock.getInRadius(event.getEntity().getLocation(), event.getRadius()).keySet()) {
                if (block.isLiquid()) {
                    block.setTypeId(0);
                }
            }
        }
    }

    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (!event.isCancelled()) {
            Block push = event.getBlock();

            for (int i = 0; i < 13; i++) {
                push = push.getRelative(event.getDirection());

                if (push.getType() == Material.AIR) {
                    break;
                }
                if (getBlockRestore().containsBlock(push)) {
                    push.getWorld().playEffect(push.getLocation(), Effect.STEP_SOUND, push.getTypeId());
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

}