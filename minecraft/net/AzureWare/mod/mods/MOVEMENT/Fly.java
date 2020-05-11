
package net.AzureWare.mod.mods.MOVEMENT;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPacket.EventPacketType;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventPullback;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.AzureWare.ui.notification.Notification;

public class Fly extends Mod {
	public static Value<String> mode = new Value("Fly", "Mode", 0);
	public static Value<Double> MotionSpeed = new Value("Fly_MotionSpeed", 1.0, 1.0, 10.0, 0.25);
	public static Value<Boolean> damage = new Value("Fly_Damage", true);
	public static Value<Boolean> uhc = new Value("Fly_uhc", true);
	public static Value<Boolean> lagback = new Value("Fly_LagBackChecks", true);
	private Value<Boolean> Watting = new Value<Boolean>("Fly_Waitting", false);
	private static Value<Double> WattingTime = new Value<Double>("Fly_WaittingTime", 2.1, 1.0, 5.0, 0.1);
	private static Value<Double> zoomboost = new Value<Double>("Fly_BoostTime", 1.0, 0.0, 10.0, 1.0);
	private static Value<Double> timerboost = new Value<Double>("Fly_timerboost", 0.0, 0.0, 5.0, 0.1);
	private static Value<Double> groundboost = new Value<Double>("Fly_Boost", 2.1, 1.0, 5.0, 0.1);

	double RandomNumber;
	double beforeFlyY;
	private int FlyCounter;
	double TimerSpeed;
	double currentSpeed;
	double moveSpeed;
	private boolean failedStart;
	private double lastDistance;
	private int boostHypixelState;
	double thisY;
	boolean hurtted = false;

	private ArrayList<Packet> packets = new ArrayList<>();

	public Fly() {
		super("Fly", Category.MOVEMENT);
		mode.addValue("Hypixel");
		mode.addValue("HypixelZoom");
		mode.addValue("NewMotion");
		mode.addValue("Motion");
	}

	@EventTarget
	public void onMove(EventMove event) {
		if (mode.getModeName() == "Hypixel") {
			PlayerUtil.setMotion(event, getBaseMoveSpeed());
		}

		if (mode.getModeName() == "HypixelZoom") {
			if (!hurtted)
				return;

			if (failedStart)
				return;

			if (!PlayerUtil.isMoving()) {
				event.setX(0D);
				event.setZ(0D);
				return;
			}
			final double amplifier = 1 + (mc.thePlayer.isPotionActive(Potion.moveSpeed)
					? 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)
					: 0);
			final double baseSpeed = 0.29D * amplifier;

			switch (boostHypixelState) {
			case 1:
				moveSpeed = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.034) * baseSpeed;
				boostHypixelState = 2;
				break;
			case 2:
				moveSpeed *= groundboost.getValueState();
				boostHypixelState = 3;
				break;
			case 3:
				moveSpeed = lastDistance
						- (mc.thePlayer.ticksExisted % 2 == 0 ? 0.0103D : 0.0123D) * (lastDistance - baseSpeed);

				boostHypixelState = 4;
				break;
			default:
				moveSpeed = lastDistance - lastDistance / 159.8D;
				break;
			}
			moveSpeed = Math.max(moveSpeed, 0.3D);

