package mineultra.game.center.managers;

import java.sql.SQLException;

import me.gonzalociocca.mineultra.DBManager;
import me.gonzalociocca.mineultra.Rank;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import mineultra.game.center.centerManager;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.Game.GameState;
import mineultra.game.center.kit.Kit;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class MiscManager implements Listener
{
    private List<String> _dontGiveClockList = null;
    private centerManager Manager = null;
    private DBManager db = null;
    public MiscManager(final centerManager manager) {
        super();
        db = manager.getDB();
        this._dontGiveClockList = new ArrayList<>();
        this.Manager = manager;
        this.Manager.GetPluginManager().registerEvents(this, manager.GetPlugin());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void InteractActive(final PlayerInteractEvent event) {
        event.setCancelled(false);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void InteractClickCancel(final PlayerInteractEvent event) {
        if (Manager.GetGame() == null) {
            return;
        }
        final Player player = event.getPlayer();
        if (!Manager.GetGame().IsAlive(player)) {
            event.setCancelled(true);
        }
        else if (event.getPlayer().getItemInHand().getType() == Material.INK_SACK && event.getPlayer().getItemInHand().getData().getData() == 15 && event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void InventoryClickCancel(final InventoryClickEvent event) {
        if (Manager.GetGame() == null) {
            return;
        }
        final Player player = UtilPlayer.searchExact(event.getWhoClicked().getName());
        if (Manager.GetGame().IsLive() && !Manager.GetGame().IsAlive(player)) {
            event.setCancelled(true);
            player.closeInventory();
        }
    }
    
    @EventHandler
    public void addClockPrevent(final InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            this._dontGiveClockList.add(event.getPlayer().getName());
        }
    }
    
    @EventHandler
    public void removeClockPrevent(final InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            this._dontGiveClockList.remove(event.getPlayer().getName());
        }
    }
    
    @EventHandler
    public void HubClockUpdate(final UpdateEvent event) {
        if (event.getType() != UpdateType.SEC2) {
            return;
        }
        if (Manager.GetGame() == null) {
            return;
        }

        for (Player player : UtilServer.getPlayers()) {
            if (!Manager.GetGame().IsAlive(player) && !this._dontGiveClockList.contains(player.getName())) {
                Manager.HubClock(player);
            }
        }
    }
    
    HashSet<String> votechest = new HashSet();
    HashSet<String> votetime = new HashSet();
    @EventHandler
     public void reset(GameStateChangeEvent event){
         votechest.clear();
         votetime.clear();;
     }

    public void vote(int mode,int type, int point){
        if(mode == 0){
         if(type == 1){
           Manager.GetGame().chest1 +=point;
        }else if(type ==  2){
           Manager.GetGame().chest2 +=point;
        }else if(type == 3){
           Manager.GetGame().chest3 +=point;
        }
        }else if(mode == 1){
         if(type == 1){
           Manager.GetGame().time1 +=point;
        }else if(type ==  2){
           Manager.GetGame().time2 +=point;
        }else if(type == 3){
           Manager.GetGame().time3 +=point;
        }
        }

    }
    @EventHandler
    public void voteChestListener(InventoryClickEvent event){
        if(event.getCurrentItem() == null){
            return;
        }
        if(event.getCurrentItem().getType() != Material.INK_SACK){
            return;
        }
        event.setCancelled(true);
        if(this.Manager.GetGame() == null){
            return;
        }
        if(this.Manager.GetGame().GetType() != GameType.SWTeams && this.Manager.GetGame().GetType() != GameType.SkyWars){
            return;
        }
        if(event.getInventory().getName() == null){
            return;
        }
        if(!event.getInventory().getName().toLowerCase().contains("chest")){
            return;
        }
        Player p =  (Player)event.getWhoClicked();
        if(votechest.contains(p.getName())){
            p.sendMessage(Colorizer.Color("&cYa has votado!"));
             p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5F, 1.5F);
            p.closeInventory();
            return;
        }
        
        if(event.getCurrentItem().getDurability() == 8){

            if((db.getPlayerData(p).getRank().isAtLeast(Rank.MVP))){
vote(0,1,3);
                p.sendMessage(Colorizer.Color("&aHas votado +3 por cofres Normales!"));
       
            }else if((db.getPlayerData(p).getRank().isAtLeast(Rank.VIP))){
vote(0,1,2);
                p.sendMessage(Colorizer.Color("&aHas votado +2 por cofres Normales!"));
       
            }else{
vote(0,1,1);
                p.sendMessage(Colorizer.Color("&aHas votado +1 por cofres Normales!"));
            }
            
       votechest.add(p.getName());
       p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        
        }else if(event.getCurrentItem().getDurability() == 10){

       if(db.getPlayerData(p).getRank().isAtLeast(Rank.MVP)){
vote(0,2,3);
           p.sendMessage(Colorizer.Color("&aHas votado +3 por cofres Medianos!"));
       
            }else if(db.getPlayerData(p).getRank().isAtLeast(Rank.VIP)){
vote(0,2,2);
           p.sendMessage(Colorizer.Color("&aHas votado +2 por cofres Medianos!"));
       
            }else{
vote(0,2,1);      
       p.sendMessage(Colorizer.Color("&aHas votado +1 por cofres Medianos!"));
            }
            
       votechest.add(p.getName());
       p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        
        
        
        }else if(event.getCurrentItem().getDurability() == 5){

           if(db.getPlayerData(p).getRank().isAtLeast(Rank.MVP)){
vote(0,3,3);
               p.sendMessage(Colorizer.Color("&aHas votado +3 por cofres OPE!"));
       
            }else if(db.getPlayerData(p).getRank().isAtLeast(Rank.VIP)){
vote(0,3,2);
               p.sendMessage(Colorizer.Color("&aHas votado +2 por cofres OPE!"));
       
            }else{
vote(0,3,1);
               p.sendMessage(Colorizer.Color("&aHas votado +1 por cofres OPE!"));
            }
            
       votechest.add(p.getName());
       p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }
        p.closeInventory();
    }
    
    
 @EventHandler
    public void voteTimeListener(InventoryClickEvent event){
        if(event.getCurrentItem() == null){
            return;
        }
        if(event.getCurrentItem().getType() != Material.INK_SACK){
            return;
        }
        event.setCancelled(true);
        if(this.Manager.GetGame() == null){
            return;
        }
        if(this.Manager.GetGame().GetType() != GameType.SWTeams && this.Manager.GetGame().GetType() != GameType.SkyWars){
            return;
        }
        if(event.getInventory().getName() == null){
            return;
        }
        if(!event.getInventory().getName().toLowerCase().contains("time")){
            return;
        }
        Player p =  (Player)event.getWhoClicked();
        if(votetime.contains(p.getName())){
            p.sendMessage(Colorizer.Color("&cYa has votado!"));
             p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.5F, 1.5F);
            return;
        }

        
        if(event.getCurrentItem().getDurability() == 8){

        

            if(db.getPlayerData(p).getRank().isAtLeast(Rank.MVP)){
vote(1,1,3);    
       p.sendMessage(Colorizer.Color("&aHas votado +3 por tiempo de Dia!"));
       
            }else if(db.getPlayerData(p).getRank().isAtLeast(Rank.VIP)){
vote(1,1,2);
                p.sendMessage(Colorizer.Color("&aHas votado +2 por tiempo de Dia!"));
            }else{
vote(1,1,1);
                p.sendMessage(Colorizer.Color("&aHas votado +1 por tiempo de Dia!"));
            }
            
       votetime.add(p.getName());
       p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        
        
        
       
        
        
        
        }else if(event.getCurrentItem().getDurability() == 10){
         if(db.getPlayerData(p).getRank().isAtLeast(Rank.MVP)){
vote(1,2,3);   
       p.sendMessage(Colorizer.Color("&aHas votado +3 por medio Dia!"));
       
            }else if(db.getPlayerData(p).getRank().isAtLeast(Rank.VIP)){
vote(1,2,2);
             p.sendMessage(Colorizer.Color("&aHas votado +2 por medio Dia!"));
            }else{
vote(1,2,1);
             p.sendMessage(Colorizer.Color("&aHas votado +1 por medio Dia!"));
            }
            
       votetime.add(p.getName());
       p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        
        
        
        }else if(event.getCurrentItem().getDurability() == 5){
            if(db.getPlayerData(p).getRank().isAtLeast(Rank.MVP)){
vote(1,3,3);      
       p.sendMessage(Colorizer.Color("&aHas votado +3 por tiempo de Noche!"));
       
            }else if(db.getPlayerData(p).getRank().isAtLeast(Rank.VIP)){
vote(1,3,2);
                p.sendMessage(Colorizer.Color("&aHas votado +2 por tiempo de Noche!"));
       
            }else{
vote(1,3,1);
                p.sendMessage(Colorizer.Color("&aHas votado +1 por tiempo de Noche!"));
            }
            
       votetime.add(p.getName());
       p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }
        
        p.closeInventory();
    }
    
    
    @EventHandler
    public void openTimeVote(PlayerInteractEvent event){
        if(event.getPlayer().getItemInHand() == null){
            return;
        }
        
        if(event.getPlayer().getItemInHand().getType() != Material.WATCH){
            return;
        }
        if(!event.getPlayer().getItemInHand().hasItemMeta()){
            return;
        }
        if(!event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()){
            return;
        }
        if(!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("time")){
           return; 
        }
        event.setCancelled(true);
        if(this.Manager.GetGame() == null){
            return;
        }
        if(this.Manager.GetGame().GetType() != null){
        if(this.Manager.GetGame().GetType() != GameType.SWTeams && this.Manager.GetGame().GetType() != GameType.SkyWars){
            return;
        }}
        if(this.Manager.GetGame().GetState() != GameState.Recruit){
            return;
        }
       Inventory iv = Bukkit.createInventory(null, 9, Colorizer.Color("&e&lTime Voting"));
ItemStack normal = new ItemStack(Material.INK_SACK,1,(byte)8);
normal.setDurability((short)8);

ItemMeta im = normal.getItemMeta();
im.setDisplayName(Colorizer.Color("&a&lDia &e&l- "+this.Manager.GetGame().time1+" Vote`s"));
normal.setItemMeta(im);

ItemStack medium = new ItemStack(Material.INK_SACK,1,(byte)10);

ItemMeta im2 = medium.getItemMeta();
im2.setDisplayName(Colorizer.Color("&a&lMedio Dia &e&l- "+this.Manager.GetGame().time2+" Vote`s"));
medium.setItemMeta(im2);
medium.setDurability((short)10);

ItemStack opchest = new ItemStack(Material.INK_SACK,1,(byte)5);
   
opchest.setDurability((short)5);
ItemMeta im3 = opchest.getItemMeta();
im3.setDisplayName(Colorizer.Color("&a&lNoche &e&l- "+this.Manager.GetGame().time3+" Vote`s"));
opchest.setItemMeta(im3);
    
iv.setContents(new ItemStack[]{
    null,null,null,normal,medium,opchest,null,null,null
});

event.getPlayer().openInventory(iv);
event.getPlayer().updateInventory();
event.setCancelled(true);
    }
    
    @EventHandler
    public void DontSteal(InventoryClickEvent event){
if(this.Manager.GetGame() != null){
    if(this.Manager.GetGame().IsLive()){
        return;
    }else{
        event.setCancelled(true);
    }
}
    }
    /*
        @EventHandler
    public void openStatistics(PlayerInteractEvent event){
            if(event.getPlayer().getItemInHand() == null){
                return;
            }
            
            if(event.getPlayer().getItemInHand().getType() != Material.ENCHANTED_BOOK){
                return;
            }
            if(this.Manager.GetGame() == null){
                return;
            }
            if(this.Manager.GetGame().IsLive() && this.Manager.GetGame().IsPlaying(event.getPlayer())){
                return;
            }

            Inventory iv = Bukkit.createInventory(null, 54, Colorizer.Color("&f&lEstadisticas"));
            Player p = event.getPlayer();
            
            ItemStack one = new ItemStack(Material.DIAMOND,1);
            
            ItemMeta im = one.getItemMeta();
            im.setDisplayName(Colorizer.Color("&a&oJuegos ganados: &c"+(double)db.getPlayerData(p).getWinned()));
            one.setItemMeta(im);
            
            ItemStack two = new ItemStack(Material.IRON_INGOT,1);
            
            ItemMeta im2 = two.getItemMeta();
            im2.setDisplayName(Colorizer.Color("&a&oJuegos perdidos: &c"+((double)db.getPlayerData(p).getLoss())));
            two.setItemMeta(im2);
            
            ItemStack tree = new ItemStack(Material.GOLD_INGOT,1);
            
            ItemMeta im3 = tree.getItemMeta();
            im3.setDisplayName(Colorizer.Color("&a&oJuegos jugados: &c"+((double)db.getPlayerData(p).getWinned()+db.getPlayerData(p).getLoss())));
            tree.setItemMeta(im3);
            
            ItemStack fourth = new ItemStack(Material.DIAMOND_AXE,1);
            
            ItemMeta im4 = fourth.getItemMeta();
            im4.setDisplayName(Colorizer.Color("&a&oAsesinatos: &c"+((double)db.getPlayerData(p).getKills())));
            fourth.setItemMeta(im4);
            
            ItemStack five = new ItemStack(Material.LAVA_BUCKET,1);
            
            ItemMeta im5 = five.getItemMeta();
            im5.setDisplayName(Colorizer.Color("&a&oMuertes: &c"+((double)db.getPlayerData(p).getDeaths())));
            five.setItemMeta(im5);
            
            ItemStack six = new ItemStack(Material.BLAZE_POWDER,1);
            
            ItemMeta im6 = six.getItemMeta();
            im6.setDisplayName(Colorizer.Color("&a&oKDA: &c"+((double)db.getPlayerData(p).getKills()/(double)db.getPlayerData(p).getDeaths())));
            six.setItemMeta(im6);
            
            iv.setContents(new ItemStack[]{
                null,null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,null,null,null,
                null,one,null,null,tree,null,null,two,null,
                null,null,fourth,null,null,null,five,null,null,
                null,null,null,null,six,null,null,null,null,
                null,null,null,null,null,null,null,null,null
            });
            
            event.getPlayer().openInventory(iv);
            event.getPlayer().updateInventory();
            event.setCancelled(true);
    }*/


    public boolean hasKit(Player player,String permkit1, String permkit2){
        if(db.getPlayerData(player).hasTransaction(permkit1)){
            return true;
        }else if(db.getPlayerData(player).hasTransaction(permkit2)){
            return true;
        }else{
            return false;
        }
    }

    @EventHandler
    public void openKits(PlayerInteractEvent event){

            if(event.getPlayer().getInventory().getItemInMainHand() == null){
                return;
            }
            
            if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.NETHER_STAR){
                return;
            }
            if(this.Manager.GetGame() == null){
                return;
            }
            if(!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()){
                return;
            }
            if(!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()){
                return;
            }
            if(!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().toLowerCase().contains("kit")){
           return;     
            }
            
            Inventory iv = Bukkit.createInventory(null, 54, Colorizer.Color("&aKit Selector"));
            Player p = event.getPlayer();
                 
            
            for(Kit kit : this.Manager.GetGame().GetKits()){
                if(!kit.isenabled()){
                    continue;
                }
            ItemStack kitem = new ItemStack(kit.getDisplayMaterial());
            ItemMeta im = kitem.getItemMeta();
            im.setDisplayName(Colorizer.Color("&aKit &a&n"+kit.GetName()));
            List<String> lore = new ArrayList();

            String permkit = "kit." + kit.GetName().replace(" ", "").replace(" ", "");
            String permkit2 = "kit." +Manager.GetGame().GetType().GetName() +"."+ kit.GetName().replace(" ", "").replace(" ", "");

            if(this.hasKit(event.getPlayer(),permkit,permkit2)){
            lore.add(Colorizer.Color("&fClick &apara Seleccionar"));
            }else{
            lore.add(Colorizer.Color("&fCompra por &c"+kit.GetCost()));
            }
            
            
            if(kit.GetDesc().length >= 1){
                for(String str : kit.GetDesc()){
                    lore.add(str);
                }
            }
            im.setLore(lore);
            kitem.setItemMeta(im);
            
            iv.addItem(kitem);
            }
            
            event.getPlayer().openInventory(iv);
            event.getPlayer().updateInventory();
            event.setCancelled(true);
        
    }
    
    

  
  List<Item> nopickup = new ArrayList();
  
  @EventHandler
  public void noPickup(PlayerPickupItemEvent event){
      if(nopickup.isEmpty()){
          return;
      }
      if(nopickup.contains(event.getItem())){
          event.setCancelled(true);
      }
  }
  
  HashMap<Player,HashMap<Item,Long>> biitem  = new HashMap();
  @EventHandler
  public void onTomahawk(PlayerInteractEvent event) throws SQLException{
    if(event.getPlayer().getItemInHand() == null){
        return;
    }
    if(this.Manager.GetGame() == null){
        return;
    }
    if(this.Manager.GetGame().GetType() != GameType.SkyWars
            && this.Manager.GetGame().GetType() != GameType.SWTeams){
        return;
    }
    if(!event.getPlayer().getItemInHand().getType().name().toLowerCase().contains("axe")){
        return;
    }
    if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() ==  Action.PHYSICAL){
        return;
    }
    Player p = event.getPlayer();

    ItemStack hawk = event.getPlayer().getItemInHand();
    Item hawk2 = (Item)p.getWorld().dropItem(new Location(p.getLocation().getWorld(),p.getLocation().getX(),p.getLocation().getY()+2,p.getLocation().getZ()), hawk);
    hawk2.setVelocity(p.getLocation().getDirection());
    HashMap<Item,Long> bi = new HashMap();
    this.nopickup.add(hawk2);
    bi.put(hawk2, System.currentTimeMillis()+1000);
    biitem.put(p, bi);
     if(p.getItemInHand().getAmount() <= 1){
     p.setItemInHand(new ItemStack(Material.AIR)); 
     }
     else{
     p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
     }
  }
  

  
  @EventHandler
  public void comeBack(UpdateEvent event){
      if(event.getType() != UpdateType.TICK){
          return;
      }
      if(this.Manager.GetGame() == null){
          return;
      }
      if(this.Manager.GetGame().GetType() != GameType.SkyWars
              && this.Manager.GetGame().GetType() != GameType.SWTeams){
          return;
      }

      if(this.Manager.GetGame().GetState() != GameState.Live){
          if(!biitem.isEmpty()){
              biitem.clear();
          }
          return;
      }
      if(biitem.isEmpty()){
          return;
      }
     for(Player p : biitem.keySet()){
         if(biitem.get(p).isEmpty()){
             biitem.remove(p);
             continue;
         }
         for(Item item : biitem.get(p).keySet()){
             if(biitem.get(p).get(item) < System.currentTimeMillis()){
                 Long diff = System.currentTimeMillis()-biitem.get(p).get(item);
                 if(diff > 500){
                     p.getInventory().addItem(item.getItemStack());
                     
                     biitem.get(p).remove(item);
                     item.remove();
                     continue;
                 }
                 for(Player paa : this.Manager.GetGame().GetPlayers(true)){
                     if(paa == p){
                         continue;
                     }
                  if(paa.getLocation().distance(item.getLocation()) <= 2){
                    this.Manager.GetDamage().NewDamageEvent(paa, p, null, EntityDamageEvent.DamageCause.CUSTOM, 1.0, true, true, true, "Tomahawk", "Tomahawk");
                    
                  }   
                 }
                 item.getWorld().strikeLightningEffect(item.getLocation());
                 Vector toloca = p.getLocation().subtract(item.getLocation()).toVector();
                 double x = toloca.getX();
                 double y = toloca.getY();
                 double z = toloca.getZ();

                 if(x > 4 || z > 4){
                     x = x/3;
                     y = y/3;
                     z = z/3;}

                 if(x>4){x=4;}
                 if(z>4){z=4;}
                 if(y>4){y=4;}

                 item.setVelocity(new Vector(x,y,z));
                      
             }
         }
     }
  }
  
    public String isOK(Double value, Double need){
        if(value >= need){
            return "&a&lDone";
        }else{
            return "&c"+(value - need);
        }
    }
    /*
    @EventHandler
    public void openAchievements(PlayerInteractEvent event){
        try {
            if(event.getPlayer().getItemInHand() == null){
                return;
            }
            
            if(event.getPlayer().getItemInHand().getType() != Material.GOLD_NUGGET){
                return;
            }
            if(this.Manager.GetGame() == null){
                return;
            }
            Inventory iv = Bukkit.createInventory(null, 54, Colorizer.Color("&f&lLogros"));
            Player p = event.getPlayer();
            
            ItemStack it1 = new ItemStack(Material.WOOD_SWORD);
            ItemMeta it1m  = it1.getItemMeta();
            it1m.setDisplayName(Colorizer.Color("&a&l50Kills: "+this.isOK((double)db.getPlayerData(p).getKills(), (double)250)));
            it1.setItemMeta(it1m);
            
            ItemStack it2 = new ItemStack(Material.STONE_SWORD);
            ItemMeta it2m  = it2.getItemMeta();
            it2m.setDisplayName(Colorizer.Color("&a&l100Kills: "+this.isOK((double)db.getPlayerData(p).getKills(), (double)500)));
            it2.setItemMeta(it2m);
            
            ItemStack it3 = new ItemStack(Material.GOLD_SWORD);
            ItemMeta it3m  = it3.getItemMeta();
            it3m.setDisplayName(Colorizer.Color("&a&l250Kills: "+this.isOK((double)db.getPlayerData(p).getKills(), (double)1000)));
            it3.setItemMeta(it3m);
            
            ItemStack it4 = new ItemStack(Material.IRON_SWORD);
            ItemMeta it4m  = it4.getItemMeta();
            it4m.setDisplayName(Colorizer.Color("&a&l500Kills: "+this.isOK((double)db.getPlayerData(p).getKills(), (double)2500)));
            it4.setItemMeta(it4m);
            
            ItemStack it5 = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta it5m  = it5.getItemMeta();
            it5m.setDisplayName(Colorizer.Color("&a&l1000Kills: "+this.isOK((double)db.getPlayerData(p).getKills(), (double)5000)));
            it5.setItemMeta(it5m);
            
            ItemStack it6 = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta it6m  = it6.getItemMeta();
            it6m.setDisplayName(Colorizer.Color("&a&l10Wins: "+this.isOK((double)db.getPlayerData(p).getWinned(), (double)50)));
            it6.setItemMeta(it6m);
            
            ItemStack it7 = new ItemStack(Material.GOLD_INGOT);
            ItemMeta it7m  = it7.getItemMeta();
            it7m.setDisplayName(Colorizer.Color("&a&l25Wins: "+this.isOK((double)db.getPlayerData(p).getWinned(), (double)125)));
            it7.setItemMeta(it7m);
            
            ItemStack it8 = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta it8m  = it8.getItemMeta();
            it8m.setDisplayName(Colorizer.Color("&a&l50Wins: "+this.isOK((double)db.getPlayerData(p).getWinned(), (double)250)));
            it8.setItemMeta(it8m);
            
            ItemStack it9 = new ItemStack(Material.GOLDEN_APPLE);
            ItemMeta it9m  = it9.getItemMeta();
            it9m.setDisplayName(Colorizer.Color("&a&l100Wins: "+this.isOK((double)db.getPlayerData(p).getWinned(), (double)500)));
            it9.setItemMeta(it9m);
            
            ItemStack it10 = new ItemStack(Material.GOLDEN_APPLE,1,(short)1);            
            ItemMeta it10m  = it10.getItemMeta();
            it10m.setDisplayName(Colorizer.Color("&a&l250Wins: "+this.isOK((double)db.getPlayerData(p).getWinned(), (double)1000)));
            it10.setItemMeta(it10m);
            
            ItemStack it11 = new ItemStack(Material.INK_SACK,1,(short)8);            
            ItemMeta it11m  = it11.getItemMeta();
            it11m.setDisplayName(Colorizer.Color("&a&l25Games: "+this.isOK((double)db.getPlayerData(p).getWinned()+db.getPlayerData(p).getLoss(), (double)25)));
            it11.setItemMeta(it11m);
            
            ItemStack it12 = new ItemStack(Material.INK_SACK,1,(short)10);            
            ItemMeta it12m  = it12.getItemMeta();
            it12m.setDisplayName(Colorizer.Color("&a&l75Games: "+this.isOK((double)db.getPlayerData(p).getWinned()+db.getPlayerData(p).getLoss(), (double)75)));
            it12.setItemMeta(it12m);
            
            ItemStack it13 = new ItemStack(Material.INK_SACK,1,(short)9);            
            ItemMeta it13m  = it13.getItemMeta();
            it13m.setDisplayName(Colorizer.Color("&a&l150Games: "+this.isOK((double)db.getPlayerData(p).getWinned()+db.getPlayerData(p).getLoss(), (double)150)));
            it13.setItemMeta(it13m);
            
            ItemStack it14 = new ItemStack(Material.INK_SACK,1,(short)13);            
            ItemMeta it14m  = it14.getItemMeta();
            it14m.setDisplayName(Colorizer.Color("&a&l300Games: "+this.isOK((double)db.getPlayerData(p).getWinned()+db.getPlayerData(p).getLoss(), (double)300)));
            it14.setItemMeta(it14m);
            
            ItemStack it15 = new ItemStack(Material.INK_SACK,1,(short)5);            
            ItemMeta it15m  = it15.getItemMeta();
            it15m.setDisplayName(Colorizer.Color("&a&l750Games: "+this.isOK((double)db.getPlayerData(p).getWinned()+db.getPlayerData(p).getLoss(), (double)750)));
            it15.setItemMeta(it15m);
            
            ItemStack it16 = new ItemStack(Material.SKULL_ITEM,1,(short)3);            
            ItemMeta it16m  = it16.getItemMeta();
            it16m.setDisplayName(Colorizer.Color("&a&lNoob &c&lKDR: "+this.isOK(this.getKDR(p), (double)0.5)));
            it16.setItemMeta(it16m);
            
            ItemStack it17 = new ItemStack(Material.SKULL_ITEM,1,(short)2);            
            ItemMeta it17m  = it17.getItemMeta();
            it17m.setDisplayName(Colorizer.Color("&a&lNormal &c&lKDR: "+this.isOK(this.getKDR(p), (double)1)));
            it17.setItemMeta(it17m);
            
            ItemStack it18 = new ItemStack(Material.SKULL_ITEM,1,(short)0);            
            ItemMeta it18m  = it18.getItemMeta();
            it18m.setDisplayName(Colorizer.Color("&a&lGood &c&lKDR: "+this.isOK(this.getKDR(p), (double)1.5)));
            it18.setItemMeta(it18m);
            
            ItemStack it19 = new ItemStack(Material.SKULL_ITEM,1,(short)1);            
            ItemMeta it19m  = it19.getItemMeta();
            it19m.setDisplayName(Colorizer.Color("&a&lPRO &c&lKDR: "+this.isOK(this.getKDR(p), (double)2.5)));
            it19.setItemMeta(it19m);
            
            ItemStack it20 = new ItemStack(Material.SKULL_ITEM,1,(short)4);            
            ItemMeta it20m  = it20.getItemMeta();
            it20m.setDisplayName(Colorizer.Color("&a&lGOD &c&lKDR: "+this.isOK(this.getKDR(p), (double)5)));
            it20.setItemMeta(it20m);
            
            iv.setContents(new ItemStack[]{
                null,null,null,null,null,null,null,null,null,
                null,it1,null,it2,it3,it4,null,it5,null,
                null,it6,null,it7,it8,it9,null,it10,null,
                null,it11,null,it12,it13,it14,null,it15,null,
                null,it16,null,it17,it18,it19,null,it20,null,
                null,null,null,null,null,null,null,null,null
            });
            
            event.getPlayer().openInventory(iv);
            event.getPlayer().updateInventory();
            event.setCancelled(true);
        } catch (SQLException ex) {
            Logger.getLogger(MiscManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    */
    
    public double getKDR(Player p) throws SQLException{
        return (double)db.getPlayerData(p).getKills()/(double)db.getPlayerData(p).getDeaths();
    }
    
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onFinderTeleport(InventoryClickEvent event){
        if(event.getCurrentItem() == null){
            return;
        }
        if(!event.getCurrentItem().hasItemMeta()){
            return;
        }
        if(!event.getCurrentItem().getItemMeta().hasDisplayName()){
            return;
        }
        if(!event.getCurrentItem().getItemMeta().hasLore()){
            return;
        }
        if(event.getInventory() == null){
            return;
        }
        if(event.getInventory().getName() == null){
            return;
        }
        if(!event.getInventory().getName().toLowerCase().contains("playerfinder")){
            return;
        }
        event.setCancelled(true);
        String name = event.getCurrentItem().getItemMeta().getDisplayName();
        name = ChatColor.stripColor(name);
        Player p = (Player)event.getWhoClicked();
        try{
            p.teleport(Bukkit.getPlayer(name));
        }catch(Exception e){
            p.sendMessage(Colorizer.Color("&aPlayer is &coffline"));
        event.getCurrentItem().setType(Material.AIR);
        }
    }
    
    @EventHandler
    public void openPlayerFinder(PlayerInteractEvent event){

            if(event.getPlayer().getItemInHand() == null){
                return;
            }
            
            if(event.getPlayer().getItemInHand().getType() != Material.COMPASS){
                return;
            }
            if(this.Manager.GetGame() == null){
                return;
            }
            if(!this.Manager.GetGame().IsLive()){
                return;
            }
        Player p = event.getPlayer();
        if(Manager.GetGame().IsAlive(p)){
            return;
        }
        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR){
            return;
        }
            Inventory iv = Bukkit.createInventory(null, 54, Colorizer.Color("&f&lPlayerFinder"));

            

            

            for(Player te : this.Manager.GetGame().GetPlayers(true)){
                ItemStack tepe = new ItemStack(Material.SKULL_ITEM);
                ItemMeta im = tepe.getItemMeta();
                im.setDisplayName(Colorizer.Color("&a&l"+te.getName()));
                List<String> lore = new ArrayList();
                lore.add(Colorizer.Color("&eClick to &fTeleport"));
                im.setLore(lore);
                tepe.setItemMeta(im);
                iv.addItem(tepe);
                
            }
        
            
            event.getPlayer().openInventory(iv);
            event.getPlayer().updateInventory();
            event.setCancelled(true);
      
    }
    
    @EventHandler
    public void openvoteChest(PlayerInteractEvent event)
    {
        if(event.getPlayer().getItemInHand() == null){
            return;
        }
        if(event.getPlayer().getWorld() != null){
            if(event.getPlayer().getItemInHand() != null){
                if(event.getPlayer().getItemInHand().hasItemMeta()){
                    if(event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()){
                        if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("voting")){
                            event.setCancelled(true);
                        }
                    }
                }
            }}
        if(event.getPlayer().getItemInHand().getType() != Material.CHEST){
            return;
        }
        if(this.Manager.GetGame() == null){
            return;
        }
        if(this.Manager.GetGame().GetType() != null){
        if(this.Manager.GetGame().GetType() != GameType.SWTeams && this.Manager.GetGame().GetType() != GameType.SkyWars){
            return;
        }}
        if(this.Manager.GetGame().GetState() != GameState.Recruit){
            return;
        }
