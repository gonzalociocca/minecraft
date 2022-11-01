import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

// $FF: renamed from: aU
public class class_20 implements Listener {

    private static int Ξ;
    private static int Π;

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();
            Location location = playermoveevent.getTo();
            Location location1 = playermoveevent.getFrom();
            double d0 = location.getY() - location1.getY();
            double d1 = class_89.method_475(location, location1);

            if ((!class_71.method_416(player.getLocation()) || class_66.method_352(player, 1)) && (d0 < 0.0D || d1 < 0.4D || class_71.HHΞ(player) || class_71.BPPΠ(player))) {
                this.method_73(player);
                this.method_74(player);
            } else {
                class_39.method_171(player, "slime_block=is");
                class_39.method_171(player, "slime_block=was");
            }

        }
    }

    private void Ξ(Player player) {
        if (player != null) {
            if (class_66.method_352(player, 2)) {
                class_39.method_170(player, "slime_block=is", class_20.field_18);
                class_39.method_170(player, "slime_block=was", class_20.field_19);
            }

        }
    }

    private void Π(Player player) {
        if (player != null) {
            if (class_39.method_168(player, "slime_block") <= 2 && class_39.method_169(player, "slime_block=cooldown")) {
                if (class_66.method_352(player, 60)) {
                    class_39.method_170(player, "slime_block=is", class_20.field_18);
                    class_39.method_170(player, "slime_block=was", class_20.field_19);
                }

                class_39.method_170(player, "slime_block=cooldown", 5);
            }

        }
    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "slime_block=is") || class_66.method_352(player, 2);
    }

    public static boolean Π(Player player) {
        return !class_39.method_169(player, "slime_block=was") || class_66.method_352(player, 2);
    }

    static {
        class_20.field_18 = 60;
        class_20.field_19 = 120;
    }
}
