import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

// $FF: renamed from: by
public class class_113 {

    private static double Ξ;

    public static boolean Ξ() {
        return class_93.method_494("craftbook");
    }

    public static void Ξ() {
        class_113.field_190 = 0.0D;
    }

    public static double Ξ() {
        if (!method_542()) {
            return 0.0D;
        } else if (class_113.field_190 > 0.0D) {
            return class_113.field_190;
        } else {
            File file = new File("plugins/CraftBook/mechanisms.yml");

            if (file.exists()) {
                YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(file);

                if (yamlconfiguration != null) {
                    double d0 = yamlconfiguration.getDouble("mechanics.BoatSpeedModifiers.max-speed");

                    if (d0 < 0.0D) {
                        d0 = 0.1D;
                    } else {
                        d0 += 0.1D;
                    }

                    class_113.field_190 = d0;
                    return d0;
                }
            }

            return 0.0D;
        }
    }

    static {
        class_113.field_190 = 0.0D;
    }
}
