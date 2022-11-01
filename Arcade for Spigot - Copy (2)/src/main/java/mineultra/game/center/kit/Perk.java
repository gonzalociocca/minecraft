package mineultra.game.center.kit;

import org.bukkit.entity.Player;
import mineultra.game.center.centerManager;
import org.bukkit.event.Listener;

public abstract class Perk implements Listener
{
    public centerManager Manager;
    public Kit Kit;
    private String _perkName;
    private String[] _perkDesc;
    private boolean _display;
    
    public Perk(final String name, final String[] perkDesc) {
        super();
        this._perkName = name;
        this._perkDesc = perkDesc;
        this._display = true;
    }
    
    public Perk(final String name, final String[] perkDesc, final boolean display) {
        super();
        this._perkName = name;
        this._perkDesc = perkDesc;
        this._display = display;
    }
    
    public void SetHost(final Kit kit) {
        this.Manager = kit.Manager;
        this.Kit = kit;
    }
    
    public String GetName() {
        return this._perkName;
    }
    
    public String[] GetDesc() {
        return this._perkDesc;
    }
    
    public boolean IsVisible() {
        return this._display;
    }
    
    public void Apply(final Player player) {
    }
}
