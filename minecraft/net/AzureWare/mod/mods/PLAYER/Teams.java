package net.AzureWare.mod.mods.PLAYER;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;


import net.AzureWare.Client;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.mod.ModManager;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class Teams extends Mod {
	public static Value<Boolean> clientfriend = new Value("Teams_ClientFriends", Boolean.valueOf(true));
	
	static String ign;
	static boolean clientfriendOld;
	public Teams() {
		super("Teams", Category.PLAYER);
	}

	public void onDisable() {
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (ign == null || ign != mc.thePlayer.getName() || clientfriendOld != clientfriend.getValueState()) {
			ign = mc.thePlayer.getName();
			clientfriendOld = clientfriend.getValueState();
		}
		
	}

	public static boolean isOnSameTeam(Entity entity) {		
		if (ModManager.getModule("Teams").isEnabled()) {
			if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("\247")) {
				if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2
						|| entity.getDisplayName().getUnformattedText().length() <= 2) {
					return false;
				}
				if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2)
						.equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
					return true;
				}
			}
		}
		return false;
	}




}
