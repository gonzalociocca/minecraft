package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by noname on 9/2/2017.
 */
public class AutoPrivateListener implements Listener {
    Main plugin;
    public AutoPrivateListener(Main main){
        plugin = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Variable.AutoPrivates.put(event.getPlayer().getName(), true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if (Variable.AutoPrivates.containsKey(event.getPlayer().getName())) {
            Variable.AutoPrivates.remove(event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPrivatizar(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getItemInHand() == null) {
            return;
        }
        if (!event.getItemInHand().getType().equals(Material.CHEST)) {
            return;
        }
        if (!event.canBuild()) {
            event.setCancelled(true);
            return;
        }

        Player pe = event.getPlayer();
        Boolean canAutoPrivate = Variable.AutoPrivates.get(pe.getName());
        if (canAutoPrivate == null) {
            canAutoPrivate = true;
            Variable.AutoPrivates.put(pe.getName(), canAutoPrivate);
        }
        if (canAutoPrivate) {
            BlockFace signface = BlockFace.EAST;
            Block bsign = event.getBlockPlaced().getRelative(BlockFace.EAST);
            if (!bsign.isEmpty()) {
                signface = BlockFace.WEST;
                bsign = event.getBlockPlaced().getRelative(BlockFace.WEST);
            }
            if (!bsign.isEmpty()) {
                signface = BlockFace.NORTH;
                bsign = event.getBlockPlaced().getRelative(BlockFace.NORTH);
            }
            if (!bsign.isEmpty()) {
                signface = BlockFace.SOUTH;
                bsign = event.getBlockPlaced().getRelative(BlockFace.SOUTH);
            }
            if (bsign.isEmpty()) {


                int a = -1;
                if (signface.equals(BlockFace.EAST)) {
                    a = 5;
                }
                if (signface.equals(BlockFace.WEST)) {
                    a = 4;
                }
                if (signface.equals(BlockFace.SOUTH)) {
                    a = 3;
                }
                if (signface.equals(BlockFace.NORTH)) {
                    a = 2;
                }
                try {
                    if (!event.getBlockPlaced().getType().equals(Material.CHEST)) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                bsign.setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) a, true);

                Sign se = (Sign) bsign.getState();

                se.setLine(0, "[Private]");
                se.setLine(1, pe.getName());
                se.update();
                pe.sendMessage(Code.Color("&c[MineLevel]&2 Cofre privatizado automaticamente, usa /auto para modo manual"));
            }
        }
    }
}
