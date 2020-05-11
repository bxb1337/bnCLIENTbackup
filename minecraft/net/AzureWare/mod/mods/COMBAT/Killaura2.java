package net.AzureWare.mod.mods.COMBAT;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.FlatColors;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.RotationUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Killaura2 extends Mod {
	
	static boolean currentlyPotting = false;
	private TimeHelper timer = new TimeHelper();
	public static EntityLivingBase target;
	private List targets = new ArrayList(0);
	private int index;
	private int xd;
	private int tpdelay;

	public static Value<Boolean> blocking = new Value("Killaura_AutoBlock", false);
	public Value<String> mode = new Value("Killaura", "Mode", 0);
	public Value<Boolean> invis = new Value("Killaura_Invisible", false);
	public Value<Boolean> mobs = new Value("Killaura_Mobs", false);
	public Value<Boolean> animals = new Value("Killaura_Animals", false);
	public Value<Double> aps = new Value("Killaura_CPS", 11.0D, 1.0D, 20.0D, 1.0D);
	public static Value<Double> reach = new Value("Killaura_Reach", 4.5D, 1.0D, 6.0D, 0.1D);
	public static EntityLivingBase curTarget;
	public Value<Boolean> players = new Value("Killaura_Players", true);
	public Value<String> espMode = new Value("Killaura", "ESP", 0);
	public Value<Boolean> targethud = new Value("Killaura_TargetHUD", true);
	private boolean isBlocking;
	private Comparator angleComparator;

	public Killaura2() {
		super("Killaura", Category.COMBAT);
		mode.mode.add("Switch");
		mode.mode.add("Tick");
		this.espMode.mode.add("None");
		this.espMode.mode.add("Box");
		this.espMode.mode.add("Flat Box");
		this.espMode.mode.add("ESP");
		this.angleComparator = Comparator.comparingDouble((e2) -> {
			return (double) RotationUtil.getRotations((Entity) e2)[0];
		});
	}

	@EventTarget
	public void onRender(final EventRender render) {
		if (Killaura2.target == null || this.espMode.isCurrentMode("None")) {
			return;
		}
		Color color = new Color(Colors.BLUE.c);
		if (Killaura2.target.hurtTime > 0) {
			color = new Color(FlatColors.RED.c);
		}
		if (this.espMode.isCurrentMode("Box")) {
			this.mc.getRenderManager();
			double x = Killaura2.target.lastTickPosX
					+ (Killaura2.target.posX - Killaura2.target.lastTickPosX) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosX;
			this.mc.getRenderManager();
			double y = Killaura2.target.lastTickPosY
					+ (Killaura2.target.posY - Killaura2.target.lastTickPosY) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosY;
			this.mc.getRenderManager();
			double z = Killaura2.target.lastTickPosZ
					+ (Killaura2.target.posZ - Killaura2.target.lastTickPosZ) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosZ;
			if (Killaura2.target instanceof EntityPlayer) {
				x -= 0.275;
				z -= 0.275;
				y += Killaura2.target.getEyeHeight() - 0.225 - (Killaura2.target.isSneaking() ? 0.25 : 0.0);
				final double mid = 0.275;
				GL11.glPushMatrix();
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				final double rotAdd = -0.25 * (Math.abs(Killaura2.target.rotationPitch) / 90.0f);
				GL11.glTranslated(0.0, rotAdd, 0.0);
				GL11.glTranslated(x + 0.275, y + 0.275, z + 0.275);
				GL11.glRotated((double) (-Killaura2.target.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
				GL11.glTranslated(-(x + 0.275), -(y + 0.275), -(z + 0.275));
				GL11.glTranslated(x + 0.275, y + 0.275, z + 0.275);
				GL11.glRotated((double) Killaura2.target.rotationPitch, 1.0, 0.0, 0.0);
				GL11.glTranslated(-(x + 0.275), -(y + 0.275), -(z + 0.275));
				GL11.glDisable(3553);
				GL11.glEnable(2848);
				GL11.glDisable(2929);
				GL11.glDepthMask(false);
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
				GL11.glLineWidth(1.0f);
				RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025,
						x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
				RenderUtil.drawBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025,
						y + 0.55 + 0.0025, z + 0.55 + 0.0025));
				GL11.glDisable(2848);
				GL11.glEnable(3553);
				GL11.glEnable(2929);
				GL11.glDepthMask(true);
				GL11.glDisable(3042);
				GL11.glPopMatrix();
			} else {
				final double width = Killaura2.target.getEntityBoundingBox().maxX
						- Killaura2.target.getEntityBoundingBox().minX;
				final double height = Killaura2.target.getEntityBoundingBox().maxY
						- Killaura2.target.getEntityBoundingBox().minY + 0.25;
				final float red = 0.0f;
				final float green = 0.5f;
				final float blue = 1.0f;
				final float alpha = 0.5f;
				final float lineRed = 0.0f;
				final float lineGreen = 0.5f;
				final float lineBlue = 1.0f;
				final float lineAlpha = 1.0f;
				final float lineWdith = 2.0f;
				RenderUtil.drawEntityESP(x, y, z, width, height, 0.0f, 0.5f, 1.0f, 0.5f, 0.0f, 0.5f, 1.0f, 1.0f, 2.0f);
			}
		} else if (espMode.isCurrentMode("Flat Box")) {
			this.mc.getRenderManager();
			double x = Killaura2.target.lastTickPosX
					+ (Killaura2.target.posX - Killaura2.target.lastTickPosX) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosX;
			this.mc.getRenderManager();
			double y = Killaura2.target.lastTickPosY
					+ (Killaura2.target.posY - Killaura2.target.lastTickPosY) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosY;
			this.mc.getRenderManager();
			double z = Killaura2.target.lastTickPosZ
					+ (Killaura2.target.posZ - Killaura2.target.lastTickPosZ) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosZ;
			if (Killaura2.target instanceof EntityPlayer) {
				x -= 0.5;
				z -= 0.5;
				y += Killaura2.target.getEyeHeight() + 0.35 - (Killaura2.target.isSneaking() ? 0.25 : 0.0);
				final double mid = 0.5;
				GL11.glPushMatrix();
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				final double rotAdd = -0.25 * (Math.abs(Killaura2.target.rotationPitch) / 90.0f);
				GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
				GL11.glRotated((double) (-Killaura2.target.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
				GL11.glTranslated(-(x + 0.5), -(y + 0.5), -(z + 0.5));
				GL11.glDisable(3553);
				GL11.glEnable(2848);
				GL11.glDisable(2929);
				GL11.glDepthMask(false);
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
				GL11.glLineWidth(2.0f);
				RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
				RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
				GL11.glDisable(2848);
				GL11.glEnable(3553);
				GL11.glEnable(2929);
				GL11.glDepthMask(true);
				GL11.glDisable(3042);
				GL11.glPopMatrix();
			} else {
				final double width = Killaura2.target.getEntityBoundingBox().maxZ
						- Killaura2.target.getEntityBoundingBox().minZ;
				final double height = 0.1;
				final float red = 0.0f;
				final float green = 0.5f;
				final float blue = 1.0f;
				final float alpha = 0.5f;
				final float lineRed = 0.0f;
				final float lineGreen = 0.5f;
				final float lineBlue = 1.0f;
				final float lineAlpha = 1.0f;
				final float lineWdith = 2.0f;
				RenderUtil.drawEntityESP(x, y + Killaura2.target.getEyeHeight() + 0.25, z, width, 0.1, 0.0f, 0.5f, 1.0f,
						0.5f, 0.0f, 0.5f, 1.0f, 1.0f, 2.0f);
			}
		} else if (espMode.isCurrentMode("ESP")) {
			if (Killaura2.target instanceof EntityPlayer) {
				if (Killaura2.target.hurtTime > 0) {
					renderBox(target, 220.0 / 255.0, 20.0 / 255.0, 60.0 / 255.0);
				} else {
					renderBox(target, 135.0 / 255.0, 206.0 / 255.0, 250.0 / 255.0);
				}
			}
		}
	}

	public void renderBox(Entity entity, double r, double g, double b) {
		double x = RenderUtil.interpolate((double) entity.posX, (double) entity.lastTickPosX);
		double y = RenderUtil.interpolate((double) entity.posY, (double) entity.lastTickPosY);
		double z = RenderUtil.interpolate((double) entity.posZ, (double) entity.lastTickPosZ);
		GL11.glPushMatrix();
		RenderUtil.pre();
		GL11.glLineWidth((float) 1.0f);
		GL11.glEnable((int) 2848);
		GL11.glColor3d(r, g, b);
		Minecraft.getMinecraft().getRenderManager();
		Minecraft.getMinecraft().getRenderManager();
		Minecraft.getMinecraft().getRenderManager();
		RenderGlobal.drawSelectionBoundingBox((AxisAlignedBB) new AxisAlignedBB(
				entity.boundingBox.minX - 0.05 - entity.posX + (entity.posX - mc.getRenderManager().renderPosX),
				entity.boundingBox.minY - entity.posY + (entity.posY - mc.getRenderManager().renderPosY),
				entity.boundingBox.minZ - 0.05 - entity.posZ + (entity.posZ - mc.getRenderManager().renderPosZ),
				entity.boundingBox.maxX + 0.05 - entity.posX + (entity.posX - mc.getRenderManager().renderPosX),
				entity.boundingBox.maxY + 0.1 - entity.posY + (entity.posY - mc.getRenderManager().renderPosY),
				entity.boundingBox.maxZ + 0.05 - entity.posZ + (entity.posZ - mc.getRenderManager().renderPosZ)));
		GL11.glDisable((int) 2848);
		RenderUtil.post();
		GL11.glPopMatrix();
	}

	public void onEnable() {
		this.target = null;
		this.index = 0;
		this.xd = 0;
		super.onEnable();
	}

	public void onDisable() {
		this.targets.clear();
		if (((Boolean) this.blocking.getValueState()).booleanValue() && this.canBlock()
				&& this.mc.thePlayer.isBlocking()) {
			this.stopAutoBlockHypixel();
		}
		super.onDisable();
	}

	private boolean canBlock() {
		return this.mc.thePlayer.getCurrentEquippedItem() != null
				&& this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
	}

	private void startAutoBlockHypixel() {
		KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
		if (this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld,
				this.mc.thePlayer.inventory.getCurrentItem())) {
			this.mc.getItemRenderer().resetEquippedProgress2();
		}
	}

	private void stopAutoBlockHypixel() {
		KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
		this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);
	}

	private boolean shouldAttack() {
		return this.timer.hasReached(
				(long) (1000.0D / (((Double) this.aps.getValueState()).doubleValue() + new Random().nextInt(3))));
	}

	@EventTarget
	public void onPre(EventPreMotion event) {
		this.targets = this.loadTargets();
		this.targets.sort(this.angleComparator);
		if (this.target != null && this.target instanceof EntityPlayer || this.target instanceof EntityMob
				|| this.target instanceof EntityAnimal) {
			this.target = null;
		}

		if (this.mc.thePlayer.ticksExisted % 50 == 0 && this.targets.size() > 1) {
			++this.index;
		}

		if (!this.targets.isEmpty()) {
			if (this.index >= this.targets.size()) {
				this.index = 0;
			}

			this.target = (EntityLivingBase) this.targets.get(this.index);
			event.setYaw(RotationUtil.faceTarget(this.target, 1000.0F, 1000.0F, false)[0]);
			event.setPitch(RotationUtil.faceTarget(this.target, 1000.0F, 1000.0F, false)[1]);

		} else if (((Boolean) this.blocking.getValueState()).booleanValue() && this.canBlock()
				&& this.mc.thePlayer.isBlocking()) {
			this.stopAutoBlockHypixel();
		}
	}

	private void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				this.mc.thePlayer);
	}

	@EventTarget
	public void onPost(EventPostMotion event) {
		if (this.target != null) {
			double angle = Math.toRadians((double) (this.target.rotationYaw - 90.0F + 360.0F)) % 360.0D;
			if (this.shouldAttack()) {

				if (mode.isCurrentMode("Switch")) {
					setDisplayName("Switch");
					this.attack();
				} else {
					setDisplayName("Tick");
					this.swap(9, this.mc.thePlayer.inventory.currentItem);
					this.attack();
					this.attack();
					this.attack();
					this.swap(9, this.mc.thePlayer.inventory.currentItem);
					this.attack();
					this.attack();
				}

				if (!this.mc.thePlayer.isBlocking() && this.canBlock()
						&& ((Boolean) this.blocking.getValueState()).booleanValue()) {
					this.startAutoBlockHypixel();
				}

				this.timer.reset();
			}

		}
	}

	private List loadTargets() {
		/*
		return (List) this.mc.theWorld.loadedEntityList.stream().filter((e) -> {
			return (double) this.mc.thePlayer.getDistanceToEntity((Entity) e) <= ((Double) this.reach.getValueState())
					.doubleValue() && this.qualifies((Entity) e);
		}).collect(Collectors.toList());
		*/
		
		List<EntityLivingBase> list = new ArrayList();
		for (Entity entity : mc.theWorld.loadedEntityList) {
			if (entity instanceof EntityLivingBase && qualifies((EntityLivingBase) entity)
					&& mc.thePlayer.getDistanceToEntity(entity) <= reach.getValueState() + 0.75) {
				list.add((EntityLivingBase) entity);
			}
		}
		return list;
	}

	private boolean qualifies(Entity e) {
		if (e == this.mc.thePlayer) {
			return false;
		} else {
			// AntiBot ab =
			// (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
			if (Teams.isOnSameTeam(e)) {
				return false;
			}
			if (AntiBot.isBot(e)) {
				return false;
			} else if (!e.isEntityAlive()) {
				return false;
			} else if (e instanceof EntityPlayer && ((Boolean) this.players.getValueState()).booleanValue()) {
				return true;
			} else if (e instanceof EntityMob && ((Boolean) this.mobs.getValueState()).booleanValue()) {
				return true;
			} else if (e instanceof EntityAnimal && ((Boolean) this.animals.getValueState()).booleanValue()) {
				return true;
			} else {
				return e.isInvisible() && !((Boolean) this.invis.getValueState()).booleanValue();
			}
		}
	}

	private void attack() {
		if (((Boolean) this.blocking.getValueState()).booleanValue() && this.canBlock()
				&& this.mc.thePlayer.isBlocking() && this.qualifies(this.target)) {
			this.stopAutoBlockHypixel();
		}

		this.mc.thePlayer.swingItem();

		this.mc.thePlayer.sendQueue
				.addToSendQueue(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
	}

	@EventTarget
	public void onPacket(EventPacket packet) {
		if (packet.getPacket() instanceof C07PacketPlayerDigging) {
			C07PacketPlayerDigging packetPlayerDigging = (C07PacketPlayerDigging) packet.getPacket();
			if (packetPlayerDigging.getStatus().equals(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
				this.isBlocking = false;
			}
		}

		if (packet.getPacket() instanceof C08PacketPlayerBlockPlacement) {
			C08PacketPlayerBlockPlacement blockPlacement = (C08PacketPlayerBlockPlacement) packet.getPacket();
			if (blockPlacement.getStack() != null && blockPlacement.getStack().getItem() instanceof ItemSword
					&& blockPlacement.getPosition().equals(new BlockPos(-1, -1, -1))) {
				this.isBlocking = true;
			}
		}
	}

	public void mmcAttack(EntityLivingBase entity) {
		this.mmcAttack(entity, false);
	}

	public void mmcAttack(EntityLivingBase entity, boolean crit) {
		this.mc.thePlayer.swingItem();
		float sharpLevel = EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(),
				entity.getCreatureAttribute());
		boolean vanillaCrit = this.mc.thePlayer.fallDistance > 0.0F && !this.mc.thePlayer.onGround
				&& !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater()
				&& !this.mc.thePlayer.isPotionActive(Potion.blindness) && this.mc.thePlayer.ridingEntity == null;
		this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
		if (crit || vanillaCrit) {
			this.mc.thePlayer.onCriticalHit(entity);
		}

		if (sharpLevel > 0.0F) {
			this.mc.thePlayer.onEnchantmentCritical(entity);
		}

	}

	public static enum Direction {
		S("S", 0), SW("SW", 1), W("W", 2), NW("NW", 3), N("N", 4), NE("NE", 5), E("E", 6), SE("SE", 7);

		private Direction(String s, int n) {
		}
	}
}