package mineultra.game.center;

import me.gonzalociocca.mineultra.DBManager;
import me.gonzalociocca.mineultra.Rank;
import mineultra.game.center.managers.*;
import org.bukkit.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockBurnEvent;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerLoginEvent;
import mineultra.core.common.util.C;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import mineultra.game.center.game.GameTeam;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import me.libraryaddict.disguise.DisguiseAPI;
import mineultra.game.center.addons.SoupAddon;
import mineultra.game.center.addons.CompassAddon;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameServerConfig;
import mineultra.core.MiniPlugin;
import mineultra.core.common.BlockRestore;
import mineultra.core.common.Blood;
import mineultra.core.common.util.Colorizer;
import static mineultra.core.common.util.Colorizer.Color;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.explosion.Explosion;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.portal.Portal;
import mineultra.core.projectile.ProjectileManager;
import mineultra.minecraft.game.core.IRelation;
import mineultra.minecraft.game.core.Buffer.Buffer;
import mineultra.minecraft.game.core.Buffer.BufferManager;
import mineultra.minecraft.game.core.damage.DamageManager;
import mineultra.minecraft.game.core.fire.Fire;
import mineultra.core.creature.Creature;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.Game.GameState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

  
public class centerManager extends MiniPlugin implements IRelation
{
    private BlockRestore _blockRestore = null;
    private Blood _blood = null;
    private BufferManager _BufferManager = null;
    private Creature _creature = null;
    private DamageManager _damageManager = null;
    private Explosion _explosionManager = null;
    private Fire _fire = null;
    private ProjectileManager _projectileManager = null;
    private Portal _portal = null;
    private GameFactory _gameFactory = null;
    private GameCreationManager _gameCreationManager = null;
    private GameGemManager _gameGemManager = null;
    private GameManager _gameManager = null;
    private GameLobbyManager _gameLobbyManager = null;
    private GameWorldManager _gameWorldManager = null;
    private MiscManager _miscManager = null;
    private GameServerConfig _serverConfig = null;
    private Game _game = null;
    static public String lobbyname = "lobby";
    private DBManager db = null;
    
    static FileConfiguration config = null;
    public centerManager(JavaPlugin pl, final center plugin, final DBManager adb, final GameServerConfig serverConfig, final DamageManager damageManager, final Creature creature, final Blood blood, final Portal portal) {
        super("Game Manager", plugin);
        db = adb;
        this._serverConfig = serverConfig;
        config = plugin.getConfig();
        
        this._blockRestore = new BlockRestore(plugin);
        this._blood = blood;
        (this._explosionManager = new Explosion(plugin, this._blockRestore)).SetDebris(false);
       
            this._BufferManager = new BufferManager(plugin);
        
        this._creature = creature;
        this._damageManager = damageManager;
        this._damageManager.UseSimpleWeaponDamage = true;
        this._fire = new Fire(plugin, this._BufferManager, damageManager);
        this._projectileManager = new ProjectileManager(plugin);

        this._portal = portal;
        this._gameFactory = new GameFactory(this);
        GameChatManager gameChatManager = new GameChatManager(this);
        this._gameCreationManager = new GameCreationManager(this);
        this._gameGemManager = new GameGemManager(this);
        this._gameManager = new GameManager(this);
        this._gameLobbyManager = new GameLobbyManager(this);
        GameFlagManager gameFlagManager = new GameFlagManager(this);
        GamePlayerManager gamePlayerManager = new GamePlayerManager(this);
        this._gameWorldManager = new GameWorldManager(this);
        try{
        this._miscManager = new MiscManager(this);
        }catch(Exception e){
            
        }
        IdleManager idleManager = new IdleManager(this);
        CompassAddon compassAddon = new CompassAddon(plugin, this);
        SoupAddon soupAddon = new SoupAddon(plugin, this);

        this.stateonly = this.getConfig().getBoolean("motd.stateonly");
        
        String nwlobby = getConfig().getString("lobby");
        if(nwlobby.length() < 20){
            lobbyname = nwlobby;
        }
    }
   
    
    public FileConfiguration getConfig(){
        return config;
    }
    
    public GameServerConfig GetServerConfig() {
        return this._serverConfig;
    }
    
