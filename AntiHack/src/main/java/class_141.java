import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

// $FF: renamed from: x
public class class_141 implements Listener {

    private HashMap<Player, Float> Ξ;
    private static HashMap<Player, Float> Π;

    public class_141() {
        this.field_238 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_141.field_239.remove(player);
        this.field_238.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        Player player = playerteleportevent.getPlayer();

        class_141.field_239.remove(player);
        this.field_238.remove(player);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        Player player = playermoveevent.getPlayer();
        float f = player.getLocation().getYaw();

        if (this.field_238.containsKey(player)) {
            float f1 = Math.abs(((Float) this.field_238.get(player)).floatValue() - f);

            class_141.field_239.put(player, Float.valueOf(f1));
            class_39.method_170(player, "heuristics=yaw_rate", 4);
        }

        this.field_238.put(player, Float.valueOf(f));
    }

    public static float Ξ(Player player) {
        return player != null && !class_39.method_169(player, "heuristics=yaw_rate") && class_141.field_239.containsKey(player) && ((Float) class_141.field_239.get(player)).floatValue() < 300.0F ? ((Float) class_141.field_239.get(player)).floatValue() : 0.0F;
    }

    static {
        class_141.field_239 = new HashMap();
    }
}
