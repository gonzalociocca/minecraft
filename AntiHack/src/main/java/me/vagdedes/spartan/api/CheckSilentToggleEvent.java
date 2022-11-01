package me.vagdedes.spartan.api;

import me.vagdedes.spartan.system.Enums;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckSilentToggleEvent extends Event implements Cancellable {

    // $FF: renamed from: Ξ me.vagdedes.spartan.system.Enums$HackType
    private Enums.HackType field_3;
    // $FF: renamed from: Ξ me.vagdedes.spartan.system.Enums$ToggleAction
    private Enums.ToggleAction field_4;
    // $FF: renamed from: Ξ boolean
    private boolean field_5 = false;
    // $FF: renamed from: Ξ org.bukkit.event.HandlerList
    private static final HandlerList field_6 = new HandlerList();

    public CheckSilentToggleEvent(Enums.HackType enums_hacktype, Enums.ToggleAction enums_toggleaction) {
        this.field_3 = enums_hacktype;
        this.field_4 = enums_toggleaction;
    }

    public Enums.HackType getHackType() {
        return this.field_3;
    }

    public Enums.ToggleAction getToggleAction() {
        return this.field_4;
    }

    public boolean isCancelled() {
        return this.field_5;
    }

    public void setCancelled(boolean flag) {
        this.field_5 = flag;
    }

    public HandlerList getHandlers() {
        return CheckSilentToggleEvent.field_6;
    }

    public static HandlerList getHandlerList() {
        return CheckSilentToggleEvent.field_6;
    }
}
