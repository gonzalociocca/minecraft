package me.gonzalociocca.mineultra;

import com.sun.xml.internal.ws.api.message.Attachment;
import me.gonzalociocca.mysql.MySQL;
import net.minecraft.server.v1_9_R1.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MineUltra
  extends JavaPlugin
  implements Listener

{

  MineUltra plugin = null;

    public void onBroadcast(){
        for(BroadcastData bd : this.broadcasts){
            bd.sendToAll(this);
        }
    }
    HashMap<String,PermissionAttachment> attachments = new HashMap();

  @Override
  public void onEnable()
  {
      plugin = this;
      PluginManager pm = getServer().getPluginManager();
      pm.registerEvents(this, this);
      this.init();

      BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

      scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
          @Override
          public void run() {
              onBroadcast();
              updateServer();
          }
      }, 20L, 60L);

      try {
          servername = this.getServerName();
      } catch (IOException e) {
         servername = null;
      }
      if(servername != null){
          plugin.insertServer(servername);
          this.loadBroadcasts();
      }
}
  


  @Override
  public void onDisable()
  {
      if(servername != null){
      finish();}
  }


    String host = "192.99.14.217";
    String port = "3306";
    String db = "vgcore";
    String user = "root";
    String pwd = "trabek123-";
    String table = "data";
    
    boolean enabled = true;
    java.sql.Connection data = null;


