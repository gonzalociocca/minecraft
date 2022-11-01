import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: aV
public class class_21 implements Listener {

    public static final int Ξ = 3;

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (class_128.method_610("use_teleport_protection")) {
            TeleportCause teleportcause = playerteleportevent.getCause();

            if (teleportcause == TeleportCause.COMMAND || teleportcause == TeleportCause.ENDER_PEARL || teleportcause == TeleportCause.PLUGIN) {
                Player player = playerteleportevent.getPlayer();

                if (class_39.method_169(player, "teleport") && !class_49.method_247(player)) {
                    double d0 = class_69.method_391(playerteleportevent.getTo(), playerteleportevent.getFrom());

                    if (d0 >= 0.05D) {
                        class_38.method_160(player, Enums.HackType.Fly, 3);
                        class_38.method_160(player, Enums.HackType.Clip, 3);
                        class_38.method_160(player, Enums.HackType.Speed, 3);
                        class_38.method_160(player, Enums.HackType.Phase, 3);
                        class_38.method_160(player, Enums.HackType.NoFall, 3);
                        class_38.method_160(player, Enums.HackType.MorePackets, 3);
                        class_38.method_160(player, Enums.HackType.BlockReach, 3);
                        class_38.method_160(player, Enums.HackType.BoatMove, 3);
                        class_38.method_160(player, Enums.HackType.IllegalPosition, 3);
                        class_38.method_160(player, Enums.HackType.Sprint, 3);
                    }
                }
            }
        }

    }

    public static void Ξ(Player player) {
        class_39.method_170(player, "teleport", 1);
    }
}
