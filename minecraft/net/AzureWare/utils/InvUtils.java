package net.AzureWare.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static int findEmptySlot() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.MainInventory[i] == null)
                return i;
        }

        return mc.thePlayer.inventory.currentItem + (mc.thePlayer.inventory.getCurrentItem() == null ? 0 : ((mc.thePlayer.inventory.currentItem < 8) ? 1 : -1));
    }

    public static int findEmptySlot(int priority) {
        if (mc.thePlayer.inventory.MainInventory[priority] == null)
            return priority;

        return findEmptySlot();
    }

    public static void swapShift(int slot) {
        Minecraft.getMinecraft().playerController.windowClick(
                Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1,
                Minecraft.getMinecraft().thePlayer);
    }

    public static void swap(int slot, int hotbarNum) {
        Minecraft.getMinecraft().playerController.windowClick(
                Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
                Minecraft.getMinecraft().thePlayer);
    }

    public static boolean isFull() {
        return !Arrays.asList(mc.thePlayer.inventory.MainInventory).contains(null);
    }

    public static int armorSlotToNormalSlot(int armorSlot) {
        return 8 - armorSlot;
    }

    public static void block() {
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
    }

    public static ItemStack getCurrentItem() {
        return mc.thePlayer.getCurrentEquippedItem() == null ? new ItemStack(Blocks.air) : mc.thePlayer.getCurrentEquippedItem();
    }

    public static ItemStack getItemBySlot(int slot) {
        return mc.thePlayer.inventory.MainInventory[slot] == null ? new ItemStack(Blocks.air) : mc.thePlayer.inventory.MainInventory[slot];
    }

    public static List<ItemStack> getHotbarContent() {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            result.add(mc.thePlayer.inventory.MainInventory[i]);
        }
        return result;
    }

    public static List<ItemStack> getAllInventoryContent() {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            result.add(mc.thePlayer.inventory.MainInventory[i]);
        }
        return result;
    }

    public static List<ItemStack> getInventoryContent() {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 9; i < 35; i++) {
            result.add(mc.thePlayer.inventory.MainInventory[i]);
        }
        return result;
    }

    public static int getEmptySlotInHotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.MainInventory[i] == null)
                return i;
        }
        return -1;
    }

    public static float getArmorScore(ItemStack itemStack) {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor))
            return -1;

        ItemArmor itemArmor = (ItemArmor) itemStack.getItem();
        float score = 0;

        //basic reduce amount
        score += itemArmor.damageReduceAmount;

        if (EnchantmentHelper.getEnchantments(itemStack).size() <= 0)
            score -= 0.1;

        int protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);

        score += protection * 0.2;

        return score;
    }

    public static boolean hasWeapon() {
        if (mc.thePlayer.inventory.getCurrentItem() != null)
            return false;

        return (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe) || (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword);
    }

    public static boolean isHeldingSword() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }
}