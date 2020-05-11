package net.AzureWare.mod.mods.COMBAT;

import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.item.ItemSword;

public class AutoClicker extends Mod {
	
	public Value<Double> cpsMax = new Value("AutoClicker_MaxCPS", 10d, 1d, 20d, 1d);
	public Value<Double> cpsMin = new Value("AutoClicker_MinCPS", 10d, 1d, 20d, 1d);
	public Value<Boolean> press = new Value("AutoClicker_Press", true);
	public Value<Boolean> sword = new Value("AutoClicker_OnlySword", true);
	public Value<Boolean> fadein = new Value("AutoClicker_FadeIn", true);
	public TimeHelper timer = new TimeHelper();
	public TimeHelper fadetimer = new TimeHelper();
	public int cps = 0;
	public AutoClicker() {
		super("AutoClicker", Category.COMBAT);
		// TODO 自动生成的构造函数存根
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (fadetimer.isDelayComplete(1000)) {
			cps = cpsMax.getValueState().intValue() - 
					Math.abs(cpsMax.getValueState().intValue() - cpsMin.getValueState().intValue()) + 
					new Random().nextInt(Math.abs(cpsMax.getValueState().intValue() - cpsMin.getValueState().intValue()));
		}else if (cps < cpsMax.getValueState()){
			cps += 1;
		}
		
		long delay = (long) (1000 / cps);
		if (timer.isDelayComplete(delay) && 
			!(press.getValueState() && !mc.gameSettings.keyBindAttack.pressed) && 
			!(sword.getValueState() && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))) {
			mc.clickMouse();
			timer.reset();
		}
	}
	
	public void onEnable() {
		fadetimer.reset();
		cps = 0;
		super.onEnable();
	}
}
