package net.AzureWare.ui.clickgui;

import java.io.IOException;

import net.AzureWare.ui.buttons.UIPopUPButton;
import net.AzureWare.utils.handler.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UIClick extends GuiScreen {
	private ClientEventHandler mouseClickedPopUpMenu = new ClientEventHandler();
    private UIPopUPButton uiPopUPButton;
    private ScaledResolution res;
    public boolean initialized;

    @Override
    public void initGui() {
    	Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("minecraft/shaders/post/blur.json"));
        this.res = new ScaledResolution(this.mc);
        this.mouseClickedPopUpMenu = new ClientEventHandler();
        if (!this.initialized) {
            this.uiPopUPButton = new UIPopUPButton(10.0f, this.res.getScaledHeight() - 10, 6.0f, 14.0f);
            this.initialized = true;
        }
    }

    public void load() {
        if (!this.initialized) {
            Runnable run = new Runnable(){

                @Override
                public void run() {
                    UIClick.access$0(UIClick.this, new UIPopUPButton(10.0f, Minecraft.getMinecraft().displayHeight - 10, 6.0f, 14.0f));
                    UIClick.this.initialized = true;
                }
            };
            new Thread(run).start();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.uiPopUPButton.draw(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.uiPopUPButton.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.uiPopUPButton.mouseReleased(mouseX, mouseY);
    }

    private boolean isHovering(int mouseX, int mouseY, int x, int y, int x2, int y2) {
        if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
            return true;
        }
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void onGuiClosed() {
    	this.mc.entityRenderer.func_181022_b();
        super.onGuiClosed();
    }

    static void access$0(UIClick uIClick, UIPopUPButton uIPopUPButton) {
        uIClick.uiPopUPButton = uIPopUPButton;
    }

}
