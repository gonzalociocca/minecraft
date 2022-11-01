import me.vagdedes.spartan.api.PlayerViolationEvent;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

// $FF: renamed from: bg
public class class_77 implements Listener {

    private static int Ξ;
    private static double Ξ;

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_69.method_369(player);
    }

    public static boolean Ξ(Player player) {
        return class_71.method_409(player, class_77.field_154, class_77.field_155) || !class_39.method_169(player, "extremely=pushed");
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (!entitydamagebyentityevent.isCancelled()) {
            DamageCause damagecause = entitydamagebyentityevent.getCause();
            Player player;

            if (entitydamagebyentityevent.getDamager() != null && entitydamagebyentityevent.getEntity() instanceof Player && (damagecause == DamageCause.ENTITY_ATTACK || damagecause == DamageCause.PROJECTILE)) {
                player = (Player) entitydamagebyentityevent.getEntity();
                class_39.method_170(player, "combat", 100);
            }

            if (entitydamagebyentityevent.getDamager() instanceof Player) {
                player = (Player) entitydamagebyentityevent.getDamager();
                if (damagecause == DamageCause.ENTITY_ATTACK) {
                    class_39.method_170(player, "hit_entity", 40);
                }
            }

        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        Player player = playermoveevent.getPlayer();
        Location location = playermoveevent.getTo();
        Double odouble = Double.valueOf(location.getY() - (double) location.getBlockY());
        double d0 = class_89.method_475(playermoveevent.getFrom(), location);
        boolean flag = player.getVehicle() != null;

        if (odouble.toString().length() <= 7) {
            class_69.field_138.put(player, odouble);
        }

        if (class_71.method_409(player, class_77.field_154, class_77.field_155)) {
            class_39.method_170(player, "extremely=pushed", 30);
        }

        if (class_71.method_416(player.getLocation())) {
            class_39.method_170(player, "ground", 1);
            if (!flag && d0 < 0.29D) {
                class_71.method_402(player);
            }
        }

        if (!class_69.method_389(playermoveevent)) {
            class_39.method_171(player, "walking");
            class_39.method_171(player, "sprint");
            class_69.HHΞ.remove(player);
            if (!flag) {
                class_71.method_402(player);
            }

        } else if (!class_39.method_169(player, "teleport_cooldown")) {
            class_49.method_248(player);
            class_38.method_160(player, Enums.HackType.Fly, 3);
            class_38.method_160(player, Enums.HackType.NoFall, 3);
            class_38.method_160(player, Enums.HackType.Sprint, 3);
            class_69.method_386(player, player.getLocation());
            class_39.method_171(player, "walking");
            class_39.method_171(player, "sprint");
            class_69.HHΞ.remove(player);
            if (!flag) {
                class_71.method_402(player);
            }

        } else {
            if (class_69.method_394(player)) {
                class_39.method_170(player, "chasing", 40);
            }

            if (class_55.method_297(player)) {
                class_69.HHΞ.put(player, Double.valueOf(d0));
            } else {
                class_69.HHΞ.remove(player);
            }

            if (!class_10.method_40(player) && !class_22.method_82(player) && !class_71.HHΞ(player) && !class_13.method_53(player) && !class_15.method_61(player) && !class_8.method_32(player) && class_71.method_396(player) <= 0.0F && !class_18.method_69(player) && !class_16.method_64(player)) {
                if (class_71.method_416(player.getLocation()) && !class_71.method_398(player) && !class_66.BBBΠ(player.getLocation()) && !class_66.method_332(player.getLocation().add(0.0D, -1.0D, 0.0D))) {
                    if (d0 >= 0.215D && d0 < 0.2203D) {
                        class_39.method_170(player, "walking", 3);
                    } else {
                        class_39.method_171(player, "walking");
                    }

                    if (d0 > 0.28D && d0 < 0.287D) {
                        class_39.method_170(player, "sprint", 3);
                    } else {
                        class_39.method_171(player, "sprint");
                    }
                } else {
                    class_39.method_171(player, "walking");
                    class_39.method_171(player, "sprint");
                }

                if (class_69.method_376(odouble.doubleValue()) && (class_71.method_416(player.getLocation()) || class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D)) || class_71.method_416(player.getLocation().add(0.0D, -1.5D, 0.0D)))) {
                    if (d0 > 0.24D && d0 < 0.29D && !player.isSprinting()) {
                        class_39.method_170(player, "walk-jumping", 3);
                    }

                    if (d0 > 0.5D && d0 < 0.68D || player.isSprinting()) {
                        class_39.method_170(player, "sprint-jumping", 3);
                    }
                } else {
                    class_39.method_171(player, "walk-jumping");
                    class_39.method_171(player, "sprint-jumping");
                }

            } else {
                class_39.method_171(player, "walking");
                class_39.method_171(player, "sprint");
            }
        }
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        Player player = playerteleportevent.getPlayer();

        if (class_49.method_246(player)) {
            class_71.method_412(player);
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockBreakEvent blockbreakevent) {
        if (!blockbreakevent.isCancelled()) {
            Player player = blockbreakevent.getPlayer();

            class_39.method_170(player, "illegal-falling", 100);
        }

    }

    @EventHandler
    private void Ξ(PlayerViolationEvent playerviolationevent) {
        Player player = playerviolationevent.getPlayer();
        Enums.HackType enums_hacktype = playerviolationevent.getHackType();

        if (enums_hacktype == Enums.HackType.BoatMove || enums_hacktype == Enums.HackType.EntityMove) {
            class_71.method_402(player);
        }

    }

    static {
        class_77.field_154 = 50;
        class_77.field_155 = 0.75D;
    }
}
