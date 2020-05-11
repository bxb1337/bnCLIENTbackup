package net.minecraft.client.gui;

import java.awt.Color;

import net.AzureWare.Client;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

public class GuiButton
extends Gui {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    protected int width = 200;
    protected int height = 20;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled = true;
    public boolean visible = true;
    protected boolean hovered;
    protected double animation;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        this.animation = 0.10000000149011612;
    }

    protected int getHoverState(boolean mouseOver) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }
        return i;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.animation = RenderUtil.getAnimationState(this.animation, this.hovered ? 0.2f : 0.1f, 0.5);
            RenderUtil.drawRect(this.xPosition, this.yPosition + 2, this.xPosition + this.width, (float)(this.yPosition + this.height) + 1.5f, new Color(0x006fb6).brighter().getRGB());
            RenderUtil.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0xFF262626);
            if (this.enabled) {
                if (!(this instanceof GuiOptionSlider)) RenderUtil.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, (float)(this.yPosition + this.height) + 1.5f, ClientUtil.reAlpha(Colors.BLACK.c, (float)this.animation * 2.0f));
            } else {
            	RenderUtil.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, ClientUtil.reAlpha(Colors.WHITE.c, 0.1f));
            }
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;
            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }
            UnicodeFontRenderer font = Client.getInstance().getFontManager().VERDANA17;
            fontrenderer.drawString(ClientUtil.removeColorCode(this.displayString), (float)this.xPosition + ((float)this.width - fontrenderer.getStringWidth(ClientUtil.removeColorCode(this.displayString))) / 2.0f, this.yPosition + (this.height - 12) / 2 + 3, Colors.WHITE.c);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
            return true;
        }
        return false;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

