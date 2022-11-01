package me.gonzalociocca.minigame.column.UserStats.DataTypes;

public class SWElo extends StatsBaseType {

    @Override
    public String getIdentifier() {
        return "SW.Elo";
    }

    public static String getStaticIdentifier() {
        return "SW.Elo";
    }

    @Override
    public StatsBaseType decode(String content) {
        return new SWElo().of(Double.parseDouble(content));
    }

    @Override
    public StatsBaseType getDefault() {
        return new SWElo().of(1000);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}

