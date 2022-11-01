import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: p
public class class_133 implements Listener {

    private static Enums.HackType Ξ;
    private static int Ξ;

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (!class_38.method_157(player, class_133.field_221, true) && !player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            if (playerinteractevent.getAction() == Action.LEFT_CLICK_AIR) {
                method_638(player);
                method_639(player);
            }

        }
    }

    private static void Ξ(Player player, String s) {
        if (player != null && s != null) {
            class_36.method_135(player, class_133.field_221.toString() + "=cancel", class_133.field_222 * 4);
            int i = class_36.method_133(player, class_133.field_221.toString() + "=cancel");

            if (i >= 2) {
                class_38.method_159(player, class_133.field_221, s);
            }

        }
    }

    private static long Ξ(Player player, int i) {
        if (player == null) {
            return 0L;
        } else {
            long j = 0L;

            if (class_45.method_209(player, class_133.field_221.toString() + "=time_between_clicks=" + i)) {
                j = class_45.method_210(player, class_133.field_221.toString() + "=time_between_clicks=" + i);
            }

            class_45.method_211(player, class_133.field_221.toString() + "=time_between_clicks=" + i);
            return j;
        }
    }

    public static int Ξ(Player player) {
        return class_36.method_133(player, class_133.field_221.toString() + "=cps=clicks") / class_133.field_222;
    }

    private static void Ξ(Player player) {
        if (player != null && class_120.method_572("FastClicks.check_cps")) {
            long i = method_636(player, 1);
            int j = class_120.method_571("FastClicks.cps_limit");

            class_36.method_135(player, class_133.field_221.toString() + "=cps=clicks", class_133.field_222);
            int k = class_36.method_133(player, class_133.field_221.toString() + "=cps=clicks");
            int l = method_637(player);

            if (k == 1) {
                class_36.method_138(player, class_133.field_221.toString() + "=cps=time");
            }

            if (i <= 100L) {
                class_36.method_135(player, class_133.field_221.toString() + "=cps=time", class_133.field_222);
            }

            int i1 = class_36.method_133(player, class_133.field_221.toString() + "=cps=time");

            if (l >= class_41.method_179(j) && i1 >= 30) {
                class_36.method_138(player, class_133.field_221.toString() + "=cps=clicks");
                class_36.method_138(player, class_133.field_221.toString() + "=cps=time");
                method_635(player, "t: cps, l: " + j + ", tm: " + i1);
            }

        }
    }

    private static void Π(Player player) {
        if (player != null && class_120.method_572("FastClicks.check_click_time")) {
            long i = method_636(player, 2);
            int j;

            if (i <= 50L) {
                class_36.method_135(player, class_133.field_221.toString() + "=click_time=50", class_133.field_222);
                j = class_36.method_133(player, class_133.field_221.toString() + "=click_time=50");
                if (j == 25) {
                    class_36.method_138(player, class_133.field_221.toString() + "=click_time=50");
                    method_635(player, "t: click-time, ms: 50, cps: " + method_637(player));
                    return;
                }
            }

            if (i <= 100L) {
                class_36.method_135(player, class_133.field_221.toString() + "=click_time=100", class_133.field_222);
                j = class_36.method_133(player, class_133.field_221.toString() + "=click_time=100");
                if (j == 45) {
                    class_36.method_138(player, class_133.field_221.toString() + "=click_time=100");
                    method_635(player, "t: click-time, ms: 100, cps: " + method_637(player));
                }
            }

        }
    }

    static {
        class_133.field_221 = Enums.HackType.FastClicks;
        class_133.field_222 = 80;
    }
}
