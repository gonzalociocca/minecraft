package mineultra.game.center.addons;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.HashSet;
import mineultra.core.common.util.C;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.meta.ItemMeta;
import mineultra.game.center.game.GameTeam;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import mineultra.game.center.centerManager;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilGear;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;

public class CompassAddon extends MiniPlugin
{
    public centerManager Manager;
    
    public CompassAddon(final JavaPlugin plugin, final centerManager manager) {
        super("Compass Addon", plugin);
        this.Manager = manager;
    }
    
    @EventHandler
    public void Update(final UpdateEvent event) {
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        if (this.Manager.GetGame() == null) {
            return;
        }
        if (!this.Manager.GetGame().IsLive()) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, j = 0; j < length; ++j) {
            final Player player = players[j];
            if (this.Manager.GetGame().CompassEnabled || !this.Manager.GetGame().IsAlive(player)) {
                final GameTeam team = this.Manager.GetGame().GetTeam(player);
                Player target = null;
                GameTeam targetTeam = null;
                double bestDist = 0.0;
                for (final Player other : this.Manager.GetGame().GetPlayers(true)) {
                    if (other.equals(player)) {
                        continue;
                    }
                    final GameTeam otherTeam = this.Manager.GetGame().GetTeam(other);
                    if (this.Manager.GetGame().GetTeamList().size() > 1 && team != null && team.equals(otherTeam) && this.Manager.GetGame().IsAlive(player)) {
                        continue;
                    }
                    final double dist = UtilMath.offset((Entity)player, (Entity)other);
                    if (target != null && dist >= bestDist) {
                        continue;
                    }
                    target = other;
                    targetTeam = otherTeam;
                    bestDist = dist;
                }
                if (target != null) {
                    if (!player.getInventory().contains(Material.COMPASS)) {
                        player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.COMPASS) });
                    }
                    player.setCompassTarget(target.getLocation());
                    for (final int i : player.getInventory().all(Material.COMPASS).keySet()) {
                        final ItemStack stack = player.getInventory().getItem(i);
                        final double heightDiff = target.getLocation().getY() - player.getLocation().getY();
                        final ItemMeta itemMeta = stack.getItemMeta();
                        itemMeta.setDisplayName("    " + C.cWhite + C.Bold + "Jugador mas cercano: " + targetTeam.GetColor() + target.getName() + "    " + C.cWhite + C.Bold + "Distancia: " + targetTeam.GetColor() + UtilMath.trim(1, bestDist) + "    " + C.cWhite + C.Bold + "Altura: " + targetTeam.GetColor() + UtilMath.trim(1, heightDiff));
                        stack.setItemMeta(itemMeta);
                        player.getInventory().setItem(i, stack);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void DropItem(final PlayerDropItemEvent event) {
        if (this.Manager.GetGame() == null || !this.Manager.GetGame().CompassEnabled) {
            return;
        }
        if (!UtilInv.IsItem(event.getItemDrop().getItemStack(), Material.COMPASS, (byte)0)) {
            return;
        }
        event.setCancelled(true);
        UtilPlayer.message((Entity)event.getPlayer(), F.main("Game", "No puedes dropearto " + F.item("Target Compass") + "."));
    }
    
    @EventHandler
    public void DeathRemove(final PlayerDeathEvent event) {
        if (this.Manager.GetGame() == null || !this.Manager.GetGame().CompassEnabled) {
            return;
        }
        final HashSet<ItemStack> remove = new HashSet<ItemStack>();
        for (final ItemStack item : event.getDrops()) {
            if (UtilInv.IsItem(item, Material.COMPASS, (byte)0)) {
                remove.add(item);
            }
        }
        for (final ItemStack item : remove) {
            event.getDrops().remove(item);
        }
    }
    
    @EventHandler
    public void InventoryClick(final InventoryClickEvent event) {
    }
    
    @EventHandler
    public void PlayerInteract(final PlayerInteractEvent event) {
        if (this.Manager.GetGame() == null || !this.Manager.GetGame().CompassEnabled) {
            return;
        }
        final Player player = event.getPlayer();
        if (!UtilGear.isMat(player.getItemInHand(), Material.COMPASS)) {
            return;
        }
        if (this.Manager.GetGame().IsAlive(player)) {
            return;
        }
        final GameTeam team = this.Manager.GetGame().GetTeam(player);
        Player target = null;
        double bestDist = 0.0;
        for (final Player other : this.Manager.GetGame().GetPlayers(true)) {
            final GameTeam otherTeam = this.Manager.GetGame().GetTeam(other);
            if (this.Manager.GetGame().GetTeamList().size() > 1 && team != null && team.equals(otherTeam) && this.Manager.GetGame().IsAlive(player)) {
                continue;
            }
            final double dist = UtilMath.offset((Entity)player, (Entity)other);
            if (target != null && dist >= bestDist) {
                continue;
            }
            target = other;
            bestDist = dist;
        }
        if (target != null) {
            player.teleport(target.getLocation().add(0.0, 1.0, 0.0));
        }
    }
}
