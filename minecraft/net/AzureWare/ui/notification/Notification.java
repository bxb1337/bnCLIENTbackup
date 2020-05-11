package net.AzureWare.ui.notification;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.AzureWare.Client;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class Notification {
	
	private String message;
	private TimeHelper timer;
	private double lastY, posY, width, height, animationX;
	private int color, imageWidth;
	private ResourceLocation image;
	private long stayTime;

	public Notification(String message, Type type) {
		this.message = message;
		timer = new TimeHelper();
		timer.reset();
		width = Client.instance.getFontManager().comfortaa16.getStringWidth(message) + 35;
		height = 20;
		animationX = width;
		stayTime = 1000;
		imageWidth = 16;
		posY = -1;
		image = new ResourceLocation("Client/notification/" + type.name().toUpperCase() + ".png");
		if (type.equals(Type.INFO))
			color = Colors.DARKGREY.c;
		else if (type.equals(Type.ERROR))
			color = new Color(36, 36, 36).getRGB();
		else if (type.equals(Type.SUCCESS))
			color = new Color(36, 36, 36).getRGB();
		else if (type.equals(Type.WARNING))
			color = Colors.DARKGREY.c;
	}

	public void draw(double getY, double lastY) {
		width = Client.instance.getFontManager().comfortaa15.getStringWidth(message) + 45;
		height = 22;
		imageWidth = 11;
		this.lastY = lastY;
		animationX = this.getAnimationState(animationX, isFinished() ? width : 0, Math.max(isFinished() ? 200 : 30, Math.abs(animationX - (isFinished() ? width : 0)) * 20) * 0.3);
		if(posY == -1)
			posY = getY;
		else
			posY = this.getAnimationState(posY, getY, 200);
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		int x1 = (int) (res.getScaledWidth() - width + animationX), x2 = (int) (res.getScaledWidth() + animationX), y1 = (int) posY - 22, y2 = (int) (y1 + height);
		GL11.glPushMatrix();
		RenderUtil.drawRect(x1, y1, x2, y2, ClientUtil.reAlpha(color, 0.85f));
		this.drawImage(image, (int)(x1 + (height - imageWidth) / 2F) - 1, y1 + (int)((height - imageWidth) / 2F), imageWidth, imageWidth);
		
		y1 += 1;
		if(message.contains(" Enabled")) {
			Client.instance.getFontManager().comfortaa15.drawString(message.replace(" Enabled", ""), (float)(x1 + 19), (float)(y1 + height / 4F), -1);
			Client.instance.getFontManager().comfortaa15.drawString(" Enabled", (float)(x1 + 20 + Client.instance.getFontManager().comfortaa15.getStringWidth(message.replace(" Enabled", ""))), (float)(y1 + height / 4F), Colors.GREY.c);
		} else if(message.contains(" Disabled")) {
			Client.instance.getFontManager().comfortaa15.drawString(message.replace(" Disabled", ""), (float)(x1 + 19), (float)(y1 + height / 4F), -1);
			Client.instance.getFontManager().comfortaa15.drawString(" Disabled", (float)(x1 + 20 + Client.instance.getFontManager().comfortaa15.getStringWidth(message.replace(" Disabled", ""))), (float)(y1 + height / 4F), Colors.GREY.c);
		} else {
			Client.instance.getFontManager().comfortaa15.drawString(message, (float)(x1 + 20), (float)(y1 + height / 4F), -1);
		}
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
	}
	
	public boolean shouldDelete() {
		return isFinished() && animationX >= width;
	}

	private boolean isFinished() {
		return timer.isDelayComplete(stayTime) && posY == lastY;
	}
	
	public double getHeight() {
		return height;
	}

	public enum Type {
		SUCCESS, INFO, WARNING, ERROR
	}
	
	public double getAnimationState(double animation, double finalState, double speed) {
		float add = (float) (RenderUtil.delta * speed);
		if (animation < finalState) {
			if (animation + add < finalState)
				animation += add;
			else
				animation = finalState;
		} else {
			if (animation - add > finalState)
				animation -= add;
			else
				animation = finalState;
		}
		return animation;
	}
	
	public void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
