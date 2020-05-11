package net.AzureWare.events;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.events.Event;

import net.AzureWare.events.EventPacket.EventPacketType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class EventAttack implements Event {
	private Entity ent;
	
	private EventPacketType type;
	public static boolean sendcancel;
	public EntityLivingBase target;
	private boolean cancelled;
	public static boolean recievecancel;
	public Packet packet;

    
	public Entity getEntity(){
		return this.ent;
	}
	
	public EventAttack(Entity e){
		this.ent = e;
	}
	
	public static enum EventPacketType {
	    SEND,  RECEIVE;
	}
	public EventAttack(EventPacketType type, Packet packet) {
	    this.type = type;
	    this.packet = packet;
	}

	
	public Packet getPacket() {
		return packet;
	}
	public void setPacket(Packet packet) {
		this.packet = packet;
	}


}
