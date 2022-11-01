import java.util.UUID;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

// $FF: renamed from: e
public class class_122 implements CommandExecutor {

    public static void Ξ(CommandSender commandsender) {
        String s = Register.field_249.getDescription().getVersion();
        String s1 = "252643";
        String s2 = "790879575";

        if (!class_6.method_25(s1)) {
            s1 = "None";
        }

        if (!class_6.method_26(s2)) {
            s2 = "None";
        }

        commandsender.sendMessage("");
        commandsender.sendMessage("\u00a74Spartan \u00a78[\u00a77(\u00a7fv: " + s + "\u00a77)\u00a78, \u00a77(\u00a7fs: " + s1 + "\u00a77)\u00a78, \u00a77(\u00a7fn: " + s2 + "\u00a77)\u00a78]");
        commandsender.sendMessage("\u00a76Link\u00a78:\u00a7e http://www.spigotmc.org/resources/25638");
    }

    public static void Π(CommandSender commandsender) {
        if (commandsender != null) {
            method_576(commandsender);
            if (commandsender instanceof Player) {
                Player player = (Player) commandsender;

                if (class_47.method_229(player, Enums.Permission.manage)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan menu");
                }

                if (class_47.method_229(player, Enums.Permission.reload)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan reload");
                }

                if (class_47.method_229(player, Enums.Permission.verbose) || class_47.method_229(player, Enums.Permission.mining)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan notifications <verbose/mining>");
                }

                if (class_47.method_229(player, Enums.Permission.info)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan info [player]");
                }

                if (class_47.method_229(player, Enums.Permission.ping)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan ping [player]");
                }

                if (class_47.method_229(player, Enums.Permission.kick)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan kick <player> <reason>");
                }

                if (class_47.method_229(player, Enums.Permission.toggle)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan toggle <check>");
                }

                if (class_47.method_229(player, Enums.Permission.warn)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan warn <player> <reason>");
                }

                if (class_47.method_229(player, Enums.Permission.use_bypass)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan bypass <player> <check> <seconds>");
                }

                if (class_47.method_229(player, Enums.Permission.ban)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan ban <player> <reason>");
                }

                if (class_47.method_229(player, Enums.Permission.unban)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan unban <player>");
                }

                if (class_47.method_229(player, Enums.Permission.ban_info)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan ban-info <player>");
                    player.sendMessage(ChatColor.RED + "Usage: /spartan ban-list");
                }

                if (class_47.method_229(player, Enums.Permission.wave)) {
                    player.sendMessage(ChatColor.RED + "Usage: /spartan wave <add/remove/clear/run> [player] [command]");
                }
            } else {
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan reload");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan ping <player>");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan kick <player> <reason>");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan toggle <check>");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan warn <player> <reason>");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan bypass <player> <check> <seconds>");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan ban <player> <reason>");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan unban <player>");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan ban-info <player>");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan ban-list");
                commandsender.sendMessage(ChatColor.RED + "Usage: /spartan wave <add/remove/clear/run> [player] [command]");
            }

        }
    }

    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] astring) {
        if (s.equalsIgnoreCase("Spartan") && (commandsender instanceof ConsoleCommandSender || commandsender instanceof Player)) {
            if (astring.length == 0) {
                method_577(commandsender);
            } else {
                String s1;

                if (astring.length == 1) {
                    if (astring[0].equalsIgnoreCase("Menu")) {
                        if (!(commandsender instanceof Player)) {
                            commandsender.sendMessage(class_125.method_599("non_console_command"));
                            return true;
                        }

                        if (!class_47.method_229((Player) commandsender, Enums.Permission.manage)) {
                            commandsender.sendMessage(class_125.method_599("command_no_access"));
                            return true;
                        }

                        class_1.method_4((Player) commandsender);
                    } else if (astring[0].equalsIgnoreCase("Ping")) {
                        if (!(commandsender instanceof Player)) {
                            commandsender.sendMessage(class_125.method_599("non_console_command"));
                            return true;
                        }

                        if (!class_47.method_229((Player) commandsender, Enums.Permission.ping)) {
                            commandsender.sendMessage(class_125.method_599("command_no_access"));
                            return true;
                        }

                        s1 = class_125.method_599("ping_command_message");
                        s1 = class_79.method_442((Player) commandsender, s1, (Enums.HackType) null);
                        commandsender.sendMessage(s1);
                    } else if (astring[0].equalsIgnoreCase("Ban-list")) {
                        if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.ban_info)) {
                            commandsender.sendMessage(class_125.method_599("command_no_access"));
                            return true;
                        }

                        commandsender.sendMessage(ChatColor.GRAY + "Banned Players" + ChatColor.DARK_GRAY + ":");
                        commandsender.sendMessage(class_118.method_557());
                    } else if (astring[0].equalsIgnoreCase("Reload")) {
                        if (commandsender instanceof Player) {
                            if (!class_47.method_229((Player) commandsender, Enums.Permission.reload)) {
                                commandsender.sendMessage(class_125.method_599("command_no_access"));
                                return true;
                            }

                            class_123.method_580((Player) commandsender, false);
                        } else {
                            class_123.method_580((Player) null, true);
                        }
                    } else if (astring[0].equalsIgnoreCase("Info")) {
                        if (!(commandsender instanceof Player)) {
                            commandsender.sendMessage(class_125.method_599("non_console_command"));
                            return true;
                        }

                        if (!class_47.method_229((Player) commandsender, Enums.Permission.info)) {
                            commandsender.sendMessage(class_125.method_599("command_no_access"));
                            return true;
                        }

                        class_3.method_11((Player) commandsender, (Player) commandsender);
                    } else {
                        method_577(commandsender);
                    }
                } else {
                    String s2;
                    String s3;

                    if (astring.length == 2) {
                        if (astring[0].equalsIgnoreCase("Notifications")) {
                            s1 = astring[1];
                            if (!(commandsender instanceof Player)) {
                                commandsender.sendMessage(class_125.method_599("non_console_command"));
                                return true;
                            }

                            if (s1.equalsIgnoreCase("Verbose")) {
                                if (!class_47.method_229((Player) commandsender, Enums.Permission.verbose)) {
                                    commandsender.sendMessage(class_125.method_599("command_no_access"));
                                    return true;
                                }

                                class_51.method_272((Player) commandsender, 0);
                            } else if (s1.equalsIgnoreCase("Mining")) {
                                if (!class_47.method_229((Player) commandsender, Enums.Permission.mining)) {
                                    commandsender.sendMessage(class_125.method_599("command_no_access"));
                                    return true;
                                }

                                class_126.method_601((Player) commandsender);
                            }
                        } else if (astring[0].equalsIgnoreCase("Wave")) {
                            s1 = astring[1];
                            if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.wave)) {
                                commandsender.sendMessage(class_125.method_599("command_no_access"));
                                return true;
                            }

                            if (s1.equalsIgnoreCase("run")) {
                                if (class_130.method_622() == 0) {
                                    commandsender.sendMessage(class_125.method_599("empty_wave_list"));
                                    return true;
                                }

                                class_130.HHΞ();
                            } else if (s1.equalsIgnoreCase("clear")) {
                                class_130.method_620();
                                commandsender.sendMessage(class_125.method_599("wave_clear_message"));
                            }
                        } else {
                            Player player;

                            if (astring[0].equalsIgnoreCase("Info")) {
                                player = Bukkit.getPlayer(astring[1]);
                                if (!(commandsender instanceof Player)) {
                                    commandsender.sendMessage(class_125.method_599("non_console_command"));
                                    return true;
                                }

                                if (!class_47.method_229((Player) commandsender, Enums.Permission.info)) {
                                    commandsender.sendMessage(class_125.method_599("command_no_access"));
                                    return true;
                                }

                                if (player == null || !player.isOnline()) {
                                    commandsender.sendMessage(class_125.method_599("player_not_found_message"));
                                    return true;
                                }

                                class_3.method_11((Player) commandsender, player);
                            } else if (astring[0].equalsIgnoreCase("Ping")) {
                                player = Bukkit.getPlayer(astring[1]);
                                if (!(commandsender instanceof Player)) {
                                    commandsender.sendMessage(class_125.method_599("non_console_command"));
                                    return true;
                                }

                                if (player == null || !player.isOnline()) {
                                    commandsender.sendMessage(class_125.method_599("player_not_found_message"));
                                    return true;
                                }

                                if (!class_47.method_229((Player) commandsender, Enums.Permission.ping)) {
                                    commandsender.sendMessage(class_125.method_599("command_no_access"));
                                    return true;
                                }

                                s2 = class_125.method_599("ping_command_message");
                                s2 = class_79.method_442(player, s2, (Enums.HackType) null);
                                commandsender.sendMessage(s2);
                            } else if (astring[0].equalsIgnoreCase("Toggle")) {
                                s1 = astring[1];
                                if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.toggle)) {
                                    commandsender.sendMessage(class_125.method_599("command_no_access"));
                                    return true;
                                }

                                try {
                                    Enums.HackType enums_hacktype = Enums.HackType.valueOf(s1);

                                    if (class_123.method_586(enums_hacktype)) {
                                        class_123.HHΞ(enums_hacktype);
                                        s3 = class_125.method_599("check_disable_message");
                                        s3 = class_79.method_442((Player) commandsender, s3, enums_hacktype);
                                        commandsender.sendMessage(s3);
                                    } else {
                                        class_123.method_589(enums_hacktype);
                                        s3 = class_125.method_599("check_enable_message");
                                        s3 = class_79.method_442((Player) commandsender, s3, enums_hacktype);
                                        commandsender.sendMessage(s3);
                                    }
                                } catch (Exception exception) {
                                    commandsender.sendMessage(class_125.method_599("non_existing_check"));
                                }
                            } else {
                                OfflinePlayer offlineplayer;

                                if (astring[0].equalsIgnoreCase("Unban")) {
                                    offlineplayer = Bukkit.getOfflinePlayer(astring[1]);
                                    if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.unban)) {
                                        commandsender.sendMessage(class_125.method_599("command_no_access"));
                                        return true;
                                    }

                                    if (!class_118.method_561(offlineplayer.getUniqueId())) {
                                        commandsender.sendMessage(class_125.method_599("player_not_banned"));
                                        return true;
                                    }

                                    class_118.method_560(offlineplayer.getUniqueId());
                                    s2 = class_125.method_599("unban_message");
                                    s2 = class_79.method_442(offlineplayer, s2, (Enums.HackType) null);
                                    commandsender.sendMessage(s2);
                                } else if (astring[0].equalsIgnoreCase("Ban-info")) {
                                    offlineplayer = Bukkit.getOfflinePlayer(astring[1]);
                                    if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.ban_info)) {
                                        commandsender.sendMessage(class_125.method_599("command_no_access"));
                                        return true;
                                    }

                                    if (!class_118.method_561(offlineplayer.getUniqueId())) {
                                        commandsender.sendMessage(class_125.method_599("player_not_banned"));
                                        return true;
                                    }

                                    commandsender.sendMessage(ChatColor.GRAY + "Ban Information" + ChatColor.DARK_GRAY + ":");
                                    commandsender.sendMessage(ChatColor.GRAY + "Player" + ChatColor.DARK_GRAY + ": " + ChatColor.RED + offlineplayer.getName());
                                    commandsender.sendMessage(ChatColor.GRAY + "Punisher" + ChatColor.DARK_GRAY + ": " + ChatColor.GREEN + class_118.method_562(offlineplayer.getUniqueId(), "punisher"));
                                    commandsender.sendMessage(ChatColor.GRAY + "Reason" + ChatColor.DARK_GRAY + ": " + ChatColor.YELLOW + class_118.method_562(offlineplayer.getUniqueId(), "reason"));
                                } else {
                                    method_577(commandsender);
                                }
                            }
                        }
                    } else if (astring.length > 2) {
                        StringBuilder stringbuilder = new StringBuilder();

                        for (int i = 2; i < astring.length; ++i) {
                            stringbuilder.append(astring[i] + " ");
                        }

                        s2 = stringbuilder.toString().substring(0, stringbuilder.length() - 1);
                        if (s2.length() > 75) {
                            commandsender.sendMessage(class_125.method_599("massive_command_reason"));
                            return true;
                        }

                        Player player1;

                        if (astring[0].equalsIgnoreCase("Kick")) {
                            player1 = Bukkit.getPlayer(astring[1]);
                            if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.kick)) {
                                commandsender.sendMessage(class_125.method_599("command_no_access"));
                                return true;
                            }

                            if (player1 == null || !player1.isOnline()) {
                                commandsender.sendMessage(class_125.method_599("player_not_found_message"));
                                return true;
                            }

                            if (s2.length() > 75) {
                                commandsender.sendMessage(class_125.method_599("massive_command_reason"));
                                return true;
                            }

                            class_49.method_250(player1, s2);
                        } else if (astring[0].equalsIgnoreCase("Warn")) {
                            player1 = Bukkit.getPlayer(astring[1]);
                            if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.warn)) {
                                commandsender.sendMessage(class_125.method_599("command_no_access"));
                                return true;
                            }

                            if (player1 == null || !player1.isOnline()) {
                                commandsender.sendMessage(class_125.method_599("player_not_found_message"));
                                return true;
                            }

                            if (s2.length() > 75) {
                                commandsender.sendMessage(class_125.method_599("massive_command_reason"));
                                return true;
                            }

                            class_49.method_249(commandsender, player1, s2);
                        } else {
                            String s4;

                            if (astring[0].equalsIgnoreCase("Ban")) {
                                OfflinePlayer offlineplayer1 = Bukkit.getOfflinePlayer(astring[1]);

                                if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.ban)) {
                                    commandsender.sendMessage(class_125.method_599("command_no_access"));
                                    return true;
                                }

                                if (s2.length() > 75) {
                                    commandsender.sendMessage(class_125.method_599("massive_command_reason"));
                                    return true;
                                }

                                class_118.method_559(offlineplayer1.getUniqueId(), commandsender.getName(), s2);
                                s4 = class_125.method_599("ban_message");
                                s4 = s4.replace("{reason}", s2);
                                s4 = s4.replace("{punisher}", commandsender.getName());
                                s4 = class_79.method_442(offlineplayer1, s4, (Enums.HackType) null);
                                commandsender.sendMessage(s4);
                            } else {
                                int j;

                                if (astring[0].equalsIgnoreCase("Bypass")) {
                                    if (astring.length <= 4) {
                                        player1 = Bukkit.getPlayer(astring[1]);
                                        s4 = astring[2];
                                        String s5 = astring[3];

                                        if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.use_bypass)) {
                                            commandsender.sendMessage(class_125.method_599("command_no_access"));
                                            return true;
                                        }

                                        if (player1 == null || !player1.isOnline()) {
                                            commandsender.sendMessage(class_125.method_599("player_not_found_message"));
                                            return true;
                                        }

                                        try {
                                            Enums.HackType enums_hacktype1 = Enums.HackType.valueOf(s4);

                                            try {
                                                j = Integer.parseInt(s5);
                                                if (j < 1 || j > 3600) {
                                                    commandsender.sendMessage(ChatColor.RED + "Seconds must be between 1 and 3600.");
                                                    return true;
                                                }

                                                class_38.method_160(player1, enums_hacktype1, j * 20);
                                                String s6 = class_125.method_599("bypass_message");

                                                s6 = class_79.method_442(player1, s6, enums_hacktype1);
                                                s6 = s6.replace("{time}", String.valueOf(j));
                                                commandsender.sendMessage(s6);
                                            } catch (Exception exception1) {
                                                commandsender.sendMessage(ChatColor.RED + s5 + " is not a valid number.");
                                            }
                                        } catch (Exception exception2) {
                                            commandsender.sendMessage(class_125.method_599("non_existing_check"));
                                        }
                                    } else {
                                        method_577(commandsender);
                                    }
                                } else if (astring[0].equalsIgnoreCase("Wave")) {
                                    s3 = astring[1];
                                    OfflinePlayer offlineplayer2 = Bukkit.getOfflinePlayer(astring[2]);

                                    if (commandsender instanceof Player && !class_47.method_229((Player) commandsender, Enums.Permission.wave)) {
                                        commandsender.sendMessage(class_125.method_599("command_no_access"));
                                        return true;
                                    }

                                    String s7;

                                    if (s3.equalsIgnoreCase("add") && astring.length >= 4) {
                                        if (class_130.method_622() >= 100) {
                                            commandsender.sendMessage(class_125.method_599("full_wave_list"));
                                            return true;
                                        }

                                        StringBuilder stringbuilder1 = new StringBuilder();

                                        for (j = 3; j < astring.length; ++j) {
                                            stringbuilder1.append(astring[j] + " ");
                                        }

                                        s7 = stringbuilder1.toString().substring(0, stringbuilder1.length() - 1);
                                        if (s7.length() > 75) {
                                            commandsender.sendMessage(class_125.method_599("massive_command_reason"));
                                            return true;
                                        }

                                        class_130.method_618(offlineplayer2.getUniqueId(), s7);
                                        String s8 = class_125.method_599("wave_add_message");

                                        s8 = class_79.method_442(offlineplayer2, s8, (Enums.HackType) null);
                                        commandsender.sendMessage(s8);
                                    } else if (s3.equalsIgnoreCase("remove") && astring.length >= 3) {
                                        if (class_130.method_622() == 0) {
                                            commandsender.sendMessage(class_125.method_599("empty_wave_list"));
                                            return true;
                                        }

                                        UUID uuid = offlineplayer2.getUniqueId();

                                        if (!class_130.method_621(uuid)) {
                                            s7 = class_125.method_599("wave_not_added_message");
                                            s7 = class_79.method_442(offlineplayer2, s7, (Enums.HackType) null);
                                            commandsender.sendMessage(s7);
                                            return true;
                                        }

                                        class_130.method_619(uuid);
                                        s7 = class_125.method_599("wave_remove_message");
                                        s7 = class_79.method_442(offlineplayer2, s7, (Enums.HackType) null);
                                        commandsender.sendMessage(s7);
                                    } else {
                                        method_577(commandsender);
                                    }
                                } else {
                                    method_577(commandsender);
                                }
                            }
                        }
                    } else {
                        method_577(commandsender);
                    }
                }
            }
        }

        return false;
    }
}
