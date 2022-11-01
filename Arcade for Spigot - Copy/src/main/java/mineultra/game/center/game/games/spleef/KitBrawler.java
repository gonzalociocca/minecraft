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
import mineultra.game.center.kit.perks.SMASHER;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitBrawler
  extends Kit
{
  public KitBrawler(centerManager manager)
  {
    super(manager, "Brawler", KitAvailability.Green, new String[] {"Much stronger knockback than other kits." }, new Perk[] {new SMASHER(), new KNOCKBACK(0.6D) }, EntityType.ZOMBIE, new ItemStack(Material.IRON_SWORD));
  }
  
  @Override
  public void GiveItems(Player player)
  {
    player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD) });
  }
}
