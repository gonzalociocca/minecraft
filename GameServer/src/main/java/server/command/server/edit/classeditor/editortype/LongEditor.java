package server.command.server.edit.classeditor.editortype;

import org.bukkit.entity.Player;
import server.api.ChatAPI;
import server.api.chat.ChatCallable;
import server.api.menu.Menu;
import server.api.menu.MenuGenerator;
import server.command.server.edit.classeditor.ClassEditor;
import server.common.Code;

import java.lang.reflect.Field;

public class LongEditor extends ClassEditor {

    static String[] usageArray = new String[]{
            "",
            Code.Color("&eValor:&f <Long>"),
            Code.Color("&eEjemplo:&f 30, 10"),
            ""
    };

    @Override
    public void edit(Player player, Object object, Field field, MenuGenerator menuGenerator) {
        super.edit(player, object, field, menuGenerator);
        player.closeInventory();
        ChatAPI.addChatCallable(player, new ChatCallable() {
            @Override
            public boolean onChat(Player player, String message) {
                Long result = Long.valueOf(message);
                try {
                    field.setLong(object, result);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                player.sendMessage(Code.Color("&a"+field.getName()+" ha sido cambiado a "+result));
                menuGenerator.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
                return true;
            }
        });
    }

    @Override
    public String[] getUsage() {
        return usageArray;
    }

    @Override
    public String[] getContent(Object object) {
        return new String[]{
                "",
                Code.Color("&eTipo:&a Long"),
                Code.Color("&eValor:&a "+object),
                ""
        };
    }
}