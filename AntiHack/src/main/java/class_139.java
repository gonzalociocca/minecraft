import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: v
public class class_139 implements Listener {

    private static HashMap<Player, Location> Ξ;

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_139.field_234.remove(player);
    }

    public static Location Ξ(Player player) {
        return !class_139.field_234.containsKey(player) ? null : (Location) class_139.field_234.get(player);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                class_139.field_234.put(player, player.getLocation());
            }
        }

    }

    static {
        class_139.field_234 = new HashMap();
    }
}
