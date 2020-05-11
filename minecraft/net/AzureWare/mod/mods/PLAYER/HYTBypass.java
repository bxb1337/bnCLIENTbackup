package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import io.netty.buffer.Unpooled;

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

import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class HYTBypass extends Mod {
   TimeHelper delay = new TimeHelper();
   Random random = new Random();
   double state = 0.0D;
   private Value spammerdelay = new Value("HYTBypass_Delay", Double.valueOf(2000.0D), Double.valueOf(500.0D), Double.valueOf(10000.0D), 100.0D);

   public HYTBypass() {
      super("HYTBypass", Category.PLAYER);
   }
	@EventTarget
   public void onUpdate(EventUpdate event) throws IOException {
      new Random();
      if(this.delay.isDelayComplete(((Double)this.spammerdelay.getValueState()).longValue())) {
         ++this.state;
         if(Minecraft.getMinecraft().getNetHandler() != null) {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C17PacketCustomPayload(
            		"AntiCheat", (new PacketBuffer(Unpooled.buffer())).writeString(
            				"{\"base64\":\"ecbeb575677ab9a37410748a5f429f9f\",\"cltitle\":\"\u6211\u7684\u4e16\u754c 1.8.9\",\"isLiquidbounce\":false,\"path\":\"mixins.mcwrapper.json\",\"player\":\"" + 
            		Minecraft.getMinecraft().thePlayer.getName() + "\"}")));
         }

         this.delay.reset();
      }

   }

   public void onDisable() {
      this.state = 0.0D;
      super.onDisable();
   }
}
