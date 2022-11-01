package me.gonzalociocca.minelevel.core.misc;

import org.bukkit.inventory.ItemStack;

public class LuckItem {
    ItemStack _itemStack;
    double _from = 0;
    double _to = 0;
    int minMobLevel = -1;
    int maxMobLevel = -1;
    double luckPercent = 0;

    public LuckItem(){
    }

    public LuckItem setLuck(double percent){
        luckPercent = percent;
        recalculateLuck(100);
        return this;
    }

    public LuckItem setItem(ItemStack itemStack){
        _itemStack = itemStack;
        return this;
    }

    public int getMinMobLevel(){
        return minMobLevel;
    }

    public int getMaxMobLevel(){
        return maxMobLevel;
    }

    public LuckItem setMinMobLevel(int level){
        minMobLevel = level;
        return this;
    }

    public LuckItem setMaxMobLevel(int level){
        maxMobLevel = level;
        return this;
    }

    public ItemStack getItemStack(){
        return _itemStack;
    }

    public boolean isFit(double luck, double mobLevel){
        return isLucky(luck) && isMobRange(mobLevel);
    }

    public void recalculateLuck(int max){
        _from = Code.getRandom().nextInt(max);
        _to = _from + Code.getPercentage(luckPercent, max);
        if(_to > max){
            _to-= max;
        }
    }

    public boolean isLucky(double luck){
        if(_to > _from){
            return luck >= _from && luck <= _to;
        } else {
            return luck >= _from || luck <= _to;
        }
    }

    public boolean isMobRange(double mobLevel){
        boolean mobMin = minMobLevel == -1 || mobLevel >= minMobLevel;
        boolean mobMax = maxMobLevel == -1 || mobLevel <= maxMobLevel;
        return mobMin && mobMax;
    }

}