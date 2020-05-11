package net.AzureWare.mod.mods.COMBAT;

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

import net.AzureWare.Client;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.client.main.Main;

public class Reach extends Mod {
	public static Value<Double> reach_reach = new Value("Reach_ComBatRange",4.2d,2.0d,6.0d,.01d);
	public static Value<Double> blockrange = new Value("Reach_BlockRange",4.2d,2.0d,6.0d,.01d);
	public Reach() {
		super("Reach", Category.COMBAT);
	}
	
	public static float getSize() {
		if (!Client.INSTANCE) Runtime.getRuntime().exit(0);
		return reach_reach.getValueState().floatValue();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.setDisplayName("" + getSize());
	}
	
}
