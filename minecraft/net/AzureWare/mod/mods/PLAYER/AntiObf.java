package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class AntiObf extends Mod {
	public AntiObf() {
		super("AntiObsidian", Category.PLAYER);
	}

	/* AntiObf By Kody Edit By VanillaMirror */
	@EventTarget
	public void OnUpdate(EventUpdate e) {
		BlockPos sand = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ));
		Block sandblock = mc.theWorld.getBlockState(sand).getBlock();
		BlockPos forge = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 2, mc.thePlayer.posZ));
		Block forgeblock = mc.theWorld.getBlockState(forge).getBlock();
		BlockPos obsidianpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ));
		Block obsidianblock = mc.theWorld.getBlockState(obsidianpos).getBlock();

		if (obsidianblock == Block.getBlockById(49)) {
			bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(),
					mc.objectMouseOver.getBlockPos().getZ());
			BlockPos downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ));
			mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
		}
		if (forgeblock == Block.getBlockById(61)) {
			bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(),
					mc.objectMouseOver.getBlockPos().getZ());
			BlockPos downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ));
			mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
		}
		if (sandblock == Block.getBlockById(12) || sandblock == Block.getBlockById(13)) {
			bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(),
					mc.objectMouseOver.getBlockPos().getZ());
			BlockPos downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ));
			PlayerUtil.tellPlayer("Sand On your Head. Care for it :D");
			mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
		}
	}

	public void bestTool(int x, int y, int z) {
		int blockId = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
		int bestSlot = 0;
		float f = -1F;
		for (int i1 = 36; i1 < 45; i1++)
			try {
				ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
				if (((curSlot.getItem() instanceof ItemTool) || (curSlot.getItem() instanceof ItemSword)
						|| (curSlot.getItem() instanceof ItemShears))
						&& curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f) {
					bestSlot = i1 - 36;
					f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
				}
			} catch (Exception exception) {
			}

		if (f != -1F) {
			mc.thePlayer.inventory.currentItem = bestSlot;
			mc.playerController.updateController();
		}
	}

}
