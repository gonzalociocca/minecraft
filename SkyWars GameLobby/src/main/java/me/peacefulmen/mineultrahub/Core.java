package me.peacefulmen.mineultrahub;

import me.gonzalociocca.mineultra.*;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.Updater;
import mineultra.core.updater.event.UpdateEvent;
import net.minecraft.server.v1_9_R1.EntityGiantZombie;
import net.minecraft.server.v1_9_R1.EntityMonster;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class Core
  extends JavaPlugin
  implements Listener

{

  Random r = new Random();
    DBManager db;


  @Override
  public void onEnable()
  {
db = new DBManager(this);
    saveDefaultConfig();

    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(this, this);
Updater upd = new Updater(this);



  }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getPlayer().getItemInHand() != null){

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR){
            return;
        }

            if(event.getPlayer().getItemInHand().getType().equals(Material.COMPASS)){
                Inventory iv = Bukkit.createInventory(null,54,Colorizer.Color("Navegador de Servers"));
                for(int a = 0; a < iv.getSize();a++){
                    iv.setItem(a,makeItem(Material.STAINED_GLASS_PANE,1,(short)9,"   ",null));
                }
                for(serverType type : serverType.values()){
                    if(type.getIcon()!=Material.AIR){
                        iv.setItem(type.getSlot(),makeItem(type.getIcon(),1,type.getDurability(),"&b"+type.getName(),Arrays.asList("  ",Colorizer.Color("&6"+svinfo.get(type).getOnline()+"/"+svinfo.get(type).getMaxOnline()),"   ",svinfo.get(type).getStatus(),"   ")));
                    }
                }
                event.getPlayer().openInventory(iv);
                event.getPlayer().updateInventory();
                event.setCancelled(true);
            }
            else if(event.getPlayer().getItemInHand().getType().equals(Material.WRITTEN_BOOK)){
                event.setCancelled(false);
                return;
            }

    }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(event.getPlayer() ==null){
            return;
        }
        if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.Helper)){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        event.setFormat(db.getPlayerData(event.getPlayer()).getRank().getChatPrefix()+event.getPlayer().getName()+": "+event.getMessage());
    }



    @EventHandler
    public void onClickServer(InventoryClickEvent event) throws SQLException {
        if(event.getCurrentItem()==null){
            return;
        }
        if(!event.getCurrentItem().hasItemMeta()){
            return;
        }
        if(!event.getCurrentItem().getItemMeta().hasDisplayName()){
            return;
        }
        String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

        if(event.getCurrentItem().getItemMeta().hasLore()){
            if(event.getCurrentItem().getItemMeta().getLore().size() == 4){
                if(!event.getCurrentItem().getItemMeta().getDisplayName().contains("Reiniciando")){
                    List<String> lore = event.getCurrentItem().getItemMeta().getLore();
                    String[] ld = ChatColor.stripColor(lore.get(1).split(" ")[1]).split("/");
                    int online = Integer.parseInt(ld[0]);
                    int maxonline = Integer.parseInt(ld[1]);
                    if(online>=maxonline){
                        Player p =(Player)event.getWhoClicked();
                        if(!db.getPlayerData(p).getRank().isAtLeast(Rank.VIP)){
                        event.getWhoClicked().sendMessage(Colorizer.Color("&cEl server esta lleno"));
                        event.setCancelled(true);
                        event.setResult(Event.Result.DENY);
                        return;}
                    }
                String server = ChatColor.stripColor(lore.get(3)).substring(7);
                ((Player)event.getWhoClicked()).chat("/"+server);
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                return;}else{
                    event.getWhoClicked().sendMessage(Colorizer.Color("&cEl server se esta reiniciando, espera."));
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }
        }
        event.setCancelled(true);
event.setResult(Event.Result.DENY);

            serverType type = serverType.getbyName(name);
        if(type == type.Unknown){
            return;
        }
        if(type.isSingle()){
            if(svinfo.get(type).isFull() && !db.getPlayerData((Player)event.getWhoClicked()).getRank().isAtLeast(Rank.VIP)){
                ((Player) event.getWhoClicked()).sendMessage(Colorizer.Color("&cServer Lleno,&e los VIP tienen prioridad al entrar."));
            return;
            }
            ((Player)event.getWhoClicked()).chat(type.getCMD());
            return;
        }
        event.getWhoClicked().closeInventory();

            ((Player)event.getWhoClicked()).openInventory(games.get(type.getName()));
            ((Player)event.getWhoClicked()).updateInventory();

    }

    HashMap<String,Inventory> games = new HashMap();

    public ItemStack makeItem(Material mat, int amount, short dura, String name,List<String> lore){
        ItemStack it = new ItemStack(mat,amount,dura);
        ItemMeta meta = it.getItemMeta();
        if(name != null) {
            meta.setDisplayName(Colorizer.Color(name));
        }
        if(lore != null){
        meta.setLore(lore);}

        it.setItemMeta(meta);
if(!it.getType().name().toLowerCase().contains("glass")){
    it = addGlow(it);
}
        return it;
    }

    private ItemStack addGlow(org.bukkit.inventory.ItemStack stack) {
        net.minecraft.server.v1_9_R1.ItemStack nmsStack = (net.minecraft.server.v1_9_R1.ItemStack) getField(CraftItemStack.asCraftCopy(stack), "handle");
        // Initialize the compound if we need to
        if (!nmsStack.hasTag()) {
            nmsStack.setTag(new NBTTagCompound());
        }
        NBTTagCompound compound = nmsStack.getTag();

        // Empty enchanting compound
        compound.set("ench", new NBTTagList());

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    private Object getField(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);

            return field.get(obj);
        } catch (Exception e) {
            // We don't care
        }        return null;
    }


    HashMap<serverType,ServerINFO> svinfo = new HashMap();


    @EventHandler
    public void updateServers(UpdateEvent event){
        if(event.getType() == UpdateType.SEC){
            for(Inventory iv : games.values()){
                for(ItemStack it : iv.getContents()){
                    if(it==null){
                        return;
                    }
                    if(it.getDurability()==4){
                        try{
                            ItemMeta im = it.getItemMeta();
                            int time = Integer.parseInt(ChatColor.stripColor(im.getDisplayName()).substring(13));
                            if(time > 1) {
                                im.setDisplayName(Colorizer.Color("&aEmpezando en " + (time - 1)));
                            }else{
                                im.setDisplayName(Colorizer.Color("&cEn progreso"));
                                it.setDurability((short)14);
                            }
                            it.setItemMeta(im);
                        }catch(Exception e){
e.printStackTrace();
                        }
                    }
                }
            }
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.getOpenInventory() != null && p.getOpenInventory().getTitle() != null && p.getOpenInventory().getTitle().contains("Navegador")){
                    Inventory iv = p.getOpenInventory().getTopInventory();

                    for(serverType type : serverType.values()){
                        if(type.getIcon()!=Material.AIR){
                            iv.setItem(type.getSlot(),this.makeItem(type.getIcon(),1,type.getDurability(),"&b"+type.getName(),Arrays.asList("  ",Colorizer.Color("&6"+svinfo.get(type).getOnline()+"/"+svinfo.get(type).getMaxOnline()),"   ",svinfo.get(type).getStatus(),"   ")));
                        }
                    }
                    p.updateInventory();
                }
            }
        }
        if(event.getType() != UpdateType.SLOW2){
            return;
        }
        svinfo.clear();

        for(serverType type : serverType.values()){
            svinfo.put(type,new ServerINFO(0,0,type));
    }

        if(games.isEmpty()){
        for(serverType type : serverType.values()){
            games.put(type.getName(),Bukkit.createInventory(null,45,type.getName()));
            for(int a = 0; a < 45;a++){
                games.get(type.getName()).setItem(a,this.makeItem(Material.STAINED_GLASS_PANE,1,(short)13,"&f     ",null));
            }
        }}

        HashSet<ServerData> green = new HashSet();
        HashSet<ServerData> yellow = new HashSet();
        HashSet<ServerData> red = new HashSet();

        HashMap<serverType,Integer> gnex = new HashMap();
        for(ServerData sd : db.getServersEnabled()){
            svinfo.get(sd.getType()).addOnline(sd.getOnline());
            svinfo.get(sd.getType()).addMaxOnline(sd.getMaxOnline());
            gnex.put(sd.getType(),10);

if(sd.getStatus().startsWith("Empezando")){
    svinfo.get(sd.getType()).addSVFree();
    yellow.add(sd);}
else if(sd.getStatus().equals("Reclutando")){
    svinfo.get(sd.getType()).addSVFree();
    green.add(sd);}
        else {
    red.add(sd);
        }
        }



        for(ServerData sd : yellow){
            if(gnex.get(sd.getType())+1 % 9 == 0 && gnex.get(sd.getType()) < 36){
                gnex.put(sd.getType(),gnex.get(sd.getType())+2);
            }
            games.get(sd.getType().getName()).setItem(gnex.get(sd.getType()),this.makeItem(Material.STAINED_CLAY,1,(short)4,"&a"+sd.getStatus(),Arrays.asList("   ",Colorizer.Color("&aOnline &e&n"+sd.getOnline()+"/"+sd.getMaxOnline()),"   ",Colorizer.Color("&fServer "+sd.getServerName()))));
            gnex.put(sd.getType(),gnex.get(sd.getType())+1);
        }
        for(ServerData sd : green){
            if(gnex.get(sd.getType())+1 % 9 == 0 && gnex.get(sd.getType()) < 36){
                gnex.put(sd.getType(),gnex.get(sd.getType())+2);
            }
            games.get(sd.getType().getName()).setItem(gnex.get(sd.getType()),this.makeItem(Material.STAINED_CLAY,1,(short)5,"&a"+sd.getStatus(),Arrays.asList("   ",Colorizer.Color("&aOnline &e&n"+sd.getOnline()+"/"+sd.getMaxOnline()),"   ",Colorizer.Color("&fServer "+sd.getServerName()))));

            gnex.put(sd.getType(),gnex.get(sd.getType())+1);
        }
        for(ServerData sd : red){
            if(gnex.get(sd.getType())+1 % 9 == 0 && gnex.get(sd.getType()) < 36){
                gnex.put(sd.getType(),gnex.get(sd.getType())+2);
            }
            games.get(sd.getType().getName()).setItem(gnex.get(sd.getType()),this.makeItem(Material.STAINED_CLAY,1,(short)14,"&a"+sd.getStatus(),Arrays.asList("   ",Colorizer.Color("&aOnline &e&n"+sd.getOnline()+"/"+sd.getMaxOnline()),"   ",Colorizer.Color("&fServer "+sd.getServerName()))));
            gnex.put(sd.getType(),gnex.get(sd.getType())+1);
        }




    }


  @Override
  public void onDisable()
  {
    HandlerList.unregisterAll((Listener) this);
    getServer().getLogger().log(Level.INFO, "MineUltra Hub [v{0}] has been disabled!", getDescription().getVersion());
  }

    public HashMap<String,Scoreboard> _scoreboard = new HashMap();
    private HashMap<String,Objective> _sideObjective = new HashMap();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.VIP)){
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(Colorizer.Color("&a[+] &a"+db.getPlayerData(event.getPlayer()).getRank().getChatPrefix()+event.getPlayer().getName()));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1F,1F);
            }
        }
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        player.getInventory().clear();

        player.getInventory().setItem(0,makeItem(Material.COMPASS,1,(short)0,"&aNavegador de Servers &7(Click-Derecho)",null));
        ItemStack it = makeItem(Material.WRITTEN_BOOK,1,(short)0,"&aEstadisticas",null);
        BookMeta meta = (BookMeta)it.getItemMeta();
        meta.setTitle(Colorizer.Color("&aEstadisticas"));
        meta.setAuthor(player.getName());
        PlayerData pd = db.getPlayerData(player);

        meta.setPages(
                "Kills: "+pd.getKills()+"\n"
                +"Deaths: "+pd.getDeaths()+"\n"
                +"\n"
                +"1er Lugar: "+pd.getFirstPlaces()+"\n"
                +"2do Lugar: "+pd.getSecondPlaces()+"\n"
                +"3er Lugar: "+pd.getThirdPlaces()+"\n"
                +"\n"
                +"Victorias: "+pd.getWinned()+"\n"
                +"Derrotas: "+pd.getLoss()
        );
        it.setItemMeta(meta);
        player.getInventory().setItem(6,addGlow(it));
        player.updateInventory();

        SetPlayerScoreboardTeam(event.getPlayer());
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p == event.getPlayer()){
                continue;
            }
                GetScoreboard(p).registerNewTeam(event.getPlayer().getName()).setPrefix(db.getPlayerData(event.getPlayer()).getRank().getScoreboardPrefix());
                GetScoreboard(p).getTeam(event.getPlayer().getName()).addPlayer(event.getPlayer());
            GetScoreboard(event.getPlayer()).registerNewTeam(p.getName()).setPrefix(db.getPlayerData(p).getRank().getScoreboardPrefix());
            GetScoreboard(event.getPlayer()).getTeam(p.getName()).addPlayer(p);
            }
    }

    @EventHandler
    public void onCMDServer(PlayerCommandPreprocessEvent event){

        for(serverType type : serverType.values()){
            if(event.getMessage().equalsIgnoreCase("/"+type.getName())){
                if(!type.isSingle()){
                event.getPlayer().openInventory(games.get(type.getName()));
                event.getPlayer().updateInventory();
                event.setCancelled(true);}
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.VIP)){
            Bukkit.broadcastMessage(Colorizer.Color("&c[-] &a"+db.getPlayerData(event.getPlayer()).getRank().getChatPrefix()+event.getPlayer().getName()));
        }
        event.setQuitMessage(null);
        for(Player p : Bukkit.getOnlinePlayers()){
            if(event.getPlayer()==p){
                continue;
            }
            GetScoreboard(p).getTeam(event.getPlayer().getName()).unregister();
        }
        _scoreboard.remove(event.getPlayer().getName());
        _sideObjective.remove(event.getPlayer().getName());
    }

  public void SetPlayerScoreboardTeam(Player player)
  {
      for (Team team : GetScoreboard(player).getTeams()) {
          if(team.hasPlayer(player)){
              team.removePlayer(player);
          }
      }
      String next = db.getPlayerData(player).getRank().getScoreboardPrefix();
      if(next.length() > 16){
          next = next.substring(0, 16);
      }
      if(GetScoreboard(player).getTeam(next) == null){
         Team t = _scoreboard.get(player.getName()).registerNewTeam(next);
                  t.setPrefix(next+" ");
         t.setOption(Team.Option.NAME_TAG_VISIBILITY,Team.OptionStatus.ALWAYS);
      }


      int nx = 10;
      _sideObjective.get(player.getName()).getScore(Colorizer.Color("&a &b")).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore(Colorizer.Color("&6Victorias")).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore(ChatColor.WHITE+"➡ "+db.getPlayerData(player).getWinned()).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore(Colorizer.Color("&b &c")).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore(Colorizer.Color("&6Rango")).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore("➡ "+db.getPlayerData(player).getRank().getScoreboardPrefix()).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore(Colorizer.Color("&c &d")).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore(Colorizer.Color("&6Coins:")).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore("➡ "+((int)db.getPlayerData(player).getCoins())).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore(Colorizer.Color("&d &f")).setScore(nx);
      nx--;
      _sideObjective.get(player.getName()).getScore("www.mineultra.com").setScore(nx);

      GetScoreboard(player).getTeam(next).addPlayer(player);
      player.setScoreboard(_scoreboard.get(player.getName()));
  }
    public Scoreboard GetScoreboard(Player p)
  {
      if(!_scoreboard.containsKey(p.getName())){
          Scoreboard sco = Bukkit.getScoreboardManager().getNewScoreboard();
_sideObjective.put(p.getName(), sco.registerNewObjective("Obj" + r.nextInt(999999999), "dummy"));
_sideObjective.get(p.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
_sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+"  "+"MineUltra"+"  "));

          _scoreboard.put(p.getName(), sco);
      }
      
    return _scoreboard.get(p.getName());
  }

    String g = ChatColor.GOLD+""+ChatColor.BOLD;
    String y = ChatColor.YELLOW+""+ChatColor.BOLD;
    String f = ChatColor.WHITE+""+ChatColor.BOLD;



  int nex = 0;

    @EventHandler
    public void updateScoreboard(UpdateEvent event){
        if(event.getType() != UpdateType.FAST){
            return;
        }
        this.setNextPrefix();
    }


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
  } /*else if(nex == 11){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" M"+y+"i"+f+"neU"+y+"l"+g+"tra")+"  ");
  } else if(nex == 12){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" Mi"+y+"n"+f+"eUl"+y+"t"+g+"ra")+"  ");
  } else if(nex == 13){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" Min"+y+"e"+f+"Ult"+y+"r"+g+"a")+"  ");
  } else if(nex == 14){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Min"+y+"e"+g+"Ultr"+y+"a")+"  ");
  } else if(nex == 15){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Min"+y+"e"+g+"Ultr"+y+"a")+"  ");
  } else if(nex == 16){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Min"+y+"e"+g+"Ultr"+y+"a")+"  ");
  }*/
      
      }
  }

}
