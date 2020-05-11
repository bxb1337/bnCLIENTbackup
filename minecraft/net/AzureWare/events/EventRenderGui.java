package net.AzureWare.events;

import com.darkmagician6.eventapi.events.Event;

public class EventRenderGui implements Event {
	public float partialTicks;
	
	public EventRenderGui(float a) {
		this.partialTicks = a;
	}

}
