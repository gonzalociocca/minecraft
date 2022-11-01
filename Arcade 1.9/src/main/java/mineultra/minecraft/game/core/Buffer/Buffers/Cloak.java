package mineultra.minecraft.game.core.Buffer.Buffers;

import java.util.Collection;
import java.util.Iterator;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import mineultra.minecraft.game.core.Buffer.BufferManager;
import mineultra.minecraft.game.core.Buffer.Buffer;
import org.bukkit.Bukkit;

public class Cloak extends Buffer
{
    public Cloak(final BufferManager manager, final String reason, final LivingEntity ent, final LivingEntity source, final Buffer.BufferType type, final int mult, final int ticks, final boolean add, final Material visualType, final byte visualData, final boolean showIndicator) {
        super(manager, reason, ent, source, type, mult, ticks, add, visualType, visualData, showIndicator, false);
        this._informOn = "You are now invisible.";
        this._informOff = "You are no longer invisible.";
    }
    
    @Override
    public void Add() {
        if (!(this._ent instanceof Player)) {
            return;
        }

        for (Iterator<? extends Player> it = Bukkit.getOnlinePlayers().iterator();it.hasNext();) {
            final Player other = it.next();
            other.hidePlayer((Player)this._ent);
        }
        for (final Entity ent : this._ent.getWorld().getEntities()) {
            if (!(ent instanceof Creature)) {
                continue;
            }
            final Creature creature = (Creature)ent;
            if (creature.getTarget() != null && !creature.getTarget().equals(this._ent)) {
                continue;
            }
            creature.setTarget((LivingEntity)null);
        }
    }
    
    @Override
    public void Remove() {
        super.Remove();

        for (Iterator<? extends Player> it = Bukkit.getOnlinePlayers().iterator();it.hasNext();) {
            final Player other = it.next();
            other.hidePlayer((Player)this._ent);
            other.showPlayer((Player)this._ent);
        }
    }
}
