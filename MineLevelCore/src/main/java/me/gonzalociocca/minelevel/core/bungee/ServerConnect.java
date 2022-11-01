package me.gonzalociocca.minelevel.core.bungee;

        import java.io.ByteArrayOutputStream;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;

        import org.bukkit.Bukkit;
        import org.bukkit.entity.Player;
        import org.bukkit.event.Listener;
        import org.bukkit.plugin.Plugin;
        import org.bukkit.plugin.PluginManager;
        import org.bukkit.scheduler.BukkitScheduler;

public class ServerConnect implements Listener {
    public MessageListener	ml;

    public String[] servers	= null;
    public Map<String,Integer> serverscount = new HashMap();

    Plugin plugin = null;
    public void enable(Plugin main)
    {
        plugin = main;
        Bukkit.getMessenger().registerOutgoingPluginChannel(main, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(main, "BungeeCord", new MessageListener(this));

        getServers();

        ml = new MessageListener(this);

        PluginManager pm = Bukkit.getServer().getPluginManager();

       // pm.registerEvents(new PreCommandListener(this), main);
    }

    public void getServers()
    {

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                if(Bukkit.getOnlinePlayers().size() > 0)
                {
                    Player player = (Player)Bukkit.getOnlinePlayers().toArray()[0];

                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try{
                        out.writeUTF("GetServers");
                        out.close();
                    }catch(IOException e){e.printStackTrace();}
                    player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

                    if(servers!=null){
                        for(String s : servers){
                            b = new ByteArrayOutputStream();
                            out = new DataOutputStream(b);
                            try{
                                out.writeUTF("PlayerCount");
                                out.writeUTF(s);
                                out.close();
                            }catch(IOException e){e.printStackTrace();}
                            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                        }
                    }
                }
            }
        }, 0L, 80L);
    }
}
