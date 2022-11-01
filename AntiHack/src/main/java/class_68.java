import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: A
public class class_68 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;
    private HashMap<Player, Double> Π;
    private HashMap<Player, Double> HHΞ;
    private HashMap<Player, Double> HΞ;

    public class_68() {
        this.field_131 = Enums.HackType.KillAura;
        this.field_132 = new HashMap();
        this.field_133 = new HashMap();
        this.HHΞ = new HashMap();
        this.field_134 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_132.remove(player);
        this.field_133.remove(player);
        this.HHΞ.remove(player);
        this.field_134.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_132.remove(player);
                this.field_133.remove(player);
                this.HHΞ.remove(player);
                this.field_134.remove(player);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_131, entity) || class_137.method_665(player, 6.0D) > 12 || !class_120.method_572("KillAura.check_aim_consistency")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                if (d0 >= 1.5D && !class_67.method_358(player, entity)) {
                    double d1 = class_67.method_357(player, entity);
                    double d2 = class_67.method_356(player, entity);
                    double d3 = class_67.HHΞ(player, entity);
                    double d4 = (double) Math.abs(player.getLocation().getPitch());
                    long i = class_140.method_676(player, false);

                    if (this.field_132.containsKey(player) && this.field_133.containsKey(player) && this.HHΞ.containsKey(player) && this.field_134.containsKey(player)) {
                        double d5 = class_89.method_482(((Double) this.field_132.get(player)).doubleValue(), d1);
                        double d6 = class_89.method_482(((Double) this.field_133.get(player)).doubleValue(), d2);
                        double d7 = class_89.method_482(((Double) this.HHΞ.get(player)).doubleValue(), d3);
                        double d8 = class_89.method_482(((Double) this.field_134.get(player)).doubleValue(), d4);

                        if (d5 < 0.2D && d1 <= 0.5D && d6 <= 4.0D && d2 <= 25.0D && d7 < 0.01D && d3 >= 0.999D && d8 > 0.0D && d8 < 8.0D && i > 0L && i <= 550L) {
                            Location location = class_67.method_360(player, entity);
                            double d9 = location == null ? 0.0D : Math.abs(location.getX());
                            double d10 = location == null ? 0.0D : Math.abs(location.getZ());
                            double d11 = class_38.method_154(player, this.field_131) >= 2 ? 6.0D : 7.0D;

                            if (d9 >= d11 || d10 >= d11) {
                                class_92.method_493(player);
                                byte b0 = 0;
                                boolean flag = entity instanceof Player;

                                if (class_137.method_660(player)) {
                                    b0 = 1;
                                } else if (class_71.method_397(player) && (flag && class_71.method_398((Player) entity) || !flag)) {
                                    b0 = 2;
                                } else if (class_137.method_663(player)) {
                                    b0 = 3;
                                } else if (flag && class_71.method_397((Player) entity) && class_71.method_398((Player) entity)) {
                                    b0 = 4;
                                } else if (class_137.method_661(player)) {
                                    b0 = 5;
                                } else if (class_137.HHΞ(player)) {
                                    b0 = 6;
                                } else if (class_137.method_662(player)) {
                                    b0 = 7;
                                } else if (!flag && class_137.method_664(entity)) {
                                    b0 = 8;
                                }

                                if (b0 != 0) {
                                    class_38.method_159(player, this.field_131, "t: aim-consistency, dir: " + d5 + ", rot: " + d6 + ", ang: " + d7 + ", x: " + d9 + ", z: " + d10 + ", t_b_c: " + i + ", pit: " + d8 + ", c: " + b0);
                                    if (class_123.method_583(player, this.field_131)) {
                                        entitydamagebyentityevent.setDamage(0.0D);
                                    }
                                }
                            }
                        }
                    }

                    this.field_132.put(player, Double.valueOf(d1));
                    this.field_133.put(player, Double.valueOf(d2));
                    this.HHΞ.put(player, Double.valueOf(d3));
                    this.field_134.put(player, Double.valueOf(d4));
                }
            }
        }

    }
}
