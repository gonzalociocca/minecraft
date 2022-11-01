package server.common;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noname on 25/5/2017.
 */
public class LongMessage {

    String message = "";
    public LongMessage(){

    }

    public void add(String str){
        message = message.isEmpty() ? str : message + " " + str;
    }

    public List<String> get(){
        List<String> args = new ArrayList();

        String[] split = message.split(" ");
        String current = "";
        for(String str : split){
            int newSize = current.length() + str.length();
            if(newSize > 48){
                args.add(current);
                current = "";
            }
            current = current.isEmpty() ? str : current + " " + str;
        }
        if(current.length() > 0){
            args.add(current);
        }
        return args;
    }

    public void send(Player player){
        for(String str : get()){
            player.sendMessage(str);
        }
    }
}