@EventHandler(priority=EventPriority.HIGHEST)
public void beforeJoin(PlayerLoginEvent event){
this.insert(event.getPlayer().getName(),event.getPlayer().getUniqueId().toString());
}

    @EventHandler
    public void onServer(ServerCommandEvent event){

        if(event.getCommand().startsWith("mu addrank")){
            String[] parts = event.getCommand().split(" ");
            String rank = parts[2];
            String name = parts[3];
            this.insert(name,"");
            Integer days = Integer.parseInt(parts[4]);
            PlayerData pd = this.getPlayerData(name);
            pd.addRank(rank, days);
            event.getSender().sendMessage(Colorizer.Color("&eRank &7"+rank+" &eadded to &7"+name+" &efor &7"+days+"days"));
            event.setCancelled(true);
        }
    }
    //CMD  0      1     2      3
    //mu addrank vip HappyBear 3

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if(this.getServerName().equalsIgnoreCase("factions") || this.getServerName().equalsIgnoreCase("opprison") || this.getServerName().equalsIgnoreCase("skyblock")){
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cmd.getName().equalsIgnoreCase("mu")){
if(args.length <= 0){
    sender.sendMessage(Colorizer.Color("&b&m-----&cComandos&b&m-----"));
    sender.sendMessage(Colorizer.Color("&a/mu ban <jugador> <razon> &b(HELPER+)"));
    sender.sendMessage(Colorizer.Color("&a/mu unban <jugador> &b(MG+)"));
    sender.sendMessage(Colorizer.Color("&a/mu kick <jugador> <mensaje> &b(HELPER+)"));
    sender.sendMessage(Colorizer.Color("&a/mu vanish &b(MG+)"));
    sender.sendMessage(Colorizer.Color("&a/mu tp <jugador> &b(MG+)"));
        return true;
}
            if(args[0].equalsIgnoreCase("topkills")){
                try {
                    int pos = 1;
sender.sendMessage(Colorizer.Color("&b&m[---------&b&nTopKills&b&m---------]"));
                    for(String s : this.getTopKills().keySet()){
                        sender.sendMessage(Colorizer.Color("&b"+pos+". &a"+s+": &c"+this.getTopKills().get(s)+"kills"));
                    pos++;
                    }
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else if(args[0].equalsIgnoreCase("topwins")){
                try {
                    int pos = 1;
                    sender.sendMessage(Colorizer.Color("&b&m[---------&b&nTop Victorias&b&m---------]"));
                    for(String s : this.getTopWins().keySet()){
                        sender.sendMessage(Colorizer.Color("&b"+pos+". &a"+s+": &c"+this.getTopWins().get(s)+"victorias"));
                        pos++;
                    }
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(args[0].equalsIgnoreCase("addrank") && sender.hasPermission("mu.addrank") || args[0].equalsIgnoreCase("addrank") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.OWNER)) {
                // /mu addrank <rank> <player> <days>
                if (args.length == 4) {
                    String rank = args[1];
                    String name = args[2];
                    this.insert(name, "");
                    Integer days = Integer.parseInt(args[3]);
                    PlayerData pd = this.getPlayerData(name);
                    pd.addRank(rank, days);
                    sender.sendMessage(Colorizer.Color("&eRank &7" + rank + " &eadded to &7" + name + " &efor &7" + days + "days"));
                } else {
                    sender.sendMessage(Colorizer.Color("&aUso: /mu addrank <rango> <jugador> <dias>"));
                }
            }else if(args[0].equalsIgnoreCase("addcoins") && sender.hasPermission("mu.addcoins")|| args[0].equalsIgnoreCase("addcoins") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.OWNER)) {
                // /mu addcoins <amount> <player>
                if (args.length == 3) {
                    int amount = Integer.parseInt(args[1]);
                    String name = args[2];
                    this.insert(name, "");
                    PlayerData pd = this.getPlayerData(name);
                    pd.addCoins(amount);
                    sender.sendMessage(Colorizer.Color("&e"+amount + " &ecoins added to &7" + name));
                } else {
                    sender.sendMessage(Colorizer.Color("&aUso: /mu addcoins <cantidad> <jugador>"));
                }
            }
           else if(args[0].equalsIgnoreCase("ban") && sender.hasPermission("mu.ban")|| args[0].equalsIgnoreCase("ban") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.Helper)){
                // /mu ban <name> <reason>
            if(args.length == 3) {
                String name = args[1];
                String reason = args[2];
                banType type = banType.getByName(reason);
if(sender instanceof Player){
    if(type == banType.Hacks || type==banType.Macros){
        PlayerData pd = this.getPlayerData(sender.getName());
        if(!pd.getRank().isAtLeast(Rank.GM)){
            sender.sendMessage(Colorizer.Color("&cSolo pueden banear por macros &dLos que son [MG] en adelante"));
        return true;}
    }
}
                PlayerData pd = this.getPlayerData(name);
                if (pd==null){
                    sender.sendMessage(Colorizer.Color("&cEse jugador no existe."));
                    return true;
                }
                if(pd.getRank().isAtLeast(Rank.Helper)){
                    sender.sendMessage(Colorizer.Color("&aNo puedes banear a otros staffs!"));
                    sender.sendMessage(Colorizer.Color("&cCuida tu comportamiento o seras baneado."));
                    return true;
                }
                pd.ban(type, sender.getName());
                sender.sendMessage(Colorizer.Color("&c" + name + " &ebaneado " + type.getDays() + "dias por &7" + type.getReason() + " &eOrigen: " + sender.getName()));
            }else{
                sender.sendMessage(Colorizer.Color("&aUso: /mu ban <jugador> <razon>"));
            sender.sendMessage(Colorizer.Color("&aRazones: "+banType.getAllNames()));
            }
           }
           else if(args[0].equalsIgnoreCase("unban") && sender.hasPermission("mu.unban")|| args[0].equalsIgnoreCase("unban") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.GM)){
               // /mu unban <name>
               if(args.length==2) {
                   String name = args[1];

                   PlayerData pd = this.getPlayerData(name);
                   if (pd==null){
                       sender.sendMessage(Colorizer.Color("&cEse jugador no existe."));
                       return true;
                   }
                   pd.unban();
                   sender.sendMessage(Colorizer.Color("&c" + name + " &edesbaneado por &7" + sender.getName()));
               }else{
                   sender.sendMessage(Colorizer.Color("&aUso: /mu ban <jugador> <razon>"));
                   sender.sendMessage(Colorizer.Color("&aRazones: "+banType.getAllNames()));}
           }
           else if(args[0].equalsIgnoreCase("kick") && sender.hasPermission("mu.kick")|| args[0].equalsIgnoreCase("kick") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.Helper)){
               // /mu kick <name> <mensaje>
               if(args.length==3) {
                   String name = args[1];
                   String mensaje = args[2];

                   if(Bukkit.getPlayer(name) != null){
                       Bukkit.getPlayer(name).kickPlayer(Colorizer.Color(mensaje+" &cKickeado por "+sender.getName()));
                       Bukkit.broadcastMessage(Colorizer.Color("&a"+name+" &cKickeado por "+sender.getName()));}
                   else{
                       sender.sendMessage(Colorizer.Color("&cJugador no encontrado."));
                   }
               }else{
                   sender.sendMessage(Colorizer.Color("&aUso: /mu kick <jugador> <mensaje>"));}
           }
           else if(args[0].equalsIgnoreCase("vanish") && sender.hasPermission("mu.vanish") || args[0].equalsIgnoreCase("vanish") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.GM)) {
               // /mu vanish
               if(sender instanceof Player){
                   Player p = (Player)sender;
                   if(p.getGameMode()==GameMode.SPECTATOR){
                       p.setGameMode(GameMode.SURVIVAL);
                   }else{
                       p.setGameMode(GameMode.SPECTATOR);}
               }else{
                   sender.sendMessage(Colorizer.Color("&cEste comando solo se puede usar siendo un jugador"));
               }
           }
           else if(args[0].equalsIgnoreCase("tp") && sender.hasPermission("mu.tp")|| args[0].equalsIgnoreCase("tp") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.GM)) {
               // /mu tp <jugador>
               if(sender instanceof Player && args.length == 2){
                   String to = args[1];
                   if(Bukkit.getPlayer(to) == null){
                       sender.sendMessage(Colorizer.Color("&CJugador no encontrado"));
                   }else{
                       Player p = (Player)sender;
                       p.sendMessage(Colorizer.Color("&aTeletransportando"));
                       p.teleport(Bukkit.getPlayer(to));
                   }
               }else if(args.length==2){
                   sender.sendMessage(Colorizer.Color("&cEste comando solo se puede usar siendo un jugador"));
               }else{
                   sender.sendMessage(Colorizer.Color("&aUso: /mu tp <jugador>"));
               }
           }
        else{
               sender.sendMessage(Colorizer.Color("&b&m-----&cComandos&b&m-----"));
               sender.sendMessage(Colorizer.Color("&a/mu ban <jugador> <razon> &b(HELPER+)"));
               sender.sendMessage(Colorizer.Color("&a/mu unban <jugador> &b(MG+)"));
               sender.sendMessage(Colorizer.Color("&a/mu kick <jugador> <mensaje> &b(HELPER+)"));
               sender.sendMessage(Colorizer.Color("&a/mu vanish &b(MG+)"));
               sender.sendMessage(Colorizer.Color("&a/mu tp <jugador> &b(MG+)"));

               return true;}
            return true;
        }
        return false;
    }
    
    
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
       }
        
    }

