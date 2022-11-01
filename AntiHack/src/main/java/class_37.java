import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

// $FF: renamed from: af
public class class_37 implements Listener {

    private static Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;
    private HashMap<Player, Integer> Π;

    public class_37() {
        this.field_43 = new HashMap();
        this.field_44 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_43.remove(player);
        this.field_44.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerRespawnEvent playerrespawnevent) {
        Player player = playerrespawnevent.getPlayer();

        this.field_43.remove(player);
        this.field_44.remove(player);
        class_38.method_160(player, class_37.field_42, 10);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_43.remove(player);
                this.field_44.remove(player);
            }
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

            if (this.method_148(player) && this.field_43.containsKey(player)) {
                boolean flag = true;

                for (int i = -1; i <= 2; ++i) {
                    if (class_66.method_350(player.getLocation().add(0.0D, (double) i, 0.0D), Material.WEB, 0.3D)) {
                        class_39.method_170(player, class_37.field_42.toString() + "=web", 20);
                        flag = false;
                        break;
                    }
                }

                if (flag && class_69.method_374(player, d0)) {
                    class_39.method_170(player, class_37.field_42.toString() + "=liquid=has", 20);
                    class_39.method_170(player, class_37.field_42.toString() + "=liquid=had", 40);
                    flag = false;
                }

                if (flag && !class_39.method_169(player, class_37.field_42.toString() + "=web")) {
                    flag = false;
                }

                boolean flag1 = !class_39.method_169(player, class_37.field_42.toString() + "=liquid=has");
                boolean flag2 = !class_39.method_169(player, class_37.field_42.toString() + "=liquid=had");

                if (!flag) {
                    this.method_145(player, d0);
                } else {
                    double d1 = ((Double) this.field_43.get(player)).doubleValue();
                    double d2 = Math.abs(d0 - d1);
                    double d3 = location.getY() - (double) location.getBlockY();
                    int j = player.getLocation().getBlockY();
                    double d4 = class_73.method_418(d0, 5);
                    int k = class_71.method_410(player);
                    int l;
                    int i1;

                    if (this.field_44.containsKey(player) && !class_71.method_416(player.getLocation()) && !class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D)) && !class_71.method_416(player.getLocation().add(0.0D, -2.0D, 0.0D)) && !class_71.method_416(player.getLocation().add(0.0D, -3.0D, 0.0D)) && !class_20.method_75(player) && class_120.method_572("Fly.check_glide") && !class_28.method_98(player) && !class_66.method_341(player.getLocation())) {
                        l = ((Integer) this.field_44.get(player)).intValue();
                        i1 = class_18.method_69(player) ? 4 : 2;
                        if (l + i1 <= k && d0 < 0.0D && k >= 20) {
                            class_36.method_135(player, class_37.field_42.toString() + "=packets", 20);
                            if (class_36.method_133(player, class_37.field_42.toString() + "=packets") >= 5) {
                                this.method_147(player, location1, "t: packets, ot: " + l + ", ct: " + k, true);
                            }
                        }
                    }

