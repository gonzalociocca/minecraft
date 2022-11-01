import java.util.HashMap;
import java.util.Iterator;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: an
public class class_53 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;
    private HashMap<Player, Double> Π;
    private HashMap<Player, Double> HHΞ;
    private HashMap<Player, Double> HΞ;
    private HashMap<Player, Integer> BΞ;
    private HashMap<Player, Double> BPPΠ;
    private HashMap<Player, Double> PΞ;
    private double Ξ;
    private double Π;
    private double HHΞ;
    private double HΞ;
    private double BΞ;
    private double BPPΠ;
    private double PΞ;
    private double OΠ;
    private double O1337;
    private double PPPΞ;
    private double PΠ;
    private double HΠ;
    private double PPΞ;
    private double OOOΠ;
    private double BΠ;
    private double OΞ;
    private double HHΠ;
    private double BBBΠ;
    private double BBΞ;
    private double OOΞ;
    private double HHHΞ;
    private double BPΞ;
    private double HHHΠ;
    private double PPPPΞ;
    private double PPΠ;
    private double PPPΠ;
    private double BBΠ;
    private double OOΠ;

    public class_53() {
        this.field_96 = Enums.HackType.Speed;
        this.field_97 = new HashMap();
        this.field_98 = new HashMap();
        this.HHΞ = new HashMap();
        this.field_99 = new HashMap();
        this.field_100 = new HashMap();
        this.BPPΠ = new HashMap();
        this.field_101 = new HashMap();
        this.field_102 = 0.292D;
        this.field_103 = 0.325D;
        this.HHΞ = 0.46D;
        this.field_104 = 0.31D;
        this.field_105 = 0.47D;
        this.BPPΠ = 0.525D;
        this.field_106 = 0.3675D;
        this.field_107 = 0.42D;
        this.O1337 = 0.7D;
        this.PPPΞ = 0.85D;
        this.field_108 = 1.2D;
        this.field_109 = 0.68D;
        this.PPΞ = 0.71D;
        this.OOOΠ = 0.7D;
        this.field_110 = 0.16D;
        this.field_111 = 0.59D;
        this.HHΠ = 0.6D;
        this.BBBΠ = 0.18D;
        this.BBΞ = 1.5D;
        this.OOΞ = 0.8D;
        this.HHHΞ = 0.62D;
        this.BPΞ = 2.15D;
        this.HHHΠ = 3.5D;
        this.PPPPΞ = 3.0D;
        this.PPΠ = 1.25D;
        this.PPPΠ = 6.0D;
        this.BBΠ = 0.5D;
        this.OOΠ = 0.75D;
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_97.remove(player);
        this.field_98.remove(player);
        this.field_99.remove(player);
        this.field_100.remove(player);
        this.BPPΠ.remove(player);
        this.HHΞ.remove(player);
        this.field_101.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_97.remove(player);
                this.field_98.remove(player);
                this.HHΞ.remove(player);
                this.field_101.remove(player);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (this.method_288(player)) {
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d0 = class_89.method_475(location1, location);

                if (d0 >= this.field_110) {
                    double d1 = location.getY() - (double) location.getBlockY();
                    double d2 = location.getY() - location1.getY();
                    double d3 = 0.0D;
                    boolean flag = false;
                    boolean flag1 = false;
                    int i = class_71.method_410(player);
                    boolean flag2 = this.HHΞ.containsKey(player);
                    double d4 = !flag2 ? 0.0D : ((Double) this.HHΞ.get(player)).doubleValue();
                    String s;

                    if (this.method_287(player) && d1 <= 0.25D && class_71.method_416(player.getLocation().add(0.0D, -d1, 0.0D))) {
                        s = "blocks";
                        flag = true;
                        if (class_66.method_352(player, 2)) {
                            d3 = this.method_289(player, d3, this.PPPΞ, 15);
                        } else {
                            d3 = this.method_289(player, d3, this.O1337, 15);
                        }
                    } else if (class_66.method_340(player.getLocation().add(0.0D, 1.0D, 0.0D), true)) {
                        s = "solid";
                        d3 = this.method_289(player, d3, this.BBΞ, 10);
                    } else if (class_66.method_346(player, 0.3D, 0.0D, 0.3D) && class_66.method_346(player, 0.3D, 1.0D, 0.3D)) {
                        if (this.field_97.containsKey(player) && (((Double) this.field_97.get(player)).doubleValue() == 0.0D && d2 == 0.0D || d2 - ((Double) this.field_97.get(player)).doubleValue() == 0.0D && !class_71.method_416(player.getLocation()))) {
                            if (player.isSneaking() && class_120.method_572("Speed.check_sneaking")) {
                                s = "sneaking";
                                d3 = this.method_289(player, d3, this.field_110, 0);
                            } else if (player.getLocation().getBlock().getType() == Material.SOUL_SAND) {
                                s = "soul-sand";
                                d3 = this.method_289(player, d3, this.BBBΠ, 5);
                            } else {
                                if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.SLIME_BLOCK) {
                                    d3 = this.method_289(player, d3, this.field_104, 0);
                                } else if (player.isSprinting()) {
                                    d3 = this.method_289(player, d3, this.field_103, 0);
                                } else {
                                    d3 = this.method_289(player, d3, this.field_102, 0);
                                }

                                if (d3 == this.field_102 && class_120.method_572("Speed.check_walking")) {
                                    s = "walking";
                                    d3 = this.method_289(player, d3, d3, 10);
                                } else {
                                    s = "ground";
                                    d3 = this.method_289(player, d3, d3, 15);
                                }
                            }
                        } else if (class_73.method_418(d2, 6) != 0.419999D && class_73.method_418(d1, 6) != 0.419999D) {
                            if ((d1 == 0.0D || d1 >= 0.084D && d1 <= 0.15D || d1 == 0.5D || d1 >= 0.75D) && d2 == 0.0D && !class_66.method_347(player, 0.2D, 0.0D, 0.2D) && class_71.method_416(player.getLocation())) {
                                s = "ground";
                                d3 = this.method_289(player, d3, this.HHΞ, 5);
                            } else {
                                s = "air";
                                if ((!this.field_99.containsKey(player) || ((Double) this.field_99.get(player)).doubleValue() != this.field_108) && class_39.method_169(player, this.field_96.toString() + "=ice=reminder")) {
                                    if (class_66.method_352(player, 3)) {
                                        if (!class_66.HHΞ(player, 0.3D, 2.0D, 0.3D)) {
                                            d3 = this.method_289(player, d3, this.PPPΞ, 12);
                                        } else {
                                            d3 = this.method_289(player, d3, this.field_105, 12);
                                        }
                                    } else if (!class_66.method_350(player.getLocation(), Material.CARPET, 0.3D) && !class_66.method_350(player.getLocation(), Material.SNOW, 0.3D)) {
                                        d3 = this.method_289(player, d3, this.field_106, 8);
                                    } else {
                                        d3 = this.method_289(player, d3, this.field_107, 8);
                                    }
                                } else {
                                    d3 = this.method_289(player, d3, this.field_111, 10);
                                }
                            }
                        } else {
                            s = "ypos";
                            if (!class_39.method_169(player, this.field_96.toString() + "=ice=reminder")) {
                                d3 = this.method_289(player, d3, this.PPΞ, 0);
                            } else {
                                d3 = this.method_289(player, d3, this.field_109, 0);
                            }
                        }
                    } else {
                        s = "liquid";
                        d3 = this.method_289(player, d3, this.OOΞ, 10);
                    }

                    for (int j = -2; j <= 0; ++j) {
                        if (class_66.method_332(player.getLocation().add(0.0D, (double) j, 0.0D))) {
                            class_39.method_170(player, this.field_96.toString() + "=ice=reminder", 20);
                            if (s != "blocks") {
                                flag1 = true;
                                d3 = this.method_289(player, d3, this.field_111, 0);
                                if (d3 == this.field_111) {
                                    s = "ice";
                                    d3 = this.method_289(player, d3, d3, 15);
                                }
                                break;
                            }

                            if (j >= -1) {
                                d3 = this.method_289(player, d3, this.field_108, 25);
                                break;
                            }
                        }
                    }

                    if ((class_71.method_416(player.getLocation()) || class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D))) && player.isSprinting() && (d2 == 0.375D || d2 >= 0.5D && d2 < 0.6D || this.field_98.containsKey(player) && class_89.method_482(d1, ((Double) this.field_98.get(player)).doubleValue()) == 0.5D)) {
                        if (!class_66.method_336(player.getLocation().add(0.0D, -1.0D, 0.0D)) && !class_66.method_336(player.getLocation().add(0.0D, -d1, 0.0D))) {
                            if (!class_66.method_348(player, 1.0D, -1.0D, 1.0D) || !class_66.method_348(player, 1.0D, -d1, 1.0D) || !class_66.method_348(player, 1.0D, 0.0D, 1.0D)) {
                                s = "upwards";
                                d3 = this.method_289(player, d3, this.OOOΠ, 15);
                                class_39.method_170(player, this.field_96.toString() + "=upwards=reminder", 30);
                            }
                        } else {
                            Location location2 = player.getLocation();

                            location2.setPitch(0.0F);
                            if (class_66.method_336(location2.add(location2.getDirection().multiply(1.0D)))) {
                                s = "stairs";
                                d3 = this.method_289(player, d3, this.OOOΠ, 15);
                            }
                        }
                    }

                    if (!class_39.method_169(player, this.field_96.toString() + "=upwards=reminder")) {
                        d3 = this.method_289(player, d3, this.OOOΠ, 15);
                    }

                    Iterator iterator = player.getNearbyEntities(3.0D, 3.0D, 3.0D).iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        if (entity instanceof Boat) {
                            s = "entity";
                            d3 = this.method_289(player, d3, this.HHΞ, 5);
                        }
                    }

                    if ((d3 == this.field_106 || d3 == this.HHΞ) && d1 == 0.0D && d2 == 0.0D && player.isSprinting() && d3 != this.OOOΠ) {
                        s = "ground";
                        d3 = this.method_289(player, d3, this.BPPΠ, 10);
                    }

                    if (class_66.method_350(player.getLocation().add(0.0D, -d1, 0.0D), Material.BED_BLOCK, 1.0D) || class_66.method_350(player.getLocation().add(0.0D, -d1, 0.0D), Material.BED_BLOCK, 0.3D)) {
                        class_39.method_170(player, this.field_96.toString() + "=bed", 10);
                    }

                    if (!class_39.method_169(player, this.field_96.toString() + "=bed")) {
                        d3 = this.method_289(player, d3, this.HHHΞ, 10);
                    }

                    if (class_71.OOΞ(player)) {
                        s = "push";
                        d3 = this.method_289(player, d3, this.PPPΠ, 8);
                    } else if (class_71.method_409(player, 1, 0.5D)) {
                        d3 = this.method_289(player, d3, this.BBΠ, 5);
                    }

                    if (class_28.method_98(player)) {
                        d3 = this.method_289(player, d3, this.OOΠ, 10);
                    }

                    if (class_17.method_65(player) || (d3 == this.field_111 || d3 == this.field_106 || d3 == this.PPΞ) && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && (class_66.method_350(player.getLocation(), Material.SLIME_BLOCK, 1.0D) || class_66.method_350(player.getLocation().add(0.0D, 1.0D, 0.0D), Material.SLIME_BLOCK, 1.0D))) {
                        d3 = this.method_289(player, d3, this.PPΠ, 5);
                    }

                    if (class_18.method_69(player)) {
                        d3 = this.method_289(player, d3, this.HHΠ, 5);
                    }

                    if (this.field_99.containsKey(player) && this.field_100.containsKey(player)) {
                        d3 = this.method_289(player, d3, ((Double) this.field_99.get(player)).doubleValue(), 0);
                    }

                    float f = class_71.method_396(player);
                    double d5 = this.method_286(player, s, d3, f);
                    boolean flag3;
                    boolean flag4;

                    if (f == 0.0F && !class_10.method_40(player) && !class_13.method_53(player) && !class_71.OOΞ(player) && !class_71.HHΞ(player) && !class_71.BPPΠ(player) && !class_28.method_98(player) && !class_17.method_65(player) && !class_71.O1337(player)) {
                        boolean flag5;
                        double d6;

                        if (i <= 12 && !class_71.method_416(player.getLocation()) && class_66.HHΞ(player, 1.0D, 2.0D, 1.0D) && !class_66.method_352(player, 2) && !class_20.method_75(player) && (d3 == this.field_106 || d3 == this.field_103 || d3 == this.BPPΠ || d3 == this.HHΞ || d3 == this.field_110 || d3 == this.field_102)) {
                            flag5 = true;

                            for (int k = -1; k <= 1; ++k) {
                                if (!class_66.method_348(player, 1.0D, (double) k, 1.0D)) {
                                    flag5 = false;
                                    break;
                                }
                            }

                            if (flag5 && d2 > 0.0D) {
                                if (!class_69.method_376(d2)) {
                                    if (!class_66.method_352(player, 3)) {
                                        class_36.method_135(player, this.field_96.toString() + "=hop=illegal", 10);
                                        if (class_36.method_133(player, this.field_96.toString() + "=hop=illegal") >= 4) {
                                            this.method_285(player, location1, "t: hop(illegal), d: " + d0 + ", dm: " + d3, true);
                                        }
                                    }
                                } else {
                                    d6 = class_73.method_418(d2, 3);
                                    flag3 = false;
                                    if (d6 == 0.083D && d0 >= 0.35D) {
                                        flag3 = true;
                                    } else if (d6 == 0.164D && d0 >= 0.355D) {
                                        flag3 = true;
                                    } else if (d6 == 0.248D && d0 >= 0.36D) {
                                        flag3 = true;
                                    } else if (d6 == 0.333D && d0 >= 0.365D) {
                                        flag3 = true;
                                    }

                                    if (flag3) {
                                        this.method_285(player, location1, "t: hop(acceleration), v: " + d6 + ", d: " + d0 + ", dm: " + d3, true);
                                    }
                                }
                            }
                        }

                        double d7;

                        if (flag2 && d3 == this.OOOΠ) {
                            d7 = Math.abs(d0 - d4);
                            if (d0 > d4 && d7 >= 0.35D && d2 == 0.5D) {
                                this.method_285(player, location1, "t: upwards(differece), d: " + d0 + ", od: " + d4 + ", diff: " + d7, true);
                            }
                        }

                        int l;

                        if (flag2) {
                            d7 = Math.abs(d0 - d4);
                            l = class_36.method_133(player, this.field_96.toString() + "=ice=ticks");
                            if (flag1 && (d3 == this.field_111 || d3 == this.PPΞ)) {
                                ++l;
                                class_36.method_134(player, this.field_96.toString() + "=ice=ticks", l);
                            } else {
                                l = 0;
                                class_36.method_138(player, this.field_96.toString() + "=ice=ticks");
                            }

                            if (d0 > d4) {
                                if (flag && d3 == this.field_108 && d7 >= 0.3D) {
                                    this.method_285(player, location1, "t: ice-speed(blocks), diff: " + d7 + ", d: " + d0 + ", od: " + d4, true);
                                } else if (l >= 20 && d0 >= 0.5D && (d7 >= 0.25D || d7 <= 0.15D)) {
                                    this.method_285(player, location1, "t: ice-speed(normal), diff: " + d7 + ", d: " + d0 + ", od: " + d4, true);
                                }
                            }
                        }

                        if (d3 == this.field_111 && !class_17.method_65(player) && class_71.method_416(player.getLocation()) & d0 >= 0.36D && class_36.method_137(player, this.field_96.toString() + "=" + s) && class_66.method_332(player.getLocation().add(0.0D, -1.0D, 0.0D)) && class_66.method_332(player.getLocation().add(1.0D, -1.0D, 0.0D)) && class_66.method_332(player.getLocation().add(-1.0D, -1.0D, 0.0D)) && class_66.method_332(player.getLocation().add(0.0D, -1.0D, 1.0D)) && class_66.method_332(player.getLocation().add(0.0D, -1.0D, -1.0D)) && class_66.method_332(player.getLocation().add(1.0D, -1.0D, 1.0D)) && class_66.method_332(player.getLocation().add(-1.0D, -1.0D, -1.0D)) && class_66.method_332(player.getLocation().add(-1.0D, -1.0D, 1.0D)) && class_66.method_332(player.getLocation().add(1.0D, -1.0D, -1.0D))) {
                            class_36.method_135(player, this.field_96.toString() + "=ice_speed", 10);
                            if (class_36.method_133(player, this.field_96.toString() + "=ice_speed") == 4) {
                                class_36.method_138(player, this.field_96.toString() + "=ice_speed");
                                this.method_285(player, location1, "t: ice-speed(ground), d: " + d0 + ", dm: " + d3, true);
                            }
                        }

                        if (d3 == this.field_106 && i > 0 && i <= 11 && class_66.HHΞ(player, 1.0D, 0.0D, 1.0D) && class_66.HHΞ(player, 1.0D, 1.0D, 1.0D) && class_66.method_348(player, 1.0D, -1.0D, 1.0D) && !class_71.method_416(player.getLocation().add(0.0D, -0.15D, 0.0D))) {
                            if (!this.BPPΠ.containsKey(player)) {
                                this.BPPΠ.put(player, Double.valueOf(d0));
                            } else {
                                this.BPPΠ.put(player, Double.valueOf(((Double) this.BPPΠ.get(player)).doubleValue() + d0));
                            }
                        } else if (d3 != this.field_109 && this.BPPΠ.containsKey(player)) {
                            d7 = ((Double) this.BPPΠ.get(player)).doubleValue();
                            if (d7 <= 1.65D) {
                                double d8 = class_73.method_418(d7, 3);

                                if (class_45.method_209(player, this.field_96.toString() + "=overall=" + d8)) {
                                    long i1 = class_45.method_210(player, this.field_96.toString() + "=overall=" + d8);

                                    if (i1 <= 700L) {
                                        class_36.method_135(player, this.field_96.toString() + "=overall", 30);
                                        if (class_36.method_133(player, this.field_96.toString() + "=overall") >= 2) {
                                            this.method_285(player, location1, "t: overall, v: " + d8 + ", t: " + i1, true);
                                        }
                                    }
                                }

                                class_45.method_211(player, this.field_96.toString() + "=overall=" + d8);
                            }

                            this.BPPΠ.remove(player);
                        }

                        if (d0 < d5 && class_39.method_169(player, this.field_96.toString() + "=bed") && !class_66.method_352(player, 2) && (d3 == this.field_106 || d3 == this.field_102 || d3 == this.field_103 || d3 == this.field_110 || d3 == this.BPPΠ)) {
                            float f1 = player.getFallDistance();
                            boolean flag6 = true;

                            if (d3 == this.field_106 || d3 == this.BPPΠ) {
                                for (l = -1; l <= 1; ++l) {
                                    if (!class_66.method_348(player, 1.0D, (double) l, 1.0D)) {
                                        flag6 = false;
                                        break;
                                    }
                                }
                            }

                            if (f1 <= 1.2F && flag6) {
                                flag4 = f1 != 0.0F && f1 != 0.0784F && f1 != 0.075444065F && f1 != 0.22777925F && f1 != 0.233632F && f1 != 0.45546773F && f1 != 0.46415937F && f1 != 0.7570025F && f1 != 0.7684762F && f1 != 1.1309065F && f1 != 1.1451067F;
                                if (d0 >= 0.215D && flag4 && !class_71.method_409(player, 1, 0.5D) && i <= 12 && player.getFireTicks() <= 0 && !class_20.method_75(player) && !class_11.method_45(player)) {
                                    this.method_285(player, location1, "t: fall-distance(illegal), d: " + d0 + ", dm: " + d3 + ", fd: " + f1 + ", a: " + i + ", r: " + d1 + ", dy: " + d2, true);
                                } else if (!class_71.method_416(player.getLocation()) && class_66.method_340(player.getLocation().add(0.0D, -1.0D, 0.0D), false) && d0 > 0.335D && f1 > 0.0F) {
                                    this.method_285(player, location1, "t: fall-distance(normal), d: " + d0 + ", dm: " + d3 + ", r: " + d1 + ", dy: " + d2, true);
                                } else if (class_71.method_416(player.getLocation()) && d0 >= 0.36D && f1 == 0.0F && d1 == 0.0D) {
                                    class_36.method_135(player, this.field_96.toString() + "=fall-distance", 10);
                                    if (class_36.method_133(player, this.field_96.toString() + "=fall-distance") == 2) {
                                        class_36.method_138(player, this.field_96.toString() + "=fall-distance");
                                        this.method_285(player, location1, "t: fall-distance(ground), d: " + d0 + ", dm: " + d3 + ", r: " + d1 + ", dy: " + d2, true);
                                    }
                                }
                            }
                        }

                        if (d3 == this.field_106 && !class_71.method_416(player.getLocation()) && d0 >= 0.34D && d0 < d5) {
                            class_36.method_135(player, this.field_96.toString() + "=hop=motion", 20);
                            if (class_36.method_133(player, this.field_96.toString() + "=hop=motion") == 12) {
                                class_36.method_138(player, this.field_96.toString() + "=hop=motion");
                                this.method_285(player, location1, "t: hop(motion), d: " + d0 + ", dm: " + d3, true);
                            }
                        }

                        if (d3 == this.BPPΠ && d0 >= 0.36D && d0 < d5 && d2 <= 0.0D) {
                            class_36.method_135(player, this.field_96.toString() + "=ground_jump_speed", 10);
                            if (class_36.method_133(player, this.field_96.toString() + "=ground_jump_speed") == 3) {
                                class_36.method_138(player, this.field_96.toString() + "=ground_jump_speed");
                                this.method_285(player, location1, "t: ground jump speed, d: " + d0 + ", dm: " + d3, true);
                            }
                        }

                        if (d0 >= 0.295D && d0 < d5 && !flag) {
                            flag5 = false;
                            d6 = class_73.method_418(d0, 8);
                            if (d3 == this.field_106 && !class_71.method_416(player.getLocation()) && class_71.method_416(player.getLocation().add(0.0D, -0.25D, 0.0D))) {
                                flag5 = true;
                            } else if (d3 == this.field_103 || d3 == this.field_102 || d3 == this.field_110) {
                                flag5 = true;
                            }

                            if (flag5) {
                                class_36.method_135(player, this.field_96.toString() + "=high_normal_speed", 20);
                                if (class_36.method_133(player, this.field_96.toString() + "=high_normal_speed") >= 18) {
                                    this.method_285(player, location1, "t: high normal speed, d: " + d0 + ", dm: " + d3, true);
                                }
                            }

                            if (class_45.method_209(player, this.field_96.toString() + "=repeated=" + d6)) {
                                long j1 = class_45.method_210(player, this.field_96.toString() + "=repeated=" + d6);

                                if (j1 <= 250L) {
                                    class_36.method_135(player, this.field_96.toString() + "=repeated", 10);
                                    if (class_36.method_133(player, this.field_96.toString() + "=repeated") == 3) {
                                        class_36.method_138(player, this.field_96.toString() + "=repeated");
                                        this.method_285(player, location1, "t: repeated, t: " + j1 + ", d: " + d6 + ", dm: " + d3, true);
                                    }
                                }
                            }

                            class_45.method_211(player, this.field_96.toString() + "=repeated=" + d6);
                        }

                        if (d3 == this.field_109 && s == "ypos" && d0 >= 0.4D && d0 < d5) {
                            class_36.method_135(player, this.field_96.toString() + "=ypos=attempts", 20);
                            if (class_36.method_133(player, this.field_96.toString() + "=ypos=attempts") == 4) {
                                class_36.method_138(player, this.field_96.toString() + "=ypos=attempts");
                                this.method_285(player, location1, "t: ypos, d: " + d0 + ", dm: " + d3 + ", dm_s: " + d5, true);
                            }
                        }
                    }

                    if (d3 > 0.0D && d5 > 0.0D && d0 >= d5) {
                        byte b0 = 1;
                        byte b1 = 1;

                        flag4 = true;
                        flag3 = class_10.method_40(player);
                        boolean flag7 = class_13.method_53(player);
                        boolean flag8 = class_8.method_32(player);

                        if (class_71.method_416(player.getLocation()) || class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D))) {
                            if (d5 == this.field_103 && d0 < 0.36D || (d5 == this.field_106 || d5 == this.HHΞ) && d0 < 0.63D || d5 == this.field_110 && d0 < 0.2D) {
                                if (class_45.method_209(player, this.field_96.toString() + "=limit=protection")) {
                                    long k1 = class_45.method_210(player, this.field_96.toString() + "=limit=protection");

                                    if (k1 >= 4000L) {
                                        flag4 = false;
                                    }
                                } else {
                                    flag4 = false;
                                }
                            }

                            class_45.method_211(player, this.field_96.toString() + "=limit=protection");
                        }

                        if (flag3) {
                            b0 = 12;
                            b1 = 10;
                        } else if (class_71.method_404(player)) {
                            b0 = 5;
                            b1 = 2;
                        }

                        if (flag4) {
                            if (!class_36.method_137(player, this.field_96.toString() + "=" + s) && class_39.method_169(player, this.field_96.toString() + "=protection=cooldown")) {
                                class_36.method_138(player, this.field_96.toString() + "=" + s);
                            }

                            class_36.method_135(player, this.field_96.toString() + "=" + s, b0);
                            if ((class_36.method_133(player, this.field_96.toString() + "=" + s) != b1 || flag7 || flag8) && (!flag3 || d0 < this.BPΞ || this.BPΞ < d5) && (!flag7 || d0 < this.HHHΠ || this.HHHΠ < d5) && (!flag8 || d0 < this.PPPPΞ || this.PPPPΞ < d5)) {
                                if (!class_36.method_137(player, this.field_96.toString() + "=" + s) && !flag3 && !flag7 && !flag8) {
                                    d3 = 1.75D + (double) f;
                                    d5 = class_69.method_385(player, d3, 4.0D, PotionEffectType.SPEED);
                                    if (d0 >= d5) {
                                        this.method_285(player, location1, "t: cancelled, r: " + s + ", d: " + d0 + ", dm: " + d3 + ", dm_s: " + d5, true);
                                    }
                                }
                            } else {
                                class_36.method_138(player, this.field_96.toString() + "=" + s);
                                String s1 = "type: normal, reason: " + s + ", d: " + d0 + ", dm: " + d3 + ", dm_s: " + d5;
                                double d9 = d0 - d5;

                                if (d9 >= 0.4D) {
                                    this.method_285(player, location1, s1, false);
                                } else {
                                    this.method_285(player, location1, s1, true);
                                }
                            }
                        }
                    }

                    if (this.field_100.containsKey(player)) {
                        this.field_100.put(player, Integer.valueOf(((Integer) this.field_100.get(player)).intValue() - 1));
                        if (((Integer) this.field_100.get(player)).intValue() <= 0) {
                            this.field_100.remove(player);
                            this.field_99.remove(player);
                        }
                    }

                    this.HHΞ.put(player, Double.valueOf(d0));
                    this.field_97.put(player, Double.valueOf(d2));
                    this.field_98.put(player, Double.valueOf(d1));
                    this.field_101.put(player, Double.valueOf(d3));
                }
            }
        }
    }

    private void Ξ(Player player, Location location, String s, boolean flag) {
        boolean flag1 = true;

        if (flag) {
            class_36.method_135(player, this.field_96.toString() + "=attempts", 40);
            if (class_36.method_133(player, this.field_96.toString() + "=attempts") >= 2) {
                if (class_11.method_45(player) && class_38.method_154(player, Enums.HackType.ImpossibleActions) <= 1) {
                    class_49.method_251(player, this.field_96, s, false);
                } else {
                    class_38.method_159(player, this.field_96, s);
                }
            } else if (class_38.method_154(player, this.field_96) == 0) {
                flag1 = false;
            } else {
                class_49.method_251(player, this.field_96, s, false);
            }
        } else {
            class_38.method_159(player, this.field_96, s);
        }

        if (flag1 && class_123.method_583(player, this.field_96)) {
            class_69.method_386(player, location);
            class_69.HHΞ(player);
        }

    }

    private double Ξ(Player player, String s, double d0, float f) {
        double d1 = d0 + (double) f * 1.45D;

        if (class_71.method_403(player)) {
            if (s == "air") {
                d1 += 0.26D;
            }
        } else if (class_71.method_404(player) && d1 != this.field_110 && d1 != this.field_102) {
            double d2 = d1 != this.field_111 && d1 != this.field_108 ? 0.275D : 0.4D;

            d1 += d2;
        }

        if (class_10.method_40(player)) {
            d1 += 0.3D;
        }

        if (class_71.method_413(player, PotionEffectType.JUMP) > 250) {
            d1 += 0.5D;
        }

        if (class_111.method_540(player)) {
            d1 += 0.3D;
        }

        return class_41.method_178(class_69.method_385(player, d1, 4.0D, PotionEffectType.SPEED));
    }

    private boolean Ξ(Player player) {
        if (player == null) {
            return false;
        } else {
            double d0 = 1.0D;
            double d1 = 1.0D;
            boolean flag = false;
            double d2 = player.getLocation().getY() - (double) player.getLocation().getBlockY();
            double d3 = 2.0D;

            if (d2 >= 0.8D) {
                d3 += 1.0D - d2;
            }

            for (double d4 = 2.0D - d2; d4 <= d3; ++d4) {
                if (class_66.BBBΠ(player.getLocation().add(0.0D, d4, 0.0D))) {
                    return true;
                }

                if (class_66.BBBΠ(player.getLocation().add(d0, d4, 0.0D)) || class_66.BBBΠ(player.getLocation().add(-d0, d4, 0.0D)) || class_66.BBBΠ(player.getLocation().add(0.0D, d4, d1)) || class_66.BBBΠ(player.getLocation().add(0.0D, d4, -d1)) || class_66.BBBΠ(player.getLocation().add(d0, d4, d1)) || class_66.BBBΠ(player.getLocation().add(-d0, d4, -d1)) || class_66.BBBΠ(player.getLocation().add(-d0, d4, d1)) || class_66.BBBΠ(player.getLocation().add(d0, d4, -d1))) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                for (int i = 0; (double) i <= 1.0D - d2; ++i) {
                    if (class_66.method_340(player.getLocation().add(d0, (double) i, 0.0D), true) || class_66.method_340(player.getLocation().add(-d0, (double) i, 0.0D), true) || class_66.method_340(player.getLocation().add(0.0D, (double) i, d1), true) || class_66.method_340(player.getLocation().add(0.0D, (double) i, -d1), true) || class_66.method_340(player.getLocation().add(d0, (double) i, d1), true) || class_66.method_340(player.getLocation().add(-d0, (double) i, -d1), true) || class_66.method_340(player.getLocation().add(-d0, (double) i, d1), true) || class_66.method_340(player.getLocation().add(d0, (double) i, -d1), true)) {
                        return false;
                    }
                }
            }

            return flag;
        }
    }

    private boolean Π(Player player) {
        return class_55.method_298(player, this.field_96) && !class_71.method_400(player) && !class_71.BPΞ(player);
    }

    private double Ξ(Player player, double d0, double d1, int i) {
        if (d1 >= d0) {
            d0 = d1;
        }

        boolean flag = true;

        if (this.field_99.containsKey(player) && d0 < ((Double) this.field_99.get(player)).doubleValue()) {
            flag = false;
        }

        if (flag && i > 0) {
            this.field_99.put(player, Double.valueOf(d0));
            this.field_100.put(player, Integer.valueOf(i));
        }

        return d0;
    }
}
