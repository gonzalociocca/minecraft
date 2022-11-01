package me.gonzalociocca.minelevel.mercado;


import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
import org.bukkit.Material;

public enum tablecolumnType
{
    Name("Name","null"),
    UUID("UUID","null"),
    Inventory("Inventory",""),
    History("History","");

    private String name = null;
    private String defa = null;

    tablecolumnType(String prefix,String def) {
        defa = def;
        name = prefix;
    }

    public String getName(){
        return name;
    }

    public static tablecolumnType getbyName(String str){
        for(tablecolumnType type : tablecolumnType.values()){
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