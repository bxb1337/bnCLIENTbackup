package net.AzureWare.events;

import com.darkmagician6.eventapi.events.Event;

public class EventChat implements Event {
	
	public String message;
	public boolean cancelled;
	
	public EventChat(String chat) {
		if(chat.contains("[Margele-IRC]")) {
			message = "Éµ±Æ";
		}else {
			message = chat;
		}
	}

	public String getMessage() {
		return message;
	}
	
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}
