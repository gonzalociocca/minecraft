package mineultra.core.common.util;

import java.lang.reflect.Field;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;



public class UtilParticle
{
  public static void PlayParticle(Player player, PCType type, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int count)
  {
    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type.getType(),false,(float)loc.getX(),(float)loc.getY(),(float)loc.getZ(),offsetX,offsetY,offsetZ,speed,count);

    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
  }

  public static void PlayParticle(PCType type, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count)
  {
    for (Player player : UtilServer.getPlayers())
      PlayParticle(player, type, location, offsetX, offsetY, offsetZ, speed, count);
  }

  public static enum PCType
  {
    HUGE_EXPLOSION(EnumParticle.EXPLOSION_HUGE,"Huge Explosion",Material.TNT), 
    LARGE_EXPLODE(EnumParticle.EXPLOSION_LARGE,"Large Explosion",Material.TNT), 
    FIREWORKS_SPARK(EnumParticle.FIREWORKS_SPARK,"Fireworks Spark",Material.FIREWORK), 
    BUBBLE(EnumParticle.WATER_BUBBLE,"Bubble",Material.WATER), 
    SUSPEND(EnumParticle.SUSPENDED,"Suspend",Material.BARRIER), 
    DEPTH_SUSPEND(EnumParticle.SUSPENDED_DEPTH,"Depth Suspend",Material.BARRIER), 
    TOWN_AURA(EnumParticle.TOWN_AURA,"Town Aura",Material.ENCHANTMENT_TABLE), 
    CRIT(EnumParticle.CRIT,"Crit",Material.IRON_SWORD), 
    MAGIC_CRIT(EnumParticle.CRIT_MAGIC,"Magic Crit",Material.DIAMOND_SWORD), 
    MOB_SPELL(EnumParticle.SPELL_MOB,"Mob Spell",Material.PAPER), 
    MOB_SPELL_AMBIENT(EnumParticle.SPELL_MOB_AMBIENT,"Mob Spell Ambient",Material.PAPER), 
    SPELL(EnumParticle.SPELL,"Spell",Material.PAPER), 
    INSTANT_SPELL(EnumParticle.SPELL_INSTANT,"Instant Spell",Material.PAPER), 
    WITCH_MAGIC(EnumParticle.SPELL_WITCH,"Witch Magic",Material.EXP_BOTTLE), 
    NOTE(EnumParticle.NOTE,"Note",Material.NOTE_BLOCK), 
    PORTAL(EnumParticle.PORTAL,"portal",Material.NETHER_BRICK_ITEM), 
    ENCHANTMENT_TABLE(EnumParticle.ENCHANTMENT_TABLE,"Enchantment Table",Material.ENCHANTMENT_TABLE), 
    EXPLODE(EnumParticle.EXPLOSION_NORMAL,"Explode",Material.TNT), 
    FLAME(EnumParticle.FLAME,"Flame",Material.BLAZE_POWDER), 
    LAVA(EnumParticle.LAVA,"Lava",Material.LAVA_BUCKET), 
    FOOTSTEP(EnumParticle.FOOTSTEP,"Footstep",Material.SUGAR), 
    SPLASH(EnumParticle.WATER_SPLASH,"Splash",Material.BOAT), 
    LARGE_SMOKE(EnumParticle.SMOKE_LARGE,"Large Smoke",Material.TNT), 
    CLOUD(EnumParticle.CLOUD,"Cloud",Material.ARROW), 
    SNOWBALL_POOF(EnumParticle.SNOWBALL,"Snowball Poof",Material.SNOW_BALL), 
    DRIP_WATER(EnumParticle.DRIP_WATER,"Drip Water",Material.WATER), 
    DRIP_LAVA(EnumParticle.DRIP_LAVA,"Drip Lava",Material.LAVA), 
    SNOW_SHOVEL(EnumParticle.SNOW_SHOVEL,"Snow Shovel",Material.SNOW_BLOCK), 
    SLIME(EnumParticle.SLIME,"Slime",Material.SLIME_BALL), 
    HEART(EnumParticle.HEART,"Heart",Material.GOLDEN_APPLE), 
    ANGRY_VILLAGER(EnumParticle.VILLAGER_ANGRY,"Angry Villager",Material.BLAZE_POWDER), 
    HAPPY_VILLAGER(EnumParticle.VILLAGER_HAPPY,"Happy Villager",Material.EMERALD);

    private String particleName;
    private Material icontype;
    private EnumParticle particle;
    private PCType(EnumParticle ep, String pName, Material icon)
    {
        particle = ep;
        icontype=icon;
        particleName = pName;
    }
    
    public EnumParticle getType(){
        return particle;
    }
    public Material getIcon(){
        return icontype;
    }
    public String getName(){
        return particleName;
    }
  }
}