package net.AzureWare.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventKey;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.main.Main;

public class IRC extends Mod {
	
	public IRC() {
		super("IRC", Category.WORLD);
		// TODO 自动生成的构造函数存根
	}

	@EventTarget
	public void onKey(EventKey e) {
		if (e.getKey() == 52) {
			mc.displayGuiScreen(new GuiChat());
		}
	}
}
