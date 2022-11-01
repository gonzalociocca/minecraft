package server.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import server.ServerPlugin;
import server.api.chat.ChatCallable;
import server.common.Code;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noname on 2/6/2017.
 */
public class ChatAPI implements Listener{

    public ChatAPI() {
        Bukkit.getPluginManager().registerEvents(this, ServerPlugin.getInstance());
    }

    private static Map<String, ChatCallable> chatCallableList = new HashMap();

    public static ChatCallable getChatCallable(String str){
        return chatCallableList.get(str);
    }

    public static void addChatCallable(Player player, ChatCallable chatCallable){
        player.sendMessage(Code.Color("&a&nEscribe una respuesta en el chat."));
        chatCallableList.put(player.getName(), chatCallable);
    }

    public static void removeChatCallable(String str){
        chatCallableList.remove(str);
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        ChatCallable chatCallable = getChatCallable(event.getPlayer().getName());
        if(chatCallable != null){
            removeChatCallable(event.getPlayer().getName());
            if(chatCallable.onChat(event.getPlayer(), event.getMessage())){
                event.setCancelled(true);
            }
        }
    }

}
