package net.AzureWare.mod.mods;

import java.util.Random;

import net.AzureWare.mod.Mod;

public class RandomModule extends Mod {

	public RandomModule() {
		super("Debug Cracked By ะกรฮ", Category.RENDER);
		this.set(true, false);
	}
}

class StringRandom {
    public String getStringRandom(int length) {
        
        String val = "";
        Random random = new Random();
        
        for(int i = 0; i < length; i++) {
            
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if( "char".equalsIgnoreCase(charOrNum) ) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}