Inventory iv = Bukkit.createInventory(null, 9, Colorizer.Color("&a&lChest Voting"));
ItemStack normal = new ItemStack(Material.INK_SACK,1,(byte)8);
normal.setDurability((short)8);

ItemMeta im = normal.getItemMeta();
im.setDisplayName(Colorizer.Color("&a&lNormal Chest`s &e&l- "+this.Manager.GetGame().chest1+" Vote`s"));
normal.setItemMeta(im);

ItemStack medium = new ItemStack(Material.INK_SACK,1,(byte)10);

ItemMeta im2 = medium.getItemMeta();
im2.setDisplayName(Colorizer.Color("&a&lMedium Chest`s &e&l- "+this.Manager.GetGame().chest2+" Vote`s"));
medium.setItemMeta(im2);
medium.setDurability((short)10);

ItemStack opchest = new ItemStack(Material.INK_SACK,1,(byte)5);
   
opchest.setDurability((short)5);
ItemMeta im3 = opchest.getItemMeta();
im3.setDisplayName(Colorizer.Color("&a&lOPChest`s &e&l- "+this.Manager.GetGame().chest3+" Vote`s"));
opchest.setItemMeta(im3);
    
iv.setContents(new ItemStack[]{
    null,null,null,normal,medium,opchest,null,null,null
});

