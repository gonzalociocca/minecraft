package server.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import server.common.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noname on 10/4/2017.
 */
public class UtilMsg {

    public static ItemStack LobbyWatch = Code.makeItemStack(Material.WATCH,Code.Color("&aVolver al Lobby"), new String[]{"", ChatColor.RESET + "Click con este item en la mano", ChatColor.RESET + "para volver al Lobby."},1,(byte)0);
    public static HashMap<String, List<String>> TurboRacersSuits;
    public static String BuildBattleTable = "BuildBattle";
    public static List<String> BuildBattleThemes;
    public static List<String> GameTips;
    public static int BuildBattleTime = 300;
    public static String Website = Code.Color("&awww.minelevel.com");
    public static String Store = Code.Color("&ewww.minelevel.com/tienda");
    public static String WebsiteUrl = Code.Color("&ahttp://www.minelevel.com/");
    public static String StoreUrl = Code.Color("&ehttp://www.minelevel.com/tienda/");

    public static String BungeeLobby = "lobby";
    public static Long GameTimeLimit = 3200L;


    public static String PrefixKit = Code.Color("&e&lKit > &a");
    public static String Buyed = PrefixKit + Code.Color("Has comprado &r%kit &apor %s");
    public static String PreBuying = PrefixKit + Code.Color("Click de nuevo para comprar kit por %s");
    public static String KitEquip = PrefixKit + Code.Color("Has equipado &e&l%s Kit.");
    public static String CantEquip = PrefixKit + Code.Color("%s no puedes usar %d Kit.");

    public static String PrefixInventory = Code.Color("&e&lInventario > &a");
    public static String HotBarCannotSwap = PrefixInventory + Code.Color("No puedes cambiar &e&l%s.");
    public static String InventoryMove = PrefixInventory + Code.Color("No puedes mover &e&l%s.");

    public static String PrefixBuffer = Code.Color("&e&lBufos > &a");
    public static String Price = Code.Color("&aPrecio: &f$");
    public static String LightPurple = Code.Color("&d");
    public static String ChestRefill = Code.Color("&e&l%g > &aCofres &e%s &arellenados!");
    public static String Respawn = Code.Color("&f&lRespawning in &c&l%s &a&lseconds");
    public static String Spectator1 = Code.Color("&f&l You are out of the instance &6&lDONT QUIT&f&l!");
    public static String Spectator2 = Code.Color("&f&lThe next instance will start soon...");
    public static String Starting = Code.Color("&a&lStarting in %s");
    public static String Start = Code.Color("&a&lBuen Juego!");
    public static String Progress = Code.Color("&f&lEn progreso");
    public static String Waiting = Code.Color("&a&lEsperando jugadores..");
    public static String MaxPlayers = Code.Color("&eMax Players");
    public static String MinPlayers = Code.Color("&eMin Players");
    public static String TotalPlayers = Code.Color("&a&lPlayers");
    public static String Currency = Code.Color("&bCreditos");
    public static String JoinVip = Code.Color("&a[&e&l+&a] &b&l%s &a&l:)");
    public static String Join = Code.Color("&a[&e&l+&a]&7 %s");
    public static String QuitVip = Code.Color("&9[&c&l-&9]&7 %s &c&l);");
    public static String Quit = Code.Color("&9[&c&l-&9]&7 %s");
    public static String TeamChange = Code.Color("You changed of team with");
    public static String AFK = Code.Color("You will be kicked for being AFK!");
    public static String AlreadyIn = Code.Color("&f&lAlready in %s.");
    public static String Queue = Code.Color("&a&lYou are %pos in %s &b&lteam");
    public static String InsufficientBuy = Code.Color("&c&lInsufficient Coins, &e&lyou need %s");
    public static String InsufficientBuy2 = Code.Color("&a&lBuy more in server.com");
    public static String ServerIsFull = Code.Color("&cEl servidor esta lleno!, compra VIP en www.minelevel.com para entrar!");
    public static String KitName = Code.Color("&aKit - &f&l%s");
    public static String AFKRemoved = Code.Color("&6&lYou will be afk removed in %s seconds...");
    public static String WarningPlayableArea = Code.Color("&4&l WARNING: &f&lRETURN TO PLAYABLE AREA!");
    public static String NoWinners = Code.Color("&f&lNo one wins the instance...");
    public static String CurrencyUpdate = Code.Color("&f&lAhora tienes &b&l%s Creditos");
    public static String GameName = Code.Color("&aGame - &f&l%s");
    public static String MapInfo = Code.Color("&aMap - &f&l%s &7created by &f&l%d");
    public static String FirstPlaceTag = "1er Lugar";
    public static String SecondPlaceTag = "2do Lugar";
    public static String ThirdPlaceTag = "3er Lugar";
    public static String FirstPlace = Code.Color("&c"+FirstPlaceTag+"&f - %s");
    public static String SecondPlace = Code.Color("&6"+SecondPlaceTag+"&f - %s");
    public static String ThirdPlace = Code.Color("&e"+ThirdPlaceTag+"&f - %s");
    public static String GameIsInProgress = Code.Color("&f&lLa partida esta en progreso..");
    public static String CannotJoinGame = Code.Color("&cNo has podido ingresar al juego.");
    public static String FirstBlood = Code.Color("&e&l%g >&f %s &7ha hecho la primera sangre!");
    public static String Countdown = Code.Color("&e&l%g >&a Empezando en &f%ss");
    public static String Bold = Code.Color("&l");
    public static String WhiteBold = Code.Color("&f&l");
    public static String Black = Code.Color("&0");
    public static String White = Code.Color("&f");
    public static String Aqua = Code.Color("&b");
    public static String Yellow = Code.Color("&e");
    public static String Gold = Code.Color("&6");
    public static String Gray = Code.Color("&7");
    public static String Green = Code.Color("&a");
    public static String Red = Code.Color("&c");
    public static String DarkGreen = Code.Color("&2");
    public static String Purple = Code.Color("&d");

