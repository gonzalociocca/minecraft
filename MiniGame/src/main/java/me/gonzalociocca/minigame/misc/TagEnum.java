package me.gonzalociocca.minigame.misc;

/**
 * Created by noname on 30/3/2017.
 */
public enum TagEnum {
    ChestTag("ChestLocations"),
    PlayerLocationTag("PlayerLocations"),
    SpectatorLocationTag("SpectatorLocations"),
    MapNameTag("MapName"),
    Signs("Signs"),
    SingleLocation1("SingleLocation1"),
    SingleLocation2("SingleLocation2"),
    Hall("Hall");

    String id;
    TagEnum(String identifier){
        id = identifier;
    }
    public String getID(){
        return id;
    }
}
