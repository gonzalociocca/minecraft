package net.minecraft.server.v1_8_R3;

import java.util.List;

/**
 * Created by ciocca on 23/10/16.
 */
public class ConcurrentThread extends Thread implements Runnable {
    MinecraftServer minecraftserver;
    WorldServer worldserver;
    boolean tickTiles;
    boolean tickEntitys;
    boolean tickPlayers;
    public ConcurrentThread(MinecraftServer ms, WorldServer world,boolean tiles, boolean entitys, boolean players) {
        tickTiles=tiles;
        tickEntitys=entitys;
        tickPlayers=players;
        minecraftserver = ms;
        worldserver = world;
        System.out.println("ConcurrentThread Loaded on World: "+worldserver.getWorld().getName());
    }


    public WorldServer getWorldServer(){
        return worldserver;
    }
    public void run(){
        double tps = 20;
        int runs = 0;
        Long start = 0L;
        Long end = 0L;
        Long sleep = 49L;
        Long tpsrecord;
        tpsrecord = System.currentTimeMillis();
        try{
        while(true){

                start = System.currentTimeMillis();
                sleep = 49L;
            try{
                if(worldserver != null){
                    if(tickTiles){try {worldserver.doTick();} catch (Exception e) {e.printStackTrace();}}
                    if(tickEntitys){try { worldserver.tickEntities();} catch (Exception e) {e.printStackTrace();}}
                    if(tickPlayers){try { worldserver.getTracker().updatePlayers();} catch (Exception e) {e.printStackTrace();}}

                }else if(runs > 100){
                    System.out.println("World Deleted? Stopped ticking entitys at "+ currentThread().getName());
                    return;
                }else{
                    System.out.println("WorldServer List is Empty at EntityThread, replenishing");
                }
                end = System.currentTimeMillis();
                sleep-=(end-start);
                if(sleep > 1){
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }}

                runs+=1;
                if(runs%1000==0){
                    Long left =((System.currentTimeMillis()-tpsrecord)-50000);
                    tps = 20.00D-(left/2500.00D);
                    if(tps<=0){
                        tps=20;
                    }
                    System.out.println(currentThread().getName()+" run: "+runs+" tps: "+tps);
                    tpsrecord=System.currentTimeMillis();
                }
            }catch(Exception ex){
                ex.printStackTrace();

            }
        }}catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("ConcurrentThread "+this.getName()+" exited at time: "+System.currentTimeMillis()+" lastRun: "+runs);
    }
}
