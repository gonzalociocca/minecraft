import java.util.ArrayList;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.class_38;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

// $FF: renamed from: aC
public class class_2 implements Listener {

    public static void Ξ(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, "\u00a70Manage Checks");
        Enums.HackType[] aenums_hacktype = Enums.HackType.values();
        int i = aenums_hacktype.length;

        for (int j = 0; j < i; ++j) {
            Enums.HackType enums_hacktype = aenums_hacktype[j];

            method_7(inventory, enums_hacktype);
        }

        class_83.method_461(inventory, "\u00a7cDisable silent checking for all", (ArrayList) null, new ItemStack(Material.getMaterial(374)), 46);
        class_83.method_461(inventory, "\u00a7cDisable all checks", (ArrayList) null, new ItemStack(Material.STAINED_CLAY, 1, (short) 14), 47);
        class_83.method_461(inventory, "\u00a74Back", (ArrayList) null, new ItemStack(Material.ARROW), 49);
        class_83.method_461(inventory, "\u00a7aEnable all checks", (ArrayList) null, new ItemStack(Material.STAINED_CLAY, 1, (short) 5), 51);
        class_83.method_461(inventory, "\u00a7aEnable silent checking for all", (ArrayList) null, new ItemStack(Material.getMaterial(373)), 52);
        player.openInventory(inventory);
    }

    private static void Ξ(Inventory inventory, Enums.HackType enums_hacktype) {
        boolean flag = class_123.method_587(enums_hacktype);
        boolean flag1 = class_123.method_586(enums_hacktype);
        String s;
        String s1;

        if (flag) {
            s = "\u00a77Silent\u00a78: \u00a7aTrue";
            s1 = "\u00a77Right click to \u00a7cdisable \u00a77silent checking.";
        } else {
            s = "\u00a77Silent\u00a78: \u00a7cFalse";
            s1 = "\u00a77Right click to \u00a7aenable \u00a77silent checking.";
        }

        String s2;
        String s3;
        String s4;
        ItemStack itemstack;

        if (flag1) {
            itemstack = new ItemStack(Material.getMaterial(351), 1, (short) 10);
            s4 = "\u00a72";
            s2 = "\u00a77Enabled\u00a78: \u00a7aTrue";
            s3 = "\u00a77Left click to \u00a7cdisable \u00a77check.";
        } else {
            itemstack = new ItemStack(Material.getMaterial(351), 1, (short) 8);
            s4 = "\u00a74";
            s2 = "\u00a77Enabled\u00a78: \u00a7cFalse";
            s3 = "\u00a77Left click to \u00a7aenable \u00a77check.";
        }

        ArrayList arraylist = new ArrayList();

        arraylist.add("");
        arraylist.add(s2);
        if (class_123.HHΞ(enums_hacktype)) {
            arraylist.add(s);
        }

        arraylist.add("\u00a77Violation Divisor\u00a78: \u00a74" + class_123.method_585(enums_hacktype));

        for (int i = 1; i <= class_38.method_150(); ++i) {
            String[] astring = class_123.method_578(enums_hacktype, i);
            int j = astring.length;

            for (int k = 0; k < j; ++k) {
                String s5 = astring[k];

                if (s5 != null) {
                    arraylist.add("\u00a77" + i + "\u00a78:\u00a7f " + s5);
                }
            }
        }

        arraylist.add("");
        arraylist.add(s3);
        if (class_123.HHΞ(enums_hacktype)) {
            arraylist.add(s1);
        }

        class_83.method_461(inventory, s4 + enums_hacktype.toString(), arraylist, itemstack, -1);
    }

    @EventHandler
    private void Ξ(InventoryClickEvent inventoryclickevent) {
        if (inventoryclickevent.getWhoClicked() instanceof Player) {
            Player player = (Player) inventoryclickevent.getWhoClicked();

            if (inventoryclickevent.getCurrentItem() == null || inventoryclickevent.getCurrentItem().getItemMeta() == null || inventoryclickevent.getCurrentItem().getItemMeta().getDisplayName() == null) {
                return;
            }

            if (inventoryclickevent.getInventory().getTitle().equalsIgnoreCase("\u00a70Manage Checks")) {
                inventoryclickevent.setCancelled(true);
                String s = inventoryclickevent.getCurrentItem().getItemMeta().getDisplayName();

                if (!class_47.method_229(player, Enums.Permission.manage)) {
                    player.sendMessage(class_125.method_599("menu_gui_no_access"));
                    player.closeInventory();
                    return;
                }

                if (s.equalsIgnoreCase("\u00a74Back")) {
                    class_1.method_4(player);
                } else if (s.equalsIgnoreCase("\u00a7cDisable all checks")) {
                    class_123.HHΞ();
                    method_6(player);
                } else if (s.equalsIgnoreCase("\u00a7aEnable all checks")) {
                    class_123.method_592();
                    method_6(player);
                } else if (s.equalsIgnoreCase("\u00a7cDisable silent checking for all")) {
                    class_123.method_594();
                    method_6(player);
                } else if (s.equalsIgnoreCase("\u00a7aEnable silent checking for all")) {
                    class_123.method_593();
                    method_6(player);
                } else if (inventoryclickevent.isLeftClick()) {
                    method_9(player, inventoryclickevent.getCurrentItem());
                } else if (inventoryclickevent.isRightClick()) {
                    method_10(player, inventoryclickevent.getCurrentItem());
                }
            }
        }

    }

    private static void Ξ(Player player, ItemStack itemstack) {
        if (itemstack != null && itemstack.getItemMeta() != null && itemstack.getItemMeta().getDisplayName() != null) {
            Enums.HackType enums_hacktype = Enums.HackType.valueOf(itemstack.getItemMeta().getDisplayName().substring(2));

            if (class_123.method_586(enums_hacktype)) {
                class_123.HHΞ(enums_hacktype);
            } else {
                class_123.method_589(enums_hacktype);
            }

            method_6(player);
        }
    }

    private static void Π(Player player, ItemStack itemstack) {
        if (itemstack != null && itemstack.getItemMeta() != null && itemstack.getItemMeta().getDisplayName() != null) {
            Enums.HackType enums_hacktype = Enums.HackType.valueOf(itemstack.getItemMeta().getDisplayName().substring(2));

            if (class_123.method_587(enums_hacktype)) {
                class_123.method_591(enums_hacktype);
            } else {
                class_123.method_590(enums_hacktype);
            }

            method_6(player);
        }
    }
}
