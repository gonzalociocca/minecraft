package server.util;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import server.api.GameAPI;
import server.instance.GameServer;

import java.util.List;

public class UtilParticle {
    public enum ViewDist {
        SHORT(8),
        NORMAL(24),
        LONG(48),
        LONGER(96),
        MAX(256);

        private int _dist;

        ViewDist(int dist) {
            _dist = dist;
        }

        public int getDist() {
            return _dist;
        }
    }

    public enum ParticleType {
        ANGRY_VILLAGER(EnumParticle.VILLAGER_ANGRY, "Lightning Cloud", Material.INK_SACK, (byte) 11),

        BLOCK_CRACK(EnumParticle.BLOCK_CRACK),

        BLOCK_DUST(EnumParticle.BLOCK_DUST),

        BUBBLE(EnumParticle.WATER_BUBBLE),

        CLOUD(EnumParticle.CLOUD, "White Smoke", Material.INK_SACK, (byte) 7),

        CRIT(EnumParticle.CRIT, "Brown Magic", Material.INK_SACK, (byte) 14),

        DEPTH_SUSPEND(EnumParticle.SUSPENDED_DEPTH),

        DRIP_LAVA(EnumParticle.DRIP_LAVA, "Lava Drip", Material.LAVA_BUCKET, (byte) 0),

        DRIP_WATER(EnumParticle.WATER_DROP, "Water Drop", Material.WATER_BUCKET, (byte) 0),

        DROPLET(EnumParticle.WATER_SPLASH, "Water Splash", Material.INK_SACK, (byte) 4),

        ENCHANTMENT_TABLE(EnumParticle.ENCHANTMENT_TABLE, "Enchantment Words", Material.BOOK, (byte) 0),

        EXPLODE(EnumParticle.SMOKE_LARGE, "Big White Smoke", Material.INK_SACK, (byte) 15),

        FIREWORKS_SPARK(EnumParticle.FIREWORKS_SPARK, "White Sparkle", Material.GHAST_TEAR, (byte) 0),

        FLAME(EnumParticle.FLAME, "Flame", Material.BLAZE_POWDER, (byte) 0),

        FOOTSTEP(EnumParticle.FOOTSTEP, "Foot Step", Material.LEATHER_BOOTS, (byte) 0),

        HAPPY_VILLAGER(EnumParticle.VILLAGER_HAPPY, "Emerald Sparkle", Material.EMERALD, (byte) 0),

        HEART(EnumParticle.HEART, "Love Heart", Material.APPLE, (byte) 0),

        HUGE_EXPLOSION(EnumParticle.EXPLOSION_HUGE, "Huge Explosion", Material.TNT, (byte) 0),

        ICON_CRACK(EnumParticle.ITEM_CRACK),

        INSTANT_SPELL(EnumParticle.SPELL_INSTANT),

        LARGE_EXPLODE(EnumParticle.EXPLOSION_LARGE, "Explosion", Material.FIREBALL, (byte) 0),

        LARGE_SMOKE(EnumParticle.SMOKE_LARGE, "Black Smoke", Material.INK_SACK, (byte) 0),

        LAVA(EnumParticle.LAVA, "Lava Debris", Material.LAVA, (byte) 0),

        MAGIC_CRIT(EnumParticle.CRIT_MAGIC, "Teal Magic", Material.INK_SACK, (byte) 6),

        /**
         * Can be colored if count is 0, color is RGB and depends on the offset of xyz
         */
        MOB_SPELL(EnumParticle.SPELL_MOB, "Black Swirls", Material.getMaterial(2263), (byte) 0),

        /**
         * Can be colored if count is 0, color is RGB and depends on the offset of xyz
         */
        MOB_SPELL_AMBIENT(EnumParticle.SPELL_MOB_AMBIENT, "Transparent Black Swirls", Material.getMaterial(2266), (byte) 0),

        NOTE(EnumParticle.NOTE, "Musical Note", Material.JUKEBOX, (byte) 0),

        PORTAL(EnumParticle.PORTAL, "UtilNetwork Effect", Material.INK_SACK, (byte) 5),

        /**
         * Can be colored if count is 0, color is RGB and depends on the offset of xyz. Offset y if 0 will default to 1, counter by making it 0.0001
         */
        RED_DUST(EnumParticle.SMOKE_NORMAL, "Red Smoke", Material.INK_SACK, (byte) 1),

        SLIME(EnumParticle.SLIME, "Slime Particles", Material.SLIME_BALL, (byte) 0),

        SNOW_SHOVEL(EnumParticle.SNOW_SHOVEL, "Snow Puffs", Material.SNOW_BALL, (byte) 0),

        SNOWBALL_POOF(EnumParticle.SNOWBALL),

        SPELL(EnumParticle.SPELL, "White Swirls", Material.getMaterial(2264), (byte) 0),

        SPLASH(EnumParticle.SPELL_INSTANT),

        SUSPEND(EnumParticle.SUSPENDED),

        TOWN_AURA(EnumParticle.TOWN_AURA, "Black Specks", Material.COAL, (byte) 0),

        WITCH_MAGIC(EnumParticle.SPELL_WITCH, "Purple Magic", Material.INK_SACK, (byte) 13);

        public EnumParticle type;
        private boolean _friendlyData;
        private String _friendlyName;
        private Material _material;
        private byte _data;

        ParticleType(EnumParticle particle) {
            type = particle;
            _friendlyData = false;
        }

        ParticleType(EnumParticle particle, String friendlyName, Material material, byte data) {
            type = particle;
            _friendlyData = true;
            _friendlyName = friendlyName;
            _material = material;
            _data = data;
        }

        public EnumParticle getParticle() {
            return type;
        }

        public boolean hasFriendlyData() {
            return _friendlyData;
        }

        public String getFriendlyName() {
            if (_friendlyName == null) {
                return toString();
            }

            return _friendlyName;
        }

        public Material getMaterial() {
            return _material;
        }

        public byte getData() {
            return _data;
        }

        public static ParticleType getFromFriendlyName(String name) {
            for (ParticleType type : values()) {
                if (type.hasFriendlyData() && type.getFriendlyName().equals(name))
                    return type;
            }
            return null;
        }
    }

    private static PacketPlayOutWorldParticles getPacket(EnumParticle type, Location location, float offsetX, float offsetY,
                                                         float offsetZ, float speed, int count, boolean displayFar) {

        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type, displayFar, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count);
        return packet;
    }

    public static void PlayParticle(ParticleType type, Location location, float offsetX, float offsetY, float offsetZ,
                                    float speed, int count) {
        GameServer game = GameAPI.getGameInBounds(location);
        List<Player> players;
        if(game != null){
            players = game.getPlayers(false);
        } /*else  if(location.getWorld().getName().equals(MapCipherBase.getLobbyWorldName())){
            players = MapCipherBase.getLobbyWorld().getPlayers();
        } */else {
            return;
        }
        PlayParticle(type, location, offsetX, offsetY, offsetZ, speed, count, ViewDist.NORMAL, players);
    }

    public static void PlayParticle(ParticleType particle, Location location, float offsetX, float offsetY, float offsetZ,
                                    float speed, int count, ViewDist dist, List<Player> players) {
        PacketPlayOutWorldParticles packet = getPacket(particle.getParticle(), location, offsetX, offsetY, offsetZ, speed, count, true);

        for (Player player : players) {
            if (UtilMath.offset(player.getLocation(), location) > dist.getDist()) {
                continue;
            }

            UtilPlayer.sendPacket(player, packet);
        }
    }
}