package mineultra.minecraft.game.core.damage.compatibility;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import mineultra.core.npc.NpcManager;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.event.Listener;

public class NpcProtectListener implements Listener
{
    private NpcManager _npcManager;
    
    public NpcProtectListener(final NpcManager npcManager) {
        super();
        this._npcManager = npcManager;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void CustomDamage(final CustomDamageEvent event) {
        if (event.GetDamageeEntity() != null && this._npcManager.GetNpcByUUID(event.GetDamageeEntity().getUniqueId()) != null) {
            event.SetCancelled("NPC");
        }
    }
}
