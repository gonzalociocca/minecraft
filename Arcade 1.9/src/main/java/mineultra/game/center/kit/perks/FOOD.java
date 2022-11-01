package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import mineultra.core.common.util.C;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;

public class FOOD extends Perk
{
    private int _amount;
    
    public FOOD(final int amount) {
        super("Strength", new String[] { String.valueOf(C.cGray) + "Your Hunger is permanently " + amount });
        this._amount = amount;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void Update(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        if(this.Manager  == null){
            return;
        }
        if (this.Manager.GetGame() == null) {
            return;
        }
        for (final Player player : this.Manager.GetGame().GetPlayers(true)) {
            if (this.Kit.HasKit(player)) {
                player.setFoodLevel(this._amount);
            }
        }
    }
}
