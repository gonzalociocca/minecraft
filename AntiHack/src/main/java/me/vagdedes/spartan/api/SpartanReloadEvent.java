package me.vagdedes.spartan.api;

import me.vagdedes.spartan.Register;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class SpartanReloadEvent extends Event implements Cancellable {

    // $FF: renamed from: Ξ boolean
    private boolean field_53 = false;
    // $FF: renamed from: Ξ org.bukkit.event.HandlerList
    private static final HandlerList field_54 = new HandlerList();

    public Plugin getPlugin() {
        return Register.field_249;
    }

    public boolean isCancelled() {
        return this.field_53;
    }

    public void setCancelled(boolean flag) {
        this.field_53 = flag;
    }

    public HandlerList getHandlers() {
        return SpartanReloadEvent.field_54;
    }

    public static HandlerList getHandlerList() {
        return SpartanReloadEvent.field_54;
    }
}
