package server.instance.core.disguise;

import org.bukkit.entity.LivingEntity;
import server.common.event.GameStateChangeEvent;
import server.instance.misc.GameState;

/**
 * Created by noname on 19/4/2017.
 */
public class GameDisguise {

    public boolean isDisguised(LivingEntity ent){
        return false;
    }

    public void playDisguiseHurtSound(LivingEntity ent){ // > GetDisguise > playHurtSound

    }

    public void onGameStateChange(GameStateChangeEvent event){
        if(event.getState() == GameState.Dead){
            //Clean Disguises
        }
    }

}
