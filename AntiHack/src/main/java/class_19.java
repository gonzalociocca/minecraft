import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// $FF: renamed from: aT
public class class_19 {

    public static void Ξ() {
        class_101 class_101 = class_101.method_512();

        if (class_101 != class_101.field_254 && class_101 != class_101.field_250 && class_101 != class_101.field_251 && class_101 != class_101.HHΞ && class_101 != class_101.field_252) {
            Iterator iterator = Bukkit.getOnlinePlayers().iterator();

            while (iterator.hasNext()) {
                Player player = (Player) iterator.next();

                if (class_55.method_297(player) && (class_66.method_339(player.getLocation()) || class_66.method_339(player.getLocation().add(0.3D, 0.0D, 0.0D)) || class_66.method_339(player.getLocation().add(-0.3D, 0.0D, 0.0D)) || class_66.method_339(player.getLocation().add(0.0D, 0.0D, 0.3D)) || class_66.method_339(player.getLocation().add(0.0D, 0.0D, -0.3D)) || class_66.method_339(player.getLocation().add(0.3D, 0.0D, 0.3D)) || class_66.method_339(player.getLocation().add(-0.3D, 0.0D, -0.3D)) || class_66.method_339(player.getLocation().add(-0.3D, 0.0D, 0.3D)) || class_66.method_339(player.getLocation().add(0.3D, 0.0D, -0.3D)))) {
                    class_39.method_170(player, "shulker-box=protection", 20);
                }
            }
        }

    }

    public static boolean Ξ(Player player) {
        return !class_39.method_169(player, "shulker-box=protection");
    }
}
