import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.system.Enums;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

// $FF: renamed from: a
public class class_118 implements Listener {

    private static File Ξ;
    private static HashMap<String, String> Ξ;

    public static UUID[] Ξ() {
        ArrayList arraylist = new ArrayList();
        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_118.field_194);

        if (yamlconfiguration != null) {
            Iterator iterator = yamlconfiguration.getKeys(true).iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                if (s.length() == 36 && StringUtils.countMatches(s, "-") == 4) {
                    arraylist.add(UUID.fromString(s));
                }
            }
        }

        return (UUID[]) arraylist.toArray(new UUID[0]);
    }

    public static String Ξ() {
        String s = "";
        UUID[] auuid = method_556();
        int i = auuid.length;

        for (int j = 0; j < i; ++j) {
            UUID uuid = auuid[j];
            OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(uuid);

            if (offlineplayer.hasPlayedBefore()) {
                s = s + ChatColor.RED + offlineplayer.getName() + ChatColor.GRAY + ", ";
            }
        }

        if (s.length() >= 2) {
            s = s.substring(0, s.length() - 2);
        } else if (s.length() == 0) {
            s = class_125.method_599("empty_ban_list");
        }

        return s;
    }

    public static void Ξ() {
        class_118.field_195.clear();
        if (!class_118.field_194.exists()) {
            String s = UUID.randomUUID().toString();

            class_79.method_444(class_118.field_194, s + ".reason", "Hacking");
            class_79.method_444(class_118.field_194, s + ".punisher", "Vagdedes");
        }

    }

    public static void Ξ(UUID uuid, String s, String s1) {
        if (uuid != null && s != null && s1 != null) {
            if (!class_118.field_194.exists()) {
                method_558();
            }

            String s2 = uuid.toString();

            class_79.method_445(class_118.field_194, s2 + ".reason", s1);
            class_118.field_195.put(s2 + ".reason", s1);
            class_79.method_445(class_118.field_194, s2 + ".punisher", s);
            class_118.field_195.put(s2 + ".punisher", s);
            OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(uuid);
            String s3 = Register.field_249.getDescription().getVersion();
            String s4 = class_125.method_599("ban_reason");

            s4 = s4.replace("{reason}", s1);
            s4 = s4.replace("{punisher}", s);
            s4 = class_79.method_442(offlineplayer, s4, (Enums.HackType) null);
            String s5 = class_125.method_599("ban_broadcast_message");

            s5 = s5.replace("{reason}", s1);
            s5 = s5.replace("{punisher}", s);
            s5 = class_79.method_442(offlineplayer, s5, (Enums.HackType) null);
            if (class_128.method_610("broadcast_on_ban")) {
                Bukkit.broadcastMessage(s5);
            } else {
                Iterator iterator = Bukkit.getOnlinePlayers().iterator();

                while (iterator.hasNext()) {
                    Player player = (Player) iterator.next();

                    if (class_47.method_229(player, Enums.Permission.ban_message)) {
                        player.sendMessage(s5);
                    }
                }
            }

            class_43.method_195("[Spartan " + s3 + "] " + offlineplayer.getName() + " was banned for " + s1, false);
            if (offlineplayer.isOnline()) {
                ((Player) offlineplayer).kickPlayer(s4);
            }

        }
    }

    public static void Ξ(UUID uuid) {
        if (uuid != null && method_561(uuid)) {
            if (!class_118.field_194.exists()) {
                method_558();
            }

            String s = uuid.toString();

            class_79.method_445(class_118.field_194, s + ".reason", (Object) null);
            class_118.field_195.remove(s + ".reason");
            class_79.method_445(class_118.field_194, s + ".punisher", (Object) null);
            class_118.field_195.remove(s + ".punisher");
            class_79.method_445(class_118.field_194, s, (Object) null);
        }
    }

    public static boolean Ξ(UUID uuid) {
        return uuid == null ? false : method_562(uuid, "reason") != null && method_562(uuid, "punisher") != null;
    }

    public static String Ξ(UUID uuid, String s) {
        if (uuid != null && s != null) {
            String s1 = uuid.toString();

            if (class_118.field_195.containsKey(s1 + "." + s)) {
                return (String) class_118.field_195.get(s1 + "." + s);
            } else {
                if (!class_118.field_194.exists()) {
                    method_558();
                }

                YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_118.field_194);

                if (yamlconfiguration == null) {
                    return s;
                } else {
                    String s2 = yamlconfiguration.getString(s1 + "." + s);

                    if (s2 == null) {
                        class_118.field_195.remove(s1 + "." + s);
                    } else {
                        class_118.field_195.put(s1 + "." + s, s2);
                    }

                    return s2;
                }
            }
        } else {
            return null;
        }
    }

    @EventHandler
    private void Ξ(PlayerLoginEvent playerloginevent) {
        Player player = playerloginevent.getPlayer();
        UUID uuid = player.getUniqueId();

        if (method_561(uuid)) {
            String s = class_125.method_599("ban_reason");

            s = s.replace("{reason}", method_562(uuid, "reason"));
            s = s.replace("{punisher}", method_562(uuid, "punisher"));
            s = class_79.method_442(player, s, (Enums.HackType) null);
            playerloginevent.disallow(Result.KICK_OTHER, s);
        }

    }

    static {
        class_118.field_194 = new File("plugins/Spartan/bans.yml");
        class_118.field_195 = new HashMap();
    }
}
