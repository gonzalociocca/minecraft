package me.crafter.mc.lockettepro;

import java.util.UUID;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.permission.Permission;

public class Dependency {

    protected static WorldGuardPlugin worldguard = null;
    protected static Plugin residence = null;
    protected static Plugin towny = null;
    protected static Plugin factions = null;
    protected static Plugin vault = null;
    protected static Permission permission = null;
    protected static Plugin askyblock = null;
    protected static Plugin plotsquared = null;

    public Dependency(Plugin plugin){
        // WorldGuard
        Plugin worldguardplugin = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldguardplugin == null || !(worldguardplugin instanceof WorldGuardPlugin)) {
            worldguard = null;
        } else {
            worldguard = (WorldGuardPlugin)worldguardplugin;
        }
        // Residence
        residence = plugin.getServer().getPluginManager().getPlugin("Residence");
        // Towny
        towny = plugin.getServer().getPluginManager().getPlugin("Towny");
        // Factions
        factions = plugin.getServer().getPluginManager().getPlugin("Factions");
        // Vault
        vault = plugin.getServer().getPluginManager().getPlugin("Vault");
        if (vault != null){
            RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
            permission = rsp.getProvider();
        }
        // ASkyblock
        askyblock = plugin.getServer().getPluginManager().getPlugin("ASkyblock");
        // PlotSquared
        plotsquared = plugin.getServer().getPluginManager().getPlugin("PlotSquared");

    }

    @SuppressWarnings("deprecation")
    public static boolean isProtectedFrom(Block block, Player player){
        if (worldguard != null){
            if (!worldguard.canBuild(player, block)) return true;
        }
        if (factions != null){
            try {
                Faction faction = Board.getFactionAt(block);
                if (faction != null && !faction.isNone()){
                   FPlayer mplayer = FPlayers.i.get(player);
                    if (mplayer != null){
                        Faction playerfaction = mplayer.getFaction();
                        if (faction != playerfaction){
                            return true;
                        }
                    }
                }
            } catch (Exception e){}
        }
        return false;
    }

    public static boolean isTownyTownOrNationOf(String line, Player player){

        if (factions != null){
            try {
                FPlayer mplayer = FPlayers.i.get(player);
                if (mplayer != null){
                    Faction faction = mplayer.getFaction();
                    if (faction != null){
                        if (line.equals("[" + faction.getTag() + "]")) return true;
                    }
                }
            } catch (Exception e) {}
        }
        return false;
    }

    public static boolean isPermissionGroupOf(String line, Player player){
        if (vault != null){
            try {
                String[] groups = permission.getPlayerGroups(player);
                for (String group : groups){
                    if (line.equals("[" + group + "]")) return true;
                }
            } catch (Exception e){}
        }
        return false;
    }

}