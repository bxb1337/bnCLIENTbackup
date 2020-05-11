package net.AzureWare.ui.clickgui.menu;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.AzureWare.Client;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UIMenuCategory {
	public Mod.Category c;
    UIMenuMods uiMenuMods;
    private MouseInputHandler handler;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    public int x2;
    public int y2;
    public boolean drag = true;
    private double arrowAngle = 0.0;
    
    
    public UIMenuCategory(Mod.Category c, int x, int y, int width, int tab_height, MouseInputHandler handler) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.tab_height = tab_height;
        this.uiMenuMods = new UIMenuMods(c, handler);
        this.handler = handler;
    }

    public void draw(int mouseX, int mouseY) {
        boolean hoverArrow;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        RenderUtil.drawRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.tab_height), ClientUtil.reAlpha(-8487037,0.5f));
        String name = "";
        name = "     " + this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length()) + "     ";
        font.drawStringWithShadow(name, (float)(this.x + (this.width - font.getStringWidth(name)) / 2), (float)(this.y + (this.tab_height - font.FONT_HEIGHT) / 2), -1);
        double xMid = this.x + this.width - 10 + 2;
        double yMid = this.y + 12 + 2;
        this.arrowAngle = RenderUtil.getAnimationState((double)this.arrowAngle, (double)(this.uiMenuMods.open ? -90 : 0), (double)1000.0);
        GL11.glPushMatrix();
        GL11.glTranslated((double)xMid, (double)yMid, (double)0.0);
        GL11.glRotated((double)this.arrowAngle, (double)0.0, (double)0.0, (double)1.0);
        GL11.glTranslated((double)(- xMid), (double)(- yMid), (double)0.0);
        boolean bl = hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
        if (hoverArrow) {
            RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/arrow_down.png"), (int)(this.x + this.width - 10), (int)(this.y + 12), (int)5, (int)5, (Color)new Color(0.7058824f, 0.7058824f, 0.7058824f));
        } else {
            RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/arrow_down.png"), (int)(this.x + this.width - 10), (int)(this.y + 12), (int)5, (int)5);
        }
        GL11.glPopMatrix();
        /*
        if(c == Mod.Category.COMBAT) {
        	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/category/combat.png"), (int)(this.x + 5), (int)(this.y + 8), (int)14, (int)14);
        } else if(c == Mod.Category.MOVEMENT) {
        	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/category/movement.png"), (int)(this.x + 5), (int)(this.y + 8), (int)14, (int)14);
        } else if(c == Mod.Category.RENDER) {
        	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/category/render.png"), (int)(this.x + 5), (int)(this.y + 7), (int)14, (int)14);
        } else if(c == Mod.Category.PLAYER) {
        	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/category/miscellaneous.png"), (int)(this.x + 5), (int)(this.y + 8), (int)14, (int)14);
        } else if(c == Mod.Category.WORLD) {
        	RenderUtil.drawImage((ResourceLocation)new ResourceLocation("Client/category/exploit.png"), (int)(this.x + 5), (int)(this.y + 8), (int)14, (int)14);
        }*/
        this.upateUIMenuMods();
        this.width = font.getStringWidth(name) + 40;
        this.uiMenuMods.draw(mouseX, mouseY);
        this.move(mouseX, mouseY);
    }

    private void move(int mouseX, int mouseY) {
        boolean hoverArrow;
        boolean bl = hoverArrow = mouseX >= this.x + this.width - 15 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 7 && mouseY <= this.y + 17;
        if (!hoverArrow && this.isHovering(mouseX, mouseY) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }
        if (hoverArrow && this.handler.canExcecute()) {
            boolean bl2 = this.uiMenuMods.open = !this.uiMenuMods.open;
        }
        if (!Mouse.isButtonDown((int)0)) {
            this.drag = false;
        }
        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.tab_height) {
            return true;
        }
        return false;
    }

    private void upateUIMenuMods() {
        this.uiMenuMods.x = this.x;
        this.uiMenuMods.y = this.y;
        this.uiMenuMods.tab_height = this.tab_height;
        this.uiMenuMods.width = this.width;
    }

    public void mouseClick(int mouseX, int mouseY) {
        this.uiMenuMods.mouseClick(mouseX, mouseY);
    }

    public void mouseRelease(int mouseX, int mouseY) {
        this.uiMenuMods.mouseRelease(mouseX, mouseY);
    }
}
