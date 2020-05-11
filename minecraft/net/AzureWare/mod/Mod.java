package net.AzureWare.mod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.darkmagician6.eventapi.EventManager;

import net.AzureWare.Client;
import net.AzureWare.mod.mods.RENDER.HUD;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.HwidTools;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.fontmanager.UnicodeFontRenderer;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.main.Main;
import net.minecraft.entity.Entity;

public class Mod {
	public static Minecraft mc = Minecraft.getMinecraft();
	public EntityPlayerSP player;
	public Value showValue;
	private String name;
	private int key;
	private Category category;
	private boolean isEnabled;
	private String desc;
	public boolean openValues;
	public double arrowAnlge;
	public double animateX;
	public float posX;
	public double hoverOpacity;
	public float circleValue;
	public boolean canSeeCircle;
	public int[] circleCoords;
	public boolean clickedCircle;
	public float posY;

	public String displayName;
	public float lastY;
	public float posYRend;
	public float displaywidth;
	public float namewidth;

	public static String fuck = Main.list;
	//public static String me = Main.list.get(1);

	public String getDisplayName() {
		return displayName;
	}
	static UnicodeFontRenderer fr = Client.getInstance().getFontManager().comfortaa17;

	public void setDisplayName(String displayName) {
		if (mc.currentScreen == Client.getInstance().getClickInterface())
			return;
		
		if (this.displayName == null) {
			this.displayName = displayName;
			displaywidth = fr.getStringWidth(displayName);
			namewidth = fr.getStringWidth(name);
			posX = 0;
			ModManager.needsort = true;
		}

		if (!this.displayName.equals(displayName)) {
			this.displayName = displayName;
			displaywidth = fr.getStringWidth(displayName);
			namewidth = fr.getStringWidth(name);
			posX = 0;
			ModManager.needsort = true;
		}
	}

	public static enum Category {
		COMBAT, MOVEMENT, RENDER, PLAYER, WORLD, NONE;
	}

	public Mod(String name, Category category) {
		this.player = this.mc.thePlayer;
		this.arrowAnlge = 0.0;
		this.animateX = 0.0;
		this.hoverOpacity = 0.0;
		this.name = name;
		this.key = -1;
		this.category = category;
		this.circleCoords = new int[2];
	}

