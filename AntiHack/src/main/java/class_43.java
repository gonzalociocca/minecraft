import java.io.File;
import org.bukkit.Bukkit;

// $FF: renamed from: bI
public class class_43 {

    private static int Ξ;
    private static File Ξ;

    public static void Ξ(String s, boolean flag) {
        if (s != null) {
            if (class_128.method_610("log_console")) {
                Bukkit.getConsoleSender().sendMessage(s);
            }

            if (flag) {
                class_4.method_19(s);
                if (class_128.method_610("log_file")) {
                    if (class_43.field_68 == null) {
                        int i = class_79.method_447("plugins/Spartan/logs/") + 1;
                        File file = new File("plugins/Spartan/logs/log" + i + ".yml");

                        if (!file.exists()) {
                            file.getParentFile().mkdirs();
                            class_43.field_68 = file;
                            class_43.field_67 = 0;
                        }
                    } else {
                        ++class_43.field_67;
                        String s1 = class_99.method_507(class_99.method_510(0, 0, 0));

                        class_79.method_445(class_43.field_68, "(" + class_43.field_67 + ")[" + s1 + "]", s);
                    }
                }
            }

        }
    }

    static {
        class_43.field_67 = 0;
        class_43.field_68 = null;
    }
}
