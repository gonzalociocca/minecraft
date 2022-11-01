import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

// $FF: renamed from: u
public class class_138 implements Listener {

    private HashMap<Player, Float> Ξ;
    private static HashMap<Player, Float> Π;

    public class_138() {
        this.field_232 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_138.field_233.remove(player);
        this.field_232.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        Player player = playerteleportevent.getPlayer();

        class_138.field_233.remove(player);
        this.field_232.remove(player);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        Player player = playermoveevent.getPlayer();
        float f = player.getLocation().getPitch();

        if (this.field_232.containsKey(player)) {
            float f1 = Math.abs(((Float) this.field_232.get(player)).floatValue() - f);

            class_138.field_233.put(player, Float.valueOf(f1));
            class_39.method_170(player, "heuristics=pitch_rate", 4);
        }

        this.field_232.put(player, Float.valueOf(f));
    }

    public static float Ξ(Player player) {
        return player != null && !class_39.method_169(player, "heuristics=pitch_rate") && class_138.field_233.containsKey(player) ? ((Float) class_138.field_233.get(player)).floatValue() : 0.0F;
    }

    static {
        class_138.field_233 = new HashMap();
    }
}
