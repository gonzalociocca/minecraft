package me.gonzalociocca.minigame.map.Cipher;

/**
 * Created by noname on 30/3/2017.
 */
public class BlockParseData {

    public enum DataType {
        Location(), LocationList();
        DataType() {
        }


    }
    int id;
    int data;
    DataType type;
    String name;
    public BlockParseData(String aname, int aid, int adata, DataType atype){
        id = aid;
        data = adata;
        type = atype;
        name = aname;
    }
    public int getID(){
        return id;
    }
    public int getData(){
        return data;
    }
    public DataType getType(){
        return type;
    }
    public String getIdentifier(){
        return name;
    }
}
