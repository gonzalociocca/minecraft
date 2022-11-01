import java.util.ArrayList;
import java.util.HashMap;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.api.CheckSilentToggleEvent;
import me.vagdedes.spartan.api.CheckToggleEvent;
import me.vagdedes.spartan.api.PlayerViolationCommandEvent;
import me.vagdedes.spartan.api.SpartanReloadEvent;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// $FF: renamed from: f
public class class_123 {

    private static ArrayList<Enums.HackType> Ξ;
    private static ArrayList<Enums.HackType> Π;
    private static HashMap<Enums.HackType, Integer> Ξ;
    private static HashMap<Enums.HackType, Integer> Π;
    private static HashMap<Enums.HackType, HashMap<Integer, ArrayList<String>>> HHΞ;
    String Ξ;

    public class_123() {
        this.field_207 = "252643";
    }

    public static String[] Ξ(Enums.HackType enums_hacktype, int i) {
        if (enums_hacktype != null && i >= 0) {
            if (class_123.HHΞ.containsKey(enums_hacktype) && ((HashMap) class_123.HHΞ.get(enums_hacktype)).containsKey(Integer.valueOf(i))) {
                return (String[]) ((ArrayList) ((HashMap) class_123.HHΞ.get(enums_hacktype)).get(Integer.valueOf(i))).toArray(new String[0]);
            } else {
                ArrayList arraylist = new ArrayList();

                if (Register.field_249.getConfig().contains(enums_hacktype.toString() + ".punishments." + i)) {
                    for (int j = 1; j <= class_38.method_151(); ++j) {
                        if (Register.field_249.getConfig().contains(enums_hacktype.toString() + ".punishments." + i + "." + j)) {
                            String s = Register.field_249.getConfig().getString(enums_hacktype.toString() + ".punishments." + i + "." + j);

                            arraylist.add(s);
                            if (!class_123.HHΞ.containsKey(enums_hacktype)) {
                                class_123.HHΞ.put(enums_hacktype, new HashMap());
                            }

                            if (class_123.HHΞ.containsKey(enums_hacktype)) {
                                HashMap hashmap = (HashMap) class_123.HHΞ.get(enums_hacktype);

                                hashmap.put(Integer.valueOf(i), arraylist);
                                class_123.HHΞ.put(enums_hacktype, hashmap);
                            }
                        }
                    }
                }

                return (String[]) arraylist.toArray(new String[0]);
            }
        } else {
            return null;
        }
    }

    public static int Ξ(Player player, Enums.HackType enums_hacktype) {
        if (player != null && enums_hacktype != null) {
            int i = 0;

            for (int j = 1; j <= class_38.method_150(); ++j) {
                String[] astring = method_578(enums_hacktype, j);
                int k = astring.length;

                for (int l = 0; l < k; ++l) {
                    String s = astring[l];

                    if (s != null) {
                        i = j;
                    }
                }
            }

            return i;
        } else {
            return 0;
        }
    }

