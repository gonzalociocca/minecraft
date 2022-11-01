import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

// $FF: renamed from: j
public class class_127 implements Listener {

    private static HashMap<UUID, Long> Ξ;

    @EventHandler
    private void Ξ(PlayerLoginEvent playerloginevent) {
        Player player = playerloginevent.getPlayer();

        class_47.method_224(player);
        int i = class_128.method_611("reconnect_cooldown");

        if (i > 0) {
            if (!class_47.method_229(player, Enums.Permission.reconnect)) {
                UUID uuid = player.getUniqueId();

                if (class_127.field_213.containsKey(uuid)) {
                    long j = System.currentTimeMillis() - ((Long) class_127.field_213.get(uuid)).longValue();

                    if (j <= (long) i * 1000L) {
                        String s = class_125.method_599("reconnect_kick_message");

                        s = class_79.method_442(player, s, (Enums.HackType) null);
                        playerloginevent.disallow(Result.KICK_OTHER, s);
                    }
                }
            }

        }
    }

    @EventHandler
    private void Ξ(PlayerQuitEvent playerquitevent) {
        Player player = playerquitevent.getPlayer();

        class_127.field_213.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
    }

    public static void Ξ() {
        Iterator iterator = Bukkit.getOnlinePlayers().iterator();

        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();

            class_127.field_213.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
        }

    }

    static {
        class_127.field_213 = new HashMap();
    }
}
