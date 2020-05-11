package net.AzureWare.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.value.Value;

public class Hitbox extends Mod {
	public static Value<Double> size = new Value("Hitbox_Size",0.2d,0.0d,1.0d,0.01d);
	
	public Hitbox() {
		super("Hitbox", Category.COMBAT);
	}
	
	public static float getSize() {
		return size.getValueState().floatValue();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.setDisplayName("" + getSize());
	}

}
