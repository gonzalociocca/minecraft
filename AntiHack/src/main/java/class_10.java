import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// $FF: renamed from: aK
public class class_10 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Π(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (entitydamagebyentityevent.getEntity() instanceof Player && !entitydamagebyentityevent.isCancelled()) {
            Player player = (Player) entitydamagebyentityevent.getEntity();

            if (entitydamagebyentityevent.getCause() == DamageCause.ENTITY_ATTACK) {
                Entity entity = entitydamagebyentityevent.getDamager();

                if (entity instanceof EnderDragon) {
                    class_22.method_83(player, 40);
                } else if (entity instanceof Player) {
                    Player player1 = (Player) entity;

                    if (player1.getItemInHand().containsEnchantment(Enchantment.KNOCKBACK)) {
                        class_22.method_83(player, 60);
                    } else {
                        method_42(player, 40);
                    }
                } else {
                    if (entity instanceof IronGolem || entity instanceof Golem) {
                        class_39.method_170(player, "golem=protection", 40);
                    }

                    method_42(player, 30);
                }
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void Ξ(EntityDamageByEntityEvent entitydamagebyentityevent) {
        if (!entitydamagebyentityevent.isCancelled()) {
            DamageCause damagecause = entitydamagebyentityevent.getCause();
            boolean flag = class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_251 && class_101.method_512() != class_101.HHΞ && class_101.method_512() != class_101.field_252;
            Player player;

            if (flag && entitydamagebyentityevent.getDamager() instanceof Player && damagecause == DamageCause.ENTITY_ATTACK && (entitydamagebyentityevent.getEntity() instanceof Guardian || entitydamagebyentityevent.getEntity() instanceof ElderGuardian)) {
                player = (Player) entitydamagebyentityevent.getDamager();
                method_42(player, 40);
            } else if (entitydamagebyentityevent.getDamager() instanceof Projectile && entitydamagebyentityevent.getEntity() instanceof Player) {
                player = (Player) entitydamagebyentityevent.getEntity();
                if (damagecause != DamageCause.ENTITY_EXPLOSION && damagecause != DamageCause.BLOCK_EXPLOSION) {
                    Projectile projectile = (Projectile) entitydamagebyentityevent.getDamager();

                    if (projectile.getShooter() instanceof Player) {
                        Player player1 = (Player) projectile.getShooter();

                        if (player1.getItemInHand().containsEnchantment(Enchantment.ARROW_KNOCKBACK)) {
                            boolean flag1 = true;

                            if (player1.equals(player)) {
                                if (!class_71.method_416(player.getLocation()) && !class_71.method_416(player.getLocation().add(0.0D, -1.0D, 0.0D))) {
                                    int i = class_36.method_133(player, "damage=protection=projectile") + 1;

                                    class_36.method_134(player, "damage=protection=projectile", i);
                                    if (i >= 5) {
                                        flag1 = false;
                                    }
                                } else {
                                    class_36.method_138(player, "damage=protection=projectile");
                                }
                            }

                            if (flag1) {
                                method_42(player, 60);
                                class_22.method_83(player, 60);
                                class_8.method_33(player, 100);
                            }
                        } else if (!player1.equals(player)) {
                            method_42(player, 40);
                        } else {
                            class_18.method_68(player, 30);
                        }
                    } else {
                        method_42(player, 30);
                    }
                }
            }
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "damage=protection");
    }

    public static boolean Π(Player player) {
        return !class_39.method_169(player, "golem=protection");
    }

    public static void Ξ(Player player, int i) {
        class_39.method_170(player, "damage=protection", i);
    }
}
