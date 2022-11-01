import java.io.File;
import java.util.HashMap;
import org.bukkit.configuration.file.YamlConfiguration;

// $FF: renamed from: aF
public class class_5 {

    private static HashMap<String, String> Ξ;

    public static String Ξ() {
        if (class_5.field_7.containsKey("host")) {
            return (String) class_5.field_7.get("host");
        } else {
            File file = new File("plugins/Spartan/mysql.yml");

            if (!file.exists()) {
                method_24();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(file);

            if (yamlconfiguration == null) {
                return "";
            } else {
                String s = yamlconfiguration.getString("host");

                class_5.field_7.put("host", s);
                return s;
            }
        }
    }

    public static String Π() {
        if (class_5.field_7.containsKey("user")) {
            return (String) class_5.field_7.get("user");
        } else {
            File file = new File("plugins/Spartan/mysql.yml");

            if (!file.exists()) {
                method_24();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(file);

            if (yamlconfiguration == null) {
                return "";
            } else {
                String s = yamlconfiguration.getString("user");

                class_5.field_7.put("user", s);
                return s;
            }
        }
    }

    public static String HHΞ() {
        if (class_5.field_7.containsKey("password")) {
            return (String) class_5.field_7.get("password");
        } else {
            File file = new File("plugins/Spartan/mysql.yml");

            if (!file.exists()) {
                method_24();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(file);

            if (yamlconfiguration == null) {
                return "";
            } else {
                String s = yamlconfiguration.getString("password");

                class_5.field_7.put("password", s);
                return s;
            }
        }
    }

    public static String HΞ() {
        if (class_5.field_7.containsKey("database")) {
            return (String) class_5.field_7.get("database");
        } else {
            File file = new File("plugins/Spartan/mysql.yml");

            if (!file.exists()) {
                method_24();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(file);

            if (yamlconfiguration == null) {
                return "";
            } else {
                String s = yamlconfiguration.getString("database");

                class_5.field_7.put("database", s);
                return s;
            }
        }
    }

    public static String BΞ() {
        if (class_5.field_7.containsKey("table")) {
            return (String) class_5.field_7.get("table");
        } else {
            File file = new File("plugins/Spartan/mysql.yml");

            if (!file.exists()) {
                method_24();
            }

            YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(file);

            if (yamlconfiguration == null) {
                return "";
            } else {
                String s = yamlconfiguration.getString("table");

                class_5.field_7.put("table", s);
                return s;
            }
        }
    }

    public static void Ξ() {
        class_5.field_7.clear();
        class_4.method_14();
        File file = new File("plugins/Spartan/mysql.yml");

        class_79.method_444(file, "host", "");
        class_79.method_444(file, "user", "");
        class_79.method_444(file, "password", "");
        class_79.method_444(file, "database", "");
        class_79.method_444(file, "table", "spartan_logs");
    }

    static {
        class_5.field_7 = new HashMap();
    }
}
