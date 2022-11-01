/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.game.games.pack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import mineultra.core.common.util.C;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.F;
import mineultra.core.common.util.MSGUtil;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.game.TeamGame;
import mineultra.game.center.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class bbupdate extends TeamGame
{

  private static long startTime = 0;
  public bbupdate(centerManager manager)
  {
    super(manager, GameType.BuildBattle, 
      new Kit[] {}, 
      new String[] {
      F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("BuildBattle").toString()) + C.cWhite + " Se el mejor constructor, y gana el juego!", 
      F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("BuildBattle").toString()) + C.cWhite + " tu destreza y habilidad se pondran a prueba."
 });

    this._help =  this.Manager.getConfig().getStringList("tips").toArray(new String[0]);

    HungerSet = 20;

    this.BlockBreak = true;
    this.DamagePvP = false;
    this.Damage = false;
    this.DamagePvE = false;
    this.DamageTeamOther = false;
    this.DamageTeamSelf = false;
    this.BlockPlace = true;
    this.DeathDropItems = true;
    this.ItemDrop = false;
    this.PrepareFreeze = false;
    this.ItemPickup = false;
    this.DeathOut = true;
    this.WorldTimeSet = 10000;
  
    startTime = System.currentTimeMillis();
      Manager.GetExplosion().SetRegenerate(false);

   
  }
 

  
@Override
  public void ParseData()
  {

      Manager.GetExplosion().SetRegenerate(true);
 List<String> tts = this.Manager.getConfig().getStringList("BuildBattle.themes");
 if(tts.isEmpty()){
     System.out.println("No hay temas!, ve a la config");
            
 }
 if(tts.size() == 1){
     this.theme = tts.get(0);
 }else{
       theme = tts.get(r.nextInt(tts.size()-1));
 }
        this.timer = this.Manager.getConfig().getInt("BuildBattle.time");
                
  }
  
  public static String Color(String s)
  {
    if (s == null) {
      return Color("&4&lError");
    }
    return s.replaceAll("(&([a-fk-or0-9]))", "§$2");
  }
  
  
  public static ItemStack read(String str)
  {
    String[] split = str.split(",");
    ArrayList<String> lores = new ArrayList();
    
    ItemStack i = new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Short.parseShort(split[2]));
    for (int a = 1; a < split.length; a++)
    {
      if (split[a].startsWith("lore:"))
      {
        ItemMeta im = i.getItemMeta();
        
        String s1 = split[a].replace("lore:", "");

        String s2 = Color(s1);
        lores.add(s2);im.setLore(lores);i.setItemMeta(im);
      }
      for (Enchantment enc : Enchantment.values()) {
        if (split[a].toUpperCase().startsWith(enc.getName().toUpperCase()))
        {
          String s1 = split[a].replace(enc.getName().toUpperCase() + ":", "");
          i.addUnsafeEnchantment(enc, Integer.parseInt(s1));
        }
      }
      if (split[a].startsWith("name:"))
      {
        ItemMeta im = i.getItemMeta();
        String nam = split[a].replace("name:", "").replace("%name", getFriendlyItemName(i.getType()));
        im.setDisplayName(Color(nam));
        i.setItemMeta(im);
      }
    }
    return i;
  }
  
  public static String getFriendlyItemName(Material m)
  {
    String str = m.toString();
    str = str.replace('_', ' ');
    str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    
    return str;
  }
Random r = new Random();

boolean finished = false;

