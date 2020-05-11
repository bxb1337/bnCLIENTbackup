package net.AzureWare.mod.mods.MOVEMENT;

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
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.client.main.Main;

public class Sprint extends Mod {
	private Value<String> mode = new Value("Sprint", "Mode", 0);
	
	public Sprint() {
		super("Sprint", Category.MOVEMENT);
		this.mode.addValue("Single");
		this.mode.addValue("AllDirection");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		boolean canSprint = mc.thePlayer.getFoodStats().getFoodLevel() > 6.0F || mc.thePlayer.capabilities.allowFlying;
		if(this.mode.isCurrentMode("AllDirection") && PlayerUtil.MovementInput() && canSprint) {
			mc.thePlayer.setSprinting(true);
		}
		
		if(this.mode.isCurrentMode("Single") && mc.thePlayer.moveForward > 0.0f && PlayerUtil.MovementInput() && canSprint) {
			mc.thePlayer.setSprinting(true);
		}
	}
	
	@Override
	public void onEnable() {
		mc.thePlayer.setSprinting(false);
		super.onEnable();;
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.setSprinting(false);
		super.onDisable();
	}
}