    public ArrayList<GameType> GetGameList() {
        return this.GetServerConfig().GameList;
    }
    
    public Blood GetBlood() {
        return this._blood;
    }
    
    public BlockRestore GetBlockRestore() {
        return this._blockRestore;
    }
    
    public BufferManager GetBuffer() {
        return this._BufferManager;
    }
    
    public Creature GetCreature() {
        return this._creature;
    }
    
    public DamageManager GetDamage() {
        return this._damageManager;
    }
    
    public Explosion GetExplosion() {
        return this._explosionManager;
    }
    
    public Fire GetFire() {
        return this._fire;
    }
    
    public ProjectileManager GetProjectile() {
        return this._projectileManager;
    }
    
    public Portal GetPortal() {
        return this._portal;
    }
    
    public GameLobbyManager GetLobby() {
        return this._gameLobbyManager;
    }
  
    public GameCreationManager GetGameCreationManager() {
        return this._gameCreationManager;
    }
    
    public GameFactory GetGameFactory() {
        return this._gameFactory;
    }
    
    public GameManager GetGameManager() {
        return this._gameManager;
    }
    
    public GameGemManager GetGameGemManager() {
        return this._gameGemManager;
    }
    
    public GameWorldManager GetGameWorldManager() {
        return this._gameWorldManager;
    }
    

    
    public ChatColor GetColor(final Player player) {
        if (this._game == null) {
            return ChatColor.GRAY;
        }
        final GameTeam team = this._game.GetTeam(player);
        if (team == null) {
            return ChatColor.GRAY;
        }
        return team.GetColor();
    }
    
    @Override
    public boolean CanHurt(final String a, final String b) {
        return this.CanHurt(UtilPlayer.searchExact(a), UtilPlayer.searchExact(b));
    }
    
    @Override
    public boolean CanHurt(final Player pA, final Player pB) {
        if (pA == null || pB == null) {
            return false;
        }
        if (!this._game.Damage) {
            return false;
        }
        if (!this._game.DamagePvP) {
            return false;
        }
        if (pA.equals(pB)) {
            return this._game.DamageSelf;
        }
        final GameTeam tA = this._game.GetTeam(pA);
        if (tA == null) {
            return false;
        }
        final GameTeam tB = this._game.GetTeam(pB);
        return tB != null && (!tA.equals(tB) || this._game.DamageTeamSelf) && (tA.equals(tB) || this._game.DamageTeamOther);
    }
    
    @Override
    public boolean IsSafe(final Player player) {
        return this._game == null || !this._game.IsPlaying(player);
    }
    
    boolean stateonly = false;
    
    
    
    @EventHandler
    public void MessageMOTD(final ServerListPingEvent event) {
String extrainformation = "-" + this._serverConfig.ServerType + "-" + ((this._game == null) ? "Unknown" : this._game.GetName()) + "-" + ((this._game == null || this._game.WorldData == null) ? "Unknown" : this._game.WorldData.MapName);
       if(stateonly){
           extrainformation = "";
       }
        if(this.GetGame() != null){
            if(this.GetGame().GetTeamList().size() > 2){
                event.setMaxPlayers(this.GetGame().GetTeamList().size());
            }else{
                event.setMaxPlayers(this.GetPlayerFull());
            }
        }
        if (this._game == null || this._game.GetState() == Game.GameState.Recruit) {
            if (this._game != null && this._game.GetCountdown() != -1) {
                event.setMotd(Color(getConfig().getString("motd.starting").replace("%s", ""+this._game.GetCountdown())) + extrainformation);
            }
            else {
                event.setMotd(Color(getConfig().getString("motd.recruiting"))+ extrainformation);
            }
        }
        else {
            event.setMotd(Color(getConfig().getString("motd.inprogress")) + extrainformation);
        }
    }

    public boolean isEnding = false;
    public int games = 0;
    @EventHandler
    public void onChange(GameStateChangeEvent event){
        if(event.equals(GameState.End)){
            games++;
            if(games >= 3){
            isEnding = true;}
        }
    }

    Random r = new Random();

