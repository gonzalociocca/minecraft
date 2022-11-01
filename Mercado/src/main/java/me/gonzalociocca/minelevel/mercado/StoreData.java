package me.gonzalociocca.minelevel.mercado;

import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuack
 */


public class StoreData {
    
    private Main plugin = null;
    
    private String name;
    private String uuid;
    private String History;
    public String Inventory;
    public StoreData(String gname, Main aThis) {
       plugin = aThis;
       name = gname.toLowerCase();

        try {
            this.loadData();
        } catch (SQLException ex) {
            Logger.getLogger(StoreData.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public void loadData() throws SQLException{
        plugin.checkData();
ResultSet res = null;
res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" WHERE Name = '"+name+"'");
try{res.next();
    this.uuid = res.getString(tablecolumnType.UUID.getName());
    this.History = res.getString(tablecolumnType.History.getName());
    this.Inventory = res.getString(tablecolumnType.Inventory.getName());
res.close();} catch(Exception e){e.printStackTrace();}}


    public String getInventory(){
        return this.Inventory;
    }
    private Integer getValue(tablecolumnType type) throws SQLException{
        ResultSet res = null;
        res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" WHERE Name = '"+name+"'");
        int value = -1;

        try{
            res.next();
            value = res.getInt(type.getName());

            res.close();} catch(Exception e){
            e.printStackTrace();}

        return value;
    }

    public synchronized void addValue(tablecolumnType type, int value){
        try{
            int val = this.getValue(type);

            val+= value;

            String pay = "UPDATE "+plugin.table+" SET "+type.getName()+" ='"+val+"' WHERE Name = '"+name+"'";

            plugin.getStatement().execute(pay);
        }catch(Exception e){

        }}
    public boolean loadItem(ItemStack it){
        String item = "";
        Map<String,Object> map = it.serialize();
        for(String s : map.keySet()){
            item = item+"<div>"+s+"<mid>"+map.get(s)+"</div>";
        }
        Inventory = Inventory+"<item>"+item+"</item>";
        this.setValue(tablecolumnType.Inventory,Inventory);
        return true;
    }
    public synchronized void setValue(tablecolumnType type, String value){
        try{

            String pay = "UPDATE "+plugin.table+" SET "+type.getName()+" ='"+value+"' WHERE Name = '"+name+"'";

            plugin.getStatement().execute(pay);
        }catch(Exception e){
            e.printStackTrace();
        }}


    
}


