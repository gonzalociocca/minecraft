package me.peacefulmen.online;

import code.husky.mysql.MySQL;
import java.io.*;
import java.net.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.config.*;
import net.md_5.bungee.event.EventHandler;

public class Premium extends Plugin
    implements Listener
{


    @Override
    public void onEnable()
    {


        if(!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File((new StringBuilder()).append(getDataFolder()).append(".players").toString());
        if(!file.exists())
            file.mkdir();
        File file1 = new File(getDataFolder(), "config.yml");
        if(!file1.exists())
            try
            {
                Files.copy(getResourceAsStream("config.yml"), file1.toPath(), new CopyOption[0]);
            }
            catch(IOException ioexception1)
            {
            }
        
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            Logger.getLogger(Premium.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        getProxy().getPluginManager().registerListener(this, this);
        lobbyPremium = config.getStringList("nologinLobby");
        defaultServer = config.getStringList("defaultServer");
        interLobby = config.getBoolean("interLobby");
        if(config.getStringList("DisabledBungeeCommandsInCrakedLobbys") != null)
            commands = config.getStringList("DisabledBungeeCommandsInCrakedLobbys");
        if(config.getStringList("MessageIsCommandDisabledInCrackedLobby") != null)
            commandsdisabled = config.getStringList("MessageIsCommandDisabledInCrackedLobby");
        if(config.getString("VerifiedPremiumUserFailToLogin") != null)
            messageFail = Colorizer.Color(config.getString("VerifiedPremiumUserFailToLogin"));
        if(config.getBoolean("AntiBot.enabled"))
        {
            resetBot();
            antibotenabled = true;
            MaxTries = config.getInt("AntiBot.Max");
        }
        if(config.getBoolean("JoinDelay.enabled"))
        {
            resetDelay();
            delayjoin = true;
            delayCount = config.getInt("JoinDelay.canJoin");
        }
        this.mySQLEnabled = config.getBoolean("MySQL.enabled");
        this.dbtable = config.getString("MySQL.dbtable");
        this.dbcolumn = config.getString("MySQL.dbcolumn");
        
        if(this.mySQLEnabled == true){
            this.openData();
            try {
                this.createTable();
            } catch (SQLException ex) {
                Logger.getLogger(Premium.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(config.getBoolean("MySQL.ConvertYMLtoMYSQL") == true){
            try {
                File folder = new File((new StringBuilder()).append(getDataFolder()).append(".players").toString());
                File[] listOfFiles = folder.listFiles();
                int triedtoconvert = 0;
                int converted = 0;
                int fails = 0;
                System.out.println("Converting all files in folder Premium.players");
                System.out.println("this can take few minutes...");
                for (File listOfFile : listOfFiles) {
                    try{
                        if (listOfFile.isFile()){
                            triedtoconvert = triedtoconvert+1;
                            this.RegPremium(String.valueOf(listOfFile.getName()).replace(".yml", ""));
                            converted = converted+1;
                        }
                    }catch(Exception e){
                        fails = fails+1;
                    }
                }
                
                System.out.println("Done, sucessfully converted"+converted+" of "+triedtoconvert+" only "+fails+" errors");

            } catch (Exception ex) {
                Logger.getLogger(Premium.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    

    }


    public void resetBot()
    {
   getProxy().getScheduler().schedule(this, new Runnable()
      {
        @Override
        public void run()
        {
antibot.clear();

        }
      }, config.getInt("AntiBot.clearDelay"), config.getInt("AntiBot.clearDelay"), TimeUnit.SECONDS);
    }

    public void resetDelay()
    {
   getProxy().getScheduler().schedule(this, new Runnable()
      {
        @Override
        public void run()
        {
joined = 0;

        }
      }, config.getInt("JoinDelay.everyAmountOfSeconds"), config.getInt("JoinDelay.everyAmountOfSeconds"), TimeUnit.SECONDS);
   
    }
    
private void createTable() throws SQLException {
    String sqlCreate = "CREATE TABLE IF NOT EXISTS `"+dbtable+"` (\n" +
"  `"+dbcolumn+"` varchar(40) NOT NULL\n" +
")";

    Statement stmt = data.createStatement();
    stmt.execute(sqlCreate);
}


    public void RegPremium(String s)
    {
        if(this.mySQLEnabled == true){
if(this.isPremium(s)){
    return;
}
java.sql.Statement statement = null;
              try {
                   statement = data.createStatement();
              } catch (SQLException ex) {
              }
if(statement != null){   
    try {
        statement.executeUpdate("INSERT INTO `"+dbtable+"`("+dbcolumn+") VALUE ('"+s.toLowerCase()+"')");
    } catch (SQLException ex) {
        Logger.getLogger(Premium.class.getName()).log(Level.SEVERE, null, ex);
    }
}
        }else{
        
        File file = new File((new StringBuilder()).append(getDataFolder()).append(".players").toString(), (new StringBuilder()).append(s.toLowerCase()).append(".yml").toString());
        if(!file.exists())
            try
            {
                file.createNewFile();
            }
            catch(IOException ioexception) { }
    }
    
    }
    
public void openData(){
    try {
    MySQL MySQL = new MySQL(Premium.this, config.getString("MySQL.host"), config.getString("MySQL.port"), config.getString("MySQL.dbname"), config.getString("MySQL.dbuser"), config.getString("MySQL.password"));
        data = MySQL.openConnection();
    } catch (SQLException | ClassNotFoundException ex) {
        Logger.getLogger(Premium.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    public boolean isPremium(String s)
    {
        if(this.mySQLEnabled == true){
              try {
                  java.sql.Statement statement = null;
                  try {
                      statement = data.createStatement();
                  } catch (SQLException ex) {
                  }
                  ResultSet res = null;
                  if(statement == null){
                      return false;
                  }
                  res = statement.executeQuery("SELECT * FROM "+dbtable+" WHERE "+dbcolumn+" = '"+s.toLowerCase()+"'");
                  while(res.next()){
                      try {
                          String s2 = res.getString(1);
                          return true;
                      }catch(Exception e){
                          return false;
                      }
                  }
                  return false;
                  
              } catch (SQLException ex) {
                Logger.getLogger(Premium.class.getName()).log(Level.SEVERE, null, ex);
              }

        }else{
            
        
        File file = new File((new StringBuilder()).append(getDataFolder()).append(".players").toString(), (new StringBuilder()).append(s.toLowerCase()).append(".yml").toString());
        return file.exists();
    }
        return false;
    }

public boolean checkPaid(String name){
    boolean isPay = false;
    
    HttpURLConnection con = null;
    URL url;
    Scanner inStream = null;
    try {
        url = new URL("https://minecraft.net/haspaid.jsp?user=" + name);
        con = (HttpURLConnection) url.openConnection();
        
        con.setConnectTimeout(3000);
        con.setReadTimeout(3000);
        

        inStream = new Scanner(con.getInputStream());

        if(inStream.hasNextLine()){
            String next = inStream.nextLine();
            isPay = "true".equalsIgnoreCase(next);
        }
    } catch (MalformedURLException e) {
    } catch (IOException e) {
    } finally{
        if(inStream != null){
            inStream.close();
        }
        if(con != null){
            con.disconnect();
        }
    }
    System.out.println("Verified: "+name+" as "+isPay);
    return isPay;
}
boolean disableBadLoginScreen = true;
    public boolean hasPaid(String s)
    {
        if(disableBadLoginScreen){
            return false;
        }
        if(nopay.contains(s)){
            return false;
        }
        if(paid.contains(s)){
            return true;
        }
        if(checkPaid(s))
        {
            paid.add(s);
            return true;
        } else
        {
            nopay.add(s);
            return false;
        }
    }
    
    
    @EventHandler
    public void onJA5(ServerConnectEvent serverconnectevent) throws IOException
    {
        String s = serverconnectevent.getPlayer().getName();
        ProxiedPlayer p = serverconnectevent.getPlayer();
        try{
        if((interLobby == true) && p.getServer().getInfo().getName() != null){
System.out.println("returning interlobby");
                if(0==1){
 if(serverconnectevent.getTarget().getName().startsWith("lobby")){
 serverconnectevent.setTarget(getProxy().getServerInfo((String)lobbyPremium.get(0)));
       
                }}
                return;
            
           
        }}catch(Exception e){
            
        }
        if(defaultServer.contains(serverconnectevent.getTarget().getName()))
        {
            if( isPremium(s))
                if(lobbyPremium.size() > 1)
                    serverconnectevent.setTarget(getProxy().getServerInfo((String)lobbyPremium.get(r.nextInt(lobbyPremium.size() - 1))));
                else
                    serverconnectevent.setTarget(getProxy().getServerInfo((String)lobbyPremium.get(0)));
        } else
        if(lobbyPremium.contains(serverconnectevent.getTarget().getName()))
            if(isPremium(s))
            {
                if(defaultServer.size() > 1)
                    serverconnectevent.setTarget(getProxy().getServerInfo((String)defaultServer.get(r.nextInt(defaultServer.size() - 1))));
            } else
            {
                serverconnectevent.setTarget(getProxy().getServerInfo((String)defaultServer.get(0)));
            }
    }
    
    @EventHandler
    public void onChat(ChatEvent chatevent)
    {
label0:
        {
            ProxiedPlayer proxiedplayer = (ProxiedPlayer)chatevent.getSender();
            if(!defaultServer.contains(proxiedplayer.getServer().getInfo().getName()) || commands == null || commands.isEmpty() || !chatevent.isCommand())
                break label0;
            Iterator iterator = commands.iterator();
            String s;
            do
            {
                if(!iterator.hasNext())
                    break label0;
                s = (String)iterator.next();
            } while(!chatevent.getMessage().toLowerCase().startsWith(s.toLowerCase()));
            if(commandsdisabled != null)
            {
                String s1;
                for(Iterator iterator1 = commandsdisabled.iterator(); iterator1.hasNext(); proxiedplayer.sendMessage(Colorizer.Color(s1)))
                    s1 = (String)iterator1.next();

            }
            chatevent.setCancelled(true);
            return;
        }
    }

    
    @EventHandler
    public void onJA2(PreLoginEvent preloginevent) throws IOException
    {
        String s = preloginevent.getConnection().getName();
        String s1 = preloginevent.getConnection().getName().replaceAll("_", "").replace("_", "");
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        if(pattern.matcher(s1).find())
        {
            preloginevent.setCancelReason("&c&lInvalid Characters in name");
            preloginevent.setCancelled(true);
        }
        String s2 = preloginevent.getConnection().getAddress().getAddress().getCanonicalHostName();
        if(delayjoin)
        {
            joined = joined + 1;
            if(joined > delayCount)
            {
                preloginevent.setCancelReason("&c&lWait to reconnect, too many people is trying to join!");
                preloginevent.setCancelled(true);
            }
        }
        if(antibotenabled)
            if(antibot.containsKey(s2))
            {
                int i = ((Integer)antibot.get(s2));
                if(i > MaxTries)
                {
                    preloginevent.setCancelReason("&c&lThe system has detected your IP as a bot, wait to reconnect!");
                    preloginevent.setCancelled(true);
                }
                antibot.put(s2, i + 1);
            } else
            {
                antibot.put(s2, 1);
            }
        
        
   if(isPremium(s)){
        System.out.println("setted to online mode, and returning");
        if(namesock.containsKey(s)){
            namesock.remove(s);
        }
        if(prev.containsKey(s2)){
            prev.remove(s2)
;        }
            preloginevent.getConnection().setOnlineMode(true);
            if(messageFail != null){
                cd2.put(s, s2);                
            }

            return;
        }else{
        System.out.println("not setted to online mode");
   }
                
        if(messageFail != null && !cd2.isEmpty() && cd2.containsKey(s) && ((String)cd2.get(s)).equalsIgnoreCase(s2))
        {
            cd2.remove(s);
            preloginevent.setCancelReason(messageFail);
            preloginevent.setCancelled(true);
            return;
        }
        preloginevent.getConnection().setOnlineMode(true);
        if(!hasPaid(s))
        {
            preloginevent.getConnection().setOnlineMode(false);
            return;
        }
        if(namesock.containsKey(s) && ((String)namesock.get(s)).equalsIgnoreCase(s2))
        {
            preloginevent.getConnection().setOnlineMode(false);
            return;
        }
        if(prev.containsKey(s2) && ((String)prev.get(s2)).equalsIgnoreCase(s))
        {
            namesock.put(s, s2);
            preloginevent.getConnection().setOnlineMode(false);
        }
        prev.put(s2, preloginevent.getConnection().getName());
        

    }

    @EventHandler
    public void onJA3(LoginEvent loginevent) throws IOException
    {
        String s = loginevent.getConnection().getName();

        String s1 = loginevent.getConnection().getAddress().getAddress().getCanonicalHostName();
        if(messageFail != null && cd2.containsKey(s)){
            cd2.remove(s);            
        }



        if(namesock.containsKey(s) && ((String)namesock.get(s)).equalsIgnoreCase(s1))
            return;
        if(!hasPaid(s))
            return;
        if(loginevent.getConnection().isOnlineMode())
        {
            RegPremium(s);
            namesock.remove(s);
        }
    }
    
       Random r = new Random();
       Configuration config = null;
       boolean interLobby = true;
      String  messageFail = null;
       List<String> commands = new ArrayList();
       List<String> commandsdisabled = new ArrayList();
       HashMap<String,Integer> antibot = new HashMap();

       boolean antibotenabled = false;
       boolean delayjoin = false;
       int MaxTries = 30;
       int delayCount = 30;
       HashMap<String,String> namesock = new HashMap();
      HashMap<String,String>  prev = new HashMap();
      HashSet<String>  nopay = new HashSet();
      HashSet<String>  paid = new HashSet();
      HashMap  cd2 = new HashMap();
      int joined = 0;
    private List<String> lobbyPremium = new ArrayList();
    private List<String> defaultServer = new ArrayList();
    String dbtable = null;
    String dbcolumn = null;
    java.sql.Connection data = null;
    private boolean mySQLEnabled = false;
}