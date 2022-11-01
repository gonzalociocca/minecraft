import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: bc
public class class_69 {

    public static HashMap<Player, Location> Ξ;
    public static HashMap<Player, Location> Π;
    public static HashMap<Player, Double> HHΞ;
    public static HashMap<Player, Double> HΞ;
    public static HashMap<Player, Double> BΞ;
    public static final double Ξ = 0.0784000015258D;
    public static final double Π = 0.08307781780646728D;

    public static void Ξ(Player player) {
        if (class_55.method_297(player)) {
            Location location = player.getLocation();
            double d0 = method_391(location, method_383(player));

            class_69.field_137.put(player, Double.valueOf(d0));
            if (class_69.field_135.containsKey(player)) {
                class_69.field_136.put(player, class_69.field_135.get(player));
            }

            class_69.field_135.put(player, location);
        } else {
            method_369(player);
        }

    }

    public static void Π(Player player) {
        if (player != null) {
            class_69.field_135.remove(player);
            class_69.field_136.remove(player);
            class_69.field_137.remove(player);
            class_69.HHΞ.remove(player);
            class_69.field_138.remove(player);
        }
    }

    public static double Ξ(Player player, Location location, double d0) {
        if (player != null && method_376(d0)) {
            double d1 = location.getY() - (double) location.getBlockY();
            double d2 = class_69.field_138.containsKey(player) && class_71.method_410(player) <= 0 ? ((Double) class_69.field_138.get(player)).doubleValue() : 0.0D;
            double d3;

            if (d1 + d0 >= 1.0D) {
                d3 = d1 + d0 - 1.0D;
            } else {
                d3 = d1 + d0;
            }

            if (d3 - d2 < 0.0D) {
                d3 = d3 - d2 + 1.0D;
            } else {
                d3 -= d2;
            }

            return d3;
        } else {
            return 0.0D;
        }
    }

    public static double Ξ(Player player, double d0) {
        if (player == null) {
            return 0.0D;
        } else {
            double d1 = !class_69.field_138.containsKey(player) ? 0.0D : ((Double) class_69.field_138.get(player)).doubleValue();

            if (d0 - d1 < 0.0D) {
                d0 = d0 - d1 + 1.0D;
            } else {
                d0 -= d1;
            }

            return d0;
        }
    }

    public static double Ξ(Player player) {
        return player == null ? 0.0D : 0.0784000015258D - (double) class_71.method_410(player) * 7.58E-4D;
    }

    public static double Π(Player player) {
        return player != null && class_69.field_137.containsKey(player) ? ((Double) class_69.field_137.get(player)).doubleValue() : 0.0D;
    }

    public static double HHΞ(Player player) {
        return player != null && class_69.HHΞ.containsKey(player) && method_373(player) != 0.0D ? ((Double) class_69.HHΞ.get(player)).doubleValue() : 0.0D;
    }

    public static boolean Ξ(Player player, double d0) {
        if (player == null) {
            return false;
        } else {
            for (int i = 0; i <= 2; ++i) {
                if (!class_66.method_346(player, 0.3D, (double) i, 0.3D)) {
                    return true;
                }
            }

            return d0 <= -1.0D && player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().isLiquid();
        }
    }

    public static boolean Ξ(Player player, double d0, double d1, double d2) {
        if (player != null && d0 >= d2 && d1 >= 0.0D && (d1 != 0.0D || class_71.method_411(player) >= 10)) {
            for (int i = 0; i <= 2; ++i) {
                if (!class_66.method_346(player, 1.0D, (double) i, 1.0D)) {
                    return false;
                }
            }

            Location location = player.getLocation();

            return !class_10.method_40(player) && !class_14.method_58(player) && !class_71.HHΞ(player) && !class_71.BPPΠ(player) && !class_15.method_61(player) && !class_8.method_32(player) && class_71.method_396(player) == 0.0F && !class_66.method_332(player.getLocation().add(0.0D, -1.0D, 0.0D)) && !class_71.BPΞ(player) && !class_18.method_69(player) && class_71.method_410(player) <= 12 && !class_16.method_64(player) && (class_71.method_416(location) || class_71.method_416(location.clone().add(0.0D, -1.0D, 0.0D)) || class_71.method_416(location.clone().add(0.0D, -1.5D, 0.0D)));
        } else {
            return false;
        }
    }

    public static boolean Ξ(double d0) {
        double d1 = method_381(d0, 3);

        return d1 == 0.419D || d1 == 0.333D || d1 == 0.164D || d1 == 0.248D || d1 == 0.083D;
    }

