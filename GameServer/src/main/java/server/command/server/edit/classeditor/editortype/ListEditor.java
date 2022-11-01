package server.command.server.edit.classeditor.editortype;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import server.api.ChatAPI;
import server.api.chat.ChatCallable;
import server.api.menu.Menu;
import server.api.menu.MenuGenerator;
import server.api.menu.PlayerMenu;
import server.command.server.edit.classeditor.ClassEditor;
import server.command.server.edit.classeditor.ClassType;
import server.common.Code;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListEditor extends ClassEditor {
    static String[] usageArray = new String[]{
            "",
            Code.Color("&eValor:&f List<Object>"),
            ""
            //todo: add way to edit
    };

    @Override
    public void edit(Player player, Object object, Field field, MenuGenerator menuGenerator) {
        super.edit(player, object, field, menuGenerator);
        ClassType classType = ClassType.getByField(object, field);
        MenuGenerator menuGenerator1 = generate(player, object, field, menuGenerator);
        menuGenerator1.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
    }

    public MenuGenerator generate(Player player, Object object, Field field, MenuGenerator menuGenerator){
        Object obj = null;
        Class subType = null;
        try {
            obj = field.get(object);
            ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
            subType = (Class<?>) stringListType.getActualTypeArguments()[0];
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        final List finalObj = (List)obj;

        final Class finalSubType = subType;
        MenuGenerator menuGenerator1 = new MenuGenerator() {
            @Override
            public Menu createMenu(Player player, Menu.OpenReason openReason) {
                Menu menu = new PlayerMenu(player,Code.Color("&3List &f -&0 Select Object"), openReason);
                final MenuGenerator finalMenuGenerator1 = this;
                for(int index = 0; index < finalObj.size(); index++){
                    final int finalIndex = index;
                    Object var = finalObj.get(index);
                    ClassType classType = ClassType.getByObject(var);
                    String objectName = var != null ? var.getClass().getSimpleName() : "" + var;
                    ItemStack itemStack = Code.makeItemStack(Material.PAPER, Code.Color("&3"+objectName));
                    ItemMeta im = itemStack.getItemMeta();
                    im.setLore(Arrays.asList(classType.getEditor().getContent(var)));
                    itemStack.setItemMeta(im);

                    menu.add(itemStack, new Menu.ClickCallable(){
                        @Override
                        public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                            super.onClick(player, menu, itemStack, event);
                            editSub(player, classType, finalIndex, var, finalObj, finalMenuGenerator1);
                        }
                    });
                }

                menu.add(Code.makeItemStack(Material.EMERALD, Code.Color("&aNuevo")), new Menu.ClickCallable(){
                    @Override
                    public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                        super.onClick(player, menu, itemStack, event);
                        try {
                            finalObj.add(newFrom(finalSubType));
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        createMenu(player, openReason).openFor(player, openReason);
                    }
                });
                if(menuGenerator != null){
                    menu.add(Code.makeItemStack(Material.ARROW, Code.Color("&aAtras")), new Menu.ClickCallable(){
                        @Override
                        public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                            super.onClick(player, menu, itemStack, event);
                            menuGenerator.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
                        }
                    });
                }

                return menu;
            }
        };

        return menuGenerator1;
    }

    public void editSub(Player player, ClassType classType, int index, Object object, List list, MenuGenerator menuGenerator){
        player.closeInventory();
        for(String str : classType.getEditor().getUsage()){
            player.sendMessage(str);
        }
        if(object instanceof String){
            ChatAPI.addChatCallable(player, new ChatCallable() {
                @Override
                public boolean onChat(Player player, String message) {
                    String coloredMsg = Code.Color(message);
                    list.set(index, coloredMsg);
                    menuGenerator.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
                    player.sendMessage(Code.Color("&aNuevo Valor:&r "+coloredMsg));
                    return true;
                }
            });
        } else if(object instanceof Integer){
            ChatAPI.addChatCallable(player, new ChatCallable() {
                @Override
                public boolean onChat(Player player, String message) {
                    list.set(index, Integer.parseInt(message));
                    menuGenerator.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
                    player.sendMessage(Code.Color("&aNuevo Valor:&r "+message));
                    return true;
                }
            });
        } else if(object instanceof Double){
            ChatAPI.addChatCallable(player, new ChatCallable() {
                @Override
                public boolean onChat(Player player, String message) {
                    list.set(index, Double.parseDouble(message));
                    menuGenerator.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
                    player.sendMessage(Code.Color("&aNuevo Valor:&r "+message));
                    return true;
                }
            });
        } else if(object instanceof Long){
            ChatAPI.addChatCallable(player, new ChatCallable() {
                @Override
                public boolean onChat(Player player, String message) {
                    list.set(index, Long.parseLong(message));
                    menuGenerator.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
                    player.sendMessage(Code.Color("&aNuevo Valor:&r "+message));
                    return true;
                }
            });
        } else {
            classType.getEditor().edit(player, object, null, menuGenerator);
        }
    }

    @Override
    public String[] getUsage() {
        return usageArray;
    }

    @Override
    public String[] getContent(Object object) {
        List objectList = (List)object;
        List<String> stringList = new ArrayList();
        String typeName = objectList != null ? objectList.getClass().getSimpleName() : null;
        int size = objectList != null ? objectList.size() : -1;
        stringList.add(0, "");
        stringList.add(1, Code.Color("&eTipo:&a "+typeName));
        stringList.add(2, Code.Color("&eValor:&a "+size+" objects"));
        stringList.add(3, "");
        if(objectList!=null) {
            for (Object obj : objectList) {
                if(obj != null){
                    stringList.add(obj.toString());
                } else {
                    stringList.add(""+obj);
                }
            }
        }
        return stringList.toArray(new String[stringList.size()]);
    }

    public Object newFrom(Class classType) throws IllegalAccessException, InstantiationException {
        if(classType.isAssignableFrom(Integer.class)){
            return 0;
        } else if(classType.isAssignableFrom(Double.class)){
            return 0D;
        } else if(classType.isAssignableFrom(Long.class)){
            return 0L;
        } else if(classType.isAssignableFrom(String.class)){
            return "";
        } else {
            return classType.newInstance();
        }
    }
}