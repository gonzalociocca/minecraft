import me.choco.veinminer.api.PlayerVeinMineEvent;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// $FF: renamed from: bD
public class class_32 implements Listener {

    private static boolean Ξ;

    public static void Ξ() {
        if (!class_32.field_31 && class_93.method_494("veinminer")) {
            Bukkit.getPluginManager().registerEvents(new class_32(), Register.field_249);
            class_32.field_31 = true;
        }

    }

    @EventHandler
    private void Ξ(PlayerVeinMineEvent playerveinmineevent) {
        Player player = playerveinmineevent.getPlayer();

        class_38.method_160(player, Enums.HackType.Nuker, 1);
        class_38.method_160(player, Enums.HackType.NoSwing, 1);
        class_38.method_160(player, Enums.HackType.FastBreak, 1);
        class_38.method_160(player, Enums.HackType.BlockReach, 1);
    }

    static {
        class_32.field_31 = false;
    }
}
