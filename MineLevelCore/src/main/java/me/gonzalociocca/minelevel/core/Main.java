/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.minelevel.core;

import me.gonzalociocca.minelevel.core.bungee.ServerConnect;
import me.gonzalociocca.minelevel.core.commands.*;
import me.gonzalociocca.minelevel.core.commands.ServerCommand;
import me.gonzalociocca.minelevel.core.database.DatabaseListener;
import me.gonzalociocca.minelevel.core.database.DatabaseUpdateThread;
import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.enums.UpdateType;
import me.gonzalociocca.minelevel.core.listeners.*;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.SubastaItem;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.server.ServerBase;
import me.gonzalociocca.minelevel.core.server.factions.Factions;
import me.gonzalociocca.minelevel.core.server.lobby.Lobby;
import me.gonzalociocca.minelevel.core.server.rex.Rex;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.updater.Updater;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.economy.MLEconomy;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;


public class Main extends JavaPlugin implements Listener {
    Main plugin = null;

    public Long starttime;

    Updater upd;
    MLEconomy mle = null;
    public ServerConnect connect;

    ServerBase _server;

    @Override
    public void onEnable() {
        this.connect = new ServerConnect();
        connect.enable(this);

        if (Variable.ServerType.equals(SvType.Factions)) {
            mle = new MLEconomy(this);
            setupEconomy();
        }
        starttime = System.currentTimeMillis();
        plugin = this;
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this, this);

        if (Variable.ServerType.equals(SvType.Factions)) {
            _server = new Factions(this);
        } else if (Variable.ServerType.equals(SvType.Lobby)) {
            _server = new Lobby(this);
        } else if (Variable.ServerType.equals(SvType.Rex)) {
            _server = new Rex(this);
        } else {
            _server = new Rex(this);
        }

        upd = new Updater(this);


    }

    @Override
    public void onDisable() {
        if (!Variable.ServerType.isReadOnly()) {

            for (PlayerData pd : Variable.DatabasePlayerMap.values()) {
                if (pd.canSave()) {
                    DatabaseUpdateThread.addToQueue(pd.getName(), pd.getSaveQuery());
                }
            }
        }

        for(int a = 1; a < 3; a++){
            if(getDB().getThread().forceAll()){
                break;
            } else {
                getLogger().log(Level.SEVERE, "DatabaseUpdateThread failed to save all while disabling, Try Number #"+a);
            }
        }
    }

    public ServerBase getSV(){
        return _server;
    }

    public DatabaseListener getDB() {
        return getSV().databaseListener;
    }

    public void setupEconomy() {

        ServicesManager sm = Bukkit.getServicesManager();

        sm.register(Economy.class, mle, Bukkit.getPluginManager().getPlugin("Vault"), ServicePriority.Normal);
    }

    boolean shutdown = false;
    boolean shuttingdown = false;

    @EventHandler
    public void onServer(UpdateEvent event) {
        if (event.getType().equals(UpdateType.SEC30)) {
            ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");
            ZonedDateTime zdt = ZonedDateTime.now(zoneId);
            java.util.Date date = java.util.Date.from(zdt.toInstant());
            if (date.getHours() == 5) {
                if (date.getMinutes() == 59) {
                    if (!shuttingdown) {
                        shuttingdown = true;
                        Bukkit.getServer().shutdown();
                    }
                }
                if (date.getMinutes() == 58) {
                    Bukkit.getServer().savePlayers();
                    shutdown = true;
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        player.kickPlayer(Code.Color("&cReinicio del server, vuelve a entrar en 5 minutos."));
                    }
                } else if (date.getMinutes() == 57) {
                    Bukkit.broadcastMessage(Code.Color("&c&lAnuncio >> &f&lReinicio del server en 1 Minuto"));
                } else if (date.getMinutes() == 56) {
                    Bukkit.broadcastMessage(Code.Color("&c&lAnuncio >> &f&lReinicio del server en 2 Minutos"));
                } else if (date.getMinutes() == 55) {
                    Bukkit.broadcastMessage(Code.Color("&c&lAnuncio >> &f&lReinicio del server en 3 Minutos"));
                }
            }

        }

        if (event.getType().equals(UpdateType.MIN1)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!Variable.ServerType.equals(SvType.Factions) || !getDB().getPlayerData(p.getName()).getRank().getType().isAtLeast(RankType.VIP)) {
                    if (Code.getRandom().nextInt(3) != 1) {
                        continue;
                    }
                }
                p.sendMessage(Code.Color("&b&m&l-----------------------------------"));
                p.sendMessage(Code.Color("&a "));
                p.sendMessage(Code.Color("&a&l[&c&l[Server&c&l] &b&lMineLevel &7-> &a&lYo]"));
                p.sendMessage(Code.Color("&e&lAyudanos a mantener el server"));
                p.sendMessage(Code.Color("&e&lComprando un &a&lVIP en www.minelevel.com"));
                p.sendMessage(Code.Color("&a "));
                p.sendMessage(Code.Color("&b&m&l-----------------------------------"));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Variable.ServerType.equals(SvType.Factions) || Variable.ServerType.equals(SvType.Lobby)) {
            if (
                    FlyCommand.run(this, sender, cmd, label, args) ||
                            GodCommand.run(this, sender, cmd, label, args) ||
                            BankCommand.run(this, sender, cmd, label, args) ||
                            RenameCommand.run(this, sender, cmd, label, args) ||
                            EventoCommand.run(this, sender, cmd, label, args) ||
                            AutoPrivateCommand.run(this, sender, cmd, label, args) ||
                            ModCommand.run(this, sender, cmd, label, args) ||
                            ServerCommand.run(this, sender, cmd, label, args) ||
                            RuneClear.run(this, sender, cmd, label, args)) {
                return true;
            }
        }
        if (MoveToCommand.run(this, sender, cmd, label, args)) {
            return true;
        }
        return false;
    }


    @EventHandler
    public void onServerStartJoin(PlayerLoginEvent event) {
        if (starttime + 5000L > System.currentTimeMillis()) {
            event.setKickMessage(Code.Color("&aEl server se esta iniciando, intenta devuelta en 5segundos"));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

}
