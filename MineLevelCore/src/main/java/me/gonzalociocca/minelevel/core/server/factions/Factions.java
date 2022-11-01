package me.gonzalociocca.minelevel.core.server.factions;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.database.DatabaseListener;
import me.gonzalociocca.minelevel.core.listeners.*;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.server.ServerBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

/**
 * Created by noname on 22/4/2017.
 */
public class Factions extends ServerBase {


    public Factions(Main main){
        super(main, true, true, true, true, true, true, true, true, true, false, false);

        Iterator<Recipe> s = Bukkit.getServer().recipeIterator();
        while (s.hasNext()) {
            Recipe s2 = s.next();
            if (s2 != null && s2.getResult().getType().equals(Material.GOLDEN_APPLE)) {
                s.remove();
            }
        }

        Bukkit.getServer().addRecipe(Code.getRecipe1());
        Bukkit.getServer().addRecipe(Code.getRecipe2());
        Bukkit.getServer().addRecipe(Code.getRecipe3());

        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new BukkitRunnable() {
            @Override
            public void run() {
                Variable.Location3 = new Location(Bukkit.getWorld("world_the_end"), -220, 30, -111);
                main.setupEconomy();
                Code.addItemsToSubasta();
            }
        }, 200L);
    }



}
