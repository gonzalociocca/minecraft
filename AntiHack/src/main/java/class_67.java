import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

// $FF: renamed from: bb
public class class_67 {

    public static boolean Ξ(Entity entity) {
        return (entity instanceof Monster || entity instanceof Animals || entity instanceof Villager || entity instanceof Player) && !(entity instanceof EnderDragon) && !(entity instanceof Giant) && !(entity instanceof Wither) && (!(entity instanceof Slime) || ((Slime) entity).getSize() != 4) && (!(entity instanceof MagmaCube) || ((MagmaCube) entity).getSize() != 4);
    }

    public static boolean Ξ(Entity entity, double d0) {
        if (entity != null && entity instanceof LivingEntity) {
            double d1 = ((Damageable) entity).getHealth();

            return d1 - d0 <= 0.0D;
        } else {
            return false;
        }
    }

    private static float[] Ξ(Player player, LivingEntity livingentity) {
        if (player != null && livingentity != null && method_353(livingentity)) {
            double d0 = livingentity.getLocation().getX() - player.getLocation().getX();
            double d1 = livingentity.getLocation().getY() + livingentity.getEyeHeight() * 0.9D - (player.getLocation().getY() + player.getEyeHeight());
            double d2 = livingentity.getLocation().getZ() - player.getLocation().getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
            float f1 = (float) (-(Math.atan2(d1, d3) * 180.0D / 3.141592653589793D));

            return new float[] { player.getLocation().getYaw() + class_89.method_477(f - player.getLocation().getYaw()), player.getLocation().getPitch() + class_89.method_477(f1 - player.getLocation().getPitch())};
        } else {
            return null;
        }
    }

    public static double Ξ(Player player, Entity entity) {
        if (player != null && entity != null && method_353(entity)) {
            float[] afloat = method_355(player, (LivingEntity) entity);

            if (afloat == null) {
                return 0.0D;
            } else {
                float f = afloat[0] - player.getLocation().getYaw();
                float f1 = afloat[1] - player.getLocation().getPitch();
                double d0 = Math.sqrt((double) (f * f + f1 * f1));

                return d0;
            }
        } else {
            return 0.0D;
        }
    }

    public static double Π(Player player, Entity entity) {
        if (player != null && entity != null && method_353(entity)) {
            Location location = player.getLocation();
            Location location1 = entity.getLocation();
            double d0 = class_89.method_475(location, location1);
            Location location2 = location.add(location.getDirection().multiply(d0));

            return class_89.method_475(location2, location1);
        } else {
            return 0.0D;
        }
    }

    public static boolean Ξ(Player player, Entity entity) {
        if (player != null && entity != null && method_353(entity)) {
            Location location = player.getLocation();
            Location location1 = entity.getLocation();

            location.setPitch(0.0F);
            double d0 = class_69.method_391(location, location1);
            double d1 = ((LivingEntity) entity).getEyeHeight();
            Location location2 = location.clone().add(location.getDirection().multiply(d0));
            Location location3 = location.clone().add(location.getDirection().multiply(d0 + 1.0D));
            Location location4 = location2.clone().add(0.0D, 1.0D, 0.0D);
            Location location5 = location3.clone().add(0.0D, 1.0D, 0.0D);

            return class_66.BBBΠ(location2) || class_66.BBBΠ(location3) || d1 > 1.0D && (class_66.BBBΠ(location4) || class_66.BBBΠ(location5));
        } else {
            return false;
        }
    }

    public static double HHΞ(Player player, Entity entity) {
        if (player != null && entity != null && method_353(entity)) {
            Location location = player.getLocation();
            Location location1 = entity.getLocation();

            location.setY(1.0D);
            location1.setY(1.0D);
            Vector vector = location1.toVector().subtract(location.toVector());
            Location location2 = location.clone();

            location2.setPitch(0.0F);
            Vector vector1 = location2.getDirection();

            return vector.normalize().dot(vector1);
        } else {
            return 0.0D;
        }
    }

