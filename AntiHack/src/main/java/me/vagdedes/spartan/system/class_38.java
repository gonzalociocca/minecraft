package me.vagdedes.spartan.system;

import java.util.HashMap;
import java.util.Iterator;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.api.PlayerViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

// $FF: renamed from: me.vagdedes.spartan.system.a
public class class_38 implements Listener {

    private static int Ξ;
    private static int Π;
    private static boolean Ξ;
    private static int HHΞ;
    private static int HΞ;
    private static int BΞ;
    private static HashMap<Player, HashMap<Enums.HackType, Integer>> Ξ;
    private static HashMap<Player, HashMap<Enums.HackType, Integer>> Π;

    private static void Π() {
        class_38.field_50.clear();
    }

    public static int Ξ() {
        return class_38.field_48;
    }

    public static int Π() {
        return class_38.HHΞ;
    }

    public static boolean Ξ(Player player) {
        return player != null && (!player.isOp() || class_128.method_610("op_bypass")) ? class_47.method_229(player, Enums.Permission.bypass) : false;
    }

    public static boolean Ξ(Player player, Enums.HackType enums_hacktype) {
        return player != null && enums_hacktype != null && (!player.isOp() || class_128.method_610("op_bypass")) ? method_152(player) || class_47.method_230(player, enums_hacktype) : false;
    }

    public static int Ξ(Player player, Enums.HackType enums_hacktype) {
        return player != null && enums_hacktype != null ? (class_38.field_50.containsKey(player) && ((HashMap) class_38.field_50.get(player)).containsKey(enums_hacktype) ? ((Integer) ((HashMap) class_38.field_50.get(player)).get(enums_hacktype)).intValue() : 0) : 0;
    }

    public static int Ξ(Player player) {
        int i = 0;

        if (player == null) {
            return i;
        } else {
            Enums.HackType[] aenums_hacktype = Enums.HackType.values();
            int j = aenums_hacktype.length;

            for (int k = 0; k < j; ++k) {
                Enums.HackType enums_hacktype = aenums_hacktype[k];

                i += method_154(player, enums_hacktype);
            }

            return i;
        }
    }

    public static void Ξ(Player player, Enums.HackType enums_hacktype, int i, boolean flag, String s) {
        if (player != null && enums_hacktype != null) {
            if (!class_38.field_50.containsKey(player)) {
                class_38.field_50.put(player, new HashMap());
            }

            if (class_38.field_50.containsKey(player)) {
                HashMap hashmap = (HashMap) class_38.field_50.get(player);
                byte b0 = 0;

                if (i > method_150()) {
                    hashmap.put(enums_hacktype, Integer.valueOf(method_150()));
                } else if (i < b0) {
                    hashmap.put(enums_hacktype, Integer.valueOf(b0));
                } else {
                    hashmap.put(enums_hacktype, Integer.valueOf(i));
                }

                class_38.field_50.put(player, hashmap);
            }

            if (flag) {
                class_49.method_251(player, enums_hacktype, s, true);
            }

            class_123.method_588(player, enums_hacktype);
        }
    }

    public static boolean Ξ(Player player, Enums.HackType enums_hacktype, boolean flag) {
        class_101 class_101 = class_101.method_512();

        return enums_hacktype != null && player != null && !method_153(player, enums_hacktype) && class_123.method_586(enums_hacktype) && (!flag || !class_128.method_610("use_tps_protection") || !class_97.method_501()) && !method_162(player, enums_hacktype) && (class_101 == class_101.field_250 || class_101 == class_101.field_254 || player.getGameMode() != GameMode.SPECTATOR) && class_38.field_49 > class_38.field_46 && class_38.field_49 < class_38.field_45 ? (enums_hacktype == Enums.HackType.Nuker || enums_hacktype == Enums.HackType.GhostHand || enums_hacktype == Enums.HackType.FastBreak || enums_hacktype == Enums.HackType.BlockReach || enums_hacktype == Enums.HackType.KillAura || enums_hacktype == Enums.HackType.NoSwing) && (class_34.method_124(player) || class_34.method_125(player) && enums_hacktype == Enums.HackType.KillAura) : true;
    }

    public static boolean Ξ(Player player, boolean flag) {
        class_101 class_101 = class_101.method_512();

        return player == null || flag && class_128.method_610("use_tps_protection") && class_97.method_501() || class_38.field_49 <= class_38.field_46 || class_38.field_49 >= class_38.field_45 || class_101 != class_101.field_250 && class_101 != class_101.field_254 && player.getGameMode() == GameMode.SPECTATOR;
    }

