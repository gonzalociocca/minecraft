package server.user.column.Simple.Type;

import com.google.gson.annotations.Expose;
import server.instance.loader.SkyWars.misc.CageEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cage {

    // StorageId, List<String>
    @Expose
    HashMap<String, List<String>> cageListMap = null;

    // StorageId, String
    @Expose
    HashMap<String, String> defaultCageMap = null;

    boolean modified = false;

    public Cage() {

    }

    public void safeCheck(){
        if(cageListMap == null){
            cageListMap = new HashMap();
        }
        if(defaultCageMap == null){
            defaultCageMap = new HashMap();
        }
    }

    public boolean hasCage(String storageId, CageEnum cageEnum) {
        List<String> cageList = cageListMap != null ? cageListMap.get(storageId) : null;
        return cageList != null && cageList.contains(cageEnum.getID());
    }

    public void addCage(String storageId, CageEnum cageEnum) {
        safeCheck();
        List<String> cageList = cageListMap.get(storageId);

        if (cageList == null) {
            cageList = new ArrayList();
            cageListMap.put(storageId, cageList);
        }

        cageList.add(cageEnum.getID());
        modified = true;
    }

    public void removeCage(String storageId, CageEnum cageEnum) {
        safeCheck();
        List<String> cageList = cageListMap.get(storageId);

        if (cageList != null) {
            cageList.remove(cageEnum.getID());
        }
        modified = true;
    }

    public CageEnum getDefaultCage(String storageId) {
        return CageEnum.getByID(defaultCageMap != null && storageId != null ? defaultCageMap.get(storageId) : null);
    }

    public boolean isModified(){
        return modified;
    }

    public void setModified(boolean value){
        modified = value;
    }
}
