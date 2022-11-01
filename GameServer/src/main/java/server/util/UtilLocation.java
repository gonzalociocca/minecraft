package server.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * Created by noname on 17/4/2017.
 */
public class UtilLocation {
    public static Location averageLocation(Collection<Location> locs)
    {
        if (locs.isEmpty()) {
            return null;
        }
        Vector vec = new Vector(0, 0, 0);
        double count = 0.0D;

        World world = null;

        for (Location spawn : locs)
        {
            count += 1.0D;
            vec.add(spawn.toVector());

            world = spawn.getWorld();
        }

        vec.multiply(1.0D / count);

        return vec.toLocation(world);
    }

    public static float getYaw(Location l1, Location l2) {
        Vector vector = l1.toVector().subtract(l2.toVector());
        return getLookAtYaw(vector);
    }

    public static float getLookAtYaw(Vector motion) {
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0;

        if (dx != 0) {

            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        return (float) (-yaw * 180 / Math.PI - 180);
    }
}

