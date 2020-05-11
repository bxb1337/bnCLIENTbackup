package net.AzureWare.mod.mods.COMBAT;

import java.util.Iterator;
import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.CombatUtil;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.Wrapper;
import net.AzureWare.value.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BowAimbot extends Mod {
   public Value attackPlayers = new Value("BowAimbot_AttackPlayers", Boolean.valueOf(true));
   public Value attackAnimals = new Value("BowAimbot_AttackAnimals", Boolean.valueOf(true));
   public Value attackMobs = new Value("BowAimbot_AttackMobs", Boolean.valueOf(true));
   Value prediction = new Value("BowAimbot_Prediction", Boolean.valueOf(true));
   public Value auto = new Value("BowAimbot_AutoShot", Boolean.valueOf(true));
   private Value mode = new Value("BowAimbot", "Mode", 0);
   private EntityLivingBase currentTarget;
   private Random random = new Random();
   private float oldYaw;
   private float oldPitch;
   private double maxAttackDistance = 60.0D;

   public BowAimbot() {
      super("BowAimbot", Category.WORLD);
      this.mode.addValue("Legit");
      this.mode.addValue("YawHead");
      this.mode.addValue("Gyroscope");
   }

   @EventTarget
   public void onUpdate(EventPreMotion event) {
      this.showValue = this.mode;
      if(this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && this.mc.thePlayer.isUsingItem()) {
         if(this.currentTarget == null) {
            this.currentTarget = this.getClosestEntity();
         } else if(this.isValid(this.currentTarget)) {
            this.doAimbot(event);
         } else {
            this.currentTarget = null;
         }
      } else {
         this.currentTarget = null;
      }

      if(this.currentTarget == null) {
         this.oldYaw = this.oldPitch = 0.0F;
      }

   }

   private void doAimbot(EventPreMotion event) {
      float[] rotations = CombatUtil.getRotations(this.currentTarget);
      int i = this.mc.thePlayer.getHeldItem().getMaxItemUseDuration() - this.mc.thePlayer.getItemInUseCount();
      float f = (float)i / 20.0F;
      f = (f * f + f * 2.0F) / 3.0F;
      if(f > 1.0F) {
         f = 1.0F;
      }

      double diffX = this.currentTarget.posX - this.mc.thePlayer.posX;
      double diffY = this.currentTarget.posY + (double)(this.currentTarget.getEyeHeight() / 2.0F) - (this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight());
      double diffZ = this.currentTarget.posZ - this.mc.thePlayer.posZ;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float step = 0.006F;
      float rotYaw = rotations[0];
      float addPitch = (float)(Math.pow((double)f, 4.0D) - (double)step * ((double)step * dist * dist + 2.0D * diffY * (double)(f * f)));
      float rotPitch = (float)(-((double)((float)Math.atan(((double)(f * f) - Math.sqrt((double)addPitch)) / ((double)step * dist)) * 180.0F) / 3.141592653589793D));
      if(!Float.isNaN(rotPitch)) {
         if(((Boolean)this.prediction.getValueState()).booleanValue()) {
            rotYaw += (rotYaw - this.oldYaw) * Math.min(this.mc.thePlayer.getDistanceToEntity(this.currentTarget), 15.0F);
         }

         this.oldYaw = rotYaw;
         this.oldPitch = rotPitch;
         if(PlayerUtil.getSpeed() > 0.0D) {
            this.oldYaw = (float)((double)this.oldYaw + PlayerUtil.getSpeed() / 2.0D * (double)Math.min(this.mc.thePlayer.getDistanceToEntity(this.currentTarget), 15.0F) * (double)(rotYaw - this.oldYaw) / (double)Math.abs(rotYaw - this.oldYaw));
         }

         if(this.mode.isCurrentMode("Legit")) {
            this.mc.thePlayer.rotationYaw = rotYaw;
            this.mc.thePlayer.rotationPitch = rotPitch;
         } else {
            event.yaw = rotYaw;
            event.pitch = rotPitch;
            if(this.mode.isCurrentMode("YawHead")) {
               Wrapper.setLook(event.yaw, event.pitch);
            }
         }

         if(this.mc.thePlayer.getItemInUseDuration() > 20 && ((Boolean)this.auto.getValueState()).booleanValue()) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, this.mc.thePlayer.getPosition(), EnumFacing.fromAngle((double)this.mc.thePlayer.rotationYaw)));
            this.mc.thePlayer.stopUsingItem();
         }

      }
   }

   private EntityLivingBase getClosestEntity() {
      EntityLivingBase closest = null;
      Iterator var3 = this.mc.theWorld.playerEntities.iterator();

      while(true) {
         Entity e;
            do {
               do {
                  if(!var3.hasNext()) {
                     return closest;
                  }

                  e = (Entity)var3.next();
               } while(!(e instanceof EntityLivingBase));
            } while(!this.isValid((EntityLivingBase)e));
         if(closest == null) {
            closest = (EntityLivingBase)e;
         } else if(this.mc.thePlayer.getDistanceToEntity(e) < this.mc.thePlayer.getDistanceToEntity(closest)) {
            closest = (EntityLivingBase)e;
         }
      }
   }

   private boolean isValid(EntityLivingBase entity) {
      return entity.isDead?false:(entity == this.mc.thePlayer?false:(entity instanceof EntityAnimal && !((Boolean)this.attackAnimals.getValueState()).booleanValue()?false:(entity instanceof EntityMob && !((Boolean)this.attackMobs.getValueState()).booleanValue()?false:(entity instanceof EntityPlayer && !((Boolean)this.attackPlayers.getValueState()).booleanValue()?false:(entity instanceof EntityPlayer && (entity instanceof EntityPlayer && (entity.isInvisible()?false:!ClientUtil.isBlockBetween(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ), new BlockPos(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)))))))));
   }
}
