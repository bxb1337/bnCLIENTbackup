package net.AzureWare.mod.mods.RENDER;

import net.AzureWare.mod.Mod;
import net.AzureWare.value.Value;

public class Chams extends Mod {

	//public static Value<Boolean> color = new Value("Chams_Apply", false);
	
	public static Value<Boolean> invisable = new Value("Chams_Apply", false);
	public static Value<Boolean> animals = new Value("Chams_Animal", false);
	public static Value<Boolean> monster = new Value("Chams_Mob", false);
	public static Value<Boolean> emt = new Value("Chams_Entity", false);

	public static Value<Double> red = new Value("Chams_Red", 0.0d, 0.0d, 255.0d, 1.0d);
	public static Value<Double> green = new Value("Chams_Green", 0.0d, 0.0d, 255.0d, 1.0d);
	public static Value<Double> blue = new Value("Chams_Blue", 0.0d, 0.0d, 255.0d, 1.0d);
	//public static Value<Double> alpha = new Value("Chams_AlphaBh", 0.0d, 0.0d, 1.0d, 1.0d);

	public Chams() {
		super("Chams", Category.RENDER);
		// TODO 自动生成的构造函数存根
	}
}