    public static double HΞ(Player player, Entity entity) {
        if (player != null && entity != null && method_353(entity)) {
            Location location = player.getLocation();

            location.setY(0.0D);
            location.setPitch(0.0F);
            Location location1 = entity.getLocation();

            location1.setPitch(0.0F);
            location1.setY(0.0D);
            if (location.distance(location1) < 1.0D) {
                return 0.0D;
            } else {
                Vector vector = location.subtract(location1).toVector();

                vector = vector.normalize();
                Vector vector1 = location.getDirection().normalize();

                vector1.setX(vector1.getX() * -1.0D);
                vector1.setY(vector1.getY() * -1.0D);
                vector1.setZ(vector1.getZ() * -1.0D);
                return (double) vector.angle(vector1);
            }
        } else {
            return 0.0D;
        }
    }

    public static Location Ξ(Player player, Entity entity) {
        if (player != null && entity != null && method_353(entity)) {
            Location location = player.getLocation();
            Location location1 = entity.getLocation();
            double d0 = location.getX() - location1.getX();
            double d1 = location.getY() - location1.getY();
            double d2 = location.getZ() - location1.getZ();
            double d3 = location.distance(location1);
            Vector vector = location.getDirection().normalize().multiply(d3);
            Vector vector1 = new Vector(d0, d1, d2);

            vector = vector1.subtract(vector);
            return new Location(player.getWorld(), vector.getX(), vector.getY(), vector.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        } else {
            return null;
        }
    }

    public static double BΞ(Player player, Entity entity) {
        if (player != null && entity != null && method_353(entity)) {
            Location location = entity.getLocation().add(0.0D, ((LivingEntity) entity).getEyeHeight(), 0.0D);
            Location location1 = player.getLocation().add(0.0D, player.getEyeHeight(), 0.0D);
            Vector vector = new Vector(location1.getYaw(), location1.getPitch(), 0.0F);
            Vector vector1 = method_362(location1, location);
            double d0 = class_89.method_478(vector.getX() - vector1.getX());
            double d1 = class_89.method_478(vector.getY() - vector1.getY());
            double d2 = class_89.method_475(location1, location);
            double d3 = location1.distance(location);
            double d4 = d0 * d2 * d3;
            double d5 = d1 * Math.abs(location.getY() - location1.getY()) * d3;

            return Math.abs(d4) + Math.abs(d5);
        } else {
            return 0.0D;
        }
    }

    public static double BPPΠ(Player player, Entity entity) {
        if (player != null && entity != null && method_353(entity)) {
            Location location = entity.getLocation().add(0.0D, ((LivingEntity) entity).getEyeHeight(), 0.0D);
            Location location1 = player.getLocation().add(0.0D, player.getEyeHeight(), 0.0D);
            Vector vector = new Vector(location1.getYaw(), location1.getPitch(), 0.0F);
            Vector vector1 = method_362(location1, location);

            return class_89.method_478(vector.distance(vector1));
        } else {
            return 0.0D;
        }
    }

    private static Vector Ξ(Location location, Location location1) {
        if (location != null && location1 != null) {
            double d0 = location1.getX() - location.getX();
            double d1 = location1.getY() - location.getY();
            double d2 = location1.getZ() - location.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
            float f1 = (float) (-(Math.atan2(d1, d3) * 180.0D / 3.141592653589793D));

            return new Vector(f, f1, 0.0F);
        } else {
            return null;
        }
    }

    public static boolean Ξ(Player player) {
        if (player == null) {
            return false;
        } else {
            if (class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_251) {
                ItemStack itemstack = player.getItemInHand();

                if (itemstack.getType() == Material.DIAMOND_SWORD || itemstack.getType() == Material.GOLD_SWORD || itemstack.getType() == Material.IRON_SWORD || itemstack.getType() == Material.STONE_SWORD || itemstack.getType() == Material.WOOD_SWORD) {
                    return class_71.method_414(player, 6.0D) > 1;
                }
            }

            return false;
        }
    }

    public static boolean Π(Player player) {
        return player == null ? false : (class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_251 ? class_71.method_415(player, Material.SHIELD) && player.isBlocking() : false);
    }
}
