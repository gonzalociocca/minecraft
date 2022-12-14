package com.gmail.filoghost.holographicdisplays.nms.v1_11_R1;

import java.lang.reflect.Method;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_11_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.nms.interfaces.FancyMessage;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSHorse;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSWitherSkull;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import com.gmail.filoghost.holographicdisplays.util.DebugHandler;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.Validator;
import com.gmail.filoghost.holographicdisplays.util.VersionUtils;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.MathHelper;
import net.minecraft.server.v1_11_R1.RegistryID;
import net.minecraft.server.v1_11_R1.RegistryMaterials;
import net.minecraft.server.v1_11_R1.World;
import net.minecraft.server.v1_11_R1.WorldServer;

public class NmsManagerImpl implements NMSManager {

	private Method validateEntityMethod;
	
	@Override
	public void setup() throws Exception {
		validateEntityMethod = World.class.getDeclaredMethod("b", Entity.class);
		validateEntityMethod.setAccessible(true);
		
		registerCustomEntity(EntityNMSSlime.class, 55);
	}
	
	@SuppressWarnings("unchecked")
	public void registerCustomEntity(Class<? extends Entity> entityClass, int id) throws Exception {
		if (VersionUtils.isMCPCOrCauldron()) {
			// MCPC+ / Cauldron entity registration.
			throw new UnsupportedOperationException("MCPC, Cauldron and similar softwares are not supported");
		} else {
			// Use reflection to get the RegistryID of entities.
			RegistryID<Class<? extends Entity>> registryID = (RegistryID<Class<? extends Entity>>) ReflectionUtils.getPrivateField(RegistryMaterials.class, EntityTypes.b, "a");
			Object[] idToClassMap = (Object[]) ReflectionUtils.getPrivateField(RegistryID.class, registryID, "d");
			
			// Save the the ID -> entity class mapping before the registration.
			Object oldValue = idToClassMap[id];

			// Register the entity class.
			registryID.a(entityClass, id);

			// Restore the ID -> entity class mapping.
			idToClassMap[id] = oldValue;
		}
	}

	@Override
	public NMSHorse spawnNMSHorse(org.bukkit.World world, double x, double y, double z, CraftHologramLine parentPiece) {
		throw new NotImplementedException("Method can only be used on 1.7 or lower");
	}
	
	@Override
	public NMSWitherSkull spawnNMSWitherSkull(org.bukkit.World bukkitWorld, double x, double y, double z, CraftHologramLine parentPiece) {
		throw new NotImplementedException("Method can only be used on 1.7 or lower");
	}
	
	@Override
	public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, CraftItemLine parentPiece, ItemStack stack) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityNMSItem customItem = new EntityNMSItem(nmsWorld, parentPiece);
		customItem.setLocationNMS(x, y, z);
		customItem.setItemStackNMS(stack);
		if (!addEntityToWorld(nmsWorld, customItem)) {
			DebugHandler.handleSpawnFail(parentPiece);
		}
		return customItem;
	}
	
	@Override
	public EntityNMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, CraftTouchSlimeLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
		EntityNMSSlime touchSlime = new EntityNMSSlime(nmsWorld, parentPiece);
		touchSlime.setLocationNMS(x, y, z);
		if (!addEntityToWorld(nmsWorld, touchSlime)) {
			DebugHandler.handleSpawnFail(parentPiece);
		}
		return touchSlime;
	}
	
	@Override
	public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, CraftHologramLine parentPiece) {
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		EntityNMSArmorStand invisibleArmorStand = new EntityNMSArmorStand(nmsWorld, parentPiece);
		invisibleArmorStand.setLocationNMS(x, y, z);
		if (!addEntityToWorld(nmsWorld, invisibleArmorStand)) {
			DebugHandler.handleSpawnFail(parentPiece);
		}
		return invisibleArmorStand;
	}
	
	private boolean addEntityToWorld(WorldServer nmsWorld, Entity nmsEntity) {
		Validator.isTrue(Bukkit.isPrimaryThread(), "Async entity add");
		
        final int chunkX = MathHelper.floor(nmsEntity.locX / 16.0);
        final int chunkZ = MathHelper.floor(nmsEntity.locZ / 16.0);
        
        if (!nmsWorld.getChunkProviderServer().isLoaded(chunkX, chunkZ)) {
        	// This should never happen
            nmsEntity.dead = true;
            return false;
        }
        
        nmsWorld.getChunkAt(chunkX, chunkZ).a(nmsEntity);
        nmsWorld.entityList.add(nmsEntity);
        
        try {
			validateEntityMethod.invoke(nmsWorld, nmsEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }
	
	@Override
	public boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity) {
		return ((CraftEntity) bukkitEntity).getHandle() instanceof NMSEntityBase;
	}

	@Override
	public NMSEntityBase getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity) {
		
		Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
		if (nmsEntity instanceof NMSEntityBase) {
			return ((NMSEntityBase) nmsEntity);
		}

		return null;
	}

	@Override
	public FancyMessage newFancyMessage(String text) {
		return new FancyMessageImpl(text);
	}

	@Override
	public boolean isUnloadUnsure(Chunk bukkitChunk) {
		return bukkitChunk.getWorld().isChunkInUse(bukkitChunk.getX(), bukkitChunk.getZ()) || !((CraftChunk) bukkitChunk).getHandle().d; // Field probably representing if the chunk is scheduled to be unloaded in ChunkProviderServer
	}
	
}
