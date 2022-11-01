package mineultra.game.center.kit.perks;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import mineultra.core.common.util.C;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;

public class SPEED extends Perk
{
    private int _level;
    
    public SPEED(final int level) {
        super("Speed", new String[] { String.valueOf(C.cGray) + "Permanent Speed " + (level + 1) });
        this._level = level;
    }
    
    @EventHandler
    public void DigSpeed(final UpdateEvent event) {
        if (event.getType() != UpdateType.SLOW) {
            return;
        }
        if (this.Manager.GetGame() == null) {
            return;
        }
        for (final Player player : this.Manager.GetGame().GetPlayers(true)) {
            if (!this.Kit.HasKit(player)) {
                continue;
            }
            this.Manager.GetBuffer().Factory().Speed(this.GetName(), (LivingEntity)player, (LivingEntity)player, 8.0, this._level, false, false, true);
        }
    }
}
