package me.vagdedes.spartan.api;

import me.vagdedes.spartan.system.Enums;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerViolationEvent extends Event implements Cancellable {

    // $FF: renamed from: Ξ org.bukkit.entity.Player
    private Player field_11;
    // $FF: renamed from: Ξ me.vagdedes.spartan.system.Enums$HackType
    private Enums.HackType field_12;
    // $FF: renamed from: Ξ java.lang.String
    private String field_13;
    // $FF: renamed from: Ξ boolean
    private boolean field_14 = false;
    // $FF: renamed from: Ξ org.bukkit.event.HandlerList
    private static final HandlerList field_15 = new HandlerList();

    public PlayerViolationEvent(Player player, Enums.HackType enums_hacktype, String s) {
        this.field_11 = player;
        this.field_12 = enums_hacktype;
        this.field_13 = s;
    }

    public Player getPlayer() {
        return this.field_11;
    }

    public Enums.HackType getHackType() {
        return this.field_12;
    }

    public String getMessage() {
        return this.field_13;
    }

    public boolean isCancelled() {
        return this.field_14;
    }

    public void setCancelled(boolean flag) {
        this.field_14 = flag;
    }

    public HandlerList getHandlers() {
        return PlayerViolationEvent.field_15;
    }

    public static HandlerList getHandlerList() {
        return PlayerViolationEvent.field_15;
    }
}
