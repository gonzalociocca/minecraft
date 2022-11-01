package server.instance.core.map.misc;

import com.google.gson.*;
import server.instance.core.map.base.MapCipherBase;
import server.util.UtilGson;

import java.lang.reflect.Type;

/**
 * Created by noname on 24/5/2017.
 */
public class MapAdapter implements JsonDeserializer<MapCipherBase> {

    @Override
    public MapCipherBase deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        Class<?> classReference = toClass(jsonObject.get("classIdentifier").getAsString());

        return (MapCipherBase) UtilGson.getBlank().fromJson(jsonObject, classReference);
    }

    public static Class<?> toClass(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
