import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: ad
public class class_33 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;
    private HashMap<Player, Double> Π;

    public class_33() {
        this.field_32 = Enums.HackType.ElytraMove;
        this.field_33 = new HashMap();
        this.field_34 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_33.remove(player);
        this.field_34.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_33.remove(player);
                this.field_34.remove(player);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (this.method_122(player)) {
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d0 = location.getY() - location1.getY();
                double d1 = location.getY() - (double) location.getBlockY();
                double d2 = class_89.method_475(location, location1);
                boolean flag = class_69.method_374(player, d0);
                int i = class_71.method_410(player);
                double d3;
                double d4;

                if (class_120.method_572("ElytraMove.check_speed")) {
                    if (d2 >= class_69.method_385(player, 3.5D, 4.0D, PotionEffectType.SPEED)) {
                        this.method_121(player, playermoveevent.getFrom(), "t: speed(normal), ds: " + d2);
                    }

                    if (this.field_33.containsKey(player) && !class_71.method_416(player.getLocation()) && !class_66.method_352(player, 2) && !class_71.HHHΠ(player)) {
                        d3 = ((Double) this.field_33.get(player)).doubleValue();
                        d4 = ((Double) this.field_33.get(player)).doubleValue() - d2;
                        if (!class_10.method_40(player)) {
                            boolean flag1 = true;

                            for (int j = -1; j <= 2; ++j) {
                                if (!class_66.method_346(player, 1.0D, (double) j, 1.0D) || !class_66.method_348(player, 1.0D, (double) j, 1.0D)) {
                                    flag1 = false;
                                    break;
                                }
                            }

                            if (flag1) {
                                double d5 = Math.abs(d3 - d2);
                                double d6 = 0.35D;
                                double d7 = (double) Math.abs(player.getLocation().getPitch());

                                if (d0 != 0.0D) {
                                    if (d3 >= 0.15D && d7 <= 30.0D && !flag) {
                                        if (i >= 20) {
                                            d6 = 0.15D;
                                        } else {
                                            d6 = 0.2D;
                                        }
                                    }
                                } else {
                                    d6 = 0.1D;
                                }

                                if (d5 >= class_41.method_178(d6) && d2 >= d3 && d2 != d5) {
                                    this.method_121(player, location1, "t: speed(difference), ds: " + d2 + ", ods: " + d3 + ", diff: " + d5 + ", l: " + d6 + ", p: " + d7);
                                }
                            }
                        }

                        if ((i >= 20 || d0 == 0.0D) && !flag) {
                            if (!class_10.method_40(player)) {
                                double d8 = class_73.method_418(d3, 3);
                                double d9 = class_73.method_418(d2, 3);
                                double d10 = class_89.method_482(d8, d9);

                                if (d10 == 0.0D && d9 <= 0.096D && d0 > 0.0D) {
                                    class_36.method_135(player, this.field_32.toString() + "=speed(repeated)", 10);
                                    if (class_36.method_133(player, this.field_32.toString() + "=speed(repeated)") >= 2) {
                                        this.method_121(player, location1, "t: speed(repeated), d: " + d9 + ", dy: " + d0);
                                    }
                                }
                            }

                            if (d4 == 0.0D && d2 > 0.0D) {
                                this.method_121(player, location1, "t: speed(stable), dy: " + d0 + ", ds: " + d2);
                            }
                        }
                    }

                    this.field_33.put(player, Double.valueOf(d2));
                }

                if (class_120.method_572("ElytraMove.check_fly") && !class_71.HHHΠ(player) && !flag) {
                    if (i >= 20) {
                        if (!class_20.method_75(player) && !class_10.method_40(player)) {
                            d3 = d2 / 0.06D * 0.15D;
                            if (d0 >= d3) {
                                this.method_121(player, location1, "t: fly(calculated), dy: " + d0 + ", ds: " + d2 + ", l: " + d3);
                            }

                            if (d0 > 0.0D && this.field_34.containsKey(player)) {
                                d4 = ((Double) this.field_34.get(player)).doubleValue();
                                if (d4 <= -0.12D) {
                                    this.method_121(player, location1, "t: fly(change), dy: " + d0 + ", ody: " + d4);
                                }
                            }
                        }

                        if (this.field_34.containsKey(player) && !class_71.method_416(player.getLocation()) && !class_66.method_352(player, 2)) {
                            d3 = class_89.method_482(d0, ((Double) this.field_34.get(player)).doubleValue());
                            if (d3 == 0.0D && d2 > 0.0D) {
                                this.method_121(player, location1, "type: fly(stable), dy: " + d0 + ", dis: " + d3 + ", ds: " + d2);
                            }
                        }
                    }

                    this.field_34.put(player, Double.valueOf(d0));
                    if (playermoveevent.getTo().getBlockY() > playermoveevent.getFrom().add(0.0D, d1, 0.0D).getBlockY() && d0 >= 3.5D) {
                        this.method_121(player, location1, "type: fly(instant), dy: " + d0);
                    }
                }

            }
        }
    }

    private void Ξ(Player player, Location location, String s) {
        class_38.method_159(player, this.field_32, s);
        if (class_123.method_583(player, this.field_32)) {
            class_69.method_386(player, location);
            class_69.HHΞ(player);
        }

    }

    private boolean Ξ(Player player) {
        return class_55.method_298(player, this.field_32) && class_36.method_133(player, "elytra") == 1 && !class_71.method_400(player) && !class_71.method_403(player) && !class_71.method_409(player, 1, 0.5D) && !class_13.method_53(player) && !class_71.O1337(player) && !class_71.PPΠ(player) && !class_71.method_405(player);
    }
}
