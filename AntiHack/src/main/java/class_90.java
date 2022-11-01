import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: L
public class class_90 implements Listener {

    private Enums.HackType Ξ;

    public class_90() {
        this.field_163 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (class_38.method_157(player, this.field_163, true) || !class_120.method_572("KillAura.check_impossible_hits")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                boolean flag = false;

                if (class_71.PPPΠ(player)) {
                    class_36.method_135(player, this.field_163.toString() + "=impossible_hits=dead", 100);
                    if (class_36.method_133(player, this.field_163.toString() + "=impossible_hits=dead") >= 3) {
                        class_38.method_159(player, this.field_163, "t: impossible hits, r: death");
                    }

                    flag = true;
                } else if (player.isSleeping()) {
                    class_38.method_159(player, this.field_163, "t: impossible hits, r: sleeping");
                    flag = true;
                } else if (!class_67.method_358(player, entitydamagebyentityevent.getEntity()) && player.isBlocking() && class_101.method_512() != class_101.field_250) {
                    class_38.method_159(player, this.field_163, "t: impossible hits, r: item usage");
                    flag = true;
                } else if (class_71.OOOΠ(player)) {
                    class_38.method_159(player, this.field_163, "t: impossible hits, r: cursor usage");
                    flag = true;
                } else if (class_71.PPΞ(player)) {
                    class_38.method_159(player, this.field_163, "t: impossible hits, r: inventory usage");
                    flag = true;
                }

                if (flag) {
                    class_92.method_493(player);
                    if (class_123.method_583(player, this.field_163)) {
                        entitydamagebyentityevent.setCancelled(true);
                    }
                }
            }
        }

    }
}
