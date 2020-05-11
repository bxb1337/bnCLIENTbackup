package net.AzureWare.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class DuelGod extends Mod {
	public TimeHelper timer = new TimeHelper();

	public DuelGod() {
		super("DuelGod", Category.COMBAT);
		// TODO 自动生成的构造函数存根
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (Killaura.target != null && mc.thePlayer.hurtTime == 1 && mc.thePlayer.onGround) {
			damagePlayer(1);
		}
	}
	
	public void damagePlayer(int damage) {
		if (damage < 1)
			damage = 1;
		if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
			damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

		double offset = 0.0625;
		if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
			for (int i = 0; i <= ((3 + damage) / offset); i++) { // TODO: teach rederpz (and myself) how math works
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
			}
		}
	}
}
