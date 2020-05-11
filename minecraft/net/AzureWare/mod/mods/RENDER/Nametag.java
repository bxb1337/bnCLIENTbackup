package net.AzureWare.mod.mods.RENDER;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.COMBAT.AntiBot;
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.GLUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.value.Value;
import net.minecraft.Wrapper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class Nametag extends Mod {
	
	private Value<Boolean> invisible = new Value<Boolean>("NameTag_Invisible", false);
	private Value<Boolean> uhc = new Value<Boolean>("NameTag_HypixelUHC", false);

	public Nametag() {
		super("NameTag", Category.RENDER);
	}
	@EventTarget
	public void onRender(EventRender event) {
		for (Object o : mc.theWorld.playerEntities) {
			EntityPlayer p = (EntityPlayer) o;
			if(p != mc.thePlayer) {
				double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosX;
                double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosY;
                double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks
                        - RenderManager.renderPosZ;
                renderNameTag(p, p.getName(), pX, pY, pZ);
			}
		}
	}
	
	private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
		if(entity.isInvisible() && !invisible.getValueState().booleanValue()) {
			return;
		}
		if(entity.noClip && uhc.getValueState().booleanValue()) {
			return;
		}
        FontRenderer fr = mc.fontRendererObj;
        float size = mc.thePlayer.getDistanceToEntity(entity) / 6.0f;
		if(size < 0.8f) {
			size = 0.8f;
		}
		pY += (entity.isSneaking() ? 0.5D : 0.7D);
		float scale = size * 2.0f;
		scale /= 100f;
        tag = entity.getDisplayName().getUnformattedText();
        
        String bot = "";
        
        
		if(AntiBot.isBot(entity) ) {
			bot = "\2479[BOT]";
		} else {
			bot = "";
		}
		

		
		String team = "";
		if(Teams.isOnSameTeam(entity)) {
			team = "\247b[TEAM]";
		} else {
			team = "";
		}
	

		if((team + bot).equals("")) team = "\247a";
		String lol = team + bot + tag;
		String hp = "\2477HP:" + (int)entity.getHealth();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        int width = mc.fontRendererObj.getStringWidth(lol) / 2;
        GLUtil.setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);

        drawBorderedRectNameTag(-width - 2, -(mc.fontRendererObj.FONT_HEIGHT + 9), width + 2, 2.0F, 1.0F,
        		ClientUtil.reAlpha(Color.BLACK.getRGB(), 0.3f), ClientUtil.reAlpha(Color.BLACK.getRGB(), 0.3f));
        GL11.glColor3f(1,1,1);
		fr.drawString(lol, -width, -(mc.fontRendererObj.FONT_HEIGHT + 8), -1);
		fr.drawString(hp, -mc.fontRendererObj.getStringWidth(hp) / 2, -(mc.fontRendererObj.FONT_HEIGHT - 2), -1);
		int COLOR = new Color(188,0,0).getRGB();
		if(entity.getHealth() > 20) {
			COLOR = -65292;
		}
		float nowhealth = (float) Math.ceil(entity.getHealth() + entity.getAbsorptionAmount());
		float health = nowhealth / (entity.getMaxHealth() + entity.getAbsorptionAmount());
		
		RenderUtil.drawRect(width + health * width*2 - width*2 +2, 2f , -width -2, 0.9f, COLOR);
        GL11.glPushMatrix();
		if (true) {
			int xOffset = 0;
			for (ItemStack armourStack : entity.inventory.armorInventory) {
				if (armourStack != null)
					xOffset -= 11;
			}
			Object renderStack;
			if (entity.getHeldItem() != null) {
				xOffset -= 8;
				renderStack = entity.getHeldItem().copy();
				if ((((ItemStack) renderStack).hasEffect())
						&& (((((ItemStack) renderStack).getItem() instanceof ItemTool))
								|| ((((ItemStack) renderStack).getItem() instanceof ItemArmor))))
					((ItemStack) renderStack).stackSize = 1;
				renderItemStack((ItemStack) renderStack, xOffset, -35);
				xOffset += 20;
			}
			for (ItemStack armourStack : entity.inventory.armorInventory)
				if (armourStack != null) {
					ItemStack renderStack1 = armourStack.copy();
					if ((renderStack1.hasEffect()) && (((renderStack1.getItem() instanceof ItemTool))
							|| ((renderStack1.getItem() instanceof ItemArmor))))
						renderStack1.stackSize = 1;
					renderItemStack(renderStack1, xOffset, -35);
					xOffset += 20;
				}
		}
        GL11.glPopMatrix();
        GLUtil.revertAllCaps();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
	
	public void renderItemStack(ItemStack stack, int x, int y) {
		GL11.glPushMatrix();
		GL11.glDepthMask(true);
		GlStateManager.clear(256);
		net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
		this.mc.getRenderItem().zLevel = -150.0F;
		whatTheFuckOpenGLThisFixesItemGlint();
		this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);
		this.mc.getRenderItem().zLevel = 0.0F;
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.5D, 0.5D, 0.5D);
		GlStateManager.disableDepth();
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		GL11.glPopMatrix();
	}
	
	private void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }
	
	public void drawBorderedRectNameTag(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        RenderUtil.drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
	
	
}
