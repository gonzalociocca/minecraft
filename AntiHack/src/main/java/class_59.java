import java.util.ArrayList;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: at
public class class_59 implements Listener {

    private int Ξ;
    private Enums.HackType Ξ;
    private ArrayList<Player> Ξ;

    public class_59() {
        this.field_121 = 600;
        this.field_122 = Enums.HackType.NoSwing;
        this.field_123 = new ArrayList();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_123.remove(player);
    }

    @EventHandler
    private void Ξ(PlayerAnimationEvent playeranimationevent) {
        Player player = playeranimationevent.getPlayer();

        if (class_38.method_157(player, this.field_122, true)) {
            this.field_123.remove(player);
        } else {
            if (playeranimationevent.getAnimationType() == PlayerAnimationType.ARM_SWING && !this.field_123.contains(player)) {
                this.field_123.add(player);
            }

        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getDamager() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getDamager();

            if (class_38.method_157(player, this.field_122, true) || class_71.method_414(player, 6.0D) > 3 || class_67.method_363(player) || class_34.method_125(player) || class_34.HHΞ(player)) {
                this.field_123.remove(player);
                return;
            }

            Entity entity = entitydamagebyentityevent.getEntity();

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                if (!this.field_123.contains(player)) {
                    if (class_39.method_169(player, this.field_122.toString() + "=first")) {
                        class_36.method_138(player, this.field_122.toString() + "=damage");
                        class_39.method_170(player, this.field_122.toString() + "=first", this.field_121);
                        return;
                    }

                    class_36.method_135(player, this.field_122.toString() + "=damage", this.field_121);
                    if (class_36.method_133(player, this.field_122.toString() + "=damage") >= 2) {
                        class_38.method_159(player, this.field_122, "t: damage, e: " + entity.getType().toString().toLowerCase().replace("_", "-"));
                    }

                    if (class_123.method_583(player, this.field_122)) {
                        entitydamagebyentityevent.setCancelled(true);
                    }
                } else {
                    this.field_123.remove(player);
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
            Block block = blockbreakevent.getBlock();

            if (!class_38.method_157(player, this.field_122, true) && !class_30.method_106(block)) {
                if (class_45.method_209(player, this.field_122.toString() + "=break=protection")) {
                    long i = class_45.method_210(player, this.field_122.toString() + "=break=protection");

                    if (i <= 50L) {
                        return;
                    }
                }

                if (!this.field_123.contains(player)) {
                    class_38.method_159(player, this.field_122, "t: break, b: " + class_66.method_343(block));
                    if (class_123.method_583(player, this.field_122)) {
                        blockbreakevent.setCancelled(true);
                    }
                } else {
                    this.field_123.remove(player);
                }

            } else {
                this.field_123.remove(player);
            }
        }
    }

    @EventHandler
    private void Ξ(PlayerInteractEvent playerinteractevent) {
        Player player = playerinteractevent.getPlayer();

        if (!class_38.method_157(player, this.field_122, true)) {
            Action action = playerinteractevent.getAction();

            if (action == Action.LEFT_CLICK_BLOCK) {
                class_45.method_211(player, this.field_122.toString() + "=break=protection");
            }

        }
    }
}
