package me.vagdedes.spartan.api;

import java.util.UUID;
import me.vagdedes.spartan.Register;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class API {

    public static String getVersion() {
        return Register.field_249.getDescription().getVersion();
    }

    public static String getMessage(String s) {
        return class_125.method_599(s);
    }

    public static boolean getSetting(String s) {
        return class_128.method_610(s);
    }

    public static boolean hasVerboseEnabled(Player player) {
        return class_51.method_271(player);
    }

    public static int getViolationResetTime() {
        return class_128.method_611("vl_reset_time");
    }

    public static void setVerbose(Player player, boolean flag) {
        class_51.method_273(player, flag, 1);
    }

    public static int getPing(Player player) {
        return class_87.method_472(player);
    }

    public static double getTPS() {
        return class_97.method_502();
    }

    public static boolean hasPermission(Player player, Enums.Permission enums_permission) {
        return class_47.method_229(player, enums_permission);
    }

    public static boolean isEnabled(Enums.HackType enums_hacktype) {
        return class_123.method_586(enums_hacktype);
    }

    public static boolean isSilent(Enums.HackType enums_hacktype) {
        return class_123.method_587(enums_hacktype);
    }

    public static int getVL(Player player, Enums.HackType enums_hacktype) {
        return class_38.method_154(player, enums_hacktype);
    }

    public static int getVL(Player player) {
        return class_38.method_155(player);
    }

    public static void setVL(Player player, Enums.HackType enums_hacktype, int i) {
        class_38.method_156(player, enums_hacktype, i, false, (String) null);
    }

    public static void reloadConfig() {
        class_123.method_580((Player) null, false);
    }

    public static void enableCheck(Enums.HackType enums_hacktype) {
        class_123.method_589(enums_hacktype);
    }

    public static void disableCheck(Enums.HackType enums_hacktype) {
        class_123.HHΞ(enums_hacktype);
    }

    public static void enableSilentChecking(Enums.HackType enums_hacktype) {
        class_123.method_590(enums_hacktype);
    }

    public static void disableSilentChecking(Enums.HackType enums_hacktype) {
        class_123.method_591(enums_hacktype);
    }

    public static void cancelCheck(Player player, Enums.HackType enums_hacktype, int i) {
        class_38.method_160(player, enums_hacktype, i);
    }

    public static void resetVL() {
        class_38.method_164();
    }

    public static void resetVL(Player player) {
        class_38.method_163(player);
    }

    public static boolean isBypassing(Player player) {
        return class_38.method_152(player);
    }

    public static boolean isBypassing(Player player, Enums.HackType enums_hacktype) {
        return class_38.method_153(player, enums_hacktype);
    }

    public static void banPlayer(UUID uuid, String s) {
        class_118.method_559(uuid, "CONSOLE", s);
    }

    public static boolean isBanned(UUID uuid) {
        return class_118.method_561(uuid);
    }

    public static void unbanPlayer(UUID uuid) {
        class_118.method_560(uuid);
    }

    public static String getBanReason(UUID uuid) {
        return class_118.method_562(uuid, "reason");
    }

    public static String getBanPunisher(UUID uuid) {
        return class_118.method_562(uuid, "punisher");
    }

    public static boolean hasMiningNotificationsEnabled(Player player) {
        return class_126.method_600(player);
    }

    public static void setMiningNotifications(Player player, boolean flag) {
        class_126.method_602(player, flag);
    }

    public static int getCPS(Player player) {
        return class_124.method_597(player);
    }

    public static UUID[] getBanList() {
        return class_118.method_556();
    }

    public static void addToWave(UUID uuid, String s) {
        class_130.method_618(uuid, s);
    }

    public static void removeFromWave(UUID uuid) {
        class_130.method_619(uuid);
    }

    public static void clearWave() {
        class_130.method_620();
    }

    public static void runWave() {
        class_130.HHΞ();
    }

    public static int getWaveSize() {
        return class_130.method_622();
    }

    public static boolean isAddedToTheWave(UUID uuid) {
        return class_130.method_621(uuid);
    }

    public static void warnPlayer(Player player, String s) {
        class_49.method_249((CommandSender) null, player, s);
    }

    public static void reloadPermissionCache() {
        class_47.method_222(true);
    }

    public static void reloadPermissionCache(Player player) {
        class_47.method_224(player);
    }
}
