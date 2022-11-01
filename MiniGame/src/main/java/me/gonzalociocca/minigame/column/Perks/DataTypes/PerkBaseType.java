package me.gonzalociocca.minigame.column.Perks.DataTypes;

public abstract class PerkBaseType {

    public abstract String getIdentifier();

    /**
     * Decode:
     * Should convert the value to a desired class
     **/
    public abstract PerkBaseType decode(String content);

    /**
     * Encode:
     * Should convert the class to a desired value
     **/
    public abstract String encode();

    public abstract PerkBaseType getDefault();

    public abstract boolean isEmpty();

}
