import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: X
public class class_114 implements Listener {

    private static Enums.HackType Ξ;

    public static void Ξ(Player player) {
        if (!class_38.method_157(player, class_114.field_191, true)) {
            Location location = player.getLocation();
            Location location1 = class_69.method_383(player);
            GameMode gamemode = player.getGameMode();

            if (class_69.method_388(location, location1) && class_55.method_297(player) && class_85.method_467(player) == 0.0F && !class_71.method_401(player) && (gamemode == GameMode.SURVIVAL || gamemode == GameMode.ADVENTURE) && class_97.method_502() >= 17.5D && !class_15.method_61(player) && !class_71.BPΞ(player) && !class_71.HHHΞ(player) && class_39.method_169(player, class_114.field_191.toString() + "=undetected-movement")) {
                boolean flag = true;

                for (int i = -1; i <= 2; ++i) {
                    if (!class_66.method_346(player, 1.0D, (double) i, 1.0D) || !class_66.method_348(player, 1.0D, (double) i, 1.0D)) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    double d0 = location.distance(location1);
                    long j = class_45.method_210(player, "undetected-movement");

                    if (j == 0L) {
                        class_45.method_211(player, "undetected-movement");
                    }

                    if (d0 > 0.0D && j > 150L) {
                        double d1 = class_89.method_475(location, location1);
                        byte b0 = 3;

                        if (d0 < 0.1D && d1 == 0.0D) {
                            b0 = 2;
                        } else if (d0 < 0.1D && class_71.method_416(player.getLocation())) {
                            b0 = 9;
                        }

                        class_36.method_135(player, "undetected-movement", 10);
                        if (class_36.method_133(player, "undetected-movement") >= b0) {
                            class_38.method_159(player, class_114.field_191, "t: undetected server-side movement, ms: " + j + ", d: " + d0);
                            if (class_123.method_583(player, class_114.field_191)) {
                                class_69.method_386(player, location1);
                                class_69.HHΞ(player);
                            }
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            class_39.method_170(player, class_114.field_191.toString() + "=undetected-movement", 5);
        }

    }

    @EventHandler
    private void Ξ(EntityDamageEvent entitydamageevent) {
        if (entitydamageevent.getEntity() instanceof Player && !entitydamageevent.isCancelled()) {
            Player player = (Player) entitydamageevent.getEntity();

            if (entitydamageevent.getCause() != DamageCause.FALL) {
                class_39.method_170(player, class_114.field_191.toString() + "=undetected-movement", 40);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerFishEvent playerfishevent) {
        if (playerfishevent.getCaught() instanceof Player && !playerfishevent.isCancelled() && playerfishevent.getState() == State.CAUGHT_ENTITY) {
            Player player = (Player) playerfishevent.getCaught();

            class_39.method_170(player, class_114.field_191.toString() + "=undetected-movement", 20);
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            class_45.method_211(player, "undetected-movement");
        }
    }

    static {
        class_114.field_191 = Enums.HackType.Exploits;
    }
}
