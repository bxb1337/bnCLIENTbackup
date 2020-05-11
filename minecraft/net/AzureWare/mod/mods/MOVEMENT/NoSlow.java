package net.AzureWare.mod.mods.MOVEMENT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.swing.JOptionPane;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventMotionUpdate;
import net.AzureWare.events.EventPostMotion;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.events.State;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.client.main.Main;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Mod {
	public static Value<String> mods = new Value("NoSlow", "Mode", 0);

	public NoSlow() {
		super("NoSlow", Category.MOVEMENT);
		mods.addValue("Vanilla");
		mods.addValue("NCP");

	}

	@EventTarget
	public void onPre(EventPreMotion e) {
		if(mods.isCurrentMode("Vanilla"))
		{
			return;
		}
        if(mods.isCurrentMode("NCP")) {
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving() ){
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				
			}
        }
	}

	@EventTarget
	public void onPost(EventPostMotion e) {
		if(mods.isCurrentMode("Vanilla"))
		{
			return;
		}
        if(mods.isCurrentMode("NCP")) {
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving()  && ( Killaura.target == null )){
					mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
				}
			}
	}

	public static boolean isOnGround(double height) {
		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
			return true;
		}
		return false;
	}
}
