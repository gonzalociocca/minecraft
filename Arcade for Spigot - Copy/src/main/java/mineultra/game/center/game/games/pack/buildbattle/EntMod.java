package mineultra.game.center.game.games.pack.buildbattle;

import mineultra.core.common.util.Colorizer;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;


public class EntMod {
    LivingEntity ete = null;
    Location forced = null;
    boolean freeze = true;
    boolean isAgeable = false;
    int age = -1;
    String name = null;
    public EntMod(LivingEntity et,Location loc, boolean frizz, boolean Ageable, int ages){
        ete = et;
        forced = loc;
        freeze = frizz;
        isAgeable = Ageable;
        age = ages;
        if(et instanceof Slime){
            isAgeable = true;
            age = 1;
        }
    }
    public boolean isFreezed(){
        return freeze;
    }
    public boolean isAgeable(){
        return isAgeable;
    }
    public int getAge(){
        if(age < 1){
            age = 1;
            return 1;
        }
        return age;
    }
    public void setAge(int ages){
        if(ages > 25){
            age = 25;
            return;
        }
       age = ages;
    }
    public void setFreeze(boolean frizz){
        freeze = frizz;
    }
    public Location getForcedLocation(){
        return forced;
    }
    public void setForcedLocation(Location loc){
        forced = loc;
    }
    public LivingEntity getEntity(){
        return this.ete;
    }
    public void setCustomName(String nam){
        this.name = Colorizer.Color(nam);
    }
    public boolean hasCustomName(){
        return name!=null;
    }
    public String getCustomName(){
        return name;
    }
    
}
