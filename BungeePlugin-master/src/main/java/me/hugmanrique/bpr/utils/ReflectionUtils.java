package me.hugmanrique.bpr.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public class ReflectionUtils {
    public static void invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            Method method = object.getClass().getDeclaredMethod(methodName);

            method.setAccessible(true);
            method.invoke(object, args);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <T> T getFieldValue(Object obj, String fieldName){
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static void setFieldValue(Object obj, String fieldName, Object value){
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            field.set(obj, value);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <T> T getStaticValue(Class<?> clazz, String fieldName){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            return (T) field.get(null);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