    public static void Ξ(Player player, Enums.HackType enums_hacktype, String s) {
        if (player != null && enums_hacktype != null && !class_49.method_246(player) && !class_9.method_35()) {
            if (!class_38.field_47) {
                class_38.field_47 = true;
                Bukkit.getScheduler().scheduleAsyncRepeatingTask(Register.field_249, new Runnable() {
                    public void run() {
                        class_6.method_28();
                    }
                }, (long) (class_89.method_479(60, 90) * 20), 1728000L);
            }

            if (s == null) {
                s = "None";
            }

            PlayerViolationEvent playerviolationevent = new PlayerViolationEvent(player, enums_hacktype, s);

            Bukkit.getPluginManager().callEvent(playerviolationevent);
            if (playerviolationevent.isCancelled()) {
                class_49.method_248(player);
            } else {
                if (!class_16.method_64(player)) {
                    method_156(player, enums_hacktype, method_154(player, enums_hacktype) + 1, true, s);
                }

                Iterator iterator = Bukkit.getOnlinePlayers().iterator();

                while (iterator.hasNext()) {
                    Player player1 = (Player) iterator.next();

                    if (class_71.PPΞ(player1) && player1.getOpenInventory().getTitle().equalsIgnoreCase("\u00a70Info:\u00a72 " + player.getName())) {
                        class_3.method_11(player1, player);
                    }
                }

            }
        }
    }

    public static void Ξ(Player player, Enums.HackType enums_hacktype, int i) {
        if (player != null && enums_hacktype != null) {
            if (!class_38.field_51.containsKey(player) && i > 0) {
                class_38.field_51.put(player, new HashMap());
            }

            if (class_38.field_51.containsKey(player)) {
                HashMap hashmap = (HashMap) class_38.field_51.get(player);
                boolean flag = hashmap.containsKey(enums_hacktype);

                if (!flag || flag && ((Integer) hashmap.get(enums_hacktype)).intValue() <= i) {
                    short short0 = 3600;
                    byte b0 = 0;

                    if (i > short0) {
                        hashmap.put(enums_hacktype, Integer.valueOf(short0));
                    } else if (i < b0) {
                        hashmap.put(enums_hacktype, Integer.valueOf(b0));
                    } else {
                        hashmap.put(enums_hacktype, Integer.valueOf(i));
                    }

                    class_38.field_51.put(player, hashmap);
                }
            }

        }
    }

    public static void Ξ(Player player) {
        if (player != null && class_38.field_51.containsKey(player)) {
            Enums.HackType[] aenums_hacktype = Enums.HackType.values();
            int i = aenums_hacktype.length;

            for (int j = 0; j < i; ++j) {
                Enums.HackType enums_hacktype = aenums_hacktype[j];

                if (((HashMap) class_38.field_51.get(player)).containsKey(enums_hacktype)) {
                    int k = ((Integer) ((HashMap) class_38.field_51.get(player)).get(enums_hacktype)).intValue();

                    if (k > 0) {
                        ((HashMap) class_38.field_51.get(player)).put(enums_hacktype, Integer.valueOf(k - 1));
                    }
                }
            }
        }

    }

    private static boolean Π(Player player, Enums.HackType enums_hacktype) {
        return player != null && enums_hacktype != null ? class_38.field_51.containsKey(player) && ((HashMap) class_38.field_51.get(player)).containsKey(enums_hacktype) && ((Integer) ((HashMap) class_38.field_51.get(player)).get(enums_hacktype)).intValue() > 0 : false;
    }

    public static void Π(Player player) {
        if (player != null) {
            class_41.method_177(player);
            class_38.field_50.remove(player);
            class_38.field_51.remove(player);
            Iterator iterator = Bukkit.getOnlinePlayers().iterator();

            while (iterator.hasNext()) {
                Player player1 = (Player) iterator.next();

                if (class_71.PPΞ(player1) && player1.getOpenInventory().getTitle().equalsIgnoreCase("\u00a70Info:\u00a72 " + player.getName())) {
                    if (player.isOnline()) {
                        class_3.method_11(player1, player);
                    } else {
                        player1.closeInventory();
                    }
                }
            }

        }
    }

    public static void Ξ() {
        method_149();
        if (class_128.method_610("violations_reset_message")) {
            Player[] aplayer = class_51.method_270();
            int i = aplayer.length;

            for (int j = 0; j < i; ++j) {
                Player player = aplayer[j];

                player.sendMessage(class_125.method_599("violations_reset"));
            }
        }

    }

    @EventHandler
    public void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        method_163(player);
    }

    static {
        class_38.field_45 = 2600;
        class_38.field_46 = 2400;
        class_38.field_47 = false;
        class_38.HHΞ = 10;
        class_38.field_48 = 100;
        class_38.field_49 = class_105.method_519(class_6.class);
        class_38.field_50 = new HashMap();
        class_38.field_51 = new HashMap();
    }
}