HashMap<GameTeam,Double> floor = new HashMap();
int timer = 500;


    public ItemStack makeItem(Material mat, int quantity, short sh, String name, String[] lore){
    ItemStack it = new ItemStack(mat,quantity,sh);
    ItemMeta imt = it.getItemMeta();
    
    if(name != null){
    imt.setDisplayName(Colorizer.Color(name));
    }
    
    if(lore != null){
    List<String> newlore = new ArrayList();
    for(String str : lore){
     newlore.add(Colorizer.Color(str));
    }
    imt.setLore(newlore);
    }
    
    it.setItemMeta(imt);
    
    return it;
        
    }

    String name1 = Colorizer.Color("&aPlot Weather");
    String name2 = Colorizer.Color("&aPlot Time");
    String name3 = Colorizer.Color("&aPlot Biome");
    String name4 = Colorizer.Color("&aPlot Floor");
    String name5 = Colorizer.Color("&aCustom Skulls");
    String name6 = Colorizer.Color("&aParticles");
    String name7 = Colorizer.Color("&aBanner Builder");
    String name8 = Colorizer.Color("&aSave Loadout");
    String name9 = Colorizer.Color("&aToggle Music");
    String name10 = Colorizer.Color("&aLanguage");
    ItemStack it1 = this.makeItem(Material.FLOWER_POT, 1, (short)1, name1, new String[]{
        "Sets the weather","for your plot!"
    });
    
    ItemStack it2 = this.makeItem(Material.COMPASS, 1, (short)1, name2, new String[]{
        "Sets the time for your plot!"
    });
    
    ItemStack it3 = this.makeItem(Material.PAPER, 1, (short)1, name3, new String[]{
        "Sets the biome for your plot!"
    });
    
    ItemStack it4 = this.makeItem(Material.STAINED_CLAY, 1, (short)1, name4, new String[]{
        "Put a block here, to use as your floor!"
    });
    
    ItemStack it5 = this.makeItem(Material.STAINED_CLAY, 1, (short)1, name5, new String[]{
        "Usefuls skulls to add detail!"
    });
    
    ItemStack it6 = this.makeItem(Material.STAINED_CLAY, 1, (short)1, name6, new String[]{
        "Select particles to put on your plot!"
    });
    
    ItemStack it7 = this.makeItem(Material.BANNER, 1, (short)1, name7, new String[]{
        "Select particles to put on your plot!"
    });
    
    ItemStack it8 = this.makeItem(Material.CHEST, 1, (short)1, name8, new String[]{
        "Select particles to put on your plot!"
    });
    
    // music box
    ItemStack it9 = this.makeItem(Material.BOAT, 1, (short)1, name9, new String[]{
        "Click this to toggle the music on, and off!"
    });
    
    ItemStack it10 = this.makeItem(Material.BOAT, 1, (short)1, name10, new String[]{
        "Click this to toggle the music on, and off!"
    });
    
public void openStarMenu(Player p){

    Inventory iv = Bukkit.createInventory(p,54,"Options");

    
iv.setContents(new ItemStack[]{
    null,null,null,null,null,null,null,null,null,
    null,null,it1 ,it2 ,null,it3 ,it4 ,null,null,
    null,null,null,it5 ,it6 ,it7 ,null,null,null,
    null,null,null,null,null,null,null,null,null,
    null,null,null,it8 ,it9 ,it10 ,null,null,null,
    null,null,null,null,null,null,null,null,null
    });

}



public void openWeather(Player pe){
/*
    size: 36
    11: stormy - blaze vara
    12: rainy - water bucket
    14: snowing - snowball
    15: sunny - girasol
    22: go back
*/    
}

HashMap<Player,Integer> times = new HashMap();

public ItemStack timestack(Player pe, int slot){
    ItemStack it = new ItemStack(Material.STAINED_CLAY);
    if(times.get(pe) == slot){
        it.setDurability((short)4);
    }else{
        it.setDurability((short)14);
    }
    ItemMeta im = it.getItemMeta();
    im.setDisplayName(Colorizer.Color("&a&l"+(this.timeOffset(pe, -1)/1000)*2.5+"hs"));
    it.setItemMeta(im);
    return it;
}

public void openTime(Player pe){
/*
    size: 36
    at 9-17 ---------
    22: goback
    */
    if(!times.containsKey(pe)){
        times.put(pe, 0);
    }
    ItemStack goback = this.makeItem(Material.ARROW, 1, (short)0, "&aGo back", null);
    
    Inventory iv = Bukkit.createInventory(null, 36, name2);
    iv.setContents(new ItemStack[]{
    null,null,null,null,null,null,null,null,null,
    timestack(pe,0),timestack(pe,1),timestack(pe,2),timestack(pe,3),timestack(pe,4),timestack(pe,5),timestack(pe,6),timestack(pe,7),timestack(pe,8),
    null,null,null,null,goback,null,null,null,null,
    null,null,null,null,null,null,null,null,null,
    });
}

public int timeOffset(Player pe,int forced){
    int time = -1;
    if(pe == null){
    if(!times.containsKey(pe)){
        times.put(pe, 0);
    }
    time = times.get(pe);}else{
    time = forced;
    }
           if(time==0){
                return 3000;
            }
            else if(time==1){
                return 5500;
            }
            else if(time==2){
                return 5800;
            }
            else if(time==3){
               return 6100;
            }
            else if(time==4){
                return 6400;
            }
            else if(time==5){
                return 6700;
            }else if(time==6){
                return 7000;
            }else if(time==7){
                return 7300;
            }else if(time==8){
                return 7600;
            }else{
                return 0;
            }
}

public void updateTime(Player pe, Player timefrom){

        if(timefrom != null){
            pe.setPlayerTime(this.timeOffset(timefrom,-1), true);
        }else{
pe.setPlayerTime(this.timeOffset(pe,-1), true);
        }
        
}

