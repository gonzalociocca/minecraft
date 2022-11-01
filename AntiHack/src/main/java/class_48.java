import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: ak
public class class_48 implements Listener {

    private static Enums.HackType Ξ;

    public static void Ξ(Player player) {
        if (class_69.method_388(player.getLocation(), class_69.method_383(player)) && method_244(player)) {
            int i;

            if (player.isSprinting()) {
                i = class_36.method_133(player, class_48.field_81.toString() + "=sprint") + 1;
                class_36.method_134(player, class_48.field_81.toString() + "=sprint", i);
            } else {
                class_36.method_138(player, class_48.field_81.toString() + "=sprint");
                if (class_71.method_408(player)) {
                    i = class_36.method_133(player, class_48.field_81.toString() + "=custom_sprint") + 1;
                    class_36.method_134(player, class_48.field_81.toString() + "=custom_sprint", i);
                } else {
                    class_36.method_138(player, class_48.field_81.toString() + "=custom_sprint");
                }

                if (class_71.method_407(player)) {
                    i = class_36.method_133(player, class_48.field_81.toString() + "=walking") + 1;
                    class_36.method_134(player, class_48.field_81.toString() + "=walking", i);
                } else {
                    class_36.method_138(player, class_48.field_81.toString() + "=walking");
                }
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (method_244(player) && class_120.method_572("NoSlowdown.check_food_eating")) {
            Block block = playerinteractevent.getClickedBlock();

            if (playerinteractevent.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.CAKE_BLOCK) {
                class_39.method_170(player, class_48.field_81.toString() + "=food-eating", 10);
            }

        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(FoodLevelChangeEvent foodlevelchangeevent) {
        if (foodlevelchangeevent.getEntity() instanceof Player && !foodlevelchangeevent.isCancelled()) {
            Player player = (Player) foodlevelchangeevent.getEntity();

            if (!method_244(player) || player.getWorld().getDifficulty() == Difficulty.PEACEFUL || player.hasPotionEffect(PotionEffectType.SATURATION) || !class_120.method_572("NoSlowdown.check_food_eating") || !class_39.method_169(player, class_48.field_81.toString() + "=food-eating")) {
                return;
            }

            if (foodlevelchangeevent.getFoodLevel() > player.getFoodLevel() && method_245(player)) {
                method_243(player, player.getLocation(), 10, "t: food eating");
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityShootBowEvent entityshootbowevent) {
        if (entityshootbowevent.getEntity() instanceof Player && !entityshootbowevent.isCancelled()) {
            Player player = (Player) entityshootbowevent.getEntity();

            if (!method_244(player) || !class_120.method_572("NoSlowdown.check_bow_shots") || !method_245(player) || class_36.method_133(player, class_48.field_81.toString() + "=dynamic_sprint") >= 4 || class_71.HHΠ(player) || class_71.BBBΠ(player)) {
                return;
            }

            entityshootbowevent.setCancelled(true);
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
            double d0 = class_89.method_475(location1, location);
            double d1 = location.getY() - location1.getY();

            if (class_69.method_375(player, d0, d1, 0.15D)) {
                int i = class_36.method_133(player, class_48.field_81.toString() + "=dynamic_sprint") + 1;

                class_36.method_134(player, class_48.field_81.toString() + "=dynamic_sprint", i);
            } else {
                class_36.method_138(player, class_48.field_81.toString() + "=dynamic_sprint");
            }

            if (method_244(player) && !class_71.method_397(player) && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_39.method_169(player, class_48.field_81.toString() + "=held") && class_36.method_133(player, class_48.field_81.toString() + "=damage") != 1 && class_71.method_406(player) && method_245(player) && class_120.method_572("NoSlowdown.check_item_usage") && (class_71.method_416(player.getLocation()) || !class_71.BBΞ(player))) {
                String s = player.getItemInHand().getType().toString().toLowerCase().replaceAll("_", "-");

                if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251) {
                    s = "(" + s + ", " + player.getInventory().getItemInOffHand().getType().toString().toLowerCase().replaceAll("_", "-") + ")";
                }

                class_36.method_135(player, class_48.field_81.toString() + "=use", 5);
                if (class_36.method_133(player, class_48.field_81.toString() + "=use") >= 3) {
                    method_243(player, location1, 10, "t: item usage, holding: " + s + ", ds: " + d0 + ", dy: " + d1);
                }

            }
        }
    }

    @EventHandler
    private void Ξ(PlayerItemHeldEvent playeritemheldevent) {
        Player player = playeritemheldevent.getPlayer();

        class_36.method_138(player, class_48.field_81.toString() + "=damage");
        class_39.method_170(player, class_48.field_81.toString() + "=held", 3);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && entitydamagebyentityevent.isCancelled() && entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (player.getItemInHand().getType() == Material.BOW) {
                class_36.method_134(player, class_48.field_81.toString() + "=damage", 1);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Π(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (method_244(player) && !class_71.BPΞ(player) && !class_71.method_409(player, 1, 0.5D) && class_120.method_572("NoSlowdown.check_cobweb_movement")) {
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d0 = location.getY() - location1.getY();
                double d1 = class_89.method_475(location, location1);
                boolean flag = true;
                boolean flag1 = false;

                if (class_66.method_332(player.getLocation().add(0.0D, -1.0D, 0.0D))) {
                    class_39.method_170(player, class_48.field_81.toString() + "=ice=protection", 20);
                }

                boolean flag2 = !class_39.method_169(player, class_48.field_81.toString() + "=limit");
                boolean flag3 = !class_39.method_169(player, class_48.field_81.toString() + "=ice=protection");
                double d2 = !flag2 && !class_10.method_40(player) ? (class_71.method_396(player) > 0.0F ? 0.4D : (flag3 ? 0.275D : (class_18.method_69(player) ? 0.175D : 0.15D))) : 0.5D;

                if (player.getLocation().getBlock().getType() == Material.AIR && player.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.WEB) {
                    if (d0 == 0.0D && d1 >= 0.21D) {
                        class_36.method_135(player, class_48.field_81.toString() + "=walking(web)", 20);
                        if (class_36.method_133(player, class_48.field_81.toString() + "=walking(web)") == 6) {
                            class_36.method_138(player, class_48.field_81.toString() + "=walking(web)");
                            method_243(player, location1, 0, "t: walking(web), dy: " + d0 + ", ds: " + d1);
                        }
                    }

                    if (d0 > 0.0D && d1 > 0.0D) {
                        class_36.method_135(player, class_48.field_81.toString() + "=jumping(web)", 20);
                        if (class_36.method_133(player, class_48.field_81.toString() + "=jumping(web)") == 6) {
                            class_36.method_138(player, class_48.field_81.toString() + "=jumping(web)");
                            method_243(player, location1, 0, "t: jumping(web), dy: " + d0 + ", ds: " + d1);
                        }
                    }
                }

                for (int i = -1; i <= 1; ++i) {
                    if (player.getLocation().add(0.0D, (double) i, 0.0D).getBlock().getType() == Material.WEB) {
                        flag1 = true;
                        boolean flag4 = false;

                        if (i == -1) {
                            flag4 = true;
                        }

                        if (i >= 0) {
                            double d3 = class_69.HHΞ(player, location1);
                            int j = class_36.method_133(player, class_48.field_81.toString() + "=web_ticks") + 1;

                            class_36.method_134(player, class_48.field_81.toString() + "=web_ticks", j);
                            if (d3 >= class_41.method_178(0.075D) && j >= 10) {
                                class_36.method_135(player, class_48.field_81.toString() + "=speed=packets", 20);
                                if (class_36.method_133(player, class_48.field_81.toString() + "=speed=packets") >= 2) {
                                    method_243(player, location1, 0, "t: web(packets), d: " + d1 + ", diff: " + d3 + ", dm: " + d2);
                                }
                            }

                            if (j <= 10 && d2 == 0.15D) {
                                d2 += 0.4D;
                            }

                            if (d2 > 0.0D) {
                                d2 = class_41.method_178(class_69.method_385(player, d2, 4.0D, PotionEffectType.SPEED));
                                if (d1 >= d2) {
                                    method_243(player, location1, 0, "type: web(speed), ds: " + d1 + ", dm: " + d2);
                                }

                                if (d1 >= 0.1D && !class_71.HHΞ(player) && !class_71.BPPΠ(player) && !class_10.method_40(player)) {
                                    double d4 = class_73.method_418(d1, 10);

                                    if (class_45.method_209(player, class_48.field_81.toString() + "=repeated=speed=" + d4)) {
                                        long k = class_45.method_210(player, class_48.field_81.toString() + "=repeated=speed=" + d4);

                                        if (k <= 250L) {
                                            class_36.method_135(player, class_48.field_81.toString() + "=repeated=speed", 10);
                                            if (class_36.method_133(player, class_48.field_81.toString() + "=repeated=speed") == 3) {
                                                class_36.method_138(player, class_48.field_81.toString() + "=repeated=speed");
                                                method_243(player, location1, 0, "t: web(repeated), t: " + k + ", d: " + d4 + ", dm: " + d2);
                                            }
                                        }
                                    }

                                    class_45.method_211(player, class_48.field_81.toString() + "=repeated=speed=" + d4);
                                }
                            }

                            if ((d0 <= -1.3D || d0 >= 0.15D && (d0 != 0.5D || d0 == 0.5D && flag4)) && !class_10.method_40(player) && !class_69.method_376(d0)) {
                                method_243(player, location1, 0, "t: web(ypos), dy: " + d0);
                            }
                        }
                        break;
                    }

                    if (i == -1) {
                        flag = false;
                    }
                }

                if (flag && !class_10.method_40(player) && (player.getLocation().getBlock().getType() == Material.WEB || !class_66.BBBΠ(player.getLocation()))) {
                    boolean flag5 = this.method_240(player, 1.0D, -1.0D, 1.0D) && d0 == 0.0D || d0 > 0.0D || this.method_240(player, 1.0D, 0.0D, 1.0D) && this.method_242(player.getLocation().add(0.0D, 1.0D, 0.0D)) && d0 <= -0.1D;

                    if (flag5 && d0 != 0.5D && !class_69.method_376(d0)) {
                        byte b0 = 1;
                        byte b1 = 1;

                        if (d0 <= 0.125D) {
                            b0 = 20;
                            b1 = 2;
                        }

                        class_36.method_135(player, class_48.field_81.toString() + "=full-block", b0);
                        if (class_36.method_133(player, class_48.field_81.toString() + "=full-block") >= b1) {
                            method_243(player, location1, 0, "t: web(full-block), dy: " + d0);
                        }
                    }
                }

                if (!flag1) {
                    class_36.method_138(player, class_48.field_81.toString() + "=web_ticks");
                }

            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockBreakEvent blockbreakevent) {
        Player player = blockbreakevent.getPlayer();

        if (method_244(player) && blockbreakevent.isCancelled() && blockbreakevent.getBlock().getType() == Material.WEB) {
            class_39.method_170(player, class_48.field_81.toString() + "=limit", 20);
        }

    }

    private boolean Ξ(Entity entity, double d0, double d1, double d2) {
        return entity == null ? false : this.method_241(entity.getLocation().add(0.0D, d1, 0.0D)) && this.method_241(entity.getLocation().add(d0, d1, 0.0D)) && this.method_241(entity.getLocation().add(-d0, d1, 0.0D)) && this.method_241(entity.getLocation().add(0.0D, d1, d2)) && this.method_241(entity.getLocation().add(0.0D, d1, -d2)) && this.method_241(entity.getLocation().add(d0, d1, d2)) && this.method_241(entity.getLocation().add(-d0, d1, -d2)) && this.method_241(entity.getLocation().add(d0, d1, -d2)) && this.method_241(entity.getLocation().add(-d0, d1, d2));
    }

    private boolean Ξ(Location location) {
        return location == null ? false : !class_66.BBBΠ(location) || location.getBlock().getType() == Material.WEB;
    }

    private boolean Π(Location location) {
        return location == null ? false : location.getBlock().getType() == Material.WEB;
    }

    private static void Ξ(Player player, Location location, int i, String s) {
        class_38.method_159(player, class_48.field_81, s);
        if (class_123.method_583(player, class_48.field_81)) {
            class_69.method_386(player, location);
            class_69.HHΞ(player);
            if (i > 0) {
                class_71.method_399(player, i);
            }
        }

    }

    private static boolean Ξ(Player player) {
        return class_55.method_298(player, class_48.field_81) && !class_71.method_400(player) && !class_13.method_53(player);
    }

    private static boolean Π(Player player) {
        return player == null ? false : class_71.HHΠ(player) || class_71.BBBΠ(player) || player.isSprinting() && class_36.method_133(player, class_48.field_81.toString() + "=sprint") >= 10 || class_71.method_408(player) && class_36.method_133(player, class_48.field_81.toString() + "=custom_sprint") >= 10 || class_71.method_407(player) && class_36.method_133(player, class_48.field_81.toString() + "=walking") >= 10 || class_36.method_133(player, class_48.field_81.toString() + "=dynamic_sprint") >= 4;
    }

    static {
        class_48.field_81 = Enums.HackType.NoSlowdown;
    }
}
