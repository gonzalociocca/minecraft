import java.util.HashMap;
import java.util.UUID;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: ae
public class class_35 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<Player, Double> Ξ;
    private HashMap<Player, UUID> Π;
    private double Ξ;
    private double Π;
    private double HHΞ;
    private double HΞ;

    public class_35() {
        this.field_35 = Enums.HackType.EntityMove;
        this.field_36 = new HashMap();
        this.field_37 = new HashMap();
        this.field_38 = 1.0D;
        this.field_39 = 0.25D;
        this.HHΞ = 0.3D;
        this.field_40 = 0.15D;
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        this.field_37.remove(player);
        this.field_36.remove(player);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();
            Entity entity = player.getVehicle();

            if (!this.method_130(player, entity)) {
                class_39.method_170(player, this.field_35.toString() + "=distance=protection", 40);
                this.field_37.remove(player);
                this.field_36.remove(player);
            } else {
                Location location = playermoveevent.getTo();
                Location location1 = playermoveevent.getFrom();
                double d0 = location.getY() - location1.getY();

                if (!this.field_37.containsKey(player) || this.field_37.containsKey(player) && entity.getUniqueId() != this.field_37.get(player)) {
                    class_39.method_170(player, this.field_35.toString() + "=distance=protection", 40);
                }

                this.field_37.put(player, entity.getUniqueId());
                if (class_39.method_169(player, this.field_35.toString() + "=distance=protection") && this.field_36.containsKey(player)) {
                    double d1 = 0.0D;
                    double d2 = class_89.method_475(location, location1);
                    double d3 = ((Double) this.field_36.get(player)).doubleValue();
                    double d4 = Math.abs(d3 - d0);
                    int i;

                    if (!class_71.method_416(entity.getLocation()) && !class_71.method_416(entity.getLocation().add(0.0D, -1.0D, 0.0D))) {
                        i = class_36.method_133(player, this.field_35.toString() + "=air_ticks") + 1;
                        class_36.method_134(player, this.field_35.toString() + "=air_ticks", i);
                    } else {
                        class_36.method_138(player, this.field_35.toString() + "=air_ticks");
                    }

                    i = class_36.method_133(player, this.field_35.toString() + "=air_ticks");
                    if (d0 >= 2.0D) {
                        this.method_129(player, location1, "t: ypos(instant), dy: " + d0, entity);
                        this.method_128(player, d0);
                    } else {
                        boolean flag = class_71.method_416(entity.getLocation()) || class_71.method_416(entity.getLocation().add(0.0D, -1.0D, 0.0D));

                        if (entity instanceof Horse) {
                            if (((Horse) entity).getInventory().contains(Material.SADDLE)) {
                                d1 = this.field_38;
                            } else {
                                d1 = this.field_39;
                            }

                            if (!flag) {
                                if (d0 >= 1.0D) {
                                    this.method_129(player, location1, "t: ypos(normal), dy: " + d0, entity);
                                } else if (d0 > 0.0D && i >= 20) {
                                    this.method_129(player, location1, "t: ypos(air), dy: " + d0 + ", air: " + i, entity);
                                } else if (d4 >= 0.15D) {
                                    this.method_129(player, location1, "t: ypos(diff), dy: " + d0 + ", diff: " + d4, entity);
                                } else if (d0 >= d3 && d0 != 0.0D && d3 != 0.0D) {
                                    this.method_129(player, location1, "t: ypos(illegal), dy: " + d0 + ", cy: " + d3, entity);
                                }
                            }
                        } else if (entity instanceof Pig) {
                            if (player.getItemInHand().getType() == Material.CARROT_STICK) {
                                d1 = this.HHΞ;
                            } else {
                                d1 = this.field_40;
                            }

                            if (!flag) {
                                if (d0 >= 0.1D) {
                                    this.method_129(player, location1, "t: ypos(normal), dy: " + d0, entity);
                                } else if (d0 > 0.0D && i >= 12) {
                                    this.method_129(player, location1, "t: ypos(air), dy: " + d0 + ", air: " + i, entity);
                                } else if (d4 >= 0.1D) {
                                    this.method_129(player, location1, "t: ypos(diff), dy: " + d0 + ", diff: " + d4, entity);
                                } else if (d0 >= d3 && d0 != 0.0D && d3 != 0.0D) {
                                    this.method_129(player, location1, "t: ypos(illegal), dy: " + d0 + ", cy: " + d3, entity);
                                }
                            }
                        }

                        double d5 = class_41.method_178(class_69.method_385((LivingEntity) entity, d1, 3.0D, PotionEffectType.SPEED));

                        if (d1 > 0.0D && d2 >= d5) {
                            this.method_129(player, location1, "t: speed, ds: " + d2 + ", dm: " + d1 + ", dm_s: " + d5, entity);
                        }

                        this.method_128(player, d0);
                    }
                } else {
                    this.method_128(player, d0);
                }
            }
        }
    }

    private void Ξ(Player player, double d0) {
        this.field_36.put(player, Double.valueOf(d0));
    }

    private void Ξ(Player player, Location location, String s, Entity entity) {
        class_38.method_159(player, this.field_35, "e: " + entity.getType().toString().toLowerCase() + ", " + s);
        if (class_123.method_583(player, this.field_35)) {
            class_69.method_386(player, location);
            class_69.HHΞ(player);
        }

    }

    private boolean Ξ(Player player, Entity entity) {
        if (player != null && entity != null && entity == player.getVehicle() && (entity instanceof Pig || entity instanceof Horse)) {
            if (((LivingEntity) entity).hasPotionEffect(PotionEffectType.LEVITATION)) {
                class_38.method_160(player, this.field_35, 40);
                return false;
            } else {
                return class_55.method_298(player, this.field_35) && !class_13.method_53(player) && !class_17.method_65(player) && !class_20.method_75(player) && !((LivingEntity) entity).isLeashed();
            }
        } else {
            return false;
        }
    }
}
