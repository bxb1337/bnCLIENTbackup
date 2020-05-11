package net.AzureWare.mod.mods.RENDER;

import java.util.Random;
import java.util.UUID;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.authlib.GameProfile;

import net.AzureWare.events.EventBoundingBox;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Freecam extends Mod {
	private EntityOtherPlayerMP freecamEntity;
    
	public Freecam() {
		super("Freecam", Category.RENDER);
	}
	
	public void onDisable() {
		try {
	        mc.thePlayer.setPositionAndRotation(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ, this.freecamEntity.rotationYaw, this.freecamEntity.rotationPitch);
	        mc.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
	        mc.renderGlobal.loadRenderers();
		} catch (Throwable e) {
			//cnm sb
		}
		this.mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.noClip = false;
        PlayerUtil.setSpeed(0);
        super.onDisable();
	}

	public void onEnable() {
        if (mc.thePlayer == null) {
            return;
        }
        this.freecamEntity = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(new UUID(69, 96), mc.thePlayer.getName()));
        this.freecamEntity.inventory = mc.thePlayer.inventory;
        this.freecamEntity.inventoryContainer = mc.thePlayer.inventoryContainer;
        this.freecamEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        this.freecamEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
        mc.theWorld.addEntityToWorld(this.freecamEntity.getEntityId(), this.freecamEntity);
        mc.renderGlobal.loadRenderers();
        super.onEnable();
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion e) {
        this.mc.thePlayer.capabilities.isFlying = true;
        this.mc.thePlayer.noClip = true;
        this.mc.thePlayer.capabilities.setFlySpeed(0.1f);
        if(mc.gameSettings.keyBindJump.pressed) {
        	mc.thePlayer.motionY = 0.4f;
        }else if(mc.gameSettings.keyBindSneak.pressed) {
        	mc.thePlayer.motionY = -0.4f;
        }else {
        	mc.thePlayer.motionY = 0;
        }
        mc.thePlayer.setSprinting(true);
        if(PlayerUtil.MovementInput()) PlayerUtil.setSpeed(1);
        e.setCancelled(true);
	}

	@EventTarget
	public void onPacketSend(EventPacket e) {
		if (e.getPacket() instanceof C03PacketPlayer && new Random().nextBoolean())
			e.setCancelled(true);
	}

	@EventTarget
	public void onBB(EventBoundingBox e) {
		e.setBoundingBox(null);
	}
}
