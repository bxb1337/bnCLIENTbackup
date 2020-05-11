package net.AzureWare.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.AzureWare.Client;
import net.AzureWare.events.EventStep;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Arrays;
import java.util.List;

public class Step extends Mod {
	public Value<String> mode = new Value<String>("Step", "Mode", 0);
  //  public Value<Boolean> time = new Value<>("Step_Timer", false);
	public Value<Double> height = new Value<Double>("Step_Height", 1.0D, 1.0D, 2.0D, 0.5D);
	public Value<Double> delay = new Value<Double>("Step_Delay", 0.0D, 0.0D, 1000.0D, 50.0D);
	TimeHelper timer = new TimeHelper();
	boolean resetTimer;

	public Step() {
		super("Step", Category.MOVEMENT);
		mode.addValue("Vanilla");
		mode.addValue("AAC");

		mode.addValue("NCP");
	}

	@Override
	public void onEnable() {
		resetTimer = false;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		if (mc.thePlayer != null) {
			mc.thePlayer.stepHeight = 0.5F;
		}
		mc.timer.timerSpeed = 1.0F;
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
		if ((mc.timer.timerSpeed < 1 && mc.thePlayer.onGround)) {
			mc.timer.timerSpeed = 1;
		}
	}

	@EventTarget
	public void onStep(EventStep event) {
		if (!PlayerUtil.isInWater()) {
			if (event.getEventType() == EventType.PRE) {
				if (!mc.thePlayer.onGround || !timer.isDelayComplete(delay.getValueState().longValue())) {
					mc.thePlayer.stepHeight = 0.5F;
					event.setHeight(0.5F);
					return;
				}
				mc.thePlayer.stepHeight = height.getValueState().floatValue();
				event.setHeight(height.getValueState().floatValue());
			} else {
				if (mode.isCurrentMode("NCP") && event.getHeight() > 0.5) {
					double height = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
					if (height >= 0.625) {
						ncpStep(height);
						timer.reset();
					}
				}
			}
		}
	}

	void ncpStep(double height) {
		List<Double> offset = Arrays.asList(0.42, 0.333, 0.248, 0.083, -0.078);
		double posX = mc.thePlayer.posX;
		double posZ = mc.thePlayer.posZ;
		double y = mc.thePlayer.posY;
		if (height < 1.1) {
			double first = 0.42;
			double second = 0.75;
			if (height != 1) {
				first *= height;
				second *= height;
				if (first > 0.425) {
					first = 0.425;
				}
				if (second > 0.78) {
					second = 0.78;
				}
				if (second < 0.49) {
					second = 0.49;
				}
			}
			if (first == 0.42)
				first = 0.41999998688698;
			mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
			if (y + second < y + height)
				mc.thePlayer.sendQueue
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
			return;
		} else if (height < 1.6) {
			for (int i = 0; i < offset.size(); i++) {
				double off = offset.get(i);//
				y += off;
				mc.thePlayer.sendQueue
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
			}
		} else if (height < 2.1) {
			double[] heights = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869 };
			for (double off : heights) {
				mc.thePlayer.sendQueue
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
			}
		} else {
			double[] heights = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
			for (double off : heights) {
				mc.thePlayer.sendQueue
						.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
			}
		}

	}
}