package server.instance.core.buffer.buffers;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import server.instance.GameServer;
import server.instance.core.buffer.Buffer;
import server.instance.core.buffer.GameBuffer;

public class Silence extends Buffer
{
    public Silence(final GameBuffer manager, final String reason, final LivingEntity ent, final LivingEntity source, final Buffer.BufferType type, final int mult, final int ticks, final boolean add, final Material visualType, final byte visualData, final boolean showIndicator) {
        super(manager, reason, ent, source, type, mult, ticks, add, visualType, visualData, showIndicator, false);
    }
    
    @Override
    public void Add(GameServer game) {
        if (this._ent instanceof Player) {
            ((Player)this._ent).playSound(this._ent.getLocation(), Sound.BAT_HURT, 0.8f, 0.8f);
        }
    }
    
    @Override
    public void Remove() {
    }
}