event.getPlayer().openInventory(iv);
event.getPlayer().updateInventory();
event.setCancelled(true);
}
    

    
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void HubClockInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (player.getItemInHand() == null) {
            return;
        }
        if (player.getItemInHand().getType() != Material.WATCH) {
            return;
        }

        if (!player.getItemInHand().hasItemMeta()) {
            return;
        }
        if (!player.getItemInHand().getItemMeta().hasDisplayName()) {
            return;
        }
        if (!player.getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("return")) {
            return;
        }


        Manager.GetPortal().SendPlayerToServer(player, Manager.getConfig().getString("GameConfig.bungeelobby"));

    }
    
    @EventHandler
    public void HubCommand(final PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/hub")
                || event.getMessage().startsWith("/lobby")
                || event.getMessage().startsWith("/leave")
                || event.getMessage().startsWith("/"+Manager.getConfig().getString("GameConfig.bungeelobby")))
        {
Manager.GetPortal().SendPlayerToServer(event.getPlayer(), Manager.getConfig().getString("GameConfig.bungeelobby"));
event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onDie(CombatDeathEvent event){
     if(event.GetEvent().getEntity() != null){
         if(event.GetLog().GetKiller() != null){
             if(event.GetLog().GetKiller().GetName() != null){

                 UtilDisplay.sendTitle((Player)event.GetEvent().getEntity(), Colorizer.Color("&cAsesinado por"), Colorizer.Color("&a"+event.GetLog().GetKiller().GetName()));
             }
         }
     }   
   }
    
    


HashMap<Item,Long> tnts = new HashMap();

@EventHandler
public void onDetonate(UpdateEvent event){
    if(event.getType() != UpdateType.FAST){
        return;
    }
    if(tnts.isEmpty()){
        return;
    }
    for(Item et : tnts.keySet()){
        if(tnts.get(et) < System.currentTimeMillis()){
           tnts.remove(et);
           Entity et2 = et.getWorld().spawnEntity(et.getLocation(), EntityType.PRIMED_TNT);
           ((CraftEntity)et2).getHandle().setInvisible(true);
           et.remove();
        }
    }
}

@EventHandler
public void onGrenade(PlayerInteractEvent event){
    if(event.getPlayer().getItemInHand() == null){
        return;
    }
    if(this.Manager.GetGame() == null){
        return;
    }
    if(event.getPlayer().getWorld().getName().equals("world")){
        return;
    }
    if(Manager.GetGame().GetType()  != GameType.SkyWars && Manager.GetGame().GetType() !=  GameType.SWTeams){
      return;
    }
    if(event.getPlayer().getItemInHand()
           .getType()  != Material.SLIME_BALL){
        return;
    }
    Player p = event.getPlayer();
     if(p.getItemInHand().getAmount() <= 1){
     p.setItemInHand(new ItemStack(Material.AIR)); 
     }
     else{
     p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
     }
    Location loca = p.getLocation();
    Item primed = p.getWorld().dropItem(new Location(loca.getWorld(),loca.getX(),loca.getY()+2,loca.getZ()), new ItemStack(Material.SLIME_BALL));
   
    primed.setVelocity(p.getLocation().getDirection());
nopickup.add(primed);
    tnts.put(primed, System.currentTimeMillis()+2500);
    
}

}
