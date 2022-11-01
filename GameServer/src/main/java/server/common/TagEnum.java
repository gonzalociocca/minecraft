package server.common;

/**
 * Created by noname on 30/3/2017.
 */
public enum TagEnum {
    EnderChestTag("EnderChestLocations"),
    ChestTag("ChestLocations"),
    PlayerLocationTag("PlayerLocations"),
    MapNameTag("MapName"),
    Signs("Signs"),
    SingleLocation1("SingleLocation1"),
    SingleLocation2("SingleLocation2"),
    SoloSpawn("SoloSpawn");

    String id;
    TagEnum(String identifier){
        id = identifier;
    }
    public String getID(){
        return id;
    }
}
