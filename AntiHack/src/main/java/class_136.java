import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: s
public class class_136 implements Listener {

    private int Ξ;
    private static HashMap<Player, Double> Ξ;

    public class_136() {
        this.field_228 = 20;
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        method_650(player);
    }

    public static void Ξ(Player player) {
        if (player != null) {
            class_136.field_229.remove(player);
            class_36.method_138(player, "accuracy=hits");
            class_36.method_138(player, "accuracy=miss");
            class_36.method_138(player, "accuracy=count");
        }
    }

    public static double Ξ(Player player) {
        return player == null ? 0.0D : (!class_136.field_229.containsKey(player) ? 0.0D : ((Double) class_136.field_229.get(player)).doubleValue());
    }

    private void Ξ(Player player, String s) {
        int i = class_36.method_133(player, "accuracy=" + s) + 1;

        class_36.method_134(player, "accuracy=" + s, i);
        int j = class_36.method_133(player, "accuracy=count") + 1;

        class_36.method_134(player, "accuracy=count", j);
        if (j >= this.field_228) {
            int k = this.method_653(player, "hits");
            int l = this.method_653(player, "miss");
            int i1 = k + l;
            double d0 = Double.valueOf((double) k).doubleValue() / Double.valueOf((double) i1).doubleValue() * 100.0D;

            class_136.field_229.put(player, Double.valueOf(d0));
            class_36.method_138(player, "accuracy=hits");
            class_36.method_138(player, "accuracy=miss");
            class_36.method_138(player, "accuracy=count");
        }

    }

    private int Ξ(Player player, String s) {
        return player == null ? 0 : (s == null ? 0 : class_36.method_133(player, "accuracy=" + s));
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && entitydamagebyentityevent.getEntity() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (class_67.method_363(player)) {
                return;
            }

            Player player1 = (Player) entitydamagebyentityevent.getEntity();

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                boolean flag = true;

                for (int i = 0; i <= 2; ++i) {
                    if (!class_66.HHΞ(player, 1.0D, (double) i, 1.0D) || !class_66.HHΞ(player1, 1.0D, (double) i, 1.0D)) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    double d0 = class_69.method_391(player.getLocation(), player1.getLocation());

                    if (d0 >= 3.0D) {
                        this.method_652(player, "hits");
                    }
                }

                class_45.method_211(player, "accuracy");
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (!class_67.method_363(player) && method_656(player) != 0) {
            if (playerinteractevent.getAction() == Action.LEFT_CLICK_AIR && class_45.method_209(player, "accuracy")) {
                long i = class_45.method_210(player, "accuracy");

                if (i > 500L) {
                    this.method_652(player, "miss");
                }
            }

        }
    }

    public static int Ξ(Player player) {
        int i = 0;
        double d0 = 6.0D;

        if (player == null) {
            return 0;
        } else {
            Iterator iterator = player.getNearbyEntities(d0, d0, d0).iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (entity != null && entity instanceof Player) {
                    ++i;
                }
            }

            return i;
        }
    }

    static {
        class_136.field_229 = new HashMap();
    }
}
