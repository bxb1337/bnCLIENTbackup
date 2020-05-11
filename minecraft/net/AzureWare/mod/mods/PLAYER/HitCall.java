package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.minecraft.network.play.client.C0EPacketClickWindow;

public class HitCall extends Mod {
	public static C0EPacketClickWindow packet1;

	public HitCall() {
		super("HitCall", Category.PLAYER);
		// TODO 自动生成的构造函数存根
	}

	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.getPacket() instanceof C0EPacketClickWindow && packet1 == null) {
			packet1 = (C0EPacketClickWindow) event.getPacket();
			new threadCall().start();
		}
	}
	
	public void onDisable() {
		packet1 = null;
		super.onDisable();
	}
	
	public void onEnable() {
		packet1 = null;
		super.onEnable();
	}
	
	class threadCall extends Thread {
		public void run() {
			while(packet1 != null) {
				try {
					sleep(20);
					mc.thePlayer.sendQueue.addToSendQueue(packet1);
				} catch (Throwable e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			ModManager.getModByName("HitCall").set(false);
		}
	}
}
