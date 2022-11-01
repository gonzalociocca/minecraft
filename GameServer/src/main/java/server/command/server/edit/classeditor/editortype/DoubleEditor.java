package server.command.server.edit.classeditor.editortype;

import org.bukkit.entity.Player;
import server.api.ChatAPI;
import server.api.chat.ChatCallable;
import server.api.menu.Menu;
import server.api.menu.MenuGenerator;
import server.command.server.edit.classeditor.ClassEditor;
import server.common.Code;

import java.lang.reflect.Field;

public class DoubleEditor extends ClassEditor {

    static String[] usageArray = new String[]{
           "",
            Code.Color("&eValor:&f <Double>"),
            Code.Color("&eEjemplo:&f 1D, 25.5D"),
            ""
    };

    @Override
    public void edit(Player player, Object object, Field field, MenuGenerator menuGenerator) {
        super.edit(player, object, field, menuGenerator);
        player.closeInventory();
        ChatAPI.addChatCallable(player, new ChatCallable() {
            @Override
            public boolean onChat(Player player, String message) {
                Double result = Double.valueOf(message);
                try {
                    field.setDouble(object, result);
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
                Code.Color("&eTipo:&a Double"),
                Code.Color("&eValor:&a "+object),
                ""
        };
    }
}
