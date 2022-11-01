import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.vagdedes.spartan.Register;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

// $FF: renamed from: aE
public class class_4 {

    private static Connection Ξ;
    private static boolean Ξ;

    public static void Ξ() {
        class_4.field_2 = true;

        try {
            class_4.field_1.close();
        } catch (Exception exception) {
            ;
        }

    }

    public static void Π() {
        String s = Register.field_249.getDescription().getVersion();
        String s1 = class_5.method_20();
        String s2 = class_5.method_21();
        String s3 = class_5.HHΞ();
        String s4 = class_5.method_22();

        if (s1.equalsIgnoreCase("")) {
            class_4.field_2 = false;
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Spartan " + s + "] Error: Host is blank");
        } else if (s2.equalsIgnoreCase("")) {
            class_4.field_2 = false;
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Spartan " + s + "] Config Error: User is blank");
        } else if (s3.equalsIgnoreCase("")) {
            class_4.field_2 = false;
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Spartan " + s + "] Config Error: Password is blank");
        } else if (s4.equalsIgnoreCase("")) {
            class_4.field_2 = false;
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Spartan " + s + "] Config Error: Database is blank");
        } else if (class_4.field_1 == null) {
            method_16(s1, s2, s3, s4);
        }

    }

    private static void Ξ(String s, String s1, String s2, String s3) {
        if (s != null && s1 != null && s2 != null && s3 != null) {
            String s4 = Register.field_249.getDescription().getVersion();

            try {
                class_4.field_1 = DriverManager.getConnection("jdbc:mysql://" + s + ":" + 3306 + "/" + s3, s1, s2);
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Spartan " + s4 + "] MySQL connected.");
            } catch (SQLException sqlexception) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Spartan " + s4 + "] MySQL Connect Error: " + sqlexception.getMessage());
            }

        }
    }

    private static void Π(String s) {
        if (s != null) {
            method_15();

            try {
                Statement statement = class_4.field_1.createStatement();

                statement.executeUpdate(s);
                statement.close();
            } catch (Exception exception) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MySQL Update:");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Command: " + s);
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + exception.getMessage());
            }

        }
    }

    public static boolean Ξ(String s) {
        try {
            Connection connection = class_4.field_1;

            if (connection == null) {
                return false;
            } else {
                DatabaseMetaData databasemetadata = connection.getMetaData();

                if (databasemetadata == null) {
                    return false;
                } else {
                    ResultSet resultset = databasemetadata.getTables((String) null, (String) null, s, (String[]) null);

                    return resultset.next();
                }
            }
        } catch (Exception exception) {
            return false;
        }
    }

    public static void Ξ(String s) {
        if (s != null) {
            if (class_4.field_2 && class_128.method_610("log_mysql")) {
                String s1 = class_5.method_23();

                if (!method_18(s1)) {
                    method_17("CREATE TABLE " + s1 + " (date TIMESTAMP, info VARCHAR(384))");
                } else {
                    method_17("INSERT INTO " + s1 + " (date, info) VALUES (\'" + class_99.method_510(0, 0, 0) + "\', \'" + s + "\')");
                }
            }

        }
    }

    static {
        class_4.field_2 = true;
    }
}
