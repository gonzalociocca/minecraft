import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: am
public class class_52 implements Listener {

    private static Enums.HackType Ξ;
    private double Ξ;
    private double Π;
    private double HHΞ;
    private double HΞ;
    private double BΞ;
    private double BPPΠ;
    private double PΞ;
    private double OΠ;

    public class_52() {
        this.field_90 = 0.5D;
        this.field_91 = 0.78D;
        this.HHΞ = 0.75D;
        this.field_92 = 1.2D;
        this.field_93 = 0.6D;
        this.BPPΠ = 1.0D;
        this.field_94 = 0.68D;
        this.field_95 = 1.5D;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();
            Location location = playermoveevent.getTo();
            Location location1 = playermoveevent.getFrom();
            double d0 = location.getY() - location1.getY();

            if (this.method_281(player, d0)) {
                double d1 = class_89.method_475(location1, location);
                float f = class_71.method_396(player);

                f = f > 0.0F ? f + 0.2F : 0.0F;
                double d2 = location.getY() - (double) location.getBlockY();

                if (this.method_280(player.getLocation()) && this.method_280(player.getLocation().add(0.0D, 1.0D, 0.0D)) && this.method_277(player, d1, 0.0D) && this.method_277(player, d1, 1.0D)) {
                    double d3 = this.field_90;

                    if (!class_71.OOΞ(player) && !class_28.method_98(player)) {
                        if (!class_66.method_332(player.getLocation().add(0.0D, -1.0D, 0.0D)) && !class_66.method_352(player, 1)) {
                            if (class_17.method_65(player)) {
                                d3 = this.HHΞ;
                            } else if (class_66.BBBΠ(player.getLocation().add(0.0D, 2.0D, 0.0D))) {
                                d3 = this.field_91;
                            } else if (!this.method_279(player, d1, 0.0D) && !this.method_279(player, d1, 1.0D) && (!this.method_279(player, d1, -1.0D) || class_71.method_416(player.getLocation())) && !class_71.HHHΞ(player)) {
                                if (class_73.method_418(d0, 6) == 0.419999D || class_73.method_418(d2, 6) == 0.419999D) {
                                    d3 = this.field_94;
                                }
                            } else {
                                d3 = this.BPPΠ;
                            }
                        } else if (class_66.BBBΠ(player.getLocation().add(0.0D, 2.0D, 0.0D))) {
                            d3 = this.field_92;
                        } else if (class_17.method_65(player)) {
                            d3 = this.HHΞ;
                        } else if (!class_66.method_352(player, 2)) {
                            d3 = this.field_93;
                        }
                    } else {
                        d3 = this.field_95;
                    }

                    d3 += (double) f;
                    double d4 = class_41.method_178(class_69.method_385(player, d3, 4.0D, PotionEffectType.SPEED));

                    if (d1 >= d4) {
                        this.method_276(player, "ds: " + d1 + ", dm: " + d3 + ", dm_s: " + d4, location1);
                    }
                }

            }
        }
    }

    private void Ξ(Player player, String s, Location location) {
        if (class_39.method_169(player, class_52.field_89.toString() + "=vl=cooldown")) {
            class_38.method_159(player, class_52.field_89, s);
            class_39.method_170(player, class_52.field_89.toString() + "=vl=cooldown", 5);
        }

        if (class_123.method_583(player, class_52.field_89)) {
            class_69.method_386(player, location);
        }

    }

    private boolean Ξ(Player player, double d0, double d1) {
        return player == null ? false : this.method_278(player.getLocation().add(d0, d1, 0.0D)) || this.method_278(player.getLocation().add(-d0, d1, 0.0D)) || this.method_278(player.getLocation().add(0.0D, d1, d0)) || this.method_278(player.getLocation().add(0.0D, d1, -d0));
    }

    private boolean Ξ(Location location) {
        return class_66.BBBΠ(location) && !class_66.method_341(location);
    }

    private boolean Π(Player player, double d0, double d1) {
        return player == null ? false : !class_66.method_340(player.getLocation().add(d0, d1, 0.0D), false) && class_66.BBBΠ(player.getLocation().add(d0, d1, 0.0D)) && !class_66.method_341(player.getLocation().add(d0, d1, 0.0D)) || !class_66.method_340(player.getLocation().add(-d0, d1, 0.0D), false) && class_66.BBBΠ(player.getLocation().add(-d0, d1, 0.0D)) && !class_66.method_341(player.getLocation().add(-d0, d1, 0.0D)) || !class_66.method_340(player.getLocation().add(0.0D, d1, d0), false) && class_66.BBBΠ(player.getLocation().add(0.0D, d1, d0)) && !class_66.method_341(player.getLocation().add(0.0D, d1, d0)) || !class_66.method_340(player.getLocation().add(0.0D, d1, -d0), false) && class_66.BBBΠ(player.getLocation().add(0.0D, d1, -d0)) && !class_66.method_341(player.getLocation().add(0.0D, d1, -d0));
    }

    private boolean Π(Location location) {
        return location == null ? false : !class_66.BBBΠ(location) || class_66.method_337(location) || class_66.method_334(location) || location.getBlock().getType() == Material.getMaterial(101);
    }

    private boolean Ξ(Player player, double d0) {
        return class_55.method_299(player, class_52.field_89, class_120.method_572("Phase.check_when_flying")) && !class_13.method_53(player) && !class_10.method_40(player) && !class_71.BPΞ(player) && !class_71.method_400(player) && !class_69.method_374(player, d0);
    }

    static {
        class_52.field_89 = Enums.HackType.Phase;
    }
}
