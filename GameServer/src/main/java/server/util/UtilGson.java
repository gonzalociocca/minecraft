package server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.instance.GameServer;
import server.instance.core.map.base.MapCipherBase;
import server.instance.core.map.misc.MapAdapter;
import server.instance.misc.GameServerAdapter;

/**
 * Created by noname on 22/5/2017.
 */
public class UtilGson {

    public static GsonBuilder getSimpleBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .disableHtmlEscaping()
                .registerTypeHierarchyAdapter(MapCipherBase.class, new MapAdapter());

        return gsonBuilder;
    }

    public static GsonBuilder getBlankBuilder(){
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls();
    }

    public static Gson getBlank(){
        return getBlankBuilder().create();
    }

    public static Gson getSimple(){
        return getSimpleBuilder().create();
    }

    public static GsonBuilder getServerBuilder(){
        return getSimpleBuilder().registerTypeHierarchyAdapter(GameServer.class, new GameServerAdapter());
    }

    public static Gson getServer(){
        return getServerBuilder().create();
    }

}
