import me.vagdedes.spartan.Register;
import org.apache.commons.lang.StringUtils;

// $FF: renamed from: bt
public class class_103 {

    private static String Ξ;

    public static boolean Ξ(String s) {
        if (s != null && !StringUtils.isNumeric(s)) {
            if (s.length() == 48 && method_515(s) != null && method_515(s).length() == 11) {
                s = s.substring(7);
                return class_103.field_180.equals(s);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String Ξ(String s) {
        if (s != null) {
            s = s.replace("http://", "");

            for (int i = 0; i <= s.length(); ++i) {
                if (s.substring(i).startsWith("/")) {
                    return s.substring(0, i);
                }
            }
        }

        return null;
    }

    static {
        class_103.field_180 = "vagdedes.me/" + Register.field_249.getName().toLowerCase() + "/verify.php?id=&nonce=";
    }
}
