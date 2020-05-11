package net.AzureWare.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

public class R2DUtil{

       public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
            R2DUtil.enableGL2D();
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            R2DUtil.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
            R2DUtil.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
            R2DUtil.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
            R2DUtil.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
            R2DUtil.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
            R2DUtil.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
            R2DUtil.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
            R2DUtil.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
            Gui.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            R2DUtil.disableGL2D();
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
	public static double[] getScreenCoords(final double x, final double y, final double z) {
		final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
		final IntBuffer viewport = BufferUtils.createIntBuffer(16);
		final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
		final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(2982, modelView);
		GL11.glGetFloat(2983, projection);
		GL11.glGetInteger(2978, viewport);
		final boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
		if (result) {
			final double[] coords = { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
			return coords;
		}
		return null;
	}

	/**
     * Enables 2D GL constants for 2D rendering.
     */
    public static void enableGL2D() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(true);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
    }

    /**
     * Disables 2D GL constants for 2D rendering.
     */
    public static void disableGL2D() {
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }
    /**
     * Draws a rectangle at the coordinates specified with the hexadecimal color.
     */
    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        enableGL2D();
        x *= 2; x1 *= 2; y *= 2; y1 *= 2;
        glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(x, y, y1 , borderC);
        drawVLine(x1 - 1, y , y1, borderC);
        drawHLine(x, x1 - 1, y, borderC);
        drawHLine(x, x1 - 2, y1 -1, borderC);
        Gui.drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }
    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        Gui.drawRect(x, x1, y + 1, x1 + 1, y1);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        Gui.drawRect(x, y + 1, x + 1, x1, y1);
    }
    public static void drawHLine(float x, float y, float x1, int y1, int y2) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        drawGradientRect(x, x1, y + 1, x1 + 1, y1, y2);
    }
    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        enableGL2D();
        glShadeModel(GL_SMOOTH);
        glBegin(GL_QUADS);
        glColor(topColor);
        glVertex2f(x, y1);
        glVertex2f(x1, y1);
        glColor(bottomColor);
        glVertex2f(x1, y);
        glVertex2f(x, y);
        glEnd();
        glShadeModel(GL_FLAT);
        disableGL2D();
    }
    
    public static Color glColor(int color, float alpha) {
        int hex = color;
        float red = (hex >> 16 & 255) / 255.0F;
        float green = (hex >> 8 & 255) / 255.0F;
        float blue = (hex & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
    
    public static Color glColor(int hex) {
        float alpha = (hex >> 24 & 255) / 256.0F;
        float red = (hex >> 16 & 255) / 255.0F;
        float green = (hex >> 8 & 255) / 255.0F;
        float blue = (hex & 255) / 255.0F;
        glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
    
	public void glColor(Color color) {
        glColor4f((color.getRed() / 255F), (color.getGreen() / 255F), (color.getBlue() / 255F), (color.getAlpha() / 255F));
    }
    
    public static void drawRect(float x, float y, float x1, float y1) {
        glBegin(GL_QUADS);
        glVertex2f(x, y1);
        glVertex2f(x1, y1);
        glVertex2f(x1, y);
        glVertex2f(x, y);
        glEnd();
    }
}