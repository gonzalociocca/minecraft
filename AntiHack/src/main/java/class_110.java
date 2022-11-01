import java.util.HashMap;
import java.util.Iterator;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: V
public class class_110 implements Listener {

    private static Enums.HackType Ξ;
    private static HashMap<Player, Integer> Ξ;
    private static int Ξ;

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_110.field_186.remove(player);
    }

    public static void Ξ() {
        if (class_110.field_187 <= 0) {
            class_110.field_187 = 20;
            Iterator iterator = Bukkit.getOnlinePlayers().iterator();

            while (iterator.hasNext()) {
                Player player = (Player) iterator.next();

                if (!class_38.method_157(player, class_110.field_185, false) && class_69.method_388(player.getLocation(), class_69.method_383(player))) {
                    int i = class_87.method_472(player);

                    if (class_110.field_186.containsKey(player)) {
                        int j = ((Integer) class_110.field_186.get(player)).intValue();
                        int k = Math.abs(i - j);

                        if (k <= 5 && i >= 400 && j >= 400) {
                            class_36.method_135(player, class_110.field_185.toString() + "=scheduler=attempts", 240);
                            if (class_36.method_133(player, class_110.field_185.toString() + "=scheduler=attempts") >= 12) {
                                class_38.method_159(player, class_110.field_185, "t: ping-spoof(scheduler), avg: " + k);
                            }
                        } else {
                            class_36.method_138(player, class_110.field_185.toString() + "=scheduler=attempts");
                        }
                    }

                    class_110.field_186.put(player, Integer.valueOf(i));
                }
            }
        } else {
            --class_110.field_187;
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (method_532(player)) {
                double d0 = class_69.method_391(playermoveevent.getTo(), playermoveevent.getFrom());
                int i = class_87.method_472(player);

                if (i >= 300 && d0 >= 0.215D) {
                    byte b0 = 20;
                    String s = class_110.field_185.toString() + "=moves";

                    class_36.method_135(player, s, b0);
                    int j = class_36.method_133(player, s);
                    boolean flag = j + class_36.method_136(player, s) - 1 == b0 && j == b0;

                    if (flag) {
                        class_36.method_135(player, class_110.field_185.toString() + "=event=attempts", 200);
                        if (class_36.method_133(player, class_110.field_185.toString() + "=event=attempts") == 8) {
                            class_36.method_138(player, class_110.field_185.toString() + "=event=attempts");
                            class_38.method_159(player, class_110.field_185, "t: ping-spoof(event)");
                        }
                    }
                }

            }
        }
    }

    private static boolean Ξ(Player player) {
        return class_55.method_298(player, class_110.field_185) && !class_71.method_400(player) && !class_71.BPΞ(player);
    }

    static {
        class_110.field_185 = Enums.HackType.Exploits;
        class_110.field_186 = new HashMap();
        class_110.field_187 = 0;
    }
}
