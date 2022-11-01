import me.vagdedes.spartan.Register;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

// $FF: renamed from: aG
public class class_6 {

    public static final String Ξ = "%%__USER__%%";
    public static final String Π = "%%__NONCE__%%";
    private static final String HHΞ = "%%__RESOURCE__%%";
    private static final String HΞ = "http://vagdedes.me/spartan/verify.php?id=&nonce=";

    public static boolean Ξ(String s) {
        return s == null ? false : StringUtils.isNumeric(s) && !s.contains("-") && !s.equalsIgnoreCase("0") && s.length() > 0;
    }

    public static boolean Π(String s) {
        return s == null ? false : s.length() >= 8 && s.length() <= 12;
    }

    public static boolean Ξ() {
        boolean flag = class_103.method_514("http://vagdedes.me/spartan/verify.php?id=&nonce=") && method_25("252643") && method_25("25638") && method_26("662399249") && "25638".equalsIgnoreCase(String.valueOf(25638)) && Register.field_249.getDescription().getName().equalsIgnoreCase("Spartan") && Register.field_249.getDescription().getVersion().startsWith("Build " + Register.field_249.getDescription().getVersion().substring(6)) && Register.field_249.getDescription().getDescription().equalsIgnoreCase("An extraordinary cross-version cheat prevention you have always dreamed of.") && Register.field_249.getDescription().getWebsite().equalsIgnoreCase("http://www.spigotmc.org/resources/" + String.valueOf(25638)) && Register.field_249.getDescription().getAuthors().toString().equalsIgnoreCase("[Evangelos Dedes @Vagdedes]");

        if (flag) {
            try {
                byte b0 = 41;
                String s = "http://vagdedes.me/spartan/verify.php?id=&nonce=".substring(0, b0) + "252643" + "http://vagdedes.me/spartan/verify.php?id=&nonce=".substring(b0, "http://vagdedes.me/spartan/verify.php?id=&nonce=".length()) + "662399249";

                flag = class_107.method_521(s).equalsIgnoreCase(String.valueOf(true));
            } catch (Exception exception) {
                ;
            }
        }

        return flag;
    }

    public static void Ξ() {
        if (!method_27()) {
            Bukkit.getPluginManager().disablePlugin(Register.field_249);
        }

    }
}
