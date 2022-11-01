import org.bukkit.entity.Player;

// $FF: renamed from: bH
public class class_41 {

    private static int Ξ;
    private static int Π;

    public static void Ξ() {
        if (class_41.field_57 <= 0) {
            class_41.field_57 = class_41.field_56;
            class_36.method_131();
            class_39.method_166();
            class_45.method_208();
        } else {
            --class_41.field_57;
        }

    }

    public static void Ξ(Player player) {
        class_39.method_173(player);
        class_36.method_139(player);
        class_45.method_212(player);
    }

    public static double Ξ(double d0) {
        double d1 = class_97.method_502();

        return d1 >= 18.0D ? d0 : (20.0D - d1) / 2.0D * d0;
    }

    public static int Ξ(int i) {
        double d0 = class_97.method_502();
        int j = (int) (20.0D - Math.floor(d0)) / 2;

        return d0 >= 18.0D ? i : j * i;
    }

    public static long Ξ(long i) {
        double d0 = class_97.method_502();
        long j = (long) (20.0D - Math.floor(d0)) / 2L;

        return d0 >= 18.0D ? i : j * i;
    }

    static {
        class_41.field_56 = 1200;
        class_41.field_57 = class_41.field_56;
    }
}
