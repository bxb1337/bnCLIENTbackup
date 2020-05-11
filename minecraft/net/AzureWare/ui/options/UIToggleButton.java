package net.AzureWare.ui.options;

import net.AzureWare.Client;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.FlatColors;
import net.AzureWare.utils.RenderUtil;

import net.AzureWare.utils.handler.MouseInputHandler;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class UIToggleButton {
	private Value value;
    private MouseInputHandler handler;
    public int width;
    private int height;
    private int lastX;
    private float animationX = 2.14748365E9f;

    public UIToggleButton(Value value, MouseInputHandler handler, int width, int height) {
        this.value = value;
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, int x, int y) {
    	FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        int radius = 4;
        String strValue = this.value.getValueName().split("_")[1];
        boolean enabled = (Boolean)this.value.getValueState();
        int color = enabled ? Colors.GREEN.c : Colors.RED.c;
        this.animate(x, mouseY, radius, enabled);
        this.toggle(mouseX, mouseY, x, y, radius);
        this.drawToggleButton(x, y, radius, color, enabled);
        font.drawString(strValue, (float)(x + 5) + 0.5f, (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0f + 0.5f, Colors.BLACK.c,true);
        font.drawString(strValue, (float)(x + 5), (float)y + (float)(this.height - font.FONT_HEIGHT) / 2.0f, enabled ? -1 : FlatColors.GREY.c,true);
        this.lastX = x;
    }

    private void drawToggleButton(int x, int y, int radius, int color, boolean enabled) {
        float xMid = x + this.width - radius * 2 - 3;
        float yMid = (float)y + (float)(this.height - radius) / 2.0f + 2.0f;
        Gui.drawRect((float)(xMid - (float)radius - 0.5f), (float)(yMid - (float)radius - 0.5f), (float)(xMid + (float)radius + 0.5f), (float)(yMid + (float)radius + 0.5f), (int)color);
        Gui.circle((float)(xMid - (float)radius), (float)yMid, (float)radius, (int)color);
        Gui.circle((float)(xMid + (float)radius), (float)yMid, (float)radius, (int)color);
        Gui.circle((float)this.animationX, (float)yMid, (float)((float)radius - 0.5f), (int)-1);
    }

    private void animate(int x, int y, int radius, boolean enabled) {
        float xEnabled;
        float xMid = x + this.width - radius * 2 - 3;
        float yMid = (float)y + (float)(this.height - radius) / 2.0f - 3.0f;
        float f = xEnabled = !enabled ? xMid - (float)radius + 0.25f : xMid + (float)radius - 0.25f;
        if (this.lastX != x) {
            this.animationX = xEnabled;
        }
        this.animationX = this.animationX == 2.14748365E9f ? xEnabled : (float)RenderUtil.getAnimationState((double)this.animationX, (double)xEnabled, (double)50.0);
    }

    private void toggle(int mouseX, int mouseY, int x, int y, int radius) {
        if (this.isHovering(mouseX, mouseY, x, y, radius) && this.handler.canExcecute()) {
            this.value.setValueState((Object)((Boolean)this.value.getValueState() == false));
        }
    }

    public boolean isHovering(int mouseX, int mouseY, int x, int y, int radius) {
        float xMid = (float)x + (float)(this.width - radius) / 2.0f;
        float yMid = (float)y + (float)(this.height - radius) / 2.0f;
        if (mouseX >= x && mouseY >= y && mouseX <= x + this.width && mouseY < y + this.height) {
            return true;
        }
        return false;
    }
}