@EventHandler
public void updateStar(UpdateEvent event){
    if(event.getType() != UpdateType.SEC){
        return;
    }
    if(this.finished){
        return;
    }
    if(this.GetState() != Game.GameState.Live && this.GetState() != Game.GameState.Prepare){
        return;
    }
    for(Player p : this.GetPlayers(true)){
this.updateTime(p,null);
        
        
    }
    
    
    
}

public void openBiome(Player pe){
    
}

public void openFloor(Player pe){
    
}

public void openSkulls(Player pe){
    
}

public void openParticles(Player pe){
    
}

public void openBannerBuilder(Player pe){
    
}

public void openSaveloadout(Player pe){
    
}

public void openToggleMusic(Player pe){
    
}

public void openLanguage(Player pe){
    
}
@EventHandler
public void onOptionClick(InventoryClickEvent event){
    if(event.getCurrentItem() == null){
        return;
    }
    if(!event.getCurrentItem().hasItemMeta()){
        return;
    }
    if(!event.getCurrentItem().getItemMeta().hasDisplayName()){
        return;
    }
    String name = event.getCurrentItem().getItemMeta().getDisplayName();
    Player pe = (Player)event.getWhoClicked();
    event.setCancelled(true);
    if(name.equalsIgnoreCase(name1)){
        this.openWeather(pe);
    }
    else if(name.equalsIgnoreCase(name2)){
        this.openTime(pe);
    }
    else if(name.equalsIgnoreCase(name3)){
        this.openBiome(pe);
    }
    else if(name.equalsIgnoreCase(name4)){
        this.openFloor(pe);
    }
    else if(name.equalsIgnoreCase(name5)){
        this.openSkulls(pe);
    }
    else if(name.equalsIgnoreCase(name6)){
        this.openParticles(pe);
    }
    else if(name.equalsIgnoreCase(name7)){
        this.openBannerBuilder(pe);
    }
    else if(name.equalsIgnoreCase(name8)){
        this.openSaveloadout(pe);
    }
    else if(name.equalsIgnoreCase(name9)){
        this.openToggleMusic(pe);
    }
    else if(name.equalsIgnoreCase(name10)){
        this.openLanguage(pe);
    }else{
        event.setCancelled(false);
    }
    
}

@EventHandler
public void onChan(GameStateChangeEvent event){    

    if(event.GetState() != Game.GameState.Live){
        return;
    }

    for(Player p : this.GetPlayers(true)){
      p.setGameMode(GameMode.CREATIVE);
      p.getInventory().setItem(0, new ItemStack(Material.DIRT));
      p.getInventory().setItem(1, new ItemStack(Material.WOOD));
      p.getInventory().setItem(2, new ItemStack(Material.STONE));
      p.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Options "+C.cDGray+"(Right Click)", new String[] { "", ChatColor.RESET + "Click-Derecho", ChatColor.RESET + "para abrir el menu." }));

}

   this.psize = this.GetPlayers(true).size();
    for(GameTeam te : this.GetTeamList()){
       double x = te.GetSpawn().getX();
       double y = te.GetSpawn().getY();
       double z = te.GetSpawn().getZ();
       World we = te.GetSpawn().getWorld();
       int found = 0;
        for(int a = 0;a < 50;a++){
if((new Location(we,x,y-a,z)).getBlock().getType().equals(Material.STAINED_CLAY)){
    if(found >= 1){
        if(floor.containsKey(te)){
            continue;
        }else{
        floor.put(te, y-a);    
        this.putSize(te, new Location(we,x,y-a,z));
        }
        
   continue;
    }
    found++;
}
     }
    }
   
 
}

HashMap<Player,Material> flormap = new HashMap();

