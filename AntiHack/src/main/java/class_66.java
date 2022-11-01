import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

// $FF: renamed from: ba
public class class_66 {

    public static boolean Ξ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251 && material == Material.FROSTED_ICE ? true : material == Material.ICE || material == Material.PACKED_ICE;
        }
    }

    public static boolean Π(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return material == Material.CHEST || material == Material.TRAPPED_CHEST || material == Material.ENDER_CHEST;
        }
    }

    public static boolean HHΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return material == Material.GOLD_PLATE || material == Material.IRON_PLATE || material == Material.STONE_PLATE || material == Material.WOOD_PLATE;
        }
    }

    public static boolean HΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return material == Material.THIN_GLASS || material == Material.STAINED_GLASS_PANE;
        }
    }

    public static boolean BΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250) {
                if (material == Material.STONE_SLAB2) {
                    return true;
                }

                if (class_101.method_512() != class_101.field_251 && material == Material.PURPUR_SLAB) {
                    return true;
                }
            }

            return material == Material.STEP || material == Material.WOOD_STEP;
        }
    }

    public static boolean BPPΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && material == Material.IRON_TRAPDOOR ? true : material == Material.TRAP_DOOR;
        }
    }

    public static boolean PΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            int i = location.getBlock().getTypeId();

            return i == 53 || i == 67 || i == 108 || i == 109 || i == 114 || i == 128 || i >= 134 && i <= 136 || i == 156 || i == 163 || i == 164 || i == 180 || i == 203;
        }
    }

    public static boolean OΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            int i = location.getBlock().getTypeId();

            return i == 85 || i == 113 || i >= 188 && i <= 192;
        }
    }

    public static boolean O1337(Location location) {
        if (location == null) {
            return false;
        } else {
            int i = location.getBlock().getTypeId();

            return i == 107 || i >= 183 && i <= 187;
        }
    }

    public static boolean PPPΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && (material == Material.ACACIA_DOOR || material == Material.BIRCH_DOOR || material == Material.DARK_OAK_DOOR || material == Material.JUNGLE_DOOR || material == Material.SPRUCE_DOOR) ? true : material == Material.IRON_DOOR_BLOCK || material == Material.WOODEN_DOOR;
        }
    }

    public static boolean PΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return material == Material.SAND || material == Material.GRAVEL;
        }
    }

    public static boolean HΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            int i = location.getBlock().getTypeId();

            return i >= 219 && i <= 234;
        }
    }

    public static boolean PPΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250) {
                if (material == Material.SLIME_BLOCK || material == Material.RED_SANDSTONE || material == Material.RED_SANDSTONE_STAIRS) {
                    return true;
                }

                if (class_101.method_512() != class_101.field_251) {
                    if (class_101.method_512() != class_101.HHΞ && material == Material.MAGMA) {
                        return true;
                    }

                    if (material == Material.END_ROD || material == Material.BEETROOT_BLOCK) {
                        return true;
                    }
                }
            }

            return !BBBΠ(location) || method_334(location) || material == Material.BED_BLOCK || material == Material.CARPET || material == Material.LEAVES || material == Material.LEAVES_2 || material == Material.VINE || material == Material.LADDER || material == Material.HUGE_MUSHROOM_1 || material == Material.HUGE_MUSHROOM_2 || material == Material.TNT || material == Material.getMaterial(149) || material == Material.getMaterial(93) || material == Material.WATER_LILY || material == Material.FLOWER_POT || material == Material.COCOA || material == Material.NETHERRACK || material == Material.GLASS || material == Material.STAINED_GLASS || material == Material.ICE || material == Material.PACKED_ICE || material == Material.SANDSTONE || material == Material.SANDSTONE_STAIRS || material == Material.QUARTZ_BLOCK || material == Material.QUARTZ_STAIRS;
        }
    }

    public static boolean Ξ(Location location, boolean flag) {
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

            return (!flag || flag && BBBΠ(location)) && !O1337(location) && !method_337(location) && !method_336(location) && !method_335(location) && !BPPΠ(location) && !PPPΞ(location) && !method_334(location) && !method_333(location) && !HHΞ(location) && !method_339(location) && material != Material.COCOA && material != Material.DRAGON_EGG && material != Material.ENDER_PORTAL_FRAME && material != Material.ENCHANTMENT_TABLE && material != Material.BED_BLOCK && material != Material.HOPPER && material != Material.FLOWER_POT && material != Material.BREWING_STAND && material != Material.ANVIL && material != Material.CAULDRON && material != Material.CARPET && material != Material.getMaterial(101) && material != Material.COBBLE_WALL && material != Material.LADDER && material != Material.WEB && material != Material.VINE && material != Material.WATER_LILY && material != Material.REDSTONE_COMPARATOR_OFF && material != Material.REDSTONE_COMPARATOR_ON && material != Material.DIODE_BLOCK_ON && material != Material.DIODE_BLOCK_OFF && material != Material.SKULL && material != Material.SNOW && material != Material.PISTON_BASE && material != Material.PISTON_STICKY_BASE && material != Material.PISTON_EXTENSION && material != Material.CACTUS && material != Material.SOIL && material != Material.SOUL_SAND && material != Material.getMaterial(151) && material != Material.getMaterial(178) && material != Material.CAKE_BLOCK && material != Material.getMaterial(127) && material != Material.FIRE && material != Material.LEAVES && material != Material.LEAVES_2;
        }
    }

    public static boolean OOOΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return material == Material.WORKBENCH || material == Material.FURNACE || material == Material.BURNING_FURNACE || material == Material.ENCHANTMENT_TABLE || O1337(location) || material == Material.HOPPER || material == Material.BED_BLOCK || material == Material.JUKEBOX || material == Material.NOTE_BLOCK || material == Material.DISPENSER || material == Material.DROPPER || material == Material.DIODE_BLOCK_ON || material == Material.DIODE_BLOCK_OFF || material == Material.REDSTONE_COMPARATOR_OFF || material == Material.REDSTONE_COMPARATOR_ON || material == Material.LEVER || material == Material.TRIPWIRE_HOOK || material == Material.ENDER_PORTAL_FRAME || material == Material.ANVIL || material == Material.BREWING_STAND || material == Material.LEVER || material == Material.WOOD_BUTTON || BPPΠ(location) || method_333(location) || PPPΞ(location) || O1337(location) || method_339(location);
        }
    }

    public static boolean BΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return material == Material.LADDER || material == Material.VINE;
        }
    }

    public static boolean OΞ(Location location) {
        return location == null ? false : method_341(location) && (BBBΠ(location.clone().add(1.0D, 0.0D, 0.0D)) || BBBΠ(location.clone().add(-1.0D, 0.0D, 0.0D)) || BBBΠ(location.clone().add(0.0D, 0.0D, 1.0D)) || BBBΠ(location.clone().add(0.0D, 0.0D, -1.0D)) || BBBΠ(location.clone().add(1.0D, 0.0D, 1.0D)) || BBBΠ(location.clone().add(-1.0D, 0.0D, -1.0D)) || BBBΠ(location.clone().add(-1.0D, 0.0D, 1.0D)) || BBBΠ(location.clone().add(1.0D, 0.0D, -1.0D)));
    }

    public static String Ξ(Block block) {
        return block == null ? null : block.getType().toString().toLowerCase().replaceAll("_", "-");
    }

    public static String Ξ(Player player, Location location) {
        if (player != null && location != null && player.getWorld() == location.getWorld()) {
            Block block = location.getBlock();
            double d0 = player.getLocation().distance(location);

            for (double d1 = 0.0D; d1 <= d0; ++d1) {
                Location location1 = player.getLocation().add(0.0D, player.getEyeHeight(), 0.0D).add(player.getLocation().getDirection().multiply(d1));
                double d2 = player.getLocation().distance(location1);
                Block block1 = location1.getBlock();
                double d3 = player.getLocation().distance(block.getLocation()) - player.getLocation().distance(block1.getLocation());

                if (method_340(location1, true) && d2 <= d0 && d3 >= 0.4D && block1.getLocation().getY() >= player.getLocation().getY()) {
                    return "r-d: " + d0 + ", c-d: " + d2 + ", b-a: " + method_343(block1) + ", b-c: " + d3;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static Block Ξ(Player player, Location location) {
        if (player != null && location != null && player.getWorld() == location.getWorld() && (BBBΠ(location) || OOOΠ(location)) && !BBBΠ(player.getLocation().add(0.0D, 1.0D, 0.0D))) {
            Block block = location.getBlock();
            double d0 = player.getLocation().distance(location);
            Block block1 = player.getTargetBlock((HashSet) null, (int) d0);
            double d1 = block1.getLocation().distance(block.getLocation());
            double d2 = 1.0D;

            if (class_71.BPΞ(player)) {
                d2 = 2.5D;
            } else if (player.getItemInHand().getType() == Material.getMaterial(383)) {
                d2 = 2.0D;
            }

            return method_340(block1.getLocation(), true) && d1 >= d2 && (block1.getLocation().getBlockX() != block.getLocation().getBlockX() || block1.getLocation().getBlockY() != block.getLocation().getBlockY() || block1.getLocation().getBlockZ() != block.getLocation().getBlockZ()) ? block1 : null;
        } else {
            return null;
        }
    }

    public static boolean Ξ(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : !entity.getLocation().add(0.0D, d1, 0.0D).getBlock().isLiquid() && !entity.getLocation().add(d0, d1, 0.0D).getBlock().isLiquid() && !entity.getLocation().add(-d0, d1, 0.0D).getBlock().isLiquid() && !entity.getLocation().add(0.0D, d1, d2).getBlock().isLiquid() && !entity.getLocation().add(0.0D, d1, -d2).getBlock().isLiquid() && !entity.getLocation().add(d0, d1, d2).getBlock().isLiquid() && !entity.getLocation().add(-d0, d1, -d2).getBlock().isLiquid() && !entity.getLocation().add(d0, d1, -d2).getBlock().isLiquid() && !entity.getLocation().add(-d0, d1, d2).getBlock().isLiquid();
    }

    public static boolean Π(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : !class_71.method_416(entity.getLocation().add(0.0D, d1, 0.0D)) && !class_71.method_416(entity.getLocation().add(d0, d1, 0.0D)) && !class_71.method_416(entity.getLocation().add(-d0, d1, 0.0D)) && !class_71.method_416(entity.getLocation().add(0.0D, d1, d2)) && !class_71.method_416(entity.getLocation().add(0.0D, d1, -d2)) && !class_71.method_416(entity.getLocation().add(d0, d1, d2)) && !class_71.method_416(entity.getLocation().add(-d0, d1, -d2)) && !class_71.method_416(entity.getLocation().add(d0, d1, -d2)) && !class_71.method_416(entity.getLocation().add(-d0, d1, d2));
    }

    public static boolean HHΞ(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : !BBBΠ(entity.getLocation().add(0.0D, d1, 0.0D)) && !BBBΠ(entity.getLocation().add(d0, d1, 0.0D)) && !BBBΠ(entity.getLocation().add(-d0, d1, 0.0D)) && !BBBΠ(entity.getLocation().add(0.0D, d1, d2)) && !BBBΠ(entity.getLocation().add(0.0D, d1, -d2)) && !BBBΠ(entity.getLocation().add(d0, d1, d2)) && !BBBΠ(entity.getLocation().add(-d0, d1, -d2)) && !BBBΠ(entity.getLocation().add(d0, d1, -d2)) && !BBBΠ(entity.getLocation().add(-d0, d1, d2));
    }

    public static boolean HΞ(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : method_340(entity.getLocation().add(0.0D, d1, 0.0D), false) && method_340(entity.getLocation().add(d0, d1, 0.0D), false) && method_340(entity.getLocation().add(-d0, d1, 0.0D), false) && method_340(entity.getLocation().add(0.0D, d1, d2), false) && method_340(entity.getLocation().add(0.0D, d1, -d2), false) && method_340(entity.getLocation().add(d0, d1, d2), false) && method_340(entity.getLocation().add(-d0, d1, -d2), false) && method_340(entity.getLocation().add(d0, d1, -d2), false) && method_340(entity.getLocation().add(-d0, d1, d2), false);
    }

    public static boolean BΞ(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : method_341(entity.getLocation().add(0.0D, d1, 0.0D)) || method_341(entity.getLocation().add(d0, d1, 0.0D)) || method_341(entity.getLocation().add(-d0, d1, 0.0D)) || method_341(entity.getLocation().add(0.0D, d1, d2)) || method_341(entity.getLocation().add(0.0D, d1, -d2)) || method_341(entity.getLocation().add(d0, d1, d2)) || method_341(entity.getLocation().add(-d0, d1, -d2)) || method_341(entity.getLocation().add(d0, d1, -d2)) || method_341(entity.getLocation().add(-d0, d1, d2));
    }

    public static boolean BPPΠ(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : HHΠ(entity.getLocation().add(0.0D, d1, 0.0D)) || HHΠ(entity.getLocation().add(d0, d1, 0.0D)) || HHΠ(entity.getLocation().add(-d0, d1, 0.0D)) || HHΠ(entity.getLocation().add(0.0D, d1, d2)) || HHΠ(entity.getLocation().add(0.0D, d1, -d2)) || HHΠ(entity.getLocation().add(d0, d1, d2)) || HHΠ(entity.getLocation().add(-d0, d1, -d2)) || HHΠ(entity.getLocation().add(d0, d1, -d2)) || HHΠ(entity.getLocation().add(-d0, d1, d2));
    }

    public static boolean HHΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return class_81.method_451(location) || material == Material.SKULL || method_333(location) || material == Material.ANVIL || material == Material.CACTUS || material == Material.COCOA || material == Material.FLOWER_POT;
        }
    }

    public static boolean BBBΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250) {
                if (material == Material.ARMOR_STAND || material == Material.STANDING_BANNER || material == Material.WALL_BANNER) {
                    return false;
                }

                if (class_101.method_512() != class_101.field_251 && (material == Material.BEETROOT_SEEDS || material == Material.END_GATEWAY || class_101.method_512() != class_101.HHΞ && material == Material.STRUCTURE_VOID)) {
                    return false;
                }
            }

            return material != Material.AIR && material != Material.STONE_BUTTON && material != Material.WOOD_BUTTON && material != Material.RED_ROSE && material != Material.YELLOW_FLOWER && !O1337(location) && material != Material.SIGN_POST && material != Material.WALL_SIGN && material != Material.BROWN_MUSHROOM && material != Material.RED_MUSHROOM && material != Material.TORCH && material != Material.REDSTONE_TORCH_ON && material != Material.getMaterial(132) && material != Material.GOLD_PLATE && material != Material.IRON_PLATE && material != Material.STONE_PLATE && material != Material.WOOD_PLATE && material != Material.TRIPWIRE_HOOK && material != Material.REDSTONE_WIRE && material != Material.RAILS && material != Material.ACTIVATOR_RAIL && material != Material.DETECTOR_RAIL && material != Material.POWERED_RAIL && material != Material.SEEDS && material != Material.MELON_SEEDS && material != Material.PUMPKIN_SEEDS && material != Material.CROPS && material != Material.ENDER_PORTAL && material != Material.PORTAL && material != Material.PUMPKIN_STEM && material != Material.MELON_STEM && material != Material.CARROT && material != Material.FIRE && material != Material.getMaterial(115) && material != Material.POTATO && material != Material.LEVER && material != Material.getMaterial(175) && material != Material.getMaterial(141) && material != Material.getMaterial(142) && material != Material.getMaterial(115) && material != Material.getMaterial(31) && material != Material.getMaterial(32) && material != Material.getMaterial(6) && material != Material.getMaterial(83) && !location.getBlock().isLiquid() && material != Material.REDSTONE_TORCH_OFF && material != Material.PISTON_MOVING_PIECE;
        }
    }

    public static boolean Ξ(Location location, Material material, double d0) {
        return location != null && material != null && d0 >= 0.0D ? location.getBlock().getType() == material || location.clone().add(0.0D, 0.0D, -d0).getBlock().getType() == material || location.clone().add(0.0D, 0.0D, d0).getBlock().getType() == material || location.clone().add(-d0, 0.0D, 0.0D).getBlock().getType() == material || location.clone().add(d0, 0.0D, 0.0D).getBlock().getType() == material || location.clone().add(d0, 0.0D, d0).getBlock().getType() == material || location.clone().add(-d0, 0.0D, -d0).getBlock().getType() == material || location.clone().add(-d0, 0.0D, d0).getBlock().getType() == material || location.clone().add(d0, 0.0D, -d0).getBlock().getType() == material : false;
    }

    public static boolean Π(Location location, Material material, double d0) {
        return location != null && material != null && d0 >= 0.0D ? location.getBlock().getType() == material && location.clone().add(0.0D, 0.0D, -d0).getBlock().getType() == material && location.clone().add(0.0D, 0.0D, d0).getBlock().getType() == material && location.clone().add(-d0, 0.0D, 0.0D).getBlock().getType() == material && location.clone().add(d0, 0.0D, 0.0D).getBlock().getType() == material && location.clone().add(d0, 0.0D, d0).getBlock().getType() == material && location.clone().add(-d0, 0.0D, -d0).getBlock().getType() == material && location.clone().add(-d0, 0.0D, d0).getBlock().getType() == material && location.clone().add(d0, 0.0D, -d0).getBlock().getType() == material : false;
    }

    public static boolean Ξ(Player player, int i) {
        if (player != null && i > 0) {
            if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250) {
                for (int j = 0; j <= i; ++j) {
                    Location location = player.getLocation().add(0.0D, (double) (-j), 0.0D);
                    Material material = location.getBlock().getType();
                    boolean flag = method_350(player.getLocation().add(0.0D, (double) (-j), 0.0D), Material.SLIME_BLOCK, 1.0D);
                    boolean flag1 = BBBΠ(player.getLocation().add(0.0D, (double) (-j), 0.0D)) && material != Material.CARPET && material != Material.SNOW && !method_335(location) && !method_336(location) && !class_81.method_451(location) && !method_341(location);

                    if (flag1 && !flag) {
                        return false;
                    }

                    if (flag) {
                        return true;
                    }
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean BBΞ(Location location) {
        if (location != null && location.getBlock().isLiquid()) {
            byte b0 = location.getBlock().getData();

            return b0 == 0 || b0 == 8;
        } else {
            return false;
        }
    }
}
