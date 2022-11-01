import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// $FF: renamed from: k
public class class_128 implements Listener {

    private static File Ξ;
    private static HashMap<String, Boolean> Ξ;
    private static HashMap<String, Integer> Π;

    public static void Ξ() {
        class_128.field_215.clear();
        class_128.field_216.clear();
        class_79.method_444(class_128.field_214, "broadcast_on_kick", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "broadcast_on_ban", Boolean.valueOf(false));
        class_79.method_444(class_128.field_214, "violations_reset_message", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "op_bypass", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "log_console", Boolean.valueOf(false));
        class_79.method_444(class_128.field_214, "log_mysql", Boolean.valueOf(false));
        class_79.method_444(class_128.field_214, "log_file", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "enable_permissions", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "vl_reset_time", Integer.valueOf(60));
        class_79.method_444(class_128.field_214, "public_verbose", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "enable_verbose_on_login", Boolean.valueOf(false));
        class_79.method_444(class_128.field_214, "fall_damage_on_teleport", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "chat_cooldown", Integer.valueOf(0));
        class_79.method_444(class_128.field_214, "command_cooldown", Integer.valueOf(0));
        class_79.method_444(class_128.field_214, "reconnect_cooldown", Integer.valueOf(5));
        class_79.method_444(class_128.field_214, "enable_mining_notifications_on_login", Boolean.valueOf(false));
        class_79.method_444(class_128.field_214, "wave_broadcast_message", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "run_wave_if_its_full", Boolean.valueOf(false));
        class_79.method_444(class_128.field_214, "staff_chat", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "use_tps_protection", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "use_teleport_protection", Boolean.valueOf(true));
        class_79.method_444(class_128.field_214, "cache_permissions", Boolean.valueOf(false));
    }

    public static boolean Ξ(String s) {
        if (s == null) {
            return false;
        } else if (class_128.field_215.containsKey(s)) {
            return ((Boolean) class_128.field_215.get(s)).booleanValue();
        } else {
            if (!class_128.field_214.exists()) {
                method_609();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_128.field_214);

            if (yamlconfiguration == null) {
                return false;
            } else {
                boolean flag = yamlconfiguration.getBoolean(s);

                class_128.field_215.put(s, Boolean.valueOf(flag));
                return flag;
            }
        }
    }

    public static int Ξ(String s) {
        if (s == null) {
            return 0;
        } else if (class_128.field_216.containsKey(s)) {
            return ((Integer) class_128.field_216.get(s)).intValue();
        } else {
            if (!class_128.field_214.exists()) {
                method_609();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_128.field_214);

            if (yamlconfiguration == null) {
                return 0;
            } else {
                int i = yamlconfiguration.getInt(s);

                class_128.field_216.put(s, Integer.valueOf(i));
                return i;
            }
        }
    }

    public static int Ξ() {
        String s = "vl_reset_time";
        int i = method_611(s);

        if (i < 60) {
            i = 60;
        } else if (i > 3600) {
            i = 3600;
        }

        class_128.field_216.put(s, Integer.valueOf(i));
        return i;
    }

    public static void Ξ(Player player) {
        if (method_610("enable_verbose_on_login") && class_47.method_229(player, Enums.Permission.verbose)) {
            class_51.method_273(player, true, 0);
        }

        if (method_610("enable_mining_notifications_on_login") && class_47.method_229(player, Enums.Permission.mining)) {
            class_126.method_602(player, true);
        }

    }

    public static void Π() {
        Iterator iterator = Bukkit.getOnlinePlayers().iterator();

        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();

            method_613(player);
        }

    }

    @EventHandler
    private void Ξ(PlayerJoinEvent playerjoinevent) {
        Player player = playerjoinevent.getPlayer();

        method_613(player);
    }

    static {
        class_128.field_214 = new File("plugins/Spartan/settings.yml");
        class_128.field_215 = new HashMap();
        class_128.field_216 = new HashMap();
    }
}
