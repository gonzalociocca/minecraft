import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: ai
public class class_44 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;
    private HashMap<Player, Double> Π;
    private HashMap<Player, Double> HHΞ;
    private HashMap<Player, Location> HΞ;
    private HashMap<Player, Double> BΞ;

    public class_44() {
        this.field_69 = Enums.HackType.MorePackets;
        this.field_70 = new HashMap();
        this.field_71 = new HashMap();
        this.HHΞ = new HashMap();
        this.field_72 = new HashMap();
        this.field_73 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.HHΞ.remove(player);
        this.field_72.remove(player);
        this.field_73.remove(player);
        this.field_70.remove(player);
        this.field_71.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.HHΞ.remove(player);
                this.field_72.remove(player);
                this.field_73.remove(player);
                this.field_70.remove(player);
                this.field_71.remove(player);
            }

            class_36.method_138(player, this.field_69.toString() + "=difference=times");
        }

    }

    @EventHandler
    private void Ξ(PlayerDeathEvent playerdeathevent) {
        Player player = playerdeathevent.getEntity();

        this.field_70.remove(player);
        this.field_71.remove(player);
        class_36.method_138(player, this.field_69.toString() + "=difference=times");
    }

    @EventHandler
    private void Ξ(PlayerRespawnEvent playerrespawnevent) {
        Player player = playerrespawnevent.getPlayer();

        this.field_70.remove(player);
        this.field_71.remove(player);
        class_36.method_138(player, this.field_69.toString() + "=difference=times");
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (this.method_207(player)) {
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d0 = location.getY() - location1.getY();
                double d1 = location.distance(location1);

                this.method_205(player);
                this.method_202(player, location1, d1);
                this.method_204(player, location1, d1, d0);
                this.method_201(player, d1, d0, location);
                if (this.field_72.containsKey(player)) {
                    double d2 = class_69.method_391(player.getLocation(), (Location) this.field_72.get(player));

                    if (d2 >= 15.0D) {
                        this.field_72.remove(player);
                    }
                }

                if (class_45.method_209(player, this.field_69.toString() + "=ms")) {
                    long i = class_45.method_210(player, this.field_69.toString() + "=ms");

                    if (i <= 40L) {
                        this.method_203(player, location1);
                    }
                }

                class_45.method_211(player, this.field_69.toString() + "=ms");
            }
        }
    }

    private void Ξ(Player player, double d0, double d1, Location location) {
        if (!class_71.method_400(player) && class_120.method_572("MorePackets.check_difference") && !class_20.method_75(player) && !class_71.BPΞ(player) && class_71.method_410(player) <= 12 && !class_69.method_374(player, d1)) {
            class_36.method_135(player, this.field_69.toString() + "=difference=ticks", 20);
            int i = class_36.method_133(player, this.field_69.toString() + "=difference=ticks") + (class_36.method_136(player, this.field_69.toString() + "=difference=ticks") - 1);
            int j = class_36.method_133(player, this.field_69.toString() + "=difference=times");
            double d2;

            if (j >= 20 && this.field_70.containsKey(player) && this.field_71.containsKey(player)) {
                d2 = ((Double) this.field_70.get(player)).doubleValue();
                double d3 = ((Double) this.field_71.get(player)).doubleValue();
                double d4 = Math.abs(d2 - d3);
                double d5 = 9.0D;

                this.field_70.remove(player);
                this.field_71.remove(player);
                class_36.method_138(player, this.field_69.toString() + "=difference=times");
                if (d4 >= class_41.method_178(1.5D) && i >= class_41.method_179(23) && (d2 >= d5 || d3 >= d5)) {
                    String s = "t: difference, c: " + d2 + ", n: " + d3 + ", d: " + d4 + ", t: " + i;

                    class_36.method_135(player, this.field_69.toString() + "=difference=attempts", 600);
                    if (class_36.method_133(player, this.field_69.toString() + "=difference=attempts") < 2 && class_38.method_154(player, this.field_69) < 1) {
                        class_49.method_251(player, this.field_69, s, false);
                    } else {
                        this.method_206(player, s, 20);
                    }
                }
            } else if (d0 > 0.0D) {
                d2 = class_69.method_391(location, class_69.method_383(player));
                boolean flag = true;

                for (int k = -1; k <= 1; ++k) {
                    if (!class_66.method_348(player, 1.0D, (double) k, 1.0D)) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    if (this.field_70.containsKey(player)) {
                        this.field_70.put(player, Double.valueOf(((Double) this.field_70.get(player)).doubleValue() + d2));
                    } else {
                        this.field_70.put(player, Double.valueOf(d2));
                    }

                    if (this.field_71.containsKey(player)) {
                        this.field_71.put(player, Double.valueOf(((Double) this.field_71.get(player)).doubleValue() + d0));
                    } else {
                        this.field_71.put(player, Double.valueOf(d0));
                    }

                    class_36.method_134(player, this.field_69.toString() + "=difference=times", j + 1);
                }
            }

        }
    }

    private void Ξ(Player player, Location location, double d0) {
        if (player != null && location != null && class_120.method_572("MorePackets.check_timer") && !class_71.method_400(player) && (!class_71.BPΞ(player) || class_71.method_410(player) < 15)) {
            class_36.method_135(player, this.field_69.toString() + "=timer=ticks", 20);
            int i = class_36.method_133(player, this.field_69.toString() + "=timer=ticks");
            int j = class_120.method_571("MorePackets.timer.max_moves_per_second");

            j = j < 22 ? 22 : j;
            if (i >= j) {
                double d1 = class_69.method_392(player, location);

                if (d1 >= d0) {
                    class_36.method_135(player, this.field_69.toString() + "=timer=attempts", 100);
                    int k = class_36.method_133(player, this.field_69.toString() + "=timer=attempts");
                    byte b0;

                    if (i <= 25) {
                        b0 = 4;
                    } else if (i <= 30) {
                        b0 = 6;
                    } else if (i <= 40) {
                        b0 = 9;
                    } else {
                        b0 = 12;
                    }

                    if (k >= b0) {
                        class_36.method_138(player, this.field_69.toString() + "=timer=attempts");
                        this.method_206(player, "t: timer, diff: " + d1 + ", l: " + j, 40);
                    }
                }
            }

        }
    }

    private void Ξ(Player player, Location location) {
        if (player != null && location != null && class_120.method_572("MorePackets.check_blink")) {
            double d0 = class_120.method_573("MorePackets.blink.distance_to_check");
            double d1 = class_69.method_392(player, location);

            class_36.method_135(player, this.field_69.toString() + "=blink=ticks", 20);
            int i = class_36.method_133(player, this.field_69.toString() + "=blink=ticks");
            int j = class_87.method_472(player) >= 100 ? 30 : 22;

            d0 = class_41.method_178(d0 < 1.0D ? 1.0D : d0);
            if (d1 >= d0) {
                class_39.method_170(player, this.field_69.toString() + "=blink=distance", 5);
            }

            if (i == j && !class_39.method_169(player, this.field_69.toString() + "=blink=distance")) {
                class_36.method_138(player, this.field_69.toString() + "=blink=ticks");
                class_39.method_171(player, this.field_69.toString() + "=blink=distance");
                this.method_206(player, "t: blink, diff: " + d0, 0);
            }

        }
    }

    private void Ξ(Player player, Location location, double d0, double d1) {
        if (player != null && location != null && class_120.method_572("MorePackets.check_overall") && !class_71.method_400(player) && (d0 >= 0.215D || player.isSneaking()) && (!class_71.BPΞ(player) || class_71.method_410(player) < 15) && (!class_20.method_75(player) && !class_66.method_352(player, 3) || d1 < 0.5D) && player.getLocation().getBlockY() >= 0 && !class_69.method_374(player, d1)) {
            byte b0 = 20;
            double d2 = 0.0D;
            double d3 = 0.0D;
            double d4 = class_69.method_392(player, location);
            byte b1 = 15;

            if (this.field_73.containsKey(player)) {
                d3 = Math.abs(d4 - ((Double) this.field_73.get(player)).doubleValue());
                d2 = Math.abs(d4 - d3);
            }

            this.field_73.put(player, Double.valueOf(d4));
            if (d3 > 0.0D) {
                class_39.method_170(player, this.field_69.toString() + "=overall=boost", b1);
            }

            if (d2 <= 0.01D && !class_39.method_169(player, this.field_69.toString() + "=overall=boost")) {
                int i = class_36.method_133(player, this.field_69.toString() + "=overall=times") + 1;

                class_36.method_134(player, this.field_69.toString() + "=overall=times", i);
                Location location1 = player.getLocation();

                if (!this.HHΞ.containsKey(player)) {
                    this.HHΞ.put(player, Double.valueOf(d4));
                } else {
                    this.HHΞ.put(player, Double.valueOf(((Double) this.HHΞ.get(player)).doubleValue() + d4));
                }

                double d5 = ((Double) this.HHΞ.get(player)).doubleValue();
                double d6 = class_69.method_391(location1, (Location) this.field_72.get(player));

                if (i == 1) {
                    this.field_72.put(player, location1);
                } else if (i == b0) {
                    class_36.method_138(player, this.field_69.toString() + "=overall=times");
                    this.HHΞ.remove(player);
                    this.field_73.remove(player);
                    if (d5 > 0.0D && d6 >= 5.5D) {
                        class_36.method_135(player, this.field_69.toString() + "=overall=attempts", b0 + 1);
                        if (class_36.method_133(player, this.field_69.toString() + "=overall=attempts") >= 2) {
                            this.method_206(player, "t: overall, diff: " + d5 + ", d: " + d6, 40);
                        }
                    }
                }
            }

        }
    }

    private void Ξ(Player player) {
        if (player != null && !class_10.method_40(player) && !class_13.method_53(player) && !class_71.BPΞ(player) && !class_71.method_400(player) && !class_66.method_341(player.getLocation()) && !class_20.method_75(player) && class_120.method_572("MorePackets.check_motion")) {
            Location location = class_69.method_383(player);

            if (location != null) {
                Location location1 = player.getLocation();
                double d0 = location1.getY() - location.getY();

                if (d0 > 2.0D && d0 % 0.5D != 0.0D && (class_71.method_416(player.getLocation()) || player.isOnGround() || !class_66.HHΞ(player, 1.0D, 0.0D, 1.0D) || !class_66.HHΞ(player, 1.0D, -1.0D, 1.0D))) {
                    this.method_206(player, "t: motion, dy: " + d0, 20);
                }
            }

        }
    }

    private void Ξ(Player player, String s, int i) {
        class_38.method_159(player, this.field_69, s);
        if (class_123.method_583(player, this.field_69)) {
            Location location = (Location) this.field_72.get(player);

            if (location == null) {
                Location location1 = class_69.method_383(player);

                class_69.method_386(player, location1);
            } else {
                class_69.method_386(player, location);
            }

            class_69.HHΞ(player);
            class_71.method_399(player, i);
        }

    }

    private boolean Ξ(Player player) {
        return class_55.method_298(player, this.field_69) && class_97.method_502() >= 17.5D && !(player.getVehicle() instanceof LivingEntity);
    }
}
