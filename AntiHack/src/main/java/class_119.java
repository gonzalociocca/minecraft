import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import me.vagdedes.spartan.system.Enums;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

// $FF: renamed from: b
public class class_119 implements Listener {

    private static File Ξ;
    private static HashMap<String, ArrayList<String>> Ξ;

    public static void Ξ() {
        class_119.field_197.clear();
        if (!class_119.field_196.exists()) {
            class_79.method_444(class_119.field_196, "blocked_commands.1", "blocked_commands_here");
            class_79.method_444(class_119.field_196, "blocked_words.1", "blocked_words_here");
        }

    }

    private static boolean Ξ(String s, String s1, String s2) {
        return s != null && s1 != null && s2 != null ? s1.equalsIgnoreCase("blocked_words") && s.toLowerCase().contains(s2.toLowerCase()) || s1.equalsIgnoreCase("blocked_commands") && (s.equalsIgnoreCase(s2) || s.toLowerCase().startsWith(s2.toLowerCase() + " ")) : false;
    }

    public static boolean Ξ(String s, String s1) {
        if (s != null && s1 != null) {
            if (class_119.field_197.containsKey(s1)) {
                Iterator iterator = ((ArrayList) class_119.field_197.get(s1)).iterator();

                while (iterator.hasNext()) {
                    String s2 = (String) iterator.next();

                    if (s2 != null && method_565(s, s1, s2)) {
                        return true;
                    }
                }
            }

            if (!class_119.field_196.exists()) {
                method_564();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(class_119.field_196);

            if (yamlconfiguration == null) {
                return false;
            } else {
                boolean flag = false;
                ArrayList arraylist = new ArrayList();

                try {
                    Iterator iterator1 = yamlconfiguration.getConfigurationSection(s1).getKeys(true).iterator();

                    while (iterator1.hasNext()) {
                        String s3 = (String) iterator1.next();

                        if (s3 != null && StringUtils.isNumeric(s3)) {
                            String s4 = yamlconfiguration.getString(s1 + "." + s3);

                            if (s4 != null) {
                                arraylist.add(s4);
                                if (method_565(s, s1, s4)) {
                                    flag = true;
                                }
                            }
                        }
                    }

                    class_119.field_197.put(s1, arraylist);
                } catch (Exception exception) {
                    ;
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerChatEvent playerchatevent) {
        Player player = playerchatevent.getPlayer();

        if (!playerchatevent.isCancelled() && !class_47.method_229(player, Enums.Permission.chat_protection)) {
            String s = playerchatevent.getMessage();

            if (!s.startsWith("/")) {
                if (!class_39.method_169(player, "chat=cooldown=delay")) {
                    playerchatevent.setCancelled(true);
                    double d0 = Double.valueOf((double) class_39.method_168(player, "chat=cooldown=delay")).doubleValue() / 20.0D;
                    String s1 = class_125.method_599("chat_cooldown_message");

                    s1 = s1.replace("{time}", String.valueOf(d0));
                    s1 = class_79.method_442(player, s1, (Enums.HackType) null);
                    player.sendMessage(s1);
                } else {
                    int i = class_128.method_611("chat_cooldown");

                    if (i >= 1 && i <= 60) {
                        class_39.method_170(player, "chat=cooldown=delay", i * 20);
                    }

                    if (method_566(s, "blocked_words")) {
                        playerchatevent.setCancelled(true);
                        player.sendMessage(class_125.method_599("blocked_word_message"));
                    }
                }
            }

        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerCommandPreprocessEvent playercommandpreprocessevent) {
        Player player = playercommandpreprocessevent.getPlayer();

        if (!playercommandpreprocessevent.isCancelled() && !class_47.method_229(player, Enums.Permission.chat_protection)) {
            String s = playercommandpreprocessevent.getMessage();

            if (s.startsWith("/")) {
                if (!class_39.method_169(player, "command=cooldown=delay")) {
                    playercommandpreprocessevent.setCancelled(true);
                    double d0 = Double.valueOf((double) class_39.method_168(player, "command=cooldown=delay")).doubleValue() / 20.0D;
                    String s1 = class_125.method_599("command_cooldown_message");

                    s1 = s1.replace("{time}", String.valueOf(d0));
                    s1 = class_79.method_442(player, s1, (Enums.HackType) null);
                    player.sendMessage(s1);
                } else {
                    int i = class_128.method_611("command_cooldown");

                    if (i >= 1 && i <= 60) {
                        class_39.method_170(player, "command=cooldown=delay", i * 20);
                    }

                    if (method_566(s.substring(1), "blocked_commands")) {
                        playercommandpreprocessevent.setCancelled(true);
                        player.sendMessage(class_125.method_599("blocked_command_message"));
                    }
                }
            }

        }
    }

    static {
        class_119.field_196 = new File("plugins/Spartan/chat_protection.yml");
        class_119.field_197 = new HashMap();
    }
}
