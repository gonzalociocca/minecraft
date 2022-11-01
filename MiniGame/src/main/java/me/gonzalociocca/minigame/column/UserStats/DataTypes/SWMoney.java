package me.gonzalociocca.minigame.column.UserStats.DataTypes;


/**
 * Created by noname on 24/3/2017.
 */
public class SWMoney extends StatsBaseType {

    @Override
    public String getIdentifier(){
        return "SW.Money";
    }

    public static String getStaticIdentifier() { return "SW.Money";}

    @Override
    public StatsBaseType decode(String content){
        return new SWMoney().of(Double.parseDouble(content));
    }

    @Override
    public StatsBaseType getDefault(){
        return new SWMoney().of(0);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
