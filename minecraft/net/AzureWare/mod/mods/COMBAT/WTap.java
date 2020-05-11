package net.AzureWare.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.mod.Mod;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class WTap extends Mod {

	public WTap() {
		super("WTap", Category.COMBAT);
	}
	
	@EventTarget
	public void onUpdate(EventPacket event) {
		if (mc.theWorld != null && mc.thePlayer != null) {
			if(event.packet instanceof C02PacketUseEntity) {
				C02PacketUseEntity packet = (C02PacketUseEntity) event.packet;
				if ((packet.getAction() == C02PacketUseEntity.Action.ATTACK) && (packet.getEntityFromWorld(mc.theWorld) != mc.thePlayer) && (mc.thePlayer.getFoodStats().getFoodLevel() > 6)) {
					boolean sprint = mc.thePlayer.isSprinting();
					mc.thePlayer.setSprinting(false);
					mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
					mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
					mc.thePlayer.setSprinting(sprint);
				}
			}
		}
	}
	
}
