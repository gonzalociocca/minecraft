package server.instance.core.buffer;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import server.instance.GameServer;
import server.instance.core.buffer.buffers.Burning;
import server.instance.core.buffer.buffers.Cloak;
import server.instance.core.buffer.buffers.FireItemImmunity;
import server.instance.core.buffer.buffers.Silence;


public class BufferFactory{

    public GameBuffer Manager;


    public BufferFactory(GameBuffer manager) {
        this.Manager = manager;
    }

    public Buffer Custom(GameServer game, String reason, LivingEntity ent, LivingEntity source, Buffer.BufferType type, double duration, int mult, boolean extend, Material indMat, byte indData, boolean showIndicator) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, type, mult, (int)(20.0D * duration), extend, indMat, indData, showIndicator, false));
    }

    public Buffer Invulnerable(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.INVULNERABLE, 0, (int)(20.0D * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator, false));
    }

    public Buffer FireItemImmunity(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, boolean extend) {
        return this.Manager.addBuffer(game, new FireItemImmunity(this.Manager, reason, ent, source, Buffer.BufferType.FIRE_ITEM_IMMUNITY, 0, (int)(20.0D * duration), extend, Material.GHAST_TEAR, (byte)0, false));
    }

    public Buffer Cloak(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, boolean extend, boolean inform) {
        return this.Manager.addBuffer(game, new Cloak(this.Manager, reason, ent, source, Buffer.BufferType.CLOAK, 0, (int)(20.0D * duration), extend, Material.GHAST_TEAR, (byte)0, false));
    }

    public Buffer Explosion(GameServer game, String reason, LivingEntity ent, LivingEntity source, int mult, double duration, boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.EXPLOSION, mult, (int)(20.0D * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator, false));
    }

    public Buffer Lightning(GameServer game, String reason, LivingEntity ent, LivingEntity source, int mult, double duration, boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.LIGHTNING, mult, (int)(20.0D * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator, false));
    }

    public Buffer Falling(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.FALLING, 0, (int)(20.0D * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator, false));
    }

    public Buffer Silence(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, boolean extend, boolean showIndicator) {
        return this.Manager.addBuffer(game, new Silence(this.Manager, reason, ent, source, Buffer.BufferType.SILENCE, 0, (int)(20.0D * duration), extend, Material.WATCH, (byte)0, showIndicator));
    }

    public Buffer Speed(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.SPEED, mult, (int)(20.0D * duration), extend, Material.FEATHER, (byte)0, showIndicator, ambient));
    }

    public Buffer Strength(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.INCREASE_DAMAGE, mult, (int)(20.0D * duration), extend, Material.IRON_SWORD, (byte)0, showIndicator, ambient));
    }

    public Buffer Hunger(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        showIndicator = false;
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.HUNGER, mult, (int)(20.0D * duration), extend, Material.ROTTEN_FLESH, (byte)0, showIndicator, ambient));
    }

    public Buffer Regen(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.REGENERATION, mult, (int)(20.0D * duration), extend, Material.INK_SACK, (byte)1, showIndicator, ambient));
    }

    public Buffer Weakness(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.WEAKNESS, mult, (int)(20.0D * duration), extend, Material.INK_SACK, (byte)15, showIndicator, ambient));
    }

    public Buffer Protection(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.DAMAGE_RESISTANCE, mult, (int)(20.0D * duration), extend, Material.IRON_CHESTPLATE, (byte)0, showIndicator, ambient));
    }

    public Buffer FireResist(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.FIRE_RESISTANCE, mult, (int)(20.0D * duration), extend, Material.BLAZE_POWDER, (byte)0, showIndicator, ambient));
    }

    public Buffer Breath(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.WATER_BREATHING, mult, (int)(20.0D * duration), extend, Material.INK_SACK, (byte)4, showIndicator, ambient));
    }

    public Buffer DigFast(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.FAST_DIGGING, mult, (int)(20.0D * duration), extend, Material.GLOWSTONE_DUST, (byte)0, showIndicator, ambient));
    }

    public Buffer DigSlow(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.SLOW_DIGGING, mult, (int)(20.0D * duration), extend, Material.WOOD_PICKAXE, (byte)0, showIndicator, ambient));
    }

    public Buffer Jump(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.JUMP, mult, (int)(20.0D * duration), extend, Material.CARROT_ITEM, (byte)0, showIndicator, ambient));
    }

    public Buffer Invisible(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        showIndicator = false;
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.INVISIBILITY, mult, (int)(20.0D * duration), extend, Material.SNOW_BALL, (byte)0, showIndicator, ambient));
    }

    public Buffer Shock(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, boolean extend, boolean showIndicator) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.SHOCK, 0, (int)(20.0D * duration), extend, Material.DEAD_BUSH, (byte)0, showIndicator, false));
    }

    public Buffer Ignite(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, boolean extend, boolean showIndicator) {
        showIndicator = false;
        return this.Manager.addBuffer(game, new Burning(this.Manager, reason, ent, source, Buffer.BufferType.BURNING, 0, (int)(20.0D * duration), extend, Material.GHAST_TEAR, (byte)0, showIndicator));
    }

    public Buffer Slow(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean stun, boolean ambient) {
        if(stun) {
            ent.setVelocity(new Vector(0, 0, 0));
        }

        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.SLOW, mult, (int)(20.0D * duration), extend, Material.WEB, (byte)0, showIndicator, ambient));
    }

    public Buffer Wither(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.WITHER, mult, (int)(20.0D * duration), extend, Material.SKULL_ITEM, (byte)1, showIndicator, ambient));
    }

    public Buffer Poison(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.POISON, mult, (int)(20.0D * duration), extend, Material.SLIME_BALL, (byte)14, showIndicator, ambient));
    }

    public Buffer Confuse(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.CONFUSION, mult, (int)(20.0D * duration), extend, Material.ENDER_PEARL, (byte)0, showIndicator, ambient));
    }

    public Buffer Blind(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.BLINDNESS, mult, (int)(20.0D * duration), extend, Material.EYE_OF_ENDER, (byte)0, showIndicator, ambient));
    }

    public Buffer NightVision(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.NIGHT_VISION, mult, (int)(20.0D * duration), extend, Material.EYE_OF_ENDER, (byte)0, showIndicator, ambient));
    }

    public Buffer HealthBoost(GameServer game, String reason, LivingEntity ent, LivingEntity source, double duration, int mult, boolean extend, boolean showIndicator, boolean ambient) {
        return this.Manager.addBuffer(game, new Buffer(this.Manager, reason, ent, source, Buffer.BufferType.HEALTH_BOOST, mult, (int)(20.0D * duration), extend, Material.APPLE, (byte)0, showIndicator, ambient));
    }
}
