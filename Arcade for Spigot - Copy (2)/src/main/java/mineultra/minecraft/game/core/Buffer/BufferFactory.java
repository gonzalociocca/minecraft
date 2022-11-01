package mineultra.minecraft.game.core.Buffer;

import org.bukkit.util.Vector;
import mineultra.minecraft.game.core.Buffer.Buffers.Cloak;
import mineultra.minecraft.game.core.Buffer.Buffers.FireItemImmunity;
import mineultra.minecraft.game.core.Buffer.Buffers.Silence;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

public class BufferFactory
{
    public BufferManager Manager;
    
    public BufferFactory(final BufferManager manager) {
        super();
        this.Manager = manager;
    }
    
    public Buffer Custom(final String reason, final LivingEntity ent, final LivingEntity source, final Buffer.BufferType type, final double duration, final int mult, final boolean extend, final Material indMat, final byte indData, final boolean showIndicator) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, type, mult, (int)(20.0 * duration), extend, indMat, indData, showIndicator, false));
    }
    
    public Buffer Invulnerable(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.INVULNERABLE, 0, (int)(20.0 * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator, false));
    }
    
    public Buffer FireItemImmunity(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final boolean extend) {
        return this.Manager.AddBuffer(new FireItemImmunity(this.Manager, reason, ent, source, Buffer.BufferType.FIRE_ITEM_IMMUNITY, 0, (int)(20.0 * duration), extend, Material.GHAST_TEAR, (byte)0, false));
    }
    
    public Buffer Cloak(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final boolean extend, final boolean inform) {
        return this.Manager.AddBuffer(new Cloak(this.Manager, reason, ent, source, Buffer.BufferType.CLOAK, 0, (int)(20.0 * duration), extend, Material.GHAST_TEAR, (byte)0, false));
    }
    
    public Buffer Explosion(final String reason, final LivingEntity ent, final LivingEntity source, final int mult, final double duration, final boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.EXPLOSION, mult, (int)(20.0 * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator, false));
    }
    
    public Buffer Lightning(final String reason, final LivingEntity ent, final LivingEntity source, final int mult, final double duration, final boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.LIGHTNING, mult, (int)(20.0 * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator, false));
    }
    
    public Buffer Falling(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.FALLING, 0, (int)(20.0 * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator, false));
    }
    
    public Buffer Silence(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final boolean extend, final boolean showIndicator) {
        return this.Manager.AddBuffer(new Silence(this.Manager, reason, ent, source, Buffer.BufferType.SILENCE, 0, (int)(20.0 * duration), extend, Material.WATCH, (byte)0, showIndicator));
    }
    
    public Buffer Speed(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.SPEED, mult, (int)(20.0 * duration), extend, Material.FEATHER,(byte) 0, showIndicator, ambient));
    }
    
    public Buffer Strength(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.INCREASE_DAMAGE, mult, (int)(20.0 * duration), extend, Material.IRON_SWORD, (byte)0, showIndicator, ambient));
    }
    
    public Buffer Hunger(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, boolean showIndicator, final boolean ambient) {
        showIndicator = false;
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.HUNGER, mult, (int)(20.0 * duration), extend, Material.ROTTEN_FLESH, (byte)0, showIndicator, ambient));
    }
    
    public Buffer Regen(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.REGENERATION, mult, (int)(20.0 * duration), extend, Material.INK_SACK, (byte)1, showIndicator, ambient));
    }
    
    public Buffer Weakness(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.WEAKNESS, mult, (int)(20.0 * duration), extend, Material.INK_SACK, (byte)15, showIndicator, ambient));
    }
    
    public Buffer Protection(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.DAMAGE_RESISTANCE, mult, (int)(20.0 * duration), extend, Material.IRON_CHESTPLATE,(byte) 0, showIndicator, ambient));
    }
    
    public Buffer FireResist(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.FIRE_RESISTANCE, mult, (int)(20.0 * duration), extend, Material.BLAZE_POWDER, (byte)0, showIndicator, ambient));
    }
    
    public Buffer Breath(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.WATER_BREATHING, mult, (int)(20.0 * duration), extend, Material.INK_SACK, (byte)4, showIndicator, ambient));
    }
    
    public Buffer DigFast(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.FAST_DIGGING, mult, (int)(20.0 * duration), extend, Material.GLOWSTONE_DUST, (byte)0, showIndicator, ambient));
    }
    
    public Buffer DigSlow(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.SLOW_DIGGING, mult, (int)(20.0 * duration), extend, Material.WOOD_PICKAXE, (byte)0, showIndicator, ambient));
    }
    
    public Buffer Jump(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.JUMP, mult, (int)(20.0 * duration), extend, Material.CARROT_ITEM, (byte)0, showIndicator, ambient));
    }
    
    public Buffer Invisible(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, boolean showIndicator, final boolean ambient) {
        showIndicator = false;
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.INVISIBILITY, mult, (int)(20.0 * duration), extend, Material.SNOW_BALL,(byte) 0, showIndicator, ambient));
    }
    
    public Buffer Shock(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final boolean extend, final boolean showIndicator) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.SHOCK, 0, (int)(20.0 * duration), extend, Material.DEAD_BUSH, (byte)0, showIndicator, false));
    }
    
    public Buffer Slow(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean stun, final boolean ambient) {
        if (stun) {
            ent.setVelocity(new Vector(0, 0, 0));
        }
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.SLOW, mult, (int)(20.0 * duration), extend, Material.WEB,(byte) 0, showIndicator, ambient));
    }
    
    public Buffer Poison(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.POISON, mult, (int)(20.0 * duration), extend, Material.SLIME_BALL, (byte)14, showIndicator, ambient));
    }
    
    public Buffer Confuse(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.CONFUSION, mult, (int)(20.0 * duration), extend, Material.ENDER_PEARL, (byte)0, showIndicator, ambient));
    }
    
    public Buffer Blind(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.BLINDNESS, mult, (int)(20.0 * duration), extend, Material.EYE_OF_ENDER, (byte)0, showIndicator, ambient));
    }
    
    public Buffer NightVision(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.NIGHT_VISION, mult, (int)(20.0 * duration), extend, Material.EYE_OF_ENDER, (byte)0, showIndicator, ambient));
    }
    
    public Buffer HealthBoost(final String reason, final LivingEntity ent, final LivingEntity source, final double duration, final int mult, final boolean extend, final boolean showIndicator, final boolean ambient) {
        return this.Manager.AddBuffer(new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.HEALTH_BOOST, mult, (int)(20.0 * duration), extend, Material.APPLE, (byte)0, showIndicator, ambient));
    }
}
