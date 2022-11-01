package me.vagdedes.spartan.system;

public class Enums {

    public static enum ToggleAction {

        Enable, Disable;
    }

    public static enum Permission {

        staff_chat, wave, reconnect, admin, reload, kick, verbose, bypass, manage, info, kick_message, ping, chat_protection, toggle, warn, use_bypass, ban, unban, ban_info, ban_message, mining;
    }

    public static enum HackType {

        Exploits, Phase, EntityMove, NoSwing, NormalMovements, Clip, ImpossibleActions, ItemDrops, AutoRespawn, InventoryClicks, Sprint, Jesus, NoSlowdown, Criticals, Nuker, GhostHand, Liquids, BlockReach, ElytraMove, BoatMove, FastBow, FastClicks, FastHeal, Fly, ImpossibleInventory, HitReach, FastBreak, Speed, FastPlace, MorePackets, NoFall, IllegalPosition, FastEat, Velocity, KillAura;
    }
}
