package server.instance;

import com.google.gson.annotations.Expose;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import server.ServerPlugin;
import server.api.GameAPI;
import server.common.*;
import server.common.event.*;
import server.instance.core.blood.GameBlood;
import server.instance.core.buffer.Buffer;
import server.instance.core.buffer.GameBuffer;
import server.instance.core.combat.DeathMessageType;
import server.instance.core.combat.GameCombat;
import server.instance.core.combat.event.ClearCombatEvent;
import server.instance.core.combat.event.CombatDeathEvent;
import server.instance.core.creature.GameCreature;
import server.instance.core.currency.GameCurrency;
import server.instance.core.damage.CustomDamageEvent;
import server.instance.core.damage.GameDamage;
import server.instance.core.disguise.GameDisguise;
import server.instance.core.explosion.GameExplosion;
import server.instance.core.flag.GameFlags;
import server.instance.core.idle.GameIdle;
import server.instance.core.join.GameJoin;
import server.instance.core.kit.GameKits;
import server.instance.core.kit.Kit;
import server.instance.core.map.base.MapCipherBase;
import server.instance.core.map.misc.MapState;
import server.instance.core.message.GameMessage;
import server.instance.core.projectile.GameProjectiles;
import server.instance.misc.GameState;
import server.instance.misc.GameTeam;
import server.user.User;
import server.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GameServer {

    // Non-Expose Start
    private final HashMap<String, Location> _playerLocationStore = new HashMap();
    private HashMap<Location, Player> PrivateBlockMap = new HashMap();
    private long _gameStateTime = System.currentTimeMillis();
    private int _countdown = -1;
    private int _alternativeCountdown = -1;
    private String Winner = "No one`s";
    private GameState _gameState = GameState.Loading;
    private long _helpTimer = 0L;
    private int _helpIndex = 0;
    private GameBlood _gameBlood = new GameBlood();
    private GameProjectiles _projectiles = new GameProjectiles();
    private GameBuffer _buffer = new GameBuffer();
    private GameIdle _gameIdle = new GameIdle();
    private boolean _prepareCountdown = false;
    private static int _temporaryIdCount = 0;
    private int _temporaryId = _temporaryIdCount++;
    // Non-Expose End

    // Expose Start
    @Expose public List<GameServer> subServerList = new ArrayList();
    @Expose public List<String> _gameDesc = new ArrayList();
    @Expose public boolean Damage = true;
    @Expose public boolean DamagePvP = true;
    @Expose public boolean DamagePvE = true;
    @Expose public boolean DamageEvP = true;
    @Expose public boolean DamageSelf = true;
    @Expose public boolean DamageFall = true;
    @Expose public boolean DamageTeamSelf = false;
    @Expose public boolean DamageTeamOther = true;
    @Expose public boolean BlockBreak = false;
    @Expose public boolean BlockBurn = false;
    @Expose public boolean BlockDecay = false;
    @Expose public boolean BlockSpread = false;
    @Expose public boolean BlockFade = false;
    @Expose public HashSet<Integer> BlockBreakAllow = new HashSet();
    @Expose public HashSet<Integer> BlockBreakDeny = new HashSet();
    @Expose public boolean BlockPlace = false;
    @Expose public HashSet<Integer> BlockPlaceAllow = new HashSet();
    @Expose public HashSet<Integer> BlockPlaceDeny = new HashSet();
    @Expose public boolean ItemPickup = false;
    @Expose public HashSet<Integer> ItemPickupAllow = new HashSet();
    @Expose public HashSet<Integer> ItemPickupDeny = new HashSet();
    @Expose public boolean ItemDrop = false;
    @Expose public HashSet<Integer> ItemDropAllow = new HashSet();
    @Expose public HashSet<Integer> ItemDropDeny = new HashSet();
    @Expose public boolean ItemDespawn = true;
    @Expose public boolean InventoryOpen = false;
    @Expose public boolean PrivateBlocks = false;
    @Expose public boolean DeathOut = true;
    @Expose public boolean DeathDropItems = false;
    @Expose public boolean DeathMessages = true;
    @Expose public double DeathSpectateSecs = 0.0D;
    @Expose public boolean QuitOut = true;
    @Expose public boolean IdleKick = true;
    @Expose public boolean CreatureAllow = false;
    @Expose public boolean CreatureAllowOverride = false;
    @Expose public int WorldTimeSet = -1;
    @Expose public boolean WorldWeatherEnabled = false;
    @Expose public int WorldWaterDamage = 0;
    @Expose public boolean WorldBoundaryKill = true;
    @Expose public int HungerSet = -1;
    @Expose public int HealthSet = -1;
    @Expose public int SpawnDistanceRequirement = 1;
    @Expose public boolean PrepareFreeze = true;
    @Expose public boolean RepairWeapons = true;
    @Expose public boolean AutoBalance = true;
    @Expose public boolean AnnounceJoinQuit = true;
    @Expose public boolean AnnounceSilence = true;
    @Expose public boolean DisplayLobbySide = true;
    @Expose public boolean AutoStart = true;
    @Expose public GameState KitRegisterState = GameState.Live;
    @Expose public boolean CompassEnabled = false;
    @Expose public boolean SoupEnabled = true;
    @Expose public boolean GiveClock = true;
    @Expose public double GemMultiplier = 1.0D;
    @Expose public HashMap<String, Integer> PrivateBlockCount = new HashMap();
    @Expose public Location spectatorSpawn = null;
    @Expose public GameMode defaultGameMode = GameMode.SURVIVAL;
    @Expose public boolean FirstKill = true;
    @Expose public GameTeam WinnerTeam = null;
    @Expose public List<String> _help = new ArrayList<String>();
    @Expose public boolean _countdownForce = false;
    @Expose public String _customWinLine = "";
    @Expose public ChatColor _helpColor = ChatColor.YELLOW;
    @Expose public int minPlayers = 2;
    @Expose public int maxPlayers = 0;
    @Expose public long endFeastTime = 8000L;
    @Expose public GameFlags _gameFlags = new GameFlags();
    @Expose public GameDisguise _gameDisguises = new GameDisguise();
    @Expose public GameCreature _gameCreature = new GameCreature();
    @Expose public GameExplosion _explosion = new GameExplosion();
    @Expose public GameDamage _damage = new GameDamage();
    @Expose public GameCurrency _gameCurrency = new GameCurrency();
    @Expose public GameMessage _gameMessage = new GameMessage();
    @Expose public String _title = "Title Here";
    @Expose public String _shortTitle = "ShortTitle";
    @Expose public String _longTitle = "Long Title Here";
    @Expose public MapCipherBase _mapCipherBase = new MapCipherBase();
    @Expose public String _storageId;
    @Expose public String classIdentifier = getClass().getCanonicalName();
    //todo: ReMake GameKits GameJoin
    public GameKits _gameKits = new GameKits();
    // Expose End

    private GameJoin _gameJoin = new GameJoin();

    public GameServer(){
        display(this);
    }

    public ArrayList<Location> getSpawns(){
        ArrayList<Location> list = new ArrayList();
        if(getMap() != null) {
            World world = getMap().getWorld();
            List<CustomLocation> customLocationList = getMap().getCustomLocationList(TagEnum.SoloSpawn.getID());
            if(customLocationList != null) {
                for (CustomLocation loc : customLocationList) {
                    list.add(new Location(world, loc.getX() + 0.50F, loc.getY(), loc.getZ() + 0.50F, loc.getYaw(), loc.getPitch()));
                }
            }
        }

        Location center = UtilLocation.averageLocation(list);
        for(Location loc : list){
            loc.setYaw(UtilLocation.getYaw(loc, center));
        }

        return list;
    }

    public String getStorageId(){
        return _storageId;
    }

    public void setStorageId(String id){
        _storageId = id;
    }

    public GameBlood getBlood(){
        return _gameBlood;
    }

    public GameCreature getCreature(){
        return _gameCreature;
    }

    public GameKits getKits(){
        return _gameKits;
    }

    public GameJoin getLogin() {
        return _gameJoin;
    }

    public GameDisguise getDisguises() {
        return _gameDisguises;
    }

    public GameCurrency getGems() {
        return _gameCurrency;
    }

    public GameFlags getFlags() {
        return _gameFlags;
    }

    public GameDamage getDamage() {
        return _damage;
    }

    public GameCombat getCombat() {
        return getDamage().getCombat();
    }

    public long getEndFeastTime() {
        return endFeastTime;
    }

    public void setEndFeastTime(long newLong) {
        endFeastTime = newLong;
    }

    public int getGameID() {
        return _temporaryId;
    }

    public GameIdle getIdle() {
        return _gameIdle;
    }

    public GameExplosion getExplosion() {
        return _explosion;
    }

    public GameProjectiles getProjectiles() {
        return _projectiles;
    }

    public GameBuffer getBuffer() {
        return _buffer;
    }

    public GameMessage getMessage(){
        return _gameMessage;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int min) {
        minPlayers = min;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int max) {
        maxPlayers = max;
    }

    public MapCipherBase getMap() {
        return _mapCipherBase;
    }

    public boolean canStartCountdown() {
        return getPlayersCount(true) >= getMinPlayers();
    }

    public final String getTitle() {
        return _title;
    }

    public String getMode() {
        return null;
    }

    public List<String> getDesc() {
        return _gameDesc;
    }

    public void setCustomWinLine(String line) {
        _customWinLine = line;
    }

    public int getCountdown() {
        return _countdown;
    }

    public void setCountdown(int time) {
        _countdown = time;
    }

    public int getAlternativeCountdown() {
        return _alternativeCountdown;
    }

    public void setAlternativeCountdown(int newValue) {
        _alternativeCountdown = newValue;
    }

    public boolean getCountdownForce() {
        return _countdownForce;
    }

    public void setCountdownForce(boolean value) {
        _countdownForce = value;
    }

    public HashMap<String, Location> getLocationStore() {
        return _playerLocationStore;
    }

    public HashMap<Location, Player> getPrivateBlockMap(){
        return PrivateBlockMap;
    }

    public GameState getState() {
        return _gameState;
    }

    public void setState(GameState state) {
        _gameState = state;
        _gameStateTime = System.currentTimeMillis();

        for (Player player : getPlayers(false)) {
            player.leaveVehicle();
        }

        GameStateChangeEvent stateEvent = new GameStateChangeEvent(this, state);
        Bukkit.getServer().getPluginManager().callEvent(stateEvent);

        System.out.println(getTitle() + " state set to " + state.toString());
    }

    public boolean isInBounds(Location loc) {
        if(loc == null || getMap() == null){
            return false;
        }
        CustomLocation loc2 = getMap().getBuildLocation();

        return loc.getWorld().getName().equalsIgnoreCase(getMap().getWorldName()) && (loc.getX() > loc2.getX() - 250 && loc.getX() < loc2.getX() + 250
                && loc.getY() > -100 && loc.getY() < 300
                && loc.getZ() > loc2.getZ() - 250 && loc.getZ() < loc2.getZ() + 250);
    }

    public long getStateTime() {
        return _gameStateTime;
    }

    public boolean inProgress() {
        return (getState() == GameState.Prepare) || (getState() == GameState.Live);
    }

    public boolean isLive() {
        return _gameState == GameState.Live;
    }

    public void restrictKits() {

    }

    public void parseData() {
    }

    public void updateScoreboard(GameTeam team){}

    public void endCheck(){}

    public void clearPlayer(final Player player) {
        if (!player.getActivePotionEffects().isEmpty()) {
            for (PotionEffect t : player.getActivePotionEffects()) {
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
        getBuffer().endBuffer(this, player, Buffer.BufferType.CLOAK, null);
        //GetLobby().fillInventory(player);

    }

    public void respawnPlayer(final Player player) {
        player.eject();

        Location loc = getLogin().getTeam(player).GetSpawn();

        teleportPlayer(player, loc, TeleportReason.Respawn);

        clearPlayer(player);

        PlayerGameRespawnEvent event = new PlayerGameRespawnEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ServerPlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Kit kit = getKits().getKit(player);
                        if(kit != null) {
                            kit.applyKit(player);
                        }
                    }
                }
                , 0L);
    }

    public boolean isPlaying(Player player) {
        return getLogin().getTeam(player) != null;
    }

    public boolean isAlive(Player player) {
        GameTeam team = getLogin().getTeam(player);

        if (team == null) {
            return false;
        }
        return team.isAlive(player);
    }

    public int getPlayersCount(boolean aliveOnly) {
        int players = 0;

        for (GameTeam team : getLogin().getTeamList()) {
            players += team.getPlayersCount(aliveOnly);
        }
        return players;
    }

    public ArrayList<Player> getPlayers(boolean aliveOnly) {
        ArrayList players = new ArrayList();

        for (GameTeam team : getLogin().getTeamList()) {
            players.addAll(team.getPlayers(aliveOnly));
        }
        return players;
    }


    public void reloadSpectatorSpawn() {

        Vector vec = new Vector(0, 0, 0);
        double count = 0.0D;

        for (GameTeam team : getLogin().getTeamList()) {
            for (Location spawn : team.GetSpawns()) {
                count += 1.0D;
                vec.add(spawn.toVector());
            }
        }

        spectatorSpawn = new Location(getMap().getWorld(), 0.0D, 0.0D, 0.0D);

        vec.multiply(1.0D / count);

        spectatorSpawn.setX(vec.getX());
        spectatorSpawn.setY(vec.getY());
        spectatorSpawn.setZ(vec.getZ());

        while ((!UtilBlock.airFoliage(spectatorSpawn.getBlock())) || (!UtilBlock.airFoliage(spectatorSpawn.getBlock().getRelative(BlockFace.UP)))) {
            spectatorSpawn.add(0.0D, 1.0D, 0.0D);
        }

        int Up = 0;

        for (int i = 0; i < 15; i++) {
            if (!UtilBlock.airFoliage(spectatorSpawn.getBlock().getRelative(BlockFace.UP)))
                break;
            spectatorSpawn.add(0.0D, 1.0D, 0.0D);
            Up++;
        }

        while (((Up > 0) && (!UtilBlock.airFoliage(spectatorSpawn.getBlock()))) || (!UtilBlock.airFoliage(spectatorSpawn.getBlock().getRelative(BlockFace.UP)))) {
            spectatorSpawn.subtract(0.0D, 1.0D, 0.0D);
            Up--;
        }

        spectatorSpawn = spectatorSpawn.getBlock().getLocation().add(0.5D, 0.1D, 0.5D);

        while ((spectatorSpawn.getBlock().getTypeId() != 0) || (spectatorSpawn.getBlock().getRelative(BlockFace.UP).getTypeId() != 0)) {
            spectatorSpawn.add(0.0D, 1.0D, 0.0D);
        }
    }

    public Location getSpectatorLocation() {
        return spectatorSpawn;
    }

    public void SetSpectator(Player player) {
        clearPlayer(player);

        teleportPlayer(player, getSpectatorLocation(), TeleportReason.Out);

        //todo: update spectator scoreboard

        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);
        player.setFlySpeed(0.1F);

        getBuffer().factory().Cloak(this, "Spectator", player, player, 7777.0D, true, true);
    }

    public DeathMessageType GetDeathMessageType() {
        if (!DeathMessages) {
            return DeathMessageType.None;
        }
        if (DeathOut) {
            return DeathMessageType.Absolute;
        }
        return DeathMessageType.Simple;
    }

    public void AnnounceGame() {
        for (Player player : getPlayers(false)) {
            AnnounceGame(player);
        }
    }

    public String getColor(final Player player) {
        final GameTeam team = getLogin().getTeam(player);
        if (team == null) {
            return "" + ChatColor.GRAY;
        }
        return team.getColor();
    }

    public void AnnounceGame(Player player) {
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0F, 1.0F);

        for (int i = 0; i < 6 - getDesc().size(); i++) {
            UtilPlayer.message(player, "");
        }
        UtilPlayer.message(player, UtilMsg.spacer);

        UtilPlayer.message(player, UtilMsg.GameName.replace("%s", getTitle()));
        UtilPlayer.message(player, "");

        for (String line : getDesc()) {
            UtilPlayer.message(player, UtilMsg.White + "- " + line);
        }

        UtilPlayer.message(player, "");
        UtilPlayer.message(player, UtilMsg.MapInfo.replace("%s", getMap().getDisplay()).replace("%d", getMap().getAuthor()));

        UtilPlayer.message(player, UtilMsg.spacer);
    }

    public void AnnounceEnd(GameTeam team) {
        if (!isLive()) {
            return;
        }
        for (Player player : getPlayers(false)) {
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0F, 1.0F);

            UtilPlayer.message(player, "");
            UtilPlayer.message(player, UtilMsg.spacer);

            UtilPlayer.message(player, UtilMsg.GameName.replace("%s", getTitle()));
            UtilPlayer.message(player, "");
            UtilPlayer.message(player, "");

            if (team != null) {
                WinnerTeam = team;
                Winner = (team.getName() + " Team");
            } else {
                UtilPlayer.message(player, UtilMsg.NoWinners);
            }

            UtilPlayer.message(player, _customWinLine);
            UtilPlayer.message(player, "");
            UtilPlayer.message(player, UtilMsg.MapInfo.replace("%s", getMap().getDisplay()).replace("%d", getMap().getAuthor()));

            UtilPlayer.message(player, UtilMsg.spacer);
        }


    }

    public void StateCountdown(int timer, boolean force) {
        if ((!getCountdownForce()) && (!force) && (!UtilTime.elapsed(getStateTime(), 15000L))) {
            return;
        }
        if (force) {
            setCountdownForce(true);
        }
        getLogin().TeamPreferenceJoin(this);

        getLogin().TeamPreferenceSwap(this);

        getLogin().TeamDefaultJoin(this);
        if (getCountdown() == -1) {
            getLogin().InformQueuePositions();
        }
        if (force) {
            setCountdownForce(true);
        }
        if (getCountdown() == -1) {
            setCountdown(timer + 1);
        }
        if ((getCountdown() > timer + 1) && (timer != -1)) {
            setCountdown(timer + 1);
        }
        if (getCountdown() > 0) {
            setCountdown(getCountdown() - 1);
        }
        boolean isTen = getCountdown() % 10 == 0;
        if ((getCountdown() > 0) && (getCountdown() <= 10 || isTen)) {
            for (Player player : getPlayers(false)) {
                UtilPlayer.message(player, UtilMsg.Countdown.replace("%s", "" + getCountdown()).replace("%g", getTitle()));
                if (isTen) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
                }
            }
        }
        if (getCountdown() == 0) {
            setState(GameState.Prepare);
        }
    }

    public void AnnounceEnd(ArrayList<Player> places) {
        for (Player player : getPlayers(false)) {
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0F, 1.0F);

            UtilPlayer.message(player, "");
            UtilPlayer.message(player, UtilMsg.spacer);

            UtilPlayer.message(player, UtilMsg.GameName.replace("%s", getTitle()));
            UtilPlayer.message(player, "");

            if ((places == null) || (places.isEmpty())) {
                UtilPlayer.message(player, "");
                UtilPlayer.message(player, UtilMsg.NoWinners);
                UtilPlayer.message(player, "");
            } else {
                if (places.size() >= 1) {
                    Winner = places.get(0).getName();
                    UtilPlayer.message(player, UtilMsg.FirstPlace.replace("%s", places.get(0).getName()));
                }

                if (places.size() >= 2) {
                    UtilPlayer.message(player, UtilMsg.SecondPlace.replace("%s", places.get(1).getName()));
                }
                if (places.size() >= 3) {
                    UtilPlayer.message(player, UtilMsg.ThirdPlace.replace("%s", places.get(2).getName()));
                }
            }
            UtilPlayer.message(player, "");
            UtilPlayer.message(player, UtilMsg.MapInfo.replace("%s", getMap().getDisplay()).replace("%d", getMap().getAuthor()));

            UtilPlayer.message(player, UtilMsg.spacer);
        }

    }

    public void Announce(String message) {
        for (Player player : getPlayers(false)) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);

            UtilPlayer.message(player, message);
        }

        System.out.println("[Anuncio] " + message);
    }

    public boolean CanThrowTNT(Location location) {
        return true;
    }

    public void resetGame() {
        getMap().setState(MapState.Loading);
        UtilScoreboard.clearServer(this, true);
        _playerLocationStore.clear();
        PrivateBlockCount.clear();
        PrivateBlockMap.clear();
        minPlayers = 2;
        maxPlayers = 0;
        _gameStateTime = System.currentTimeMillis();
        _countdown = -1;
        _alternativeCountdown = -1;
        _prepareCountdown = false;
        _countdownForce = false;
        Winner = "No one`s";
        spectatorSpawn = null;
        getBuffer().cleanAll();
        getExplosion().cleanAll();
        getProjectiles().cleanAll();
        getCombat().cleanAll();
        getDamage().cleanAll();
        getIdle().cleanAll();
        getLogin().cleanAll();
        getGems().cleanAll();

        //Bukkit.getPluginManager().registerEvents(this, Manager.getPlugin());
        setState(GameState.Loading);
    }


    public void checkHelp(UpdateEvent event) {
        if ((_help == null) || (_help.size() == 0)) {
            return;
        }
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        if (getState() != GameState.Recruit) {
            return;
        }
        if (!UtilTime.elapsed(_helpTimer, 8000L)) {
            return;
        }
        if (_helpColor == ChatColor.YELLOW)
            _helpColor = ChatColor.GREEN;
        else {
            _helpColor = ChatColor.YELLOW;
        }
        _helpTimer = System.currentTimeMillis();

        String msg = UtilMsg.WhiteBold + "TIP " + ChatColor.RESET + _helpColor + _help.get(_helpIndex);

        for (Player player : getPlayers(false)) {
            player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);

            UtilPlayer.message(player, msg);
        }

        _helpIndex = ((_helpIndex + 1) % _help.size());
    }

    public void StartPrepareCountdown() {
        _prepareCountdown = true;
    }

    public boolean CanStartPrepareCountdown() {
        return _prepareCountdown;
    }

    public boolean canHurt(final String a, final String b) {
        return getFlags().canHurt(this, UtilPlayer.searchExact(a), UtilPlayer.searchExact(b));
    }

    public void checkStateUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.SEC) {
            if (getState() == GameState.Loading) {
                if (getMap() != null && getMap().getState() == MapState.Build) {
                    setState(GameState.Recruit);
                }
            } else if (getState() == GameState.Recruit) {

                if (getCountdown() != -1) {
                    StateCountdown(-1, false);
                    if (!getCountdownForce() && !canStartCountdown()) {
                        setCountdown(-1);
                    }
                } else if (AutoStart) {
                    if (getPlayersCount(true) >= getMaxPlayers()) {
                        StateCountdown(20, false);
                    } else if (getPlayersCount(true) >= getMinPlayers()) {
                        StateCountdown(60, false);
                    }
                }

            } else if (getState() == GameState.Prepare) {
                if (CanStartPrepareCountdown()) {
                    if (getAlternativeCountdown() < 0) {
                        setAlternativeCountdown(10);
                    } else {
                        setAlternativeCountdown(getAlternativeCountdown() - 1);
                    }
                    int alternativeCountdown = getAlternativeCountdown();
                    if (alternativeCountdown == 0) {
                        for (Player player : getPlayers(false)) {
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2.0F, 2.0F);
                        }
                        if (getPlayersCount(true) < getMinPlayers()) {
                            Announce(UtilMsg.WhiteBold + getTitle() + " finished, not enough players");
                            setState(GameState.Dead);
                        } else {
                            setState(GameState.Live);
                        }
                    } else {
                        for (Player player : getPlayers(false)) {
                            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
                        }
                    }
                }
            } else if (getState() == GameState.Live) {
                if (UtilTime.elapsed(getStateTime(), UtilMsg.GameTimeLimit * 1000)) {
                    setState(GameState.End);
                }
            } else if (getState() == GameState.End) {
                if (UtilTime.elapsed(getStateTime(), getEndFeastTime())) {
                    setState(GameState.Dead);
                }
            }
        }
    }

    public void checkScoreboard(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {
            if (getState() != GameState.Dead && getState() != GameState.Loading) {
                for (GameTeam team : getLogin().getTeamList()) {
                    updateScoreboard(team);
                }
            }
        }
    }

    public void checkRemoval(PlayerQuitEvent event) {
        getLogin().removePlayer(this, event.getPlayer(), true, true);
    }

    public void checkEnd(UpdateEvent event) {
        if (event.getType() == UpdateType.SEC2) {
            endCheck();
        }
    }

    public void checkEndFireworks(UpdateEvent event) {
        if (event.getType() == UpdateType.FASTEST) {
            if (getState() == GameState.End) {
                Color color = Color.GREEN;

                if (WinnerTeam != null) {
                    color = WinnerTeam.getRawColor();
                }

                Location loc = getSpectatorLocation().clone().add(Math.random() * 160 - 80, 10 + Math.random() * 20, Math.random() * 160 - 80);

                UtilFirework.playFirework(loc, FireworkEffect.Type.BALL_LARGE, color, false, false);
            }
        }
    }

    public void checkRespawn(PlayerRespawnEvent event) {
        if (!inProgress()) {
            //event.setRespawnLocation(Manager.GetLobby().getSpawn());
            //Manager.GetLobby().sendToLobbyWithItems(event.getPlayer(), true);
        } else if (isAlive(event.getPlayer())) {
            event.setRespawnLocation(getLogin().getTeam(event.getPlayer()).GetSpawn());
        } else {
            SetSpectator(event.getPlayer());
            event.setRespawnLocation(getSpectatorLocation());
        }
    }

    int _colorId = 0;

    public void checkDisplayBossBar(UpdateEvent event) {
        if (event.getType() != UpdateType.FASTER) {
            return;
        }
        ChatColor col = ChatColor.RED;
        if (this._colorId == 1) {
            col = ChatColor.YELLOW;
        } else if (this._colorId == 2) {
            col = ChatColor.GREEN;
        } else if (this._colorId == 3) {
            col = ChatColor.AQUA;
        }
        this._colorId = ((this._colorId + 1) % 4);

        String text = col + UtilMsg.Bold + UtilMsg.Website;

        double health = 1.0D;
        /*for (Player player : MapCipherBase.getLobbyWorld().getPlayers()) {
            UtilDisplay.displayTextBar(this.Manager.getPlugin(), player, health, text);
        }*/
    }

    public void tickMap(UpdateEvent event){
        if(event.getType() == UpdateType.TICK && getMap() != null){
            getMap().tick();
        }
    }

    public void onUpdate(UpdateEvent event) {
        tickMap(event);
        getBlood().onUpdate(event);
        getCreature().onUpdate(event);
        getIdle().onUpdate(this, event);
        getBuffer().onUpdate(this, event);
        getCombat().onUpdate(event);
        getProjectiles().onUpdate(this, event);
        getExplosion().onUpdate(event);
        getFlags().onUpdate(this, event);
        checkScoreboard(event);
        checkEndFireworks(event);
        checkEnd(event);
        checkStateUpdate(event);
        checkDisplayBossBar(event);
        checkTeamQueueSize(event);
        checkHelp(event);
    }

    public void onProjectileLaunch(ProjectileLaunchEvent event) {

    }

    public void onBlockBreak(BlockBreakEvent event) {
        getFlags().onBlockBreak(this, event);
        getExplosion().onBlockBreak(event);
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        getFlags().onBlockPlace(this, event);
        getExplosion().onBlockPlace(event);
    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {
        getBuffer().onPlayerRespawn(event);
        checkRespawn(event);
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        getBuffer().onPlayerQuit(event);
        getIdle().onPlayerQuit(event);
        checkRemoval(event);
    }

    public void onPlayerDeath(PlayerDeathEvent event) {
        getBlood().onPlayerDeath(event);
        getCombat().onPlayerDeath(this, event);
        getFlags().onPlayerDeath(this, event);
    }

    public void onEntityDeath(EntityDeathEvent event) {
        getBuffer().onEntityDeath(event);
        getCreature().onEntityDeath(event);
    }

    public void onEntityTarget(EntityTargetEvent event) {
        getBuffer().onEntityTarget(event);
    }

    public void onInventoryClick(InventoryClickEvent event){
        getKits().onInventoryClick(this, event);
    }

    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        getBuffer().onInventoryPickupItem(event);
        getProjectiles().onInventoryPickupItem(event);
        getBlood().onInventoryPickupItem(event);
    }

    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        getFlags().onPlayerPickupItem(this, event);
        getBuffer().onPlayerPickupItem(event);
        getProjectiles().onPlayerPickupItem(event);
        getBlood().onPlayerPickupItem(event);
    }

    public void onEntityDamage(EntityDamageEvent event) {
        getDamage().onEntityDamage(this, event);
        getCombat().onEntityDamage(event);
    }

    public void onCustomDamage(CustomDamageEvent event) {
        getFlags().onCustomDamage(this, event);
        getDamage().onCustomDamage(this, event);
        getBuffer().onCustomDamage(event);
        getProjectiles().onCustomDamage(event);
    }

    public void onClearCombat(ClearCombatEvent event) {
        getCombat().onClearCombat(event);
    }

    public void onEntityExplode(EntityExplodeEvent event) {
        getExplosion().onEntityExplode(this, event);
        getCreature().onEntityExplode(event);
    }

    public void onExplosionPrime(ExplosionPrimeEvent event) {
        getExplosion().onExplosionPrime(event);
    }

    public void onItemSpawn(ItemSpawnEvent event) {
        getExplosion().onItemSpawn(event);
    }

    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        getExplosion().onBlockPistonExtend(event);
    }

    public void onCombatDeath(CombatDeathEvent event) {
        getCombat().onCombatDeath(this, event);
        getGems().onCombatDeath(this, event);
    }

    public void onPlayerStateChange(PlayerStateChangeEvent event) {
        getGems().onPlayerStateChange(this, event);
    }

    public void checkScoreboardClear(GameStateChangeEvent event) {
        UtilScoreboard.clearServer(this, false);
    }

    public void checkTeamGeneration(GameStateChangeEvent event) {
        if (event.getState() == GameState.Recruit) {
            int nextColor = 0;

            ArrayList<Location> allSpawns = getSpawns();
            Code.removeBlocks(allSpawns);

            //if (getType().isSolo()) {
                GameTeam newTeam = new GameTeam(this, nextColor++, allSpawns);
                getLogin().addTeam(this, newTeam);
                setMaxPlayers(newTeam.getMaxSize());
            /*} else {
                for (Location loc : allSpawns) {
                    ArrayList<Location> spawns = new ArrayList();
                    spawns.add(loc);
                    GameTeam newTeam = new GameTeam(this, nextColor++, spawns);
                    getLogin().addTeam(newTeam);
                    setMaxPlayers(getMaxPlayers() + newTeam.getMaxSize());
                }
            }*/
            restrictKits();
            reloadSpectatorSpawn();
            parseData();
        }
    }

    public void checkTeamQueueSize(UpdateEvent event) {
        if (event.getType() == UpdateType.TICK) {
            for (GameTeam team : getLogin().getTeamList()) {
                int amount = 0;
                if (getLogin().getTeamPreferences().containsKey(team)) {
                    amount = getLogin().getTeamPreferences().get(team).size();
                }
                if (team.getTeamEntity() != null) {
                    if (getCountdown() == -1) {
                        team.getTeamEntity().setCustomName(team.getFormattedName() + " Team" + ChatColor.RESET + "  " + amount + " in queue");
                    } else {
                        team.getTeamEntity().setCustomName(team.getPlayersCount(false) + " Players  " + team.getFormattedName() + " Team" + ChatColor.RESET + "  " + amount + " in queue");
                    }
                }
            }
        }
    }

    public void checkChunkUnload(GameStateChangeEvent event) {
        if (event.getState() == GameState.Loading) {
            for (Chunk chunk : getMap().getWorld().getLoadedChunks()) {
                if (chunk != null) {
                    GameServer game = GameAPI.getGameInBounds(new Location(chunk.getWorld(), chunk.getX() * 16, 128, chunk.getZ() * 16));
                    if (game == null) {
                        chunk.unload(false, false);
                    }
                }
            }
        }
    }


    public void onGameStateChange(GameStateChangeEvent event) {
        getDisguises().onGameStateChange(event);
        getGems().onGameStateChange(this, event);
        checkScoreboardClear(event);
        checkTeamGeneration(event);
        checkChunkUnload(event);
        checkPlayerPrepare(event);
        checkTeleportOut(event);
    }


    public void checkTeleportOut(final GameStateChangeEvent event) {
        if (event.getState() == GameState.Dead) {
            int i = 0;
            for (final Player player : event.getGame().getPlayers(false)) {
                i += 2;
                Bukkit.getServer().getScheduler().runTaskLater(ServerPlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        //Manager.Clear(player); Already cleared when sended to lobby
                        //UtilInv.Clear(player);
                        //event.getGame().getBuffer().endBuffer(player, buffer.BufferType.CLOAK, "Spectator");
                        //player.eject();
                        //player.leaveVehicle();
                        /*
                        if (player.getWorld().getId().equals(MapCipherBase.getLobbyWorldName())) {
                            Manager.GetLobby().sendToLobbyWithItems(player, false);
                        } else {
                            Manager.GetLobby().sendToLobbyWithItems(player, true);
                        }*/
                    }
                }, i);
            }
        }
    }

    public void checkPlayerPrepare(GameStateChangeEvent event) {
        if (event.getState() == GameState.Prepare) {
            ArrayList<Player> players = getPlayers(true);
            for (int i = 0; i < players.size(); i++) {
                final Player player = players.get(i);

                Bukkit.getServer().getScheduler().runTaskLater(ServerPlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        //team.spawnTeleport(player); moved to teleport on team join.

                        clearPlayer(player);
                        UtilInv.Clear(player);
                        User pd = ServerPlugin.getPlayerData(player.getName());
                        /*String myKit = pd.getDataManager().getKits().getDefaultKit(null);
                        if (myKit != null && !myKit.isEmpty()) {
                            for (Kit kit : getKits().getKitArray()) {
                                if (kit.getDisplay().equals(myKit)) {
                                    getKits().setKit(player, kit, true);
                                }
                            }
                        }*/
                        getKits().validateKit(GameServer.this, player, getLogin().getTeam(player));
                    }
                }, i);
            }
            Bukkit.getServer().getScheduler().runTaskLater(ServerPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    AnnounceGame();

                    StartPrepareCountdown();

                    GamePrepareCountdownCommence event = new GamePrepareCountdownCommence(GameServer.this);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }
            }, players.size());
        }
    }


    public void onItemDespawn(ItemDespawnEvent event) {
        getFlags().onItemDespawn(this, event);
    }

    public void onBlockSpread(BlockSpreadEvent event) {
        getFlags().onBlockSpread(this, event);
    }

    public void onBlockFade(BlockFadeEvent event) {
        getFlags().onBlockFade(this, event);
    }

    public void onLeavesDecay(LeavesDecayEvent event) {
        getFlags().onLeavesDecay(this, event);
    }

    public void onBlockBurn(BlockBurnEvent event) {
        getFlags().onBlockBurn(this, event);
    }

    public void onCreatureSpawn(CreatureSpawnEvent event) {
        getFlags().onCreatureSpawn(this, event);
        getCreature().onCreatureSpawn(event);
    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        getFlags().onEntityDamageByEntityEvent(event);
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        getFlags().onPlayerInteract(this, event);
        getKits().onPlayerInteract(event);
    }

    public void onChunkUnload(ChunkUnloadEvent event){
        getMap().onChunkUnload(event);
    }

    public void onChunkLoad(ChunkLoadEvent event){
        getMap().onChunkLoad(event);
    }

    public void onPlayerDropItem(PlayerDropItemEvent event) {
        getFlags().onPlayerDropItem(this, event);
    }

    public void onAsyncChat(AsyncPlayerChatEvent event){
        getMessage().onAsyncChat(event);
    }

    public void teleportPlayer(Player player, Location loc, TeleportReason reason) {
        if (player != null && loc != null) {
            loc.getWorld().getChunkAt(loc);
            player.teleport(loc);
        }
    }

    public void display(GameServer gameServer) {
        System.out.println("======================== =============================================");
        System.out.println("GameServer Successfully Loaded");
        System.out.println("Path: " + gameServer.getClass().getCanonicalName());
        System.out.println("======================== =============================================");
    }
}