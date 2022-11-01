package mineultra.minecraft.game.core.damage;

import org.bukkit.ChatColor;
import mineultra.core.common.util.C;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import java.util.HashMap;
import java.util.ArrayList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public final class CustomDamageEvent extends Event
{
    private static final HandlerList handlers;
    private final EntityDamageEvent.DamageCause _eventCause;
    private double _initialDamage;
    private final ArrayList<DamageChange> _damageMult;
    private final ArrayList<DamageChange> _damageMod;
    private final ArrayList<String> _cancellers;
    private final HashMap<String, Double> _knockbackMod;
    private final LivingEntity _damageeEntity;
    private Player _damageePlayer;
    private LivingEntity _damagerEntity;
    private Player _damagerPlayer;
    private final Projectile _projectile;
    private boolean _ignoreArmor;
    private boolean _ignoreRate;
    private boolean _knockback;
    private boolean _damageeBrute;
    private boolean _damageToLevel;
    
    static {
        handlers = new HandlerList();
    }
    
    public CustomDamageEvent(final LivingEntity damagee, final LivingEntity damager, final Projectile projectile, final EntityDamageEvent.DamageCause cause, final double damage, final boolean knockback, final boolean ignoreRate, final boolean ignoreArmor, final String initialSource, final String initialReason, final boolean cancelled) {
        super();
        this._damageMult = new ArrayList<>();
        this._damageMod = new ArrayList<>();
        this._cancellers = new ArrayList<>();
        this._knockbackMod = new HashMap<>();
        this._ignoreArmor = false;
        this._ignoreRate = false;
        this._knockback = true;
        this._damageeBrute = false;
        this._damageToLevel = true;
        this._eventCause = cause;
        if (initialSource == null || initialReason == null) {
            this._initialDamage = damage;
        }
        this._damageeEntity = damagee;
        if (this._damageeEntity != null && this._damageeEntity instanceof Player) {
            this._damageePlayer = (Player)this._damageeEntity;
        }
        this._damagerEntity = damager;
        if (this._damagerEntity != null && this._damagerEntity instanceof Player) {
            this._damagerPlayer = (Player)this._damagerEntity;
        }
        this._projectile = projectile;
        this._knockback = knockback;
        this._ignoreRate = ignoreRate;
        this._ignoreArmor = ignoreArmor;


        if (initialSource != null && initialReason != null) {
            this.AddMod(initialSource, initialReason, damage, true);
        }
        if (this._eventCause == EntityDamageEvent.DamageCause.FALL) {
            this._ignoreArmor = true;
        }
        if(this._eventCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            this._ignoreArmor = false;
           
        }
        if (cancelled) {
            this.SetCancelled("Pre-Cancelled");
        }
    }
    
    @Override
    public HandlerList getHandlers() {
        return CustomDamageEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return CustomDamageEvent.handlers;
    }
    
    public void AddMult(final String source, final String reason, final double mod, final boolean useAttackName) {
        this._damageMult.add(new DamageChange(source, reason, mod, useAttackName));
    }
    
    public void AddMod(final String source, final String reason, final double mod, final boolean useAttackName) {
        this._damageMod.add(new DamageChange(source, reason, mod, useAttackName));
    }
    
    public void AddKnockback(final String reason, final double d) {
        this._knockbackMod.put(reason, d);
    }
    
    public boolean IsCancelled() {
        return !this._cancellers.isEmpty();
    }
    
    public void SetCancelled(final String reason) {
        this._cancellers.add(reason);
    }
    
    public double GetDamage() {
        double damage = this.GetDamageInitial();
        for (final DamageChange mult : this._damageMult) {
            damage *= mult.GetDamage();
        }
        for (final DamageChange mult : this._damageMod) {
            damage += mult.GetDamage();
        }
        return damage;
    }
    
    public LivingEntity GetDamageeEntity() {
        return this._damageeEntity;
    }
    
    public Player GetDamageePlayer() {
        return this._damageePlayer;
    }
    
    public LivingEntity GetDamagerEntity(final boolean ranged) {
        if (ranged) {
            return this._damagerEntity;
        }
        if (this._projectile == null) {
            return this._damagerEntity;
        }
        return null;
    }
    
    public Player GetDamagerPlayer(final boolean ranged) {
        if (ranged) {
            return this._damagerPlayer;
        }
        if (this._projectile == null) {
            return this._damagerPlayer;
        }
        return null;
    }
    
    public Projectile GetProjectile() {
        return this._projectile;
    }
    
    public EntityDamageEvent.DamageCause GetCause() {
        return this._eventCause;
    }
    
    public double GetDamageInitial() {
        return this._initialDamage;
    }
    
    public void SetIgnoreArmor(final boolean ignore) {
        this._ignoreArmor = ignore;
    }
    
    public void SetIgnoreRate(final boolean ignore) {
        this._ignoreRate = ignore;
    }
    
    public void SetKnockback(final boolean knockback) {
        this._knockback = knockback;
    }
    
    public void SetBrute() {
        this._damageeBrute = true;
    }
    
    public boolean IsBrute() {
        return this._damageeBrute;
    }
    
    public String GetReason() {
        String reason = "";
        for (final DamageChange change : this._damageMod) {
            if (change.UseReason()) {
                reason = String.valueOf(reason) + C.mSkill + change.GetReason() + ChatColor.GRAY + ", ";
            }
        }
        if (reason.length() > 0) {
            reason = reason.substring(0, reason.length() - 2);
            return reason;
        }
        return null;
    }
    
    public boolean IsKnockback() {
        return this._knockback;
    }
    
    public boolean IgnoreRate() {
        return this._ignoreRate;
    }
    
    public boolean IgnoreArmor() {
        return this._ignoreArmor;
    }
    
    public void SetDamager(final LivingEntity ent) {
        if (ent == null) {
            return;
        }
        this._damagerEntity = ent;
        this._damagerPlayer = null;
        if (ent instanceof Player) {
            this._damagerPlayer = (Player)ent;
        }
    }
    
    public ArrayList<DamageChange> GetDamageMod() {
        return this._damageMod;
    }
    
    public ArrayList<DamageChange> GetDamageMult() {
        return this._damageMult;
    }
    
    public HashMap<String, Double> GetKnockback() {
        return this._knockbackMod;
    }
    
    public ArrayList<String> GetCancellers() {
        return this._cancellers;
    }
    
    public void SetDamageToLevel(final boolean val) {
        this._damageToLevel = val;
    }
    
    public boolean DisplayDamageToLevel() {
        return this._damageToLevel;
    }
}
