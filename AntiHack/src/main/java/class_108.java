import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: U
public class class_108 implements Listener {

    private Enums.HackType Ξ;

    public class_108() {
        this.field_183 = Enums.HackType.Exploits;
    }

    @EventHandler
    private void Ξ(PlayerLoginEvent playerloginevent) {
        Player player = playerloginevent.getPlayer();

        if (class_69.method_382(player.getLocation())) {
            playerloginevent.disallow(Result.KICK_OTHER, "illegal player location");
        }

    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (class_69.method_382(player.getLocation())) {
                playerteleportevent.setCancelled(true);
                if (!class_38.method_157(player, this.field_183, false)) {
                    class_38.method_159(player, this.field_183, "t: illegal player location");
                }
            }

        }
    }
}
