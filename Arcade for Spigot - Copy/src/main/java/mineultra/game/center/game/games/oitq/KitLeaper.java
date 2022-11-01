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
import mineultra.game.center.kit.perks.DOUBLEJUMP;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitLeaper
  extends Kit
{
  public KitLeaper(centerManager manager)
  {
    super(manager, "Jumper", KitAvailability.Free, new String[] {"Evade and kill using your double jump!" }, new Perk[] {new DOUBLEJUMP("Double Jump", 0.9D, 0.9D, true) }, EntityType.ZOMBIE, new ItemStack(Material.IRON_AXE));
  }
  
  @Override
  public void GiveItems(Player player)
  {
    player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.IRON_AXE) });
    player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.BOW) });
    if (this.Manager.GetGame().GetState() == Game.GameState.Live)
    {
      player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(262, (byte)1, 1, F.item("Super Arrow")) });
      
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
