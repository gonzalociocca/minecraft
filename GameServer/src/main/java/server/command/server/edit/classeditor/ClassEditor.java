package server.command.server.edit.classeditor;

import org.bukkit.entity.Player;
import server.api.menu.MenuGenerator;

import java.lang.reflect.Field;

public abstract class ClassEditor {
    public void edit(Player player, Object object, Field field, MenuGenerator menuGenerator){
        if(player != null) {
            for (String str : getUsage()) {
                player.sendMessage(str);
            }
        }
    }

    public abstract String[] getUsage();

    public abstract String[] getContent(Object object);
}
