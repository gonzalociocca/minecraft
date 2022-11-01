package mineultra.core.common;

import java.util.HashMap;
import java.util.HashSet;
import mineultra.core.MiniPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CachedPerm
{

  public CachedPerm()
  {

  }

  
HashMap<String,HashSet> stored = new HashMap();
HashMap<String,HashSet> nostored = new HashMap();
Long reset = 0L;
 public boolean hasPerm(Player p, String perm){
     boolean ret = false;
    if(!stored.containsKey(p.getName())){
        stored.put(p.getName(), new HashSet());
    }
    if(!nostored.containsKey(p.getName())){
        nostored.put(p.getName(), new HashSet());
    }
    
    if(stored.get(p.getName()).contains(perm)){
    ret = true;
    }
    else if(nostored.get(p.getName()).contains(perm)){
        ret = false;
    }
    else if(p.hasPermission(perm)){
      stored.get(p.getName()).add(perm);
      ret = true;
    }else{
      nostored.get(p.getName()).add(perm);
      ret = false;
    }
    
    if(reset < System.currentTimeMillis()){
        reset = System.currentTimeMillis()+15000L;
        stored.clear();
        nostored.clear();
    }
    return ret;
}
 public void reset(Player p){
     if(nostored.containsKey(p.getName())){
         nostored.remove(p.getName());
     }
 }
 

}