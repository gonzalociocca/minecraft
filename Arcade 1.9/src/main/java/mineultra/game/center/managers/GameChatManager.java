package mineultra.game.center.managers;

import java.util.Iterator;

import me.gonzalociocca.mineultra.DBManager;
import me.gonzalociocca.mineultra.Rank;
import mineultra.game.center.game.GameTeam;
import org.bukkit.entity.Player;
import mineultra.game.center.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventHandler;
import mineultra.core.common.util.C;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import mineultra.game.center.centerManager;
import org.bukkit.event.Listener;

public class GameChatManager implements Listener
{
    centerManager Manager = null;
    DBManager db = null;
    public GameChatManager(final centerManager manager) {
        super();
        db = manager.getDB();
        this.Manager = manager;
        this.Manager.GetPluginManager().registerEvents((Listener)this, (Plugin)this.Manager.GetPlugin());
    }
    
    @EventHandler
    public void Me(final PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/me")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void HandleChat(final AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player sender = event.getPlayer();
        String dead = "";
        if (this.Manager.GetGame() != null && this.Manager.GetGame().GetTeam(sender) != null && !this.Manager.GetGame().IsAlive(sender)) {
            dead = String.valueOf(C.cGray) + "Dead ";
        }

        String rankStr = db.getPlayerData(sender).getRank().getChatPrefix()+" ";

        event.setFormat(String.valueOf(dead) + rankStr + this.Manager.GetColor(sender) + "%1$s " + ChatColor.WHITE + "%2$s");
        if (this.Manager.GetGame() != null && this.Manager.GetGame().GetState() == Game.GameState.Live) {
            boolean globalMessage = false;
            final GameTeam team = this.Manager.GetGame().GetTeam(sender);
            if (team != null) {
                if (event.getMessage().charAt(0) == '@') {
                    event.setMessage(event.getMessage().substring(1, event.getMessage().length()));
                    event.setFormat(String.valueOf(C.cWhite) + C.Bold + "Team" + " " + dead + rankStr + team.GetColor() + "%1$s " + C.cWhite + "%2$s");
                }
                else {
                    globalMessage = true;
                    event.setFormat(String.valueOf(dead) + rankStr + team.GetColor() + "%1$s " + C.cWhite + "%2$s");

                    
                        event.setFormat(String.valueOf(dead) + rankStr + team.GetColor() + "%1$s " + C.cWhite + "%2$s");
                    
                }
            }
            if (globalMessage) {
                return;
            }
            final Iterator<Player> recipientIterator = event.getRecipients().iterator();
            while (recipientIterator.hasNext()) {
                final Player receiver = recipientIterator.next();
                if (this.db.getPlayerData(receiver).getRank().isAtLeast(Rank.Helper)) {
                    continue;
                }
                if (this.Manager.GetGame().GetTeam(receiver) == null || this.Manager.GetGame().GetTeam(sender) == this.Manager.GetGame().GetTeam(receiver)) {
                    continue;
                }
                recipientIterator.remove();
             
            }
        }
    }
}
