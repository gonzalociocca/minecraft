package me.vagdedes.spartan.api;

import me.vagdedes.spartan.system.Enums;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckToggleEvent extends Event implements Cancellable {

    // $FF: renamed from: Ξ me.vagdedes.spartan.system.Enums$HackType
    private Enums.HackType field_244;
    // $FF: renamed from: Ξ me.vagdedes.spartan.system.Enums$ToggleAction
    private Enums.ToggleAction field_245;
    // $FF: renamed from: Ξ boolean
    private boolean field_246 = false;
    // $FF: renamed from: Ξ org.bukkit.event.HandlerList
    private static final HandlerList field_247 = new HandlerList();

    public CheckToggleEvent(Enums.HackType enums_hacktype, Enums.ToggleAction enums_toggleaction) {
        this.field_244 = enums_hacktype;
        this.field_245 = enums_toggleaction;
    }

    public Enums.HackType getHackType() {
        return this.field_244;
    }

    public Enums.ToggleAction getToggleAction() {
        return this.field_245;
    }

    public boolean isCancelled() {
        return this.field_246;
    }

    public void setCancelled(boolean flag) {
        this.field_246 = flag;
    }

    public HandlerList getHandlers() {
        return CheckToggleEvent.field_247;
    }

    public static HandlerList getHandlerList() {
        return CheckToggleEvent.field_247;
    }
}
