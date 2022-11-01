import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

// $FF: renamed from: S
public class class_104 implements Listener {

    private Enums.HackType Ξ;

    public class_104() {
        this.field_181 = Enums.HackType.Exploits;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerLoginEvent playerloginevent) {
        Player player = playerloginevent.getPlayer();

        class_39.method_170(player, "chat=cooldown=join", 30);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerChatEvent playerchatevent) {
        if (!playerchatevent.isCancelled()) {
            Player player = playerchatevent.getPlayer();
            class_101 class_101 = class_101.method_512();
            int i = playerchatevent.getMessage().length();
            short short0 = 100;

            if (class_101 != class_101.field_254 && class_101 != class_101.field_250 && class_101 != class_101.field_251 && class_101 != class_101.HHΞ && class_101 != class_101.field_252) {
                short0 = 256;
            }

            if (i > short0) {
                playerchatevent.setCancelled(true);
                if (!class_38.method_157(player, this.field_181, false)) {
                    class_38.method_159(player, this.field_181, "t: illegal chat message length, len: " + i + ", l: " + short0);
                }
            } else if (class_71.PPPΠ(player)) {
                playerchatevent.setCancelled(true);
                if (!class_38.method_157(player, this.field_181, false)) {
                    class_38.method_159(player, this.field_181, "t: using chat will being dead");
                }
            } else if (!class_39.method_169(player, "chat=cooldown=join")) {
                playerchatevent.setCancelled(true);
            }

        }
    }
}
