package me.peacefulmen.mineultrahub;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;

public class SVSign
  implements Listener
{
  private final List<String> hosts;
  private final String display;
  private final Core p;
  private final HashMap<Integer, ServerListPing> svs = new HashMap();
  private final HashMap<Integer, Integer> svscount = new HashMap();
  private final HashMap<Integer, Integer> svsmax = new HashMap();
  private final HashMap<Integer, String> svsname = new HashMap();
  private List<Location> signs = new ArrayList();
  
  public SVSign(Core plugin, List<String> _host, List<String> ports, List<Location> _signs, List<String> names, String _display)
  {
    this.p = plugin;
    this.hosts = _host;
    this.display = Colorizer.Color(_display);
    this.signs = _signs;
    for (int a = 0; a < names.size(); a++)
    {
      ServerListPing sv = new ServerListPing(new InetSocketAddress((String)this.hosts.get(a), Integer.parseInt((String)ports.get(a))));
      
      this.svs.put(a, sv);
      this.svsname.put(a, names.get(a));
      System.out.println("New SVSigns created");
    }
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(this, plugin);
  }
  
  int next = 0;
  
  @EventHandler
  public void updateCount(UpdateEvent event)
  {
    if (event.getType() != UpdateType.MIN1) {
      return;
    }
    this.next += 1;
    if (this.next >= this.svs.size()) {
      this.next = 0;
    }
    try
    {
      StatusResponse pl = ((ServerListPing)this.svs.get(this.next)).fetchData();
      this.svscount.put(this.next,Integer.parseInt(pl.getOnline()));
      this.svsmax.put(this.next, Integer.parseInt(pl.getMaxOnline()));
    }
    catch (Exception e) {}
  }
  
  int next2 = 0;
  
  @EventHandler
  public void updateSigns(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    this.next2 += 1;
    if (this.next2 >= this.signs.size()) {
      this.next2 = 0;
    }
    updateSign(this.next2);
  }
  
  public void updateSign(int a)
  {
    Block block = Bukkit.getWorld("world").getBlockAt((Location)this.signs.get(a));
    Sign thesign = (Sign)block.getState();
    thesign.setLine(0, Colorizer.Color("&a&l------------"));
    thesign.setLine(1, Colorizer.Color(this.display));
    thesign.setLine(2, Colorizer.Color("&4&l" + getCount() + "/" + getMaxCount()));
    thesign.setLine(3, Colorizer.Color("&a&l------------"));
    thesign.update();
  }
  
  public String GetDisplay()
  {
    return Colorizer.Color(this.display);
  }
  
  public int getMaxCount()
  {
    int all = 0;
    for (int a = 0; a < this.svsmax.size(); a++) {
      all += (this.svsmax.get(a));
    }
    return all;
  }
  
  public int getCount()
  {
    int all = 0;
    for (int a = 0; a < this.svscount.size(); a++) {
      all += (this.svscount.get(a));
    }
    return all;
  }
  
  Random r = new Random();
  HashMap<String, Long> slow = new HashMap();
  
  @EventHandler
  public void portals(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    
    for (Player pep : Bukkit.getOnlinePlayers()) {
      if ((pep.getLocation().getBlock().getType() == Material.STATIONARY_WATER) || (pep.getLocation().getBlock().getType() == Material.WATER)) {
        for (Location loc : this.signs) {
          if ((!this.slow.containsKey(pep.getName())) || 
            ((this.slow.get(pep.getName())) <= System.currentTimeMillis())) {
            if (loc.distance(pep.getLocation()) < 10.0D)
            {
              int low = 9999;
              String sv = (String)this.svsname.get(0);
              for (int a = 0; a < this.svscount.size(); a++) {
                if ((this.svscount.get(a)) < low)
                {
                  low = (this.svscount.get(a));
                  sv = (String)this.svsname.get(a);
                }
              }
              this.slow.put(pep.getName(), System.currentTimeMillis() + 500L);
              pep.chat("/" + sv);
            }
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onSignEvent(PlayerInteractEvent event)
  {
    if ((event.getAction() != Action.RIGHT_CLICK_BLOCK) && (event.getAction() != Action.LEFT_CLICK_BLOCK)) {
      return;
    }
    Block be = event.getClickedBlock();
    if ((be.getType() != Material.SIGN_POST) && (be.getType() != Material.SIGN) && (be.getType() != Material.WALL_SIGN)) {
      return;
    }
    Sign sign = (Sign)be.getState();
    String[] lines = sign.getLines();
    if (ChatColor.stripColor(lines[1]).equalsIgnoreCase(ChatColor.stripColor(this.display)))
    {
      int low = 9999;
      String sv = (String)this.svsname.get(0);
      for (int a = 0; a < this.svscount.size(); a++) {
        if ((this.svscount.get(a)) < low)
        {
          low = (this.svscount.get(a));
          sv = (String)this.svsname.get(a);
        }
      }
      event.getPlayer().chat("/" + sv);
      event.setCancelled(true);
    }
  }
}
