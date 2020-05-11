package net.AzureWare.events;

import com.darkmagician6.eventapi.events.Event;

public class EventJump implements Event{
    private double motionY;
    private boolean pre;
    
    public double getMotionY() {
        return motionY;
    }
    public void setMotionY(double motiony) {
        this.motionY = motiony;
    }

    public boolean isPre() {
        return pre;
    }

    public boolean isPost() {
        return !pre;
    }

}
