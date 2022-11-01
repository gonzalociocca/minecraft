/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.game.games.spleef;

import mineultra.core.itemstack.ItemStackFactory;
import mineultra.game.center.centerManager;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.KNOCKBACK;
import mineultra.game.center.kit.perks.LEAP;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitLeaper
  extends Kit
{
  public KitLeaper(centerManager manager)
  {
    super(manager, "Jumper", KitAvailability.Free, new String[] {"Leap to escape and damage blocks!" }, new Perk[] {new LEAP("Smashing Leap", 1.2D, 1.2D, 8000L), new KNOCKBACK(0.3D) }, EntityType.PIG_ZOMBIE, new ItemStack(Material.STONE_AXE));
  }
  
  @Override
  public void GiveItems(Player player)
  {
    player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.STONE_AXE) });
  }
}
