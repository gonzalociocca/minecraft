package me.gonzalociocca.minelevel.core;


import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
import org.bukkit.Material;

public enum columnType
{
  Name("Name","null",false),
  UUID("UUID","null",false),
  Kills("Kills","0",true),
  Deaths("Deaths","0",true),
  MineriaPts("MineriaPts","0",true),
  ColiseoPts("ColiseoPts","0",true),
  LastMessages("LastMessages","",false),
  LastReports("LastReports","",false),
  LastTransactions("LastTransactions","",false),
  Pets("Pets","",false),
  Ranks("Ranks","",false),
  Diamonds("Diamonds","0",true),
  Friends("Friends","",false),
  Bans("Bans","",false);

  private String name = null;
  private String defa = null;
  private boolean isInteger = false;
  columnType(String prefix,String def,boolean isInt) {
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

    public static columnType getbyName(String str){
      for(columnType type : columnType.values()){
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