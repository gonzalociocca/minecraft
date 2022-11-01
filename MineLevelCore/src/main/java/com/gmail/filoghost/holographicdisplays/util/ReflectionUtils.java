//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gmail.filoghost.holographicdisplays.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionUtils {
    private static Method getStackTraceElementMethod;

    public ReflectionUtils() {
    }

    public static void putInPrivateStaticMap(Class<?> clazz, String fieldName, Object key, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Map map = (Map)field.get((Object)null);
        map.put(key, value);
    }

    public static void setPrivateField(Class<?> clazz, Object handle, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(handle, value);
    }

    public static Object getPrivateField(Class<?> clazz, Object handle, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(handle);
    }

    public static StackTraceElement getStackTraceElement(int index) {
        try {
            if(getStackTraceElementMethod == null) {
                getStackTraceElementMethod = Throwable.class.getDeclaredMethod("getStackTraceElement", new Class[]{Integer.TYPE});
                getStackTraceElementMethod.setAccessible(true);
            }

            Throwable throwable = new Throwable();
            return (StackTraceElement)getStackTraceElementMethod.invoke(throwable, new Object[]{Integer.valueOf(index)});
        } catch (Exception var2) {
            return (new Throwable()).getStackTrace()[index];
        }
    }
}
