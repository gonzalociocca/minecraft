package server.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import server.ServerPlugin;
import server.api.GameAPI;
import server.api.MenuAPI;
import server.command.CommandHandler;
import server.common.event.GameStateChangeEvent;
import server.common.event.PlayerStateChangeEvent;
import server.common.event.UpdateEvent;
import server.instance.GameServer;
import server.instance.core.combat.event.ClearCombatEvent;
import server.instance.core.combat.event.CombatDeathEvent;
import server.instance.core.damage.CustomDamageEvent;

import java.util.regex.Pattern;

/**
 * Created by noname on 18/4/2017.
 */
public class VanillaListener implements Listener {


    public VanillaListener() {
        Bukkit.getPluginManager().registerEvents(this, ServerPlugin.getInstance());
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        GameServer mainServer = GameAPI.getServerInterface().getMainServer();
        mainServer.onUpdate(event);
        if(mainServer.subServerList != null && !mainServer.subServerList.isEmpty()){
            for(GameServer gameServer : mainServer.subServerList){
                gameServer.onUpdate(event);
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() != null) {
            GameServer game = GameAPI.getGameInBounds(event.getEntity().getLocation());
            if (game != null) {
                game.onProjectileLaunch(event);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        GameServer game = GameAPI.getGameOf(event.getPlayer());
        if (game != null) {
            game.onBlockBreak(event);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockPlaceEvent event) {
        GameServer game = GameAPI.getGameOf(event.getPlayer());
        if (game != null) {
            game.onBlockPlace(event);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        GameServer game = GameAPI.getGameOf(event.getPlayer());
        if(game != null){
            game.onPlayerRespawn(event);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        GameServer game = GameAPI.getGameOf(event.getPlayer());
        if(game != null){
            game.onPlayerQuit(event);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event){
        GameServer game = GameAPI.getGameOf(event.getPlayer());
        if(game != null){
            game.onPlayerPickupItem(event);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        GameServer game = GameAPI.getGameOf(event.getEntity());
        if(game != null){
            game.onPlayerDeath(event);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getEntity().getLocation());
        if(game != null){
            game.onEntityDeath(event);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getEntity().getLocation());
        if(game != null){
            game.onEntityTarget(event);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getCurrentItem() != null && event.getWhoClicked() instanceof Player){
            GameServer game = GameAPI.getGameOf((Player)event.getWhoClicked());
            if(game != null){
                game.onInventoryClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getItem().getLocation());
        if(game != null){
            game.onInventoryPickupItem(event);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getEntity().getLocation());
        if(game != null){
            game.onEntityDamage(event);
        }
    }

    @EventHandler
    public void onCustomDamage(CustomDamageEvent event){
        GameServer game = event.getGame();
        if(game != null){
            game.onCustomDamage(event);
        }
    }

    @EventHandler
    public void onClearCombat(ClearCombatEvent event){
        GameServer game = event.getGame();
        if(game != null){
            game.onClearCombat(event);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getLocation());
        if(game != null){
            game.onEntityExplode(event);
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getLocation());
        if(game != null){
            game.onItemSpawn(event);
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getEntity().getLocation());
        if(game != null){
            game.onExplosionPrime(event);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getBlock().getLocation());
        if(game != null){
            game.onBlockPistonExtend(event);
        }
    }

    @EventHandler
    public void onPlayerStateChange(PlayerStateChangeEvent event){
        GameServer game = event.getGame();
        if(game != null){
            game.onPlayerStateChange(event);
        }
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event){
        GameServer game = event.getGame();
        if(game != null){
            game.onGameStateChange(event);
        }
    }

    @EventHandler
    public void onCombatDeath(CombatDeathEvent event){
        GameServer game = event.getGame();
        if(game != null){
            game.onCombatDeath(event);
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getLocation());
        if(game != null){
            game.onItemDespawn(event);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getBlock().getLocation());
        if(game != null){
            game.onBlockSpread(event);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getBlock().getLocation());
        if(game != null){
            game.onBlockFade(event);
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getBlock().getLocation());
        if(game != null){
            game.onLeavesDecay(event);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getBlock().getLocation());
        if(game != null){
            game.onBlockBurn(event);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event){
        GameServer game = GameAPI.getGameInBounds(event.getLocation());
        if(game != null){
            game.onCreatureSpawn(event);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        GameServer game = null;
        if(event.getEntity() != null){
            game = GameAPI.getGameInBounds(event.getEntity().getLocation());
        } else if(event.getDamager() != null){
            game = GameAPI.getGameInBounds(event.getDamager().getLocation());
        }
        if(game != null){
            game.onEntityDamageByEntity(event);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        GameServer game = GameAPI.getGameOf(event.getPlayer());
        if(game != null){
            game.onPlayerInteract(event);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        GameServer game = GameAPI.getGameOf(event.getPlayer());
        if(game != null){
            game.onPlayerDropItem(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event){
        String base = event.getMessage();
        if(!base.startsWith("/")){
            return;
        }
        String[] args = base.split(Pattern.quote(" "));

        MenuAPI.handleCommands(event, args);
        CommandHandler.onCommand(event, args);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event){
        GameServer game = GameAPI.getGameOf(event.getPlayer());
        if(game != null){
            game.onAsyncChat(event);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        ServerPlugin.getPlayerData(event.getPlayer().getName());
        GameServer gameServer = GameAPI.getServerInterface().getMainServer();
        gameServer.getLogin().PlayerAdd(gameServer, event.getPlayer(), null);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event){

    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        GameServer gameServer = GameAPI.getGameInBounds(event.getChunk());
        if(gameServer != null){
            gameServer.onChunkUnload(event);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        GameServer gameServer = GameAPI.getGameInBounds(event.getChunk());
        if(gameServer != null){
            gameServer.onChunkLoad(event);
        }
    }

}
