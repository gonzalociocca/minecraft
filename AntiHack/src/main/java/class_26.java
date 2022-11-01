import com.nisovin.magicspells.events.SpellCastEvent;
import com.nisovin.magicspells.events.SpellCastedEvent;
import com.nisovin.magicspells.events.SpellEvent;
import me.vagdedes.spartan.Register;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

// $FF: renamed from: bA
public class class_26 implements Listener {

    private static boolean Ξ;

    public static void Ξ() {
        if (!class_26.field_21 && class_93.method_494("magicspells")) {
            Bukkit.getPluginManager().registerEvents(new class_26(), Register.field_249);
            class_26.field_21 = true;
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(SpellCastEvent spellcastevent) {
        if (!spellcastevent.isCancelled()) {
            Player player = spellcastevent.getCaster();

            class_9.method_37(player, 40);
        }

    }

    @EventHandler
    private void Ξ(SpellCastedEvent spellcastedevent) {
        Player player = spellcastedevent.getCaster();

        class_9.method_37(player, 40);
    }

    @EventHandler
    private void Ξ(SpellEvent spellevent) {
        Player player = spellevent.getCaster();

        class_9.method_37(player, 40);
    }

    static {
        class_26.field_21 = false;
    }
}
