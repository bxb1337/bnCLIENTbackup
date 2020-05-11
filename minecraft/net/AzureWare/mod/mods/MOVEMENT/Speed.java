package net.AzureWare.mod.mods.MOVEMENT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.Display;
import org.w3c.dom.events.UIEvent;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventPullback;
import net.AzureWare.events.EventStep;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.mod.mods.WORLD.Scaffold2;
import net.AzureWare.mod.mods.WORLD.Timer;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.MathUtils;
import net.AzureWare.utils.MovementUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import ibxm.Player;
import net.minecraft.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Speed extends Mod {

	private int stage = 1;
	private double lastDist, moveSpeedOG, lastDistOG;
	public static Value mode = new Value("Speed", "Mode", 0);
	public boolean shouldslow = false;
	double count = 0.0;
	boolean collided = false;
	int spoofSlot = 0;
	public static TimeHelper timer = new TimeHelper();
	public static TimeHelper lastcheck = new TimeHelper();
	boolean lessSlow;
	double less;
	double stair;

	TimeHelper lastCheck = new TimeHelper();

	public Speed() {
		super("Speed", Category.MOVEMENT);
		mode.mode.add("Hypixel");
		showValue = mode;
	}

	double randomY = random(1334.0D, -1332.0D) / 250000.0D;

	private double random(double min, double max) {
		return Math.random() * (min - max) + max;
	}

	private double a;
	private boolean moving;
	private int c;
	private long ms;
	private boolean onGround;
	private double f;
	private boolean g;
	private double distance;

	@EventTarget
	public void onUpdate(EventUpdate event) {
		setDisplayName(mode.getModeAt(mode.getCurrentMode()));

		if (mode.isCurrentMode("HypixelCN2")) {
			lastDist = Math.sqrt(((mc.thePlayer.posX - mc.thePlayer.prevPosX)
					* (mc.thePlayer.posX - mc.thePlayer.prevPosX))
					+ ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ)));
		}
		if (mode.isCurrentMode("Hypixel")) {
			double var7 = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			this.distance = Math.sqrt(var7 * var7 + zDist * zDist);
		}
		if (lastDist > 5.0D) {
			lastDist = 0.0D;
		}

		lastDist = Math.sqrt(((mc.thePlayer.posX - mc.thePlayer.prevPosX) * (mc.thePlayer.posX - mc.thePlayer.prevPosX))
				+ ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ)));

	}

	@EventTarget
	public void onStep(EventStep event) {
		double height = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
		if (height > 0.7) {
			less = 0;
		}
		if (height == 0.5)
			stair = 0.75;
	}

	public Value<Boolean> lagback = new Value("Speed_LagBackChecks", true);
	private double moveSpeed;
	private double speed = 0.07999999821186066D;

	@EventTarget
	public void onPullback(EventPullback e) {
		if (lagback.getValueState()) {
			ClientUtil.sendClientMessage("(LagBackCheck) Speed Disabled", Notification.Type.WARNING);
			set(false);
		}

	}

	@EventTarget
	public void onMotion(EventMove event) {

		if (mode.isCurrentMode("Hypixel")) {
			if (mc.thePlayer.isCollidedHorizontally) {
				this.collided = true;
			}

			if (this.collided) {
				mc.timer.timerSpeed = 1.0F;
				stage = -1;
			}

			if (this.stair > 0.0D) {
				this.stair -= 0.25D;
			}

			this.less -= this.less > 1.0D ? 0.12D : 0.11D;
			if (this.less < 0.0D) {
				this.less = 0.0D;
			}

			if (!PlayerUtil.isInLiquid() && PlayerUtil.isOnGround(0.01D) && isMoving()) {
				this.collided = mc.thePlayer.isCollidedHorizontally;
				if (stage >= 0 || this.collided) {
					stage = 0;
					a = 0.4086666D + (double) PlayerUtil.getJumpEffect() * 0.1D;
					if (this.stair == 0.0D) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY = a;
						event.setY(mc.thePlayer.motionY);
					}

					++this.less;
					this.lessSlow = this.less > 1.0D && !this.lessSlow;
					if (this.less > 1.12D) {
						this.less = 1.12D;
					}
				}
			}

			this.speed = this.getHypixelSpeed(stage) + 0.0331D;
			this.speed *= 0.91D;
			if (this.stair > 0.0D) {
				this.speed *= 0.66D - (double) PlayerUtil.getSpeedEffect() * 0.1D;
			}

			if (stage < 0) {
				this.speed = getBaseMoveSpeed();
			}

			if (this.lessSlow) {
				this.speed *= 0.93D;
			}

			if (PlayerUtil.isInLiquid()) {
				this.speed = 0.12D;
			}

			if (isMoving()) {
				if(!((TargetStrafe)ModManager.getModByName("TargetStrafe")).doStrafeAtSpeed(event, speed))
				this.setMotion(event, this.speed);
				++stage;
			}
		}

	}

	private void setMotion(EventMove em, double speed) {
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if ((forward == 0.0D) && (strafe == 0.0D)) {
			em.setX(0.0D);
			em.setZ(0.0D);
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1;
				} else if (forward < 0.0D) {
					forward = -1;
				}
			}
			em.setX(mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 88.0D))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 87.9000815258789D)));
			em.setZ(mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 88.0D))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 87.9000815258789D)));

		}
	}

	@Override
	public void onDisable() {
		if (mc.thePlayer == null)
			return;
		mc.timer.timerSpeed = 1.0F;
		super.onDisable();

	}

	public static boolean isNotCollidingBelow(double paramDouble) {
		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(0.0D, -paramDouble, 0.0D)).isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public void onEnable() {

		boolean thePlayer = mc.thePlayer == null;
		this.collided = thePlayer ? false : mc.thePlayer.isCollidedHorizontally;
		this.spoofSlot = thePlayer ? 1 : mc.thePlayer.inventory.currentItem;
		this.lessSlow = false;
		if (mc.thePlayer != null) {
			this.speed = PlayerUtil.defaultSpeed();
		}
		this.less = 0.0D;
		this.count = 0.0D;
		this.lastDist = 0.0D;
		stage = 2;
		mc.timer.timerSpeed = 1.0F;
		super.onEnable();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}

	private double getHypixelSpeed(int stage) {
		double value = PlayerUtil.defaultSpeed() + (0.028 * PlayerUtil.getSpeedEffect())
				+ (double) PlayerUtil.getSpeedEffect() / 15;
		double firstvalue = 0.4145 + (double) PlayerUtil.getSpeedEffect() / 12.5;
		double thirdvalue = 0.4045 + (double) PlayerUtil.getSpeedEffect() / 12.5;
		double decr = (((double) stage / 500) * 3);

		if (stage == 0) {
			// JUMP
			if (timer.isDelayComplete(300)) {
				timer.reset();
				// mc.timer.timerSpeed = 1.354f;
			}
			if (!lastCheck.isDelayComplete(500)) {
				if (!shouldslow)
					shouldslow = true;
			} else {
				if (shouldslow)
					shouldslow = false;
			}
			value = 0.64 + (PlayerUtil.getSpeedEffect() + (0.028 * PlayerUtil.getSpeedEffect())) * 0.134;
		} else if (stage == 1) {
			value = firstvalue;
		} else if (stage == 2) {
			value = thirdvalue;
		} else if (stage >= 3) {
			value = thirdvalue - decr;
		}
		if (shouldslow || !lastCheck.isDelayComplete(500) || collided) {
			value = 0.2;
			if (stage == 0)
				value = 0;
		}

		return Math.max(value, shouldslow ? value : PlayerUtil.defaultSpeed() + (0.028 * PlayerUtil.getSpeedEffect()));
	}

	protected boolean isMoving() {
		return mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
	}

}

//
