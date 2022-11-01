package me.gonzalociocca.minigame.games;

import me.gonzalociocca.minigame.games.game.GameBase;
import me.gonzalociocca.minigame.games.game.SkyWarsGame;
import me.gonzalociocca.minigame.map.Cipher.MapCipherBase;
import me.gonzalociocca.minigame.map.Cipher.SkyWarsSoloMap;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by noname on 27/3/2017.
 */
public enum GameType {
    SkyWarsSolo("&2SkyWars", "SWSolo", false, SkyWarsSoloMap.class, SkyWarsGame.class),
    EggWarsSolo("&2EggWars", "EggSolo", false, SkyWarsSoloMap.class, SkyWarsGame.class),
    BedWarsSolo("&2BedWars", "BedSolo", false, SkyWarsSoloMap.class, SkyWarsGame.class);
    String display;
    String shortName;
    boolean allowTeams;
    Class<? extends MapCipherBase> maptype;
    Class<? extends GameBase> gamebase;
    GameType(String displayName, String ashortName, boolean Teams, Class<? extends MapCipherBase> type, Class<? extends GameBase> game) {
        display = displayName;
        shortName = ashortName;
        allowTeams = Teams;
        maptype = type;
        gamebase = game;
    }
    public String getDisplay(){
        return display;
    }

    public boolean areTeamsAllowed(){
        return allowTeams;
    }

    public String getShortName(){
        return shortName;
    }
    public static GameType getByShortName(String str){
        for(GameType type: GameType.values()){
            if(type.getShortName().equalsIgnoreCase(str)){
                return type;
            }
        }
        return GameType.SkyWarsSolo;
    }
    public static GameType getByDisplay(String str){
        String strClean = ChatColor.stripColor(str);
        for(GameType type: GameType.values()){
            if(type.getDisplay().equalsIgnoreCase(strClean)){
                return type;
            }
        }
        return GameType.SkyWarsSolo;
    }

    public Class<? extends MapCipherBase> getMapType(){
        return maptype;
    }
    public Class<? extends GameBase> getGameType(){
        return gamebase;
    }


}
