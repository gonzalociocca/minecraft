import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: bJ
public class class_45 implements Listener {

    private static HashMap<Player, HashMap<String, Long>> Ξ;

    public static void Ξ() {
        class_45.field_74.clear();
    }

    public static boolean Ξ(Player player, String s) {
        return player != null && s != null ? class_45.field_74.containsKey(player) && ((HashMap) class_45.field_74.get(player)).containsKey(s) : false;
    }

    public static long Ξ(Player player, String s) {
        return player != null && s != null && class_45.field_74.containsKey(player) && ((HashMap) class_45.field_74.get(player)).containsKey(s) ? System.currentTimeMillis() - ((Long) ((HashMap) class_45.field_74.get(player)).get(s)).longValue() : 0L;
    }

    public static void Ξ(Player player, String s) {
        if (!class_45.field_74.containsKey(player)) {
            class_45.field_74.put(player, new HashMap());
        }

        if (class_45.field_74.containsKey(player)) {
            HashMap hashmap = (HashMap) class_45.field_74.get(player);

            hashmap.put(s, Long.valueOf(System.currentTimeMillis()));
            class_45.field_74.put(player, hashmap);
        }

    }

    public static void Ξ(Player player) {
        if (player != null) {
            class_45.field_74.remove(player);
        }
    }

    @EventHandler
    public void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        method_212(player);
    }

    static {
        class_45.field_74 = new HashMap();
    }
}
