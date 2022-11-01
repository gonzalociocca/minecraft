package me.gonzalociocca.minelevel.core.user.ban;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.database.DatabaseListener;
import me.gonzalociocca.minelevel.core.enums.ColumnType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ban {

    private String _executor;
    private BanType _banType;
    private long _startTime;
    private long _endTime;
    private String _note;
    private String _Message;

    public Ban(BanType banType, long startTime,  long endTime, String executor, String note) {
        _executor = executor;
        _banType = banType;
        _startTime = startTime;
        _endTime = endTime;
        _note = note == null ? "" : note;

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
        Date resultdate = new Date(_endTime);
        String result = sdf.format(resultdate);
        _Message = Code.Color("&aEstas baneado por "+_executor+"&f hasta "+result +(_note.isEmpty() ? "" : "&e Nota:"+_note));
    }

    public String getMessage() {
        return _Message;
    }

    public String getNote(){
        return _note;
    }

    public String getExecutor(){
        return _executor;
    }

    public BanType getBanType(){
        return _banType;
    }

    public long getStartTime(){
        return _startTime;
    }

    public long getEndTime(){
        return _endTime;
    }

    public long getTimeLeft(long now){
        return _endTime - now;
    }

    public int getDaysLeft(long now){
        return Math.max(0,(int)((_endTime - now)/(1000*60*60*24)));
    }
    public int getHoursLeft(long now){
        return Math.max(0,(int)((_endTime - now)%86400000)/3600000);
    }
    public int getMinutesLeft(long now){
        return Math.max(0,(int)((_endTime - now)%(60000*60))/60000);
    }
    public int getSecondsLeft(long now){
        return Math.max(0,(int)((_endTime - now)%(60000))/1000);
    }

    public boolean hasExpired(){
        return System.currentTimeMillis() > _endTime;
    }

    /**
     * String[0] = (String) BanType.getBanTypeName();
     * String[1] = (long) _startTime;
     * String[2] = (long) _endTime;
     * String[3] = (String) _executor
     * String[4] = (String) _note
    */

    public String toString(){
        return new String(
                _banType.getBanTypeName() + "," +
                        _startTime + "," +
                        _endTime + "," +
                        _executor + "," +
                        _note
        );
    }

    public static Ban fromString(String str){
        Ban myBan = null;
        try {
            if (str != null && !str.isEmpty()) {
                String updated = str.replace(":", ","); // Update ":" to "," due to version update.
                String[] split = updated.split(",");
                BanType banType = BanType.getByName(split[0]);
                long startTime = Long.parseLong(split[1]);
                long endTime = Long.parseLong(split[2]);
                String executor = split[3];
                String note = split.length > 4 ? split[4] : "";

                if (banType != BanType.Unknown && (System.currentTimeMillis() < endTime)) {
                    myBan = new Ban(banType, startTime, endTime, executor, note);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return myBan;
    }

    public static String handleBan(Main plugin, boolean isConsole, PlayerData senderData, CommandSender sender, String[] args){
        String message = "&4Error";

        if(!isConsole && !senderData.getRank().getType().isAtLeast(RankType.Builder)){
            message = "&eEste comando solo es para staff.";
        }
        else if (args.length < 2){
            message = "&aUso: &e/ban <nombre> <tipo de baneo> <notas adicionales>";
            sender.sendMessage(Code.Color("&aTipos de Baneo: &e" + Ban.getAllNames()));
        } else {
            String name = args[0];
            String type = args[1];
            BanType banType = BanType.getByName(type);
            String notes = null;
            RankType rankType = isConsole ? RankType.OWNER : senderData.getRank().getType();
            PlayerData otherData = plugin.getDB().getPlayerData(name);
            Player other = Bukkit.getPlayer(name);

            if (banType == BanType.Unknown) {
                message = "&aEspecifica Tipo de Baneo: &e" + Ban.getAllNames();
            } else {
                if (args.length > 2) {
                    notes = "";
                    for (int a = 2; a < args.length; a++) {
                        notes = notes + " " + args[a].replaceAll("[^A-Za-z0-9]", "");
                    }
                }

                if (otherData == null) {
                    message = "&cEl jugador " + name + " no existe.";
                } else if (!rankType.isAtLeast(RankType.GM)) {
                    message = "&eSolo staffs con el rango GM en adelante pueden usar este comando.";
                } else if (banType == BanType.Permanente && !rankType.isAtLeast(RankType.ADMIN)) {
                    message = "&eSolo admins pueden banear permanente.";
                } else if (otherData.getRank().getType().isAtLeast(RankType.Builder) && !rankType.isAtLeast(RankType.ADMIN)) {
                    message = "&eSolo admins pueden banear a otros staffs.";
                } else {
                    otherData.ban(banType, sender.getName(), notes);
                    if (other != null) {
                        other.kickPlayer(Code.Color(otherData.getBan().getMessage()));
                    }
                    message = "&a"+name+" ha sido baneado " + banType.getDefaultDays()+" dias por "+banType.getReason()+" Nota: "+notes;
                }
            }
        }
        return Code.Color(message);
    }

    public static String handleUnban(Main plugin, boolean isConsole, PlayerData senderData, CommandSender sender, String[] args){
        String message = "&4Error";

        if(args.length < 1){
            message = "&aUso: /unban <nombre>";
        } else {
            RankType rankType = isConsole ? RankType.OWNER : senderData.getRank().getType();
            if(!rankType.isAtLeast(RankType.ADMIN)){
                message = "&eSolo admins pueden desbanear.";
            } else {
                String name = args[0];
                PlayerData other = plugin.getDB().getPlayerData(name);

                if (other == null) {
                    message = "&cEse jugador no existe.";
                } else {
                    other.unban();
                    message = "&c" + name + " &edesbaneado por &7" + sender.getName();
                }
            }
        }
        return Code.Color(message);
    }


    public static String getAllNames(){
        String names = "";
        for(BanType tp : BanType.values()){
            if(tp==BanType.Unknown){
                continue;
            }
            names = names+tp.getBanTypeName()+" | ";
        }
        return names;
    }

    public static void ban(PlayerData pd, BanType type, String sender, String note) {
        String name = pd.getName();
        DatabaseListener dbl = pd.getDatabaseListener();
        String table = dbl.table;
        ResultSet res;

        try {
            res = dbl.getStatement().executeQuery("SELECT * FROM " + table + " WHERE Name = '" + name + "'");
            res.next();
            long startTime = System.currentTimeMillis();
            long endTime = startTime + (type.getDefaultDays()*86400000L);

            Ban ban = new Ban(type, startTime, endTime, sender, note);

            String rks = res.getString(ColumnType.Bans.getName());
            rks = rks.isEmpty() ? rks + ban.toString() : rks + ";" + ban.toString();

            pd.getBanList().clear();
            parseBans(pd, rks);

            String pay = "UPDATE " + table + " SET " + ColumnType.Bans.getName() + " ='" + rks + "' WHERE Name = '" + name + "'";
            dbl.getStatement().execute(pay);
        } catch (Exception e) {
            System.out.println("Failed to ban " + name + "looks like a mysql issue!");
        }
    }

    public static void parseSingleBan(PlayerData pd, String str){
        Ban ban = Ban.fromString(str);
        if(ban != null){
            pd.getBanList().add(ban);
        }
    }

    public static void parseBans(PlayerData pd, String str) {
        try {
            if (str != null && !str.isEmpty()) {
                if (str.contains(";")) {
                    String[] rks = str.split(";");
                    for (String rk : rks) {
                        parseSingleBan(pd, rk);
                    }
                } else {
                    parseSingleBan(pd, str);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void unban(PlayerData pd){
        DatabaseListener dbl = pd.getDatabaseListener();
        try {
            String pay = "UPDATE " + dbl.table + " SET " + ColumnType.Bans.getName() + " ='' WHERE Name = '" + pd.getName() + "'";
            dbl.getStatement().execute(pay);
            pd.getBanList().clear();
        } catch (Exception e) {
            System.out.println("Failed to ban " + pd.getName() + "looks like a mysql issue!");
        }
    }

}