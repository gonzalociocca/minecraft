package me.gonzalociocca.minigame.lobby;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.events.Update.UpdateEvent;
import me.gonzalociocca.minigame.games.GameModifier;
import me.gonzalociocca.minigame.games.GameType;
import me.gonzalociocca.minigame.games.game.GameBase;
import me.gonzalociocca.minigame.map.Cipher.LobbyMap;
import me.gonzalociocca.minigame.map.Cipher.MapCipherBase;
import me.gonzalociocca.minigame.misc.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by noname on 23/3/2017.
 */
public class LobbyManager implements Listener {
    Core core;
    Location spawnLocation;
    List<InventoryItem> LobbyItems = new ArrayList();
    boolean removeJoinQuitMessages = true;
    boolean clearInventoryOnJoin = true;

    /**
     * Games Menu:
     * Map Type > Map Arena #Number
     */
    ArrayList gameSigns = new ArrayList();

    MapCipherBase lobbyMap;
    ArrayList<LobbySign> lobbySigns = new ArrayList();

    public LobbyManager(Core main) {
        core = main;
        long start = System.currentTimeMillis();
        lobbyMap = core.getMapManager().loadCustomMap("Lobby", LobbyMap.class);
        lobbyMap.setCustomWorldName(Core.getWorldLobbyName());
        lobbyMap.setCustomBuildLocation(new CustomLocation(0, 0, 0));
        lobbyMap.reBuildMap();
        lobbyMap.queueBuild(Integer.MAX_VALUE);

        System.out.println("Took " + (System.currentTimeMillis() - start) / 1000 + "s to parse and build the lobby");
        Bukkit.getPluginManager().registerEvents(this, core);
        World world = Bukkit.getWorld(Core.getWorldLobbyName());

        for (CustomLocation loc : lobbyMap.getCustomLocationList(TagEnum.Signs.getID())) {
            Block block = world.getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
            BlockState state = block.getState();
            if (state instanceof Sign) {
                Sign sign = (Sign) state;
                String[] lines = sign.getLines();
                String line1 = lines.length > 0 ? lines[0] : null;
                String line2 = lines.length > 1 ? lines[1] : null;
                String line3 = lines.length > 2 ? lines[2] : null;
                String line4 = lines.length > 3 ? lines[3] : null;
                if (line1 != null) {
                    if (line1.equalsIgnoreCase("game")) {
                        if (line2 != null) {
                            for (GameType type : GameType.values()) {
                                if (type.getShortName().equalsIgnoreCase(line2)) {
                                    LobbySign lobbySign = new LobbySign(type, block);
                                    if (line3 != null) {
                                        lobbySign.setGameModifier(GameModifier.getByName(line3));
                                    }
                                    lobbySigns.add(lobbySign);
                                }
                            }
                        }
                    } else if (line1.equalsIgnoreCase("spawn")) {
                        Location newLoc = sign.getLocation().clone().add(0.50F, 0, 0.50F);
                        newLoc.setPitch(0);
                        newLoc.setYaw(Code.getYawOfSignRotation(sign.getRawData()));
                        setSpawn(newLoc);
                        block.setType(Material.AIR, true);
                    }
                }
            }
        }

        lobbyMap.getCustomLocation(TagEnum.SingleLocation1.getID());

        for (LobbySign mySign : lobbySigns) {
            GameBase newGame = core.getGameManager().createGame(mySign.getGameType(), mySign.getGameModifier());
            mySign.setGame(newGame);
        }
    }

    public Location getSpawn() {
        if (spawnLocation != null) {
            return spawnLocation;
        } else {
            spawnLocation = new Location(Bukkit.getWorld(Core.getWorldLobbyName()), 0, 128, 0);
            return spawnLocation;
        }
    }

    public void setSpawn(Location location) {
        spawnLocation = location;
    }

    public void addLobbyItem(ItemStack it, int slot) {
        LobbyItems.add(new InventoryItem(it, slot));
    }

    public void removeLobbyItem(int slot) {
        InventoryItem toremove = null;
        for (InventoryItem item : LobbyItems) {
            if (item.getSlot() == slot) {
                toremove = item;
                break;
            }
        }
        if (toremove != null) {
            LobbyItems.remove(toremove);
        }
    }

    public void sendToLobbyWithItems(PlayerData pd, boolean teleport) {
        if (pd.getPlayer() == null || !pd.getPlayer().isOnline()) {
            return;
        }
        if (teleport) {
            pd.getPlayer().teleport(getSpawn());
        }
        pd.getPlayer().setGameMode(GameMode.SURVIVAL);
        PlayerInventory inv = pd.getPlayer().getInventory();
        if (clearInventoryOnJoin) {
            inv.clear();
        }
        if (!LobbyItems.isEmpty()) {
            for (InventoryItem item : LobbyItems) {
                inv.setItem(item.getSlot(), item.getItemStack());
            }
        }
        pd.getPlayer().sendMessage(Code.Color("&aHas sido teletransportado al Lobby."));
    }

    @EventHandler
    public void onPre(PlayerLoginEvent event) {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.sendToLobbyWithItems(core.getPlayerData(player.getName()), true);

        if (removeJoinQuitMessages) {
            event.setJoinMessage(null);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (removeJoinQuitMessages) {
            event.setQuitMessage(null);
        }
    }

    @EventHandler
    public void onUpdateSigns(UpdateEvent event) {
        for (LobbySign sign : lobbySigns) {
            sign.update();
        }
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getState() != null && event.getClickedBlock().getState() instanceof Sign) {
                for (LobbySign sign : lobbySigns) {
                    if (sign.getBlock().getLocation().equals(event.getClickedBlock().getLocation())) {
                        sign.interact(core.getPlayerData(event.getPlayer().getName()));
                    }
                }
            }
        }
    }
}
