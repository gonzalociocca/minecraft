package mineultra.game.center.kit.perks.data;

import org.bukkit.entity.Entity;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FireflyData
{
    public Player Player;
    public Location Location;
    public long Time;
    public HashSet<Entity> Targets;
    
    public FireflyData(final Player player) {
        super();
        this.Targets = new HashSet<Entity>();
        this.Player = player;
        this.Location = player.getLocation();
        this.Time = System.currentTimeMillis();
    }
}