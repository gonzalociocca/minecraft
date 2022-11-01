package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.EventType;
import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.listeners.FactionsEventListener;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by noname on 9/2/2017.
 */
public class EventoCommand {
    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (Variable.ServerType.equals(SvType.Factions) && cmd.getName().equalsIgnoreCase("evento")) {
            EventType event = FactionsEventListener.getLastEvent().getEventType();
            if (args.length == 1 && args[0].equals("skip") && sender.hasPermission("minelevel.mod")) {
                FactionsEventListener.getLastEvent().skip();
            }
            else if(args.length==1 && args[0].equals("subastas") && sender instanceof Player){
                Player player = (Player)sender;
                if(!event.equals(EventType.Subastas)){
                    player.sendMessage(Code.Color("&a> &nSubastas"));
                    player.sendMessage(Code.Color("&eLa subasta ha finalizado"));
                    player.sendMessage(Code.Color("&eespera hasta el siguiente evento de subastas!"));
                    player.sendMessage(Code.Color("&eencontraras muchos items de tu agrado."));
                } else {
                    player.openInventory(Variable.SubastasInventory);
                }
            }
            else if (event.equals(EventType.MasXP)) {
                sender.sendMessage(Code.Color("&a> &nEvento MasXP"));
                sender.sendMessage(Code.Color("&eXP al cazar monstruos y animales aumentada en un "+Variable.ActiveXpBoost+"%"));
                sender.sendMessage(Code.Color("&eTiempo Restante: " + FactionsEventListener.getLastEvent().getTimeLeft()));
            } else if(event.equals(EventType.Subastas)){
                sender.sendMessage(Code.Color("&a> &nSubastas"));
                sender.sendMessage(Code.Color("&eVe al npc subastas en /spawn para abrir el menu de subastas"));
                sender.sendMessage(Code.Color("&edonde encontraras items de edicion limitada"));
                sender.sendMessage(Code.Color("&ea diferentes precios y podras comprarlos si hay stock suficiente."));
                sender.sendMessage(Code.Color("&eTiempo Restante: " + FactionsEventListener.getLastEvent().getTimeLeft()));
            }
            else if (event.equals(EventType.Mineria)) {
                sender.sendMessage(Code.Color("&c[Evento]&a Mina minerales de todo tipo"));
                sender.sendMessage(Code.Color("&amientras mas minerales mines, mayor sera la recompensa"));
                sender.sendMessage(Code.Color("&ay los pocos expertos recibiran premios extra"));
                sender.sendMessage(Code.Color("&cTiempo Restante: " + FactionsEventListener.getLastEvent().getTimeLeft()));
            } else if (event.equals(EventType.Coliseo)) {
                if (sender instanceof Player && !FactionsEventListener.hasColiseoStarted()) {
                    Player p = Bukkit.getPlayer(sender.getName());
                    if(p!=null) {
                        Variable.ColiseoPreviousLocations.put(p.getName(),p.getLocation());
                        Bukkit.getPlayer(sender.getName()).teleport(Variable.Location3);
                    }
                } else {
                    sender.sendMessage(Code.Color("&c[Evento]&a El evento ya ha comenzado usa /evento para ingresar a la proxima!"));
                }
                sender.sendMessage(Code.Color("&c[Evento] &aNo se pierden items al morir"));
                sender.sendMessage(Code.Color("&cEl evento pvp empezara pronto, espera aqui!"));
            } else if (event.equals(EventType.Min5)) {
                sender.sendMessage(Code.Color("&c[Evento]&a Hay 30 minutos de pausa entre cada evento"));
                sender.sendMessage(Code.Color("&aSiguiente evento: " + FactionsEventListener.getLastEvent().getNextGameEvent().getTitle()));
                sender.sendMessage(Code.Color("&aen " + FactionsEventListener.getLastEvent().getTimeLeft()));
            }
            return true;
        }
        return false;
    }
}
