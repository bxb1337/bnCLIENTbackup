package net.AzureWare.utils.fontmanager;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class UnicodeFontRenderer extends FontRenderer {
	private final UnicodeFont font;
	HashMap<String, Float> widthMap = new HashMap<>();
	HashMap<String, Float> heightMap = new HashMap<>();
	
	public UnicodeFontRenderer(Font awtFont) {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
				Minecraft.getMinecraft().getTextureManager(), false);
		this.font = new UnicodeFont(awtFont);
		this.font.addAsciiGlyphs();
		this.font.getEffects().add(new ColorEffect(Color.WHITE));

		try {
			this.font.loadGlyphs();
		} catch (SlickException var3) {
			throw new RuntimeException(var3);
		}

		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
	}

	public float drawString(String string, int x, int y, int color) {
		return this.drawString(string, (float) x, (float) y, color);
	}

	public int drawString(String string, float x, float y, int color) {
		if (string == null) {
			return 0;
		} else {
			GL11.glPushMatrix();
			GL11.glScaled(0.5D, 0.5D, 0.5D);
			boolean blend = GL11.glIsEnabled(3042);
			boolean lighting = GL11.glIsEnabled(2896);
			boolean texture = GL11.glIsEnabled(3553);
			if (!blend) {
				GL11.glEnable(3042);
			}

			if (lighting) {
				GL11.glDisable(2896);
			}

			if (texture) {
				GL11.glDisable(3553);
			}

			x *= 2.0F;
			y *= 2.0F;
			this.font.drawString(x, y, string, new org.newdawn.slick.Color(color));
			if (texture) {
				GL11.glEnable(3553);
			}

			if (lighting) {
				GL11.glEnable(2896);
			}

			if (!blend) {
				GL11.glDisable(3042);
			}

			GlStateManager.color(0.0F, 0.0F, 0.0F);
			GL11.glPopMatrix();
			GlStateManager.bindTexture(0);
			return ((int) x);
		}
	}

	public int drawStringWithShadow(String text, float x, float y, int color) {
		this.drawString(text, x + 1.0F, y + 1.0F, -16777216);
		return this.drawString(text, x, y, color);
	}

	public int getCharWidth(char c) {
		return this.getStringWidth(Character.toString(c));
	}

	public int getStringWidth(String string) {
		if (widthMap.containsKey(string)) {
			return widthMap.get(string).intValue();
		}else {
			float width = (float) (this.font.getWidth(string) / 2);
			widthMap.put(string, width);
			return (int) width;
		}
	}

	public float getStringHeight(String string) {
		if (heightMap.containsKey(string)) {
			return heightMap.get(string);
		}else {
			float height = (float) (this.font.getHeight(string) / 2);
			heightMap.put(string, height);
			return height;
		}
	}

	public void drawCenteredString(String text, float x, float y, int color) {
		this.drawString(text, x - (float) (this.getStringWidth(text) / 2), y, color);
	}
}
