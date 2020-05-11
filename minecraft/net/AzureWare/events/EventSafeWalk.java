package net.AzureWare.events;

import com.darkmagician6.eventapi.events.Event;

public class EventSafeWalk implements Event {
   public boolean safe;

   public EventSafeWalk(boolean safe) {
      this.safe = safe;
   }

   public void setSafe(boolean safe) {
      this.safe = safe;
   }
}
