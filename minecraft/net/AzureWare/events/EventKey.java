package net.AzureWare.events;

import com.darkmagician6.eventapi.events.Event;

public class EventKey implements Event {
	public int key;
	
	public EventKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
}
