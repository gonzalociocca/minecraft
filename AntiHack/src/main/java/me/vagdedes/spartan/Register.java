package me.vagdedes.spartan;

import java.util.Iterator;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Register extends JavaPlugin implements Listener {

    // $FF: renamed from: Ξ int
    public static int field_248;
    // $FF: renamed from: Ξ org.bukkit.plugin.Plugin
    public static Plugin field_249;

    public void onEnable() {
        loadConfig0();
        if (class_101.method_512() == class_101.field_254) {
            Bukkit.getConsoleSender().sendMessage("[Spartan] The server\'s version or type is not supported.");
            Bukkit.getConsoleSender().sendMessage("[Spartan] Please contact the plugin\'s developer if you think this is in error.");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            Register.field_249 = this;
            class_123.method_582();
            class_47.method_223();
            class_128.method_614();
            class_9.method_36(40);
            class_127.method_608();
            Bukkit.getPluginManager().registerEvents(this, this);
            Bukkit.getPluginManager().registerEvents(new class_38(), this);
            Bukkit.getPluginManager().registerEvents(new class_132(), this);
            Bukkit.getPluginManager().registerEvents(new class_133(), this);
            Bukkit.getPluginManager().registerEvents(new class_134(), this);
            Bukkit.getPluginManager().registerEvents(new class_53(), this);
            Bukkit.getPluginManager().registerEvents(new class_116(), this);
            Bukkit.getPluginManager().registerEvents(new class_58(), this);
            Bukkit.getPluginManager().registerEvents(new class_37(), this);
            Bukkit.getPluginManager().registerEvents(new class_39(), this);
            Bukkit.getPluginManager().registerEvents(new class_23(), this);
            Bukkit.getPluginManager().registerEvents(new class_36(), this);
            Bukkit.getPluginManager().registerEvents(new class_62(), this);
            Bukkit.getPluginManager().registerEvents(new class_46(), this);
            Bukkit.getPluginManager().registerEvents(new class_57(), this);
            Bukkit.getPluginManager().registerEvents(new class_40(), this);
            Bukkit.getPluginManager().registerEvents(new class_0(), this);
            Bukkit.getPluginManager().registerEvents(new class_2(), this);
            Bukkit.getPluginManager().registerEvents(new class_3(), this);
            Bukkit.getPluginManager().registerEvents(new class_1(), this);
            Bukkit.getPluginManager().registerEvents(new class_98(), this);
            Bukkit.getPluginManager().registerEvents(new class_60(), this);
            Bukkit.getPluginManager().registerEvents(new class_65(), this);
            Bukkit.getPluginManager().registerEvents(new class_63(), this);
            Bukkit.getPluginManager().registerEvents(new class_44(), this);
            Bukkit.getPluginManager().registerEvents(new class_72(), this);
            Bukkit.getPluginManager().registerEvents(new class_10(), this);
            Bukkit.getPluginManager().registerEvents(new class_61(), this);
            Bukkit.getPluginManager().registerEvents(new class_131(), this);
            Bukkit.getPluginManager().registerEvents(new class_90(), this);
            Bukkit.getPluginManager().registerEvents(new class_48(), this);
            Bukkit.getPluginManager().registerEvents(new class_14(), this);
            Bukkit.getPluginManager().registerEvents(new class_22(), this);
            Bukkit.getPluginManager().registerEvents(new class_42(), this);
            Bukkit.getPluginManager().registerEvents(new class_21(), this);
            Bukkit.getPluginManager().registerEvents(new class_136(), this);
            Bukkit.getPluginManager().registerEvents(new class_139(), this);
            Bukkit.getPluginManager().registerEvents(new class_54(), this);
            Bukkit.getPluginManager().registerEvents(new class_117(), this);
            Bukkit.getPluginManager().registerEvents(new class_45(), this);
            Bukkit.getPluginManager().registerEvents(new class_80(), this);
            Bukkit.getPluginManager().registerEvents(new class_56(), this);
            Bukkit.getPluginManager().registerEvents(new class_128(), this);
            Bukkit.getPluginManager().registerEvents(new class_27(), this);
            Bukkit.getPluginManager().registerEvents(new class_121(), this);
            Bukkit.getPluginManager().registerEvents(new class_140(), this);
            Bukkit.getPluginManager().registerEvents(new class_78(), this);
            Bukkit.getPluginManager().registerEvents(new class_70(), this);
            Bukkit.getPluginManager().registerEvents(new class_119(), this);
            Bukkit.getPluginManager().registerEvents(new class_64(), this);
            Bukkit.getPluginManager().registerEvents(new class_31(), this);
            Bukkit.getPluginManager().registerEvents(new class_50(), this);
            Bukkit.getPluginManager().registerEvents(new class_104(), this);
            Bukkit.getPluginManager().registerEvents(new class_102(), this);
            Bukkit.getPluginManager().registerEvents(new class_59(), this);
            Bukkit.getPluginManager().registerEvents(new class_141(), this);
            Bukkit.getPluginManager().registerEvents(new class_86(), this);
            Bukkit.getPluginManager().registerEvents(new class_100(), this);
            Bukkit.getPluginManager().registerEvents(new class_74(), this);
            Bukkit.getPluginManager().registerEvents(new class_20(), this);
            Bukkit.getPluginManager().registerEvents(new class_115(), this);
            Bukkit.getPluginManager().registerEvents(new class_77(), this);
            Bukkit.getPluginManager().registerEvents(new class_25(), this);
            Bukkit.getPluginManager().registerEvents(new class_11(), this);
            Bukkit.getPluginManager().registerEvents(new class_118(), this);
            Bukkit.getPluginManager().registerEvents(new class_126(), this);
            Bukkit.getPluginManager().registerEvents(new class_88(), this);
            Bukkit.getPluginManager().registerEvents(new class_142(), this);
            Bukkit.getPluginManager().registerEvents(new class_124(), this);
            Bukkit.getPluginManager().registerEvents(new class_127(), this);
            Bukkit.getPluginManager().registerEvents(new class_13(), this);
            Bukkit.getPluginManager().registerEvents(new class_12(), this);
            Bukkit.getPluginManager().registerEvents(new class_52(), this);
            Bukkit.getPluginManager().registerEvents(new class_24(), this);
            Bukkit.getPluginManager().registerEvents(new class_137(), this);
            Bukkit.getPluginManager().registerEvents(new class_108(), this);
            Bukkit.getPluginManager().registerEvents(new class_112(), this);
            Bukkit.getPluginManager().registerEvents(new class_15(), this);
            Bukkit.getPluginManager().registerEvents(new class_28(), this);
            Bukkit.getPluginManager().registerEvents(new class_129(), this);
            Bukkit.getPluginManager().registerEvents(new class_114(), this);
            Bukkit.getPluginManager().registerEvents(new class_17(), this);
            Bukkit.getPluginManager().registerEvents(new class_82(), this);
            Bukkit.getPluginManager().registerEvents(new class_68(), this);
            Bukkit.getPluginManager().registerEvents(new class_138(), this);
            Bukkit.getPluginManager().registerEvents(new class_96(), this);
            Bukkit.getPluginManager().registerEvents(new class_7(), this);
            Bukkit.getPluginManager().registerEvents(new class_16(), this);
            Bukkit.getPluginManager().registerEvents(new class_94(), this);
            Bukkit.getPluginManager().registerEvents(new class_143(), this);
            Bukkit.getPluginManager().registerEvents(new class_76(), this);
            Bukkit.getPluginManager().registerEvents(new class_110(), this);
            Bukkit.getPluginManager().registerEvents(new class_84(), this);
            Bukkit.getPluginManager().registerEvents(new class_135(), this);
            this.getCommand("spartan").setExecutor(new class_122());
            if (class_101.method_512() != class_101.field_250 && class_101.method_512() != class_101.field_254 && class_101.method_512() != class_101.field_251) {
                Bukkit.getPluginManager().registerEvents(new class_29(), this);
                Bukkit.getPluginManager().registerEvents(new class_35(), this);
                Bukkit.getPluginManager().registerEvents(new class_33(), this);
                Bukkit.getPluginManager().registerEvents(new class_75(), this);
                Bukkit.getPluginManager().registerEvents(new class_106(), this);
            }

            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    class_47.method_222(false);
                    class_41.method_176();
                    class_97.method_504();
                    Iterator iterator = Bukkit.getOnlinePlayers().iterator();

                    while (iterator.hasNext()) {
                        Player player = (Player) iterator.next();

                        class_124.method_596(player);
                        class_71.HHΞ(player);
                        class_69.method_368(player);
                        class_114.method_545(player);
                        class_48.method_231(player);
                        class_50.method_255(player);
                        class_76.method_425(player);
                        class_75.method_422(player);
                        class_56.method_300(player);
                        class_135.method_645(player);
                        class_38.method_161(player);
                    }

                    class_19.method_70();
                    class_92.method_492();
                    class_110.method_530();
                    class_39.method_172();
                    class_9.method_34();
                }
            }, 0L, 0L);
            Register.field_248 = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    class_38.method_164();
                }
            }, 0L, (long) (class_128.method_612() * 20));
        }
    }
}
