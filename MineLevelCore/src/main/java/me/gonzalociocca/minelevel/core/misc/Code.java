package me.gonzalociocca.minelevel.core.misc;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.TimeUnit;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by noname on 8/2/2017.
 */
public class Code {
    static Random random = new Random();
    public static Random getRandom(){
        return random;
    }

    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(cbc, (byte) 2));
    }
    public static TextComponent toHoverMessage(String str, String hovermsg) {
        TextComponent tc = new TextComponent(str);
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hovermsg).create()));

        return tc;
    }
    public static boolean isEquipment(ItemStack it) {
        String itname = it.getType().name().toLowerCase();
        if (itname.contains("helmet")) {
            return true;
        } else if (itname.contains("chestplate")) {
            return true;
        } else if (itname.contains("leggings")) {
            return true;
        } else if (itname.contains("boots")) {
            return true;
        } else if (itname.contains("sword")) {
            return true;
        } else if (itname.contains("pickaxe")) {
            return true;
        } else if (itname.contains("shovel")) {
            return true;
        } else if (itname.contains("axe")) {
            return true;
        } else if (itname.contains("hoe")) {
            return true;
        } else if (itname.contains("bow")) {
            return true;
        } else if (itname.contains("spade")){
            return true;
        }
        return false;

    }
    private static ItemStack toGlowingItemStack(org.bukkit.inventory.ItemStack stack) {
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

    private static Object getField(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);

            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
                    public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                        return (o2.getValue()).compareTo(o1.getValue());
                    }
                }
        );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    private static final HashMap<String, String> varcache = new HashMap();

    public static String Color(String s)
    {
        if (s == null) {
            return Color("&4&lError");
        }
        return s.replaceAll("(&([a-fk-or0-9]))", "§$2");
    }

    public static String replaceVars(String msg, String[] vars)
    {
        for (String str : vars)
        {
            String[] s = str.split("-");
            varcache.put(s[0], s[1]);
        }
        for (String str : varcache.keySet()) {
            msg = msg.replace("{$" + str + "}", varcache.get(str));
        }
        return msg;
    }
    public static LinkedHashMap<String, Integer> getTopExample(Main plugin) throws SQLException {
        LinkedHashMap<String,Integer> map = new LinkedHashMap();
        ResultSet res = null;
        res = plugin.getDB().getStatement().executeQuery("SELECT * FROM " + plugin.getDB().table + " ORDER BY ColiseoPts DESC LIMIT 10");

        while (res.next()) {
            map.put(res.getString("Name"), res.getInt("ColiseoPts"));
        }
        map = (LinkedHashMap) sortByValue((Map) map);
        return map;
    }
    public static float getRealMoveSpeed(float userSpeed, boolean isFly) {
        float defaultSpeed = isFly ? 0.1F : 0.2F;
        float maxSpeed = 1.0F;
        if (userSpeed < 1.0F) {
            return defaultSpeed * userSpeed;
        }
        float ratio = (userSpeed - 1.0F) / 9.0F * (maxSpeed - defaultSpeed);
        return ratio + defaultSpeed;
    }
    public static double trim(int degree, double d)
    {
        String format = "#.#";

        for (int i = 1; i < degree; i++) {
            format = format + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d)).doubleValue();
    }

    public static int r(int i)
    {
        return random.nextInt(i);
    }

    public static double offset(org.bukkit.util.Vector a, org.bukkit.util.Vector b)
    {
        return a.subtract(b).length();
    }
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    public static String now()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static String when(long time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Long.valueOf(time));
    }

    public static String date()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static String since(long epoch)
    {
        return "Took " + convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".";
    }

    public static double convert(long time, int trim, TimeUnit type)
    {
        if (type == TimeUnit.FIT)
        {
            if (time < 60000L) type = TimeUnit.SECONDS;
            else if (time < 3600000L) type = TimeUnit.MINUTES;
            else if (time < 86400000L) type = TimeUnit.HOURS; else {
                type = TimeUnit.DAYS;
            }
        }
        if (type == TimeUnit.DAYS) return Code.trim(trim, time / 86400000.0D);
        if (type == TimeUnit.HOURS) return Code.trim(trim, time / 3600000.0D);
        if (type == TimeUnit.MINUTES) return Code.trim(trim, time / 60000.0D);
        if (type == TimeUnit.SECONDS) return Code.trim(trim, time / 1000.0D);
        return Code.trim(trim, time);
    }

    public static String MakeStr(long time)
    {
        return convertString(time, 1, TimeUnit.FIT);
    }

    public static String MakeStr(long time, int trim)
    {
        return convertString(time, trim, TimeUnit.FIT);
    }

    public static String convertString(long time, int trim, TimeUnit type)
    {
        if (time == -1L) return "Permanent";

        if (type == TimeUnit.FIT)
        {
            if (time < 60000L) type = TimeUnit.SECONDS;
            else if (time < 3600000L) type = TimeUnit.MINUTES;
            else if (time < 86400000L) type = TimeUnit.HOURS; else {
                type = TimeUnit.DAYS;
            }
        }
        if (type == TimeUnit.DAYS) return Code.trim(trim, time / 86400000.0D) + " Days";
        if (type == TimeUnit.HOURS) return Code.trim(trim, time / 3600000.0D) + " Hours";
        if (type == TimeUnit.MINUTES) return Code.trim(trim, time / 60000.0D) + " Minutes";
        if (type == TimeUnit.SECONDS) return Code.trim(trim, time / 1000.0D) + " Seconds";
        return Code.trim(trim, time) + " Milliseconds";
    }

    public static boolean elapsed(long from, long required)
    {
        return System.currentTimeMillis() - from > required;
    }

    public static ItemStack makeItemStack(Material type, int amount, byte data){
       return makeItemStack(type, null, null, amount, data);
    }

    public static ItemStack makeItemStack(Material type, String display, String[] lore, int amount, byte data){
        ItemStack item = new ItemStack(type,amount,data);
        ItemMeta im = item.getItemMeta();
        if(display!=null) {
            im.setDisplayName(display);
        }
        if(lore!=null) {
            im.setLore(Arrays.asList(lore));
        }
        item.setItemMeta(im);
        return item;
    }

    public static void enchant(ItemStack itemStack, ItemMeta im, Enchantment enchantment, int enchlevel){
        if(im instanceof EnchantmentStorageMeta){
            ((EnchantmentStorageMeta)im).addStoredEnchant(enchantment, enchlevel, true);
            itemStack.setItemMeta(im);
        } else {
            itemStack.addUnsafeEnchantment(enchantment, enchlevel);
        }
    }

    //373:0 1 name:test_test_test lore:test_test_test;test_test sharpness:5 durability:3
    public static ItemStack makeItemStack(String str){
        ItemStack item;
        String[] args = str.split(" ");

        String[] val1 = args[0].split(":");// ID:Data

        int val2 = Integer.parseInt(args[1]); // Amount

        if(val1.length>1){
            item = new ItemStack(Material.getMaterial(Integer.parseInt(val1[0])),val2,(byte)Integer.parseInt(val1[1]));
        }else{
            item = new ItemStack(Material.getMaterial(Integer.parseInt(val1[0])),val2);
        }

        if(args.length > 2){
            ItemMeta im = item.getItemMeta();
            for(int a = 2; a < args.length;a++){
                String val3 = args[a];
                String val3value = val3.split(":")[1];
                if(val3.startsWith("name:")){
                    im.setDisplayName(Code.Color(val3value.replace("_"," ")));
                    item.setItemMeta(im);
                }else if(val3.startsWith("lore:")){
                    List<String> lore = new ArrayList();
                    for(String s : val3value.split(";")){
                        lore.add(Code.Color(s.replace("_"," ")));
                    }
                    im.setLore(lore);
                    item.setItemMeta(im);
                }else if(val3.startsWith("sharpness:")){
                    enchant(item, im, Enchantment.DAMAGE_ALL,Integer.parseInt(val3value));
                }else if(val3.startsWith("durability:")){
                    enchant(item, im, Enchantment.DURABILITY,Integer.parseInt(val3value));
                }else if(val3.startsWith("fire_aspect:")){
                    enchant(item, im, Enchantment.FIRE_ASPECT,Integer.parseInt(val3value));
                }else if(val3.startsWith("arrow_fire:")){
                    enchant(item, im, Enchantment.ARROW_FIRE,Integer.parseInt(val3value));
                }else if(val3.startsWith("arrow_damage:")){
                    enchant(item, im, Enchantment.ARROW_DAMAGE,Integer.parseInt(val3value));
                }else if(val3.startsWith("arrow_infinite:")){
                    enchant(item, im, Enchantment.ARROW_INFINITE,Integer.parseInt(val3value));
                }else if(val3.startsWith("arrow_knockback:")){
                    enchant(item, im, Enchantment.ARROW_KNOCKBACK,Integer.parseInt(val3value));
                }else if(val3.startsWith("damage_arthropods:")){
                    enchant(item, im, Enchantment.DAMAGE_ARTHROPODS,Integer.parseInt(val3value));
                }else if(val3.startsWith("damage_undead:")){
                    enchant(item, im, Enchantment.DAMAGE_UNDEAD,Integer.parseInt(val3value));
                }else if(val3.startsWith("depth_strider:")){
                    enchant(item, im, Enchantment.DEPTH_STRIDER,Integer.parseInt(val3value));
                }else if(val3.startsWith("dig_speed:")){
                    enchant(item, im, Enchantment.DIG_SPEED,Integer.parseInt(val3value));
                }else if(val3.startsWith("water_worker:")){
                    enchant(item, im, Enchantment.WATER_WORKER,Integer.parseInt(val3value));
                }else if(val3.startsWith("thorns:")){
                    enchant(item, im, Enchantment.THORNS,Integer.parseInt(val3value));
                }else if(val3.startsWith("silk_touch:")){
                    enchant(item, im, Enchantment.SILK_TOUCH,Integer.parseInt(val3value));
                }else if(val3.startsWith("protection_projectile:")){
                    enchant(item, im, Enchantment.PROTECTION_PROJECTILE,Integer.parseInt(val3value));
                }else if(val3.startsWith("protection_fire:")){
                    enchant(item, im, Enchantment.PROTECTION_FIRE,Integer.parseInt(val3value));
                }else if(val3.startsWith("protection_environmental:")){
                    enchant(item, im, Enchantment.PROTECTION_ENVIRONMENTAL,Integer.parseInt(val3value));
                }else if(val3.startsWith("protection_explosions:")){
                    enchant(item, im, Enchantment.PROTECTION_EXPLOSIONS,Integer.parseInt(val3value));
                }else if(val3.startsWith("protection_fall:")){
                    enchant(item, im, Enchantment.PROTECTION_FALL,Integer.parseInt(val3value));
                }else if(val3.startsWith("knockback:")){
                    enchant(item, im, Enchantment.KNOCKBACK,Integer.parseInt(val3value));
                }else if(val3.startsWith("loot_bonus_blocks:")){
                    enchant(item, im, Enchantment.LOOT_BONUS_BLOCKS,Integer.parseInt(val3value));
                }else if(val3.startsWith("loot_bonus_mobs:")){
                    enchant(item, im, Enchantment.LOOT_BONUS_MOBS,Integer.parseInt(val3value));
                }else if(val3.startsWith("luck:")){
                    enchant(item, im, Enchantment.LUCK,Integer.parseInt(val3value));
                }else if(val3.startsWith("lure:")){
                    enchant(item, im, Enchantment.LURE,Integer.parseInt(val3value));
                }else if(val3.startsWith("oxygen:")){
                    enchant(item, im, Enchantment.OXYGEN,Integer.parseInt(val3value));
                }
            }
        }
        return item;
    }

    public static void giveRandomMisteryBox(Player player, int amount, boolean broadcast, String broadcastExtra){
        String box = getRandomMisteryBoxName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"crate givekey "+player.getName()+" "+box+" "+amount);
        player.sendMessage(Code.Color("&bHas recibido &n"+amount+" Cajas Misteriosas "+box));
        if(broadcast){
            String str = "Llave Misteriosa";
            if(amount> 1){
                str = "Llaves Misteriosas";
            }
            if(broadcastExtra!=null){
                Bukkit.broadcastMessage(Code.Color("&b" + player.getName() + " Ha recibido &n" + amount + " "+str+" " + box +" &bpor "+broadcastExtra));
            }else {
                Bukkit.broadcastMessage(Code.Color("&b" + player.getName() + " Ha recibido &n" + amount + " "+str+" "+ box));
            }
        }
    }


    public static String getRandomMisteryBoxName(){
        if(Variable.BoxLuck==null){
            Variable.BoxLuck = new HashMap();
            Variable.BoxLuck.put("Uno",4);
            Variable.BoxLuck.put("Dos",1);
            Variable.BoxLuck.put("Tres",7);
        }
        int Uno = Variable.BoxLuck.get("Uno");
        if(Uno > 0){
            Variable.BoxLuck.put("Uno",--Uno);
            return "Uno";
        }
        int Dos = Variable.BoxLuck.get("Dos");
        if(Dos > 0){
            Variable.BoxLuck.put("Dos",--Dos);
            return "Dos";
        }
        int Tres = Variable.BoxLuck.get("Tres");
        if(Tres > 0) {
            Variable.BoxLuck.put("Tres", --Tres);
            return "Tres";
        }

        Variable.BoxLuck.put("Uno",3);
        Variable.BoxLuck.put("Dos",1);
        Variable.BoxLuck.put("Tres",6);
        return getRandomMisteryBoxName();
    }
    // 372:1 1 name:&aTest_Test

    public static void addItemsToSubasta() {
        Variable.SubastaItems = new ArrayList();
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:7 fire_aspect:4 durability:5"), 5000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:7 fire_aspect:4 durability:5 knockback:1"), 5000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:7 fire_aspect:4 durability:5 knockback:2"), 5000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:7 fire_aspect:1 durability:2"), 4250));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:7 fire_aspect:1 durability:2 knockback:1"), 4250));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:7 fire_aspect:1 durability:2 knockback:2"), 4250));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:6 fire_aspect:3 durability:4"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:6 fire_aspect:1 durability:2"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:6 fire_aspect:3 durability:4 knockback:1"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:6 fire_aspect:1 durability:2 knockback:1"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:6 fire_aspect:3 durability:4 knockback:2"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:6 fire_aspect:1 durability:2 knockback:2"), 3000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:5 fire_aspect:2 durability:3"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:5 fire_aspect:1 durability:1"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:5 fire_aspect:2 durability:3 knockback:1"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:5 fire_aspect:1 durability:1 knockback:1"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:5 fire_aspect:2 durability:3 knockback:2"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("276:0 1 sharpness:5 fire_aspect:1 durability:1 knockback:2"), 1500));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:6 fire_aspect:3 durability:4"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:6 fire_aspect:1 durability:2"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:6 fire_aspect:3 durability:4 knockback:1"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:6 fire_aspect:1 durability:2 knockback:1"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:6 fire_aspect:3 durability:4 knockback:2"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:6 fire_aspect:1 durability:2 knockback:2"), 3000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:5 fire_aspect:2 durability:3"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:5 fire_aspect:1 durability:1"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:5 fire_aspect:2 durability:3 knockback:1"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:5 fire_aspect:1 durability:1 knockback:1"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:5 fire_aspect:2 durability:3 knockback:2"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("279:0 1 sharpness:5 fire_aspect:1 durability:1 knockback:2"), 1500));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:7 arrow_fire:3 durability:5 arrow_knockback:2 arrow_infinite:1"), 5000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:7 arrow_fire:3 durability:5 arrow_infinite:1"), 5000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:7 arrow_fire:3 durability:5 arrow_knockback:1 arrow_infinite:1"), 5000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:6 arrow_fire:2 durability:4 arrow_knockback:2 arrow_infinite:1"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:6 arrow_fire:2 durability:4 arrow_infinite:1"), 3000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:6 arrow_fire:2 durability:4 arrow_knockback:1 arrow_infinite:1"), 3000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:5 arrow_fire:1 durability:3 arrow_knockback:2 arrow_infinite:1"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:5 arrow_fire:1 durability:3 arrow_infinite:1"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("261:0 1 arrow_damage:5 arrow_fire:1 durability:3 arrow_knockback:1 arrow_infinite:1"), 1500));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:5 durability:3 oxygen:1"), 1000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:5 durability:3 thorns:2 oxygen:2"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:5 durability:3 thorns:2 protection_projectile:5 protection_explosions:5 protection_fire:5 oxygen:5"), 3000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:6 durability:4 oxygen:2"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:6 durability:4 thorns:3 oxygen:3"), 2000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:6 durability:4 thorns:3 protection_projectile:6 protection_explosions:6 protection_fire:6 oxygen:6"), 3500));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:7 durability:5 oxygen:3"), 2000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:7 durability:5 thorns:4 oxygen:4"), 2500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("310:0 1 protection_environmental:7 durability:5 thorns:4 protection_projectile:7 protection_explosions:7 protection_fire:7 oxygen:7"), 4000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:5 durability:3"), 1000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:5 durability:3 thorns:2"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:5 durability:3 thorns:2 protection_projectile:5 protection_explosions:5 protection_fire:5"), 3000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:6 durability:4"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:6 durability:4 thorns:3"), 2000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:6 durability:4 thorns:3 protection_projectile:6 protection_explosions:6 protection_fire:6"), 3500));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:7 durability:5"), 2000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:7 durability:5 thorns:4"), 2500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("311:0 1 protection_environmental:7 durability:5 thorns:4 protection_projectile:7 protection_explosions:7 protection_fire:7"), 4000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:5 durability:3"), 1000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:5 durability:3 thorns:2"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:5 durability:3 thorns:2 protection_projectile:5 protection_explosions:5 protection_fire:5"), 3000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:6 durability:4"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:6 durability:4 thorns:3"), 2000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:6 durability:4 thorns:3 protection_projectile:6 protection_explosions:6 protection_fire:6"), 3500));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:7 durability:5"), 2000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:7 durability:5 thorns:4"), 2500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("312:0 1 protection_environmental:7 durability:5 thorns:4 protection_projectile:7 protection_explosions:7 protection_fire:7"), 4000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:5 durability:3 protection_fall:1"), 1000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:5 durability:3 thorns:2 protection_fall:2"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:5 durability:3 thorns:2 protection_projectile:5 protection_explosions:5 protection_fire:5 protection_fall:5"), 3000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:6 durability:4 protection_fall:2"), 1500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:6 durability:4 thorns:3 protection_fall:3"), 2000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:6 durability:4 thorns:3 protection_projectile:6 protection_explosions:6 protection_fire:6 protection_fall:6"), 3500));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:7 durability:5 protection_fall:3"), 2000));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:7 durability:5 thorns:4 protection_fall:4"), 2500));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("313:0 1 protection_environmental:7 durability:5 thorns:4 protection_projectile:7 protection_explosions:7 protection_fire:7 protection_fall:7"), 4000));

        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("322:1 64"), 600));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("384:0 64"), 200));
        Variable.SubastaItems.add(new SubastaItem(Code.makeItemStack("368:0 64"), 500));
    }

    public static ShapedRecipe getRecipe1(){
        ItemStack goldenapple = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);
        ShapedRecipe sp = new ShapedRecipe(goldenapple);
        sp.shape("AAA", "ABA", "AAA");
        sp.setIngredient('A', Material.GOLD_INGOT);
        sp.setIngredient('B', Material.APPLE);
        return sp;
    }

    public static ShapedRecipe getRecipe2(){
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 1);
        ShapedRecipe sp2 = new ShapedRecipe(gapple);
        sp2.shape("AAA", "ABA", "AAA");
        sp2.setIngredient('A', Material.GOLD_NUGGET);
        sp2.setIngredient('B', Material.APPLE);
        return sp2;
    }

    public static ShapedRecipe getRecipe3(){
        ItemStack goldenapples = new ItemStack(Material.GOLDEN_APPLE, 9, (short) 1);
        ShapedRecipe sp3 = new ShapedRecipe(goldenapples);
        sp3.shape("AAA", "ABA", "AAA");
        sp3.setIngredient('A', Material.GOLD_BLOCK);
        sp3.setIngredient('B', Material.APPLE);
        return sp3;
    }

    public static double getPercentage(double n, int total) {
        return (n / total) * 100;
    }

}
