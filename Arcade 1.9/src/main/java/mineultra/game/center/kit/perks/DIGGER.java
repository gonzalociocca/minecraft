package mineultra.game.center.kit.perks;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import mineultra.core.common.util.C;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;

public class DIGGER extends Perk
{
    public DIGGER() {
        super("Digger", new String[] { String.valueOf(C.cGray) + "Permanent Fast Digging II" });
    }
    
    @EventHandler
    public void DigSpeed(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        if (this.Manager.GetGame() == null) {
            return;
        }
        for (final Player player : this.Manager.GetGame().GetPlayers(true)) {
            if (!this.Kit.HasKit(player)) {
                continue;
            }
            this.Manager.GetBuffer().Factory().DigFast(this.GetName(), (LivingEntity)player, (LivingEntity)player, 1.9, 1, false, false, true);
        }
    }
}
