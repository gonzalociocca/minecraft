package mineultra.minecraft.game.core.damage;

import net.minecraft.server.v1_9_R1.*;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Entity;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.F;
import mineultra.core.common.util.MSGUtil;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilEvent;
import mineultra.core.common.util.UtilGear;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.npc.NpcManager;
import mineultra.game.center.centerManager;
import mineultra.minecraft.game.core.combat.CombatManager;
import mineultra.minecraft.game.core.damage.compatibility.NpcProtectListener;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;

public class DamageManager extends MiniPlugin
{
    private CombatManager _combatManager;

    public boolean UseSimpleWeaponDamage;
    public boolean DisableDamageChanges;
    private boolean _enabled;
    private centerManager mana;

    public DamageManager(final JavaPlugin plugin, centerManager manager,final CombatManager combatManager, final NpcManager npcManager) {
        super("Damage Manager", plugin);
        mana = manager;
        this.UseSimpleWeaponDamage = false;
        this.DisableDamageChanges = false;
        this._enabled = true;
        this._combatManager = combatManager;

        this.RegisterEvents((Listener)new NpcProtectListener(npcManager));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void StartDamageEvent(final EntityDamageEvent event) {
        if (!this._enabled) {
            return;
        }
        boolean preCancel = false;
        if (event.isCancelled()) {
            preCancel = true;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        final LivingEntity damagee = this.GetDamageeEntity(event);
        final LivingEntity damager = UtilEvent.GetDamagerEntity(event, true);
        final Projectile projectile = this.GetProjectile(event);
        if (projectile instanceof Fish) {
            return;
        }
        if (!this.DisableDamageChanges) {
            this.WeaponDamage(event, damager);
        }
        this.NewDamageEvent(damagee, damager, projectile, event.getCause(), event.getDamage(), true, false, false, null, null, preCancel);
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void removeDemArrowsCrazyMan(final EntityDamageEvent event) {
        if (event.isCancelled()) {
            final Projectile projectile = this.GetProjectile(event);
            if (projectile instanceof Arrow) {
                projectile.teleport(new Location(projectile.getWorld(), 0.0, 0.0, 0.0));
                projectile.remove();
            }
        }
    }
    
    public void NewDamageEvent(final LivingEntity damagee, final LivingEntity damager, final Projectile proj, final EntityDamageEvent.DamageCause cause, final double damage, final boolean knockback, final boolean ignoreRate, final boolean ignoreArmor, final String source, final String reason) {
        this.NewDamageEvent(damagee, damager, proj, cause, damage, knockback, ignoreRate, ignoreArmor, source, reason, false);
    }
    
    public void NewDamageEvent(final LivingEntity damagee, final LivingEntity damager, final Projectile proj, final EntityDamageEvent.DamageCause cause, final double damage, final boolean knockback, final boolean ignoreRate, final boolean ignoreArmor, final String source, final String reason, final boolean cancelled) {
        this._plugin.getServer().getPluginManager().callEvent((Event)new CustomDamageEvent(damagee, damager, proj, cause, damage, knockback, ignoreRate, ignoreArmor, source, reason, cancelled));
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void CancelDamageEvent(final CustomDamageEvent event) {
        if (event.GetDamageePlayer() != null) {
            final Player damagee = event.GetDamageePlayer();
            if (damagee.getGameMode() != GameMode.SURVIVAL) {
                event.SetCancelled("Damagee in Creative");
                return;
            }
            if (!event.IgnoreRate() && !this._combatManager.Get(damagee.getName()).CanBeHurtBy(event.GetDamagerEntity(true))) {
                event.SetCancelled("World/Monster Damage Rate");
                return;
            }
        }
        if (event.GetDamagerPlayer(true) != null) {
            final Player damager = event.GetDamagerPlayer(true);
            if (damager.getGameMode() != GameMode.SURVIVAL) {
                event.SetCancelled("Damager in Creative");
                return;
            }
            if (!event.IgnoreRate() && !this._combatManager.Get(damager.getName()).CanHurt(event.GetDamageeEntity())) {
                event.SetCancelled("PvP Damage Rate");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void EndDamageEvent(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }

        this.Damage(event);
        if (event.GetProjectile() != null && event.GetProjectile() instanceof Arrow) {
            final Player player = event.GetDamagerPlayer(true);
            if (player != null) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0.5f);
            }
        }
    }
    
    private void Damage(final CustomDamageEvent event) {
        if (event.GetDamageeEntity() == null) {
            return;
        }
        if (event.GetDamageeEntity().getHealth() <= 0.00 ) {
            return;
        }
        if (event.GetDamageePlayer() != null) {
            this._combatManager.AddAttack(event);
        }
        if (event.GetDamagerPlayer(true) != null && event.DisplayDamageToLevel() && event.GetCause() != EntityDamageEvent.DamageCause.THORNS) {
            event.GetDamagerPlayer(true).setLevel((int)event.GetDamage());
        }
        try {
            double bruteBonus = 0.0;
            if (event.IsBrute() && (event.GetCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.GetCause() == EntityDamageEvent.DamageCause.PROJECTILE || event.GetCause() == EntityDamageEvent.DamageCause.CUSTOM)) {
                bruteBonus = Math.min(8.0, event.GetDamage());
            }
            this.HandleDamage(event.GetDamageeEntity(), event.GetDamagerEntity(true), event.GetCause(), (float)(event.GetDamage() + bruteBonus), event.IgnoreArmor());
            event.GetDamageeEntity().playEffect(EntityEffect.HURT);
            if (event.GetCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                ((CraftLivingEntity)event.GetDamageeEntity()).getHandle().k(((CraftLivingEntity)event.GetDamageeEntity()).getHandle().ay + 1);
            }
            
            double knockback = event.GetDamage();
            if (knockback < 2.0) {
               
                knockback = 2.0;
            }
            knockback = Math.log10(knockback);
            for (final double cur : event.GetKnockback().values()) {
                knockback *= cur;
            }
            if (event.IsKnockback() && event.GetDamagerEntity(true) != null) {
                final Vector trajectory = UtilAlg.getTrajectory2d((Entity)event.GetDamagerEntity(true), (Entity)event.GetDamageeEntity());
                trajectory.multiply(0.6 * knockback);
                trajectory.setY(Math.abs(trajectory.getY()));
                UtilAction.velocity((Entity)event.GetDamageeEntity(), trajectory, 0.2 + trajectory.length() * 0.8, false, 0.0, Math.abs(0.2 * knockback), 0.4 + 0.04 * knockback, true);
            }
            this.DisplayDamage(event);
        }
        catch (IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DamageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void DisplayDamage(final CustomDamageEvent event) {
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player player = players[i];
            if (UtilGear.isMat(player.getItemInHand(), Material.BOOK)) {
                UtilPlayer.message((Entity)player, " ");
                UtilPlayer.message((Entity)player, MSGUtil.getLineSpacer());
                UtilPlayer.message((Entity)player, String.valueOf(F.elem("Reason ")) + event.GetReason());
                UtilPlayer.message((Entity)player, String.valueOf(F.elem("Cause ")) + event.GetCause());
                UtilPlayer.message((Entity)player, String.valueOf(F.elem("Damager ")) + UtilEnt.getName((Entity)event.GetDamagerEntity(true)));
                UtilPlayer.message((Entity)player, String.valueOf(F.elem("Damagee ")) + UtilEnt.getName((Entity)event.GetDamageeEntity()));
                UtilPlayer.message((Entity)player, String.valueOf(F.elem("Projectile ")) + UtilEnt.getName((Entity)event.GetProjectile()));
                UtilPlayer.message((Entity)player, String.valueOf(F.elem("Damage ")) + event.GetDamage());
                UtilPlayer.message((Entity)player, String.valueOf(F.elem("Damage Initial ")) + event.GetDamageInitial());
             /*   for (final DamageChange cur : event.GetDamageMod()) {
                    UtilPlayer.message((Entity)player, String.valueOf(F.elem("Mod ")) + cur.GetDamage() + " - " + cur.GetReason() + " by " + cur.GetSource());
                }
                for (final DamageChange cur : event.GetDamageMult()) {
                    UtilPlayer.message((Entity)player, String.valueOf(F.elem("Mult ")) + cur.GetDamage() + " - " + cur.GetReason() + " by " + cur.GetSource());
                }*/
                for (final String cur2 : event.GetKnockback().keySet()) {
                    UtilPlayer.message((Entity)player, String.valueOf(F.elem("Knockback ")) + cur2 + " = " + event.GetKnockback().get(cur2));
                }
                for (final String cur2 : event.GetCancellers()) {
                    UtilPlayer.message((Entity)player, String.valueOf(F.elem("Cancel ")) + cur2);
                }
            }
        }
    }
    
    private void HandleDamage(final LivingEntity damagee, final LivingEntity damager, final EntityDamageEvent.DamageCause cause, final float damage, final boolean ignoreArmor) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final EntityLiving entityDamagee = ((CraftLivingEntity)damagee).getHandle();
        EntityLiving entityDamager = null;
        if (damager != null) {
            entityDamager = ((CraftLivingEntity)damager).getHandle();
        }
        entityDamagee.aG = 1.5f;
        if (entityDamagee.noDamageTicks > entityDamagee.maxNoDamageTicks / 2.0f) {
            if (damage <= entityDamagee.lastDamage) {
                return;
            }
            this.ApplyDamage(entityDamagee, damage - entityDamagee.lastDamage, ignoreArmor);
            entityDamagee.lastDamage = damage;
        }
        else {
            entityDamagee.lastDamage = damage;
           // wat entityDamagee.aw = entityDamagee.getHealth();
            this.ApplyDamage(entityDamagee, damage, ignoreArmor);
        }
        if (entityDamager != null) {
            entityDamagee.b(entityDamager);
        }
        if (entityDamager != null && entityDamager instanceof EntityHuman) {

            entityDamagee.lastDamage=100;// this. should work ._lastDamageByPlayerTime.setInt(entityDamagee, 100);
            entityDamagee.killer = (EntityHuman)entityDamager;
        }
        if (entityDamagee.getHealth() <= 0.000f) {
            if (entityDamager != null) {
                if (entityDamager instanceof EntityHuman) {
                    entityDamagee.die(DamageSource.playerAttack((EntityHuman)entityDamager));
                }
                else if (entityDamager instanceof EntityLiving) {
                    entityDamagee.die(DamageSource.mobAttack(entityDamager));
                }
                else {
                    entityDamagee.die(DamageSource.GENERIC);
                }
            }
            else {
                entityDamagee.die(DamageSource.GENERIC);
            }
        }
    }
    
    @EventHandler
    public void DamageSound(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.GetCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        final LivingEntity damagee = event.GetDamageeEntity();
        if (damagee == null) {
            return;
        }
   /*insert disguise sounds here*/
        
        Sound sound = Sound.ENTITY_PLAYER_HURT;
        final float vol = 1.0f;
        float pitch = 1.0f;
        if (damagee instanceof Player) {
            final Player player = (Player)damagee;
            final double r = Math.random();
            ItemStack stack = null;
            if (r > 0.5) {
                stack = player.getInventory().getChestplate();
            }
            else if (r > 0.25) {
                stack = player.getInventory().getLeggings();
            }
            else if (r > 0.1) {
                stack = player.getInventory().getHelmet();
            }
            else {
                stack = player.getInventory().getBoots();
            }
            if (stack != null) {
                if (stack.getType().toString().contains("LEATHER_")) {
                    sound = Sound.ENTITY_SKELETON_SHOOT;
                    pitch = 2.0f;
                }
                else if (stack.getType().toString().contains("CHAINMAIL_")) {
                    sound = Sound.ENTITY_ITEM_BREAK;
                    pitch = 1.4f;
                }
                else if (stack.getType().toString().contains("GOLD_")) {
                    sound = Sound.ENTITY_ITEM_BREAK;
                    pitch = 1.8f;
                }
                else if (stack.getType().toString().contains("IRON_")) {
                    sound = Sound.ENTITY_BLAZE_SHOOT;
                    pitch = 0.7f;
                }
                else if (stack.getType().toString().contains("DIAMOND_")) {
                    sound = Sound.ENTITY_BLAZE_SHOOT;
                    pitch = 0.9f;
                }
            }
            damagee.getWorld().playSound(damagee.getLocation(), sound, vol, pitch);
            return;
        }
        UtilEnt.PlayDamageSound(damagee);
    }
    public Float itemvalue(ItemStack val){
      float vl = 0F;
      if(val != null){
          if(val.getType() != null){
              if(val.getType().toString().toLowerCase().contains("diamond")){
                  return 4F;
              }
              if(val.getType().toString().toLowerCase().contains("iron")){
                  return 3F;
              }
              if(val.getType().toString().toLowerCase().contains("chain")){
                  return 2.5F;
              }
               if(val.getType().toString().toLowerCase().contains("gold")){
                  return 3F;
              }
                if(val.getType().toString().toLowerCase().contains("leather")){
                  return 1.5F;
              }
          }
      }
      return vl;
    }
    
    public Float damagevalue(EntityLiving pe){

        if(pe == null){
            return 2F;
        }
        
      Float value = 10F;
      try{
        if(pe.getEquipment(EnumItemSlot.HEAD) != null){
  ItemStack helmet = CraftItemStack.asNewCraftStack(pe.getEquipment(EnumItemSlot.HEAD).getItem());
value += this.itemvalue(helmet);
if(helmet.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)){
    value += helmet.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
}
         }
        if(pe.getEquipment(EnumItemSlot.CHEST) != null){
  ItemStack helmet = CraftItemStack.asNewCraftStack(pe.getEquipment(EnumItemSlot.CHEST).getItem());
value += this.itemvalue(helmet);
if(helmet.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)){
    value += helmet.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
}
         }
        if(pe.getEquipment(EnumItemSlot.LEGS) != null){
  ItemStack helmet = CraftItemStack.asNewCraftStack(pe.getEquipment(EnumItemSlot.LEGS).getItem());
value += this.itemvalue(helmet);
if(helmet.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)){
    value += helmet.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
}
         }
        if(pe.getEquipment(EnumItemSlot.FEET) != null){
  ItemStack helmet = CraftItemStack.asNewCraftStack(pe.getEquipment(EnumItemSlot.FEET).getItem());
value += this.itemvalue(helmet);
if(helmet.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)){
    value += helmet.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
}
         }
      
      
      }catch(Exception e){
             e.printStackTrace();
             return -1F;
         }
        
        return value;
    }
    
