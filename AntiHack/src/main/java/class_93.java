import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

// $FF: renamed from: bo
public class class_93 {

    public static boolean Ξ(String s) {
        Plugin[] aplugin = Bukkit.getPluginManager().getPlugins();
        int i = aplugin.length;

        for (int j = 0; j < i; ++j) {
            Plugin plugin = aplugin[j];

            if (plugin.isEnabled() && plugin.getName().equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }
}