@EventHandler
public void onInter(PlayerInteractEvent event){
    if(!this.IsLive()){
        return;
    }
    if(this.finished == true){
        return;
    }
    if(event.getPlayer().getItemInHand() == null){
        return;
    }
    if(event.getPlayer().getItemInHand().getType() != Material.NETHER_STAR){
        return;
    }
    if(!event.getPlayer().getItemInHand().hasItemMeta()){
        return;
    }
if(!flormap.containsKey(event.getPlayer())){
    flormap.put(event.getPlayer(), Material.STAINED_CLAY);
}
    Inventory inv = Bukkit.createInventory(null, 54, Colorizer.Color("&aOpciones de Plot"));
ItemStack flor = ItemStackFactory.Instance.CreateStack(flormap.get(event.getPlayer()), (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Piso de Plot", new String[] { "", ChatColor.RESET + "Pon un bloque aqui", ChatColor.RESET + "para usarlo de piso!" });

inv.setContents(new ItemStack[]{
    null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,flor,null,null,
    null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,
});
    event.getPlayer().openInventory(inv);
    event.getPlayer().updateInventory();
}

@EventHandler
public void onINV(InventoryClickEvent event){

    if(event.getCurrentItem() == null){
        return;
    }
    if(!event.getCurrentItem().hasItemMeta()){
        return;
    }
    if(event.getInventory().getName() == null){
        return;
    }
    if(!event.getInventory().getName().toLowerCase().contains("plot")){
        return;
    }
    
    event.setCancelled(true);
    if(event.getCursor().getType() == Material.AIR || event.getCursor() == null){
        return;
    }
    Player p = (Player)event.getWhoClicked();
    this.flormap.put(p, event.getCursor().getType());
this.ChangeFlor(this.GetTeam(p), event.getCursor().getType());
    p.closeInventory();

}

public void ChangeFlor(GameTeam te, Material mat){
    if(te == null){
        return;
    }
Location loc = new Location(te.GetSpawn().getWorld(),te.GetSpawn().getBlockX(),floor.get(te)+1,te.GetSpawn().getBlockZ());
Location dist1 = new Location(te.GetSpawn().getWorld(),this.CornerMinX.get(te),floor.get(te)+1,te.GetSpawn().getBlockZ());
Location dist2 = new Location(te.GetSpawn().getWorld(),this.CornerMaxX.get(te),floor.get(te)+1,te.GetSpawn().getBlockZ());
double distance = dist1.distance(dist2);

for(int z = 0;z < distance;z++)
{
    for(int x = 0;x < distance;x++){
    Location loc1 = loc.clone().add(x, 0, z);
    Location loc1B = loc.clone().add(x, -1, z);
if(!this.isOut(te, loc1)){
    loc1.getBlock().setType(mat);
    loc1B.getBlock().setType(mat);
}
Location loc2 = loc.clone().subtract(x, 0, z);
Location loc2B = loc.clone().subtract(x, -1, z);
if(!this.isOut(te, loc2)){
 loc2.getBlock().setType(mat);
 loc2B.getBlock().setType(mat);
}

Location loc3 = loc.clone().add(-x, 0, z);
Location loc4 = loc.clone().add(x, 0, -z);

Location loc3B = loc.clone().add(-x, -1, z);
Location loc4B = loc.clone().add(x, -1, -z);
if(!this.isOut(te, loc3)){
 loc3.getBlock().setType(mat);
 loc3B.getBlock().setType(mat);
}
if(!this.isOut(te, loc4)){
 loc4.getBlock().setType(mat);
 loc4B.getBlock().setType(mat);
}
    
    }

}
}

@EventHandler
public void onClick(InventoryClickEvent event){
    if(!this.IsLive()){
        return;
    }
    if(this.finished == true){ 
       event.setCancelled(true);
    }
    
}

@EventHandler(priority=EventPriority.HIGHEST)
public void onInteract(PlayerInteractEvent event){
        if(!this.IsLive()){
        return;
    }
        if(this.GetTeam(event.getPlayer()) == null){
            event.setCancelled(true);
            return;
        }
   if(this.finished == true){
       return;
   }else{
        event.setCancelled(false);
    }
}

@EventHandler(priority=EventPriority.HIGHEST)
public void onPlace(BlockPlaceEvent event){
    if(!this.IsLive()){
        return;
    }
            if(this.GetTeam(event.getPlayer()) == null){
            event.setCancelled(true);
            return;
        }
    if(this.finished == true){
        event.setCancelled(true);
        return;
    }else{
        event.setCancelled(false);
    }
    
    GameTeam te = this.GetTeam(event.getPlayer());

   
    if(event.getBlock().getLocation().getY() <= this.floor.get(te)){
       event.setCancelled(true);
    event.getPlayer().sendMessage(Colorizer.Color("&cNo puedes construir fuera de tu plot!"));
   return;
   } 
   Location compare1 = new Location(event.getBlock().getWorld(),event.getBlock().getX(),event.getBlock().getY(),event.getBlock().getZ());
   
   Location compare2 = new Location(te.GetSpawn().getWorld(),te.GetSpawn().getX(),event.getBlock().getY(),te.GetSpawn().getZ());
  
   if(this.isOut(te, compare1.getBlock().getLocation())){
    
    event.setCancelled(true);
    event.getPlayer().sendMessage(Colorizer.Color("&cNo puedes construir fuera de tu plot!"));        
    
}
}

@EventHandler(priority=EventPriority.HIGHEST)
public void onBreak(BlockBreakEvent event){
    if(!this.IsLive()){
        return;
    }
            if(this.GetTeam(event.getPlayer()) == null){
            event.setCancelled(true);
            return;
        }
    if(this.finished == true){
        event.setCancelled(true);
        return;
    }else{
        event.setCancelled(false);
    }
    
    GameTeam te = this.GetTeam(event.getPlayer());

   
    if(event.getBlock().getLocation().getY() <= this.floor.get(te)){
       event.setCancelled(true);
    event.getPlayer().sendMessage(Colorizer.Color("&cNo puedes romper fuera de tu Plot!"));
   return;
   } 
   Location compare1 = new Location(event.getBlock().getWorld(),event.getBlock().getX(),event.getBlock().getY(),event.getBlock().getZ());
   
   Location compare2 = new Location(te.GetSpawn().getWorld(),te.GetSpawn().getX(),event.getBlock().getY(),te.GetSpawn().getZ());
   
   if(this.isOut(te, compare1.getBlock().getLocation())){

    event.setCancelled(true);
    event.getPlayer().sendMessage(Colorizer.Color("&cNo puedes romper fuera de tu Plot!"));        
    
}
}

HashMap<GameTeam,Double> CornerMinX = new HashMap();
HashMap<GameTeam,Double> CornerMaxX = new HashMap();

HashMap<GameTeam,Double> CornerMinZ = new HashMap();
HashMap<GameTeam,Double> CornerMaxZ = new HashMap();

public boolean isOut(GameTeam te, Location loca ){
    if(te == null){
        return true;
    }
    Double MinX = this.CornerMinX.get(te);
    Double MaxX = this.CornerMaxX.get(te);
    Double MinZ = this.CornerMinZ.get(te);
    Double MaxZ = this.CornerMaxZ.get(te);
    
    if(loca.getBlockX() <= MinX){
        return true;
    }else if(loca.getBlockX() >= MaxX){
        return true;
    }else if(loca.getBlockZ() <= MinZ){
        return true;
    }else if(loca.getBlockZ() >= MaxZ){
        return true;
    }else{
        return false;
    }
    
}
HashMap<Player,Integer> vote = new HashMap();
@EventHandler
public void Voting(PlayerInteractEvent event){
    if(!this.IsLive()){
        return;
    }
    if(!this.finished){
        return;
    }
    
    if(event.getPlayer().getItemInHand() == null){
        return;
    }
    try{
    if(vota.get(index).equals(event.getPlayer())){
        return;
    }    
    }catch(Exception e){
        
    }
    
    short dura = event.getPlayer().getItemInHand().getDurability();
    Player pe = event.getPlayer();
    if(dura == 14){
                if(vote.containsKey(pe)){
            if(vote.get(pe) == 2){
                return;
            }
        }
        vote.put(pe, 1);
      pe.sendMessage(Colorizer.Color("&e&lVoto confirmado: &4&lSUPER POOP"));
      pe.getWorld().playSound(pe.getLocation(), Sound.CAT_MEOW, 0.5F, 0.5F);
    }else if(dura == 6 ){
                if(vote.containsKey(pe)){
            if(vote.get(pe) == 2){
                return;
            }
        }
        pe.sendMessage(Colorizer.Color("&e&lVoto confirmado: &c&lPOOP"));
        pe.getWorld().playSound(pe.getLocation(), Sound.CAT_MEOW, 1F, 1F);
        vote.put(pe, 2);
        
    }else if(dura == 3){
                        if(vote.containsKey(pe)){
            if(vote.get(pe) == 3){
                return;
            }
        }
        pe.sendMessage(Colorizer.Color("&e&lVoto confirmado: &a&lOK"));
        pe.getWorld().playSound(pe.getLocation(), Sound.CAT_MEOW, 1.5F, 1.5F);
        vote.put(pe, 3);
    }else if(dura == 13){
                        if(vote.containsKey(pe)){
            if(vote.get(pe) == 4){
                return;
            }
        }
        pe.sendMessage(Colorizer.Color("&e&lVoto confirmado: &a&lBUENO"));
        pe.getWorld().playSound(pe.getLocation(), Sound.CAT_MEOW, 2F, 2F);
        vote.put(pe, 4);
    }else if(dura == 11){
                        if(vote.containsKey(pe)){
            if(vote.get(pe) == 5){
                return;
            }
        }
        pe.sendMessage(Colorizer.Color("&e&lVoto confirmado: &b&lEPICO"));
        pe.getWorld().playSound(pe.getLocation(), Sound.CAT_MEOW, 2.5F, 2.5F);
        vote.put(pe, 5);
    }else if(dura == 4){
                        if(vote.containsKey(pe)){
            if(vote.get(pe) == 6){
                return;
            }
        }
        pe.sendMessage(Colorizer.Color("&e&lVoto confirmado: &6&lLEGENDARIO"));
        pe.getWorld().playSound(pe.getLocation(), Sound.CAT_MEOW, 3F, 3F);
        vote.put(pe, 6);
    }
    
}
List<Player> vota = null;
int nextvote = 17;
int index = 0;

HashMap<Player,Integer> total = new HashMap();


@EventHandler
public void votation(UpdateEvent event){
    if(event.getType() != UpdateType.SEC){
        return;
    }
    if(!this.IsLive()){
        return;
    }
    if(!this.finished){
        return;
    }
    if(vota == null){
        vota = new ArrayList();
        for(Player p : this.GetPlayers(true)){
            vota.add(p);
        }
    }
    if(index >= vota.size()){
        return;
    }
    Player p = vota.get(index);
    if(nextvote > 16){
        nextvote--;
        for(Player pls : this.GetPlayers(false)){
            UtilDisplay.sendTitle(pls, " ", Colorizer.Color("&7Plot Dueño: "+p.getDisplayName()));
            pls.sendMessage(Colorizer.Color("&ePlot Dueño: &7"+p.getDisplayName()));
            pls.teleport(this.GetTeam(p).GetSpawn());
        }
    }

    
    if(nextvote <= 0){
    if(vote.isEmpty()){
       total.put(p, 0);
    }else{
        for(Player pa : vote.keySet()){

            if(!total.containsKey(p)){
                total.put(p, 0);
            }
            total.put(p, total.get(p)+vote.get(pa));
        }
    }
    vote.clear();
    index+=1;
        nextvote = 16;
        Player place1 = null;
            int last =  0;
    for(Player pla : total.keySet()){
  if(total.get(pla) > last){
      place1 = pla;
      last = total.get(pla);
  }
  }
        for(Player pls : this.GetPlayers(false)){
            if(index >= vota.size()){
            
                pls.teleport(this.GetTeam(place1).GetSpawn());
                continue;
            }
            UtilDisplay.sendTitle(pls, " ", Colorizer.Color("&7Plot Dueño: "+vota.get(index).getDisplayName()));
            pls.sendMessage(Colorizer.Color("&ePlot Dueño: &7"+vota.get(index).getDisplayName()));
            pls.teleport(this.GetTeam(vota.get(index)).GetSpawn());
        }
        
    }
    if(!p.isOnline()){
        nextvote = 0;
        return;
    }
    for(Player pan : Bukkit.getOnlinePlayers()){
        UtilDisplay.sendActionBar(pan, Colorizer.Color("&c"+nextvote+" &esegundos restantes para votar por esta plot!"));
    pan.getWorld().playSound(pan.getLocation(), Sound.DOOR_OPEN, 4F, 4F);
    }
    
    nextvote--;
    

     ItemStack s1 = new ItemStack(Material.STAINED_CLAY,1,(short)14);
     ItemMeta im1 = s1.getItemMeta();
     im1.setDisplayName(Colorizer.Color("&4&lSUPER POOP &7(Click-Derecho)"));
     s1.setItemMeta(im1);
     
     ItemStack s2 = new ItemStack(Material.STAINED_CLAY,1,(short)6);
     ItemMeta im2 = s2.getItemMeta();
     im2.setDisplayName(Colorizer.Color("&c&lPOOP &7(Click-Derecho)"));
     s2.setItemMeta(im2);
     
     ItemStack s3 = new ItemStack(Material.STAINED_CLAY,1,(short)3);
     ItemMeta im3 = s3.getItemMeta();
     im3.setDisplayName(Colorizer.Color("&a&lOK &7(Click-Derecho)"));
     s3.setItemMeta(im3);
     
     ItemStack s4 = new ItemStack(Material.STAINED_CLAY,1,(short)13);
     ItemMeta im4 = s4.getItemMeta();
     im4.setDisplayName(Colorizer.Color("&a&lBUENO &7(Click-Derecho)"));
     s4.setItemMeta(im4);
     
     ItemStack s5 = new ItemStack(Material.STAINED_CLAY,1,(short)11);
     ItemMeta im5 = s5.getItemMeta();
     im5.setDisplayName(Colorizer.Color("&b&lEPICO &7(Click-Derecho)"));
     s5.setItemMeta(im5);
     
     ItemStack s6 = new ItemStack(Material.STAINED_CLAY,1,(short)4);
     ItemMeta im6 = s6.getItemMeta();
     im6.setDisplayName(Colorizer.Color("&6&lLEGENDARIO &7(Click-Derecho)"));
     s6.setItemMeta(im6);
     
    for(Player pa : this.GetPlayers(true)){
     pa.getInventory().clear();
     pa.getInventory().setItem(0, s1);
     pa.getInventory().setItem(1, s2);
     pa.getInventory().setItem(2, s3);
     pa.getInventory().setItem(3, s4);
     pa.getInventory().setItem(4, s5);
     pa.getInventory().setItem(5, s6);
     pa.updateInventory();
     
       
    }
    
}





@EventHandler
public void cancel(UpdateEvent event){
 if(event.getType() != UpdateType.SEC){
     return;
 }
 if(!this.IsLive()){
     return;
 }
 if(finished == true){
     return;
 }
 for(Player p : this.GetPlayers(true)){

     if(this.isOut(this.GetTeam(p), p.getLocation().getBlock().getLocation())){
         this.GetTeam(p).SpawnTeleport(p);
         p.sendMessage(Colorizer.Color("&cNo salgas de la zona, hasta que termines de construir!"));
  }
 }
}

public void putSize(GameTeam te, Location loc){
    double locsize = 0;
    for(int a = 0;a < 50;a++){
        
     Location loca = new Location(loc.getWorld(),loc.getX(),loc.getY(),loc.getZ()+a);
     Location altern = new Location(loc.getWorld(),loc.getX(),loc.getY()+2,loc.getZ());
         if(loca.getBlock().getType().equals(Material.STAINED_CLAY) && altern.getBlock().getType().equals(Material.AIR)){
             locsize +=1;
         }else{
          this.CornerMinX.put(te, loc.getBlockX()-locsize);
          this.CornerMaxX.put(te, loc.getBlockX()+locsize);
          
          this.CornerMinZ.put(te, loc.getBlockZ()-locsize);
          this.CornerMaxZ.put(te, loc.getBlockZ()+locsize);
             return;
         }
     
    }
}

HashMap<Firework,Long> fires = new HashMap();

public void ExplodeFirework(Location loc ){


        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fm = firework.getFireworkMeta();
        fm.setPower(2);
        
        fm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED).withFade(Color.GREEN).build());
        fm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.RED).withFade(Color.GREEN).build());
        fm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.RED).withFade(Color.GREEN).build());
        
        firework.setFireworkMeta(fm);

        fires.put(firework, System.currentTimeMillis()+r.nextInt(1000));
}

