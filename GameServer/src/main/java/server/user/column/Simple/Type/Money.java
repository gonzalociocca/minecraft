package server.user.column.Simple.Type;

import com.google.gson.annotations.Expose;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectDoubleHashMap;

/**
 * Created by noname on 19/5/2017.
 */
public class Money {

    @Expose
    ObjectDoubleHashMap map = null;

    boolean modified = false;

    public void safeCheck(){
        if(map == null){
            map = new ObjectDoubleHashMap();
        }
    }

    public double get(String storageId){
        return map.get(storageId);
    }

    public void add(String storageId, double value){
        safeCheck();
        map.addToValue(storageId, value);
        modified = true;
    }

    public void substract(String storageId, double value){
        safeCheck();
        if(value < 0){
            map.addToValue(storageId, value);
        } else {
            map.addToValue(storageId, -value);
        }
        modified = true;
    }

    public boolean isModified(){
        return modified;
    }

    public void setModified(boolean value){
        modified = value;
    }
}
