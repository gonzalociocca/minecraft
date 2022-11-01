package server.instance.misc;

import com.google.gson.*;
import server.instance.GameServer;
import server.util.UtilGson;

import java.lang.reflect.Type;

/**
 * Created by noname on 24/5/2017.
 */
public class GameServerAdapter implements JsonDeserializer<GameServer> {

    @Override
    public GameServer deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        Class<?> classReference = toClass(jsonObject.get("classIdentifier").getAsString());

        JsonArray jsonArray = jsonObject.getAsJsonArray("subServerList");
        jsonObject.remove("subServerList");
        GameServer gameServer = (GameServer) UtilGson.getSimple().fromJson(jsonObject, classReference);
        if(!jsonArray.isJsonNull() && jsonArray.size() > 0){
            for(JsonElement obj : jsonArray){
                gameServer.subServerList.add((GameServer) UtilGson.getServer().fromJson(obj, toClass(obj.getAsJsonObject().get("classIdentifier").getAsString())));
            }
        }

        return gameServer;
    }

    public static Class<?> toClass(String str){
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
