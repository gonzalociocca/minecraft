package me.gonzalociocca.minigame.games.game;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.events.GameStateChangeEvent;
import me.gonzalociocca.minigame.games.GameModifier;
import me.gonzalociocca.minigame.games.GameState;
import me.gonzalociocca.minigame.games.GameTeam;
import me.gonzalociocca.minigame.games.GameType;
import me.gonzalociocca.minigame.games.combat.CombatHandler;
import me.gonzalociocca.minigame.lobby.LobbySign;
import me.gonzalociocca.minigame.map.Cipher.HallMap;
import me.gonzalociocca.minigame.map.Cipher.LobbyMap;
import me.gonzalociocca.minigame.map.Cipher.MapCipherBase;
import me.gonzalociocca.minigame.misc.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by noname on 30/3/2017.
 */
public abstract class GameBase {
    /**
     * Information that should be stored on a game:
     * OnKill: Killer & Killed
     * OnTeam: Players of Team, Team
     * OnDeath: Spectator Data & Send Combat Data
     * OnQuit: Give Reward & Delay Quit.
     * OnGameFinishFeast: Congratulate Winners & Delay Quit
     * OnOutsideGame: Give Rewards
     **/
    Core core;
    GameType gametype;
    MapCipherBase map;

    GameState state;
    ArrayList<GameTeam> teams = new ArrayList();
    GameTeam spectators;
    CombatHandler combathandler;
    GameMode defaultGamemode = GameMode.SURVIVAL;
    double defaultHealth = 20.0D;
    int defaultFood = 20;
    public int countdown = -1;
    public int endGameCountdown = -1;
    GameModifier modifier;
    int gameID;
    CustomLocation hallLocation = null;
    Location hallSpawnLocation = null;
    static int gameCount = 0;

    public GameBase(Core plugin, GameType type, MapCipherBase mapbase, GameModifier mod) {
        modifier = mod;
        core = plugin;
        gametype = type;
        map = mapbase;
        state = GameState.Loading;
        combathandler = new CombatHandler(plugin, this);
        gameID = gameCount++;
    }


    public void loadHall() {
        if (hallLocation == null) {
            return;
        }
        long start = System.currentTimeMillis();
        MapCipherBase buildedHall = core.getMapManager().loadCustomMap("Hall", HallMap.class);
        buildedHall.setCustomWorldName(Core.getWorldGameName());
        buildedHall.setCustomBuildLocation(hallLocation);
        buildedHall.reBuildMap();
        buildedHall.queueBuild(Integer.MAX_VALUE);

        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms to parse and build the hall");

        World world = Bukkit.getWorld(Core.getWorldGameName());

        for (CustomLocation loc : buildedHall.getCustomLocationList(TagEnum.Signs.getID())) {
            Block block = world.getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
            BlockState state = block.getState();
            if (state instanceof Sign) {
                Sign sign = (Sign) state;
                String[] lines = sign.getLines();
                String line1 = lines.length > 0 ? lines[0] : null;
                String line2 = lines.length > 1 ? lines[1] : null;
                String line3 = lines.length > 2 ? lines[2] : null;
                String line4 = lines.length > 3 ? lines[3] : null;
                if (line1 != null) {
                    if (line1.equalsIgnoreCase("First")) {
                        sign.setLine(0, "Primero");
                    } else if (line1.equalsIgnoreCase("Second")) {
                        sign.setLine(0, "Segundo");
                    } else if (line1.equalsIgnoreCase("Third")) {
                        sign.setLine(0, "Tercero");
                    } else if (line1.equalsIgnoreCase("Spawn")) {
                        Location hall = sign.getLocation().clone().add(0.5F, 0, 0.5F);
                        hall.setPitch(0);
                        hall.setYaw(Code.getYawOfSignRotation(sign.getRawData()));
                        hallSpawnLocation = hall;
                    }
                    world.getBlockAt((int) sign.getX(), (int) sign.getY(), (int) sign.getZ()).setType(Material.AIR, true);
                }
            }
        }

    }

    public CustomLocation getHallLocation() {
        return hallLocation;
    }

    public void setHallLocation(CustomLocation customLocation) {
        hallLocation = customLocation;
    }

    public int getGameID() {
        return gameID;
    }

    public GameModifier getModifier() {
        return modifier;
    }

    public void setGameModifier(GameModifier newModifier) {
        modifier = newModifier;
    }

    public int getEndGameCountdown() {
        return endGameCountdown;
    }

    public CombatHandler getCombatHandler() {
        return combathandler;
    }

