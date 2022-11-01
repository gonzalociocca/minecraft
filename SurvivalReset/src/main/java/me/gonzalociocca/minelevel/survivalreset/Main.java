package me.gonzalociocca.minelevel.survivalreset;

/**
 *
 * @author ciocca
 */
        import net.minecraft.server.v1_8_R3.*;
        import org.bukkit.*;
        import org.bukkit.configuration.file.FileConfiguration;
        import org.bukkit.configuration.file.YamlConfiguration;
        import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
        import org.bukkit.entity.*;
        import org.bukkit.event.EventHandler;
        import org.bukkit.event.Listener;
        import org.bukkit.event.player.PlayerJoinEvent;
        import org.bukkit.event.player.PlayerLoginEvent;
        import org.bukkit.plugin.PluginManager;
        import org.bukkit.plugin.java.JavaPlugin;
        import java.io.File;
        import java.io.IOException;
        import java.util.*;


public class Main extends JavaPlugin implements Listener {
    Main plugin = null;


    File messageYML;
    FileConfiguration messageConfig;

    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(cbc, (byte) 2));
    }

    @Override
    public void onEnable() {
        plugin = this;
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this, this);


        try {
            this.messageYML = new File(getDataFolder(), "/messages.yml");
            if (!this.messageYML.exists()) {
                try {
                    this.messageYML.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.messageConfig = YamlConfiguration.loadConfiguration(this.messageYML);

            saveMessagesConfig();
            saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.InitWorldReset();
    }

    public void InitWorldReset(){
        org.bukkit.World world = Bukkit.getWorld("world");
    }

    @EventHandler
    public void onJoin1(PlayerJoinEvent event){
        event.getPlayer().kickPlayer("Mantenimiento del Server.");
    }
    @EventHandler
    public void onLogin1(PlayerLoginEvent event){
        event.setKickMessage("Mantenimiento del Server.");
        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
    }

    @Override
    public void onDisable() {
    }


    public FileConfiguration getMessagesConfig() {
        return this.messageConfig;
    }

    public void saveMessagesConfig() {
        try {
            this.messageConfig.save(this.messageYML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
