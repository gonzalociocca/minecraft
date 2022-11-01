import java.util.Iterator;
import me.vagdedes.spartan.api.PlayerViolationEvent;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

// $FF: renamed from: t
public class class_137 implements Listener {

    private double Ξ;
    private int Ξ;

    public class_137() {
        this.field_230 = 4.0D;
        this.field_231 = 15;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (player.getGameMode() != GameMode.ADVENTURE && player.getGameMode() != GameMode.SURVIVAL || class_69.HHΞ(player)) {
                return;
            }

            Entity entity = entitydamagebyentityevent.getEntity();
            double d0 = class_89.method_475(player.getLocation(), entity.getLocation());
            double d1 = class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 ? 4.0D : 4.5D;

            d1 = class_41.method_178(d1);
            d1 = d1 > 6.0D ? 6.0D : d1;
            if (d0 >= d1) {
                int i = class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 ? 2 : 3;

                class_36.method_135(player, "combat=distance", 88);
                if (class_36.method_133(player, "combat=distance") >= i) {
                    class_39.method_170(player, "combat=distance", this.field_231);
                }
            }
        }

    }

    @EventHandler
    private void Ξ(PlayerViolationEvent playerviolationevent) {
        Player player = playerviolationevent.getPlayer();
        Enums.HackType enums_hacktype = playerviolationevent.getHackType();

        if (enums_hacktype == Enums.HackType.Speed) {
            class_39.method_170(player, "combat=movement", this.field_231);
        } else if (enums_hacktype == Enums.HackType.HitReach) {
            class_39.method_170(player, "combat=distance", this.field_231);
        } else if (enums_hacktype == Enums.HackType.NoSwing && class_38.method_154(player, enums_hacktype) >= 1) {
            class_39.method_170(player, "combat=animation", this.field_231);
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        Player player = playermoveevent.getPlayer();

        if (class_55.method_297(player) && !class_69.HHΞ(player)) {
            Location location = playermoveevent.getTo();
            Location location1 = playermoveevent.getFrom();
            double d0 = class_89.method_475(location1, location);
            double d1 = location.getY() - location1.getY();

            if (class_141.method_681(player) >= 50.0F) {
                class_36.method_135(player, "combat=yaw", 10);
                if (class_36.method_133(player, "combat=yaw") >= 5) {
                    class_39.method_170(player, "combat=yaw", this.field_231);
                }
            }

            if (class_138.method_670(player) >= 40.0F) {
                class_36.method_135(player, "combat=pitch", 10);
                if (class_36.method_133(player, "combat=pitch") >= 5) {
                    class_39.method_170(player, "combat=pitch", this.field_231);
                }
            }

            if (class_71.HHΠ(player) || class_71.BBBΠ(player) || d0 > 0.34D && d1 > 0.164D) {
                class_39.method_170(player, "combat=movement", this.field_231);
            }

        }
    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "combat=movement");
    }

    public static boolean Π(Player player) {
        return !class_39.method_169(player, "combat=yaw");
    }

    public static boolean HHΞ(Player player) {
        return !class_39.method_169(player, "combat=pitch");
    }

    public static boolean HΞ(Player player) {
        return !class_39.method_169(player, "combat=animation");
    }

    public static boolean BΞ(Player player) {
        return !class_69.HHΞ(player) && !class_39.method_169(player, "combat=distance");
    }

    public static boolean Ξ(Entity entity) {
        return !class_71.method_416(entity.getLocation().add(0.0D, -0.25D, 0.0D));
    }

    public static int Ξ(Player player, double d0) {
        int i = 0;

        if (player == null) {
            return i;
        } else {
            Iterator iterator = player.getNearbyEntities(d0, d0, d0).iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (entity != null) {
                    if (entity instanceof Monster) {
                        ++i;
                    }

                    if (entity instanceof Player) {
                        Player player1 = (Player) entity;

                        if (class_71.method_397(player1) || class_71.method_398(player)) {
                            ++i;
                        }
                    }
                }
            }

            return i;
        }
    }

    public static boolean Ξ(Player player, Enums.HackType enums_hacktype, Entity entity) {
        return player != null && enums_hacktype != null ? (entity != null && entity instanceof LivingEntity && (entity.getVehicle() != null || !class_67.method_353(entity)) ? false : !class_38.method_157(player, enums_hacktype, true) && !class_67.method_363(player) && !player.isFlying()) : false;
    }
}
