package net.AzureWare.mod.mods.WORLD;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventKey;
import net.AzureWare.events.EventMove;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.mod.mods.MOVEMENT.Fly;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class AntiFall extends Mod {

	   private boolean saveMe;
	   
	   private TimeHelper timer = new TimeHelper();
	public Value<Double> falldistance = new Value("AntiFall_FallDistance",10d,5d,30d,0.1d);
	public Value<Boolean> onlyvoid = new Value("AntiFall_OnlyVoid",true);
	
	public AntiFall() {
		super("AntiFall", Category.WORLD);
		// TODO 自动生成的构造函数存根
	}


	   private boolean isBlockUnder() {
		     if (mc.thePlayer.posY < 0.0D)
		       return false; 
		     for (int off = 0; off < (int)mc.thePlayer.posY + 2; off += 2) {
		       AxisAlignedBB bb = mc.thePlayer.boundingBox.offset(0.0D, -off, 0.0D);
		       if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty())
		         return true; 
		     } 
		     return false;
		   }
		   
	   @EventTarget
		   private void onMove(EventMove e) {
		     if (mc.thePlayer.fallDistance > ((Double)falldistance.getValueState()).doubleValue())
		       if  (
		         !((Boolean)this.onlyvoid.getValueState()).booleanValue() || !isBlockUnder()) {
		         if (!this.saveMe) {
		           this.saveMe = true;
		           e.setY(mc.thePlayer.fallDistance*2);
		         } 
//		         mc.thePlayer.posY += mc.thePlayer.fallDistance; 
//		         e.setX(0);
//		         e.setZ(0);
//		         e.setY(mc.thePlayer.fallDistance);
//					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.fallDistance,
//							mc.thePlayer.posZ);

		       }  

		   }
	
    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (this.saveMe && mc.thePlayer.isCollidedVertically) {
            this.saveMe = false;
          } 
    }
	
}