    public static String OutOfServiceComplete = Code.Color("&4Fuera de Servicio.");
    public static String OutOfServicePart1 = Code.Color("&4Fuera de");
    public static String OutOfServicePart2 = Code.Color("&4Servicio.");
    public static String In = "en";


    public static String ScoreboardPrepare = Code.Color("&fPreparate en &a");
    public static String ScoreboardStarting = Code.Color("&fEmpieza en &a");
    public static String ScoreboardRecruiting = Code.Color("&aReclutando");
    public static String ScoreboardPlayers = Code.Color("&fJugadores: &a");
    public static String ScoreboardMap = Code.Color("&fMapa &a");
    public static String ScoreboardMode = Code.Color("&fModo &r");
    public static String ScoreboardKills = Code.Color("&7 Kills");
    public static String ScoreboardAliveLeft = Code.Color("&fQueda");
    public static String ScoreboardAlive = Code.Color(" Vivo");
    public static String ScoreboardEvent = Code.Color("&fEvento &a");
    public static String ScoreboardEventInProgress = Code.Color("&4En Progreso");
    public static String ScoreboardNoEnemyLeft = Code.Color("&fSin enemigos");

    public static String spacer = Code.Color("&a&m&l---------------------------------------------");

    public static String[] stringToStringArray(String str, int limit, int divisor){
        List<String> stringList = stringToStringList(str, limit, divisor);
        return stringList.toArray(new String[stringList.size()]);
    }

    public static List<String> stringToStringList(String str, int limit, int divisor){
        List<String> stringList = new ArrayList();
        int div = limit > divisor ? limit - divisor : limit;
        if(str.length() > limit){
            String[] strArray = str.split(" ");
            String small = "";
            for(String var : strArray){
                small = small + var;
                if(small.length() > div){
                    stringList.add(small);
                    small = "";
                }
            }
            if(!small.isEmpty()){
                stringList.add(small);
            }
        } else {
            stringList.add(str);
        }
        return stringList;
    }


}
