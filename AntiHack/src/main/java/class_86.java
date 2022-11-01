import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: J
public class class_86 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, UUID> Ξ;

    public class_86() {
        this.field_160 = Enums.HackType.KillAura;
        this.field_161 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_161.remove(player);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();
            Entity entity = entitydamagebyentityevent.getEntity();

            if (!class_137.method_666(player, this.field_160, entity) || !class_120.method_572("KillAura.check_hit_time")) {
                return;
            }

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                for (int i = 0; i <= 2; ++i) {
                    if (!class_66.HHΞ(entity, 1.0D, (double) i, 1.0D)) {
                        this.field_161.remove(player);
                        return;
                    }
                }

                class_45.method_211(player, this.field_160.toString() + "=time_between_hits");
                class_36.method_138(player, this.field_160.toString() + "=hit_time");
                this.field_161.put(player, entity.getUniqueId());
            }
        }

    }

    private int Ξ(Player player) {
        if (player == null) {
            return 0;
        } else {
            int i = 0;

            if (class_45.method_209(player, this.field_160.toString() + "=time_between_hits")) {
                long j = class_45.method_210(player, this.field_160.toString() + "=time_between_hits");

                if (j <= 450L) {
                    class_36.method_135(player, this.field_160.toString() + "=hit_time", 10);
                }

                i = class_36.method_133(player, this.field_160.toString() + "=hit_time");
            }

            return i;
        }
    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (!class_38.method_157(player, this.field_160, true) && this.field_161.containsKey(player)) {
            if (playerinteractevent.getAction() == Action.LEFT_CLICK_AIR) {
                Iterator iterator = player.getNearbyEntities(3.8D, 3.8D, 3.8D).iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    if (entity != null && entity instanceof LivingEntity && this.field_161.get(player) == entity.getUniqueId()) {
                        double d0 = class_89.method_475(player.getLocation(), entity.getLocation());
                        double d1 = class_67.method_356(player, entity);
                        double d2 = class_67.method_357(player, entity);

                        if (d0 >= 1.5D && d1 <= 27.5D && d2 <= 1.5D) {
                            int i = this.method_470(player);

                            if (i >= 5) {
                                class_36.method_135(player, this.field_160.toString() + "=hit_time=attempts", 100);
                                if (class_36.method_133(player, this.field_160.toString() + "=hit_time=attempts") == 22) {
                                    class_92.method_493(player);
                                    this.field_161.remove(player);
                                    class_36.method_138(player, this.field_160.toString() + "=hit_time=attempts");
                                    class_38.method_159(player, this.field_160, "t; hit time, tm: " + i + ", e: " + entity.getType().toString().toLowerCase());
                                    if (class_123.method_583(player, this.field_160)) {
                                        playerinteractevent.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
