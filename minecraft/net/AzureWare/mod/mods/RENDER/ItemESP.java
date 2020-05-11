package net.AzureWare.mod.mods.RENDER;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

public class ItemESP extends Mod {

	public ItemESP() {
		super("ItemESP", Category.RENDER);
	}
	
	@EventTarget
	public void onUpdate(EventRender event) {
		for (Object o : mc.theWorld.loadedEntityList) {
    		if (!(o instanceof EntityItem)) continue;
    		EntityItem item = (EntityItem)o;
 		   	double var10000 = item.posX;
 		   	Minecraft.getMinecraft().getRenderManager();
 		   	double x = var10000 - RenderManager.renderPosX;
 		   	var10000 = item.posY + 0.5D;
 		   	Minecraft.getMinecraft().getRenderManager();
 		   	double y = var10000 - RenderManager.renderPosY;
 		   	var10000 = item.posZ;
 		   	Minecraft.getMinecraft().getRenderManager();
 		   	double z = var10000 - RenderManager.renderPosZ;
 		   	GL11.glEnable(3042);
 		   	GL11.glLineWidth(2.0F);
 		   	GL11.glColor4f(1, 1, 1, .75F);
 		   	GL11.glDisable(3553);
 		   	GL11.glDisable(2929);
 		   	GL11.glDepthMask(false);
 	   		RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - .2D, y - 0.3d, z - .2D, x + .2D, y - 0.4d, z + .2D));
 	   		GL11.glColor4f(1, 1, 1, 0.15f);
 	   		RenderUtil.drawBoundingBox(new AxisAlignedBB(x - .2D, y - 0.3d, z - .2D, x + .2D, y - 0.4d, z + .2D));
 	   		GL11.glEnable(3553);
 	   		GL11.glEnable(2929);
 	   		GL11.glDepthMask(true);
 	   		GL11.glDisable(3042);
    	}
	}
}
