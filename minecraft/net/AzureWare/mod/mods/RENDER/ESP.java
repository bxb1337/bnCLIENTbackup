package net.AzureWare.mod.mods.RENDER;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class ESP extends Mod {
	public static Value<String> mode = new Value("ESP", "Mode", 0);
	private Value<Boolean> invisible = new Value<Boolean>("ESP_Invisible", false);
	public ESP() {
		super("ESP", Category.RENDER);
		this.mode.addValue("Box");
		this.mode.addValue("ArBox");

		this.mode.addValue("2D");	
		this.mode.addValue("Ar2D");	

		this.mode.addValue("HotRender");
}
	
	@EventTarget
	public void onRender(EventRender event) {
		if(this.mode.isCurrentMode("Box")) {
			this.setDisplayName("Box");
			for(Object o : mc.theWorld.loadedEntityList) {
				if(o instanceof EntityPlayer) {
					EntityPlayer ent = (EntityPlayer)o;
					if(ent != mc.thePlayer && !ent.isDead) {
						if(Teams.isOnSameTeam(ent)) {
							RenderUtil.renderEntity(ent,Color.GREEN.getRGB(),2);
						} else if(ent.isInvisible()) {
							RenderUtil.renderEntity(ent,Color.YELLOW.getRGB(),2);
						} else {
							RenderUtil.renderEntity(ent,Color.WHITE.getRGB(),2);
						}
					}
				}
			}
		}
		if(this.mode.isCurrentMode("ArBox")) {
			this.setDisplayName("ArBox");
			for(Object o : mc.theWorld.loadedEntityList) {
				if(o instanceof EntityPlayer) {
					EntityPlayer ent = (EntityPlayer)o;
					if(ent != mc.thePlayer && !ent.isDead) {
						if(Teams.isOnSameTeam(ent)) {
							RenderUtil.renderEntity(ent,Color.GREEN.getRGB(),1);
						} else if(ent.isInvisible()) {
							RenderUtil.renderEntity(ent,Color.YELLOW.getRGB(),1);
						} else {
							RenderUtil.renderEntity(ent,Color.WHITE.getRGB(),1);
						}
					}
				}
			}
		}
		if(this.mode.isCurrentMode("2D")) {
			this.setDisplayName("2D");
			this.doOther2DESP();
		}
		
		if(this.mode.isCurrentMode("Ar2D")) {
			this.setDisplayName("Ar2D");
			for(Object o : mc.theWorld.loadedEntityList) {
				if(o instanceof EntityPlayer) {
					EntityPlayer ent = (EntityPlayer)o;
					if(ent != mc.thePlayer && !ent.isDead) {
						if(Teams.isOnSameTeam(ent)) {
							RenderUtil.renderEntity(ent,Color.GREEN.getRGB(),3);
						} else if(ent.isInvisible()) {
							RenderUtil.renderEntity(ent,Color.YELLOW.getRGB(),3);
						} else {
							RenderUtil.renderEntity(ent,Color.RED.getRGB(),3);
						}
					}
				}
			}		}
		if(this.mode.isCurrentMode("HotRender")) {
			this.setDisplayName("HotRender");
	//		this.doOther2DESP();
		}
		if (!Client.INSTANCE) Runtime.getRuntime().exit(0);
	}
	
	private boolean isValid(EntityLivingBase entity) {
		return entity == this.mc.thePlayer ? false : (entity.getHealth() <= 0.0F ? false : (entity instanceof EntityPlayer));
	}
	
	public void renderBox(Entity entity,double r,double g, double b) {
		if(entity.isInvisible() && !invisible.getValueState().booleanValue()) {
			return;
		}
		
		double x = RenderUtil.interpolate((double)entity.posX, (double)entity.lastTickPosX);
        double y = RenderUtil.interpolate((double)entity.posY, (double)entity.lastTickPosY);
        double z = RenderUtil.interpolate((double)entity.posZ, (double)entity.lastTickPosZ);
		GL11.glPushMatrix();
		RenderUtil.pre();
        GL11.glLineWidth((float)1.0f);
        GL11.glEnable((int)2848);
        GL11.glColor3d(r,g,b);
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        Minecraft.getMinecraft().getRenderManager();
        RenderGlobal.drawSelectionBoundingBox((AxisAlignedBB)new AxisAlignedBB(
        		entity.boundingBox.minX
                - 0.05
                - entity.posX
                + (entity.posX - mc.getRenderManager().renderPosX),
                entity.boundingBox.minY
                - entity.posY
                + (entity.posY - mc.getRenderManager().renderPosY),
                entity.boundingBox.minZ
                - 0.05
                - entity.posZ
                + (entity.posZ - mc.getRenderManager().renderPosZ),
                entity.boundingBox.maxX
                + 0.05
                - entity.posX
                + (entity.posX - mc.getRenderManager().renderPosX),
                entity.boundingBox.maxY
                + 0.1
                - entity.posY
                + (entity.posY - mc.getRenderManager().renderPosY),
                entity.boundingBox.maxZ
                + 0.05
                - entity.posZ
                + (entity.posZ - mc.getRenderManager().renderPosZ)));
        GL11.glDisable((int)2848);
        RenderUtil.post();
        GL11.glPopMatrix();
        if (!Client.INSTANCE) Runtime.getRuntime().exit(0);
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
	
	private void doOther2DESP() {
        for (final EntityPlayer entity : this.mc.theWorld.playerEntities) {
    		if(entity.isInvisible() && !invisible.getValueState().booleanValue()) {
    			return;
    		}
            if (isValid(entity)) {
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                final float partialTicks = mc.timer.renderPartialTicks;
                this.mc.getRenderManager();
                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().renderPosX;
                this.mc.getRenderManager();
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().renderPosY;
                this.mc.getRenderManager();
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().renderPosZ;
                final float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
                final float DISTANCE_SCALE = Math.min(DISTANCE * 0.15f, 0.15f);
                float SCALE = 0.035f;
                SCALE /= 2.0f;
                final float xMid = (float)x;
                final float yMid = (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f);
                final float zMid = (float)z;
                GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(-SCALE, -SCALE, -SCALE);
                final Tessellator tesselator = Tessellator.getInstance();
                final WorldRenderer worldRenderer = tesselator.getWorldRenderer();
                final float HEALTH = entity.getHealth();
                int COLOR = -1;
                if (HEALTH > 20.0) {
                    COLOR = -65292;
                }
                else if (HEALTH >= 10.0) {
                    COLOR = -16711936;
                }
                else if (HEALTH >= 3.0) {
                    COLOR = -23296;
                }
                else {
                    COLOR = -65536;
                }
                final Color gray = new Color(0, 0, 0);
                final double thickness = 1.5f + DISTANCE * 0.01f;
                final double xLeft = -20.0;
                final double xRight = 20.0;
                final double yUp = 27.0;
                final double yDown = 130.0;
                final double size = 10.0;
                Color color = new Color(255, 255, 255);
                if (entity.hurtTime > 0) {
                    color = new Color(255, 0, 0);
                } else if(Teams.isOnSameTeam(entity)) {
                	color = new Color(0, 255, 0);
                } else if(entity.isInvisible()) {
                	color = new Color(255,255,0);
                }
                drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 0.5f, Colors.BLACK.c, 0);
                drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness, color.getRGB(), 0);
                drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp), (float)xLeft - 2.0f, (float)yDown, 0.15f, Colors.BLACK.c, new Color(100, 100, 100).getRGB());
                drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp) * Math.min(1.0f, entity.getHealth() / 20.0f), (float)xLeft - 2.0f, (float)yDown, 0.15f, Colors.BLACK.c, COLOR);
                drawBorderedRect((float)xLeft, (float)yDown + 2.0f, (float)xRight, (float)yDown + 3.0f + DISTANCE * 0.2f, 0.15f, Colors.BLACK.c, new Color(100, 100, 100).getRGB());
                drawBorderedRect((float)xLeft, (float)yDown + 2.0f, (float)xLeft + (float)(xRight - xLeft) * Math.min(1.0f, entity.getFoodStats().getFoodLevel() / 20.0f), (float)yDown + 3.0f + DISTANCE * 0.2f, 0.15f, Colors.BLACK.c, new Color(0, 150, 255).getRGB());
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GlStateManager.disableBlend();
                GL11.glDisable(3042);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glNormal3f(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }
	
	public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
	      drawRect(x, y, x2, y2, col2);
	      float f = (float)(col1 >> 24 & 255) / 255.0F;
	      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
	      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
	      float f3 = (float)(col1 & 255) / 255.0F;
	      GL11.glEnable(3042);
	      GL11.glDisable(3553);
	      GL11.glBlendFunc(770, 771);
	      GL11.glEnable(2848);
	      GL11.glPushMatrix();
	      GL11.glColor4f(f1, f2, f3, f);
	      GL11.glLineWidth(l1);
	      GL11.glBegin(1);
	      GL11.glVertex2d((double)x, (double)y);
	      GL11.glVertex2d((double)x, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y);
	      GL11.glVertex2d((double)x, (double)y);
	      GL11.glVertex2d((double)x2, (double)y);
	      GL11.glVertex2d((double)x, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y2);
	      GL11.glEnd();
	      GL11.glPopMatrix();
	      GL11.glEnable(3553);
	      GL11.glDisable(3042);
	      GL11.glDisable(2848);
	   }
	
	public static void drawRect(float g, float h, float i, float j, int col1) {
	      float f = (float)(col1 >> 24 & 255) / 255.0F;
	      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
	      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
	      float f3 = (float)(col1 & 255) / 255.0F;
	      GL11.glEnable(3042);
	      GL11.glDisable(3553);
	      GL11.glBlendFunc(770, 771);
	      GL11.glEnable(2848);
	      GL11.glPushMatrix();
	      GL11.glColor4f(f1, f2, f3, f);
	      GL11.glBegin(7);
	      GL11.glVertex2d((double)i, (double)h);
	      GL11.glVertex2d((double)g, (double)h);
	      GL11.glVertex2d((double)g, (double)j);
	      GL11.glVertex2d((double)i, (double)j);
	      GL11.glEnd();
	      GL11.glPopMatrix();
	      GL11.glEnable(3553);
	      GL11.glDisable(3042);
	      GL11.glDisable(2848);
	}

}