			final double yaw = PlayerUtil.getDirection();
			event.setX(-Math.sin(yaw) * moveSpeed);
			event.setZ(Math.cos(yaw) * moveSpeed);
			mc.thePlayer.motionX = event.getX();
			mc.thePlayer.motionZ = event.getZ();
		}

	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if (e.getType() == EventPacketType.SEND) {
			Packet packet = e.getPacket();
			if (packet instanceof C03PacketPlayer) {
				((C03PacketPlayer) packet).onGround = true;
				if (mode.getModeName() == "Hypixel" || (mode.getModeName() == "HypixelZoom" && hurtted))
				{
					if (mc.thePlayer.isMoving() && !PlayerUtil.isOnGround(0.001)) {
						++FlyCounter;
//						 if (FlyCounter == 10) {
//							 ((C03PacketPlayer) packet).y = beforeFlyY;
//							PlayerUtil.tellPlayer("Back Before Y:"+beforeFlyY);
//							FlyCounter = 0;

//						}else {
							if(FlyCounter % 2 == 0)
							{
								thisY += RandomNumber;
								((C03PacketPlayer) packet).y = thisY;
								PlayerUtil.tellPlayer("Position UP:"+RandomNumber);
							}
//						}
					}
				}

				
			}
			if (mode.getModeName() == "Hypixel" || (mode.getModeName() == "HypixelZoom" && hurtted)
					|| mode.getModeName() == "NewMotion") {
				if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition
						|| packet instanceof C03PacketPlayer.C06PacketPlayerPosLook
						|| packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation
						|| packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
					this.packets.add(packet);
					e.setSendcancel(true);
				}
			}
		}

	}

	@EventTarget
	public void onPreUpdate(EventPreMotion event) {
		if (mode.getModeName() == "Motion" || mode.getModeName() == "NewMotion") {
			mc.thePlayer.motionY = 0;
			if (mc.thePlayer.movementInput.jump) {
				mc.thePlayer.motionY = 2.0;
			} else if (mc.thePlayer.movementInput.sneak) {
				mc.thePlayer.motionY = -2.0;
			}

			setMoveSpeed(MotionSpeed.getValueState());
		}

		if (mode.getModeName() == "Hypixel") {

//			if (mc.thePlayer.isMoving() && !PlayerUtil.isOnGround(0.001)) {
//				++FlyCounter;
//				 if (FlyCounter == 10) {
//					mc.thePlayer.setPosition(mc.thePlayer.posX, beforeFlyY,
//							mc.thePlayer.posZ);
//					thisY = beforeFlyY;
//					PlayerUtil.tellPlayer("Back Before Y:"+beforeFlyY);
//					FlyCounter = 0;
//
//				}else {
//					if(FlyCounter % 2 == 0)
//					{
//						RandomNumber =  10e-13;
//						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomNumber,
//								mc.thePlayer.posZ);
//						thisY += RandomNumber;
//						PlayerUtil.tellPlayer("Position UP:"+RandomNumber);
//					}
//				}
//			}
			mc.thePlayer.motionY = 0;
		}

		if (mode.getModeName() == "HypixelZoom") {
			if (!hurtted)
				return;
			
				
			
			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
			
			mc.thePlayer.lastReportedPosY = 0;
			mc.thePlayer.jumpMovementFactor = 0;
			
			
			mc.timer.timerSpeed = (float)(1.0+TimerSpeed/currentSpeed);
//			if (mc.thePlayer.isMoving() && !PlayerUtil.isOnGround(0.001)) {
//				++FlyCounter;
//				 if (FlyCounter == 10) {
//					mc.thePlayer.setPosition(mc.thePlayer.posX, beforeFlyY,
//							mc.thePlayer.posZ);
//					thisY = beforeFlyY;
//					PlayerUtil.tellPlayer("Back Before Y:"+beforeFlyY);
//					FlyCounter = 0;

//				}else {
//					if(FlyCounter % 2 == 0)
//					{
//						RandomNumber =  10e-8;
//						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomNumber,
//								mc.thePlayer.posZ);
//						thisY += RandomNumber;
//						PlayerUtil.tellPlayer("Position UP:"+RandomNumber);
//					}
//				}
//			}
			currentSpeed = Math.max(currentSpeed + 1, TimerSpeed);
			if (!failedStart)
				mc.thePlayer.motionY = 0D;
		}
	}

	@Override
	public void onEnable() {
		FlyCounter = 0;
		beforeFlyY = mc.thePlayer.posY;
		thisY = mc.thePlayer.posY;
		if (mode.getModeName() == "Hypixel") {
			mc.thePlayer.jump();
			beforeFlyY = mc.thePlayer.posY + 0.41999998688698;
			thisY = mc.thePlayer.posY + 0.41999998688698;
//			mc.thePlayer.motionY = 0.405;
		}

		if (mode.getModeName() == "HypixelZoom") {

			hurtted = false;
			if (damage.getValueState())
				damagePlayer();
			if (Watting.getValueState()) {
				(new Thread(() -> {
					try {
						Thread.sleep(WattingTime.getValueState().longValue() * 100l);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					mc.thePlayer.jump();
					beforeFlyY = mc.thePlayer.posY + 0.41999998688698;
					thisY = mc.thePlayer.posY + 0.41999998688698;
					hurtted = true;

				})).start();
			} else {
				mc.thePlayer.jump();
				beforeFlyY = mc.thePlayer.posY + 0.41999998688698;
				thisY = mc.thePlayer.posY + 0.41999998688698;
				hurtted = true;

			}
			TimerSpeed = timerboost.getValue() * 100 * (1+zoomboost.getValue() / 10);
			currentSpeed = 100 * (1+zoomboost.getValue() / 10);
			boostHypixelState = 1;
			moveSpeed = 0.1D;
			lastDistance = 0D;
			failedStart = false;
		}

		super.onEnable();
	}

	@Override
	public void onDisable() {
		if (mode.getModeName() == "Hypixel" || mode.getModeName() == "HypixelZoom"
				|| mode.getModeName() == "NewMotion") {
			NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
			if (mc.theWorld != null) {
				for (Packet packet : this.packets) {
					var1.sendPacketNoEvent(packet);

				}
			}

			this.packets.clear();
		}
		mc.thePlayer.motionX = 0.0;
		mc.thePlayer.motionZ = 0.0;
		mc.timer.timerSpeed = 1.0f;
//		if (mode.getModeName() == "HypixelZoom") {
//			mc.thePlayer.motionX = 0.0;
//			mc.thePlayer.motionZ = 0.0;
//		}

		super.onDisable();
	}

	public void damagePlayer() {
		if (mc.thePlayer.onGround) {

			for (int index = 0; index <= 67 + (23 * (uhc.getValueState() ? 1 : 0)); ++index) {
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
						mc.thePlayer.posX, mc.thePlayer.posY + 2.535E-9D, mc.thePlayer.posZ, false));
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
						mc.thePlayer.posX, mc.thePlayer.posY + 1.05E-10D, mc.thePlayer.posZ, false));
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
						mc.thePlayer.posX, mc.thePlayer.posY + 0.0448865D, mc.thePlayer.posZ, false));
			}
			mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));

		}

	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}

	@EventTarget
	public void onPullback(EventPullback e) {
		if (lagback.getValueState()) {
			ClientUtil.sendClientMessage("(LagBackCheck) Fly Disabled", Notification.Type.WARNING);
			set(false);
		}

	}

	private void setMoveSpeed(double speed) {
		double forward = mc.thePlayer.movementInput.moveForward;
		double strafe = mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionZ = 0.0;
		} else {
			if (forward != 0.0) {
				if (strafe > 0.0) {
					yaw += ((forward > 0.0) ? -45 : 45);
				} else if (strafe < 0.0) {
					yaw += ((forward > 0.0) ? 45 : -45);
				}
				strafe = 0.0;
				if (forward > 0.0) {
					forward = 1.0;
				} else if (forward < 0.0) {
					forward = -1.0;
				}
			}
			mc.thePlayer.motionX = forward * speed * -Math.sin(Math.toRadians(yaw))
					+ strafe * speed * Math.cos(Math.toRadians(yaw));
			mc.thePlayer.motionZ = forward * speed * Math.cos(Math.toRadians(yaw))
					- strafe * speed * -Math.sin(Math.toRadians(yaw));
		}
	}
}
