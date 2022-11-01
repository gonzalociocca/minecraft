package server.user.column.Simple.Type;

import com.google.gson.annotations.Expose;
import server.instance.core.kit.Kit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Kits {

    @Expose
    HashMap<String, List<String>> _kitListMap = null;

    @Expose
    HashMap<String, String> _defaultKit = null;

    boolean modified = false;

    public Kits() {

    }

    public void safeCheck(){
        if(_kitListMap == null) {
            _kitListMap = new HashMap();
        }
        if(_defaultKit == null){
            _defaultKit = new HashMap();
        }
    }

    public boolean has(String storageId, Kit kit) {
        List<String> kitList = _kitListMap != null ? _kitListMap.get(storageId) : null;
        return kitList != null && kitList.contains(kit.getPermission());
    }

    public void add(String storageId, Kit kit) {
        safeCheck();
        List<String> kits = _kitListMap.get(storageId);
        if (kits == null) {
            kits = new ArrayList();
            _kitListMap.put(storageId, kits);
        }
        kits.add(kit.getPermission());
        modified = true;
    }

    public String getDefault(String storageId) {
        return _defaultKit != null ? _defaultKit.get(storageId) : null;
    }

    public void setDefault(String storageId, Kit kit) {
        safeCheck();
        _defaultKit.put(storageId, kit.getPermission());
        modified = true;
    }

    public boolean isModified(){
        return modified;
    }

    public void setModified(boolean value){
        modified = value;
    }
}
