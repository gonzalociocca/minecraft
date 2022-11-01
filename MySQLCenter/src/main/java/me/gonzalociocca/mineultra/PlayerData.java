package me.gonzalociocca.mineultra;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import sun.net.ftp.FtpDirEntry;

/**
 *
 * @author cuack
 */


public class PlayerData {
    
    private MineUltra plugin = null;
    
    private String name = null;
    
    private int Kills = 0;
    private int Deaths = 0;
    private int Loss = 0;
    private int Winned = 0;
    private int firstPlace = 0;
    private int secondPlace = 0;
    private int thirdPlace = 0;
    private int Coins = 0;
    private String lastMessages = null;
    private String lastReports = null;
    private String pets = null;
    private List<Rank> ranks = new ArrayList();
    private List<banType> bans = new ArrayList();
    private List<Transaction> transactions = new ArrayList();
    
    public PlayerData(String gname, MineUltra aThis) {
       plugin = aThis;
       name = gname.toLowerCase();

        try {
            this.loadData();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerData.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public void loadData() throws SQLException{
        plugin.checkData();
ResultSet res = null;
res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" WHERE Name = '"+name+"'");
try{res.next(); 
this.Kills = res.getInt(columnType.Kills.getName());
this.Deaths = res.getInt(columnType.Deaths.getName());
this.Loss = res.getInt(columnType.Loss.getName());
this.Winned = res.getInt(columnType.Winned.getName());
this.firstPlace = res.getInt(columnType.FirstPlace.getName());
this.secondPlace = res.getInt(columnType.SecondPlace.getName());
this.thirdPlace = res.getInt(columnType.ThirdPlace.getName());
    this.Coins = res.getInt(columnType.Coins.getName());
    parseTransactions(res.getString(columnType.LastTransactions.getName()));
    parseRanks(res.getString(columnType.Ranks.getName()));
    parseBans(res.getString(columnType.Bans.getName()));
//parsePets(res.getString(columnType.Pets.getName()));

res.close();} catch(Exception e){
  e.printStackTrace();}

    }


    
public void addRank(String rank, int days){
try{
ResultSet res;
res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" WHERE Name = '"+name+"'");
res.next();
String rks = res.getString(columnType.Ranks.getName());
if(!rks.isEmpty()){
rks = rks+";"+rank+":"+System.currentTimeMillis()+","+(System.currentTimeMillis()+(86400000L*days));   
}else{
rks = rank+":"+System.currentTimeMillis()+","+(System.currentTimeMillis()+(86400000L*days));  
}

//reload ranks for player
this.ranks.clear();
this.parseRanks(rks);

//update ranks
String pay = "UPDATE "+plugin.table+" SET "+columnType.Ranks.getName()+" ='"+rks+"' WHERE Name = '"+name+"'";
plugin.getStatement().execute(pay);  
this.addTransaction(rank, days);
}catch(Exception e){
    System.out.println("Failed to addrank to "+name +"looks like a mysql issue!");

}}

    public void ban(banType type, String sender){
        try{
            ResultSet res;
            res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" WHERE Name = '"+name+"'");
            res.next();
            String rks = res.getString(columnType.Bans.getName());
            if(!rks.isEmpty()){
                rks = rks+";"+type.getName()+":"+System.currentTimeMillis()+","+(System.currentTimeMillis()+(86400000L*type.getDays()))+","+sender;
            }else{
                rks = type.getName()+":"+System.currentTimeMillis()+","+(System.currentTimeMillis()+(86400000L*type.getDays()))+","+sender;
            }

//reload ranks for player
            this.bans.clear();
            this.parseBans(rks);

//update ranks
            String pay = "UPDATE "+plugin.table+" SET "+columnType.Bans.getName()+" ='"+rks+"' WHERE Name = '"+name+"'";
            plugin.getStatement().execute(pay);
        }catch(Exception e){
            System.out.println("Failed to ban "+name +"looks like a mysql issue!");

        }}

    public void unban(){
        try{
            String pay = "UPDATE "+plugin.table+" SET "+columnType.Bans.getName()+" ='' WHERE Name = '"+name+"'";
            plugin.getStatement().execute(pay);
            this.bans.clear();
        }catch(Exception e){
            System.out.println("Failed to ban "+name +"looks like a mysql issue!");

        }}
    public boolean isBanned(){
        if(bans.isEmpty()){
            return false;
        }
        return true;
    }
    public banType getBan(){
        if(bans.isEmpty()){
            return banType.Unknown;
        }
        if(this.bans.size() > 1){
            banType high = banType.Unknown;
            for(banType ban : bans){
            if(ban.getDays() > high.getDays()){
                high = ban;
            }
            }
            return high;
        }else{
            return bans.get(0);
        }
    }

    public boolean hasTransaction(String itemname){
        for(Transaction ts : this.transactions){
if(ts.getItem().equalsIgnoreCase(itemname) && !ts.isExpired()){
    return true;
}}
        return false;
    }

    public List<Transaction> getTransactions(){
        return this.transactions;
    }
