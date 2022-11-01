package mineultra.game.center.managers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.kit.Kit;

public class LobbyEnt
{
    private Kit _kit;
    private GameTeam _team;
    private Entity _ent;
    private Location _loc;
    
    public LobbyEnt(final Entity ent, final Location loc, final Kit kit) {
        super();
        this._ent = ent;
        this._loc = loc;
        this._kit = kit;
    }
    
    public LobbyEnt(final Entity ent, final Location loc, final GameTeam team) {
        super();
        this._ent = ent;
        this._loc = loc;
        this._team = team;
    }
    
    public Kit GetKit() {
        return this._kit;
    }
    
    public GameTeam GetTeam() {
        return this._team;
    }
    
    public Entity GetEnt() {
        return this._ent;
    }
    
    public Location GetLocation() {
        return this._loc;
    }
}
