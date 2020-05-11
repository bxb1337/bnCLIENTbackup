package net.AzureWare.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.minecraft.potion.Potion;

public class AntiDebuff extends Mod {

	public AntiDebuff() {
		super("AntiDebuff", Category.RENDER);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.thePlayer.removePotionEffectClient(Potion.blindness.getId());
		mc.thePlayer.removePotionEffectClient(Potion.moveSlowdown.getId());
		mc.thePlayer.removePotionEffectClient(9);
	}
}