public void addTransaction(String itemname, int days){
try{
ResultSet res;
res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" WHERE Name = '"+name+"'");
res.next();
String rks = res.getString(columnType.LastTransactions.getName());
if(!rks.isEmpty()){
rks = rks+";"+itemname+":"+System.currentTimeMillis()+","+(System.currentTimeMillis()+(86400000L*days));   
}else{
rks = itemname+":"+System.currentTimeMillis()+","+(System.currentTimeMillis()+(86400000L*days));  
}

//reload transactions for player
this.transactions.clear();
this.parseTransactions(rks);

//update transactions
String pay = "UPDATE "+plugin.table+" SET "+columnType.LastTransactions.getName()+" ='"+rks+"' WHERE Name = '"+name+"'";
plugin.getStatement().execute(pay);  
}catch(Exception e){
    System.out.println("Failed to addtransaction to "+name +"looks like a mysql issue!");
    e.printStackTrace();
}}

//Transaction format:
// <item>:datestart,datestop;item:datestart,datestop
private void parseTransactions(String str){
   if(str==null || str.isEmpty()){
       return;
   }
   
  if(str.contains(";")){
      String[] s = str.split(";");
      for(String s2 : s){
       String[] s3 = s2.split(":");
       
       String itemname = s3[0];
       String[] date = s3[1].split(",");
       Long startTime = Long.parseLong(date[0]);
       Long stopTime = Long.parseLong(date[1]);
       boolean expired = true;
       if(System.currentTimeMillis() < stopTime){
           expired = false;
       }
       
       this.transactions.add(new Transaction(itemname,startTime,stopTime));
      }
  }else{
      String s2 = str;
       String[] s3 = s2.split(":");
       
       String itemname = s3[0];
       String[] date = s3[1].split(",");
       Long startTime = Long.parseLong(date[0]);
       Long stopTime = Long.parseLong(date[1]);
       boolean expired = true;
       if(System.currentTimeMillis() < stopTime){
           expired = false;
       }
       
       this.transactions.add(new Transaction(itemname,startTime,stopTime));
      
  }
   
}

/*
Rank format:
VIP:started,finished;

*/

private void parsePartRank(String rk){
          if(!rk.contains(",") || !rk.contains(":")){
              return;
          }
      String[] rktime = rk.split(":");
      String rkname = rktime[0];
      String[] rktime2 = rktime[1].split(",");

      Long now = System.currentTimeMillis();
      //Long rankstart = Long.parseLong(rktime2[0]); not in use.
      Long finished = Long.parseLong(rktime2[1]);
      if(now < finished){
          Rank myrank = Rank.User.getByName(rkname);
          if(myrank != Rank.User){
              myrank.setDaysleft((int)((finished - now) /86400000L));
              ranks.add(myrank);
        }
       }
}

