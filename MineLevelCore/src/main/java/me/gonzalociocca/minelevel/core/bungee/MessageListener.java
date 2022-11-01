package me.gonzalociocca.minelevel.core.bungee;

        import java.io.ByteArrayInputStream;
        import java.io.DataInputStream;
        import java.io.IOException;
        import org.bukkit.entity.Player;
        import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener
{
    public ServerConnect connect;

    public MessageListener(ServerConnect instance)
    {
        connect = instance;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message)
    {
        if(!channel.equals("BungeeCord"))
            return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        String sub = "";

        try
        {
            sub = in.readUTF();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        if(sub.equals("GetServers"))
        {
            try
            {
                connect.servers = in.readUTF().split(", ");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        try {
            while(in.available()>0){
                connect.serverscount.put(in.readUTF(),in.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
