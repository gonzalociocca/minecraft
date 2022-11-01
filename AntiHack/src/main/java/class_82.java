import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: H
public class class_82 implements Listener {

    private Enums.HackType Ξ;

    public class_82() {
        this.field_158 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_158, entity) || !class_120.method_572("KillAura.check_hitbox") || class_71.method_414(player, 6.0D) > 5) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                byte b0 = 0;
                double d0 = (double) Math.abs(player.getLocation().getPitch());
                double d1 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d1 >= 1.0D && d0 >= 80.0D) {
                    b0 = 1;
                } else if (d1 >= 1.5D && d0 >= 70.0D) {
                    b0 = 2;
                } else if (d1 >= 2.0D && d0 >= 60.0D) {
                    b0 = 3;
                } else if (d1 >= 2.5D && d0 >= 50.0D) {
                    b0 = 4;
                } else if (d1 >= 3.0D && d0 >= 40.0D) {
                    b0 = 5;
                } else if (d1 >= 3.5D && d0 >= 35.0D) {
                    b0 = 6;
                }

                if (b0 != 0) {
                    class_92.method_493(player);
                    class_36.method_135(player, this.field_158.toString() + "=hitbox", 300);
                    if (class_36.method_133(player, this.field_158.toString() + "=hitbox") >= 2) {
                        class_38.method_159(player, this.field_158, "t: hit-box, c: " + b0 + ", p: " + d0 + ", d: " + d1);
                        if (class_123.method_583(player, this.field_158)) {
                            entitydamagebyentityevent.setCancelled(true);
                        }
                    }
                }
            }
        }

    }
}
