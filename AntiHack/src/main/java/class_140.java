import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: w
public class class_140 implements Listener {

    private int Ξ;
    private static HashMap<Player, Long> Ξ;
    private static HashMap<Player, Long> Π;
    private static HashMap<Player, Long> HHΞ;

    public class_140() {
        this.field_235 = 7;
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_140.field_237.remove(player);
        class_140.field_236.remove(player);
        class_140.HHΞ.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (playerinteractevent.getAction() == Action.LEFT_CLICK_AIR) {
            if (class_45.method_209(player, "heuristics=time_between_clicks")) {
                long i = class_45.method_210(player, "heuristics=time_between_clicks");

                class_140.HHΞ.put(player, Long.valueOf(i));
                if (!class_140.field_236.containsKey(player)) {
                    class_140.field_236.put(player, Long.valueOf(i));
                } else {
                    class_140.field_236.put(player, Long.valueOf(((Long) class_140.field_236.get(player)).longValue() + i));
                }

                int j = class_36.method_133(player, "heuristics=time_between_clicks") + 1;

                class_36.method_134(player, "heuristics=time_between_clicks", j);
                if (j >= this.field_235) {
                    class_36.method_138(player, "heuristics=time_between_clicks");
                    if (class_140.field_236.containsKey(player)) {
                        long k = ((Long) class_140.field_236.get(player)).longValue() / (long) this.field_235;

                        class_140.field_237.put(player, Long.valueOf(k));
                        class_140.field_236.remove(player);
                    }
                }
            }

            class_45.method_211(player, "heuristics=time_between_clicks");
        }

    }

    public static long Ξ(Player player, boolean flag) {
        return player == null ? 0L : (!flag && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251 ? 1L : (!class_140.field_237.containsKey(player) ? 0L : ((Long) class_140.field_237.get(player)).longValue()));
    }

    public static long Ξ(Player player) {
        return player != null && class_140.HHΞ.containsKey(player) ? ((Long) class_140.HHΞ.get(player)).longValue() : 0L;
    }

    static {
        class_140.field_236 = new HashMap();
        class_140.field_237 = new HashMap();
        class_140.HHΞ = new HashMap();
    }
}
