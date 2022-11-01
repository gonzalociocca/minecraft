package server.instance.core.damage;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import server.util.C;
import server.instance.GameServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CustomDamageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private EntityDamageEvent.DamageCause _eventCause;
    private double _initialDamage;
    private ArrayList _damageMult = new ArrayList();
    private ArrayList _damageMod = new ArrayList();
    private ArrayList _cancellers = new ArrayList();
    private HashMap _knockbackMod = new HashMap();
    private LivingEntity _damageeEntity;
    private Player _damageePlayer;
    private LivingEntity _damagerEntity;
    private Player _damagerPlayer;
    private Projectile _projectile;
    private Location _knockbackOrigin = null;
    private boolean _ignoreArmor = false;
    private boolean _ignoreRate = false;
    private boolean _knockback = true;
    private boolean _damageeBrute = false;
    private boolean _damageToLevel = true;
    GameServer _game;

    public CustomDamageEvent(GameServer game, LivingEntity damagee, LivingEntity damager, Projectile projectile, EntityDamageEvent.DamageCause cause, double damage, boolean knockback, boolean ignoreRate, boolean ignoreArmor, String initialSource, String initialReason, boolean cancelled) {
        _game = game;
        this._eventCause = cause;
        this._initialDamage = damage;
        this._damageeEntity = damagee;
        if(this._damageeEntity != null && this._damageeEntity instanceof Player) {
            this._damageePlayer = (Player)this._damageeEntity;
        }

        this._damagerEntity = damager;
        if(this._damagerEntity != null && this._damagerEntity instanceof Player) {
            this._damagerPlayer = (Player)this._damagerEntity;
        }

        this._projectile = projectile;
        this._knockback = knockback;
        this._ignoreRate = ignoreRate;
        this._ignoreArmor = ignoreArmor;
        if(initialSource != null && initialReason != null) {
            this.addMod(initialSource, initialReason, 0.0D, true);
        }

        if(this._eventCause == EntityDamageEvent.DamageCause.FALL) {
            this._ignoreArmor = true;
        }

        if(cancelled) {
            this.setCancelled("Pre-Cancelled");
        }

    }

    public GameServer getGame(){
        return _game;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void addMult(String source, String reason, double mod, boolean useAttackName) {
        this._damageMult.add(new DamageChange(source, reason, mod, useAttackName));
    }

    public void addMod(String source, String reason, double mod, boolean useAttackName) {
        this._damageMod.add(new DamageChange(source, reason, mod, useAttackName));
    }

    public void addKnockback(String reason, double d) {
        this._knockbackMod.put(reason, Double.valueOf(d));
    }

    public boolean isCancelled() {
        return !this._cancellers.isEmpty();
    }

    public void setCancelled(String reason) {
        this._cancellers.add(reason);
    }

    public double getDamage() {
        double damage = this.getDamageInitial();

        DamageChange mult;
        Iterator var4;
        for(var4 = this._damageMod.iterator(); var4.hasNext(); damage += mult.GetDamage()) {
            mult = (DamageChange)var4.next();
        }

        for(var4 = this._damageMult.iterator(); var4.hasNext(); damage *= mult.GetDamage()) {
            mult = (DamageChange)var4.next();
        }

        return damage;
    }

    public LivingEntity getDamageeEntity() {
        return this._damageeEntity;
    }

    public Player getDamageePlayer() {
        return this._damageePlayer;
    }

    public LivingEntity getDamagerEntity(boolean ranged) {
        return ranged?this._damagerEntity:(this._projectile == null?this._damagerEntity:null);
    }

    public Player getDamagerPlayer(boolean ranged) {
        return ranged?this._damagerPlayer:(this._projectile == null?this._damagerPlayer:null);
    }

    public Projectile getProjectile() {
        return this._projectile;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return this._eventCause;
    }

    public double getDamageInitial() {
        return this._initialDamage;
    }

    public void setIgnoreArmor(boolean ignore) {
        this._ignoreArmor = ignore;
    }

    public void setIgnoreRate(boolean ignore) {
        this._ignoreRate = ignore;
    }

    public void setKnockback(boolean knockback) {
        this._knockback = knockback;
    }

    public void setBrute() {
        this._damageeBrute = true;
    }

    public boolean isBrute() {
        return this._damageeBrute;
    }

    public String getReason() {
        String reason = "";
        Iterator var3 = this._damageMod.iterator();

        while(var3.hasNext()) {
            DamageChange change = (DamageChange)var3.next();
            if(change.UseReason()) {
                reason = reason + C.mSkill + change.GetReason() + ChatColor.GRAY + ", ";
            }
        }

        if(reason.length() > 0) {
            reason = reason.substring(0, reason.length() - 2);
            return reason;
        } else {
            return null;
        }
    }

    public boolean isKnockback() {
        return this._knockback;
    }

    public boolean ignoreRate() {
        return this._ignoreRate;
    }

    public boolean ignoreArmor() {
        return this._ignoreArmor;
    }

    public void setDamager(LivingEntity ent) {
        if(ent != null) {
            this._damagerEntity = ent;
            this._damagerPlayer = null;
            if(ent instanceof Player) {
                this._damagerPlayer = (Player)ent;
            }

        }
    }

    public void setInitialDamage(int i){
        _initialDamage = i;
    }

    public void setDamagee(LivingEntity ent) {
        this._damageeEntity = ent;
        this._damageePlayer = null;
        if(ent instanceof Player) {
            this._damageePlayer = (Player)ent;
        }

    }

    public void changeReason(String initial, String reason) {
        Iterator var4 = this._damageMod.iterator();

        while(var4.hasNext()) {
            DamageChange change = (DamageChange)var4.next();
            if(change.GetReason().equals(initial)) {
                change.setReason(reason);
            }
        }

    }

    public void setKnockbackOrigin(Location loc) {
        this._knockbackOrigin = loc;
    }

    public Location getKnockbackOrigin() {
        return this._knockbackOrigin;
    }

    public ArrayList getDamageMod() {
        return this._damageMod;
    }

    public ArrayList getDamageMult() {
        return this._damageMult;
    }

    public HashMap getKnockback() {
        return this._knockbackMod;
    }

    public ArrayList getCancellers() {
        return this._cancellers;
    }

    public void setDamageToLevel(boolean val) {
        this._damageToLevel = val;
    }

    public boolean displayDamageToLevel() {
        return this._damageToLevel;
    }

    @Deprecated
    public void setCancelled(boolean isCancelled) {
        this.setCancelled("No reason given because SOMEONE IS AN IDIOT");
    }
}