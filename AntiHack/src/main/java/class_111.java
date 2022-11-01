import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import com.shampaggon.crackshot.events.WeaponScopeEvent;
import com.shampaggon.crackshot.events.WeaponShootEvent;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

// $FF: renamed from: bx
public class class_111 implements Listener {

    private static boolean Ξ;

    public static void Ξ() {
        if (!class_111.field_188 && class_93.method_494("crackshot")) {
            Bukkit.getPluginManager().registerEvents(new class_111(), Register.field_249);
            class_111.field_188 = true;
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(WeaponScopeEvent weaponscopeevent) {
        Player player = weaponscopeevent.getPlayer();

        if (!weaponscopeevent.isCancelled()) {
            class_9.method_37(player, 20);
            if (weaponscopeevent.isZoomIn()) {
                class_36.method_134(player, "crackshot=compatibility=scope", 1);
            } else {
                class_36.method_138(player, "crackshot=compatibility=scope");
            }
        } else {
            class_36.method_138(player, "crackshot=compatibility=scope");
        }

    }

    @EventHandler
    private void Ξ(WeaponPreShootEvent weaponpreshootevent) {
        Player player = weaponpreshootevent.getPlayer();

        class_38.method_160(player, Enums.HackType.KillAura, 40);
        class_9.method_37(player, 40);
    }

    @EventHandler
    private void Ξ(WeaponShootEvent weaponshootevent) {
        Player player = weaponshootevent.getPlayer();

        class_38.method_160(player, Enums.HackType.KillAura, 40);
        class_9.method_37(player, 40);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(WeaponDamageEntityEvent weapondamageentityevent) {
        if (!weapondamageentityevent.isCancelled()) {
            Entity entity = weapondamageentityevent.getVictim();

            if (entity instanceof Player) {
                class_9.method_37((Player) entity, 60);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageEvent entitydamageevent) {
        if (!entitydamageevent.isCancelled()) {
            Entity entity = entitydamageevent.getEntity();

            if (entity instanceof Player && method_540((Player) entity)) {
                class_9.method_37((Player) entity, 60);
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (!entitydamagebyentityevent.isCancelled()) {
            Entity entity = entitydamagebyentityevent.getDamager();

            if (entity instanceof Player && method_540((Player) entity)) {
                class_9.method_37((Player) entity, 30);
            }
        }

    }

    public static boolean Ξ(Player player) {
        return class_36.method_133(player, "crackshot=compatibility=scope") != 0;
    }

    static {
        class_111.field_188 = false;
    }
}
