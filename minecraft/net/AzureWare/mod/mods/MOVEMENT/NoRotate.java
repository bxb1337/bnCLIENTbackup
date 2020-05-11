package net.AzureWare.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.mod.Mod;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Mod {
    public NoRotate() {
		super("NoRotate", Category.MOVEMENT);
		// TODO 自动生成的构造函数存根
	}

	@EventTarget
    private void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook look = (S08PacketPlayerPosLook)e.getPacket();
            look.yaw = this.mc.thePlayer.rotationYaw;
            look.pitch = this.mc.thePlayer.rotationPitch;
        }
    }

}
