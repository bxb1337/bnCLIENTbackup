package net.AzureWare.mod.mods.RENDER;

import net.AzureWare.Client;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;

public class Gui extends Mod {

	public Gui() {
		super("Gui", Category.NONE);
		this.setKey(54);
	}
	
	public void onEnable() {
		this.mc.displayGuiScreen(Client.getInstance().getClickInterface());
		this.set(false);
	}

}
