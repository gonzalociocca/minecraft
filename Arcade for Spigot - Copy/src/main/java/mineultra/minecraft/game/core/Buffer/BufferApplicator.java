package mineultra.minecraft.game.core.Buffer;

import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.entity.LivingEntity;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;

public class BufferApplicator
{
    private HashMap<String, PotionEffectType> _effectMap;
    
    public BufferApplicator() {
        super();
        (this._effectMap = new HashMap<String, PotionEffectType>()).put("Blindness", PotionEffectType.BLINDNESS);
        this._effectMap.put("Confusion", PotionEffectType.CONFUSION);
        this._effectMap.put("DamageResist", PotionEffectType.DAMAGE_RESISTANCE);
        this._effectMap.put("FastDig", PotionEffectType.FAST_DIGGING);
        this._effectMap.put("FireResist", PotionEffectType.FIRE_RESISTANCE);
        this._effectMap.put("Harm", PotionEffectType.HARM);
        this._effectMap.put("Heal", PotionEffectType.HEAL);
        this._effectMap.put("Hunger", PotionEffectType.HUNGER);
        this._effectMap.put("Strength", PotionEffectType.INCREASE_DAMAGE);
        this._effectMap.put("Jump", PotionEffectType.JUMP);
        this._effectMap.put("Poison", PotionEffectType.POISON);
        this._effectMap.put("Regeneration", PotionEffectType.REGENERATION);
        this._effectMap.put("Slow", PotionEffectType.SLOW);
        this._effectMap.put("SlowDig", PotionEffectType.SLOW_DIGGING);
        this._effectMap.put("Speed", PotionEffectType.SPEED);
        this._effectMap.put("Breathing", PotionEffectType.WATER_BREATHING);
        this._effectMap.put("Weakness", PotionEffectType.WEAKNESS);
        this._effectMap.put("Invisibility", PotionEffectType.INVISIBILITY);
        this._effectMap.put("NightVision", PotionEffectType.NIGHT_VISION);
    }
    
    public void clearEffects(final Player player) {
        for (final PotionEffectType cur : this._effectMap.values()) {
            player.removePotionEffect(cur);
        }
    }
    
    public void listEffect(final Player caller) {
        caller.sendMessage(ChatColor.RED + "[B] " + ChatColor.YELLOW + "Listing Potion Effects;");
        caller.sendMessage(ChatColor.DARK_GREEN + "Health Effects: " + ChatColor.AQUA + "Harm, Heal, Poison, Regeneration, Hunger");
        caller.sendMessage(ChatColor.DARK_GREEN + "Damage Effects: " + ChatColor.AQUA + "Strength, Weakness, DamageResist, FireResist");
        caller.sendMessage(ChatColor.DARK_GREEN + "Movement Effects: " + ChatColor.AQUA + "Slow, Speed, Jump, FireResist");
        caller.sendMessage(ChatColor.DARK_GREEN + "Vision Effects: " + ChatColor.AQUA + "Blindness, Confusion, NightVision");
        caller.sendMessage(ChatColor.DARK_GREEN + "Misc Effects: " + ChatColor.AQUA + "FastDig, SlowDig, Breathing, Invisibility");
    }
    
    public HashMap<String, PotionEffectType> readEffect(final Player caller, final String eString) {
        final HashMap<String, PotionEffectType> eList = new HashMap<String, PotionEffectType>();
        final ArrayList<String> errorList = new ArrayList<String>();
        final String[] eToken = eString.split(",");
        String[] array;
        for (int length = (array = eToken).length, i = 0; i < length; ++i) {
            final String eCur = array[i];
            for (final String cur : this._effectMap.keySet()) {
                if (cur.equalsIgnoreCase(eCur)) {
                    eList.put(cur, this._effectMap.get(cur));
                }
                else {
                    errorList.add(eCur);
                }
            }
        }
        if (!errorList.isEmpty()) {
            String out = ChatColor.RED + "[B] " + ChatColor.YELLOW + "Invalid Effects:" + ChatColor.AQUA;
            for (final String cur2 : errorList) {
                out = String.valueOf(out) + " '" + cur2 + "'";
            }
            caller.sendMessage(out);
        }
        return eList;
    }
    
