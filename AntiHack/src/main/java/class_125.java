import java.io.File;
import java.util.HashMap;
import org.bukkit.configuration.file.YamlConfiguration;

// $FF: renamed from: h
public class class_125 {

    private static File Ξ;
    private static HashMap<String, String> Ξ;

    public static void Ξ() {
        class_125.field_210.clear();
        class_79.method_444(class_125.field_209, "verbose_enable", "&8[&2Spartan&8] &aYou enabled verbose.");
        class_79.method_444(class_125.field_209, "verbose_disable", "&8[&2Spartan&8] &cYou disabled verbose.");
        class_79.method_444(class_125.field_209, "violations_reset", "&8[&2Spartan&8] &cVLs &7have been reset&8!");
        class_79.method_444(class_125.field_209, "config_reload", "&8[&2Spartan&8]&e Config successfully reloaded.");
        class_79.method_444(class_125.field_209, "kick_reason", "&8[&2Spartan&8]&c {reason}");
        class_79.method_444(class_125.field_209, "kick_broadcast_message", "&8[&2Spartan&8]&c {player}&7 was kicked for&4 {reason}");
        class_79.method_444(class_125.field_209, "command_no_access", "&cYou do not have access to this command.");
        class_79.method_444(class_125.field_209, "menu_gui_no_access", "&cYou don\'t have permission to do this.");
        class_79.method_444(class_125.field_209, "ping_command_message", "&2{player}&7\'s latency&8:&a {ping}ms");
        class_79.method_444(class_125.field_209, "player_not_found_message", "&cPlayer not found.");
        class_79.method_444(class_125.field_209, "non_console_command", "&cThis command can only be used by a player.");
        class_79.method_444(class_125.field_209, "player_violation_reset_message", "&aViolations successfully reset for player&8: &2{player}");
        class_79.method_444(class_125.field_209, "blocked_command_message", "&cYou are not allowed to dispatch that command.");
        class_79.method_444(class_125.field_209, "blocked_word_message", "&cYou are not allowed to type that.");
        class_79.method_444(class_125.field_209, "verbose_message", "&8[&2Spartan&8] &c{player} failed {detection} (VL: {vls:detection}) &8[&7(&fVersion: {server:version}&7)&8, &7(&fSilent: {silent:detection}&7)&8, (&fPing: {ping}ms&7)&8, &7(&fTPS: {tps}&7)&8, &7(&f{info}&7)&8]");
        class_79.method_444(class_125.field_209, "non_existing_check", "&8[&2Spartan&8] &cThis check doesn\'t exist.");
        class_79.method_444(class_125.field_209, "massive_command_reason", "&cThe length of the reason is too big.");
        class_79.method_444(class_125.field_209, "check_enable_message", "&8[&2Spartan&8] &aYou enabled the check&8:&7 {detection}");
        class_79.method_444(class_125.field_209, "check_disable_message", "&8[&2Spartan&8] &cYou disabled the check&8:&7 {detection}");
        class_79.method_444(class_125.field_209, "warning_message", "&8[&2Spartan&8]&c {reason}");
        class_79.method_444(class_125.field_209, "warning_feedback_message", "&8[&2Spartan&8]&7 You warned &c{player} &7for&8: &4{reason}");
        class_79.method_444(class_125.field_209, "bypass_message", "&8[&2Spartan&8] &c{player} &7is now bypassing the &4{detection} &7check for &e{time} &7seconds.");
        class_79.method_444(class_125.field_209, "ban_message", "&7You banned &c{player} &7for &4{reason}");
        class_79.method_444(class_125.field_209, "unban_message", "&7You unbanned &c{player}");
        class_79.method_444(class_125.field_209, "player_not_banned", "&cThis player is not banned.");
        class_79.method_444(class_125.field_209, "ban_broadcast_message", "&8[&2Spartan&8]&c {player}&7 was banned for&4 {reason}");
        class_79.method_444(class_125.field_209, "ban_reason", "&8[&2Spartan&8]&c {reason}");
        class_79.method_444(class_125.field_209, "chat_cooldown_message", "&cPlease wait {time} second(s) until typing again.");
        class_79.method_444(class_125.field_209, "mining_notifications_enable", "&8[&2Spartan&8] &aYou enabled mining notifications.");
        class_79.method_444(class_125.field_209, "mining_notifications_disable", "&8[&2Spartan&8] &cYou disabled mining notifications.");
        class_79.method_444(class_125.field_209, "reconnect_kick_message", "&8[&2Spartan&8]&c Please wait a few seconds before logging in back.");
        class_79.method_444(class_125.field_209, "empty_ban_list", "&cThere are currently no banned players.");
        class_79.method_444(class_125.field_209, "command_cooldown_message", "&cPlease wait {time} second(s) until dispatching a command again.");
        class_79.method_444(class_125.field_209, "wave_start_message", "&8[&2Spartan&8]&c The wave is starting.");
        class_79.method_444(class_125.field_209, "wave_end_message", "&8[&2Spartan&8]&c The wave has ended with a total of {total} action(s).");
        class_79.method_444(class_125.field_209, "wave_clear_message", "&8[&2Spartan&8]&c The wave was cleared.");
        class_79.method_444(class_125.field_209, "wave_add_message", "&8[&2Spartan&8]&a {player} was added to the wave.");
        class_79.method_444(class_125.field_209, "wave_remove_message", "&8[&2Spartan&8]&c {player} was removed from the wave.");
        class_79.method_444(class_125.field_209, "full_wave_list", "&8[&2Spartan&8]&c The wave list is full.");
        class_79.method_444(class_125.field_209, "empty_wave_list", "&8[&2Spartan&8]&c The wave list is empty.");
        class_79.method_444(class_125.field_209, "wave_not_added_message", "&8[&2Spartan&8]&c {player} is not added to the wave.");
        class_79.method_444(class_125.field_209, "staff_chat_message", "&8[&4Staff Chat&8]&c {player} &e{message}");
        class_79.method_444(class_125.field_209, "staff_chat_character", "@");
    }

    public static String Ξ(String s) {
        if (s == null) {
            return "None";
        } else if (class_125.field_210.containsKey(s)) {
            return (String) class_125.field_210.get(s);
        } else {
            if (!class_125.field_209.exists()) {
                method_598();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_125.field_209);

            if (yamlconfiguration == null) {
                return s;
            } else {
                String s1 = yamlconfiguration.getString(s);

                if (s1 == null) {
                    return s;
                } else {
                    s1 = s1.replaceAll("&", "\u00a7");
                    class_125.field_210.put(s, s1);
                    return s1;
                }
            }
        }
    }

    static {
        class_125.field_209 = new File("plugins/Spartan/language.yml");
        class_125.field_210 = new HashMap();
    }
}
