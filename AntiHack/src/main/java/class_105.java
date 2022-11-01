import sun.misc.IOUtils;

// $FF: renamed from: bu
public class class_105 {

    public static byte[] Ξ(Class oclass) {
        if (oclass != null) {
            try {
                return IOUtils.readFully(class_105.class.getResourceAsStream("/" + oclass.getName().replaceAll("\\.", "/") + ".class"), -1, true);
            } catch (Exception exception) {
                ;
            }
        }

        return null;
    }

    public static int Ξ(Class oclass) {
        byte[] abyte = method_518(oclass);

        return abyte == null ? 0 : abyte.length;
    }
}
