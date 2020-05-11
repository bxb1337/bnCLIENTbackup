package net.AzureWare.mod.mods.WORLD;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventRender;
import net.AzureWare.events.EventWorldLoaded;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.utils.RenderUtil;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.AxisAlignedBB;

public class MurderFinder extends Mod {
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public MurderFinder() {
		super("MurderFinder", Category.WORLD);
		// TODO 自动生成的构造函数存根
	}
	
	@EventTarget
	public void onWorld(EventWorldLoaded e) {
		entities.clear();
	}
		
	@EventTarget
	public void onRender3D(EventRender e) {
		mc.theWorld.loadedEntityList.forEach(o -> {
			Entity en = (Entity)o;
			if (!en.isEntityAlive() && entities.contains(en)) {
				entities.remove(en);
			}
			
			if (en instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) en;

				if (player != mc.thePlayer && !player.isInvisible()) {
					if (player.getCurrentEquippedItem() != null) {
						Item handItem = player.getCurrentEquippedItem().getItem();
						if (!handItem.equals(Item.getItemById(266)) && 
							!handItem.equals(Item.getItemById(261)) && 
							!handItem.equals(Item.getItemById(262)) && 
							!handItem.equals(Item.getItemById(345)) && 
							!handItem.equals(Item.getItemById(402)) && 
							!(handItem instanceof ItemPotion) && 
							!(handItem instanceof ItemBlock) && 
							!handItem.equals(Item.getItemById(355))) {
							if (!entities.contains(en)) {
								PlayerUtil.tellPlayer("\247b[AzureWare]\247a找到Murder:" + en.getName());
								entities.add(en);
							}
						} else if (entities.contains(en)) {
								entities.remove(en);
						}
					}
				}
			}
		});
		
		mc.theWorld.loadedEntityList.forEach(o -> {
			Entity entity = (Entity)o;
			if ((entity.isEntityAlive() && entities.contains(entity))) {

				
		        Color color = new Color(Colors.DARKRED.c);
		        
		        mc.getRenderManager();
		        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
		        mc.getRenderManager();
		        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY;
		        mc.getRenderManager();
		        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
	            double width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
	            double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25;
	            float red = 1.0f;
	            float green = 0.0f;
	            float blue = 0.0f;
	            float alpha = 0.5f;
	            float lineRed = 0.0f;
	            float lineGreen = 0.5f;
	            float lineBlue = 1.0f;
	            float lineAlpha = 1.0f;
	            float lineWdith = 2.0f;
	            RenderUtil.drawEntityESP(x, y, z, width, height, 1.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 1.0f, 1.0f, 2.0f);

			}
		});
	}

	private boolean hasSword(EntityPlayer en) {
	    for (int i = 0; i < 8; i++) {
		if (en.getInventory()[i].getItem() instanceof ItemSword) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public void onDisable() {
		super.onDisable();
		entities.clear();
	}
}
