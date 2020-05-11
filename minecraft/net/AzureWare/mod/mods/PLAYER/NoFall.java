package net.AzureWare.mod.mods.PLAYER;

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

import org.apache.commons.lang3.RandomUtils;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.COMBAT.Criticals;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.block.BlockAir;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

public class NoFall extends Mod {
	private float b = 1.0F;
	private boolean c = true;
	public static boolean Set = false;
	public double ticks = 0;
	private int d;
	private Value mode = new Value("NoFall", "Mode", 0);
	public TimeHelper timer = new TimeHelper();
	public TimeHelper NoFallDelay = new TimeHelper();
	private int times;
	private float lastFall;
	private boolean showed;
	private boolean showed2;
	private int ongroundtick;

	public NoFall() {
		super("NoFall", Category.PLAYER);
		mode.mode.add("Hypixel");
		mode.mode.add("Mineplex");
		mode.mode.add("AAC");
		showValue = mode;
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mode.isCurrentMode("Hypixel")) {
			setDisplayName("Hypixel");
			NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
//			Double curX = mc.thePlayer.posX;
//			Double curY = mc.thePlayer.posY;
//			Double curZ = mc.thePlayer.posZ;


					float falldis = 2.4f + PlayerUtil.getJumpEffect();
					// && isBlockUnder()
					if (mc.thePlayer.fallDistance - lastFall >= falldis) {
					//if (mc.thePlayer.fallDistance >= falldis && isBlockUnder()) {
						lastFall = mc.thePlayer.fallDistance;
						var1.sendPacketNoEvent(new C03PacketPlayer(true));
						PlayerUtil.tellPlayer("NoFall Packet");
					} else if (mc.thePlayer.isCollidedVertically) {
						lastFall = 0.0f;
					}
				

		} else if (mode.isCurrentMode("Mineplex")) {
			setDisplayName("Mineplex");
			if (mc.thePlayer.fallDistance > 2.5F) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				mc.thePlayer.fallDistance = 0.5F;
			}
		}
	}

	public static boolean isBlockUnder() {
		if (mc.thePlayer.posY < 0.0D)
			return false;
		for (int off = 0; off < (int) mc.thePlayer.posY + 2; off += 2) {
			AxisAlignedBB bb = mc.thePlayer.boundingBox.offset(0.0D, -off, 0.0D);
			if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty())
				return true;
		}
		return false;
	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if (mode.isCurrentMode("AAC")) {
			if (e.getPacket() instanceof C03PacketPlayer) {
				if (mc.thePlayer.fallDistance > 3.0F && timer.isDelayComplete(200)) {
					mc.thePlayer.fallDistance = 0.5f;
					timer.reset();
					C04PacketPlayerPosition p = new C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN,
							mc.thePlayer.posZ, true);
					mc.thePlayer.sendQueue.addToSendQueue(p);
				}
			}
		}
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
