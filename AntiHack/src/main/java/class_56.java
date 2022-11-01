import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

// $FF: renamed from: aq
public class class_56 implements Listener {

    private static Enums.HackType Ξ;
    private static long Ξ;

    public static void Ξ(Player player) {
        if (!class_38.method_157(player, class_56.field_114, false) && class_45.method_209(player, class_56.field_114.toString() + "=time") && !class_71.PPPΠ(player) && class_39.method_169(player, class_56.field_114.toString() + "=cooldown")) {
            long i = class_45.method_210(player, class_56.field_114.toString() + "=time");

            if (i <= class_56.field_115) {
                class_38.method_159(player, class_56.field_114, "ms: " + i);
                class_39.method_170(player, class_56.field_114.toString() + "=cooldown", (int) class_56.field_115 / 50);
            }
        }

    }

    @EventHandler
    private void Ξ(PlayerDeathEvent playerdeathevent) {
        Player player = playerdeathevent.getEntity();

        if (!class_38.method_157(player, class_56.field_114, false)) {
            class_45.method_211(player, class_56.field_114.toString() + "=time");
        }
    }

    static {
        class_56.field_114 = Enums.HackType.AutoRespawn;
        class_56.field_115 = 750L;
    }
}
