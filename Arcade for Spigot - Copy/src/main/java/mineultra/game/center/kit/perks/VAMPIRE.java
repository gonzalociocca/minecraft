package mineultra.game.center.kit.perks;

import org.bukkit.event.EventHandler;
import mineultra.game.center.game.Game;
import org.bukkit.entity.Player;
import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilPlayer;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import mineultra.game.center.kit.Perk;

public class VAMPIRE extends Perk
{
    private int _recover;
    
    public VAMPIRE(final int recover) {
        super("Vampire", new String[] { String.valueOf(C.cGray) + "You heal " + recover + "HP when you kill someone" });
        this._recover = recover;
    }
    
    @EventHandler
    public void PlayerKillAward(final CombatDeathEvent event) {
        final Game game = this.Manager.GetGame();
        if (game == null) {
            return;
        }
        if (!(event.GetEvent().getEntity() instanceof Player)) {
            return;
        }
        if (event.GetLog().GetKiller() == null) {
            return;
        }
        final Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
        if (killer == null) {
            return;
        }
        UtilPlayer.health(killer, this._recover);
    }
}
