package me.gonzalociocca.mineultra;

import org.bukkit.Material;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
import org.bukkit.Material;

public enum banType
{
    Hacks("hacks","usar hacks en nuestros servidores", 30),
    Grifear("grifear","grifear en nuestros servidores", 20),
    Bugear("bugear","bugear en nuestros servidores", 10),
    Macros("macros","usar macros o autoclick en nuestros servidores", 10),
    Insultos("insultos","Hablar mal en nuestros servidores", 15),
    Unknown("Unknown","sin razon",0);


    String name;
    String reason;
    int days;

    banType(String aname, String areason, int adays) {
        name = aname;
        reason = areason;
        days = adays;
    }

    public static banType getByName(String str){
        for(banType tp : banType.values()){
            if(tp.getName().equalsIgnoreCase(str)){
                return tp;
            }
        }
        return banType.Unknown;
    }

    public static String getAllNames(){
        String names = "";
        for(banType tp : banType.values()){
            if(tp==banType.Unknown){
                continue;
            }
            names = names+tp.getName()+" | ";
        }
        return names;
    }


    public String getName(){
        return name;
    }

    public String getReason(){
        return reason;
    }

    public int getDays(){
        return days;
    }

    private String kickMessage = "Unknown";

    public void setKickMessage(String str){
        kickMessage = str;
    }

    public String getKickMessage(){
        return kickMessage;
    }

}