    @EventHandler
    public void onJoin(PlayerLoginEvent event){

        if(GetGame()==null){
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(Colorizer.Color("&aReiniciando..."));
            return;
        }
        int max = -1;

            if(GetGame().GetTeamList().size() > 2) {
                max = GetGame().GetTeamList().size();
            }else{
                max = this.GetPlayerFull();
            }


        if(max== -1){
            return;
        }
        if(!GetGame().canJoinAfterStart){
            if(GetGame().GetState() == GameState.Live || GetGame().GetState() == GameState.Prepare){
                event.setKickMessage(Colorizer.Color("&cEl juego ya ha empezado!"));
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            }
        }
        if(Bukkit.getOnlinePlayers().size() >= this.GetPlayerFull()){
            if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.VIP)){
            HashSet<Player> cankick = new HashSet<Player>();
            for(Player py : Bukkit.getOnlinePlayers()){
                if(!db.getPlayerData(py).getRank().isAtLeast(Rank.VIP)){
                    event.setResult(PlayerLoginEvent.Result.ALLOWED);
                    py.kickPlayer(Colorizer.Color("&aUn vip ha entrado a la sala, y no habia espacio para ti"));
                    return;
                }
            }}else{
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Colorizer.Color("&aServer lleno!"));
            }
        }else{
            event.setResult(PlayerLoginEvent.Result.ALLOWED);
        }
    }


    @EventHandler
    public void updateStatus(UpdateEvent event){
        if(event.getType() != UpdateType.FAST){
            return;
        }
        if(GetGame() != null){
            if(GetGame().GetTeamList().size() > 2) {
                db.setServerMaxOnline(GetGame().GetTeamList().size());
            }else{
                db.setServerMaxOnline(this.GetPlayerFull());
            }
        }
        if(this.isEnding){
            db.setServerStatus("Reiniciando");
            return;
        }
        if (this._game == null || this._game.GetState() == Game.GameState.Recruit) {
            if (this._game != null && this._game.GetCountdown() != -1) {

                db.setServerStatus("Empezando en "+this._game.GetCountdown());
            }
            else {
                db.setServerStatus("Reclutando");
            }
        }
        else {
            db.setServerStatus("En progreso");
        }
    }


