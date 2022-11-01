package mineultra.game.center.kit.perks;

import java.util.HashMap;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import mineultra.game.center.kit.perks.data.FireflyData;
import java.util.HashSet;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilParticle;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.game.GameTeam;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TRYHARD extends Perk
{
    
    public TRYHARD() {
        super("TryHard", new String[] { String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " with Star to " + C.cGreen + "TryHard" });
    }
    HashMap<Player,Long> trys = new HashMap();
    @EventHandler
    public void Skill(final PlayerInteractEvent event) {
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
        if (!event.getPlayer().getItemInHand().getType().equals(Material.NETHER_STAR)) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.usable(player, this.GetName())) {
             return;
        }
        if(trys.containsKey(player)){
            return;
        }
        Recharge.Instance.useForce(player, this.GetName(), 10000L);
        UtilPlayer.message((Entity)player, F.main("Skill", "You used " + F.skill(this.GetName()) + "."));
GameTeam t = this.Manager.GetGame().GetTeam(player);
 MobDisguise de = new MobDisguise(DisguiseType.ZOMBIE,true,true);
 
 de.getWatcher().setCustomNameVisible(true);
 de.getWatcher().setCustomName(t.GetColor()+player.getName());
 
 DisguiseAPI.disguiseToAll(player, de);
 player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20*8,1));
 trys.put(player, System.currentTimeMillis());

    }
    
    @EventHandler
    public void update(UpdateEvent event){
        if(event.getType() != UpdateType.FAST){
            return;
        }
        if(trys.isEmpty()){
            return;
        }
        for(Player p : trys.keySet()){
 if(UtilTime.elapsed(trys.get(p), 10000)){
        DisguiseAPI.undisguiseToAll(p);
    UtilPlayer.message((Entity)p, F.main("Skill", "Expired " + F.skill(this.GetName()) + "."));
trys.remove(p);
    }
        }
    }
    
    @EventHandler
    public void custom(CustomDamageEvent event){
        if(event.GetDamagerPlayer(true) == null){
            return;
        }
        if(!Kit.HasKit(event.GetDamagerPlayer(true))){
            return;
        }
       if(!DisguiseAPI.isDisguised(event.GetDamagerPlayer(true))){
            return;
        }
        if(event.GetDamageePlayer() == null){
            return;
        }
 Player p =    event.GetDamageePlayer();
    p.getWorld().createExplosion(p.getLocation(), 0.6f);
    }
    
    @EventHandler
    public void custom2(CustomDamageEvent event){
        if(event.GetDamageePlayer() == null){
            return;
        }
        if(!Kit.HasKit(event.GetDamageePlayer())){
            return;
        }
        if(!DisguiseAPI.isDisguised(event.GetDamageePlayer())){
            return;
        }
   if(event.GetCause() == DamageCause.MAGIC
           || event.GetCause() == DamageCause.BLOCK_EXPLOSION
           || event.GetCause() == DamageCause.CUSTOM
           || event.GetCause() == DamageCause.ENTITY_EXPLOSION
           || event.GetCause() == DamageCause.CONTACT){
       event.SetCancelled("TryHard");
   }
    }
}
