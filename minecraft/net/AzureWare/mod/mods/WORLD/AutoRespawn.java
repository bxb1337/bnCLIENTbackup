package net.AzureWare.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;

public class AutoRespawn extends Mod
{
    public AutoRespawn() {
        super("Respawn", Mod.Category.WORLD);
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!this.mc.thePlayer.isEntityAlive()) {
            this.mc.thePlayer.respawnPlayer();
        }
    }
}
