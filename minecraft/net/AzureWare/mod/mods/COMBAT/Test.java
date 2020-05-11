package net.AzureWare.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventMotionUpdate;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.mod.ModManager;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.value.Value;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;

public class Test extends Mod {
	public static Value<Double> hurttime = new Value("Test_HurtTime",15d,0d,20d,1d);

	public Value<Boolean> Debug_Critical = new Value<Boolean>("Test_CriticalTest", false);
	public Value<Boolean> Debug = new Value<Boolean>("Test_Bhop", false);
	public Value<Boolean> Debug_0 = new Value<Boolean>("Test_Deta", false);

	public Test() {
		super("Test", Category.COMBAT);
	}
	
	
	@EventTarget
	public void onUpdate(EventMotionUpdate e) {
		if (Debug_0.getValueState()) {
			
		 if(mc.thePlayer.isBlocking() && mc.thePlayer.moving()) {
             if(e.isPre()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
             }

             if(e.isPost()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
             }
          }
		}
	
	}
	@EventTarget
	public void onPacket(EventPacket event) {
		if (Debug_Critical.getValueState()) {
			final C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
	        if ((packet.getAction() == C02PacketUseEntity.Action.ATTACK ) ) {
	            if (ModManager.getModByName("Killaura").isEnabled()) {
	            	this.setDisplayName(" Packet");
	            	  double[] arrd = new double[]{0.061429999876543D + ExhiRandom(-9999, 9999) / 10000000, 0.0D, 0.01251100099987654D+ ExhiRandom(-9999, 9999) / 10000000, 0.0D};
	 		         int n = arrd.length;

	 		         for(int n2 = 0; n2 < n; ++n2) {
	 		            double offset = arrd[n2];
	 		            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offset, this.mc.thePlayer.posZ, true));
	 		         
	 		      }

	            }
	          
	        }
	
}
	}
	
	
    private static int randomNumber(int max, int min) {
        return (int)(Math.random() * (double)(max - min)) + min;
     }
    private static int ExhiRandom(final int n, final int n2) {
        return (int)(Math.random() * (n - n2)) + n2;
    }
    private static int GetRandomNumber(final int n, final int n2) {
        return (int)(Math.random() * (n - n2)) + n2;
    }
}

