import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: aj
public class class_46 implements Listener {

    private static Enums.HackType Ξ;
    private HashMap<Player, Float> Ξ;

    public class_46() {
        this.field_76 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_76.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_76.remove(player);
            }
        }

    }

    @EventHandler
    private void Ξ(EntityDamageEvent entitydamageevent) {
        if (entitydamageevent.getEntity() instanceof Player && entitydamageevent.getCause() == DamageCause.FALL) {
            Player player = (Player) entitydamageevent.getEntity();

            class_39.method_170(player, class_46.field_75.toString() + "=land", 10);
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (this.method_221(player)) {
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d0 = location.getY() - location1.getY();
                double d1 = location.getY() - (double) location.getBlockY();
                boolean flag = true;

                int i;

                for (i = -2; i <= 2; ++i) {
                    if (class_71.method_416(player.getLocation().add(0.0D, (double) i, 0.0D))) {
                        flag = false;
                        break;
                    }
                }

                this.method_219(player, d0, location1);
                this.method_220(player, d0, d1, location1);
                i = class_71.method_410(player);
                if (class_120.method_572("NoFall.check_fall") && flag && d0 <= -0.1D && this.field_76.containsKey(player)) {
                    float f = player.getFallDistance() - ((Float) this.field_76.get(player)).floatValue();

                    if (playermoveevent.getTo().getBlockY() < playermoveevent.getFrom().getBlockY()) {
                        float f1 = player.getFallDistance();
                        double d2 = (double) f1 - Math.floor((double) f1);

                        if (f <= 0.0F && d2 == 0.0D && i >= 8) {
                            this.method_218(player, "t: air, f: " + f + ", a: " + i, location1, 5);
                        }
                    }
                }
            }

            this.field_76.put(player, Float.valueOf(player.getFallDistance()));
        }
    }

    private void Ξ(Player player, String s, Location location, int i) {
        class_38.method_159(player, class_46.field_75, s);
        if (class_123.method_583(player, class_46.field_75)) {
            class_69.method_386(player, location);
            class_71.method_399(player, i);
            if (class_128.method_610("fall_damage_on_teleport")) {
                class_22.method_84(player, 10);
                player.damage(2.0D);
            }
        }

    }

    private void Ξ(Player player, double d0, Location location) {
        if (player != null && location != null && class_120.method_572("NoFall.check_landing") && !class_71.HHΞ(player) && !class_71.BPPΠ(player)) {
            if (d0 < 0.0D) {
                if (player.getFallDistance() >= 6.0F) {
                    class_39.method_170(player, class_46.field_75.toString() + "=spoofed(land)", 10);
                }
            } else if (!class_39.method_169(player, class_46.field_75.toString() + "=spoofed(land)")) {
                class_39.method_171(player, class_46.field_75.toString() + "=spoofed(land)");
                if (class_71.method_416(player.getLocation()) && class_69.method_387(player) && class_66.method_340(player.getLocation().add(0.0D, -1.0D, 0.0D), true) && class_66.HHΞ(player, 0.3D, 0.0D, 0.3D) && !player.getAllowFlight() && class_39.method_169(player, class_46.field_75.toString() + "=land")) {
                    this.method_218(player, "t: land", location, 10);
                }
            }

        }
    }

    private void Ξ(Player player, double d0, double d1, Location location) {
        if (player != null && location != null && class_120.method_572("NoFall.check_ground") && class_69.method_387(player) && d0 >= 0.0D) {
            boolean flag = class_66.HHΞ(player, 1.0D, 2.0D, 1.0D);

            if (flag) {
                for (int i = -1; i <= 2; ++i) {
                    if (!class_66.method_348(player, 1.0D, (double) i, 1.0D)) {
                        flag = false;
                        break;
                    }
                }
            }

            if (flag) {
                class_36.method_135(player, class_46.field_75.toString() + "=ground", 20);
                if (class_36.method_133(player, class_46.field_75.toString() + "=ground") == 10) {
                    class_69.HHΞ(player);
                    this.method_218(player, "t: ground, r: " + d1, location, 10);
                }
            }

        }
    }

    private boolean Ξ(Player player) {
        if (player == null) {
            return false;
        } else {
            for (int i = 0; i <= 2; ++i) {
                if (!class_66.method_346(player, 1.0D, (double) i, 1.0D) || class_66.method_350(player.getLocation().add(0.0D, (double) i, 0.0D), Material.WEB, 1.0D)) {
                    return false;
                }
            }

            return class_55.method_298(player, class_46.field_75) && !class_71.method_400(player) && !class_13.method_53(player) && player.getLocation().getBlockY() >= 0 && !class_71.BPΞ(player) && !class_71.O1337(player) && !class_71.PPPΞ(player) && !class_20.method_75(player) && !class_15.method_61(player) && !class_66.method_341(player.getLocation());
        }
    }

    static {
        class_46.field_75 = Enums.HackType.NoFall;
    }
}
