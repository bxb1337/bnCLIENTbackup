package net.AzureWare.mod.mods.RENDER;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.swing.JOptionPane;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Mod {
	private Value<String> mode = new Value("Fullbright", "Mode", 0);
	
	public float oldGammaSetting;
	
	public Fullbright() {
		super("Fullbright", Category.RENDER);
		this.mode.addValue("Gamma");
		this.mode.addValue("Potion");
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(this.mode.isCurrentMode("Gamma")) {
			this.oldGammaSetting = mc.gameSettings.gammaSetting;
			mc.gameSettings.gammaSetting = 1000.0f;
		}
		if(this.mode.isCurrentMode("Potion")) {
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 4000, 1));
		}
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = this.oldGammaSetting;
		super.onDisable();
	}

}
