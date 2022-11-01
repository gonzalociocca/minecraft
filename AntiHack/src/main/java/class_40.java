import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

// $FF: renamed from: ag
public class class_40 implements Listener {

    private Enums.HackType Ξ;

    public class_40() {
        this.field_55 = Enums.HackType.IllegalPosition;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_390(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (!class_38.method_157(player, this.field_55, true)) {
                float f = player.getLocation().getPitch();
                boolean flag = false;

                class_36.method_135(player, this.field_55.toString() + "=rotate", 5);
                int i = class_36.method_133(player, this.field_55.toString() + "=rotate");

                if (i >= 50) {
                    class_38.method_159(player, this.field_55, "t: rotations, a: " + i);
                    flag = true;
                }

                if (Math.abs(f) >= 91.0F) {
                    class_38.method_159(player, this.field_55, "t: location, p: " + f);
                    flag = true;
                }

                if (flag && class_123.method_583(player, this.field_55)) {
                    Location location = player.getLocation();

                    location.setYaw(0.0F);
                    location.setPitch(0.0F);
                    class_69.method_386(player, location);
                }

            }
        }
    }
}
