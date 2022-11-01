import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: bG
public class class_39 implements Listener {

    private static HashMap<Player, HashMap<String, Integer>> Ξ;

    public static void Ξ() {
        class_39.field_52.clear();
    }

    public static boolean Ξ(Player player, String s) {
        return player == null ? false : (s == null ? false : class_39.field_52.containsKey(player) && ((HashMap) class_39.field_52.get(player)).containsKey(s));
    }

    public static int Ξ(Player player, String s) {
        return player != null && s != null && class_39.field_52.containsKey(player) && ((HashMap) class_39.field_52.get(player)).containsKey(s) ? ((Integer) ((HashMap) class_39.field_52.get(player)).get(s)).intValue() : 0;
    }

    public static boolean Π(Player player, String s) {
        return method_168(player, s) == 0;
    }

    public static void Ξ(Player player, String s, int i) {
        if (player != null && s != null && method_168(player, s) != i && i >= 0) {
            if (!class_39.field_52.containsKey(player)) {
                class_39.field_52.put(player, new HashMap());
            }

            if (class_39.field_52.containsKey(player)) {
                HashMap hashmap = (HashMap) class_39.field_52.get(player);

                hashmap.put(s, Integer.valueOf(i));
                class_39.field_52.put(player, hashmap);
            }

        }
    }

    public static void Ξ(Player player, String s) {
        if (player != null && s != null && class_39.field_52.containsKey(player) && ((HashMap) class_39.field_52.get(player)).containsKey(s)) {
            HashMap hashmap = (HashMap) class_39.field_52.get(player);

            hashmap.put(s, Integer.valueOf(0));
            class_39.field_52.put(player, hashmap);
        }
    }

    public static void Π() {
        Iterator iterator = class_39.field_52.keySet().iterator();

        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();
            Iterator iterator1 = ((HashMap) class_39.field_52.get(player)).keySet().iterator();

            while (iterator1.hasNext()) {
                String s = (String) iterator1.next();
                int i = ((Integer) ((HashMap) class_39.field_52.get(player)).get(s)).intValue();

                if (i > 0) {
                    ((HashMap) class_39.field_52.get(player)).put(s, Integer.valueOf(i - 1));
                }
            }
        }

    }

    public static void Ξ(Player player) {
        if (player != null) {
            class_39.field_52.remove(player);
        }
    }

    @EventHandler
    public void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        method_173(player);
    }

    static {
        class_39.field_52 = new HashMap();
    }
}
