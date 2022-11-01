import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

// $FF: renamed from: aP
public class class_15 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageEvent entitydamageevent) {
        if (entitydamageevent.getEntity() instanceof Player && !entitydamageevent.isCancelled()) {
            Player player = (Player) entitydamageevent.getEntity();
            DamageCause damagecause = entitydamageevent.getCause();
            boolean flag = class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_251 && class_101.method_512() != class_101.HHΞ;
            boolean flag1 = flag && class_101.method_512() != class_101.field_252;

            if (damagecause != DamageCause.FIRE && damagecause != DamageCause.FIRE_TICK && (damagecause != DamageCause.FALL || !class_71.method_416(player.getLocation()) && !class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D)) && !class_71.method_416(player.getLocation().add(0.0D, -1.5D, 0.0D)))) {
                if (flag && damagecause == DamageCause.HOT_FLOOR || flag1 && damagecause == DamageCause.MAGIC || damagecause == DamageCause.CONTACT) {
                    method_62(player, 30);
                }
            } else {
                method_62(player, 10);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (!playermoveevent.isCancelled()) {
            Player player = playermoveevent.getPlayer();

            for (int i = -2; i <= 1; ++i) {
                if (class_66.method_350(player.getLocation().add(0.0D, (double) i, 0.0D), Material.CACTUS, 1.0D) || class_66.method_350(player.getLocation().add(0.0D, (double) i, 0.0D), Material.CACTUS, 0.3D)) {
                    method_62(player, 30);
                    break;
                }
            }
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "floor=protection");
    }

    public static void Ξ(Player player, int i) {
        class_39.method_170(player, "floor=protection", i);
    }
}
