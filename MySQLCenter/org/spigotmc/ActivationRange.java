//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.spigotmc;

import co.aikar.timings.SpigotTimings;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.v1_9_R1.*;
import org.spigotmc.SpigotWorldConfig;

public class ActivationRange {
    static AxisAlignedBB maxBB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    static AxisAlignedBB miscBB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    static AxisAlignedBB animalBB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    static AxisAlignedBB monsterBB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

    public ActivationRange() {
    }

    public static byte initializeEntityActivationType(Entity entity) {
        return (byte)(!(entity instanceof EntityMonster) && !(entity instanceof EntitySlime)?(!(entity instanceof EntityCreature) && !(entity instanceof EntityAmbient)?3:2):1);
    }

    public static boolean initializeEntityActivationState(Entity entity, SpigotWorldConfig config) {
        return entity.activationType == 3 && config.miscActivationRange == 0 || entity.activationType == 2 && config.animalActivationRange == 0 || entity.activationType == 1 && config.monsterActivationRange == 0 || entity instanceof EntityHuman || entity instanceof EntityProjectile || entity instanceof EntityEnderDragon || entity instanceof EntityComplexPart || entity instanceof EntityWither || entity instanceof EntityFireball || entity instanceof EntityWeather || entity instanceof EntityTNTPrimed || entity instanceof EntityFallingBlock || entity instanceof EntityEnderCrystal || entity instanceof EntityFireworks;
    }

    public static void activateEntities(World world) {
        int miscActivationRange = world.spigotConfig.miscActivationRange;
        int animalActivationRange = world.spigotConfig.animalActivationRange;
        int monsterActivationRange = world.spigotConfig.monsterActivationRange;
        int maxRange = Math.max(monsterActivationRange, animalActivationRange);
        maxRange = Math.max(maxRange, miscActivationRange);
        maxRange = Math.min((world.spigotConfig.viewDistance << 4) - 8, maxRange);
        Iterator var5 = world.players.iterator();

        while(var5.hasNext()) {
            EntityHuman player = (EntityHuman)var5.next();
            player.activatedTick = (long)MinecraftServer.currentTick;
            maxBB = player.getBoundingBox().grow((double)maxRange, 256.0D, (double)maxRange);
            miscBB = player.getBoundingBox().grow((double)miscActivationRange, 256.0D, (double)miscActivationRange);
            animalBB = player.getBoundingBox().grow((double)animalActivationRange, 256.0D, (double)animalActivationRange);
            monsterBB = player.getBoundingBox().grow((double)monsterActivationRange, 256.0D, (double)monsterActivationRange);
            int i = MathHelper.floor(maxBB.a / 16.0D);
            int j = MathHelper.floor(maxBB.d / 16.0D);
            int k = MathHelper.floor(maxBB.c / 16.0D);
            int l = MathHelper.floor(maxBB.f / 16.0D);

            for(int i1 = i; i1 <= j; ++i1) {
                for(int j1 = k; j1 <= l; ++j1) {
                    if(world.getWorld().isChunkLoaded(i1, j1)) {
                        activateChunkEntities(world.getChunkAt(i1, j1));
                    }
                }
            }
        }

    }

    private static void activateChunkEntities(Chunk chunk) {
        List[] var1 = chunk.entitySlices;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            List slice = var1[var3];
            Iterator var5 = slice.iterator();

            while(var5.hasNext()) {
                Entity entity = (Entity)var5.next();
                if((long)MinecraftServer.currentTick > entity.activatedTick) {
                    if(entity.defaultActivationState) {
                        entity.activatedTick = (long)MinecraftServer.currentTick;
                    } else {
                        switch(entity.activationType) {
                            case 1:
                                if(monsterBB.b(entity.getBoundingBox())) {
                                    entity.activatedTick = (long)MinecraftServer.currentTick;
                                }
                                break;
                            case 2:
                                if(animalBB.b(entity.getBoundingBox())) {
                                    entity.activatedTick = (long)MinecraftServer.currentTick;
                                }
                                break;
                            case 3:
                            default:
                                if(miscBB.b(entity.getBoundingBox())) {
                                    entity.activatedTick = (long)MinecraftServer.currentTick;
                                }
                        }
                    }
                }
            }
        }

    }

    public static boolean checkEntityImmunities(Entity entity) {
        if(!entity.inWater && entity.fireTicks <= 0) {
            if(!(entity instanceof EntityArrow)) {
                if(!entity.onGround || !entity.passengers.isEmpty() || entity.isPassenger()) {
                    return true;
                }
            } else if(!((EntityArrow)entity).inGround) {
                return true;
            }

            if(entity instanceof EntityLiving) {
                EntityLiving living = (EntityLiving)entity;
                if(living.hurtTicks > 0 || living.effects.size() > 0) {
                    return true;
                }

                if(entity instanceof EntityCreature && ((EntityCreature)entity).getGoalTarget() != null) {
                    return true;
                }

                if(entity instanceof EntityVillager && ((EntityVillager)entity).da()) {
                    return true;
                }

                if(entity instanceof EntityAnimal) {
                    EntityAnimal animal = (EntityAnimal)entity;
                    if(animal.isBaby() || animal.isInLove()) {
                        return true;
                    }

                    if(entity instanceof EntitySheep && ((EntitySheep)entity).isSheared()) {
                        return true;
                    }
                }

                if(entity instanceof EntityCreeper && ((EntityCreeper)entity).isIgnited()) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static boolean checkIfActive(Entity entity)
    {
        boolean isActive = (entity.activatedTick >= MinecraftServer.currentTick) || (entity.defaultActivationState);
        if (!isActive)
        {
            if ((MinecraftServer.currentTick - entity.activatedTick - 1L) % 20L == 0L)
            {
                if (checkEntityImmunities(entity)) {
                    entity.activatedTick = (MinecraftServer.currentTick + 20);
                }
                isActive = true;
            }
        }
        else if ((!entity.defaultActivationState) && (entity.ticksLived % 4 == 0) && (!checkEntityImmunities(entity))) {
            isActive = false;
        }
        int x = MathHelper.floor(entity.locX);
        int z = MathHelper.floor(entity.locZ);
        if ((isActive) && (!entity.world.areChunksLoaded(new BlockPosition(x, 0, z), 16))) {
            isActive = false;
        }
        return isActive;
    }
}
