import java.util.Iterator;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// $FF: renamed from: M
public class class_92 {

    private static Enums.HackType Ξ;
    private static int Ξ;
    private static int Π;

    public static void Ξ() {
        if (class_120.method_572("KillAura.check_overall")) {
            if (class_92.field_167 <= 0) {
                class_92.field_167 = class_92.field_166;
                Iterator iterator = Bukkit.getOnlinePlayers().iterator();

                while (iterator.hasNext()) {
                    Player player = (Player) iterator.next();
                    int i = class_36.method_133(player, class_92.field_165.toString() + "=overall=violatios");

                    class_36.method_138(player, class_92.field_165.toString() + "=overall=violatios");
                    class_36.method_138(player, class_92.field_165.toString() + "=overall=cached");
                    if (i >= 20) {
                        class_36.method_134(player, class_92.field_165.toString() + "=overall=cached", i);
                    }
                }
            } else {
                int j = class_92.field_166 - class_92.field_167;
                Iterator iterator1 = Bukkit.getOnlinePlayers().iterator();

                while (iterator1.hasNext()) {
                    Player player1 = (Player) iterator1.next();

                    if (j <= class_123.method_579(player1, class_92.field_165) + 1) {
                        int k = class_36.method_133(player1, class_92.field_165.toString() + "=overall=cached");

                        if (k > 0) {
                            class_38.method_159(player1, class_92.field_165, "t: overall, vls: " + k);
                        }
                    }
                }

                --class_92.field_167;
            }
        } else {
            class_92.field_167 = class_92.field_166;
        }

    }

    public static void Ξ(Player player) {
        int i = class_36.method_133(player, class_92.field_165.toString() + "=overall=violatios");

        class_36.method_134(player, class_92.field_165.toString() + "=overall=violatios", i + 1);
    }

    static {
        class_92.field_165 = Enums.HackType.KillAura;
        class_92.field_166 = 1200;
        class_92.field_167 = class_92.field_166;
    }
}
