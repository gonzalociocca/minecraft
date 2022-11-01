package mineultra.game.center.game.games.skywars;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import mineultra.core.common.MySQL;
import mineultra.game.center.centerManager;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author cuack
 */
public class Stats implements Listener {
    centerManager plugin = null;
    String host = null;
    String port = "3306";
    String db = "stats";
    String user = "root";
    String pwd = "meh";
    String table = "statistics";
    boolean enabled = true;
    public Stats(centerManager pl){
        System.out.println("Starting Statistics...");
        plugin = pl;
        try{
        host = plugin.getConfig().getString("stats.host");
        port = plugin.getConfig().getString("stats.port");
        db = plugin.getConfig().getString("stats.db");
        user = plugin.getConfig().getString("stats.user");
        pwd = plugin.getConfig().getString("stats.pwd");
        table = plugin.getConfig().getString("stats.table");
        }catch(Exception e){
            System.out.println("Statistics Disabled");
            return;
        }
        if(host==null){
            return;
        }
        try{
        this.openData();    
        }catch(Exception e){
            System.out.println("Cant establish connection to Statistics");
            enabled = false;
            return;
        }
        
        try {
            this.createTable();
        } catch (SQLException ex) {
System.out.println("[Stats] Cannot insert the Table");
enabled = false;
            return;

            
        }
        pl.GetPluginManager().registerEvents(this, pl.GetPlugin());
        System.out.println("Statistics started.");
        enabled = true;
    }
    
    @EventHandler
    public void insertStats(PlayerJoinEvent event){
        if(enabled == false){
            return;
        }
        Player p = event.getPlayer();
String sqlinsert = "INSERT INTO `"+table+"` (Name,UUID,Wins,Played,Deaths,Kills) \n" +
"  SELECT '"+p.getName()+"','"+p.getUniqueId().toString()+"','"+0+"','"+0+"','"+0+"','"+0+"' FROM dual\n" +
"WHERE NOT EXISTS \n" +
"  (SELECT UUID FROM "+table+" WHERE UUID='"+p.getUniqueId().toString()+"');";
    if(this.getStatement() == null){
        enabled = false;
        return;
    }
        try {
            this.getStatement().execute(sqlinsert);
            System.out.println("Record inserted");
        } catch (SQLException ex) {
            System.out.println("Record not inserted"); }
    }
    
/*
ID   /   Name   /   UUID   /   Wins   /   Played   /   Deaths   /   Kills
INT      String     String     INT          INT         INT          INT
*/
private void createTable() throws SQLException {
            if(enabled == false){
            return;
        }
    String sqlCreate =
"CREATE TABLE IF NOT EXISTS `"+table+"` ("
            + "`ID` int(11) NOT NULL auto_increment"
            +",`Name` varchar(255) NOT NULL"
            +",`UUID` varchar(255) NOT NULL"
            +",`Wins` int(11) NOT NULL"
            +",`Played` int(11) NOT NULL"
            +",`Deaths` int(11) NOT NULL"
            +",`Kills` int(11) NOT NULL"
            +",PRIMARY KEY  (`ID`)"
            +
            ")";

   
    this.getStatement().execute(sqlCreate);
    
}

@EventHandler
public void onDie(CombatDeathEvent event){
            if(enabled == false){
            return;
        }
  Player p = (Player) event.GetEvent().getEntity();
  this.addValue(p, "Deaths", 1);
  if(event.GetLog().GetKiller() != null){
      this.addValue(Bukkit.getPlayer(event.GetLog().GetKiller().GetName()), "Kills", 1);
      
  }
}



    public void addValue(Player p, String type, int value){
                if(enabled == false){
            return;
        }
        if(p == null){
            System.out.println("Error trying to record player kill, player is null");
            return;
        }
        if(p.getName() == null){
            System.out.println("Error trying to record player kill, name is null");
        return;
        }




try{
    int val = this.getValue(p, type)+value;
             String pay = "UPDATE "+table+" SET "+type+" ='"+val+"' WHERE UUID = '"+p.getUniqueId()+"'";

     getStatement().execute(pay);                
    
            
}catch(Exception e){
    
}

    }

    java.sql.Connection data = null;
    
    public void checkData(){
                if(enabled == false){
            return;
        }
        if(data == null){
            openData();
        }else try {
            if(data.isClosed()){
                openData();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Stats.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }


    
public final void openData(){
            if(enabled == false){
            return;
        }
    try {
    MySQL MySQL = new MySQL(plugin.GetPlugin(), host, port, db, user, pwd);
        
        data = MySQL.openConnection();
    } catch (SQLException | ClassNotFoundException ex) {
        Logger.getLogger(Stats.class.getName()).log(Level.SEVERE, null, ex);
 
    }

}

java.sql.Statement Statement = null;
public java.sql.Statement getStatement(){
            if(enabled == false){
            return null;
        }
        try {
            if(Statement == null){
                Statement = data.createStatement();
            }else if(Statement.isClosed()){
                Statement = data.createStatement();
            }
            
            
            return Statement;
        } catch (SQLException ex) {
            Logger.getLogger(Stats.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Statement;
}
/*
ID   /   Name   /   UUID   /   Wins   /   Played   /   Deaths   /   Kills
INT      String     String     INT          INT         INT          INT
*/

/**
   * Request MySQL data 
   * Values: "Deaths", "Wins", "Played", "Kills", "UUID", "Name", "ID"
     * @param p
     * @param type
   * @exception SQLException
   * @return Statistics Value
   */ 
public int getValue(Player p, String type) throws SQLException{
            if(enabled == false){
            return 0;
        }
        this.checkData();
        if(p == null){
            System.out.println("Error trying to get player, player is null");
            return 0;
        }
        if(p.getName() == null){
            System.out.println("Error trying to get player, name is null");
            return 0;
        }

    ResultSet res = null;

    
res = getStatement().executeQuery("SELECT * FROM "+table+" WHERE UUID = '"+p.getUniqueId().toString()+"'");
int value = 0;

try{
res.next(); 

value = res.getInt(type);
 
res.close();    
} catch(Exception e){
  e.printStackTrace();
  }

   return value;
    }
    
}
