package mineultra.minecraft.game.core.combat;

public class CombatDamage
{
    private String _name;
    private double _dmg;
    private long _time;
    
    public CombatDamage(final String name, final double dmg) {
        super();
        this._name = name;
        this._dmg = dmg;
        this._time = System.currentTimeMillis();
    }
    
    public String GetName() {
        return this._name;
    }
    
    public double GetDamage() {
        return this._dmg;
    }
    
    public long GetTime() {
        return this._time;
    }
}