    public void InitializeGameMap() {
        World world = Bukkit.getWorld(Core.getWorldGameName());

        for (CustomLocation loc : map.getCustomLocationList(TagEnum.PlayerLocationTag.getID())) {
            world.getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()).setType(Material.AIR, true);
        }
        ArrayList<CustomLocation> locs = map.getCustomLocationList(TagEnum.SpectatorLocationTag.getID());
        spectators = new GameTeam(999, true, locs, ChatColor.values().length - 1, false, true, NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
        for (CustomLocation loc : locs) {
            world.getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()).setType(Material.AIR, true);
        }
        CustomLocation hall = map.getCustomLocation(TagEnum.Hall.getID());
        world.getBlockAt((int) hall.getX(), (int) hall.getY(), (int) hall.getZ()).setType(Material.AIR, true);
        setHallLocation(hall);
    }

    public void InitializeCountdown() {
        countdown = 90;
    }

    public abstract void startGame();

    public void startGameEndFeast() {
        loadHall();

        endGameCountdown = 20;

        for (PlayerData pd : getPlayersList(true, true)) {
            pd.getPlayer().sendMessage(Code.Color("&cEl juego ha terminado!"));
            makeSpectator(pd, hallSpawnLocation == null);
            if (hallSpawnLocation != null) {
                pd.getPlayer().setGameMode(GameMode.ADVENTURE);
                pd.getPlayer().teleport(hallSpawnLocation);
            }
        }
    }

    public boolean canJoin(PlayerData pd) {
        return getState().equals(GameState.InWait) || getState().equals(GameState.Starting);
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int newCount) {
        countdown = newCount;
    }

    public boolean isPlayer(PlayerData link) {
        for (GameTeam team : getTeams()) {
            if (team.hasPlayer(link)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSpectator(PlayerData pd) {
        return spectators.hasPlayer(pd);
    }

    public GameTeam getTeam(PlayerData pd) {
        for (GameTeam team : teams) {
            if (team.hasPlayer(pd)) {
                return team;
            }
        }
        return null;
    }

    public GameTeam getPreferedTeam(boolean isVip) {
        GameTeam lessTeam = null;
        int less = 999;
        for (GameTeam team : getTeams()) {
            int size = team.getPlayers().size();
            if (team.getMaxSize() > size && size < less) {
                lessTeam = team;
                less = size;
            }
        }
        if (isVip && lessTeam == null && !getTeams().isEmpty()) {
            for (GameTeam team : getTeams()) {
                if (team.getMaxSize() > 0) {
                    for (PlayerData pd : (ArrayList<PlayerData>) team.getPlayers().clone()) {
                        if (!pd.getPerksManager().getGlobalRanks(false).getRank().getRankType().isAtLeast(Rank.RankType.VIP)) {
                            removeFromGame(pd);
                            return team;
                        }
                    }
                }
            }
        }
        return lessTeam;
    }

    public void makePlayer(PlayerData pd) {
        if (getTeams().isEmpty()) {
            pd.getPlayer().sendMessage(Code.Color("&cMap not working, no teams loaded."));
            return;
        }
        if (isPlayer(pd)) {
            pd.getPlayer().sendMessage(Code.Color("&eYa estas jugando en esta partida!"));
            return;
        }

        GameTeam preferedTeam = getPreferedTeam(pd.getPerksManager().getGlobalRanks(false).getRank().getRankType().isAtLeast(Rank.RankType.VIP));

        if (preferedTeam != null) {
            for (GameBase base : core.getGameManager().getGames()) {
                if (base.hasPlayer(pd)) {
                    base.removeFromGame(pd);
                }
            }
            pd.getPlayer().setGameMode(defaultGamemode);
            pd.getPlayer().setHealth(defaultHealth);
            pd.getPlayer().setFoodLevel(defaultFood);
            preferedTeam.addPlayer(pd);

            if (getCountdown() > 0 && countdown < 30) {
                countdown += 7;
            }

            addPlayerToScoreboard(pd);
        } else {
            pd.getPlayer().sendMessage(Code.Color("&cTodos los lugares estan llenos!"));
        }
    }

    public void removeFromGame(PlayerData pd) {
        if (getState().equals(GameState.InGame) || getState().equals(GameState.Finishing)) {
            giveReward(pd);
        }
        if (isPlayer(pd)) {
            GameTeam team = getTeam(pd);
            team.removePlayer(pd);
        }
        if (isSpectator(pd)) {
            spectators.removePlayer(pd);
        }
        core.getLobbyManager().sendToLobbyWithItems(pd, true);
    }

    public void makeSpectator(PlayerData pd, boolean teleport) {
        if (isPlayer(pd)) {
            getTeam(pd).removePlayer(pd);
        }
        if (!isSpectator(pd)) {
            getCombatHandler().sendCombatData(pd);
            pd.getPlayer().setGameMode(GameMode.SPECTATOR);
            spectators.addPlayer(pd);
        }
    }

    public boolean hasPlayer(PlayerData pd) {
        for (GameTeam team : getTeams()) {
            if (team.hasPlayer(pd)) {
                return true;
            }
        }
        if (spectators.hasPlayer(pd)) {
            return true;
        }
        return false;
    }

    public List<PlayerData> getPlayersList(boolean incplayers, boolean incspectators) {
        List<PlayerData> arraylist = new ArrayList();
        if (incplayers) {
            for (GameTeam team : getTeams()) {
                arraylist.addAll(team.getPlayers());
            }
        }
        if (incspectators) {
            arraylist.addAll(spectators.getPlayers());
        }
        return arraylist;
    }


    public abstract void giveReward(PlayerData link);

    public void resetGame() {
        for (PlayerData pd : getPlayersList(true, true)) {
            removeFromGame(pd);
        }
        getTeams().clear();
        spectators = null;

        hallLocation = null;
        hallSpawnLocation = null;
        endGameCountdown = -1;
        setCountdown(-1);
        map.setFullyLoaded(false);
        map.isLoading = false;
    }

    public List<GameTeam> getTeams() {
        return teams;
    }

    public void changeState(GameState newstate) {
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(this, state, newstate));
        state = newstate;
        for (GameTeam team : getTeams()) {
            team.clearSideBoard();
        }
    }


    public int getPlayersSize(boolean incPlayers, boolean incSpectators) {
        int amount = 0;
        if (incPlayers) {
            for (GameTeam team : teams) {
                amount += team.getPlayers().size();
            }
        }
        if (incSpectators) {
            amount += spectators.getPlayers().size();
        }
        return amount;
    }

    public boolean canFinish() {
        return getPlayersSize(true, false) <= 1;
    }

    public boolean canStartCountdown() {
        return getPlayersSize(true, false) > ((getTeams().size() + 1) / 2);
    }

    public boolean canReset() {
        return getPlayersSize(true, false) < 1 && getEndGameCountdown() <= 0;
    }

    public boolean canBuild() {
        return getState().equals(GameState.InGame);
    }

    public GameState getState() {
        return state;
    }

    public GameType getType() {
        return gametype;
    }

    public MapCipherBase getMap() {
        return map;
    }

    public abstract void updateScoreboard(GameTeam team);

    public void checkSafePlay() {
        if (!getState().equals(GameState.InGame)) {
            return;
        }
        for (PlayerData pd : getPlayersList(true, true)) {
            if (pd.getPlayer() == null || !pd.getPlayer().isOnline() || !isInBounds(pd.getPlayer().getLocation()) || pd.getPlayer().isDead()) {
                removeFromGame(pd);
            }
        }
    }

    public boolean isInBounds(Location loc) {
        CustomLocation loc2 = getMap().getBuildLocation();

        return loc.getWorld().getName().equalsIgnoreCase(Core.getWorldGameName()) && (loc.getX() > loc2.getX() - 250 && loc.getX() < loc2.getX() + 250
                && loc.getY() > -100 && loc.getY() < 300
                && loc.getZ() > loc2.getZ() - 250 && loc.getZ() < loc2.getZ() + 250);
    }

    public void onDeath(){

    }
    public void onDamage(EntityDamageEvent event) {
        if (!getState().equals(GameState.InGame) || !getCombatHandler().canDamage()) {
            event.setCancelled(true);
            return;
        }
        double finalDamage = event.getFinalDamage();

        PlayerData victim = event.getEntity() instanceof Player ? core.getPlayerData(event.getEntity().getName()) : null;
        PlayerData damager = null;
        if (event instanceof EntityDamageByEntityEvent) {
            damager = ((EntityDamageByEntityEvent) event).getDamager() instanceof Player ? core.getPlayerData(((EntityDamageByEntityEvent) event).getDamager().getName()) : null;

            EntityDamageByEntityEvent eventcast = (EntityDamageByEntityEvent) event;
            if (victim != null) {
                Player player = (Player) event.getEntity();
                if (eventcast.getDamager() != null) {
                    if (damager != null) {
                        if (!getCombatHandler().canPvP()) {
                            event.setCancelled(true);
                            return;
                        }
                        getCombatHandler().getData(core.getPlayerData(player.getName())).addDamageReceived(damager, finalDamage, event.getCause());
                    } else {
                        if (!getCombatHandler().canPvE()) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
            if (damager != null) {
                if (victim != null) {
                    if (!getCombatHandler().canPvP()) {
                        event.setCancelled(true);
                        return;
                    }
                    getCombatHandler().getData(damager).addDamageDone(victim, finalDamage, event.getCause());
                } else {
                    if (!getCombatHandler().canPvE()) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

        if (victim != null) {
            double newHealth = ((Player) event.getEntity()).getHealth() - finalDamage;
            if (newHealth <= 0.0) {
                if (damager != null) {
                    getCombatHandler().getData(damager).addKill(victim);
                    getCombatHandler().getData(victim).addDeath(damager);
                } else {
                    getCombatHandler().getData(victim).addDeath(null);
                }
                makeSpectator(victim, true);
                Location loc = event.getEntity().getLocation();
                loc.getWorld().playEffect(loc, Effect.FIREWORKS_SPARK, 1);
                event.setCancelled(true);
            }
        }
    }

    public abstract void addPlayerToScoreboard(PlayerData pd);

    public abstract void removePlayerFromScoreboard(GameTeam fromTeam, PlayerData pd);
}
