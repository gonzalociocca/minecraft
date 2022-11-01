package me.gonzalociocca.minelevel.core.misc;

import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

/**
 * Created by noname on 9/2/2017.
 */
public class Variable {
    public static String address = "127.0.0.1";
    public static String port = "3306";
    public static String db = "minelevel";
    public static String user = "root";
    public static String password = "notevinomas123-";
    public static double ActiveXpBoost = 0;
    public static List<SubastaItem> SubastaItems; // ItemStack/Full Price

    public static HashMap<String, String> tabListMap = new HashMap();
    public static HashMap<String, String> silentTabListMap = new HashMap();

    public static HashMap<String, Location> ColiseoPreviousLocations = new HashMap();
    public static HashMap<String, Long> ColiseoAntiAfk = new HashMap();

    public static HashMap<String,Integer> BoxLuck;


    public static Inventory SubastasInventory = null;

    public static Set<Player> staffList = new HashSet();
    public static Map<String, PlayerData> DatabasePlayerMap = new java.util.concurrent.ConcurrentHashMap<>();
    public static HashMap<String, Long> DiosCooldown = new HashMap();
    public static HashSet<String> GodList = new HashSet();
    public static Location Location1;
    public static Location Location2;
    public static Location Location3;
    public static Location Portal1Location;
    public static Location Portal2Location;
    public static boolean ShuttingDown;
    public static HashMap<String, Boolean> AutoPrivates = new HashMap();
    public static SvType ServerType = SvType.Factions;
    public static HashMap<String, PermissionAttachment> Attachments = new HashMap();
    public static HashMap<String, Long> BankLimit = new HashMap();
    public static boolean RewardsSended = false;
    public static boolean ColiseoStarted = false;
    public static boolean ColiseoPhase1 = false;
    public static boolean ColiseoPhase2 = false;
    public static FactionsEvent currentEvent;
    public static String Currency = Code.Color("&3Diamonds:");
    public static String Color1 = Code.Color("&a &b");
    public static String Color2 = Code.Color("&b &c");
    public static String Color3 = Code.Color("&c &d");
    public static String Color4 = Code.Color("&d &f");
    public static String Website = "www.minelevel.com";
    public static String Gold = ChatColor.GOLD+""+ChatColor.BOLD;
    public static String Yellow = ChatColor.YELLOW+""+ChatColor.BOLD;
    public static String White = ChatColor.WHITE+""+ChatColor.BOLD;
    public static String[] scoreboardPrefixs = new String[]{
            Yellow + " " + "M" + White + "ineLevel" + "  "
            ,Gold + " " + "M" + Yellow + "i" + White + "neLevel" + "  "
            ,Gold + " " + "Mi" + Yellow + "n" + White + "eLevel" + "  "
            ,Gold + " " + "Min" + Yellow + "e" + White + "Level" + "  "
            ,Gold + " " + "Mine" + Yellow + "L" + White + "evel" + "  "
            ,Yellow + " " + "M" + Yellow + "ineL" + Yellow + "e" + White + "vel" + "  "
            ,White + " " + "M" + Yellow + "i" + Gold + "neLe" + Yellow + "v" + White + "el" + "  "
            ,White + " " + "Mi" + Yellow + "n" + Gold + "eLev" + Yellow + "e" + White + "l" + "  "
            ,White + " " + "Min" + Yellow + "e" + Gold + "Leve" + Yellow + "l" + "  "
            ,White + " " + "Mine" + Yellow + "L" + Gold + "evel" + "  "
    };
    public static HashMap<String, Long> CompassLimit = new HashMap();

    public static String LobbyServerMenu = Code.Color("&f&n&lLista de Servidores");
    public static String ServerRex = Code.Color("&c&n&lRex");
    public static String ServerFacciones = Code.Color("&a&n&lSurvival y Facciones");
    public static String ServerSkyWars = Code.Color("&b&n&lSkyWars");
    public static String ServerSkyWarsBeta = Code.Color("&b&n&lSkyWars Beta");
    public static ItemStack RexStar = Code.makeItemStack(Material.NETHER_STAR,Code.Color("&e&n&lEstrella Fugaz"),new String[]{},1,(byte)0);

    public static String MessageLagKickNonVip = Code.Color("&aEl server esta lleno, &ecompra vip en www.minelevel.com/tienda &fpara entrar.");

}
