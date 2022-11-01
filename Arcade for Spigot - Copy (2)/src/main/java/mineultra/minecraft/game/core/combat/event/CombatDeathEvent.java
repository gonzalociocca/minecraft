package mineultra.minecraft.game.core.combat.event;

import mineultra.minecraft.game.core.combat.DeathMessageType;
import mineultra.minecraft.game.core.combat.CombatLog;
import mineultra.minecraft.game.core.combat.ClientCombat;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class CombatDeathEvent extends Event
{
    private static final HandlerList handlers;
    private EntityDeathEvent _event;
    private ClientCombat _clientCombat;
    private CombatLog _log;
    private DeathMessageType _messageType;
    
    static {
        handlers = new HandlerList();
    }
    
    public CombatDeathEvent(final EntityDeathEvent event, final ClientCombat clientCombat, final CombatLog log) {
        super();
        this._messageType = DeathMessageType.Detailed;
        this._event = event;
        this._clientCombat = clientCombat;
        this._log = log;
    }
    
    public HandlerList getHandlers() {
        return CombatDeathEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return CombatDeathEvent.handlers;
    }
    
    public ClientCombat GetClientCombat() {
        return this._clientCombat;
    }
    
    public CombatLog GetLog() {
        return this._log;
    }
    
    public EntityDeathEvent GetEvent() {
        return this._event;
    }
    
    public void SetBroadcastType(final DeathMessageType value) {
        this._messageType = value;
    }
    
    public DeathMessageType GetBroadcastType() {
        return this._messageType;
    }
}
