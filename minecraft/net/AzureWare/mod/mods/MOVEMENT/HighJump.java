package net.AzureWare.mod.mods.MOVEMENT;

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

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.ui.notification.Notification.Type;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.EntityUtils;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class HighJump extends Mod {

	private Value mode = new Value("HighJump", "Mode", 0);
	int counter = 0;
	int counter2 = 0;
	private Value<Double> boost = new Value("HighJump_Boost", 0.5D, 0.1D, 5.0D, 0.05D);
	TimeHelper wait = new TimeHelper();

	public HighJump() {
		super("HighJump", Category.MOVEMENT);
		mode.addValue("Vanilla");
		mode.addValue("Hypixel");
		mode.addValue("Mineplex");
	}
	@Override
	public void onEnable() {
		counter = 0;
		counter2 = 0;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public void damagePlayer(int damage) {
		if (damage < 1)
			damage = 1;
		if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
			damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

		double offset = 0.0625;
		if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
			for (int i = 0; i <= ((3 + damage) / offset); i++) { // TODO: teach rederpz (and myself) how math works
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
			}
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (mode.isCurrentMode("Mineplex")) {
			damagePlayer(1);

			mc.thePlayer.lastReportedPosY = 0.0D;
			mc.thePlayer.lastReportedPitch = 999.0F;
			mc.thePlayer.onGround = false;
			mc.thePlayer.motionX = 0.0D;
			mc.thePlayer.motionZ = 0.0D;
			mc.thePlayer.jumpMovementFactor = 0.0F;
			if (mc.thePlayer.isCollidedVertically) {
				if (this.counter2 >= 1) {
					this.counter2 = 0;
					this.set(false);
				}

				mc.getNetHandler().getNetworkManager().sendPacket(
						new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
				if (mc.thePlayer.hurtTime == 9) {
					++this.counter;
					this.counter = 1;
					mc.thePlayer.motionY = 2;
				}
			} else if (mc.thePlayer.motionY < 4.9D) {
				if (mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
					float var4 = mc.thePlayer.rotationYaw * 0.017453292F;
					mc.thePlayer.motionX = (double) (-MathHelper.sin(var4)) * 0.05D;
					mc.thePlayer.motionZ = (double) MathHelper.cos(var4) * 0.05D;
				}
				++this.counter;
			}
		}
	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if ((this.mc.thePlayer.onGround) && (this.mc.gameSettings.keyBindForward.pressed)
				&& (this.wait.isDelayComplete(500L) && mode.isCurrentMode("Vanilla"))) {
			this.mc.thePlayer.motionY = ((Double) this.boost.getValueState()).doubleValue();
			this.wait.reset();
		}

		boolean blockUnderneath = false;
		int i = 0;
		while (i < this.mc.thePlayer.posY + 2.0) {
			BlockPos pos = new BlockPos(this.mc.thePlayer.posX, i, this.mc.thePlayer.posZ);
			if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
				blockUnderneath = true;
			}
			++i;
		}
		if (mode.isCurrentMode("Hypixel")) {
			if (!blockUnderneath) {
				if (e.getPacket() instanceof C03PacketPlayer) {
					if (mc.thePlayer.fallDistance > 5.0F) {
						this.mc.thePlayer.motionY = ((Double) this.boost.getValueState()).doubleValue();
					}
				}
			}
		}
	}
}
