package net.AzureWare.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.network.Packet;

public class EventReceivePacket extends EventCancellable {
	private Packet packet;
    private EventType type;
    public double x;
    public double y;
    public double z;
    
    public EventReceivePacket(Packet packet, EventType type) {
        this.packet = packet;
        this.type = type;
    }

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
    
}
