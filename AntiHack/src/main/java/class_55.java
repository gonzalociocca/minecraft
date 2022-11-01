import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.entity.Player;

// $FF: renamed from: ap
public class class_55 {

    public static boolean Ξ(Player player) {
        return player == null ? false : !class_71.PPPΠ(player) && !class_71.HHHΞ(player) && !class_22.method_82(player) && !class_38.method_158(player, true) && !player.isSleeping();
    }

    public static boolean Ξ(Player player, Enums.HackType enums_hacktype) {
        return player != null && enums_hacktype != null ? !class_38.method_157(player, enums_hacktype, true) && !class_71.PPPΠ(player) && !class_71.HHHΞ(player) && !class_22.method_82(player) : false;
    }

    public static boolean Ξ(Player player, Enums.HackType enums_hacktype, boolean flag) {
        return player != null && enums_hacktype != null ? !class_38.method_157(player, enums_hacktype, true) && !class_71.PPPΠ(player) && !class_22.method_82(player) && (!class_71.HHHΞ(player) || flag) : false;
    }
}
