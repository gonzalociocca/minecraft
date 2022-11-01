package mineultra.game.center;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockBurnEvent;
import java.io.File;
import org.bukkit.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerLoginEvent;
import mineultra.core.common.util.C;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import mineultra.game.center.game.GameTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import me.libraryaddict.disguise.DisguiseAPI;
import mineultra.game.center.addons.SoupAddon;
import mineultra.game.center.addons.CompassAddon;
import mineultra.game.center.managers.IdleManager;
import mineultra.game.center.managers.GamePlayerManager;
import mineultra.game.center.managers.GameFlagManager;
import mineultra.game.center.managers.GameChatManager;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameServerConfig;
import mineultra.game.center.managers.MiscManager;
import mineultra.game.center.managers.GameWorldManager;
import mineultra.game.center.managers.GameLobbyManager;
import mineultra.game.center.managers.GameManager;
import mineultra.game.center.managers.GameGemManager;
import mineultra.game.center.managers.GameCreationManager;
import mineultra.core.MiniPlugin;
import mineultra.core.common.BlockRestore;
import mineultra.core.common.Blood;
import mineultra.core.common.CachedPerm;
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
import mineultra.game.center.game.games.skywars.Stats;
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
    public Stats playerstatistics = null;
    static public String lobbyname = "lobby";
    
    static FileConfiguration config = null;
    public centerManager(JavaPlugin pl,final center plugin, final GameServerConfig serverConfig,final DamageManager damageManager, final Creature creature, final Blood blood, final Portal portal) {
        super("Game Manager", plugin);
     
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
        try{
        this.playerstatistics = new Stats(this);            
        }catch(Exception e){
            System.out.println("Stats disabled.");
        }

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
    
    public Stats GetStats(){
        return this.playerstatistics;
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
            if(this.GetGame().GetTeamList().size() > 1){
                event.setMaxPlayers(this.GetGame().GetTeamList().size());
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
    
    
    int gamesplayed = 0;
    Long end = -1L;
    
 @EventHandler
 public void onEnd(GameStateChangeEvent event){
    if(event.GetState() == GameState.End){
        if(event.GetGame().GetType() != GameType.SkyWars){
            return;
        }
        gamesplayed++;
        if(gamesplayed >= 3){
            end = System.currentTimeMillis()+2000L;
        }
    }
 }
 
 @EventHandler
 public void onEnd(UpdateEvent event){
     if(event.getType() != UpdateType.SEC){
         return;
     }
     if(end ==-1L){
         return;
     }
     else if(end < System.currentTimeMillis()){
         Bukkit.getServer().shutdown();
     }
 }
    
CachedPerm perm = new CachedPerm();

    @EventHandler
    public void MessageJoin(final PlayerJoinEvent event) {
        if (this._game == null || this._game.AnnounceJoinQuit) {
           if(perm.hasPerm(event.getPlayer(),"mineultra.vip")){
    event.setJoinMessage(
            Colorizer.Color(
                    getConfig().getString("messages.joinvip",F.sys("Join", event.getPlayer().getName()) ).replace("%s", event.getPlayer().getName())));       
    }
    else {
    event.setJoinMessage(
            Colorizer.Color(
                 getConfig().getString("messages.join",F.sys("Join", event.getPlayer().getName()) ).replace("%s", event.getPlayer().getName())));
     }}
        else {
            event.setJoinMessage(null);
        }
    }
    
    @EventHandler
    public void MessageQuit(final PlayerQuitEvent event) {
        if (this._game == null || this._game.AnnounceJoinQuit) {
         if(perm.hasPerm(event.getPlayer(),"mineultra.vip")){
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
       if(this.GetGame().GetType() == GameType.BuildBattle){
   player.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.WATCH, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Salir de la partida", new String[] { "", ChatColor.RESET + "Click sosteniendo esto", ChatColor.RESET + "para ir al Hub." }));
            
       }else{
            player.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.WATCH, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Return to Hub", new String[] { "", ChatColor.RESET + "Click while holding this", ChatColor.RESET + "to return to the Hub." }));
   
       }
   }else{
        player.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.WATCH, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Return to Hub", new String[] { "", ChatColor.RESET + "Click while holding this", ChatColor.RESET + "to return to the Hub." }));
   
   }
  
   
   try{
       if(this._game == null){
        return;
    }
    if(this._game.GetType() == null){
        return;
    }
    if(this.GetGame().GetType() != GameType.BuildBattle && this.GetGame().GetType() != GameType.TurboRacers){
player.getInventory().setItem(5, ItemStackFactory.Instance.CreateStack(Material.ENCHANTED_BOOK, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Statistics", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to view your Statistics." }));
    }
   
if(this.GetGame().GetType() == GameType.SWTeams){
player.getInventory().setItem(2, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Kit Selector", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to open the Kit Selector." }));
    
}    
    if(this.GetGame().GetType() == GameType.SkyWars){
        
   player.getInventory().setItem(2, ItemStackFactory.Instance.CreateStack(Material.GOLD_NUGGET, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Achievements", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to view your Achievements." }));

   if(this.GetGame().GetState() != GameState.Live && this.GetGame().GetState() != GameState.Prepare){

player.getInventory().setItem(1, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Kit Selector", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to open the Kit Selector." }));



   }

    }
    if(this.GetGame().IsLive()){
   player.getInventory().setItem(0, ItemStackFactory.Instance.CreateStack(Material.COMPASS, (byte)0, 1, (short)0, String.valueOf(C.cGreen) + "Player Finder", new String[] { "", ChatColor.RESET + "Right-Click with this", ChatColor.RESET + "to open the Player Finder." }));
    
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
            if (perm.hasPerm(event.getPlayer(),"mineultra.vip")) {
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
        try{
        if(DisguiseAPI.isDisguised(player)){
            DisguiseAPI.undisguiseToAll(player);
        }}catch(Exception e){
            
        }
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
