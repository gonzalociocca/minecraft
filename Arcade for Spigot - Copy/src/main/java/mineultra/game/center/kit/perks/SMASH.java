package mineultra.game.center.kit.perks;

import java.util.Iterator;
import org.bukkit.util.Vector;
import org.bukkit.entity.Projectile;
import org.bukkit.Sound;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Entity;
import java.util.HashSet;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;
import org.bukkit.Location;

public class SMASH extends Perk
{
    private HashSet<Entity> _arrows;
    private String _name;
    private double _power;
    private long _recharge;
    
    public SMASH(final String name, final double power, final long recharge) {
        super(name, new String[] { String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + name });
        this._arrows = new HashSet<Entity>();
        this._name = name;
        this._power = power;
        this._recharge = recharge;
    }
    
    @EventHandler
    public void Fire(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }
        if (event.getPlayer().getItemInHand().getType() != Material.DIAMOND_AXE) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, this._name, this._recharge, true, true)) {
            return;
        }
        Vector loca = event.getPlayer().getLocation().getDirection();
event.getPlayer().setVelocity(new Vector(loca.getX()*3,loca.getY()*2,loca.getZ()*3));
        UtilPlayer.message((Entity)player, F.main("Game", "You used " + F.skill(this._name) + "."));
    }
    
  
    

}