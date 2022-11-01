package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.UpdateType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.ban.Ban;
import me.gonzalociocca.minelevel.core.user.mute.Mute;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModCommand {

    public static void handleVanish(Main plugin, boolean isConsole, PlayerData pd, CommandSender sender, String[] args) {
        if (!isConsole && pd != null) {
            RankType rankType = pd.getRank().getType();
            if (sender instanceof Player && rankType.isAtLeast(RankType.Builder)) {
                Player p = (Player) sender;
                if (p.getGameMode() == GameMode.SPECTATOR) {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(Code.Color("&aVanish Desactivado"));
                } else {
                    p.setAllowFlight(true);
                    p.setGameMode(GameMode.SPECTATOR);
                    p.sendMessage(Code.Color("&aVanish Activado"));
                }
            } else {
                sender.sendMessage(Code.Color("&eEste comando solo es para staffs."));
            }
        } else {
            sender.sendMessage(Code.Color("&cEste comando solo se puede usar siendo un jugador"));
        }
    }

    public static void handleList(Main plugin, boolean isConsole, PlayerData pd, CommandSender sender, String[] args) {

        String largeString = "";

        for (String str : Variable.tabListMap.values()) {
            boolean isEmpty = largeString.isEmpty();
            largeString = isEmpty ? str : largeString + ", " + str;
            if (!isEmpty && largeString.length() > 48) {
                sender.sendMessage(largeString);
                largeString = "";
            }
        }

        if (isConsole || pd != null && pd.getRank().getType().isAtLeast(RankType.Builder)) {
            for (String str : Variable.silentTabListMap.values()) {
                boolean isEmpty = largeString.isEmpty();
                largeString = isEmpty ? str : largeString + ", " + str;
                if (!isEmpty && largeString.length() > 48) {
                    sender.sendMessage(largeString);
                    largeString = "";
                }
            }
        }

        if (!largeString.isEmpty()) {
            sender.sendMessage(largeString);
        }
    }

    public void handleReport(PlayerData pd, CommandSender sender, String[] args) {

    }

    public static void handleKick(Main plugin, boolean isConsole, PlayerData pd, CommandSender sender, String[] args) {

        if (isConsole || pd != null && pd.getRank().getType().isAtLeast(RankType.Builder)) {
            if (args.length >= 1) {
                String name = args[0];
                String message = "";
                if (args.length > 1) {
                    for (int a = 1; a < args.length; a++) {
                        message = message.isEmpty() ? args[a] : message + " " + args[a];
                    }
                }
                Player player = Bukkit.getPlayer(name);
                if (player != null) {
                    String kickMessage = "&a" + player.getName() + " ha sido kickeado por " + sender.getName();
                    if (!message.isEmpty()) {
                        kickMessage = kickMessage + "&e Nota: " + message;
                    }
                    kickMessage = Code.Color(kickMessage);

                    player.kickPlayer(kickMessage);
                    Bukkit.broadcastMessage(kickMessage);

                } else {
                    sender.sendMessage(Code.Color("&cJugador no encontrado."));
                }
            } else {
                sender.sendMessage(Code.Color("&aUso: /kick <jugador> <mensaje>"));
            }
        } else {
            sender.sendMessage(Code.Color("&eEste comando solo es para staffs."));
        }
    }

    public static void handleTeleport(Main plugin, boolean isConsole, PlayerData pd, CommandSender sender, String[] args) {
        RankType rankType = isConsole ? RankType.OWNER : pd.getRank().getType();
        if (rankType.isAtLeast(RankType.Builder)) {
            if (sender instanceof Player && args.length == 1) {
                Player to = Bukkit.getPlayer(args[0]);

                if (to == null) {
                    sender.sendMessage(Code.Color("&cJugador no encontrado"));
                } else {
                    Player p = (Player) sender;
                    p.sendMessage(Code.Color("&aTeletransportando"));
                    p.teleport(to);
                }
            } else if (args.length == 2) {
                sender.sendMessage(Code.Color("&cEste comando solo se puede usar siendo un jugador"));
            } else {
                sender.sendMessage(Code.Color("&aUso: /tp <jugador>"));
            }
        } else {
            sender.sendMessage(Code.Color("&eEste comando solo es para staffs."));
        }
    }

    public static void handleNear(Main plugin, boolean isConsole, PlayerData pd, CommandSender sender, String[] args) {
        if(isConsole){
            sender.sendMessage(Code.Color("&cEste comando solo puede ser usado por jugadores."));
            return;
        }
        RankType rankType = pd.getRank().getType();
        if (rankType.isAtLeast(RankType.VIP) && sender instanceof Player) {

            Player player = (Player)sender;

            if (player == null) {
                sender.sendMessage(Code.Color("&4Error"));
                return;
            }
            Location myLoc = player.getLocation();

            Map<String, Integer> distancePlayers = new UnifiedMap();
            for (Player other : player.getWorld().getPlayers()) {
                int distance = (int) other.getLocation().distance(myLoc);
                if (distance < 1024) {
                    PlayerData otherpd = plugin.getDB().getPlayerData(other.getName());
                    if (!otherpd.isSilent()) {
                        distancePlayers.put(other.getPlayerListName(), distance);
                    }
                }
            }

            if (!distancePlayers.isEmpty()) {
                String largeString = "";
                for (Map.Entry<String, Integer> entry : distancePlayers.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    String distanceValue = "&f(&6" + value + "m" + "&f)";
                    largeString = largeString.isEmpty() ? key + distanceValue : largeString + ", " + key + distanceValue;
                    if (largeString.length() > 48) {
                        sender.sendMessage(Code.Color(largeString));
                        largeString = "";
                    }
                }
                if (!largeString.isEmpty()) {
                    sender.sendMessage(Code.Color(largeString));
                }
            } else {
                sender.sendMessage(Code.Color("&4No se han encontrado jugadores cerca."));
            }


        } else {
            sender.sendMessage(Code.Color("&aEste comando solo es para vips."));
        }
    }


    public class UserReport {

        Player _victim;
        String _reported;
        String _comment;
        String[] _messageArray;
        long reportTime = System.currentTimeMillis();

        public UserReport(Player victim, String reported, String comment) {
            _victim = victim;
            _reported = reported;
            _comment = comment;

            _messageArray = new String[3];
            _messageArray[0] = Code.Color("&a" + _victim.getName() + "&f reporto a &c" + _reported + " &fpor");
            _messageArray[1] = Code.Color("&e" + _comment);
        }

        public void sendReportInfo(CommandSender sender, long now) {
            sender.sendMessage(_messageArray[0]);
            sender.sendMessage(_messageArray[1] + " &9Hace " + getMinutes(now) + " minutos.");
            String extra = _messageArray[2];
            if (extra != null && !extra.isEmpty()) {
                sender.sendMessage(extra);
            }
        }

        private int getMinutes(long now) {
            return (int) ((now - reportTime) / 60000L);
        }

        public String[] getMessageArray() {
            return _messageArray;
        }

        public Player getVictim() {
            return _victim;
        }

        public String getReported() {
            return _reported;
        }

        public String getComment() {
            return _comment;
        }

    }

    HashMap<String, UserReport> reportMap = new HashMap();

    public void handleReport(Main plugin, boolean isConsole, PlayerData pd, CommandSender sender, String[] args) {
        if (args.length >= 2 && sender instanceof Player) {
            String reported = args[0];
            String comments = "";
            for (int a = 1; a < args.length; a++) {
                comments = comments.isEmpty() ? args[a] : " " + args[a];
            }
            if (reportMap.get(sender.getName()) != null) {
                sender.sendMessage(Code.Color("&aTu reporte anterior ha sido borrado, solo puedes enviar 1 reporte a la ves."));
            }
            reportMap.put(sender.getName(), new UserReport((Player) sender, reported, comments));
            sender.sendMessage(Code.Color("&aTu reporte ha sido enviado, no te disconectes."));
        } else {
            sender.sendMessage(Code.Color("&eUsado incorrectamente, usa /report <nombre> <razon>"));
        }
    }

    public void onReportUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.SLOW2) {
            long now = System.currentTimeMillis();

            if (!reportMap.isEmpty()) {
                for (Iterator<UserReport> it = reportMap.values().iterator(); it.hasNext(); ) {
                    UserReport nextReport = it.next();
                    if (!nextReport.getVictim().isOnline()) {
                        it.remove();
                        continue;
                    }
                    if (nextReport.getMinutes(now) > 30) {
                        nextReport.getVictim().sendMessage(Code.Color("&f&lTu reporte ha expirado, ve al foro si quieres reportar."));
                        nextReport.getVictim().sendMessage(Code.Color("&f&lwww.minelevel.com"));
                        it.remove();
                        continue;
                    }
                    for (Player player : Variable.staffList) {
                        if (player != null && player.isOnline()) {
                            nextReport.sendReportInfo(player, now);
                        }
                    }
                    break;
                }
            }
        }
    }


    public static void sendCommandInfo(CommandSender sender) {
        sender.sendMessage(Code.Color("&b&m-----&cComandos&b&m-----"));
        sender.sendMessage(Code.Color("&a/kickall <mensaje>    &c(Solo emergencias)"));
        sender.sendMessage(Code.Color("&a/whitelist on|off     &c(Solo emergencias)"));
        sender.sendMessage(Code.Color("&a/ban <jugador> <tipo> <notas>"));
        sender.sendMessage(Code.Color("&a/unban <jugador>"));
        sender.sendMessage(Code.Color("&a/mute <jugador> <minutos> <notas>"));
        sender.sendMessage(Code.Color("&a/unmute <jugador>"));
        sender.sendMessage(Code.Color("&a/vanish"));
        sender.sendMessage(Code.Color("&a/tp <jugador>"));
        sender.sendMessage(Code.Color("&a/kick <jugador> <mensaje>"));
        //sender.sendMessage(Code.Color("&a/list"));
    }

    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args) {
        boolean isConsole = sender instanceof ConsoleCommandSender;

        if (cmd.getName().equalsIgnoreCase("mod")) {
            PlayerData pd = isConsole ? null : plugin.getDB().getPlayerData(sender.getName());

            if (label.equalsIgnoreCase("vanish")) {
                handleVanish(plugin, isConsole, pd, sender, args);
            } else if (label.equalsIgnoreCase("list")) {
                handleList(plugin, isConsole, pd, sender, args);
            } else if (label.equalsIgnoreCase("near")) {
                handleNear(plugin, isConsole, pd, sender, args);
            } else if (args.length <= 0) {
                sendCommandInfo(sender);
            } else if (label.equalsIgnoreCase("ban")) {
                sender.sendMessage(Ban.handleBan(plugin, isConsole, pd, sender, args));
            } else if (label.equalsIgnoreCase("unban")) {
                sender.sendMessage(Ban.handleUnban(plugin, isConsole, pd, sender, args));
            } else if (label.equalsIgnoreCase("mute")) {
                sender.sendMessage(Mute.handleMute(plugin, isConsole, pd, sender, args));
            } else if (label.equalsIgnoreCase("unmute")) {
                sender.sendMessage(Mute.handleUnmute(plugin, isConsole, pd, sender, args));
            } else if (label.equalsIgnoreCase("kick") && sender.hasPermission("minelevel.kick") || label.equalsIgnoreCase("kick") && sender instanceof Player && plugin.getDB().getPlayerData(sender.getName()).getRank().getType().isAtLeast(RankType.Helper)) {
                handleKick(plugin, isConsole, pd, sender, args);
            } else if (label.equalsIgnoreCase("tp")) {
                handleTeleport(plugin, isConsole, pd, sender, args);
            } else {
                sendCommandInfo(sender);
            }
            return true;
        }
        return false;
    }
}
