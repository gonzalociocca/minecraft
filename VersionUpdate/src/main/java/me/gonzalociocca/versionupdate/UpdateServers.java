/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.versionupdate;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class UpdateServers extends BukkitRunnable
{
	public Main plugin;
	
	public UpdateServers(Main instance)
	{
		this.plugin = instance;
	}
	
	ByteArrayOutputStream b = new ByteArrayOutputStream();
	DataOutputStream out = new DataOutputStream(b);

	@Override
	public void run()
	{
		
		
		if(Bukkit.getOnlinePlayers().size() <= 0)
			return;
		else
		{
			Player player = Bukkit.getOnlinePlayers().iterator().next();
			
			try
			{
				out.writeUTF("GetServers");
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
		}
	}
}
