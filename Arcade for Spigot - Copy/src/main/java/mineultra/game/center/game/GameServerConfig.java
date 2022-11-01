package mineultra.game.center.game;

import mineultra.game.center.GameType;
import java.util.ArrayList;

public class GameServerConfig
{
    public String ServerType;
    public int MinPlayers;
    public int MaxPlayers;
    public ArrayList<GameType> GameList;
    
    public GameServerConfig() {
        super();
        this.ServerType = null;
        this.MinPlayers = -1;
        this.MaxPlayers = -1;
        this.GameList = new ArrayList<>();
    }
    
    public boolean IsValid() {
        return this.ServerType != null && this.MinPlayers != -1 && this.MaxPlayers != -1;
    }
}
