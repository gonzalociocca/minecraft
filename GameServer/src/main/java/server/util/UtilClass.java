package server.util;

import com.google.gson.annotations.Expose;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noname on 4/6/2017.
 */
public class UtilClass {
    public static Map<Class, List<Field>> getFieldMap(Object object) {
        HashMap<Class, List<Field>> classFieldHashMap = new HashMap<Class, List<Field>>();

        for (Field field : object.getClass().getFields()) {
            if (field.isAnnotationPresent(Expose.class)) {
                List<Field> list = classFieldHashMap.computeIfAbsent(field.getType(), k -> new ArrayList());
                list.add(field);
            }
        }
        return classFieldHashMap;
    }
}
