package me.gonzalociocca.minigame.column.UserStats.DataTypes;

/**
 * Created by noname on 6/4/2017.
 */
public class SWKills extends StatsBaseType {

    @Override
    public String getIdentifier(){
        return "SW.Kills";
    }

    public static String getStaticIdentifier() { return "SW.Kills";}

    @Override
    public StatsBaseType decode(String content){
        return new SWKills().of(Double.parseDouble(content));
    }

    @Override
    public StatsBaseType getDefault(){
        return new SWKills().of(0);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
