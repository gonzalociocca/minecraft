package me.gonzalociocca.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Van Rage
 */
public class MySQLManager {
    private JavaPlugin plugin = null;
    private HashMap<String,MySQL> databases = new HashMap();
    
    MySQLManager(JavaPlugin pl) {
    plugin = pl;
    }
    
    public JavaPlugin getPlugin(){
        return plugin;
    }
    private void log(java.lang.Exception tolog){
          Logger.getLogger(MySQLManager.class.getName()).log(Level.SEVERE, null, tolog);        
    }
    private void log(String tolog){
          Logger.getLogger(MySQLManager.class.getName()).log(Level.SEVERE, null, tolog);        
    }
    
/**
* Creates a new MySQL instance
*
* @param hostname
* Name of the host
* @param port
* Port number
* @param database
* Database name
* @param username
* Username
* @param password
* Password
*/
    public MySQL createDatabase(String gametype, String host, String port, String db, String user, String pwd){
        if(databases.containsKey(gametype)){
           return databases.get(gametype);
        }
        MySQL mysql = new me.gonzalociocca.mysql.MySQL(plugin, host, port, db, user, pwd);
        databases.put(gametype, mysql);
    return mysql;
    }

    
    public Connection getConnection(String gametype){
        
        if(databases.containsKey(gametype)){
            try {
                return databases.get(gametype).openConnection();
            } catch (SQLException | ClassNotFoundException ex) {
                log(ex);
            }
        }
         log("Cannot open new mysql connection for: "+gametype);
        return null;
    }
    
    

    
    
    
}
