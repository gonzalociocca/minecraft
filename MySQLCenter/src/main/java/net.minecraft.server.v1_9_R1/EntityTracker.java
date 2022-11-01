package net.minecraft.server.v1_9_R1;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.TrackingRange;

public class EntityTracker {
    private static final Logger a = LogManager.getLogger();
    private final WorldServer world;
    private Set<EntityTrackerEntry> c = Sets.newHashSet();
    public IntHashMap<EntityTrackerEntry> trackedEntities = new IntHashMap();
    private int e;

    public EntityTracker(WorldServer worldserver) {
        this.world = worldserver;
        this.e = worldserver.getMinecraftServer().getPlayerList().d();
    }

    public static long a(double d0) {
        return MathHelper.d(d0 * 4096.0D);
    }

    public void track(Entity entity) {
        if(entity instanceof EntityPlayer) {
            this.addEntity(entity, 512, 2);
            EntityPlayer entityplayer = (EntityPlayer)entity;
            Iterator iterator = this.c.iterator();

            while(iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
                if(entitytrackerentry.b() != entityplayer) {
                    entitytrackerentry.updatePlayer(entityplayer);
                }
            }
        } else if(entity instanceof EntityFishingHook) {
            this.addEntity(entity, 64, 5, true);
        } else if(entity instanceof EntityArrow) {
            this.addEntity(entity, 64, 20, false);
        } else if(entity instanceof EntitySmallFireball) {
            this.addEntity(entity, 64, 10, false);
        } else if(entity instanceof EntityFireball) {
            this.addEntity(entity, 64, 10, false);
        } else if(entity instanceof EntitySnowball) {
            this.addEntity(entity, 64, 10, true);
        } else if(entity instanceof EntityEnderPearl) {
            this.addEntity(entity, 64, 10, true);
        } else if(entity instanceof EntityEnderSignal) {
            this.addEntity(entity, 64, 4, true);
        } else if(entity instanceof EntityEgg) {
            this.addEntity(entity, 64, 10, true);
        } else if(entity instanceof EntityPotion) {
            this.addEntity(entity, 64, 10, true);
        } else if(entity instanceof EntityThrownExpBottle) {
            this.addEntity(entity, 64, 10, true);
        } else if(entity instanceof EntityFireworks) {
            this.addEntity(entity, 64, 10, true);
        } else if(entity instanceof EntityItem) {
            this.addEntity(entity, 64, 20, true);
        } else if(entity instanceof EntityMinecartAbstract) {
            this.addEntity(entity, 80, 3, true);
        } else if(entity instanceof EntityBoat) {
            this.addEntity(entity, 80, 3, true);
        } else if(entity instanceof EntitySquid) {
            this.addEntity(entity, 64, 3, true);
        } else if(entity instanceof EntityWither) {
            this.addEntity(entity, 80, 3, false);
        } else if(entity instanceof EntityShulkerBullet) {
            this.addEntity(entity, 80, 3, true);
        } else if(entity instanceof EntityBat) {
            this.addEntity(entity, 80, 3, false);
        } else if(entity instanceof EntityEnderDragon) {
            this.addEntity(entity, 160, 3, true);
        } else if(entity instanceof IAnimal) {
            this.addEntity(entity, 80, 3, true);
        } else if(entity instanceof EntityTNTPrimed) {
            this.addEntity(entity, 160, 10, true);
        } else if(entity instanceof EntityFallingBlock) {
            this.addEntity(entity, 160, 20, true);
        } else if(entity instanceof EntityHanging) {
            this.addEntity(entity, 160, 2147483647, false);
        } else if(entity instanceof EntityArmorStand) {
            this.addEntity(entity, 160, 3, true);
        } else if(entity instanceof EntityExperienceOrb) {
            this.addEntity(entity, 160, 20, true);
        } else if(entity instanceof EntityAreaEffectCloud) {
            this.addEntity(entity, 160, 2147483647, true);
        } else if(entity instanceof EntityEnderCrystal) {
            this.addEntity(entity, 256, 2147483647, false);
        }

    }

    public void addEntity(Entity entity, int i, int j) {
        this.addEntity(entity, i, j, false);
    }

