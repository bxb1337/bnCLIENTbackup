package net.AzureWare.mod.mods.WORLD;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.COMBAT.AntiBot;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AutoParty extends Mod {

	private TimeHelper timer = new TimeHelper();
	private Value<Double> delay = new Value("AutoParty_Delay", 1000D, 200D, 5000D, 100D);
	private List<String> invitedlist = new ArrayList();
	
	public AutoParty() {
		super("AutoParty", Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		if(timer.isDelayComplete(delay.getValueState().longValue())) {
			String PlayerName = getPlayerName();
			if(PlayerName == null) {
				PlayerUtil.tellPlayer("\247b[AzureWare]\247a本房间已邀请完毕！");
			}else {
				mc.thePlayer.sendChatMessage("/p " + PlayerName);
			}
			timer.reset();
		}
	}
	
	@Override
	public void onEnable() {
		invitedlist.clear();
		timer.reset();
		super.onEnable();
	}
	
	private String getPlayerName() {
		for(Entity entity : GuiPlayerTabOverlay.getPlayerList()) {
			if(entity != mc.thePlayer && entity instanceof EntityPlayer && !invitedlist.contains(entity.getName())) {
				invitedlist.add(entity.getName());
				return entity.getName();
			}
		}
		return null;
	}

}
