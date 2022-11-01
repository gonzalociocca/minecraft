/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.minelevel.mercado;

/**
 *
 * @author ciocca
 */

import me.gonzalociocca.minelevel.core.database.MySQL;
import me.gonzalociocca.minelevel.core.misc.Code;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class Main extends JavaPlugin implements Listener
{
    HashMap<String,PermissionAttachment> attachments = new HashMap();

	@Override
	public void onEnable()
	{
      PluginManager pm =  Bukkit.getPluginManager();
        pm.registerEvents(this,this);
        this.checkData();
        try {
            this.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
    public void onDisable()
    {

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
    private ItemStack addGlow(org.bukkit.inventory.ItemStack stack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = (net.minecraft.server.v1_8_R3.ItemStack) getField(CraftItemStack.asCraftCopy(stack), "handle");
        // Initialize the compound if we need to
        if (!nmsStack.hasTag()) {
            nmsStack.setTag(new NBTTagCompound());
        }
        NBTTagCompound compound = nmsStack.getTag();

        // Empty enchanting compound
        compound.set("ench", new NBTTagList());

        return CraftItemStack.asCraftMirror(nmsStack);
    }


    String host = "127.0.0.1";
    String port = "3306";
    String db = "minelevel";
    String user = "root";
    String pwd = "notevinomas123-";
    String table = "mercado";

    boolean enabled = true;
    java.sql.Connection data = null;


    @EventHandler(priority= EventPriority.HIGHEST)
    public void beforeJoin(PlayerLoginEvent event){
        this.insert(event.getPlayer().getName(),event.getPlayer().getUniqueId().toString());
    }


    public void checkData(){
        if(enabled == false){
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

    private void insert(String name, String uuid){
        String sqlinsert = "INSERT INTO `"+table+"` ("
                + tablecolumnType.Name.getName()+","
                + tablecolumnType.UUID.getName()+","
                + tablecolumnType.Inventory.getName()+","
                + tablecolumnType.History.getName()
                +") " +
                "  SELECT '"+name.toLowerCase()+"',"
                + "'"+uuid+"',"
                + "'"+tablecolumnType.Inventory.getDefault()+"',"
                + "'"+tablecolumnType.History.getDefault()+"'"
                + " FROM dual " +
                "WHERE NOT EXISTS " +
                "( SELECT Name FROM "+table+" WHERE Name='"+name.toLowerCase()+"' );";

        try {
            this.getStatement().execute(sqlinsert);
            System.out.println("Record inserted");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Record not inserted");
        }
    }

    public void removePlayerData(String name){
        if(this.playerdata.containsKey(name.toLowerCase())){
            this.playerdata.remove(name.toLowerCase());}
    }

    HashMap<String,StoreData> playerdata = new HashMap();

    public StoreData getPlayerData(String name){
        if(playerdata.containsKey(name.toLowerCase())){
            return playerdata.get(name.toLowerCase());
        }else{
            StoreData pd = new StoreData(name.toLowerCase(),this);
            if(pd != null){
                playerdata.put(name.toLowerCase(), pd);
                return pd;
            }
        }
        return null;
    }

    private void createTable() throws SQLException {
        if(enabled == false){
            return;
        }

        String sqlCreate =
                "CREATE TABLE IF NOT EXISTS `"+table+"` ("
                        + "`ID` int(11) NOT NULL auto_increment"
                        +",`"+tablecolumnType.Name.getName()+"` varchar(255) NOT NULL"
                        +",`"+tablecolumnType.UUID.getName()+"` varchar(255) NOT NULL"
                        +",`"+tablecolumnType.Inventory.getName()+"` text(4999) NOT NULL"
                        +",`"+tablecolumnType.History.getName()+"` text(4999) NOT NULL"
                        +",PRIMARY KEY  (`ID`)"
                        +
                        ")";


        this.getStatement().execute(sqlCreate);
    }

    java.sql.Statement Statement = null;

    public java.sql.Statement getStatement(){
        if(enabled == false){
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


    public final void openData(){
        if(enabled == false){
            return;
        }
        MySQL mysql = new MySQL(this, host, port, db, user, pwd);
        try {
            data = mysql.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void LoadPlayer(PlayerJoinEvent event){
           getPlayerData(event.getPlayer().getName());
    }

    @EventHandler(ignoreCancelled = true)
    public void UnloadPlayer(PlayerQuitEvent event){
        this.removePlayerData(event.getPlayer().getName());
    }

    LinkedHashMap<String,Integer> topmineria = null;

    public LinkedHashMap<String,Integer> getTopMineria() throws SQLException {
        if(topmineria != null){
            return topmineria;
        }
        else{
            ResultSet res = null;
            res = this.getStatement().executeQuery("SELECT * FROM "+this.table+" ORDER BY MineriaPts DESC LIMIT 10");
            topmineria = new LinkedHashMap();

            while(res.next()){
                topmineria.put(res.getString("Name"),res.getInt("MineriaPts"));}
        }
        topmineria = (LinkedHashMap)this.sortByValue((Map)topmineria);
        return topmineria;
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

        public ItemStack makeItem(Material mat, int amount, short dura, String name,List<String> lore){
            ItemStack it = new ItemStack(mat,amount,dura);
            ItemMeta meta = it.getItemMeta();
            if(name != null) {
                meta.setDisplayName(Code.Color(name));
            }
            if(lore != null){
                meta.setLore(lore);}

            it.setItemMeta(meta);
            if(!it.getType().name().toLowerCase().contains("glass")){
                it = addGlow(it);
            }
            return it;
        }

    public final static HashMap<Map<String, Object>, Map<String, Object>> serializeItemStack(ItemStack itemStack) {

            Map<String, Object> serializedItemStack, serializedItemMeta;
            HashMap<Map<String, Object>, Map<String, Object>> serializedMap = new HashMap<Map<String, Object>, Map<String, Object>>();

            if (itemStack == null){ itemStack = new ItemStack(Material.AIR);}
            serializedItemMeta = (itemStack.hasItemMeta())
                    ? itemStack.getItemMeta().serialize()
                    : null;
            itemStack.setItemMeta(null);
            serializedItemStack = itemStack.serialize();

            serializedMap.put(serializedItemStack, serializedItemMeta);
            return serializedMap;

    }
    public final static ItemStack deserializeItemStack(final HashMap<Map<String, Object>, Map<String, Object>> serializedItemStackMap) {

            Map.Entry<Map<String, Object>, Map<String, Object>> serializedItemStack = serializedItemStackMap.entrySet().iterator().next();

            ItemStack itemStack = ItemStack.deserialize(serializedItemStack.getKey());
            if (serializedItemStack.getValue() != null) {
                ItemMeta itemMeta = (ItemMeta)ConfigurationSerialization.deserializeObject(serializedItemStack.getValue(), ConfigurationSerialization.getClassByAlias("ItemMeta"));
                itemStack.setItemMeta(itemMeta);
            }

            return itemStack;

    }

        public final static List<HashMap<Map<String, Object>, Map<String, Object>>> serializeItemStackList(final ItemStack[] itemStackList) {
            final List<HashMap<Map<String, Object>, Map<String, Object>>> serializedItemStackList = new ArrayList<HashMap<Map<String, Object>, Map<String, Object>>>();

            for (ItemStack itemStack : itemStackList) {
                Map<String, Object> serializedItemStack, serializedItemMeta;
                HashMap<Map<String, Object>, Map<String, Object>> serializedMap = new HashMap<Map<String, Object>, Map<String, Object>>();

                if (itemStack == null) itemStack = new ItemStack(Material.AIR);
                serializedItemMeta = (itemStack.hasItemMeta())
                        ? itemStack.getItemMeta().serialize()
                        : null;
                itemStack.setItemMeta(null);
                serializedItemStack = itemStack.serialize();

                serializedMap.put(serializedItemStack, serializedItemMeta);
                serializedItemStackList.add(serializedMap);
            }
            return serializedItemStackList;
        }

        public final static ItemStack[] deserializeItemStackList(final List<HashMap<Map<String, Object>, Map<String, Object>>> serializedItemStackList) {
            final ItemStack[] itemStackList = new ItemStack[serializedItemStackList.size()];

            int i = 0;
            for (HashMap<Map<String, Object>, Map<String, Object>> serializedItemStackMap : serializedItemStackList) {
                Map.Entry<Map<String, Object>, Map<String, Object>> serializedItemStack = serializedItemStackMap.entrySet().iterator().next();

                ItemStack itemStack = ItemStack.deserialize(serializedItemStack.getKey());
                if (serializedItemStack.getValue() != null) {
                    ItemMeta itemMeta = (ItemMeta)ConfigurationSerialization.deserializeObject(serializedItemStack.getValue(), ConfigurationSerialization.getClassByAlias("ItemMeta"));
                    itemStack.setItemMeta(itemMeta);
                }

                itemStackList[i++] = itemStack;
            }
            return itemStackList;
        }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("mercado") && sender instanceof Player){
            if(args.length==1){
                if(args[0].equalsIgnoreCase("load")){
                    this.getPlayerData(sender.getName()).loadItem(((Player)sender).getItemInHand());
                    sender.sendMessage("Inv: "+this.getPlayerData(sender.getName()).Inventory);}
                else if(args[0].equalsIgnoreCase("get")){
                    String[] inv = this.getPlayerData(sender.getName()).getInventory().split("<item>");
                int next = 0;
                //            String|Object
                //<item>   <div>d2<mid>d1</div>   </item>
                List<ItemStack> items = new ArrayList();
                while(next<inv.length){
                    String s = inv[next];
                    if(s!=null) {
                        try{
                        s = s.substring(0, s.indexOf("</item>"));
                        Map<String, Object> item = new HashMap<String, Object>();
                        String[] ops = s.split("<div>");
                        int nops = 0;
                        while (nops < ops.length) {
                            try {
                                if(ops[nops]!=null){
                                ops[nops] = ops[nops].substring(0, ops[nops].indexOf("</div>"));
                                String[] part1 = ops[nops].split("<mid>");
                                    System.out.println("put "+part1[0]+" -> "+part1[1]);
                                item.put(part1[0], part1[1]);}
                            }catch(Exception e){e.printStackTrace();}
                            nops++;
                        }
                        //items.add(deserialize(item));
                        }catch(Exception e){e.printStackTrace();}
                    }
                    next++;
                }
                for(ItemStack it : items){
                    ((Player)sender).getInventory().addItem(it);
                }
                }}
                return true;
            }
        return false;
    }
    //|test||test2||test3|

    public void giveItemFromStore(Player player){
        String item = this.getPlayerData(player.getName()).getInventory();
        String[] sp1 = item.split("<div>");
        Map<String,Object> map = new HashMap();
        List<String> items = new ArrayList();
        for(int a = 0;a < sp1.length;a++){
        }

    }

    public void openStore(Player p, String tienda){
        p.sendMessage(Code.Color("&f&l[&a&lMercado&f&l] &b&lAbriendo tienda de "+tienda));
        if(p.getName().equalsIgnoreCase(tienda)){
            Inventory iv = Bukkit.createInventory(null,9, Code.Color("&dTienda de "+tienda));

        }else{

        }
        //0-8 = items
        //9-17 = buy items

    }
    public void viewStore(Player p, String tienda){
        p.sendMessage(new String[]{Code.Color("&f&l[&a&lMercado&f&l] &f&lTienda de "+tienda),Code.Color("&bUsa Click-Derecho en el cartel para abrir la tienda virtual")});
    }

      @EventHandler
      public void onSignClick(PlayerInteractEvent event){
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                if(event.getClickedBlock().getState() != null && event.getClickedBlock().getState() instanceof org.bukkit.block.Sign){
                    org.bukkit.block.Sign sign = (Sign) event.getClickedBlock().getState();
                    if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[tienda]")){
                        if(sign.getLine(1) != null && sign.getLine(1).length()>0){
                            this.viewStore(event.getPlayer(),sign.getLine(1));
                        }
                    }
                }
            }else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if(event.getClickedBlock().getState() != null && event.getClickedBlock().getState() instanceof org.bukkit.block.Sign){
                    org.bukkit.block.Sign sign = (Sign) event.getClickedBlock().getState();
                    if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[tienda]")){
                        if(sign.getLine(1) != null && sign.getLine(1).length()>0){
                            this.openStore(event.getPlayer(),sign.getLine(1));
                        }
                    }
                }
            }
        }

    }
