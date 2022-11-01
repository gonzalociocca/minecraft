package mineultra.core.common.util;

import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.BACKSTAB;
import mineultra.game.center.kit.perks.BATWAVE;
import mineultra.game.center.kit.perks.FLAMEDASH;
import mineultra.game.center.kit.perks.FOOD;
import mineultra.game.center.kit.perks.INFERNO;
import mineultra.game.center.kit.perks.ROPEDARROW;
import mineultra.game.center.kit.perks.SPEED;

public class Perks
{


public static Perk perkbyName(String name){
    if(name.equalsIgnoreCase("Ropped Arrow")){
    return  new ROPEDARROW("Ropped Arrow",1.0,6000L);
}else if(name.equalsIgnoreCase("Backstab")){
    return new BACKSTAB();
}else if(name.equalsIgnoreCase("Bat wave")){
    return new BATWAVE();
}else if(name.equalsIgnoreCase("Flame Dash")){
    return new FLAMEDASH();
}else if(name.equalsIgnoreCase("Inferno")){
    return new INFERNO();
}else if(name.equalsIgnoreCase("Speed")){
    return new SPEED(1);
}else if(name.equalsIgnoreCase("Food")){
    return new FOOD(20);
}
    
  return null;
}

}