/*
 * Decompiled with CFR 0_132.
 */
package net.AzureWare.mod.mods.COMBAT;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import com.darkmagician6.eventapi.EventTarget;

import ibxm.Module;
import net.AzureWare.events.EventAttack;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.mod.ModManager;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

public class Criticals extends Mod {
	public Value<String> mode = new Value<String>("Criticals", "Mode", 0);
	public Value<Double> Delay = new Value<Double>("Criticals_Delay", 333.0, 0.0, 1000.0, 1.0);

	private TimeHelper timer = new TimeHelper();
	private static Random random = new Random();

	int stage, count;
	double y;
	private int groundTicks;

	public Criticals() {
		super("Criticals", Category.COMBAT);
		mode.mode.add("Packet");
		mode.mode.add("Packet2");
		mode.mode.add("Edit");
		mode.mode.add("Cracking");
		mode.mode.add("Hypixel");
		mode.mode.add("Hypixel2");
		mode.mode.add("HVH");

	}

	private boolean canCrit(EntityLivingBase e) {
		EntityLivingBase target = e;
		return target.hurtTime > 8 && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround;
	}

	@EventTarget
	private void onPacket(EventUpdate e10) {
		super.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
		// Helper.sendMessage((mc.thePlayer.posY-4.0)+"");
		// mc.thePlayer.addPotionEffect(new
		// PotionEffect(Potion.jump.getId(),100,level.getValue().intValue()-1));

	}

	@Override
	public void onDisable() {
		// mc.thePlayer.removePotionEffect(Potion.jump.getId());
	}

	boolean autoCrit(EntityLivingBase e) {
		if (this.isEnabled() == false)
			return false;
		mc.thePlayer.onCriticalHit(e);
		if (!(e instanceof EntityPlayer))
			return false;

		if (canCrit(e)) {
			if (this.timer.hasReached(Delay.getValueState().longValue())) {
				this.timer.reset();
				switch (mode.getModeAt(mode.getCurrentMode())) {
				case "Cracking":
					break;
				case "Edit":
					Crit(new Double[] { 0.0, 0.419999986886978, 0.3331999936342235, 0.2481359985909455,
							0.164773281826067, 0.083077817806467, 0.0, -0.078400001525879, -0.155232004516602,
							-0.230527368912964, -0.304316827457544, -0.376630498238655, -0.104080378093037 });
					break;
				case "Packet":
					Crit(new Double[] { 0.625, -RandomUtils.nextDouble(0.0, 0.625) });
					break;
				case "HVH":
					Crit2(new Double[] { 0.06250999867916107D, -9.999999747378752E-6D, 0.0010999999940395355D });
					break;
				case "Packet2":
					Crit2(new Double[] { 0.626, 0.0 });
					break;
				case "Hypixel":
					Crit2(new Double[] { 0.051D, 0.0125D });
					break;
				case "Hypixel2":
					Crit2(new Double[] { 0.0412622959183674D, 0.01D, 0.0412622959183674D, 0.01D, 0.001D });
					break;
				}

				return true;
			}
		}
		return false;

	}

	public static void Crit2(Double[] value) {
		NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
		Double curX = mc.thePlayer.posX;
		Double curY = mc.thePlayer.posY;
		Double curZ = mc.thePlayer.posZ;
		for (Double offset : value) {
			var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + offset, curZ, false));
		}
	}

	public static void Crit(Double[] value) {
		NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
		Double curX = mc.thePlayer.posX;
		Double curY = mc.thePlayer.posY;
		Double curZ = mc.thePlayer.posZ;
		Double RandomY = 0.0;
		for (Double offset : value) {

			curY += offset;
			// Printer.print(String.valueOf(curY));

			var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + RandomY, curZ, false));

		}
	}

	public static void CritAppointRandom(Double[] value) {
		NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
		Double curX = mc.thePlayer.posX;
		Double curY = mc.thePlayer.posY;
		Double curZ = mc.thePlayer.posZ;
		Double RandomY = 0.0;

		for (Double offset : value) {
			curY += offset;

			// Printer.print(String.valueOf(curY));

			var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + RandomY, curZ, false));

		}
	}

}
