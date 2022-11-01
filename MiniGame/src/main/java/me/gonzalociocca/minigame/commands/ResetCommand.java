package me.gonzalociocca.minigame.commands;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.games.GameType;
import me.gonzalociocca.minigame.games.game.GameBase;
import me.gonzalociocca.minigame.map.Cipher.MapCipherBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
/**
 * Created by noname on 30/3/2017.
 */
public class ResetCommand {
    public static boolean onCommand(Core plugin, boolean isPlayer, boolean isConsole, CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("reset")) {
            for(GameBase base : plugin.getGameManager().getGames()){
                base.resetGame();
            }
            return true;
        }
        return false;
    }
}
