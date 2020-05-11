package net.AzureWare.mod.mods.RENDER;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.minecraft.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Projectiles extends Mod {

	public Projectiles() {
		super("Projectiles", Category.RENDER);
	}
	
	private EntityLivingBase entity;
    private MovingObjectPosition blockCollision, entityCollision;
    private static AxisAlignedBB aim;
	@EventTarget
	public void onRender(EventRender event) {
		if (!Client.INSTANCE) Runtime.getRuntime().exit(0);
		if(mc.thePlayer.inventory.getCurrentItem() != null) {
			EntityPlayerSP player = mc.thePlayer;
	        ItemStack stack = player.inventory.getCurrentItem();
	        int item = Item.getIdFromItem(mc.thePlayer.getHeldItem().getItem());
	        if ((item == 261 || item == 368 || item == 332 || item == 344)) {
	        	double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks - Math.cos(Math.toRadians(player.rotationYaw)) * 0.16F;
	            double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks + player.getEyeHeight() - 0.1D;
	            double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks - Math.sin(Math.toRadians(player.rotationYaw)) * 0.16F;
	            double itemBow = stack.getItem() instanceof ItemBow ? 1.0F : 0.4F;
	            
	            double yaw = Math.toRadians(player.rotationYaw);
	            double pitch = Math.toRadians(player.rotationPitch);
	            
	            double trajectoryX = -Math.sin(yaw) * Math.cos(pitch) * itemBow;
	            double trajectoryY = -Math.sin(pitch) * itemBow;
	            double trajectoryZ =  Math.cos(yaw) * Math.cos(pitch) * itemBow;
	            double trajectory = Math.sqrt(trajectoryX * trajectoryX + trajectoryY * trajectoryY + trajectoryZ * trajectoryZ);
	            
	            trajectoryX /= trajectory;
	            trajectoryY /= trajectory;
	            trajectoryZ /= trajectory;
	            
	            if (stack.getItem() instanceof ItemBow) {
	            	float bowPower = (72000 - player.getItemInUseCount()) / 20.0F;
	                bowPower = (bowPower * bowPower + bowPower * 2.0F) / 3.0F;
	                if (bowPower > 1.0F)
	                {
	                    bowPower = 1.0F;
	                }
	                bowPower *= 3.0F;
	                trajectoryX *= bowPower;
	                trajectoryY *= bowPower;
	                trajectoryZ *= bowPower;
	            } else {
	            	trajectoryX *= 1.5D;
	                trajectoryY *= 1.5D;
	                trajectoryZ *= 1.5D;
	            }
	            
	            GL11.glPushMatrix();
	            GL11.glDisable(GL11.GL_TEXTURE_2D);
	            GL11.glEnable(GL11.GL_BLEND);
	            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	            GL11.glDisable(GL11.GL_DEPTH_TEST);
	            GL11.glDepthMask(false);
	            GL11.glEnable(GL11.GL_LINE_SMOOTH);
	            GL11.glLineWidth(2.0F);
	            double gravity = stack.getItem() instanceof ItemBow ? 0.05D : 0.03D;
	            GL11.glColor4f(0.0F, 1.0F, 0.2F, 0.5F);
	            GL11.glBegin(GL11.GL_LINE_STRIP);
	            
	            for (int i = 0; i < 2000; i++) {
	                GL11.glVertex3d(posX - mc.getRenderManager().renderPosX, posY - mc.getRenderManager().renderPosY, posZ - mc.getRenderManager().renderPosZ);
	                
	                posX += trajectoryX * 0.1D;
	                posY += trajectoryY * 0.1D;
	                posZ += trajectoryZ * 0.1D;
	                
	                trajectoryX *= 0.999D;
	                trajectoryY *= 0.999D;
	                trajectoryZ *= 0.999D;
	                
	                trajectoryY = (trajectoryY - gravity * 0.1D);
	                Vec3 vec = new Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ);
	                blockCollision = mc.theWorld.rayTraceBlocks(vec, new Vec3(posX, posY, posZ));
	                
	                for (Entity o : mc.theWorld.getLoadedEntityList()) {
	                	if (o instanceof EntityLivingBase && !(o instanceof EntityPlayerSP)) {
	                		entity = (EntityLivingBase) o;
	                		AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand(0.3D, 0.3D, 0.3D);
	                		entityCollision = entityBoundingBox.calculateIntercept(vec, new Vec3(posX, posY, posZ));
	                		
	                		if (entityCollision != null) {
	                			blockCollision = entityCollision;
	                		}
	                		
	                		if (entityCollision != null) {
	                            GL11.glColor4f(1.0F, 0.0F, 0.2F, 0.5F);
	                        }
	                		
	                		if (entityCollision != null) {
	                            blockCollision = entityCollision;
	                        }
	                	}
	                }
	                if (blockCollision != null) {
	                	break;
	                }
	            }
	            GL11.glEnd();
	            double renderX = posX - mc.getRenderManager().renderPosX;
	            double renderY = posY - mc.getRenderManager().renderPosY;
	            double renderZ = posZ - mc.getRenderManager().renderPosZ;
	            GL11.glPushMatrix();
	            GL11.glTranslated(renderX - 0.5D, renderY - 0.5D, renderZ - 0.5D);
	            switch (blockCollision.sideHit.getIndex()) {
	            case 2:
                case 3:
                    GlStateManager.rotate(90, 1, 0, 0);
                    aim = new AxisAlignedBB(0.0D, 0.5D, -1.0D, 1.0D, 0.45D, 0.0D);
                    break;

                case 4:
                case 5:
                    GlStateManager.rotate(90, 0, 0, 1);
                    aim = new AxisAlignedBB(0.0D, -0.5D, 0.0D, 1.0D, -0.45D, 1.0D);
                    break;

                default:
                    aim = new AxisAlignedBB(0.0D, 0.5, 0.0D, 1.0D, 0.45D, 1.0D);
                    break;
	            }
	            
	            drawBox(aim);
	            func_181561_a(aim);
	            GL11.glPopMatrix();
	            GL11.glDisable(GL11.GL_BLEND);
	            GL11.glEnable(GL11.GL_TEXTURE_2D);
	            GL11.glEnable(GL11.GL_DEPTH_TEST);
	            GL11.glDepthMask(true);
	            GL11.glDisable(GL11.GL_LINE_SMOOTH);
	            GL11.glPopMatrix();
	        }
		}
	}
	
	public static void func_181561_a(AxisAlignedBB p_181561_0_) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(3, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(3, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(1, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		tessellator.draw();
	}
	
	public static void drawBox(AxisAlignedBB bb)
    {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();
    }
}
