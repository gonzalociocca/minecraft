package me.gonzalociocca.minigame.column.UserStats;

import com.sun.org.glassfish.external.statistics.Stats;
import me.gonzalociocca.minigame.column.UserStats.DataTypes.*;
import me.gonzalociocca.minigame.misc.PlayerData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class StatsManager {
    PlayerData playerdata;
    HashMap<String, StatsBaseType> data = new HashMap();
    public static HashMap<String, StatsBaseType> DataTypes;

    static {
        DataTypes = new HashMap<String, StatsBaseType>();

        DataTypes.put(SWMoney.getStaticIdentifier(), new SWMoney().of(0));
        DataTypes.put(SWElo.getStaticIdentifier(), new SWElo().of(1200));
        DataTypes.put(SWKills.getStaticIdentifier(), new SWKills().of(0));
        DataTypes.put(SWDeaths.getStaticIdentifier(), new SWDeaths().of(0));
    }

    public StatsManager(PlayerData pd) {
        playerdata = pd;
    }

    private Object getDataOf(String str) {
        Object object = data.get(str);
        if (object == null) {
            object = DataTypes.get(str).getDefault();
            data.put(str, (StatsBaseType) object);
        }
        return object;
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
    }

    public String encodeAllData() {
        String str = "";
        if (!data.isEmpty()) {
            for (Map.Entry<String, StatsBaseType> entry : data.entrySet()) {
                if (str.isEmpty()) {
                    str = str + entry.getValue().encode();
                } else {
                    str = str + ";" + entry.getValue().encode();
                }
            }
        }
        return str;
    }

    public SWMoney getSWMoney() {
        return (SWMoney) this.getDataOf(SWMoney.getStaticIdentifier());
    }

    public SWElo getSWElo() {
        return (SWElo) this.getDataOf(SWElo.getStaticIdentifier());
    }

    public SWKills getSWKills() {
        return (SWKills) this.getDataOf(SWKills.getStaticIdentifier());
    }

    public SWDeaths getSWDeaths() {
        return (SWDeaths) this.getDataOf(SWDeaths.getStaticIdentifier());
    }


    public boolean saveAllData() {
        if (playerdata.preventSave || playerdata.playerid == -1) {
            return false;
        }
        String encoded = encodeAllData();
        if (!playerdata.shouldSave(encoded)) {
            return false;
        }
        try {
            playerdata.core.getMySQL().getStatement().execute("UPDATE " + playerdata.core.getMySQL().getUserTable() + " SET " +
                    getColumn() + " ='" + encoded + "'" +
                    " WHERE ID = '" + playerdata.playerid + "'");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getColumn() {
        return "Stats";
    }
}
