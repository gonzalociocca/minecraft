package mineultra.game.center.game.games.pack.buildbattle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mineultra.core.common.MySQL;
import mineultra.core.common.util.C;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.MSGUtil;
import mineultra.core.common.util.MatColorUtil;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.common.util.UtilParticle.PCType;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.game.TeamGame;
import mineultra.game.center.game.games.pack.turboracers.columnType;
import mineultra.game.center.kit.Kit;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.EntityLightning;
import net.minecraft.server.v1_8_R2.EntityLiving;
import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PacketPlayOutMapChunk;
import net.minecraft.server.v1_8_R2.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_8_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLightningStrike;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class BuildBattle extends TeamGame
{

  private static long startTime = 0;
  public BuildBattle(centerManager manager)
  {
    super(manager, GameType.BuildBattle, 
      new Kit[] {}, 
      new String[] {
 });

    this._help =  null;
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
    this.WorldTimeSet = 0;
    this.CreatureAllow = true;
    this.CreatureAllowOverride=true;
    startTime = System.currentTimeMillis();
      Manager.GetExplosion().SetRegenerate(false);

 this.init();   
  
  }
 

  
@Override
public void ParseData()
  {
this.WorldData.World.setSpawnFlags(true, true);

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

    String name1 = Colorizer.Color("&aClima de Parcela");
    String name2 = Colorizer.Color("&aTiempo de Parcela");
    String name3 = Colorizer.Color("&aBioma de Parcela");
    String name4 = Colorizer.Color("&aPiso de Parcela");
    String name5 = Colorizer.Color("&aCabezas Custom");
    String name6 = Colorizer.Color("&aParticulas");
    String name7 = Colorizer.Color("&aCreador de Banners");
    String name8 = Colorizer.Color("&aGuardar Items de la Barra");
    String name9 = Colorizer.Color("&aMusica");
    String name10 = Colorizer.Color("&aEstadisticas");
    String goback = Colorizer.Color("&aGo back");
    
    ItemStack it1 = this.makeItem(Material.FEATHER, 1, (short)0, name1, new String[]{
        "&7Setea el clima","&7de tu parcela!"
    });
    
    ItemStack it2 = this.makeItem(Material.getMaterial(347), 1, (short)0, name2, new String[]{
        "&7Setea el tiempo de tu parcela!"
    });
    
    ItemStack it3 = this.makeItem(Material.PAPER, 1, (short)0, name3, new String[]{
        "&7Setea el Bioma de tu parcela!"
    });
    
    ItemStack it4 = this.makeItem(Material.STAINED_CLAY, 1, (short)0, name4, new String[]{
        "&7Pon un bloque aqui, para usarlo de piso!"
    });
    
    ItemStack it5 = this.makeItem(Material.SKULL_ITEM, 1, (short)1, name5, new String[]{
        "&7Utiles cabezas para añadir detalles!"
    });
    
    ItemStack it6 = this.makeItem(Material.FIREWORK, 1, (short)0, name6, new String[]{
        "&7Selecciona particulas para usar en tu parcela!"
    });
    
    ItemStack it7 = this.makeItem(Material.BANNER, 1, (short)0, name7, new String[]{
        "&7Craftea un banner customizado!"
    });
 
    ItemStack it8 = this.makeItem(Material.CHEST, 1, (short)0, name8, new String[]{
        "&7Guarda los items de la barra","&7para usarlos en otras partidas"
    });

    ItemStack it9 = this.makeItem(Material.NOTE_BLOCK, 1, (short)0, name9, new String[]{
        "&7Activa o desactiva la musica"
    });

    ItemStack it10 = this.makeItem(Material.ENCHANTED_BOOK, 1, (short)0, name10, new String[]{
        "&7Ve tus estadisticas!"
    });
    
HashMap<Player,Integer> biomes = new HashMap();
ItemStack bPlains = this.makeItem(Material.GRASS, 1, (short)0, "&aPlanicie",null);
ItemStack bMesa = this.makeItem(Material.getMaterial(38), 1, (short)2, "&aMesa",null);
ItemStack bOcean = this.makeItem(Material.getMaterial(38), 1, (short)1, "&aOceano",null);
ItemStack bDesert = this.makeItem(Material.CACTUS, 1, (short)0, "&aDesierto",null);
ItemStack bForest = this.makeItem(Material.LOG, 1, (short)0, "&aBosque",null);
ItemStack bJungle = this.makeItem(Material.LOG, 1, (short)3, "&aJungla",null);
ItemStack bIcePlains = this.makeItem(Material.SNOW_BALL, 1, (short)0, "&aPlanicies de Hielo",null);
ItemStack bSwamp = this.makeItem(Material.SAPLING, 1, (short)0, "&aPantano",null);
ItemStack bSavanna = this.makeItem(Material.SANDSTONE, 1, (short)0, "&aSabana",null);

public void openBiome(Player pe){
    Inventory iv = Bukkit.createInventory(null, 36, name3);
    iv.setContents(new ItemStack[]{
    null,null,null,null,null,null,null,null,null,
    bPlains,bMesa,bOcean,bDesert,bForest,bJungle,bIcePlains,bSwamp,bSavanna,
    null,null,null,null,gobacke,null,null,null,null,
    null,null,null,null,null,null,null,null,null,
    });
    pe.openInventory(iv);
    pe.updateInventory();
}
    
    
HashMap<Player,Integer> weathers = new HashMap();

ItemStack wStormy = this.makeItem(Material.BLAZE_POWDER, 1, (short)0, "&aStormy",null);
ItemStack wRainy = this.makeItem(Material.WATER_BUCKET, 1, (short)0, "&aRainy",null);
ItemStack wSnowing = this.makeItem(Material.SNOW_BALL, 1, (short)0, "&aSnowing",null);
ItemStack wSunny = this.makeItem(Material.YELLOW_FLOWER, 1, (short)0, "&aSunny",null);

ItemStack gobacke = this.makeItem(Material.ARROW, 1, (short)0, goback, null);
    
public void openWeather(Player pe){

    if(!weathers.containsKey(pe)){
        weathers.put(pe, 0);
    }

    Inventory iv = Bukkit.createInventory(null, 36, name1);
    iv.setContents(new ItemStack[]{
    null,null,null,null,null,null,null,null,null,
    null,null,wStormy,wRainy,null,wSnowing,wSunny,null,null,
    null,null,null,null,gobacke,null,null,null,null,
    null,null,null,null,null,null,null,null,null,
    });
    
    pe.openInventory(iv);
    pe.updateInventory();
    
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
    im.setDisplayName(Colorizer.Color("&a&l"+slot*3+"hs"));
    it.setItemMeta(im);
    return it;
}

public void openTime(Player pe){

    if(!times.containsKey(pe)){
        times.put(pe, 0);
    }



    Inventory iv = Bukkit.createInventory(null, 36, name2);
    iv.setContents(new ItemStack[]{
    null,null,null,null,null,null,null,null,null,
    timestack(pe,0),timestack(pe,1),timestack(pe,2),timestack(pe,3),timestack(pe,4),timestack(pe,5),timestack(pe,6),timestack(pe,7),timestack(pe,8),
    null,null,null,null,gobacke,null,null,null,null,
    null,null,null,null,null,null,null,null,null,
    });
    
    pe.openInventory(iv);
    pe.updateInventory();
}

@EventHandler
public void onOptionsEvent(InventoryClickEvent event){
    if(event.getInventory().getName() == null){
        return;
    }
    if(event.getWhoClicked()==null){
        return;
    }
   String invname = event.getInventory().getName();
    if(event.getCurrentItem() != null){
        if(event.getCurrentItem().hasItemMeta()){
            if(event.getCurrentItem().getItemMeta().hasDisplayName()){
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.goback)){
                    this.openOptions((Player)event.getWhoClicked());
                    event.setCancelled(true);
                    
                    return;
                }
            }
        }
    }
    if(invname.equalsIgnoreCase(name1)){
        Player p = (Player)event.getWhoClicked();
        if(event.getSlot()== 11){
            this.weathers.put(p, 0);
        }if(event.getSlot()==12){
            this.weathers.put(p, 1);
        }if(event.getSlot()==14){
            this.weathers.put(p, 2);
        }if(event.getSlot()==15){
            this.weathers.put(p, 3);
        }
    event.setCancelled(true);
    }
    if(invname.equalsIgnoreCase(name2)){
        Player p = (Player)event.getWhoClicked();
        if(event.getSlot() >= 9 && event.getSlot() <= 17){
            times.put(p, event.getRawSlot()-9);
            
            p.setPlayerTime(this.timeOffset(p, 0), true);
            event.setCancelled(true);
        }
    }
    if(invname.equalsIgnoreCase(name3)){
        Player p = (Player)event.getWhoClicked();
         if(event.getSlot() == 9){//plains
        this.sendBiomeChange(p, 1,true);
        }else if(event.getSlot() == 10){//mesa
        this.sendBiomeChange(p, 37,true);
        }else if(event.getSlot() == 11){//ocean
        this.sendBiomeChange(p, 0,true);
        }else if(event.getSlot()==12){//desert
        this.sendBiomeChange(p, 2,true);
        }else if(event.getSlot()==13){//forest
        this.sendBiomeChange(p, 4,true);
        }else if(event.getSlot()==14){//jungle
        this.sendBiomeChange(p, 21,true);
        }else if(event.getSlot()==15){//ice plains
        this.sendBiomeChange(p, 12,true);
        }else if(event.getSlot()==16){//swamp
        this.sendBiomeChange(p, 6,true);
        }else if(event.getSlot()==17){//savanna
        this.sendBiomeChange(p, 35,true);
        }
        
        event.setCancelled(true);
    }
    if(invname.equalsIgnoreCase("Banner Base Color")){
        int dura = event.getSlot();
     if(dura < 16 && dura >= 0){
        Player pe = (Player)event.getWhoClicked();
        ItemStack ban = this.banners.get(pe);
        BannerMeta bm = (BannerMeta)ban.getItemMeta();
        bm.setBaseColor(MatColorUtil.byDurability((short)dura));
        ban.setItemMeta(bm);
        banners.put(pe, ban);
        this.openBannerBuilderPatternColor((Player)event.getWhoClicked());
     }
        event.setCancelled(true);

    }
    if(event.getInventory().getName().equalsIgnoreCase("Add layer")){
        
int dura = event.getSlot();
if(dura < PatternType.values().length){
Player pe = (Player)event.getWhoClicked();
ItemStack ban = this.banners.get(pe);
BannerMeta bm = (BannerMeta)ban.getItemMeta();
bm.addPattern(new Pattern(this.bannerlcs.get(pe), PatternType.values()[dura]));
ban.setItemMeta(bm);
banners.put(pe, ban);

this.openBannerBuilderPatternColor((Player)event.getWhoClicked());

}
else if(dura==49){
Player pe = (Player)event.getWhoClicked();
pe.getInventory().addItem(this.banners.get(pe));
pe.closeInventory();
}

        event.setCancelled(true);

    }
    if(invname.equalsIgnoreCase("Layer color")){
        if(event.getSlot() < 16 && event.getSlot() >= 0){
        Player pe = (Player)event.getWhoClicked();
        int slot = event.getSlot();
        this.bannerlcs.put(pe, MatColorUtil.byDurability((short)slot));
        this.openBannerBuilderPattern((Player)event.getWhoClicked());
        event.setCancelled(true);
        }
        else if(event.getSlot()==22){
Player pe = (Player)event.getWhoClicked();
pe.getInventory().addItem(this.banners.get(pe));
pe.closeInventory();
}

        event.setCancelled(true);

    }
    if(invname.equalsIgnoreCase(name6) && event.getCurrentItem() != null){
           Player p = (Player)event.getWhoClicked();        
        String iname = event.getCurrentItem().getItemMeta().getDisplayName();
        if(iname==null){
            return;
        }
        if(iname.equalsIgnoreCase(Colorizer.Color("&aParticle List"))){
            this.openParticleList(p);
            event.setCancelled(true);
            return;
        }
        for(PCType pc : PCType.values()){
         if(iname.contains(pc.getName())){

           ItemStack toadd = this.makeItem(pc.getIcon(), 1, (short)0,"&a"+pc.getName(), new String[]{
              "&7Particle Effect"
              ,"      "
              ,"&aRight-Click to add Particle" 
           });
           p.getInventory().addItem(toadd);
           p.closeInventory();
         }
        }
        event.setCancelled(true);
    }
    if(invname.equalsIgnoreCase(Colorizer.Color("&aParticle List"))){
   Player pe = (Player)event.getWhoClicked();
   if(!particles.containsKey(pe)){
       event.setCancelled(true);
       return;
   }
   if(particles.get(pe).isEmpty()){
       event.setCancelled(true);
       return;
   }
 if(event.getCurrentItem().getItemMeta().hasDisplayName()){
      String iname = event.getCurrentItem().getItemMeta().getDisplayName();
      if(iname==null){
          event.setCancelled(true);
          return;
      }
      for(PCType pc : PCType.values()){
          if(iname.contains(pc.getName())){
for(StaticPS ps : particles.get(pe)){
   if(ps.getType() == pc && particles.get(pe).contains(ps)){
     pe.sendMessage(Colorizer.Color("&aRemoviste &c"+pc.getName()));
     event.getCurrentItem().setType(Material.AIR);
     particles.get(pe).remove(ps);
     
     event.setCancelled(true);
     return;
   }
}
        
          }
      }
    }
    event.setCancelled(true);
   }
    
   if(invname.equalsIgnoreCase(name5)){
       Player player = (Player)event.getWhoClicked();
       ItemStack it = event.getCurrentItem();
       if(it== null){
           return;
       }
       if(!it.hasItemMeta()){
           return;
       }
       if(!it.getItemMeta().hasDisplayName()){
           return;
       }
       for(HeadType type : HeadType.values()){
           if(it.getItemMeta().getDisplayName().equalsIgnoreCase(Colorizer.Color(type.getName()))){
               this.openHeadsInventory(player, type,1);
               event.setCancelled(true);
               return;
           }
       }
   }
   if(invname.equalsIgnoreCase(name10)){
       event.setCancelled(true);
       return;
   }
   
 if(this.headdb != null){
     if(event.getCurrentItem() != null){
         
         if(event.getCurrentItem().hasItemMeta()){
             Player pe = (Player)event.getWhoClicked();
             if(!headpages.containsKey(pe)){
                 headpages.put(pe, 1);
             }
             if(event.getSlot() == 48){
            for(HeadType type : HeadType.values()){
                if(invname.equalsIgnoreCase(Colorizer.Color(type.getName()))){
                this.openHeadsInventory(pe, type, this.headpages.get(pe)-1);
                event.setCancelled(true);
                return;
                }
               }
             }
             else if(event.getSlot() == 50){
            for(HeadType type : HeadType.values()){
                if(invname.equalsIgnoreCase(Colorizer.Color(type.getName()))){
                this.openHeadsInventory(pe, type, this.headpages.get(pe)+1);
                event.setCancelled(true);
                return;
                }
               }
             }
             else if(event.getCurrentItem().getType() == Material.SKULL_ITEM){
     for(HeadType type : HeadType.values()){
         if(invname.equalsIgnoreCase(Colorizer.Color(type.getName()))){
             ((Player)event.getWhoClicked()).getInventory().addItem(event.getCurrentItem());
             event.setCancelled(true);
             return;
         }
     }
 }
         
         }}}
}