private void parseRanks(String str){
    if(str.length() > 5){
     if(str.contains(";")){
      String[] rks = str.split(";");
      for(String rk : rks){
this.parsePartRank(rk);
      }
     } else{
      this.parsePartRank(str);
    }
   }
    Player pl = Bukkit.getPlayer(name);
    if(pl != null && getRank().isAtLeast(Rank.Helper)){
        Player player = Bukkit.getPlayer(name);
        PermissionAttachment attachment = player.addAttachment(plugin);
        plugin.attachments.put(player.getName(),attachment);
        for(String s : this.getRank().getPermissions()){
            plugin.attachments.get(player.getName()).unsetPermission(s);
            plugin.attachments.get(player.getName()).setPermission(s,true);
        }
    }
    
  }

    private void parseBans(String str){
        if(str.length() > 5){
            if(str.contains(";")){
                String[] rks = str.split(";");
                for(String rk : rks){
                    this.parsePartBan(rk);
                }
            } else{
                this.parsePartBan(str);
            }
        }
    }

    private void parsePartBan(String ban){
        if(!ban.contains(",") || !ban.contains(":")){
            return;
        }
        String[] bantime = ban.split(":");
        String banname = bantime[0];
        String[] bantime2 = bantime[1].split(",");
        String origen = bantime2[2];

        Long now = System.currentTimeMillis();
        //Long rankstart = Long.parseLong(rktime2[0]); not in use.
        Long finished = Long.parseLong(bantime2[1]);
        if(now < finished){
            banType myban = banType.getByName(banname);
            if(myban != banType.Unknown){
                int days = (int)((finished - now)/(1000*60*60*24));
                myban.setKickMessage("&aBaneado por "+myban.getReason()+" &cfinaliza en "+days+" dias &dOrigen: "+origen);
                bans.add(myban);
            }
        }
    }

public Rank getRank(){
   Rank rank = Rank.User;
   if(!this.ranks.isEmpty()){
       for(Rank raks : this.ranks){
           if(raks.getPos() > rank.getPos()){
               rank = raks;
           }
       }
   }
   return rank;
}

public int getKills(){
    return this.Kills;
}
    public void addKills(int kill){
        this.addValue(columnType.Kills,kill);
    }
    public void removeKills(int kill){
        this.addValue(columnType.Kills,(-kill));
    }
public int getDeaths(){
    return this.Deaths;
}
    public void addDeaths(int death){
        this.addValue(columnType.Deaths,death);
    }
    public void removeDeaths(int death){
        this.addValue(columnType.Deaths,(-death));
    }
public int getLoss(){
    return this.Loss;
}
    public void addLoss(int loss){
        this.addValue(columnType.Loss,loss);
    }
    public void removeLoss(int loss){
        this.addValue(columnType.Loss,(-loss));
    }
public int getWinned(){
    return this.Winned;
}
    public void addWinned(int win){
        this.addValue(columnType.Winned,win);
    }
    public void removeWinned(int win){this.addValue(columnType.Winned,(-win));}
public int getFirstPlaces(){
    return this.firstPlace;
}
    public void addFirstPlace(int first){
        this.addValue(columnType.FirstPlace,first);
    }
    public void removeFirstPlace(int first){
        this.addValue(columnType.FirstPlace,(-first));
    }
public int getSecondPlaces(){
    return this.secondPlace;
}
    public void addSecondPlace(int second){
        this.addValue(columnType.SecondPlace,second);
    }
    public void removeSecondPlace(int second){
        this.addValue(columnType.SecondPlace,(-second));
    }
public int getThirdPlaces(){
        return this.thirdPlace;
    }
    public void addThirdPlace(int third){
        this.addValue(columnType.ThirdPlace,third);
    }
    public void removeThirdPlace(int third){
        this.addValue(columnType.ThirdPlace,(-third));
    }
public double getCoins(){
        return this.Coins;
    }
    public void addCoins(int coins){
        this.addValue(columnType.Coins,coins);
    }
    public void removeCoins(int coins){
        this.addValue(columnType.Coins,(-coins));
    }

    private Integer getValue(columnType type) throws SQLException{
        ResultSet res = null;
        res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" WHERE Name = '"+name+"'");
        int value = -1;

        try{
            res.next();
            value = res.getInt(type.getName());

            res.close();} catch(Exception e){
            e.printStackTrace();}

        return value;
    }
    
private void addValue(columnType type, int value){
try{
    int val = this.getValue(type);

    val+= value;
    
String pay = "UPDATE "+plugin.table+" SET "+type.getName()+" ='"+val+"' WHERE Name = '"+name+"'";

     plugin.getStatement().execute(pay);  
}catch(Exception e){
    
}}


    
}


