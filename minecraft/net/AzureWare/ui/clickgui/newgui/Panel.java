package net.AzureWare.ui.clickgui.newgui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.lwjgl.util.Color;

import net.AzureWare.Client;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.RenderUtil;

public class Panel {
    private long lastFrame;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width = 85;
    private int height = 10;
    private Mod.Category cat;
    private boolean isOpen;
    private ModulePanel modulePanel;
    private int openValueY;
    private int allModules;
    private static final float SPEED = 100.0f;
    private float yAdd = -this.width;

    public Panel(int x, int y, Mod.Category cat) {
        this.x = x;
        this.y = y;
        this.cat = cat;
        this.modulePanel = new ModulePanel(cat, x * 2, y, this.width, this.width, y, 0);
        this.allModules = this.getAllModulesSize();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.update(mouseX, mouseY);
        Color color = new Color(41, 128, 185, 1);
        this.yAddCalc(this.getDelta());
        this.dropShadow();
        if (this.isOpen) {
            this.updateModulePanel(this.x, this.y + this.height + (int)this.yAdd, this.width, this.height, this.y, this.yAdd);
            this.modulePanel.drawScreen(mouseX, mouseY, partialTicks, this.getDelta());
        }
        GuiIngame.drawRect((int)(this.x - 2), (int)this.y, (int)(this.x + this.width + 2), (int)(this.y + this.height + 1), (int)Colors.BLACK.c);
        GuiIngame.drawRect((int)(this.x - 2), (int)this.y, (int)(this.x + this.width + 2), (int)(this.y + this.height), ClientUtil.reAlpha((int)-16230783, 0.5f));
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.cat.name(), (float)this.x + (float)this.width / 2.0f - this.getStringWidth() / 2.0f, (float)this.y + (float)this.height / 2.0f - this.getStringHeight() / 2.0f - 2.0f, -1);
        this.openValueY = (int)((float)this.modulePanel.yAxis + this.modulePanel.yAdd);
    }

    private float getDelta() {
        return 10;
    }

    private int getAllModulesSize() {
        int mod = 0;
        for (Mod m : ModManager.getModList()) {
            if (!this.cat.equals((Object)m.getCategory())) continue;
            ++mod;
        }
        return mod;
    }

    private void dropShadow() {
        RenderUtil.drawRect((float)((float)this.x + 3.0f), (float)((float)this.y + 3.0f), (float)((float)(this.x + this.width) + 3.0f), (float)((float)(this.y + this.height) + 3.0f), (int)285212672);
        RenderUtil.drawRect((float)((float)this.x + 2.5f), (float)((float)this.y + 2.5f), (float)((float)(this.x + this.width) + 2.5f), (float)((float)(this.y + this.height) + 2.5f), (int)570425344);
        RenderUtil.drawRect((float)((float)this.x + 2.0f), (float)((float)this.y + 2.0f), (float)((float)(this.x + this.width) + 2.0f), (float)((float)(this.y + this.height) + 2.0f), (int)855638016);
        RenderUtil.drawRect((float)((float)this.x + 1.5f), (float)((float)this.y + 1.5f), (float)((float)(this.x + this.width) + 1.5f), (float)((float)(this.y + this.height) + 1.5f), (int)1140850688);
        RenderUtil.drawRect((float)((float)this.x + 1.0f), (float)((float)this.y + 1.0f), (float)((float)(this.x + this.width) + 1.0f), (float)((float)(this.y + this.height) + 1.0f), (int)1426063360);
        RenderUtil.drawRect((float)((float)this.x + 0.5f), (float)((float)this.y + 0.5f), (float)((float)(this.x + this.width) + 0.5f), (float)((float)(this.y + this.height) + 0.5f), (int)1711276032);
    }

    private void yAddCalc(float delta) {
        if (this.yAdd < 0.0f && this.isOpen) {
            this.yAdd += 100.0f * delta;
        }
        if (this.yAdd > 0.0f) {
            this.yAdd = 0.0f;
        }
    }

    private float getStringWidth() {
    	return Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.cat.name()) - 2;
    }

    private float getStringHeight() {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 6;
    }

    private void update(int mouseX, int mouseY) {
        if (ClickGUI.move == this.cat) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }

    private void updateModulePanel(int x, int y, int width, int height, int panelY, float yAdd) {
        this.modulePanel.update(x, y, width, height, panelY, yAdd);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.isHovering(mouseX, mouseY, this.x, this.y, this.x + this.width, this.y + this.height)) {
            if (mouseButton == 0) {
                ClickGUI.move = this.cat;
                this.x2 = mouseX - this.x;
                this.y2 = mouseY - this.y;
            }
            if (mouseButton == 1 && !(this.isOpen = !this.isOpen)) {
                this.yAdd = -this.openValueY;
            }
        }
        if (this.isOpen) {
            this.modulePanel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    private int categoryModsSize() {
        int cool = 0;
        for (Mod m : ModManager.getModList()) {
            if (m.getCategory() != this.cat) continue;
            ++cool;
        }
        return cool;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.isOpen) {
            this.modulePanel.mouseReleased(mouseX, mouseY, state);
        }
    }

    private boolean isHovering(int mouseX, int mouseY, int xLeft, int yUp, int xRight, int yBottom) {
        return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Mod.Category getCat() {
        return this.cat;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
