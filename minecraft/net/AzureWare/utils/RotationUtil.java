package net.AzureWare.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil {
   public static float[] getNeededRotationsToEntity(Entity entity) {
      Minecraft.getMinecraft();
      EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
      double x = entity.posX - player.posX;
      double y = entity.posY - player.posY;
      double z = entity.posZ - player.posZ;
      double yaw = -Math.atan2(x, z) * 57.0D;
      y = y / (double)player.getDistanceToEntity(entity);
      double pitch = Math.asin(y) * 57.0D - 20.0D;
      pitch = -pitch;
      return new float[]{(float)yaw, (float)(pitch > 90.0D?90.0D:(pitch < -90.0D?-90.0D:pitch))};
   }
	public static int wrapAngleToDirection(float yaw, int zones) {
		int angle = (int) ((double) (yaw + (float) (360 / (2 * zones))) + 0.5) % 360;
		if (angle < 0) {
			angle += 360;
		}
		return angle / (360 / zones);
	}
   public static float[] getRotations(final BlockPos pos, final EnumFacing facing) {
       return getRotations(pos.getX(), pos.getY(), pos.getZ(), facing);
   }
   public static Vec3 getVectorForRotation(final Rotation rotation) {
       float yawCos = MathHelper.cos(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
       float yawSin = MathHelper.sin(-rotation.getYaw() * 0.017453292F - (float) Math.PI);
       float pitchCos = -MathHelper.cos(-rotation.getPitch() * 0.017453292F);
       float pitchSin = MathHelper.sin(-rotation.getPitch() * 0.017453292F);
       return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
   }
   public static float[] getRotations(final double x, final double y, final double z, final EnumFacing facing) {
       final EntityPig temp = new EntityPig(Minecraft.theWorld);
       temp.posX = x + 0.5;
       temp.posY = y + 0.5;
       temp.posZ = z + 0.5;

       temp.posX += facing.getDirectionVec().getX() * 0.5;
       temp.posY += facing.getDirectionVec().getY() * 0.5;
       temp.posZ += facing.getDirectionVec().getZ() * 0.5;

       return getRotations(temp);
   }

   public static float[] getRotations(Entity ent) {
       double x = ent.posX;
       double z = ent.posZ;
       double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
       return RotationUtil.getRotationFromPosition(x, z, y);
   }
   public static float[] getAverageRotations(List<Entity> targetList) {
       double posX = 0.0;
       double posY = 0.0;
       double posZ = 0.0;
       for (Entity ent : targetList) {
           posX += ent.posX;
           posY += ent.boundingBox.maxY - 2.0;
           posZ += ent.posZ;
       }
       return new float[]{getRotationFromPosition(posX /= (double)targetList.size(), posZ /= (double)targetList.size(), posY /= (double)targetList.size())[0], getRotationFromPosition(posX, posZ, posY)[1]};
   }


   public static double getRotationDifference(final Rotation a, final Rotation b) {
       return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
   }
   private static float getAngleDifference(final float a, final float b) {
       return ((((a - b) % 360F) + 540F) % 360F) - 180F;
   }

   public static float[] getBowAngles(Entity entity) {
       double xDelta = entity.posX - entity.lastTickPosX;
       double zDelta = entity.posZ - entity.lastTickPosZ;
       double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
       d -= d % 0.8;
       double xMulti = 1.0;
       double zMulti = 1.0;
       boolean sprint = entity.isSprinting();
       xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
       zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
       double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
       double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
       double y = Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
       double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
       float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
       float pitch = (float)Math.toDegrees(Math.atan2(y, dist));
       return new float[]{yaw, pitch};
   }

   public static float[] getRotationFromPosition(double x, double z, double y) {
       double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
       double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
       double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
       double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
       float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
       float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
       return new float[]{yaw, pitch};
   }

   public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
       float g = 0.006f;
       float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
       return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt(sqrt)) / (double)(g * d3)));
   }

   public static float getYawChange(double posX, double posZ) {
       double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
       double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
       double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(- Math.atan(deltaX / deltaZ)));
       return MathHelper.wrapAngleTo180_float(- Minecraft.getMinecraft().thePlayer.rotationYaw - (float)yawToEntity);
   }

   public static float getYawChangeByHead(double posX, double posZ) {
       double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
       double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
       double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(- Math.atan(deltaX / deltaZ)));
       return MathHelper.wrapAngleTo180_float(- Minecraft.getMinecraft().thePlayer.rotationYawHead - (float)yawToEntity);
   }
   
   public static float getPitchChange(Entity entity, double posY) {
       double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
       double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
       double deltaY = posY - 2.2 + (double)entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
       double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
       double pitchToEntity = - Math.toDegrees(Math.atan(deltaY / distanceXZ));
       return - MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float)pitchToEntity) - 2.5f;
   }

   public static float getNewAngle(float angle) {
       if ((angle %= 360.0f) >= 180.0f) {
           angle -= 360.0f;
       }
       if (angle < -180.0f) {
           angle += 360.0f;
       }
       return angle;
   }

   public static float getDistanceBetweenAngles(float angle1, float angle2) {
       float angle = Math.abs(angle1 - angle2) % 360.0f;
       if (angle > 180.0f) {
           angle = 360.0f - angle;
       }
       return angle;
   }
   
   public static float[] grabBlockRotations(BlockPos pos) {
       return RotationUtil.getVecRotation(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0), new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5));
   }

   public static float[] getVecRotation(Vec3 position) {
       return RotationUtil.getVecRotation(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0.0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0), position);
   }

   public static float[] getVecRotation(Vec3 origin, Vec3 position) {
       Vec3 difference = position.subtract(origin);
       double distance = difference.flat().lengthVector();
       float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
       float pitch = (float)(- Math.toDegrees(Math.atan2(difference.yCoord, distance)));
       return new float[]{yaw, pitch};
   }


   public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
       double var6;
       double var4 = target.posX - Minecraft.getMinecraft().thePlayer.posX;
       double var8 = target.posZ - Minecraft.getMinecraft().thePlayer.posZ;
       if (target instanceof EntityLivingBase) {
           EntityLivingBase var10 = (EntityLivingBase)target;
           var6 = var10.posY + (double)var10.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
       } else {
           var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight());
       }
       Random rnd = new Random();
       double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
       float var12 = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
       float var13 = (float)(- Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / 3.141592653589793);
       float pitch = RotationUtil.changeRotation(Minecraft.getMinecraft().thePlayer.rotationPitch, var13, p_706253);
       float yaw = RotationUtil.changeRotation(Minecraft.getMinecraft().thePlayer.rotationYaw, var12, p_706252);
       return new float[]{yaw, pitch};
   }
   

 
   public static float changeRotation(float p_706631, float p_706632, float p_706633) {
       float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
       if (var4 > p_706633) {
           var4 = p_706633;
       }
       if (var4 < - p_706633) {
           var4 = - p_706633;
       }
       return p_706631 + var4;
   }


   public static float getYawChangeGiven(double posX, double posZ, float yaw) {
	      double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
	      double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
	      double yawToEntity;
	      if(deltaZ < 0.0D && deltaX < 0.0D) {
	         yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
	      } else if(deltaZ < 0.0D && deltaX > 0.0D) {
	         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
	      } else {
	         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
	      }

	      return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
	   }
   public static float[] getRotationToLocation(Vec3 loc) {
       double xDiff = loc.xCoord - Minecraft.thePlayer.posX;
       double yDiff = loc.yCoord - (Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
       double zDiff = loc.zCoord - Minecraft.thePlayer.posZ;

       double distance = (double) MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);

       float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
       float pitch = (float) (-(Math.atan2(yDiff, distance) * 180.0D / Math.PI));

       return new float[]{yaw, pitch};
   }
}
