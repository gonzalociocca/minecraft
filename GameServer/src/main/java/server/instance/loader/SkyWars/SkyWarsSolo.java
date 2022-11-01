package server.instance.loader.SkyWars;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import server.ServerPlugin;
import server.api.BoardAPI;
import server.api.board.PlayerBoard;
import server.common.Code;
import server.common.TeleportReason;
import server.common.UpdateType;
import server.common.event.GameStateChangeEvent;
import server.common.event.UpdateEvent;
import server.instance.core.combat.CombatComponent;
import server.instance.core.combat.event.CombatDeathEvent;
import server.instance.core.damage.CustomDamageEvent;
import server.instance.loader.SkyWars.misc.CageEnum;
import server.instance.loader.SkyWars.misc.ChestLoot;
import server.instance.loader.SkyWars.misc.RandomItem;
import server.instance.loader.SoloGame;
import server.instance.misc.GameState;
import server.instance.misc.GameTeam;
import server.user.User;
import server.user.column.Simple.SimpleData;
import server.user.column.Simple.Type.Cage;
import server.util.*;

import java.util.HashMap;
import java.util.Map;

public class SkyWarsSolo extends SoloGame {
    private ChestLoot _playerArmor = new ChestLoot();
    private ChestLoot _playerFood = new ChestLoot();
    private ChestLoot _playerTool = new ChestLoot();
    private ChestLoot _playerProjectile = new ChestLoot();
    private ChestLoot _playerBlock = new ChestLoot();
    private ChestLoot _middleArmor = new ChestLoot();
    private ChestLoot _middleFood = new ChestLoot();
    private ChestLoot _middleTool = new ChestLoot();
    private ChestLoot _middleProjectile = new ChestLoot();
    private ChestLoot _middleBlock = new ChestLoot();


    private long eventTime = 60L * 5 * 1000L;
    private long eventSpaceTime = 60 * 5 * 1000L;
    @Expose private int eventType = 0;
    private String nextEventName = "Relleno";

    public SkyWarsSolo() {
        super();
        getMap().classIdentifier = SkyWarsSoloMap.class.getCanonicalName();
        setStorageId("SkyWarsSolo");
        String lore = "El ultimo hombre en pie gana.";

        _help = null;
        BlockBreak = true;
        DamageFall = false;
        DamagePvP = true;
        Damage = true;
        DamagePvE = true;
        DamageTeamOther = true;
        DamageTeamSelf = true;
        BlockPlace = true;
        DeathDropItems = true;
        ItemDrop = true;
        PrepareFreeze = true;
        ItemPickup = true;
        DeathOut = true;
        WorldTimeSet = 0;
        CreatureAllow = true;
        CreatureAllowOverride = true;
        CompassEnabled = true;
        WorldBoundaryKill = false;
        DamageSelf = true;
        DamageEvP = true;
        BlockDecay = false;
        BlockFade = false;
        BlockBurn = false;
        BlockSpread = false;
        InventoryOpen = true;

        BlockPlaceDeny.add(Material.CHEST.getId());
        BlockPlaceDeny.add(Material.HOPPER.getId());
        BlockPlaceDeny.add(Material.PISTON_BASE.getId());
        BlockPlaceDeny.add(Material.PISTON_STICKY_BASE.getId());

        setupPlayerLoot();
        setupMiddleLoot();
    }



