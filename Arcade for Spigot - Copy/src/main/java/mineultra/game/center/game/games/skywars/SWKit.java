package mineultra.game.center.game.games.skywars;

import java.util.ArrayList;
import java.util.List;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.ItemReader;
import mineultra.core.common.util.Perks;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.itemstack.ItemStackFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.centerManager;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.perks.ROPEDARROW;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class SWKit extends Kit
{
    int KitNumber = 0;
    boolean enabled = false;
    public SWKit(final centerManager manager, int number) {
        super(manager, "Archer", KitAvailability.Green,new String[]{}
                , new Perk[]{}, EntityType.PLAYER, new ItemStack(Material.BOW));
    this.KitNumber = number;
if(this.Manager.getConfig().getString("kits.kit"+KitNumber+".enabled") == null){
    HandlerList.unregisterAll(this);

return;
}
enabled = this.Manager.getConfig().getBoolean("kits.kit"+KitNumber+".enabled");
   
    }
    
    @Override
    public String GetName(){

     String name = this.Manager.getConfig().getString("kits.kit"+KitNumber+".name");
this._kitName = name;
return name;

    }
  Perk[] perks = null;
    @Override
    public Perk[] GetPerks(){

  if(perks == null){
      List<Perk> newperks = new ArrayList<>();
List<String> mylist = this.Manager.getConfig().getStringList("kits.kit"+KitNumber+".perks");
if(mylist != null){
    if(!mylist.isEmpty()){
for(String pka : mylist){
 Perk add = Perks.perkbyName(pka);
 if(add != null){
     add.SetHost(this);
     newperks.add(add);
 }
  }
    }
}
  perks = newperks.toArray(new Perk[0]);
  }
return perks;

    }
    
    @Override
    public String[] GetDesc(){
List<String> predesc = this.Manager.getConfig().getStringList("kits.kit"+KitNumber+".desc");
if(predesc == null){
    return new String[0];
}
String[] desc = predesc.toArray(new String[0]);
for(int a = 0; a < desc.length;a++){
    desc[a] = Colorizer.Color(desc[a]);
}
return desc;
    }

@Override
public int GetCost() {
return this.Manager.getConfig().getInt("kits.kit"+KitNumber+".price");
    }
    
    @Override
    public void GiveItems(final Player player) {
      List<String> equipment =  this.Manager.getConfig().getStringList("kits.kit"+KitNumber+".equipment");
      List<String> inventory =  this.Manager.getConfig().getStringList("kits.kit"+KitNumber+".inventory");
      
      if(equipment != null){
          if(!equipment.isEmpty()){
  for(String str : equipment){
      ItemStack item = ItemReader.read(str);
      if(item != null){
          if(item.getType().name().toLowerCase().contains("helm")){
              player.getInventory().setHelmet(item);
          }else if(item.getType().name().toLowerCase().contains("plate")){
              player.getInventory().setChestplate(item);
          }else if(item.getType().name().toLowerCase().contains("leggin")){
              player.getInventory().setLeggings(item);
          }else if(item.getType().name().toLowerCase().contains("boot")){
              player.getInventory().setBoots(item);
          }else try{
         player.getInventory().setHelmet(item);
          }catch(Exception e){
              
          }
      }
  }
  
          }
      }
      
      if(inventory != null){
          if(!inventory.isEmpty()){
  for(String str : inventory){
      ItemStack item = ItemReader.read(str);
      if(item != null){
player.getInventory().addItem(item);
      }
  }
          }
      }
              
      
    }
    
    @Override
    public Material getDisplayMaterial(){
     return Material.getMaterial(this.Manager.getConfig().getInt("kits.kit"+KitNumber+".icon"));
    }
    
    @Override
  public boolean isenabled(){
    return enabled;
}
    @Override
    public Entity SpawnEntity(final Location loc) {

        return null;
    }
}
