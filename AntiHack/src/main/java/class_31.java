import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

// $FF: renamed from: ac
public class class_31 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;

    public class_31() {
        this.field_29 = Enums.HackType.Clip;
        this.field_30 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_30.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                this.field_30.remove(player);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (this.method_112(player)) {
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d0 = class_89.method_475(location, location1);
                double d1 = location.getY() - location1.getY();
                double d2 = location.getY() - (double) location.getBlockY();
                float f = class_71.method_396(player) / 2.0F;
                double d3 = 0.4D + (double) f;
                double d4 = class_69.method_385(player, d3, 4.0D, PotionEffectType.SPEED);
                double d5 = class_17.method_65(player) ? 1.6D : 1.35D;

                if (this.field_30.containsKey(player)) {
                    double d6 = ((Double) this.field_30.get(player)).doubleValue();

                    if (d6 <= -(d5 - 0.0784000015258D)) {
                        for (int i = -1; i <= 1; ++i) {
                            if (!class_66.method_348(player, 0.3D, (double) i, 0.3D)) {
                                class_39.method_170(player, this.field_29.toString() + "=climb", 40);
                                break;
                            }
                        }
                    }

                    if (class_13.method_53(player)) {
                        d5 = 15.0D;
                    } else if (class_71.O1337(player)) {
                        d5 = 6.0D;
                    } else if (!class_71.BPΞ(player) && class_39.method_169(player, this.field_29.toString() + "=climb")) {
                        if (this.method_111(player, d1) && d1 < 0.0D) {
                            d5 = 4.0D;
                        } else if (class_14.method_57(player)) {
                            d5 = 3.25D;
                        } else if (!class_10.method_40(player) && !class_12.method_49(player)) {
                            if (class_14.method_58(player)) {
                                d5 = 1.5D;
                            }
                        } else {
                            d5 = 2.0D;
                        }
                    } else {
                        d5 = 5.0D;
                    }

                    if (location.getBlockY() != location1.getBlockY() && !class_20.method_75(player)) {
                        double d7 = Math.abs(d1);
                        boolean flag = class_71.method_416(player.getLocation()) || class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D));

                        if ((class_71.method_410(player) <= 1 || d6 > -d5 && flag) && playermoveevent.getFrom().getBlockY() >= 0 || d7 >= 4.0D && !class_13.method_53(player) || d7 >= 15.0D) {
                            String s = d1 >= 0.0D ? "up" : "down";

                            if (d7 >= d5 && (d1 > 0.0D || d1 < 0.0D && !class_20.method_76(player))) {
                                this.method_110(player, "t: instant, d: " + d7 + ", l: " + d5 + ", r: " + s, location1);
                                return;
                            }
                        }
                    }
                }

                if (!class_10.method_40(player) && !class_71.HHHΠ(player)) {
                    byte b0 = 0;
                    double d8 = 0.299D;
                    String s1 = "none";
                    Block block = player.getLocation().getBlock();
                    Block block1 = player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock();

                    if (this.method_114(player, d8, 0.0D, d8) && this.method_114(player, d8, 1.0D, d8)) {
                        b0 = 1;
                    } else if (this.method_114(player, d8, 0.0D, d8)) {
                        b0 = 2;
                    } else if (this.method_114(player, d8, 1.0D, d8)) {
                        b0 = 3;
                    }

                    if (b0 != 0 && !class_17.method_65(player)) {
                        if (b0 == 1) {
                            s1 = "(" + class_66.method_343(block) + ", " + class_66.method_343(block1) + ")";
                        } else if (b0 == 2) {
                            s1 = class_66.method_343(block);
                        } else if (b0 == 3) {
                            s1 = class_66.method_343(block1);
                        }

                        if (d3 > 0.0D && d4 > 0.0D && d0 >= d4) {
                            this.method_110(player, "t: speed(normal), c " + b0 + ", b: " + s1 + ", ds: " + d0 + ", dm: " + d3 + ", dm_s: " + d4, player.getLocation());
                        } else if (!class_71.O1337(player) && (d1 > 0.5D || d1 <= -0.8D)) {
                            this.method_110(player, "t: ypos(normal), c: " + b0 + ", b: " + s1 + ", dy: " + d1, player.getLocation());
                        } else if (b0 == 1 && this.method_114(player, d8, 2.0D - d2, d8)) {
                            if (!class_71.O1337(player) && (d1 >= 0.25D || d1 <= -0.25D)) {
                                this.method_110(player, "t: ypos(full-block), c: " + b0 + ", b: " + s1 + ", dy: " + d1, player.getLocation());
                            }

                            if (d1 != 0.0D && !class_7.method_31(player)) {
                                class_36.method_135(player, this.field_29.toString() + "=repeated=dy=" + d1, 5);
                                if (!class_71.O1337(player) && class_36.method_133(player, this.field_29.toString() + "=repeated=dy=" + d1) >= 4) {
                                    this.method_110(player, "type: ypos(repeated), case: " + b0 + ", block(s): " + s1 + ", dy: " + d1, player.getLocation());
                                } else if (this.field_30.containsKey(player)) {
                                    if (class_71.O1337(player)) {
                                        class_36.method_138(player, this.field_29.toString() + "=repeated=dy=" + d1);
                                    }

                                    double d9 = class_73.method_418(d1, 4);
                                    double d10 = class_73.method_418(((Double) this.field_30.get(player)).doubleValue(), 4);

                                    if (d10 == 0.2D && d9 != -0.0784D) {
                                        this.method_110(player, "t: ypos(illegal), c: 1, b: " + s1 + ", cr: " + d9 + ", pr: " + d10, player.getLocation());
                                    } else if (d10 == -0.0784D && d9 != -0.1216D) {
                                        this.method_110(player, "t: ypos(illegal), c: 2, b: " + s1 + ", cr: " + d9 + ", pr: " + d10, player.getLocation());
                                    } else if (d10 == -0.1216D && d9 != 0.2D) {
                                        this.method_110(player, "t: ypos(illegal), c: 3, b: " + s1 + ", cr: " + d9 + ", pr: " + d10, player.getLocation());
                                    } else if (d10 == d9) {
                                        this.method_110(player, "t: ypos(illegal), c: 4, b: " + s1 + ", cr: " + d9 + ", pr: " + d10, player.getLocation());
                                    }
                                }
                            }
                        }
                    }

                    b0 = 0;
                    if (this.method_115(player.getLocation()) && this.method_115(player.getLocation().add(0.0D, 1.0D, 0.0D))) {
                        b0 = 1;
                    } else if (this.method_115(player.getLocation())) {
                        b0 = 2;
                    } else if (this.method_115(player.getLocation().add(0.0D, 1.0D, 0.0D))) {
                        b0 = 3;
                    }

                    if (b0 != 0 && (location.getBlockX() != location1.getBlockX() || location.getBlockZ() != location1.getBlockZ())) {
                        boolean flag1 = class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251;
                        double d11 = !class_66.method_350(player.getLocation(), Material.SOIL, 0.3D) && (!flag1 || !class_66.method_350(player.getLocation(), Material.GRASS_PATH, 0.3D)) ? 0.0D : 0.1D;

                        if (b0 == 1) {
                            s1 = "(" + class_66.method_343(block) + ", " + class_66.method_343(block1) + ")";
                        } else if (b0 == 2) {
                            s1 = class_66.method_343(block);
                        } else if (b0 == 3) {
                            s1 = class_66.method_343(block1);
                        }

                        Location location2 = player.getLocation().add(0.0D, d11, 0.0D);

                        location2.setPitch(0.0F);
                        Location location3 = location2.clone().add(location2.clone().getDirection().multiply(1.0D));
                        Location location4 = location2.clone().add(location2.clone().getDirection().multiply(-1.0D));

                        if (this.method_115(location2) && (this.method_115(location3) || this.method_115(location4))) {
                            d3 = 0.25D + (double) f;
                            if (class_66.method_338(block.getLocation()) || class_66.method_338(block1.getLocation())) {
                                d3 += 0.35D;
                            }

                            d4 = class_69.method_385(player, d3, 4.0D, PotionEffectType.SPEED);
                            if (d0 >= d4) {
                                this.method_110(player, "type: speed(block-change), block(s): " + s1 + ", ds: " + d0 + ", dm: " + d3 + ", dm_s: " + d4, playermoveevent.getFrom());
                            } else if (!this.method_113(player, 1.0D, 0.0D, 1.0D)) {
                                if (class_39.method_169(player, this.field_29.toString() + "=block-change")) {
                                    class_39.method_170(player, this.field_29.toString() + "=block-change", 5);
                                    class_49.method_251(player, this.field_29, "t: speed(block-change), b: " + s1 + ", ds: " + d0, false);
                                }

                                if (!class_123.method_587(this.field_29)) {
                                    class_69.method_386(player, location1);
                                }
                            }
                        }
                    }
                }

                this.field_30.put(player, Double.valueOf(d1));
            }
        }
    }

    private void Ξ(Player player, String s, Location location) {
        class_38.method_159(player, this.field_29, s);
        if (class_123.method_583(player, this.field_29)) {
            class_69.method_386(player, location);
        }

    }

    private boolean Ξ(Player player, double d0) {
        if (class_69.method_374(player, d0)) {
            class_39.method_170(player, this.field_29.toString() + "=liquid", 20);
            return true;
        } else {
            return !class_39.method_169(player, this.field_29.toString() + "=liquid");
        }
    }

    private boolean Ξ(Player player) {
        return class_55.method_299(player, this.field_29, class_120.method_572("Clip.check_when_flying")) && !class_71.method_400(player) && !class_71.BPPΠ(player);
    }

    private boolean Ξ(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : !this.method_115(entity.getLocation().add(d0, d1, 0.0D)) || !this.method_115(entity.getLocation().add(-d0, d1, 0.0D)) || !this.method_115(entity.getLocation().add(0.0D, d1, d2)) || !this.method_115(entity.getLocation().add(0.0D, d1, -d2)) || !this.method_115(entity.getLocation().add(d0, d1, d2)) || !this.method_115(entity.getLocation().add(-d0, d1, -d2)) || !this.method_115(entity.getLocation().add(d0, d1, -d2)) || !this.method_115(entity.getLocation().add(-d0, d1, d2));
    }

    private boolean Π(Entity entity, double d0, double d1, double d2) {
        return class_66.method_348(entity, d0, d1, d2) && !class_66.HHΞ(entity, d0, d1, d2);
    }

    private boolean Ξ(Location location) {
        return location == null ? false : class_66.method_340(location, true) || location.getBlock().getType() == Material.PISTON_EXTENSION || location.getBlock().getType() == Material.PISTON_MOVING_PIECE;
    }
}
