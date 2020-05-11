package net.AzureWare.ui.clickgui.newgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import org.lwjgl.input.Mouse;

import net.AzureWare.Client;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.value.Value;

public class ModulePanel {
    private Mod.Category cat;
    private int x;
    private int y;
    private int width;
    private int height;
    private int panelY;
    private boolean valueClicked;
    private boolean valueClickedMode;
    float yAdd;
    public int yAxis;
    private boolean isOpen;
    private float maxY;

    public ModulePanel(Mod.Category cat, int x, int y, int width, int height, int panelY, int yAdd) {
        this.yAdd = -this.width;
        this.yAxis = 0;
        this.maxY = 0.0f;
        this.cat = cat;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.panelY = panelY;
        this.yAdd = yAdd;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks, float delta) {
        this.yAxis = 0;
        if (this.y + this.yAxis >= this.panelY) {
            this.doDropShadow();
        }
        for (Mod m : ModManager.getModList()) {
            if (m.getCategory() != this.cat) continue;
            float stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.getName());
            float stringHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 5;
            if (this.y + this.yAxis >= this.panelY) {
                Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis), (int)(this.x + this.width - 1), (int)(this.y + this.yAxis + this.height), ClientUtil.reAlpha((int)(m.isEnabled() ? -16165503 : -15785172), 0.5f));
                int xOld = this.x;
                int yOld = this.y;
                int yAxisOld = this.yAxis;
                boolean hover = false;
                if (this.isOpen) {
                    if (this.isHovering(mouseX, mouseY, this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height)) {
                        ClickGUI.currentMod = m;
                        hover = true;
                    }
                    if (ClickGUI.currentMod == m && this.yAdd == 0.0f) {
                        Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis), (int)(this.x + this.width - 1), (int)(this.y + this.yAxis + this.height), ClientUtil.reAlpha((int)-13877680, 0.5F));
                    }
                }
                this.isOpen = this.yAdd == 0.0f;
                this.drawEffectToButton(mouseX, mouseY, this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height, hover, m, delta);
                float halfWidth = (float)this.width / 2.0f;
                float halfHeight = (float)this.height / 2.0f;
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(m.getName(), (float)this.x + halfWidth - stringWidth / 2.0f , (float)this.y + halfHeight - stringHeight / 2.0f + (float)this.yAxis - 2.0f , -1);
                Minecraft.getMinecraft().fontRendererObj.drawString(m.hasValues() ? (m.openValues ? "-" : "+") : "", (float)(this.x + this.width - 8), (float)this.y + halfHeight - stringHeight / 2.0f + (float)this.yAxis - 2.0f , -1, true);
                if (m.openValues) {
                    for (Value value : Value.list) {
                        String valueMod = value.getValueName().split("_")[0];
                        String valueName = value.getValueName().split("_")[1];
                        if (!valueMod.equalsIgnoreCase(m.getName())) continue;
                        this.yAxis += this.height;
                        float valueStringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(valueName);
                        float valueStringHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 5;
                        if (value.isValueMode) {
                            this.yAxis -= this.height;
                            for (int i = 0; i < value.listModes().size(); ++i) {
                                float currentRadius;
                                this.yAxis += this.height;
                                if (value.getCurrentMode() == i) {
                                    currentRadius = value.currentRadius;
                                } else {
                                    value.getClass();
                                    currentRadius = 4.0f;
                                }
                                float realRadius = currentRadius;
                                Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis), (int)(this.x + this.width - 1), (int)(this.y + this.yAxis + this.height), ClientUtil.reAlpha((int)-15785172,0.5f));
                                int n = this.x + this.width;
                                value.getClass();
                                Gui.drawCircle((int)(n - 4 - 4), (int)(this.y + this.yAxis + this.height / 2), (int)((int)realRadius), (Color)new Color(Colors.AQUA.c));
                                if (value.getCurrentMode() == i) {
                                    int n2 = this.x + this.width;
                                    value.getClass();
                                    Gui.drawFilledCircle((int)(n2 - 8), (int)(this.y + this.yAxis + this.height / 2), (float)realRadius, ClientUtil.reAlpha((int)-14057287,0.5f));
                                }
                                String name = value.getModeAt(i);
                                int stringWidthMode = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
                                int stringHeightMode = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 5;
                                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, (float)this.x + (float)this.width / 2.0f - (float)stringWidthMode / 2.0f, (float)(this.y + this.yAxis) + (float)this.height / 2.0f - (float)stringHeightMode / 2.0f - 2.0f , -1);
                                int[] array = new int[4];
                                int n3 = 0;
                                int n4 = this.x + this.width;
                                value.getClass();
                                array[n3] = n4 - 4 - 10;
                                int n5 = 1;
                                int n6 = this.y + this.yAxis + this.height / 2;
                                value.getClass();
                                array[n5] = n6 - 4;
                                int n7 = 2;
                                int n8 = this.x + this.width;
                                value.getClass();
                                array[n7] = n8 + 4 - 6;
                                int n9 = 3;
                                int n10 = this.y + this.yAxis + this.height / 2;
                                value.getClass();
                                array[n9] = n10 + 4;
                                int[] hitBox = array;
                                if (Mouse.isButtonDown((int)0) && !value.disabled) {
                                    if (!this.valueClickedMode && this.isHovering(mouseX, mouseY, hitBox[0], hitBox[1], hitBox[2], hitBox[3])) {
                                        value.setCurrentMode(i);
                                        value.currentRadius = 0.0f;
                                        this.valueClickedMode = true;
                                    }
                                } else {
                                    this.valueClickedMode = false;
                                }
                                if (value.currentRadius < 4.0f) {
                                    value.currentRadius += 3.0f * delta;
                                }
                                if (!(value.currentRadius > 4.0f)) continue;
                                value.currentRadius = 4.0f;
                            }
                        }
                        if (value.isValueBoolean) {
                            Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis), (int)(this.x + this.width - 1), (int)(this.y + this.yAxis + this.height), ClientUtil.reAlpha((int)-15785172,0.5f));
                            if (((Boolean)value.getValueState()).booleanValue()) {
                                Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis), (int)(this.x + 2), (int)(this.y + this.yAxis + this.height), ClientUtil.reAlpha((int)-14057287,0.5f));
                            }
                            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(valueName, (float)this.x + halfWidth - valueStringWidth / 2.0f, (float)this.y + halfHeight - valueStringHeight / 2.0f + (float)this.yAxis - 2.0f , Boolean.valueOf((boolean)((Boolean)value.getValueState())) != false ? -14057287 : -1);
                            if (Mouse.isButtonDown((int)0) && !value.disabled) {
                                if (this.isHovering(mouseX, mouseY, this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height) && !this.valueClicked) {
                                    this.valueClicked = true;
                                    value.setValueState((Object)((Boolean)value.getValueState() == false));
                                }
                            } else {
                                this.valueClicked = false;
                            }
                            if (value.disabled) {
                                Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis), (int)(this.x + this.width), (int)(this.y + this.yAxis + this.height), ClientUtil.reAlpha((int)-15785172,0.5f));
                            }
                        }
                        if (!value.isValueDouble) continue;
                        double min = (Double)value.getValueMin();
                        double max = (Double)value.getValueMax();
                        double step = value.getSteps();
                        double valAbs = mouseX - (this.x + 1);
                        double perc = valAbs / (double)(this.width - 2);
                        perc = Math.min((double)Math.max((double)0.0, (double)perc), (double)1.0);
                        double valRel = (max - min) * perc;
                        double valuu = min + valRel;
                        double percSlider = ((Double)value.getValueState() - min) / (max - min);
                        double val = (double)(this.x + 1) + (double)(this.width - 2) * percSlider;
                        float valueStringWidthDouble = Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf((Object)String.valueOf((Object)valueName)) + " " + value.getValueState());
                        Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis), (int)(this.x + this.width - 1), (int)(this.y + this.yAxis + this.height), ClientUtil.reAlpha((int)-15785172,0.5f));
                        GuiIngame.drawRect((float)(this.x + 1), (float)(this.y + this.yAxis), (float)((float)val), (float)(this.y + this.yAxis + this.height - 1), ClientUtil.reAlpha((int)-14057287,0.7f));
                        if (Mouse.isButtonDown((int)0) && !value.disabled && this.isHovering(mouseX, mouseY, this.x, this.y + (int)this.yAdd + this.yAxis, this.x + this.width, this.y + (int)this.yAdd + this.yAxis + this.height)) {
                            value.sliderX = mouseX - this.x;
                            valuu = (double)Math.round((double)(valuu * (1.0 / step))) / (1.0 / step);
                            value.setValueState((Object)valuu);
                        }
                        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(String.valueOf((Object)String.valueOf((Object)valueName)) + " " + value.getValueState(), (float)this.x + halfWidth - valueStringWidthDouble / 2.0f, (float)this.y + halfHeight - valueStringHeight / 2.0f + (float)this.yAxis - 2.0f , -1);
                        if (!value.disabled) continue;
                        Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis), (int)(this.x + this.width), (int)(this.y + this.yAxis + this.height - 1), (int)Colors.BLACK.c);
                    }
                    Client.getInstance().getFileUtil().saveValues();
                }
                if (m.openValues) {
                    Gui.drawRect((int)(this.x + 1), (int)(this.y + this.yAxis + this.height - 1), (int)(this.x + this.width - 1), (int)(this.y + this.yAxis + this.height), (int)Colors.WHITE.c);
                    Gui.drawRect((int)(xOld + 1), (int)(yOld + yAxisOld + this.height), (int)(xOld + this.width - 1), (int)(yOld + yAxisOld + this.height + 1), (int)Colors.WHITE.c);
                }
            }
            this.yAxis += this.height;
            this.maxY = this.yAdd == 0.0f ? (float)(this.y + this.yAxis) : -1.0f;
        }
    }

    private void doDropShadow() {
    }

    private void drawEffectToButton(int mouseX, int mouseY, int xLeft, int yUp, int xRight, int yDown, boolean hovered, Mod mod, float delta) {
        if (this.isOpen) {
            if (Mouse.isButtonDown((int)0)) {
                if (!mod.clickedCircle) {
                    if (hovered) {
                        mod.circleValue = 0.0f;
                        mod.canSeeCircle = true;
                        mod.circleCoords[0] = mouseX;
                        mod.circleCoords[1] = mouseY;
                    }
                    mod.clickedCircle = true;
                }
            } else {
                mod.clickedCircle = false;
            }
            if (mod.canSeeCircle) {
                Gui.drawFilledCircle((int)mod.circleCoords[0], (int)mod.circleCoords[1], (float)mod.circleValue, (int)Integer.MAX_VALUE, (int)xLeft, (int)yUp, (int)xRight, (int)yDown);
            }
            if (mod.circleValue < 88.0f) {
                mod.circleValue += 200.0f * delta;
            } else {
                mod.canSeeCircle = false;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    private boolean isHovering(int mouseX, int mouseY, int xLeft, int yUp, int xRight, int yBottom) {
        return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }

    public void update(int x, int y, int width, int height, int panelY, float yAdd) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.panelY = panelY;
        this.yAdd = yAdd;
    }
}
