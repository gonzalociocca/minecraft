import java.io.File;
import java.util.ArrayList;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

// $FF: renamed from: bh
public class class_79 {

    public static String Ξ(OfflinePlayer offlineplayer, String s, Enums.HackType enums_hacktype) {
        if (s == null) {
            return null;
        } else {
            String s1 = String.valueOf(class_97.method_502());
            String s2 = Register.field_249.getDescription().getVersion();
            String s3 = class_101.method_512().toString().substring(1).replace("_", ".");

            if (s1.length() > 5) {
                s1 = s1.substring(0, 5);
            }

            s = s.replace("{tps}", s1);
            s = s.replace("{plugin:version}", s2);
            s = s.replace("{server:version}", s3);
            s = s.replace("{date:time}", class_99.method_507(class_99.method_510(0, 0, 0)));
            s = s.replace("{date:d-m-y}", class_99.method_508(class_99.method_510(0, 0, 0)));
            s = s.replace("{date:m-d-y}", class_99.HHΞ(class_99.method_510(0, 0, 0)));
            s = s.replace("{date:y-m-d}", class_99.method_509(class_99.method_510(0, 0, 0)));
            if (offlineplayer != null) {
                s = s.replace("{player}", offlineplayer.getName());
                s = s.replace("{uuid}", offlineplayer.getUniqueId().toString());
                if (offlineplayer.isOnline()) {
                    s = s.replace("{ping}", String.valueOf(class_87.method_472((Player) offlineplayer)));
                    s = s.replace("{vls}", String.valueOf(class_38.method_155((Player) offlineplayer)));
                    s = s.replace("{world}", ((Player) offlineplayer).getWorld().getName());
                    s = s.replace("{health}", String.valueOf(((Player) offlineplayer).getHealth()));
                    s = s.replace("{gamemode}", ((Player) offlineplayer).getGameMode().toString().toLowerCase());
                    s = s.replace("{x}", String.valueOf(((Player) offlineplayer).getLocation().getX()));
                    s = s.replace("{y}", String.valueOf(((Player) offlineplayer).getLocation().getY()));
                    s = s.replace("{z}", String.valueOf(((Player) offlineplayer).getLocation().getZ()));
                    s = s.replace("{yaw}", String.valueOf(((Player) offlineplayer).getLocation().getYaw()));
                    s = s.replace("{pitch}", String.valueOf(((Player) offlineplayer).getLocation().getPitch()));
                    s = s.replace("{cps}", String.valueOf(class_124.method_597((Player) offlineplayer)));
                }
            }

            if (enums_hacktype != null) {
                s = s.replace("{detection}", enums_hacktype.toString());
                s = s.replace("{silent:detection}", String.valueOf(class_123.method_587(enums_hacktype)));
                if (offlineplayer.isOnline()) {
                    s = s.replace("{vls:detection}", String.valueOf(class_38.method_154((Player) offlineplayer, enums_hacktype)));
                }
            }

            s = s.replaceAll("&", "\u00a7");
            return s;
        }
    }

    public static void Ξ(boolean flag, ArrayList<Enums.HackType> arraylist, Enums.HackType enums_hacktype) {
        if (flag) {
            if (!arraylist.contains(enums_hacktype)) {
                arraylist.add(enums_hacktype);
            }
        } else if (arraylist.contains(enums_hacktype)) {
            arraylist.remove(enums_hacktype);
        }

    }

    public static void Ξ(File file, String s, Object object) {
        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception exception) {
                ;
            }
        }

        if (!yamlconfiguration.contains(s)) {
            method_445(file, s, object);
        }

    }

    public static void Π(File file, String s, Object object) {
        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(file);

        yamlconfiguration.set(s, object);

        try {
            yamlconfiguration.save(file);
        } catch (Exception exception) {
            ;
        }

    }

    public static void Ξ(String s, Object object) {
        if (!Register.field_249.getConfig().contains(s)) {
            Register.field_249.getConfig().set(s, object);
            Register.field_249.saveConfig();
            Register.field_249.reloadConfig();
        }

    }

    public static int Ξ(String s) {
        ArrayList arraylist = new ArrayList();
        File[] afile = (new File(s)).listFiles();

        if (afile != null) {
            File[] afile1 = afile;
            int i = afile.length;

            for (int j = 0; j < i; ++j) {
                File file = afile1[j];

                if (file.isFile()) {
                    arraylist.add(file.getName());
                }
            }
        }

        return arraylist.size();
    }
}
