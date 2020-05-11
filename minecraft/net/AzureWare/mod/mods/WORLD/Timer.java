package net.AzureWare.mod.mods.WORLD;

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

public class Timer extends Mod{

	private Value<Double> timer = new Value("Timer_Timer", 1.0D, 0.5D, 10.0D);
	
	public Timer() {
		super("Timer", Category.WORLD);
	}
	@EventTarget
	public void onUpdate(EventUpdate e) {
		mc.timer.timerSpeed = timer.getValueState().floatValue();
	}
	
	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
	}
	
}
