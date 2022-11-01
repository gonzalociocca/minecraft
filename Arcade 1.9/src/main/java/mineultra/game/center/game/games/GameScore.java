package mineultra.game.center.game.games;

import org.bukkit.entity.Player;

public class GameScore
{
    public Player Player;
    public double Score;
    
    public GameScore(final Player player, final double i) {
        super();
        this.Player = player;
        this.Score = i;
    }
}
