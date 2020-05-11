package net.AzureWare.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.value.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class ChestESP extends Mod {
	public Value<Boolean> chest = new Value("ChestESP_Chest",false);
	public Value<Boolean> enderchest = new Value("ChestESP_EnderChest",false);
	public static Value<String> modes = new Value<String>("ChestESP", "Mode", 0);

   public ChestESP() {
      super("ChestESP", Category.RENDER);
      modes.addValue("Old");
      modes.addValue("New");
   }
	@EventTarget
   public void onRender(EventRender event) {
      Iterator var2 = this.mc.theWorld.loadedTileEntityList.iterator();
      if (modes.isCurrentMode("Old")) {
          while(var2.hasNext()) {
              TileEntity tileentity = (TileEntity)var2.next();
              if(chest.getValueState().booleanValue() && tileentity instanceof TileEntityChest) {
                 renderChest(tileentity.getPos());
              }

              if(enderchest.getValueState().booleanValue() && tileentity instanceof TileEntityEnderChest) {
                 renderEnderChest(tileentity.getPos());
              }
           }
      }
      if (modes.isCurrentMode("New")) {
    	  for (TileEntity chest : mc.theWorld.loadedTileEntityList)
          {
              if (chest instanceof TileEntityEnderChest || chest instanceof TileEntityChest)
              {
                  double x = (double) chest.getPos().getX() - mc.getRenderManager().viewerPosX;
                  double y = (double) chest.getPos().getY() - mc.getRenderManager().viewerPosY;
                  double z = (double) chest.getPos().getZ() - mc.getRenderManager().viewerPosZ;
                  
                  GL11.glPushMatrix();
                  GL11.glEnable(GL11.GL_BLEND);
                  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                  GL11.glLineWidth(1.0F);
                  GL11.glDisable(GL11.GL_TEXTURE_2D);
                  GL11.glDisable(GL11.GL_DEPTH_TEST);
                  GL11.glDepthMask(true);
                  
                  int r = chest instanceof TileEntityEnderChest ? 180 : 0;
                  int g = chest instanceof TileEntityEnderChest ? 0 : 100;
                  int b = chest instanceof TileEntityEnderChest ? 250 : 200;
                  GL11.glColor4d(r, g, b, 0.2);
                  
                  RenderGlobal.func_181561_a(new AxisAlignedBB(x + 0.06, y, z + 0.06, x + 0.94D, y + 0.88D, z + 0.94));
                  RenderGlobal.func_181561_a(new AxisAlignedBB(x + 0.06, y, z + 0.06, x + 0.94D, y + 0.88D, z + 0.94));
                  RenderUtil.drawBox(new AxisAlignedBB(x + 0.06, y, z + 0.06, x + 0.94D, y + 0.88D, z + 0.94));
                  
                  GL11.glEnable(GL11.GL_TEXTURE_2D);
                  GL11.glEnable(GL11.GL_DEPTH_TEST);
                  GL11.glDepthMask(true);
                  GL11.glDisable(GL11.GL_BLEND);
                  GL11.glPopMatrix();
              }
          }
           
      }

   }

   public static void renderChest(BlockPos blockPos) {
      double d0 = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
      double d1 = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
      double d2 = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(1.0F);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(true);
      GL11.glColor4d(255.0D, 170.0D, 0.0D, 1.0D);
      RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 1.0D, d2 + 1.0D), RenderUtil.rainbowEffect(100).getRed(), RenderUtil.rainbowEffect(100).getGreen(), RenderUtil.rainbowEffect(100).getBlue(), 255);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void renderEnderChest(BlockPos blockPos) {
	  double d0 = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
	  double d1 = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
	  double d2 = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(1.0F);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(true);
      GL11.glColor4d(170.0D, 0.0D, 170.0D, 1.0D);
      RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 1.0D, d2 + 1.0D), RenderUtil.rainbowEffect(100).getRed(), RenderUtil.rainbowEffect(100).getGreen(), RenderUtil.rainbowEffect(100).getBlue(), 255);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }
}
