package com.gmail.nossr50.util.player;

import com.gmail.nossr50.database.FlatfileDatabaseManager;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.party.PartyManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public final class UserManager
{
    public static void track(McMMOPlayer mcMMOPlayer)
    {
        mcMMOPlayer.getPlayer().setMetadata("mcMMO: Player Data", new FixedMetadataValue(mcMMO.p, mcMMOPlayer));
    }

    public static void remove(Player player)
    {
        player.removeMetadata("mcMMO: Player Data", mcMMO.p);
    }

    public static void clearAll()
    {
        for (Player player : mcMMO.p.getServer().getOnlinePlayers()) {
            remove(player);
        }
    }

    public static void saveAll()
    {/*
        ImmutableList<Player> onlinePlayers = ImmutableList.copyOf(mcMMO.p.getServer().getOnlinePlayers());
        mcMMO.p.debug("Saving mcMMOPlayers... (" + onlinePlayers.size() + ")");
        for (UnmodifiableIterator localUnmodifiableIterator = onlinePlayers.iterator(); localUnmodifiableIterator.hasNext();)
        {
            Player player = (Player)localUnmodifiableIterator.next();
            getPlayer(player).getProfile().save();
        }*/
        try {
            List<PlayerProfile> pros = new ArrayList();
            for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers()) {
                if (mcMMOPlayer.getProfile().isLoaded() && mcMMOPlayer.getProfile().changed) {
                    pros.add(mcMMOPlayer.getProfile());
                }
            }
            ((FlatfileDatabaseManager) mcMMO.getDatabaseManager()).saveAll(pros);
            PartyManager.saveParties();
        }catch(Exception e){
            System.out.println("Couldnt save mcmmo players");
            e.printStackTrace();
        }
    }

    public static Collection<McMMOPlayer> getPlayers()
    {
        Collection<McMMOPlayer> playerCollection = new ArrayList();
        for (Player player : mcMMO.p.getServer().getOnlinePlayers()) {
            if (hasPlayerDataKey(player)) {
                playerCollection.add(getPlayer(player));
            }
        }
        return playerCollection;
    }

    public static McMMOPlayer getPlayer(String playerName)
    {
        return retrieveMcMMOPlayer(playerName, false);
    }

    public static McMMOPlayer getOfflinePlayer(OfflinePlayer player)
    {
        if ((player instanceof Player)) {
            return getPlayer((Player)player);
        }
        return retrieveMcMMOPlayer(player.getName(), true);
    }

    public static McMMOPlayer getOfflinePlayer(String playerName)
    {
        return retrieveMcMMOPlayer(playerName, true);
    }

    public static McMMOPlayer getPlayer(Player player)
    {
        return (McMMOPlayer)((MetadataValue)player.getMetadata("mcMMO: Player Data").get(0)).value();
    }

    private static McMMOPlayer retrieveMcMMOPlayer(String playerName, boolean offlineValid)
    {
        Player player = mcMMO.p.getServer().getPlayerExact(playerName);
        if (player == null)
        {
            if (!offlineValid) {
                mcMMO.p.getLogger().warning("A valid mcMMOPlayer object could not be found for " + playerName + ".");
            }
            return null;
        }
        return getPlayer(player);
    }

    public static boolean hasPlayerDataKey(Entity entity)
    {
        return (entity != null) && (entity.hasMetadata("mcMMO: Player Data"));
    }
}
