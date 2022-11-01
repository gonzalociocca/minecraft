import org.bukkit.Location;
import org.bukkit.Material;

// $FF: renamed from: bi
public class class_81 {

    public static boolean Ξ(Location location) {
        if (location != null && !method_459(location.clone())) {
            double d0 = location.getY() - (double) location.getBlockY();
            double d1 = 1.0D - d0;

            if (d0 == 0.0D) {
                d1 = 0.0D;
            }

            if (!class_66.method_350(location, Material.SNOW, 0.3D) && !class_66.method_350(location, Material.CARPET, 0.3D) && !class_66.method_341(location) && (method_452(location, 0.3D, -1.0D, 0.3D) || !method_452(location, 0.3D, d1, 0.3D))) {
                double d2 = location.getX() - (double) location.getBlockX();
                double d3 = location.getZ() - (double) location.getBlockZ();
                double d4 = -0.01D;

                if (d0 != 0.0D) {
                    d4 = 0.0D;
                }

                if (method_450(location.clone().add(0.0D, d4, 0.0D))) {
                    return true;
                } else if (d2 > 0.3D && d2 < 0.7D && d3 > 0.3D && d3 < 0.7D) {
                    return HHΞ(location.clone().add(0.0D, d4, 0.0D));
                } else {
                    boolean flag;
                    boolean flag1;
                    boolean flag2;

                    if (d2 >= 0.0D && d2 < 0.3D && d3 >= 0.0D && d3 < 0.3D) {
                        if (HHΞ(location.clone().add(0.0D, d4, 0.0D))) {
                            return true;
                        } else {
                            flag = method_450(location.clone().add(0.0D, d4, -1.0D));
                            flag1 = method_450(location.clone().add(-1.0D, d4, 0.0D));
                            flag2 = method_450(location.clone().add(-1.0D, d4, -1.0D));
                            return flag || flag1 || flag2;
                        }
                    } else if (d2 > 0.7D && d2 < 1.0D && d3 >= 0.0D && d3 < 0.3D) {
                        if (HHΞ(location.clone().add(0.0D, d4, 0.0D))) {
                            return true;
                        } else {
                            flag = method_450(location.clone().add(1.0D, d4, 0.0D));
                            flag1 = method_450(location.clone().add(0.0D, d4, -1.0D));
                            flag2 = method_450(location.clone().add(1.0D, d4, -1.0D));
                            return flag || flag1 || flag2;
                        }
                    } else if (d2 > 0.7D && d2 < 1.0D && d3 > 0.7D && d3 < 1.0D) {
                        if (HHΞ(location.clone().add(0.0D, d4, 0.0D))) {
                            return true;
                        } else {
                            flag = method_450(location.clone().add(1.0D, d4, 0.0D));
                            flag1 = method_450(location.clone().add(0.0D, d4, 1.0D));
                            flag2 = method_450(location.clone().add(1.0D, d4, 1.0D));
                            return flag || flag1 || flag2;
                        }
                    } else if (d2 >= 0.0D && d2 < 0.3D && d3 > 0.7D && d3 < 1.0D) {
                        if (HHΞ(location.clone().add(0.0D, d4, 0.0D))) {
                            return true;
                        } else {
                            flag = method_450(location.clone().add(-1.0D, d4, 0.0D));
                            flag1 = method_450(location.clone().add(0.0D, d4, 1.0D));
                            flag2 = method_450(location.clone().add(-1.0D, d4, 1.0D));
                            return flag || flag1 || flag2;
                        }
                    } else {
                        return d2 >= 0.0D && d2 < 0.3D && d3 > 0.3D && d3 < 0.7D ? (HHΞ(location.clone().add(0.0D, d4, 0.0D)) ? true : method_450(location.clone().add(-1.0D, d4, 0.0D))) : (d2 > 0.7D && d2 < 1.0D && d3 > 0.3D && d3 < 0.7D ? (HHΞ(location.clone().add(0.0D, d4, 0.0D)) ? true : method_450(location.clone().add(1.0D, d4, 0.0D))) : (d2 > 0.3D && d2 < 0.7D && d3 >= 0.0D && d3 < 0.3D ? (HHΞ(location.clone().add(0.0D, d4, 0.0D)) ? true : method_450(location.clone().add(0.0D, d4, -1.0D))) : (d2 > 0.3D && d2 < 0.7D && d3 > 0.7D && d3 < 1.0D ? (HHΞ(location.clone().add(0.0D, d4, 0.0D)) ? true : method_450(location.clone().add(0.0D, d4, 1.0D))) : false)));
                    }
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private static boolean HHΞ(Location location) {
        return location == null ? false : location.getBlock().getType().isSolid() && class_66.BBBΠ(location);
    }

    private static boolean HΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();
            double d0 = location.getY() - (double) location.getBlockY();
            double d1 = 1.0D - d0;

            return (class_66.BBBΠ(location) && !method_451(location) || method_451(location) || (method_451(location.clone().add(0.0D, -1.0D, 0.0D)) || class_66.method_336(location)) && d0 == 0.5D || class_66.method_335(location) && (d0 == 0.0D || d0 == 0.5D)) && (!class_66.BBBΠ(location.clone().add(0.0D, d1, 0.0D)) || method_451(location.clone().add(0.0D, d1, 0.0D)) || location.clone().add(0.0D, d1, 0.0D).getBlock().getType() == Material.SNOW || location.clone().add(0.0D, d1, 0.0D).getBlock().getType() == Material.CARPET || class_66.PPPΞ(location.clone().add(0.0D, d1, 0.0D))) || (material == Material.getMaterial(101) || class_66.method_334(location)) && d0 == 0.0D || class_66.method_339(location.clone().add(0.0D, -1.0D, 0.0D)) && d0 <= 0.5D;
        }
    }

    public static boolean Π(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();

            return class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251 && (material == Material.CHORUS_PLANT || material == Material.END_ROD) ? true : class_66.method_334(location) || class_66.PPPΞ(location) || class_66.BPPΠ(location) || class_66.method_333(location) || class_66.method_337(location) || class_66.O1337(location) || material == Material.getMaterial(101) || material == Material.ANVIL || material == Material.CACTUS || material == Material.CAKE_BLOCK || material == Material.COCOA || material == Material.COBBLE_WALL || material == Material.SKULL || material == Material.FLOWER_POT || material == Material.DRAGON_EGG;
        }
    }

    private static boolean Ξ(Location location, double d0, double d1, double d2) {
        return location == null ? false : class_66.method_341(location.clone().add(0.0D, d1, 0.0D)) || class_66.method_341(location.clone().add(d0, d1, 0.0D)) || class_66.method_341(location.clone().add(-d0, d1, 0.0D)) || class_66.method_341(location.clone().add(0.0D, d1, d2)) || class_66.method_341(location.clone().add(0.0D, d1, -d2)) || class_66.method_341(location.clone().add(d0, d1, d2)) || class_66.method_341(location.clone().add(-d0, d1, -d2)) || class_66.method_341(location.clone().add(d0, d1, -d2)) || class_66.method_341(location.clone().add(-d0, d1, d2));
    }

    private static boolean BΞ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.clone().getBlock().getType();

            return O1337(location) || material == Material.AIR && (method_454(location, Material.SKULL) && method_457(location, 50) || method_454(location, Material.COBBLE_WALL) && method_457(location, 50) || method_454(location, Material.FLOWER_POT) && method_457(location, 50) || method_454(location, Material.CACTUS) && method_457(location, 238) || method_454(location, Material.ANVIL) && (method_457(location, 175) || method_457(location, 300)) || method_454(location, Material.ENCHANTMENT_TABLE) && method_457(location, 300) || method_454(location, Material.CHEST) && method_457(location, 238) || method_454(location, Material.TRAPPED_CHEST) && method_457(location, 238) || method_454(location, Material.ENDER_CHEST) && method_457(location, 238) || method_454(location, Material.CAKE_BLOCK) && method_457(location, 238) || method_454(location, Material.CARPET) && method_457(location, 300) || method_454(location, Material.HOPPER) && method_457(location, 300) || method_454(location, Material.BREWING_STAND) && method_457(location, 300) || method_454(location, Material.CAULDRON) && method_457(location, 300) || method_454(location, Material.SOIL) && method_457(location, 300) || method_454(location, Material.SOUL_SAND) && method_457(location, 300) || method_454(location, Material.LEAVES) && method_457(location, 300) || method_454(location, Material.LEAVES_2) && method_457(location, 300) || method_454(location, Material.BED_BLOCK) && method_457(location, 300) || method_454(location, Material.ENDER_PORTAL_FRAME) && method_457(location, 300) || method_455(location) && method_457(location, 300) || BPPΠ(location) && method_457(location, 300));
        }
    }

    private static boolean Ξ(Location location, Material material) {
        return location != null && material != null ? method_456(location, material) && (location.getBlock().getType() == Material.AIR || location.getBlock().getType() == material) && (location.clone().add(0.0D, 0.0D, -0.3D).getBlock().getType() == material && method_456(location.clone().add(0.0D, 0.0D, -0.3D), material) || location.clone().add(0.0D, 0.0D, 0.3D).getBlock().getType() == material && method_456(location.clone().add(0.0D, 0.0D, 0.3D), material) || location.clone().add(-0.3D, 0.0D, 0.0D).getBlock().getType() == material && method_456(location.clone().add(-0.3D, 0.0D, 0.0D), material) || location.clone().add(0.3D, 0.0D, 0.0D).getBlock().getType() == material && method_456(location.clone().add(0.3D, 0.0D, 0.0D), material) || location.clone().add(0.3D, 0.0D, 0.3D).getBlock().getType() == material && method_456(location.clone().add(0.3D, 0.0D, 0.3D), material) || location.clone().add(-0.3D, 0.0D, -0.3D).getBlock().getType() == material && method_456(location.clone().add(-0.3D, 0.0D, -0.3D), material) || location.clone().add(-0.3D, 0.0D, 0.3D).getBlock().getType() == material && method_456(location.clone().add(-0.3D, 0.0D, 0.3D), material) || location.clone().add(0.3D, 0.0D, -0.3D).getBlock().getType() == material && method_456(location.clone().add(0.3D, 0.0D, -0.3D), material)) : false;
    }

    private static boolean BPPΠ(Location location) {
        return location == null ? false : method_456(location, (Material) null) && (location.getBlock().getType() == Material.AIR || class_66.method_336(location)) && (class_66.method_336(location.clone().add(0.0D, 0.0D, -0.3D)) && method_456(location.clone().add(0.0D, 0.0D, -0.3D), (Material) null) || class_66.method_336(location.clone().add(0.0D, 0.0D, 0.3D)) && method_456(location.clone().add(0.0D, 0.0D, 0.3D), (Material) null) || class_66.method_336(location.clone().add(-0.3D, 0.0D, 0.0D)) && method_456(location.clone().add(-0.3D, 0.0D, 0.0D), (Material) null) || class_66.method_336(location.clone().add(0.3D, 0.0D, 0.0D)) && method_456(location.clone().add(0.3D, 0.0D, 0.0D), (Material) null) || class_66.method_336(location.clone().add(0.3D, 0.0D, 0.3D)) && method_456(location.clone().add(0.3D, 0.0D, 0.3D), (Material) null) || class_66.method_336(location.clone().add(-0.3D, 0.0D, -0.3D)) && method_456(location.clone().add(-0.3D, 0.0D, -0.3D), (Material) null) || class_66.method_336(location.clone().add(-0.3D, 0.0D, 0.3D)) && method_456(location.clone().add(-0.3D, 0.0D, 0.3D), (Material) null) || class_66.method_336(location.clone().add(0.3D, 0.0D, -0.3D)) && method_456(location.clone().add(0.3D, 0.0D, -0.3D), (Material) null));
    }

    private static boolean PΞ(Location location) {
        return location == null ? false : method_456(location, (Material) null) && (location.getBlock().getType() == Material.AIR || class_66.method_335(location)) && (class_66.method_335(location.clone().add(0.0D, 0.0D, -0.3D)) && method_456(location.clone().add(0.0D, 0.0D, -0.3D), (Material) null) || class_66.method_335(location.clone().add(0.0D, 0.0D, 0.3D)) && method_456(location.clone().add(0.0D, 0.0D, 0.3D), (Material) null) || class_66.method_335(location.clone().add(-0.3D, 0.0D, 0.0D)) && method_456(location.clone().add(-0.3D, 0.0D, 0.0D), (Material) null) || class_66.method_335(location.clone().add(0.3D, 0.0D, 0.0D)) && method_456(location.clone().add(0.3D, 0.0D, 0.0D), (Material) null) || class_66.method_335(location.clone().add(0.3D, 0.0D, 0.3D)) && method_456(location.clone().add(0.3D, 0.0D, 0.3D), (Material) null) || class_66.method_335(location.clone().add(-0.3D, 0.0D, -0.3D)) && method_456(location.clone().add(-0.3D, 0.0D, -0.3D), (Material) null) || class_66.method_335(location.clone().add(-0.3D, 0.0D, 0.3D)) && method_456(location.clone().add(-0.3D, 0.0D, 0.3D), (Material) null) || class_66.method_335(location.clone().add(0.3D, 0.0D, -0.3D)) && method_456(location.clone().add(0.3D, 0.0D, -0.3D), (Material) null));
    }

    private static boolean Π(Location location, Material material) {
        if (location == null) {
            return false;
        } else {
            double d0 = class_69.method_381(location.clone().getY(), 4);

            return material != null && (material == Material.CAKE_BLOCK && d0 != 0.5D || material == Material.ENDER_CHEST && d0 != 0.875D || material == Material.TRAPPED_CHEST && d0 != 0.875D || material == Material.CHEST && d0 != 0.875D || material == Material.ANVIL && d0 != 0.0D || material == Material.CACTUS && d0 != 0.9375D || material == Material.FLOWER_POT && d0 != 0.375D || material == Material.COBBLE_WALL && d0 != 0.5D || material == Material.SKULL && d0 != 0.5D || material == Material.ENCHANTMENT_TABLE && d0 != 0.75D || material == Material.CARPET && d0 != 0.0625D || material == Material.HOPPER && d0 != 0.625D && d0 != 0.0D || material == Material.BREWING_STAND && d0 != 0.875D && d0 != 0.125D || material == Material.CAULDRON && d0 != 0.3125D && d0 != 0.0D || material == Material.SOIL && d0 != 0.9375D || material == Material.SOUL_SAND && d0 != 0.875D || material == Material.LEAVES && d0 != 0.0D || material == Material.LEAVES_2 && d0 != 0.0D || material == Material.BED_BLOCK && d0 != 0.5625D || material == Material.ENDER_PORTAL_FRAME && d0 != 0.8125D) || class_66.method_335(location) && d0 != 0.5D || class_66.method_336(location) && d0 != 0.5D && d0 != 0.0D;
        }
    }

    private static boolean Ξ(Location location, int i) {
        if (location == null) {
            return false;
        } else {
            double d0 = 1000.0D - (double) i;
            double d1 = (double) Math.round((location.getX() - (double) location.getBlockX()) * 1000.0D);
            double d2 = (double) Math.round((location.getZ() - (double) location.getBlockZ()) * 1000.0D);

            return d1 == d0 || d2 == d0 || d1 == (double) i || d2 == (double) i;
        }
    }

    private static boolean OΠ(Location location) {
        if (location == null) {
            return false;
        } else {
            Material material = location.getBlock().getType();
            double d0 = class_69.method_381(location.getY(), 1);

            return class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251 && material == Material.END_ROD && d0 != 0.0D && class_69.method_381(location.getY(), 3) != 0.625D ? true : class_66.method_334(location) && d0 != 0.0D || class_66.PPPΞ(location) && d0 != 0.0D || class_66.method_337(location) && d0 != 0.5D || class_66.O1337(location) && d0 != 0.5D || material == Material.getMaterial(101) && d0 != 0.0D || class_66.BPPΠ(location) && d0 != 0.0D;
        }
    }

    private static boolean O1337(Location location) {
        return location == null ? false : PPPΞ(location) && method_458(location);
    }

    private static boolean PPPΞ(Location location) {
        return location == null ? false : (!class_66.BBBΠ(location.clone().add(0.3D, 0.0D, 0.0D)) || method_458(location.clone().add(0.3D, 0.0D, 0.0D))) && (!class_66.BBBΠ(location.clone().add(-0.3D, 0.0D, 0.0D)) || method_458(location.clone().add(-0.3D, 0.0D, 0.0D))) && (!class_66.BBBΠ(location.clone().add(0.0D, 0.0D, 0.3D)) || method_458(location.clone().add(0.0D, 0.0D, 0.3D))) && (!class_66.BBBΠ(location.clone().add(0.0D, 0.0D, -0.3D)) || method_458(location.clone().add(0.0D, 0.0D, -0.3D))) && (!class_66.BBBΠ(location.clone().add(0.3D, 0.0D, 0.3D)) || method_458(location.clone().add(0.3D, 0.0D, 0.3D))) && (!class_66.BBBΠ(location.clone().add(-0.3D, 0.0D, -0.3D)) || method_458(location.clone().add(-0.3D, 0.0D, -0.3D))) && (!class_66.BBBΠ(location.clone().add(0.3D, 0.0D, -0.3D)) || method_458(location.clone().add(0.3D, 0.0D, -0.3D))) && (!class_66.BBBΠ(location.clone().add(-0.3D, 0.0D, 0.3D)) || method_458(location.clone().add(-0.3D, 0.0D, 0.3D)));
    }

    private static boolean PΠ(Location location) {
        return method_453(location.clone().add(0.0D, -1.0D, 0.0D)) && method_453(location);
    }
}
