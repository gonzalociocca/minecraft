package me.gonzalociocca.minelevel.core.misc;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by noname on 9/3/2017.
 */
public class SubastaItem {
    ItemStack itemstack;
    int fullprice;
    List<SubastaBid> bids = new ArrayList();
    int lastPrice;
    int glassSlot = -1;
    public SubastaItem(ItemStack item, int price){
        itemstack = item;
        fullprice = price;
        lastPrice = this.getInitialPrice();
    }
    public int getGlassSlot(){
        return glassSlot;
    }
    public void setGlassSlot(int var1){
        glassSlot = var1;
    }
    public ItemStack getItemStack(){
        return itemstack;
    }

    public int getFullPrice(){
        return fullprice;
    }

    public int getInitialPrice(){
        return fullprice/3;
    }

    public int getNextBidPrice(){
        return lastPrice;
    }
    public void increaseNextBidPrice(){
        lastPrice*=1.10;
    }
    public void resetAll(){
        bids.clear();
        lastPrice = this.getInitialPrice();
        glassSlot = -1;
    }

    public ItemStack addLoreBids(ItemStack it){
        List<String> lore = new ArrayList();
        List<SubastaBid> offers = this.getSortedBids();
        int pos = 0;
        for(SubastaBid sb : offers){
            if(pos >=5){
                break;
            }
            lore.add(Code.Color(++pos+"# &e"+sb.getBidderName()+" por $"+sb.getMaxBid()));
        }
        ItemMeta im = it.getItemMeta();
        im.setLore(lore);
        it.setItemMeta(im);
        return it;
    }

    public boolean addBidder(Main main, Player player){
        PlayerData pd = main.getDB().getPlayerData(player.getName());
        int bidAmount = this.getNextBidPrice();
        if(pd.getDiamonds() >= bidAmount){
            int var1 = this.getGlassSlot();
            if(var1 < 0){
                return false;
            }
            boolean bidAdded = false;
            for(SubastaBid sb : bids){
                if(sb.getBidCount() >= 10){
                    player.sendMessage(Code.Color("&eHas alcanzado el limite maximo de ofertas en este Item!"));
                    return false;
                }
                if(!sb.canBidOnTime()){
                    player.sendMessage(Code.Color("&eSolo puedes ofertar al mismo item cada 5 segundos!"));
                    return false;
                }
                if(sb.getBidderName().equals(player.getName())){
                    sb.changeBid(bidAmount);
                    player.sendMessage("&fPuedes ofertar "+(10-sb.getBidCount())+" veces mas en este item.");
                    bidAdded = true;
                    break;
                }
            }
            if(!bidAdded){
                bids.add(new SubastaBid(player.getName(),bidAmount));
            }

            increaseNextBidPrice();

            ItemStack offer = Code.makeItemStack("160:4 1 name:&aClick_para_ofertar_por_$"+this.getNextBidPrice());
            offer = addLoreBids(offer);

            Variable.SubastasInventory.setItem(var1, offer);

            for(HumanEntity entity : Variable.SubastasInventory.getViewers()){
                if(entity instanceof Player){
                    ((Player)entity).updateInventory();
                }
            }
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
            player.sendMessage(Code.Color("&a&n&lSubastas&e: Oferta añadida por $"+bidAmount));
            player.sendMessage(Code.Color("&eSe te cobrara si estas conectado y hay espacio en tu inventario"));
            player.sendMessage(Code.Color("&eal terminar la subasta"));
            return true;
        }else{
            player.sendMessage(Code.Color("&a&n&lSubastas&e: Tienes dinero insuficiente para ofertar en esta subasta!"));
            return false;
        }
    }

    public List<SubastaBid> getSortedBids(){
        List<SubastaBid> sorted = new ArrayList(bids);
        Collections.sort(sorted, new Comparator<SubastaBid>() {
            @Override
            public int compare(SubastaBid bid1, SubastaBid bid2)
            {
                return bid2.getMaxBid() - bid1.getMaxBid();
            }
        });
        return sorted;
    }

}
