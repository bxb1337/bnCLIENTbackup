package net.minecraft.client.gui;

import com.google.common.collect.Lists;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import net.AzureWare.Client;
import net.AzureWare.ui.UIMenu.UIMenu;
import net.AzureWare.ui.UIMenu.UIMenuSlot;
import net.AzureWare.ui.altLogin.GuiAltLogin;
import net.AzureWare.ui.particle.Particle;
import net.AzureWare.ui.particles.ParticleManager;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.fontmanager.UnicodeFontRenderer;
import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

/*
 * Exception performing whole class analysis ignored.
 */
public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	private ArrayList<Particle> particles;
	private Random random;
	private static final AtomicInteger field_175373_f = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private static final Random RANDOM = new Random();
	private int bgstate;
	private float updateCounter;
	private String splashText;
	private GuiButton buttonResetDemo;
	private int panoramaTimer;
	private DynamicTexture viewportTexture;
	private boolean field_175375_v;
	private final Object threadLock;
	private String openGLWarning1;
	private String openGLWarning2;
	private String openGLWarningLink;
	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	public static final String field_96138_a = "Please click " + (Object) EnumChatFormatting.UNDERLINE + "here"
			+ (Object) EnumChatFormatting.RESET + " for more information.";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private ResourceLocation backgroundTexture;
	private UIMenu menu;
	private GuiButton realmsButton;
	public static ParticleManager particleManager;
	private double animationX;
	private boolean started;
	// UnicodeFontRenderer font= Client.getInstance().getFontManager().comfortaa20;;

	public GuiMainMenu() {
		block18: {
			BufferedReader bufferedreader;
			this.random = new Random();
			this.field_175375_v = true;
			this.threadLock = new Object();
			this.openGLWarning2 = field_96138_a;
			this.splashText = "missingno";
			bufferedreader = null;
			try {
				try {
					String s;
					ArrayList list = Lists.newArrayList();
					bufferedreader = new BufferedReader((Reader) new InputStreamReader(
							Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(),
							Charsets.UTF_8));
					while ((s = bufferedreader.readLine()) != null) {
						if ((s = s.trim()).isEmpty())
							continue;
						list.add((Object) s);
					}
					if (!list.isEmpty()) {
						do {
							this.splashText = (String) list.get(RANDOM.nextInt(list.size()));
						} while (this.splashText.hashCode() == 125780783);
					}
				} catch (IOException list) {
					break block18;
				}
			} catch (Throwable throwable) {
				if (bufferedreader != null) {
					try {
						bufferedreader.close();
					} catch (IOException iOException) {
						// empty catch block
					}
				}
				throw throwable;
			}
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException iOException) {
					// empty catch block
				}
			}
		}
		this.updateCounter = RANDOM.nextFloat();
		this.openGLWarning1 = "";
		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
			this.openGLWarning1 = I18n.format((String) "title.oldgl1", (Object[]) new Object[0]);
			this.openGLWarning2 = I18n.format((String) "title.oldgl2", (Object[]) new Object[0]);
			this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}
	}

	public void updateScreen() {
		++this.panoramaTimer;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */

	public void initGui() {
		// font

		this.particles = new ArrayList();
		ScaledResolution resolution = new ScaledResolution(this.mc);
		int i = 0;
		while (i < 150) {
			this.particles.add(new Particle(this.random.nextInt(resolution.getScaledWidth()) + 10,
					this.random.nextInt(resolution.getScaledHeight())));
			++i;
		}
		this.mc.gameSettings.guiScale = 2;
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background",
				this.viewportTexture);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			this.splashText = "Happy new year!";
		} else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
			this.splashText = "OOoooOOOoooo! Spooky!";
		}
		int i2 = 24;
		int j = this.height / 4 + 48;
		if (this.mc.isDemo()) {
			this.addDemoButtons(j, 24);
		} else {
			this.addSingleplayerMultiplayerButtons(j, 24);
		}
		Object object = this.threadLock;
		synchronized (object) {
			this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
			this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
			int k = Math.max((int) this.field_92023_s, (int) this.field_92024_r);
			this.field_92022_t = (this.width - k) / 2;
			this.field_92020_v = this.field_92022_t + k;
			this.field_92019_w = this.field_92021_u + 24;
		}
		this.mc.func_181537_a(false);
		ArrayList slots = new ArrayList();
		slots.add(new UIMenuSlot("Singleplayer", (GuiScreen) new GuiSelectWorld((GuiScreen) this)));
		slots.add(new UIMenuSlot("Multiplayer", (GuiScreen) new GuiMultiplayer((GuiScreen) this)));
		slots.add(new UIMenuSlot("AltManager", (GuiScreen) new GuiAltLogin((GuiScreen) this)));
		slots.add(new UIMenuSlot("Options", (GuiScreen) new GuiOptions((GuiScreen) this, this.mc.gameSettings)));
		slots.add(new UIMenuSlot("Quit", null));
		this.menu = new UIMenu(slots);
		this.started = false;

		// font = Client.getInstance().getFontManager().comfortaa20;

	}

	private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
		int width = 180;
		int height = 20;
	}

	private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
		this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_,
				I18n.format((String) "menu.playdemo", (Object[]) new Object[0])));
		this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1,
				I18n.format((String) "menu.resetdemo", (Object[]) new Object[0]));
		this.buttonList.add(this.buttonResetDemo);
		ISaveFormat isaveformat = this.mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
		if (worldinfo == null) {
			this.buttonResetDemo.enabled = false;
		}
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		ISaveFormat isaveformat;
		WorldInfo worldinfo;
		if (button.id == 0) {
			this.mc.displayGuiScreen((GuiScreen) new GuiOptions((GuiScreen) this, this.mc.gameSettings));
		}
		if (button.id == 5) {
			this.mc.displayGuiScreen(
					(GuiScreen) new GuiLanguage((GuiScreen) this, this.mc.gameSettings, this.mc.getLanguageManager()));
		}
		if (button.id == 1) {
			this.mc.displayGuiScreen((GuiScreen) new GuiSelectWorld((GuiScreen) this));
		}
		if (button.id == 2) {
			this.mc.displayGuiScreen((GuiScreen) new GuiMultiplayer((GuiScreen) this));
		}
		if (button.id == 14 && this.realmsButton.visible) {
			this.switchToRealms();
		}
		if (button.id == 4) {
			this.mc.displayGuiScreen((GuiScreen) new GuiAltLogin((GuiScreen) this));
		}
		if (button.id == 3) {
			this.mc.shutdown();
		}
		if (button.id == 11) {
			this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
		}
		if (button.id == 12
				&& (worldinfo = (isaveformat = this.mc.getSaveLoader()).getWorldInfo("Demo_World")) != null) {
			GuiYesNo guiyesno = GuiSelectWorld.func_152129_a((GuiYesNoCallback) this, (String) worldinfo.getWorldName(),
					(int) 12);
			this.mc.displayGuiScreen((GuiScreen) guiyesno);
		}
	}

	private void switchToRealms() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms((GuiScreen) this);
	}

	public void confirmClicked(boolean result, int id) {
		if (result && id == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen((GuiScreen) this);
		} else if (id == 13) {
			if (result) {
				try {
					Class oclass = Class.forName((String) "java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
							new Object[] { new URI(this.openGLWarningLink) });
				} catch (Throwable throwable) {
					logger.error("Couldn't open link", throwable);
				}
			}
			this.mc.displayGuiScreen((GuiScreen) this);
		}
	}


	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution res = new ScaledResolution(this.mc);

		RenderUtil.drawImage(new ResourceLocation("Client/mainMenu/backGround.png"), (int) 0, 0, res.getScaledWidth(),
				res.getScaledHeight());
