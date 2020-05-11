package net.AzureWare.utils;

import java.awt.Color;
import java.util.ArrayList;

import net.AzureWare.ui.notification.Notification;
import net.AzureWare.ui.notification.Notification.Type;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.BlockPos;

public enum ClientUtil {
	INSTANCE;
	private static ArrayList<Notification> notifications = new ArrayList<>();
	public static double addY;
	
	public void drawNotifications() {
		try {
			ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
			double startY = res.getScaledHeight() - 25;
			final double lastY = startY;
			for (int i = 0; i < notifications.size(); i++) {
				Notification not = notifications.get(i);
				if (not.shouldDelete())
					notifications.remove(i);
				not.draw(startY, lastY);
				startY -= not.getHeight() + 1;
			}
		}catch (Throwable e){
			
		}
	}
	
	public static void sendClientMessage(String message, Type type) {
		if (notifications.size() > 8) notifications.remove(0);
		notifications.add(new Notification(message, type));
	}
	
	public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

	   public static boolean isBlockBetween(BlockPos start, BlockPos end) {
		      Minecraft mc = Minecraft.getMinecraft();
		      int startX = start.getX();
		      int startY = start.getY();
		      int startZ = start.getZ();
		      int endX = end.getX();
		      int endY = end.getY();
		      int endZ = end.getZ();
		      double diffX = (double)(endX - startX);
		      double diffY = (double)(endY - startY);
		      double diffZ = (double)(endZ - startZ);
		      double x = (double)startX;
		      double y = (double)startY;
		      double z = (double)startZ;
		      double STEP = 0.1D;
		      int STEPS = (int)Math.max(Math.abs(diffX), Math.max(Math.abs(diffY), Math.abs(diffZ))) * 4;

		      for(int i = 0; i < STEPS - 1; ++i) {
		         x += diffX / (double)STEPS;
		         y += diffY / (double)STEPS;
		         z += diffZ / (double)STEPS;
		         if(x != (double)endX || y != (double)endY || z != (double)endZ) {
		            BlockPos pos = new BlockPos(x, y, z);
		            Block block = mc.theWorld.getBlockState(pos).getBlock();
		            if(block.getMaterial() != Material.air && block.getMaterial() != Material.water && !(block instanceof BlockVine) && !(block instanceof BlockLadder)) {
		               return true;
		            }
		         }
		      }

		      return false;
		   }

	public static String removeColorCode(String displayString) {
		return displayString.replaceAll("\247.", "");
	}

}
