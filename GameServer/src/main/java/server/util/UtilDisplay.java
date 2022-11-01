package server.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class UtilDisplay
{
public static final int ENTITY_ID = 1234;
private static final HashMap<String, Boolean> hasHealthBar = new HashMap();

  public static void sendPacket(Player player, Packet packet) {
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
if(1 == 1){
    return;
}
    entityPlayer.playerConnection.sendPacket(packet);
  }

	public static boolean works = true;
public static String nmsver = null;


public static void sendActionBar(Player player, String message){
if(nmsver == null){
nmsver = Bukkit.getServer().getClass().getPackage().getName();
nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);    
}


    try {
Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
Object p = c1.cast(player);
Object ppoc = null;
Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
if (nmsver.equalsIgnoreCase("v1_8_R1") || !nmsver.startsWith("v1_8_")) {
Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
Method m3 = c2.getDeclaredMethod("a", String.class);
Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(cbc, (byte) 2);
} else {
Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
Object o = c2.getConstructor(new Class<?>[] {String.class}).newInstance(message);
ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(o, (byte) 2);
}
Method m1 = c1.getDeclaredMethod("getHandle");
Object h = m1.invoke(p);
Field f1 = h.getClass().getDeclaredField("playerConnection");
Object pc = f1.get(h);
Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
m5.invoke(pc, ppoc);
} catch (Exception ex) {
ex.printStackTrace();
works = false;
}
}

  public static PacketPlayOutSpawnEntityLiving getMobPacket(String text, double healthPercent, Location loc)
  {
      PacketPlayOutSpawnEntityLiving mobPacket = null;
      try {
           mobPacket = new PacketPlayOutSpawnEntityLiving();
          
          Field a = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("a");
          a.setAccessible(true);
          a.set(mobPacket, 1234);
          
          
          Field b = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("b");
          b.setAccessible(true);
          b.set(mobPacket, (int)EntityType.ENDER_DRAGON.getTypeId());
          
          Field c = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("c");
          c.setAccessible(true);
          c.set(mobPacket,((int)Math.floor(loc.getBlockX() * 32.0D)));
          
          
          Field d = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("d");
          d.setAccessible(true);
          d.set(mobPacket, MathHelper.floor(-6400.0D));
          
          
          Field e = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("e");
          e.setAccessible(true);
          e.set(mobPacket,((int)Math.floor(loc.getBlockZ() * 32.0D)));
          
          
          Field f = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("f");
          f.setAccessible(true);
          f.set(mobPacket, 0);
          
          Field g = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("g");
          g.setAccessible(true);
          g.set(mobPacket, 0);
          
          Field h = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("h");
          h.setAccessible(true);
          h.set(mobPacket, 0);
          
          Field i = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("i");
          i.setAccessible(true);
          i.set(mobPacket,(byte)0);
          
          Field j = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("j");
          j.setAccessible(true);
          j.set(mobPacket,(byte)0);
          
          Field k = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("k");
          k.setAccessible(true);
          k.set(mobPacket,(byte)0);
          
          
          
          DataWatcher watcher = getWatcher(text, healthPercent * 200.0D);
          
          Field l = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("l");
          l.setAccessible(true);
          l.set(mobPacket,watcher);
          
          return mobPacket;
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
          Logger.getLogger(UtilDisplay.class.getName()).log(Level.SEVERE, null, ex);
      }
      return mobPacket;
  }
 
  public static PacketPlayOutEntityDestroy getDestroyEntityPacket() {
    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(1234);

    return packet;
  }

  public static PacketPlayOutEntityMetadata getMetadataPacket(DataWatcher watcher) {
     PacketPlayOutEntityMetadata metaPacket = null;
      try
     {
       metaPacket = new PacketPlayOutEntityMetadata();
         
         Field a = PacketPlayOutEntityMetadata.class.getDeclaredField("a");
         a.setAccessible(true);
         a.set(metaPacket, 1234);
         try
         {
             Field b = PacketPlayOutEntityMetadata.class.getDeclaredField("b");
             
             b.setAccessible(true);
             b.set(metaPacket, watcher.c());
         } catch (Exception e) {
             e.printStackTrace();
         }
         
         return metaPacket;
     } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
          Logger.getLogger(UtilDisplay.class.getName()).log(Level.SEVERE, null, ex);
    }
      return metaPacket;
  }

  public static DataWatcher getWatcher(String text, double health) {
    DataWatcher watcher = new DataWatcher(null);

    watcher.a(0, (byte)32);
    watcher.a(6, (float)health);
    watcher.a(10, text);
    watcher.a(11, (byte)1);
    watcher.a(16, (int)health);

    return watcher;
  }

  public static void displayTextBar(JavaPlugin plugin, final Player player, double healthPercent, String text)
  {
      /*
    PacketPlayOutSpawnEntityLiving mobPacket = getMobPacket(text, healthPercent, player.getLocation());

    sendPacket(player, mobPacket);
    hasHealthBar.put(player.getId(), true);

    new BukkitRunnable()
    {
      @Override
      public void run() {
        PacketPlayOutEntityDestroy destroyEntityPacket = UtilDisplay.getDestroyEntityPacket();

        UtilDisplay.sendPacket(player, destroyEntityPacket);
        UtilDisplay.hasHealthBar.put(player.getId(), false);
      }
    }
    .runTaskLater(plugin, 120L);*/
  }

  public static void sendTitle(Player player, String text, String sub){
/*
TitleManager.sendTimings(player, 0, 50, 0);
TitleManager.sendTitle(player, "{\"text\":\"\",\"extra\":[{\"text\":\""+ text +"\",\"color\":\"yellow\"}]}");


if(sub != null){
TitleManager.sendSubTitle(player, "{\"text\":\"\",\"extra\":[{\"text\":\""+ sub +"\",\"color\":\"gold\"}]}");
}*/
  
  }
  
  public static void displayLoadingBar(final String text, final String completeText, final Player player, final int healthAdd, long delay, final boolean loadUp, final JavaPlugin plugin) {
 if(1==1){
     return;
 }
      PacketPlayOutSpawnEntityLiving mobPacket = getMobPacket(text, 0.0D, player.getLocation());

    sendPacket(player, mobPacket);
   hasHealthBar.put(player.getName(), true);

    new BukkitRunnable() {
      int health = player.getHealth() != 0 ? 0 : 200;

      @Override
      public void run()
      {
        if (loadUp ? health < 200 : health > 0) {
          DataWatcher watcher = UtilDisplay.getWatcher(text, health);
          PacketPlayOutEntityMetadata metaPacket = UtilDisplay.getMetadataPacket(watcher);

          UtilDisplay.sendPacket(player, metaPacket);

          if (loadUp)
            health += healthAdd;
          else
            health -= healthAdd;
        }
        else {
          DataWatcher watcher = UtilDisplay.getWatcher(text, loadUp ? 200 : 0);
          PacketPlayOutEntityMetadata metaPacket = UtilDisplay.getMetadataPacket(watcher);
          PacketPlayOutEntityDestroy destroyEntityPacket = UtilDisplay.getDestroyEntityPacket();

          UtilDisplay.sendPacket(player, metaPacket);
          UtilDisplay.sendPacket(player, destroyEntityPacket);
          UtilDisplay.hasHealthBar.put(player.getName(), false);

PacketPlayOutSpawnEntityLiving mobPacket = UtilDisplay.getMobPacket(completeText, 100.0D, player.getLocation());

          UtilDisplay.sendPacket(player, mobPacket);
          UtilDisplay.hasHealthBar.put(player.getName(), true);

          DataWatcher watcher2 = UtilDisplay.getWatcher(completeText, 200.0D);
          PacketPlayOutEntityMetadata metaPacket2 = UtilDisplay.getMetadataPacket(watcher2);

          UtilDisplay.sendPacket(player, metaPacket2);

          new BukkitRunnable()
          {
            @Override
            public void run() {
              PacketPlayOutEntityDestroy destroyEntityPacket = UtilDisplay.getDestroyEntityPacket();

              UtilDisplay.sendPacket(player, destroyEntityPacket);
              UtilDisplay.hasHealthBar.put(player.getName(), false);
            }
          }
          .runTaskLater(plugin, 40L);

          cancel();
        }
      }
    }
    .runTaskTimer(plugin, delay, delay);
  }

  public static void displayLoadingBar(String text, String completeText, Player player, int secondsDelay, boolean loadUp, JavaPlugin plugin) {
    if(1==1){
     return;
 }
      int healthChangePerSecond = 200 / secondsDelay;

    displayLoadingBar(text, completeText, player, healthChangePerSecond, 20L, loadUp, plugin);
  }
}