    public static boolean Ξ(Player player, double d0, double d1) {
        double d2 = method_381(d1, 3);
        double d3 = method_371(player, d1);
        double d4 = method_381(d3, 3);

        return method_376(d0) && (d0 == d1 || d0 == d3 || method_378(d2) || method_378(d4) || HHΞ(d2) || HHΞ(d4));
    }

    public static boolean Π(double d0) {
        return d0 == 0.876D || d0 == 0.501D || d0 == 0.666D || d0 == 0.749D || d0 == 0.753D || d0 == 0.919D || d0 == 0.585D || d0 == 0.747D || d0 == 0.995D || d0 == 0.001D;
    }

    public static boolean Π(Player player, double d0) {
        return HHΞ(d0) || HHΞ(method_371(player, d0));
    }

    public static boolean HHΞ(double d0) {
        return Math.abs(0.419D - d0) <= 0.02D || Math.abs(0.333D - d0) <= 0.02D || Math.abs(0.248D - d0) <= 0.02D || Math.abs(0.164D - d0) <= 0.02D || Math.abs(0.083D - d0) <= 0.02D;
    }

    public static boolean HΞ(double d0) {
        double d1 = class_73.method_418(d0, 5);

        return d1 == 0.1176D || d1 == 0.11215D || d1 == 0.15444D || d1 == 0.03684D || d1 == 0.07531D;
    }

    public static double Ξ(double d0, int i) {
        return class_73.method_418(d0 - Math.floor(d0), i);
    }

    public static boolean Ξ(Location location) {
        if (location == null) {
            return false;
        } else {
            Double odouble = Double.valueOf(Math.abs(location.getX()));
            Double odouble1 = Double.valueOf(Math.abs(location.getY()));
            Double odouble2 = Double.valueOf(Math.abs(location.getZ()));

            return class_89.method_487(odouble.doubleValue()) || class_89.method_487(odouble1.doubleValue()) || class_89.method_487(odouble2.doubleValue()) || odouble.doubleValue() == Double.MAX_VALUE || odouble1.doubleValue() == Double.MAX_VALUE || odouble2.doubleValue() == Double.MAX_VALUE || odouble.intValue() == Integer.MAX_VALUE || odouble1.intValue() == Integer.MAX_VALUE || odouble2.intValue() == Integer.MAX_VALUE;
        }
    }

    public static Location Ξ(Player player) {
        return player != null && class_69.field_135.containsKey(player) ? (Location) class_69.field_135.get(player) : null;
    }

    public static Location Π(Player player) {
        return player != null && class_69.field_136.containsKey(player) ? (Location) class_69.field_136.get(player) : null;
    }

    public static double Ξ(LivingEntity livingentity, double d0, double d1, PotionEffectType potioneffecttype) {
        if (livingentity != null && potioneffecttype != null) {
            boolean flag = potioneffecttype == PotionEffectType.SPEED && livingentity instanceof Player && class_71.HHΞ((Player) livingentity);

            if (flag || livingentity.hasPotionEffect(potioneffecttype)) {
                int i = class_71.method_413(livingentity, potioneffecttype);

                if (flag & !livingentity.hasPotionEffect(PotionEffectType.SPEED)) {
                    i = 255;
                }

                d0 += ((double) i + 1.0D) * (d0 / d1);
            }

            return d0;
        } else {
            return d0;
        }
    }