    public Float getFalseDamage(EntityLiving et, float dmg){
       float damage = dmg;
     
float damagevalue = this.damagevalue(et);
if(damagevalue == -1F){
    return -1F;
}
        float div = 0F;
        if(et!= null){
            if(et.getBukkitEntity() instanceof CraftLivingEntity){
                for (PotionEffect potionEffect : ((CraftLivingEntity) et.getBukkitEntity()).getActivePotionEffects()) {
                    if(potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE)){
                        div+=potionEffect.getAmplifier();
                    break;
                    }
                }
            }else if(et.getBukkitEntity() instanceof CraftPlayer){
                for (PotionEffect potionEffect : ((CraftPlayer) et.getBukkitEntity()).getActivePotionEffects()) {
                    if(potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE)){
                        div+=potionEffect.getAmplifier();
                        break;
                    }
                }
            }
        }
        if(div > 0F){
            div = div/2;
        }

          Float divide = damagevalue / 10F+div;
          damage = ((damage) /divide);
          
       
        return damage;
        
        
    }
    
    private void ApplyDamage(final EntityLiving entityLiving, float damage, final boolean ignoreArmor) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
float fakehealth =(entityLiving.getHealth()) - (damage/4);
float fakedamage = getFalseDamage(entityLiving,damage);
if(fakedamage == -1F){
    entityLiving.setHealth(fakehealth);
    return;
}

