import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

// $FF: renamed from: br
public class class_99 {

    public static String Ξ(Timestamp timestamp) {
        if (timestamp != null) {
            String s = timestamp.toString().substring(11, 13);
            String s1 = timestamp.toString().substring(14, 16);
            String s2 = timestamp.toString().substring(17, 19);

            return s + ":" + s1 + ":" + s2;
        } else {
            return null;
        }
    }

    public static String Π(Timestamp timestamp) {
        if (timestamp != null) {
            String s = timestamp.toString().substring(0, 4);
            String s1 = timestamp.toString().substring(5, 7);
            String s2 = timestamp.toString().substring(8, 10);

            return s2 + "/" + s1 + "/" + s;
        } else {
            return null;
        }
    }

    public static String HHΞ(Timestamp timestamp) {
        if (timestamp != null) {
            String s = timestamp.toString().substring(0, 4);
            String s1 = timestamp.toString().substring(5, 7);
            String s2 = timestamp.toString().substring(8, 10);

            return s1 + "/" + s2 + "/" + s;
        } else {
            return null;
        }
    }

    public static String HΞ(Timestamp timestamp) {
        if (timestamp != null) {
            String s = timestamp.toString().substring(0, 4);
            String s1 = timestamp.toString().substring(5, 7);
            String s2 = timestamp.toString().substring(8, 10);

            return s + "/" + s1 + "/" + s2;
        } else {
            return null;
        }
    }

    public static Timestamp Ξ(int i, int j, int k) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(5, i);
        calendar.add(10, j);
        calendar.add(12, k);
        date = calendar.getTime();
        Timestamp timestamp = new Timestamp(date.getTime());

        return timestamp;
    }
}
