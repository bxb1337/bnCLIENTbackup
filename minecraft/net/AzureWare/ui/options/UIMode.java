package net.AzureWare.ui.options;

import net.AzureWare.Client;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.handler.MouseInputHandler;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class UIMode {
	private int height;
    public int width;
    private Value value;
    private MouseInputHandler handler;

    public UIMode(Value value, MouseInputHandler handler, int width, int height) {
        this.value = value;
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, int x, int y) {
        this.setNextMode(mouseX, mouseY, x, y);
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        String displayText = String.valueOf((Object)this.value.getModeTitle()) + " " + this.value.getModeAt(this.value.getCurrentMode());
        String modeCountText = String.valueOf((int)(this.value.getCurrentMode() + 1)) + "/" + this.value.mode.size();
        if (this.isHovering(mouseX, mouseY, x, y)) {
            Gui.drawRect((int)x, (int)y, (int)(x + this.width), (int)(y + this.height), (int)ClientUtil.reAlpha((int)Colors.BLACK.c, (float)0.35f));
        }
        font.drawString(displayText, (float)x + (float)(this.width - font.getStringWidth(displayText)) / 2.0f + 0.5f, (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0f + 0.5f, Colors.BLACK.c);
        font.drawString(displayText, (float)x + (float)(this.width - font.getStringWidth(displayText)) / 2.0f, (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0f, -1);
        font.drawString(modeCountText, (float)(x + this.width - font.getStringWidth(modeCountText) - 4) + 0.5f, (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0f + 0.5f, Colors.BLACK.c);
        font.drawString(modeCountText, (float)(x + this.width - font.getStringWidth(modeCountText) - 4), (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0f, -1);
    }

    private void setNextMode(int mouseX, int mouseY, int x, int y) {
        if (this.isHovering(mouseX, mouseY, x, y) && this.handler.canExcecute()) {
            if (this.value.getCurrentMode() < this.value.mode.size() - 1) {
                this.value.setCurrentMode(this.value.getCurrentMode() + 1);
            } else {
                this.value.setCurrentMode(0);
            }
        }
    }

    public boolean isHovering(int mouseX, int mouseY, int x, int y) {
        if (mouseX >= x && mouseY >= y && mouseX <= x + this.width && mouseY < y + this.height) {
            return true;
        }
        return false;
    }
}
