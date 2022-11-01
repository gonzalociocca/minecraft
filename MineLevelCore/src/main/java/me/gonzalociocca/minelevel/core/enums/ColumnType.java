package me.gonzalociocca.minelevel.core.enums;

public enum ColumnType
{
    Name("Name","null",false),
    UUID("UUID","null",false),
    LastTransactions("LastTransactions","",false),
    Ranks("Ranks","",false),
    Diamonds("Diamonds","0",true),
    Mutes("Mutes", "", false),
    Bans("Bans","",false);

    private String name = null;
    private String defa = null;
    private boolean isInteger = false;
    ColumnType(String prefix,String def,boolean isInt) {
        defa = def;
        name = prefix;
        isInteger = isInt;
    }

    public String getName(){
        return name;
    }

    public boolean isInteger(){
        return isInteger;
    }

    public static ColumnType getbyName(String str){
        for(ColumnType type : ColumnType.values()){
            if(type.getName().equalsIgnoreCase(str)){
                return type;
            }
        }
        return null;
    }

    public String getDefault(){
        return defa;
    }
}