import java.util.Iterator;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// $FF: renamed from: bL
public class class_49 {

    private static int Ξ;

    public static boolean Ξ(Player player) {
        return class_39.method_168(player, "detection") == class_49.field_82;
    }

    public static boolean Π(Player player) {
        return !class_39.method_169(player, "detection");
    }

    public static void Ξ(Player player) {
        class_39.method_170(player, "detection", class_49.field_82);
    }

    public static void Ξ(CommandSender commandsender, Player player, String s) {
        if (player != null && s != null) {
            String s1 = class_125.method_599("warning_message");

            s1 = class_79.method_442(player, s1, (Enums.HackType) null);
            s1 = s1.replace("{reason}", s);
            player.sendMessage(s1);
            if (commandsender != null) {
                String s2 = class_125.method_599("warning_feedback_message");

                s2 = class_79.method_442(player, s2, (Enums.HackType) null);
                s2 = s2.replace("{reason}", s);
                commandsender.sendMessage(s2);
            }

        }
    }

    public static void Ξ(Player player, String s) {
        if (player != null && s != null) {
            String s1 = Register.field_249.getDescription().getVersion();
            String s2 = class_125.method_599("kick_reason");

            s2 = s2.replace("{reason}", s);
            s2 = class_79.method_442(player, s2, (Enums.HackType) null);
            String s3 = class_125.method_599("kick_broadcast_message");

            s3 = s3.replace("{reason}", s);
            s3 = class_79.method_442(player, s3, (Enums.HackType) null);
            if (class_128.method_610("broadcast_on_kick")) {
                Bukkit.broadcastMessage(s3);
            } else {
                Iterator iterator = Bukkit.getOnlinePlayers().iterator();

                while (iterator.hasNext()) {
                    Player player1 = (Player) iterator.next();

                    if (class_47.method_229(player1, Enums.Permission.kick_message)) {
                        player1.sendMessage(s3);
                    }
                }
            }

            class_43.method_195("[Spartan " + s1 + "] " + player.getName() + " was kicked for " + s, false);
            player.kickPlayer(s2);
        }
    }

    public static void Ξ(Player player, Enums.HackType enums_hacktype, String s, boolean flag) {
        if (player != null && enums_hacktype != null) {
            method_248(player);
            String s1 = String.valueOf(class_97.method_502());
            String s2 = Register.field_249.getDescription().getVersion();
            String s3 = class_101.method_512().toString().substring(1).replace("_", ".");

            if (s == null) {
                s = "None";
            }

            if (s1.length() > 5) {
                s1 = s1.substring(0, 5);
            }

            if (flag && class_38.method_154(player, enums_hacktype) < class_38.method_150()) {
                class_43.method_195("[Spartan " + s2 + "] " + player.getName() + " failed " + enums_hacktype.toString() + " (VL: " + class_38.method_154(player, enums_hacktype) + ") [(Version: " + s3 + "), (Silent: " + class_123.method_587(enums_hacktype) + "), (Ping: " + class_87.method_472(player) + "ms), (TPS: " + s1 + "), (" + s + ")]", true);
            }

            if (class_38.method_154(player, enums_hacktype) % class_123.method_585(enums_hacktype) == 0) {
                String s4 = class_125.method_599("verbose_message");

                s4 = s4.replace("{info}", s);
                s4 = class_79.method_442(player, s4, enums_hacktype);
                if (class_128.method_610("public_verbose")) {
                    Player[] aplayer = class_51.method_270();
                    int i = aplayer.length;

                    for (int j = 0; j < i; ++j) {
                        Player player1 = aplayer[j];

                        player1.sendMessage(s4);
                    }
                } else if (class_51.method_274(player)) {
                    player.sendMessage(s4);
                }
            }

        }
    }

    public static void Ξ(Player player, String s, int i) {
        if (player != null && s != null) {
            Iterator iterator = Bukkit.getOnlinePlayers().iterator();

            while (iterator.hasNext()) {
                Player player1 = (Player) iterator.next();

                if (class_126.method_600(player1)) {
                    int j = player.getLocation().getBlockX();
                    int k = player.getLocation().getBlockY();
                    int l = player.getLocation().getBlockZ();

                    player1.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " found " + ChatColor.DARK_RED + i + " " + s + " \u00a77on \u00a78" + j + "\u00a77, \u00a78" + k + "\u00a77, \u00a78" + l);
                    class_43.method_195(player.getName() + " found " + i + " " + s + " on " + j + ", " + k + ", " + l, false);
                }
            }

        }
    }

    static {
        class_49.field_82 = 2;
    }
}