@EventHandler
public void Detonate(UpdateEvent event){
    if(event.getType() != UpdateType.FAST){
        return;
    }
    if(fires.isEmpty()){
       return; 
    }
   
    for(Iterator<Firework> it = fires.keySet().iterator();it.hasNext();){
        if(!it.hasNext()){
            return;
        }
        
        Firework fe = it.next();
        if(fe == null){
            fires.remove(fe);
        }
        if(fires.get(fe) < System.currentTimeMillis()){
            if(fe != null){
            fe.detonate();    
            }
            
        }
    }
}
int delayend = 10;
  @Override
  public void EndCheck()
  {
    if (!IsLive()){
        return;
    }
if(vota == null){
    return;
}
if(delayend != 10){
    return;
}

if(index >= vota.size()){
  Player place1 = null;
  Player place2 = null;
  Player place3 = null;
  int last = 0;
  
  for(Player pla : total.keySet()){
  if(total.get(pla) > last){
      place1 = pla;
      last = total.get(pla);
  }
  }
  last = 0;
  
  for(Player pla : total.keySet()){
      if(place1 != null){
          if(pla ==  place1){
              continue;
          }
      }
  if(total.get(pla) > last){
      place2 = pla;
      last = total.get(pla);
  }
  }
  
  last = 0;
  
  for(Player pla : total.keySet()){
      if(place1 != null){
          if(pla ==  place1){
              continue;
          }
      }
      if(place2 != null){
          if(pla ==  place2){
              continue;
          }
      }
      
  if(total.get(pla) > last){
      place3 = pla;
      last = total.get(pla);
  }
  }
  
  for(int a = 0;a < 16;a++){
   try{
   this.ExplodeFirework(this.GetTeam(place1).GetSpawn());       
   }catch(Exception e){
this.SetState(Game.GameState.End);
  }

  }
  
  for(Player paa : Bukkit.getOnlinePlayers()){
      paa.sendMessage(Colorizer.Color(MSGUtil.getLineSpacer()));
      paa.sendMessage("                                 ");
      paa.sendMessage("               Ganadores");
      if(place1!= null){
      paa.sendMessage(Colorizer.Color("&e                1st &7- &6"+total.get(place1)));
      paa.sendMessage(Colorizer.Color("&7                "+place1.getDisplayName()));
      }
      
      if(place2!= null){
      paa.sendMessage(Colorizer.Color("&6                2nd &7- &6"+total.get(place2)));
      paa.sendMessage(Colorizer.Color("&7                "+place2.getDisplayName())); 
      }
      
      if(place3!= null){
      paa.sendMessage(Colorizer.Color("&c                3rd &7- &6"+total.get(place3)));
      paa.sendMessage(Colorizer.Color("&7                "+place3.getDisplayName())); 
      }
      
        paa.sendMessage("                                 ");
        paa.sendMessage(Colorizer.Color(MSGUtil.getLineSpacer()));
  }
  
  
    
this.delayend = 5;   
}

 }

