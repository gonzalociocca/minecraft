import java.util.Iterator;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

// $FF: renamed from: l
public class class_129 implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerChatEvent playerchatevent) {
        Player player = playerchatevent.getPlayer();

        if (!playerchatevent.isCancelled() && class_47.method_229(player, Enums.Permission.staff_chat) && class_128.method_610("staff_chat")) {
            String s = playerchatevent.getMessage();

            if (s.startsWith(class_125.method_599("staff_chat_character"))) {
                playerchatevent.setCancelled(true);
                s = s.substring(1, s.length());
                String s1 = class_125.method_599("staff_chat_message");

                s1 = s1.replace("{message}", s);
                s1 = class_79.method_442(player, s1, (Enums.HackType) null);
                Iterator iterator = Bukkit.getOnlinePlayers().iterator();

                while (iterator.hasNext()) {
                    Player player1 = (Player) iterator.next();

                    if (class_47.method_229(player1, Enums.Permission.staff_chat)) {
                        player1.sendMessage(s1);
                    }
                }
            }

        }
    }
}
