package me.gonzalociocca.versionupdate.customhopper;
 
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
 
public class CustomHopperEvent extends Event {

ItemStack fromMove = null;
int fromMoveSlot = -1;
boolean added = false;
boolean removed = false;

public CustomHopperEvent(Inventory hopper, Inventory dest){

if(hopper==null || dest == null){
    return;
}
    if(dest.firstEmpty() == -1){
        return;
    }
  for(ItemStack it : hopper.getContents()){
  fromMoveSlot++;
      if(it==null){
          continue;
      }
      fromMove = it;
      break;
  }
  if(fromMove == null){
      return;
  }
  if(fromMove.getAmount() > 1){
      fromMove.setAmount(fromMove.getAmount()-1);
      removed = true;
  }else{
      hopper.setItem(fromMoveSlot, new ItemStack(Material.AIR));
      removed=true;
  }
  
  
  if(dest.containsAtLeast(fromMove, 1)){
      for(ItemStack it : dest.getContents()){
          if(it==null){continue;}
          if(!it.isSimilar(fromMove)){
              continue;
          }
          if(it.getAmount() < it.getMaxStackSize()){
              it.setAmount(it.getAmount()+1);
              added = true;
              break;
          }
      }
  }
  if(added == false){
          ItemStack clone = fromMove.clone();
          clone.setAmount(1);
          dest.addItem(clone);
          added = true;
  }}


//System.out.println("Fired: added="+added+" removed="+removed);

  private static final HandlerList handlers = new HandlerList();
  
@Override
  public HandlerList getHandlers()
  {
    return handlers;
  }

}
 