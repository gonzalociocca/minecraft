import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: n
public class class_131 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;

    public class_131() {
        this.field_218 = Enums.HackType.Criticals;
        this.field_219 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_219.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        Player player = playerteleportevent.getPlayer();

        this.field_219.remove(player);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (!this.method_629(player)) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                boolean flag = this.method_626(player) || this.HHΞ(player) || this.method_627(player);
                boolean flag1 = class_123.method_583(player, this.field_218);

                if (flag && flag1) {
                    entitydamagebyentityevent.setCancelled(true);
                }
            }
        }

    }

    private boolean Ξ(Player player) {
        if (player != null && !player.isOnGround() && !player.hasPotionEffect(PotionEffectType.BLINDNESS) && player.getFallDistance() != 0.0F) {
            if (player.getLocation().getY() % 1.0D == 0.0D) {
                class_36.method_135(player, this.field_218.toString() + "=location=attempts", 150);
                if (class_36.method_133(player, this.field_218.toString() + "=location=attempts") >= 2) {
                    class_38.method_159(player, this.field_218, "t: location");
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private boolean Π(Player player) {
        if (player != null && this.method_628(player, 1.0D)) {
            float f = player.getFallDistance();

            if (f > 0.0F && f <= 0.07F && (class_71.method_416(player.getLocation().add(0.0D, (double) (-f), 0.0D)) || class_71.method_416(player.getLocation().add(0.0D, -((double) f + 0.1D), 0.0D)))) {
                class_38.method_159(player, this.field_218, "t: mini-jump, f: " + f);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean HHΞ(Player player) {
        if (player != null && (!player.isOnGround() || !class_71.method_416(player.getLocation())) && this.method_628(player, 0.3D) && !class_49.method_246(player) && class_71.method_413(player, PotionEffectType.JUMP) <= 250) {
            boolean flag = false;
            double d0 = player.getLocation().getY() - (double) player.getLocation().getBlockY();

            if (class_71.method_410(player) >= 20 && d0 >= 0.15D && d0 <= 0.75D && this.field_219.containsKey(player) && ((Double) this.field_219.get(player)).doubleValue() == d0) {
                class_38.method_159(player, this.field_218, "t: position, r: " + d0);
                flag = true;
            }

            this.field_219.put(player, Double.valueOf(d0));
            return flag;
        } else {
            return false;
        }
    }

    private boolean Ξ(Player player, double d0) {
        if (player != null && class_39.method_169(player, this.field_218.toString() + "=block_free=" + d0)) {
            byte b0 = 40;

            if (!class_71.method_416(player.getLocation())) {
                b0 = 60;
            }

            for (int i = 0; i <= 2; ++i) {
                if (!class_66.HHΞ(player, d0, (double) i, d0)) {
                    class_39.method_170(player, this.field_218.toString() + "=block_free=" + d0, b0);
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean HΞ(Player player) {
        if (player == null) {
            return false;
        } else {
            for (int i = -1; i <= 2; ++i) {
                if (!class_66.method_346(player, 1.0D, (double) i, 1.0D)) {
                    return false;
                }
            }

            return !class_38.method_157(player, this.field_218, true) && !class_71.method_400(player) && !class_71.HHHΞ(player) && !class_20.method_75(player) && (class_66.BBBΠ(player.getLocation().add(0.0D, -1.0D, 0.0D)) || !class_66.BBBΠ(player.getLocation().add(0.0D, -1.0D, 0.0D)) && class_66.BBBΠ(player.getLocation().add(0.0D, -2.0D, 0.0D)));
        }
    }
}
