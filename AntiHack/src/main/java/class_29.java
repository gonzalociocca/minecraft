import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: ab
public class class_29 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;
    private double Ξ;
    private double Π;
    private double HHΞ;
    private double HΞ;
    private double BΞ;
    private double BPPΠ;

    public class_29() {
        this.field_23 = Enums.HackType.BoatMove;
        this.field_24 = new HashMap();
        this.field_25 = 3.5D;
        this.field_26 = 0.415D;
        this.HHΞ = 0.25D;
        this.field_27 = 0.5D;
        this.field_28 = 1.25D;
        this.BPPΠ = 6.0D;
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_24.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_24.remove(player);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (!this.method_104(player)) {
                this.method_102(player, "", -2);
                class_36.method_138(player, this.field_23.toString() + "=ticks");
            } else {
                double d0 = this.field_28;
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d1 = location.getY() - location1.getY();
                double d2 = class_89.method_475(location, location1);
                double d3 = class_69.method_392(player, location1);
                int i = class_36.method_133(player, this.field_23.toString() + "=ticks") + 1;

                class_36.method_134(player, this.field_23.toString() + "=ticks", i);
                double d4;
                String s;

                if (player.getLocation().getBlock().getType() != Material.ICE && player.getLocation().getBlock().getType() != Material.PACKED_ICE) {
                    if (player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                        d4 = this.field_26;
                        s = "water";
                        this.method_102(player, "water", -5);
                    } else if (player.getLocation().getBlock().getType() != Material.AIR) {
                        d4 = this.HHΞ;
                        s = "ground";
                        this.method_102(player, "ground", -1);
                    } else {
                        d4 = this.field_27;
                        s = "air";
                        this.method_102(player, "air", -1);
                    }
                } else {
                    d4 = this.field_25;
                    s = "ice";
                    this.method_102(player, "ice", -20);
                }

                if (class_113.method_544() > d4) {
                    d4 = class_113.method_544();
                }

                if (d4 == this.field_25) {
                    d0 = this.BPPΠ;
                }

                d4 = class_41.method_178(d4);
                d0 = class_41.method_178(d0);
                if (d3 >= d0 && i >= 2) {
                    this.method_103(player, location1, "t: " + s + "(packets), ds: " + d3 + ", dm: " + d0);
                }

                if (d4 > 0.0D && d2 >= d4) {
                    class_36.method_135(player, this.field_23.toString() + "=" + s, 20);
                    if (class_36.method_133(player, this.field_23.toString() + "=" + s) == 3) {
                        this.method_103(player, location, "t: " + s + "(horizontal), ds: " + d2 + ", dm: " + d4);
                    }
                }

                if (this.field_24.containsKey(player) && class_66.HHΞ(player, 1.0D, 0.0D, 1.0D)) {
                    double d5 = ((Double) this.field_24.get(player)).doubleValue() - d1;

                    if (d5 == 0.0D && s != "water" && !class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D)) || d1 >= 0.1D) {
                        class_36.method_135(player, this.field_23.toString() + "=" + s, 20);
                        if (class_36.method_133(player, this.field_23.toString() + "=" + s) == 3) {
                            this.method_103(player, location1, "t: " + s + "(vertical), dy: " + d1 + ", distance: " + d5);
                        }
                    }
                }

                this.field_24.put(player, Double.valueOf(d1));
            }
        }
    }

    private void Ξ(Player player, String s, int i) {
        if (!s.equalsIgnoreCase("ice")) {
            class_36.method_134(player, this.field_23.toString() + "=ice", i);
        }

        if (!s.equalsIgnoreCase("water")) {
            class_36.method_134(player, this.field_23.toString() + "=water", i);
        }

        if (!s.equalsIgnoreCase("ground")) {
            class_36.method_134(player, this.field_23.toString() + "=ground", i);
        }

        if (!s.equalsIgnoreCase("air")) {
            class_36.method_134(player, this.field_23.toString() + "=air", i);
        }

    }

    private void Ξ(Player player, Location location, String s) {
        class_38.method_159(player, this.field_23, s);
        if (class_123.method_583(player, this.field_23)) {
            class_69.method_386(player, location);
        }

    }

    private boolean Ξ(Player player) {
        return class_55.method_298(player, this.field_23) && !class_13.method_53(player) && player.getVehicle() != null && player.getVehicle() instanceof Boat && !class_66.method_350(player.getLocation().add(0.0D, 1.0D, 0.0D), Material.SLIME_BLOCK, 1.0D) && !class_66.method_350(player.getLocation().add(0.0D, 1.0D, 0.0D), Material.PISTON_BASE, 1.0D) && !class_66.method_350(player.getLocation().add(0.0D, 1.0D, 0.0D), Material.PISTON_STICKY_BASE, 1.0D) && !class_66.method_350(player.getLocation().add(0.0D, 1.0D, 0.0D), Material.PISTON_EXTENSION, 1.0D) && !class_66.method_350(player.getLocation().add(0.0D, 1.0D, 0.0D), Material.PISTON_MOVING_PIECE, 1.0D);
    }
}
