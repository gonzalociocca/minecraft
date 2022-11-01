import java.lang.reflect.Field;
import org.bukkit.entity.Player;

// $FF: renamed from: bk
public class class_85 {

    private static float Ξ(float f) {
        if (f == 0.0F) {
            return 0.0F;
        } else {
            float f1 = (72000.0F - f) / 20.0F;

            f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;
            if ((double) f1 < 0.1D) {
                return f1;
            } else {
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                }

                return f1;
            }
        }
    }

    private static float Ξ(int i) {
        if (i == 0) {
            return 0.0F;
        } else {
            float f = (float) (72000 - i) / 20.0F;

            f = (f * f + f * 2.0F) / 3.0F;
            if ((double) f < 0.1D) {
                return f;
            } else {
                if (f > 1.0F) {
                    f = 1.0F;
                }

                return f;
            }
        }
    }

    private static Object Ξ(Player player) {
        class_101 class_101 = class_101.method_512();

        if (player != null && class_101 != class_101.field_254) {
            try {
                Field field = class_101 != class_101.field_250 && class_101 != class_101.field_251 ? (class_101 != class_101.HHΞ && class_101 != class_101.field_252 ? Class.forName("net.minecraft.server." + class_91.field_164 + ".EntityLiving").getDeclaredFields()[67] : Class.forName("net.minecraft.server." + class_91.field_164 + ".EntityLiving").getDeclaredFields()[66]) : Class.forName("net.minecraft.server." + class_91.field_164 + ".EntityHuman").getDeclaredFields()[29];

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
            return Float.valueOf(0.0F);
        }
    }

    public static float Ξ(Player player) {
        class_101 class_101 = class_101.method_512();

        if (player != null && class_101 != class_101.field_254) {
            Object object = method_466(player);

            return object instanceof Integer ? method_465(((Integer) object).intValue()) : (object instanceof Float ? method_464(((Float) object).floatValue()) : 0.0F);
        } else {
            return 0.0F;
        }
    }
}
