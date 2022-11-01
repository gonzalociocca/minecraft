package me.gonzalociocca.minigame.games;

import me.gonzalociocca.minigame.map.Cipher.MapCipherBase;
import me.gonzalociocca.minigame.map.Cipher.SkyWarsSoloMap;

/**
 * Created by noname on 27/3/2017.
 */
public enum GameState {
    Loading("&9Reiniciando"),
    InWait("&eReclutando"),
    Starting("&2Empezando"),
    InGame("&cEn Juego"),
    Finishing("&4Terminado");

    String stateFormat;
    GameState(String format) {
        stateFormat = format;
    }
    public String getDisplay(){
        return stateFormat;
    }

}
