package net.AzureWare.ui.clickgui.awui;

import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.AzureWare.Client;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.ui.clickgui.newgui.ClickGUI;
import net.AzureWare.ui.clickgui.newgui.Panel;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.fontmanager.UnicodeFontRenderer;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ClickGui extends GuiScreen {
	
	private int stage = 0;
	UnicodeFontRenderer font;
	UnicodeFontRenderer font1;
	UnicodeFontRenderer font2;
	ScaledResolution sr;
	public static float scale = 1f;
	
    public ClickGui() {
    	
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	sr = new ScaledResolution(mc);
    	
    	if (sr.getScaledHeight() > 420 && sr.getScaledWidth() > 570) {
    		scale = 1.0f;
    	}else {
    		scale = 0.8f;
    	}
    	
    	GL11.glScaled(scale, scale, scale);
    	    	
    	RenderUtil.drawImage(new ResourceLocation("Client/newui/background.png"), 0, 0, (int)(sr.getScaledWidth() * (1 / scale)), (int)(sr.getScaledHeight() * (1 / scale)));
    	font = Client.getInstance().getFontManager().comfortaa20;
    	font1 = Client.getInstance().getFontManager().comfortaa18;
    	font2 = Client.getInstance().getFontManager().comfortaa16;
    	RenderUtil.drawImage(new ResourceLocation("Client/newui/navibar_no.png"), 5, 10, 1075 / 3 - 3, 78 / 3 + 5);
    	RenderUtil.drawImage(new ResourceLocation("Client/newui/navibar_yes.png"), (215 / 3) * stage + 5, 10, 215 / 3, 78 / 3 + 5);
    	
    	font.drawCenteredString("Combat", 6 + (215 / 6) * 1, 18, 0xEEEEEE);
    	font.drawCenteredString("Movement", 6 + (215 / 6) * 3, 18, 0xEEEEEE);
    	font.drawCenteredString("Render", 6 + (215 / 6) * 5, 18, 0xEEEEEE);
    	font.drawCenteredString("Player", 6 + (215 / 6) * 7, 18, 0xEEEEEE);
    	font.drawCenteredString("World", 6 + (215 / 6) * 9, 18, 0xEEEEEE);
    	font = Client.getInstance().getFontManager().VERDANA16;
    	int y = 78 / 3 + 20;
    	int x = 5;
    	int count = 0;
    	Category cate = Category.COMBAT;
    	if (stage == 0) cate = Category.COMBAT;
    	if (stage == 1) cate = Category.MOVEMENT;
    	if (stage == 2) cate = Category.RENDER;
    	if (stage == 3) cate = Category.PLAYER;
    	if (stage == 4) cate = Category.WORLD;
		for(Mod mod : ModManager.getEnabledModListHUD()) {
			if (mod.getCategory() == cate) {
				int modneedysize = 0;
                for (Value value : Value.list) {
                	boolean flag = false;
                    String valueMod = value.getValueName().split("_")[0];
                    String valueName = value.getValueName().split("_")[1];
                    if (!valueMod.equalsIgnoreCase(mod.getName())) continue;
                    modneedysize++;
                    
                    if (value.isValueMode) {
                    	modneedysize++;
                    }
					if (modneedysize > 6 && y != 78 / 3 + 20 && !flag ) {
                    	flag = true;
    					y = 78 / 3 + 20;
    					x += 140;
                    }
                }

                count++;
				RenderUtil.drawImage(new ResourceLocation("Client/newui/navibar_no.png"), x, y, 130, modneedysize * 20 + 25);
				
				modneedysize = 0;
                for (Value value : Value.list) {
                    String valueMod = value.getValueName().split("_")[0];
                    String valueName = value.getValueName().split("_")[1];
                    if (!valueMod.equalsIgnoreCase(mod.getName())) continue;
                    modneedysize++;
                    if (value.isValueBoolean) {
                    	if ((boolean)value.getValueState()) {
                    		font2.drawString(valueName, 12 + x + 16f, y + 6 + modneedysize * 20, 0xEEEEEE);
                    		RenderUtil.drawImage(new ResourceLocation("Client/newui/value_boolean_true.png"), 12 + x, y + 6 + modneedysize * 20, 10, 10);
                    	} else {
                    		font2.drawString(valueName, 12 + x + 16f, y + 6 + modneedysize * 20, 0xCCCCCC);
                    		RenderUtil.drawImage(new ResourceLocation("Client/newui/value_boolean_false.png"), 12 + x, y + 6 + modneedysize * 20, 10, 10);
                    	}
                    }
                    
                    if (value.isValueDouble) {
                    	
                    	int enablevalue = (int)(107 * ((((Double)value.getValueState()) - ((Double)value.getValueMin())) / (((Double)value.getValueMax()) - ((Double)value.getValueMin()))));
                    	RenderUtil.drawImage(new ResourceLocation("Client/newui/value_boolean_true.png"), 12 + x, y + 6 + modneedysize * 20 + 10, enablevalue, 2);
                    	RenderUtil.drawImage(new ResourceLocation("Client/newui/value_boolean_false.png"), 12 + x + enablevalue, y + 6 + modneedysize * 20 + 10, 107 - enablevalue, 2);
                    	
                    	RenderUtil.drawImage(new ResourceLocation("Client/newui/value_boolean_true.png"), 12 + x + enablevalue, y + 4 + modneedysize * 20 + 10, 6, 6);
                    	font2.drawString(valueName, 12f + x, y + 4 + modneedysize * 20, 0xEEEEEE);
                    	font2.drawString(((Double)value.getValueState()).toString(), 12f + x + 108 - font2.getStringWidth(((Double)value.getValueState()).toString()),
                    			y + 4 + modneedysize * 20, 0xa0a0a0);
                    	
                    	if (Mouse.isButtonDown(0) && ((Double)value.getValueState() > (Double)value.getValueMin()) && 
                    			this.isHovering(mouseX, mouseY, 4 + x, y + 4 + modneedysize * 20 + 6, enablevalue + 9 + x, 2 + y + 6 + modneedysize * 20 + 16) && 
                    			!this.isHovering(mouseX, mouseY, 12 + x + enablevalue, y + 4 + modneedysize * 20 + 10, 6 + 12 + x + enablevalue, 6 + y + 4 + modneedysize * 20 + 10)) {
                    		value.setValueState(formatDouble((Double)(value.getValueState()) - value.getSteps()));
                    		Client.getInstance().getFileUtil().saveValues();
                    	}
                    	
                    	if (Mouse.isButtonDown(0) && ((Double)value.getValueState() < (Double)value.getValueMax()) && 
                    			this.isHovering(mouseX, mouseY, 15 + x + enablevalue, y + 4 + modneedysize * 20 + 6, 107 + 18 + x , 2 + y + 6 + modneedysize * 20 + 16) && 
                    			!this.isHovering(mouseX, mouseY, 12 + x + enablevalue, y + 4 + modneedysize * 20 + 10, 6 + 12 + x + enablevalue, 6 + y + 4 + modneedysize * 20 + 10)) {
                    		value.setValueState(formatDouble((Double)(value.getValueState()) + value.getSteps()));
                    		Client.getInstance().getFileUtil().saveValues();
                    	}
                    }
                    
                    if (value.isValueMode) {
                    	modneedysize++;
                    	font2.drawString(value.getModeTitle(), 12f + x, y + 8 + (modneedysize - 1) * 20, 0xCCCCCC);
                    	RenderUtil.drawImage(new ResourceLocation("Client/newui/value_menu.png"), 12 + x, y + modneedysize * 20, 108, 20);
                    	font2.drawString(value.getModeAt(value.getCurrentMode()), 18f + x, y + 24 + (modneedysize - 1) * 20, 0xEEEEEE);
                    	font2.drawString("<" + (value.getCurrentMode() + 1) + "/" + value.mode.size() + ">", 90f + x, y + 24 + (modneedysize - 1) * 20, 0xAAAAAA);
                    	
                    }
                }
                font2.drawString(mod.getName(), 12f + x, y + 6, 0xEEEEEE);
				if (mod.isEnabled()) {
					RenderUtil.drawImage(new ResourceLocation("Client/newui/mod_enable_bg.png"), x + 130 - 30, y + 5, 22, 11);
					RenderUtil.drawImage(new ResourceLocation("Client/newui/mod_enable_button.png"), x + 130 - 30 + 11, y + 5, 11, 11);
				} else {
					RenderUtil.drawImage(new ResourceLocation("Client/newui/mod_disable_bg.png"), x + 130 - 30, y + 5, 22, 11);
					RenderUtil.drawImage(new ResourceLocation("Client/newui/mod_disable_button.png"), x + 130 - 30, y + 5, 11, 11);
				}
				y += modneedysize * 20 + 30;
				if (y > sr.getScaledHeight() * (1 / scale) - 150) {
					y = 78 / 3 + 20;
					x += 140;
				}
			}
		}
		font1.drawString("Designed by ButterCookies :3 " , 365f, 24f, 0xCCCCCC);
		font1.drawString(Client.CLIENT_O , 365f, 34f, 0xCCCCCC);
		RenderUtil.drawImage(new ResourceLocation("Client/newui/logo.png"), (int)(sr.getScaledWidth() * (1 / scale)) - 116 / 2 - 10, (int)(sr.getScaledHeight() * (1 / scale)) - 135 / 2 - 10, 116 / 2, 135 / 2);
    }
    
    public static double formatDouble(double d) {
        return (double)Math.round(d*100)/100;
    }
    
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	
    }
    
    public void initGui() {
        if (this.mc.theWorld != null && !this.mc.gameSettings.ofFastRender) {
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        super.initGui();
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
    	if(this.isHovering(mouseX, mouseY, (215 / 3) * 0 + 5, 10, (215 / 3) * 1 + 5, 78 / 3 + 10)) { //COMBAT
    		stage = 0;
    	}
    	
    	if(this.isHovering(mouseX, mouseY, (215 / 3) * 1 + 5, 10, (215 / 3) * 2 + 5, 78 / 3 + 10)) { //MOVEMENT
    		stage = 1;
    	}
    	
    	if(this.isHovering(mouseX, mouseY, (215 / 3) * 2 + 5, 10, (215 / 3) * 3 + 5, 78 / 3 + 10)) { //RENDER
    		stage = 2;
    	}
    	
    	if(this.isHovering(mouseX, mouseY, (215 / 3) * 3 + 5, 10, (215 / 3) * 4 + 5, 78 / 3 + 10)) { //PLAYER
    		stage = 3;
    	}
    	
    	if(this.isHovering(mouseX, mouseY, (215 / 3) * 4 + 5, 10, (215 / 3) * 5 + 5, 78 / 3 + 10)) { //WORLD
    		stage = 4;
    	}
    	
    	Category cate = Category.COMBAT;
    	if (stage == 0) cate = Category.COMBAT;
    	if (stage == 1) cate = Category.MOVEMENT;
    	if (stage == 2) cate = Category.RENDER;
    	if (stage == 3) cate = Category.PLAYER;
    	if (stage == 4) cate = Category.WORLD;
    	
    	int y = 78 / 3 + 20;
    	int x = 5;
    	int count = 0;
    	ScaledResolution sr = new ScaledResolution(mc);
    	
		for(Mod mod : ModManager.getEnabledModListHUD()) {
			if (mod.getCategory() == cate) {
				int modneedysize = 0;
                for (Value value : Value.list) {
                	boolean flag = false;
                    String valueMod = value.getValueName().split("_")[0];
                    String valueName = value.getValueName().split("_")[1];
                    if (!valueMod.equalsIgnoreCase(mod.getName())) continue;
                    modneedysize++;
                	
                    if (modneedysize > 6 && y != 78 / 3 + 20 && !flag) {
                    	flag = true;
    					y = 78 / 3 + 20;
    					x += 140;
                    }
                }
				
                modneedysize = 0;
                for (Value value : Value.list) {
                	boolean flag = false;
                    String valueMod = value.getValueName().split("_")[0];
                    String valueName = value.getValueName().split("_")[1];
                    if (!valueMod.equalsIgnoreCase(mod.getName())) continue;
                    modneedysize++;
                    
                    if (value.isValueBoolean) {
                    	if(this.isHovering(mouseX, mouseY, 12 + x, y + 6 + modneedysize * 20, 12 + x + 10, y + 6 + modneedysize * 20 + 10)) {
                    		value.setValueState(!(boolean)value.getValueState());
                    	}
                    }
                    
                    if (value.isValueMode) {
                    	modneedysize++;
                    	if(this.isHovering(mouseX, mouseY, 12 + x, y + modneedysize * 20, 108 + 12 + x, 20 + y + modneedysize * 20)) {
                    		if (value.mode.size() <= value.getCurrentMode() + 1) {
                    			value.setCurrentMode(0);
                    		} else {
                    			value.setCurrentMode(value.getCurrentMode() + 1);
                    		}
                    		
                    	}
                    }
                    
                }

                
                count++;
                
            	if(this.isHovering(mouseX, mouseY, x + 130 - 30, y + 5, x + 130 - 30 + 22, y + 5 + 11)) {
            		mod.toggle();
            	}
            	
				y += modneedysize * 20 + 30;
				
				if (y > sr.getScaledHeight() * (1 / scale) - 150) {
					y = 78 / 3 + 20;
					x += 140;
				}
			}
		}
		Client.getInstance().getFileUtil().saveValues();
    }

    private boolean isHovering(int mouseX, int mouseY, int xLeft, int yUp, int xRight, int yBottom) {
    	xLeft *= scale;
    	yUp *= scale;
    	xRight *= scale;
    	yBottom *= scale;
        return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }
    
    public void onGuiClosed() {
        this.mc.entityRenderer.func_181022_b();
    }
}
