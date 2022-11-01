import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

// $FF: renamed from: N
public class class_94 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;

    public class_94() {
        this.field_168 = Enums.HackType.KillAura;
        this.field_169 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_169.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        Player player = playerteleportevent.getPlayer();

        this.field_169.remove(player);
        class_36.method_134(player, this.field_168.toString() + "=modulus=teleport", 1);
    }

    @EventHandler
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_390(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            class_36.method_138(player, this.field_168.toString() + "=modulus=teleport");
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_168, entity) || !class_120.method_572("KillAura.check_modulo") || class_36.method_133(player, this.field_168.toString() + "=modulus=teleport") != 0 || class_67.method_358(player, entity)) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                Location location = player.getLocation();
                double d0 = (double) Math.abs(location.getPitch());
                double d1 = (double) Math.abs(location.getYaw());
                boolean flag = false;
                double d2 = d1 % 0.5D;

                if (d2 == 0.0D) {
                    class_38.method_159(player, this.field_168, "t: modulo(normal), yd: " + d2 + ", y: " + d1);
                    flag = true;
                }

                if (!flag && this.field_169.containsKey(player)) {
                    double d3 = Math.abs(d0 - ((Double) this.field_169.get(player)).doubleValue());
                    double d4 = d3 % 0.5D;

                    if (d3 >= 15.0D && d4 == 0.0D) {
                        class_38.method_159(player, this.field_168, "t: modulo(cached), pd: " + d4 + ", p: " + d3);
                        flag = true;
                    }
                }

                this.field_169.put(player, Double.valueOf(d0));
                if (flag && class_123.method_583(player, this.field_168)) {
                    entitydamagebyentityevent.setCancelled(true);
                }
            }
        }

    }
}
