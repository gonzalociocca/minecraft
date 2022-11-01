package mineultra.minecraft.game.core;

import org.bukkit.entity.Player;

public interface IRelation
{
    boolean CanHurt(Player p0, Player p1);
    
    boolean CanHurt(String p0, String p1);
    
    boolean IsSafe(Player p0);
}
