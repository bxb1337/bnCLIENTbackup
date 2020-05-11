package net.AzureWare.mod.mods.RENDER;

import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.PLAYER.AimBot;
import net.AzureWare.value.Value;

public class AntiAim extends Mod {

	public AntiAim() {
		super("AntiAim", Category.WORLD);
		// TODO 自动生成的构造函数存根
	}

	public Value<Boolean> render = new Value<Boolean>("AntiAim_Render", true);
	public static Value<Boolean> renderEntity = new Value<Boolean>("AntiAim_RenderEntity", true);
	public static Value<Double> Speed = new Value("AntiAim_Speed", 0.2d, 0.0d, 1.0d, 0.01d);

	private Value<Boolean> YawBoolean = new Value<Boolean>("AntiAim_Yaw", true);
	private Value<Boolean> PitchBoolean = new Value<Boolean>("AntiAim_Pitch", true);

	public static Value<Double> Pitch = new Value("AntiAim_Pitch", 180d, 0d, 720d, 10d);
	public static Value<Double> Yaw = new Value("AntiAim_Yaw", 180d, 0d, 720d, 10d);

	@EventTarget
	public void onUpdate(EventUpdate e) {
		Random rand = new Random();
		// mc.thePlayer.setRotationYawHead(rand.nextInt(360));
		if (render.getValueState()) {
			mc.thePlayer.rotationYawHead = rand.nextInt(360);
			mc.thePlayer.renderYawOffset = rand.nextInt(360);
		}

	}

	@EventTarget
	public void onUpdate(EventPreMotion e) {
		Random rand = new Random();
		if (!ModManager.getModByName("Killaura").isEnabled()) {
			if (!render.getValueState()) {
				mc.thePlayer.rotationYawHead = e.getYaw();
			//	mc.thePlayer.rotationYawHead = e.getYaw();
				mc.thePlayer.renderYawOffset = rand.nextInt(rand.nextInt(Yaw.getValueState().intValue()));
		//		mc.thePlayer.rotationPitchHead = e.pitch;
			//	mc.thePlayer.renderYawOffset = e.getYaw();
		//		mc.thePlayer.renderYawOffset = e.yaw;
				mc.thePlayer.rotationPitchHead = e.getPitch();
				mc.thePlayer.renderArmPitch = e.getPitch();
			}
			if (!render.getValueState() && AimBot.aimed == null) {
			//	mc.thePlayer.renderYawOffset = e.yaw;
			//	mc.thePlayer.rotationPitchHead = e.pitch;
				//mc.thePlayer.prevRenderArmPitch = e.getPitch();
				if (YawBoolean.getValueState()) {
					e.setYaw(rand.nextInt(Yaw.getValueState().intValue()));

				}
				if (PitchBoolean.getValueState()) {
					e.setPitch(rand.nextInt(Pitch.getValueState().intValue()));

				}

			}
			mc.thePlayer.rotationYawHead = e.getYaw();
			mc.thePlayer.rotationPitchHead = e.getPitch();


		}
	}
}
