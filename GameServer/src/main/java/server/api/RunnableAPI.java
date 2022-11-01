package server.api;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import server.ServerPlugin;
import server.api.runnable.RunnableCallback;

import java.util.Iterator;
import java.util.Set;

public class RunnableAPI extends Thread implements Listener{

    static Set<RunnableCallback> runnableCallbackList = Sets.newConcurrentHashSet();

    public RunnableAPI(){
        Bukkit.getPluginManager().registerEvents(this, ServerPlugin.getInstance());
        start();
    }

    public static void add(RunnableCallback runnableCallback){
        runnableCallbackList.add(runnableCallback);
    }

    @Override
    public void run() {
        super.run();
        while (true){
                if(!runnableCallbackList.isEmpty()){
                    for(Iterator<RunnableCallback> it = runnableCallbackList.iterator(); it.hasNext();){
                        try {
                            RunnableCallback runnableCallback = it.next();
                            runnableCallback.onRun();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        it.remove();
                    }
                }
            try {
                sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
