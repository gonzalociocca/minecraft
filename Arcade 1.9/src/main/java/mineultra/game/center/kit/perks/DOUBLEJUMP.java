/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilServer;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;

public class DOUBLEJUMP extends Perk
{
    private double _power;
    private double _heightMax;
    private boolean _control;
    
    public DOUBLEJUMP(final String name, final double power, final double heightLimit, final boolean control) {
        super("Jumper", new String[] { String.valueOf(C.cYellow) + "Tap Jump Twice" + C.cGray + " to " + C.cGreen + name });
        this._power = power;
        this._heightMax = heightLimit;
        this._control = control;
    }
    
    @EventHandler
    public void FlightHop(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        event.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);
        if (this._control) {
            UtilAction.velocity((Entity)player, this._power, 0.2, this._heightMax, true);
        }
        else {
            UtilAction.velocity((Entity)player, player.getLocation().getDirection(), this._power, true, this._power, 0.0, this._heightMax, true);
        }
        player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
    }
    
    @EventHandler
    public void FlightUpdate(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player player = players[i];
            if (player.getGameMode() != GameMode.CREATIVE) {
                if (this.Kit.HasKit(player)) {
                    if (UtilEnt.isGrounded((Entity)player) || UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN))) {
                        player.setAllowFlight(true);
                    }
                }
            }
        }
    }
}
