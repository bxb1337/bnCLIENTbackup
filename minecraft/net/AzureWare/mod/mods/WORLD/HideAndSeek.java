package net.AzureWare.mod.mods.WORLD;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventChat;
import net.AzureWare.events.EventRender;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.events.EventWorldLoaded;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.FlatColors;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class HideAndSeek extends Mod {
	public static List<EntityLivingBase> kids = new ArrayList();
	public TimeHelper timer = new TimeHelper();
	public HideAndSeek() {
		super("HideAndSeek", Category.WORLD);
	}
	
	public void onEnable() {
		kids.clear();
	}
	
	public void onDisable() {
		kids.clear();
	}
	
	@EventTarget
	public void onWorldLoaded(EventWorldLoaded e) {
		kids.clear();
	}
	
	@EventTarget
	public void onChat(EventChat e) {
		if(e.getMessage().contains("躲猫猫")) timer.reset();
	}
	
	@EventTarget
	public void onRender(EventRender e) {
		for (EntityLivingBase entity : kids) {
	        if (entity == null) {
	            return;
	        }
	        Color color = new Color(Colors.DARKRED.c);
	        
	        mc.getRenderManager();
	        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
	        mc.getRenderManager();
	        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY;
	        mc.getRenderManager();
	        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
	        if (entity instanceof EntityPlayer) {
	            double d = entity.isSneaking() ? 0.25 : 0.0;
	            double mid = 0.275;
	            GL11.glPushMatrix();
	            GL11.glEnable(3042);
	            GL11.glBlendFunc(770, 771);
	            double rotAdd = -0.25 * (Math.abs(entity.rotationPitch) / 90.0f);
	            GL11.glTranslated(0.0, rotAdd, 0.0);
	            GL11.glTranslated(((x -= 0.275) + 0.275), ((y += entity.getEyeHeight() - 0.225 - d) + 0.275), ((z -= 0.275) + 0.275));
	            GL11.glRotated((-entity.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
	            GL11.glTranslated((-(x + 0.275)), (-(y + 0.275)), (-(z + 0.275)));
	            GL11.glTranslated((x + 0.275), (y + 0.275), (z + 0.275));
	            GL11.glRotated(entity.rotationPitch, 1.0, 0.0, 0.0);
	            GL11.glTranslated((-(x + 0.275)), (-(y + 0.275)), (-(z + 0.275)));
	            GL11.glDisable(3553);
	            GL11.glEnable(2848);
	            GL11.glDisable(2929);
	            GL11.glDepthMask(false);
	            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), 1.0f);
	            GL11.glLineWidth(1.0f);
	            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
	            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), 0.5f);
	            RenderUtil.drawBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
	            GL11.glDisable(2848);
	            GL11.glEnable(3553);
	            GL11.glEnable(2929);
	            GL11.glDepthMask(true);
	            GL11.glDisable(3042);
	            GL11.glPopMatrix();
	        } else {
	            double width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
	            double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25;
	            float red = 1.0f;
	            float green = 0.0f;
	            float blue = 0.0f;
	            float alpha = 0.5f;
	            float lineRed = 0.0f;
	            float lineGreen = 0.5f;
	            float lineBlue = 1.0f;
	            float lineAlpha = 1.0f;
	            float lineWdith = 2.0f;
	            RenderUtil.drawEntityESP(x, y, z, width, height, 1.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 1.0f, 1.0f, 2.0f);
	        }
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		for (Entity entity : mc.theWorld.loadedEntityList) {
			if (entity instanceof EntityLivingBase && 
				!(entity instanceof EntityPlayer) && 
				!(entity instanceof EntityArmorStand) &&
				!(entity instanceof EntityWither) && 
				!kids.contains(entity) &&
				!entity.getName().contains("\247c\247l") &&
				timer.isDelayComplete(5000)) {
				double pos = (entity.posY - (int)entity.posY);
				if(pos > 0.1 && (pos + "").length() > 8) {
					kids.add((EntityLivingBase) entity);
					PlayerUtil.tellPlayer("\247b[AzureWare]\247a检测到一个异常动物:" + entity.getName());
				}
			}
		}
		
		for (EntityLivingBase entity : kids) {
			if (entity.isDead || entity.getHealth() < 0) kids.remove(entity);
		}
	}
}
