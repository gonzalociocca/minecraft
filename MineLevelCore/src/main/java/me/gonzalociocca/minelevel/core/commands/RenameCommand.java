package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by noname on 9/2/2017.
 */
public class RenameCommand {
    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (Variable.ServerType.equals(SvType.Factions) && cmd.getName().equalsIgnoreCase("rename")) {
            if (args.length < 1) {
                sender.sendMessage(Code.Color("&c[&aMineLevel&c] &cPara cambiar el nombre de el item en tu mano"));
                sender.sendMessage(Code.Color("&ausa /rename <nombre>"));
                return true;
            } else if (plugin.getDB().getPlayerData(sender.getName()).getRank().getType().isAtLeast(RankType.VIP)) {
                Player p = (Player) sender;
                if (!Code.isEquipment(p.getItemInHand())) {
                    sender.sendMessage(Code.Color("&c&lSolo se puede renombrar equipos y armas"));
                    return true;
                }
                String name = "";

                for (String s : args) {
                    if (s != null) {
                        name = name + " " + s;
                    }
                }
                name = name.substring(1);
                name = Code.Color(name);
                if (name.length() >= 48) {
                    name = name.substring(0, 47);
                    sender.sendMessage(Code.Color("&c[&aMineLevel&c] &c Nombre del item limitado a 48 caracteres."));
                }

                ItemMeta im = p.getItemInHand().getItemMeta();
                im.setDisplayName(name);
                p.getItemInHand().setItemMeta(im);
                sender.sendMessage(Code.Color("&c[&aMineLevel&c] &aNuevo nombre: " + name));
                return true;
            } else {
                sender.sendMessage(Code.Color("&cComando /rename solo esta disponible para vips!"));
            }
            return true;
        }
        return false;
    }
}
