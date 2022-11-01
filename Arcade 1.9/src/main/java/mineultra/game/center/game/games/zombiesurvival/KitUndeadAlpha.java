package mineultra.game.center.game.games.zombiesurvival;


import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.game.center.centerManager;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.*;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

public class KitUndeadAlpha
  extends Kit
{
  public KitUndeadAlpha(centerManager manager)
  {
    super(manager, "Alpha Undead", KitAvailability.Free, new String[] {"Leap at those undead" }, new Perk[] {new LEAP("Leap", 1.0D, 1.0D, 8000L), new STRENGTH(0), new IRONDEF(2.0D), new REGENERATION(1),new SPEED(1)}, EntityType.SKELETON, new ItemStack(Material.STONE_AXE));
  }
  
  @Override
  public void GiveItems(Player player)
  {
    player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.DIAMOND_AXE) });

    MobDisguise disguise = new MobDisguise(DisguiseType.WITHER_SKELETON);
    String name = (this.Manager.GetGame().GetTeam(player).GetColor() + player.getName());
    if(name.length() > 16){
      name = name.substring(0,15);
    }
    disguise.getWatcher().setCustomName(name);
    disguise.getWatcher().setCustomNameVisible(true);
    disguise.setHearSelfDisguise(true);
  
    DisguiseAPI.disguiseToAll(player, disguise);
  }
  
  @Override
  public void SpawnCustom(LivingEntity ent)
  {
    if ((ent instanceof Skeleton))
    {
      Skeleton skel = (Skeleton)ent;
      skel.setSkeletonType(Skeleton.SkeletonType.WITHER);
    }
  }
}
