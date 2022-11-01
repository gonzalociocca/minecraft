package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

/**
 * Created by ciocca on 23/10/16.
 */
public class ConcurrentCatchupThread extends Thread implements Runnable {
    MinecraftServer minecraftserver;
    boolean tickTiles;
    boolean tickEntitys;
    boolean tickPlayers;
    Random r;
    public ConcurrentCatchupThread(MinecraftServer ms,int number) {
        minecraftserver = ms;
        r = new Random();
        System.out.println("ConcurrentCatchupThread "+number);
    }

    public void run(){

            while(true){
                try {
                    Thread.sleep(r.nextInt(5)+1);
                for(ConcurrentThread tr : minecraftserver.concurrentThreads){
                    if(tr != null){
                    if((System.currentTimeMillis()-tr.tpsrecord) > ((tr.runs%1000)*50)){
                        tr.catchup();
                    }}
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
}
