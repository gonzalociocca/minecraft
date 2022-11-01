package me.gonzalociocca.minelevel.core.enums;

/**
 * Created by noname on 19/2/2017.
 */
public enum SvType {
    Lobby(true),
    Factions(false),
    Rex(true),
    Other(true);
    boolean isReadOnly;
    SvType(boolean readOnly){
        isReadOnly = readOnly;
    }
    public boolean isReadOnly(){
        return isReadOnly;
    }
}