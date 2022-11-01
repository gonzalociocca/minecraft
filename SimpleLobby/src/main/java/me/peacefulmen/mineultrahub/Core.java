package me.peacefulmen.mineultrahub;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.gonzalociocca.mineultra.DBManager;
import me.gonzalociocca.mineultra.MineUltra;
import me.gonzalociocca.mineultra.Rank;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.Updater;
import mineultra.core.updater.event.UpdateEvent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class Core
  extends JavaPlugin
  implements Listener
{
  public Core instance = this;
  public FileConfiguration config = getConfig();
  public HashMap<String, Scoreboard> _scoreboard = new HashMap();
  private HashMap<String, Objective> _sideObjective = new HashMap();
  Random r = new Random();
  DBManager db;
  @Override
  public void onEnable()
  {
    saveDefaultConfig();
    db = new DBManager((MineUltra)Bukkit.getPluginManager().getPlugin("MineUltra"));

    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(this, this);
    
    loadValues();
    
    loadMenu1();
    loadMenu2();
    Updater upd = new Updater(this);

  }
  
  ItemStack[] menuinv1 = { new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14) };
  ItemStack[] menuinv2 = { new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14) };
  HashMap<String, String> getserver = new HashMap();
  
  public void loadMenu1()
  {
    List<ItemStack> newitems = new ArrayList();
    try
    {
      for (int a = 0; a < 50; a++)
      {
        ItemStack is = ItemReader.read(this.config.getString("Menu.items1.S" + a + ".item"));
        newitems.add(is);
        this.getserver.put(is.getItemMeta().getDisplayName(), this.config.getString("Menu.items1.S" + a + ".server"));
      }
    }
    catch (Exception e) {}
    int next = 1;
    for (int a = 0; a < newitems.size(); a++)
    {
      this.menuinv1[next] = ((ItemStack)newitems.get(a));
      next += 2;
    }
  }
  
  HashSet<String> ponys = new HashSet();
  ItemStack[] gadgetsmenu = { new ItemStack(Material.INK_SACK, 1, (short)5), new ItemStack(Material.INK_SACK, 1, (short)5), new ItemStack(Material.INK_SACK, 1, (short)5), new ItemStack(Material.INK_SACK, 1, (short)5), new ItemStack(Material.INK_SACK, 1,(short) 5), new ItemStack(Material.INK_SACK, 1, (short)5), new ItemStack(Material.INK_SACK, 1, (short)5), new ItemStack(Material.INK_SACK, 1, (short)5), new ItemStack(Material.INK_SACK, 1,(short) 5) };
  HashMap<String, Location> rods = new HashMap();
  
  @EventHandler
  public void noblock(BlockBreakEvent event)
  {
    if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.OWNER)){
      return;
    }
    event.setCancelled(true);
  }
  
  @EventHandler
  public void noblock(BlockPlaceEvent event)
  {
    if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.OWNER)){
      return;
    }
    event.setCancelled(true);
  }
  
  @EventHandler
  public void noblock(PlayerInteractEvent event)
  {
    if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.OWNER)){
      return;
    }
    if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
      event.setCancelled(true);
    }
  }
  
  public static void sendActionBar(Player player, String message)
  {
    CraftPlayer p = (CraftPlayer)player;
    IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    p.getHandle().sendMessage(cbc);
  }
  
  @EventHandler
  public void stay(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    for (Player p : Bukkit.getOnlinePlayers()) {
      if (p.getLocation().distance(Bukkit.getWorld("world").getSpawnLocation()) > 200.0D) {
        p.setHealth(0);
      }
    }
  }
  
  @EventHandler
  public void fishPerk(PlayerFishEvent event)
  {
    if (this.rods.containsKey(event.getPlayer().getName()))
    {
      Location rod = (Location)this.rods.get(event.getPlayer().getName());
      Vector loca = event.getPlayer().getLocation().getDirection();
      event.getPlayer().setVelocity(new Vector(loca.getX() * 5.0D, 1.0D, loca.getZ() * 5.0D));
      this.rods.remove(event.getPlayer().getName());
      return;
    }
    this.rods.put(event.getPlayer().getName(), event.getHook().getLocation());
  }
  
  @EventHandler
  public void noplace(BlockPlaceEvent event)
  {
    if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
      event.setCancelled(true);
    }
  }
  
  int nxwool = 0;
  
  public void setNext(Block ben)
  {
    this.nxwool += 1;
    if (this.nxwool > 15) {
      this.nxwool = 0;
    }
    ben.setTypeIdAndData(Material.WOOL.getId(), (byte)this.nxwool, true);
  }
  
  public Location getHorseOffSets(Location loca, int next)
  {
    if (next == 0) {
      return new Location(loca.getWorld(), loca.getX(), loca.getY() - 1.0D, loca.getZ());
    }
    if (next == 1) {
      return new Location(loca.getWorld(), loca.getX() + 1.0D, loca.getY() - 1.0D, loca.getZ());
    }
    if (next == 2) {
      return new Location(loca.getWorld(), loca.getX(), loca.getY() - 1.0D, loca.getZ() + 1.0D);
    }
    if (next == 3) {
      return new Location(loca.getWorld(), loca.getX() - 1.0D, loca.getY() - 1.0D, loca.getZ());
    }
    if (next == 4) {
      return new Location(loca.getWorld(), loca.getX(), loca.getY() - 1.0D, loca.getZ() - 1.0D);
    }
    return null;
  }
  
  public void loadMenu2()
  {
    List<ItemStack> newitems = new ArrayList();
    try
    {
      for (int a = 0; a < 50; a++)
      {
        ItemStack is = ItemReader.read(this.config.getString("Menu.items2.S" + a + ".item"));
        newitems.add(is);
        this.getserver.put(is.getItemMeta().getDisplayName(), this.config.getString("Menu.items2.S" + a + ".server"));
      }
    }
    catch (Exception e) {}
    int next = 1;
    for (int a = 0; a < newitems.size(); a++)
    {
      this.menuinv2[next] = ((ItemStack)newitems.get(a));
      next += 2;
    }
  }
  
  public void loadValues()
  {
    try
    {
      SVSign sv;
      for (int a = 0; a < 50; a++)
      {
        String[] locs = getConfig().getString("Menu.signs.S" + a + ".location").split(":");
        String[] server = getConfig().getString("Menu.signs.S" + a + ".server").split(",");
        String[] hsts = server[0].split(":");
        String[] prs = server[1].split(":");
        String display = server[2];
        String[] nms = server[3].split(":");
        List<String> names = new ArrayList();
        List<String> ports = new ArrayList();
        List<String> hosts = new ArrayList();
        for (String pr : prs) {
          ports.add(pr);
        }
        for (String hs : hsts) {
          hosts.add(hs);
        }
        for (String nm : nms) {
          names.add(nm);
        }
        List<Location> signs = new ArrayList();
        for (String loc : locs)
        {
          String[] lc = loc.split(",");
          signs.add(new Location(Bukkit.getWorld(lc[0]), Integer.parseInt(lc[1]), Integer.parseInt(lc[2]), Integer.parseInt(lc[3])));
        }
        sv = new SVSign(this, hosts, ports, signs, names, display);
      }
    }
    catch (Exception e) {}
  }
  
  @EventHandler
  public void onServer(InventoryClickEvent event)
  {
    if (event.getCurrentItem() == null) {
      return;
    }
    if (!event.getCurrentItem().hasItemMeta()) {
      return;
    }
    if (!event.getCurrentItem().getItemMeta().hasDisplayName()) {
      return;
    }
    if (!event.getCurrentItem().getItemMeta().hasLore()) {
      return;
    }
    Player p = (Player)event.getWhoClicked();
    if (this.getserver.get(event.getCurrentItem().getItemMeta().getDisplayName()) != null) {
      p.chat("/" + (String)this.getserver.get(event.getCurrentItem().getItemMeta().getDisplayName()));
    }
    if (db.getPlayerData(p).getRank().isAtLeast(Rank.OWNER)) {
      return;
    }
    event.setCancelled(true);
  }
  
  @EventHandler
  public void onMenu(PlayerInteractEvent event)
  {
    if (event.getItem() == null) {
      return;
    }
    if (!event.getItem().hasItemMeta()) {
      return;
    }
    if (!event.getItem().getItemMeta().hasDisplayName()) {
      return;
    }
    if (event.getItem().getType().equals(Material.TNT))
    {
      event.getPlayer().chat("/smash");
      event.setCancelled(true);
      return;
    }
    if (event.getItem().getType().equals(Material.NETHER_STAR))
    {
      Player p = event.getPlayer();
      
      Inventory inv = Bukkit.createInventory(null, 18, Colorizer.Color("&a&lServers &c&lPvP/Survival!"));
      inv.setContents(this.menuinv1);
      
      p.openInventory(inv);
      p.updateInventory();
    }
    else if (event.getItem().getType().equals(Material.MAGMA_CREAM))
    {
      Player p = event.getPlayer();
      
      Inventory inv = Bukkit.createInventory(null, 45, Colorizer.Color("&c&lMiniJuegos"));
      inv.setContents(this.menuinv2);
      
      p.openInventory(inv);
      p.updateInventory();
    }
    else if (event.getItem().getType().equals(Material.SUGAR))
    {
      event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
      event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 5, true));
      ItemStack sugar = event.getPlayer().getItemInHand();
      ItemMeta im = sugar.getItemMeta();
      im.setDisplayName(Colorizer.Color("&c&lDisable &e&lFastMode"));
      sugar.setItemMeta(im);
      sugar.setType(Material.SULPHUR);
    }
    else if (event.getItem().getType().equals(Material.SULPHUR))
    {
      event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
      event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0, true));
      ItemStack sugar = event.getPlayer().getItemInHand();
      ItemMeta im = sugar.getItemMeta();
      im.setDisplayName(Colorizer.Color("&a&lEnable &e&lFastMode"));
      sugar.setItemMeta(im);
      sugar.setType(Material.SUGAR);
    }
  }
  
  public String getPrefix(Player player)
  {
    return Colorizer.Color("&e");
  }
  
  public void SetPlayerScoreboardTeam(Player player)
  {
    for (Team team : GetScoreboard(player).getTeams()) {
      if (team.hasPlayer(player)) {
        team.removePlayer(player);
      }
    }
    String next = getPrefix(player);
    if (next.length() > 16) {
      next = next.substring(0, 16);
    }
    if (GetScoreboard(player).getTeam(next) == null) {
      ((Scoreboard)this._scoreboard.get(player.getName())).registerNewTeam(next).setPrefix(next + " ");
    }
    int nx = 10;
    ((Scoreboard)this._scoreboard.get(player.getName())).resetScores(Colorizer.Color("&f&l" + this.breset));
    this.breset = this.btotal;
    ((Objective)this._sideObjective.get(player.getName())).getScore(Colorizer.Color("&a &b")).setScore(nx);
    nx -= 1;
    ((Objective)this._sideObjective.get(player.getName())).getScore(Colorizer.Color("&6Rango:")).setScore(nx);
    nx -= 1;
    ((Objective)this._sideObjective.get(player.getName())).getScore("➡ "+db.getPlayerData(player).getRank().getScoreboardPrefix()).setScore(nx);
    nx -= 1;
    ((Objective)this._sideObjective.get(player.getName())).getScore(Colorizer.Color("&c &d")).setScore(nx);
    nx -= 1;
    ((Objective)this._sideObjective.get(player.getName())).getScore(Colorizer.Color("&6Coins:")).setScore(nx);
    nx -= 1;
    ((Objective)this._sideObjective.get(player.getName())).getScore(Colorizer.Color("➡ " + db.getPlayerData(player).getCoins())).setScore(nx);
    nx -= 1;
    ((Objective)this._sideObjective.get(player.getName())).getScore(Colorizer.Color("&d &f")).setScore(nx);
    nx -= 1;
    ((Objective)this._sideObjective.get(player.getName())).getScore(Colorizer.Color("&6Online:")).setScore(nx);
    nx -= 1;
    ((Objective)this._sideObjective.get(player.getName())).getScore(Colorizer.Color("➡ " + this.breset)).setScore(nx);
    nx -=1;
    ((Objective)this._sideObjective.get(player.getName())).getScore("      ").setScore(nx);
    nx -=1;
    ((Objective)this._sideObjective.get(player.getName())).getScore("www.mineultra.com").setScore(nx);

    GetScoreboard(player).getTeam(next).addPlayer(player);
    player.setScoreboard((Scoreboard)this._scoreboard.get(player.getName()));
  }

  @EventHandler
  public void updateScoreboard(UpdateEvent event){
    if(event.getType() != UpdateType.FAST){
      return;
    }
    this.setNextPrefix();
  }

  String g = ChatColor.GOLD+""+ChatColor.BOLD;
  String y = ChatColor.YELLOW+""+ChatColor.BOLD;
  String f = ChatColor.WHITE+""+ChatColor.BOLD;

  int nex = 0;

  public void setNextPrefix(){
    nex++;
    if(nex > 9){
      nex = 0;
    }
    for(Player p : Bukkit.getOnlinePlayers()){

      if(nex == 0){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(y+" "+"M"+f+"ineUltra")+"  ");
      } else if(nex == 1){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" "+"M"+y+"i"+f+"neUltra")+"  ");
      } else if(nex == 2){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" "+"Mi"+y+"n"+f+"eUltra")+"  ");
      } else if(nex == 3){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" "+"Min"+y+"e"+f+"Ultra")+"  ");
      } else if(nex == 4){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" "+"Mine"+y+"U"+f+"ltra")+"  ");
      } else if(nex == 5){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(y+" "+"M"+g+"ineU"+y+"l"+f+"tra")+"  ");
      } else if(nex == 6){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"M"+y+"i"+g+"neUl"+y+"t"+f+"ra")+"  ");
      } else if(nex == 7){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Mi"+y+"n"+g+"eUlt"+y+"r"+f+"a")+"  ");
      } else if(nex == 8){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Min"+y+"e"+g+"Ultr"+y+"a")+"  ");
      } else if(nex == 9){
        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Mine"+y+"U"+g+"ltra")+"  ");
      }
    }
  }


  public Scoreboard GetScoreboard(Player p)
  {
    if (!this._scoreboard.containsKey(p.getName()))
    {
      Scoreboard sco = Bukkit.getScoreboardManager().getNewScoreboard();
      this._sideObjective.put(p.getName(), sco.registerNewObjective("Obj" + this.r.nextInt(999999999), "dummy"));
      ((Objective)this._sideObjective.get(p.getName())).setDisplaySlot(DisplaySlot.SIDEBAR);
      ((Objective)this._sideObjective.get(p.getName())).setDisplayName(Colorizer.Color("&a-&l=&a&lMine&b&lUltra&b&l=&b-"));
      
      this._scoreboard.put(p.getName(), sco);
    }
    return (Scoreboard)this._scoreboard.get(p.getName());
  }
  
  @EventHandler
  public void setTeam(PlayerJoinEvent event)
  {
    SetPlayerScoreboardTeam(event.getPlayer());
    event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1));
  }
  
  @EventHandler
  public void setTeam(PlayerRespawnEvent event)
  {
    SetPlayerScoreboardTeam(event.getPlayer());
    event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
    
    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1));
  }
  
  @EventHandler
  public void removeTeam(PlayerQuitEvent event)
  {
    try
    {
      for (Team team : GetScoreboard(event.getPlayer()).getTeams()) {
        if (team.hasPlayer(event.getPlayer())) {
          team.removePlayer(event.getPlayer());
        }
      }
    }
    catch (IllegalArgumentException|IllegalStateException e) {}
    ((Scoreboard)this._scoreboard.get(event.getPlayer().getName())).clearSlot(DisplaySlot.SIDEBAR);
    this._scoreboard.remove(event.getPlayer().getName());
    this._sideObjective.remove(event.getPlayer().getName());
  }

  
  @EventHandler
  public void onWChange(PlayerChangedWorldEvent event)
  {
    SetPlayerScoreboardTeam(event.getPlayer());
  }
  
  @EventHandler
  public void colorChat(PlayerChatEvent event)
  {
    event.setFormat(Colorizer.Color(db.getPlayerData(event.getPlayer()).getRank().getChatPrefix()+event.getPlayer().getName()+"&f: "+event.getMessage()));
  }
  
  @EventHandler
  public void onpre(PlayerPreLoginEvent event)
  {
    if (event.getResult() == PlayerPreLoginEvent.Result.KICK_FULL)
    {
      if (Bukkit.getOnlinePlayers().size() > 200) {
        return;
      }
      event.setResult(PlayerPreLoginEvent.Result.ALLOWED);
      event.allow();
    }
  }
  
  @EventHandler
  public void onpre(PlayerLoginEvent event)
  {
    if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL)
    {
      if (Bukkit.getOnlinePlayers().size() > 200) {
        return;
      }
      event.setResult(PlayerLoginEvent.Result.ALLOWED);
      event.allow();
    }
  }
  
  @EventHandler
  public void net(PlayerDropItemEvent event)
  {
    if (db.getPlayerData(event.getPlayer().getName()).getRank().isAtLeast(Rank.OWNER)) {
      return;
    }
    event.setCancelled(true);
  }
  
  @EventHandler
  public void onnet2(InventoryClickEvent event)
  {
    if(event.getWhoClicked()==null){
      return;
    }
    if (db.getPlayerData(event.getWhoClicked().getName()).getRank().isAtLeast(Rank.OWNER)) {
      return;
    }
    event.setCancelled(true);
  }
  
  @EventHandler
  public void nodie(PlayerDeathEvent event)
  {
    event.setDeathMessage("");
  }
  
  @EventHandler
  public void solo(PlayerRespawnEvent event)
  {
    this.isVanish.put(event.getPlayer().getName(), Boolean.valueOf(true));
    for (Player p : Bukkit.getOnlinePlayers())
    {
      if (!this.isVanish.containsKey(p.getName())) {
        this.isVanish.put(p.getName(), Boolean.valueOf(true));
      }
      event.getPlayer().hidePlayer(p);
      if (((Boolean)this.isVanish.get(p.getName())).booleanValue() == true) {
        p.hidePlayer(event.getPlayer());
      }
    }
    event.getPlayer().setDisplayName(Colorizer.Color("&3&l> &b&l" + event.getPlayer().getName()));
    Player p = event.getPlayer();
    ItemStack star = new ItemStack(Material.NETHER_STAR, 1);
    ItemMeta im = star.getItemMeta();
    im.setDisplayName(Colorizer.Color("&a&lServer Survival+Faction!"));
    star.setItemMeta(im);
    
    ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
    ItemMeta im2 = glass.getItemMeta();
    im2.setDisplayName(Colorizer.Color("&a          &r"));
    glass.setItemMeta(im2);
    glass.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
    
    ItemStack shop = new ItemStack(Material.DIAMOND, 1);
    ItemMeta im3 = shop.getItemMeta();
    im3.setDisplayName(Colorizer.Color("&e&lHelp us keep the Server!&r"));
    shop.setItemMeta(im3);
    
    ItemStack web = new ItemStack(Material.EMERALD, 1);
    ItemMeta im4 = web.getItemMeta();
    im4.setDisplayName(Colorizer.Color("&e&lOur forums!&r"));
    web.setItemMeta(im4);
    
    ItemStack yt = new ItemStack(Material.EXP_BOTTLE, 1);
    ItemMeta im5 = yt.getItemMeta();
    im5.setDisplayName(Colorizer.Color("&c&lYoutuber Rank&r"));
    yt.setItemMeta(im5);
    
    ItemStack invi = new ItemStack(Material.STICK, 1);
    ItemMeta im6 = invi.getItemMeta();
    im6.setDisplayName(Colorizer.Color("&a&lShow Players&r"));
    invi.setItemMeta(im6);
    
    ItemStack gad = new ItemStack(Material.FISHING_ROD, 1);
    ItemMeta gadm = gad.getItemMeta();
    gadm.setDisplayName(Colorizer.Color("&e&lLucky ROD&r"));
    gad.setItemMeta(gadm);
    
    ItemStack sugar = new ItemStack(Material.SUGAR, 1);
    ItemMeta sugarm = sugar.getItemMeta();
    sugarm.setDisplayName(Colorizer.Color("&a&lEnable &e&lFastMode"));
    sugar.setItemMeta(sugarm);
    
    p.getInventory().setContents(new ItemStack[] { star, m2, null, gad, sugar, shop, web, yt, invi, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass });
    
    p.updateInventory();
    p.updateInventory();
  }
  
  HashMap<String, Boolean> isVanish = new HashMap();
  
  @EventHandler
  public void solo(PlayerJoinEvent event)
  {
    this.isVanish.put(event.getPlayer().getName(), Boolean.valueOf(true));
    for (Player p : Bukkit.getOnlinePlayers())
    {
      if (!this.isVanish.containsKey(p.getName())) {
        this.isVanish.put(p.getName(), Boolean.valueOf(true));
      }
      event.getPlayer().hidePlayer(p);
      if (((Boolean)this.isVanish.get(p.getName())).booleanValue() == true) {
        p.hidePlayer(event.getPlayer());
      }
    }
    event.setJoinMessage(null);
    event.getPlayer().setDisplayName(Colorizer.Color("&3&l> &b&l" + event.getPlayer().getName()));
    Player p = event.getPlayer();
    ItemStack star = new ItemStack(Material.NETHER_STAR, 1);
    ItemMeta im = star.getItemMeta();
    im.setDisplayName(Colorizer.Color("&a&lServer Survival+Faction!"));
    star.setItemMeta(im);
    
    ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
    ItemMeta im2 = glass.getItemMeta();
    im2.setDisplayName(Colorizer.Color("&a          &r"));
    glass.setItemMeta(im2);
    glass.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
    
    ItemStack shop = new ItemStack(Material.DIAMOND, 1);
    ItemMeta im3 = shop.getItemMeta();
    im3.setDisplayName(Colorizer.Color("&e&lHelp us keep the Server!&r"));
    shop.setItemMeta(im3);
    
    ItemStack web = new ItemStack(Material.EMERALD, 1);
    ItemMeta im4 = web.getItemMeta();
    im4.setDisplayName(Colorizer.Color("&e&lOur forums!&r"));
    web.setItemMeta(im4);
    
    ItemStack yt = new ItemStack(Material.EXP_BOTTLE, 1);
    ItemMeta im5 = yt.getItemMeta();
    im5.setDisplayName(Colorizer.Color("&c&lYoutuber Rank&r"));
    yt.setItemMeta(im5);
    
    ItemStack invi = new ItemStack(Material.STICK, 1);
    ItemMeta im6 = invi.getItemMeta();
    im6.setDisplayName(Colorizer.Color("&a&lShow Players&r"));
    invi.setItemMeta(im6);
    
    ItemStack gad = new ItemStack(Material.FISHING_ROD, 1);
    ItemMeta gadm = gad.getItemMeta();
    gadm.setDisplayName(Colorizer.Color("&e&lLucky ROD&r"));
    gad.setItemMeta(gadm);
    
    ItemStack sugar = new ItemStack(Material.SUGAR, 1);
    ItemMeta sugarm = sugar.getItemMeta();
    sugarm.setDisplayName(Colorizer.Color("&a&lEnable &e&lFastMode"));
    sugar.setItemMeta(sugarm);
    
    p.getInventory().setContents(new ItemStack[] { star, m2, glass, gad, sugar, shop, web, yt, invi, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass, glass });
    
    p.updateInventory();
    p.updateInventory();
  }
  
  @EventHandler
  public void onMoveme(PlayerMoveEvent event)
  {
    if (event.getPlayer().getLocation().getY() < 0.0D) {
      event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
    }
  }
  
  @EventHandler
  public void onYT(PlayerInteractEvent event)
  {
    if (event.getPlayer().getItemInHand() == null) {
      return;
    }
    if ((event.getClickedBlock() != null) && (event.getPlayer().getGameMode() != GameMode.CREATIVE)) {
      event.setCancelled(true);
    }
    if (event.getPlayer().getItemInHand().getType() == Material.EXP_BOTTLE)
    {
      event.getPlayer().sendMessage(Colorizer.Color("&a&m&l---------------------------------------------"));
      event.getPlayer().sendMessage(Colorizer.Color("&a                                &r"));
      event.getPlayer().sendMessage(Colorizer.Color("&e&lBuscamos youtubers con 500subs en adelante"));
      event.getPlayer().sendMessage(Colorizer.Color("&epara ayudarnos a agrandar el server"));
      event.getPlayer().sendMessage(Colorizer.Color("&ehay personas, que nos ayudan en ello, pero no es tan facil"));
      event.getPlayer().sendMessage(Colorizer.Color("&eSolicitalo en: &chttp://www.minelevel.com/foro"));
      event.getPlayer().sendMessage(Colorizer.Color("&a                                &r"));
      event.getPlayer().sendMessage(Colorizer.Color("&a&m&l---------------------------------------------"));
      event.setCancelled(true);
    }
    else if (event.getPlayer().getItemInHand().getType() == Material.DIAMOND)
    {
      event.getPlayer().sendMessage(Colorizer.Color("&a&m&l---------------------------------------------"));
      event.getPlayer().sendMessage(Colorizer.Color("&a                                &r"));
      event.getPlayer().sendMessage(Colorizer.Color("&e&lAyudanos a mantener y mejorar el servidor!"));
      event.getPlayer().sendMessage(Colorizer.Color("&eComprando un vip para la modalidad que mas te guste"));
      event.getPlayer().sendMessage(Colorizer.Color("&epodras tener Kits, dinero xp, y mucho mas!"));
      event.getPlayer().sendMessage(Colorizer.Color("&eSolo ve a: &b&lhttp://www.MineLevel.com/tienda"));
      event.getPlayer().sendMessage(Colorizer.Color("&a                                &r"));
      event.getPlayer().sendMessage(Colorizer.Color("&a&m&l---------------------------------------------"));
    }
    else if (event.getPlayer().getItemInHand().getType() == Material.EMERALD)
    {
      event.getPlayer().sendMessage(Colorizer.Color("&a&m&l---------------------------------------------"));
      event.getPlayer().sendMessage(Colorizer.Color("&a                                &r"));
      event.getPlayer().sendMessage(Colorizer.Color("&e&lFamiliarizate con el servidor!"));
      event.getPlayer().sendMessage(Colorizer.Color("&eNecesitas ayuda?, quieres nuevos amigos?"));
      event.getPlayer().sendMessage(Colorizer.Color("&eeso y muchas otras cosas mas en nuestra web"));
      event.getPlayer().sendMessage(Colorizer.Color("&eSolo ve a: &b&lhttp://www.MineLevel.com/tienda"));
      event.getPlayer().sendMessage(Colorizer.Color("&a                                &r"));
      event.getPlayer().sendMessage(Colorizer.Color("&a&m&l---------------------------------------------"));
    }
  }
  
  @EventHandler
  public void onHIDE(PlayerInteractEvent event)
  {
    if (event.getPlayer().getItemInHand() == null) {
      return;
    }
    if (event.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD)
    {
      for (Player p : Bukkit.getOnlinePlayers()) {
        event.getPlayer().hidePlayer(p);
      }
      ItemStack stick = new ItemStack(Material.STICK);
      ItemMeta im = stick.getItemMeta();
      im.setDisplayName(Colorizer.Color("&a&lMostrar Jugadores&r"));
      stick.setItemMeta(im);
      event.getPlayer().setItemInHand(stick);
      this.isVanish.put(event.getPlayer().getName(), Boolean.valueOf(true));
    }
    else if (event.getPlayer().getItemInHand().getType() == Material.STICK)
    {
      for (Player p : Bukkit.getOnlinePlayers()) {
        event.getPlayer().showPlayer(p);
      }
      ItemStack stick = new ItemStack(Material.BLAZE_ROD);
      ItemMeta im = stick.getItemMeta();
      im.setDisplayName(Colorizer.Color("&c&lOcultar Jugadores&r"));
      stick.setItemMeta(im);
      event.getPlayer().setItemInHand(stick);
      this.isVanish.put(event.getPlayer().getName(), Boolean.valueOf(false));
    }
  }
  
  @EventHandler
  public void hidej2(PlayerQuitEvent event)
  {
    event.setQuitMessage(null);
  }
  
  @EventHandler
  public void endmg(EntityDamageEvent event)
  {
    event.setCancelled(true);
  }
}
