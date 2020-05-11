package net.AzureWare.events;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.events.Event;

import net.minecraft.entity.Entity;
//import net.AzureWare.utils.LBRotateUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class EventPacket implements Event {
	private EventPacketType type;
	public static boolean sendcancel;
	public EntityLivingBase target;
	private boolean cancelled;
	public static boolean recievecancel;
	public Packet packet;
    private boolean isPre;
	private Entity ent;

	public static enum EventPacketType {
	    SEND,  RECEIVE;
	}
	public EventPacket(EventPacketType type, Packet packet) {
	    this.type = type;
	    this.packet = packet;
	    this.sendcancel = false;
	    this.recievecancel = false;
		if (this.packet instanceof C03PacketPlayer) {
			//LBRotateUtil.l(this.packet);
		}
	    
        if (this.packet instanceof S08PacketPlayerPosLook) {
        	EventManager.call(new EventPullback());
        }
	}
	public EventPacket(Entity e){
		this.ent = e;
	}
	public EventPacketType getType() {
		return type;
	}
	public void setType(EventPacketType type) {
		this.type = type;
	}
	public boolean isSendcancel() {
		return sendcancel;
	}
	public void setSendcancel(boolean sendcancel) {
		this.sendcancel = sendcancel;
	}
	public EntityLivingBase getTarget() {
		return target;
	}
	public void setTarget(EntityLivingBase target) {
		this.target = target;
	}
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public boolean isRecievecancel() {
		return recievecancel;
	}
	public void setRecievecancel(boolean recievecancel) {
		this.recievecancel = recievecancel;
	}
	public Packet getPacket() {
		return packet;
	}
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	 public boolean isPre() {
	        return isPre;
	    }
	 
	 public boolean isPost() {
	        return !isPre;
	 }
		public Entity getEntity(){
			return this.ent;
		}
}
