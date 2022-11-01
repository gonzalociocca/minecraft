// $FF: renamed from: be
public class class_73 {

    public static double Ξ(double d0, int i) {
        boolean flag = false;

        if (d0 < 0.0D) {
            d0 = Math.abs(d0);
            flag = true;
        }

        i = (int) Math.pow(10.0D, (double) i);
        d0 *= (double) i;
        d0 = Math.floor(d0) / (double) i;
        return flag ? -d0 : d0;
    }
}
