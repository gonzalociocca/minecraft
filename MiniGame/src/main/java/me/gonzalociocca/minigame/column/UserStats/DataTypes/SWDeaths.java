package me.gonzalociocca.minigame.column.UserStats.DataTypes;


/**
 * Created by noname on 24/3/2017.
 */
public class SWDeaths extends StatsBaseType {

    @Override
    public String getIdentifier(){
        return "SW.Deaths";
    }

    public static String getStaticIdentifier() { return "SW.Deaths";}

    @Override
    public StatsBaseType decode(String content){
        return new SWDeaths().of(Double.parseDouble(content));
    }

    @Override
    public StatsBaseType getDefault(){
        return new SWDeaths().of(0);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
