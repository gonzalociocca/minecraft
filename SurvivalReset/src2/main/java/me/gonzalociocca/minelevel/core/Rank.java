package me.gonzalociocca.minelevel.core;

/**
 *
 * @author cuack
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

import me.gonzalociocca.minelevel.core.Colorizer;
import org.bukkit.Material;

public enum Rank
{
  User(0,"User","&3","&3","noneperm",0),
  OTHER(1,"OTHER","&3","&3","noneperm",0),
  VIP(2,"VIP","&6&lVIP ","&e(&6&lVIP&e)&e","essentials.sethome.multiple:essentials.sethome.multiple.vip:essentials.kits.vip:preciousstones.limit2:essentials.enderchest:essentials.workbench",1),
  ELITE(3,"ELITE","&9&lELITE ","&e(&9&lELITE&e)&9&l","essentials.sethome.multiple:essentials.sethome.multiple.elite:essentials.kits.elite:preciousstones.limit3:essentials.enderchest:essentials.workbench",2),
  MVP(4,"MVP","&b&lMVP ","&6(&b&lMVP&6)&b&l","essentials.sethome.multiple:essentials.sethome.multiple.mvp:essentials.kits.mvp:preciousstones.limit4:essentials.enderchest:essentials.workbench",4),
  YT(5,"YT","&a&lYT ","&a&lYT&a","noneperm",0),
  DIOS(6,"DIOS","&b&lDIOS ","&b&k|||&b&lDIOS&b&k|||&3&l","essentials.sethome.multiple:essentials.sethome.multiple.dios:essentials.kits.dios:preciousstones.limit5:essentials.enderchest:essentials.workbench:essentials.enderchest.others:essentials.invsee",6),
  Builder(7,"Constructor","&2Constructor ","&a(&2&lCONSTRUCTOR&a)&a&l","essentials.helpop.receive:essentials.sethome.multiple:essentials.sethome.multiple.staff:preciousstones.limit5:minelevel.ban:minelevel.unban:minelevel.kick:minelevel.vanish:minelevel.tp:essentials.unlimited:essentials.unlimited.item-all",0),
  Helper(8,"Ayudante","&aAyudante ","&2(&a&lAYUDANTE&2)&e&l","essentials.helpop.receive:essentials.sethome.multiple:essentials.sethome.multiple.staff:preciousstones.limit5:minelevel.ban:minelevel.unban:minelevel.kick:minelevel.vanish:minelevel.tp",0),
  GM(9,"GM","&5&lGM ","&d(&5&lGM&d)&d&l","nocheatplus.command.notify:nocheatplus.notify:essentials.helpop:essentials.sethome.multiple:essentials.sethome.multiple.staff:preciousstones.limit5:minelevel.ban:minelevel.unban:minelevel.kick:minelevel.vanish:minelevel.tp",0),
  ADMIN(10,"Admin","&c&lADMIN ","&4(&c&lADMIN&4)&4&l","nocheatplus.command.notify:nocheatplus.notify:essentials.helpop.receive:essentials.sethome.multiple:essentials.sethome.multiple.staff:preciousstones.limit5:minelevel.ban:minelevel.unban:minelevel.kick:minelevel.vanish:minelevel.tp",0),
  OWNER(11,"Owner","&4&lOWNER ","&c(&4&lCREADOR&c)&4&l","nocheatplus.command.notify:nocheatplus.notify:essentials.helpop.receive:essentials.sethome.multiple:essentials.sethome.multiple.staff:preciousstones.limit5:minelevel.ban:minelevel.unban:minelevel.kick:minelevel.vanish:minelevel.tp",0);

  private String name = null;
  private String sprefix = null;
  private String cprefix = null;
  private int pos = 0;
  private List<String> permissions = new ArrayList();
  private int power = 0;
  
  private Rank(Integer ps, String n, String scoprefix, String chatprefix, String perms, int rkpower) {

      if(perms.contains(":")){
          for(String s : perms.split(":")){
              permissions.add(s);
          }
      }else{
          permissions.add(perms);
      }
      power = rkpower;
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
  public int getPower(){return this.power;}

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