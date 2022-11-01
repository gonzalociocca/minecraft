package mineultra.minecraft.game.core.Buffer.Buffers;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import mineultra.minecraft.game.core.Buffer.BufferManager;
import mineultra.minecraft.game.core.Buffer.Buffer;

public class Silence extends Buffer
{
    public Silence(final BufferManager manager, final String reason, final LivingEntity ent, final LivingEntity source, final Buffer.BufferType type, final int mult, final int ticks, final boolean add, final Material visualType, final byte visualData, final boolean showIndicator) {
        super(manager, reason, ent, source, type, mult, ticks, add, visualType, visualData, showIndicator, false);
    }
    
    @Override
    public void Add() {
        if (this._ent instanceof Player) {
            ((Player)this._ent).playSound(this._ent.getLocation(), Sound.ENTITY_BAT_HURT, 0.8f, 0.8f);
        }
    }
    
    @Override
    public void Remove() {
    }
}
