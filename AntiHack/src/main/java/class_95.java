// $FF: renamed from: bp
public class class_95 {

    public static String Ξ(String s, int i) {
        if (s != null && i > 0) {
            int j = s.length();

            if (i > j) {
                i = j;
            }

            return s.substring(0, i);
        } else {
            return s;
        }
    }
}