    public void addEntity(Entity entity, int ai, int j, boolean flag) {
        AsyncCatcher.catchOp("entity track");
        final int i = TrackingRange.getEntityTrackingRange(entity, ai);

        try {
            if(this.trackedEntities.b(entity.getId())) {
                throw new IllegalStateException("Entity is already tracked!");
            }

            EntityTrackerEntry throwable = new EntityTrackerEntry(entity, i, this.e, j, flag);
            this.c.add(throwable);
            this.trackedEntities.a(entity.getId(), throwable);
            throwable.scanPlayers(this.world.players);
        } catch (Throwable var11) {
            CrashReport crashreport = CrashReport.a(var11, "Adding entity to track");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity To Track");
            crashreportsystemdetails.a("Tracking range", i + " blocks");
            crashreportsystemdetails.a("Update interval", new Callable() {
                public String a() throws Exception {
                    String s = "Once per " + i + " ticks";
                    if(i == 2147483647) {
                        s = "Maximum (" + s + ")";
                    }

                    return s;
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
            entity.appendEntityCrashDetails(crashreportsystemdetails);
            ((EntityTrackerEntry)this.trackedEntities.get(entity.getId())).b().appendEntityCrashDetails(crashreport.a("Entity That Is Already Tracked"));

            try {
                throw new ReportedException(crashreport);
            } catch (ReportedException var10) {
                a.error("\"Silently\" catching entity tracking error.", var10);
            }
        }

    }

    public void untrackEntity(Entity entity) {
        AsyncCatcher.catchOp("entity untrack");
        if(entity instanceof EntityPlayer) {
            EntityPlayer entitytrackerentry1 = (EntityPlayer)entity;
            Iterator iterator = this.c.iterator();

            while(iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
                entitytrackerentry.a(entitytrackerentry1);
            }
        }

        EntityTrackerEntry entitytrackerentry11 = (EntityTrackerEntry)this.trackedEntities.d(entity.getId());
        if(entitytrackerentry11 != null) {
            this.c.remove(entitytrackerentry11);
            entitytrackerentry11.a();
        }

    }

    public void updatePlayers() {

        try {
            ArrayList arraylist = Lists.newArrayList();

            for(EntityTrackerEntry i : c) {
                i.track(this.world.players);
                if (i.b) {
                    Entity entityplayer = i.b();
                    if (entityplayer instanceof EntityPlayer) {
                        arraylist.add((EntityPlayer) entityplayer);
                    }
                }
            }

            for (int var7 = 0; var7 < arraylist.size(); ++var7) {
                EntityPlayer var8 = (EntityPlayer) arraylist.get(var7);
                Iterator iterator1 = this.c.iterator();

                while (iterator1.hasNext()) {
                    EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) iterator1.next();
                    if (entitytrackerentry1.b() != var8) {
                        entitytrackerentry1.updatePlayer(var8);
                    }
                }
            }
        }catch(Exception e){

        }
    }

    public void a(EntityPlayer entityplayer) {
        Iterator iterator = this.c.iterator();

        while(iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
            if(entitytrackerentry.b() == entityplayer) {
                entitytrackerentry.scanPlayers(this.world.players);
            } else {
                entitytrackerentry.updatePlayer(entityplayer);
            }
        }

    }

    public void a(Entity entity, Packet<?> packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntities.get(entity.getId());
        if(entitytrackerentry != null) {
            entitytrackerentry.broadcast(packet);
        }

    }

    public void sendPacketToEntity(Entity entity, Packet<?> packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntities.get(entity.getId());
        if(entitytrackerentry != null) {
            entitytrackerentry.broadcastIncludingSelf(packet);
        }

    }

    public void untrackPlayer(EntityPlayer entityplayer) {
        Iterator iterator = this.c.iterator();

        while(iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
            entitytrackerentry.clear(entityplayer);
        }

    }

    public void a(EntityPlayer entityplayer, Chunk chunk) {
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList();
        Iterator iterator = this.c.iterator();

        while(iterator.hasNext()) {
            EntityTrackerEntry entity1 = (EntityTrackerEntry)iterator.next();
            Entity entity = entity1.b();
            if(entity != entityplayer && entity.ab == chunk.locX && entity.ad == chunk.locZ) {
                entity1.updatePlayer(entityplayer);
                if(entity instanceof EntityInsentient && ((EntityInsentient)entity).getLeashHolder() != null) {
                    arraylist.add(entity);
                }

                if(!entity.bu().isEmpty()) {
                    arraylist1.add(entity);
                }
            }
        }

        Entity entity11;
        if(!arraylist.isEmpty()) {
            iterator = arraylist.iterator();

            while(iterator.hasNext()) {
                entity11 = (Entity)iterator.next();
                entityplayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(entity11, ((EntityInsentient)entity11).getLeashHolder()));
            }
        }

        if(!arraylist1.isEmpty()) {
            iterator = arraylist1.iterator();

            while(iterator.hasNext()) {
                entity11 = (Entity)iterator.next();
                entityplayer.playerConnection.sendPacket(new PacketPlayOutMount(entity11));
            }
        }

    }

    public void a(int i) {
        this.e = (i - 1) * 16;
        Iterator iterator = this.c.iterator();

        while(iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
            entitytrackerentry.a(this.e);
        }

    }
}