public void insertServer(String svname){
String sqlinsert = "INSERT INTO `ServerData` ("
        + "servername"+","
        + "broadcasts"+","
        + "online"+","
        + "maxonline"+","
        + "playerlist"+","
        + "status"+","
        + "enabled"+","
        + "type"
        +") " +
"  SELECT '"+svname.toLowerCase()+"',"
        + "'',"
        + "'0',"
        + "'0',"
        + "'',"
        + "'',"
        + "'true',"
        + "'Unknown'"
        + " FROM dual " +
"WHERE NOT EXISTS " +
"( SELECT servername FROM ServerData WHERE servername='"+svname.toLowerCase()+"' );";
    
        try {
            this.getStatement().execute(sqlinsert);
            System.out.println("Record inserted -servername");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Record not inserted -servername");
        }
}

private void insert(String name, String uuid){
String sqlinsert = "INSERT INTO `"+table+"` ("
        + columnType.Name.getName()+","
        + columnType.UUID.getName()+","
        + columnType.Kills.getName()+","
        + columnType.Deaths.getName()+","
        + columnType.Loss.getName()+","
        + columnType.Winned.getName()+","
        + columnType.FirstPlace.getName()+","
        + columnType.SecondPlace.getName()+","
        + columnType.ThirdPlace.getName()+","
        + columnType.LastMessages.getName()+","
        + columnType.LastReports.getName()+","
        + columnType.LastTransactions.getName()+","
        + columnType.Pets.getName()+","
        + columnType.Ranks.getName()+","
        + columnType.Coins.getName()+","
        + columnType.Friends.getName()+","
        + columnType.Bans.getName()
        +") " +
"  SELECT '"+name.toLowerCase()+"',"
        + "'"+uuid+"',"
        + "'"+columnType.Kills.getDefault()+"',"
        + "'"+columnType.Deaths.getDefault()+"',"
        + "'"+columnType.Loss.getDefault()+"',"
        + "'"+columnType.Winned.getDefault()+"',"
        + "'"+columnType.FirstPlace.getDefault()+"',"
        + "'"+columnType.SecondPlace.getDefault()+"',"
        + "'"+columnType.ThirdPlace.getDefault()+"',"
        + "'"+columnType.LastMessages.getDefault()+"',"
        + "'"+columnType.LastReports.getDefault()+"',"
        + "'"+columnType.LastTransactions.getDefault()+"',"
        + "'"+columnType.Pets.getDefault()+"',"
        + "'"+columnType.Ranks.getDefault()+"',"
        + "'"+columnType.Coins.getDefault()+"',"
        + "'"+columnType.Friends.getDefault()+"',"
        + "'"+columnType.Bans.getDefault()+"'"
        + " FROM dual " +
"WHERE NOT EXISTS " +
"( SELECT Name FROM "+table+" WHERE Name='"+name.toLowerCase()+"' );";

        try {
            this.getStatement().execute(sqlinsert);
            System.out.println("Record inserted");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Record not inserted");
        }
}

