package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

//import me.tojatta.api.utilities.vector.impl.Vector3;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventRender;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.AStarCustomPathFinder;
import net.AzureWare.utils.NukerUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.utils.Vec3;
import net.AzureWare.utils.Wrapper;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
//import net.minecraft.util.Vec3;

import java.util.ArrayList;

public class TeleportBedFucker extends Mod {
	public BlockPos playerBed;
	public BlockPos fuckingBed;
	public ArrayList<BlockPos> posList;

	TimeHelper timer = new TimeHelper();

	public Value<Double> delay = new Value<Double>("TP2Bed_Delay", 600d, 200d, 3000d, 100d);

	public TeleportBedFucker() {
		super("TP2Bed", Category.PLAYER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onEnable() {
		try {
			posList = new ArrayList<BlockPos>(NukerUtil.list);
			posList.sort((o1, o2) -> {
				double distance1 = getDistanceToBlock(o1);
				double distance2 = getDistanceToBlock(o2);
				return (int) (distance1 - distance2);
			});

			if (posList.size() < 3) {
				this.set(false);
			}

			ArrayList<BlockPos> posListFor = new ArrayList<BlockPos>(posList);
			int index = 1;
			for (BlockPos kid : posListFor) {
				index++;
				if (index % 2 == 1) {
					posList.remove(kid);
				}
			}

			playerBed = posList.get(0);
			posList.remove(0);
			if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && PlayerUtil.isOnGround(0.01)) {
				for (int i = 0; i < 49; i++) {
					mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
							mc.thePlayer.posX, mc.thePlayer.posY + 0.06249D, mc.thePlayer.posZ, false));
					mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
							mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
				}
				mc.thePlayer.onGround = false;
				PlayerUtil.setMotion(0);
				mc.thePlayer.jumpMovementFactor = 0;
			}
			fuckingBed = posList.get(0);
		} catch (Throwable e) {
			this.set(false);
		}
		super.onEnable();

	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if (e.getPacket() instanceof C03PacketPlayer) {
			// e.setCancelled(true);
		}
	}
	@Override
	public void onDisable() {
		Wrapper.canSendMotionPacket = true;
		super.onDisable();
	}


	private ArrayList<Vec3> path = new ArrayList<>();

	@EventTarget
	public void onRender(EventRender e) {
		try {
			for (Vec3 vec3 : path) {
				mc.getRenderManager();
				double x = vec3.getX() - (mc.getRenderManager()).getRenderPosX();
				mc.getRenderManager();
				double y = vec3.getY() - (mc.getRenderManager()).getRenderPosY();
				mc.getRenderManager();
				double z = vec3.getZ() - (mc.getRenderManager()).getRenderPosZ();
				double width = mc.thePlayer.getEntityBoundingBox().maxX - mc.thePlayer.getEntityBoundingBox().minX;
				double height = mc.thePlayer.getEntityBoundingBox().maxY - mc.thePlayer.getEntityBoundingBox().minY
						+ 0.25;
				RenderUtil.drawEntityESP(x, y, z, width, height, 0, 1, 0f, 0.2f, 0, 0, 0.0f, 1f, 2f);
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}


	@EventTarget
	public void onUpdate(EventUpdate e) {
		for (BlockPos pos : posList)
			if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed)) {
				PlayerUtil.tellPlayer("\247b[AzureWare]Destory!" + pos);
				posList.remove(pos);
				posList.sort((o1, o2) -> {
					double distance1 = getDistanceToBlock(o1);
					double distance2 = getDistanceToBlock(o2);
					return (int) (distance1 - distance2);
				});
				fuckingBed = posList.get(0);
			}
		if (mc.thePlayer.getDistance(fuckingBed.getX(), fuckingBed.getY(), fuckingBed.getZ()) < 4) {
			Wrapper.canSendMotionPacket = true;
			PlayerUtil.tellPlayer("\247b[AzureWare]Teleported! :3");
			this.set(false);
		}

		if (timer.isDelayComplete(delay.getValueState())) {
			Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			Vec3 to = new Vec3(fuckingBed.getX() + 1, fuckingBed.getY(), fuckingBed.getZ() + 1);

			path = computePath(topFrom, to);

			if (mc.thePlayer.getDistance(fuckingBed.getX(), fuckingBed.getY(), fuckingBed.getZ()) > 4) {
				PlayerUtil.tellPlayer("\247b[Hanabi]Trying to teleport...");
				Wrapper.canSendMotionPacket = false;
				for (Vec3 pathElm : path) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(),
							pathElm.getY(), pathElm.getZ(), true));
				}
			}

			timer.reset();
		}
		if (posList.size() == 0) {
			this.set(false);
		}
	}

	public double getDistanceToBlock(BlockPos pos) {
		return mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ());
	}

	private boolean canPassThrow(BlockPos pos) {
		Block block = Minecraft.getMinecraft().theWorld
				.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
		return block.getMaterial() == Material.air || block.getMaterial() == Material.plants
				|| block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water
				|| block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
	}

	private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
		if (!canPassThrow(new BlockPos(topFrom.mc()))) {
			topFrom = topFrom.addVector(0, 1, 0);
		}
		AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
		pathfinder.compute();

		int i = 0;
		Vec3 lastLoc = null;
		Vec3 lastDashLoc = null;
		ArrayList<Vec3> path = new ArrayList<Vec3>();
		ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
		for (Vec3 pathElm : pathFinderPath) {
			if (i == 0 || i == pathFinderPath.size() - 1) {
				if (lastLoc != null) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
				}
				path.add(pathElm.addVector(0.5, 0, 0.5));
				lastDashLoc = pathElm;
			} else {
				boolean canContinue = true;
				if (pathElm.squareDistanceTo(lastDashLoc) > 5 * 5) {
					canContinue = false;
				} else {
					double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
					double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
					double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
					double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
					double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
					double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
					cordsLoop: for (int x = (int) smallX; x <= bigX; x++) {
						for (int y = (int) smallY; y <= bigY; y++) {
							for (int z = (int) smallZ; z <= bigZ; z++) {
								if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
									canContinue = false;
									break cordsLoop;
								}
							}
						}
					}
				}
				if (!canContinue) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
					lastDashLoc = lastLoc;
				}
			}
			lastLoc = pathElm;
			i++;
		}
		return path;
	}

}

class PlayerPos {
	double x;
	double y;
	double z;

	public PlayerPos(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}
}
