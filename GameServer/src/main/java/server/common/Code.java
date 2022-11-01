package server.common;

import com.sk89q.jnbt.*;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.data.DataException;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.instance.core.map.misc.SchematicClipboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 * Created by noname on 8/2/2017.
 */
public class Code {
    static Random random = new Random();

    public static Random getRandom() {
        return random;
    }

    public static Color decodeRawColor(int id) {
        int raw = id % 16;
        return getRawColorOfInteger(raw);
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

    private static final HashMap<String, String> varcache = new HashMap();

    public static String Color(String s) {
        if (s == null) {
            return Color("&4&lError");
        }
        return s.replaceAll("(&([a-fk-or0-9]))", ChatColor.COLOR_CHAR + "$2");
    }

    public static String replaceVars(String msg, String[] vars) {
        for (String str : vars) {
            String[] s = str.split("-");
            varcache.put(s[0], s[1]);
        }
        for (String str : varcache.keySet()) {
            msg = msg.replace("{$" + str + "}", varcache.get(str));
        }
        return msg;
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

    public static double trim(int degree, double d) {
        String format = "#.#";

        for (int i = 1; i < degree; i++) {
            format = format + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d)).doubleValue();
    }


    public static double offset(org.bukkit.util.Vector a, org.bukkit.util.Vector b) {
        return a.subtract(b).length();
    }

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static String when(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Long.valueOf(time));
    }

    public static String date() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static double convert(long time, int trim, TimeUnit type) {
        if (type == TimeUnit.DAYS) return Code.trim(trim, time / 86400000.0D);
        if (type == TimeUnit.HOURS) return Code.trim(trim, time / 3600000.0D);
        if (type == TimeUnit.MINUTES) return Code.trim(trim, time / 60000.0D);
        if (type == TimeUnit.SECONDS) return Code.trim(trim, time / 1000.0D);
        return Code.trim(trim, time);
    }