    public static void Ξ(Player player, boolean flag) {
        SpartanReloadEvent spartanreloadevent = new SpartanReloadEvent();

        Bukkit.getPluginManager().callEvent(spartanreloadevent);
        if (!spartanreloadevent.isCancelled()) {
            String s = class_125.method_599("config_reload");

            if (player != null) {
                player.sendMessage(s);
            } else if (flag) {
                Bukkit.getConsoleSender().sendMessage(s);
            }

            Bukkit.getScheduler().cancelTask(Register.field_248);
            Register.field_248 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Register.field_249, new Runnable() {
                public void run() {
                    class_38.method_164();
                }
            }, 0L, (long) (class_128.method_612() * 20));
            method_582();
        }
    }

    public static void Ξ(Enums.HackType enums_hacktype) {
        if (enums_hacktype != null) {
            int i = class_89.method_479(5, 10);
            int j = class_89.method_479(0, 1);

            class_79.method_446(enums_hacktype.toString() + ".enabled", Boolean.valueOf(true));
            if (HHΞ(enums_hacktype)) {
                class_79.method_446(enums_hacktype.toString() + ".silent", Boolean.valueOf(false));
            }

            class_79.method_446(enums_hacktype.toString() + ".violation_divisor", Integer.valueOf(1));
            class_79.method_446(enums_hacktype.toString() + ".cancel_after_violation", Integer.valueOf(j));
            int k = Register.field_249.getConfig().getInt(enums_hacktype.toString() + ".cancel_after_violation");

            if (k < 0) {
                class_123.field_205.put(enums_hacktype, Integer.valueOf(-1));
            } else if (k > class_38.method_150()) {
                class_123.field_205.put(enums_hacktype, Integer.valueOf(class_38.method_150() + 1));
            } else {
                class_123.field_205.put(enums_hacktype, Integer.valueOf(k + 1));
            }

            boolean flag = Register.field_249.getConfig().getBoolean(enums_hacktype.toString() + ".enabled");
            boolean flag1 = Register.field_249.getConfig().getBoolean(enums_hacktype.toString() + ".silent");
            int l = Register.field_249.getConfig().getInt(enums_hacktype.toString() + ".violation_divisor");

            class_123.field_206.put(enums_hacktype, Integer.valueOf(l));
            class_79.method_443(flag, class_123.field_203, enums_hacktype);
            class_79.method_443(flag1, class_123.field_204, enums_hacktype);
            if (!Register.field_249.getConfig().contains(enums_hacktype.toString() + ".punishments")) {
                for (int i1 = 1; i1 <= class_38.method_150(); ++i1) {
                    if (Register.field_249.getConfig().contains(enums_hacktype.toString() + ".punishments." + i1)) {
                        return;
                    }
                }

                class_79.method_446(enums_hacktype.toString() + ".punishments." + i + ".1", "spartan warn {player} You were detected for {detection}");
                class_79.method_446(enums_hacktype.toString() + ".punishments." + i + ".2", "spartan kick {player} {detection}");
            }

        }
    }

    public static void Ξ() {
        class_123.field_206.clear();
        class_123.field_203.clear();
        class_123.field_204.clear();
        class_123.HHΞ.clear();
        class_123.field_205.clear();
        Register.field_249.reloadConfig();
        Enums.HackType[] aenums_hacktype = Enums.HackType.values();
        int i = aenums_hacktype.length;

        for (int j = 0; j < i; ++j) {
            Enums.HackType enums_hacktype = aenums_hacktype[j];

            method_581(enums_hacktype);
        }

        class_120.method_569();
        class_128.method_609();
        class_125.method_598();
        class_119.method_564();
        class_5.method_24();
        class_118.method_558();
        class_113.method_543();
        class_111.method_533();
        class_26.method_89();
        class_109.method_527();
        class_130.method_617();
        class_32.method_116();
        class_47.method_222(true);
    }

    public static boolean Ξ(Player player, Enums.HackType enums_hacktype) {
        if (player != null && enums_hacktype != null) {
            boolean flag = !class_9.method_35() && !method_587(enums_hacktype) && class_38.method_154(player, enums_hacktype) >= method_584(enums_hacktype);

            if (flag) {
                class_36.method_134(player, "cached_air_ticks", class_71.method_410(player));
            } else {
                class_36.method_138(player, "cached_air_ticks");
            }

            return flag;
        } else {
            return false;
        }
    }

    public static int Ξ(Enums.HackType enums_hacktype) {
        return enums_hacktype != null && class_123.field_205.containsKey(enums_hacktype) ? ((Integer) class_123.field_205.get(enums_hacktype)).intValue() : 0;
    }

    public static int Π(Enums.HackType enums_hacktype) {
        return enums_hacktype != null && class_123.field_206.containsKey(enums_hacktype) ? ((Integer) class_123.field_206.get(enums_hacktype)).intValue() : 1;
    }

    public static boolean Ξ(Enums.HackType enums_hacktype) {
        return class_123.field_203.contains(enums_hacktype);
    }

    public static boolean Π(Enums.HackType enums_hacktype) {
        return class_123.field_204.contains(enums_hacktype);
    }

    public static void Ξ(Player player, Enums.HackType enums_hacktype) {
        if (player != null && enums_hacktype != null) {
            if (method_586(enums_hacktype)) {
                int i = class_38.method_154(player, enums_hacktype);
                String[] astring = method_578(enums_hacktype, i);
                int j = astring.length;

                for (int k = 0; k < j; ++k) {
                    String s = astring[k];

                    if (s != null) {
                        s = s.replace("{player}", player.getName());
                        s = class_79.method_442(player, s, enums_hacktype);
                        PlayerViolationCommandEvent playerviolationcommandevent = new PlayerViolationCommandEvent(player, enums_hacktype, s);

                        Bukkit.getPluginManager().callEvent(playerviolationcommandevent);
                        if (playerviolationcommandevent.isCancelled()) {
                            return;
                        }

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                    }
                }
            }

        }
    }

    public static boolean HHΞ(Enums.HackType enums_hacktype) {
        return enums_hacktype == null ? false : enums_hacktype != Enums.HackType.Velocity && enums_hacktype != Enums.HackType.AutoRespawn && enums_hacktype != Enums.HackType.FastClicks;
    }

    public static void Π(Enums.HackType enums_hacktype) {
        if (enums_hacktype != null && !method_586(enums_hacktype)) {
            CheckToggleEvent checktoggleevent = new CheckToggleEvent(enums_hacktype, Enums.ToggleAction.Enable);

            Bukkit.getPluginManager().callEvent(checktoggleevent);
            if (!checktoggleevent.isCancelled()) {
                String s = Register.field_249.getDescription().getVersion();

                Register.field_249.getConfig().set(enums_hacktype.toString() + ".enabled", Boolean.valueOf(true));
                Register.field_249.saveConfig();
                method_581(enums_hacktype);
                class_43.method_195("[Spartan " + s + "] " + enums_hacktype.toString() + " has been enabled.", false);
            }
        }
    }

    public static void HHΞ(Enums.HackType enums_hacktype) {
        if (enums_hacktype != null && class_123.field_203.contains(enums_hacktype)) {
            CheckToggleEvent checktoggleevent = new CheckToggleEvent(enums_hacktype, Enums.ToggleAction.Disable);

            Bukkit.getPluginManager().callEvent(checktoggleevent);
            if (!checktoggleevent.isCancelled()) {
                String s = Register.field_249.getDescription().getVersion();

                Register.field_249.getConfig().set(enums_hacktype.toString() + ".enabled", Boolean.valueOf(false));
                Register.field_249.saveConfig();
                method_581(enums_hacktype);
                class_43.method_195("[Spartan " + s + "] " + enums_hacktype.toString() + " has been disabled.", false);
            }
        }
    }

    public static void HΞ(Enums.HackType enums_hacktype) {
        if (enums_hacktype != null && !class_123.field_204.contains(enums_hacktype) && HHΞ(enums_hacktype)) {
            CheckSilentToggleEvent checksilenttoggleevent = new CheckSilentToggleEvent(enums_hacktype, Enums.ToggleAction.Enable);

            Bukkit.getPluginManager().callEvent(checksilenttoggleevent);
            if (!checksilenttoggleevent.isCancelled()) {
                String s = Register.field_249.getDescription().getVersion();

                Register.field_249.getConfig().set(enums_hacktype.toString() + ".silent", Boolean.valueOf(true));
                Register.field_249.saveConfig();
                method_581(enums_hacktype);
                class_43.method_195("[Spartan " + s + "] " + enums_hacktype.toString() + " is silent checking.", false);
            }
        }
    }

    public static void BΞ(Enums.HackType enums_hacktype) {
        if (enums_hacktype != null && class_123.field_204.contains(enums_hacktype) && HHΞ(enums_hacktype)) {
            CheckSilentToggleEvent checksilenttoggleevent = new CheckSilentToggleEvent(enums_hacktype, Enums.ToggleAction.Disable);

            Bukkit.getPluginManager().callEvent(checksilenttoggleevent);
            if (!checksilenttoggleevent.isCancelled()) {
                String s = Register.field_249.getDescription().getVersion();

                Register.field_249.getConfig().set(enums_hacktype.toString() + ".silent", Boolean.valueOf(false));
                Register.field_249.saveConfig();
                method_581(enums_hacktype);
                class_43.method_195("[Spartan " + s + "] " + enums_hacktype.toString() + " is no longer silent checking.", false);
            }
        }
    }

    public static void Π() {
        class_9.method_36(20);
        String s = Register.field_249.getDescription().getVersion();
        Enums.HackType[] aenums_hacktype = Enums.HackType.values();
        int i = aenums_hacktype.length;

        for (int j = 0; j < i; ++j) {
            Enums.HackType enums_hacktype = aenums_hacktype[j];

            method_589(enums_hacktype);
        }

        class_43.method_195("[Spartan " + s + "] All checks have been enabled.", false);
    }

    public static void HHΞ() {
        class_9.method_36(20);
        String s = Register.field_249.getDescription().getVersion();
        Enums.HackType[] aenums_hacktype = Enums.HackType.values();
        int i = aenums_hacktype.length;

        for (int j = 0; j < i; ++j) {
            Enums.HackType enums_hacktype = aenums_hacktype[j];

            HHΞ(enums_hacktype);
        }

        class_43.method_195("[Spartan " + s + "] All checks have been disabled.", false);
    }

    public static void HΞ() {
        class_9.method_36(20);
        String s = Register.field_249.getDescription().getVersion();
        Enums.HackType[] aenums_hacktype = Enums.HackType.values();
        int i = aenums_hacktype.length;

        for (int j = 0; j < i; ++j) {
            Enums.HackType enums_hacktype = aenums_hacktype[j];

            method_590(enums_hacktype);
        }

        class_43.method_195("[Spartan " + s + "] All checks are now silent checking.", false);
    }

    public static void BΞ() {
        class_9.method_36(20);
        String s = Register.field_249.getDescription().getVersion();
        Enums.HackType[] aenums_hacktype = Enums.HackType.values();
        int i = aenums_hacktype.length;

        for (int j = 0; j < i; ++j) {
            Enums.HackType enums_hacktype = aenums_hacktype[j];

            method_591(enums_hacktype);
        }

        class_43.method_195("[Spartan " + s + "] All checks are no longer silent checking.", false);
    }

    static {
        class_123.field_203 = new ArrayList();
        class_123.field_204 = new ArrayList();
        class_123.field_205 = new HashMap();
        class_123.field_206 = new HashMap();
        class_123.HHΞ = new HashMap();
    }
}
