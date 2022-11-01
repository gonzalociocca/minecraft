package server.user.column.Simple.Type;

import com.google.gson.annotations.Expose;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectIntHashMap;

/**
 * Created by noname on 19/5/2017.
 */
public class BlockBreak {

    @Expose
    ObjectIntHashMap map = null;

    boolean modified = false;

    public void safeCheck(){
        if(map == null){
            map = new ObjectIntHashMap();
        }
    }

    public int get(String storageId){
        return map.get(storageId);
    }

    public void add(String storageId, int value){
        safeCheck();
        map.addToValue(storageId, value);
        modified = true;
    }

    public void substract(String storageId, int value){
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
