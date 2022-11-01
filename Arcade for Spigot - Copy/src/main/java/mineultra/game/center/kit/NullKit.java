package mineultra.game.center.kit;

import org.bukkit.entity.Creature;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import mineultra.game.center.centerManager;

public class NullKit extends Kit
{
    public NullKit(final centerManager manager) {
        super(manager, "Null Kit", KitAvailability.Null, new String[] { "It does nothing!" }, new Perk[] { }, null, null);
    }
    
    @Override
    public void GiveItems(final Player player) {
    }
    
    @Override
    public Creature SpawnEntity(final Location loc) {
        return null;
    }
}