public void removePlayerData(String name){
    if(this.playerdata.containsKey(name.toLowerCase())){
    this.playerdata.remove(name.toLowerCase());}
}
 HashMap<String,PlayerData> playerdata = new HashMap();

public PlayerData getPlayerData(String name){
    if(playerdata.containsKey(name.toLowerCase())){
        return playerdata.get(name.toLowerCase());
    }else{
       PlayerData pd = new PlayerData(name.toLowerCase(),this);
       if(pd != null){
           playerdata.put(name.toLowerCase(), pd);
           return pd;
       }
    }
    return null;
}

    public void setServerStatus(String str){ status = str;    }

HashSet<BroadcastData> broadcasts = new HashSet();

    public void loadBroadcasts(){

        ResultSet res = null;
        try {
            res = getStatement().executeQuery("SELECT * FROM ServerData WHERE servername = '"+servername+"'");
            while(res.next()){
                String bs = res.getString("broadcasts");
                if(bs.contains(":")){
                    for(String str : bs.split(":")){
                        String[] data = str.split(",");
                        broadcasts.add(new BroadcastData(data[0],Long.parseLong(data[1]),Long.parseLong(data[2]),Long.parseLong(data[3])));
                    }
                }else{
                    String[] data = bs.split(",");
                    broadcasts.add(new BroadcastData(data[0],Long.parseLong(data[1]),Long.parseLong(data[2]),Long.parseLong(data[3])));
                }
                  }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashSet<ServerData> getServersEnabled(){
        HashSet<ServerData> servers = new HashSet();

        ResultSet res = null;
        try {
            res = getStatement().executeQuery("SELECT * FROM ServerData WHERE enabled = 'true'");
            while(res.next()){
                String servername = res.getString("servername");
                int online = Integer.parseInt(res.getString("online"));
                int maxonline = Integer.parseInt(res.getString("maxonline"));
                String playerlist = res.getString("playerlist");
                String status = res.getString("status");
                String broadcasts = res.getString("broadcasts");
                String type = res.getString("type");

                boolean enabled = true;
                servers.add(new ServerData(online,maxonline,playerlist,status,enabled,servername,broadcasts,serverType.getbyName(type)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return servers;
    }

    public void finish(){
        try{

            String pay = "UPDATE ServerData SET " +
                    "online = '"+0+"'," +
                    "maxonline = '"+0+"'," +
                    "playerlist = ''," +
                    "status = 'Reiniciando'," +
                    "enabled = '"+ "true" +"'" +
                    " WHERE servername = '"+servername+"'";
            plugin.getStatement().execute(pay);
        }catch(Exception e){
            System.out.println("Failed to update ServerData to "+servername +"looks like a mysql issue!");
            e.printStackTrace();
        }
    }

int maxonline = Bukkit.getMaxPlayers();
    public void updateServer(){
        if(servername == null){
            return;
        }
        try{

            String pay = "UPDATE ServerData SET " +
                    "online = '"+ Bukkit.getOnlinePlayers().size()+"'," +
                    "maxonline = '"+ maxonline+"'," +
                    "playerlist = '"+ Bukkit.getOnlinePlayers().toString().replace("]","").replace("}","").replace("[","").replace("CraftPlayer{name=","")+"'," +
                    "status = '"+ status +"'," +
                    "enabled = '"+ "true" +"'" +
                    " WHERE servername = '"+this.getServerName()+"'";
            plugin.getStatement().execute(pay);
        }catch(Exception e){
            System.out.println("Failed to update ServerData to "+servername +"looks like a mysql issue!");
            e.printStackTrace();
        }}

    String status = "Unknown";
    String servername = null;

    public String getServerName() throws IOException {
        if(servername != null){
            return servername;
        }
        String content;
        BufferedReader br = new BufferedReader(new FileReader("server.dat"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            content = sb.toString();
        } finally {
            br.close();
        }
        servername = content.replaceAll("[^A-Za-z0-9]", "");
        return servername;
    }

private void createTable() throws SQLException {
            if(enabled == false){
            return;
        }
          
    String sqlCreate =
"CREATE TABLE IF NOT EXISTS `"+table+"` ("
            + "`ID` int(11) NOT NULL auto_increment"
            +",`"+columnType.Name.getName()+"` varchar(255) NOT NULL"
            +",`"+columnType.UUID.getName()+"` varchar(255) NOT NULL"
            +",`"+columnType.Kills.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Deaths.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Loss.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Winned.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.FirstPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.SecondPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.ThirdPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.LastMessages.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.LastReports.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.LastTransactions.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Pets.getName()+"` text(4999) NOT NULL"
        +",`"+columnType.Ranks.getName()+"` text(4999) NOT NULL"
        +",`"+columnType.Coins.getName()+"` text(4999) NOT NULL"
        +",`"+columnType.Friends.getName()+"` text(4999) NOT NULL"
        +",`"+columnType.Bans.getName()+"` text(4999) NOT NULL"
            +",PRIMARY KEY  (`ID`)"
            +
            ")";


    this.getStatement().execute(sqlCreate);
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
         }
        return Statement;
}


public final void openData(){
        if(enabled == false){
        return;
        }
        MySQL MySQL = new MySQL(this, host, port, db, user, pwd);
      try {
          data = MySQL.openConnection();
      } catch (SQLException | ClassNotFoundException ex) {
     
      }
}

     public void init(){
        System.out.println("[MineUltra] Initializing");
        try{
        this.openData();    
        }catch(Exception e){
            System.out.println("[MineUltra] Disabled");
            enabled = false;
            return;
        }
        
      try {
          this.createTable();
      } catch (SQLException ex) {
          Logger.getLogger(MineUltra.class.getName()).log(Level.SEVERE, null, ex);
      }
        System.out.println("MineUltra Initialized.");
        enabled = true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void LoadPlayer(PlayerJoinEvent event){
        //pre load player data
        if(getPlayerData(event.getPlayer().getName()).isBanned()){
            event.getPlayer().kickPlayer(Colorizer.Color(getPlayerData(event.getPlayer().getName()).getBan().getKickMessage()));
        }
        if(getPlayerData(event.getPlayer().getName()).getRank().isAtLeast(Rank.VIP)){
            PlayerData pe = getPlayerData(event.getPlayer().getName());
            if(pe.getRank().getDaysLeft() < 16 && pe.getRank().getDaysLeft() >= 0){
                event.getPlayer().sendMessage(Colorizer.Color("&c&l[MineUltra] &eLe quedan "+pe.getRank().getDaysLeft()+" a tu rango "+pe.getRank().getChatPrefix()));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void UnloadPlayer(PlayerQuitEvent event){
        if(playerdata.containsKey(event.getPlayer().getName().toLowerCase())){
            playerdata.remove(event.getPlayer().getName().toLowerCase());
        }
    }


    LinkedHashMap<String,Integer> topkills = null;
    public LinkedHashMap<String,Integer> getTopKills() throws SQLException {
        if(topkills != null){
            return topkills;
        }
        else{
            ResultSet res = null;
            res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" ORDER BY Kills DESC LIMIT 10");
            topkills = new LinkedHashMap();
            while(res.next()){
                topkills.put(res.getString("Name"),res.getInt("Kills"));}
        }

        topkills = (LinkedHashMap)this.sortByValue((Map)topkills);
        return topkills;
    }

    LinkedHashMap<String,Integer> topwins = null;

    public LinkedHashMap<String,Integer> getTopWins() throws SQLException {
        if(topwins != null){
            return topwins;
        }
        else{
            ResultSet res = null;
            res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" ORDER BY Winned DESC LIMIT 10");
            topwins = new LinkedHashMap();

            while(res.next()){
                topwins.put(res.getString("Name"),res.getInt("Winned"));}
        }
        topwins = (LinkedHashMap)this.sortByValue((Map)topwins);
        return topwins;
    }

    LinkedHashMap<String,Integer> toparchers = null;

    public LinkedHashMap<String,Integer> getTopArchers() throws SQLException {
        if(toparchers != null){
            return toparchers;
        }
        else{
            toparchers = new LinkedHashMap();
        }
        return toparchers;
    }
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}
