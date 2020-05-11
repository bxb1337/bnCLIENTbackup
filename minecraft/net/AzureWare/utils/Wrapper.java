package net.AzureWare.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Wrapper {
	
	   private static Minecraft mc;

	    static {
	        mc = Minecraft.getMinecraft();
	    }
	public static final String INSTANCE = null;
	public static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}
	
	public static EntityPlayerSP getPlayer() {
		return getMinecraft().thePlayer;
	}
	
	public static World getWorld() {
		return getMinecraft().theWorld;
	}
	
	public static void addChat(String msg) {
		getPlayer().addChatComponentMessage(new ChatComponentText(msg));
	}
	
	public static FontRenderer getFontRenderer() {
		return getMinecraft().fontRendererObj;
	}

    public static void drawCenteredString(String s, int x, int y, int colour) {
        x -= Wrapper.getMinecraft().fontRendererObj.getStringWidth(s) / 2;
        Wrapper.getMinecraft().fontRendererObj.drawStringWithShadow(s, x, y, colour);
    }

    public static int getIntFromColor(int r, int g, int b){
        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        b = b & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
    public static int getIntFromColor(Color color){
        return color.getRGB();
    }
    public static Color getColorFromHex(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);

        return new Color(r, g, b);
    }

    public static void setLook(float rotationYaw, float rotationPitch) {
        if (Wrapper.mc.gameSettings.thirdPersonView > 0) {
            Wrapper.mc.thePlayer.rotationPitchHead = rotationPitch;
            Wrapper.mc.thePlayer.rotationYawHead = rotationYaw;
            Wrapper.mc.thePlayer.renderYawOffset = rotationYaw;
        }
    }

    public static double getDoubleRandom(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double getRandomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public void multiply(double speed) {
        Wrapper.mc.thePlayer.motionX *= speed;
        Wrapper.mc.thePlayer.motionZ *= speed;
    }
	public static boolean canSendMotionPacket = true;

}
