import com.gmail.nossr50.api.AbilityAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

// $FF: renamed from: bE
public class class_34 {

    public static boolean Ξ() {
        return class_93.method_494("mcmmo");
    }

    public static boolean Ξ(Player player) {
        if (player != null && method_123()) {
            try {
                boolean flag = AbilityAPI.isAnyAbilityEnabled(player);

                return flag;
            } catch (Exception exception) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean Π(Player player) {
        if (player != null && method_123()) {
            Material material = player.getItemInHand().getType();

            return material == null ? false : class_71.method_414(player, 6.0D) > 0 && (class_71.method_397(player) || class_71.method_398(player)) && (material == Material.DIAMOND_AXE || material == Material.GOLD_AXE || material == Material.IRON_AXE || material == Material.STONE_AXE || material == Material.WOOD_AXE);
        } else {
            return false;
        }
    }

    public static boolean HHΞ(Player player) {
        return !method_123() ? false : (player == null ? false : player.getItemInHand() == null);
    }
}
