package net.AzureWare.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;

public class AutoWalk extends Mod {
   public AutoWalk() {
      super("AutoWalk", Category.MOVEMENT);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      this.mc.gameSettings.keyBindForward.pressed = true;
   }

   public void onDisable() {
      super.onDisable();
      this.mc.gameSettings.keyBindForward.pressed = false;
   }
}
