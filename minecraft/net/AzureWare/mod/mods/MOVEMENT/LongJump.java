package net.AzureWare.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventMotionUpdate;
import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventPullback;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import net.minecraft.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public class LongJump extends Mod {
	private Value<String> mode = new Value("LongJump", "Mode", 0);
	private Value<Boolean> lagback = new Value("LongJump_LagBackChecks", false);
	private TimeHelper timer = new TimeHelper();

	public int speed = 0;
	public int togglestage = 0;

	@EventTarget
	public void onPullback(EventPullback e) {
		if (lagback.getValueState()) {
			ClientUtil.sendClientMessage("LongJump Lagback Checked", Notification.Type.WARNING);
			this.set(false);
		}
	}

	public LongJump() {
		super("LongJump", Category.MOVEMENT);
		mode.mode.add("Hypixel");
		mode.mode.add("Austin");

		// mode.mode.add("HypixelDamage");

	}

	private int count, count2, fastCount, stage;
	double hypixel;

	public void onEnable() {
		
		timer.reset();
		mc.gameSettings.keyBindJump.pressed = true;
		fastCount = 100;
		mc.timer.timerSpeed = 1f;
		stage = 1;
		hypixel = 0;
		double x = mc.thePlayer.posX;
		double y = mc.thePlayer.posY;
		double z = mc.thePlayer.posZ;
		 if(this.mc.thePlayer != null) {
	         this.a = this.c = 0;
	         this.f = false;
	         

	      }

		super.onEnable();
	}
	 private int a;
	   private int c;
	   private float moveStarfing;
	   private float moveForward;
	   private boolean f;

	public void onDisable() {
		mc.gameSettings.keyBindJump.pressed = false;
		mc.thePlayer.ticksExisted = 0;
		this.togglestage = 0;
		mc.thePlayer.motionX *= 0;
		mc.thePlayer.motionZ *= 0;
		mc.thePlayer.jumpMovementFactor = 0.1f;
		count = 0;

		fastCount = 100;
		PlayerUtil.setMotion(0.2);
		mc.thePlayer.jumpMovementFactor = 0;
		mc.thePlayer.capabilities.isFlying = false;
		mc.timer.timerSpeed = 1f;
		mc.thePlayer.capabilities.allowFlying = false;
		super.onDisable();
	}

	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		if ((timer.isDelayComplete(200) && mc.thePlayer.onGround) || timer.isDelayComplete(2000) && mode.isCurrentMode("Hypixel")){
			this.set(false);
		}

		if (mode.isCurrentMode("Hypixel")) {
			if (PlayerUtil.MovementInput() && this.mc.thePlayer.fallDistance < 1.0f) {

				final float direction = this.mc.thePlayer.rotationYaw;
				final float x = (float) Math.cos((direction + 90.0f) * 3.141592653589793 / 180.0);
				final float z = (float) Math.sin((direction + 90.0f) * 3.141592653589793 / 180.0);
				if (this.mc.thePlayer.isCollidedVertically && PlayerUtil.MovementInput()
						&& this.mc.gameSettings.keyBindJump.pressed) {
					this.mc.thePlayer.motionX = x * 0.29f;
					this.mc.thePlayer.motionZ = z * 0.29f;

				}
				if (this.mc.thePlayer.motionY == 0.33319999363422365 && PlayerUtil.MovementInput()) {
					this.mc.thePlayer.motionX = x * 1.161;
					this.mc.thePlayer.motionZ = z * 1.161;
				}
			}

		}

		if (mode.isCurrentMode("Austin")) {
			this.setDisplayName("Austim");
		      if(this.mc.thePlayer.onGround && this.f) {
		          this.f = false;
		       }

		       if(PlayerUtil.isMoving()) {
		          if(this.mc.thePlayer.onGround) {
		             if(++this.a <= 2) {
		            	 PlayerUtil.setStrafe(0.0D);
		             } else if(this.a == 3) {
		            	 PlayerUtil.setStrafe(0.46D);
		             } else {
		                this.mc.thePlayer.jump();
		                this.f = true;
		                this.a = this.c = 0;
		             }
		          } else if(++this.c == 1) {
		        	  PlayerUtil.setStrafe(PlayerUtil.getSpeed() + 0.44D);
		          } else {
		        	  PlayerUtil.setStrafe(PlayerUtil.getDistance() + 0.008D);
		             if(this.c > 5 && this.c < 12) {
		                this.mc.thePlayer.motionY += 0.025D;
		             }
		          }
		       } else {
		          this.mc.thePlayer.motionX *= 0.75D;
		          this.mc.thePlayer.motionZ *= 0.75D;
		       }

		       float var4 = this.mc.thePlayer.moveStrafing;
		       float var3 = this.mc.thePlayer.moveForward;
		       if((this.moveStarfing != var4 || this.moveForward != var3) && (this.moveStarfing != 0.0F || this.moveForward != 0.0F)) {
		          this.mc.thePlayer.motionX *= 0.8D;
		          this.mc.thePlayer.motionZ *= 0.8D;
		       }

		       this.moveStarfing = var4;
		       this.moveForward = var3;


			}
		
	}

	@EventTarget
	public void onPacket(EventPacket e) {
		Packet packet = e.getPacket();
		if (packet instanceof S08PacketPlayerPosLook) {
			hypixel = 0;
		}
	}

}