    public static String convertString(long time, int trim, TimeUnit type) {
        if (time == -1L) return "Permanent";

        if (type == TimeUnit.DAYS) return Code.trim(trim, time / 86400000.0D) + " Days";
        if (type == TimeUnit.HOURS) return Code.trim(trim, time / 3600000.0D) + " Hours";
        if (type == TimeUnit.MINUTES) return Code.trim(trim, time / 60000.0D) + " Minutes";
        if (type == TimeUnit.SECONDS) return Code.trim(trim, time / 1000.0D) + " Seconds";
        return Code.trim(trim, time) + " Milliseconds";
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static ItemStack makeItemStack(Material type, int amount) {
        return makeItemStack(type, null, null, amount, (byte) 0);
    }

    public static ItemStack makeItemStack(Material type, String display) {
        return makeItemStack(type, display, null, 1, (byte) 0);
    }

    public static ItemStack makeItemStack(Material type, String display, String[] lore) {
        return makeItemStack(type, display, lore, 1, (byte) 0);
    }

    public static ItemStack makeItemStack(Material type, String display, int amount) {
        return makeItemStack(type, display, null, amount, (byte) 0);
    }

    public static ItemStack makeItemStack(Material type, String display, String[] lore, int amount, byte data) {
        ItemStack item = new ItemStack(type, amount, data);
        ItemMeta im = item.getItemMeta();
        if(display != null) {
            im.setDisplayName(display);
        }
        if (lore != null) {
            im.setLore(Arrays.asList(lore));
        }
        item.setItemMeta(im);
        return item;
    }

    //373:0 1 name:test_test_test lore:test_test_test;test_test sharpness:5 durability:3
    public static ItemStack makeItemStack(String str) {
        ItemStack item;
        String[] args = str.split(" ");

        String[] val1 = args[0].split(":");// ID:Data

        int val2 = Integer.parseInt(args[1]); // Amount

        if (val1.length > 1) {
            item = new ItemStack(Material.getMaterial(Integer.parseInt(val1[0])), val2, (byte) Integer.parseInt(val1[1]));
        } else {
            item = new ItemStack(Material.getMaterial(Integer.parseInt(val1[0])), val2);
        }
        if (args.length > 2) {
            ItemMeta im = item.getItemMeta();
            for (int a = 2; a < args.length; a++) {
                String val3 = args[a];
                String val3value = val3.split(":")[1];
                if (val3.startsWith("name:")) {
                    im.setDisplayName(Code.Color(val3value.replace("_", " ")));
                    item.setItemMeta(im);
                } else if (val3.startsWith("lore:")) {
                    List<String> lore = new ArrayList();
                    for (String s : val3value.split(";")) {
                        lore.add(Code.Color(s.replace("_", " ")));
                    }
                    im.setLore(lore);
                    item.setItemMeta(im);
                } else if (val3.startsWith("sharpness:")) {
                    item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Integer.parseInt(val3value));
                } else if (val3.startsWith("durability:")) {
                    item.addUnsafeEnchantment(Enchantment.DURABILITY, Integer.parseInt(val3value));
                } else if (val3.startsWith("fire_aspect:")) {
                    item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, Integer.parseInt(val3value));
                } else if (val3.startsWith("arrow_fire:")) {
                    item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, Integer.parseInt(val3value));
                } else if (val3.startsWith("arrow_damage:")) {
                    item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, Integer.parseInt(val3value));
                } else if (val3.startsWith("arrow_infinite:")) {
                    item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, Integer.parseInt(val3value));
                } else if (val3.startsWith("arrow_knockback:")) {
                    item.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, Integer.parseInt(val3value));
                } else if (val3.startsWith("damage_arthropods:")) {
                    item.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, Integer.parseInt(val3value));
                } else if (val3.startsWith("damage_undead:")) {
                    item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, Integer.parseInt(val3value));
                } else if (val3.startsWith("depth_strider:")) {
                    item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, Integer.parseInt(val3value));
                } else if (val3.startsWith("dig_speed:")) {
                    item.addUnsafeEnchantment(Enchantment.DIG_SPEED, Integer.parseInt(val3value));
                } else if (val3.startsWith("water_worker:")) {
                    item.addUnsafeEnchantment(Enchantment.WATER_WORKER, Integer.parseInt(val3value));
                } else if (val3.startsWith("thorns:")) {
                    item.addUnsafeEnchantment(Enchantment.THORNS, Integer.parseInt(val3value));
                } else if (val3.startsWith("silk_touch:")) {
                    item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, Integer.parseInt(val3value));
                } else if (val3.startsWith("protection_projectile:")) {
                    item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, Integer.parseInt(val3value));
                } else if (val3.startsWith("protection_fire:")) {
                    item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, Integer.parseInt(val3value));
                } else if (val3.startsWith("protection_environmental:")) {
                    item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.parseInt(val3value));
                } else if (val3.startsWith("protection_explosions:")) {
                    item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, Integer.parseInt(val3value));
                } else if (val3.startsWith("protection_fall:")) {
                    item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, Integer.parseInt(val3value));
                } else if (val3.startsWith("knockback:")) {
                    item.addUnsafeEnchantment(Enchantment.KNOCKBACK, Integer.parseInt(val3value));
                } else if (val3.startsWith("loot_bonus_blocks:")) {
                    item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Integer.parseInt(val3value));
                } else if (val3.startsWith("loot_bonus_mobs:")) {
                    item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, Integer.parseInt(val3value));
                } else if (val3.startsWith("luck:")) {
                    item.addUnsafeEnchantment(Enchantment.LUCK, Integer.parseInt(val3value));
                } else if (val3.startsWith("lure:")) {
                    item.addUnsafeEnchantment(Enchantment.LURE, Integer.parseInt(val3value));
                } else if (val3.startsWith("oxygen:")) {
                    item.addUnsafeEnchantment(Enchantment.OXYGEN, Integer.parseInt(val3value));
                }
            }
        }
        return item;
    }

    public static ArrayList<org.bukkit.Location> searchSignsWith(String str, ArrayList<org.bukkit.Location> locs, boolean setAir) {
        ArrayList<Location> newList = new ArrayList();


        for (org.bukkit.Location loc : locs) {
            Block block = loc.getBlock();
            BlockState state = block.getState();
            if (state instanceof Sign) {
                Sign sign = (Sign) state;
                if (sign.getLines().length > 0) {
                    if (sign.getLine(0).equalsIgnoreCase(str)) {
                        if (setAir) {
                            block.setType(Material.AIR, false);
                        }
                        newList.add(loc);
                    }
                }
            }
        }
        return newList;
    }

    public static void removeBlocks(ArrayList<org.bukkit.Location> locs) {

        for (org.bukkit.Location loc : locs) {
            Block block = loc.getBlock();
            block.setType(Material.AIR, false);
        }
    }

    public static ArrayList<CustomLocation> decodeLocations(String str) {

        ArrayList<CustomLocation> list = new ArrayList();
        if (!str.isEmpty()) {
            String[] locations = str.split("!");
            for (String location : locations) {
                CustomLocation newLoc = decodeLocation(location);
                if (newLoc != null) {
                    list.add(newLoc);
                }
            }
        }

        return list;
    }

    public static CustomLocation decodeLocation(String str) {
        return CustomLocation.fromString(str);
    }

    public static SchematicClipboard loadSchematicFromFile(File file) {
        try {
            return loadSchematicFromStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SchematicClipboard loadSchematicFromStream(InputStream stream) {
        NBTInputStream nbtStream = null;
        try {
            nbtStream = new NBTInputStream(new GZIPInputStream(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //com.sk89q.worldedit.Vector origin;
        //com.sk89q.worldedit.Vector offset;
        NamedTag rootTag = null;
        try {
            rootTag = nbtStream.readNamedTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            nbtStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!rootTag.getName().equals("Schematic")) {
            try {
                throw new DataException("Tag \"Schematic\" does not exist or is not first");
            } catch (DataException e) {
                e.printStackTrace();
            }
        } else {
            CompoundTag schematicTag = (CompoundTag) rootTag.getTag();
            Map schematic = schematicTag.getValue();

            short width = ShortTag.class.cast(schematic.get("Width")).getValue();
            short length = ShortTag.class.cast(schematic.get("Length")).getValue();
            short height = ShortTag.class.cast(schematic.get("Height")).getValue();

            /*
            int materials;
            int blockId;
            int blockData;
            materials = IntTag.class.cast(schematic.get("WEOriginX")).getValue();
            blockId = IntTag.class.cast(schematic.get("WEOriginY")).getValue();
            blockData = IntTag.class.cast(schematic.get("WEOriginZ")).getValue();
            origin = new com.sk89q.worldedit.Vector(materials, blockId, blockData);

            materials = IntTag.class.cast(schematic.get("WEOffsetX")).getValue();
            blockId = IntTag.class.cast(schematic.get("WEOffsetY")).getValue();
            blockData = IntTag.class.cast(schematic.get("WEOffsetZ")).getValue();
            offset = new com.sk89q.worldedit.Vector(materials, blockId, blockData);
            */

            String var29 = StringTag.class.cast(schematic.get("Materials")).getValue();
            if (!var29.equals("Alpha")) {
                try {
                    throw new DataException("Schematic file is not an Alpha schematic");
                } catch (DataException e) {
                    e.printStackTrace();
                }
            } else {
                byte[] var30 = ByteArrayTag.class.cast(schematic.get("Blocks")).getValue();
                byte[] var31 = ByteArrayTag.class.cast(schematic.get("Data")).getValue();
                byte[] addId = new byte[0];
                short[] blocks = new short[var30.length];

                Object varBlocks = schematic.get("AddBlocks");

                if (varBlocks != null) {
                    addId = ByteArrayTag.class.cast(varBlocks).getValue();
                }

                for (int tileEntities = 0; tileEntities < var30.length; ++tileEntities) {
                    if (tileEntities >> 1 >= addId.length) {
                        blocks[tileEntities] = (short) (var30[tileEntities] & 255);
                    } else if ((tileEntities & 1) == 0) {
                        blocks[tileEntities] = (short) (((addId[tileEntities >> 1] & 15) << 8) + (var30[tileEntities] & 255));
                    } else {
                        blocks[tileEntities] = (short) (((addId[tileEntities >> 1] & 240) << 4) + (var30[tileEntities] & 255));
                    }
                }

                List var32 = ListTag.class.cast(schematic.get("TileEntities")).getValue();
                HashMap tileEntitiesMap = new HashMap();
                Iterator size = var32.iterator();

                while (true) {
                    Tag clipboard;
                    int y;
                    int z;
                    int index;
                    do {
                        if (!size.hasNext()) {
                            com.sk89q.worldedit.Vector var33 = new com.sk89q.worldedit.Vector(width, height, length);
                            SchematicClipboard var34 = new SchematicClipboard(var33);
                            //var34.setOrigin(origin);
                            //var34.setOffset(offset);

                            for (int var35 = 0; var35 < width; ++var35) {
                                for (y = 0; y < height; ++y) {
                                    for (z = 0; z < length; ++z) {
                                        index = y * width * length + z * width + var35;
                                        BlockVector var36 = new BlockVector(var35, y, z);
                                        BaseBlock var38 = getBlockForId(blocks[index], (short) var31[index]);
                                        Object value = tileEntitiesMap.get(var36);

                                        if (value != null) {
                                            var38.setNbtData(new CompoundTag((Map) value));
                                        }

                                        var34.setBlock(var36, var38);
                                    }
                                }
                            }

                            return var34;
                        }

                        clipboard = (Tag) size.next();
                    } while (!(clipboard instanceof CompoundTag));

                    CompoundTag x = (CompoundTag) clipboard;
                    y = 0;
                    z = 0;
                    index = 0;
                    HashMap pt = new HashMap();

                    Map.Entry entry;
                    for (Iterator block = x.getValue().entrySet().iterator(); block.hasNext(); pt.put(entry.getKey(), entry.getValue())) {
                        entry = (Map.Entry) block.next();
                        if ((entry.getKey()).equals("x")) {
                            if (entry.getValue() instanceof IntTag) {
                                y = ((IntTag) entry.getValue()).getValue().intValue();
                            }
                        } else if ((entry.getKey()).equals("y")) {
                            if (entry.getValue() instanceof IntTag) {
                                z = ((IntTag) entry.getValue()).getValue().intValue();
                            }
                        } else if ((entry.getKey()).equals("z") && entry.getValue() instanceof IntTag) {
                            index = ((IntTag) entry.getValue()).getValue().intValue();
                        }
                    }

                    BlockVector var37 = new BlockVector(y, z, index);
                    tileEntitiesMap.put(var37, pt);
                }
            }
        }
        return null;
    }

    public static BaseBlock getBlockForId(int id, short data) {
        switch (id) {
            default:
                return new BaseBlock(id, data);
        }
    }

    public static boolean compareLocation(org.bukkit.Location loc1, org.bukkit.Location loc2) {
        return loc1 != null && loc2 != null && loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ();
    }

    public static float getYawOfSignRotation(byte rotation) {
        return rotation > 8 ? -((rotation - 8) * 22.5F) : rotation * 22.5F;
    }

    public static String addLocationToString(String origin, String key, CustomLocation value) {
        String str = origin.isEmpty() ? origin : origin + ";";

        str = str + "Location=" + key + "=<" + value.toString() + ">";

        return str;
    }

    public static String addLocationListToString(String origin, String key, List<CustomLocation> value) {
        String str = origin.isEmpty() ? origin : origin + ";";

        str = str + "LocationList=" + key + "=<";

        for (int a = 0; a < value.size(); a++) {
            CustomLocation loc = value.get(a);
            str = a != 0 ? str + "!" + loc.toString() : str + loc.toString();
        }
        str = str + ">";

        return str;
    }

    public static String addStringToString(String origin, String key, String value) {
        String str = origin.isEmpty() ? origin : origin + ";";
        str = str + "String=" + key + "=<" + value + ">";

        return str;
    }

    public static Color getRawColorOfInteger(int id) {
        if (id == 0) {
            return Color.AQUA;
        } else if (id == 1) {
            return Color.BLACK;
        } else if (id == 2) {
            return Color.BLUE;
        } else if (id == 3) {
            return Color.NAVY;
        } else if (id == 4) {
            return Color.BLUE;
        } else if (id == 5) {
            return Color.SILVER;
        } else if (id == 6) {
            return Color.OLIVE;
        } else if (id == 7) {
            return Color.PURPLE;
        } else if (id == 8) {
            return Color.FUCHSIA;
        } else if (id == 9) {
            return Color.ORANGE;
        } else if (id == 10) {
            return Color.GRAY;
        } else if (id == 11) {
            return Color.GREEN;
        } else if (id == 12) {
            return Color.PURPLE;
        } else if (id == 13) {
            return Color.RED;
        } else if (id == 14) {
            return Color.WHITE;
        } else if (id == 15) {
            return Color.YELLOW;
        } else {
            return Color.AQUA;
        }
    }

    public static String getColorOfInteger(int id) {
        if (id == 0) {
            return "" + ChatColor.AQUA;
        } else if (id == 1) {
            return "" + ChatColor.BLACK;
        } else if (id == 2) {
            return "" + ChatColor.BLUE;
        } else if (id == 3) {
            return "" + ChatColor.DARK_AQUA;
        } else if (id == 4) {
            return "" + ChatColor.DARK_BLUE;
        } else if (id == 5) {
            return "" + ChatColor.DARK_GRAY;
        } else if (id == 6) {
            return "" + ChatColor.DARK_GREEN;
        } else if (id == 7) {
            return "" + ChatColor.DARK_PURPLE;
        } else if (id == 8) {
            return "" + ChatColor.DARK_RED;
        } else if (id == 9) {
            return "" + ChatColor.GOLD;
        } else if (id == 10) {
            return "" + ChatColor.GRAY;
        } else if (id == 11) {
            return "" + ChatColor.GREEN;
        } else if (id == 12) {
            return "" + ChatColor.LIGHT_PURPLE;
        } else if (id == 13) {
            return "" + ChatColor.RED;
        } else if (id == 14) {
            return "" + ChatColor.WHITE;
        } else if (id == 15) {
            return "" + ChatColor.YELLOW;
        } else if (id > 15 && id < 32) {
            return getColorOfInteger(id % 16) + ChatColor.BOLD;
        } else if (id > 31 && id < 48) {
            return getColorOfInteger(id % 16) + ChatColor.BOLD + ChatColor.UNDERLINE;
        } else {
            return "" + ChatColor.WHITE;
        }
    }

    public static ItemStack itemFromJSON(JSONObject jsonObject) {
        int type = (int) jsonObject.get("classeditor");
        int amount = (int) jsonObject.get("Amount");
        short durability = (short) jsonObject.get("Durability");
        byte data = (byte) jsonObject.get("Data");

        ItemStack itemStack = new ItemStack(type, amount, durability, data);

        String display = (String) jsonObject.get("Display");
        JSONArray jsonLore = (JSONArray) jsonObject.get("Lore");
        JSONArray jsonFlags = (JSONArray) jsonObject.get("Flags");
        JSONArray jsonEnchants = (JSONArray) jsonObject.get("Enchants");

        ItemMeta im = itemStack.getItemMeta();

        if (display != null) {
            im.setDisplayName(display);
        }

        if (jsonLore != null) {
            im.setLore(jsonLore);
        }

        if (jsonFlags != null) {
            for (Object object : jsonFlags) {
                im.addItemFlags(ItemFlag.valueOf((String) object));
            }
        }

        if (jsonEnchants != null) {
            for(Object object : jsonEnchants){
                JSONObject jsonEnchant = (JSONObject)object;
                Map.Entry entry = (Map.Entry) jsonEnchant.entrySet().iterator().next();
                String enchantName = (String)entry.getKey();
                int enchantLevel = (int)entry.getValue();
                im.addEnchant(Enchantment.getByName(enchantName), enchantLevel, true);
            }
        }

        itemStack.setItemMeta(im);

        return itemStack;
    }

    public static JSONObject itemToJSON(ItemStack itemStack) {
        JSONObject jsonObject = new JSONObject();

        if (itemStack.hasItemMeta()) {

            ItemMeta im = itemStack.getItemMeta();

            if (im.hasDisplayName()) {
                jsonObject.put("Display", im.getDisplayName());
            }

            if (im.hasLore()) {
                JSONArray jsonLore = new JSONArray();
                jsonLore.addAll(im.getLore());
                jsonObject.put("Lore", jsonLore);
            }

            if (!im.getItemFlags().isEmpty()) {
                JSONArray jsonFlags = new JSONArray();
                for (ItemFlag itemFlag : im.getItemFlags()) {
                    jsonFlags.add(itemFlag.name());
                }
                jsonObject.put("Flags", jsonFlags);
            }

            if (im.hasEnchants()) {
                JSONArray jsonEnchants = new JSONArray();
                for (Map.Entry<Enchantment, Integer> entry : im.getEnchants().entrySet()) {
                    JSONObject jsonEnchant = new JSONObject();
                    jsonEnchant.put(entry.getKey().getName(), entry.getValue());
                    jsonEnchants.add(jsonEnchant);
                }
                jsonObject.put("Enchants", jsonEnchants);
            }
        }

        jsonObject.put("classeditor", itemStack.getTypeId());
        jsonObject.put("Amount", itemStack.getAmount());
        jsonObject.put("Durability", itemStack.getDurability());
        jsonObject.put("Data", itemStack.getData().getData());

        return jsonObject;
    }
}
