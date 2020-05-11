package net.AzureWare.mod.mods.PLAYER;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventRender;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.COMBAT.AntiBot;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.RotationUtil;
import net.AzureWare.utils.WorldUtil;
import net.AzureWare.value.Value;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

public class AimBot extends Mod {
	public AimBot() {
		super("AimBot", Category.PLAYER);
	}

	public static Vec3 aimed;
	public static Value<Boolean> shothead = new Value<Boolean>("Aimbot_AimShotHead", true);
	public static Value<Boolean> team = new Value<Boolean>("AimBot_Team", true);
	public static boolean firstShot = true;

	public static Value<Double> range = new Value<Double>("AimBot_Reach", 100.0, 10.0, 500.0, 10.0);
	public Value<Double> deviation = new Value<Double>("AimBot_Differ", 1.5, 0.0, 10.0, 0.1);

	@EventTarget(Priority.LOW)
	private void onPre(EventPreMotion event) {
		List<EntityPlayer> targets = WorldUtil.getLivingPlayers().stream().filter(this::isValid)
				.sorted(Comparator.comparing(e -> this.mc.thePlayer.getDistanceToEntity(e)))
				.collect(Collectors.toList());

		if (targets.size() <= 0)
			return;

		this.aimed = this.getFixedLocation(targets.get(0), deviation.getValueState().floatValue(),
				shothead.getValueState());

		float[] rotations = getRot(targets.get(0));
		Random rand = new Random();

		event.setYaw(rotations[0]);
		mc.thePlayer.rotationYawHead = rand.nextInt(360);
	//	System.out.println("1");
		mc.thePlayer.renderYawOffset = rand.nextInt(360);
		event.setPitch(rotations[1]);
	}

	@EventTarget
	private void onRender3D(EventRender event) {
		if (this.aimed == null)
			return;

		double posX = this.aimed.xCoord - this.mc.getRenderManager().getRenderPosX();
		double posY = this.aimed.yCoord - this.mc.getRenderManager().getRenderPosY();
		double posZ = this.aimed.zCoord - this.mc.getRenderManager().getRenderPosZ();
		RenderUtil.drawOutlinedBlockESP(posX - 0.5, posY - 0.5, posZ - 0.5, 255, 0, 0, 20, 10);

	}

	private Vec3 getFixedLocation(EntityLivingBase entity, float velocity, boolean head) {
		double x = entity.posX + ((entity.posX - entity.lastTickPosX) * velocity);
		double y = entity.posY + ((entity.posY - entity.lastTickPosY) * (velocity * 0.3))
				+ (head ? entity.getEyeHeight() : 1.0);
		double z = entity.posZ + ((entity.posZ - entity.lastTickPosZ) * velocity);

		return new Vec3(x, y, z);
	}

	private boolean isValid(EntityPlayer entity) {
		if (entity.isDead || entity.getHealth() <= 0)
			return false;

		if (this.mc.thePlayer.getDistanceToEntity(entity) > range.getValueState())
			return false;

		if (!this.mc.thePlayer.canEntityBeSeen(entity))
			return false;

		if (Teams.isOnSameTeam(entity) && team.getValueState())
			return false;

		if (ModManager.getModByName("Antibot").isEnabled() && AntiBot.isBot(entity))
			return false;

		return true;
	}

	private float[] getPlayerRotations(Entity entity) {
		double distanceToEnt = this.mc.thePlayer.getDistanceToEntity(entity);
		double predictX = entity.posX + (entity.posX - entity.lastTickPosX) * (distanceToEnt * 0.8);
		double predictZ = entity.posZ + (entity.posZ - entity.lastTickPosZ) * (distanceToEnt * 0.8);

		double x = predictX - this.mc.thePlayer.posX;
		double z = predictZ - this.mc.thePlayer.posZ;
		double h = entity.posY + 1.0 - (this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight());

		double h1 = Math.sqrt(x * x + z * z);
		float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;

		float pitch = -RotationUtil.getTrajAngleSolutionLow((float) h1, (float) h, 1);

		return new float[] { yaw, pitch };
	}

	private float getDistance(Entity e) {
		return Math.abs(this.mc.thePlayer.rotationYaw - RotationUtil.getRotations(e)[0]);
	}
	
	private float[] getRot(EntityLivingBase en) {
		double xAim = (en.posX - 0.5) + (en.posX - en.lastTickPosX) * 5.5;
		double yAim = en.posY + (en.getEyeHeight());
		double zAim = (en.posZ - 0.5) + (en.posZ - en.lastTickPosZ) * 5.5;
		if (firstShot || shothead.getValueState()) {
			return PlayerUtil.getFacePos(new Vec3(xAim, yAim - 0.35, zAim));
		}
		float[] rots = PlayerUtil.getFacePos(new Vec3(xAim, yAim - 0.35, zAim));
		Item heldItem = mc.thePlayer.getCurrentEquippedItem().getItem();
		if (heldItem != null) {
			if (heldItem instanceof ItemSpade) {
				rots[1] += 4.2;
			}
			if (heldItem instanceof ItemHoe) {
				rots[1] += 6.5;
			}
		}
		return rots;
	}
	
	public static ArrayList<EntityLivingBase> getClosestEntities(float range) {
		ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (PlayerUtil.isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!AimBot.isValid(en)) {
					continue;
				}
				if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < range) {
					entities.add(en);
				}
			}
		}
		return entities;
	}
	
	
	private static boolean isValid(EntityLivingBase entity) {
		if (entity.isDead || entity.getHealth() <= 0)
			return false;

		if (mc.thePlayer.getDistanceToEntity(entity) > range.getValueState())
			return false;

		if (!mc.thePlayer.canEntityBeSeen(entity))
			return false;

		if (Teams.isOnSameTeam(entity) && team.getValueState())
			return false;

		if (ModManager.getModByName("Antibot").isEnabled() && AntiBot.isBot(entity))
			return false;

		return true;
	}

	
	@Override
	public void onDisable() {
		firstShot = true;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		firstShot = true;
		super.onEnable();
	}



}
