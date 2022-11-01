package mineultra.game.center.game.games.survivalgames.kit;

import mineultra.game.center.centerManager;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.BACKSTAB;
import mineultra.game.center.kit.perks.IRONDEF;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitKnight
        extends Kit
{
    public KitKnight(centerManager manager)
    {
        super(manager, "Knight", KitAvailability.Free, new String[0], new Perk[] { new IRONDEF(0.5D) }, EntityType.ZOMBIE, new ItemStack(Material.IRON_SWORD));
    }

    public void GiveItems(Player player) {}

    @Override
    public boolean isenabled(){
        return true;
    }

    @Override
    public Entity SpawnEntity(final Location loc) {
        return null;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.IRON_SWORD;
    }

}
