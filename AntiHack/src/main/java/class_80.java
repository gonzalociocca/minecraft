import java.util.Iterator;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: G
public class class_80 implements Listener {

    private Enums.HackType Ξ;

    public class_80() {
        this.field_157 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_157, entity) || class_137.method_665(player, 6.0D) > 12 || !class_120.method_572("KillAura.check_entity_raytrace")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_69.method_391(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D) {
                    for (double d1 = 0.0D; d1 <= d0; ++d1) {
                        Block block = player.getLocation().add(player.getLocation().getDirection().multiply(d1)).getBlock();
                        Iterator iterator = player.getNearbyEntities(d1, d1, d1).iterator();

                        while (iterator.hasNext()) {
                            Entity entity1 = (Entity) iterator.next();

                            if (class_67.method_353(entity1) && entity1 != entity) {
                                double d2 = class_67.method_356(player, entity1);
                                double d3 = entity1.getLocation().distance(block.getLocation());
                                double d4 = entity1.getLocation().distance(entity.getLocation());

                                if (d4 >= 1.0D && d3 <= 0.8D && d2 > 0.0D && d2 <= 7.5D) {
                                    class_92.method_493(player);
                                    class_38.method_159(player, this.field_157, "t: entity raytrace, r: " + d2 + ", e_d_b: " + d3 + ", e_d_e: " + d4);
                                    if (class_123.method_583(player, this.field_157)) {
                                        entitydamagebyentityevent.setCancelled(true);
                                    }

                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
