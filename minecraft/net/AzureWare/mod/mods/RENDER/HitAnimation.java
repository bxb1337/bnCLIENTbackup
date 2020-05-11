package net.AzureWare.mod.mods.RENDER;

import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.value.Value;

public class HitAnimation extends Mod {
	public static Value<String> mode = new Value("HitAnimation", "Mode", 0);
	public static Value<Boolean> NoFire = new Value("HitAnimation_NoFire", false);
	public static Value<Boolean> Smooth = new Value("HitAnimation_Smooth", false);
	public static Value<Boolean> EveryThingBlock = new Value("HitAnimation_EveryThingBlock", false);
    public static Value<Double> x = new Value<Double>("HitAnimation_x",  0.0, -1.0, 1.0, 0.1);
    public static Value<Double> y = new Value<Double>("HitAnimation_y",  0.0, -1.0, 1.0, 0.1);
    public static Value<Double> z = new Value<Double>("HitAnimation_z",  0.0, -1.0, 1.0, 0.1);
	public static Value<Double> Speed = new Value<Double>("HitAnimation_Speed",  10.0, 1.0, 50.0, 1.0);	
    public static Value<Boolean> Eliminates = new Value<Boolean>("HitAnimation_Eliminates",false);
    public static Value<Double> HurtTime = new Value<Double>("HitAnimation_HurtTime",  6.0, 0.0, 10.0, 1.0);
	public HitAnimation() {
		super("HitAnimation", Category.RENDER);
		this.mode.addValue("Swang");
		this.mode.addValue("Swank");
		this.mode.addValue("Swing");
		this.mode.addValue("Swong");
		this.mode.addValue("Remix");
		this.mode.addValue("SwAing");
		this.mode.addValue("Custom");
		this.mode.addValue("None");
		this.mode.addValue("Old");
		this.mode.addValue("Gay");
		this.mode.addValue("Punch");
		this.mode.addValue("rotate");
		this.mode.addValue("Winter");
		this.mode.addValue("circle");
	}

}
