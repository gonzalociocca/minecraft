package server.instance.core.flag;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import server.ServerPlugin;
import server.common.Code;
import server.common.TeleportReason;
import server.common.UpdateType;
import server.common.event.PlayerDeathOutEvent;
import server.common.event.UpdateEvent;
import server.instance.GameServer;
import server.instance.core.damage.CustomDamageEvent;
import server.instance.misc.GameState;
import server.instance.misc.GameTeam;
import server.util.*;

/**
 * Created by noname on 18/4/2017.
 */
public class GameFlags {

    public boolean canHurt(GameServer game, final Player pA, final Player pB) {
        if (pA == null || pB == null) {
            return false;
        }
        if (!game.Damage) {
            return false;
        }
        if (!game.DamagePvP) {
            return false;
        }
        if (pA.equals(pB)) {
            return game.DamageSelf;
        }
        final GameTeam tA = game.getLogin().getTeam(pA);
        if (tA == null) {
            return false;
        }
        final GameTeam tB = game.getLogin().getTeam(pB);
        return tB != null && (!tA.equals(tB) || game.DamageTeamSelf) && (tA.equals(tB) || game.DamageTeamOther);
    }

    public void checkDamage(GameServer game, CustomDamageEvent event) {

        LivingEntity damagee = event.getDamageeEntity();
        LivingEntity damager = event.getDamagerEntity(true);
        /*if (event.getDamageePlayer() != null && damagee.getWorld().getId().equals(MapCipherBase.getLobbyWorldName())) {
            event.setCancelled("In Lobby");
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                Manager.GetLobby().sendToLobbyWithItems(event.getDamageePlayer(), true);
            }
            return;
        }*/
        if (!game.DamageFall && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled("Fall Damage Disabled");
            return;
        }
        if (!game.Damage) {
            event.setCancelled("Damage Disabled");
            return;
        }
        if (game.getState() != GameState.Live) {
            event.setCancelled("GameServer not Live");
            return;
        }
        if ((damagee != null) && ((damagee instanceof Player)) && (!game.isAlive((Player) damagee))) {
            event.setCancelled("Damagee Not Playing");
            return;
        }
        if ((damager != null) && ((damager instanceof Player)) && (!game.isAlive((Player) damager))) {
            event.setCancelled("Damager Not Playing");
            return;
        }
        if ((damagee != null) && (damager != null)) {
            if (((damagee instanceof Player)) && ((damager instanceof Player))) {
                if (!canHurt(game, (Player) damagee, (Player) damager)) {
                    event.setCancelled("Damage Rules");
                }
            } else if ((damager instanceof Player)) {
                if (!game.DamagePvE) {
                    event.setCancelled("PvE Disabled");
                }
            } else if ((damagee instanceof Player)) {
                if (!game.DamageEvP) {
                    event.setCancelled("EvP Disabled");
                    return;
                }
            }
        }
    }

