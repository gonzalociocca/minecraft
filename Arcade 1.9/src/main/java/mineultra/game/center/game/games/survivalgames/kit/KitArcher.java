package mineultra.game.center.game.games.survivalgames.kit;

        import mineultra.core.common.util.Colorizer;
        import mineultra.game.center.centerManager;

        import mineultra.game.center.kit.Kit;
        import mineultra.game.center.kit.KitAvailability;
        import mineultra.game.center.kit.Perk;
        import mineultra.game.center.kit.perks.BARRAGE;
        import mineultra.game.center.kit.perks.FLETCHER;
        import org.bukkit.Location;
        import org.bukkit.Material;
        import org.bukkit.entity.Entity;
        import org.bukkit.entity.EntityType;
        import org.bukkit.entity.Player;
        import org.bukkit.inventory.ItemStack;

public class KitArcher
        extends Kit
{
    public KitArcher(centerManager manager)
    {
        super(manager, "Archer", KitAvailability.Green, new String[] {Colorizer.Color("&7Craftea flechas con el terreno.") }, new Perk[] { new FLETCHER(20, 3, true), new BARRAGE(5, 250L, true, false) }, EntityType.ZOMBIE, new ItemStack(Material.BOW));
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
        return Material.SPECTRAL_ARROW;
    }
}
