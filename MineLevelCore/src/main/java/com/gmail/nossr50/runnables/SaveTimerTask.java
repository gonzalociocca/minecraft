package com.gmail.nossr50.runnables;

import com.gmail.nossr50.database.FlatfileDatabaseManager;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.mcMMO;
import com.google.common.collect.Lists;
import org.bukkit.scheduler.BukkitRunnable;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.player.UserManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SaveTimerTask extends BukkitRunnable {
    int tasknamecount = 0;

    Thread tr;
    @Override
    public void run() {
        tasknamecount++;
        tr = new Thread(new Runnable() {
            @Override
            public void run() {
                List<PlayerProfile> pros = new ArrayList();
                for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers()) {
                    if (mcMMOPlayer.getProfile().isLoaded() && mcMMOPlayer.getProfile().changed) {
                        pros.add(mcMMOPlayer.getProfile());
                    }
                }
                ((FlatfileDatabaseManager)mcMMO.getDatabaseManager()).saveAll(pros);
                PartyManager.saveParties();
            }
        });
        tr.setName("mcMMO Saving Task #"+tasknamecount);
        tr.setDaemon(false);
        tr.start();
    }

}
