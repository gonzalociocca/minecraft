package me.gonzalociocca.minigame.events.Damage.combat.event;

import me.gonzalociocca.minigame.events.Damage.DeathMessageType;
import me.gonzalociocca.minigame.events.Damage.combat.ClientCombat;
import me.gonzalociocca.minigame.events.Damage.combat.CombatLog;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

public class CombatDeathEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private EntityDeathEvent _event;
    private ClientCombat _clientCombat;
    private CombatLog _log;
    private DeathMessageType _messageType = DeathMessageType.Detailed;
    
    public CombatDeathEvent(EntityDeathEvent event, ClientCombat clientCombat, CombatLog log)
    {
        _event = event;
        _clientCombat = clientCombat;
        _log = log;
    }
 
    public HandlerList getHandlers()
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
    public ClientCombat GetClientCombat()
    {
    	return _clientCombat;
    }

	public CombatLog GetLog() 
	{
		return _log;
	}

	public EntityDeathEvent GetEvent() 
	{
		return _event;
	}
	
	public void SetBroadcastType(DeathMessageType value)
	{
		_messageType = value;
	}
	
	public DeathMessageType GetBroadcastType()
	{
		return _messageType;
	}
}