try{
fakehealth = entityLiving.getHealth() - fakedamage;
}catch(Exception e){
    
}    
               
   
entityLiving.setHealth(fakehealth);
    }
    
    private void WeaponDamage(final EntityDamageEvent event, final LivingEntity ent) {
        if (!(ent instanceof Player)) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final Player damager = (Player)ent;
        if (this.UseSimpleWeaponDamage) {
            if (event.getDamage() > 1.0) {
                event.setDamage(event.getDamage() - 1.0);
            }
            if (damager.getItemInHand().getType().name().contains("GOLD_")) {
                event.setDamage(event.getDamage() + 2.0);
            }
            return;
        }
        if (damager.getItemInHand() == null || !UtilGear.isWeapon(damager.getItemInHand())) {
            event.setDamage(1);
            return;
        }
        final Material mat = damager.getItemInHand().getType();
        int damage = 6;
        if (mat.name().contains("WOOD")) {
            damage -= 3;
        }
        else if (mat.name().contains("STONE")) {
            damage -= 2;
        }
        else if (mat.name().contains("DIAMOND")) {
            ++damage;
        }
        else if (mat.name().contains("GOLD")) {
            damage += 0;
        }
        event.setDamage(damage);
    }
    
    private LivingEntity GetDamageeEntity(final EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            return (LivingEntity)event.getEntity();
        }
        return null;
    }
    
    private Projectile GetProjectile(final EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return null;
        }
        final EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent)event;
        if (eventEE.getDamager() instanceof Projectile) {
            return (Projectile)eventEE.getDamager();
        }
        return null;
    }
    
    public void SetEnabled(final boolean var) {
        this._enabled = var;
    }
    
    public CombatManager GetCombatManager() {
        return this._combatManager;
    }
}
