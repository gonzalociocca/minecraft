/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks;

import org.bukkit.event.EventHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.HashMap;
import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilGear;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;

public class RADAR extends Perk
{
    private HashMap<Player, Long> _lastTick;
    
    public RADAR() {
        super("Radar Scanner", new String[] { String.valueOf(C.cYellow) + "Hold Compass" + C.cGray + " to use " + C.cGreen + "Radar Scanner", "Ticks get faster when you are near a Hider!" });
        this._lastTick = new HashMap<Player, Long>();
    }
    
    @EventHandler
    public void Update(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player player = players[i];
            if (UtilGear.isMat(player.getItemInHand(), Material.COMPASS)) {
                if (this.Kit.HasKit(player)) {
                    if (this.Manager.IsAlive(player)) {
                        double closest = 999.0;
                        Player[] players2;
                        for (int length2 = (players2 = UtilServer.getPlayers()).length, j = 0; j < length2; ++j) {
                            final Player other = players2[j];
                            if (!other.equals(player)) {
                                if (this.Manager.IsAlive(other)) {
                                    if (this.Manager.GetColor(other) == ChatColor.AQUA) {
                                        final double dist = UtilMath.offset((Entity)other, (Entity)player);
                                        if (dist < closest) {
                                            closest = dist;
                                        }
                                    }
                                }
                            }
                        }
                        final double scale = Math.max(0.0, Math.min(1.0, (closest - 3.0) / 10.0));
                        if (this._lastTick.containsKey(player) && !UtilTime.elapsed(this._lastTick.get(player), (long)(2000.0 * scale)) && !Recharge.Instance.usable(player, "Radar")) {
                            return;
                        }
                        this._lastTick.put(player, System.currentTimeMillis());
                        Recharge.Instance.useForce(player, "Radar", (long)(2000.0 * scale));
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, (float)(2.0 - 1.0 * scale));
                        player.setCompassTarget(player.getLocation().add(Math.random() * 10.0 - 5.0, 0.0, Math.random() * 10.0 - 5.0));
                    }
                }
            }
        }
    }
}
