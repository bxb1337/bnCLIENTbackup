package net.AzureWare.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketMotior extends Mod {

	private long time = 0;
	private long lastTime;
	private int times;
	private TimeHelper timer = new TimeHelper();
	public static boolean critical;

	public PacketMotior() {
		super("PacketMotior", Category.WORLD);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (timer.isDelayComplete(1000)) {
			this.setDisplayName("PPS:" + times);
			if (times > 22) {
				PlayerUtil.tellPlayer("\247b[AzureWare]\247c警告！Packet发送数量不正常 已经限制发包!！");
			}

			if (times > 20) {
				critical = false;

			}

			else {

				critical = true;
			}
			times = 0;
			timer.reset();
		}

	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if (e.getPacket() instanceof C03PacketPlayer && !ModManager.getModByName("Fly").isEnabled()) {
			times++;
		}
	}
}
