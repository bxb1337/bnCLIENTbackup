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

import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.PLAYER.Spammer.StringRandom;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Mod {
   private Value packet = new Value("Regen_Packets", Double.valueOf(10.0D), Double.valueOf(1.0D), Double.valueOf(1000.0D), 1.0D);
   private TimeHelper delay = new TimeHelper();
   private Value<Double> regendelay = new Value("Regen_Delay", Double.valueOf(500.0D), Double.valueOf(0.0D), Double.valueOf(10000.0D), 100D);
   
   public Regen() {
      super("Regen", Category.WORLD);
      this.showValue = this.packet;
   }
   @EventTarget
   public void onMotion(EventPreMotion event) {
	   if(delay.isDelayComplete(regendelay.getValueState().intValue())) {
		   if(!ModManager.getModByName("Fly").isEnabled()) {
			   if(!(this.mc.thePlayer.fallDistance > 2.0F)) {
			      if(this.mc.thePlayer.getHealth() < this.mc.thePlayer.getMaxHealth() && this.mc.thePlayer.getFoodStats().getFoodLevel() >= 19) {
					   if(this.mc.thePlayer.onGround) {
					    	  for(int i = 0; (double)i < ((Double)this.packet.getValueState()).doubleValue(); ++i) {
					    		  if(this.mc.thePlayer.onGround) {
					    			  this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
					    			  this.setDisplayName("Health:" + this.mc.thePlayer.getHealth());
					    			  delay.reset();
					    		  }else {
					    			  this.setDisplayName("OtherNoGround");
					    		  }
						      }
					   }else {
						   this.setDisplayName("NoGround");
					   }
			       }else {
			    	   this.setDisplayName("MaxHealth");
			       }
			   }else {
				   this.setDisplayName("Falling");
			   }
		   }else {
			   this.setDisplayName("Flying");
		   }
	   }
   }
}
