package me.peacefulmen.factionstournament;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Rel;
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class Core
  extends JavaPlugin
  implements Listener

{

  Random r = new Random();

    FileConfiguration config;
  @Override
  public void onEnable()
  {
    saveDefaultConfig();
      config = this.getConfig();
      this.loadFactions();
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(this, this);
      Updater upd = new Updater(this);

  }

    @Override
    public void onDisable()
    {
        this.saveConfig();
        HandlerList.unregisterAll((Listener) this);
        getServer().getLogger().log(Level.INFO, "MineUltra Hub [v{0}] has been disabled!", getDescription().getVersion());
    }


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

    String defwarname = "Desafiar Faction";
    String prewarname = "Jugadores vs Jugadores";
    String menuname = "Opciones de Faction";

    List<FactionData> factions = new ArrayList();

    public void loadFactions(){
        this.factions.clear();

        for(String id : config.getConfigurationSection("FactionsTournament").getKeys(false)){
            String factionid = id.substring(2);

            boolean inscripto = config.getBoolean("FactionsTournament."+id+".inscripto");
            String tag = config.getString("FactionsTournament."+id+".tag");
            String leader = config.getString("FactionsTournament."+id+".leader");
            int elo = config.getInt("FactionsTournament."+id+".elo");
            List<String> players = new ArrayList();
            for(String s : config.getString("FactionsTournament."+id+".players").split(",")){
                players.add(s);
            }
            factions.add(new FactionData(this,factionid,inscripto,tag,leader,elo,players));
        }
    }

    List<BattleData> battles = new ArrayList();

            @EventHandler
            public void onMenu(final InventoryClickEvent event){
                if(event.getInventory()==null){
                    return;
                }
                if(event.getCurrentItem()==null){
                    event.setCancelled(true);
                    return;
                }
                if(event.getInventory().getName().equals(menuname)){
                    if(event.getSlot()==12){
                        //open factions last matches
                    }
                    else if(event.getSlot()==13){
                        this.openFactions((Player)event.getWhoClicked());
                    }else if(event.getSlot()==14){
                        this.onInscribirse((Player)event.getWhoClicked());
                    }
                    event.setResult(Event.Result.DENY);
                    event.setCancelled(true);
                }
                else if(event.getInventory().getName().equals(defwarname)){
                    FactionData fdenemy = null;
                    FactionData fdally = null;
                    for(FactionData fd : this.factions){
                        if(fd.getMassiveFaction()==null){
                            continue;
                        }
                        if(fd.getBanner().equals(event.getCurrentItem())){
                        fdenemy = fd;
                        }
                        if(fd.getMassiveFaction().getFPlayerLeader().getName().equalsIgnoreCase(event.getWhoClicked().getName())){
                            fdally = fd;
                        }
                    }
                    if(fdenemy != null && fdally != null) {
                     if(fdenemy == fdally){
                         event.getWhoClicked().sendMessage(Colorizer.Color("&cNo puedes desafiar tu propia faction!"));
                         event.getWhoClicked().closeInventory();
                     }else{
                        this.openWar((Player) event.getWhoClicked(), fdally, fdenemy);
                    }}

                    event.setCancelled(true);
                }
                else if(event.getInventory().getName().contains(" vs ")){
                    String[] fs = event.getInventory().getName().split(" vs ");
                    String f1 = fs[0];
                    String f2 = fs[1];
                    FactionData fd1 = null;
                    FactionData fd2 = null;
                    for(FactionData fd : this.factions){
                        if(fd.isInscripto()){
                            if(fd.getTag().equals(f1)){
                                fd1 = fd;
                            }else if(fd.getTag().equals(f2)){
                                fd2 = fd;
                            }
                        }
                    }
                    if(fd1 == null ){
                        System.out.println("fd1 is null");
                        return;
                    }if(fd2 == null ){
                        System.out.println("fd2 is null");
                        return;
                    }
                    int size = -1;
                    if(event.getSlot()==12){
                    size = 4;
                    }
                    else if(event.getSlot()==13){
                    size = 5;
                    }else if(event.getSlot()==14){
                    size = 6;
                    }
                    if(size != -1){
                        for(BattleData bd : battles){
                            if(bd.getFactionA().equals(fd1) || bd.getFactionB().equals(fd1)
                         ||   bd.getFactionA().equals(fd2) || bd.getFactionB().equals(fd2)){
                               event.getWhoClicked().sendMessage(Colorizer.Color("&cYa has enviado una peticion, espera a que expire."));
                                event.setCancelled(true);
                                return;
                            }
                        }
                        battles.add(new BattleData(this,fd1,fd2,size));
                    }
                    event.setCancelled(true);
                }
            }

    @EventHandler
    public void onUpdate(UpdateEvent event){
        if(event.getType() != UpdateType.SEC3){
            return;
        }
        int max = battles.size();
        for(int a = 0; a < max;a++){
            if(battles.get(a).hasExpired()){
                battles.remove(a);
                --max;--a;}
            else if(!battles.get(a).isPlaying()){
                BattleData bd = battles.get(a);
                if(bd.getFactionA().getMassiveFaction() == null || bd.getFactionB().getMassiveFaction() ==null){
                    break;
                }
                if(!bd.isFactionAReady()){
                    bd.getFactionA().getMassiveFaction().getFPlayerLeader().sendMessage(Colorizer.Color("&aUsa &e/ft aceptar &apara aceptar la Batalla contra la faction "+bd.getFactionB().getTag()));
                }
                if(!bd.isFactionBReady()){
                    bd.getFactionB().getMassiveFaction().getFPlayerLeader().sendMessage(Colorizer.Color("&aUsa &e/ft aceptar &apara aceptar la Batalla contra la faction "+bd.getFactionA().getTag()));
                }
            }
        }

    }

    public boolean isInscripto(String faction){
        try{
            if(this.config.getBoolean("FactionsTournament."+"ID"+faction+".inscripto")){
                return true;
            }else{
                return false;
            }

        }catch(Exception e){
            return false;
        }
    }

    int tournamentDays = 0;

    public void addFaction(String id, Faction fac){
        this.config.set("FactionsTournament."+"ID"+id+".inscripto",true);
        this.config.set("FactionsTournament."+"ID"+id+".tag",fac.getTag());
        this.config.set("FactionsTournament."+"ID"+id+".leader",fac.getFPlayerLeader().getName());
        this.config.set("FactionsTournament."+"ID"+id+".elo",100);
        this.config.set("FactionsTournament."+"ID"+id+".players",fac.getFPlayerLeader().getName());
        this.saveConfig();
        this.loadFactions();

    }

    public void onInscribirse(Player p){
        FPlayer fp = FPlayers.i.get(p);
        if(tournamentDays >= 15){
            p.sendMessage(Colorizer.Color("&cLas inscripciones estan cerradas, espera al siguiente torneo en "+(30-tournamentDays)));
        }
        else if(!fp.getRole().isAtLeast(Rel.LEADER)){
            p.sendMessage(Colorizer.Color("&cSolo el Lider puede inscibir su faction en este torneo!"));
        }else if(isInscripto(fp.getFaction().getId())){
            p.sendMessage(Colorizer.Color("&aTu faction ya esta inscripta en el torneo de este mes!"));
        }else try {
            if(Economy.hasEnough(p.getName(), BigDecimal.valueOf(1000000))){
                Economy.substract(p.getName(), BigDecimal.valueOf(1000000));
                this.addFaction(fp.getFactionId(),fp.getFaction());
                p.sendMessage(Colorizer.Color("&aFelicidades, hemos inscripto tu faction en el torneo!"));
            }else{
                p.sendMessage(Colorizer.Color("&cDinero insuficiente necesitas $1.000.000 para inscribir tu faction!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ft")){
            if(args.length <= 0){
                sender.sendMessage(Colorizer.Color("&b&m--------&b&nComandos&b&m--------"));
                return true;
            }
            if(!(sender instanceof Player)){
                return false;
            }
            Player p = ((Player)sender);

            if(args[0].equalsIgnoreCase("inscribirse")){
                this.onInscribirse(p);
return true;
            }
            if(args[0].equalsIgnoreCase("top")){
                LinkedHashMap<FactionData,Integer> top = new LinkedHashMap();
                for(FactionData fd : factions){
                    top.put(fd,fd.getElo());
                }
                top = (LinkedHashMap<FactionData, Integer>) sortByValue(top);

                sender.sendMessage(Colorizer.Color("&bFactions Top"));
                sender.sendMessage(Colorizer.Color("&b&m-------------------------"));
                int toppos = 1;
                for(FactionData fd : top.keySet()){
                    sender.sendMessage(Colorizer.Color("&a"+toppos+". &d"+fd.getTag()+" &f- &e"+fd.getElo()));
                toppos++;
                    if(toppos > 10){
                        break;
                    }
                }
                sender.sendMessage(Colorizer.Color("&b&m-------------------------"));
                return true;
            }
            if(args[0].equalsIgnoreCase("war")){
                //this.openWar((Player)sender);
                return true;
            }
            if(args[0].equalsIgnoreCase("menu")){
                this.openMenu((Player)sender);
                return true;
            }

            else{
                sender.sendMessage(Colorizer.Color("&b&m-----&cComandos&b&m-----"));


                return true;}
        }
        return false;
    }

    public void openMenu(Player p){
        Inventory iv = Bukkit.createInventory(null,27,menuname);
        for(int a = 0; a < iv.getSize();a++){
            iv.setItem(a,makeItem(Material.STAINED_GLASS_PANE,1,(short)6,"    ",null));
        }
        iv.setItem(12,makeItem(Material.WRITTEN_BOOK,1,(short)0,"&aHistorial de Batallas",null));
        iv.setItem(13,makeItem(Material.DIAMOND_SWORD,1,(short)0,"&cDesafiar Faction",null));
        iv.setItem(14,makeItem(Material.BOOKSHELF,1,(short)0,"&aInscribirse",null));
        p.openInventory(iv);
        p.updateInventory();
    }

    public void openWar(Player p,FactionData fd,FactionData fdenemy){
        Inventory iv = Bukkit.createInventory(null,27,fd.getTag()+" vs "+fdenemy.getTag());
        for(int a = 0; a < iv.getSize();a++){
            iv.setItem(a,makeItem(Material.STAINED_GLASS_PANE,1,(short)6,"    ",null));
        }
        iv.setItem(12,makeItem(Material.STAINED_CLAY,1,(short)6,"&a4vs4",null));
        iv.setItem(13,makeItem(Material.STAINED_CLAY,1,(short)4,"&a5vs5",null));
        iv.setItem(14,makeItem(Material.STAINED_CLAY,1,(short)5,"&a6vs6",null));
        p.openInventory(iv);
        p.updateInventory();
    }

    public void openFactions(Player p){
        Inventory iv = Bukkit.createInventory(null,54,defwarname);
        for(int a = 0; a < iv.getSize();a++){
            iv.setItem(a,makeItem(Material.STAINED_GLASS_PANE,1,(short)6,"    ",null));
        }
        int i = 10;
        for(FactionData efe : this.factions){
            if((i+1)%9==0){
                i+=2;
            }
            if(efe.getMassiveFaction() != null && efe.getMassiveFaction().getFPlayerLeader().isOnline()){
            iv.setItem(i++,efe.getBanner());}
        }
        p.closeInventory();
        p.openInventory(iv);
        p.updateInventory();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}
