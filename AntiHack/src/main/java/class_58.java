import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: as
public class class_58 implements Listener {

    private static Enums.HackType Ξ;
    private static HashMap<Player, Double> Ξ;

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_58.field_120.remove(player);
    }

    private long Ξ(Player player) {
        long i = -1L;

        if (player.hasPotionEffect(PotionEffectType.REGENERATION) && player.getFireTicks() <= 0) {
            int j = class_71.method_413(player, PotionEffectType.REGENERATION);

            if (j <= 9) {
                i = j == 0 ? 300L : 45L;
            }
        } else if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            if (class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_251) {
                i = 450L;
            } else {
                i = 3600L;
            }
        }

        return i;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(EntityRegainHealthEvent entityregainhealthevent) {
        if (entityregainhealthevent.getEntity() instanceof Player && !entityregainhealthevent.isCancelled()) {
            Player player = (Player) entityregainhealthevent.getEntity();

            if (!this.method_310(player) || entityregainhealthevent.getRegainReason() == RegainReason.CUSTOM || entityregainhealthevent.getRegainReason() == RegainReason.MAGIC) {
                return;
            }

            if (player.getFoodLevel() <= 17 && !player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                class_38.method_159(player, class_58.field_119, "t: illegal, fl: " + player.getFoodLevel());
                if (!class_123.method_587(class_58.field_119)) {
                    entityregainhealthevent.setCancelled(true);
                }

                return;
            }

            if (class_45.method_209(player, class_58.field_119.toString() + "=time") && class_58.field_120.containsKey(player) && ((Double) class_58.field_120.get(player)).doubleValue() != player.getHealth()) {
                long i = class_45.method_210(player, class_58.field_119.toString() + "=time");
                long j = this.method_308(player);

                if (i < j) {
                    if (class_123.method_583(player, class_58.field_119)) {
                        entityregainhealthevent.setCancelled(true);
                    }

                    class_36.method_135(player, class_58.field_119.toString() + "=attempts", 10);
                    if (class_36.method_133(player, class_58.field_119.toString() + "=attempts") >= 2) {
                        class_38.method_159(player, class_58.field_119, "t: unusual, ms: " + i + ", l: " + j);
                    }

                    return;
                }
            }

            class_45.method_211(player, class_58.field_119.toString() + "=time");
            class_58.field_120.put(player, Double.valueOf(player.getHealth()));
        }

    }

    private boolean Ξ(Player player) {
        return !class_38.method_157(player, class_58.field_119, true) && player.getWorld().getDifficulty() != Difficulty.PEACEFUL;
    }

    static {
        class_58.field_119 = Enums.HackType.FastHeal;
        class_58.field_120 = new HashMap();
    }
}
