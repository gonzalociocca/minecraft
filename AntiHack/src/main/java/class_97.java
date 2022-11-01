// $FF: renamed from: bq
public class class_97 {

    private static int Ξ;
    private static int Π;
    private static double Ξ;
    private static double Π;
    private static int HHΞ;
    private static long[] Ξ;

    public static boolean Ξ() {
        return class_97.field_171 > 0;
    }

    public static double Ξ() {
        double d0 = method_503(100);

        return d0 > 20.0D ? 20.0D : (d0 < 0.0D ? 0.0D : d0);
    }

    private static double Ξ(int i) {
        if (class_97.HHΞ < i) {
            return 20.0D;
        } else {
            try {
                int j = (class_97.HHΞ - 1 - i) % class_97.field_175.length;
                long k = System.currentTimeMillis() - class_97.field_175[j];

                return (double) i / ((double) k / 1000.0D);
            } catch (Exception exception) {
                return 0.0D;
            }
        }
    }

    public static void Ξ() {
        method_505();
        class_97.field_175[class_97.HHΞ % class_97.field_175.length] = System.currentTimeMillis();
        ++class_97.HHΞ;
    }

    private static void Π() {
        if (class_97.field_171 > 0) {
            --class_97.field_171;
        }

        ++class_97.field_172;
        class_97.field_173 += method_502();
        if (class_97.field_172 >= 5) {
            double d0 = class_97.field_173 / Double.valueOf((double) class_97.field_172).doubleValue();

            if (class_97.field_174 > 0.0D) {
                double d1 = class_97.field_174 - d0;

                if (d1 >= 1.0D) {
                    class_97.field_171 = 60;
                }
            }

            class_97.field_174 = d0;
            class_97.field_173 = 0.0D;
            class_97.field_172 = 0;
        }

    }

    static {
        class_97.field_171 = 0;
        class_97.field_172 = 0;
        class_97.field_173 = 0.0D;
        class_97.field_174 = 0.0D;
        class_97.HHΞ = 0;
        class_97.field_175 = new long[600];
    }
}