	public void onEnable() {
		if (Client.INSTANCE) {
			if (HUD.sound.getValueState().booleanValue()) {
				Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.2F, 0.6F);
			}
			if (HUD.noti.getValueState().booleanValue()) {
				ClientUtil.sendClientMessage(name + " Enabled", Notification.Type.SUCCESS);
			}
		} else {
			Runtime.getRuntime().exit(-1);
		}
	}

	public void onDisable() {
		if (Client.INSTANCE) {
			if (HUD.sound.getValueState().booleanValue() && !this.name.equalsIgnoreCase("gui")) {
				Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.2F, 0.5F);
			}
			if (HUD.noti.getValueState().booleanValue() && !this.name.equalsIgnoreCase("gui")) {
				ClientUtil.sendClientMessage(name + " Disabled", Notification.Type.ERROR);
			}
		} else {
			Runtime.getRuntime().exit(-1);
		}
	}

	public void onRenderArray() {
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
		//	fr = Client.getInstance().getFontManager().jellolight20;
		}
		
		namewidth = fr.getStringWidth(name);
		
		if (lastY - posY > 0)
			posYRend = 10;
		if (lastY - posY < 0)
			posYRend = -10;
		if (posYRend != 0)
			posYRend = (float) this.getAnimationState(posYRend, 0, 150);
		
		float modwidth = fr.getStringWidth(
				getName() + ((getDisplayName() != null) ? getDisplayName() + " " : ""));
		
		if (isEnabled) {
			if (posX < modwidth)
				posX = (float) this.getAnimationState(posX, modwidth, modwidth * 4);
		} else {
			if (posX > 0)
				posX = (float) this.getAnimationState(posX, 0, modwidth * 4);
		}

	}

	public void onToggle() {
	}

	public void disableValues() {
	}

	public double getAnimationState(double animation, double finalState, double speed) {
		float add = (float) (RenderUtil.delta * speed);
		if (animation < finalState) {
			if (animation + add < finalState)
				animation += add;
			else
				animation = finalState;
		} else {
			if (animation - add > finalState)
				animation -= add;
			else
				animation = finalState;
		}
		return animation;
	}

	public String getValue() {
		if (Client.INSTANCE) {
			if (this.showValue == null) {
				return "";
			}
			return this.showValue.isValueMode ? this.showValue.getModeAt(this.showValue.getCurrentMode())
					: String.valueOf(this.showValue.getValueState());
		} else {
			Runtime.getRuntime().exit(-1);
		}
		return desc;

	}

	public String getNameWithSuffix() {
		return String.valueOf(this.getName()) + (this.getValue().equalsIgnoreCase("") ? ""
				: new StringBuilder(String.valueOf(this.getValue())).append(" ").toString());
	}

	public void set(boolean state) {
		this.set(state, false);
		Client.getInstance().getFileUtil().saveMods();
	}

	public void set(boolean state, boolean safe) {
		if (Client.INSTANCE) {
			ModManager.needsort = true;
			this.isEnabled = state;
			this.onToggle();
			if (state) {
				EventManager.register(this);
				if (this.mc.theWorld != null) {
					this.onEnable();
				}

			} else {
				EventManager.unregister(this);
				if (this.mc.theWorld != null) {
					this.onDisable();
				}

			}
			if (safe) {
				Client.getInstance().getFileUtil().saveMods();
			}
		} else {
			Runtime.getRuntime().exit(-1);
		}

	}

	public String getName() {
    	if (Client.INSTANCE) {
    		return this.name;
    	} else {
    		Runtime.getRuntime().exit(-1);
    		return "nimasile";
    	}
	}

	public int getKey() {
		return this.key;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Category getCategory() {
		return this.category;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean hasValues() {
		for (Value value : Value.list) {
			String name = value.getValueName().split("_")[0];
			if (!name.equalsIgnoreCase(this.getName()))
				continue;
			return true;
		}
		return false;
	}

	public void toggle() {
		try {
			if (this.isEnabled()) {
				this.set(false);
			} else {
				this.set(true);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	protected static String sigma() throws Exception {
		String hwid = hug(System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME")
				+ System.getProperty("user.name"));
		return hwid;
	}

	private static String hug(String text) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return zerodayisaminecraftcheat(sha1hash);
	}

	private static String zerodayisaminecraftcheat(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; ++i) {
			int halfbyte = data[i] >>> 4 & 15;
			int two_halfs = 0;
			do {
				if (halfbyte >= 0 && halfbyte <= 9) {
					buf.append((char) (48 + halfbyte));
				} else {
					buf.append((char) (97 + (halfbyte - 10)));
				}
				halfbyte = data[i] & 15;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	protected static boolean isProcessRunning(String ProcessName) {
		String line = null;
		StringBuilder sb = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process1 = runtime.exec("tasklist /fi \"IMAGENAME eq " + ProcessName + "*\"");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process1.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().contains("PID");
	}

	protected static boolean isProcessRunningTitle(String ProcessTitle) {
		String line = null;
		StringBuilder sb = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process1 = runtime.exec("tasklist /fi \"windowtitle eq " + ProcessTitle + "*\"");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process1.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().contains("PID");
	}



	protected static String encode(String string) throws Exception {

		String retObj = Base64.getUrlEncoder().encodeToString(sigma().getBytes());
		return ((String) retObj).replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().substring(0, 8)
				+ ((String) retObj).replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().substring(
						((String) retObj).replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().length() - 8,
						((String) retObj).replaceAll("\\d+", "").replaceAll("=", "").toLowerCase().length());
	}
}
