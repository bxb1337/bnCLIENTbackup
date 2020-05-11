package net.AzureWare.mod.mods.RENDER;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.COMBAT.AntiBot;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.value.Value;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class BigGod extends Mod{

	public Value mode = new Value("BigGod", "Mode", 0);
	
	public BigGod() {
		super("BigGod", Category.RENDER);
		mode.addValue("Yaoer");
		mode.addValue("Taijun");
		mode.addValue("Ganga");
		// TODO 自动生成的构造函数存根
	}

	public Meme yaoer = new Meme("Client/yaoer.png");
	public Meme taijun = new Meme("Client/taijun.png");
	public Meme ganga = new Meme("Client/ganga.png");
	
	@EventTarget
	public void onRender(EventRender event) {
		for (EntityPlayer p : mc.theWorld.playerEntities) {
			if(p != mc.thePlayer && p.canEntityBeSeen(mc.thePlayer) && !AntiBot.isBot(p) && !p.isInvisible()) {
				
				double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks
	                    - RenderManager.renderPosX;
	            double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks
	                    - RenderManager.renderPosY;
	            double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks
	                    - RenderManager.renderPosZ;
				
				GL11.glPushMatrix();
		        GL11.glTranslatef((float) pX, (float) pY + (p.isSneaking() ? 0.8f : 1.3F), (float) pZ);
		        GL11.glNormal3f(1.0F, 1.0F, 1.0F);
		        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		        GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
		        
		        float scale = 0.06f;
		        GL11.glScalef(-scale, -scale, scale);
		        
		        GL11.glDisable(GL11.GL_LIGHTING);
		        GL11.glDisable(GL11.GL_DEPTH_TEST);
		        GL11.glEnable(GL11.GL_BLEND);
		        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		        
		        GL11.glPushMatrix();
		        GL11.glColor4f(1, 1, 1, 1);
		        if(mode.isCurrentMode("Yaoer")) {
		        	 RenderUtil.drawImage(yaoer.getResourceLocation(), -8, -14, 16, 16); 
		        	 this.setDisplayName("Yaoer");
		        }
		        if(mode.isCurrentMode("Taijun")) {
		        	RenderUtil.drawImage(taijun.getResourceLocation(), -8, -14, 16, 16);
		        	this.setDisplayName("Taijun");
		        }
		        if(mode.isCurrentMode("Ganga")) {
		        	RenderUtil.drawImage(ganga.getResourceLocation(), -8, -14, 16, 16);
		        	this.setDisplayName("Ganga");
		        }
		        GL11.glPopMatrix();
		        GL11.glPopMatrix();
			}
		}
		
	}

	
	public class Meme {
	    public String name;
	    public int offX;
	    public int offY;
	    public int size;
	    
	    public Meme(String name) {
	        this.name = name;
	        this.offX = 0;
	        this.offY = 0;
	        this.size = 8;
	    }
	    
	    public ResourceLocation getResourceLocation() {
	        return new ResourceLocation(this.name);
	    }
	    
	    @Override
	    public String toString() {
	        return this.offX + "," + this.offY + "," + this.size;
	    }
	}
	
}
