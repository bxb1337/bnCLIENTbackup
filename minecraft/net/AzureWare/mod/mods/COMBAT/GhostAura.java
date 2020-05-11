package net.AzureWare.mod.mods.COMBAT;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class GhostAura extends Mod {
	public GhostAura() {
		super("GhostAura", Category.COMBAT);
		// TODO 自动生成的构造函数存根
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		float range = 4.2f;
		ArrayList<EntityLivingBase> playerlist = new ArrayList<EntityLivingBase>();
		for (Entity entity : mc.theWorld.loadedEntityList) {
			float diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw)) - Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(entity)[0])));
			if (entity instanceof EntityLivingBase && mc.thePlayer.getDistanceToEntity(entity) < range && diff < 100) playerlist.add((EntityLivingBase) entity);
		}
		
		if (playerlist.size() > 0) {
			if (!mc.gameSettings.keyBindAttack.pressed) {
				 mc.gameSettings.keyBindAttack.pressed = true;
			} else {
				 mc.gameSettings.keyBindAttack.pressed = false;
			}
			
		}
	}
}
