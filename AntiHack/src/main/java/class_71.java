import java.util.Collection;
import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: bd
public class class_71 implements Listener {

    public static float Ξ(Player player) {
        if (player == null) {
            return 0.0F;
        } else {
            float f = player.getWalkSpeed() - 0.2F;

            if (f > 0.0F) {
                class_39.method_170(player, "walk_difference", 40);
            } else if (!class_39.method_169(player, "walk_difference")) {
                return 1.0F;
            }

            return f < 0.0F ? 0.0F : f;
        }
    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "combat");
    }

    public static boolean Π(Player player) {
        return !class_39.method_169(player, "hit_entity");
    }

    public static void Ξ(Player player, int i) {
        class_39.method_170(player, "teleport_cooldown", i);
    }

    public static boolean HHΞ(Player player) {
        if (player == null) {
            return false;
        } else if (!class_39.method_169(player, "speed_effect")) {
            return true;
        } else {
            boolean flag = player.hasPotionEffect(PotionEffectType.SPEED);

            if (flag) {
                class_39.method_170(player, "speed_effect", 60);
            }

            return flag;
        }
    }

    public static boolean HΞ(Player player) {
        if (player == null) {
            return false;
        } else if (!class_39.method_169(player, "vehicle=has")) {
            return true;
        } else {
            boolean flag = player.getVehicle() != null;

            if (flag) {
                class_39.method_170(player, "vehicle=has", 80);
                class_39.method_170(player, "vehicle=had", 160);
            }

            return flag;
        }
    }

    public static boolean BΞ(Player player) {
        return !class_39.method_169(player, "vehicle=had");
    }

    public static void Ξ(Player player) {
        class_39.method_171(player, "vehicle=has");
        class_39.method_171(player, "vehicle=had");
    }

    public static boolean BPPΠ(Player player) {
        return method_403(player) || method_404(player);
    }

    public static boolean PΞ(Player player) {
        if (player == null) {
            return false;
        } else if (!class_39.method_169(player, "low_jump_effect")) {
            return true;
        } else {
            boolean flag = player.hasPotionEffect(PotionEffectType.JUMP) && (method_413(player, PotionEffectType.JUMP) < 128 || method_413(player, PotionEffectType.JUMP) > 250);

            if (flag) {
                class_39.method_170(player, "low_jump_effect", 100);
            }

            return flag;
        }
    }

    public static boolean OΠ(Player player) {
        return method_413(player, PotionEffectType.JUMP) >= 128 && method_413(player, PotionEffectType.JUMP) <= 250;
    }

    public static boolean O1337(Player player) {
        if (player == null) {
            return false;
        } else if (!class_39.method_169(player, "levitation_effect")) {
            return true;
        } else {
            boolean flag = class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_251 && player.hasPotionEffect(PotionEffectType.LEVITATION);

            if (flag) {
                class_39.method_170(player, "levitation_effect", 60);
            }

            return flag;
        }
    }

    public static boolean PPPΞ(Player player) {
        if (player == null) {
            return false;
        } else if (!class_39.method_169(player, "nearby_entities")) {
            return true;
        } else {
            boolean flag = false;
            Iterator iterator = player.getNearbyEntities(3.0D, 3.0D, 3.0D).iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (entity instanceof Boat || entity instanceof FallingBlock) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                class_39.method_170(player, "nearby_entities", 40);
            }

            return flag;
        }
    }

    public static boolean PΠ(Player player) {
        return player == null ? false : player.hasPotionEffect(PotionEffectType.POISON) || player.hasPotionEffect(PotionEffectType.HARM);
    }

    public static boolean HΠ(Player player) {
        return class_85.method_467(player) >= 0.07F;
    }

    public static boolean PPΞ(Player player) {
        return class_36.method_133(player, "inventory_use") >= 8 && player.getOpenInventory().countSlots() > 46;
    }

    public static boolean OOOΠ(Player player) {
        return player == null ? false : player.getOpenInventory().getCursor().getType() != Material.AIR;
    }

    public static boolean BΠ(Player player) {
        return !class_39.method_169(player, "walking");
    }

    public static boolean OΞ(Player player) {
        return !class_39.method_169(player, "sprint");
    }

    public static boolean HHΠ(Player player) {
        return !class_39.method_169(player, "sprint-jumping");
    }

    public static boolean BBBΠ(Player player) {
        return !class_39.method_169(player, "walk-jumping");
    }

    public static boolean BBΞ(Player player) {
        if (player == null) {
            return false;
        } else {
            int i = player.getItemInHand().getTypeId();

            return i == 260 || i == 282 || i == 297 || i == 319 || i == 320 || i == 322 || i == 349 || i == 350 || i == 357 || i == 360 || i >= 363 && i <= 367 || i == 375 || i >= 391 && i <= 394 || i == 400 || i == 423 || i >= 411 && i <= 413;
        }
    }

    public static boolean Ξ(Player player, int i, double d0) {
        return player == null ? false : class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251 && method_414(player, d0) >= i;
    }

    public static boolean OOΞ(Player player) {
        return class_77.method_435(player);
    }

    public static boolean HHHΞ(Player player) {
        if (player == null) {
            return false;
        } else {
            boolean flag = player.getVehicle() == null && (player.isFlying() || player.getGameMode() == GameMode.CREATIVE && player.getAllowFlight() || class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && player.getGameMode() == GameMode.SPECTATOR);

            if (flag) {
                class_39.method_170(player, "flying", 60);
            } else if (!class_39.method_169(player, "flying")) {
                flag = true;
            }

            return flag;
        }
    }

    public static int Ξ(Player player) {
        return PPPΠ(player) ? 0 : class_36.method_133(player, "air_ticks");
    }

    public static int Π(Player player) {
        return PPPΠ(player) ? 0 : class_36.method_133(player, "ground_ticks");
    }

    public static void Π(Player player) {
        class_36.method_138(player, "air_ticks");
    }

    public static void HHΞ(Player player) {
        if (class_55.method_297(player)) {
            boolean flag = method_416(player.getLocation()) || !class_39.method_169(player, "ground");
            int i;

            if (!flag && !player.getLocation().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid() && player.getVehicle() == null) {
                i = class_36.method_133(player, "air_ticks") + 1;
                class_36.method_134(player, "air_ticks", i);
                class_36.method_138(player, "ground_ticks");
            } else if (flag) {
                i = class_36.method_133(player, "ground_ticks") + 1;
                class_36.method_134(player, "ground_ticks", i);
                class_36.method_138(player, "air_ticks");
            }

            if (player.getOpenInventory().countSlots() > 46) {
                i = class_36.method_133(player, "inventory_use") + 1;
                class_36.method_134(player, "inventory_use", i);
            } else {
                class_36.method_138(player, "inventory_use");
            }
        } else {
            class_36.method_138(player, "air_ticks");
            class_36.method_138(player, "ground_ticks");
            class_36.method_138(player, "inventory_use");
        }

    }

    public static int Ξ(LivingEntity livingentity, PotionEffectType potioneffecttype) {
        if (livingentity != null && potioneffecttype != null) {
            if (livingentity.hasPotionEffect(potioneffecttype)) {
                Collection collection = livingentity.getActivePotionEffects();
                Iterator iterator = collection.iterator();

                while (iterator.hasNext()) {
                    PotionEffect potioneffect = (PotionEffect) iterator.next();

                    if (potioneffect.getType().equals(potioneffecttype)) {
                        return potioneffect.getAmplifier();
                    }
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public static boolean BPΞ(Player player) {
        return class_36.method_133(player, "elytra") == 1 || !class_39.method_169(player, "elytra");
    }

    public static boolean HHHΠ(Player player) {
        return BPΞ(player) && player.getItemInHand().getType() == Material.FIREWORK;
    }

    public static int Ξ(Player player, double d0) {
        int i = 0;

        if (player == null) {
            return 0;
        } else {
            Iterator iterator = player.getNearbyEntities(d0, d0, d0).iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (entity != null && entity instanceof LivingEntity) {
                    ++i;
                }
            }

            return i;
        }
    }

    public static boolean Ξ(Player player, Material material) {
        if (player != null && material != null) {
            ItemStack itemstack = player.getItemInHand();

            if (itemstack != null && itemstack.getType() != null && itemstack.getType() == material) {
                return true;
            } else {
                if (class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_251) {
                    ItemStack itemstack1 = player.getInventory().getItemInOffHand();

                    if (itemstack1 != null && itemstack1.getType() != null && itemstack1.getType() == material) {
                        return true;
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean PPPPΞ(Player player) {
        if (player == null) {
            return false;
        } else if (class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254) {
            WorldBorder worldborder = player.getWorld().getWorldBorder();
            Location location = player.getLocation();

            location.setY(1.0D);
            Location location1 = worldborder.getCenter();
            double d0 = class_89.method_482(location1.getX(), location.getX());
            double d1 = class_89.method_482(location1.getZ(), location.getZ());
            double d2 = location.distance(location1.clone().add(d0, 0.0D, d1));
            double d3 = location.distance(location1.clone().add(-d0, 0.0D, -d1));
            double d4 = location.distance(location1.clone().add(-d0, 0.0D, d1));
            double d5 = location.distance(location1.clone().add(d0, 0.0D, -d1));
            double d6 = worldborder.getSize();

            return d2 >= d6 && d3 > 1.0D || d3 >= d6 && d2 > 1.0D || d4 >= d6 && d5 > 1.0D || d5 >= d6 && d4 > 1.0D;
        } else {
            return false;
        }
    }

    public static boolean PPΠ(Player player) {
        if (player == null) {
            return false;
        } else if (class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254) {
            WorldBorder worldborder = player.getWorld().getWorldBorder();
            Location location = player.getLocation();

            location.setY(1.0D);
            Location location1 = worldborder.getCenter();
            double d0 = class_89.method_482(location1.getX(), location.getX());
            double d1 = class_89.method_482(location1.getZ(), location.getZ());
            double d2 = location.distance(location1.clone().add(d0, 0.0D, d1));
            double d3 = location.distance(location1.clone().add(-d0, 0.0D, -d1));
            double d4 = location.distance(location1.clone().add(-d0, 0.0D, d1));
            double d5 = location.distance(location1.clone().add(d0, 0.0D, -d1));
            double d6 = worldborder.getSize();

            return d2 >= d6 - 1.0D && d2 <= d6 + 1.0D && d3 > 1.0D || d3 >= d6 - 1.0D && d3 <= d6 + 1.0D && d2 > 1.0D || d4 >= d6 - 1.0D && d4 <= d6 + 1.0D && d5 > 1.0D || d5 >= d6 - 1.0D && d5 <= d6 + 1.0D && d4 > 1.0D;
        } else {
            return false;
        }
    }

    public static boolean PPPΠ(Player player) {
        return player == null ? false : player.isDead() || player.getHealth() <= 0.0D;
    }

    public static boolean Ξ(Location location) {
        return class_81.method_449(location);
    }
}