                    if (class_71.method_416(player.getLocation())) {
                        this.method_145(player, d0);
                    } else {
                        boolean flag3;
                        byte b0;

                        if (!class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D)) && !class_71.method_416(player.getLocation().add(0.0D, -2.0D, 0.0D)) && !class_10.method_40(player) && !class_71.method_403(player) && !class_15.method_61(player) && !class_28.method_98(player) && !flag1 && !class_71.method_405(player)) {
                            if (d0 < 0.0D && class_120.method_572("Fly.check_glide") && !class_18.method_69(player) && !class_14.method_58(player) && !class_20.method_75(player) && j >= 0 && player.getFireTicks() <= 0 && d3 != 0.5D && !class_71.O1337(player) && !class_71.PPPΞ(player) && !class_12.method_49(player) && !flag2) {
                                if (d1 <= 0.0D) {
                                    l = class_36.method_133(player, class_37.field_42.toString() + "=illegal=ticks") + 1;
                                    class_36.method_134(player, class_37.field_42.toString() + "=illegal=ticks", l);
                                    i1 = Math.abs(k - l);
                                    if (d0 > d1 && k >= 12 && i1 >= 12 && l >= 2 && !class_66.BBBΠ(player.getLocation().add(0.0D, -1.0D, 0.0D))) {
                                        class_36.method_135(player, class_37.field_42.toString() + "=illegal=attempts", 20);
                                        if (class_36.method_133(player, class_37.field_42.toString() + "=illegal=attempts") >= 2 || !class_71.method_416(player.getLocation().add(0.0D, -3.0D, 0.0D)) || d0 >= -1.0D) {
                                            this.method_147(player, location1, "t: change, d: " + d0 + ", od: " + d1 + ", a: " + k + ", r: " + d3, true);
                                        }
                                    } else if (!class_13.method_55(player)) {
                                        flag3 = false;
                                        if (k >= 15) {
                                            if (d2 >= 0.08D) {
                                                this.method_147(player, location1, "t: illegal, d: " + d0 + ", ds: " + d2 + ", a: " + k + ", r: " + d3, false);
                                                flag3 = true;
                                            } else if (d0 >= -3.0D) {
                                                double d5 = Math.abs(d2 - class_69.method_372(player));

                                                if (d5 >= 0.035D) {
                                                    this.method_147(player, location, "t: calculated, d: " + d0 + ", ds: " + d2 + ", diff: " + d5 + ", a: " + k + ", r: " + d3, false);
                                                    flag3 = true;
                                                }
                                            }
                                        } else if (d2 >= 0.4D) {
                                            this.method_147(player, location, "t: pre, d: " + d0 + ", ds: " + d2 + ", a: " + k + ", r: " + d3, false);
                                            flag3 = true;
                                        }

                                        if (flag3) {
                                            class_39.method_170(player, class_37.field_42.toString() + "=cancel=up", 5);
                                            class_39.method_170(player, class_37.field_42.toString() + "=cancel=down", 5);
                                        }
                                    }
                                } else {
                                    class_36.method_138(player, class_37.field_42.toString() + "=illegal=ticks");
                                    class_36.method_138(player, class_37.field_42.toString() + "=illegal=attempts");
                                }
                            }

                            if (class_120.method_572("Fly.check_fly")) {
                                boolean flag4 = d2 >= 0.09D && d0 != 0.5D && !class_20.method_75(player) && !class_71.BPPΠ(player) && !class_18.method_69(player) && !class_69.method_376(d0);

                                b0 = 0;
                                if (d0 >= 0.0D && d1 < 0.0D) {
                                    b0 = 1;
                                } else if (d0 > 0.0D && d1 <= 0.0D) {
                                    b0 = 2;
                                }

                                if (b0 != 0) {
                                    class_36.method_135(player, class_37.field_42.toString() + "=neutral", 20);
                                    if (class_36.method_133(player, class_37.field_42.toString() + "=neutral") >= 8 || flag4) {
                                        this.method_147(player, location1, "t: neutral, d: " + d0 + ", od: " + d1 + ", a: " + k + ", i: " + flag4 + ", c: " + b0, true);
                                    }
                                }
                            }
                        }

                        if (this.method_146(player, d0, d4)) {
                            byte b1 = 20;

                            b0 = 2;
                            if (d0 >= 1.0D) {
                                b1 = 1;
                                b0 = 1;
                            } else if (class_11.method_45(player) && d0 >= 0.0D) {
                                b1 = 40;
                                b0 = 6;
                            } else if (class_10.method_40(player) || class_66.method_352(player, 2) || class_15.method_61(player) || player.getFireTicks() > 0 || class_20.method_75(player) && d0 < 0.0D || class_28.method_98(player) || class_18.method_69(player) || flag1) {
                                b1 = 40;
                                b0 = 4;
                            }

                            flag3 = class_39.method_169(player, class_37.field_42.toString() + "=cancel=down");
                            if (class_120.method_572("Fly.check_fly") && !class_71.O1337(player) && (!class_71.method_416(player.getLocation().add(0.0D, -0.25D, 0.0D)) && !class_69.method_376(d0) && d0 != 0.5D || !class_71.method_416(player.getLocation().add(0.0D, -0.99999D, 0.0D)) || !class_71.method_416(player.getLocation()) && class_66.method_348(player, 1.0D, -1.0D, 1.0D) && class_66.HHΞ(player, 1.0D, 0.0D, 1.0D) && class_66.HHΞ(player, 1.0D, 1.0D, 1.0D) && class_66.HHΞ(player, 1.0D, 2.0D, 1.0D) && !class_10.method_40(player) && !class_8.method_32(player) && !class_15.method_61(player))) {
                                boolean flag5 = class_39.method_169(player, class_37.field_42.toString() + "=cancel=up");

                                if (d2 <= 0.08D && flag5 && !class_71.method_405(player) && d0 > 0.0D && (d0 != 0.5D || !class_17.method_65(player))) {
                                    class_36.method_135(player, class_37.field_42.toString() + "=up", b1);
                                    if (class_36.method_133(player, class_37.field_42.toString() + "=up") >= b0) {
                                        this.method_147(player, location1, "t: up, d: " + d0 + ", ds: " + d2 + ", r: " + d3, true);
                                    }
                                }

                                if (k >= 8 && !class_12.method_49(player) && d2 == 0.0D && !class_71.PPΠ(player) && !class_17.method_65(player) && !class_71.PPPΞ(player)) {
                                    boolean flag6 = false;

                                    if ((d0 == 0.0D || flag1) && k < 40) {
                                        class_36.method_135(player, class_37.field_42.toString() + "=stable", b1);
                                        if (class_36.method_133(player, class_37.field_42.toString() + "=stable") == b0) {
                                            flag6 = true;
                                        }
                                    } else {
                                        flag6 = true;
                                    }

                                    if (flag6) {
                                        this.method_147(player, location1, "t: stable, d: " + d0 + ", a: " + k + ", r: " + d3, true);
                                    }
                                }
                            }

                            if (class_120.method_572("Fly.check_glide") && !class_10.method_40(player) && d0 < 0.0D && d0 >= -0.2D && d2 <= 0.07D && flag3 && k >= 5 && j >= 0 && !class_71.PPPΞ(player) && !class_71.method_405(player) && (!class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D)) || k >= 40)) {
                                class_36.method_135(player, class_37.field_42.toString() + "=down", b1);
                                if (class_36.method_133(player, class_37.field_42.toString() + "=down") >= b0) {
                                    if (class_11.method_45(player)) {
                                        class_49.method_251(player, class_37.field_42, "type: down(normal), d: " + d0 + ", ds: " + d2 + ", r: " + d3, false);
                                        if (class_123.method_583(player, class_37.field_42)) {
                                            class_69.method_386(player, location1);
                                            class_69.HHΞ(player);
                                        }
                                    } else {
                                        this.method_147(player, location1, "t: down, d: " + d0 + ", ds: " + d2 + ", r: " + d3, true);
                                    }
                                }
                            }
                        }

                        this.method_145(player, d0);
                    }
                }
            } else {
                this.method_145(player, d0);
            }
        }
    }

    private void Ξ(Player player, double d0) {
        this.field_43.put(player, Double.valueOf(d0));
        this.field_44.put(player, Integer.valueOf(class_71.method_410(player)));
    }

    private boolean Ξ(Player player, double d0, double d1) {
        boolean flag = class_66.method_341(player.getLocation());
        boolean flag1 = d1 == 0.33319D || d1 == -0.098D || d1 == -0.15523D || d1 == -0.0784D;
        boolean flag2 = class_71.method_416(player.getLocation()) || class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D)) || class_71.method_416(player.getLocation().add(0.0D, -2.0D, 0.0D));

        return (!flag1 || !flag2) && !flag || !class_69.method_380(d0) && d0 > 0.0D;
    }

    private void Ξ(Player player, Location location, String s, boolean flag) {
        if (!class_10.method_40(player) && !class_12.method_49(player) && !class_15.method_61(player) && !class_8.method_32(player)) {
            class_38.method_159(player, class_37.field_42, s);
        } else {
            class_36.method_135(player, class_37.field_42.toString() + "=attempts", 20);
            if (class_36.method_133(player, class_37.field_42.toString() + "=attempts") >= 2) {
                class_38.method_159(player, class_37.field_42, s);
            } else {
                class_49.method_251(player, class_37.field_42, s, false);
            }
        }

        if (class_123.method_583(player, class_37.field_42)) {
            class_69.method_386(player, location);
            if (flag) {
                class_69.HHΞ(player);
            }
        }

    }

    private boolean Ξ(Player player) {
        return class_55.method_298(player, class_37.field_42) && !class_71.method_400(player) && !class_66.method_350(player.getLocation(), Material.BED_BLOCK, 1.0D) && !class_13.method_53(player) && !class_71.method_409(player, 1, 0.5D) && player.getLocation().getBlockY() > -60 && !class_71.BPΞ(player);
    }

    static {
        class_37.field_42 = Enums.HackType.Fly;
    }
}
