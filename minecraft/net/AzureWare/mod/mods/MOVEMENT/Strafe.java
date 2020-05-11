package net.AzureWare.mod.mods.MOVEMENT;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.mod.ModManager;
import net.AzureWare.utils.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Strafe extends Mod {

	public boolean air = true;
	
    public Strafe() {
        super("Strafe", Category.MOVEMENT);
      }

	@EventTarget
    public void onPreMotion(EventPreMotion event) {
	      if(!mc.thePlayer.onGround) {
	         if(this.mc.gameSettings.keyBindJump.pressed) {
	            PlayerUtil.setSpeed(PlayerUtil.getSpeed());
	         }
	      }

     }
}
