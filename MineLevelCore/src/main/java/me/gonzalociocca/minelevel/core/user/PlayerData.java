package me.gonzalociocca.minelevel.core.user;

import me.gonzalociocca.minelevel.core.database.DatabaseListener;
import me.gonzalociocca.minelevel.core.enums.ColumnType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.ban.Ban;
import me.gonzalociocca.minelevel.core.user.ban.BanType;
import me.gonzalociocca.minelevel.core.user.mute.Mute;
import me.gonzalociocca.minelevel.core.user.rank.Rank;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import me.gonzalociocca.minelevel.core.user.transaction.Transaction;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PlayerData {

    public String scoreboardValue1 = "";
    public String scoreboardValue2 = "";
    public String scoreboardValue3 = "";
    public String scoreboardValue4 = "";

    private DatabaseListener plugin = null;
    private String name = null;
    private double Diamonds = 0;
    private List<Rank> rankList = new ArrayList();
    private List<Mute> muteList = new ArrayList();
    private List<Ban> banList = new ArrayList();
    private List<Transaction> transactions = new ArrayList();
    public boolean changed = false;
    Player currentPlayer;
    boolean isSilent = false;

    public PlayerData(String gname, DatabaseListener aThis) throws SQLException {
        plugin = aThis;
        name = gname.toLowerCase();
        this.loadData();
    }

    public DatabaseListener getDatabaseListener(){
        return plugin;
    }

    public boolean preventsave = false;

    public void loadData() {
        try {
            plugin.checkData();
            ResultSet res;
            res = plugin.getStatement().executeQuery("SELECT * FROM " + plugin.table + " WHERE Name = '" + name + "'");

            res.next();
            Diamonds = res.getDouble(ColumnType.Diamonds.getName());
            parseTransactions(res.getString(ColumnType.LastTransactions.getName()));
            parseRanks(res.getString(ColumnType.Ranks.getName()));
            Ban.parseBans(this, res.getString(ColumnType.Bans.getName()));
            Mute.parseMutes(this, res.getString(ColumnType.Mutes.getName()));
            res.close();
            if(getRank().getType().isAtLeast(RankType.Builder)){
                isSilent = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            changed = false;
            preventsave = true;
        }
    }

    public boolean isSilent(){
        return isSilent;
    }

    public void addRank(String rank, int days) {
        try {
            ResultSet res;
            res = plugin.getStatement().executeQuery("SELECT * FROM " + plugin.table + " WHERE Name = '" + name + "'");
            res.next();
            String rks = res.getString(ColumnType.Ranks.getName());
            if (!rks.isEmpty()) {
                rks = rks + ";" + rank + ":" + System.currentTimeMillis() + "," + (System.currentTimeMillis() + (86400000L * days));
            } else {
                rks = rank + ":" + System.currentTimeMillis() + "," + (System.currentTimeMillis() + (86400000L * days));
            }

//reload ranks for player
            this.rankList.clear();
//update ranks
            String pay = "UPDATE " + plugin.table + " SET " + ColumnType.Ranks.getName() + " ='" + rks + "' WHERE Name = '" + name + "'";
            plugin.getStatement().execute(pay);
            this.addTransaction(rank, days);
            this.loadData();
        } catch (Exception e) {
            System.out.println("Failed to addrank to " + name + "looks like a mysql issue!");
            e.printStackTrace();
        }
        this.reSendPermissions(Bukkit.getPlayer(name));
    }

    public Mute getMute() {
        Mute high = null;
        if (!this.muteList.isEmpty()) {
            long now = System.currentTimeMillis();

            for(Iterator<Mute> it = muteList.iterator();it.hasNext();) {
                Mute mute = it.next();
                long timeLeft = mute.getTimeLeft(now);
                if(timeLeft < 0){
                    it.remove();
                    continue;
                }
                if (high == null || timeLeft > high.getTimeLeft(now)) {
                    high = mute;
                }
            }
        }
        return high;
    }

    public List<Mute> getMuteList(){
        return muteList;
    }

    public List<Ban> getBanList(){
        return banList;
    }

    public void ban(BanType type, String sender, String note) {
        Ban.ban(this, type, sender, note);
    }

    public void unban() {
        Ban.unban(this);
    }

    public boolean isBanned() {
        if (!banList.isEmpty()) {
            for (Ban ban : banList) {
                if (!ban.hasExpired()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Ban getBan() {
        Ban high = null;
        if (!this.banList.isEmpty()) {
            long now = System.currentTimeMillis();

            for (Iterator<Ban> it = banList.iterator(); it.hasNext();) {
                Ban ban = it.next();
                long timeLeft = ban.getTimeLeft(now);
                if(timeLeft < 0){
                    it.remove();continue;
                }
                if (high == null || timeLeft > high.getTimeLeft(now)) {
                    high = ban;
                }
            }
        }
        return high;
    }

    public boolean hasTransaction(String itemname) {
        for (Transaction ts : this.transactions) {
            if (ts.getItem().equalsIgnoreCase(itemname) && !ts.isExpired()) {
                return true;
            }
        }
        return false;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void addTransaction(String itemname, int days) {
        try {
            ResultSet res;
            res = plugin.getStatement().executeQuery("SELECT * FROM " + plugin.table + " WHERE Name = '" + name + "'");
            res.next();
            String rks = res.getString(ColumnType.LastTransactions.getName());
            if (!rks.isEmpty()) {
                rks = rks + ";" + itemname + ":" + System.currentTimeMillis() + "," + (System.currentTimeMillis() + (86400000L * days));
            } else {
                rks = itemname + ":" + System.currentTimeMillis() + "," + (System.currentTimeMillis() + (86400000L * days));
            }

//reload transactions for player
            this.transactions.clear();
            this.parseTransactions(rks);

//update transactions
            String pay = "UPDATE " + plugin.table + " SET " + ColumnType.LastTransactions.getName() + " ='" + rks + "' WHERE Name = '" + name + "'";
            plugin.getStatement().execute(pay);
        } catch (Exception e) {
            System.out.println("Failed to addtransaction to " + name + "looks like a mysql issue!");
            e.printStackTrace();
        }
    }

    //Transaction format:
// <item>:datestart,datestop;item:datestart,datestop
    private void parseTransactions(String str) {
        if (str == null || str.isEmpty()) {
            return;
        }

        if (str.contains(";")) {
            String[] s = str.split(";");
            for (String s2 : s) {
                String[] s3 = s2.split(":");

                String itemname = s3[0];
                String[] date = s3[1].split(",");
                Long startTime = Long.parseLong(date[0]);
                Long stopTime = Long.parseLong(date[1]);
                boolean expired = true;
                if (System.currentTimeMillis() < stopTime) {
                    expired = false;
                }

                this.transactions.add(new Transaction(itemname, startTime, stopTime));
            }
        } else {
            String s2 = str;
            String[] s3 = s2.split(":");

            String itemname = s3[0];
            String[] date = s3[1].split(",");
            Long startTime = Long.parseLong(date[0]);
            Long stopTime = Long.parseLong(date[1]);
            boolean expired = true;
            if (System.currentTimeMillis() < stopTime) {
                expired = false;
            }

            this.transactions.add(new Transaction(itemname, startTime, stopTime));

        }

    }

    private void parseSingleRank(String rankString) {
        Rank rank = Rank.fromString(rankString);
        if(rank != null){
            rankList.add(rank);
        }
    }

    private void parseRanks(String str) {
        if (str.length() > 5) {
            if (str.contains(";")) {
                String[] rks = str.split(";");
                for (String rk : rks) {
                    parseSingleRank(rk);
                }
            } else {
                parseSingleRank(str);
            }
        }

    }

    public void reSendPermissions(Player player) {
        if (player != null) {
            currentPlayer = player;
            try {
                PermissionAttachment attachmentold = Variable.Attachments.get(player.getName());
                if (attachmentold != null) {
                    player.removeAttachment(attachmentold);
                }
            } catch (Exception e) {
            }

            PermissionAttachment attachment = player.addAttachment(plugin.getPlugin());
            Rank rank = getRank();

            for (String s : rank.getType().getPermissions()) {
                attachment.setRawPermission(s, true);
            }
            for (String s : rank.getType().getNegativePermissions()) {
                attachment.setRawPermission(s, false);
            }
            attachment.getPermissible().recalculatePermissions();

            Variable.Attachments.put(player.getName(), attachment);
        }
    }

    public synchronized Rank getRank() {
        return Rank.getCurrentRank(getRankList());
    }

    public synchronized List<Rank> getRankList() {
        return this.rankList;
    }

    public synchronized double getDiamonds() {
        return this.Diamonds;
    }

    public synchronized boolean hasDiamonds(double amount) {
        return this.Diamonds >= amount;
    }

    public synchronized void addDiamonds(double diamonds) {
        this.Diamonds += diamonds;
        changed = true;
    }

    public synchronized void removeDiamonds(double diamonds) {
        this.Diamonds -= diamonds;
        changed = true;
    }

    public synchronized EconomyResponse withdrawDiamonds(double diamonds) {
        this.Diamonds -= diamonds;
        changed = true;
        return new EconomyResponse(diamonds, Diamonds, EconomyResponse.ResponseType.SUCCESS, Code.Color("&aTransferencia de diamantes Exitosa!"));
    }

    public synchronized EconomyResponse depositDiamonds(double diamonds) {
        this.Diamonds += diamonds;
        changed = true;
        return new EconomyResponse(diamonds, Diamonds, EconomyResponse.ResponseType.SUCCESS, Code.Color("&aTransferencia de diamantes Exitosa!"));
    }

    public String getPing() {
        String sp = "";
        if (currentPlayer != null) {
            int ping = ((CraftPlayer) currentPlayer).getHandle().ping;
            if (ping > 999 || ping < 0) {
                sp = Code.Color("&7Calculando...");
            } else if (ping < 200) {
                sp = Code.Color("&a" + ping + " &7ms ping");
            } else if (ping > 199 && ping < 400) {
                sp = Code.Color("&e" + ping + " &7ms ping");
            } else if (ping > 399) {
                sp = Code.Color("&4" + ping + " &7ms ping");
            }
        }
        return sp;
    }

    public String getName() {
        return this.name;
    }

    public boolean canSave() {
        if (preventsave) {
            return false;
        }
        return changed;
    }

    public String getSaveQuery() {
        String query = "UPDATE " + plugin.table + " SET " +
                ColumnType.Diamonds.getName() + " ='" + this.Diamonds + "'" +
                " WHERE Name = '" + name + "'";
        changed = false;
        return query;
    }



}


