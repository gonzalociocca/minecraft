package me.gonzalociocca.mineultra;

/**
 *
 * @author cuack
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
import org.bukkit.Material;

public enum Rank
{
  User(0,"User","&7","&7","noneperm"),
  OTHER(1,"OTHER","&7","&7","noneperm"),
  VIP(2,"VIP","&e[VIP] ","&e[&lVIP&e]&e","noneperm"),
  MVP(3,"MVP","&a[MVP] ","&a[MVP]&e","noneperm"),
  YT(4,"YT","&a[YT] ","&a[YT]&e","noneperm"),
  Helper(5,"Helper","&d[HELPER] ","&d[HELPER]&e","mu.ban:mu.unban:mu.kick:mu.vanish:mu.tp"),
  GM(6,"GM","&d[MG] ","&d[MG]&e","mu.ban:mu.unban:mu.kick:mu.vanish:mu.tp"),
  ADMIN(7,"Admin","&c[ADMIN] ","&c[ADMIN]&e","mu.ban:mu.unban:mu.kick:mu.vanish:mu.tp"),
  OWNER(8,"Owner","&c[OWNER] ","&c[OWNER]&e","mu.ban:mu.unban:mu.kick:mu.vanish:mu.tp");

  private String name = null;
  private String sprefix = null;
  private String cprefix = null;
  private int pos = 0;
  private List<String> permissions = new ArrayList();
  
  private Rank(Integer ps, String n, String scoprefix, String chatprefix, String perms) {

      if(perms.contains(":")){
          for(String s : perms.split(":")){
              permissions.add(s);
          }
      }else{
          permissions.add(perms);
      }
      
      pos = ps;
      name = n;
      sprefix = scoprefix;
      cprefix = chatprefix;
  }
  
  public List<String> getPermissions(){
      return permissions;
  }
  
  public String getScoreboardPrefix(){
      return Colorizer.Color(sprefix);
  }
  
  public String getChatPrefix(){
      return Colorizer.Color(cprefix);
  }
  

  public String getName(){
      return name;
  }
  
  public Rank getByName(String str){
      for(Rank rk : Rank.values()){
          if(rk.getName().equalsIgnoreCase(str)){
              return rk;
          }
      }
      return Rank.User;
  }
    public boolean isAtLeast(Rank compare){
        return this.getPos() >= compare.getPos();
    }
  
 public int getPos(){
     return pos;
 }
int daysleft = -1;
    public int getDaysLeft(){
        return daysleft;
    }
    public void setDaysleft(int a){
        daysleft = a;
    }
}