package me.gonzalociocca.minigame.commands;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.games.GameState;
import me.gonzalociocca.minigame.games.game.GameBase;
import me.gonzalociocca.minigame.misc.Code;
import me.gonzalociocca.minigame.misc.CustomLocation;
import me.gonzalociocca.minigame.misc.PlayerData;
import me.gonzalociocca.minigame.misc.TagEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by noname on 31/3/2017.
 */
public class GameCommand {
    public static boolean onCommand(Core plugin, boolean isPlayer, boolean isConsole, CommandSender sender, Command command, String label, String[] args) {
        if (isPlayer && command.getName().equals("game")) {
            Player player = (Player)sender;
            PlayerData pd = plugin.getPlayerData(player.getName());
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("join")){
                    if(args.length>1){
                        int gameid = Integer.parseInt(args[1]);
                        for(GameBase base : plugin.getGameManager().getGames()){
                            if(base.getGameID() == gameid && base.canJoin(pd)){
                                base.makePlayer(pd);
                            }
                        }
                    }
                }
                else if(args[0].equalsIgnoreCase("list")){
                    for(GameBase base : plugin.getGameManager().getGames()){
                        sender.sendMessage(Code.Color("&f"+base.getGameID()+": " + "&e Estado: "+base.getState().getDisplay()));
                    }
                }
            }else{
                sender.sendMessage(Code.Color("/game join <mapname>"));
                sender.sendMessage(Code.Color("/game list"));
            }
            return true;
        }
        return false;
    }
}
