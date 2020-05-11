package net.AzureWare.ui.buttons;

import org.lwjgl.opengl.GL11;

import net.AzureWare.Client;
import net.AzureWare.ui.UIDesignSettings;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.FlatColors;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class UIPopUPChooseButton {
	public float x;
    public float y;
    public float currentRadius;
    public float minRadius;
    public float maxRadius;
    private boolean open;
    private boolean animateUp;
    private boolean animateDown;
    private MouseInputHandler mouseClickedPopUpMenu = new MouseInputHandler(0);
    private double animationScale;
    public String textureLocation;
    public String name;

    public UIPopUPChooseButton(String name, int x, int y, String textureLocation) {
        this.name = name;
        this.maxRadius = 15.0f;
        this.textureLocation = textureLocation;
        this.x = x;
        this.y = y;
    }

    public void draw(int mouseX, int mouseY) {
        this.maxRadius = 12.0f;
        float add = RenderUtil.delta * 100.0f;
        this.currentRadius = this.currentRadius + add < this.maxRadius ? (this.currentRadius += add) : this.maxRadius;
        this.animationScale = RenderUtil.getAnimationState(this.animationScale, this.isHovering(mouseX, mouseY) ? 1.05 : 1.0, 1.0);
        if (this.animationScale < 1.0) {
            this.animationScale = 1.0;
        }
        float xMid = this.x + this.maxRadius / 2.0f;
        float yMid = this.y + this.maxRadius / 2.0f;
        GL11.glPushMatrix();
        GL11.glTranslated((double)xMid, (double)yMid, (double)0.0);
        if (this.isHovering(mouseX, mouseY)) {
            GL11.glScaled((double)this.animationScale, (double)this.animationScale, (double)0.0);
        }
        GL11.glTranslated((double)(- xMid), (double)(- yMid), (double)0.0);
        if (this.currentRadius > 1.0f) {
            Gui.circle(this.x, this.y, this.currentRadius, UIDesignSettings.getColor());
            if (this.isHovering(mouseX, mouseY)) {
                Gui.drawFilledCircle(this.x, this.y, this.currentRadius + 0.5f, ClientUtil.reAlpha(Colors.BLACK.c, 0.1f));
            }
            RenderUtil.drawImage(new ResourceLocation(this.textureLocation), (int)(this.x - this.currentRadius / 2.0f), (int)(this.y - this.currentRadius / 2.0f), (int)this.currentRadius, (int)this.currentRadius);
        }
        GL11.glPopMatrix();
        if (this.currentRadius == this.maxRadius && this.isHovering(mouseX, mouseY)) {
        	FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
            int xFont = (int)(this.x + this.maxRadius + 5.0f);
            int yFont = (int)(this.y - this.maxRadius / 4.0f - 1.0f);
            RenderUtil.drawRoundedRect(xFont - 2, yFont - 1, xFont + font.getStringWidth(this.name) + 2, yFont + font.FONT_HEIGHT + 1, 1.0f, FlatColors.ASPHALT.c);
            font.drawString(this.name, (float)xFont, yFont, FlatColors.GREY.c);
        }
    }

    public boolean clicked(int mouseX, int mouseY) {
        if (this.currentRadius == this.maxRadius && this.mouseClickedPopUpMenu.canExcecute() && this.isHovering(mouseX, mouseY)) {
            return true;
        }
        return false;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if ((float)mouseX >= this.x - this.currentRadius && (float)mouseX <= this.x + this.currentRadius && (float)mouseY >= this.y - this.currentRadius && (float)mouseY <= this.y + this.currentRadius) {
            return true;
        }
        return false;
    }

    public void setY(float y) {
        this.y = y;
    }
}
