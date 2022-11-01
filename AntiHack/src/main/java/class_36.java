import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: bF
public class class_36 implements Listener {

    private static HashMap<Player, HashMap<String, Integer>> Ξ;

    public static void Ξ() {
        class_36.field_41.clear();
    }

    public static boolean Ξ(Player player, String s) {
        return player != null && s != null ? class_36.field_41.containsKey(player) && ((HashMap) class_36.field_41.get(player)).containsKey(s) : false;
    }

    public static int Ξ(Player player, String s) {
        return player != null && s != null && class_36.field_41.containsKey(player) && ((HashMap) class_36.field_41.get(player)).containsKey(s) ? ((Integer) ((HashMap) class_36.field_41.get(player)).get(s)).intValue() : 0;
    }

    public static void Ξ(Player player, String s, int i) {
        if (player != null && s != null && method_133(player, s) != i) {
            if (!class_36.field_41.containsKey(player)) {
                class_36.field_41.put(player, new HashMap());
            }

            if (class_36.field_41.containsKey(player)) {
                HashMap hashmap = (HashMap) class_36.field_41.get(player);

                hashmap.put(s, Integer.valueOf(i));
                class_36.field_41.put(player, hashmap);
            }

        }
    }

    public static void Π(Player player, String s, int i) {
        if (player != null && s != null && i >= 0) {
            if (class_39.method_169(player, s + "=" + player.getUniqueId().toString() + "=remove")) {
                if (method_133(player, s) > 0) {
                    method_134(player, s, 0);
                }

                class_39.method_170(player, s + "=" + player.getUniqueId().toString() + "=remove", i);
            }

            method_134(player, s, method_133(player, s) + 1);
        }
    }

    public static int Π(Player player, String s) {
        return class_39.method_168(player, s + "=" + player.getUniqueId().toString() + "=remove");
    }

    public static boolean Π(Player player, String s) {
        return method_133(player, s) >= 0;
    }

    public static void Ξ(Player player, String s) {
        if (player != null && s != null && class_36.field_41.containsKey(player) && ((HashMap) class_36.field_41.get(player)).containsKey(s)) {
            HashMap hashmap = (HashMap) class_36.field_41.get(player);

            hashmap.put(s, Integer.valueOf(0));
            class_36.field_41.put(player, hashmap);
            class_39.method_171(player, s + "=" + player.getUniqueId().toString() + "=remove");
        }
    }

    public static void Ξ(Player player) {
        if (player != null) {
            class_36.field_41.remove(player);
        }
    }

    @EventHandler
    public void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        method_139(player);
    }

    static {
        class_36.field_41 = new HashMap();
    }
}
