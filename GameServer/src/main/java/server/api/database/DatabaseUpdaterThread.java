package server.api.database;

import com.google.common.collect.Sets;
import server.ServerPlugin;
import server.api.DatabaseAPI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Created by noname on 14/4/2017.
 */
public class DatabaseUpdaterThread extends Thread {

    // Query should be forced on Quit
    // but it should be delayed if the user has time.

    ConcurrentHashMap<String, Set<SqlQuery>> sqlData = new ConcurrentHashMap();
    boolean pauseQueue = false;

    public DatabaseUpdaterThread() {
        super();
        start();
    }

    public void add(String origin, SqlQuery sqlQuery){
        Set<SqlQuery> set = sqlData.get(origin);
        if(set == null){
            set = Sets.newConcurrentHashSet();
            sqlData.put(origin, set);
        }
        set.add(sqlQuery);
    }

    public void forceAll() {
        pauseQueue = true;

        for (Iterator<Map.Entry<String, Set<SqlQuery>>> it = sqlData.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Set<SqlQuery>> entry = it.next();
                Set<SqlQuery> set = entry.getValue();
                for (Iterator<SqlQuery> setIterator = set.iterator(); setIterator.hasNext(); ) {
                        SqlQuery sqlQuery = setIterator.next();
                        try {
                            execute(sqlQuery);
                            setIterator.remove();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println(sqlQuery.getQuery());
                        }
                }
                if (set.isEmpty()) {
                    it.remove();
                }
        }

        pauseQueue = false;
    }

    public void execute(SqlQuery sqlQuery) throws SQLException {
        if(sqlQuery.hasCallback() && sqlQuery.getQuery().contains("SELECT") && !sqlQuery.getQuery().contains("INSERT")){
            ResultSet resultSet = DatabaseAPI.getConnection().getStatement().executeQuery(sqlQuery.getQuery());
            sqlQuery.getCallback().onSuccess(resultSet);

            if(resultSet != null) {
                resultSet.close();
            }
        } else {
            DatabaseAPI.getConnection().getStatement().execute(sqlQuery.getQuery());
            if(sqlQuery.hasCallback()){
                sqlQuery.getCallback().onSuccess(null);
            }
        }
    }

    public void forceUpdate(String name) {
        pauseQueue = true;
        try {
            String player = name.toLowerCase();
            if (sqlData.containsKey(player)) {
                Set<SqlQuery> set = sqlData.get(player);

                for (Iterator<SqlQuery> it = set.iterator(); it.hasNext(); ) {
                    SqlQuery sqlQuery = it.next();
                    try {
                        execute(sqlQuery);
                        it.remove();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println(sqlQuery.getQuery());
                    }
                }
                if (set.isEmpty()) {
                    sqlData.remove(player);
                } else {
                    ServerPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to update " + set.size() + " mysql queries for player of name: " + player);
                }
            }
        }finally {
            pauseQueue = false;
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }


    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                if(!pauseQueue) {
                    for (Iterator<Map.Entry<String, Set<SqlQuery>>> it = sqlData.entrySet().iterator(); it.hasNext(); ) {
                        if(!pauseQueue) {
                            Map.Entry<String, Set<SqlQuery>> entry = it.next();
                            Set<SqlQuery> set = entry.getValue();
                            for (Iterator<SqlQuery> setIterator = set.iterator(); setIterator.hasNext(); ) {
                                if(!pauseQueue) {
                                    SqlQuery sqlQuery = setIterator.next();
                                    try {
                                        execute(sqlQuery);
                                        setIterator.remove();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        System.out.println(sqlQuery.getQuery());
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
