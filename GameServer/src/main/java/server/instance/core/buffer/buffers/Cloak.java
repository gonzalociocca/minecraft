package server.instance.core.buffer.buffers;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import server.instance.GameServer;
import server.instance.core.buffer.Buffer;
import server.instance.core.buffer.GameBuffer;

public class Cloak extends Buffer
{
    public Cloak(final GameBuffer manager, final String reason, final LivingEntity ent, final LivingEntity source, final Buffer.BufferType type, final int mult, final int ticks, final boolean add, final Material visualType, final byte visualData, final boolean showIndicator) {
        super(manager, reason, ent, source, type, mult, ticks, add, visualType, visualData, showIndicator, false);
        this._informOn = "Ahora eres invisible.";
        this._informOff = "Ya no eres invisible.";
    }
    
    @Override
    public void Add(GameServer game) {
        if (!(this._ent instanceof Player)) {
            return;
        }

        Player myPlayer = (Player)_ent;
        EntityPlayer entityPlayer = ((CraftPlayer)myPlayer).getHandle();

        myPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 3, true), true);
        entityPlayer.k = false;

        for(Player player : game.getPlayers(true)){
            player.hidePlayer(myPlayer);
        }

        for (final Entity ent : _ent.getWorld().getEntities()) {
            if (!(ent instanceof Creature)) {
                continue;
            }
            final Creature creature = (Creature)ent;
            if (creature.getTarget() != null && !creature.getTarget().equals(this._ent)) {
                continue;
            }
            creature.setTarget(null);
        }
    }
    
    @Override
    public void Remove() {
        super.Remove();

        Player myPlayer = (Player)_ent;
        EntityPlayer entityPlayer = ((CraftPlayer)myPlayer).getHandle();

        entityPlayer.k = true;
        myPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);

        for(Player player : Bukkit.getOnlinePlayers()){
            player.showPlayer(myPlayer);
        }



    }
}
