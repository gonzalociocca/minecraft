package mineultra.minecraft.game.core.damage;

public class DamageChange
{
    private final String _source;
    private final String _reason;
    private final double _modifier;
    private final boolean _useReason;
    
    public DamageChange(final String source, final String reason, final double modifier, final boolean useReason) {
        super();
        this._source = source;
        this._reason = reason;
        this._modifier = modifier;
        this._useReason = useReason;
    }
    
    public String GetSource() {
        return this._source;
    }
    
    public String GetReason() {
        return this._reason;
    }
    
    public double GetDamage() {
        return this._modifier;
    }
    
    public boolean UseReason() {
        return this._useReason;
    }
}
