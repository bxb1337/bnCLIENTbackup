package net.AzureWare.mod.mods.PLAYER;

import java.util.logging.Logger;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.WORLD.Timer;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2APacketParticles;

public class DevMode extends Mod {
	 boolean faithful = false, watchdog = false, erisium = false;
		public Logger LOGGER = Logger.getLogger(Client.CLIENT_NAME);

	    TimeHelper timer = new TimeHelper();
	    int lastKey;
	   // String mode;
	    
	    
	public DevMode() {
		super("DevMode", Category.PLAYER);
	}

	
	@EventTarget
	public void onPacket(EventPacket ep) {
		if (ep.getPacket() instanceof S08PacketPlayerPosLook) {
			timer.reset();
		}
		if (ep.getPacket() instanceof C03PacketPlayer) {
			ep.isCancelled();
		}	 
              
		  
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
 
	if (timer.isDelayComplete(1350)) {	
      mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX , mc.thePlayer.posY  - 100, mc.thePlayer.posZ , false));
      mc.thePlayer.fallDistance = 100;
        LOGGER.info("In Stop");
	}
	

	}
}
