/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.customhopper;

/**
 *
 * @author ciocca
 */

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import me.gonzalociocca.versionupdate.customhopper.CustomHopperEvent;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;


public class Main extends JavaPlugin implements Listener
{
	
	@Override
	public void onEnable()
	{
		PluginManager pm = this.getServer().getPluginManager();
                pm.registerEvents(this, this);

	}
        
 

 @EventHandler(ignoreCancelled = true)
 public void onMove(final InventoryMoveItemEvent event){
     if(event.getSource() != null){

if(event.getDestination()==null){
    return;
}
event.setCancelled(true);
getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
@Override
public void run() {

             Bukkit.getPluginManager().callEvent(new CustomHopperEvent(event.getSource(),event.getDestination()));

}
}, 1L);

         
     }

 }
}
