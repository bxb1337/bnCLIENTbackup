package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;

import net.AzureWare.Client;
import net.AzureWare.mod.mods.WORLD.IRC;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen {
	private String reason;
	private IChatComponent message;
	private List<String> multilineMessage;
	private final GuiScreen parentScreen;
	private int field_175353_i;
	String messageAddon = "";

	public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
		this.parentScreen = screen;
		this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
		this.message = chatComp;
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the
	 * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when
	 * the GUI is displayed and when the window resizes, the buttonList is cleared
	 * beforehand.
	 */
	public void initGui() {
		this.buttonList.clear();
		this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(),
				this.width - 50);

		if (this.multilineMessage != null) {
			for (String s : this.multilineMessage) {
				
				if (s.contains("Sharing your Ban ID may affect the processing of your appeal")) {
					
					if (s.contains("WATCHDOG")) {
						messageAddon = "AzureWare已收集你的特征报告，会在最快的时间内修复绕过";
					} else {
						messageAddon = "AzureWare ModBan光荣！";
					}
				}
			}
		}

		this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
		if (this.field_175353_i > 500) {
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 180, I18n.format("gui.toMenu", new Object[0])));
		} else {
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100,
					this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT,
					I18n.format("gui.toMenu", new Object[0])));
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for
	 * buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2,
				this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
		int i = this.height / 2 - this.field_175353_i / 2;

		if (this.multilineMessage != null) {
			for (String s : this.multilineMessage) {
//				if (s.contains("WATCHDOG CHEAT DETECTION")) {
//					s = "\2477 Reason: \247f 看门狗作弊检测 [GoodGame Cheater]";
//					System.out.println("你已经被看门狗检测");
//				}
//				if (s.contains("Cheating through the use of unfair game advantages.")) {
//					s = "\2477 Reason: \247f Blacklisted Modifications [NMSL]";
//					System.out.println("你已经被Mod Ban掉辣");
//
//				}
			//	s.replace("WATCHDOG CHEAT DETECTION", "看门狗作弊检测 [GoodGame Cheater]");
			//	s.replace(("Cheating through the use of unfair game advantages."),
			//			"\247f Blacklisted Modifications [NMSL]");
				this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
				i += this.fontRendererObj.FONT_HEIGHT;
				
			}
		}
		if (messageAddon != null) {
			
			this.fontRendererObj.drawCenteredString(messageAddon, width / 2 - 80, height, 0xffffff);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
