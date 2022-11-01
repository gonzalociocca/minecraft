package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by noname on 9/2/2017.
 */
public class GodCommand {

    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (Variable.ServerType.equals(SvType.Factions) && cmd.getName().equals("god")) {
            if (args.length <= 0) {
                if (plugin.getDB().getPlayerData(sender.getName()).getRank().getType().isAtLeast(RankType.ELITE)) {
                    if (Variable.GodList.contains(sender.getName())) {
                        sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a Modo Invencible Desactivado."));
                        Variable.GodList.remove(sender.getName());
                        return true;
                    } else {
                        sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a Modo Invencible Activado."));
                        Variable.GodList.add(sender.getName());
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
                            sender.sendMessage(Code.Color("&aNo puedes controlar el modo invencible de los jugadores de ELITE en adelante."));
                            return true;
                        } else if (!Variable.GodList.contains(p.getName())) {
                            p.sendMessage(Code.Color("&c[&aMineLevel&c] &aModo invencible activado por &b&k|||&b&lDIOS&b&k|||&3&l " + sender.getName() + "."));
                            Variable.GodList.add(p.getName());
                            Variable.DiosCooldown.put(sender.getName(), System.currentTimeMillis() + 30000L);
                            sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a Modo Invencible activado para " + p.getName()));
                            return true;
                        } else {
                            sender.sendMessage(Code.Color("&c[&aMineLevel&c] &aEl jugador ya tiene el modo invencible activado!"));
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
                sender.sendMessage(Code.Color("&c[&aMineLevel&c]&a Uso: /god o /god <nombre>"));
                return true;
            }
        }
        return false;
    }
}
