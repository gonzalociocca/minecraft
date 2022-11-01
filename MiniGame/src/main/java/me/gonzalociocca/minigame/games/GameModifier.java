package me.gonzalociocca.minigame.games;

public enum GameModifier {
    Normal("Normal", "&aNormal"), Insane("Insane", "&cInsano"), Ranked("Ranked", "&9Competitivo");

    String mod;
    String display;
    GameModifier(String modifier, String adisplay){
        mod = modifier;
        display = adisplay;
    }
    public String getName(){
        return mod;
    }
    public String getDisplay(){
        return display;
    }
    public static GameModifier getByName(String str){
        for(GameModifier modi : GameModifier.values()){
            if(modi.getName().equalsIgnoreCase(str)){
                return modi;
            }
        }
        return GameModifier.Normal;
    }

}