    public static void Ξ(Player player, Location location) {
        if (player != null && location != null && !class_71.PPPΠ(player) && !player.isSleeping() && !method_382(location)) {
            player.leaveVehicle();
            if (player.getWorld() == location.getWorld()) {
                player.teleport(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            } else {
                player.teleport(player);
            }

        }
    }

    public static void HHΞ(Player player) {
        if (player != null && player.getLocation().getBlockY() > 0 && !class_71.method_416(player.getLocation()) && (!class_66.BBBΠ(player.getLocation().add(0.0D, 1.0D, 0.0D)) || class_81.method_451(player.getLocation()))) {
            double d0 = player.getLocation().getY();
            double d1 = d0 - (double) player.getLocation().getBlockY();
            double d2 = 0.0D;

            for (double d3 = 0.0D; d3 <= d0; ++d3) {
                Location location = player.getLocation();

                location.setY(d0 - d3);
                if (!class_66.method_341(location) && !class_81.method_451(location) && class_71.method_416(location) || player.getLocation().getBlockY() <= 0) {
                    int i = class_71.method_410(player);
                    int j = class_36.method_133(player, "cached_air_ticks");
                    int k = i > j ? i : j;

                    class_36.method_138(player, "cached_air_ticks");
                    if (class_128.method_610("fall_damage_on_teleport") && (k >= 18 && !class_11.method_45(player) || d2 >= 4.0D)) {
                        double d4 = Double.valueOf((double) k).doubleValue() / 3.0D;

                        class_22.method_84(player, 10);
                        player.damage(d4 > d2 ? d4 : d2);
                        player.setFallDistance(0.0F);
                    }

                    return;
                }

                ++d2;
                method_386(player, location.add(0.0D, -d1, 0.0D));
            }

        }
    }

    public static boolean Ξ(Player player) {
        return player == null ? false : class_71.method_416(player.getLocation()) != player.isOnGround();
    }

    public static boolean Ξ(Location location, Location location1) {
        return method_391(location, location1) > 0.0D;
    }

    public static boolean Ξ(PlayerMoveEvent playermoveevent) {
        return playermoveevent == null ? false : !playermoveevent.isCancelled() && method_388(playermoveevent.getTo(), playermoveevent.getFrom());
    }

    public static boolean Π(PlayerMoveEvent playermoveevent) {
        if (playermoveevent == null) {
            return false;
        } else {
            Location location = playermoveevent.getTo();
            Location location1 = playermoveevent.getFrom();

            return !playermoveevent.isCancelled() && location.getWorld() == location1.getWorld() && (location.getPitch() != location1.getPitch() || location.getYaw() != location1.getYaw());
        }
    }

    public static double Ξ(Location location, Location location1) {
        return location != null && location1 != null && location.getWorld() == location1.getWorld() ? location.distance(location1) : 0.0D;
    }

    public static double Ξ(Player player, Location location) {
        if (player != null && location != null && location.getWorld() == player.getWorld()) {
            Location location1 = method_383(player);
            Location location2 = player.getLocation();
            double d0 = 0.0D;

            if (location1 != null && location1.getWorld() == location2.getWorld()) {
                double d1 = location2.distance(location);
                double d2 = location2.distance(location1);

                d0 = Math.abs(d1 - d2);
            }

            return d0;
        } else {
            return 0.0D;
        }
    }

    public static double Π(Player player, Location location) {
        if (player != null && location != null && location.getWorld() == player.getWorld()) {
            Location location1 = method_383(player);
            Location location2 = player.getLocation();
            double d0 = 0.0D;

            if (location1 != null && location1.getWorld() == location2.getWorld()) {
                double d1 = location2.getY();
                double d2 = d1 - location.getY();
                double d3 = d1 - location1.getY();

                d0 = Math.abs(d2 - d3);
            }

            return d0;
        } else {
            return 0.0D;
        }
    }

    public static double HHΞ(Player player, Location location) {
        if (player != null && location != null && location.getWorld() == player.getWorld()) {
            Location location1 = method_383(player);
            Location location2 = player.getLocation();
            double d0 = 0.0D;

            if (location1 != null && location1.getWorld() == location2.getWorld()) {
                double d1 = class_89.method_475(location2, location);
                double d2 = class_89.method_475(location2, location1);

                d0 = Math.abs(d1 - d2);
            }

            return d0;
        } else {
            return 0.0D;
        }
    }

    public static boolean Π(Player player) {
        if (player == null) {
            return false;
        } else {
            if (player.isSprinting() || class_71.method_408(player) || class_71.method_407(player) || class_71.HHΠ(player) || class_71.BBBΠ(player)) {
                Iterator iterator = player.getNearbyEntities(6.0D, 6.0D, 6.0D).iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    if (entity != null && entity instanceof Player) {
                        double d0 = class_89.method_475(player.getLocation(), entity.getLocation());

                        if (d0 >= 3.6D) {
                            Player player1 = (Player) entity;
                            Location location = player1.getLocation();

                            if (player1.isSprinting() || class_71.method_408(player1) || class_71.method_407(player1) || class_71.HHΠ(player1) || class_71.BBBΠ(player1) || class_71.method_397(player1) || HHΞ(player1) >= 0.16D) {
                                Location location1 = player.getLocation();

                                location1.setPitch(0.0F);
                                location1 = location1.add(location1.getDirection().multiply(d0));
                                double d1 = class_89.method_475(location1, location);

                                return d1 <= 1.25D;
                            }
                        }
                    }
                }
            }

            return false;
        }
    }

    public static boolean HHΞ(Player player) {
        return !class_39.method_169(player, "chasing");
    }

    static {
        class_69.field_135 = new HashMap();
        class_69.field_136 = new HashMap();
        class_69.HHΞ = new HashMap();
        class_69.field_137 = new HashMap();
        class_69.field_138 = new HashMap();
    }
}
