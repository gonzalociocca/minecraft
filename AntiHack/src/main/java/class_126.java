import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

// $FF: renamed from: i
public class class_126 implements Listener {

    private static ArrayList<UUID> Ξ;
    private int Ξ;

    public class_126() {
        this.field_212 = 60;
    }

    public static boolean Ξ(Player player) {
        return player == null ? false : class_126.field_211.contains(player.getUniqueId());
    }

    public static void Ξ(Player player) {
        if (player != null) {
            if (!method_600(player)) {
                class_126.field_211.add(player.getUniqueId());
                player.sendMessage(class_125.method_599("mining_notifications_enable"));
            } else {
                class_126.field_211.remove(player.getUniqueId());
                player.sendMessage(class_125.method_599("mining_notifications_disable"));
            }

        }
    }

    public static void Ξ(Player player, boolean flag) {
        if (player != null) {
            if (flag) {
                if (!method_600(player)) {
                    class_126.field_211.add(player.getUniqueId());
                    player.sendMessage(class_125.method_599("mining_notifications_enable"));
                }
            } else if (method_600(player)) {
                class_126.field_211.remove(player.getUniqueId());
                player.sendMessage(class_125.method_599("mining_notifications_disable"));
            }

        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(BlockBreakEvent blockbreakevent) {
        Player player = blockbreakevent.getPlayer();

        if (!blockbreakevent.isCancelled() && player.getGameMode() == GameMode.SURVIVAL) {
            Material material = blockbreakevent.getBlock().getType();

            if (material == Material.DIAMOND_ORE || material == Material.EMERALD_ORE || material == Material.REDSTONE_ORE) {
                class_39.method_170(player, "mining=allow", this.field_212);
            }

        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerPickupItemEvent playerpickupitemevent) {
        Player player = playerpickupitemevent.getPlayer();
        Item item = playerpickupitemevent.getItem();
        int i = playerpickupitemevent.getItem().getTicksLived();
        int j = item.getEntityId();

        if (!playerpickupitemevent.isCancelled() && !class_39.method_169(player, "mining=allow") && player.getGameMode() == GameMode.SURVIVAL && i <= this.field_212 && class_39.method_169(player, "mining=drop") && class_39.method_169(player, "mining=drop=" + j)) {
            Material material = item.getItemStack().getType();
            int k = item.getItemStack().getAmount();

            if (material == Material.DIAMOND || material == Material.EMERALD || material == Material.REDSTONE) {
                String s;

                if (k > 1) {
                    s = material.toString().toLowerCase().replace("_", "-") + "s";
                } else {
                    s = material.toString().toLowerCase().replace("_", "-");
                }

                class_49.method_252(player, s, k);
            }

        } else {
            class_39.method_171(player, "mining=drop=" + j);
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    private void Ξ(PlayerDropItemEvent playerdropitemevent) {
        if (!playerdropitemevent.isCancelled()) {
            Player player = playerdropitemevent.getPlayer();
            Item item = playerdropitemevent.getItemDrop();
            Material material = item.getItemStack().getType();
            int i = item.getEntityId();

            if (material == Material.DIAMOND || material == Material.EMERALD || material == Material.REDSTONE) {
                class_39.method_170(player, "mining=drop=" + i, 1200);
                class_39.method_170(player, "mining=drop" + i, this.field_212);
            }

        }
    }

    static {
        class_126.field_211 = new ArrayList();
    }
}
