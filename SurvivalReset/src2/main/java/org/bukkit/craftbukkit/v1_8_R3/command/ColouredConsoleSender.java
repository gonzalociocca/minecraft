//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.bukkit.craftbukkit.v1_8_R3.command;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.libs.jline.Terminal;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.command.CraftConsoleCommandSender;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;

public class ColouredConsoleSender extends CraftConsoleCommandSender {
    private final Terminal terminal = ((CraftServer)this.getServer()).getReader().getTerminal();
    private final Map<ChatColor, String> replacements = new EnumMap(ChatColor.class);
    private final ChatColor[] colors = ChatColor.values();

    protected ColouredConsoleSender() {
        this.replacements.put(ChatColor.BLACK, Ansi.ansi().a(Attribute.RESET).fg(Color.BLACK).boldOff().toString());
        this.replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Attribute.RESET).fg(Color.BLUE).boldOff().toString());
        this.replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Attribute.RESET).fg(Color.GREEN).boldOff().toString());
        this.replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Attribute.RESET).fg(Color.CYAN).boldOff().toString());
        this.replacements.put(ChatColor.DARK_RED, Ansi.ansi().a(Attribute.RESET).fg(Color.RED).boldOff().toString());
        this.replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Color.MAGENTA).boldOff().toString());
        this.replacements.put(ChatColor.GOLD, Ansi.ansi().a(Attribute.RESET).fg(Color.YELLOW).boldOff().toString());
        this.replacements.put(ChatColor.GRAY, Ansi.ansi().a(Attribute.RESET).fg(Color.WHITE).boldOff().toString());
        this.replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Attribute.RESET).fg(Color.BLACK).bold().toString());
        this.replacements.put(ChatColor.BLUE, Ansi.ansi().a(Attribute.RESET).fg(Color.BLUE).bold().toString());
        this.replacements.put(ChatColor.GREEN, Ansi.ansi().a(Attribute.RESET).fg(Color.GREEN).bold().toString());
        this.replacements.put(ChatColor.AQUA, Ansi.ansi().a(Attribute.RESET).fg(Color.CYAN).bold().toString());
        this.replacements.put(ChatColor.RED, Ansi.ansi().a(Attribute.RESET).fg(Color.RED).bold().toString());
        this.replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Color.MAGENTA).bold().toString());
        this.replacements.put(ChatColor.YELLOW, Ansi.ansi().a(Attribute.RESET).fg(Color.YELLOW).bold().toString());
        this.replacements.put(ChatColor.WHITE, Ansi.ansi().a(Attribute.RESET).fg(Color.WHITE).bold().toString());
        this.replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
        this.replacements.put(ChatColor.BOLD, Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
        this.replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
        this.replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Attribute.UNDERLINE).toString());
        this.replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Attribute.ITALIC).toString());
        this.replacements.put(ChatColor.RESET, Ansi.ansi().a(Attribute.RESET).toString());
    }

    public void sendMessage(String message) {
        /*if(this.terminal.isAnsiSupported()) {
            if(!this.conversationTracker.isConversingModaly()) {
                String result = message;
                ChatColor[] var3 = this.colors;
                int var4 = this.colors.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    ChatColor color = var3[var5];
                    if(this.replacements.containsKey(color)) {
                        result = result.replaceAll("(?i)" + color.toString(), (String)this.replacements.get(color));
                    } else {
                        result = result.replaceAll("(?i)" + color.toString(), "");
                    }
                }

                System.out.println(result + Ansi.ansi().reset().toString());
            }
        } else {*/
            super.sendMessage(message);
        //}

    }

    public static ConsoleCommandSender getInstance() {
        return (ConsoleCommandSender)(Bukkit.getConsoleSender() != null?Bukkit.getConsoleSender():new ColouredConsoleSender());
    }
}