//		RenderUtil.drawBorderedRect((res.getScaledWidth() * 14) / 19 - 5f, 0, res.getScaledWidth() * 20 / 21,
//				res.getScaledHeight(), 12, ClientUtil.reAlpha(Colors.AQUA.c, 0.5f),
//				ClientUtil.reAlpha(Colors.BLACK.c, 0f));

		// RenderUtil.drawRoundedRect((res.getScaledWidth() * 14) / 19 - 5f, 0,
		// res.getScaledWidth() * 20 / 21,
		// res.getScaledHeight(), 24f, ClientUtil.reAlpha(Colors.BLACK.c, 0.1f));
		// RenderUtil.drawRect((res.getScaledWidth() * 14) / 19, 0, res.getScaledWidth()
		// * 20 / 21,
		// res.getScaledHeight(), ClientUtil.reAlpha(Colors.BLACK.c, 0.1f));
		this.menu.draw(mouseX, mouseY);
		UnicodeFontRenderer font = Client.getInstance().getFontManager().comfortaa15;
		UnicodeFontRenderer font1 = Client.getInstance().getFontManager().payback15;

		;

		// if ( == 0) {
//		font.drawString(Data, (res.getScaledWidth() * 14) / 19, res.getScaledHeight() / 18,
//				Color.WHITE.darker().getRGB());
//		font1.drawString(Nof.contains(Client.CLIENT_VERSION) ? "The Latest Version" : "Not The Latest",
//				(res.getScaledWidth() * 14) / 19, res.getScaledHeight() / 2, Color.WHITE.darker().getRGB());
//		}
		// System.out.println(Data);

		// tick ++;

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Object object = this.threadLock;
		synchronized (object) {
			if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v
					&& mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
				GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink((GuiYesNoCallback) this,
						this.openGLWarningLink, 13, true);
				guiconfirmopenlink.disableSecurityWarning();
				this.mc.displayGuiScreen((GuiScreen) guiconfirmopenlink);
			}
		}
	}
}