public DBManager getDB(){
    return this.db;
}

    @EventHandler
    public void MessageJoin(final PlayerJoinEvent event) {
        if (this._game == null || this._game.AnnounceJoinQuit) {
            event.setJoinMessage(null);
            if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.VIP)){
                for(Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(Colorizer.Color("&a[+] &a"+db.getPlayerData(event.getPlayer()).getRank().getChatPrefix()+event.getPlayer().getName()));
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1F,1F);
                }
            }
    else {
                event.setJoinMessage(Colorizer.Color("&7[+] "+event.getPlayer().getName()));
     }}
        else {
            event.setJoinMessage(null);
        }
    }
    
    @EventHandler
    public void MessageQuit(final PlayerQuitEvent event) {
        if (this._game == null || this._game.AnnounceJoinQuit) {
         if(db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.VIP)){
    event.setQuitMessage(
            Colorizer.Color(
                 getConfig().getString("messages.quitvip",F.sys("Quit", event.getPlayer().getName()) ).replace("%s", event.getPlayer().getName())));       
    }
    else {
    event.setQuitMessage(
            Colorizer.Color(
            getConfig().getString("messages.quit",F.sys("Quit", event.getPlayer().getName()) ).replace("%s", event.getPlayer().getName())));
    
  }  }
        else {
            event.setQuitMessage((String)null);
        }
    }


    public Game GetGame() {
        return this._game;
    }
    
    public void SetGame(final Game game) {
        this._game = game;
    }
    
    public int GetPlayerMin() {
        return this.GetServerConfig().MinPlayers;
    }
    
    public int GetPlayerFull() {
        return this.GetServerConfig().MaxPlayers;
    }
    
    public void HubClock(final Player player) {
        if (this._game != null && !this._game.GiveClock) {
            return;
        }
   if(this.GetGame() != null){
       if(this.GetGame().kitMenuEnabled){
           player.getInventory().setItem(1, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Kit Selector", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to open the Kit Selector." }));
       }
            player.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.WATCH, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Salir de la partida", new String[] { "", ChatColor.RESET + "Click while holding this", ChatColor.RESET + "to return to the Hub." }));
   }else{
        player.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.WATCH, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Salir de la partida", new String[] { "", ChatColor.RESET + "Click while holding this", ChatColor.RESET + "to return to the Hub." }));
   
   }
  
   
   try{
       if(this._game == null){
        return;
    }
    if(this._game.GetType() == null){
        return;
    }

    if(this.GetGame().IsLive()){
   player.getInventory().setItem(0, ItemStackFactory.Instance.CreateStack(Material.COMPASS, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Player Finder", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to open the Player Finder." }));
        player.updateInventory();
    }
        if(this._game.GetType() == GameType.SWTeams
                && this._game.GetState() != GameState.Live 
                && this._game.GetState() != GameState.Prepare
                || this._game.GetType() == GameType.SkyWars
                && this._game.GetState() != GameState.Live 
                && this._game.GetState() != GameState.Prepare){
        player.getInventory().setItem(3, ItemStackFactory.Instance.CreateStack(Material.CHEST, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Chest Voting", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to open the vote contest." }));
    player.getInventory().setItem(4, ItemStackFactory.Instance.CreateStack(Material.WATCH, (byte)0, 1, (short)0, String.valueOf(C.cYellow) + "Time Voting", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to open the time contest." }));
    
        }}catch(Exception e){
        
    }
    }



    @EventHandler
    public void Login(final PlayerLoginEvent event) {
        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getServer().getMaxPlayers()) {
            if (this.db.getPlayerData(event.getPlayer()).getRank().isAtLeast(Rank.VIP)) {
                event.allow();
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
                return;
            }
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, config.getString("messages.svfull"));
        }
    }
    
    public boolean IsAlive(final Player player) {
        return this._game != null && this._game.IsAlive(player);
    }
    
    public void Clear(final Player player) {
       
        if(!player.getActivePotionEffects().isEmpty()) {
        for(PotionEffect t : player.getActivePotionEffects()){
            player.removePotionEffect(t.getType());
        }            
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        UtilInv.Clear(player);
        player.setSprinting(false);
        player.setFoodLevel(20);
        player.setSaturation(3.0f);
        player.setExhaustion(0.0f);
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        player.setFallDistance(0.0f);
        player.setLevel(0);
        player.setExp(0.0f);
  /*      ((CraftPlayer)player).getHandle().k = true;
    *//*    ((CraftPlayer)player).getHandle().p(0);
      */  this.GetBuffer().EndBuffer((LivingEntity)player, Buffer.BufferType.CLOAK, null);
        this.HubClock(player);
        if(Bukkit.getPluginManager().isPluginEnabled("LibsDisguises")){
        try{
        if(DisguiseAPI.isDisguised(player)){
            DisguiseAPI.undisguiseToAll(player);
        }}catch(Exception e){
            
        }}

    }
    
    public ArrayList<String> LoadFiles(final String gameName) {
        final File folder = new File(config.getString("mapconfig.folder").replace("%game", gameName));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final ArrayList<String> maps = new ArrayList<>();
        System.out.println("Searching for maps in: " + folder);
        File[] listFiles;
        for (int length = (listFiles = folder.listFiles()).length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            if (file.isFile()) {
                String name = file.getName();
                if (name.length() >= 5) {
                    name = name.substring(name.length() - 4, name.length());
                    if (!file.getName().equals(".zip")) {
                        maps.add(file.getName().substring(0, file.getName().length() - 4));
                    }
                }
            }
        }
        if(maps.isEmpty()){
            System.out.println(ChatColor.GREEN+"No maps found please put the maps in:");
            System.out.println(ChatColor.RED+""+folder.getAbsolutePath());
        }
        for (final String map : maps) {
            System.out.println("Found Map: " + map);
        }
        return maps;
    }
    

  
    
    @EventHandler
    public void BlockBurn(final BlockBurnEvent event) {
        if (this._game == null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void BlockSpread(final BlockSpreadEvent event) {
        if (this._game == null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void BlockFade(final BlockFadeEvent event) {
        if (this._game == null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void BlockDecay(final LeavesDecayEvent event) {
        if (this._game == null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void MobSpawn(final CreatureSpawnEvent event) {
        if (this._game == null) {
            event.setCancelled(true);
        }
    }

}
