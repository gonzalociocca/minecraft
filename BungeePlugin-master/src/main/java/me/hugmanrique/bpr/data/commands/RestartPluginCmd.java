package me.hugmanrique.bpr.data.commands;

import me.hugmanrique.bpr.BPRestarter;
import me.hugmanrique.bpr.data.PluginRestart;
import me.hugmanrique.bpr.data.RestartOption;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public class RestartPluginCmd extends BaseCmd {
    private static final String PERMISSION = "bpr.restart";

    public RestartPluginCmd(BPRestarter main) {
        super("restartplugin", main);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(PERMISSION)){
            sendMessage(sender, "noPerm");
            return;
        }

        if (args.length < 0){
            sendMessage(sender, "notArgs");
            return;
        }

        String pluginName = args[0];
        Plugin plugin = main.getProxy().getPluginManager().getPlugin(pluginName);

        if (plugin == null){
            sendMessage(sender, "pluginNotFound");
            return;
        }

        // Run the actual plugin restart
        boolean result = new PluginRestart(plugin, RestartOption.FIRE_JOIN_EVENT, RestartOption.FIRE_QUIT_EVENT).run();

        if (result){
            sendMessage(sender, "restartSuccess");
        } else {
            sendMessage(sender, "restartError");
        }
    }
}
