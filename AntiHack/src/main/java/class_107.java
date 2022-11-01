import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;

// $FF: renamed from: bv
public class class_107 {

    public static String Ξ(String s) {
        URL url = new URL(s);
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
        String s1;

        if ((s1 = bufferedreader.readLine()) != null) {
            bufferedreader.close();
        }

        return s1;
    }

    public static String Π(String s) {
        InputStream inputstream = (new URL(s)).openStream();
        String s1 = IOUtils.toString(inputstream);

        IOUtils.closeQuietly(inputstream);
        return s1;
    }

    public static String[] Ξ(String s) {
        URL url = new URL(s);
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
        ArrayList arraylist = new ArrayList();

        String s1;

        while ((s1 = bufferedreader.readLine()) != null) {
            arraylist.add(s1);
        }

        return (String[]) arraylist.toArray(new String[0]);
    }

    public static String[] Π(String s) {
        InputStream inputstream = (new URL(s)).openStream();
        ArrayList arraylist = new ArrayList();

        try {
            arraylist.add(IOUtils.toString(inputstream));
        } finally {
            IOUtils.closeQuietly(inputstream);
        }

        return (String[]) arraylist.toArray(new String[0]);
    }
}
