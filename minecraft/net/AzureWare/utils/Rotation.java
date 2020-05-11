package net.AzureWare.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;



public final class Rotation {
   private float yaw;
   private float pitch;

   public final void toPlayer( EntityPlayer player) {
      float var2 = this.yaw;
      boolean var3 = false;
      if(!Float.isNaN(var2)) {
         var2 = this.pitch;
         var3 = false;
         if(!Float.isNaN(var2)) {
            this.fixedSensitivity(Minecraft.getMinecraft().gameSettings.mouseSensitivity);
            player.rotationYaw = this.yaw;
            player.rotationPitch = this.pitch;
            return;
         }
      }

   }

   public final void fixedSensitivity(float sensitivity) {
      float f = sensitivity * 0.6F + 0.2F;
      float gcd = f * f * f * 1.2F;
      this.yaw -= this.yaw % gcd;
      this.pitch -= this.pitch % gcd;
   }

   public final float getYaw() {
      return this.yaw;
   }

   public final void setYaw(float var1) {
      this.yaw = var1;
   }

   public final float getPitch() {
      return this.pitch;
   }

   public final void setPitch(float var1) {
      this.pitch = var1;
   }

   public Rotation(float yaw, float pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public final float component1() {
      return this.yaw;
   }

   public final float component2() {
      return this.pitch;
   }

   public final Rotation copy(float yaw, float pitch) {
      return new Rotation(yaw, pitch);
   }

   public static Rotation copy$default(Rotation var0, float var1, float var2, int var3, Object var4) {
      if((var3 & 1) != 0) {
         var1 = var0.yaw;
      }

      if((var3 & 2) != 0) {
         var2 = var0.pitch;
      }

      return var0.copy(var1, var2);
   }

   public String toString() {
      return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ")";
   }

   public int hashCode() {
      return Float.hashCode(this.yaw) * 31 + Float.hashCode(this.pitch);
   }

   public boolean equals( Object var1) {
      if(this != var1) {
         if(var1 instanceof Rotation) {
            Rotation var2 = (Rotation)var1;
            if(Float.compare(this.yaw, var2.yaw) == 0 && Float.compare(this.pitch, var2.pitch) == 0) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
