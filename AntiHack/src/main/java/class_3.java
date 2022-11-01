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

// $FF: renamed from: aD
public class class_3 implements Listener {

    public static void Ξ(Player player, Player player1) {
        int i = class_38.method_155(player1);

        if (i > 64) {
            i = 64;
        } else if (i == 0) {
            i = 1;
        }

        Inventory inventory = Bukkit.createInventory(player, 54, "\u00a70Info:\u00a72 " + player1.getName());
        Enums.HackType[] aenums_hacktype = Enums.HackType.values();
        int j = aenums_hacktype.length;

        for (int k = 0; k < j; ++k) {
            Enums.HackType enums_hacktype = aenums_hacktype[k];

            method_12(inventory, player1, enums_hacktype);
        }

        ArrayList arraylist = new ArrayList();

        arraylist.add("\u00a77CPS\u00a78:\u00a7a " + class_124.method_597(player1));
        arraylist.add("\u00a77Latency\u00a78:\u00a7a " + class_87.method_472(player1));
        arraylist.add("\u00a77Violations\u00a78:\u00a7a " + class_38.method_155(player1));
        class_83.method_461(inventory, "\u00a72Information", arraylist, new ItemStack(Material.BOOK, i), 48);
        class_83.method_461(inventory, "\u00a74Close", (ArrayList) null, new ItemStack(Material.ARROW), 49);
        class_83.method_461(inventory, "\u00a7cReset", (ArrayList) null, new ItemStack(Material.REDSTONE), 50);
        player.openInventory(inventory);
    }

    private static void Ξ(Inventory inventory, Player player, Enums.HackType enums_hacktype) {
        int i = class_38.method_154(player, enums_hacktype);

        if (i > 64) {
            i = 64;
        }

        ItemStack itemstack = new ItemStack(Material.QUARTZ_BLOCK);

        if (i >= 1) {
            itemstack = new ItemStack(Material.STAINED_CLAY, i, (short) 14);
        }

        ArrayList arraylist = new ArrayList();

        arraylist.add("\u00a77Violations\u00a78:\u00a7a " + class_38.method_154(player, enums_hacktype));
        class_83.method_461(inventory, "\u00a72" + enums_hacktype.toString(), arraylist, itemstack, -1);
    }

    @EventHandler
    private void Ξ(InventoryClickEvent inventoryclickevent) {
        if (inventoryclickevent.getWhoClicked() instanceof Player) {
            Player player = (Player) inventoryclickevent.getWhoClicked();

            if (inventoryclickevent.getCurrentItem() == null || inventoryclickevent.getCurrentItem().getItemMeta() == null || inventoryclickevent.getCurrentItem().getItemMeta().getDisplayName() == null) {
                return;
            }

            if (inventoryclickevent.getInventory().getTitle().contains("\u00a70Info:\u00a72 ")) {
                inventoryclickevent.setCancelled(true);
                String s = inventoryclickevent.getCurrentItem().getItemMeta().getDisplayName();

                if (s.equalsIgnoreCase("\u00a74Close")) {
                    player.closeInventory();
                } else if (s.equalsIgnoreCase("\u00a7cReset")) {
                    Player player1 = Bukkit.getPlayer(inventoryclickevent.getInventory().getTitle().substring(10));

                    if (!class_47.method_229(player, Enums.Permission.manage)) {
                        player.sendMessage(class_125.method_599("menu_gui_no_access"));
                        player.closeInventory();
                        return;
                    }

                    if (player1 == null || !player1.isOnline()) {
                        player.sendMessage(class_125.method_599("player_not_found_message"));
                        player.closeInventory();
                        return;
                    }

                    player.closeInventory();
                    String s1 = class_125.method_599("player_violation_reset_message");

                    s1 = s1.replace("{player}", player1.getName());
                    class_38.method_163(player);
                    player.sendMessage(s1);
                }
            }
        }

    }
}
