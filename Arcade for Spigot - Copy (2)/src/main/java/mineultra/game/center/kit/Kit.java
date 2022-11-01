package mineultra.game.center.kit;

import mineultra.core.common.util.C;
import mineultra.core.common.util.MSGUtil;
import org.bukkit.entity.Skeleton;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilPlayer;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.EntityType;
import mineultra.game.center.centerManager;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.Listener;

public abstract class Kit implements Listener
{
    public centerManager Manager;
    public  String _kitName;
    private final String[] _kitDesc;
    private final KitAvailability _kitAvailability;
    private final Perk[] _kitPerks;
    protected EntityType _entityType;
    protected ItemStack _itemInHand;
    protected Material _displayItem;
    
    public Kit(final centerManager manager, final String name, final KitAvailability kitAvailability, final String[] kitDesc, final Perk[] kitPerks, final EntityType entityType, final ItemStack itemInHand) {
        super();
        this.Manager = manager;
        this._kitName = name;
        this._kitDesc = kitDesc;
        this._kitPerks = kitPerks;
        Perk[] kitPerks2;
        for (int length = (kitPerks2 = this._kitPerks).length, i = 0; i < length; ++i) {
            final Perk perk = kitPerks2[i];
            perk.SetHost(this);
        }
        this._kitAvailability = kitAvailability;
        this._entityType = entityType;
        this._itemInHand = itemInHand;
        this._displayItem = Material.BOOK;
        if (itemInHand != null) {
            this._displayItem = itemInHand.getType();
        }
    }
    
    public String GetFormattedName() {
        return this.GetAvailability().GetColor() + "§l" + this._kitName;
    }
    
    public boolean isenabled(){
    return false;
}
    
    public String GetName() {
        return _kitName;
    }
    
    public ItemStack GetItemInHand() {
        return this._itemInHand;
    }
    
    public KitAvailability GetAvailability() {
        return this._kitAvailability;
    }
    
    public String[] GetDesc() {
        return this._kitDesc;
    }
    
    public Perk[] GetPerks() {
        return this._kitPerks;
    }
    
    public boolean HasKit(final Player player) {
        return this.Manager.GetGame() != null && this.Manager.GetGame().HasKit(player, this);
    }
    
    public void ApplyKit(final Player player) {
        UtilInv.Clear(player);
        Perk[] kitPerks;
        for (int length = (kitPerks = this._kitPerks).length, i = 0; i < length; ++i) {
            final Perk perk = kitPerks[i];
            perk.Apply(player);
        }
        this.GiveItems(player);
        UtilInv.Update((Entity)player);
    }
    
    public abstract void GiveItems(final Player p0);
    
    public Entity SpawnEntity(final Location loc) {
        EntityType type = this._entityType;
        if (type == EntityType.PLAYER || type == EntityType.ZOMBIE) {
            type = EntityType.PIG_ZOMBIE;
        }
        final LivingEntity entity = (LivingEntity)this.Manager.GetCreature().SpawnEntity(loc, type);
        entity.setRemoveWhenFarAway(false);
        entity.setCustomName(this.GetAvailability().GetColor() + this.GetName() + " Kit" + ((this.GetAvailability() == KitAvailability.Blue) ? (ChatColor.GRAY + " (" + ChatColor.WHITE + "Ultra" + ChatColor.GRAY + ")") : ""));
        entity.setCustomNameVisible(true);/*
      ((CraftLivingEntity)entity).getHandle().setOnFire(50);
      ((CraftLivingEntity)entity).getHandle().setSneaking(true);*/
      ((CraftLivingEntity)entity).getHandle().setSprinting(true);
      /*((CraftLivingEntity)entity).getHandle().setAirTicks(50);
      */

 
      entity.getEquipment().setItemInHand(this._itemInHand);
        if (type == EntityType.SKELETON && (this.GetName().contains("Wither") || this.GetName().contains("Alpha"))) {
            final Skeleton skel = (Skeleton)entity;
            skel.setSkeletonType(Skeleton.SkeletonType.WITHER);
        }
        UtilEnt.Vegetate((Entity)entity);
        UtilEnt.silence((Entity)entity, true);
        UtilEnt.ghost((Entity)entity, true, false);
        this.SpawnCustom(entity);
        return (Entity)entity;
    }
    
    public void SpawnCustom(final LivingEntity ent) {
    }
    
    public void DisplayDesc(final Player player) {
        for (int i = 0; i < 3; ++i) {
            UtilPlayer.message((Entity)player, "");
        }
        UtilPlayer.message((Entity)player, MSGUtil.getLineSpacer());
        UtilPlayer.message((Entity)player, "§aKit - §f§l" + this.GetName());
        String[] getDesc;
        for (int length = (getDesc = this.GetDesc()).length, j = 0; j < length; ++j) {
            final String line = getDesc[j];
            UtilPlayer.message((Entity)player, String.valueOf(C.cGray) + "  " + line);
        }
        Perk[] getPerks;
        for (int length2 = (getPerks = this.GetPerks()).length, k = 0; k < length2; ++k) {
            final Perk perk = getPerks[k];
            if (perk.IsVisible()) {
                UtilPlayer.message((Entity)player, "");
                UtilPlayer.message((Entity)player, String.valueOf(C.cWhite) + C.Bold + perk.GetName());
                String[] getDesc2;
                for (int length3 = (getDesc2 = perk.GetDesc()).length, l = 0; l < length3; ++l) {
                    final String line2 = getDesc2[l];
                    UtilPlayer.message((Entity)player, String.valueOf(C.cGray) + "  " + line2);
                }
            }
        }
        UtilPlayer.message((Entity)player, MSGUtil.getLineSpacer());
    }
    
    public int GetCost() {
        return 2000;
    }
    
    public Material getDisplayMaterial() {
        return this._displayItem;
    }
    
    public void Deselected(final Player player) {
    }
    
    public void Selected(final Player player) {
    }
}
