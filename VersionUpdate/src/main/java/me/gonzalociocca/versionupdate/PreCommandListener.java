/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.versionupdate;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredListener;


public class PreCommandListener implements Listener
{
	public Main plugin;
	
	public PreCommandListener(Main instance)
	{
		plugin = instance;
	}
        
	@EventHandler
	public void onPlayerPreCommand(PlayerCommandPreprocessEvent event) throws IOException
	{
            
            if(event.isCancelled()){
                return;
            }
		Player player = event.getPlayer();

   if(event.getMessage().equalsIgnoreCase("/entity")){
       World w = player.getWorld();
       HashMap<String,Integer> count = new HashMap();
       for(EntityType ett : EntityType.values()){
           count.put(ett.name(), 0);
       }
       for(Entity et : w.getEntities()){
         count.put(et.getType().name(), count.get(et.getType().name())+1);
       }
       for(String str : count.keySet()){
           if(count.get(str) <= 0){
               continue;
           }
           player.sendMessage(Colorizer.Color("&a"+str+": "+count.get(str)));
       }
       for(RegisteredListener rs : PlayerInteractEvent.getHandlerList().getRegisteredListeners()){
           if(rs.getPlugin().getName().equalsIgnoreCase("WorldEdit")){
          PlayerInteractEvent.getHandlerList().unregister(rs);}
       }

       event.setCancelled(true);
   }

		
		if(plugin.servers == null)
		{
			return;
		}
		
		for(String s : plugin.servers)
		{
			if(event.getMessage().equalsIgnoreCase("/" + s))
				{
					event.setCancelled(true);
					
					ByteArrayOutputStream b = new ByteArrayOutputStream();
                            try (DataOutputStream out = new DataOutputStream(b)) {
                                out.writeUTF("Connect");
                                out.writeUTF(s);
                            }
					
					player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
					return;
				}
			
	
		}
	}
}
