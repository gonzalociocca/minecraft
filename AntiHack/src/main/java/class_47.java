import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// $FF: renamed from: bK
public class class_47 {

    private static HashMap<Player, ArrayList<Enums.Permission>> Ξ;
    private static HashMap<Player, ArrayList<Enums.HackType>> Π;
    private static int Ξ;
    private static int Π;

    public static void Ξ(boolean flag) {
        if (class_47.field_80 > 0 && !flag) {
            --class_47.field_80;
        } else {
            class_47.field_80 = class_47.field_79;
            method_223();
        }

    }

    public static void Ξ() {
        if (class_128.method_610("cache_permissions")) {
            Iterator iterator = Bukkit.getOnlinePlayers().iterator();

            while (iterator.hasNext()) {
                Player player = (Player) iterator.next();

                method_224(player);
            }
        }

    }

    public static void Ξ(Player player) {
        if (player != null && class_128.method_610("cache_permissions")) {
            Enums.Permission[] aenums_permission = Enums.Permission.values();
            int i = aenums_permission.length;

            int j;

            for (j = 0; j < i; ++j) {
                Enums.Permission enums_permission = aenums_permission[j];

                if (!player.hasPermission("spartan.admin") && !player.hasPermission("spartan.*") && !player.hasPermission("spartan." + enums_permission)) {
                    method_226(player, enums_permission);
                } else {
                    method_225(player, enums_permission);
                }
            }

            Enums.HackType[] aenums_hacktype = Enums.HackType.values();

            i = aenums_hacktype.length;

            for (j = 0; j < i; ++j) {
                Enums.HackType enums_hacktype = aenums_hacktype[j];

                if (player.hasPermission("spartan.bypass." + enums_hacktype.toString().toLowerCase())) {
                    method_227(player, enums_hacktype);
                } else {
                    method_228(player, enums_hacktype);
                }
            }

        }
    }

    private static void Ξ(Player player, Enums.Permission enums_permission) {
        if (player != null && enums_permission != null) {
            if (!class_47.field_77.containsKey(player)) {
                class_47.field_77.put(player, new ArrayList());
            }

            if (class_47.field_77.containsKey(player)) {
                ArrayList arraylist = (ArrayList) class_47.field_77.get(player);

                if (!arraylist.contains(enums_permission)) {
                    arraylist.add(enums_permission);
                }

                class_47.field_77.put(player, arraylist);
            }

        }
    }

    private static void Π(Player player, Enums.Permission enums_permission) {
        if (player != null && enums_permission != null && class_47.field_77.containsKey(player)) {
            ArrayList arraylist = (ArrayList) class_47.field_77.get(player);

            arraylist.remove(enums_permission);
            class_47.field_77.put(player, arraylist);
        }
    }

    private static void Ξ(Player player, Enums.HackType enums_hacktype) {
        if (player != null && enums_hacktype != null) {
            if (!class_47.field_78.containsKey(player)) {
                class_47.field_78.put(player, new ArrayList());
            }

            if (class_47.field_78.containsKey(player)) {
                ArrayList arraylist = (ArrayList) class_47.field_78.get(player);

                if (!arraylist.contains(enums_hacktype)) {
                    arraylist.add(enums_hacktype);
                }

                class_47.field_78.put(player, arraylist);
            }

        }
    }

    private static void Π(Player player, Enums.HackType enums_hacktype) {
        if (player != null && enums_hacktype != null && class_47.field_78.containsKey(player)) {
            ArrayList arraylist = (ArrayList) class_47.field_78.get(player);

            arraylist.remove(enums_hacktype);
            class_47.field_78.put(player, arraylist);
        }
    }

    public static boolean Ξ(Player player, Enums.Permission enums_permission) {
        return player != null && enums_permission != null ? !class_128.method_610("enable_permissions") || class_128.method_610("cache_permissions") && class_47.field_77.containsKey(player) && ((ArrayList) class_47.field_77.get(player)).contains(enums_permission) || !class_128.method_610("cache_permissions") && player.hasPermission("spartan." + enums_permission.toString().toLowerCase()) : false;
    }

    public static boolean Ξ(Player player, Enums.HackType enums_hacktype) {
        return player != null && enums_hacktype != null ? !class_128.method_610("enable_permissions") || class_128.method_610("cache_permissions") && class_47.field_78.containsKey(player) && ((ArrayList) class_47.field_78.get(player)).contains(enums_hacktype) || !class_128.method_610("cache_permissions") && player.hasPermission("spartan.bypass." + enums_hacktype.toString().toLowerCase()) : false;
    }

    static {
        class_47.field_77 = new HashMap();
        class_47.field_78 = new HashMap();
        class_47.field_79 = 600;
        class_47.field_80 = class_47.field_79;
    }
}
