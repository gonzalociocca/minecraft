package server.common;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import server.instance.GameServer;
import server.instance.misc.GameState;
import server.util.UtilMsg;

/**
 * Created by noname on 8/4/2017.
 */
public class LobbySign {

    Block signBlock;
    Sign signState;
    GameServer base;
    boolean enabled = true;

    public LobbySign(Block xsignBlock) {
        signBlock = xsignBlock;
        signState = (Sign)signBlock.getState();
    }

    public Block getBlock() {
        return signBlock;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void interact(Player player) {
        if (base != null && isEnabled()) {
            if(!base.getLogin().PlayerAdd(base, player, null)){
                player.sendMessage(UtilMsg.CannotJoinGame);
            } else {
                update();
            }
        } else {
            player.sendMessage(UtilMsg.OutOfServiceComplete);
        }
    }

    public void update() {
        if(base != null && isEnabled()) {
            if (base.getMap() != null) {
                signState.setLine(0, UtilMsg.WhiteBold + base.getMap().getDisplay());
            }
            if (base.getState() == GameState.Prepare) {
                signState.setLine(1, base.getState().getDisplay() + " en &f" + base.getCountdown());
            } else {
                signState.setLine(1, base.getState().getDisplay());
            }

            int size = base.getPlayersCount(true);
            int maxSize = base.getMaxPlayers();


            signState.setLine(3, UtilMsg.White + size + "/" + maxSize);
            signState.update(false, false);
        } else {
            signState.setLine(0, UtilMsg.OutOfServicePart1);
            signState.setLine(1, UtilMsg.OutOfServicePart2);

            signState.setLine(3, UtilMsg.White + 0 + "/" + 0);
            signState.update(false, false);
        }
    }

    public GameServer getGame() {
        return base;
    }

    public void setGame(GameServer newGame) {
        base = newGame;
    }
}
