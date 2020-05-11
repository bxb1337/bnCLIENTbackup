package net.AzureWare.mod.mods.MOVEMENT;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPacket.EventPacketType;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.value.Value;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Disabler extends Mod {
	private boolean laggedback, felldown;
	private float[] lastrot = new float[2];
	int lastKey;
	boolean startBlink = false;
	private List<Packet> packets = new ArrayList<>();
	private final Value<Boolean> Flight = new Value<Boolean>("Disabler_Flight", true);
	public static Value<Double> SpeedValue = new Value("Disabler_Speed", 1.0, 1.0, 5.0, 0.25);

	public Disabler() {
		super("Disabler", Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		if (mc.theWorld == null)
			return;
		if (mc.thePlayer != null) {
			laggedback = false;
			felldown = false;
			startBlink = false;
			lastrot[0] = mc.thePlayer.rotationYaw;
			lastrot[1] = mc.thePlayer.rotationPitch;
			if (mc.thePlayer.onGround)
				mc.thePlayer.jump();
		}
		super.onEnable();
	}

	@Override
	public void onDisable() {
		NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
		if (mc.theWorld != null) {
			for (Packet packet : this.packets) {
				var1.sendPacketNoEvent(packet);

			}
		}

		this.packets.clear();
		startBlink = false;
		if (mc.thePlayer != null) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		}

		super.onDisable();
	}

	@EventTarget
	public void onEvent(EventPreMotion event) {
		if (felldown) {
				mc.thePlayer.motionY = 0.0;
			if (laggedback) {
				if (mc.thePlayer.movementInput.jump) {
					mc.thePlayer.motionY = 2.0;
				} else if (mc.thePlayer.movementInput.sneak) {
					mc.thePlayer.motionY = -2.0;
				} 

				setMoveSpeed(SpeedValue.getValueState());
			} else {
				mc.thePlayer.rotationYaw = lastrot[0];
				mc.thePlayer.rotationPitch = lastrot[1];
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
		} else if (mc.thePlayer.fallDistance > 0.0) {
			felldown = true;
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		}

	}

	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.getType() == EventPacketType.RECEIVE) {
			if (felldown && !mc.isSingleplayer() && mc.theWorld != null) {
				if (event.getPacket() instanceof S08PacketPlayerPosLook) {
					S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
					if (mc.thePlayer.posY > packet.getY()) {
						if(!Flight.getValueState())
						{
							this.set(false);
							return;
						}
						laggedback = true;
						startBlink = true;
					}
				}
			}
		} else if (event.getType() == EventPacketType.SEND) {
			Packet packet = event.getPacket();

			if (startBlink) {
				if (packet instanceof C03PacketPlayer) {
					((C03PacketPlayer) packet).onGround = true;
				}
				if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition
						|| packet instanceof C03PacketPlayer.C06PacketPlayerPosLook
						|| packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation
						|| packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
					this.packets.add(packet);
					event.setSendcancel(true);
				}
			}
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
