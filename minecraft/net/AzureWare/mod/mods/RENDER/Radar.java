package net.AzureWare.mod.mods.RENDER;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventRender2D;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;

import java.util.Base64;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Radar extends Mod
{
    public int scale;
    private boolean dragging;
    float hue;
    private TimeHelper timer;
    private Value Asize = new Value("Radar_Size", Double.valueOf(80.0D), Double.valueOf(30.0D), Double.valueOf(200.0D), 1.0D);
    public Radar() {
        super("Radar", Category.RENDER);
        this.timer = new TimeHelper();
    }
	@EventTarget
    public void onRender2D(final EventRender2D event) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.scale = 2;
        final int size = ((Double)Asize.getValueState()).intValue();
        final float xOffset = sr.getScaledWidth() - size - 120;
        final float yOffset = 2;
        final float playerOffsetX = (float)Minecraft.getMinecraft().thePlayer.posX;
        final float playerOffsetZ = (float)Minecraft.getMinecraft().thePlayer.posZ;
        final int width = sr.getScaledWidth();
        final int height = sr.getScaledHeight();
        this.rectangleBordered(xOffset, yOffset, xOffset + size, yOffset + size, 0.5, Colors.getColor(90), Colors.getColor(0));
        this.rectangleBordered(xOffset + 1.0f, yOffset + 1.0f, xOffset + size - 1.0f, yOffset + size - 1.0f, 1.0, Colors.getColor(90), Colors.getColor(61));
        this.rectangleBordered((double)xOffset + 2.5, (double)yOffset + 2.5, (double)(xOffset + size) - 2.5, (double)(yOffset + size) - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
        this.rectangleBordered(xOffset + 3.0f, yOffset + 3.0f, xOffset + size - 3.0f, yOffset + size - 3.0f, 0.5, Colors.getColor(27), Colors.getColor(61));
        this.drawRect((double)xOffset + (size / 2 - 0.5), (double)yOffset + 3.5, (double)xOffset + (size / 2 + 0.5), (double)(yOffset + size) - 3.5, Colors.getColor(255, 80));
        this.drawRect((double)xOffset + 3.5, (double)yOffset + (size / 2 - 0.5), (double)(xOffset + size) - 3.5, (double)yOffset + (size / 2 + 0.5), Colors.getColor(255, 80));
        for (final Object obj : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
            if (obj instanceof EntityPlayer) {
                final EntityPlayer ent = (EntityPlayer)obj;
                if (ent == Minecraft.getMinecraft().thePlayer || ent.isInvisible()) {
                    continue;
                }
                final float pTicks = this.mc.timer.renderPartialTicks;
                final float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * pTicks - playerOffsetX) * this.scale);
                final float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * pTicks - playerOffsetZ) * this.scale);
                int color;
                if (Teams.isOnSameTeam(ent)) {
                    color = new Color(0, 255, 0).getRGB();
                }
                else {
                    color = (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(ent) ? new Color(255, 0, 0).getRGB() : new Color(255, 255, 0).getRGB());
                }
                final float cos = (float)Math.cos((double)Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292519943295);
                final float sin = (float)Math.sin((double)Minecraft.getMinecraft().thePlayer.rotationYaw * 0.017453292519943295);
                float rotY = -(posZ * cos - posX * sin);
                float rotX = -(posX * cos + posZ * sin);
                if (rotY > size / 2 - 5) {
                    rotY = size / 2 - 5.0f;
                }
                else if (rotY < -(size / 2 - 5)) {
                    rotY = -(size / 2 - 5);
                }
                if (rotX > size / 2 - 5.0f) {
                    rotX = size / 2 - 5;
                }
                else if (rotX < -(size / 2 - 5)) {
                    rotX = -(size / 2 - 5.0f);
                }
                this.rectangleBordered((double)(xOffset + size / 2 + rotX) - 1.5, (double)(yOffset + size / 2 + rotY) - 1.5, (double)(xOffset + size / 2 + rotX) + 1.5, (double)(yOffset + size / 2 + rotY) + 1.5, 0.5, color, Colors.getColor(46));
            }
        }
    }
    
    public void circle(final float x, final float y, final float radius, final int fill) {
        this.arc(x, y, 0.0f, 360.0f, radius, fill);
    }
    
    public void arc(final float x, final float y, final float start, final float end, final float radius, final int color) {
        this.arcEllipse(x, y, start, end, radius, radius, color);
    }
    
    public void arcEllipse(final float x, final float y, float start, float end, final float w, final float h, final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final Tessellator var15 = Tessellator.getInstance();
        final WorldRenderer var16 = var15.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
                final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w;
            final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        this.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void drawRoundedRect(float x, float y, float x2, float y2, final float round, final int color) {
        x += round / 2.0f + 0.5;
        y += round / 2.0f + 0.5;
        x2 -= round / 2.0f + 0.5;
        y2 -= round / 2.0f + 0.5;
        this.drawRect(x, y, x2, y2, color);
        this.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        this.circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        this.circle(x + round / 2.0f, y + round / 2.0f, round, color);
        this.circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        this.drawRect(x - round / 2.0f - 0.5f, y + round / 2.0f, x2, y2 - round / 2.0f, color);
        this.drawRect(x, y + round / 2.0f, x2 + round / 2.0f + 0.5f, y2 - round / 2.0f, color);
        this.drawRect(x + round / 2.0f, y - round / 2.0f - 0.5f, x2 - round / 2.0f, y2 - round / 2.0f, color);
        this.drawRect(x + round / 2.0f, y, x2 - round / 2.0f, y2 + round / 2.0f + 0.5f, color);
    }
    
    public void drawRect(final double x1, final double y1, final double x2, final double y2, final int color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        this.color(color);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public void drawRect(final float x1, final float y1, final float x2, final float y2, final int color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        this.color(color);
        GL11.glBegin(7);
        GL11.glVertex2d((double)x2, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x1, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public void color(final int color) {
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
    }
}