    public void doEffect(final Player caller, final String name, final HashMap<String, PotionEffectType> eMap, final String durationString, final String strengthString, final boolean extend) {
        final ArrayList<Player> targetList = new ArrayList<Player>();
        final ArrayList<String> invalidList = new ArrayList<String>();
        if (name.equalsIgnoreCase("all")) {
            Player[] players;
            for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
                final Player cur = players[i];
                targetList.add(cur);
            }
        }
        else {
            final String[] playerTokens = name.split(",");
            String[] array;
            for (int length2 = (array = playerTokens).length, j = 0; j < length2; ++j) {
                final String curName = array[j];
                final Player target = (Player)UtilPlayer.matchOnline(null, name, false);
                if (target != null) {
                    targetList.add(target);
                }
                else {
                    invalidList.add(curName);
                }
            }
        }
        if (!invalidList.isEmpty()) {
            String out = ChatColor.RED + "[B] " + ChatColor.YELLOW + "Invalid Targets:";
            for (final String cur2 : invalidList) {
                out = String.valueOf(out) + " '" + cur2 + "'";
            }
            caller.sendMessage(out);
        }
        if (targetList.isEmpty()) {
            caller.sendMessage(ChatColor.RED + "[B] " + ChatColor.YELLOW + "No Valid Targets Listed.");
            return;
        }
        if (eMap.isEmpty()) {
            caller.sendMessage(ChatColor.RED + "[B] " + ChatColor.YELLOW + "No Valid Effects Listed.");
            return;
        }
        double duration;
        int strength;
        try {
            duration = Double.parseDouble(durationString);
            strength = Integer.parseInt(strengthString);
            if (duration <= 0.0) {
                caller.sendMessage(ChatColor.RED + "[B] " + ChatColor.YELLOW + "Invalid Effect Duration.");
                return;
            }
            if (strength < 0) {
                caller.sendMessage(ChatColor.RED + "[B] " + ChatColor.YELLOW + "Invalid Effect Strength.");
                return;
            }
        }
        catch (Exception ex) {
            caller.sendMessage(ChatColor.RED + "[B] " + ChatColor.YELLOW + "Invalid Effect Duration/Strength.");
            return;
        }
        caller.sendMessage(ChatColor.RED + "[B] " + ChatColor.YELLOW + "Applying Effect(s) to Target(s).");
        for (final Player curPlayer : targetList) {
            for (final String cur3 : this._effectMap.keySet()) {
                this.addEffect((LivingEntity)curPlayer, cur3, this._effectMap.get(cur3), duration, strength, true, extend);
            }
        }
    }
    
    public boolean addEffect(final LivingEntity target, final String effectName, final PotionEffectType type, final double duration, final int strength, final boolean inform, final boolean extend) {
        int oldDur = 0;
        if (target.hasPotionEffect(type)) {
            for (final PotionEffect cur : target.getActivePotionEffects()) {
                if (cur.getType().equals((Object)type)) {
                    if (cur.getAmplifier() > strength) {
                        return true;
                    }
                    if (!extend) {
                        continue;
                    }
                    oldDur += cur.getDuration();
                }
            }
            target.removePotionEffect(type);
        }
        target.addPotionEffect(new PotionEffect(type, (int)(duration * 20.0) + oldDur, strength), true);
        if (inform && target instanceof Player) {
            final Player tPlayer = (Player)target;
            UtilPlayer.message((Entity)tPlayer, F.main("Buffer", "You received " + F.elem(String.valueOf(effectName) + " " + (strength + 1)) + " for " + F.time(new StringBuilder().append(UtilMath.trim(1, duration * 20.0)).toString()) + " Seconds."));
        }
        return false;
    }
}
