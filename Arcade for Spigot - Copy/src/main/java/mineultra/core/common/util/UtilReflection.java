/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.core.common.util;


import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R2.DataWatcher;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_8_R2.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R2.PacketPlayOutSpawnEntityLiving;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;

public class UtilReflection
{
 /*
    public static Object getValue(PacketPlayOutEntityTeleport packet, String field) throws IllegalArgumentException, IllegalAccessException{
try{
for(Field f : packet.getClass().getDeclaredFields()){
   f.setAccessible(true);
   if(f.getName() == null ? field == null : f.getName().equals(field)){
   return f.get(packet);
   }
}}catch(SecurityException | IllegalArgumentException | IllegalAccessException e){
    System.out.println("Field exception:");
    e.printStackTrace();
}

System.out.println("Failed to handle packet "+packet.toString());
        return -1;
    }
    
    public static Object getValue(CraftItemStack packet, String field) throws IllegalArgumentException, IllegalAccessException{
try{
for(Field f : packet.getClass().getDeclaredFields()){
   f.setAccessible(true);
   if(f.getName() == null ? field == null : f.getName().equals(field)){
   return f.get(packet);
   }
}}catch(SecurityException | IllegalArgumentException | IllegalAccessException e){
    System.out.println("Field exception:");
    e.printStackTrace();
}

System.out.println("Failed to handle packet "+packet.toString());
        return -1;
    }
    
    public static Object getValue(PacketPlayOutEntityVelocity packet, String field) throws IllegalArgumentException, IllegalAccessException{
try{
for(Field f : packet.getClass().getDeclaredFields()){
   f.setAccessible(true);
   if(f.getName() == null ? field == null : f.getName().equals(field)){
   return f.get(packet);
   }
}}catch(SecurityException | IllegalArgumentException | IllegalAccessException e){
    System.out.println("Field exception:");
    e.printStackTrace();
}

System.out.println("Failed to handle packet "+packet.toString());
        return -1;
    }
    
    public static Object getValue(PacketPlayOutRelEntityMove packet, String field) throws IllegalArgumentException, IllegalAccessException{
try{
for(Field f : packet.getClass().getDeclaredFields()){
   f.setAccessible(true);
   if(f.getName() == null ? field == null : f.getName().equals(field)){
   return f.get(packet);
   }
}}catch(SecurityException | IllegalArgumentException e){
    System.out.println("Field exception:");
    e.printStackTrace();
}
System.out.println("Failed to handle packet "+packet.toString());
        return -1;
    }

    public static Object getValue(PacketPlayOutSpawnEntityLiving packet, String field) throws IllegalArgumentException, IllegalAccessException{
try{
for(Field f : packet.getClass().getDeclaredFields()){
   f.setAccessible(true);
   if(f.getName() == null ? field == null : f.getName().equals(field)){
   return f.get(packet);
   }
}}catch(SecurityException | IllegalArgumentException | IllegalAccessException e){
    System.out.println("Field exception:");
    e.printStackTrace();
}

System.out.println("Failed to handle packet "+packet.toString());
        return -1;
    }
    
    
    public static Object getValue(PacketPlayOutRelEntityMoveLook packet, String field) throws IllegalArgumentException, IllegalAccessException{
try{
for(Field f : packet.getClass().getDeclaredFields()){
   f.setAccessible(true);
   if(f.getName() == null ? field == null : f.getName().equals(field)){
   return f.get(packet);
   }
}}catch(SecurityException | IllegalArgumentException e){
    System.out.println("Field exception:");
    e.printStackTrace();
}

System.out.println("Failed to handle packet "+packet.toString());
        return -1;
    }
    public static Object getValue(PacketPlayOutEntityMetadata packet, String field) throws IllegalArgumentException, IllegalAccessException{
try{
for(Field f : packet.getClass().getDeclaredFields()){
   f.setAccessible(true);
   if(f.getName() == null ? field == null : f.getName().equals(field)){
   return f.get(packet);
   }
}}catch(SecurityException | IllegalArgumentException | IllegalAccessException e){
    System.out.println("Field exception:");
    e.printStackTrace();
}

System.out.println("Failed to handle packet "+packet.toString());
        return -1;
    }
    */

}