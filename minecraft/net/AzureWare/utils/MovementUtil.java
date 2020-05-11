package net.AzureWare.utils;

import net.minecraft.client.Minecraft;

public class MovementUtil {
	
	 public static float getSpeed() {
	        return (float) Math.sqrt(Minecraft.thePlayer.motionX * Minecraft.thePlayer.motionX + Minecraft.thePlayer.motionZ * Minecraft.thePlayer.motionZ);
	    }

	    public static void strafe() {
	        strafe(getSpeed());
	    }

	    public static boolean isMoving() {
	        return Minecraft.thePlayer != null && (Minecraft.thePlayer.movementInput.moveForward != 0F || Minecraft.thePlayer.movementInput.moveStrafe != 0F);
	    }

	    public static boolean hasMotion() {
	        return Minecraft.thePlayer.motionX != 0D && Minecraft.thePlayer.motionZ != 0D && Minecraft.thePlayer.motionY != 0D;
	    }

	    public static void strafe(final float speed) {
	        if(!isMoving())
	            return;

	        final double yaw = getDirection();
	        Minecraft.thePlayer.motionX = -Math.sin(yaw) * speed;
	        Minecraft.thePlayer.motionZ = Math.cos(yaw) * speed;
	    }

	    public static void forward(final double length) {
	        final double yaw = Math.toRadians(Minecraft.thePlayer.rotationYaw);
	        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX + (-Math.sin(yaw) * length), Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + (Math.cos(yaw) * length));
	    }

	    public static double getDirection() {
	        float rotationYaw = Minecraft.thePlayer.rotationYaw;

	        if(Minecraft.thePlayer.moveForward < 0F)
	            rotationYaw += 180F;

	        float forward = 1F;
	        if(Minecraft.thePlayer.moveForward < 0F)
	            forward = -0.5F;
	        else if(Minecraft.thePlayer.moveForward > 0F)
	            forward = 0.5F;

	        if(Minecraft.thePlayer.moveStrafing > 0F)
	            rotationYaw -= 90F * forward;

	        if(Minecraft.thePlayer.moveStrafing < 0F)
	            rotationYaw += 90F * forward;

	        return Math.toRadians(rotationYaw);
	    }
}
