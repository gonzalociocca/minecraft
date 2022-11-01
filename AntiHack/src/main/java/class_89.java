import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Location;

// $FF: renamed from: bm
public class class_89 {

    public static boolean Ξ(double d0, double d1) {
        return d0 + d1 > 1.0D && d0 - d1 < 1.0D;
    }

    public static double Ξ(Location location, Location location1) {
        double d0 = location.getX() - location1.getX();
        double d1 = location.getZ() - location1.getZ();

        return Math.sqrt(d0 * d0 + d1 * d1);
    }

    public static double Π(Location location, Location location1) {
        double d0 = location.getY() - location1.getY();

        return Math.sqrt(d0 * d0);
    }

    public static float Ξ(float f) {
        f %= 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    public static double Ξ(double d0) {
        d0 %= 360.0D;
        if (d0 >= 180.0D) {
            d0 -= 360.0D;
        }

        if (d0 < -180.0D) {
            d0 += 360.0D;
        }

        return d0;
    }

    public static int Ξ(int i, int j) {
        Random random = new Random();
        int k = random.nextInt(j - i) + i;

        return k;
    }

    public static double Ξ(double d0, double d1) {
        Random random = new Random();
        double d2 = d0 + (d1 - d0) * random.nextDouble();

        return d2;
    }

    public static float Ξ(float f, float f1) {
        Random random = new Random();
        float f2 = f + (f1 - f) * random.nextFloat();

        return f2;
    }

    public static double Π(double d0, double d1) {
        double d2;

        if (d0 >= d1) {
            d2 = d0 - d1;
        } else {
            d2 = d1 - d0;
        }

        return d2;
    }

    public static float Π(float f, float f1) {
        float f2;

        if (f >= f1) {
            f2 = f - f1;
        } else {
            f2 = f1 - f;
        }

        return f2;
    }

    public static int Π(int i, int j) {
        int k;

        if (i >= j) {
            k = i - j;
        } else {
            k = j - i;
        }

        return k;
    }

    public static double Ξ(ArrayList<Double> arraylist) {
        double d0 = Double.POSITIVE_INFINITY;

        double d1;

        for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); d0 = Math.min(d0, d1)) {
            d1 = ((Double) iterator.next()).doubleValue();
        }

        return d0;
    }

    public static double Π(ArrayList<Double> arraylist) {
        double d0 = 0.0D;

        double d1;

        for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); d0 = Math.max(d0, d1)) {
            d1 = ((Double) iterator.next()).doubleValue();
        }

        return d0;
    }

    public static boolean Ξ(double d0) {
        return Math.getExponent(d0) == 1023 || Math.getExponent(d0) == -1022;
    }
}
