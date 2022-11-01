package me.gonzalociocca.mineultra;


import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
import org.bukkit.Material;

public enum serverType
{
    SkyWars("SkyWars", Material.GOLD_SWORD,(short)0,14,false,null),
    SurvivalGames("SurvivalGames",Material.CHEST,(short)0,15,false,null),
    BuildBattle("BuildBattle",Material.BOOKSHELF,(short)0,22,false,null),
    DTN("Destruye el Nexo",Material.END_CRYSTAL,(short)0,23,false,null),
    ZombieSurvival("ZombieSurvival", Material.SKULL_ITEM,(short)2,24,false,null),
    SuperSpleef("SuperSpleef", Material.TNT,(short) 0,25,false,null),
    OITQ("Uno en la flecha", Material.SPECTRAL_ARROW,(short) 0,31,false,null),
    Factions("Survival y Factions", Material.DIAMOND_SWORD,(short) 0,19,true,"/endworld"),
    SkyBlock("SkyBlock", Material.GRASS,(short) 0,20,true,"/skyblock"),
    OPPrison("OPPrison", Material.IRON_FENCE,(short) 0,28,true,"/prison"),
    Rex("Rex", Material.GOLD_SWORD,(short) 0,29,true,"/rex"),
    Unknown("Unknown", Material.AIR,(short) 0,0,false,null);


String name;
Material icon;
    short dura;
    int slot;
    boolean isSingle;
    String cmd;
    serverType(String str,Material aicon, short adura,int aslot,boolean aisSingle, String acmd) {
       cmd = acmd;
        isSingle=aisSingle;
        slot = aslot;
name = str;
        icon = aicon;
        dura = adura;
    }

    public String getName(){
        return name;
    }

    public static serverType getbyName(String str){
        for(serverType type : serverType.values()){
            if(type.getName().equalsIgnoreCase(str)){
                return type;
            }
        }
        return serverType.Unknown;
    }

    public Material getIcon(){
        return icon;
    }
    public short getDurability(){
        return dura;
    }
    public int getSlot(){
        return slot;
    }
    public boolean isSingle(){
        return isSingle;
    }
    public String getCMD(){
        return cmd;
    }


}