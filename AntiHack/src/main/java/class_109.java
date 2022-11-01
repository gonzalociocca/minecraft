import be.anybody.api.AbilityEvent;
import me.vagdedes.spartan.Register;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

// $FF: renamed from: bw
public class class_109 implements Listener {

    private static boolean Ξ;

    public static void Ξ() {
        if (!class_109.field_184 && class_93.method_494("advancedabilities")) {
            Bukkit.getPluginManager().registerEvents(new class_109(), Register.field_249);
            class_109.field_184 = true;
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(AbilityEvent abilityevent) {
        Player player = abilityevent.getPlayer();

        class_9.method_37(player, 60);
    }

    static {
        class_109.field_184 = false;
    }
}
