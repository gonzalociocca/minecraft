import org.bukkit.Bukkit;

// $FF: renamed from: bs
public class class_101 {

    public static class_101 Ξ;

    public static class_101 Ξ() {
        if (class_101.field_178 != null) {
            return class_101.field_178;
        } else {
            String s = Bukkit.getVersion();

            s = s.substring(0, s.length() - 1);

            try {
                for (int i = 0; i <= s.length(); ++i) {
                    if (s.substring(i).startsWith("(MC: ")) {
                        s = s.substring(i + 5);

                        for (int j = 0; j <= s.length(); ++j) {
                            class_101[] aclass_101 = class_101.values();
                            int k = aclass_101.length;

                            for (int l = 0; l < k; ++l) {
                                class_101 class_101 = aclass_101[l];

                                if (class_101.toString().equalsIgnoreCase("V" + s.substring(0, s.length() - j).replace(".", "_"))) {
                                    class_101.field_178 = class_101;
                                    return class_101.field_178;
                                }
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                ;
            }

            class_101.field_178 = class_101.field_254;
            return class_101.field_178;
        }
    }

    static {
        class_101.field_178 = null;
    }

    public static enum class_144 {

        // $FF: renamed from: Ξ bs$a
        field_250, // $FF: renamed from: Π bs$a
field_251, HHΞ, // $FF: renamed from: HΞ bs$a
field_252, // $FF: renamed from: BΞ bs$a
field_253, BPPΠ, // $FF: renamed from: PΞ bs$a
field_254;
    }
}
