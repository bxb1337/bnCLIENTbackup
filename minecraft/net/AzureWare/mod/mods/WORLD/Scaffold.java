package net.AzureWare.mod.mods.WORLD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.swing.JOptionPane;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventSafeWalk;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RotationUtil;
import net.AzureWare.utils.TimeHelper;
import net.minecraft.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Mod {
	BlockData blockData;
	int blockSlot;
	boolean isSneaking;
	TimeHelper towerTimer = new TimeHelper();

	public Scaffold() {
		super("Scaffold", Category.WORLD);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnable() {
		super.onEnable();
	}

	@EventTarget
	public void onPreMotion(EventPreMotion e) {
		if (getBlockSlot() == -1) {
			PlayerUtil.tellPlayer("\247b[Margele]\247aÃ»·½¿éÄã´îÄãÂèµÄB");
			this.set(false);
		}
		if ((mc.gameSettings.keyBindSneak.pressed)) {
			PlayerUtil.setSpeed(0.11);
			isSneaking = true;
		} else {
			isSneaking = false;
		}

		blockSlot = getBlockSlot();

		BlockPos tempPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1 - (isSneaking ? 0.01 : 0),
				mc.thePlayer.posZ);
		blockData = getBlockData(tempPos);

		float[] rotate = RotationUtil.getRotationFromPosition(blockData.pos.getX(), blockData.pos.getZ(),
				blockData.pos.getY());
		e.yaw = rotate[0];
		e.pitch = rotate[1];
		mc.thePlayer.rotationYawHead = e.yaw;
	}

	@EventTarget
	public void onPostMotion(EventPostMotion e) {
		if (blockData != null) {
			mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(blockSlot));
			if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
					mc.thePlayer.inventory.getStackInSlot(blockSlot), blockData.pos, blockData.face,
					getVec3ByBlockData(blockData))) {
				if (mc.gameSettings.keyBindJump.pressed
						&& !(mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed
								|| mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed)) {
					mc.thePlayer.jump();
					mc.thePlayer.motionX = 0;
					mc.thePlayer.motionZ = 0;
					if (towerTimer.isDelayComplete(1000)) {
						mc.thePlayer.motionY = 0;
						towerTimer.reset();
					}
				}
			}
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
			mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		}
	}

	@EventTarget
	public void onSate(EventSafeWalk e) { // Safe Walk
		// e.setSafe(true);
	}

	public float[] getRotationFromPosition(double x, double y, double z, EnumFacing face) {
		double xDiff = x - mc.thePlayer.posX + face.getFrontOffsetX() / 2 + 0.5;
		double zDiff = z - mc.thePlayer.posZ + face.getFrontOffsetZ() / 2 + 0.5f;
		double yDiff = y - mc.thePlayer.posY + 0.5;
		double yPlayer = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - yDiff;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.14) - 90.0f;
		float pitch = (float) (-Math.atan2(yPlayer, dist) * 180.0 / 3.14);
		if (yaw < 0)
			yaw += 360;
		return new float[] { yaw, pitch };
	}

	public int getBlockSlot() {
		for (int i = 0; i < 9; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i + 36);
			if (slot.getHasStack() && slot.getStack().getItem() instanceof ItemBlock) {
				return i;
			}
		}
		
		if (mc.thePlayer == null || mc.theWorld== null) {
			return 0;
		}

		return 0;
	}

	public Vec3 getVec3ByBlockData(BlockData data) {
		EnumFacing face = data.face;
		BlockPos pos = data.pos;
		Random rand = new Random();

		Boolean isKeepY = face == EnumFacing.UP || face == EnumFacing.DOWN;
		Boolean isKeepXY = face == EnumFacing.WEST || face == EnumFacing.EAST;
		Boolean isKeepYZ = face == EnumFacing.SOUTH || face == EnumFacing.NORTH;

		double x = pos.getX() + 0.5 + face.getFrontOffsetX() / 2 + (isKeepY ? (rand.nextDouble() * 0.6 - 0.3) : 0)
				+ (isKeepYZ ? (rand.nextDouble() * 0.6 - 0.3) : 0);
		double y = pos.getY() + 0.5 + face.getFrontOffsetY() / 2 + (isKeepY ? 0 : (rand.nextDouble() * 0.6 - 0.3));
		double z = pos.getZ() + 0.5 + face.getFrontOffsetZ() / 2 + (isKeepY ? (rand.nextDouble() * 0.6 - 0.3) : 0)
				+ (isKeepXY ? (rand.nextDouble() * 0.6 - 0.3) : 0);
		return new Vec3(x, y, z);
	}

	public BlockData getBlockData(BlockPos pos) {
		BlockData tempData = null;

		int times = 0;
		while (tempData == null) {
			if (times >= 2)
				break;
			if (!isBlockPosAir(pos.add(0, 0, 1))) { // r1
				tempData = new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
				break;
			} else if (!isBlockPosAir(pos.add(0, 0, -1))) {
				tempData = new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
				break;
			} else if (!isBlockPosAir(pos.add(1, 0, 0))) {
				tempData = new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
				break;
			} else if (!isBlockPosAir(pos.add(-1, 0, 0))) {
				tempData = new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
				break;
			} else if (!isBlockPosAir(pos.add(0, -1, 0))) {
				tempData = new BlockData(pos.add(0, -1, 0), EnumFacing.UP); // up when jump
				break;
			} else if (!isBlockPosAir(pos.add(0, 1, 0)) && isSneaking) {
				tempData = new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN); // down
				break;
			} else if (!isBlockPosAir(pos.add(0, 1, 1)) && isSneaking) {
				tempData = new BlockData(pos.add(0, 1, 1), EnumFacing.DOWN); // down
				break;
			} else if (!isBlockPosAir(pos.add(0, 1, -1)) && isSneaking) {
				tempData = new BlockData(pos.add(0, 1, -1), EnumFacing.DOWN); // down
				break;
			} else if (!isBlockPosAir(pos.add(1, 1, 0)) && isSneaking) {
				tempData = new BlockData(pos.add(1, 1, 0), EnumFacing.DOWN); // down
				break;
			} else if (!isBlockPosAir(pos.add(-1, 1, 0)) && isSneaking) {
				tempData = new BlockData(pos.add(-1, 1, 0), EnumFacing.DOWN); // down
				break;
			} else if (!isBlockPosAir(pos.add(1, 0, 1))) { // r2
				tempData = new BlockData(pos.add(1, 0, 1), EnumFacing.NORTH);
				break;
			} else if (!isBlockPosAir(pos.add(-1, 0, -1))) {
				tempData = new BlockData(pos.add(-1, 0, -1), EnumFacing.SOUTH);
				break;
			} else if (!isBlockPosAir(pos.add(1, 0, 1))) {
				tempData = new BlockData(pos.add(1, 0, 1), EnumFacing.WEST);
				break;
			} else if (!isBlockPosAir(pos.add(-1, 0, -1))) {
				tempData = new BlockData(pos.add(-1, 0, -1), EnumFacing.EAST);
				break;
			} else if (!isBlockPosAir(pos.add(-1, 0, 1))) { // r3
				tempData = new BlockData(pos.add(-1, 0, 1), EnumFacing.NORTH);
				break;
			} else if (!isBlockPosAir(pos.add(1, 0, -1))) {
				tempData = new BlockData(pos.add(1, 0, -1), EnumFacing.SOUTH);
				break;
			} else if (!isBlockPosAir(pos.add(1, 0, -1))) {
				tempData = new BlockData(pos.add(1, 0, -1), EnumFacing.WEST);
				break;
			} else if (!isBlockPosAir(pos.add(-1, 0, 1))) {
				tempData = new BlockData(pos.add(-1, 0, 1), EnumFacing.EAST);
				break;
			}
			pos = pos.down();
			times++;
		}
		return tempData;
	}

	public boolean isBlockPosAir(BlockPos pos) {
		if (getBlockByPos(pos) == Blocks.air) {
			return true;
		}
		return false;
	}

	public Block getBlockByPos(BlockPos pos) {
		return mc.theWorld.getBlockState(pos).getBlock();
	}

}

class BlockData {
	public BlockPos pos;
	public EnumFacing face;

	public BlockData(BlockPos pos, EnumFacing face) {
		this.pos = pos;
		this.face = face;
	}

	@Override
	public String toString() {
		return "Pos:" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ". Face:" + face;
	}
}