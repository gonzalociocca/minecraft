package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

/**
 * Created by ciocca on 23/10/16.
 */
public class ConcurrentThread extends Thread implements Runnable {
    MinecraftServer minecraftserver;
    WorldServer worldserver;
    boolean tickTiles;
    boolean tickEntitys;
    boolean tickPlayers;
    Random r;
    double tps;
    int runs;
    Long tpsrecord;
    int saveperiod;
    public ConcurrentThread(MinecraftServer ms, WorldServer world,boolean tiles, boolean entitys, boolean players) {
        tickTiles=tiles;
        tickEntitys=entitys;
        tickPlayers=players;
        minecraftserver = ms;
        worldserver = world;
        System.out.println("ConcurrentThread Loaded on World: "+worldserver.getWorld().getName());
        r = new Random();
        saveperiod=600+ r.nextInt(600);
        tpsrecord = System.currentTimeMillis();
        runs = 0;
    }
/*
 Tick 100 200 300
 Long start = 0
 Long end = 100*50ms 200*50ms 300*50ms
 Long time = start - end;
 if(100*50<time){}
 */

    public WorldServer getWorldServer(){
        return worldserver;
    }

    public void run(){
        tps = 20;
        runs = 0;
        Long start = 0L;
        Long end = 0L;
        tpsrecord = System.currentTimeMillis();
        Long sleep = 49L;
        try{
            while(true){

                start = System.currentTimeMillis();
                sleep = 50L;
                try{
                    if(worldserver != null){
                        runs++;
                        if(runs%1000==0){
                            Long left =((System.currentTimeMillis()-tpsrecord)-50000);
                            tps = 20.00D-(left/2500.00D);
                            if(tps<=0){
                                tps=-1;
                            }
                            System.out.println(currentThread().getName()+" run: "+runs+" tps: "+tps);
                            tpsrecord=System.currentTimeMillis();
                        }
                        if(tickTiles){try {
                            worldserver.doTick();
                            if(runs%saveperiod==0){
                                minecraftserver.getPlayerList().savePlayers(worldserver);
                            }
                            else if(runs%(saveperiod-50)==0){
                                worldserver.save(false,null);
                            }
                        } catch (Exception e) {e.printStackTrace();}}
                        if(tickEntitys){try { worldserver.tickEntities();} catch (Exception e) {e.printStackTrace();}}
                        if(tickPlayers){try { worldserver.getTracker().updatePlayers();} catch (Exception e) {e.printStackTrace();}}

                    }else{
                        System.out.println(this.getName()+" WorldServer is null");
                    }
                    end = System.currentTimeMillis();
                    if((System.currentTimeMillis()-tpsrecord) > ((runs%1000)*50)){
                        sleep = 0L;
                    }
                    sleep-=(end-start);
                    if(sleep > 0){
                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }}

                }catch(Exception ex){
                    ex.printStackTrace();

                }
            }}catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("ConcurrentThread "+this.getName()+" exited at time: "+System.currentTimeMillis()+" lastRun: "+runs);
    }
    public void catchup(){

        try{
                    if(worldserver != null){
                        runs++;
                        if(runs%1000==0){
                            Long left =((System.currentTimeMillis()-tpsrecord)-50000);
                            tps = 20.00D-(left/2500.00D);
                            if(tps<=0){
                                tps=20;
                            }
                            System.out.println(currentThread().getName()+" run: "+runs+" tps: "+tps);
                            tpsrecord=System.currentTimeMillis();
                        }
                        if(tickTiles){try {
                            worldserver.doTick();
                            if(runs%saveperiod==0){
                                minecraftserver.getPlayerList().savePlayers(worldserver);
                            }
                            else if(runs%(saveperiod-50)==0){
                                worldserver.save(false,null);
                            }
                        } catch (Exception e) {e.printStackTrace();}}
                        if(tickEntitys){try { worldserver.tickEntities();} catch (Exception e) {e.printStackTrace();}}
                        if(tickPlayers){try { worldserver.getTracker().updatePlayers();} catch (Exception e) {e.printStackTrace();}}

                    }else{
                        System.out.println("catchup worldserver null");
                        return;
                    }

            }catch(Exception e){
            e.printStackTrace();
        }
    }
}
