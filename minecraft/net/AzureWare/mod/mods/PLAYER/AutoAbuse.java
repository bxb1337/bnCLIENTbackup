package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.command.commands.CommandPrefix;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;

public class AutoAbuse extends Mod {
   public static String prefix = "[AzureWare]";
   TimeHelper delay = new TimeHelper();
   double state = 0.0D;
   private Value spammerdelay = new Value("AutoAbuse_Delay", Double.valueOf(2000.0D), Double.valueOf(500.0D), Double.valueOf(10000.0D), 100D);
   int num;
   public AutoAbuse() {
      super("AutoAbuse", Mod.Category.PLAYER);
   }
	@EventTarget
   public void onUpdate(EventUpdate event) {
	  this.setDisplayName("Delay:" + ((Double)this.spammerdelay.getValueState()).doubleValue() + " Times:" + num);
      new Random();
      try {
		CommandPrefix.loadText();
	} catch (IOException e) {
		e.printStackTrace();
	}
      if(this.delay.isDelayComplete(((Double)this.spammerdelay.getValueState()).longValue())) {
         ++this.state;
         num++;
         this.delay.reset();
      }
   }

   public void onDisable() {
	  num = 0;
      this.state = 0.0D;
      super.onDisable();
   }
}
