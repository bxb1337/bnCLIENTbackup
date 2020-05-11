package net.AzureWare.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventBoundingBox;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.MOVEMENT.Jesus;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Phase extends Mod {

	private int delay;
	double multiplier;
	double mx;
	double mz;
	double x;
	double z;

	private Value<String> mode = new Value("Phase", "Mode", 0);

	public Phase() {
		super("Phase", Category.WORLD);
		mode.mode.add("Hypixel");
	}

	@EventTarget
	public void onPacket(EventPacket ep) {
		if (this.isInsideBlock()) {
			return;
		}

		multiplier = 0.2D;
		mx = Math.cos(Math.toRadians((double) (mc.thePlayer.rotationYaw + 90.0F)));
		mz = Math.sin(Math.toRadians((double) (mc.thePlayer.rotationYaw + 90.0F)));
		x = (double) mc.thePlayer.movementInput.moveForward * 0.2D * mx
				+ (double) mc.thePlayer.movementInput.moveStrafe * 0.2D * mz;
		z = (double) mc.thePlayer.movementInput.moveForward * 0.2D * mz
				- (double) mc.thePlayer.movementInput.moveStrafe * 0.2D * mx;
		if (mc.thePlayer.isCollidedHorizontally && ep.getPacket() instanceof C03PacketPlayer) {
			++this.delay;
			C03PacketPlayer player = (C03PacketPlayer) ep.getPacket();
			if (this.delay >= 5) {
				player.x += x;
				player.z += z;
				--player.y;
				this.delay = 0;
			}
		}
	}

	@EventTarget
	public void onBlock(EventBoundingBox ebb) {
		if (ebb.getBoundingBox() != null && ebb.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY
				&& mc.thePlayer.isSneaking()) {
			ebb.setBoundingBox(null);
		}

		if (mc.thePlayer == null) {
			return;
		}

		mc.thePlayer.noClip = true;
		if (ebb.getY() > mc.thePlayer.posY + (this.isInsideBlock() ? 0 : 1)) {
			ebb.setBoundingBox(null);
		}

		if (mc.thePlayer.isCollidedHorizontally && ebb.getY() > mc.thePlayer.boundingBox.minY - 0.4D) {
			ebb.setBoundingBox(null);
		}
	}

	@EventTarget
	public void onPost(EventPostMotion event) {
		multiplier = 0.3D;
		mx = Math.cos(Math.toRadians((double) (mc.thePlayer.rotationYaw + 90.0F)));
		mz = Math.sin(Math.toRadians((double) (mc.thePlayer.rotationYaw + 90.0F)));

		x = (double) mc.thePlayer.movementInput.moveForward * multiplier * mx
				+ (double) mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
		z = (double) mc.thePlayer.movementInput.moveForward * multiplier * mz
				- (double) mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
		byte var15 = -1;
		if (mode.isCurrentMode("Hypixel")) {
			setDisplayName("Hypixel");
			var15 = 1;
		}

		double posY;
		int i;
		double posX;
		if (mode.isCurrentMode("Hypixel")) {
			if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && !this.isInsideBlock()) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x,
						mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
				posX = mc.thePlayer.posX;
				posY = mc.thePlayer.posY;
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX,
						posY - (Jesus.isOnLiquid() ? 9000.0D : 0.09D), mc.thePlayer.posZ, false));
				mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
			}
		}
	}

	private boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); y < MathHelper
					.floor_double(mc.thePlayer.boundingBox.maxY) + 1; ++y) {
				for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper
						.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
					Block block = mc.thePlayer.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
					if (block != null && !(block instanceof BlockAir)) {
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
								mc.theWorld.getBlockState(new BlockPos(x, y, z)));
						if (block instanceof BlockHopper) {
							boundingBox = new AxisAlignedBB((double) x, (double) y, (double) z, (double) (x + 1),
									(double) (y + 1), (double) (z + 1));
						}

						if (boundingBox != null && mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

}