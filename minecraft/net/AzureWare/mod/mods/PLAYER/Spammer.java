package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.command.commands.CommandPrefix;
import net.AzureWare.command.commands.CommandSpammer;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import io.netty.buffer.Unpooled;
import net.minecraft.Wrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.swing.JOptionPane;


public class Spammer extends Mod {
   public static String text = "AzureWare Hacked Client Code by Margele.";
   public static String prefix = "[AzureWare]";
   TimeHelper delay = new TimeHelper();
   Random random = new Random();
   double state = 0.0D;
   private Value spammerdelay = new Value("Spammer_Delay", Double.valueOf(2000.0D), Double.valueOf(500.0D), Double.valueOf(10000.0D), 100D);
   private Value randomstring = new Value<Boolean>("Spammer_RandomString", true);
   private int num;
   public Spammer() {
      super("Spammer", Category.PLAYER);
   }
	@EventTarget
   public void onUpdate(EventUpdate event){
	  this.setDisplayName("Delay:" + ((Double)this.spammerdelay.getValueState()).doubleValue() + " Times:" + num);
	  try {
		CommandSpammer.loadText();
		CommandPrefix.loadText();
	} catch (IOException e) {
		e.printStackTrace();
	}
      new Random();
      if(this.delay.isDelayComplete(((Double)this.spammerdelay.getValueState()).longValue())) {
         ++this.state;
         num++;
         String message = prefix + text;
         if(((Boolean)this.randomstring.getValueState()).booleanValue()) {
        	 message = message + " >" + new StringRandom().getStringRandom(6) + "<";
         }
         this.mc.thePlayer.sendChatMessage(message);
         this.delay.reset();
      }
   }

   public void onDisable() {
      this.state = 0.0D;
      num = 0;
      super.onDisable();
   }
   
   public class StringRandom {
	    public String getStringRandom(int length) {
	        
	        String val = "";
	        Random random = new Random();
	        
	        for(int i = 0; i < length; i++) {
	            
	            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
	            if( "char".equalsIgnoreCase(charOrNum) ) {
	                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
	                val += (char)(random.nextInt(26) + temp);
	            } else if( "num".equalsIgnoreCase(charOrNum) ) {
	                val += String.valueOf(random.nextInt(10));
	            }
	        }
	        return val;
	    }
   }
   }
