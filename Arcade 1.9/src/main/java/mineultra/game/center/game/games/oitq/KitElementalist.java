package mineultra.game.center.game.games.oitq;


import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilServer;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.game.center.centerManager;
import mineultra.game.center.game.Game;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.ARROWREBOUND;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitElementalist
  extends Kit
{
  public KitElementalist(centerManager manager)
  {
    super(manager, "Enchanter", KitAvailability.Blue, new String[] {"3 Kills, 1 Arrow." }, new Perk[] {new ARROWREBOUND(2, 1.2F) }, EntityType.ZOMBIE, new ItemStack(Material.BOW));
  }
  
  @Override
  public void GiveItems(Player player)
  {
    player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.STONE_SWORD) });
    player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.BOW) });
    if (this.Manager.GetGame().GetState() == Game.GameState.Live)
    {
      player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(262,(byte) 1, 1, F.item("Super Arrow")) });
      
      final Player fPlayer = player;
      
      UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(this.Manager.GetPlugin(), new Runnable()
      {
        @Override
        public void run()
        {
          UtilInv.Update(fPlayer);
        }
      }, 10L);
    }
  }
}
