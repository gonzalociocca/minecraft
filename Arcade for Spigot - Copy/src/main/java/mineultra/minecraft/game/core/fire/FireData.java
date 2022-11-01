package mineultra.minecraft.game.core.fire;

import org.bukkit.entity.LivingEntity;

public class FireData
{
    private LivingEntity _owner;
    private long _expireTime;
    private long _delayTime;
    private double _burnTime;
    private int _damage;
    private String _skillName;
    
    public FireData(final LivingEntity owner, final double expireTime, final double delayTime, final double burnTime, final int damage, final String skillName) {
        super();
        this._owner = owner;
        this._expireTime = System.currentTimeMillis() + (long)(1000.0 * expireTime);
        this._delayTime = System.currentTimeMillis() + (long)(1000.0 * delayTime);
        this._burnTime = burnTime;
        this._damage = damage;
        this._skillName = skillName;
    }
    
    public LivingEntity GetOwner() {
        return this._owner;
    }
    
    public double GetBurnTime() {
        return this._burnTime;
    }
    
    public int GetDamage() {
        return this._damage;
    }
    
    public String GetName() {
        return this._skillName;
    }
    
    public boolean IsPrimed() {
        return System.currentTimeMillis() > this._delayTime;
    }
    
    public boolean Expired() {
        return System.currentTimeMillis() > this._expireTime;
    }
}
