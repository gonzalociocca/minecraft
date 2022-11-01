import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

// $FF: renamed from: aw
public class class_62 implements Listener {

    private Enums.HackType Ξ;

    public class_62() {
        this.field_127 = Enums.HackType.FastPlace;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockPlaceEvent blockplaceevent) {
        if (!blockplaceevent.isCancelled()) {
            Player player = blockplaceevent.getPlayer();
            Block block = blockplaceevent.getBlock();

            if (!class_38.method_157(player, this.field_127, true) && block.getType() != Material.FIRE && !class_71.HHHΞ(player)) {
                if (!class_39.method_169(player, this.field_127.toString() + "=cooldown")) {
                    if (class_123.method_583(player, this.field_127)) {
                        blockplaceevent.setCancelled(true);
                    }
                } else {
                    if (class_45.method_209(player, this.field_127.toString() + "=time")) {
                        boolean flag = false;
                        long i = class_45.method_210(player, this.field_127.toString() + "=time");

                        if (i <= class_41.method_180(50L)) {
                            class_36.method_135(player, this.field_127.toString() + "=fast", 20);
                            if (class_36.method_133(player, this.field_127.toString() + "=fast") >= 5) {
                                class_38.method_159(player, this.field_127, "t: fast, ms: " + i + ", b: " + class_66.method_343(block));
                                flag = true;
                            }
                        } else if (i <= class_41.method_180(100L)) {
                            class_36.method_135(player, this.field_127.toString() + "=medium", 20);
                            if (class_36.method_133(player, this.field_127.toString() + "=medium") >= 6) {
                                class_38.method_159(player, this.field_127, "t: medium, ms: " + i + ", b: " + class_66.method_343(block));
                                flag = true;
                            }
                        } else if (i <= class_41.method_180(150L)) {
                            class_36.method_135(player, this.field_127.toString() + "=slow", 20);
                            if (class_36.method_133(player, this.field_127.toString() + "=slow") >= 8) {
                                class_38.method_159(player, this.field_127, "t: slow, ms: " + i + ", b: " + class_66.method_343(block));
                                flag = true;
                            }
                        }

                        if (flag && class_123.method_583(player, this.field_127)) {
                            int j = class_120.method_571("FastPlace.cancel_seconds");

                            j = j > 60 ? 60 : j;
                            blockplaceevent.setCancelled(true);
                            class_39.method_170(player, this.field_127.toString() + "=cooldown", j * 20);
                        }
                    }

                    class_45.method_211(player, this.field_127.toString() + "=time");
                }

            }
        }
    }
}
