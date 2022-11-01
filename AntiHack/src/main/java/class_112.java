import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

// $FF: renamed from: W
public class class_112 implements Listener {

    private Enums.HackType Ξ;

    public class_112() {
        this.field_189 = Enums.HackType.Exploits;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(SignChangeEvent signchangeevent) {
        if (!signchangeevent.isCancelled()) {
            Player player = signchangeevent.getPlayer();
            short short0 = 250;

            if (signchangeevent.getLine(0).length() >= short0 || signchangeevent.getLine(1).length() >= short0 || signchangeevent.getLine(2).length() >= short0 || signchangeevent.getLine(3).length() >= short0) {
                signchangeevent.setCancelled(true);
                if (!class_38.method_157(player, this.field_189, false)) {
                    class_38.method_159(player, this.field_189, "t: illegal sign character length");
                }
            }
        }

    }
}
