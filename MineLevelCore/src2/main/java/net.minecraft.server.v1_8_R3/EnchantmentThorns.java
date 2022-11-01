//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraft.server.v1_8_R3;

import java.util.Random;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Enchantment;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.EnchantmentSlotType;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.ItemArmor;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MinecraftKey;

public class EnchantmentThorns extends Enchantment {
    public EnchantmentThorns(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.ARMOR_TORSO);
        this.c("thorns");
    }

    public int a(int i) {
        return 10 + 20 * (i - 1);
    }

    public int b(int i) {
        return super.a(i) + 50;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean canEnchant(ItemStack itemstack) {
        return itemstack.getItem() instanceof ItemArmor?true:super.canEnchant(itemstack);
    }

    public void b(EntityLiving entityliving, Entity entity, int i) {
        Random random = entityliving.bc();
        ItemStack itemstack = EnchantmentManager.a(Enchantment.THORNS, entityliving);
        if(entity != null && a(i, random)) {
            if(entity != null) {
                entity.damageEntity(DamageSource.a(entityliving), (float)b(i, random));
                entity.makeSound("damage.thorns", 0.5F, 1.0F);
            }

            if(itemstack != null) {
                itemstack.damage(3, entityliving);
            }
        } else if(itemstack != null) {
            itemstack.damage(1, entityliving);
        }

    }

    public static boolean a(int i, Random random) {
        return i <= 0?false:random.nextFloat() < 0.15F * (float)i;
    }

    public static int b(int i, Random random) {
        return i > 10?i - 10:1 + random.nextInt(4);
    }
}
