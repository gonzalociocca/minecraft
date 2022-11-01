import org.bukkit.entity.Player;

// $FF: renamed from: bl
public class class_87 {

    public static int Ξ(Player player) {
        int i = ((Integer) class_91.method_490(player, "ping")).intValue();

        return i < 0 ? 0 : i;
    }
}
