package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.utils.FormatUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandkickall
        extends EssentialsCommand
{
    public Commandkickall()
    {
        super("kickall");
    }

    public void run(Server server, CommandSource sender, String commandLabel, String[] args)
    {
        try{
        String kickReason = args.length > 0 ? getFinalArg(args, 0) : I18n.tl("kickDefault", new Object[0]);
        kickReason = FormatUtil.replaceFormat(kickReason.replace("\\n", "\n").replace("|", "\n"));
        for (Player onlinePlayer : this.ess.getOnlinePlayers()) {
            if ((!sender.isPlayer()) || (!onlinePlayer.getName().equalsIgnoreCase(sender.getPlayer().getName()))) {
                onlinePlayer.kickPlayer(kickReason);
            }
        }
        sender.sendMessage(I18n.tl("kickedAll", new Object[0]));}catch (Exception e){e.printStackTrace();}
    }
}
