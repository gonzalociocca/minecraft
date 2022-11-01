package mineultra.game.center.game.games.pack.buildbattle;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum HeadType
{
  Colors("&cColors"),
  Animals("&cAnimals"),
  Blocks("&cBlocks"),
  Foods("&cFoods"),
  Interior("&cInterior"),
  Misc("&cMisc"),
  Mobs("&cMobs");
  

String name = null;
  private HeadType(String str) {
name=str;
  }
  public String getName(){
      return name;
  }
  
}
