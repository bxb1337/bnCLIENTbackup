package net.AzureWare.mod.mods.WORLD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventChat;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.mod.mods.PLAYER.AutoAbuse;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;

public class AutoL extends Mod {

	public static TimeHelper LTimer = new TimeHelper();
	public Value<Boolean> ad = new Value("AutoL_AD", true);
	public Value<Boolean> wdr = new Value("AutoL_WatchdogReport", true);
	public Value<Boolean> abuse = new Value("AutoL_Abuse", false);
	private static Value mode = new Value("AutoL", "Mode", 0);

	public static List<String> wdred = new ArrayList();

	public AutoL() {
		super("AutoL", Category.WORLD);
		mode.addValue("Chinese");
		mode.addValue("English");
		mode.addValue("Japanese");
		mode.addValue("French");
		mode.addValue("Russia1");
		mode.addValue("Russia2");


	}

	@EventTarget
	public void onChat(EventChat e) {
		try {
			if (e.getMessage().contains(mc.thePlayer.getName()) && e.getMessage().contains(Killaura.target.getName())
					&& LTimer.isDelayComplete(1000)) {
				if (mode.isCurrentMode("Chinese")) {
					mc.thePlayer.sendChatMessage(AutoAbuse.prefix + Killaura.target.getName() + " L "
							
							+ (ad.getValueState() ? " Buy AzureWare At mcheika.com" : ""));
				}
				
				if (mode.isCurrentMode("English")) {
					mc.thePlayer.sendChatMessage(AutoAbuse.prefix + Killaura.target.getName() + " L "
							
							+ (ad.getValueState() ? " Buy AzureWare At mcheika.com" : ""));
				}
				if (mode.isCurrentMode("Japanese")) {
					mc.thePlayer.sendChatMessage(AutoAbuse.prefix + Killaura.target.getName() + " ÒÛÁ¢¤¿¤º "
							
							+ (ad.getValueState() ? " Buy AzureWare At mcheika.com" : ""));
				}
				if (mode.isCurrentMode("Russia1")) {
					mc.thePlayer.sendChatMessage(AutoAbuse.prefix + Killaura.target.getName() + " Cyka Blyat"
							
							+ (ad.getValueState() ? " Buy AzureWare At mcheika.com" : ""));
				}
				if (mode.isCurrentMode("Russia2")) {
					mc.thePlayer.sendChatMessage(AutoAbuse.prefix + Killaura.target.getName() + " §³§å§Ü§Ñ §Ò§Ý§ñ§Õ§îe"
							
							+ (ad.getValueState() ? " Buy AzureWare At mcheika.com" : ""));
				}
				if (mode.isCurrentMode("French")) {
					mc.thePlayer.sendChatMessage(AutoAbuse.prefix + Killaura.target.getName() + " Merde"
							
							+ (ad.getValueState() ? " Buy AzureWare At mcheika.com" : ""));
				}

				if (wdr.getValueState() && !wdred.contains(Killaura.target.getName())) {
					wdred.add(Killaura.target.getName());
					Minecraft.getMinecraft().thePlayer
							.sendChatMessage("/wdr " + Killaura.target.getName() + " ka fly reach nokb jesus ac");
				}
				LTimer.reset();
			}
		} catch (Throwable e1) {

		}

	}

}
