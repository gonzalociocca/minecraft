package mineultra.core.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.Colorizer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Rank
{
  public Rank(){
      
  }
  public Rank(JavaPlugin plugin)
  {
      int val = 0;
 for(String px : plugin.getConfig().getStringList("ranks")){
     String[] px2 = px.split(",");
     prefixs.put(px2[1], px2[0]);
     val++;
     value.put(px2[1], val);
 }
  }
  static HashMap<String,String> prefixs = new HashMap();
  static HashMap<String,Integer> value = new HashMap();
  CachedPerm perm = new CachedPerm();
  
  public String prefix(Player player){
      String prx = "none";
      for(String ss : prefixs.keySet()){
          if(perm.hasPerm(player,ss)){
          if(!prx.equals("none")){
              
              
              
              int ssval = value.get(ss);
              int prxval = value.get(prx);
              if(ssval > prxval){
                  prx = ss;
              }else{
                  continue;
              }
          }
      prx = ss;
          }
          
      }
      
     if(prx.equals("none")){
         prx =  "";
     }else{
         prx = Colorizer.Color(prefixs.get(prx));
     }
     
      return prx;
  }
  public static Collection<String> tags(){
      return prefixs.values();
  }
 

}