import java.util.HashMap;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

// $FF: renamed from: ao
public class class_54 implements Listener {

    private Enums.HackType Ξ;
    private HashMap<String, Location> Ξ;

    public class_54() {
        this.field_112 = Enums.HackType.Sprint;
        this.field_113 = new HashMap();
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();
        String s = player.getName();

        this.field_113.remove(s + "=backwards");
        this.field_113.remove(s + "=sideways=1");
        this.field_113.remove(s + "=sideways=2");
        this.field_113.remove(s + "=diagonal=1");
        this.field_113.remove(s + "=diagonal=2");
    }

    @EventHandler
    private void Ξ(PlayerTeleportEvent playerteleportevent) {
        if (playerteleportevent.getCause() != TeleportCause.UNKNOWN) {
            Player player = playerteleportevent.getPlayer();

            if (!class_49.method_246(player)) {
                String s = player.getName();

                this.field_113.remove(s + "=backwards");
                this.field_113.remove(s + "=sideways=1");
                this.field_113.remove(s + "=sideways=2");
                this.field_113.remove(s + "=diagonal=1");
                this.field_113.remove(s + "=diagonal=2");
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerMoveEvent playermoveevent) {
        if (class_69.method_389(playermoveevent)) {
            Player player = playermoveevent.getPlayer();

            if (this.method_296(player)) {
                Location location = playermoveevent.getFrom();

                this.method_293(player, location);
                this.method_294(player, location);
            }
        }
    }

    private void Ξ(Player player, Location location) {
        if (player != null && location != null && class_120.method_572("Sprint.check_food_sprinting")) {
            if (!player.isSprinting() && class_71.method_408(player)) {
                class_36.method_134(player, this.field_112.toString() + "=sprint", class_36.method_133(player, this.field_112.toString() + "=sprint") + 1);
            } else {
                class_36.method_138(player, this.field_112.toString() + "=sprint");
            }

            if ((player.isSprinting() || class_71.method_408(player) && class_36.method_133(player, this.field_112.toString() + "=sprint") >= 10) && player.getFoodLevel() <= 6) {
                class_36.method_135(player, this.field_112.toString() + "=food=level", 15);
                if (class_36.method_133(player, this.field_112.toString() + "=food=level") >= 10) {
                    this.method_295(player, location, "t: food-level");
                }
            }

        }
    }

    private void Π(Player player, Location location) {
        if (player != null && location != null && !class_10.method_40(player) && !class_13.method_53(player) && !class_28.method_98(player) && class_120.method_572("Sprint.check_omnidirectional_sprinting") && !class_111.method_540(player) && (class_71.method_416(player.getLocation()) || player.isOnGround()) && class_71.method_396(player) == 0.0F) {
            Location location1 = player.getLocation();

            location1.setPitch(0.0F);
            float f = location1.getYaw();
            String s = player.getName();
            double d0 = class_41.method_178(0.06D);
            double d1 = class_69.method_385(player, 0.29D, 4.0D, PotionEffectType.SPEED);
            Location location2;
            Location location3;

            if (this.field_113.containsKey(s + "=backwards") || this.field_113.containsKey(s + "=sideways=1") || this.field_113.containsKey(s + "=sideways=2") || this.field_113.containsKey(s + "=diagonal=1") || this.field_113.containsKey(s + "=diagonal=2")) {
                location2 = (Location) this.field_113.get(s + "=backwards");
                double d2 = class_69.method_391(location2, location1);
                Location location4 = (Location) this.field_113.get(s + "=sideways=1");
                double d3 = class_69.method_391(location4, location1);

                location3 = (Location) this.field_113.get(s + "=sideways=2");
                double d4 = class_69.method_391(location3, location1);
                Location location5 = (Location) this.field_113.get(s + "=diagonal=1");
                double d5 = class_69.method_391(location5, location1);
                Location location6 = (Location) this.field_113.get(s + "=diagonal=2");
                double d6 = class_69.method_391(location6, location1);
                String s1 = d2 <= d0 ? "backwards" : (d3 > d0 && d4 > d0 ? (d5 > d0 && d6 > d0 ? null : "diagonal") : "sideways");

                if (s1 != null) {
                    class_36.method_135(player, this.field_112.toString() + "=illegal", 20);
                    if (class_36.method_133(player, this.field_112.toString() + "=illegal") == 12) {
                        this.method_295(player, location, "t: illegal, direction: " + s1);
                    }
                }
            }

            this.field_113.put(s + "=backwards", location1.clone().add(location1.getDirection().multiply(-d1)));
            location2 = location1.clone();
            float f1 = f + 90.0F;

            location2.setYaw(f1 <= 360.0F ? f1 : f1 - 360.0F);
            this.field_113.put(s + "=sideways=1", location2.add(location2.getDirection().multiply(d1)));
            Location location7 = location1.clone();
            float f2 = f - 90.0F;

            location7.setYaw(f2 >= 0.0F ? f2 : f2 + 360.0F);
            this.field_113.put(s + "=sideways=2", location7.add(location7.getDirection().multiply(d1)));
            Location location8 = location1.clone();
            float f3 = f + 135.0F;

            location8.setYaw(f3 <= 360.0F ? f3 : f3 - 360.0F);
            this.field_113.put(s + "=diagonal=1", location8.add(location8.getDirection().multiply(d1)));
            location3 = location1.clone();
            float f4 = f - 135.0F;

            location3.setYaw(f4 >= 0.0F ? f4 : f4 + 360.0F);
            this.field_113.put(s + "=diagonal=2", location3.add(location3.getDirection().multiply(d1)));
        }
    }

    private void Ξ(Player player, Location location, String s) {
        class_38.method_159(player, this.field_112, s);
        if (class_123.method_583(player, this.field_112)) {
            class_69.method_386(player, location);
        }

    }

    private boolean Ξ(Player player) {
        return class_55.method_298(player, this.field_112) && !class_71.method_400(player);
    }
}
