package mineultra.game.center.game.games.spleef;

import mineultra.core.itemstack.ItemStackFactory;
import mineultra.game.center.centerManager;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.FLETCHER;
import mineultra.game.center.kit.perks.KNOCKBACK;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitArcher
  extends Kit
{
  public KitArcher(centerManager manager)
  {
    super(manager, "Archer", KitAvailability.Blue, new String[] {"Arrows will damage spleef blocks in a small radius." }, new Perk[] {new FLETCHER(2, 2, false), new KNOCKBACK(0.3D) }, EntityType.SKELETON, new ItemStack(Material.BOW));
  }
  
  @Override
  public void GiveItems(Player player)
  {
    player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.BOW) });
  }
}
