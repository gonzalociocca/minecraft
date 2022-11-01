package me.gonzalociocca.minigame.column.Perks;

import me.gonzalociocca.minigame.column.Perks.DataTypes.GlobalRanks;
import me.gonzalociocca.minigame.column.Perks.DataTypes.PerkBaseType;
import me.gonzalociocca.minigame.misc.PlayerData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PerksManager {
    public PlayerData playerdata;
    public HashMap<String, PerkBaseType> data = new HashMap();
    public static HashMap<String, PerkBaseType> DataTypes;
    int firstHashcode;
    static {
        DataTypes = new HashMap<String, PerkBaseType>();

        DataTypes.put(GlobalRanks.getStaticIdentifier(), new GlobalRanks(new ArrayList()));
    }

    public PerksManager(PlayerData pd) {
        playerdata = pd;
    }

    private Object getDataOf(String str) {
        Object object = data.get(str);
        if (object != null) {
            return object;
        }
        PerkBaseType obj = DataTypes.get(str).getDefault();
        data.put(str, obj);

        return obj;
    }

    public void parseAllData(String encodeddata) {
        data.clear();
        if (encodeddata.isEmpty()) {
            return;
        }
        String[] datapart = encodeddata.split(";");
        for (String s : datapart) {
            String[] part2 = s.split("=");
            String ID = part2[0];
            String content = part2[1].replaceFirst("<", "").replaceFirst(">", "");
            data.put(ID, DataTypes.get(ID).decode(content));
        }
        firstHashcode = data.hashCode();
    }

    public String encodeAllData() {
        String str = "";
        if (!data.isEmpty()) {
            for (Map.Entry<String, PerkBaseType> entry : data.entrySet()) {
                PerkBaseType value = entry.getValue();
                if(value.isEmpty()){
                    continue;
                }
                if (str.isEmpty()) {
                    str = str + value.encode();
                } else {
                    str = str + ";" + value.encode();
                }
            }
        }
        return str;
    }

    public static String getColumn() {
        return "Perks";
    }

    public GlobalRanks getGlobalRanks(boolean shouldModify){
        if(shouldModify){
            playerdata.reLoadPerks();
        }
        return (GlobalRanks) getDataOf(GlobalRanks.getStaticIdentifier());
    }
    public boolean saveAllData() {
        if(playerdata.playerid==-1){
            return false;
        }
        String encoded = encodeAllData();

        try {
            playerdata.core.getMySQL().getStatement().execute("UPDATE "+playerdata.core.getMySQL().getUserTable()+" SET "+
                    getColumn()+" ='"+encoded+"'"+
                    " WHERE ID = '"+playerdata.playerid+"'");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}