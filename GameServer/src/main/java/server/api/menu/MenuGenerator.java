package server.api.menu;

import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Created by noname on 26/5/2017.
 */
    public abstract class MenuGenerator {

        public boolean onCommand(String[] args){return false;}
        public boolean onInteractEntity(Entity entity){return false;}
        public boolean onInteractSign(Sign sign){return false;}
        public abstract Menu createMenu(Player player, Menu.OpenReason openReason);
    }
