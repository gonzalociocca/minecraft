import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;

// $FF: renamed from: aJ
public class class_9 {

    private static int Ξ;

    public static void Ξ() {
        if (class_9.field_16 > 0) {
            --class_9.field_16;
        }

    }

    public static boolean Ξ() {
        return class_9.field_16 > 0;
    }

    public static void Ξ(int i) {
        if (i > 0) {
            class_9.field_16 = i;
        }

    }

    public static void Ξ(Player player, int i) {
        if (player != null && i > 0) {
            class_38.method_160(player, Enums.HackType.Phase, i);
            class_38.method_160(player, Enums.HackType.Speed, i);
            class_38.method_160(player, Enums.HackType.Sprint, i);
            class_38.method_160(player, Enums.HackType.Fly, i);
            class_38.method_160(player, Enums.HackType.NormalMovements, i);
            class_38.method_160(player, Enums.HackType.HitReach, i);
        }
    }

    static {
        class_9.field_16 = 0;
    }
}
