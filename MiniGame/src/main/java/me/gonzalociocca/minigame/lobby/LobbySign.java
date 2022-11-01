package me.gonzalociocca.minigame.lobby;

import me.gonzalociocca.minigame.games.GameModifier;
import me.gonzalociocca.minigame.games.GameTeam;
import me.gonzalociocca.minigame.games.GameType;
import me.gonzalociocca.minigame.games.game.GameBase;
import me.gonzalociocca.minigame.misc.Code;
import me.gonzalociocca.minigame.misc.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * Created by noname on 8/4/2017.
 */
public class LobbySign {

    GameModifier mod = GameModifier.Normal;
    GameType type;
    Block signBlock;
    GameBase base;

    public LobbySign(GameType xtype, Block xsignBlock) {
        type = xtype;
        signBlock = xsignBlock;
    }
    public Block getBlock(){
        return signBlock;
    }

    public void interact(PlayerData pd){
        if(base!=null && base.canJoin(pd)){
            base.makePlayer(pd);
        }
    }
    public void update() {
        Sign sign = (Sign) signBlock.getState();
        if (base != null && base.getMap() != null) {
            sign.setLine(0, Code.Color("&f&l" + base.getMap().getName()));
        }
        sign.setLine(1, Code.Color(mod.getDisplay()));
        if (base != null && base.getState() != null) {
            sign.setLine(2, Code.Color(base.getState().getDisplay()));
        }
        int size = 0;
        int maxSize = 0;
        if (base != null) {
            for (GameTeam team : base.getTeams()) {
                maxSize += team.getMaxSize();
                size += team.getPlayers().size();
            }
        }

        sign.setLine(3, Code.Color("&f" + size + "/" + maxSize));
        sign.update(true, true);
    }

    public GameType getGameType() {
        return type;
    }

    public GameBase getGame() {
        return base;
    }

    public void setGame(GameBase newGame) {
        base = newGame;
    }

    public GameModifier getGameModifier() {
        return mod;
    }

    public void setGameModifier(GameModifier newMod) {
        mod = newMod;
    }
}
