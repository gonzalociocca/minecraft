package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by noname on 9/2/2017.
 */
public class FlyCommand {
    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equals("fly")) {
            if (args.length <= 0) {
                if (plugin.getDB().getPlayerData(sender.getName()).getRank().getType().isAtLeast(RankType.ELITE)) {
                    Player p = Bukkit.getPlayer(sender.getName());
                    if (p.getWorld().getPVP() && !plugin.getDB().getPlayerData(sender.getName()).getRank().getType().isAtLeast(RankType.Builder)) {
                        p.sendMessage(Code.Color("&c[&aMineLevel&c] &cNo se puede activar el vuelo en mundos PvP"));
                        return true;
                    } else if (!p.getAllowFlight()) {
                        p.sendMessage(Code.Color("&c[&aMineLevel&c] &aVuelo activado, usa /fly para desactivarlo."));
                        p.setAllowFlight(true);
                        return true;
                    } else {
                        p.sendMessage(Code.Color("&c[&aMineLevel&c] &aVuelo desactivado, usa /fly para activarlo."));
                        p.setAllowFlight(false);
                        return true;
                    }
                } else {
                    sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a Rango Minimo: Elite."));
                    return true;
                }
            } else if (args.length == 1) {
                if (plugin.getDB().getPlayerData(sender.getName()).getRank().getType().isAtLeast(RankType.DIOS)) {
                    if (Variable.DiosCooldown.containsKey(sender.getName()) && Variable.DiosCooldown.get(sender.getName()) > System.currentTimeMillis()) {
                        sender.sendMessage(Code.Color("&c[&aMineUltra&c] &aEspera unos segundos antes de otorgar privilegios!"));
                        return true;
                    }
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player p = Bukkit.getPlayer(args[0]);
                        if (plugin.getDB().getPlayerData(p.getName()).getRank().getType().isAtLeast(RankType.ELITE)) {
                            sender.sendMessage(Code.Color("&aNo puedes controlar el vuelo de los jugadores de ELITE en adelante."));
                            return true;
                        } else if (p.getWorld().getPVP()) {
                            sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a No se puede activar el vuelo en mundos PvP"));
                            return true;
                        } else if (!p.getAllowFlight()) {
                            p.sendMessage(Code.Color("&c[&aMineLevel&c] &aVuelo activado, por &b&k|||&b&lDIOS&b&k|||&3&l " + sender.getName() + "."));
                            p.setAllowFlight(true);
                            Variable.DiosCooldown.put(sender.getName(), System.currentTimeMillis() + 30000L);
                            sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a Vuelo activado para " + p.getName()));
                            return true;
                        } else {
                            sender.sendMessage(Code.Color("&c[&aMineLevel&c] &aEl jugador ya tiene el fly activado!"));
                            return true;
                        }
                    } else {
                        sender.sendMessage(Code.Color("&c[&aMineLevel&c] &aJugador no esta conectado."));
                        return true;
                    }
                } else {
                    sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a Rango Minimo: Dios."));
                    return true;
                }


            } else {
                sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a Uso: /fly o /fly <nombre>"));
                return true;
            }
        }
        return false;
    }
}
