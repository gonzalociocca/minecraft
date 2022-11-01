import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// $FF: renamed from: bM
public class class_51 {

    private static HashMap<UUID, Integer> Ξ;

    public static Player[] Ξ() {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = class_51.field_88.keySet().iterator();

        while (iterator.hasNext()) {
            UUID uuid = (UUID) iterator.next();
            Player player = Bukkit.getPlayer(uuid);

            if (method_274((Player) player)) {
                arraylist.add((Player) player);
            }
        }

        return (Player[]) arraylist.toArray(new Player[0]);
    }

    public static boolean Ξ(Player player) {
        return player == null ? false : class_51.field_88.containsKey(player.getUniqueId());
    }

    public static void Ξ(Player player, int i) {
        if (player != null && i >= 0 && i <= 1) {
            if (!method_271(player)) {
                class_51.field_88.put(player.getUniqueId(), Integer.valueOf(i));
                player.sendMessage(class_125.method_599("verbose_enable"));
            } else {
                class_51.field_88.remove(player.getUniqueId());
                player.sendMessage(class_125.method_599("verbose_disable"));
            }

        }
    }

    public static void Ξ(Player player, boolean flag, int i) {
        if (player != null && i >= 0 && i <= 1) {
            if (flag) {
                if (!method_271(player)) {
                    class_51.field_88.put(player.getUniqueId(), Integer.valueOf(i));
                    player.sendMessage(class_125.method_599("verbose_enable"));
                }
            } else if (method_271(player)) {
                class_51.field_88.remove(player.getUniqueId());
                player.sendMessage(class_125.method_599("verbose_disable"));
            }

        }
    }

    public static boolean Π(Player player) {
        return player != null && class_51.field_88.containsKey(player.getUniqueId()) ? ((Integer) class_51.field_88.get(player.getUniqueId())).intValue() == 1 || ((Integer) class_51.field_88.get(player.getUniqueId())).intValue() == 0 && class_47.method_229(player, Enums.Permission.verbose) : false;
    }

    static {
        class_51.field_88 = new HashMap();
    }
}
