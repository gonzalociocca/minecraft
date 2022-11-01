import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: z
public class class_143 implements Listener {

    private int Ξ;
    private Enums.HackType Ξ;

    public class_143() {
        this.field_241 = 10;
        this.field_242 = Enums.HackType.KillAura;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_242, entity) || class_137.method_665(player, 6.0D) > 12 || !class_120.method_572("KillAura.check_aim_accuracy")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D && !class_67.method_358(player, entity)) {
                    long i = class_140.method_676(player, false);

                    if (i > 0L && i <= 550L) {
                        double d1 = Math.abs(class_67.BPPΠ(player, entity));
                        int j = class_36.method_133(player, this.field_242.toString() + "aim_accuracy=hit");
                        int k = class_36.method_133(player, this.field_242.toString() + "aim_accuracy=miss");

                        if (d1 <= 3.0D) {
                            ++j;
                            class_36.method_134(player, this.field_242.toString() + "aim_accuracy=hit", j);
                        } else {
                            ++k;
                            class_36.method_134(player, this.field_242.toString() + "aim_accuracy=miss", k);
                        }

                        int l = j + k;

                        if (l >= this.field_241) {
                            class_36.method_138(player, this.field_242.toString() + "aim_accuracy=hit");
                            class_36.method_138(player, this.field_242.toString() + "aim_accuracy=miss");
                            double d2 = Double.valueOf((double) j).doubleValue() / Double.valueOf((double) l).doubleValue() * 100.0D;

                            if (d2 >= 90.0D) {
                                class_92.method_493(player);
                                class_38.method_159(player, this.field_242, "t: aim-accuracy, p: " + d2);
                                if (class_123.method_583(player, this.field_242)) {
                                    entitydamagebyentityevent.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
