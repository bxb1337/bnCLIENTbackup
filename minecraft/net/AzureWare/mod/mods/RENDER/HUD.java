package net.AzureWare.mod.mods.RENDER;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.base.FinalizableSoftReference;
import com.google.common.collect.Lists;

import net.AzureWare.Client;
import net.AzureWare.command.commands.CommandTitle;
import net.AzureWare.events.EventChat;
import net.AzureWare.events.EventKey;
import net.AzureWare.events.EventRender2D;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.mod.mods.MOVEMENT.Fly;
import net.AzureWare.mod.mods.WORLD.Scaffold2;
import net.AzureWare.ui.loginui.GuiLogin;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.FlatColors;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.R2DUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.utils.fontmanager.UnicodeFontRenderer;
import net.AzureWare.value.Value;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class HUD extends Mod {
	private Value<Boolean> tabgui = new Value("HUD_TabGui", false);
	// private Value<Boolean> mcfont = new Value("HUD_MCFont", false);
	private Value<Boolean> hotbar = new Value("HUD_BlackHotbar", true);
	private Value<Boolean> armor = new Value("HUD_ArmorStatus", false);
	private Value<Boolean> potion = new Value("HUD_PotionStatus", false);
	private Value<Boolean> array = new Value("HUD_ArrayList", false);
	// private Value<Boolean> dab = new Value("HUD_DabOnScreen", false);
	private Value<Boolean> rainbow = new Value("HUD_RainbowArrayList", false);
	public static Value<Boolean> sound = new Value("HUD_ModuleSound", false);
	public static Value<Boolean> noti = new Value("HUD_Notification", false);
	public static Value<Boolean> fastrender = new Value("HUD_Fast", false);
	public static Value<Boolean> block = new Value("HUD_BlockCount", false);

	private Value<Double> alpha = new Value("HUD_BGAlphaMax", 0.5d, 0.0d, 1.0d, 0.01d);
	private Value<Double> alphaMin = new Value("HUD_BGAlphaMin", 0.3d, 0.0d, 1.0d, 0.01d);
	private Value<Double> fps = new Value("HUD_FPSSpoof", 0d, 0d, 10000d, 1d);
	public Value logomode = new Value("HUD", "Logo", 0);
	public static Value font = new Value("HUD", "Font", 0);

	private int s = 0;
	int x = 0;
	private TimeHelper timer1 = new TimeHelper();
	private TimeHelper timer2 = new TimeHelper();
	private boolean Dab1;
	private int addXPort;
	static float YPort;

	private UnicodeFontRenderer tabguifr;

	private ArrayList<Category> categoryValues;
	private int currentCategoryIndex, currentModIndex, currentSettingIndex;

	private int screen;
	static int movingAlpha = 20;

	private TimeHelper movingHelper = new TimeHelper();
	private TimeHelper logohelper = new TimeHelper();
	private TimeHelper logohelper2 = new TimeHelper();
	private int img;
	private int img2 = 0;
	private int potionEffectY;

	public HUD() {
		super("HUD", Category.RENDER);
		this.categoryValues = new ArrayList<Category>();
		this.currentCategoryIndex = 0;
		this.currentModIndex = 0;
		this.currentSettingIndex = 0;
		this.screen = 0;
		this.categoryValues.addAll(Arrays.asList(Category.values()));
		this.categoryValues.remove(5);

		this.logomode.addValue("AzureWare");
		this.logomode.addValue("Zio");
		this.font.addValue("Comfortaa");
		this.font.addValue("Simpleton");
		this.font.addValue("Payback");
		this.font.addValue("VERDANA");
		this.font.addValue("ArialBold");
		this.font.addValue("Robotobold");
		this.font.addValue("JelloFont");
		// this.logomode.addValue("NewDebug");
		// this.logomode.addValue("NewestDebug");
		this.logomode.addValue("Text");
		this.logomode.addValue("None");
	}


	@EventTarget
	public void onRender(EventRender2D event) {
		if (logomode.isCurrentMode("NewDebug") || logomode.isCurrentMode("NewestDebug")) {
			if ((logohelper.isDelayComplete(10) && logomode.isCurrentMode("NewDebug"))
					|| (logohelper.isDelayComplete(35) && logomode.isCurrentMode("NewestDebug"))) {
				img++;
				if (logomode.isCurrentMode("NewDebug")) {
					if (img >= 321) {
						img = 6;
					}
					logohelper.reset();

					if (img >= 113) {
						img2 = 113;
					} else {
						img2 = img;
					}
					YPort = 45;
				} else {
					if (img >= 125) {
						img = 1;
					}
					logohelper.reset();

					if (img >= 48) {
						img2 = 48;
					} else {
						img2 = img;
					}
					YPort = 38;
				}
			}
			if (logomode.isCurrentMode("NewDebug")) {
				RenderUtil.drawImage((ResourceLocation) new ResourceLocation("Client/logo/(" + img2 + ").png"), -4, -2,
						115, 55);
			} else {
				RenderUtil.drawImage((ResourceLocation) new ResourceLocation("Client/logo2/" + img2 + ".png"), -4, -2,
						120, 46);
			}

		} else if (this.logomode.isCurrentMode("Debug")) {
			RenderUtil.drawImage((ResourceLocation) new ResourceLocation("Client/debug.png"), -45, -23, (int) 180,
					(int) 90);
			YPort = 42;
		} else if (this.logomode.isCurrentMode("Wurst")) {
			RenderUtil.drawImage((ResourceLocation) new ResourceLocation("Client/wurst.png"), 0, 2, (int) 128,
					(int) 32);
			YPort = 32;
		} else if (this.logomode.isCurrentMode("Slowly")) {
			RenderUtil.drawImage((ResourceLocation) new ResourceLocation("Client/slowly.png"), 0, 2, (int) 100,
					(int) 60);
			YPort = 58;
		} else if (this.logomode.isCurrentMode("AzureWare")) {
			RenderUtil.drawImage(new ResourceLocation("Client/aw.png"), 0, 0, 325 / 5, 360 / 5);
			YPort = 70;

		} else if (this.logomode.isCurrentMode("Zio")) {
			RenderUtil.drawImage(new ResourceLocation("Client/Zio.png"), 0, 0, 325 / 5, 360 / 5);
			YPort = 70;

		} else if (this.logomode.isCurrentMode("None")) {
			YPort = 4;
		} else if (this.logomode.isCurrentMode("Text")) {

			if (!font.isCurrentMode("JelloFont")) {
				UnicodeFontRenderer fontRender = Client.getInstance().getFontManager().comfortaa17;

				fontRender.drawStringWithShadow(CommandTitle.Title.substring(0, 1), 7, 3.5f,
						new Color(0x66CCFF).darker().getRGB());
				fontRender.drawStringWithShadow(CommandTitle.Title.substring(1, CommandTitle.Title.length()),
						7 + fontRender.getStringWidth(CommandTitle.Title.substring(0, 1)), 3.5f, 0xFFFFFF);
				YPort = 12;
			} else {
				UnicodeFontRenderer fontRender = Client.getInstance().getFontManager().comfortaa40;

				fontRender.drawString(CommandTitle.Title.substring(0, 1), 7, 15f,
						new Color(0x66CCFF).darker().getRGB());
				fontRender.drawString(CommandTitle.Title.substring(1, CommandTitle.Title.length()),
						7 + fontRender.getStringWidth(CommandTitle.Title.substring(0, 1)), 15f,
						new Color(0xFFFFFF).darker().getRGB());
			}

		}

		if (movingHelper.isDelayComplete(10)) {
			if (this.mc.gameSettings.keyBindForward.isKeyDown() || this.mc.gameSettings.keyBindLeft.isKeyDown()
					|| this.mc.gameSettings.keyBindRight.isKeyDown() || this.mc.gameSettings.keyBindBack.isKeyDown()) {
				if (movingAlpha < (int) (alpha.getValueState().floatValue() * 100)) {
					movingAlpha += 2;
				}
			} else {
				if (movingAlpha > (int) (alphaMin.getValueState().floatValue() * 100)) {
					movingAlpha -= 2;
				}
			}
			movingHelper.reset();
		}

		if (hotbar.getValueState().booleanValue()) {
			UnicodeFontRenderer font;
			int otherX;
			font = Client.getInstance().getFontManager().comfortaa17;
			UnicodeFontRenderer font1 = Client.getInstance().getFontManager().jellolight20;

			otherX = 0;
			final ScaledResolution sr = new ScaledResolution(mc);
			RenderUtil.drawRect(0, sr.getScaledHeight() - 23, sr.getScaledWidth(), sr.getScaledHeight(),
					ClientUtil.reAlpha(Colors.BLACK.c, ((float) movingAlpha) / 100));
			// Only HotBar
			// RenderUtil.drawRect(sr.getScaledWidth() / 2 - 91, sr.getScaledHeight() - 23,
			// sr.getScaledWidth() / 2 + 91, sr.getScaledHeight(),
			// ClientUtil.reAlpha(Colors.BLACK.c, ((float)movingAlpha) / 100));

			// SlotOverlay
			if (mc.thePlayer.inventory.currentItem == 0) {
				RenderUtil.drawRect(sr.getScaledWidth() / 2 - 91, sr.getScaledHeight() - 23,
						(sr.getScaledWidth() / 2 + 91) - 20 * 8, sr.getScaledHeight(), Integer.MAX_VALUE);
			} else {
				RenderUtil.drawRect((sr.getScaledWidth() / 2) - 91 + mc.thePlayer.inventory.currentItem * 20,
						sr.getScaledHeight() - 23,
						(sr.getScaledWidth() / 2) + 91 - 20 * (8 - mc.thePlayer.inventory.currentItem),
						sr.getScaledHeight(), Integer.MAX_VALUE);
			}
			GlStateManager.disableBlend();

			if (!this.font.isCurrentMode("JelloFont")) {
				font.drawStringWithShadow("X:", 2, sr.getScaledHeight() - 11 + otherX, 0x66ccff);
				font.drawStringWithShadow(Long.toString(Math.round(mc.thePlayer.posX)), 2 + 10,
						sr.getScaledHeight() - 11 + otherX, Color.LIGHT_GRAY.getRGB());
				font.drawStringWithShadow("Y:", 2 + 40, sr.getScaledHeight() - 11 + otherX, 0x66ccff);
				font.drawStringWithShadow(Long.toString(Math.round(mc.thePlayer.posY)), 2 + 50,
						sr.getScaledHeight() - 11 + otherX, Color.LIGHT_GRAY.getRGB());
				font.drawStringWithShadow("Z:", 2 + 80, sr.getScaledHeight() - 11 + otherX, 0x66ccff);
				font.drawStringWithShadow(Long.toString(Math.round(mc.thePlayer.posZ)), 2 + 90,
						sr.getScaledHeight() - 11 + otherX, Color.LIGHT_GRAY.getRGB());
			} else {
				font1.drawString("X:", 2, sr.getScaledHeight() - 11 + otherX, 0x66ccff);
				font1.drawString(Long.toString(Math.round(mc.thePlayer.posX)), 2 + 10,
						sr.getScaledHeight() - 11 + otherX, Color.LIGHT_GRAY.darker().getRGB());
				font1.drawString("Y:", 2 + 40, sr.getScaledHeight() - 11 + otherX, 0x66ccff);
				font1.drawString(Long.toString(Math.round(mc.thePlayer.posY)), 2 + 50,
						sr.getScaledHeight() - 11 + otherX, Color.LIGHT_GRAY.darker().getRGB());
				font1.drawString("Z:", 2 + 80, sr.getScaledHeight() - 11 + otherX, 0x66ccff);
				font1.drawString(Long.toString(Math.round(mc.thePlayer.posZ)), 2 + 90,
						sr.getScaledHeight() - 11 + otherX, Color.LIGHT_GRAY.darker().getRGB());
			}

			DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			Date today = Calendar.getInstance().getTime();
			String renderTodayDate = df.format(today);

			DateFormat dft = new SimpleDateFormat("HH:mm:ss");
			Date time = Calendar.getInstance().getTime();
			String rendertime = dft.format(time);

			if (!this.font.isCurrentMode("JelloFont")) {
				font.drawString(rendertime + " " + renderTodayDate, sr.getScaledWidth() - 1 - font.getStringWidth(rendertime + " " + renderTodayDate), sr.getScaledHeight() - 21 + otherX, Color.LIGHT_GRAY.getRGB());
			} else {
				font1.drawString(rendertime + " " + renderTodayDate, sr.getScaledWidth() - font1.getStringWidth(rendertime + " " + renderTodayDate), sr.getScaledHeight() - 21 + otherX, Color.LIGHT_GRAY.getRGB());
			}

			String renderString = (Client.isDebugMode ? "Release" : "Release") + " Build"
					+ (Client.isDebugMode ? "" : " " + Client.CLIENT_VERSION);

			if (!this.font.isCurrentMode("JelloFont")) {
				font.drawStringWithShadow(renderString, sr.getScaledWidth() - font.getStringWidth(renderString) - 2.5f,
						sr.getScaledHeight() - font.FONT_HEIGHT - 1, Color.LIGHT_GRAY.getRGB());

				font.drawStringWithShadow("FPS: ", 2, sr.getScaledHeight() - 21 + otherX, 0x66ccff);
				font.drawStringWithShadow((Minecraft.getDebugFPS() + fps.getValueState().intValue()) + "", 22,
						sr.getScaledHeight() - 21 + otherX, Color.LIGHT_GRAY.getRGB());
				font.drawStringWithShadow("Ping: ", 2 + 50, sr.getScaledHeight() - 21 + otherX, 0x66ccff);
				font.drawStringWithShadow(
						mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "ms", 2 + 73,
						sr.getScaledHeight() - 21 + otherX, Color.LIGHT_GRAY.getRGB());
			} else {
				font1.drawString(renderString, sr.getScaledWidth() - font.getStringWidth(renderString) - 2.5f,
						sr.getScaledHeight() - font.FONT_HEIGHT - 1, Color.LIGHT_GRAY.darker().getRGB());

				font1.drawString("FPS: ", 2, sr.getScaledHeight() - 21 + otherX, 0x66ccff);
				font1.drawString((Minecraft.getDebugFPS() + fps.getValueState().intValue()) + "", 22,
						sr.getScaledHeight() - 21 + otherX, Color.LIGHT_GRAY.darker().getRGB());
				font1.drawString("Ping: ", 2 + 50, sr.getScaledHeight() - 21 + otherX, 0x66ccff);
				font1.drawString(mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "ms",
						2 + 73, sr.getScaledHeight() - 21 + otherX, Color.LIGHT_GRAY.darker().getRGB());
			}

		}
		if (this.armor.getValueState().booleanValue()) {
			ScaledResolution sr = new ScaledResolution(mc);
			renderStuffStatus(sr);
		}
		if (this.potion.getValueState().booleanValue()) {
			ScaledResolution sr = new ScaledResolution(mc);
			renderPotionEffects();
		}
		if (this.array.getValueState().booleanValue()) {
			ScaledResolution sr = new ScaledResolution(mc);
			renderArray(sr);
		}

		// FontRenderer font = mc.fontRendererObj;
//		if (block.getValueState() && ModManager.getModByName("Scaffold").isEnabled()) {
//			UnicodeFontRenderer fontRender = Client.getInstance().getFontManager().comfortaa20;
//			ScaledResolution sr = new ScaledResolution(mc);
//			fontRender.drawString("Block: " + Scaffold2.getBlocksCount(), sr.getScaledWidth() / 2,
//					sr.getScaledHeight() / (4 * 3), Color.DARK_GRAY.brighter().getRGB());
//
//		}

		//
		if (tabgui.getValueState().booleanValue()) {
			addXPort = 55;
			int RenderColor;
			int[] counter = { 0 };

			int otherX = 1;
			this.tabguifr = Client.getInstance().getFontManager().comfortaa17;
			int startX = 5;
			int startY = (int) HUD.YPort + 2;
			RenderUtil.drawRect(startX, startY, startX + this.getWidestCategory() + 6,
					startY + this.categoryValues.size() * (9 + 2),
					ClientUtil.reAlpha(Colors.BLACK.c, (float) movingAlpha / 100));
			for (Category c : this.categoryValues) {
				RenderColor = -1;

				if (this.getCurrentCategorry().equals(c)) {
					RenderUtil.drawImage(new ResourceLocation("Client/newui/tabgui.png"), startX, startY,
							this.getWidestCategory() + 6, 11);

					// RenderUtil.drawRect(startX, startY, startX + this.getWidestCategory() + 6,
					// startY + 9 + 2,
					// new Color(51, 0, 10, 190).getRGB());
				}

				String name = c.name();
				tabguifr.drawStringWithShadow(
						name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase(),
						startX + 2 + (this.getCurrentCategorry().equals(c) ? 2 : 0), startY + otherX * 1.5f,
						RenderColor);
				startY += 9 + 2;
				counter[0]++;
			}

			if (screen == 1 || screen == 2) {
				int startModsX = startX + this.getWidestCategory() + 6;
				int startModsY = ((5 + 9) + 2) + currentCategoryIndex * (9 + 2);
				RenderUtil.drawRect(startModsX, startModsY, startModsX + this.getWidestMod() + 5,
						startModsY + this.getModsForCurrentCategory().size() * (9 + 2),
						ClientUtil.reAlpha(Colors.BLACK.c, ((float) movingAlpha) / 100));
				for (Mod m : getModsForCurrentCategory()) {
					if (this.getCurrentModule().equals(m)) {
						RenderUtil.drawImage(new ResourceLocation("Client/newui/tabgui.png"), startModsX, startModsY,
								this.getWidestMod() + 5, +9 + 2);
					}
					tabguifr.drawStringWithShadow(m.getName(),
							startModsX + 2 + (this.getCurrentModule().equals(m) ? 2 : 0), startModsY + otherX * 1.5f,
							m.isEnabled() ? -1 : Color.GRAY.getRGB());
					startModsY += 9 + 2;
				}
			}
		} else {
			addXPort = 0;
		}
	}

	@EventTarget
	public void onKey(EventKey e) {
		if (tabgui.getValueState().booleanValue()) {
			switch (e.getKey()) {
			case Keyboard.KEY_UP:
				this.up();
				break;
			case Keyboard.KEY_DOWN:
				this.down();
				break;
			case Keyboard.KEY_RIGHT:
				this.right(Keyboard.KEY_RIGHT);
				break;
			case Keyboard.KEY_LEFT:
				this.left();
				break;
			case Keyboard.KEY_RETURN:
				this.right(Keyboard.KEY_RETURN);
				break;
			}
		}

	}

	private void up() {
		if (this.currentCategoryIndex > 0 && this.screen == 0) {
			this.currentCategoryIndex--;
		} else if (this.currentCategoryIndex == 0 && this.screen == 0) {
			this.currentCategoryIndex = this.categoryValues.size() - 1;
		} else if (this.currentModIndex > 0 && this.screen == 1) {
			this.currentModIndex--;
		} else if (this.currentModIndex == 0 && this.screen == 1) {
			this.currentModIndex = this.getModsForCurrentCategory().size() - 1;
		} else if (this.currentSettingIndex > 0 && this.screen == 2) {
			this.currentSettingIndex--;
		}

	}

	private void down() {
		if (this.currentCategoryIndex < this.categoryValues.size() - 1 && this.screen == 0) {
			this.currentCategoryIndex++;
		} else if (this.currentCategoryIndex == this.categoryValues.size() - 1 && this.screen == 0) {
			this.currentCategoryIndex = 0;
		} else if (this.currentModIndex < this.getModsForCurrentCategory().size() - 1 && this.screen == 1) {
			this.currentModIndex++;
		} else if (this.currentModIndex == this.getModsForCurrentCategory().size() - 1 && this.screen == 1) {
			this.currentModIndex = 0;
		}
	}

	private void right(int key) {
		if (this.screen == 0) {
			this.screen = 1;
		} else if (this.screen == 1 && this.getCurrentModule() != null) {
			this.getCurrentModule().toggle();
		}

	}

	private void left() {
		if (this.screen == 1) {
			this.screen = 0;
			this.currentModIndex = 0;
		} else if (this.screen == 2) {
			this.screen = 1;
			this.currentSettingIndex = 0;
		}

	}

	private Category getCurrentCategorry() {
		return this.categoryValues.get(this.currentCategoryIndex);
	}

	private Mod getCurrentModule() {
		return getModsForCurrentCategory().get(currentModIndex);
	}

	private ArrayList<Mod> getModsForCurrentCategory() {
		ArrayList<Mod> mods = new ArrayList<Mod>();
		Category c = getCurrentCategorry();
		for (Mod m : ModManager.getModList()) {
			if (m.getCategory().equals(c)) {
				mods.add(m);
			}
		}
		return mods;
	}

	private int getWidestMod() {
		int width = 0;
		for (Mod m : ModManager.getModList()) {
			int cWidth = tabguifr.getStringWidth(m.getName());
			if (cWidth > width) {
				width = cWidth;
			}
		}
		return width;
	}

	private int getWidestCategory() {
		int width = 0;
		for (Category c : this.categoryValues) {
			String name = c.name();
			int cWidth = tabguifr.getStringWidth(
					name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase());
			if (cWidth > width) {
				width = cWidth;
			}
		}
		return width + 2;
	}

	public UnicodeFontRenderer getCurrentRenderer() {
		UnicodeFontRenderer fr = Client.getInstance().getFontManager().comfortaa17;

		if (HUD.font.isCurrentMode("Simpleton")) {
			fr = Client.getInstance().getFontManager().simpleton17;
		} else if (HUD.font.isCurrentMode("Payback")) {
			fr = Client.getInstance().getFontManager().payback18;
		} else if (HUD.font.isCurrentMode("VERDANA")) {
			fr = Client.getInstance().getFontManager().VERDANA18;
		} else if (HUD.font.isCurrentMode("ArialBold")) {
			fr = Client.getInstance().getFontManager().arialBold18;
		} else if (HUD.font.isCurrentMode("Robotobold")) {
			fr = Client.getInstance().getFontManager().robotobold18;
		} else if (HUD.font.isCurrentMode("JelloFont")) {
			fr = Client.getInstance().getFontManager().jellolight20;
		} else if (HUD.font.isCurrentMode("Comfortaa")) {
			// fr = Client.getInstance().getFontManager().jellolight20;
		}

		return fr;
	}

	static UnicodeFontRenderer lastFont = null;

	private void renderArray(ScaledResolution sr) {
		UnicodeFontRenderer fr = this.getCurrentRenderer();

		if (lastFont != fr) {
			for (Mod mod : ModManager.getModList()) {
				mod.posX = 0;
			}
			lastFont = fr;
		}

		ArrayList<Mod> mods = new ArrayList(ModManager.getEnabledModListHUD());

		int counter[] = { 0 };
		float nextY = 0.5f;
		for (Mod module : mods) {
			module.onRenderArray();
			if (module.getCategory() == Category.RENDER)
				continue;
			if (!module.isEnabled() && module.posX == 0)
				continue;
			// ²Êºç
			int color;
			if (rainbow.getValueState().booleanValue()) {
				color = RenderUtil.rainbow(counter[0] * -50);
			} else {
				color = -12000000;
			}

			// Module ÐÅÏ¢
			String modName = module.getName();
			String displayName = module.getDisplayName();
			float modwidth = module.posX;

			// »­±³¾°
			if (!font.isCurrentMode("JelloFont")) {
				RenderUtil.drawRect(sr.getScaledWidth() - modwidth - 3.5f, nextY + module.posYRend - 0.5f,
						sr.getScaledWidth(), nextY + module.posYRend + 9.5f,
						ClientUtil.reAlpha(Colors.BLACK.c, ((float) movingAlpha) / 100));
			}

			color = font.isCurrentMode("JelloFont") ? color = 0xffffff : color;

			if (!font.isCurrentMode("JelloFont"))
				fr.drawString(modName, sr.getScaledWidth() - modwidth - 3f, nextY + 0.5F, -16777216);

			fr.drawString(modName, sr.getScaledWidth() - modwidth - 3.5f, nextY, color);

			if (displayName != null) {
				if (!font.isCurrentMode("JelloFont"))
					fr.drawString(displayName,
							sr.getScaledWidth() - modwidth + fr.getStringWidth(modName + " ") - 4 + 0.5f, nextY + 0.5f,
							-16777216);
				fr.drawString(displayName, sr.getScaledWidth() - modwidth + fr.getStringWidth(modName + " ") - 4, nextY,
						Color.white.darker().getRGB());
			}

			nextY += 10;
			counter[0]++;
		}
	}

	private void renderStuffStatus(ScaledResolution scaledRes) {
		int yOffset = mc.thePlayer.isInsideOfMaterial(Material.water) ? 0 : 0;
		for (int slot = 3, xOffset = 0; slot >= 0; slot--) {
			ItemStack stack = mc.thePlayer.inventory.armorItemInSlot(slot);
			GuiIngame gi = new GuiIngame(mc);
			if (stack != null) {
				mc.getRenderItem().renderItemIntoGUI(stack, scaledRes.getScaledWidth() / 2 + 90 - xOffset,
						scaledRes.getScaledHeight() - 55 - (yOffset / 2) + 15);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				mc.fontRendererObj.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "",
						scaledRes.getScaledWidth() + 190 - xOffset * 2
								+ (stack.getMaxDamage() - stack.getItemDamage() >= 100 ? 4
										: (stack.getMaxDamage() - stack.getItemDamage() <= 100
												&& stack.getMaxDamage() - stack.getItemDamage() >= 10 ? 7 : 11)),
						scaledRes.getScaledHeight() * 2 - 88 - yOffset + 30, 0xFFFFFF);
				GL11.glScalef(2F, 2F, 2F);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				xOffset -= 18;
			}
		}
	}

	private void renderPotionEffects() {
		FontRenderer font = mc.fontRendererObj;
		ScaledResolution res = new ScaledResolution(this.mc);
		int y = (int) ((double) (res.getScaledHeight()
				- (font.FONT_HEIGHT + 1) * this.mc.thePlayer.getActivePotionEffects().size()) - ClientUtil.addY) - 23;
		Iterator localIterator1 = this.mc.thePlayer.getActivePotionEffects().iterator();
		List<PotionEffect> myList = Lists.newArrayList(localIterator1);
		myList.sort((o1, o2) -> font.getStringWidth(I18n.format(o1.getEffectName(), new Object[0]))
				- font.getStringWidth(I18n.format(o2.getEffectName(), new Object[0])));
		for (PotionEffect effect : myList) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String name = I18n.format(potion.getName(), new Object[0]);
			int color = Integer.MIN_VALUE;
			if (effect.getEffectName() == "potion.weither") {
				color = -16777216;
			} else if (effect.getEffectName() == "potion.weakness") {
				color = -9868951;
			} else if (effect.getEffectName() == "potion.waterBreathing") {
				color = -16728065;
			} else if (effect.getEffectName() == "potion.saturation") {
				color = -11179217;
			} else if (effect.getEffectName() == "potion.resistance") {
				color = -5658199;
			} else if (effect.getEffectName() == "potion.regeneration") {
				color = -1146130;
			} else if (effect.getEffectName() == "potion.poison") {
				color = -14513374;
			} else if (effect.getEffectName() == "potion.nightVision") {
				color = -6737204;
			} else if (effect.getEffectName() == "potion.moveSpeed") {
				color = FlatColors.WHITE.c;
			} else if (effect.getEffectName() == "potion.moveSlowdown") {
				color = Colors.DARKGREY.c;
			} else if (effect.getEffectName() == "potion.jump") {
				color = Colors.WHITE.c;
			} else if (effect.getEffectName() == "potion.invisibility") {
				color = -9404272;
			} else if (effect.getEffectName() == "potion.hunger") {
				color = -16744448;
			} else if (effect.getEffectName() == "potion.heal") {
				color = -65536;
			} else if (effect.getEffectName() == "potion.harm") {
				color = -3730043;
			} else if (effect.getEffectName() == "potion.fireResistance") {
				color = Colors.ORANGE.c;
			} else if (effect.getEffectName() == "potion.healthBoost") {
				color = -40121;
			} else if (effect.getEffectName() == "potion.digSpeed") {
				color = Colors.YELLOW.c;
			} else if (effect.getEffectName() == "potion.digSlowdown") {
				color = Colors.DARKGREY.c;
			} else if (effect.getEffectName() == "potion.damageBoost") {
				color = -7667712;
			} else if (effect.getEffectName() == "potion.confusion") {
				color = Colors.DARKGREEN.c;
			} else if (effect.getEffectName() == "potion.blindness") {
				color = -8355712;
			} else if (effect.getEffectName() == "potion.absorption") {
				color = Colors.YELLOW.c;
			}

			font.drawString(name,
					(float) res.getScaledWidth() - font.getStringWidth(
							ClientUtil.removeColorCode(name) + Potion.getDurationString(effect) + " ") - 2.0F,
					y + 2f, color);
			name = Potion.getDurationString(effect);
			font.drawString(name, (float) (res.getScaledWidth() - font.getStringWidth(name) - 2) + 0.5F, y + 2.5F,
					Colors.DARKGREY.c);
			font.drawString(name, (float) (res.getScaledWidth() - font.getStringWidth(name) - 2), y + 2f,
					Colors.GREY.c);
			y += font.FONT_HEIGHT;
			potionEffectY += font.FONT_HEIGHT;
		}

	}

	public void drawArc(float cx, float cy, double r, int c, int startpoint, double arc, int linewidth) {
		r *= 2.0;
		cx *= 2.0f;
		cy *= 2.0f;
		float f = (float) (c >> 24 & 255) / 255.0f;
		float f1 = (float) (c >> 16 & 255) / 255.0f;
		float f2 = (float) (c >> 8 & 255) / 255.0f;
		float f3 = (float) (c & 255) / 255.0f;
		enableGL2D();
		GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
		GL11.glLineWidth((float) linewidth);
		GL11.glEnable((int) 2848);
		GL11.glColor4f((float) f1, (float) f2, (float) f3, (float) f);
		GL11.glBegin((int) 3);
		int i = startpoint;
		while ((double) i <= arc) {
			double x = Math.sin((double) ((double) i * 3.141592653589793 / 180.0)) * r;
			double y = Math.cos((double) ((double) i * 3.141592653589793 / 180.0)) * r;
			GL11.glVertex2d((double) ((double) cx + x), (double) ((double) cy + y));
			++i;
		}
		GL11.glEnd();
		GL11.glDisable((int) 2848);
		GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
		disableGL2D();
	}

	public void drawFullCircle(int cx, int cy, double r, int c) {
		r *= 2.0;
		cx *= 2;
		cy *= 2;
		float f = (float) (c >> 24 & 255) / 255.0f;
		float f1 = (float) (c >> 16 & 255) / 255.0f;
		float f2 = (float) (c >> 8 & 255) / 255.0f;
		float f3 = (float) (c & 255) / 255.0f;
		enableGL2D();
		GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
		GL11.glColor4f((float) f1, (float) f2, (float) f3, (float) f);
		GL11.glBegin((int) 6);
		int i = 0;
		while (i <= 360) {
			double x = Math.sin((double) ((double) i * 3.141592653589793 / 180.0)) * r;
			double y = Math.cos((double) ((double) i * 3.141592653589793 / 180.0)) * r;
			GL11.glVertex2d((double) ((double) cx + x), (double) ((double) cy + y));
			++i;
		}
		GL11.glEnd();
		GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
		disableGL2D();
	}

	public void enableGL2D() {
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDisable((int) 3553);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDepthMask((boolean) true);
		GL11.glEnable((int) 2848);
		GL11.glHint((int) 3154, (int) 4354);
		GL11.glHint((int) 3155, (int) 4354);
	}

	public void disableGL2D() {
		GL11.glEnable((int) 3553);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
		GL11.glDisable((int) 2848);
		GL11.glHint((int) 3154, (int) 4352);
		GL11.glHint((int) 3155, (int) 4352);
	}

	public int transitionTo(int from, int to) {
		int i;
		if (from < to && Minecraft.getDebugFPS() >= 60) {
			i = 0;
			while (i < 3) {
				++from;
				++i;
			}
		}
		if (from > to && Minecraft.getDebugFPS() >= 60) {
			i = 0;
			while (i < 3) {
				--from;
				++i;
			}
		}
		if (from < to && Minecraft.getDebugFPS() >= 40 && Minecraft.getDebugFPS() <= 59) {
			i = 0;
			while (i < 4) {
				++from;
				++i;
			}
		}
		if (from > to && Minecraft.getDebugFPS() >= 40 && Minecraft.getDebugFPS() <= 59) {
			i = 0;
			while (i < 4) {
				--from;
				++i;
			}
		}
		if (from < to && Minecraft.getDebugFPS() >= 0 && Minecraft.getDebugFPS() <= 39) {
			i = 0;
			while (i < 6) {
				++from;
				++i;
			}
		}
		if (from > to && Minecraft.getDebugFPS() >= 0 && Minecraft.getDebugFPS() <= 39) {
			i = 0;
			while (i < 6) {
				--from;
				++i;
			}
		}
		return from;
	}
}
