import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// $FF: renamed from: bn
public class class_91 {

    public static final String Ξ;

    public static boolean Ξ(Object object, String s) {
        try {
            return object.getClass().isAssignableFrom(Class.forName(s));
        } catch (Exception exception) {
            return false;
        }
    }

    public static Object Ξ(Player player, String s) {
        if (player != null && s != null) {
            try {
                Class oclass = Class.forName("org.bukkit.craftbukkit." + class_91.field_164 + ".entity.CraftPlayer");
                Object object = oclass.getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);

                return object.getClass().getDeclaredField(s).get(object);
            } catch (Exception exception) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Object Π(Player player, String s) {
        if (player != null && s != null) {
            try {
                Field field = Class.forName("net.minecraft.server." + class_91.field_164 + ".EntityHuman").getDeclaredField(s);

                if (field == null) {
                    return null;
                } else {
                    field.setAccessible(true);
                    Class oclass = Class.forName("org.bukkit.craftbukkit." + class_91.field_164 + ".entity.CraftPlayer");

                    return field.get(oclass.getDeclaredMethod("getHandle", new Class[0]).invoke(player, new Object[0]));
                }
            } catch (Exception exception) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Object HHΞ(Player player, String s) {
        if (player != null && s != null) {
            try {
                Field field = Class.forName("net.minecraft.server." + class_91.field_164 + ".EntityLiving").getDeclaredField(s);

                if (field == null) {
                    return null;
                } else {
                    field.setAccessible(true);
                    Class oclass = Class.forName("org.bukkit.craftbukkit." + class_91.field_164 + ".entity.CraftPlayer");

                    return field.get(oclass.getDeclaredMethod("getHandle", new Class[0]).invoke(player, new Object[0]));
                }
            } catch (Exception exception) {
                return null;
            }
        } else {
            return null;
        }
    }

    static {
        class_91.field_164 = Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
}
