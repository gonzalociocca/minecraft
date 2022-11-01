package server.instance.core.buffer.buffers;

import server.instance.core.buffer.GameBuffer;
import server.instance.core.buffer.Buffer;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

public class Burning extends Buffer {

    public Burning(GameBuffer manager, String reason, LivingEntity ent, LivingEntity source, BufferType type, int mult, int ticks, boolean add, Material visualType, byte visualData, boolean showIndicator) {
        super(manager, reason, ent, source, type, mult, ticks, add, visualType, visualData, showIndicator, false);
    }

    public void Add() {}

    public void Remove() {}

    public void OnConditionAdd() {
        if(this._ent.getFireTicks() > 0) {
            this._ent.setFireTicks(this._ent.getFireTicks() + this._ticksTotal);
        } else {
            this._ent.setFireTicks(this._ticksTotal);
        }

    }
}