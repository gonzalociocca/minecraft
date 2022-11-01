import java.util.ArrayList;
import java.util.Iterator;
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

// $FF: renamed from: aB
public class class_1 implements Listener {

    public static void Ξ(Player player) {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = Runtime.getRuntime().availableProcessors();
        long i1 = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
        long j1 = Runtime.getRuntime().totalMemory() / 1024L / 1024L;
        long k1 = Runtime.getRuntime().maxMemory() / 1024L / 1024L;
        long l1 = j1 - i1;
        long i2 = k1 - l1;

        Player player1;

        for (Iterator iterator = Bukkit.getOnlinePlayers().iterator(); iterator.hasNext(); i += class_38.method_155(player1)) {
            player1 = (Player) iterator.next();
        }

        Enums.HackType[] aenums_hacktype = Enums.HackType.values();
        int j2 = aenums_hacktype.length;

        for (int k2 = 0; k2 < j2; ++k2) {
            Enums.HackType enums_hacktype = aenums_hacktype[k2];

            if (class_123.method_586(enums_hacktype)) {
                ++j;
            } else {
                ++k;
            }
        }

        Inventory inventory = Bukkit.createInventory(player, 27, "\u00a70Main Menu");
        ArrayList arraylist = new ArrayList();

        arraylist.add("");
        arraylist.add("\u00a77Server Statistics\u00a78:");
        arraylist.add("\u00a77TPS\u00a78: \u00a75" + class_97.method_502());
        arraylist.add("\u00a77Version\u00a78: \u00a73" + class_101.method_512().toString().substring(1).replaceAll("_", "."));
        arraylist.add("\u00a77Available Processors\u00a78: \u00a7d" + l);
        arraylist.add("\u00a77Given Memory\u00a78: \u00a74" + k1 + "MB");
        arraylist.add("\u00a77Allocated Memory\u00a78: \u00a7c" + j1 + "MB");
        arraylist.add("\u00a77Free Allocated Memory\u00a78: \u00a7e" + i1 + "MB");
        arraylist.add("\u00a77Total Free Memory\u00a78: \u00a7a" + i2 + "MB");
        class_83.method_461(inventory, "\u00a76Reload Config", arraylist, new ItemStack(Material.REDSTONE_COMPARATOR), 10);
        ArrayList arraylist1 = new ArrayList();

        arraylist1.add("");
        if (class_51.method_271(player)) {
            arraylist1.add("\u00a77Left click to \u00a7cdisable \u00a77verbose.");
        } else {
            arraylist1.add("\u00a77Left Click to \u00a7aenable \u00a77verbose.");
        }

        if (class_126.method_600(player)) {
            arraylist1.add("\u00a77Right click to \u00a7cdisable \u00a77mining notifications.");
        } else {
            arraylist1.add("\u00a77Right Click to \u00a7aenable \u00a77mining notifications.");
        }

        class_83.method_461(inventory, "\u00a73Notifications", arraylist1, new ItemStack(Material.PAPER), 12);
        ArrayList arraylist2 = new ArrayList();

        arraylist2.add("");
        arraylist2.add("\u00a77Global Violations\u00a78:\u00a7c " + i);
        class_83.method_461(inventory, "\u00a74Reset Violations", arraylist2, new ItemStack(Material.BEDROCK), 14);
        ArrayList arraylist3 = new ArrayList();

        arraylist3.add("");
        arraylist3.add("\u00a77Enabled Checks\u00a78:\u00a7a " + j);
        arraylist3.add("\u00a77Disabled Checks\u00a78:\u00a7c " + k);
        class_83.method_461(inventory, "\u00a72Manage Checks", arraylist3, new ItemStack(Material.IRON_SWORD), 16);
        player.openInventory(inventory);
    }

    @EventHandler
    private void Ξ(InventoryClickEvent inventoryclickevent) {
        if (inventoryclickevent.getWhoClicked() instanceof Player) {
            Player player = (Player) inventoryclickevent.getWhoClicked();

            if (inventoryclickevent.getCurrentItem() == null || inventoryclickevent.getCurrentItem().getItemMeta() == null || inventoryclickevent.getCurrentItem().getItemMeta().getDisplayName() == null) {
                return;
            }

            if (inventoryclickevent.getInventory().getTitle().equalsIgnoreCase("\u00a70Main Menu")) {
                inventoryclickevent.setCancelled(true);
                String s = inventoryclickevent.getCurrentItem().getItemMeta().getDisplayName();

                if (s.equalsIgnoreCase("\u00a76Reload Config")) {
                    if (!class_47.method_229(player, Enums.Permission.reload)) {
                        player.sendMessage(class_125.method_599("menu_gui_no_access"));
                        player.closeInventory();
                        return;
                    }

                    class_123.method_580(player, true);
                    player.closeInventory();
                } else if (s.equalsIgnoreCase("\u00a73Notifications")) {
                    if (inventoryclickevent.getClick().isLeftClick()) {
                        if (!class_47.method_229(player, Enums.Permission.verbose)) {
                            player.sendMessage(class_125.method_599("menu_gui_no_access"));
                            player.closeInventory();
                            return;
                        }

                        class_51.method_272(player, 0);
                        player.closeInventory();
                    } else if (inventoryclickevent.getClick().isRightClick()) {
                        if (!class_47.method_229(player, Enums.Permission.mining)) {
                            player.sendMessage(class_125.method_599("menu_gui_no_access"));
                            player.closeInventory();
                            return;
                        }

                        class_126.method_601(player);
                        player.closeInventory();
                    }
                } else if (s.equalsIgnoreCase("\u00a74Reset Violations")) {
                    if (!class_47.method_229(player, Enums.Permission.manage)) {
                        player.sendMessage(class_125.method_599("menu_gui_no_access"));
                        player.closeInventory();
                        return;
                    }

                    class_38.method_164();
                    if (!class_51.method_271(player) && class_128.method_610("violations_reset_message")) {
                        player.sendMessage(class_125.method_599("violations_reset"));
                    }

                    player.closeInventory();
                } else if (s.equalsIgnoreCase("\u00a72Manage Checks")) {
                    if (!class_47.method_229(player, Enums.Permission.manage)) {
                        player.sendMessage(class_125.method_599("menu_gui_no_access"));
                        player.closeInventory();
                        return;
                    }

                    class_2.method_6(player);
                }
            }
        }

    }
}
