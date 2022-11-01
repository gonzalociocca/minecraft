package server.instance.core.join;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import server.ServerPlugin;
import server.api.GameAPI;
import server.common.Code;
import server.instance.GameServer;
import server.instance.core.kit.Kit;
import server.instance.misc.GameState;
import server.instance.misc.GameTeam;
import server.user.User;
import server.util.F;
import server.util.UtilMsg;
import server.util.UtilPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GameJoin {

    //todo: Team Based
    // 1 Team = <X Players>
    // 1 Team = <X Spawn Location>, Min=1, Max=NaN
    // 1 Player = 1 Scoreboard not related to teams

    private final HashMap<GameTeam, ArrayList<Player>> _teamPreference = new HashMap();
    private final ArrayList<GameTeam> _teamList = new ArrayList();
    public HashMap<GameTeam, ArrayList<Player>> getTeamPreferences() {
        return _teamPreference;
    }

    public void RemoveTeamPreference(Player player) {
        for (ArrayList queue : _teamPreference.values())
            queue.remove(player);
    }

    public void TeamDefaultJoin(GameServer game) {
        for (Player player : game.getPlayers(false)) {
            if (player.isDead()) {
                player.sendMessage(F.main("Afk Monitor", Code.Color(UtilMsg.AFK)));
                //Manager.GetLobby().sendToLobbyWithItems(player, true);
            } else if (!game.isPlaying(player)) {
                PlayerAdd(game, player, null);
            }
        }
    }

    public boolean PlayerAdd(GameServer game, Player player, GameTeam team) {
        if (game.getState() != GameState.Recruit) {
            return false;
        }
        if (game.getPlayersCount(true) >= game.getMaxPlayers()) {
            return false;
        }
        if (team == null) {
            team = chooseTeam(player);
        }
        if (team == null) {
            return false;
        }

        GameServer mainServer = GameAPI.getServerInterface().getMainServer();
        mainServer.getLogin().removePlayer(game, player, true, true);
        if(mainServer.subServerList != null && !mainServer.subServerList.isEmpty()){
            for(GameServer gameServer : mainServer.subServerList){
                gameServer.getLogin().removePlayer(game, player, true, true);
            }
        }

        User pd = ServerPlugin.getPlayerData(player.getName());
        /*String defaultKitPermission = pd.getDataManager().getKits() .getKits().getDefaultKit(getGame().getType());
        GameKits kits = getGame().getKits();

        if(defaultKitPermission != null) {
            kits.selectKit(player, defaultKitPermission, false);
        } else {
            kits.selectKit(player, kits.getFirstKit(), false);
        }
        */
        setPlayerTeam(game, player, team, true);

        return true;
    }

    public void TeamPreferenceSwap(GameServer game) {
        Iterator<Player> queueIterator;
        for (Iterator localIterator1 = getTeamPreferences().keySet().iterator(); localIterator1.hasNext(); ) {
            GameTeam team = (GameTeam) localIterator1.next();

            for (queueIterator = team.getPlayers(false).iterator(); queueIterator.hasNext(); ) {
                Player player = queueIterator.next();

                GameTeam currentTeam = getTeam(player);
                if (currentTeam != null) {
                    if (team == currentTeam) {
                        queueIterator.remove();
                    } else {
                        for (Player other : team.getPlayers(false)) {
                            if (!other.equals(player)) {
                                GameTeam otherPref = GetTeamPreference(other);
                                if (otherPref != null) {
                                    if (otherPref.equals(currentTeam)) {
                                        UtilPlayer.message(player, F.main("Team", Code.Color(UtilMsg.TeamChange) + F.elem(new StringBuilder().append(team.getColor()).append(other.getName()).toString()) + "."));
                                        UtilPlayer.message(other, F.main("Team", Code.Color(UtilMsg.TeamChange) + F.elem(new StringBuilder().append(currentTeam.getColor()).append(player.getName()).toString()) + "."));


                                        queueIterator.remove();
                                        setPlayerTeam(game, player, team, false);
                                        setPlayerTeam(game, other, currentTeam, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void TeamPreferenceJoin(GameServer game) {
        for (GameTeam team : getTeamPreferences().keySet()) {
            Iterator<Player> queueIterator = ((ArrayList) getTeamPreferences().get(team)).iterator();
            while (queueIterator.hasNext()) {
                Player player = queueIterator.next();
                if (!CanJoinTeam(team)) {
                    break;
                }
                queueIterator.remove();
                if (!game.isPlaying(player)) {
                    PlayerAdd(game, player, team);
                } else {
                    setPlayerTeam(game, player, team, false);
                }
            }
        }
    }

    public void removePlayer(GameServer game, Player player, boolean forceHardRemove, boolean forceReward) {
        GameTeam team = getTeam(player);
        if (team != null) {
            RemoveTeamPreference(player);
            Kit kit = game.getKits().getKit(player);
            if(kit != null){
                //kit.removePlayer(player);
            }

            if (forceReward) {
                game.getGems().RewardGems(game, player, true);
            }
            if (!forceHardRemove && game.inProgress()) {
                team.setPlayerState(player, GameTeam.PlayerState.OUT);
            } else {
                //todo: game.removePlayerFromScoreboard(team, player);
                team.RemovePlayer(player);
            }
        }
    }

    public void setPlayerTeam(GameServer game, Player player, GameTeam team, boolean spawnTeleport) {
        player.setGameMode(game.defaultGameMode);

        GameTeam pastTeam = getTeam(player);
        if (pastTeam != null) {
            pastTeam.RemovePlayer(player);
        }

        team.AddPlayer(player);

        game.getKits().validateKit(game, player, team);
        //todo: game.addPlayerToScoreboard(player, team, false);

        if (spawnTeleport) {
            team.spawnTeleport(player);
        }
    }

    public ArrayList<GameTeam> getTeamList() {
        return _teamList;
    }

    public GameTeam chooseTeam(Player player) {
        GameTeam team = null;

        for (GameTeam _teamList1 : _teamList) {
            if ((team == null) || (_teamList1.getSize() < team.getSize())) {
                team = _teamList1;
            }
        }

        return team;
    }

    public void addTeam(GameServer game, GameTeam team) {
        getTeamList().add(team);

        team.setSpawnRequirement(game.SpawnDistanceRequirement);
    }

    public boolean hasTeam(GameTeam team) {
        for (GameTeam cur : getTeamList()) {
            if (cur.equals(team))
                return true;
        }
        return false;
    }

    public GameTeam getTeam(Player player) {
        if (player == null) {
            return null;
        }
        for (GameTeam team : _teamList) {
            if (team.hasPlayer(player))
                return team;
        }
        return null;
    }

    public String getColor(Player player) {
        GameTeam team = getTeam(player);
        String color = "" + ChatColor.GRAY;
        if (team != null) {
            color = team.getColor();
        }
        return color;
    }

    public GameTeam getTeam(String color) {
        for (GameTeam team : _teamList) {
            if (team.getColor().equals(color))
                return team;
        }
        return null;
    }

    public GameTeam getTeam(String player, boolean aliveOnly) {
        for (GameTeam team : getTeamList()) {
            if (team.hasPlayer(player, aliveOnly))
                return team;
        }
        return null;
    }

    public boolean CanJoinTeam(GameTeam team) {
        return team.getPlayersCount(true) < Math.max(1, team.getMaxSize());
    }

    public GameTeam GetTeamPreference(Player player) {
        for (GameTeam team : _teamPreference.keySet()) {
            if ((_teamPreference.get(team)).contains(player)) {
                return team;
            }
        }
        return null;
    }

    public String GetTeamQueuePosition(Player player) {
        ArrayList queue = new ArrayList();
        int i = 0;
        for (Iterator localIterator = _teamPreference.values().iterator(); i < queue.size(); localIterator.hasNext()
                ) {
            queue = (ArrayList) localIterator.next();

            i = 0;


            if (queue.get(i).equals(player))
                return i + 1 + "/" + queue.size();
            i++;
        }

        return "First";
    }

    public void InformQueuePositions() {
        for (GameTeam team : _teamPreference.keySet()) {
            for (Player player : team.getPlayers(false)) {
                UtilPlayer.message(player, F.main("Team", "You are " + F.elem(GetTeamQueuePosition(player)) + " in the queue for " + F.elem(new StringBuilder(String.valueOf(team.getFormattedName())).append(" Team").toString()) + "."));
            }
        }
    }

    public void AddTeamPreference(Player player, GameTeam team) {
        GameTeam past = GetTeamPreference(player);

        GameTeam current = getTeam(player);
        if ((current != null) && (current.equals(team))) {
            RemoveTeamPreference(player);
            UtilPlayer.message(player, F.main("Team", Code.Color(UtilMsg.AlreadyIn.replace("%s", F.elem(team.getFormattedName())))));
            return;
        }
        if ((past == null) || (!past.equals(team))) {
            if (past != null) {
                RemoveTeamPreference(player);
            }
            if (!getTeamPreferences().containsKey(team)) {
                getTeamPreferences().put(team, new ArrayList());
            }
            getTeamPreferences().get(team).add(player);
        }

        String pos = F.elem(GetTeamQueuePosition(player));

        if (pos.toLowerCase().contains("unkn")) {
            pos = "First";
        }
        String are = Code.Color(UtilMsg.Queue).replace("%pos", pos).replace("%s", team.getFormattedName());
        UtilPlayer.message(player, F.main("Team", are));

    }

    public void TeamClick(GameServer game, Player player, GameTeam team) {
        if (game.getState() == GameState.Recruit && hasTeam(team)) {
            AddTeamPreference(player, team);
        }
    }

    public void cleanAll(){
        _teamList.clear();
        _teamPreference.clear();
    }
}
