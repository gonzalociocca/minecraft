package mineultra.game.center.kit.perks;

import org.bukkit.entity.Item;
import org.bukkit.Sound;
import org.bukkit.util.Vector;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Player;
import java.util.HashMap;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;

public class FLAMINGSWORD extends Perk
{
    private HashMap<Player, Long> _active;
    
    public FLAMINGSWORD() {
        super("Flaming Sword", new String[] { "Attacks ignite opponents for 4 seconds.", String.valueOf(C.cYellow) + "Hold Block" + C.cGray + " to use " + C.cGreen + "Inferno" });
        this._active = new HashMap<Player, Long>();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void IgniteTarget(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(false);
        if (damager == null) {
            return;
        }
        if (!this.Kit.HasKit(damager)) {
            return;
        }
        this.Manager.GetBuffer().Factory().Ignite("Flaming Sword", event.GetDamageeEntity(), (LivingEntity)damager, 4.0, false, false);
    }
    
    @EventHandler
    public void Activate(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        if (!event.getPlayer().getItemInHand().getType().toString().contains("_SWORD")) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, "Inferno", 4000L, true, true)) {
            return;
        }
        this._active.put(player, System.currentTimeMillis());
        UtilPlayer.message((Entity)player, F.main("Skill", "You used " + F.skill("Inferno") + "."));
    }
    
    @EventHandler
    public void Update(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player cur = players[i];
            if (this._active.containsKey(cur)) {
                if (!cur.isBlocking()) {
                    this._active.remove(cur);
                }
                else if (UtilTime.elapsed(this._active.get(cur), 1500L)) {
                    this._active.remove(cur);
                }
                else {
                    final Item fire = cur.getWorld().dropItem(cur.getEyeLocation(), ItemStackFactory.Instance.CreateStack(Material.FIRE));
                    this.Manager.GetFire().Add(fire, (LivingEntity)cur, 0.7, 0.0, 0.5, 1, "Inferno");
                    fire.teleport(cur.getEyeLocation());
                    final double x = 0.07 - UtilMath.r(14) / 100.0;
                    final double y = 0.07 - UtilMath.r(14) / 100.0;
                    final double z = 0.07 - UtilMath.r(14) / 100.0;
                    fire.setVelocity(cur.getLocation().getDirection().add(new Vector(x, y, z)).multiply(1.6));
                    cur.getWorld().playSound(cur.getLocation(), Sound.GHAST_FIREBALL, 0.1f, 1.0f);
                }
            }
        }
    }
}
