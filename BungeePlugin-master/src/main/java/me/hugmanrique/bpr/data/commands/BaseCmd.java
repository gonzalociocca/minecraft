package me.hugmanrique.bpr.data.commands;

import me.hugmanrique.bpr.BPRestarter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public abstract class BaseCmd extends Command {
    protected BPRestarter main;

    public BaseCmd(String name, BPRestarter main) {
        super(name);
        this.main = main;
    }

    protected void sendMessage(CommandSender sender, String key){
        String phrase = main.getLanguage().getPhrase(key);
        sender.sendMessage(TextComponent.fromLegacyText(phrase));
    }
}
