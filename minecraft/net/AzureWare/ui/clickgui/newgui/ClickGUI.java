package net.AzureWare.ui.clickgui.newgui;

import java.io.IOException;
import java.util.ArrayList;

import net.AzureWare.Client;
import net.AzureWare.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class ClickGUI
extends GuiScreen {
    public static int guiMode;
    public static float alpha;
    protected Minecraft mc = Minecraft.getMinecraft();
    private ArrayList<Panel> panels = new ArrayList();
    public static Mod currentMod;
    public static Mod.Category move;
    public static Mod.Category cats;

    static {
        currentMod = null;
    }

    public ClickGUI() {
        int xAxis = 20;
        for (Mod.Category c : Mod.Category.values()) {
            if (c == Mod.Category.NONE) continue;
            Panel panel = new Panel(xAxis, 10, c);
            this.panels.add(panel);
            xAxis += panel.getWidth() + 5;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        currentMod = null;
        ClickGUI.drawToggleButton(mouseX, mouseY);
        for (Panel p : this.panels) {
            p.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    public static void drawToggleButton(int mouseX, int mouseY) {
        //Client.getInstance().getTaskBar().drawScreen(mouseX, mouseY);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Panel p : this.panels) {
            p.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (currentMod != null) {
            if (mouseButton == 0) {
                currentMod.set(!currentMod.isEnabled());
            }
            if (mouseButton == 1 && currentMod.hasValues()) {
                ClickGUI.currentMod.openValues = !ClickGUI.currentMod.openValues;
            }
        }
    }

    public void initGui() {
        if (this.mc.theWorld != null && !this.mc.gameSettings.ofFastRender) {
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        super.initGui();
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel p : this.panels) {
            p.mouseReleased(mouseX, mouseY, state);
        }
        move = null;
    }

    public void onGuiClosed() {
        this.mc.entityRenderer.func_181022_b();
        //Client.getInstance().getFileUtil().saveGUI();
    }

    public ArrayList<Panel> getPanels() {
        return this.panels;
    }
}