    public void checkDamageExplosion(GameServer game, CustomDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) && (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            return;
        }
        Player damagee = event.getDamageePlayer();
        if (damagee == null) {
            return;
        }
        Player damager = event.getDamagerPlayer(true);
        if (damager == null) {
            return;
        }
        if (canHurt(game, damagee, damager)) {
            return;
        }
        event.setCancelled("Allied Explosion");
    }

    public void checkPlayerPickupItem(GameServer game, PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if ((game == null) || (!game.isAlive(player)) || (game.getState() != GameState.Live)) {
            event.setCancelled(true);
            return;
        }
        if (game.ItemPickup) {
            if (game.ItemPickupDeny.contains(Integer.valueOf(event.getItem().getItemStack().getTypeId()))) {
                event.setCancelled(true);
            }
        } else if (!game.ItemPickupAllow.contains(Integer.valueOf(event.getItem().getItemStack().getTypeId()))) {
            event.setCancelled(true);
        }
        if(player.getGameMode() == GameMode.CREATIVE){
            event.setCancelled(true);
        }
    }

    public void checkPlayerDropItem(GameServer game, PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if ((!game.isAlive(player)) || (game.getState() != GameState.Live)) {
            if ((!player.isOp()) || (player.getGameMode() != GameMode.CREATIVE)) {
                event.setCancelled(true);
            }
            return;
        }
        if (game.ItemDrop) {
            if (game.ItemDropDeny.contains(Integer.valueOf(event.getItemDrop().getItemStack().getTypeId()))) {
                event.setCancelled(true);
            }
        } else if (!game.ItemDropAllow.contains(Integer.valueOf(event.getItemDrop().getItemStack().getTypeId()))) {
            event.setCancelled(true);
        }
    }

    public void checkInteractLive(GameServer game, PlayerInteractEvent event){
        if(game.getState() != GameState.Live){
            event.setCancelled(true);
        }
    }

    public void checkInventoryOpen(GameServer game, PlayerInteractEvent event) {

        if (!game.inProgress()) {
            return;
        }
        if (game.InventoryOpen) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        if (!UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        event.setCancelled(true);
    }

    public void checkBlockPlace(GameServer game, BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!game.isAlive(player)) {
            if (player.getGameMode() != GameMode.CREATIVE || !player.isOp()) {
                event.setCancelled(true);
            }
        } else if (game.BlockPlace) {
            if (!game.BlockPlaceDeny.isEmpty() && game.BlockPlaceDeny.contains(Integer.valueOf(event.getBlock().getTypeId()))) {
                event.setCancelled(true);
            }
        } else if (game.BlockPlaceAllow.isEmpty() || !game.BlockPlaceAllow.contains(Integer.valueOf(event.getBlock().getTypeId()))) {
            event.setCancelled(true);
        }
    }

    public void checkBlockBreak(GameServer game, BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (game.getState() == GameState.Live) {
            if (!game.isAlive(player)) {
                event.setCancelled(true);
            } else if (game.BlockBreak) {
                if (!game.BlockBreakDeny.isEmpty() && game.BlockBreakDeny.contains(Integer.valueOf(event.getBlock().getTypeId()))) {
                    event.setCancelled(true);
                }
            } else if (game.BlockBreakAllow.isEmpty() || !game.BlockBreakAllow.contains(Integer.valueOf(event.getBlock().getTypeId()))) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

    public void checkPrivateBlocksPlace(GameServer game, BlockPlaceEvent event) {

        if (!game.PrivateBlocks) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!UtilBlock.usable(event.getBlockPlaced())) {
            return;
        }
        if ((event.getBlockPlaced().getType() != Material.CHEST) &&
                (event.getBlockPlaced().getType() != Material.FURNACE) &&
                (event.getBlockPlaced().getType() != Material.BURNING_FURNACE) &&
                (event.getBlockPlaced().getType() != Material.WORKBENCH)) {
            return;
        }
        String privateKey = event.getPlayer().getName();
        if (!game.PrivateBlockCount.containsKey(privateKey)) {
            game.PrivateBlockCount.put(privateKey, Integer.valueOf(0));
        }
        if (game.PrivateBlockCount.get(privateKey).intValue() == 4) {
            return;
        }
        if (game.PrivateBlockCount.get(privateKey).intValue() > 1) {
            return;
        }
        game.getPrivateBlockMap().put(event.getBlockPlaced().getLocation(), event.getPlayer());
        game.PrivateBlockCount.put(event.getPlayer().getName(), Integer.valueOf(game.PrivateBlockCount.get(event.getPlayer().getName()).intValue() + 1));
        event.getPlayer().sendMessage(F.main(game.getTitle(), "Can't touch this. Na na nana!"));
        if (game.PrivateBlockCount.get(privateKey).intValue() == 4) {
            event.getPlayer().sendMessage(F.main(game.getTitle(), "Protected block limit reached.  Stay classy Ultra ranker ;)"));
        } else if (game.PrivateBlockCount.get(privateKey).intValue() == 2) {

            event.getPlayer().sendMessage(F.main(game.getTitle(), "Protected block limit reached. Thieves are scary"));
        }
    }

    public void checkPrivateBlockPlaceCancel(GameServer game, BlockPlaceEvent event) {

        if (!game.PrivateBlocks) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        Block block = event.getBlockPlaced();
        if (block.getType() != Material.CHEST) {
            return;
        }
        Player player = event.getPlayer();

        BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        BlockFace[] arrayOfBlockFace1;
        int j = (arrayOfBlockFace1 = faces).length;
        for (int i = 0; i < j; i++) {
            BlockFace face = arrayOfBlockFace1[i];

            Block other = block.getRelative(face);
            if (other.getType() == Material.CHEST) {
                if (game.getPrivateBlockMap().containsKey(other.getLocation())) {
                    Player owner = game.getPrivateBlockMap().get(other.getLocation());
                    if (!player.equals(owner)) {
                        GameTeam ownerTeam = game.getLogin().getTeam(owner);
                        GameTeam playerTeam = game.getLogin().getTeam(player);
                        if ((ownerTeam == null) || (playerTeam == null) || (ownerTeam.equals(playerTeam))) {
                            UtilPlayer.message(event.getPlayer(), F.main("GameServer",
                                    "You cannot combine " +
                                            F.elem(new StringBuilder(UtilMsg.Purple).append(
                                                    UtilName.getFriendlyName(event.getBlock())).toString()) +
                                            " with " + F.elem(new StringBuilder().append(game.getColor(owner)).append(owner.getName()).append(".").toString())));

                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void checkPrivateBlocksBreak(GameServer game, BlockBreakEvent event) {

        if (!game.PrivateBlocks) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!game.getPrivateBlockMap().containsKey(event.getBlock().getLocation())) {
            return;
        }
        Player owner = game.getPrivateBlockMap().get(event.getBlock().getLocation());
        Player player = event.getPlayer();
        if (owner.equals(player)) {
            game.getPrivateBlockMap().remove(event.getBlock().getLocation());
        } else {
            GameTeam ownerTeam = game.getLogin().getTeam(owner);
            GameTeam playerTeam = game.getLogin().getTeam(player);
            if ((ownerTeam != null) && (playerTeam != null) && (!ownerTeam.equals(playerTeam))) {
                return;
            }
            UtilPlayer.message(event.getPlayer(), F.main("GameServer",
                    F.elem(new StringBuilder(String.valueOf(UtilMsg.Purple)).append(UtilName.getFriendlyName(event.getBlock())).toString()) +
                            " belongs to " + F.elem(new StringBuilder().append(game.getColor(owner)).append(owner.getName()).append(".").toString())));

            event.setCancelled(true);
        }
    }

    public void checkPrivateBlocksInteract(GameServer game, PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        if ((event.getClickedBlock().getType() != Material.CHEST) &&
                (event.getClickedBlock().getType() != Material.FURNACE) &&
                (event.getClickedBlock().getType() != Material.BURNING_FURNACE)) {
            return;
        }

        if (!game.PrivateBlocks) {
            return;
        }
        if (!game.getPrivateBlockMap().containsKey(event.getClickedBlock().getLocation())) {
            return;
        }
        Player owner = game.getPrivateBlockMap().get(event.getClickedBlock().getLocation());
        Player player = event.getPlayer();
        if (owner.equals(player)) {
            return;
        }
        GameTeam ownerTeam = game.getLogin().getTeam(owner);
        GameTeam playerTeam = game.getLogin().getTeam(player);
        if ((ownerTeam != null) && (playerTeam != null) && (!ownerTeam.equals(playerTeam))) {
            return;
        }
        UtilPlayer.message(event.getPlayer(), F.main("GameServer",
                F.elem(new StringBuilder(String.valueOf(UtilMsg.Purple)).append(
                        UtilName.getFriendlyName(event.getClickedBlock())).toString()) +
                        " belongs to " + F.elem(new StringBuilder().append(game.getColor(owner)).append(owner.getName()).append(".").toString())));

        event.setCancelled(true);
    }

    public void checkDeath(final GameServer game, PlayerDeathEvent event){
        final Player player = event.getEntity();

        game.getBuffer().factory().Blind(game, "Ghost", player, player, 1.5D, 0, false, false, false);

        player.setFireTicks(0);
        player.setFallDistance(0.0F);
        if (game.DeathDropItems) {
            for (ItemStack stack : event.getDrops()) {
                player.getWorld().dropItem(player.getLocation(), stack);
            }
        }
        event.getDrops().clear();
        if ((game.getState() == GameState.Live) && (game.DeathOut)) {
            PlayerDeathOutEvent outEvent = new PlayerDeathOutEvent(game, player);
            Bukkit.getServer().getPluginManager().callEvent(outEvent);
            if (!outEvent.isCancelled()) {
                game.getLogin().removePlayer(game, player, false, false);
            }
        }
        if ((game.DeathSpectateSecs <= 0.0D) && ((game.getLogin().getTeam(player) == null) || (game.getLogin().getTeam(player).getRespawnTime() <= 0.0D))) {
            if (game.isAlive(player)) {
                game.respawnPlayer(player);
            } else {
                game.SetSpectator(player);
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ServerPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    player.setFireTicks(0);
                    player.setVelocity(new Vector(0, 0, 0));
                }
            }, 0L);
        } else {
            double time = game.DeathSpectateSecs;
            if ((game.getLogin().getTeam(player) != null) &&
                    (game.getLogin().getTeam(player).getRespawnTime() > time)) {
                time = game.getLogin().getTeam(player).getRespawnTime();
            }
            UtilInv.Clear(player);

            game.getBuffer().factory().Blind(game, "Ghost", player, player, 1.5D, 0, false, false, false);
            game.getBuffer().factory().Cloak(game, "Ghost", player, player, time, false, false);

            player.setGameMode(GameMode.CREATIVE);
            player.setFlying(true);

            for (int i = 0; i < 9; i++) {
                player.getInventory().setItem(i, new ItemStack(Material.SKULL));
            }
            UtilAction.velocity(player, new Vector(0, 0, 0), 1.0D, true, 0.4D, 0.0D, 1.0D, true);

            UtilPlayer.message(player, Code.Color(UtilMsg.Respawn.replace("%s", "" + time)));

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ServerPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (game.isAlive(player)) {
                        game.respawnPlayer(player);
                    } else {
                        game.SetSpectator(player);
                    }
                    player.setFireTicks(0);
                    player.setVelocity(new Vector(0, 0, 0));
                }
            }, (int) (time * 20.0D));
        }
    }


    /* This is laggy */
    /*@EventHandler
    public void PlayerMoveCancel(PlayerMoveEvent event) {
        if (UtilMath.offset2d(event.getFrom(), event.getTo()) <= 0.0D) {
            return;
        }
        GameServer instance = Manager.getGameOf(event.getPlayer());
        if (instance != null && instance.PrepareFreeze && (instance.getState() == GameState.Recruit)) {

            event.getFrom().setPitch(event.getTo().getPitch());
            event.getFrom().setYaw(event.getTo().getYaw());
            event.setTo(event.getFrom());
        }
    }*/

    public void checkBoundary(GameServer game){
        if ((game.getState() == GameState.Live)) {
            for (Player player : game.getPlayers(false)) {
                if (!game.isInBounds(player.getLocation())) {
                    if (!game.isAlive(player)) {
                        game.teleportPlayer(player, game.getSpectatorLocation(), TeleportReason.Out);
                    } else if (!game.WorldBoundaryKill) {
                        UtilPlayer.message(player, UtilMsg.WarningPlayableArea);

                        game.getDamage().newDamageEvent(game, player, null, null,
                                EntityDamageEvent.DamageCause.VOID, 4.0D, false, false, false,
                                "Void", "Void Damage");

                        player.getWorld().playSound(player.getLocation(), Sound.NOTE_BASS, 2.0F, 1.0F);
                        player.getWorld().playSound(player.getLocation(), Sound.NOTE_BASS, 2.0F, 1.0F);
                    } else {
                        game.getDamage().newDamageEvent(game, player, null, null,
                                EntityDamageEvent.DamageCause.VOID, 9001.0D, false, false, false,
                                "Void", "Void Damage");
                    }
                }
            }
        }
    }

    public void checkWaterDamage(GameServer game){
        if (game.WorldWaterDamage > 0 && game.isLive()) {
            for (Player player : game.getPlayers(true)) {
                if ((player.getLocation().getBlock().getTypeId() == 8) || (player.getLocation().getBlock().getTypeId() == 9)) {
                    game.getDamage().newDamageEvent(game, player, null, null,
                            EntityDamageEvent.DamageCause.DROWNING, 4.0D, true, false, false,
                            "Water", "Water Damage");

                    player.getWorld().playSound(player.getLocation(),
                            Sound.SPLASH, 0.8F,
                            1.0F + (float) Math.random() / 2.0F);
                }
            }
        }
    }

    public void checkHealthFood(GameServer game){

        for (Player player : game.getPlayers(false)) {
            if (game.getState() == GameState.Recruit || (!game.isAlive(player))) {
                player.setMaxHealth(20.0D);
                player.setHealth(20.0D);
                player.setFoodLevel(20);

            }
        }
        if (game.isLive())
            if (game.HungerSet != -1) {
                for (Player player : game.getPlayers(true)) {
                    player.setFoodLevel(game.HungerSet);
                }
            }
        if (game.HealthSet != -1) {
            for (Player player : game.getPlayers(true)) {
                player.setHealth(game.HealthSet);
            }
        }
    }

    public void checkWeather(GameServer game){
        if (!game.WorldWeatherEnabled) {
            if (game.getMap() != null) {
                game.getMap().getWorld().setStorm(false);
                game.getMap().getWorld().setThundering(false);
            }
        }
    }

    public void onPlayerDropItem(GameServer game, PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        }
        checkPlayerDropItem(game, event);
    }

    public void onBlockPlace(GameServer game, BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        }
        checkBlockPlace(game, event);
        checkPrivateBlockPlaceCancel(game, event);
        checkPrivateBlocksPlace(game, event);
    }

    public void onBlockBreak(GameServer game, BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        }
        checkBlockBreak(game, event);
        checkPrivateBlocksBreak(game, event);
    }

    public void onPlayerInteract(GameServer game, PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        }
        checkInteractLive(game, event);
        checkInventoryOpen(game, event);
        checkPrivateBlocksInteract(game, event);
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            if (p.getGameMode() == GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    public void onCustomDamage(GameServer game, CustomDamageEvent event) {
        if (event.getDamagerPlayer(false) != null) {
            if (event.getDamagerPlayer(false).getGameMode() == GameMode.CREATIVE) {
                event.setCancelled("You are a spectator now!");
            }
        }
        checkDamage(game, event);
        checkDamageExplosion(game, event);
    }

    public void onPlayerDeath(GameServer game, PlayerDeathEvent event) {
        checkDeath(game, event);
    }

    public void onCreatureSpawn(GameServer game, CreatureSpawnEvent event) {
        if (!game.CreatureAllow && !game.CreatureAllowOverride) {
            event.setCancelled(true);
        }
    }

    public void onUpdate(GameServer game, UpdateEvent event) {
        if(event.getType() == UpdateType.TICK){
            game.getMap().getWorld().setTime(12000L);
        }
        else if (event.getType() == UpdateType.FAST) {
            checkWaterDamage(game);
            checkBoundary(game);
            checkHealthFood(game);
        } else if (event.getType() == UpdateType.SEC) {
            checkWeather(game);
        }
    }


    public void onBlockBurn(GameServer game, BlockBurnEvent event) {
        if (!event.isCancelled() && !game.BlockBurn) {
            event.setCancelled(true);
        }
    }

    public void onLeavesDecay(GameServer game, LeavesDecayEvent event) {
        if (!event.isCancelled() && !game.BlockDecay) {
            event.setCancelled(true);
        }
    }

    public void onBlockFade(GameServer game, BlockFadeEvent event) {
        if (!event.isCancelled() && !game.BlockFade) {
            event.setCancelled(true);
        }
    }

    public void onBlockSpread(GameServer game, BlockSpreadEvent event) {
        if (!event.isCancelled() && !game.BlockSpread) {
            event.setCancelled(true);
        }
    }

    public void onItemDespawn(GameServer game, ItemDespawnEvent event) {
        if (!event.isCancelled() && !game.ItemDespawn) {
            event.setCancelled(true);
        }
    }

    public void onInventoryClick(GameServer game, InventoryClickEvent event) {
        if(event.getWhoClicked() != null && (event.getWhoClicked() instanceof Player)) {
            Player player = (Player) event.getWhoClicked();

            if (game.isLive() && !game.isAlive(player)) {
                event.setCancelled(true);
            }
        }
    }


    public void onPlayerPickupItem(GameServer game, PlayerPickupItemEvent event){
        checkPlayerPickupItem(game, event);
    }

}
