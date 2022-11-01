
package mineultra.game.center.game.games.pack.turboracers;

import net.minecraft.server.v1_8_R2.EntityCow;
import net.minecraft.server.v1_8_R2.EntityInsentient;
import net.minecraft.server.v1_8_R2.EntityTypes;
import net.minecraft.server.v1_8_R2.EntityZombie;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.server.v1_8_R2.EntityArmorStand;
import net.minecraft.server.v1_8_R2.EntityLiving;
import org.bukkit.entity.EntityType;
 
public enum CustomEntity {
 
 
    Vehicle("ArmorStand", 30, EntityType.ARMOR_STAND, EntityArmorStand.class, CustomVehicleCar.class),
    Missile("ArmorStand", 30, EntityType.ARMOR_STAND, EntityArmorStand.class, CustomMissile.class),
    ItemDrop("ArmorStand", 30, EntityType.ARMOR_STAND, EntityArmorStand.class, CustomItemDrop.class);
 
private String name = null;
private int id = -1;
private EntityType entityType = null;
private Class<? extends EntityArmorStand> nmsClass = null;
private Class<? extends EntityArmorStand> customClass = null;
 
private CustomEntity(String name, int id, EntityType entityType, Class<? extends EntityArmorStand> nmsClass, Class<? extends EntityArmorStand> customClass){
this.name = name;
this.id = id;
this.entityType = entityType;
this.nmsClass = nmsClass;
this.customClass = customClass;
}
 
public String getName(){
return this.name;
}
 
public int getID(){
return this.id;
}
 
public EntityType getEntityType(){
return this.entityType;
}
 
public Class<? extends EntityArmorStand> getNMSClass(){
return this.nmsClass;
}
 
public Class<? extends EntityArmorStand> getCustomClass(){
return this.customClass;
}

public void registerEntities(CustomEntity entity){

        try{
            Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[]{Class.class, String.class, int.class});
            a.setAccessible(true);
            a.invoke(null, entity.getCustomClass(), entity.getName(), entity.getID());
        }catch (Exception e){
            e.printStackTrace();
        }
}


}