    @Override
    public void parseData() {

        getMap().getWorld().setSpawnFlags(true, true);

        getExplosion().setRegenerateGround(true);
        for (GameTeam t : getLogin().getTeamList()) {
            t.setVisible(false);
        }
        for(GameTeam team : getLogin().getTeamList()){
            for(Location loc : team.GetSpawns()){
                loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()).setType(Material.AIR, false);
            }
        }
        fillAllChests();
    }

    HashMap<Location, CageEnum> cages = new HashMap();


    public void fillAllChests() {
        SkyWarsSoloMap map = (SkyWarsSoloMap) getMap();

        for (Location loc : map.getNormalChests()) {
            fillChest(loc.getBlock(), false);
        }
        for (Location loc : map.getMiddleChests()) {
            byte raw = loc.getBlock().getState().getRawData();
            loc.getBlock().breakNaturally(new ItemStack(Material.AIR));
            loc.getBlock().setTypeIdAndData(Material.CHEST.getId(), raw, true);

            fillChest(loc.getBlock(), true);
        }
    }

    public void refillAllChests(boolean isMiddle) {
        SkyWarsSoloMap map = (SkyWarsSoloMap) getMap();

        if (isMiddle) {
            for (Location loc : map.getNormalChests()) {
                fillChest(loc.getBlock(), true);
            }
        } else {
            for (Location loc : map.getNormalChests()) {
                fillChest(loc.getBlock(), false);
            }
        }
    }


    public void checkCageRemoval(GameStateChangeEvent event){
        if(event.getState() == GameState.Live) {
            for (Map.Entry<Location, CageEnum> cage : cages.entrySet()) {
                cage.getValue().removeAt(cage.getKey());
            }
            cages.clear();
        }
    }

    private void fillChest(Block block, boolean isMiddleChest) {
        BlockState state = block.getState();
        Chest chest = state instanceof Chest ? (Chest) state : null;
        if (chest == null) {
            return;
        }

        chest.getBlockInventory().clear();

        int nextSlot = 0;
        Inventory blockInv = chest.getBlockInventory();

        if (isMiddleChest) {
            for (int i = 0; i < 1 + UtilMath.r(2); i++) {
                blockInv.setItem(nextSlot++, _middleArmor.getLoot(nextSlot, blockInv));
            }
            for (int i = 0; i < 1 + UtilMath.r(3); i++) {
                blockInv.setItem(nextSlot++, _middleFood.getLoot(nextSlot, blockInv));
            }
            for (int i = 0; i < 1 + UtilMath.r(2); i++) {
                blockInv.setItem(nextSlot++, _middleTool.getLoot(nextSlot, blockInv));
            }
            for (int i = 0; i < 1 + UtilMath.r(2); i++) {
                blockInv.setItem(nextSlot++, _middleProjectile.getLoot(nextSlot, blockInv));
            }
            for (int i = 0; i < 1 + UtilMath.r(2); i++) {
                blockInv.setItem(nextSlot++, _middleBlock.getLoot(nextSlot, blockInv));
            }
        } else {
            for (int i = 0; i < 1 + UtilMath.r(2); i++)
                blockInv.setItem(nextSlot++, _playerArmor.getLoot(nextSlot, blockInv));

            for (int i = 0; i < 1 + UtilMath.r(3); i++)
                blockInv.setItem(nextSlot++, _playerFood.getLoot(nextSlot, blockInv));

            for (int i = 0; i < 1 + UtilMath.r(2); i++)
                blockInv.setItem(nextSlot++, _playerTool.getLoot(nextSlot, blockInv));

            for (int i = 0; i < 1 + UtilMath.r(2); i++)
                blockInv.setItem(nextSlot++, _playerProjectile.getLoot(nextSlot, blockInv));

            for (int i = 0; i < 1 + UtilMath.r(2); i++)
                blockInv.setItem(nextSlot++, _playerBlock.getLoot(nextSlot, blockInv));
        }
        chest.update(true, true);
    }

    public String getEventName() {
        return "Relleno";
    }

    public int getEventMinutes() {
        long rest = (getStateTime()+eventTime) - System.currentTimeMillis();
        return (int)(rest / 60000L);
    }

    public int getEventSeconds() {
        long rest = (getStateTime()+eventTime) - System.currentTimeMillis();
        return (int)((rest % 60000L)/1000L);
    }


    public void checkGameEvents(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {
            if (isLive() && UtilTime.elapsed(getStateTime(), eventTime)) {
                if (eventType == 0) {
                    boolean isMiddle = Code.getRandom().nextBoolean();
                    String refillType = isMiddle ? "del centro" : "de islas";
                    String refillMessage = UtilMsg.ChestRefill.replace("%g", getTitle()).replace("%s", refillType);
                    refillAllChests(isMiddle);
                    for (Player player : getPlayers(false)) {
                        player.sendMessage(refillMessage);
                    }
                    eventTime = eventTime + eventSpaceTime;
                }
            }
        }
    }

    public void checkPearls(UpdateEvent event) {/*
        if (event.getType() != UpdateType.TICK || _pearls.isEmpty()) {
            return;
        }

        Iterator<Projectile> pearlIter = _pearls.iterator();

        while (pearlIter.hasNext()) {
            Projectile proj = pearlIter.next();

            if (!proj.isValid()) {
                undoPearlEffect(proj.getShooter());
                proj.remove();
                pearlIter.remove();
                continue;
            }

            UtilParticle.PlayParticle(UtilParticle.ParticleType.WITCH_MAGIC, proj.getLocation(), 0f, 0f, 0f, 0f, 1, UtilParticle.ViewDist.MAX, getPlayers(false));
        }
        */
    }

    public void doPearlEffect(ProjectileSource source){
        if(source instanceof CraftPlayer){
            CraftPlayer craftPlayer = (CraftPlayer)source;
            craftPlayer.getHandle().k = false;
            craftPlayer.getHandle().setInvisible(true);
        }
    }
    public void undoPearlEffect(ProjectileSource source){
        if(source instanceof CraftPlayer){
            CraftPlayer craftPlayer = (CraftPlayer)source;
            craftPlayer.getHandle().k = true;
            craftPlayer.getHandle().setInvisible(false);
        }
    }

    @Override
    public void updateScoreboard(GameTeam team) {
        super.updateScoreboard(team);

        for(Player p1 : team.getPlayers(false)) {
            PlayerBoard playerBoard = BoardAPI.getBoard(p1);
            Scoreboard scoreboard = playerBoard.getBoard();
            Map<Integer, String> data = playerBoard.getData();
            Objective objective = UtilScoreboard.getSideBoardObjective(scoreboard, UtilMsg.Green + getTitle());

            if (getState() == GameState.Live) {
                int playersAlive = getPlayersCount(true);
                String currentEvent = getEventName();

                int currentEventMinLeft = getEventMinutes();
                int currentEventSecLeft = getEventSeconds();
                String eventTimeLeft;
                String aliveLeft;

                if (currentEventMinLeft > 0) {
                    eventTimeLeft = " " + "   " + UtilMsg.Green + UtilMsg.In + " " + currentEventMinLeft + "m " + currentEventSecLeft + "s";
                } else if (currentEventSecLeft > 0) {
                    eventTimeLeft = " " + "   " + UtilMsg.Green + UtilMsg.In + " " + currentEventSecLeft + "s";
                } else {
                    eventTimeLeft = " " + "   " + UtilMsg.ScoreboardEventInProgress;
                }

                if (playersAlive > 1) {
                    aliveLeft = " " + UtilMsg.ScoreboardAliveLeft + "n " + UtilMsg.Green + playersAlive + UtilMsg.ScoreboardAlive + "s";
                } else {
                    aliveLeft = " " + UtilMsg.ScoreboardAliveLeft + " " + UtilMsg.Green + playersAlive + UtilMsg.ScoreboardAlive;
                }


                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, "     ", 10);

                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.ScoreboardEvent + currentEvent, 9);

                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, eventTimeLeft, 8);

                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, "    ", 7);

                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, aliveLeft, 6);

            } else if (getState() == GameState.End) {
                int index = 6;
                int placesSize = getPlaces().size();
                for (int a = placesSize; a > 0; a--) {
                    int place = a - 1;
                    Player player = getPlaces().get(place);
                    if (place == 0) {
                        UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.FirstPlace.replace("%s", player.getName()), index++);
                    } else if (place == 1) {
                        UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.SecondPlace.replace("%s", player.getName()), index++);
                    } else if (place == 2) {
                        UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.ThirdPlace.replace("%s", player.getName()), index++);
                    } else {
                        break;
                    }
                }

                if (placesSize > 0) {
                    UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, "      ", index++);
                }
            }
        }
    }


    private void setupPlayerLoot() {
        _playerArmor.addLoot(new RandomItem(Material.LEATHER_HELMET, 20));
        _playerArmor.addLoot(new RandomItem(Material.LEATHER_CHESTPLATE, 32));
        _playerArmor.addLoot(new RandomItem(Material.LEATHER_LEGGINGS, 28));
        _playerArmor.addLoot(new RandomItem(Material.LEATHER_BOOTS, 16));
        _playerArmor.addLoot(new RandomItem(Material.GOLD_HELMET, 5));
        _playerArmor.addLoot(new RandomItem(Material.GOLD_CHESTPLATE, 8));
        _playerArmor.addLoot(new RandomItem(Material.GOLD_LEGGINGS, 7));
        _playerArmor.addLoot(new RandomItem(Material.GOLD_BOOTS, 4));
        _playerFood.addLoot(new RandomItem(Material.BAKED_POTATO, 1, 1, 4));
        _playerFood.addLoot(new RandomItem(Material.COOKED_BEEF, 1, 1, 2));
        _playerFood.addLoot(new RandomItem(Material.COOKED_CHICKEN, 1, 1, 2));
        _playerFood.addLoot(new RandomItem(Material.CARROT_ITEM, 1, 1, 3));
        _playerFood.addLoot(new RandomItem(Material.BREAD, 1, 1, 3));
        _playerFood.addLoot(new RandomItem(Material.APPLE, 1, 1, 4));
        _playerFood.addLoot(new RandomItem(Material.PORK, 1, 1, 4));
        _playerFood.addLoot(new RandomItem(Material.ROTTEN_FLESH, 1, 1, 6));
        _playerTool.addLoot(new RandomItem(Material.WOOD_SWORD, 2));
        _playerTool.addLoot(new RandomItem(Material.STONE_SWORD, 1));
        _playerTool.addLoot(new RandomItem(Material.FISHING_ROD, 2));
        _playerTool.addLoot(new RandomItem(Material.STONE_AXE, 2));
        _playerTool.addLoot(new RandomItem(Material.STONE_PICKAXE, 3));
        _playerTool.addLoot(new RandomItem(Material.IRON_AXE, 1));
        _playerTool.addLoot(new RandomItem(Material.IRON_PICKAXE, 2));
        _playerProjectile.addLoot(new RandomItem(Material.ARROW, 18, 2, 8));
        _playerProjectile.addLoot(new RandomItem(Material.SNOW_BALL, 60, 2, 5));
        _playerProjectile.addLoot(new RandomItem(Material.EGG, 60, 2, 5));
        _playerBlock.addLoot(new RandomItem(Material.COBBLESTONE, 30, 8, 16));
        _playerBlock.addLoot(new RandomItem(Material.DIRT, 30, 8, 16));
        _playerBlock.addLoot(new RandomItem(Material.WOOD, 30, 8, 16));
    }

    private void setupMiddleLoot() {
        _middleArmor.addLoot(new RandomItem(Material.GOLD_HELMET, 20));
        _middleArmor.addLoot(new RandomItem(Material.GOLD_CHESTPLATE, 32));
        _middleArmor.addLoot(new RandomItem(Material.GOLD_LEGGINGS, 28));
        _middleArmor.addLoot(new RandomItem(Material.GOLD_BOOTS, 16));
        _middleArmor.addLoot(new RandomItem(Material.IRON_HELMET, 20));
        _middleArmor.addLoot(new RandomItem(Material.IRON_CHESTPLATE, 32));
        _middleArmor.addLoot(new RandomItem(Material.IRON_LEGGINGS, 28));
        _middleArmor.addLoot(new RandomItem(Material.IRON_BOOTS, 16));
        _middleArmor.addLoot(new RandomItem(Material.DIAMOND_HELMET, 5));
        _middleArmor.addLoot(new RandomItem(Material.DIAMOND_CHESTPLATE, 8));
        _middleArmor.addLoot(new RandomItem(Material.DIAMOND_LEGGINGS, 7));
        _middleArmor.addLoot(new RandomItem(Material.DIAMOND_BOOTS, 4));
        _middleFood.addLoot(new RandomItem(Material.COOKED_BEEF, 1, 1, 3));
        _middleFood.addLoot(new RandomItem(Material.COOKED_CHICKEN, 1, 1, 3));
        _middleFood.addLoot(new RandomItem(Material.MUSHROOM_SOUP, 1));
        _middleFood.addLoot(new RandomItem(Material.GRILLED_PORK, 1, 1, 3));
        _middleTool.addLoot(new RandomItem(Material.IRON_SWORD, 1));
        _middleTool.addLoot(new RandomItem(Material.DIAMOND_SWORD, 1));
        _middleTool.addLoot(new RandomItem(Material.FISHING_ROD, 1));
        _middleTool.addLoot(new RandomItem(Material.DIAMOND_AXE, 1));
        _middleTool.addLoot(new RandomItem(Material.DIAMOND_PICKAXE, 1));
        _middleTool.addLoot(new RandomItem(Material.BOW, 1));
        _middleProjectile.addLoot(new RandomItem(Material.ARROW, 2, 4, 12));
        _middleProjectile.addLoot(new RandomItem(Material.ENDER_PEARL, 1, 1, 2));
        _middleBlock.addLoot(new RandomItem(Material.BRICK, 30, 12, 24));
        _middleBlock.addLoot(new RandomItem(Material.GLASS, 30, 12, 24));
        _middleBlock.addLoot(new RandomItem(Material.SOUL_SAND, 30, 12, 24));
    }

    public void checkKillReward(CombatDeathEvent event) {
        if (!(event.getEvent().getEntity() instanceof Player)) {
            return;
        }

        Player killed = (Player) event.getEvent().getEntity();

        if (event.getLog().GetKiller() != null) {
            Player killer = UtilPlayer.searchExact(event.getLog().GetKiller().GetName());

            if (killer != null && !killer.equals(killed)) {
                killer.giveExpLevels(2);
                killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            }
        }

        for (CombatComponent log : event.getLog().GetAttackers()) {
            if (event.getLog().GetKiller() != null && log.equals(event.getLog().GetKiller()))
                continue;
            Player assist = UtilPlayer.searchExact(log.GetName());
            if (assist != null) {
                assist.giveExpLevels(1);
                assist.playSound(assist.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
            }

        }
    }

    public void checkEggs(CustomDamageEvent event)
    {
        if(event.getGame() != this){
            return;
        }
        event.setDamageToLevel(false);

        if(event.getProjectile() == null){
            return;
        }
        Projectile projectile = event.getProjectile();

        if (projectile instanceof Egg || projectile instanceof Snowball)
        {
            event.setInitialDamage(1); // Cause isnt showed, artificial method doesnt work right.
            event.setIgnoreRate(true);

            Vector vel = event.getProjectile().getVelocity().multiply(0.2);

            if (vel.getY() < 0.1)
                vel.setY(0.1);

            event.getDamageeEntity().setVelocity(vel);
        }
    }

    public void checkBlockBreak(BlockBreakEvent e)
    {
        super.onBlockBreak(e);

        if(e.isCancelled()){
            return;
        }
        e.setExpToDrop(0);

        final Block block = e.getBlock();

        if (e.getBlock().getType() == Material.WEB)
        {
            Bukkit.getScheduler().runTaskLater(ServerPlugin.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    for (int i=0 ; i<1 + UtilMath.r(2) ; i++)
                        block.getWorld().dropItem(block.getLocation().add(0.5, 0.2, 0.5), new ItemStack(Material.STRING));

                }}, 1);

        }

        if (e.getBlock().getType() == Material.GRAVEL)
        {
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);

            Bukkit.getScheduler().runTaskLater(ServerPlugin.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    for (int i=0 ; i<1 + UtilMath.r(3) ; i++)
                        block.getWorld().dropItem(block.getLocation().add(0.5, 0.2, 0.5), new ItemStack(Material.FLINT));

                }}, 1);
        }

        if (e.getBlock().getType() == Material.IRON_ORE)
        {
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);

            Bukkit.getScheduler().runTaskLater(ServerPlugin.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    block.getWorld().dropItem(block.getLocation().add(0.5, 0.2, 0.5), new ItemStack(Material.IRON_INGOT));

                }}, 1);
        }
    }


    @Override
    public void onGameStateChange(GameStateChangeEvent event){
        super.onGameStateChange(event);
        checkCageRemoval(event);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        super.onBlockBreak(event);
        checkBlockBreak(event);
    }

    @Override
    public void teleportPlayer(Player player, Location loc, TeleportReason reason) {
        if(reason == TeleportReason.Spawn) {
            User pd = ServerPlugin.getPlayerData(player.getName());
            SimpleData simpleData = pd.getSimpleData();
            Cage cage = simpleData.getCage();
            CageEnum cageEnum = cage.getDefaultCage(getStorageId());
            cageEnum.applyAt(loc);
            cages.put(loc, cageEnum);
        }
        super.teleportPlayer(player, loc, reason);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        super.onUpdate(event);
        checkGameEvents(event);
        checkPearls(event);
    }


    @Override
    public void resetGame() {
        super.resetGame();
        eventTime = eventSpaceTime;
        eventType = 0;
        nextEventName = "Relleno";
    }

    @Override
    public void onCombatDeath(CombatDeathEvent event){
        super.onCombatDeath(event);
        checkKillReward(event);
    }

    @Override
    public void onCustomDamage(CustomDamageEvent event){
        super.onCustomDamage(event);
        checkEggs(event);
    }

    @Override
    public void onProjectileLaunch(ProjectileLaunchEvent event){
        super.onProjectileLaunch(event);
    }

}


