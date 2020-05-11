package net.AzureWare.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerInfo {

	public EntityPlayer player;
	
	public int firstX;
	public int firstY;
	public int firstZ;
	public boolean touchedGround;
	public boolean wasInvisible;
	public boolean toolCheck;
	public boolean hurt;
	public double Ticks =0;
	
	public PlayerInfo(EntityPlayer player) {
		this.player = player;
		this.firstX = (int) player.posX;
		this.firstY = (int) player.posY;
		this.firstZ = (int) player.posZ;
		this.touchedGround = false;
		this.wasInvisible = false;
        this.toolCheck = false;
        this.hurt = false;

	}

	public void update() {
		if (this.player.onGround && this.player.isOnGround(0.01)) {
			this.touchedGround = true;
		}
		if (this.player.isInvisible()) {
			this.wasInvisible = true;
		}
        if (this.player.getHeldItem() != null) {
        	this.toolCheck = true;
        }
        if (this.player.hurtTime >= 1) {
        	hurt = true;
        }
	}

	public boolean moved() {
		return this.firstX != (int) this.player.posX || this.firstY != (int) this.player.posY
				|| this.firstZ != (int) this.player.posZ;
	}

}
