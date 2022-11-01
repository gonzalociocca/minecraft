/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.versionupdate;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener
{
	public Main plugin;
	
	public MessageListener(Main instance)
	{
		plugin = instance;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if(!channel.equals("BungeeCord"))
			return;
		
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		
		String sub = "";
		
		try
		{
			sub = in.readUTF();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		if(sub.equals("GetServers"))
		{
			try
			{
				plugin.servers = in.readUTF().split(", ");
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