public HashMap<Player,Byte> biomedata = new HashMap();

public boolean cDistance(Chunk c, Player pe){
    World we = pe.getWorld();
    Location loc1 = pe.getLocation();
    Location loc2 = new Location(we,c.getX(),loc1.getY(),c.getZ());
    
    if(loc1.distanceSquared(loc2) < 250){
        return true;
    }
    return false;
}

public void sendBiomeChange(Player player, int biomeid, boolean save){
Location loc  = player.getLocation();
Chunk c = loc.getChunk();
if(save){
this.biomedata.put(player, (byte)(biomeid));
}
net.minecraft.server.v1_8_R2.Chunk ce = ((CraftChunk)c).getHandle();

byte[] replacebiome = new byte[256];
for(int a = 0; a < replacebiome.length;a++){
    replacebiome[a]= (byte)biomeid;
}
ce.a(replacebiome);

for(int dx = -4;dx < 4;dx++){
for(int dz = -4; dz < 4;dz++){
   Chunk cs = (loc.clone().add(dx*16, 0, dz*16)).getChunk();
   if(!cs.isLoaded()){
       continue;
   }
((CraftChunk)cs).getHandle().a(replacebiome);

((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(((CraftChunk)cs).getHandle(), true, 65535));
}
}
}



public int timeOffset(Player pe,int forced){
    int time = -1;
    if(pe != null){
    if(!times.containsKey(pe)){
        times.put(pe, 0);
    }
    time = times.get(pe);}
    
    else{
    time = forced;
    }
           if(time==0){
                return 11000;
            }
            else if(time==1){
                return 11300;
            }
            else if(time==2){
                return 11600;
            }
            else if(time==3){
               return 11900;
            }
            else if(time==4){
                return 12200;
            }
            else if(time==5){
                return 12500;
            }else if(time==6){
                return 12800;
            }else if(time==7){
                return 13100;
            }else if(time==8){
                return 13400;
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
public void updateWeather(Player pe, Player weatherfrom){
if(!weathers.containsKey(pe)){
    weathers.put(pe, 3);
}
        if(weatherfrom != null){
            int wet = weathers.get(weatherfrom);
if(wet >=2){
    pe.setPlayerWeather(WeatherType.CLEAR);
}else{
    pe.setPlayerWeather(WeatherType.DOWNFALL);
}
        }else{
            
int wet = weathers.get(pe);
if(wet >=2){
    if(wet == 2){
Location loc = pe.getLocation();
PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SNOW_SHOVEL,true,(float)loc.getX(),(float)loc.getY(),(float)loc.getZ(),7F,20F,7F,0F,500,0);
((CraftPlayer)pe).getHandle().playerConnection.sendPacket(packet);

    }
    pe.setPlayerWeather(WeatherType.CLEAR);
}else{
    if(wet==0){
EntityLightning et = ((CraftLightningStrike)pe.getWorld().spawnEntity(pe.getLocation().add(r.nextInt(8),r.nextInt(16),r.nextInt(8)), EntityType.LIGHTNING)).getHandle();
et.setOnFire(0);
et.isEffect = true;
et.isSilent = false;
PacketPlayOutSpawnEntityWeather packet =  new PacketPlayOutSpawnEntityWeather(et);
((CraftPlayer)pe).getHandle().playerConnection.sendPacket(packet);
et.die();
    }
    pe.setPlayerWeather(WeatherType.DOWNFALL);
}
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
    if(this.GetState() != GameState.Live && this.GetState() != GameState.Prepare){
        return;
    }

    for(Player p : this.GetPlayers(true)){
        if(p.getInventory().getItem(8)==null){
p.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Options "+C.cDGray+"(Right Click)", new String[] { "", ChatColor.RESET + "Click-Derecho", ChatColor.RESET + "para abrir el menu." }));
        }
        else if(p.getInventory().getItem(8).getType() != Material.NETHER_STAR){
      p.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Options "+C.cDGray+"(Right Click)", new String[] { "", ChatColor.RESET + "Click-Derecho", ChatColor.RESET + "para abrir el menu." }));
        }

this.updateTime(p,null);
this.updateWeather(p, null);
        
        
    }
    
    
    
}
HeadDatabase headdb = null;

HashMap<Player,Integer> headpages = new HashMap();


String skColor = Colorizer.Color(HeadType.Colors.getName());
String skAnimals = Colorizer.Color(HeadType.Animals.getName());
String skBlocks = Colorizer.Color(HeadType.Blocks.getName());
String skFoods = Colorizer.Color(HeadType.Foods.getName());
String skInterior= Colorizer.Color(HeadType.Interior.getName());
String skMisc = Colorizer.Color(HeadType.Misc.getName());
String skMobs = Colorizer.Color(HeadType.Mobs.getName());

public void openHeadsInventory(Player player,HeadType type, int page){
    if(page < 1){
        page = 1;
    }
    List<ItemStack> list = headdb.getList(type);
    Inventory iv = null;
    boolean toopen = true;
    if(player.getOpenInventory() != null){
        if(player.getOpenInventory().getTopInventory() != null){
        if(player.getOpenInventory().getTopInventory().getTitle() != null){
            if(player.getOpenInventory().getTopInventory().getTitle().equalsIgnoreCase(Colorizer.Color(type.getName()))){
            iv = player.getOpenInventory().getTopInventory();
            iv.clear();
                toopen = false;
            }
        }}
    }
    if(iv==null){
 iv =Bukkit.createInventory(null, 54,Colorizer.Color(type.getName()));
    }
headpages.put(player, page);
  int index = 0;          
    for(int a = (page*45)-45; a < list.size();a++){
    if(index >= 45){
        continue;
    }
    iv.setItem(index,list.get(a));
    index++;
    }
    
    iv.setItem(49, this.gobacke);
if(list.size() > 45){
    iv.setItem(50, headdb.craftCustomHead("{display:{Name:\"Next Page\"},SkullOwner:{Id:\"6754c1a4-746b-45ad-ac39-5763b8e20c3a\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNiMGNiZTFjOWQ3YTFiMmQ2MWRmNDVjNmQyOWE3NzRjYzU2NGUyZWI5YTliNzU2ZmNmOWUxMzM0ZTM3MSJ9fX0=\"}]}}}"));
if(page > 1){
    iv.setItem(48, headdb.craftCustomHead("{display:{Name:\"Prev page\"},SkullOwner:{Id:\"3c648c2b-e09a-4016-b470-17af45c5169a\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE2ZGI2YjZkMTJiYjRkYzc2ODJhOTZiM2I5MzNlN2EyOTUxZDM1NzE4NzkxOGU4YmVkYzY1ZGU5ZDc5NiJ9fX0=\"}]}}}"));
}
}
if(toopen){
    player.openInventory(iv);
}
player.updateInventory();
    
}

public void openSkulls(Player pe){
    if(headdb==null){
        headdb = new HeadDatabase(this.Manager);
    }

    Inventory iv = Bukkit.createInventory(null, 36, name5);
    
   iv.setItem(10, this.makeItem(Material.INK_SACK, 1, (short)4, skColor, null));
   iv.setItem(11, this.makeItem(Material.EGG, 1, (short)0,skAnimals, null));
   iv.setItem(12, this.makeItem(Material.BRICK, 1, (short)0,skBlocks, null));
   iv.setItem(13, this.makeItem(Material.COOKED_BEEF, 1, (short)0, skFoods, null));
   iv.setItem(14, this.makeItem(Material.BOOKSHELF, 1, (short)0, skInterior, null));
   iv.setItem(15, this.makeItem(Material.GOLD_NUGGET, 1, (short)0, skMisc, null));
   iv.setItem(16, this.makeItem(Material.MOB_SPAWNER, 1, (short)0, skMobs, null));
   iv.setItem(31, this.gobacke);
   pe.openInventory(iv);
   pe.updateInventory();
}

public void openParticles(Player pe){
    Inventory iv = Bukkit.createInventory(null, 54, name6);
int nx = 10;

for(PCType pct : PCType.values()){
if(nx==17 || nx==35 || nx==26 || nx==44){
    nx+=2;
}
if(pct.getIcon() != Material.PAPER){
    ItemStack it = this.makeItem(pct.getIcon(), 1,(short) 0, "&a"+pct.getName(), new String[]{
    "&7Particle Effect", "      ","&aClick to select!"
    });
    iv.setItem(nx, it);
    nx++;
}
}
iv.setItem(49, this.gobacke);
ItemStack cchest = this.makeItem(Material.CHEST, 1, (short)0, "&aParticle List", new String[]{
   "&7Remove particles from your plot" 
});
iv.setItem(50, cchest);

pe.openInventory(iv);
pe.updateInventory();

}

public void openParticleList(Player pe){
Inventory iv = Bukkit.createInventory(null, 54,Colorizer.Color("&aParticle List"));
if(!this.particles.containsKey(pe)){
    pe.sendMessage(Colorizer.Color("&cNo has colocado particulas"));
pe.closeInventory();
    return;
}
if(this.particles.get(pe).isEmpty()){
    pe.sendMessage(Colorizer.Color("&cNo has colocado particulas"));
pe.closeInventory();
    return;
}


int nx = 10;
for(StaticPS pc : particles.get(pe)){
if(nx==17 || nx==35 || nx==26 || nx==44){
    nx+=2;
}
PCType pce = pc.getType();
iv.addItem(this.makeItem(pce.getIcon(), 1, (short)0, "&a"+pce.getName(), new String[]{
"&7Particle Effect"
,"     "
,"&7 X: "+pc.getX()
,"&7 Y: "+pc.getY()
,"&7 Z: "+pc.getZ()
,"     "
,"&eClick to remove!"
}));
nx++;
}

pe.openInventory(iv);
pe.updateInventory();
}


HashMap<Player,ItemStack> banners = new HashMap();
HashMap<Player,DyeColor> bannerlcs = new HashMap();
HashMap<Player, PatternType> bannerpatt = new HashMap();

public void openBannerBaseColor(Player pe){

banners.put(pe, (new ItemStack(Material.BANNER)));
bannerlcs.put(pe, DyeColor.SILVER);

Inventory iv = Bukkit.createInventory(null, 36,"Banner Base Color");

for(int a = 0; a < 16;a++){
    ItemStack it = new ItemStack(Material.BANNER);

    BannerMeta im = (BannerMeta)it.getItemMeta();
    im.setDisplayName(ChatColor.GREEN+MatColorUtil.byDurability((short)a).name());
    im.setBaseColor(MatColorUtil.byDurability((short)a));
    it.setItemMeta(im);

    iv.addItem(it);
}
iv.setItem(22,this.gobacke);

pe.openInventory(iv);
pe.updateInventory();
}

public void openBannerBuilderPattern(Player pe){
Inventory iv = Bukkit.createInventory(null, 54, "Add layer");
if(!banners.get(pe).getItemMeta().hasDisplayName()
        ){
ItemStack ite=banners.get(pe);
BannerMeta ime = (BannerMeta)ite.getItemMeta();
ime.setDisplayName(Colorizer.Color("&aFinish"));
ite.setItemMeta(ime);
banners.put(pe, ite);
}
iv.setItem(49, banners.get(pe));

for(int a = 0; a < PatternType.values().length;a++){
ItemStack it = new ItemStack(Material.BANNER);

PatternType pt = PatternType.values()[a];

BannerMeta im = (BannerMeta)it.getItemMeta();
im.setBaseColor(this.bannerlcs.get(pe));
im.setDisplayName(Colorizer.Color("&a"+pt.name()));
if(bannerlcs.get(pe)==DyeColor.BLACK){
im.addPattern(new Pattern(DyeColor.WHITE,pt));    
}else{
im.addPattern(new Pattern(DyeColor.BLACK,pt));
}

it.setItemMeta(im);

iv.setItem(a,it);
}



pe.openInventory(iv);
pe.updateInventory();
}

public void openBannerBuilderPatternColor(Player pe){
  
Inventory iv = Bukkit.createInventory(null, 36,"Layer color");

for(int a = 0; a < 16;a++){
    ItemStack it = new ItemStack(Material.INK_SACK);

    ItemMeta im = it.getItemMeta();
    im.setDisplayName(ChatColor.GREEN+MatColorUtil.byDurability((short)a).name());
    it.setDurability((short)a);
    it.setItemMeta(im);

    iv.addItem(it);
}
if(!banners.get(pe).getItemMeta().hasDisplayName()){
ItemStack ite=banners.get(pe);
BannerMeta ime = (BannerMeta)ite.getItemMeta();
ime.setDisplayName(Colorizer.Color("&aFinish"));
ite.setItemMeta(ime);
banners.put(pe, ite);
}
iv.setItem(22, banners.get(pe));

pe.openInventory(iv);
pe.updateInventory();
}

public void openSaveloadout(Player pe){
    this.savetomysql(pe);
}

@EventHandler
public void onMusicUpdate(GameStateChangeEvent event){
    if(event.GetState() == GameState.Live){
        for(Player p : Bukkit.getOnlinePlayers()){
if(this.GetTeam(p) ==null){
    continue;
}
Location loc = this.GetTeam(p).GetSpawn();
net.minecraft.server.v1_8_R2.PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(1005,new BlockPosition(loc.getX(),loc.getY(),loc.getZ()),2258,false);
((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }
    }
}

HashMap<Player,Boolean> musics = new HashMap();

public void openToggleMusic(Player pe){
Location loc = pe.getLocation();
if(!musics.containsKey(pe)){
    musics.put(pe, true);
}

    if(musics.get(pe)==true){

net.minecraft.server.v1_8_R2.PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(1005,new BlockPosition(loc.getX(),loc.getY(),loc.getZ()),0,true);
net.minecraft.server.v1_8_R2.PacketPlayOutWorldEvent packet3 = new PacketPlayOutWorldEvent(1005,new BlockPosition(loc.getX(),loc.getY(),loc.getZ()),0,false);

((CraftPlayer)pe).getHandle().playerConnection.sendPacket(packet); 
((CraftPlayer)pe).getHandle().playerConnection.sendPacket(packet3); 
pe.sendMessage(Colorizer.Color("&aDesactivaste la musica"));

musics.put(pe, false);
    }else{
net.minecraft.server.v1_8_R2.PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(1005,new BlockPosition(loc.getX(),loc.getY(),loc.getZ()),2258,false);
((CraftPlayer)pe).getHandle().playerConnection.sendPacket(packet); 
pe.sendMessage(Colorizer.Color("&aActivaste la musica"));
musics.put(pe, true);
    }
}

@EventHandler
public void onPistonFlip(BlockPistonRetractEvent event){
event.setCancelled(true);
}
    
public void openStatistics(Player pe){
 Inventory inv = Bukkit.createInventory(null, 27, Colorizer.Color(name10));
      try {
ItemStack it1 = this.makeItem(Material.GOLD_INGOT, 1, (short)0, "&aPrimer Lugar: &c"+this.getValue(pe, columnType.FirstPlace), null);
ItemStack it2 = this.makeItem(Material.EMERALD, 1, (short)0, "&aSegundo Lugar: &c"+this.getValue(pe, columnType.SecondPlace), null);
ItemStack it3 = this.makeItem(Material.DIAMOND, 1, (short)0, "&aTercer Lugar: &c"+this.getValue(pe, columnType.ThirdPlace), null);
ItemStack it4 = this.makeItem(Material.PAPER, 1, (short)1, "&aPartidas jugadas: &c"+this.getValue(pe, columnType.Played), null);
inv.setItem(10, it1);
inv.setItem(12, it2);
inv.setItem(14, it3);
inv.setItem(16, it4);

      } catch (SQLException ex) {
          Logger.getLogger(BuildBattle.class.getName()).log(Level.SEVERE, null, ex);
      }
 pe.openInventory(inv);
 pe.updateInventory();
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
    
    if(name.equalsIgnoreCase(name1)){
        this.openWeather(pe);
         event.setCancelled(true);
    }
    else if(name.equalsIgnoreCase(name2)){
        this.openTime(pe);
         event.setCancelled(true);
    }
    else if(name.equalsIgnoreCase(name3)){
        this.openBiome(pe);
         event.setCancelled(true);
    }
    else if(name.equalsIgnoreCase(name4)){
    if(event.getCursor().getType() == Material.AIR || event.getCursor() == null){
        return;
    }
    Player p = (Player)event.getWhoClicked();
    if(!event.getCursor().getType().isSolid()){
        p.sendMessage(Colorizer.Color("&cSolo bloques solidos!"));
        event.setCancelled(true);
        p.closeInventory();
        return;
    }
    this.flormap.put(p, event.getCursor());
    
this.ChangeFloor(this.GetTeam(p), event.getCursor());
    p.closeInventory();
     event.setCancelled(true);
    
    }
    else if(name.equalsIgnoreCase(name5)){
        this.openSkulls(pe); 
        event.setCancelled(true);
        
    }
    else if(name.equalsIgnoreCase(name6)){
        this.openParticles(pe);
         event.setCancelled(true);
    }
    else if(name.equalsIgnoreCase(name7)){
        this.openBannerBaseColor(pe);
        event.setCancelled(true);
    }
    else if(name.equalsIgnoreCase(name8)){
        this.openSaveloadout(pe);
         event.setCancelled(true);
    }
    else if(name.equalsIgnoreCase(name9)){
        this.openToggleMusic(pe);
         event.setCancelled(true);
    }
    else if(name.equalsIgnoreCase(name10)){
        this.openStatistics(pe);
         event.setCancelled(true);
    }else{
        return;
    }
    
    event.setCancelled(true);
}

@EventHandler(priority=EventPriority.HIGHEST)
public void onCombust(EntityCombustEvent event){
    event.setCancelled(true);
}

@EventHandler(priority=EventPriority.HIGHEST)
public void onFire(BlockBurnEvent event){
    event.setCancelled(true );
}    
@EventHandler(priority=EventPriority.HIGHEST)
public void onFire(BlockSpreadEvent event){
    event.setCancelled(true );
}    

@EventHandler(priority=EventPriority.HIGHEST)
public void onFire(ExplosionPrimeEvent event){
    event.setCancelled(true );
}    

@EventHandler(priority=EventPriority.HIGHEST)
public void onFire(EntityChangeBlockEvent event){
    event.setCancelled(true );
}    
@EventHandler
public void onChanta(GameStateChangeEvent event){    

    if(event.GetState() != GameState.Prepare){
        return;
    }
try{
   this.psize = this.GetPlayers(true).size();
    for(GameTeam te : this.GetTeamList()){
       double x = te.GetSpawn().getX();
       double y = te.GetSpawn().getY();
       double z = te.GetSpawn().getZ();
       World we = te.GetSpawn().getWorld();

        for(int a = 0;a < 50;a++){
if(y-a > 0){
if((new Location(we,x,y-a,z)).getBlock().getType().equals(Material.STAINED_CLAY)){
        floor.put(te, y-a);    
        this.putSize(te, new Location(we,x,y-a,z));
   }
  }
 }}}catch(Exception e){
     e.printStackTrace();
 

}
}


@EventHandler
public void onChan(GameStateChangeEvent event){    

    if(event.GetState() != GameState.Live){
        return;
    }
try{
    for(Player p : this.GetPlayers(true)){
      p.setGameMode(GameMode.CREATIVE);
      p.getInventory().setItem(0, new ItemStack(Material.DIRT));
      p.getInventory().setItem(1, new ItemStack(Material.WOOD));
      p.getInventory().setItem(2, new ItemStack(Material.STONE));
        try {
            String str = this.getValue(p, columnType.Items);
            if(str.length() > 4){
          p.getInventory().clear();
      p.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Options "+C.cDGray+"(Right Click)", new String[] { "", ChatColor.RESET + "Click-Derecho", ChatColor.RESET + "para abrir el menu." }));
          if(str.contains(";")){
          for(String tem : str.split(";")){
             p.getInventory().addItem(packetItem.deserialize(tem));
          }
          }else{
             p.getInventory().addItem(packetItem.deserialize(str));
          }
            }

        } catch (SQLException ex) {
            Logger.getLogger(BuildBattle.class.getName()).log(Level.SEVERE, null, ex);
        }

      p.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Options "+C.cDGray+"(Right Click)", new String[] { "", ChatColor.RESET + "Click-Derecho", ChatColor.RESET + "para abrir el menu." }));
p.updateInventory();
    }}catch(Exception e){
        e.printStackTrace();
    }

}

HashMap<Player,ItemStack> flormap = new HashMap();

public void openOptions(Player pe){
Inventory inv = Bukkit.createInventory(null, 54, Colorizer.Color("&aOpciones de Plot"));
        ItemStack itt4 = it4.clone();
        if(flormap.containsKey(pe)){
            itt4.setType(flormap.get(pe).getType());
            itt4.setDurability(flormap.get(pe).getDurability());
         }
inv.setContents(new ItemStack[]{
    
    null,null,null,null,null,null,null,null,null,
    null,null,it1 ,it2 ,null,it3 ,it4,null,null,
    null,null,null,it5 ,it6 ,it7 ,null,null,null,
    null,null,null,null,null,null ,null,null,null,
    null,null,null,it8 ,it9 ,it10,null,null,null,
    null,null,null,null,null,null,null,null,null,
});
    pe.openInventory(inv);
    pe.updateInventory();
}

HashMap<Player,List<StaticPS>> particles = new HashMap();

public void useParticle(Player player, Location loc, PCType type){
  if(!particles.containsKey(player)){
      particles.put(player, new ArrayList());
  }
  List<StaticPS> list = particles.get(player);
  if(list.size() >= 20){
     player.sendMessage(Colorizer.Color("Maximum amount of Particles placed: "+20));
  return;
  }
  list.add(new StaticPS(player,loc,type));
  player.sendMessage(Colorizer.Color("&a"+type.getName()+" placed at: X:"+loc.getX()+" Y:"+loc.getY()+" Z:"+loc.getZ()));
}

@EventHandler
public void onParticleUpdate(UpdateEvent event){
    if(event.getType() != UpdateType.FASTEST){
        return;
    }
    if(particles.isEmpty()){
        return;
    }
    for(List<StaticPS> pcs : particles.values()){
        if(pcs.isEmpty()){
            continue;
        }
        for(StaticPS pc : pcs){
            pc.rePlay();
        }
    }
}


 @EventHandler(priority=EventPriority.HIGHEST)
 public void onPlace(BlockPlaceEvent event){
     if(event.getBlock()==null){
         return;
     }
     if(event.getPlayer().getItemInHand()==null){
         return;
     }
     if(!event.getPlayer().getItemInHand().hasItemMeta()){
         return;
     }
     if(!event.getPlayer().getItemInHand().getItemMeta().hasLore()){
         return;
     }
     
     List<String> lore = event.getPlayer().getItemInHand().getItemMeta().getLore();
     
     if(lore.get(0).equalsIgnoreCase(Colorizer.Color("&7Particle Effect"))){
event.setCancelled(true);
}
}

 

@EventHandler(priority=EventPriority.HIGHEST)
public void onOptionsInteract(PlayerInteractEvent event){
    if(!this.IsLive()){
        return;
    }
    if(this.finished == true){
        return;
    }
    if(event.getPlayer().getItemInHand() == null){
        return;
    }

Player pe = event.getPlayer();
ItemStack hand = pe.getItemInHand();

if(hand.getType() ==Material.NETHER_STAR && hand.hasItemMeta()){
if(!flormap.containsKey(event.getPlayer())){
    flormap.put(event.getPlayer(), new ItemStack(Material.STAINED_CLAY));
}
this.openOptions(event.getPlayer());
}

if(hand.hasItemMeta()){
if(hand.getItemMeta().hasLore() && hand.getItemMeta().hasDisplayName()){
String name = hand.getItemMeta().getDisplayName();
List<String> lore = hand.getItemMeta().getLore();

if(lore.get(0).equalsIgnoreCase(Colorizer.Color("&7Particle Effect"))){
for(PCType pc : PCType.values()){
if(name.contains(pc.getName())){
this.useParticle(pe, pe.getLocation(), pc);
event.setCancelled(true);
}
}
}

}
}



}



public void ChangeFloor(GameTeam te, ItemStack item){
    if(te == null){
        return;
    }
Location loc = new Location(te.GetSpawn().getWorld(),te.GetSpawn().getBlockX(),floor.get(te)+1,te.GetSpawn().getBlockZ());
Location dist1 = new Location(te.GetSpawn().getWorld(),this.CornerMinX.get(te.GetName()),floor.get(te)+1,te.GetSpawn().getBlockZ());
Location dist2 = new Location(te.GetSpawn().getWorld(),this.CornerMaxX.get(te.GetName()),floor.get(te)+1,te.GetSpawn().getBlockZ());
double distance = dist1.distance(dist2);

for(int z = 0;z < distance;z++)
{
    for(int x = 0;x < distance;x++){
    Location loc1 = loc.clone().add(x, 0, z);
    Location loc1B = loc.clone().add(x, -1, z);
if(!this.isOut(te, loc1)){
    loc1.getBlock().setTypeIdAndData(item.getTypeId(), (byte) item.getDurability(), true);    
    loc1B.getBlock().setTypeIdAndData(item.getTypeId(), (byte) item.getDurability(), true);
}
Location loc2 = loc.clone().subtract(x, 0, z);
Location loc2B = loc.clone().subtract(x, 1, z);
if(!this.isOut(te, loc2)){
 loc2.getBlock().setTypeIdAndData(item.getTypeId(), (byte) item.getDurability(), true);
 loc2B.getBlock().setTypeIdAndData(item.getTypeId(), (byte) item.getDurability(), true);
}

Location loc3 = loc.clone().add(-x, 0, z);
Location loc4 = loc.clone().add(x, 0, -z);

Location loc3B = loc.clone().add(-x, -1, z);
Location loc4B = loc.clone().add(x, -1, -z);
if(!this.isOut(te, loc3)){
 loc3.getBlock().setTypeIdAndData(item.getTypeId(), (byte) item.getDurability(), true);
 loc3B.getBlock().setTypeIdAndData(item.getTypeId(), (byte) item.getDurability(), true);
}
if(!this.isOut(te, loc4)){
 loc4.getBlock().setTypeIdAndData(item.getTypeId(), (byte) item.getDurability(), true);
 loc4B.getBlock().setTypeIdAndData(item.getTypeId(), (byte) item.getDurability(), true);
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
public void onNPlace(BlockPlaceEvent event){
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

HashMap<String,Double> CornerMinX = new HashMap();
HashMap<String,Double> CornerMaxX = new HashMap();

HashMap<String,Double> CornerMinZ = new HashMap();
HashMap<String,Double> CornerMaxZ = new HashMap();

public boolean isOut(GameTeam te, Location loca ){
    if(te == null){
 
        return true;
    }
    if(te.GetPlayers(true).isEmpty()){

        return true;
    }



    double MinX = loca.getX()-13;
    double MaxX = loca.getX()+13;
    double MinZ = loca.getZ()-13;
    double MaxZ = loca.getZ()+13;
    

    
    try{
    MinX = (double)this.CornerMinX.get(te.GetName());
    MaxX = (double)this.CornerMaxX.get(te.GetName());
    MinZ = (double)this.CornerMinZ.get(te.GetName());
    MaxZ = (double)this.CornerMaxZ.get(te.GetName());}
    catch(Exception e){
}
 
   
    

try{
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
    }}catch(Exception e){

    e.printStackTrace();
        return true;
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
public void updateWeatherOnFinish(UpdateEvent event){
    if(event.getType() !=  UpdateType.SEC){
        return;
    }
        if(!this.finished){
            return;
        }   
 if(vota.get(index)==null){
     return;
 }
Player pe = vota.get(index);

int wet = weathers.get(pe);
Location loc = pe.getLocation();
PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SNOW_SHOVEL,true,(float)loc.getX(),(float)loc.getY(),(float)loc.getZ(),7F,20F,7F,0F,500,0);

for(Player p : Bukkit.getOnlinePlayers()){
    this.updateTime(p, pe);
    if(p.getWorld()!=pe.getWorld()){
        continue;
    }
if(wet >=2){
    if(wet == 2){

((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);

    }
    p.setPlayerWeather(WeatherType.CLEAR);
}else{
    if(wet==0){
EntityLightning et = ((CraftLightningStrike)pe.getWorld().spawnEntity(pe.getLocation().add(r.nextInt(8),r.nextInt(16),r.nextInt(8)), EntityType.LIGHTNING)).getHandle();
et.setOnFire(0);
et.isEffect = true;

et.isSilent = false;
PacketPlayOutSpawnEntityWeather packet2 =  new PacketPlayOutSpawnEntityWeather(et);

((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet2);
et.die();
    }
    p.setPlayerWeather(WeatherType.DOWNFALL);
}
}

}

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
            try{
            this.sendBiomeChange(pls, this.biomedata.get(p), false);
            }catch(Exception e){
                
            }
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
           if(!pls.isOnline()){
               continue;
           }
          if(place1 != null){
                pls.teleport(this.GetTeam(place1).GetSpawn());
                continue;}
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
    if(this.CornerMinX.containsKey(te.GetName())){
        return;
    }
    double locsize = 0;
    for(int a = 0;a < 99;a++){
        
     Location loca = new Location(loc.getWorld(),loc.getX(),loc.getY(),loc.getZ()+a);
     Location altern = new Location(loca.getWorld(),loca.getX(),loca.getY()+2,loca.getZ());

     
     if(loca.getBlock().getType().equals(Material.STAINED_CLAY) && altern.getBlock().getType().equals(Material.AIR)){
             locsize +=1;
       }else if(!CornerMinX.containsKey(te.GetName())){

          this.CornerMinX.put(te.GetName(), (double)(loc.getBlockX()-((int)locsize)));
          this.CornerMaxX.put(te.GetName(), (double)(loc.getBlockX()+((int)locsize)));
          
          this.CornerMinZ.put(te.GetName(), (double)(loc.getBlockZ()-((int)locsize)));
          this.CornerMaxZ.put(te.GetName(), (double)(loc.getBlockZ()+((int)locsize)));
break;          
         }else{
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


if(delayend != 10){
    return;
}
if(Bukkit.getOnlinePlayers().size() < 2){
        this.delayend = 5;
}
if(vota == null){
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

  }

  }
  
  for(Player paa : Bukkit.getOnlinePlayers()){
      paa.sendMessage(Colorizer.Color(MSGUtil.getLineSpacer()));
      paa.sendMessage("                                 ");
      paa.sendMessage("               Ganadores");
      if(place1!= null){
      paa.sendMessage(Colorizer.Color("&e                1st &7- &6"+total.get(place1)));
      paa.sendMessage(Colorizer.Color("&7                "+place1.getDisplayName()));
      try{
      this.addValue(paa, columnType.FirstPlace, "1", true);
      }catch(Exception e){ 
          
      }}
      
      if(place2!= null){
      paa.sendMessage(Colorizer.Color("&6                2nd &7- &6"+total.get(place2)));
      paa.sendMessage(Colorizer.Color("&7                "+place2.getDisplayName())); 
      try{
      this.addValue(paa, columnType.SecondPlace, "1", true);
      }catch(Exception e){ 
          
      }}
      
      if(place3!= null){
      paa.sendMessage(Colorizer.Color("&c                3rd &7- &6"+total.get(place3)));
      paa.sendMessage(Colorizer.Color("&7                "+place3.getDisplayName())); 
      try{
      this.addValue(paa, columnType.ThirdPlace, "1", true);
      }catch(Exception e){ 
          
      }}
      
        paa.sendMessage("                                 ");
        paa.sendMessage(Colorizer.Color(MSGUtil.getLineSpacer()));
        try{
      this.addValue(paa, columnType.Played, "1", true);
      }catch(Exception e){ 
          
      }
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
if(delayend > 0 && delayend < 3){
    if(!Bukkit.getOnlinePlayers().isEmpty()){
        String lobby = this.Manager.getConfig().getString("BuildBattle.lobby");
        System.out.println("Sending all players to server: "+lobby);
        for(Player p : Bukkit.getOnlinePlayers()){
            this.Manager.GetPortal().SendPlayerToServer(p, lobby);
        }
    }
}
    if(delayend <= 0){

        SetState(Game.GameState.End);
        delayend = 10;
        Bukkit.getServer().shutdown();
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
String ls = Colorizer.Color("Tiempo: &a"+"0"+min+":");
String str = "0"+min+":";

if(time < 10){
    ls = ls+"0"+time;
    str = str+"0"+time;
}else{
    ls = ls+time;
    str = str+time;
}

lasttime = ls;




return str;
}

    java.sql.Connection data = null;
    boolean mysqlenabled = false;

    public void checkData(){
        if(!mysqlenabled){
            return;
        }

        if(data == null){
            openData();
        }else try {
            if(data.isClosed()){
                openData();
            }
        } catch (SQLException ex) {
       }
    }
    
@EventHandler(priority=EventPriority.HIGHEST)
public void onTarget(EntityTargetEvent event){
    event.setTarget(null);
    event.setCancelled(true);
}
    
@EventHandler
public void onMobMenu(PlayerInteractEntityEvent event){
    if(event.getRightClicked()==null){
    return;
}
    if(ets.isEmpty()){
        return;
    }
    Entity et = event.getRightClicked();
    if(!(et instanceof LivingEntity)){
    return;
    }
    LivingEntity ete = (LivingEntity)et;
    for(Player pe : ets.keySet()){
        for(EntMod em: ets.get(pe)){
            if(em.getEntity()==et){
              this.openMobIventory(pe, em);
              event.setCancelled(true);
            }
        }
    }
}
HashMap<Player,EntMod> openEntmod = new HashMap();

public HashMap<Player,EntMod> nextchat = new HashMap();

@EventHandler(priority=EventPriority.HIGHEST)
public void onMobRename(PlayerChatEvent event){
    if(!nextchat.containsKey(event.getPlayer())){
        return;
    }
    nextchat.get(event.getPlayer()).setCustomName(event.getMessage());
    nextchat.remove(event.getPlayer());
    event.getPlayer().sendMessage(Colorizer.Color("&aNuevo nombre: &r"+Colorizer.Color(event.getMessage())));
event.setCancelled(true);
}

@EventHandler
public void onMobInv(InventoryClickEvent event){
    if(event.getCurrentItem()==null){
        return;
    }
    if(event.getInventory().getName() ==null){
        return;
    }
    if(!"Opciones de Mob".equals(event.getInventory().getName())){
        return;
    }
    event.setCancelled(true);
    String name = event.getCurrentItem().getItemMeta().getDisplayName();
    if(name.equalsIgnoreCase(Colorizer.Color("&aDescongelar"))){
     openEntmod.get((Player)event.getWhoClicked()).setFreeze(false);
    event.getWhoClicked().sendMessage(Colorizer.Color("Descongelado"));
     event.getWhoClicked().closeInventory();
    
    }
    else if(name.equalsIgnoreCase(Colorizer.Color("&aCambiar nombre"))){
     this.nextchat.put((Player)event.getWhoClicked(), openEntmod.get((Player)event.getWhoClicked()));
     event.getWhoClicked().sendMessage(Colorizer.Color("Escribe el nombre"));
     event.getWhoClicked().closeInventory();
    
    }
    else if(name.equalsIgnoreCase(Colorizer.Color("&bCongelar"))){
     openEntmod.get((Player)event.getWhoClicked()).setFreeze(true);
    event.getWhoClicked().sendMessage(Colorizer.Color("&aCongelado"));
     event.getWhoClicked().closeInventory();
    
    }else if(name.equalsIgnoreCase(Colorizer.Color("&aAumentar tamaño"))){
     openEntmod.get((Player)event.getWhoClicked()).setAge(openEntmod.get((Player)event.getWhoClicked()).getAge()+1);
    event.getWhoClicked().sendMessage(Colorizer.Color("&aNuevo Tamaño: "+openEntmod.get((Player)event.getWhoClicked()).getAge()));

    }else if(name.equalsIgnoreCase(Colorizer.Color("&aReducir tamaño"))){
     openEntmod.get((Player)event.getWhoClicked()).setAge(openEntmod.get((Player)event.getWhoClicked()).getAge()-1);
    event.getWhoClicked().sendMessage(Colorizer.Color("&aNuevo Tamaño: "+openEntmod.get((Player)event.getWhoClicked()).getAge()));

    }else if(name.equalsIgnoreCase(Colorizer.Color("&aRemover Mob"))){
     EntMod em = openEntmod.get((Player)event.getWhoClicked());
     LivingEntity et = em.getEntity();
     if(ets.containsKey((Player)event.getWhoClicked())){
       for(EntMod emm : ets.get((Player)event.getWhoClicked())){
           if(emm==em){
        event.getWhoClicked().sendMessage(Colorizer.Color("&aRemovido"));
        ets.get((Player)event.getWhoClicked()).remove(emm);
               et.remove();
               event.getWhoClicked().closeInventory();
               break;
           }
       }
     }
    }
}
        

public void openMobIventory(Player pe, EntMod em){
    Inventory iv = Bukkit.createInventory(null, 9, "Opciones de Mob");
    this.openEntmod.put(pe, em);
    
    if(em.isFreezed()){
       iv.addItem(this.makeItem(Material.TORCH, 1, (short)0, "&aDescongelar", null));
    }else{
       iv.addItem(this.makeItem(Material.PACKED_ICE, 1, (short)0, "&bCongelar", null));        
    }
    if(em.isAgeable()){
       iv.addItem(this.makeItem(Material.SLIME_BALL, 1, (short)0,"&aAumentar tamaño", null));
       iv.addItem(this.makeItem(Material.SLIME_BALL, 1, (short)0,"&aReducir tamaño", null));
    
    }
    iv.addItem(this.makeItem(Material.NAME_TAG, 1, (short)0, "&aCambiar Nombre", null));
    
    
    iv.addItem(this.makeItem(Material.BARRIER, 1, (short)0, "&aRemover Mob", null));
    pe.openInventory(iv);
    pe.updateInventory();
    
}
    
HashMap<Player,List<EntMod>> ets = new HashMap();

@EventHandler(priority=EventPriority.HIGHEST)
public void onCustomMobSpawn(CreatureSpawnEvent event){
    Player target = null;
    double targetdist = -1;
for(Player pe : Bukkit.getOnlinePlayers()){
    if(pe.getWorld() != event.getEntity().getWorld()){
       continue;
    }
    Location loc1 = pe.getLocation();
    Location loc2 = event.getLocation();
   if(target==null){
    if(loc1.distanceSquared(loc2) < 50)
    {
      target = pe;  
    }}else{
       if(loc1.distanceSquared(loc2) < targetdist){
          pe =target;
       }
   }
           }
if(target==null)
{
 event.setCancelled(true);
return;
}

if(!ets.containsKey(target)){
    ets.put(target, new ArrayList());
}

if(ets.get(target).size() > 14){
    target.sendMessage(Colorizer.Color("&cSolo puedes spawnear hasta 15 mobs"));
    event.setCancelled(true);
    return;
}
event.setCancelled(false);
CraftLivingEntity cre = (CraftLivingEntity)event.getEntity();
cre.setVelocity(new Vector(0,0,0));
Location loc = event.getLocation();

cre.getHandle().setPosition(loc.getX(), loc.getY(), loc.getZ());

EntMod entmod = new EntMod(event.getEntity(),loc,true,false,-1);
ets.get(target).add(entmod);

}    


@EventHandler
public void updateMobs(UpdateEvent event){
    if(event.getType() != UpdateType.TICK){
        return;
    }
    if(ets.isEmpty()){
        return;
    }
    for(List<EntMod> ete : ets.values()){
       for(EntMod ed : ete){
        if(ed.hasCustomName()){
        ed.getEntity().setCustomNameVisible(true);
        ed.getEntity().setCustomName(ed.getCustomName());
        }
        if(ed.isAgeable()){
         if(ed.getEntity() instanceof Slime){
             ((Slime)ed.getEntity()).setSize(ed.getAge());
         }
         if(ed.getEntity() instanceof MagmaCube){
             ((MagmaCube)ed.getEntity()).setSize(ed.getAge());
         }
        }
        EntityLiving ea = ((CraftLivingEntity)ed.getEntity()).getHandle();

        if(!ed.isFreezed()){
        ea.noclip=false;
        continue;
        }
        Location loc = ed.getForcedLocation();
        ed.getEntity().setVelocity(new Vector(0,0,0));
        ea.motX = 0;
        ea.motY=0;
        ea.motZ=0;

        ea.lastX=0;
        ea.lastY=0;
        ea.lastZ=0;
        ea.updateEffects=false;
        ea.yaw = loc.getYaw();
        ea.pitch=loc.getPitch();
        ea.lastYaw =  loc.getYaw();
        ea.lastPitch = loc.getYaw();
        ea.noclip=true;

        ea.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
ea.getBukkitEntity().getLocation().setDirection(loc.getDirection());
ea.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(),loc.getYaw(), loc.getPitch());
ea.positionChanged=true;
        }}
    
}
    
    
    @EventHandler
    public void insertStats(PlayerJoinEvent event){
if(!mysqlenabled){
            return;
        }
        Player p = event.getPlayer();
String sqlinsert = "INSERT INTO `"+table+"` ("
        + "Name,"
        + "UUID,"
        + columnType.FirstPlace.getName()+","
        + columnType.SecondPlace.getName()+","
        + columnType.ThirdPlace.getName()+","
        + columnType.Played.getName()+","
        + columnType.Items.getName()
        +") \n" +
"  SELECT '"+p.getName()+"','"+p.getUniqueId().toString()+"','0','0','0','0','null' FROM dual\n" +
"WHERE NOT EXISTS \n" +
"  (SELECT UUID FROM "+table+" WHERE UUID='"+p.getUniqueId().toString()+"');";
    
        try {
            this.getStatement().execute(sqlinsert);
            System.out.println("Record inserted");
        } catch (SQLException ex) {
            System.out.println("Record not inserted");
        }
    }
    
  
private void createTable() throws SQLException {

    String sqlCreate =
"CREATE TABLE IF NOT EXISTS `"+table+"` ("
            + "`ID` int(11) NOT NULL auto_increment"
            +",`Name` varchar(255) NOT NULL"
            +",`UUID` varchar(255) NOT NULL"
            +",`"+columnType.FirstPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.SecondPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.ThirdPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Played.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Items.getName()+"` text(4999) NOT NULL"
            +",PRIMARY KEY  (`ID`)"
            +
            ")";

    this.getStatement().execute(sqlCreate);

}

java.sql.Statement Statement = null;
public java.sql.Statement getStatement(){
if(!mysqlenabled){
            return null;
        }
        try {
            if(Statement == null){
                Statement = data.createStatement();
            }else if(Statement.isClosed()){
                Statement = data.createStatement();
            }
            
            
            return Statement;
        } catch (SQLException ex) {
         }
        return Statement;
}

 public void savetomysql(Player pe)
{
Inventory iv = pe.getInventory();
String str = "";

for(int a = 0; a < 9;a++){
   if(iv.getItem(a) == null){
       continue;
   }
str = str+packetItem.serialize(iv.getItem(a))+";";

}
if(str.length() > 2){
str = str.substring(0, str.length()-2);
}
pe.closeInventory();
pe.sendMessage(Colorizer.Color("&aGuardado como tu barra default!"));
if(str.isEmpty()){
    return;
}

this.setValue(pe, columnType.Items, str);

}
 
 
    public void setValue(Player p, columnType type, String value){
if(!mysqlenabled){
    return;
}
        if(p == null){
            System.out.println("Error trying to record player kill, player is null");
            return;
        }
        if(p.getName() == null){
            System.out.println("Error trying to record player kill, name is null");
        return;
        }
try{



    
String pay = "UPDATE "+table+" SET "+type.getName()+" ='"+value+"' WHERE UUID = '"+p.getUniqueId()+"'";

     getStatement().execute(pay);  
}catch(Exception e){
    
}}
 
    public void addValue(Player p, columnType type, String value, boolean isInteger){
if(!mysqlenabled){
    return;
}
        if(p == null){
            System.out.println("Error trying to record player kill, player is null");
            return;
        }
        if(p.getName() == null){
            System.out.println("Error trying to record player kill, name is null");
        return;
        }
try{
    String val = this.getValue(p, type);

    if(isInteger){
        
    int cint = Integer.parseInt(val);
    cint+= Integer.parseInt(value);
    val = ""+cint;
    }else{
        String[] vals = val.split("-");
        for(String str : vals){

        }
        val = vals[0]+value+"-"+vals[1];
    }
if(val.contains(";;")){
    val = val.replace(";;", ";");
}
    
String pay = "UPDATE "+table+" SET "+type.getName()+" ='"+val+"' WHERE UUID = '"+p.getUniqueId()+"'";

     getStatement().execute(pay);  
}catch(Exception e){
    
}}
    
HashMap<String,HashMap<columnType,String>> cvalues = new HashMap();
HashMap<String,HashMap<columnType,Long>> cvaluesdelay = new HashMap();

public String getValue(Player p, columnType type) throws SQLException{
if(!cvalues.containsKey(p.getName())){
    cvalues.put(p.getName(), new HashMap());
}    
if(!cvaluesdelay.containsKey(p.getName())){
    cvaluesdelay.put(p.getName(), new HashMap());
}
if(cvaluesdelay.get(p.getName()).containsKey(type)){
    if(cvaluesdelay.get(p.getName()).get(type) > System.currentTimeMillis()){
        return cvalues.get(p.getName()).get(type);
    }
}
        

    if(!this.mysqlenabled){return null;}
        this.checkData();
        if(p == null){return null;}
        if(p.getName() == null){return null;}
ResultSet res = null;

res = getStatement().executeQuery("SELECT * FROM "+table+" WHERE UUID = '"+p.getUniqueId().toString()+"'");
String value = null;

try{res.next(); 
value = res.getString(type.getName());
cvalues.get(p.getName()).put(type, value);
cvaluesdelay.get(p.getName()).put(type, System.currentTimeMillis()+5000);

res.close();} catch(Exception e){
}

   return value;
    }

  centerManager plugin = this.Manager;
  
    String host = "127.0.0.1";
    String port = "3306";
    String db = "stats";
    String user = "root";
    String pwd = "test";
    String table = "test";
    
 public final void openData(){

    try {
    MySQL MySQL = new MySQL(plugin.GetPlugin(), host, port, db, user, pwd);
    
        data = MySQL.openConnection();
    } catch (SQLException | ClassNotFoundException ex) {
  ex.printStackTrace();
    }

    
}


     public void init(){
        System.out.println("Starting BuildBattle stats...");

        host = plugin.getConfig().getString("BuildBattle.mysql.host");
        port = plugin.getConfig().getString("BuildBattle.mysql.port");
        db = plugin.getConfig().getString("BuildBattle.mysql.db");
        user = plugin.getConfig().getString("BuildBattle.mysql.user");
        pwd = plugin.getConfig().getString("BuildBattle.mysql.pwd");
        table = plugin.getConfig().getString("BuildBattle.mysql.table");

        try{
        this.openData();    
        mysqlenabled = true;
        }catch(Exception e){
            this.mysqlenabled = false;
            System.out.println("[BuildBattle] Stats Disabled");
            return;
        }
        
        try {
            this.createTable();
        } catch (SQLException ex) {
ex.printStackTrace();
System.out.println("[BuildBattle] Cannot insert the Table");
            return;
        }
        System.out.println("BuildBattle started.");
    }


  
  }

  
  
  
  

  

 


