package mineultra.game.center.kit;

import org.bukkit.ChatColor;

public enum KitAvailability
{
    Free("Free", 0, ChatColor.YELLOW), 
    Green("Green", 1, ChatColor.GREEN), 
    Blue("Blue", 2, ChatColor.AQUA), 
    Hide("Hide", 3, ChatColor.YELLOW), 
    Null("Null", 4, ChatColor.BLACK);
    
    ChatColor _color;
    
    private KitAvailability(final String s, final int n, final ChatColor color) {
        this._color = color;
    }
    
    public ChatColor GetColor() {
        return this._color;
    }
}
