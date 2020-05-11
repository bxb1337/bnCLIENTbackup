package net.AzureWare.mod.mods.COMBAT;

import net.AzureWare.events.EventAttackEntity;
import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventReceivePacket;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.CombatUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.utils.Wrapper;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import com.darkmagician6.eventapi.EventTarget;

public class SuperHit extends Mod {
	   private EntityLivingBase entity;
	   private Value modes = new Value("TeleportHit", "TargetMode", 0);
	   private Value mode = new Value("TeleportHit", "Mode", 0);
	   TimeHelper time = new TimeHelper();
	   private boolean hit;

	   public SuperHit() {
	      super("TeleportHit", Category.WORLD);
	      this.modes.mode.add("FOV");
	      this.modes.mode.add("Distance");
	      this.mode.addValue("Old");
	      this.mode.addValue("New");
	   }

	   @EventTarget
	   public void onPreMotion(EventPreMotion event) {
	      Vec3 vec;
	      double entX;
	      double entZ;
	      double entY;
	      if(this.mode.isCurrentMode("New")) {
	         if(!this.hit) {
	            this.entity = null;
	            if(this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null && this.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
	               this.entity = (EntityPlayer)this.mc.objectMouseOver.entityHit;
	            } else {
	               this.setTarget();
	            }
	         }

	         if(this.entity != null && Mouse.isButtonDown(0)) {
	            if(!this.hit) {
	               this.hit = true;
	               return;
	            }

	            if(this.hit && this.mc.thePlayer.onGround) {
	               this.mc.thePlayer.jump();
	            }

	            if(this.mc.thePlayer.fallDistance > 0.0F) {
	               event.yaw = CombatUtil.getRotations(this.entity)[0];
	               event.pitch = CombatUtil.getRotations(this.entity)[1];
	               Wrapper.setLook(event.yaw, event.pitch);
	               if((double)this.mc.thePlayer.getDistanceToEntity(this.entity) > 3.5D) {
	                  vec = this.mc.thePlayer.getVectorForRotation(0.0F, this.mc.thePlayer.rotationYaw);
	                  entX = this.mc.thePlayer.posX + vec.xCoord * (double)(this.mc.thePlayer.getDistanceToEntity(this.entity) - 1.0F);
	                  entZ = this.mc.thePlayer.posZ + vec.zCoord * (double)(this.mc.thePlayer.getDistanceToEntity(this.entity) - 1.0F);
	                  entY = (double)this.entity.getPosition().getY() + 0.25D;
	                  ArrayList positions = PlayerUtil.vanillaTeleportPositions(entX, entY + 1.0D, entZ, 4.0D);

	                  for(int j = 0; j < 1; ++j) {
	                     for(int i = 0; i < positions.size(); ++i) {
	                        Vector3f pos = (Vector3f)positions.get(i);
	                        if(i == 0) {
	                           new Vector3f((float)this.mc.thePlayer.posX, (float)this.mc.thePlayer.posY, (float)this.mc.thePlayer.posZ);
	                        } else {
	                           Vector3f var10000 = (Vector3f)positions.get(i - 1);
	                        }

	                        this.mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), false));
	                     }
	                  }

	                  this.mc.thePlayer.onCriticalHit(this.entity);
	                  this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.entity, Action.ATTACK));
	                  this.hit = false;
	               }
	            }
	         }
	      } else if(this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null) {
	         vec = this.mc.objectMouseOver.entityHit.getVectorForRotation(0.0F, this.mc.objectMouseOver.entityHit.rotationYaw);
	         entX = this.mc.objectMouseOver.entityHit.posX - vec.xCoord * 0.5D;
	         entZ = this.mc.objectMouseOver.entityHit.posZ - vec.zCoord * 0.5D;
	         entY = (double)this.mc.objectMouseOver.entityHit.getPosition().getY();

	         Vector3f var14;
	         for(Iterator var15 = PlayerUtil.vanillaTeleportPositions(entX, entY, entZ, 1.0D).iterator(); var15.hasNext(); var14 = (Vector3f)var15.next()) {
	            ;
	         }
	      }

	   }

	   private void setTarget() {
	      double closest = 2.147483647E9D;
	      EntityLivingBase target = null;
	      Iterator var5 = this.mc.theWorld.loadedEntityList.iterator();

	      while(var5.hasNext()) {
	         Object o = var5.next();
	         if(o instanceof EntityLivingBase) {
	            EntityLivingBase e = (EntityLivingBase)o;
	            if(this.isValidTarget(e)) {
	               if(this.modes.isCurrentMode("FOV")) {
	                  float dist = CombatUtil.getRotations(e)[0];
	                  float yawDiff = CombatUtil.getYawDifference(CombatUtil.getNewAngle(this.mc.thePlayer.rotationYaw), CombatUtil.getNewAngle(dist));
	                  if((double)yawDiff < closest) {
	                     closest = (double)yawDiff;
	                     target = e;
	                  }
	               } else {
	                  double dist1 = (double)this.mc.thePlayer.getDistanceToEntity(e);
	                  if(dist1 < closest) {
	                     closest = dist1;
	                     target = e;
	                  }
	               }
	            }
	         }
	      }

	      this.entity = target;
	   }

	   @EventTarget
	   public void onEvent(EventPacket event) {
	      if(event.getPacket() instanceof S18PacketEntityTeleport) {
	         S18PacketEntityTeleport packet = (S18PacketEntityTeleport)event.getPacket();
	         if(packet.getEntityId() == 80085) {
	            event.setCancelled(true);
	         }
	      }

	   }

	   @EventTarget
	   public void doAttack(EventAttackEntity event) {
	      if(this.mode.isCurrentMode("Old")) {
	         if(!this.time.isDelayComplete(500L)) {
	            return;
	         }

	         if(event.getPlayerIn() == this.mc.thePlayer && (double)this.mc.thePlayer.getDistanceToEntity(event.getTarget()) > 3.5D) {
	            Vec3 vec = this.mc.thePlayer.getVectorForRotation(0.0F, this.mc.thePlayer.rotationYaw);
	            double x = this.mc.thePlayer.posX + vec.xCoord * (double)(this.mc.thePlayer.getDistanceToEntity(event.getTarget()) - 1.0F);
	            double z = this.mc.thePlayer.posZ + vec.zCoord * (double)(this.mc.thePlayer.getDistanceToEntity(event.getTarget()) - 1.0F);
	            double y = (double)event.getTarget().getPosition().getY() + 0.25D;
	            ArrayList positions = PlayerUtil.vanillaTeleportPositions(x, y, z, 3.0D);

	            for(int j = 0; j < 1; ++j) {
	               for(int i = 0; i < positions.size() - 1; ++i) {
	                  Vector3f pos = (Vector3f)positions.get(i);
	                  if(i == 0) {
	                     new Vector3f((float)this.mc.thePlayer.posX, (float)this.mc.thePlayer.posY, (float)this.mc.thePlayer.posZ);
	                  } else {
	                     Vector3f var10000 = (Vector3f)positions.get(i - 1);
	                  }

	                  this.mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), true));
	                  positions.size();
	               }
	            }

	            this.time.reset();
	         }
	      }

	   }

	   private boolean isValidTarget(EntityLivingBase entity) {
	      return entity == this.mc.thePlayer?false:(entity.isInvisible()?false:entity instanceof EntityPlayer);
	   }
	}
