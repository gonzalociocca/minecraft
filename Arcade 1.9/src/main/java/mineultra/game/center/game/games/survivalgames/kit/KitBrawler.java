package mineultra.game.center.game.games.survivalgames.kit;

import mineultra.core.common.util.Colorizer;
import mineultra.game.center.centerManager;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.BACKSTAB;
import mineultra.game.center.kit.perks.MAMMOTH;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitBrawler
        extends Kit
{
    public KitBrawler(centerManager manager)
    {
        super(manager, "Brawler", KitAvailability.Green, new String[] { Colorizer.Color("&7Gigante y Musculoso") }, new Perk[] { new MAMMOTH() }, EntityType.ZOMBIE, new ItemStack(Material.IRON_SWORD));
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
        return Material.SLIME_BLOCK;
    }

}
