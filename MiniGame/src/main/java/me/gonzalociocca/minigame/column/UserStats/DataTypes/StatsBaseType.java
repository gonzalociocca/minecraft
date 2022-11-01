package me.gonzalociocca.minigame.column.UserStats.DataTypes;

public abstract class StatsBaseType {

    public abstract String getIdentifier();
    double value = 0;

    public StatsBaseType of(double newValue){
        value = newValue;
        return this;
    }

    public void addToValue(double addValue){
        value+=addValue;
        value = Math.round(value);
    }
    public void substractValue(double substractValue){
        value-=substractValue;
    }
    public void setValue(double newValue){
        value = newValue;
    }
    public double getValue(){
        return value;
    }
    /**
     * Decode:
     * Should convert the value to a desired class
     **/
    public abstract StatsBaseType decode(String content);

    public String encode(){
        return getIdentifier()+"=<"+getValue()+">";
    }

    public abstract StatsBaseType getDefault();

    public abstract boolean isEmpty();

}
