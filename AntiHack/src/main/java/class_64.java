import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: ay
public class class_64 implements Listener {

    private Enums.HackType Ξ;

    public class_64() {
        this.field_129 = Enums.HackType.ImpossibleActions;
    }

    @EventHandler
    private void Ξ(PlayerDeathEvent playerdeathevent) {
        Player player = playerdeathevent.getEntity();

        if (!class_38.method_157(player, this.field_129, true)) {
            class_39.method_170(player, this.field_129.toString() + "=interact", 20);
        }
    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        if (!playerinteractevent.isCancelled()) {
            Player player = playerinteractevent.getPlayer();

            if (!class_38.method_157(player, this.field_129, true) && class_120.method_572("ImpossibleActions.check_actions") && class_39.method_169(player, this.field_129.toString() + "=interact")) {
                Action action = playerinteractevent.getAction();

                if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && (player.isSleeping() || class_71.PPPΠ(player) || class_71.OOOΠ(player) || class_71.PPΞ(player))) {
                    Block block = playerinteractevent.getClickedBlock();
                    String s = action.toString().toLowerCase().replaceAll("_", "-");

                    if (block == null) {
                        class_38.method_159(player, this.field_129, "t: interact, a: " + s);
                    } else {
                        class_38.method_159(player, this.field_129, "t: interact, a: " + s + ", b: " + class_66.method_343(block));
                    }

                    if (class_123.method_583(player, this.field_129)) {
                        playerinteractevent.setCancelled(true);
                    }
                }

            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (!playermoveevent.isCancelled() && class_120.method_572("ImpossibleActions.check_cactus")) {
            Player player = playermoveevent.getPlayer();

            if (!class_38.method_157(player, this.field_129, true)) {
                Location location = player.getLocation();
                double d0 = 0.9375D;
                double d1 = location.getY() - (double) location.getBlockY();
                double d2 = d1 < d0 ? 0.0D : 1.0D - d1;
                boolean flag = (!class_66.BBBΠ(location) || location.getBlock().getType() == Material.CACTUS) && !class_66.BBBΠ(location.clone().add(0.0D, 1.0D, 0.0D)) && class_66.method_348(player, 0.3D, d2, 0.3D) && (class_71.method_416(player.getLocation()) || player.isOnGround());

                if (flag && d1 != d0 && location.clone().add(0.0D, -0.5D, 0.0D).getBlock().getType() == Material.CACTUS) {
                    class_36.method_135(player, this.field_129.toString() + "=cactus", 20);
                    if (class_36.method_133(player, this.field_129.toString() + "=cactus") >= 5) {
                        if (class_39.method_169(player, this.field_129.toString() + "=cactus")) {
                            class_38.method_159(player, this.field_129, "t: cactus");
                            class_39.method_170(player, this.field_129.toString() + "=cactus", 2);
                        }

                        if (class_123.method_583(player, this.field_129)) {
                            player.damage(1.0D);
                        }
                    }
                }

            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockPlaceEvent blockplaceevent) {
        if (!blockplaceevent.isCancelled()) {
            Player player = blockplaceevent.getPlayer();
            Block block = blockplaceevent.getBlock();
            Location location = block.getLocation();

            if (!class_38.method_157(player, this.field_129, true) && class_66.BBBΠ(location)) {
                if (class_120.method_572("ImpossibleActions.check_item_use") && class_71.method_406(player)) {
                    class_38.method_159(player, this.field_129, "t: item-use, e: place");
                    if (class_123.method_583(player, this.field_129)) {
                        blockplaceevent.setCancelled(true);
                    }
                }

                if (block.getType() != Material.FIRE && !player.isFlying() && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)) {
                    if (!class_39.method_169(player, this.field_129.toString() + "=cooldown")) {
                        if (class_123.method_583(player, this.field_129)) {
                            blockplaceevent.setCancelled(true);
                        }
                    } else {
                        Location location1 = player.getLocation();

                        if (location.getBlockY() == location1.getBlockY() - 1) {
                            boolean flag = false;
                            int i;

                            if (class_120.method_572("ImpossibleActions.check_tower") && location.getBlockX() == location1.getBlockX() && location.getBlockZ() == location1.getBlockZ() && class_66.method_350(location.clone().add(0.0D, -1.0D, 0.0D), block.getType(), 1.0D) && class_66.method_350(location.clone().add(0.0D, -2.0D, 0.0D), block.getType(), 1.0D)) {
                                i = class_71.method_413(player, PotionEffectType.JUMP);
                                int j = class_41.method_179(i >= 1 && i <= 2 && !class_71.method_416(player.getLocation()) ? 6 : 4);

                                class_36.method_135(player, this.field_129.toString() + "=tower=normal", 20);
                                if (class_36.method_133(player, this.field_129.toString() + "=tower=normal") >= j) {
                                    class_38.method_159(player, this.field_129, "t: tower(normal), b: " + class_66.method_343(block));
                                    flag = true;
                                }

                                if (!flag && class_45.method_209(player, this.field_129.toString() + "=tower")) {
                                    long k = class_45.method_210(player, this.field_129.toString() + "=tower");

                                    if (k <= 300L) {
                                        --j;
                                        class_36.method_135(player, this.field_129.toString() + "=tower=constant", 20);
                                        if (class_36.method_133(player, this.field_129.toString() + "=tower=constant") >= j) {
                                            class_38.method_159(player, this.field_129, "t: tower(constant), b: " + class_66.method_343(block));
                                            flag = true;
                                        }
                                    }
                                }

                                class_45.method_211(player, this.field_129.toString() + "=tower");
                            }

                            if (!flag && class_120.method_572("ImpossibleActions.check_scaffold") && location.clone().add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.AIR && (class_71.method_416(location1) || class_71.method_416(location1.clone().add(0.0D, -1.0D, 0.0D))) && (class_89.method_484(location.getBlockX(), location1.getBlockX()) <= 1 && location.getBlockZ() == location1.getBlockZ() || class_89.method_484(location.getBlockZ(), location1.getBlockZ()) <= 1 && location.getBlockX() == location1.getBlockX())) {
                                double d0 = class_69.method_373(player);
                                double d1 = class_41.method_178(class_69.method_385(player, 0.22D, 4.0D, PotionEffectType.SPEED));

                                if (d0 >= d1) {
                                    class_36.method_135(player, this.field_129.toString() + "=scaffold=custom", 20);
                                }

                                class_36.method_135(player, this.field_129.toString() + "=scaffold=normal", 20);
                                int l = class_36.method_133(player, this.field_129.toString() + "=scaffold=normal");
                                int i1 = class_36.method_133(player, this.field_129.toString() + "=scaffold=custom");

                                if (l >= 6) {
                                    class_38.method_159(player, this.field_129, "t: scaffold(normal), b: " + class_66.method_343(block));
                                    flag = true;
                                } else if (i1 >= 3 && d0 >= d1) {
                                    class_38.method_159(player, this.field_129, "t: scaffold(custom), b: " + class_66.method_343(block) + ", d: " + d0);
                                    flag = true;
                                }
                            }

                            if (flag && class_123.method_583(player, this.field_129)) {
                                i = class_120.method_571("ImpossibleActions.cancel_seconds");
                                i = i > 60 ? 60 : i;
                                blockplaceevent.setCancelled(true);
                                class_39.method_170(player, this.field_129.toString() + "=cooldown", i * 20);
                            }
                        }
                    }

                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockBreakEvent blockbreakevent) {
        if (!blockbreakevent.isCancelled()) {
            Player player = blockbreakevent.getPlayer();

            if (!class_38.method_157(player, this.field_129, true) && class_120.method_572("ImpossibleActions.check_item_use") && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250) {
                if (class_71.method_406(player)) {
                    class_38.method_159(player, this.field_129, "t: item-use, e: break");
                    if (class_123.method_583(player, this.field_129)) {
                        blockbreakevent.setCancelled(true);
                    }
                }

            }
        }
    }
}
