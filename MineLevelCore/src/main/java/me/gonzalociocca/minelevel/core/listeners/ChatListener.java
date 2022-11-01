package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.mute.Mute;
import me.gonzalociocca.minelevel.core.user.rank.Rank;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noname on 8/2/2017.
 */
public class ChatListener implements Listener {
    Main plugin;
    public ChatListener(Main main){
        plugin = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(PlayerChatEvent event) {
        PlayerData pd = plugin.getDB().getPlayerData(event.getPlayer().getName());
        Mute mute = pd.getMute();

        if(mute != null){
            event.getPlayer().sendMessage(mute.getMessage());
            event.setCancelled(true);
            return;
        }
        Rank rk = pd.getRank();

        String msgtype = "";

        if (!event.getMessage().startsWith("!")) {
            List<Player> toremove = new ArrayList();
            for (Player p : event.getRecipients()) {
                if (p.getWorld() != event.getPlayer().getWorld()) {
                    toremove.add(p);
                }
            }
            for (Player p : toremove) {
                event.getRecipients().remove(p);
            }

        } else if (plugin.getDB().getPlayerData(event.getPlayer().getName()).getRank().getType().isAtLeast(RankType.VIP)) {
            msgtype = Code.Color("&7(&b&l!&7)&r");
        } else if (plugin.getDB().getPlayerData(event.getPlayer().getName()).getDiamonds() > 4) {
            plugin.getDB().getPlayerData(event.getPlayer().getName()).removeDiamonds(5);
            msgtype = Code.Color("&7(&b&l!&7)&r");
            event.getPlayer().sendMessage(Code.Color("&7[&cMineLevel&7]&a Usaste 5 diamantes para enviar el mensaje"));
        } else {
            event.getPlayer().sendMessage(Code.Color("&c7[&cMineLevel&7]&a Diamantes insuficientes para hablar por global, necesitas 5"));
            event.setCancelled(true);
            return;
        }

        if (rk.getType().isAtLeast(RankType.VIP)) {
            event.setFormat(msgtype + "" + rk.getType().getChatPrefix() + " " + event.getPlayer().getName() + Code.Color("&6: " + event.getMessage()));
        } else {
            try {
                event.setFormat(msgtype + rk.getType().getChatPrefix() + event.getPlayer().getName() + Code.Color(": &7") + event.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        PlayerData pd = plugin.getDB().getPlayerData(event.getPlayer().getName());
        RankType rankType = pd.getRank().getType();
        String listName;

        if(rankType.isAtLeast(RankType.Helper)){
            Variable.staffList.add(event.getPlayer());
        }

        if(pd.isSilent()){
            listName = rankType.getScoreboardPrefix() + event.getPlayer().getName();
            Variable.silentTabListMap.put(event.getPlayer().getName(), listName);
            listName = ""+Code.getRandom().nextInt(50000000);
        }else{
            listName = rankType.getScoreboardPrefix() + event.getPlayer().getName();
            Variable.tabListMap.put(event.getPlayer().getName(), listName);
        }
        event.getPlayer().setPlayerListName(listName);

        if (rankType.isAtLeast(RankType.VIP)) {

            long now = System.currentTimeMillis();

            if(!pd.isSilent()) {
                String message = Code.Color("&a[+] &a" + rankType.getChatPrefix() + " " + event.getPlayer().getName());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(message);
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                }
            }

            for(Rank rk : pd.getRankList()){
                int days = rk.getDaysLeft(now);
                int hours = rk.getHoursLeft(now);
                int minutes = rk.getMinutesLeft(now);
                event.getPlayer().sendMessage(Code.Color("&7[&cMineLevel&7] &eLe quedan "
                        + days + " dias, "
                        + hours + " horas, "
                        + minutes + " minutos "
                        + "a tu rango " + rk.getType().getChatPrefix()));
            }
        }

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        PlayerData pd = plugin.getDB().getPlayerData(event.getPlayer().getName());
        RankType rankType = pd.getRank().getType();

        if (!pd.isSilent() && !pd.isBanned() && rankType.isAtLeast(RankType.VIP)) {
            Bukkit.broadcastMessage(Code.Color("&c[-] &a" + pd.getRank().getType().getChatPrefix() + " " + event.getPlayer().getName()));
        }
        if(rankType.isAtLeast(RankType.Helper)){
            Variable.staffList.remove(event.getPlayer());
        }
        Variable.tabListMap.remove(event.getPlayer().getName());
        Variable.silentTabListMap.remove(event.getPlayer().getName());
        event.setQuitMessage(null);
    }
}
