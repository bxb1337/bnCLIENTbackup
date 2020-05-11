package net.AzureWare.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

public class FastDig extends Mod {

	private static Value mode = new Value("FastDig", "Mode", 0);
	public static float speed;
	public static int delay;

	public FastDig() {
		super("FastDig", Category.WORLD);
		mode.addValue("Potion");
		mode.addValue("Packet");
		mode.addValue("Test");

	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mode.isCurrentMode("Potion")) {
			Minecraft.getMinecraft().thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 0, 1));
		}
		if (mode.isCurrentMode("Packet")) {
			mc.playerController.blockHitDelay = 0;
			if (mc.playerController.curBlockDamageMP >= 0.7F) {
				mc.playerController.curBlockDamageMP = 1.0F;
			}

		}
		if (mode.isCurrentMode("Test")) {
			mc.playerController.blockHitDelay = 0;
			if (mc.playerController.curBlockDamageMP > 0.8F) {
				mc.playerController.curBlockDamageMP = 1.0F;
			}

			boolean item = mc.thePlayer.getCurrentEquippedItem() == null;
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 20, item ? 1 : 0));
			speed = 1.18F;
			delay = 0;

		}
	}

	public void onDisable() {
		if (mode.isCurrentMode("Potion")) {
			Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.digSpeed.getId());
		}
		if (mode.isCurrentMode("Packet")) {

		}
		if (mode.isCurrentMode("Test")) {
			Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.digSpeed.getId());

		}
	}
}
