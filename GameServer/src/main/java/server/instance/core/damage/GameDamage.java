package server.instance.core.damage;

import com.google.gson.annotations.Expose;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import server.instance.GameServer;
import server.instance.core.combat.GameCombat;
import server.util.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class GameDamage {

    private GameCombat _combat = new GameCombat();
    protected Field _lastDamageByPlayerTime;
    protected Method _k;
    @Expose public boolean UseSimpleWeaponDamage = false;
    @Expose public boolean DisableDamageChanges = false;
    @Expose
    private boolean _enabled = true;


    public GameDamage() {
        try {
            this._lastDamageByPlayerTime = EntityLiving.class.getDeclaredField("lastDamageByPlayerTime");
            this._lastDamageByPlayerTime.setAccessible(true);
            this._k = EntityLiving.class.getDeclaredMethod("damageArmor", Float.TYPE);
            this._k.setAccessible(true);
        } catch (Exception var7) {
            var7.printStackTrace();
        }
    }


    public void checkDamageStart(GameServer game, EntityDamageEvent event) {
        if(this._enabled) {
            boolean preCancel = false;
            if(event.isCancelled()) {
                preCancel = true;
            }

            if(event.getEntity() instanceof LivingEntity) {
                LivingEntity damagee = this.GetDamageeEntity(event);
                LivingEntity damager = UtilEvent.GetDamagerEntity(event, true);
                Projectile projectile = this.GetProjectile(event);
                if(!(projectile instanceof Fish)) {
                    if(!this.DisableDamageChanges) {
                        this.weaponDamage(event, damager);
                    }

                    double damage = event.getDamage();
                    if(projectile != null && projectile instanceof Arrow) {
                        damage = projectile.getVelocity().length() * 3.0D;
                    }

                    this.newDamageEvent(game, damagee, damager, projectile, event.getCause(), damage, true, false, false, null, null, preCancel);
                    event.setCancelled(true);
                }
            }
        }
    }

    public void checkRemoveDemArrowsCrazyMan(EntityDamageEvent event) {
        if(event.isCancelled()) {
            Projectile projectile = this.GetProjectile(event);
            if(projectile instanceof Arrow) {
                projectile.teleport(new Location(projectile.getWorld(), 0.0D, 0.0D, 0.0D));
                projectile.remove();
            }
        }

    }

    public void newDamageEvent(GameServer game, LivingEntity damagee, LivingEntity damager, Projectile proj, DamageCause cause, double damage, boolean knockback, boolean ignoreRate, boolean ignoreArmor, String source, String reason) {
        this.newDamageEvent(game, damagee, damager, proj, cause, damage, knockback, ignoreRate, ignoreArmor, source, reason, false);
    }

    public void newDamageEvent(GameServer game, LivingEntity damagee, LivingEntity damager, Projectile proj, DamageCause cause, double damage, boolean knockback, boolean ignoreRate, boolean ignoreArmor, String source, String reason, boolean cancelled) {
        Bukkit.getServer().getPluginManager().callEvent(new CustomDamageEvent(game, damagee, damager, proj, cause, damage, knockback, ignoreRate, ignoreArmor, source, reason, cancelled));
    }

    public void checkDamageCancel(GameServer game, CustomDamageEvent event) {
        if(event.getDamageeEntity().getHealth() <= 0.0D) {
            event.setCancelled("0 Health");
        } else {
            Player damager;
            if(event.getDamageePlayer() != null) {
                damager = event.getDamageePlayer();
                if(damager.getGameMode() != GameMode.SURVIVAL) {
                    event.setCancelled("Damagee in Creative");
                    return;
                }

                if(!game.isAlive(damager)) {
                    event.setCancelled("Damagee in Spectator");
                    return;
                }

                if(!event.ignoreRate() && !getCombat().get(damager.getName()).CanBeHurtBy(event.getDamagerEntity(true))) {
                    event.setCancelled("World/Monster Damage Rate");
                    return;
                }
            }

            if(event.getDamagerPlayer(true) != null) {
                damager = event.getDamagerPlayer(true);
                if(damager.getGameMode() != GameMode.SURVIVAL) {
                    event.setCancelled("Damager in Creative");
                    return;
                }

                if(!event.ignoreRate() && !getCombat().get(damager.getName()).CanHurt(event.getDamageeEntity())) {
                    event.setCancelled("PvP Damage Rate");
                    return;
                }
            }

        }
    }

    public void checkEnchants(GameServer game, CustomDamageEvent event) {
        if(!event.isCancelled()) {
            Player damagee = event.getDamageePlayer();
            if(damagee != null) {
                ItemStack[] e;
                int enchants = (e = damagee.getInventory().getArmorContents()).length;

                for(int stack = 0; stack < enchants; ++stack) {
                    ItemStack damager = e[stack];
                    if(damager != null) {
                        Map enchants1 = damager.getEnchantments();
                        Iterator var9 = enchants1.keySet().iterator();

                        while(var9.hasNext()) {
                            Enchantment e1 = (Enchantment)var9.next();
                            if(e1.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                                event.addMod("Ench Prot", damagee.getName(), 0.5D * (double)((Integer)enchants1.get(e1)).intValue(), false);
                            } else if(e1.equals(Enchantment.PROTECTION_FIRE) && event.getCause() == DamageCause.FIRE && event.getCause() == DamageCause.FIRE_TICK && event.getCause() == DamageCause.LAVA) {
                                event.addMod("Ench Prot", damagee.getName(), 0.5D * (double)((Integer)enchants1.get(e1)).intValue(), false);
                            } else if(e1.equals(Enchantment.PROTECTION_FALL) && event.getCause() == DamageCause.FALL) {
                                event.addMod("Ench Prot", damagee.getName(), 0.5D * (double)((Integer)enchants1.get(e1)).intValue(), false);
                            } else if(e1.equals(Enchantment.PROTECTION_EXPLOSIONS) && event.getCause() == DamageCause.ENTITY_EXPLOSION) {
                                event.addMod("Ench Prot", damagee.getName(), 0.5D * (double)((Integer)enchants1.get(e1)).intValue(), false);
                            } else if(e1.equals(Enchantment.PROTECTION_PROJECTILE) && event.getCause() == DamageCause.PROJECTILE) {
                                event.addMod("Ench Prot", damagee.getName(), 0.5D * (double)((Integer)enchants1.get(e1)).intValue(), false);
                            }
                        }
                    }
                }
            }

            Player var10 = event.getDamagerPlayer(true);
            if(var10 != null) {
                ItemStack var11 = var10.getItemInHand();
                if(var11 == null) {
                    return;
                }

                Map var12 = var11.getEnchantments();
                Iterator var14 = var12.keySet().iterator();

                while(var14.hasNext()) {
                    Enchantment var13 = (Enchantment)var14.next();
                    if(!var13.equals(Enchantment.ARROW_KNOCKBACK) && !var13.equals(Enchantment.KNOCKBACK)) {
                        if(var13.equals(Enchantment.ARROW_DAMAGE)) {
                            event.addMod("Enchant", "Ench Damage", 0.5D * (double)((Integer)var12.get(var13)).intValue(), true);
                        } else if((var13.equals(Enchantment.ARROW_FIRE) || var13.equals(Enchantment.FIRE_ASPECT)) && game.getBuffer() != null) {
                            game.getBuffer().factory().Ignite(game, "Ench Fire", event.getDamageeEntity(), var10, 1.0D * (double) ((Integer) var12.get(var13)).intValue(), false, false);
                        }
                    } else {
                        event.addKnockback("Ench Knockback", 1.0D + 0.5D * (double)((Integer)var12.get(var13)).intValue());
                    }
                }
            }

        }
    }

    public void checkProjectileSound(CustomDamageEvent event) {
        if(!event.isCancelled() && event.getDamage() > 0.0D) {
            Damage(event);
            if(event.getProjectile() != null && event.getProjectile() instanceof Arrow) {
                Player player = event.getDamagerPlayer(true);
                if(player != null) {
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.5F, 0.5F);
                }
            }
        }

        //this.displayDamage(event);
    }

    private void Damage(CustomDamageEvent event) {
        if(event.getDamageeEntity() != null) {
            if(event.getDamageeEntity().getHealth() > 0.0D) {
                if(event.getDamageePlayer() != null) {
                    getCombat().addAttack(event);
                }

                if(event.getDamagerPlayer(true) != null && event.displayDamageToLevel() && event.getCause() != DamageCause.THORNS) {
                    event.getDamagerPlayer(true).setLevel((int)event.getDamage());
                }

                try {
                    double e = 0.0D;
                    if(event.isBrute() && (event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE || event.getCause() == DamageCause.CUSTOM)) {
                        e = Math.min(8.0D, event.getDamage() * 2.0D);
                    }

                    this.HandleDamage(event.getDamageeEntity(), event.getDamagerEntity(true), event.getCause(), (float)(event.getDamage() + e), event.ignoreArmor());
                    event.getDamageeEntity().playEffect(EntityEffect.HURT);
                    if(event.getCause() == DamageCause.PROJECTILE) {
                        DataWatcher watcher = ((CraftLivingEntity)event.getDamageeEntity()).getHandle().getDataWatcher();
                        watcher.watch(9, Byte.valueOf((byte)(watcher.getByte(9) + 1))); //Play hurt i guess.
                    }

                    if(event.isKnockback() && event.getDamagerEntity(true) != null) {
                        double knockback = event.getDamage();
                        if(knockback < 2.0D) {
                            knockback = 2.0D;
                        }

                        knockback = Math.log10(knockback);

                        double origin;
                        for(Iterator vel = event.getKnockback().values().iterator(); vel.hasNext(); knockback *= origin) {
                            origin = ((Double)vel.next()).doubleValue();
                        }

                        Location origin1 = event.getDamagerEntity(true).getLocation();
                        if(event.getKnockbackOrigin() != null) {
                            origin1 = event.getKnockbackOrigin();
                        }

                        Vector trajectory = UtilAlg.getTrajectory2d(origin1, event.getDamageeEntity().getLocation());
                        trajectory.multiply(0.6D * knockback);
                        trajectory.setY(Math.abs(trajectory.getY()));
                        if(event.getDamageeEntity() instanceof Player && UtilGear.isMat(((Player)event.getDamageeEntity()).getItemInHand(), Material.SUGAR)) {
                            Bukkit.broadcastMessage("--------- " + UtilEnt.getName(event.getDamageeEntity()) + " hurt by " + UtilEnt.getName(event.getDamagerEntity(true)) + "-----------");
                            Bukkit.broadcastMessage(F.main("Debug", "Damage: " + event.getDamage()));
                        }

                        double vel1 = 0.2D + trajectory.length() * 0.8D;
                        UtilAction.velocity(event.getDamageeEntity(), trajectory, vel1, false, 0.0D, Math.abs(0.2D * knockback), 0.4D + 0.04D * knockback, true);
                    }
                } catch (IllegalAccessException var10) {
                    var10.printStackTrace();
                } catch (IllegalArgumentException var11) {
                    var11.printStackTrace();
                } catch (InvocationTargetException var12) {
                    var12.printStackTrace();
                }

            }
        }
    }

    private void HandleDamage(LivingEntity damagee, LivingEntity damager, DamageCause cause, float damage, boolean ignoreArmor) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        EntityLiving entityDamagee = ((CraftLivingEntity)damagee).getHandle();
        EntityLiving entityDamager = null;
        if(damager != null) {
            entityDamager = ((CraftLivingEntity)damager).getHandle();
        }

        entityDamagee.aG = 1.5F;
        if((float)entityDamagee.noDamageTicks > (float)entityDamagee.maxNoDamageTicks / 2.0F) {
            if(damage <= entityDamagee.lastDamage) {
                return;
            }

            this.applyDamage(entityDamagee, damage - entityDamagee.lastDamage, ignoreArmor);
            entityDamagee.lastDamage = damage;
        } else {
            entityDamagee.lastDamage = damage;
            entityDamagee.aw = entityDamagee.getHealth();
            this.applyDamage(entityDamagee, damage, ignoreArmor);
        }

        if(entityDamager != null) {
            entityDamagee.b(entityDamager);
        }

        if(entityDamager != null && entityDamager instanceof EntityHuman) {
            this._lastDamageByPlayerTime.setInt(entityDamagee, 100);
            entityDamagee.killer = (EntityHuman)entityDamager;
        }

        if(entityDamagee.getHealth() <= 0.0F) {
            if(entityDamager != null) {
                if(entityDamager instanceof EntityHuman) {
                    entityDamagee.die(DamageSource.playerAttack((EntityHuman)entityDamager));
                } else if(entityDamager instanceof EntityLiving) {
                    entityDamagee.die(DamageSource.mobAttack(entityDamager));
                } else {
                    entityDamagee.die(DamageSource.GENERIC);
                }
            } else {
                entityDamagee.die(DamageSource.GENERIC);
            }
        }

    }

    public void checkDamageSound(GameServer game, CustomDamageEvent event) {
        if(!event.isCancelled()) {
            if(event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE) {
                LivingEntity damagee = event.getDamageeEntity();
                if(damagee != null) {
                    if(game.getDisguises().isDisguised(damagee)) {
                        game.getDisguises().playDisguiseHurtSound(damagee);
                    } else {
                        Sound sound = Sound.HURT_FLESH;
                        float vol = 1.0F;
                        float pitch = 1.0F;
                        if(damagee instanceof Player) {
                            Player player = (Player)damagee;
                            double r = Math.random();
                            ItemStack stack = null;
                            if(r > 0.5D) {
                                stack = player.getInventory().getChestplate();
                            } else if(r > 0.25D) {
                                stack = player.getInventory().getLeggings();
                            } else if(r > 0.1D) {
                                stack = player.getInventory().getHelmet();
                            } else {
                                stack = player.getInventory().getBoots();
                            }

                            if(stack != null) {
                                if(stack.getType().toString().contains("LEATHER_")) {
                                    sound = Sound.SHOOT_ARROW;
                                    pitch = 2.0F;
                                } else if(stack.getType().toString().contains("CHAINMAIL_")) {
                                    sound = Sound.ITEM_BREAK;
                                    pitch = 1.4F;
                                } else if(stack.getType().toString().contains("GOLD_")) {
                                    sound = Sound.ITEM_BREAK;
                                    pitch = 1.8F;
                                } else if(stack.getType().toString().contains("IRON_")) {
                                    sound = Sound.BLAZE_HIT;
                                    pitch = 0.7F;
                                } else if(stack.getType().toString().contains("DIAMOND_")) {
                                    sound = Sound.BLAZE_HIT;
                                    pitch = 0.9F;
                                }
                            }

                            damagee.getWorld().playSound(damagee.getLocation(), sound, vol, pitch);
                        } else {
                            UtilEnt.PlayDamageSound(damagee);
                        }
                    }
                }
            }
        }
    }

    private void applyDamage(EntityLiving entityLiving, float damage, boolean ignoreArmor) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if(!ignoreArmor) {
            int j = 25 - entityLiving.br();
            float k = damage * (float)j;
            this._k.invoke(entityLiving, Float.valueOf(damage));
            damage = k / 25.0F;
        }

        entityLiving.setHealth(entityLiving.getHealth() - damage);
    }

    private void weaponDamage(EntityDamageEvent event, LivingEntity ent) {
        if(ent instanceof Player) {
            if(event.getCause() == DamageCause.ENTITY_ATTACK) {
                Player damager = (Player)ent;
                if(this.UseSimpleWeaponDamage) {
                    if(event.getDamage() > 1.0D) {
                        event.setDamage(event.getDamage() - 1.0D);
                    }

                    if(UtilGear.isWeapon(damager.getItemInHand()) && damager.getItemInHand().getType().name().contains("GOLD_")) {
                        event.setDamage(event.getDamage() + 2.0D);
                    }

                } else if(damager.getItemInHand() != null && UtilGear.isWeapon(damager.getItemInHand())) {
                    Material mat = damager.getItemInHand().getType();
                    int damage = 6;
                    if(mat.name().contains("WOOD")) {
                        damage -= 3;
                    } else if(mat.name().contains("STONE")) {
                        damage -= 2;
                    } else if(mat.name().contains("DIAMOND")) {
                        ++damage;
                    } else if(mat.name().contains("GOLD")) {
                        damage += 0;
                    }

                    event.setDamage((double)damage);
                } else {
                    event.setDamage(1.0D);
                }
            }
        }
    }

    private LivingEntity GetDamageeEntity(EntityDamageEvent event) {
        return event.getEntity() instanceof LivingEntity?(LivingEntity)event.getEntity():null;
    }

    private Projectile GetProjectile(EntityDamageEvent event) {
        if(!(event instanceof EntityDamageByEntityEvent)) {
            return null;
        } else {
            EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent)event;
            return eventEE.getDamager() instanceof Projectile?(Projectile)eventEE.getDamager():null;
        }
    }

    public void SetEnabled(boolean var) {
        this._enabled = var;
    }

    public GameCombat getCombat() {
        return this._combat;
    }

    public void onCustomDamage(GameServer game, CustomDamageEvent event){
        checkDamageCancel(game, event);
        checkEnchants(game, event);
        checkDamageSound(game, event);
        checkProjectileSound(event);
    }

    public void onEntityDamage(GameServer game, EntityDamageEvent event){
        checkDamageStart(game, event);
        checkRemoveDemArrowsCrazyMan(event);
    }

    public void cleanAll(){
        //nothing to clear?
    }
}