@EventHandler
public void delayEnd(UpdateEvent event){
    if(event.getType() != UpdateType.SEC){
        return;
    }
    if(!this.IsLive()){
        return;
    }
    if(delayend <= 5){
        delayend--;
    }
    if(delayend <= 0){
        SetState(Game.GameState.End);
        for(Player p : Bukkit.getOnlinePlayers()){
            this.Manager.GetPortal().SendPlayerToServer(p, this.Manager.getConfig().getString("BuildBattle.lobby"));
        }
        delayend = 10;
    }
}
 String border1 = null;
 String border2 = null;
 String border3 = null;
 String map = null;
 String theme = "Cake";
 int psize = 0;
 String lastnamereset = "hola";
 @EventHandler
  @Override
  public void ScoreboardUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    if(!this.IsLive()){
        return;
    }
if(timer <= 0){
    this.finished = true;
}
    timer--;

  
   int line = 9;
this.GetObjectiveSide().getScore(Colorizer.Color("&a ")).setScore(line--);

 if(this.finished == true){

     this.GetObjectiveSide().getScoreboard().resetScores(Colorizer.Color("Time: &a00:0"));
 this.GetObjectiveSide().getScoreboard().resetScores(Colorizer.Color("&b  "));
 }else{
this.GetObjectiveSide().getScore(Colorizer.Color("Tiempo: &a"+this.getTime())).setScore(line--);
this.GetObjectiveSide().getScore(Colorizer.Color("&b  ")).setScore(line--);
 
 }
 this.GetObjectiveSide().getScore(Colorizer.Color("Tema: &a"+theme)).setScore(line--);
 
 this.GetObjectiveSide().getScore(Colorizer.Color("&c  ")).setScore(line--);
 if(this.finished == true){

this.GetObjectiveSide().getScoreboard().resetScores(Colorizer.Color("Players: &a"+psize));
this.GetObjectiveSide().getScore(Colorizer.Color("Dueño:")).setScore(line--);
 Player place1 = null;

if(index >= vota.size()){
    int last =  0;
    for(Player pla : total.keySet()){
  if(total.get(pla) > last){
      place1 = pla;
      last = total.get(pla);
  }
  }
}
String plai = null;
if(place1 == null){
plai = Colorizer.Color("&a"+this.vota.get(index).getName());    
}else{
    plai = Colorizer.Color("&a"+place1.getName());
}



if(plai.length() > 15){
    plai = plai.substring(0, 15);
}
if(!lastnamereset.equals(plai)){
    this.GetObjectiveSide().getScoreboard().resetScores(lastnamereset);
}
lastnamereset = plai;

this.GetObjectiveSide().getScore(plai).setScore(line--);
 }else{
 this.GetObjectiveSide().getScore(Colorizer.Color("Players: &a"+psize)).setScore(line--);     
 }

 
 this.GetObjectiveSide().getScore(Colorizer.Color("&e  ")).setScore(line--);
 this.GetObjectiveSide().getScore(Colorizer.Color(this.Manager.getConfig().getString("GameConfig.url"))).setScore(line--);
  for(GameTeam team : this.GetTeamList()){
    for(Player player : team.GetPlayers(false)){
    this.SetPlayerScoreboardTeam(player, team.GetName());
  } 
  }
  }
  
 
  
String lasttime = "asd";

public String getTime(){
this.GetObjectiveSide().getScoreboard()
        .resetScores(lasttime);


int time = 0;
time = timer;
int min = 0;
for(int a = 0;a < 10;a++){
if(time - 60 > 0){
    time -= 60;
    min++;
}    
}
if(time < 10){
    time = 0+time;
}
String ls = Colorizer.Color("Tiempo: &a"+"0"+min+":"+time);
lasttime = ls;
return "0"+min+":"+time;
}

 
}

  
  
  
  

  

 


