import org.bukkit.Material;
import org.bukkit.block.Block;

// $FF: renamed from: bC
public class class_30 {

    public static boolean Ξ() {
        return class_93.method_494("treefeller");
    }

    public static boolean Ξ(Block block) {
        return block == null ? false : method_105() && (block.getType() == Material.LOG || block.getType() == Material.LOG_2);
    }
}
