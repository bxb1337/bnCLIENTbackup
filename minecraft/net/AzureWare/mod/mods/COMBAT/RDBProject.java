package net.AzureWare.mod.mods.COMBAT;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
//import net.AzureWare.utils.RDBProject.RotationData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class RDBProject extends Mod {

	public class RotationData {

	}

	float lastYaw;
	ArrayList<Float> data = new ArrayList<Float>();
	TimeHelper timer = new TimeHelper();
	
	public RDBProject() {
		super("RDBProject", Category.COMBAT);
		// TODO 自动生成的构造函数存根
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {

		float diffYaw = mc.thePlayer.rotationYaw - lastYaw;
		lastYaw = mc.thePlayer.rotationYaw;
		if (diffYaw != 0f) {
			data.add(diffYaw);
			//PlayerUtil.tellPlayer(diffYaw + "");
		}
		
		for (Entity entity : mc.theWorld.loadedEntityList) {
			if (entity instanceof EntityLivingBase && mc.thePlayer.getDistanceToEntity(entity) < 5) {
				EntityLivingBase ent = (EntityLivingBase) entity;
				if (mc.gameSettings.keyBindAttack.pressed && timer.isDelayComplete(200)) {
					String str = "rdbs.add(new RotationData(new float[] {";
					float f2 = 0;
					for (float f : data) {
						str = str + f + "f, ";
						f2 += f;
					}
					str = str.substring(0, str.length() - 2);
					str = str + "})); //" + f2;
					PlayerUtil.tellPlayer(f2 + "");
					System.out.println(str);
					data.clear();
					timer.reset();
				}
			}
		}
	}

}
