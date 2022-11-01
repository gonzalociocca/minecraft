package me.vagdedes.spartan.api;

import me.vagdedes.spartan.system.Enums;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerViolationCommandEvent extends Event implements Cancellable {

    // $FF: renamed from: Ξ org.bukkit.entity.Player
    private Player field_143;
    // $FF: renamed from: Ξ me.vagdedes.spartan.system.Enums$HackType
    private Enums.HackType field_144;
    // $FF: renamed from: Ξ java.lang.String
    private String field_145;
    // $FF: renamed from: Ξ boolean
    private boolean field_146 = false;
    // $FF: renamed from: Ξ org.bukkit.event.HandlerList
    private static final HandlerList field_147 = new HandlerList();

    public PlayerViolationCommandEvent(Player player, Enums.HackType enums_hacktype, String s) {
        this.field_143 = player;
        this.field_144 = enums_hacktype;
        this.field_145 = s;
    }

    public Player getPlayer() {
        return this.field_143;
    }

    public Enums.HackType getHackType() {
        return this.field_144;
    }

    public String getCommand() {
        return this.field_145;
    }

    public boolean isCancelled() {
        return this.field_146;
    }

    public void setCancelled(boolean flag) {
        this.field_146 = flag;
    }

    public HandlerList getHandlers() {
        return PlayerViolationCommandEvent.field_147;
    }

    public static HandlerList getHandlerList() {
        return PlayerViolationCommandEvent.field_147;
    }
}
