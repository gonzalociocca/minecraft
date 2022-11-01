import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: ah
public class class_42 implements Listener {

    private Enums.HackType Ξ;
    private int Ξ;
    private int Π;
    private double Ξ;
    private double Π;
    private double HHΞ;
    private double HΞ;
    private double BΞ;
    private double BPPΠ;
    private double PΞ;
    private double OΠ;
    private double O1337;

    public class_42() {
        this.field_58 = Enums.HackType.Jesus;
        this.field_59 = 18;
        this.field_60 = 12;
        this.field_61 = 0.19D;
        this.field_62 = 0.225D;
        this.HHΞ = 0.15D;
        this.field_63 = 0.13D;
        this.field_64 = 0.3D;
        this.BPPΠ = 0.45D;
        this.field_65 = 0.68D;
        this.field_66 = 0.5D;
        this.O1337 = 0.15D;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();
            double d0 = playermoveevent.getTo().getY() - (double) playermoveevent.getTo().getBlockY();

            if (!class_66.HHΞ(player, 1.0D, 2.0D, 1.0D) || !class_66.HHΞ(player, 1.0D, 2.0D - d0, 1.0D)) {
                for (int i = -1; i <= 0; ++i) {
                    if (class_66.method_332(player.getLocation().add(0.0D, (double) i, 0.0D))) {
                        class_39.method_170(player, this.field_58.toString() + "=ice", 20);
                        break;
                    }
                }
            }

            if (!this.method_185(player)) {
                this.method_190(player);
                this.method_191(player);
            } else {
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d1 = class_89.method_475(location, location1);
                double d2 = location.getY() - location1.getY();
                double d3 = 0.0D;

                if ((class_66.BBBΠ(player.getLocation()) || !class_66.HHΞ(player, 0.3D, 0.0D, 0.3D)) && !player.getLocation().getBlock().isLiquid()) {
                    class_39.method_170(player, this.field_58.toString() + "=ground", 15);
                } else if (class_39.method_169(player, this.field_58.toString() + "=ground")) {
                    if (class_71.method_410(player) > 95 && !player.getLocation().getBlock().isLiquid() && !player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().isLiquid()) {
                        class_39.method_170(player, this.field_58.toString() + "=air_ticks", 40);
                    }

                    if (d2 < -1.8D) {
                        class_39.method_170(player, this.field_58.toString() + "=y_distance", 20);
                    }

                    if (this.method_193(player, Material.STATIONARY_LAVA, 0.3D)) {
                        if (d2 == 0.0D && class_36.method_133(player, this.field_58.toString() + "=normal") >= 10) {
                            d3 = this.O1337 + (double) this.method_194(player) * this.O1337;
                        } else if (player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && (player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.STATIONARY_LAVA || player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA)) {
                            if ((player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType() != Material.STATIONARY_LAVA || !this.method_183(player, 1.0D, 0.0D, 1.0D)) && (player.getLocation().add(0.0D, -2.0D, 0.0D).getBlock().getType() != Material.STATIONARY_LAVA || !this.method_183(player, 1.0D, 0.0D, 1.0D) || !this.method_183(player, 1.0D, -1.0D, 1.0D))) {
                                d3 = this.BPPΠ;
                            } else {
                                d3 = this.HHΞ;
                            }
                        } else if (class_66.BBΞ(player.getLocation())) {
                            if (player.getFireTicks() <= 1) {
                                d3 = this.field_63;
                            } else {
                                d3 = this.field_64;
                            }
                        } else {
                            d3 = this.BPPΠ;
                        }
                    } else if (this.method_193(player, Material.STATIONARY_WATER, 0.3D)) {
                        if (d2 == 0.0D && class_36.method_133(player, this.field_58.toString() + "=normal") >= 10) {
                            d3 = this.O1337 + (double) this.method_194(player) * this.O1337;
                        } else if (player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && (player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.STATIONARY_WATER || player.getLocation().getBlock().getType() != Material.STATIONARY_WATER)) {
                            if ((player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType() != Material.STATIONARY_WATER || !this.method_183(player, 1.0D, 0.0D, 1.0D)) && (player.getLocation().add(0.0D, -2.0D, 0.0D).getBlock().getType() != Material.STATIONARY_WATER || !this.method_183(player, 1.0D, 0.0D, 1.0D) || !this.method_183(player, 1.0D, -1.0D, 1.0D))) {
                                d3 = this.BPPΠ;
                            } else {
                                d3 = this.field_61;
                            }
                        } else if (class_66.BBΞ(player.getLocation())) {
                            d3 = this.field_62 + (double) this.method_194(player) * 3.5D;
                        } else {
                            d3 = this.BPPΠ + (double) this.method_194(player) * 1.75D;
                        }
                    } else {
                        this.method_190(player);
                    }

                    double d4;

                    if ((!player.getLocation().getBlock().isLiquid() || !player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid()) && (!class_66.method_350(player.getLocation(), Material.STATIONARY_WATER, 0.3D) || !class_66.method_350(player.getLocation().add(0.0D, 1.0D, 0.0D), Material.STATIONARY_WATER, 0.3D)) && (!class_66.method_350(player.getLocation(), Material.STATIONARY_LAVA, 0.3D) || !class_66.method_350(player.getLocation().add(0.0D, 1.0D, 0.0D), Material.STATIONARY_LAVA, 0.3D))) {
                        this.method_191(player);
                    } else {
                        d4 = 0.3D;
                        double d5 = -1.0D;
                        double d6 = Math.abs(d2);

                        if (d6 >= 0.12D) {
                            double d7 = class_73.method_418(d6, 10);

                            if (class_45.method_209(player, this.field_58.toString() + "=repeated=ypos=" + d7)) {
                                long j = class_45.method_210(player, this.field_58.toString() + "=repeated=ypos=" + d7);

                                if (j <= 500L) {
                                    this.method_182(player, location1, "t: ypos(repeated), dy: " + d2 + ", ms: " + j);
                                }
                            }

                            class_45.method_211(player, this.field_58.toString() + "=repeated=ypos=" + d7);
                        }

                        if (player.getFireTicks() > 0) {
                            d4 = 0.5D;
                        } else if (class_18.method_69(player)) {
                            d4 = 0.4D;
                        }

                        if (d2 <= d5 || d2 >= d4 && d2 != 0.5D && !player.hasPotionEffect(PotionEffectType.JUMP) && class_66.HHΞ(player, 1.0D, 0.0D, 1.0D) && class_66.HHΞ(player, 1.0D, 1.0D, 1.0D)) {
                            class_36.method_135(player, this.field_58.toString() + "=pos", 1);
                            if (class_36.method_133(player, this.field_58.toString() + "=pos") == 1 && class_39.method_169(player, this.field_58.toString() + "=air_ticks")) {
                                this.method_182(player, location1, "t: ypos(normal), dy: " + d2);
                            }
                        }
                    }

                    this.method_192(player, d1, d2);
                    if (d3 > 0.0D) {
                        if (class_71.PPPΞ(player)) {
                            d3 = this.field_65;
                        } else if (class_18.method_69(player) && this.field_66 > d3) {
                            d3 = this.field_66;
                        }

                        d4 = this.method_184(player, d3);
                        if (d1 < d4) {
                            this.method_189(player, d1, d2, location1);
                            this.HHΞ(player, d1, location1);
                            this.method_187(player, d1, d2, d0, location1);
                            this.method_186(player, d2, location1);
                            this.method_188(player, d1, location1);
                        }

                        if (d4 > 0.0D && d1 >= d4 && class_39.method_169(player, this.field_58.toString() + "=y_distance")) {
                            class_36.method_135(player, this.field_58.toString() + "=attempts", 1);
                            if (class_36.method_133(player, this.field_58.toString() + "=attempts") == 1 && class_39.method_169(player, this.field_58.toString() + "=air_ticks")) {
                                this.method_182(player, location1, "t: speed, d: " + d1 + ", dm: " + d3 + ", dm_s: " + d4);
                            }
                        }
                    }

                }
            }
        }
    }

    private void Ξ(Player player, Location location, String s) {
        class_38.method_159(player, this.field_58, s);
        if (class_123.method_583(player, this.field_58)) {
            double d0 = player.getLocation().getY() - (double) player.getLocation().getBlockY();

            class_69.method_386(player, location.add(0.0D, -d0, 0.0D));
            class_69.HHΞ(player);
            if (player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().isLiquid()) {
                class_69.method_386(player, player.getLocation().add(0.0D, -1.0D, 0.0D));
            }
        }

    }

    private boolean Ξ(Player player, double d0, double d1, double d2) {
        return player == null ? false : class_66.HHΞ(player, d0, d1, d2) && class_66.method_346(player, d0, d1, d2);
    }

    private double Ξ(Player player, double d0) {
        double d1 = d0;

        if (!class_36.method_137(player, this.field_58.toString() + "=normal")) {
            if (d0 != this.BPPΠ) {
                if (d0 != this.field_61 && d0 != this.HHΞ) {
                    d1 = d0 + 0.35D;
                } else {
                    d1 = d0 + 0.48D;
                }
            } else {
                d1 = d0 + 0.2D;
            }

            d1 += (double) class_71.method_396(player);
        }

        if (!class_39.method_169(player, this.field_58.toString() + "=ice")) {
            d1 += 0.6D;
        }

        return class_41.method_178(class_69.method_385(player, d1, 4.0D, PotionEffectType.SPEED));
    }

    private boolean Ξ(Player player) {
        if (player == null) {
            return false;
        } else {
            byte b0 = player.getLocation().getBlock().getData();

            return !class_71.method_400(player) && !class_10.method_40(player) && !class_71.method_409(player, 1, 0.5D) && class_55.method_298(player, this.field_58) && !class_71.BPΞ(player) && (b0 <= 1 || b0 >= 8) && !class_13.method_53(player) && !class_71.O1337(player) && (!class_66.method_346(player, 0.3D, 1.0D, 0.3D) || !class_66.method_346(player, 0.3D, 0.0D, 0.3D) || !class_66.method_346(player, 0.3D, -1.0D, 0.3D) || !class_66.method_346(player, 0.3D, -2.0D, 0.3D));
        }
    }

    private void Ξ(Player player, double d0, Location location) {
        if (player != null && location != null && class_66.BBΞ(player.getLocation()) && !class_71.PPPΞ(player)) {
            byte b0 = 0;
            double d1 = class_73.method_418(d0, 2);
            double d2 = player.getFireTicks() > 0 ? 0.55D : (class_18.method_69(player) ? 0.4D : (class_71.method_405(player) ? 0.248D : 0.164D));

            if (class_66.HHΞ(player, 1.0D, 0.0D, 1.0D) && class_66.HHΞ(player, 1.0D, 1.0D, 1.0D)) {
                b0 = 1;
            } else if (class_66.HHΞ(player, 0.3D, 0.0D, 0.3D) && class_66.HHΞ(player, 0.3D, 1.0D, 0.3D)) {
                b0 = 2;
            }

            if (b0 != 2) {
                for (int i = -1; i <= 1; ++i) {
                    if (!class_66.method_348(player, 1.0D, (double) i, 1.0D) && !class_66.method_349(player, 1.0D, (double) i, 1.0D)) {
                        b0 = 3;
                        break;
                    }
                }
            }

            Location location1 = player.getLocation();

            if (b0 != 0 && d0 >= d2 && !location1.clone().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid() && (location1.getBlock().isLiquid() || location1.clone().add(0.0D, -1.0D, 0.0D).getBlock().isLiquid() || location1.clone().add(0.0D, -2.0D, 0.0D).getBlock().isLiquid())) {
                if (b0 == 2 && (d1 == 0.34D || d1 == 0.3D || d1 == 0.5D)) {
                    class_36.method_135(player, this.field_58.toString() + "=upwards=irregular", 5);
                    if (class_36.method_133(player, this.field_58.toString() + "=upwards=irregular") >= 3) {
                        this.method_182(player, location, "t: upwards(irregular), dy: " + d0);
                    }
                } else if (b0 != 3 && b0 != 2) {
                    if (class_36.method_137(player, this.field_58.toString() + "=normal")) {
                        this.method_182(player, location, "t: upwards(after), dy: " + d0);
                    } else if ((d0 >= 0.24D && !class_20.method_75(player) || d0 >= 3.0D) && !class_18.method_69(player)) {
                        this.method_182(player, location, "t: upwards(before), dy: " + d0);
                    }
                } else if (d0 >= 0.48D) {
                    this.method_182(player, location, "t: upwards(half-solid), dy: " + d0 + ", case: " + b0);
                } else if (d0 >= 0.24D) {
                    class_36.method_135(player, this.field_58.toString() + "=upwards=half-solid", 5);
                    if (class_36.method_133(player, this.field_58.toString() + "=upwards=half-solid") >= 3) {
                        this.method_182(player, location, "t: upwards[half-solid(repeated)], dy: " + d0);
                    }
                }
            }

        }
    }

    private void Ξ(Player player, double d0, double d1, double d2, Location location) {
        if (player != null && location != null && class_66.BBΞ(player.getLocation()) && !class_71.PPPΞ(player)) {
            if (class_36.method_137(player, this.field_58.toString() + "=normal") && d0 > 0.0D && this.method_194(player) == 0 && (this.method_183(player, 0.0D, 1.0D, 0.0D) && player.getLocation().getBlock().isLiquid() && (d2 >= 0.5D || d2 > 0.0D && d0 >= 0.16D) || this.method_183(player, 0.0D, 0.0D, 0.0D) && this.method_183(player, 0.0D, 1.0D, 0.0D) && player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().isLiquid())) {
                class_36.method_135(player, this.field_58.toString() + "=walking", 20);
                if (class_36.method_133(player, this.field_58.toString() + "=walking") >= 16) {
                    this.method_182(player, location, "t: walking, d: " + d0 + ", dy: " + d1);
                }
            }

        }
    }

    private void Π(Player player, double d0, Location location) {
        if (player != null && location != null && !class_71.PPPΞ(player) && class_36.method_137(player, this.field_58.toString() + "=normal") && d0 >= 0.01D) {
            Location location1 = player.getLocation();
            boolean flag = location1.getBlock().isLiquid();

            location1 = location1.add(0.0D, 0.25D, 0.0D);
            boolean flag1 = !class_66.method_350(location1, Material.STATIONARY_WATER, 0.3D) && !class_66.method_350(location1, Material.STATIONARY_LAVA, 0.3D);

            location1 = location1.add(0.0D, 0.75D, 0.0D);
            boolean flag2 = !class_66.method_350(location1, Material.STATIONARY_WATER, 0.3D) && !class_66.method_350(location1, Material.STATIONARY_LAVA, 0.3D);

            if (flag && flag1 && flag2) {
                class_36.method_135(player, this.field_58.toString() + "=illegal", 20);
                if (class_36.method_133(player, this.field_58.toString() + "=illegal") >= 5) {
                    this.method_182(player, location, "t: illegal, d: " + d0);
                }
            }

        }
    }

    private void Ξ(Player player, double d0, double d1, Location location) {
        if (player != null && location != null && !class_71.PPPΞ(player)) {
            double d2 = player.getLocation().getY() - (double) player.getLocation().getBlockY();
            double d3 = 1.0D - d2;

            if (d1 == 0.0D && d2 != 0.5D && d0 > 0.0D && !class_71.PPΠ(player) && (class_66.HHΞ(player, 0.3D, 0.0D, 0.3D) && class_66.HHΞ(player, 0.3D, 1.0D, 0.3D) && class_66.HHΞ(player, 0.3D, 2.0D, 0.3D) || player.getLocation().getBlock().isLiquid() && player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().isLiquid() && player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid()) && (class_66.method_351(player.getLocation().add(0.0D, -d3, 0.0D), Material.STATIONARY_WATER, 0.3D) || class_66.method_351(player.getLocation().add(0.0D, -d3, 0.0D), Material.STATIONARY_LAVA, 0.3D)) && !player.getLocation().add(0.0D, d3, 0.0D).getBlock().isLiquid()) {
                class_36.method_135(player, this.field_58.toString() + "=stable", 6);
                if (class_36.method_133(player, this.field_58.toString() + "=stable") == 5) {
                    this.method_182(player, location, "t: horizontal, d: " + d0);
                }
            }

        }
    }

    private void HHΞ(Player player, double d0, Location location) {
        if (player != null && location != null && this.method_194(player) <= 3) {
            double d1 = 0.1D;

            if (this.method_194(player) == 1) {
                d1 = 0.175D;
            } else if (this.method_194(player) == 2) {
                d1 = 0.225D;
            } else if (this.method_194(player) == 3) {
                d1 = 0.275D;
            }

            if (d0 >= d1 && !class_66.BBBΠ(player.getLocation().add(0.0D, 1.0D, 0.0D)) && !player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid()) {
                double d2 = class_73.method_418(d0, 12);

                if (class_45.method_209(player, this.field_58.toString() + "=repeated=" + d2)) {
                    long i = class_45.method_210(player, this.field_58.toString() + "=repeated=" + d2);

                    if (i <= 50L) {
                        String s = String.valueOf(d2);

                        if (!s.contains("E-")) {
                            class_36.method_135(player, this.field_58.toString() + "=repeated", 20);
                            if (class_36.method_133(player, this.field_58.toString() + "=repeated") == 4) {
                                this.method_182(player, location, "t: repeated, ms: " + i + ", d: " + d2);
                            }
                        }
                    }
                }

                class_45.method_211(player, this.field_58.toString() + "=repeated=" + d2);
            }

        }
    }

    private void Ξ(Player player) {
        if (class_71.method_396(player) > 0.0F) {
            class_36.method_134(player, this.field_58.toString() + "=normal", -(this.field_59 * 2));
        } else {
            class_36.method_134(player, this.field_58.toString() + "=normal", -this.field_59);
        }

    }

    private void Π(Player player) {
        if (class_39.method_169(player, this.field_58.toString() + "=y_distance")) {
            class_36.method_134(player, this.field_58.toString() + "=pos", -this.field_60);
        } else {
            class_36.method_134(player, this.field_58.toString() + "=pos", -(this.field_60 * 2));
        }

    }

    private void Ξ(Player player, double d0, double d1) {
        if (player != null) {
            int i = class_36.method_133(player, this.field_58.toString() + "=normal") + 1;

            if (d0 > 0.0D) {
                class_36.method_134(player, this.field_58.toString() + "=normal", i);
            }

            int j = class_36.method_133(player, this.field_58.toString() + "=pos") + 1;

            if (d1 != 0.0D) {
                class_36.method_134(player, this.field_58.toString() + "=pos", j);
            }

        }
    }

    private boolean Ξ(Player player, Material material, double d0) {
        if (player != null && material != null) {
            double d1 = player.getLocation().getY() - (double) player.getLocation().getBlockY();

            return player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().getType() == material || class_66.method_351(player.getLocation(), material, d0) || class_66.method_351(player.getLocation().add(0.0D, -d1, 0.0D), material, d0) || class_66.method_351(player.getLocation().add(0.0D, -1.0D, 0.0D), material, d0) || class_66.HHΞ(player, 0.3D, -1.0D, 0.3D) && class_66.method_351(player.getLocation().add(0.0D, -2.0D, 0.0D), material, d0);
        } else {
            return false;
        }
    }

    private int Ξ(Player player) {
        return class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && player.getInventory().getBoots() != null && player.getInventory().getBoots().containsEnchantment(Enchantment.DEPTH_STRIDER) ? player.getInventory().getBoots().getEnchantmentLevel(Enchantment.DEPTH_STRIDER) : 0;
    }
}
