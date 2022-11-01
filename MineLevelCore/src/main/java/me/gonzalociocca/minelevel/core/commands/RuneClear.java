package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by noname on 27/3/2017.
 */
public class RuneClear {

    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (Variable.ServerType.equals(SvType.Factions) && cmd.getName().equalsIgnoreCase("runeclear")) {
            if(sender instanceof Player){
                Player player = (Player)sender;
                if(player.getItemInHand()!=null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()){
                    if(player.getItemInHand().getType().equals(Material.EMERALD)){
                        PlayerData pd = plugin.getDB().getPlayerData(sender.getName());
                        player.setItemInHand(new ItemStack(Material.AIR));
                        pd.addDiamonds(75);
                        sender.sendMessage(Code.Color("&e"+(75)+" diamantes añadidos a tu cuenta."));
                        player.updateInventory();
                        return true;
                    }
                }
            }
            sender.sendMessage(Code.Color("&e&nConversion de Runas"));
            sender.sendMessage(Code.Color("&f"));
            sender.sendMessage(Code.Color("&f> Runas No Colocadas = $75 Cada Una."));
            sender.sendMessage(Code.Color("&f> Runas Colocadas = $150 Cada Una."));
            sender.sendMessage(Code.Color("&fPara vender tus runas usa /runeclear con tu item en la mano."));
            return true;
        }
        return false;
    }
}
