import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

// $FF: renamed from: g
public class class_124 implements Listener {

    private static int Ξ;

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (playerinteractevent.getAction() == Action.LEFT_CLICK_AIR && class_39.method_169(player, "cps=counter=cooldown")) {
            class_36.method_135(player, "cps=counter", class_124.field_208);
        }

    }

    public static void Ξ(Player player) {
        if (class_36.method_136(player, "cps=counter") == 1) {
            int i = class_36.method_133(player, "cps=counter");

            class_36.method_134(player, "cps=counter=result", i);
            class_39.method_170(player, "cps=counter=cooldown", 40);
        }

    }

    public static int Ξ(Player player) {
        int i = class_133.method_637(player);
        int j = class_36.method_133(player, "cps=counter=result") / (class_124.field_208 / 20);

        return i > j ? i : j;
    }

    static {
        class_124.field_208 = 80;
    }
}
