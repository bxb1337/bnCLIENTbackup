package net.AzureWare.mod.mods.MOVEMENT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventBoundingBox;
import net.AzureWare.events.EventMotionUpdate;
import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.utils.Wrapper;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Jesus extends Mod {

	public static Value<String> modes = new Value<String>("Jesus", "Mode", 0);
	int stage;
	int water;
	private TimeHelper timer = new TimeHelper();
	private boolean wasWater = false;
	private int ticks = 0;

	public Jesus() {
		super("Jesus", Category.MOVEMENT);
		modes.addValue("WaterSpeed");
		modes.addValue("Float");
	//	modes.addValue("Bhop");

	}

	public static boolean isInLiquid() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer == null) {
			return false;
		}
		boolean inLiquid = false;
		final int y = (int) (mc.thePlayer.boundingBox.minY + 0.02);
		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
				final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && !(block instanceof BlockAir)) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static boolean isOnLiquid() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer == null) {
			return false;
		}
		boolean onLiquid = false;
		final int y = (int) mc.thePlayer.boundingBox.offset(0.0, -0.0, 0.0).minY;
		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
				final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && !(block instanceof BlockAir)) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					onLiquid = true;
				}
			}
		}
		return onLiquid;
	}

	private boolean canJeboos() {
		if (!(mc.thePlayer.fallDistance >= 3.0f || mc.gameSettings.keyBindJump.isPressed() || isInLiquid()
				|| mc.thePlayer.isSneaking())) {
			return true;
		}
		return false;
	}

	private boolean a;

	@EventTarget
	public void onMotionUpdate(EventMove e) {
		if (modes.isCurrentMode("WaterSpeed")) {
			if (isInsideBlock()) {
				a = !a;
				double i;
				if (mc.thePlayer.isSneaking()) {
					i = -0.12D;
				} else if (mc.gameSettings.keyBindJump.isKeyDown()) {
					i = 0.12D;
					e.setOnGround(true);
				} else {
					i = a ? 0.05D : 0.12D;
				}

				mc.thePlayer.motionY = i;
			}
		} else if (modes.isCurrentMode("Float")) {
			if (isInsideBlock()) {
				a = !a;
				double i;
				if (mc.thePlayer.isSneaking()) {
					i = -0.12D;
				} else if (mc.gameSettings.keyBindJump.isKeyDown()) {
					i = 0.12D;
					e.setOnGround(true);
				} else {
					i = a ? 0.05D : 0.12D;
				}

				mc.thePlayer.motionY = i;
			}
		}

	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if (modes.isCurrentMode("Float") && e.getPacket() instanceof C03PacketPlayer && canJeboos() && isOnLiquid()) {
			C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
			Minecraft var10001 = mc;
			packet.y = Minecraft.thePlayer.ticksExisted % 2 == 0 ? packet.y + 0.01D : packet.y - 0.01D;
		}

	}

	@EventTarget
	public void onBB(EventBoundingBox e) {
		if (modes.isCurrentMode("Float") && e.getBlock() instanceof BlockLiquid && canJeboos()) {
			e.setBoundingBox(
					new AxisAlignedBB(e.getX(), e.getY(), e.getZ(), e.getX() + 1.0D, e.getY() + 1.0D, e.getZ() + 1.0D));
		}

	}

	boolean shouldJesus() {
		double x = Minecraft.thePlayer.posX;
		double y = Minecraft.thePlayer.posY;
		double z = Minecraft.thePlayer.posZ;
		ArrayList pos = new ArrayList(
				Arrays.asList(new BlockPos[] { new BlockPos(x + 0.3D, y, z + 0.3D), new BlockPos(x - 0.3D, y, z + 0.3D),
						new BlockPos(x + 0.3D, y, z - 0.3D), new BlockPos(x - 0.3D, y, z - 0.3D) }));
		Iterator var9 = pos.iterator();

		while (var9.hasNext()) {
			BlockPos po = (BlockPos) var9.next();
			if (Minecraft.theWorld.getBlockState(po).getBlock() instanceof BlockLiquid) {
				if (Minecraft.theWorld.getBlockState(po).getProperties().get(BlockLiquid.LEVEL) instanceof Integer) {
					if (((Integer) ((Comparable) Minecraft.theWorld.getBlockState(po).getProperties()
							.get(BlockLiquid.LEVEL))).intValue() <= 4) {
						return true;
					}
				}
			}
		}

		return false;
	}

	@EventTarget
	public void onPre(EventPreMotion e) {
		if (modes.isCurrentMode("Bhop")) {
			if (Minecraft.thePlayer.isInWater() && !Minecraft.thePlayer.isSneaking() && shouldJesus()) {
				Minecraft.thePlayer.motionY = 0.09;
			}
			if (Minecraft.thePlayer.onGround || Minecraft.thePlayer.isOnLadder()) {
				wasWater = false;
			}
			if (Minecraft.thePlayer.motionY > 0.0 && wasWater) {
				if (Minecraft.thePlayer.motionY <= 0.11) {
					EntityPlayerSP player = Minecraft.thePlayer;
					player.motionY *= 1.2671;
				}
				EntityPlayerSP player2 = Minecraft.thePlayer;
				player2.motionY += 0.05172;
			}
			if (isInLiquid() && !Minecraft.thePlayer.isSneaking()) {
				if (ticks < 3) {
					Minecraft.thePlayer.motionY = 0.13;
					++ticks;
					wasWater = false;
				} else {
					Minecraft.thePlayer.motionY = 0.5;
					ticks = 0;
					wasWater = true;
				}
			}
		} else if (modes.isCurrentMode("Float") && isInLiquid() && !Minecraft.thePlayer.isSneaking()
				&& !mc.gameSettings.keyBindJump.isPressed()) {
			Minecraft.thePlayer.motionY = 0.05;
			Minecraft.thePlayer.onGround = true;
		}
	}

	protected boolean isInsideBlock() {
		if (mc.thePlayer == null) {
			return false;
		} else {
			AxisAlignedBB var1 = mc.thePlayer.getEntityBoundingBox();
			boolean var2 = false;

			for (int var3 = MathHelper.floor_double(var1.minX); var3 < MathHelper.floor_double(var1.maxX) + 1; ++var3) {
				int var4 = (int) var1.minY;

				for (int var5 = MathHelper.floor_double(var1.minZ); var5 < MathHelper.floor_double(var1.maxZ)
						+ 1; ++var5) {
					Block var6 = mc.theWorld.getBlockState(new BlockPos(var3, var4, var5)).getBlock();
					if (var6 != null && !(var6 instanceof BlockAir)) {
						if (!(var6 instanceof BlockLiquid)) {
							return false;
						}

						var2 = true;
					}
				}
			}

			return var2;
		}
	}

}
