package net.AzureWare.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Mod {
   private Value mode = new Value("Velocity", "Mode", 0);
   private Value horizontal = new Value("Velocity_Horizontal", Double.valueOf(100.0D), Double.valueOf(0.0D), Double.valueOf(100.0D), 1.0D);
   private Value vertical = new Value("Velocity_Vertical", Double.valueOf(100.0D), Double.valueOf(0.0D), Double.valueOf(100.0D), 1.0D);
   private Value pushSpeed = new Value("Velocity_PushSpeed ", Double.valueOf(0.25D), Double.valueOf(0.001D), Double.valueOf(0.4D), 0.05D);
   private Value pushStart = new Value("Velocity_StartDelay ", Double.valueOf(8.0D), Double.valueOf(2.0D), Double.valueOf(9.0D), 1.0D);

  
   private double motionX;
   private double motionZ;
   
   public Velocity() {
      super("Velocity", Category.COMBAT);
      this.mode.addValue("Vanilla");
      this.mode.addValue("Mineplex");
      this.mode.addValue("AAC Pull");
      this.mode.addValue("AAC Ultra Pull");
      this.mode.addValue("Best");
      this.mode.addValue("AirMove");
      this.mode.addValue("Reverse");

   }

   public int getH() {
      return ((Double)this.horizontal.getValueState()).intValue();
   }

   public int getV() {
      return ((Double)this.vertical.getValueState()).intValue();
   }

   @EventTarget
   public void onPacket(EventPacket event) {
	   
      if(this.mode.isCurrentMode("Vanilla") || this.mode.isCurrentMode("Mineplex")) {
         if(this.mode.isCurrentMode("Vanilla")) {
        	 this.setDisplayName("Vanilla");
         }else {
        	 this.setDisplayName("Mineplex");
         }
         if(event.packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity exp = (S12PacketEntityVelocity)event.packet;
            if(((Double)this.vertical.getValueState()).intValue() == 0 && ((Double)this.horizontal.getValueState()).intValue() == 0) {
               event.setCancelled(true);
            } else if(exp.getEntityID() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
               exp.motionX = exp.getMotionX() * (this.getH() / 100);
               exp.motionY = exp.getMotionY() * (this.getV() / 100);
               exp.motionZ = exp.getMotionZ() * (this.getH() / 100);
            }
         }

         if(event.packet instanceof S27PacketExplosion) {
            S27PacketExplosion exp1 = (S27PacketExplosion)event.packet;
            if(this.getH() == 0 && this.getV() == 0) {
               event.setCancelled(true);
            } else {
               exp1.field_149152_f = exp1.func_149149_c() * (float)(this.getH() / 100);
               exp1.field_149153_g = exp1.func_149144_d() * (float)(this.getV() / 100);
               exp1.field_149159_h = exp1.func_149147_e() * (float)(this.getH() / 100);
            }
         }
      }

   }

   @EventTarget
   public void onUpdate(EventPacket event) {
      if(this.mode.isCurrentMode("AAC Pull")) {
         this.setDisplayName("AAC Pull");
         if(Minecraft.getMinecraft().thePlayer.hurtTime == 9) {
            this.motionX = Minecraft.getMinecraft().thePlayer.motionX;
            this.motionZ = Minecraft.getMinecraft().thePlayer.motionZ;
         } else if(Minecraft.getMinecraft().thePlayer.hurtTime == 4) {
            Minecraft.getMinecraft().thePlayer.motionX = -this.motionX * 0.6D;
            Minecraft.getMinecraft().thePlayer.motionZ = -this.motionZ * 0.6D;
         }
      }


      if(this.mode.isCurrentMode("AAC Ultra Pull")) {
         this.setDisplayName("AAC Ultra Pull");
         if(Minecraft.getMinecraft().thePlayer.hurtTime == 9) {
            this.motionX = Minecraft.getMinecraft().thePlayer.motionX;
            this.motionZ = Minecraft.getMinecraft().thePlayer.motionZ;
         } else if(Minecraft.getMinecraft().thePlayer.hurtTime == 8) {
            Minecraft.getMinecraft().thePlayer.motionX = -this.motionX * 0.45D;
            Minecraft.getMinecraft().thePlayer.motionZ = -this.motionZ * 0.45D;
         }
      }

   }
   
   
   @EventTarget
   public void onEvent(EventPreMotion event) {
      this.showValue = this.mode;
      double speed;
      if(this.mode.isCurrentMode("Reverse")) {
          this.setDisplayName("Reverse");

         if((double)this.mc.thePlayer.hurtTime > ((Double)this.pushStart.getValueState()).doubleValue()) {
            speed = 0.1D;
            if(PlayerUtil.getSpeed() < speed) {
               PlayerUtil.setSpeed(speed);
            }

            PlayerUtil.toFwd(((Double)this.pushSpeed.getValueState()).doubleValue());
         }
      } else if(this.mode.isCurrentMode("AirMove")) {
          this.setDisplayName("AirPush");

         if(this.mc.thePlayer.hurtTime == 9) {
            this.motionX = this.mc.thePlayer.motionX;
            this.motionZ = this.mc.thePlayer.motionZ;
         } else if((double)this.mc.thePlayer.hurtTime == ((Double)this.pushStart.getValueState()).doubleValue() - 1.0D) {
            speed = ((Double)this.pushSpeed.getValueState()).doubleValue();
            this.mc.thePlayer.motionX *= -speed;
            this.mc.thePlayer.motionZ *= -speed;
         }
      } else if(this.mode.isCurrentMode("Best") && this.mc.thePlayer.hurtTime > 1) {
          this.setDisplayName("Best");

         this.mc.thePlayer.onGround = true;
      }

   }


}
