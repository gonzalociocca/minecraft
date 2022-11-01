package me.gonzalociocca.minelevel.core.database;

import com.google.common.collect.Sets;
import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.PlayerData;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class DatabaseUpdateThread extends Thread {
    /**
     * Goals:
     * Run store buyable perks like ranks, boosters, companions(block pets), mystery boxes on main thread
     * <p>
     * Load PlayerData on a Separated Thread and let players wait until is loaded.
     * Stats, kits, money, etc, should be added to queue.
     * Force sql call run on Quit
     **/
    Main Manager;

    static ConcurrentHashMap<String, Set<String>> sqlData = new ConcurrentHashMap();

    public DatabaseUpdateThread(Main manager) {
        super();
        Manager = manager;
    }

    public static void addToQueue(String name, String sqlUpdate) {
        String player = name.toLowerCase();

        Set<String> set = sqlData.get(player);
        if (set == null) {
            set = Sets.newConcurrentHashSet();
            sqlData.put(player, set);
        }
        set.add(sqlUpdate);
    }

    boolean pauseQueue = false;

    public boolean forceAll(){
        pauseQueue = true;
        for(Iterator<Map.Entry<String, Set<String>>> it = sqlData.entrySet().iterator();it.hasNext();){
            Map.Entry<String, Set<String>> entry = it.next();
            String key = entry.getKey();
            Set<String> value = entry.getValue();
            if(value != null && !value.isEmpty()){
                for(Iterator<String> it2 = value.iterator();it2.hasNext();){
                    String sqlUpdate = it2.next();
                    try {
                        Manager.getDB().getStatement().execute(sqlUpdate);
                        it2.remove();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(value.isEmpty()){
                    it.remove();
                }
            }
        }
        pauseQueue = false;

        return sqlData.isEmpty();
    }

    public void forceUpdate(String name) {
        pauseQueue = true;
        try {
            String player = name.toLowerCase();
            if (sqlData.containsKey(player)) {
                Set<String> set = sqlData.get(player);

                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                    String sqlUpdate = it.next();
                    try {
                        Manager.getDB().getStatement().execute(sqlUpdate);
                        it.remove();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (set.isEmpty()) {
                    sqlData.remove(player);
                } else {
                    Manager.getLogger().log(Level.SEVERE, "Failed to update " + set.size() + " mysql queries for player of name: " + player);
                }
            }
        } finally {
            pauseQueue = false;
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    public static boolean checkMap = false;

    public static void checkMap(){
        for (PlayerData pd1 : Variable.DatabasePlayerMap.values()) {
            if (pd1.canSave()) {
                DatabaseUpdateThread.addToQueue(pd1.getName(), pd1.getSaveQuery());
                pd1.changed = false;
            }
        }
        checkMap = false;
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            try{
                if(checkMap){
                    checkMap();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                if (!pauseQueue) {
                    for (Iterator<Map.Entry<String, Set<String>>> it = sqlData.entrySet().iterator(); it.hasNext(); ) {
                        if (!pauseQueue) {
                            Map.Entry<String, Set<String>> entry = it.next();
                            Set<String> set = entry.getValue();
                            for (Iterator<String> setIterator = set.iterator(); setIterator.hasNext(); ) {
                                if (!pauseQueue) {
                                    String sqlUpdate = setIterator.next();
                                    try {
                                        Manager.getDB().getStatement().execute(sqlUpdate);
                                        setIterator.remove();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (set.isEmpty()) {
                                it.remove();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
