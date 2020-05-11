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
import net.AzureWare.utils.Colors;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.util.EnumParticleTypes;

public class Particles extends Mod {

	
	
	public static Value mode = new Value("Particles", "Logo", 0);

	
	
	public Particles() {
		super("Particles", Category.RENDER);
		mode.addValue("Hearts");
		mode.addValue("Cloud");
		mode.addValue("Redstone");
		mode.addValue("SwordArt");
		mode.addValue("EXPLOSION");
		mode.addValue("Magic");

		mode.addValue("Flame");
		mode.addValue("Smoke");

	}

	@EventTarget
	   public void onUpdate(EventUpdate event) {
	      if(mode.isCurrentMode("Hearts")) {
	    	  if (mc.thePlayer.isMoving()) {

	         mc.theWorld.spawnParticle(EnumParticleTypes.HEART, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	      }
	      }
	      
	      if(mode.isCurrentMode("EXPLOSION")) {
	    	  if (mc.thePlayer.isMoving()) {

		         mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		         mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, mc.thePlayer.posX, mc.thePlayer.posY + 10.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		         mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, mc.thePlayer.posX * 0.1d, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ *0.1D , 0.0D, 0.0D, 0.0D, new int[0]);
	    	  }
	      }
	      
	      
	      if(mode.isCurrentMode("SwordArt")) {
	    	  if (mc.thePlayer.isMoving()) {
		         mc.theWorld.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, mc.thePlayer.posX, mc.thePlayer.posY + 10.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);

		         mc.theWorld.spawnParticle(EnumParticleTypes.CRIT, mc.thePlayer.posX , mc.thePlayer.posY + 1.4D, mc.thePlayer.posZ , 0.0D, 0.0D, 0.0D, new int[0]);
		         mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		         mc.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, mc.thePlayer.posX, mc.thePlayer.posY + 5.0d , mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		        // mc.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		         mc.theWorld.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	    	  }
	    	  
	    	  
	      }
	      if(mode.isCurrentMode("Magic")) {
	    	  if (mc.thePlayer.isMoving()) {

		         mc.theWorld.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		      }
	      }
	      
	      if(mode.isCurrentMode("Cloud")) {
	    	  if (mc.thePlayer.isMoving()) {

	         mc.theWorld.spawnParticle(EnumParticleTypes.CLOUD, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	      }
	      }
	      if(mode.isCurrentMode("Redstone")) {
	    	  if (mc.thePlayer.isMoving()) {

	         mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.1D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	         mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	         mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.3D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	         mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.4D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	         mc.theWorld.spawnParticle(EnumParticleTypes.REDSTONE, mc.thePlayer.posX, mc.thePlayer.posY + 0.5D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	      }
	      }
	      if(mode.isCurrentMode("Flame")) {
	    	  if (mc.thePlayer.isMoving()) {

	         mc.theWorld.spawnParticle(EnumParticleTypes.FLAME, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	      }
	      }
	      if(mode.isCurrentMode("Smoke")) {
	    	  if (mc.thePlayer.isMoving()) {

	         mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_LARGE, mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	      }
	      }
	   }
	}


