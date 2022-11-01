package me.gonzalociocca.minigame.commands;

import me.gonzalociocca.minigame.column.Perks.DataTypes.GlobalRanks;
import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.misc.Code;
import me.gonzalociocca.minigame.misc.PlayerData;
import me.gonzalociocca.minigame.misc.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by noname on 25/3/2017.
 */
public class RankCommand {
    public static boolean onCommand(Core plugin, boolean isPlayer, boolean isConsole, CommandSender sender, Command command, String label, String[] args) {{
        if(command.getName().equals("rank")){// give[0] name[1] ranktype[2] days[3]
            if(args.length >= 1 && isConsole) {
                if(args.length == 4 && args[0].equalsIgnoreCase("give")){
                    PlayerData pd = plugin.getPlayerData(args[1]);
                    long days = (long)Integer.parseInt(args[3]);
                    GlobalRanks gb = pd.getPerksManager().getGlobalRanks(true);

                    gb.addRank(System.currentTimeMillis(),System.currentTimeMillis()+(days*86400000L), args[2]);
                    pd.getPerksManager().saveAllData();
                }
            } else if(isPlayer) {
                PlayerData pd = plugin.getPlayerData(sender.getName());
                GlobalRanks gb = pd.getPerksManager().getGlobalRanks(false);
                Rank rank = gb.getRank();
                if(rank.getRankType().equals(Rank.RankType.User)){
                    sender.sendMessage(Code.Color("&f> Rango Actual: "+rank.getRankType().getScoreboardPrefix()+" &fPermanente"));
                }else{
                    long finishtime = (rank.getFinishTime()-System.currentTimeMillis()/(1000*60*60*24));
                    String str = "&fFinaliza en "+finishtime+" dias.";
                    if(finishtime> 999){
                        str = "&fPermanente";
                    }
                    sender.sendMessage(Code.Color("&f> Rango Actual: "+rank.getRankType().getScoreboardPrefix()+" "+str));
                }
            } else {
                sender.sendMessage(Code.Color("&b&nComandos"));
                sender.sendMessage(Code.Color("&f/rank give <name> <rankname> <days>"));
            }
            return true;
        }
        return false;
    }
}

}
