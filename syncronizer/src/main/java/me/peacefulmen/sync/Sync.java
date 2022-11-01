package me.peacefulmen.sync;

import io.netty.channel.ChannelFutureListener;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.query.RemoteQuery;

public class Sync extends Plugin implements Listener{

Configuration config = null;
private java.sql.Connection data = null;
private String serverid = "";
private int total = 0;
String host = "0.0.0.0";
String port = null;
String db = null;
String user = null;
String pwd = null;
String table = null;
String count = null;
String name = null;
List<String> svs = new ArrayList();

    @Override
    public void onEnable() {
        
    if(!this.getDataFolder().exists()){
        this.getDataFolder().mkdir();
    }

      File file = new File(getDataFolder(), "config.yml");
      if(!file.exists()){
        try {
            Files.copy(getResourceAsStream("config.yml"), file.toPath(), new CopyOption[0]);
        } catch (IOException ex) {
            Logger.getLogger(Sync.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    try {
        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));

    } catch (IOException ex) {
        Logger.getLogger(Sync.class.getName()).log(Level.SEVERE, null, ex);
    }
    host = config.getString("MySQL.host");
    port = config.getString("MySQL.port");
    db = config.getString("MySQL.db");
    user = config.getString("MySQL.user");
    pwd = config.getString("MySQL.pwd");
    table = config.getString("MySQL.table");
    count = config.getString("MySQL.column");
    name = config.getString("MySQL.column2");
getProxy().getPluginManager().registerListener(this, this);

this.serverid = "server"+config.getInt("serverid");

for(int a = 0;a < config.getInt("bungeeamount");a++){
   svs.add("server"+a);
}
openData();
UpdateTick();

    try {
        this.createTable();
    } catch (SQLException ex) {
        Logger.getLogger(Sync.class.getName()).log(Level.SEVERE, null, ex);
    }

                 }




public void UpdateTick(){
   getProxy().getScheduler().schedule(this, new Runnable()
      {
        @Override
        public void run()
        {
            try {
     int bet = 0;
    for(Iterator<String> ese = svs.iterator();ese.hasNext();){
        String nxsv = ese.next();

        int nxint;
        if((nxint = getServerCount(nxsv)) != -1){
bet+=nxint;            
        }
    }
total = bet;
            UpdateMyCount();
            } catch (SQLException ex) {
                Logger.getLogger(Sync.class.getName()).log(Level.SEVERE, null, ex);
            }
   
             if(data == null){
                openData();
            }
            
        }
      }, 5L, 5L, TimeUnit.SECONDS);
}

public void openData(){
    try {
    MySQL MySQL = new MySQL(Sync.this, host, port, db, user, pwd);
        
        data = MySQL.openConnection();
    } catch (SQLException | ClassNotFoundException ex) {
        Logger.getLogger(Sync.class.getName()).log(Level.SEVERE, null, ex);
    }
}


  public void UpdateMyCount(){
java.sql.Statement statement = null;
              try {
                   statement = data.createStatement();
              } catch (SQLException ex) {
              }

try{
             String pay = "UPDATE "+table+" SET "+count+" ='"+getProxy().getOnlineCount()+"' WHERE "+name+" = '"+serverid+"'";
            if(statement != null){
            statement.execute(pay);                
     statement.closeOnCompletion();
            }
}catch(Exception e){
    
}

  }
  
  
  public int getServerCount(String servername) throws SQLException{
int count2 = -1;
java.sql.Statement statement = null;
              try {
                   statement = data.createStatement();
              } catch (SQLException ex) {
              }
    ResultSet res = null;
    if(statement == null){
        return -1;
    }
    
res = statement.executeQuery("SELECT * FROM "+table+" WHERE "+name+" = '"+servername+"'");
try{
res.next();

 count2 = res.getInt(count);
 
res.close();    
} catch(Exception e){
  e.printStackTrace();
          this.RegServer(servername);
  }

   return count2;

  }
    public void RegServer(String s)
    {


java.sql.Statement statement = null;
              try {
                   statement = data.createStatement();
              } catch (SQLException ex) {
              }
if(statement != null){
    try {
        statement.executeUpdate("INSERT INTO `"+table+"`("+name+","+count+") VALUE ('"+s.toLowerCase()+"','0')");
    } catch (SQLException ex) {
        Logger.getLogger(Sync.class.getName()).log(Level.SEVERE, null, ex);
    }

        }
        
       
    
    }
@EventHandler(priority = EventPriority.HIGHEST)
public void changePing(ProxyPingEvent event){
    ServerPing old = event.getResponse();
    old.setDescription(Colorizer.Color("&a      MineUltra Network &7| &4&lFull 1.9!                      &b\u2744 &cHot Sale! &e\u27A4 &6&lMINEULTRA.COM &b\u2744"));
    Players oldp = old.getPlayers();
    oldp.setOnline(total);
    old.setPlayers(oldp);

    event.setResponse(old);
}
private void createTable() throws SQLException {
    String sqlCreate =
"CREATE TABLE IF NOT EXISTS `"+table+"` (`"+name+"` varchar(40) NOT NULL\n" +
            ",`"+count+"` int(10) NOT NULL\n" +

            ")";

    Statement stmt = data.createStatement();
    stmt.execute(sqlCreate);
}



}
  

   
    
    
    
