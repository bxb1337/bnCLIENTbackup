package net.AzureWare.mod.mods.PLAYER;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventTick;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.MOVEMENT.Fly;
import net.AzureWare.utils.Colors;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MovementInputFromOptions;

public class InvMove extends Mod {

	boolean inInventory = false;

	public InvMove() {
		super("InvMove", Category.PLAYER);
	}

	@EventTarget
	public void onTick(EventTick event) {
		if (mc.currentScreen instanceof GuiChat) {
			return;
		}
		if (mc.currentScreen != null) {
			KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
					mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump };
			for (KeyBinding bind : moveKeys) {
				KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
			}
			if (!inInventory) {

				inInventory = !inInventory;
			}

		} else {
			if (inInventory) {

				inInventory = !inInventory;
			}
		}
	}
}
