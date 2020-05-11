package net.AzureWare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.Display;

import com.darkmagician6.eventapi.EventManager;

import net.AzureWare.command.CommandManager;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.ui.clickgui.awui.ClickGui;
import net.AzureWare.utils.FileUtil;
//import net.AzureWare.utils.RDBProject.RDBManager;
import net.AzureWare.utils.fontmanager.FontManager;
import net.minecraft.Wrapper;
import net.minecraft.client.main.Main;

public class Client {
	public static boolean INSTANCE = false;
	public Logger LOGGER = Logger.getLogger(CLIENT_NAME);
	public static Client instance;
	public static String CLIENT_NAME = "AzureWare";
	public static String CLIENT_VERSION = "Reborn R3";
	// public static String CLIENT_Beta = "Happy New Year :D ---Zeta 0.6";
	public static String CLIENT_O = "This Client Owned By Margele & Theresa";
	public static String Helper = "Special Help From Kody";

	public static String Yiyan;
	public static String Server = "Vanilla";
	public ModManager modMgr;
//	public RDBManager rotationMgr;
	public EventManager eventMgr;
	public FileUtil fileMgr;
	private FontManager fontManager;
	private ClickGui clickface;
	public CommandManager cmdMgr;

	public static boolean isTestUser = false;
	public static String prefix = "\247a[USER]\247r";
	public static boolean isDebugMode = false;
	public static boolean isMod = false;
	public static String path;
	public static String IRCName;
	public static String playerName;
	public static String playingGame = "NoGames";
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();
	// final String text = "×Ö´®ÎÄ×Ö";
	// final byte[] textByte = text.getBytes("UTF-8");

	public static boolean validatePassword(String origin, String inputString, String charsetname) {
		if (origin.equals(MD5Encode(inputString, charsetname).toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}



	public Client() {

		System.out.println(isDebugMode);
		// isDebugMode = true;

		instance = this;

		fontManager = new FontManager();

		cmdMgr = new CommandManager();

		modMgr = new ModManager();
		INSTANCE = true;

		clickface = new ClickGui();

		fileMgr = new FileUtil();
		// rotationMgr = new RDBManager();

		eventMgr = new EventManager();
		// if ((runningconfig() +
		// runningconfig2()).toLowerCase().contains("xbootclasspath") ||
		// (runningconfig() + runningconfig2()).toLowerCase().contains("javaagent"))
		// System.exit(status);

		Display.setTitle(CLIENT_NAME + " " + CLIENT_VERSION);
	}

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setDoOutput(true);
			connection.setReadTimeout(99781);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			Map<String, List<String>> map = connection.getHeaderFields();
			for (String key : map.keySet()) {
			}
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
		return result;
	}


	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digestString = md.digest(resultString.getBytes(charsetname));
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(digestString);
			else
				resultString = byteArrayToHexString(digestString);
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		String byteToHex = null;
		for (int i = 0; i < b.length; i++) {
			byteToHex = byteToHexString(b[i]);
			resultSb.append(byteToHex);
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	public void stop() {
		// LOGGER.info(CLIENT_NAME + " Shutting down...");
	}

	public static Client getInstance() {
		return instance;
	}

	public FileUtil getFileUtil() {
		return this.fileMgr;
	}

	public EventManager getEventMgr() {
		return this.eventMgr;
	}

	public FontManager getFontManager() {
		return this.fontManager;
	}

	public ClickGui getClickInterface() {
		return this.clickface;
	}
}
