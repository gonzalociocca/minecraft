package server.instance.core.combat.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import server.instance.GameServer;
import server.instance.core.combat.ClientCombat;
import server.instance.core.combat.CombatLog;
import server.instance.core.combat.DeathMessageType;

public class CombatDeathEvent extends Event
{
    private GameServer _game;
    private static final HandlerList handlers;
    private EntityDeathEvent _event;
    private ClientCombat _clientCombat;
    private CombatLog _log;
    private DeathMessageType _messageType;

    static {
        handlers = new HandlerList();
    }
    
    public CombatDeathEvent(GameServer game, final EntityDeathEvent event, final ClientCombat clientCombat, final CombatLog log) {
        super();
        _game = game;
        this._messageType = DeathMessageType.Detailed;
        this._event = event;
        this._clientCombat = clientCombat;
        this._log = log;
    }

    public GameServer getGame(){
        return _game;
    }
    
    public HandlerList getHandlers() {
        return CombatDeathEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return CombatDeathEvent.handlers;
    }
    
    public ClientCombat getClientCombat() {
        return this._clientCombat;
    }
    
    public CombatLog getLog() {
        return this._log;
    }
    
    public EntityDeathEvent getEvent() {
        return this._event;
    }
    
    public void setBroadcastType(final DeathMessageType value) {
        this._messageType = value;
    }
    
    public DeathMessageType getBroadcastType() {
        return this._messageType;
    }
}
