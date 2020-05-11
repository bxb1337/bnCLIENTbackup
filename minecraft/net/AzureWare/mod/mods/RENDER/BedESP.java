package net.AzureWare.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventRender;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.value.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import net.minecraft.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;

public class BedESP extends Mod {
   private static ArrayList blockIds = new ArrayList();
   private ArrayList toRender = new ArrayList();

   public BedESP() {
      super("BedESP", Category.RENDER);
   }

   public void onEnable() {
      this.mc.renderGlobal.loadRenderers();
      this.toRender.clear();
      super.onEnable();
   }
	@EventTarget
   public void onRenderBlock(EventRender event) {
      Iterator positions = BlockPos.getAllInBox(this.mc.thePlayer.getPosition().subtract(new Vec3i(15, 15, 15)), this.mc.thePlayer.getPosition().add(new Vec3i(15, 15, 15))).iterator();
      BlockPos bedPos = null;

      while((bedPos = (BlockPos)positions.next()) != null && (!(this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockBed) || !true)) {
         ;
      }
	   
	   
      this.toRender.add(bedPos);

      for(int i = 0; i < this.toRender.size(); ++i) {
         BlockPos pos_1 = (BlockPos)this.toRender.get(i);
         int id = Block.getIdFromBlock(this.mc.theWorld.getBlockState(pos_1).getBlock());
         if(!blockIds.contains(Integer.valueOf(id))) {
            this.toRender.remove(i);
         }
      }

   }

   @EventTarget
   public void onRender(EventRender event) {
      Iterator var3 = this.toRender.iterator();

      while(var3.hasNext()) {
         BlockPos pos = (BlockPos)var3.next();
         this.renderBlock(pos);
      }

   }

   private void renderBlock(BlockPos pos) {
      this.mc.getRenderManager();
      double x = (double)pos.getX() - RenderManager.renderPosX;
      this.mc.getRenderManager();
      double y = (double)pos.getY() - RenderManager.renderPosY;
      this.mc.getRenderManager();
      double z = (double)pos.getZ() - RenderManager.renderPosZ;
      RenderUtil.drawSolidBlockESP(x, y, z, 0.0F, 0.5F, 1.0F, 0.25F);
   }
}
