package net.AzureWare.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventAttackEntity;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.value.Value;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Superknockback extends Mod {
	
	public static Value<Double> packets = new Value("Superknockback_Packets", 5.0D, 2.0D, 10.0D, 1.0D);
	private EntityPlayer entity;
	
	public Superknockback() {
		super("Superknockback", Category.COMBAT);
	}
	
	@EventTarget
	public void onAttack(EventAttackEntity e) {
		if (mc.thePlayer.getDistanceToEntity(e.target) <= 1.0f || mc.thePlayer.getEntityBoundingBox().intersectsWith(e.target.getEntityBoundingBox())) {
            int i = 0;
            while ((double)i < this.packets.getValueState() * 10.0d) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
                ++i;
            }
        }
	}

}
