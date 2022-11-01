/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.util;


import java.lang.reflect.Field;

public class UtilReflection
{
    public static void set(Object object, String fieldName, Object value){
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if(field != null) {
            field.setAccessible(true);
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Field "+fieldName+" is Null w/obj: " + object.getClass().getSimpleName() +" w/val: "+value);
        }
    }

    public static Object get(Object object, String fieldName){
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if(field != null) {
            field.setAccessible(true);
            try {
                return field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Field "+fieldName+" is Null w/obj: " + object.getClass().getSimpleName());
        }
        return null;
    }
 /*
    public static Object getValue(PacketPlayOutEntityTeleport packet, String field) throws IllegalArgumentException, IllegalAccessException{
try{
for(Field f : packet.getClass().getDeclaredFields()){
   f.setAccessible(true);
   if(f.getId() == null ? field == null : f.getId().equals(field)){
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
   if(f.getId() == null ? field == null : f.getId().equals(field)){
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
   if(f.getId() == null ? field == null : f.getId().equals(field)){
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
   if(f.getId() == null ? field == null : f.getId().equals(field)){
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
   if(f.getId() == null ? field == null : f.getId().equals(field)){
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
   if(f.getId() == null ? field == null : f.getId().equals(field)){
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
   if(f.getId() == null ? field == null : f.getId().equals(field)){
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