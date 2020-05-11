package net.AzureWare.mod.mods.RENDER;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.events.EventRender;
import net.AzureWare.events.EventRender2D;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.RenderUtil;

import net.AzureWare.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class BlockOverlay extends Mod {
	
	private Value<Boolean> renderString = new Value<Boolean>("BlockHighlight_Render String", true);
	
	public BlockOverlay() {
		super("BlockOverlay", Category.RENDER);
	}
	
	@EventTarget
    public void onRender(EventRender2D event) {
        BlockPos pos = this.mc.objectMouseOver.getBlockPos();
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        String s = String.valueOf((Object)block.getLocalizedName());
        String s1 = block.getLocalizedName();
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && ((Boolean)this.renderString.getValueState()).booleanValue()) {
            FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
            ScaledResolution res = new ScaledResolution(this.mc);
            int x = res.getScaledWidth() / 2 - font.getStringWidth(s1) / 2;
            int y = res.getScaledHeight() / 2 + 8;
            RenderUtil.drawRect((float)x, (float)y, (float)(x + font.getStringWidth(s) + 3), (float)((float)(y + font.FONT_HEIGHT) + 0.5f), (int)ClientUtil.reAlpha((int)Colors.BLACK.c, (float)0.2f));
            font.drawString(s1, (float)(x + 1), (float)y + 1, -1, true);
        }
    }
	
	@EventTarget
    public void render(EventRender event) {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = this.mc.objectMouseOver.getBlockPos();
            Block block = this.mc.theWorld.getBlockState(pos).getBlock();
            String s = block.getLocalizedName();
            this.mc.getRenderManager();
            double x = (double)pos.getX() - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y = (double)pos.getY() - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z = (double)pos.getZ() - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glColor4f((float)0.2f, (float)0.5f, (float)0.8f, (float)0.25f);
            double minX = block instanceof BlockStairs || Block.getIdFromBlock((Block)block) == 134 ? 0.0 : block.getBlockBoundsMinX();
            double minY = block instanceof BlockStairs || Block.getIdFromBlock((Block)block) == 134 ? 0.0 : block.getBlockBoundsMinY();
            double minZ = block instanceof BlockStairs || Block.getIdFromBlock((Block)block) == 134 ? 0.0 : block.getBlockBoundsMinZ();
            RenderUtil.drawBoundingBox((AxisAlignedBB)new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            GL11.glColor4f((float)0.2f, (float)0.5f, (float)0.8f, (float)1.0f);
            GL11.glLineWidth((float)0.5f);
            RenderUtil.drawOutlinedBoundingBox((AxisAlignedBB)new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

}
