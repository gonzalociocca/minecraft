package me.gonzalociocca.minelevel.core.user.mute;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.database.DatabaseListener;
import me.gonzalociocca.minelevel.core.enums.ColumnType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.ban.BanType;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mute {

    private String _executor;
    private long _startTime;
    private long _endTime;
    private String _note;
    private String _Message;

    public Mute(long startTime,  long endTime, String executor, String note) {
        _executor = executor;
        _startTime = startTime;
        _endTime = endTime;
        _note = note == null ? "" : note;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
        Date resultdate = new Date(_endTime);
        String result = sdf.format(resultdate);

        _Message = Code.Color("&aEstas muteado por "+_executor+"&f hasta "+result +(_note.isEmpty() ? "" : "&e Nota:"+_note));
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
     * String[1] = (long) _startTime;
     * String[2] = (long) _endTime;
     * String[3] = (String) _executor
     * String[4] = (String) _note
     */

    public String toString(){
        return new String(
                        _startTime + "," +
                        _endTime + "," +
                        _executor + "," +
                        _note
        );
    }

    public static Mute fromString(String updated){
        Mute myMute = null;
        try {
            if (!updated.isEmpty()) {
                String[] split = updated.split(",");
                long startTime = Long.parseLong(split[0]);
                long endTime = Long.parseLong(split[1]);
                String executor = split[2];
                String note = split.length > 3 ? split[3] : null;

                if (System.currentTimeMillis() < endTime) {
                    myMute = new Mute(startTime, endTime, executor, note);

                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return myMute;
    }

    public static String handleMute(Main plugin, boolean isConsole, PlayerData senderData, CommandSender sender, String[] args){
        String message = "&4Error";

        if(!isConsole && !senderData.getRank().getType().isAtLeast(RankType.Builder)){
            message = "&eEste comando solo es para staff.";
        }
        else if (args.length < 2){
            message = "&aUso: &e/mute <nombre> <minutos> <notas adicionales>";
        } else {
            String name = args[0];
            int minutes = Integer.parseInt(args[1]);
            String notes = null;
            RankType rankType = isConsole ? RankType.OWNER : senderData.getRank().getType();
            PlayerData otherData = plugin.getDB().getPlayerData(name);

            if (minutes < 1) {
                message = "&eComando Invalido, especifica los minutos.";
            } else {
                if (args.length > 2) {
                    notes = "";
                    for (int a = 2; a < args.length; a++) {
                        notes = notes + " " + args[a].replaceAll("[^A-Za-z0-9]", "");
                    }
                }

                if (otherData == null) {
                    message = "&cEl jugador " + name + " no existe.";
                } else if (!rankType.isAtLeast(RankType.Builder)) {
                    message = "&eSolo staffs con el rango Constructor en adelante pueden usar este comando.";
                } else if (minutes > 1440 && !rankType.isAtLeast(RankType.ADMIN)) {
                    message = "&eSolo admins pueden mutear por mas de 1 dia.";
                } else if (otherData.getRank().getType().isAtLeast(RankType.Builder) && !rankType.isAtLeast(RankType.ADMIN)) {
                    message = "&eSolo admins pueden mutear a otros staffs.";
                } else {
                    Mute.mute(otherData,minutes*60000L,sender.getName(), notes);
                    message = "&a"+name+" ha sido muteado por " + minutes + " minutos.";
                }
            }
        }
        return Code.Color(message);
    }

    public static String handleUnmute(Main plugin, boolean isConsole, PlayerData senderData, CommandSender sender, String[] args){
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
                    unmute(other);
                    message = "&c" + name + " &edesbaneado por &7" + sender.getName();
                }
            }
        }
        return Code.Color(message);
    }

    public static void parseSingleMute(PlayerData pd, String str){
        Mute mute = Mute.fromString(str);
        if(mute != null){
            pd.getMuteList().add(mute);
        }
    }

    public static void parseMutes(PlayerData pd, String str) {
        if (str != null && !str.isEmpty()) {
            if (str.contains(";")) {
                String[] rks = str.split(";");
                for (String rk : rks) {
                    parseSingleMute(pd, rk);
                }
            } else {
                parseSingleMute(pd, str);
            }
        }
    }

    public static void unmute(PlayerData pd){
        DatabaseListener dbl = pd.getDatabaseListener();
        try {
            String pay = "UPDATE " + dbl.table + " SET " + ColumnType.Mutes.getName() + " ='' WHERE Name = '" + pd.getName() + "'";
            dbl.getStatement().execute(pay);
            pd.getMuteList().clear();
        } catch (Exception e) {
            System.out.println("Failed to unmute " + pd.getName() + " looks like a mysql issue!");
            e.printStackTrace();
        }
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

    public static void mute(PlayerData pd, long duration, String sender, String note) {
        try {
            DatabaseListener dbl = pd.getDatabaseListener();
            ResultSet res;
            res = dbl.getStatement().executeQuery("SELECT * FROM " + dbl.table + " WHERE Name = '" + pd.getName() + "'");
            res.next();
            String mutes = res.getString(ColumnType.Mutes.getName());
            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis() + duration;

            Mute newMute = new Mute(startTime, endTime, sender, note);

            mutes = mutes.isEmpty() ? newMute.toString() : mutes + ";" + newMute.toString();

            pd.getMuteList().clear();
            Mute.parseMutes(pd, mutes);

            String pay = "UPDATE " + dbl.table + " SET " + ColumnType.Mutes.getName() + " ='" + mutes + "' WHERE Name = '" + pd.getName() + "'";
            dbl.getStatement().execute(pay);
        } catch (Exception e) {
            System.out.println("Failed to mute " + pd.getName() + " looks like a mysql issue!");
            e.printStackTrace();
        }
    }


}