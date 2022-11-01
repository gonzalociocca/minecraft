/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDamageEvent;
import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.recharge.Recharge;
import mineultra.game.center.kit.Perk;


public class SMASHER extends Perk
{
    public SMASHER() {
        super("Smasher", new String[] { String.valueOf(C.cGray) + "Hitting blocks damages all surrounding blocks" });
    }
    
    @EventHandler
    public void BlockSmash(final BlockDamageEvent event) {
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (this.Manager.GetGame() == null) {
            return;
        }
        if (!this.Manager.GetGame().IsAlive(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, this.GetName(), 250L, false, false)) {
            return;
        }
        for (final Block block : UtilBlock.getSurrounding(event.getBlock(), false)) {
            final BlockDamageEvent blockDamage = new BlockDamageEvent(event.getPlayer(), block, event.getPlayer().getItemInHand(), false);
            this.Manager.GetPlugin().getServer().getPluginManager().callEvent((Event)blockDamage);
        }
        final BlockDamageEvent blockDamage2 = new BlockDamageEvent(event.getPlayer(), event.getBlock(), event.getPlayer().getItemInHand(), false);
        this.Manager.GetPlugin().getServer().getPluginManager().callEvent((Event)blockDamage2);
    }
    
    @EventHandler
    public void BlockSmash(final BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (this.Manager.GetGame() == null) {
            return;
        }
        if (!this.Manager.GetGame().IsAlive(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, this.GetName(), 50L, false, false)) {
            return;
        }
        for (final Block block : UtilBlock.getSurrounding(event.getBlock(), false)) {
            final BlockBreakEvent blockDamage = new BlockBreakEvent(block, player);
            this.Manager.GetPlugin().getServer().getPluginManager().callEvent((Event)blockDamage);
        }
    }
}
