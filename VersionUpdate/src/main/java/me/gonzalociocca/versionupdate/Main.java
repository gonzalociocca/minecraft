/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.versionupdate;

/**
 *
 * @author ciocca
 */

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import me.gonzalociocca.versionupdate.customhopper.CustomHopperEvent;
import net.minecraft.server.v1_9_R1.PacketPlayOutMapChunk;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.v1_9_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityEffect;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.fusesource.jansi.Ansi;

public class Main extends JavaPlugin implements Listener
{
	public MessageListener	ml;
	
	public String[]	servers	= null;
	
	public Main instance = this;
   
	@Override
	public void onEnable()
	{
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener(this));
		
		getServers();
		
		ml = new MessageListener(this);
		
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this,this);
		pm.registerEvents(new PreCommandListener(this), this);
                
ItemStack is = new ItemStack(Material.GOLDEN_APPLE, 1,(short)1);

ShapelessRecipe sr = new ShapelessRecipe(is);
sr.addIngredient(4, Material.GOLD_BLOCK);
Bukkit.addRecipe(sr);

     }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHDApples(PlayerItemConsumeEvent event) {
        if(event.isCancelled()){
            return;
        }
        if(event.getItem() == null){
            return;
        }
                
        if(event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 1){
            event.setCancelled(true);
            
            Player p = event.getPlayer();
            p.sendMessage(Colorizer.Color("&aComida!"));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*45,1,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,20*120,3,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*300,1,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*300,1,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,20*45,1,true,true), true);
ItemStack it1 = p.getInventory().getItemInMainHand();
if(it1 != null){
    if(it1.getType() == Material.GOLDEN_APPLE && it1.getDurability() == 1){
        if(it1.getAmount() > 1){
            it1.setAmount(it1.getAmount()-1);
        }else{
            it1 = new ItemStack(Material.AIR);
        }
        p.getInventory().setItemInMainHand(it1);
        p.updateInventory();
        return;
    }
}

ItemStack it2 = p.getInventory().getItemInOffHand();
if(it2 != null){
    if(it2.getType() == Material.GOLDEN_APPLE && it2.getDurability() == 1){
        if(it2.getAmount() > 1){
            it2.setAmount(it2.getAmount()-1);
        }else{
            it2 = new ItemStack(Material.AIR);
        }
        p.getInventory().setItemInOffHand(it2);
        p.updateInventory();
        return;
    }
}
            
        }
        

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


 
  @EventHandler
  public void onPvP(org.bukkit.event.entity.EntityDamageByEntityEvent event){
      if(event.isCancelled()){
          return;
      }
      if(event.getDamage() <= 0){
          return;
      }
      if(event.getEntity() == null){
          return;
      }
      event.getEntity().playEffect(EntityEffect.HURT);
  
  }

    ConcurrentHashMap<Player,Limiter> limiters = new ConcurrentHashMap();

    public Limiter getLimiter(Player p){
        if(!limiters.containsKey(p)){
            limiters.put(p, new Limiter(p));
        }
        return limiters.get(p);
    }
    @EventHandler
    public void Limiter(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR)
        if(!this.getLimiter(event.getPlayer()).canInteract()){
            event.setCancelled(true);
        }}

    @EventHandler
    public void Limiter(PlayerItemHeldEvent event){
        if(!this.getLimiter(event.getPlayer()).canItemHeld()){
            event.setCancelled(true);
        }}
    @EventHandler
    public void Limiter(InventoryClickEvent event){
        if(!this.getLimiter((Player)event.getWhoClicked()).canInventoryClick()){
            event.setCancelled(true);
        }}
    @EventHandler
    public void Limiter(PlayerDropItemEvent event){
        if(!this.getLimiter((Player)event.getPlayer()).canDropItem()){
            event.setCancelled(true);
        }}
    @EventHandler
    public void Limiter(PlayerCommandPreprocessEvent event){
        if(!this.getLimiter((Player)event.getPlayer()).canUseCommand()){
            event.setCancelled(true);
        }}

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        limiters.remove(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getInventory().getItemInMainHand().getType().name().contains("CHEST")){
                BlockPlaceEvent evt = new BlockPlaceEvent(event.getClickedBlock().getRelative(event.getBlockFace()),event.getClickedBlock().getState(),event.getClickedBlock(),event.getPlayer().getInventory().getItemInMainHand(),event.getPlayer(),true);
                Bukkit.getServer().getPluginManager().callEvent(evt);
                if(evt.isCancelled()){
                    event.setCancelled(true);
                }else{
                    if(event.getPlayer().getInventory().getItemInMainHand().getAmount() > 1){
                        event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
                    }else{
                        event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    }
                }
            }
            if(event.getPlayer().getInventory().getItemInOffHand().getType().name().contains("CHEST")){
                BlockPlaceEvent evt = new BlockPlaceEvent(event.getClickedBlock().getRelative(event.getBlockFace()),event.getClickedBlock().getState(),event.getClickedBlock(),event.getPlayer().getInventory().getItemInOffHand(),event.getPlayer(),true);
                Bukkit.getServer().getPluginManager().callEvent(evt);
                if(evt.isCancelled()){
                    event.setCancelled(true);
                }else{
                    if(event.getPlayer().getInventory().getItemInOffHand().getAmount() > 1){
                        event.getPlayer().getInventory().getItemInOffHand().setAmount(event.getPlayer().getInventory().getItemInOffHand().getAmount()-1);
                    }else{
                        event.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    }
                }

            }
        }
    }



  @EventHandler
  public void OldPvPSpeed(PlayerJoinEvent event)
  {
 event.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(30.0D);
 event.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.10D);
  }

 public boolean shutdown = false;
	public void getServers()
	{
		final ByteArrayOutputStream b = new ByteArrayOutputStream();
		final DataOutputStream out = new DataOutputStream(b);
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run()
			{
                            

if(shutdown == false){
ZoneId zoneId = ZoneId.of( "America/Argentina/Buenos_Aires" );
ZonedDateTime zdt = ZonedDateTime.now( zoneId );
java.util.Date date = java.util.Date.from( zdt.toInstant() );
if(date.getHours() == 5){
    if(date.getMinutes() > 58){
        Bukkit.getServer().shutdown();
        shutdown = true;
    }
}}else{
    return;
}
                for(World w : Bukkit.getWorlds()){
                    if(w.getPlayers().size() <= 0){
                        continue;
                    }
                    w.setMonsterSpawnLimit(w.getPlayers().size()*20);
                    w.setAnimalSpawnLimit(w.getPlayers().size()*20);
                }
                            
				if(Bukkit.getOnlinePlayers().size() <= 0){
					return;}
				else
				{
					Player player = Bukkit.getOnlinePlayers().iterator().next();

					try
					{
						out.writeUTF("GetServers");
						out.close();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
					
					if(b.toByteArray().length > 32760)
					{
						b.reset();
						return;
					}
					
					player.sendPluginMessage(instance, "BungeeCord", b.toByteArray());
				}
			}
		}, 0L, 80L);
	}
}
