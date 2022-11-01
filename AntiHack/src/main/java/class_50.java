import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: al
public class class_50 implements Listener {

    private static Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;
    private HashMap<Player, Double> Π;
    private HashMap<Player, Double> HHΞ;
    private HashMap<Player, Double> HΞ;
    private static HashMap<Player, Location> BΞ;

    public class_50() {
        this.field_84 = new HashMap();
        this.field_85 = new HashMap();
        this.HHΞ = new HashMap();
        this.field_86 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_84.remove(player);
        this.HHΞ.remove(player);
        this.field_85.remove(player);
        this.field_86.remove(player);
        class_50.field_87.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_84.remove(player);
                this.HHΞ.remove(player);
                this.field_85.remove(player);
                this.field_86.remove(player);
                class_50.field_87.remove(player);
            }
        }

    }

    public static void Ξ(Player player) {
        if (class_120.method_572("NormalMovements.check_block_climbing") && method_266(player) && !class_39.method_169(player, class_50.field_83.toString() + "=jump")) {
            Location location = player.getLocation();

            if (class_50.field_87.containsKey(player)) {
                Location location1 = (Location) class_50.field_87.get(player);
                double d0 = location.getY() - location1.getY();
                int i = class_71.method_410(player);
                boolean flag = class_71.method_416(location) || class_71.method_416(location.clone().add(0.0D, -0.99999D, 0.0D));

                if (d0 == 0.0D && i >= 5 && !flag && !class_12.method_49(player) && !class_71.PPΠ(player) && !class_17.method_65(player) && !class_71.PPPΞ(player) && !class_71.O1337(player) && !class_69.method_374(player, d0) && !class_66.method_349(player, 1.0D, 0.0D, 1.0D) && player.getFireTicks() <= 0 && !class_10.method_40(player) && !class_66.HHΞ(player, 0.299D, 0.0D, 0.299D) && !class_66.BBBΠ(location) && !method_267(player, d0) && !class_15.method_61(player)) {
                    method_263(player, location1, "t: climbing(walls), a: " + i);
                }
            }

            class_50.field_87.put(player, location);
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();
            Location location = playermoveevent.getTo();
            Location location1 = playermoveevent.getFrom();
            double d0 = location.getY() - location1.getY();
            double d1 = location.getY() - (double) location.getBlockY();

            if (method_266(player) && this.field_84.containsKey(player)) {
                double d2 = 0.6D;
                double d3 = d2;
                double d4 = ((Double) this.field_84.get(player)).doubleValue();
                double d5 = Math.abs(d0 - d4);
                int i = class_71.method_410(player);

                if (d0 < d4 && i > 0 && class_69.method_376(d0) && class_69.method_376(d4)) {
                    class_39.method_170(player, class_50.field_83.toString() + "=jump", 4);
                }

                if (class_13.method_53(player)) {
                    d3 = 5.0D;
                } else if (!class_20.method_75(player) && (!class_66.method_352(player, 3) || d0 > -1.0D)) {
                    if (class_14.method_57(player)) {
                        d3 = 3.1D;
                    } else if (method_267(player, d0)) {
                        d3 = 2.0D;
                    } else if (!class_10.method_40(player) && !class_28.method_98(player) && !class_17.method_65(player)) {
                        if (class_19.method_71(player)) {
                            d3 = 1.3D;
                        } else if (class_66.method_352(player, 1)) {
                            d3 = 1.1D;
                        } else if (class_71.PPPΞ(player)) {
                            d3 = 0.61D;
                        }
                    } else {
                        d3 = 1.5D;
                    }
                } else {
                    d3 = 3.6D;
                }

                this.method_258(player, d0, d4, d1, d5, i, location1);
                this.method_260(player, d0, location1);
                this.method_261(player, d0, d1, location1);
                this.method_259(player, d0, d4, d1, d5, i, location, location1);
                this.method_257(player, d0, d1, d3, d2, location, location1);
                this.method_262(player, d0);
            } else {
                this.method_262(player, d0);
            }
        }
    }

    private void Ξ(Player player, double d0, double d1, double d2, double d3, Location location, Location location1) {
        if (class_120.method_572("NormalMovements.check_step_hacking")) {
            double d4 = class_69.method_393(player, location1);
            double d5 = class_73.method_418(d4, 10);
            boolean flag = class_10.method_40(player);
            boolean flag1 = method_267(player, d0);
            boolean flag2 = class_69.method_376(d0);
            boolean flag3 = class_71.BPPΠ(player);
            boolean flag4 = class_71.method_403(player);
            boolean flag5 = class_20.method_75(player);
            boolean flag6 = class_66.method_341(player.getLocation());

            if (d0 >= d2 && !flag4) {
                method_263(player, location1, "t: step(high), d: " + d0 + ", dm: " + d2);
            } else if (d0 == 0.25D && d2 == d3 && !flag1 && !flag && !flag2 && !flag5 && !flag6 && !class_12.method_49(player) && (class_66.method_348(player, 1.0D, 0.0D, 1.0D) || d0 != 0.25D) && !class_15.method_61(player)) {
                method_263(player, location1, "t: step(low), d: " + d0);
            } else if (!flag1) {
                if (d0 >= 0.01D && d0 % 0.5D != 0.0D && d4 >= 0.2D && !class_71.HHΞ(player) && !flag3 && !flag && !flag5 && !flag2 && !flag6) {
                    class_36.method_135(player, class_50.field_83.toString() + "=step=packets", 3);
                    if ((d4 < 1.0D || class_19.method_71(player)) && d4 < 1.2D) {
                        if (class_36.method_133(player, class_50.field_83.toString() + "=step=packets") >= 2) {
                            method_263(player, location1, "t: step[packets(normal)], d: " + d0 + ", c: " + d4);
                        } else if (class_45.method_209(player, class_50.field_83.toString() + "=step=packets=" + d5) && class_45.method_210(player, class_50.field_83.toString() + "=step=packets=" + d5) <= 200L) {
                            method_263(player, location1, "t: step[packets(repeated)], d: " + d0 + ", v: " + d5);
                        }
                    } else {
                        method_263(player, location1, "t: step[packets(high)], d: " + d0 + ", c: " + d4);
                    }

                    class_45.method_211(player, class_50.field_83.toString() + "=step=packets=" + d5);
                }

                if (d0 > 0.25D && !flag2 && !class_14.method_57(player) && !flag5 && !flag6 && this.method_264(location1) && this.method_264(location) && !flag4) {
                    if (class_45.method_209(player, class_50.field_83.toString() + "=repeated_step")) {
                        long i = class_45.method_210(player, class_50.field_83.toString() + "=repeated_step");
                        boolean flag7 = class_66.method_340(player.getLocation(), true) || class_66.method_349(player, 0.3D, 0.0D, 0.3D);
                        double d6 = class_10.method_41(player) ? 0.6D : (!class_10.method_40(player) && !class_18.method_69(player) ? 0.25D : 0.33D);

                        if (i <= 100L && (d0 != 0.5D || flag7) && d0 >= d6) {
                            class_36.method_135(player, class_50.field_83.toString() + "=repeated_step", 6);
                            if (class_36.method_133(player, class_50.field_83.toString() + "=repeated_step") >= 2) {
                                method_263(player, location1, "t: step(repeated), d: " + d0 + ", ms: " + i);
                            }
                        }
                    }

                    class_45.method_211(player, class_50.field_83.toString() + "=repeated_step");
                }

                if (!flag && d0 >= 0.34D && !flag5 && d1 != 0.5D && !flag2 && !class_11.method_45(player) && !flag3 && !class_7.method_31(player) && !class_19.method_71(player)) {
                    for (int j = -1; j <= 1; ++j) {
                        if (!this.method_268(player, 1.0D, (double) j, 1.0D)) {
                            return;
                        }
                    }

                    double d7 = location1.getY() - (double) location1.getBlockY();
                    double d8 = Math.abs(d1 - d7);
                    double d9 = Math.abs(d0 - d8);

                    if (d8 != d0 && d1 != d7 && d9 >= 0.35D) {
                        method_263(player, location1, "t: step(difference), d: " + d0 + ", r: " + d1 + ", or: " + d7 + ", diff: " + d9);
                    }
                }
            }

        }
    }

    private void Ξ(Player player, double d0, double d1, double d2, double d3, int i, Location location) {
        if (class_120.method_572("NormalMovements.check_jump_movement") && !class_10.method_40(player) && !class_71.method_416(player.getLocation()) && !class_14.method_58(player) && d2 != 0.5D && d2 != 0.0D && !class_66.method_341(player.getLocation()) && !method_267(player, d0)) {
            Location location1 = player.getLocation();
            int j = player.getFireTicks();
            int k = !class_18.method_69(player) && !class_71.method_405(player) ? (j <= 0 && class_66.HHΞ(player, 0.3D, 2.0D, 0.3D) ? 1 : 5) : 8;
            boolean flag = class_66.method_336(player.getLocation().add(0.0D, -d2, 0.0D)) || class_66.method_336(player.getLocation().add(0.0D, -1.0D, 0.0D));
            byte b0;

            if (class_69.method_376(d0) && !class_11.method_45(player) && !class_15.method_61(player) && i >= k && d0 != d2 && !flag) {
                b0 = 0;
                double d4 = Math.abs(class_69.method_372(player) - d3);
                boolean flag1 = class_69.method_377(player, d0, d2);
                boolean flag2 = class_69.method_376(d1) || i >= 20 || d1 <= -0.03D && j <= 0 && i >= 5 && d4 >= 0.4D && !flag1 && !class_71.method_416(location1.clone().add(0.0D, -0.3D, 0.0D));

                if (d0 >= d1) {
                    b0 = 1;
                } else if (d3 >= 0.09D) {
                    b0 = 2;
                } else if (d0 < d1 && !class_69.method_376(d1)) {
                    b0 = 3;
                } else if (d0 < d1 && class_69.method_376(d1) && i >= 6 && !flag1) {
                    b0 = 4;
                }

                if (flag2 && b0 != 0) {
                    byte b1 = 1;
                    byte b2 = 1;

                    if (b0 == 4) {
                        b1 = 8;
                        b2 = 2;
                    }

                    class_36.method_135(player, class_50.field_83.toString() + "=jump=illegal", b1);
                    if (class_36.method_133(player, class_50.field_83.toString() + "=jump=illegal") >= b2) {
                        method_263(player, location, "type: jump(illegal), d: " + d0 + ", od: " + d1 + ", air: " + i + ", r: " + d2 + ", case: " + b0 + ", diff: " + d4);
                    }
                }
            } else if (d0 > 0.0D && d0 != 0.5D && d2 >= 0.1D && !class_71.method_416(location1.clone().add(0.0D, -0.6D, 0.0D)) && !class_20.method_75(player) && !class_69.method_376(d0) && !class_69.method_376(d2) && !class_71.BPPΠ(player) && i >= k) {
                b0 = 1;
                byte b3 = 1;

                if (d0 < 0.09D && i <= 30 || class_18.method_69(player)) {
                    b0 = 8;
                    b3 = 2;
                }

                class_36.method_135(player, class_50.field_83.toString() + "=jump=unusual", b0);
                if (class_36.method_133(player, class_50.field_83.toString() + "=jump=unusual") >= b3) {
                    method_263(player, location, "t: jump(unusual), d: " + d0 + ", a: " + i + ", r: " + d2);
                }
            }

        }
    }

    private void Ξ(Player player, double d0, double d1, double d2, double d3, int i, Location location, Location location1) {
        if (class_120.method_572("NormalMovements.check_slime_boost") && d0 != d2 && class_66.HHΞ(player, 1.0D, 2.0D, 1.0D) && class_66.HHΞ(player, 1.0D, 3.0D, 1.0D) && !method_267(player, d0) && !class_7.method_31(player) && !class_28.method_98(player) && !class_15.method_61(player) && class_20.method_75(player) && !class_66.method_341(player.getLocation()) && !class_71.method_403(player) && !class_71.method_416(player.getLocation()) && !class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D))) {
            boolean flag = class_66.method_347(player, 1.0D, 0.0D, 1.0D) && class_66.method_347(player, 1.0D, -1.0D, 1.0D) && class_66.method_347(player, 1.0D, -2.0D, 1.0D);

            if (d0 > 0.0D && i > 0) {
                boolean flag1 = this.field_86.containsKey(player);
                double d4 = !flag1 ? 0.0D : ((Double) this.field_86.get(player)).doubleValue();
                double d5 = d4 * 0.085D;
                double d6 = !class_10.method_40(player) && !class_17.method_66(player) ? (flag1 && d5 >= 0.0D ? d5 : 0.0D) : 1.0D;
                double d7 = Math.abs(d0 - d6);
                byte b0 = 0;

                if (d7 >= 0.5D && i > 1) {
                    b0 = 1;
                } else if (d7 >= 0.2D && d0 >= 0.6D) {
                    b0 = 2;
                } else if (d0 == d7 && d0 >= 0.2D && (!class_69.method_376(d0) || !class_69.method_377(player, d0, d2))) {
                    Location location2 = player.getLocation();

                    if (i >= 6 && !class_71.method_416(location2) && !class_71.method_416(location2.clone().add(0.0D, -1.0D, 0.0D))) {
                        b0 = 3;
                    }
                }

                if (d0 >= 0.1D && d0 >= d6 && b0 != 0) {
                    method_263(player, location1, "t: slime(calculated), d: " + d0 + ", l: " + d6 + ", diff: " + d7 + ", a: " + i + ", c: " + b0 + ", r: " + d2);
                }
            }

            if (d1 <= 0.0D && d0 > 0.0D && flag && !class_10.method_40(player) && (i >= 20 || i <= 6 || !class_69.method_376(d0))) {
                method_263(player, location1, "type: slime(stable), d: " + d0 + ", air: " + i);
            } else if (class_69.method_376(d0) && i >= 20) {
                method_263(player, location1, "type: slime(jump), d: " + d0 + ", air: " + i);
            } else if (location.getBlockY() > location1.clone().add(0.0D, d2, 0.0D).getBlockY()) {
                if (d0 > d1 && d0 != 0.5D) {
                    method_263(player, location1, "type: slime(illegal), d: " + d0 + ", od: " + d1);
                }

                double d8;

                if (d0 > 0.0D) {
                    d8 = class_73.method_418(d0, 10);
                    if (class_45.method_209(player, class_50.field_83.toString() + "=slime=repeated=" + d8)) {
                        long j = class_45.method_210(player, class_50.field_83.toString() + "=slime=repeated=" + d8);

                        if (j <= 250L) {
                            method_263(player, location1, "type: slime(repeated), d: " + d0);
                        }
                    }

                    class_45.method_211(player, class_50.field_83.toString() + "=slime=repeated=" + d8);
                }

                if (this.field_85.containsKey(player)) {
                    d8 = ((Double) this.field_85.get(player)).doubleValue() + 0.4D;
                    if (d3 >= d8 && d3 != 0.5D) {
                        class_36.method_135(player, class_50.field_83.toString() + "=slime=difference", 20);
                        if (class_36.method_133(player, class_50.field_83.toString() + "=slime=difference") >= 2) {
                            method_263(player, location1, "t: slime(difference), ds: " + d3 + ", ods: " + d8);
                        }
                    }
                }
            }

        }
    }

    private void Ξ(Player player, double d0, Location location) {
        if (class_120.method_572("NormalMovements.check_block_climbing") && !class_10.method_40(player) && !method_267(player, d0) && !class_14.method_58(player)) {
            boolean flag = class_66.method_342(player.getLocation().add(0.0D, -1.0D, 0.0D)) && class_66.method_342(player.getLocation()) || !class_66.BBBΠ(player.getLocation().add(0.0D, -1.0D, 0.0D)) && class_66.method_342(player.getLocation()) || class_66.method_342(player.getLocation().add(0.0D, -1.0D, 0.0D)) && class_66.method_342(player.getLocation().add(0.0D, 1.0D, 0.0D)) && !class_66.BBBΠ(player.getLocation());
            boolean flag1 = (player.getLocation().getBlock().getType() == Material.VINE || player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.VINE) && player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.LADDER;
            int i;

            if (flag && !flag1) {
                i = class_36.method_133(player, class_50.field_83.toString() + "=climbing=in") + 1;
                class_36.method_134(player, class_50.field_83.toString() + "=climbing=in", i);
                class_36.method_138(player, class_50.field_83.toString() + "=climbing=out");
                if (d0 >= 0.01D && this.HHΞ(player.getLocation().add(0.0D, 2.0D, 0.0D)) && (i > 6 || d0 >= 0.34D && i >= 3)) {
                    if (!class_69.method_380(d0) && !class_69.method_376(d0) && (!class_18.method_69(player) || d0 >= 0.45D) && !class_71.method_403(player)) {
                        if (class_7.method_31(player)) {
                            class_49.method_251(player, class_50.field_83, "type: climbing(ladder/vine), d: " + d0 + ", t: " + i, false);
                            if (class_123.method_583(player, class_50.field_83)) {
                                class_69.method_386(player, location);
                                class_69.HHΞ(player);
                            }
                        } else {
                            method_263(player, location, "t: climbing(ladder/vine), d: " + d0 + ", t: " + i);
                        }
                    } else if (!class_18.method_69(player)) {
                        double d1 = class_73.method_418(d0, 5);

                        if ((d1 == 0.11215D || d1 == 0.15444D || d1 == 0.1176D) && !class_66.method_342(player.getLocation())) {
                            method_263(player, location, "t: climbing(illegal), d: " + d0 + ", case: 1");
                        } else if ((d1 == 0.11215D || d1 == 0.15444D || d1 == 0.03684D || d1 == 0.07531D) && class_66.method_342(player.getLocation())) {
                            class_36.method_135(player, class_50.field_83.toString() + "=climbing=illegal", 5);
                            if (class_36.method_133(player, class_50.field_83.toString() + "=climbing=illegal") >= 5) {
                                method_263(player, location, "t: climbing(illegal), d: " + d0 + ", case: 2");
                            }
                        } else if (class_69.method_376(d0)) {
                            class_36.method_135(player, class_50.field_83.toString() + "=climbing=jumping", 5);
                            if (class_36.method_133(player, class_50.field_83.toString() + "=climbing=jumping") >= 2) {
                                if (class_11.method_45(player)) {
                                    class_49.method_251(player, class_50.field_83, "t: climbing(jumping), d: " + d0, false);
                                    if (!class_123.method_587(class_50.field_83)) {
                                        class_69.method_386(player, location);
                                        class_69.HHΞ(player);
                                    }
                                } else {
                                    method_263(player, location, "t: climbing(jumping), d: " + d0);
                                }
                            }
                        }
                    }
                }
            } else {
                i = class_36.method_133(player, class_50.field_83.toString() + "=climbing=out") + 1;
                class_36.method_134(player, class_50.field_83.toString() + "=climbing=out", i);
                if (i >= 5) {
                    class_36.method_138(player, class_50.field_83.toString() + "=climbing=in");
                    class_36.method_138(player, class_50.field_83.toString() + "=climbing=out");
                }
            }

        }
    }

    private void Ξ(Player player, double d0, double d1, Location location) {
        if (class_120.method_572("NormalMovements.check_hop_movement") && !class_10.method_40(player) && !class_71.method_404(player) && d1 != 0.0D && d0 > -2.0D && !class_11.method_45(player) && !class_71.HHΞ(player) && !method_267(player, d0) && !class_18.method_69(player)) {
            for (int i = -1; i <= 2; ++i) {
                if (!class_66.method_348(player, 1.0D, (double) i, 1.0D) || !class_66.method_346(player, 1.0D, (double) i, 1.0D)) {
                    return;
                }
            }

            if (class_66.HHΞ(player, 1.0D, 2.0D, 1.0D)) {
                boolean flag = false;
                boolean flag1 = false;
                double d2 = !this.HHΞ.containsKey(player) ? 0.0D : Math.abs(((Double) this.HHΞ.get(player)).doubleValue() - d1);

                this.HHΞ.put(player, Double.valueOf(d1));
                String s;

                if (d0 >= 0.0D) {
                    if (d1 >= 0.05D && d1 <= 0.082D) {
                        flag = true;
                    }

                    if (d1 >= 0.05D && d2 <= 0.082D) {
                        flag1 = true;
                    }

                    s = "up";
                } else {
                    if (d1 <= 0.075D) {
                        flag = true;
                    }

                    if (d2 <= 0.075D) {
                        flag1 = true;
                    }

                    s = "down";
                }

                if (flag && d1 >= 0.003D) {
                    class_36.method_135(player, class_50.field_83.toString() + "=hop=static", 10);
                    if (class_36.method_133(player, class_50.field_83.toString() + "=hop=static") == 8) {
                        class_36.method_138(player, class_50.field_83.toString() + "=hop=static");
                        method_263(player, location, "t: hop(static), r: " + s + ", v: " + d1);
                    }
                }

                if (flag1 && d2 != 0.0D) {
                    class_36.method_135(player, class_50.field_83.toString() + "=hop=dynamic", 10);
                    if (class_36.method_133(player, class_50.field_83.toString() + "=hop=dynamic") == 5) {
                        class_36.method_138(player, class_50.field_83.toString() + "=hop=dynamic");
                        method_263(player, location, "t: hop(dynamic), r: " + s + ", diff: " + d2);
                    }
                }
            }

        }
    }

    private void Ξ(Player player, double d0) {
        Location location = player.getLocation();
        boolean flag = class_71.method_416(location) || class_71.method_416(location.clone().add(0.0D, -1.0D, 0.0D)) || class_71.method_416(location.clone().add(0.0D, -2.0D, 0.0D)) || class_71.method_416(location.clone().add(0.0D, -3.0D, 0.0D));

        if (d0 < 0.0D && !flag) {
            this.field_86.put(player, Double.valueOf(Math.abs(d0 / 0.0784000015258D)));
        } else if (d0 > 0.0D && this.field_86.containsKey(player)) {
            this.field_86.put(player, Double.valueOf(((Double) this.field_86.get(player)).doubleValue() - 1.25D));
        }

        if (this.field_84.containsKey(player)) {
            this.field_85.put(player, Double.valueOf(Math.abs(d0 - ((Double) this.field_84.get(player)).doubleValue())));
        }

        this.field_84.put(player, Double.valueOf(d0));
    }

    private static void Ξ(Player player, Location location, String s) {
        class_38.method_159(player, class_50.field_83, s);
        if (class_123.method_583(player, class_50.field_83) && location.getBlockY() >= 0) {
            class_69.method_386(player, location);
            class_69.HHΞ(player);
        }

    }

    private boolean Ξ(Location location) {
        return class_66.method_340(location, false) || class_66.method_341(location);
    }

    private boolean Π(Location location) {
        return class_66.method_341(location) || !class_66.BBBΠ(location);
    }

    private boolean HHΞ(Location location) {
        return this.method_265(location) && this.method_265(location.clone().add(0.3D, 0.0D, 0.0D)) && this.method_265(location.clone().add(-0.3D, 0.0D, 0.0D)) && this.method_265(location.clone().add(0.0D, 0.0D, 0.3D)) && this.method_265(location.clone().add(0.0D, 0.0D, -0.3D)) && this.method_265(location.clone().add(0.3D, 0.0D, 0.3D)) && this.method_265(location.clone().add(-0.3D, 0.0D, -0.3D)) && this.method_265(location.clone().add(-0.3D, 0.0D, 0.3D)) && this.method_265(location.clone().add(0.3D, 0.0D, -0.3D));
    }

    private static boolean Ξ(Player player) {
        return class_55.method_298(player, class_50.field_83) && !class_71.method_400(player) && !class_71.O1337(player) && !class_13.method_53(player) && !class_71.BPΞ(player);
    }

    private static boolean Ξ(Player player, double d0) {
        if (class_69.method_374(player, d0)) {
            class_39.method_170(player, class_50.field_83.toString() + "=liquid", 20);
            return true;
        } else {
            return !class_39.method_169(player, class_50.field_83.toString() + "=liquid");
        }
    }

    private boolean Ξ(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : this.method_269(entity.getLocation().add(0.0D, d1, 0.0D)) && this.method_269(entity.getLocation().add(d0, d1, 0.0D)) && this.method_269(entity.getLocation().add(-d0, d1, 0.0D)) && this.method_269(entity.getLocation().add(0.0D, d1, d2)) && this.method_269(entity.getLocation().add(0.0D, d1, -d2)) && this.method_269(entity.getLocation().add(d0, d1, d2)) && this.method_269(entity.getLocation().add(-d0, d1, -d2)) && this.method_269(entity.getLocation().add(d0, d1, -d2)) && this.method_269(entity.getLocation().add(-d0, d1, d2));
    }

    private boolean HΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250) {
                if (material == Material.SLIME_BLOCK) {
                    return false;
                }

                if (class_101.method_512() != class_101.field_251 && (material == Material.END_ROD || material == Material.GRASS_PATH || material == Material.BEETROOT_BLOCK || material == Material.CHORUS_PLANT)) {
                    return false;
                }
            }

            return !class_66.O1337(location) && !class_66.method_336(location) && !class_66.BPPΠ(location) && !class_66.PPPΞ(location) && !class_66.method_333(location) && !class_66.HHΞ(location) && !class_66.method_339(location) && material != Material.COCOA && material != Material.DRAGON_EGG && material != Material.ENDER_PORTAL_FRAME && material != Material.ENCHANTMENT_TABLE && material != Material.FLOWER_POT && material != Material.BREWING_STAND && material != Material.getMaterial(101) && material != Material.LADDER && material != Material.WEB && material != Material.VINE && material != Material.WATER_LILY && material != Material.REDSTONE_COMPARATOR_OFF && material != Material.REDSTONE_COMPARATOR_ON && material != Material.DIODE_BLOCK_ON && material != Material.DIODE_BLOCK_OFF && material != Material.SKULL && material != Material.CACTUS && material != Material.SOIL && material != Material.SOUL_SAND && material != Material.getMaterial(151) && material != Material.getMaterial(178) && material != Material.CAKE_BLOCK && material != Material.getMaterial(127) && material != Material.FIRE;
        }
    }

    static {
        class_50.field_83 = Enums.HackType.NormalMovements;
        class_50.field_87 = new HashMap();
    }
}
