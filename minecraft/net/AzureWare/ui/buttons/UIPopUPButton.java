package net.AzureWare.ui.buttons;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.AzureWare.Client;
import net.AzureWare.ui.UIDesignSettings;
import net.AzureWare.ui.clickgui.menu.UIMenu;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.handler.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UIPopUPButton {
	private float x;
    private float y;
    private float currentRadius;
    private float minRadius;
    private float maxRadius;
    private float menuSizeRadius;
    private boolean open;
    private boolean animateUp;
    private boolean animateDown;
    private ClientEventHandler mouseClickedPopUpMenu;
    private ArrayList<UIPopUPChooseButton> popUpButtons = new ArrayList();
    private UIPopUPChooseButton openButton = null;
    private UIMenu menu;
    private boolean now;
    
    public UIPopUPButton(float x, float y, float minRadius, float maxRadius) {
        this.x = x;
        this.y = y;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.currentRadius = maxRadius;
        this.mouseClickedPopUpMenu = new ClientEventHandler();
        this.popUpButtons.add(new UIPopUPChooseButton("ClickGUI", (int)x + (int)maxRadius, 0, "Client/menu_icon.png"));
        int yAxis = (int)y - 60;
        for (UIPopUPChooseButton button : this.popUpButtons) {
            button.setY(yAxis);
            yAxis -= 40;
        }
        this.openButton = this.popUpButtons.get(0);
        this.menu = new UIMenu();
    }
    
    public void draw(int mouseX, int mouseY) {
    	
        ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        boolean hovering = this.isHovering(mouseX, mouseY) && this.currentRadius == this.maxRadius;
        this.y = sc.getScaledHeight() - 10;
        int yAxis = (int)this.y - 50;
        for (UIPopUPChooseButton button : this.popUpButtons) {
            button.setY(yAxis);
            yAxis -= 30;
        }
        this.animate();
        GL11.glPushMatrix();
        float scale = this.currentRadius / this.maxRadius;
        float xMid = this.x + this.maxRadius - 0.5f;
        float yMid = (float)sc.getScaledHeight() - this.maxRadius - 10.0f;
        GL11.glTranslated((double)xMid, (double)yMid, (double)0.0);
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        GL11.glTranslated((double)(- xMid), (double)(- yMid), (double)0.0);
        /*
        Gui.circle(this.maxRadius + 10.0f, (float)sc.getScaledHeight() - this.maxRadius - 10.0f, this.menuSizeRadius, ClientUtil.reAlpha(-1, 0.8f));
        Gui.circle(this.maxRadius + 10.0f, (float)sc.getScaledHeight() - this.maxRadius - 10.0f, this.maxRadius, UIDesignSettings.getColor());
        
        if (hovering) {
            Gui.drawFilledCircle(this.maxRadius + 10.0f, (float)sc.getScaledHeight() - this.maxRadius - 10.0f, this.maxRadius + 0.5f, ClientUtil.reAlpha(Colors.BLACK.c, 0.1f));
        }
        if (this.mouseClickedPopUpMenu.canExcecute(Mouse.isButtonDown((int)0)) && hovering) {
            this.animateDown = true;
        }
        
        if (this.currentRadius > this.maxRadius / 2.0f && !this.open) {
            font.drawString("+", this.x + this.maxRadius - 0.5f - (float)(font.getStringWidth("+") / 2), (float)sc.getScaledHeight() - this.maxRadius - 12.0f - (float)(font.FONT_HEIGHT / 2), -1);
        } else {
            RenderUtil.drawImage(new ResourceLocation("Client/settings_icon.png"), (int)(this.x + this.maxRadius - 0.5f - 9.0f) + 3, (int)((float)sc.getScaledHeight() - this.maxRadius - 12.0f - 8.0f) + 2, 15, 15);
        }*/
        GL11.glPopMatrix();
        int rad = 0;
        for (UIPopUPChooseButton button : this.popUpButtons) {
            if (this.open) {
                button.draw(mouseX, mouseY);
                if (!button.clicked(mouseX, mouseY)) continue;
                this.openButton = button;
                continue;
            }
            if (rad == 0) {
                rad = (int)(- button.maxRadius);
            }
            button.currentRadius = rad;
            rad = (int)((float)rad - button.maxRadius);
        }
        if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
            float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
            this.menu.draw(mouseX, mouseY);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
            this.menu.mouseClick(mouseX, mouseY);
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
            this.menu.mouseRelease(mouseX, mouseY);
        }
    }

    private void animate() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        float add = RenderUtil.delta * 45.0f;
        float menuAdd = RenderUtil.delta * 1500.0f;
        int maxMenuSize = res.getScaledWidth() + (int)((float)res.getScaledWidth() * 0.25f);
        if (this.open && !this.animateDown) {
            this.menuSizeRadius = this.menuSizeRadius + menuAdd > (float)maxMenuSize ? (float)maxMenuSize : (this.menuSizeRadius += menuAdd);
        } else {
            this.now = true;
            this.menuSizeRadius = this.menuSizeRadius <= (float)maxMenuSize / 1.2631578f ? 0.0f : (this.menuSizeRadius -= menuAdd);
        }
        if (this.animateDown) {
            if (this.currentRadius - add > this.minRadius) {
                this.currentRadius -= add;
            } else {
                this.currentRadius = this.minRadius;
                this.animateDown = false;
                this.animateUp = true;
                this.open = !this.open;
            }
        } else if (this.animateUp) {
            if (this.currentRadius + add < this.maxRadius) {
                this.currentRadius += add;
            } else {
                this.currentRadius = this.maxRadius;
                this.animateUp = false;
            }
        }
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if ((float)mouseX >= this.x && (float)mouseX <= this.x + this.maxRadius * 2.0f && (float)mouseY >= this.y - this.maxRadius * 2.0f && (float)mouseY <= this.y) {
            return true;
        }
        return false;
    }
}
