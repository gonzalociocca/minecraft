package mineultra.minecraft.game.core.Buffer.Buffers;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import mineultra.minecraft.game.core.Buffer.Buffer;
import mineultra.minecraft.game.core.Buffer.BufferManager;

public class Burning extends Buffer
{
    public Burning(final BufferManager manager, final String reason, final LivingEntity ent, final LivingEntity source, final Buffer.BufferType type, final int mult, final int ticks, final boolean add, final Material visualType, final byte visualData, final boolean showIndicator) {
        super(manager, reason, ent, source, type, mult, ticks, add, visualType, visualData, showIndicator, false);
    }
    
    @Override
    public void Add() {
    }
    
    @Override
    public void Remove() {
    }
    
    @Override
    public void OnBufferAdd() {
        if (this._ent.getFireTicks() > 0) {
            this._ent.setFireTicks(this._ent.getFireTicks() + this._ticksTotal);
        }
        else {
            this._ent.setFireTicks(this._ticksTotal);
        }
    }
}
