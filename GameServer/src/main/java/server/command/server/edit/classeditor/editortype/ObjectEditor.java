package server.command.server.edit.classeditor.editortype;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import server.api.menu.Menu;
import server.api.menu.MenuGenerator;
import server.api.menu.PlayerMenu;
import server.command.server.edit.classeditor.ClassEditor;
import server.command.server.edit.classeditor.ClassType;
import server.common.Code;
import server.util.UtilClass;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by noname on 4/6/2017.
 */
public class ObjectEditor extends ClassEditor {

    static String[] usageArray = new String[]{
            "",
            Code.Color("&eValor:&f Object"),
            ""
    };

    @Override
    public void edit(Player player, Object object, Field field, MenuGenerator menuGenerator) {
        super.edit(player, object, field, menuGenerator);
        if(object != null && field != null){
            try {
                menuGenerator = generator(menuGenerator, field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else if(object != null){
            menuGenerator = generator(menuGenerator, object);
        }
        menuGenerator.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
    }

    @Override
    public String[] getUsage() {
        return usageArray;
    }

    public static MenuGenerator generator(final MenuGenerator prev, Object object) {
        Map<Class, List<Field>> fieldMap = UtilClass.getFieldMap(object);

        return new MenuGenerator() {
            final MenuGenerator newGen = this;
            @Override
            public Menu createMenu(Player player, Menu.OpenReason openReason) {
                Menu menu = new PlayerMenu(player, Code.Color("&3"+object.getClass().getSimpleName() + "&f -&0 Select Type"), Menu.OpenReason.Menu);
                for (Iterator<Map.Entry<Class, List<Field>>> it = fieldMap.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Class, List<Field>> entry = it.next();
                    Class type = entry.getKey();
                    List<Field> fieldList = entry.getValue();
                    menu.add(Code.makeItemStack(Material.PAPER, Code.Color("&3" + type.getSimpleName()), fieldList.size()), new Menu.ClickCallable() {
                        @Override
                        public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                            super.onClick(player, menu, itemStack, event);
                            MenuGenerator generatorSub = generatorSub(newGen, type, fieldList, object);
                            generatorSub.createMenu(player, openReason).openFor(player, openReason);
                        }
                    });
                }
                if(prev != null){
                    menu.add(Code.makeItemStack(Material.ARROW, Code.Color("&aAtras")), new Menu.ClickCallable(){
                        @Override
                        public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                            super.onClick(player, menu, itemStack, event);
                            prev.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
                        }
                    });
                }
                return menu;
            }
        };
    }

    public static MenuGenerator generatorSub(final MenuGenerator prev, Class type, List<Field> fieldList, Object object){
        return new MenuGenerator() {
            MenuGenerator menuGenerator = this;
            @Override
            public Menu createMenu(Player player, Menu.OpenReason openReason) {
                Menu menu2 = new Menu(Code.Color("&3"+type.getSimpleName() + "&f -&0 Select Object"));
                for (Field field : fieldList) {
                    Object obj = null;
                    try {
                        obj = field.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    ClassType classType = ClassType.getByObject(obj);

                    menu2.add(Code.makeItemStack(Material.PAPER, Code.Color("&3")+field.getName(), classType.getEditor().getContent(obj)), new Menu.ClickCallable() {
                        @Override
                        public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                            super.onClick(player, menu, itemStack, event);
                            ClassType classType = ClassType.getByField(object, field);
                            if(classType != null){
                                classType.getEditor().edit(player, object, field, menuGenerator);
                            }
                        }
                    });
                }
                if(prev != null){
                    menu2.add(Code.makeItemStack(Material.ARROW, Code.Color("&aAtras")), new Menu.ClickCallable(){
                        @Override
                        public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
                            super.onClick(player, menu, itemStack, event);
                            prev.createMenu(player, Menu.OpenReason.Menu).openFor(player, Menu.OpenReason.Menu);
                        }
                    });
                }
                return menu2;
            }
        };
    }

    @Override
    public String[] getContent(Object object) {
        String className = object != null ? object.getClass().getSimpleName() : "Null Object";
        return new String[]{
                "",
                Code.Color("&eTipo:&a "+className),
                Code.Color("&eValor:&a ?"),
                ""
        };
    }
}
