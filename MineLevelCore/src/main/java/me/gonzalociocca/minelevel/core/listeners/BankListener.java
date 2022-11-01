package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noname on 9/2/2017.
 */
public class BankListener implements Listener {
    Main plugin;
    public BankListener(Main main){
        plugin=main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onBanco(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getName().equals(null)) {
            return;
        }
        if (event.getCurrentItem() == null) {
            return;
        }
        if (!event.getCurrentItem().hasItemMeta()) {
            return;
        }

        if (event.getClickedInventory().getName().equals(Code.Color("&cOpciones del Banco."))) {
            if (!Variable.BankLimit.containsKey(event.getWhoClicked().getName())) {
                Variable.BankLimit.put(event.getWhoClicked().getName(), 0L);
            }
            if (Variable.BankLimit.get(event.getWhoClicked().getName()) > System.currentTimeMillis()) {
                event.getWhoClicked().sendMessage(Code.Color("&cEspera 1 segundo antes de clickear"));
                event.setCancelled(true);
                return;
            } else {
                Variable.BankLimit.put(event.getWhoClicked().getName(), System.currentTimeMillis() + 750L);
            }
            event.setCancelled(true);
            if (event.getCurrentItem().getItemMeta().hasLore()) {
                if (event.getCurrentItem().getItemMeta().getLore().contains(Code.Color("&aPrecio: &b&l" + 25 + "&b Diamantes"))) {
                    int a = 25;
                    if (plugin.getDB().getPlayerData(event.getWhoClicked().getName()).getDiamonds() < a) {
                        event.getWhoClicked().sendMessage(Code.Color("&cDiamantes en el banco insuficientes."));
                        return;
                    }
                    int d = 1;
                    int en = 0;
                    for (ItemStack it : event.getWhoClicked().getInventory().getContents()) {
                        if (it == null) {
                            en++;
                        }
                    }
                    if (a > 64) {
                        d = d + a / 64;
                    }
                    if (en < d) {
                        event.getWhoClicked().sendMessage(Code.Color("&cEspacio insuficiente para guardar las runas"));
                        return;
                    }

                    plugin.getDB().getPlayerData(event.getWhoClicked().getName()).removeDiamonds(a);
                    event.getWhoClicked().sendMessage(Code.Color("&c" + a + " extraidos del banco."));
                } else if (event.getSlot() == 3) {
                    for (int as = 3; as < 6; as++) {
                        ItemStack it = new ItemStack(Material.SULPHUR);
                        ItemMeta im = it.getItemMeta();
                        im.setDisplayName(Code.Color("&aExtraer " + (32 * as) + " Diamantes"));
                        it.setItemMeta(im);
                        event.getClickedInventory().setItem(as, it);
                    }
                } else if (event.getSlot() == 4) {
                    for (int as = 3; as < 6; as++) {
                        ItemStack it = new ItemStack(Material.DIAMOND);
                        ItemMeta im = it.getItemMeta();
                        im.setDisplayName(Code.Color("&aDepositar " + (32 * as) + " Diamantes"));
                        it.setItemMeta(im);
                        event.getClickedInventory().setItem(as, it);
                    }
                }
            } else {
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Depositar")) {
                    int a = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]);
                    int d = 0;
                    if (event.getWhoClicked().getInventory().contains(Material.DIAMOND)) {
                        HashMap<Integer, ? extends ItemStack> its = event.getWhoClicked().getInventory().all(Material.DIAMOND);
                        for (Integer in : its.keySet()) {
                            if (a <= 0) {
                                break;
                            }
                            ItemStack it = event.getWhoClicked().getInventory().getItem(in);
                            if (it.getAmount() > a) {
                            } else {
                                a -= it.getAmount();
                                d += it.getAmount();
                                event.getWhoClicked().getInventory().setItem(in, new ItemStack(Material.AIR));
                            }
                        }

                        event.getWhoClicked().sendMessage(Code.Color("&c" + d + " diamantes guardados en el banco."));
                        plugin.getDB().getPlayerData(event.getWhoClicked().getName()).addDiamonds(d);

                    }
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Extraer")) {
                    int a = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]);
                    if (plugin.getDB().getPlayerData(event.getWhoClicked().getName()).getDiamonds() < a) {
                        event.getWhoClicked().sendMessage(Code.Color("&cDiamantes en el banco insuficientes."));
                        return;
                    }
                    int d = 1;
                    int en = 0;
                    for (ItemStack it : event.getWhoClicked().getInventory().getContents()) {
                        if (it == null) {
                            en++;
                        }
                    }
                    if (a > 64) {
                        d = d + a / 64;
                    }
                    if (en < d) {
                        event.getWhoClicked().sendMessage(Code.Color("&cEspacio insuficiente para guardar los diamantes"));
                        return;
                    }
                    plugin.getDB().getPlayerData(event.getWhoClicked().getName()).removeDiamonds(a);
                    event.getWhoClicked().getInventory().addItem(new ItemStack(Material.DIAMOND, a));
                    event.getWhoClicked().sendMessage(Code.Color("&c" + a + " extraidos del banco."));
                }
            }
        }
    }

    public static void openBanco(Player p) {
        Inventory iv = Bukkit.createInventory(null, 9, Code.Color("&cOpciones del Banco."));
        ItemStack extraer = new ItemStack(Material.SULPHUR);
        ItemMeta im = extraer.getItemMeta();
        im.setDisplayName(Code.Color("&cExtraer Diamantes"));
        im.setLore(Arrays.asList("", Code.Color("&aClickealo para abrir el menu de extracciones")));
        extraer.setItemMeta(im);

        ItemStack depositar = new ItemStack(Material.DIAMOND);
        ItemMeta im2 = depositar.getItemMeta();
        im2.setDisplayName(Code.Color("&cDepositar Diamantes"));
        im2.setLore(Arrays.asList("", Code.Color("&aClickealo para abrir el menu de depositos")));
        depositar.setItemMeta(im2);

        ItemStack canjear = new ItemStack(Material.NETHER_STAR);
        ItemMeta im3 = canjear.getItemMeta();
        im3.setDisplayName(Code.Color("&cCanjear Diamantes"));
        im3.setLore(Arrays.asList("", Code.Color("&aClickealo para abrir el menu de canjes")));
        canjear.setItemMeta(im3);

        iv.setItem(3, extraer);
        iv.setItem(4, depositar);
        iv.setItem(5, canjear);

        p.openInventory(iv);

    }
}
