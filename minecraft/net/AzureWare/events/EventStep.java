package net.AzureWare.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.EventType;

public class EventStep extends EventCancellable {
    private double stepHeight;
    private double realHeight;
    private boolean active;
    private boolean pre;
    
    public EventStep(EventType eventType, float height) {
        this.eventType = eventType;
        this.height = height;
    }

    public EventStep(boolean state, double stepHeight, double realHeight) {
        this.pre = state;
        this.stepHeight = stepHeight;
        this.realHeight = realHeight;
    }

    public EventStep(boolean state, double stepHeight) {
        this.pre = state;
        this.realHeight = stepHeight;
        this.stepHeight = stepHeight;
    }

    public boolean isPre() {
        return this.pre;
    }

    public double getStepHeight() {
        return this.stepHeight;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }

    public void setActive(boolean bypass) {
        this.active = bypass;
    }

    public double getRealHeight() {
        return this.realHeight;
    }

    public void setRealHeight(double realHeight) {
        this.realHeight = realHeight;
    }


   private float height;
   private EventType eventType;


   public float getHeight() {
       return height;
   }

   public void setHeight(float height) {
       this.height = height;
   }

   public EventType getEventType() {
       return eventType;
   }
}

