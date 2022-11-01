/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.Effect;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.NautHashMap;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.recharge.Recharge;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.event.PerkLeapEvent;

public class LEAP extends Perk
{
    private String _name;
    private double _power;
    private double _heightMax;
    private long _recharge;
    private int _maxUses;
    private NautHashMap<String, Integer> _uses;
    
    public LEAP(final String name, final double power, final double heightLimit, final long recharge) {
        super("Leaper", new String[] { String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + name });
        this._uses = new NautHashMap<String, Integer>();
        this._name = name;
        this._power = power;
        this._heightMax = heightLimit;
        this._recharge = recharge;
        this._maxUses = 0;
    }
    
    public LEAP(final String name, final double power, final double heightLimit, final long recharge, final int uses) {
        super("Leaper", new String[] { String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + name + C.cGray + "  (" + C.cWhite + uses + " Charges" + C.cGray + ")" });
        this._uses = new NautHashMap<String, Integer>();
        this._name = name;
        this._power = power;
        this._heightMax = heightLimit;
        this._recharge = recharge;
        this._maxUses = uses;
    }
    
    @EventHandler
    public void Leap(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }
        if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE")) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (this._maxUses > 0) {
            if (!this._uses.containsKey(player.getName())) {
                this._uses.put(player.getName(), this._maxUses);
            }
            if (this._uses.get(player.getName()) <= 0) {
                UtilPlayer.message((Entity)player, F.main("Skill", "You cannot use " + F.skill(this._name) + " anymore."));
                return;
            }
        }
        if (!Recharge.Instance.use(player, this._name, this._recharge, true, true)) {
            return;
        }
        if (this._maxUses > 0) {
            int count = this._uses.get(player.getName());
            --count;
            player.setExp(Math.min(0.99f, count / this._maxUses));
            this._uses.put(player.getName(), count);
        }
        Entity ent = (Entity)player;
        if (player.getVehicle() != null && player.getVehicle() instanceof Horse) {
            ent = player.getVehicle();
        }
        UtilAction.velocity(ent, this._power, 0.2, this._heightMax, true);
        player.setFallDistance(0.0f);
        UtilPlayer.message((Entity)player, F.main("Skill", "You used " + F.skill(this._name) + "."));
        player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
        final PerkLeapEvent leapEvent = new PerkLeapEvent(player);
        UtilServer.getServer().getPluginManager().callEvent((Event)leapEvent);
    }
}
