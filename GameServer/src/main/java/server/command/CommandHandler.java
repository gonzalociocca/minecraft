package server.command;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import server.command.server.ServerCommand;

/**
 * Created by noname on 17/5/2017.
 */
public class CommandHandler {

    public static void onCommand(PlayerCommandPreprocessEvent event, String[] args) {
        ServerCommand.onCommand(event, args);
    }

}
