package mineultra.core.common.util;

import com.destroystokyo.paper.Title;
import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Location;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;

public class UtilDisplay
{
private static final HashMap<String, Boolean> hasHealthBar = new HashMap();

	public static boolean works = true;


public static void sendActionBar(Player player, String message){
    IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc,(byte)2);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
}
 
  public static PacketPlayOutEntityDestroy getDestroyEntityPacket() {

    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { 1234 });

    return packet;
  }
    public static void sendTitle(Player player, String text, String sub){
      //  Title tit = new Title(""+text,""+sub);
      //  player.sendTitle(tit);
    }

  public static void displayTextBar(JavaPlugin plugin, final Player player, double healthPercent, String text)
  {
 hasHealthBar.put(player.getName(), true);
 
  }


  public static void displayLoadingBar(final String text, final String completeText, final Player player, final int healthAdd, long delay, final boolean loadUp, final JavaPlugin plugin) {
    if(1==1){
        return;
    }
      BossBar bar1 = Bukkit.createBossBar(completeText, BarColor.BLUE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
      bar1.setTitle(completeText);
      bar1.setVisible(true);
      bar1.setProgress(100);
      bar1.setColor(BarColor.RED);
      bar1.setStyle(BarStyle.SEGMENTED_20);
      bar1.addPlayer(player);

  }

  public static void displayLoadingBar(String text, String completeText, Player player, int secondsDelay, boolean loadUp, JavaPlugin plugin) {
if(1==1){
    return;
}
      int healthChangePerSecond = 200 / secondsDelay;

    displayLoadingBar(text, completeText, player, healthChangePerSecond, 20L, loadUp, plugin);
  }
}