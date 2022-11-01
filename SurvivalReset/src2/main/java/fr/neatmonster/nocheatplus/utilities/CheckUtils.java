package fr.neatmonster.nocheatplus.utilities;

import fr.neatmonster.nocheatplus.NCPAPIProvider;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.checks.access.ICheckConfig;
import fr.neatmonster.nocheatplus.checks.access.ICheckData;
import fr.neatmonster.nocheatplus.checks.blockbreak.BlockBreakData;
import fr.neatmonster.nocheatplus.checks.combined.CombinedData;
import fr.neatmonster.nocheatplus.checks.fight.FightData;
import fr.neatmonster.nocheatplus.checks.inventory.InventoryData;
import fr.neatmonster.nocheatplus.checks.moving.MovingConfig;
import fr.neatmonster.nocheatplus.components.NoCheatPlusAPI;
import fr.neatmonster.nocheatplus.hooks.APIUtils;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import fr.neatmonster.nocheatplus.logging.LogManager;
import fr.neatmonster.nocheatplus.logging.StaticLog;
import fr.neatmonster.nocheatplus.logging.Streams;
import fr.neatmonster.nocheatplus.utilities.ds.count.ActionFrequency;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CheckUtils
{
    private static final Set<Integer> logOnce = Collections.synchronizedSet(new HashSet());

    public static void kickIllegalMove(Player player, MovingConfig cc)
    {
        player.kickPlayer(cc.msgKickIllegalMove);
        StaticLog.logWarning("[NCP] Disconnect " + player.getName() + " due to illegal move!");
    }

    public static final long guessKeepAliveTime(Player player, long now, long maxAge)
    {
        int tick = TickTask.getTick();
        long ref = Long.MIN_VALUE;

        FightData fData = FightData.getData(player);
        ref = Math.max(ref, fData.speedBuckets.lastUpdate());
        ref = Math.max(ref, now - 50L * (tick - fData.lastAttackTick));

        ref = Math.max(ref, fData.regainHealthTime);

        ref = Math.max(ref, CombinedData.getData(player).lastMoveTime);

        InventoryData iData = InventoryData.getData(player);
        ref = Math.max(ref, iData.lastClickTime);
        ref = Math.max(ref, iData.instantEatInteract);

        BlockBreakData bbData = BlockBreakData.getData(player);
        ref = Math.max(ref, bbData.frequencyBuckets.lastUpdate());
        ref = Math.max(ref, bbData.fastBreakfirstDamage);
        if ((ref > now) || (ref < now - maxAge)) {
            return Long.MIN_VALUE;
        }
        return ref;
    }

    public static boolean isBadCoordinate(float... floats)
    {
        for (int i = 0; i < floats.length; i++) {
            if ((Float.isNaN(floats[i])) || (Float.isInfinite(floats[i]))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBadCoordinate(double... doubles)
    {
        for (int i = 0; i < doubles.length; i++)
        {
            double x = doubles[i];
            if ((Double.isNaN(x)) || (Double.isInfinite(x)) || (Math.abs(x) > 3.2E7D)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEnabled(CheckType checkType, Player player, ICheckData data, ICheckConfig cc)
    {
        if (cc == null)
        {
            if (!checkType.isEnabled(player)) {
                return false;
            }
        }
        else if (!cc.isEnabled(checkType)) {
            return false;
        }
        return !hasBypass(checkType, player, data);
    }

    public static boolean hasBypass(CheckType checkType, Player player, ICheckData data)
    {
        String permission = checkType.getPermission();
        //if (Bukkit.isPrimaryThread()) {
            if ((permission != null) && (player.hasPermission(permission))) {
                return true;
            }
        //}
       /* else if (permission != null)
        {
            if (data == null)
            {
                if (checkType.hasCachedPermission(player, permission)) {
                    return true;
                }
            }
            else if (data.hasCachedPermission(permission)) {
                return true;
            }
            if (!APIUtils.needsSynchronization(checkType)) {
                improperAPIAccess(checkType);
            }
        }*/
        return NCPExemptionManager.isExempted(player, checkType);
    }

    private static void improperAPIAccess(CheckType checkType)
    {
        String trace = Arrays.toString(Thread.currentThread().getStackTrace());
        int ref = trace.hashCode() ^ new Integer(trace.length()).hashCode();

        boolean details = logOnce.add(Integer.valueOf(ref));
        String extra;
        if (details) {
            extra = " (id=" + ref + ")";
        } else {
            extra = " (see earlier log with id=" + ref + ")";
        }
        NCPAPIProvider.getNoCheatPlusAPI().getLogManager().severe(Streams.STATUS, "Off primary thread call to hasByPass for " + checkType + extra + ".");
        if (details)
        {
            NCPAPIProvider.getNoCheatPlusAPI().getLogManager().severe(Streams.STATUS, trace);
            if (logOnce.size() > 10000)
            {
                logOnce.clear();
                NCPAPIProvider.getNoCheatPlusAPI().getLogManager().severe(Streams.STATUS, "Cleared log-once ids, due to exceeding the maximum number of stored ids.");
            }
        }
    }

    public static void debug(Player player, CheckType checkType, String message)
    {
        NCPAPIProvider.getNoCheatPlusAPI().getLogManager().debug(Streams.TRACE_FILE, getLogMessagePrefix(player, checkType) + message);
    }

    public static String getLogMessagePrefix(Player player, CheckType checkType)
    {
        String base = "[" + checkType + "] ";
        if (player != null) {
            base = base + "[" + player.getName() + "] ";
        }
        return base;
    }

    public static Random getRandom()
    {
        return (Random)NCPAPIProvider.getNoCheatPlusAPI().getGenericInstance(Random.class);
    }
}
