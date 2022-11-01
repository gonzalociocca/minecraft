package mineultra.minecraft.game.core.Buffer.Buffers;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import mineultra.minecraft.game.core.Buffer.BufferManager;
import mineultra.minecraft.game.core.Buffer.Buffer;

public class FireItemImmunity extends Buffer
{
    public FireItemImmunity(final BufferManager manager, final String reason, final LivingEntity ent, final LivingEntity source, final Buffer.BufferType type, final int mult, final int ticks, final boolean add, final Material visualType, final byte visualData, final boolean showIndicator) {
        super(manager, reason, ent, source, type, mult, ticks, add, visualType, visualData, showIndicator, false);
    }
}
