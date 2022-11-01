import java.io.File;
import java.util.Iterator;
import java.util.UUID;
import me.vagdedes.spartan.system.Enums;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

// $FF: renamed from: m
public class class_130 {

    private static File Ξ;

    public static void Ξ() {
        if (!class_130.field_217.exists()) {
            String s = UUID.randomUUID().toString();

            class_79.method_444(class_130.field_217, s + ".command", "ban {player} wave punishment example");
        }

    }

    public static void Ξ(UUID uuid, String s) {
        if (uuid != null && s != null) {
            if (method_622() >= 100) {
                if (class_128.method_610("run_wave_if_its_full")) {
                    HHΞ();
                }

            } else {
                method_617();
                String s1 = uuid.toString();

                class_79.method_445(class_130.field_217, s1 + ".command", s);
            }
        }
    }

    public static void Ξ(UUID uuid) {
        if (uuid != null && method_622() != 0 && class_130.field_217.exists()) {
            String s = uuid.toString();

            class_79.method_445(class_130.field_217, s + ".command", (Object) null);
            class_79.method_445(class_130.field_217, s, (Object) null);
        }
    }

    public static void Π() {
        int i = 0;
        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_130.field_217);

        if (yamlconfiguration != null) {
            Iterator iterator = yamlconfiguration.getKeys(true).iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                if (s.length() == 36 && StringUtils.countMatches(s, "-") == 4) {
                    ++i;
                    UUID uuid = UUID.fromString(s);

                    method_619(uuid);
                    if (i >= 100) {
                        break;
                    }
                }
            }
        }

    }

    public static boolean Ξ(UUID uuid) {
        String s = uuid.toString();
        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_130.field_217);

        return yamlconfiguration != null && yamlconfiguration.getString(s) != null && yamlconfiguration.getString(s + ".command") != null;
    }

    public static void HHΞ() {
        boolean flag = class_128.method_610("wave_broadcast_message");

        if (flag) {
            Bukkit.broadcastMessage(class_125.method_599("wave_start_message"));
        }

        int i = 0;
        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_130.field_217);

        if (yamlconfiguration != null) {
            Iterator iterator = yamlconfiguration.getKeys(true).iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                if (s.length() == 36 && StringUtils.countMatches(s, "-") == 4) {
                    ++i;
                    UUID uuid = UUID.fromString(s);

                    try {
                        OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(uuid);
                        String s1 = yamlconfiguration.getString(s + ".command");

                        if (s1 != null) {
                            s1 = class_79.method_442(offlineplayer, s1, (Enums.HackType) null);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s1);
                        }
                    } catch (Exception exception) {
                        ;
                    }

                    method_619(uuid);
                    if (i >= 100) {
                        break;
                    }
                }
            }
        }

        if (flag) {
            String s2 = class_125.method_599("wave_end_message");

            s2 = s2.replace("{total}", String.valueOf(i));
            Bukkit.broadcastMessage(s2);
        }

    }

    public static int Ξ() {
        int i = 0;
        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_130.field_217);

        if (yamlconfiguration != null) {
            Iterator iterator = yamlconfiguration.getKeys(true).iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                if (s.length() == 36 && StringUtils.countMatches(s, "-") == 4) {
                    ++i;
                }
            }
        }

        return i;
    }

    static {
        class_130.field_217 = new File("plugins/Spartan/wave.yml");
    }
}
