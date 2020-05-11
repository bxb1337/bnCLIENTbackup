package net.AzureWare.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.command.Command;
import net.AzureWare.events.EventTick;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3d;

public class CommandTeleport extends Command {
	public CommandTeleport(String[] commands) {
        super(commands);
    }


	
	   public   double x ;
	   public  double y;
	   public  double z;
	   private double blocksPerTeleport = 100.0D;
	   private long lastTp;
	   private Vec3 lastPos;
	   
    @Override
    public void onCmd(String[] args) {
    	if(args.length == 1) {
            ClientUtil.sendClientMessage("-tp X Y Z", Notification.Type.WARNING);
         } else {
        		  x = Keyboard.getKeyIndex((String)args[1].toUpperCase());
        	      y = Keyboard.getKeyIndex((String)args[2].toUpperCase());
        	      z = Keyboard.getKeyIndex((String)args[3].toUpperCase());    	
                  ClientUtil.sendClientMessage("Succeed", Notification.Type.SUCCESS);

               super.onCmd(args);
   
}

    }
    
    @EventTarget
	 public void onTick(EventTick event) {
	      Minecraft mc = Minecraft.getMinecraft();
	      Vec3d targetVec = new Vec3d(this.x, this.y, this.z);
	      Vec3d tpDirectionVec = targetVec.subtract(mc.thePlayer.getPositionVector()).normalize();
	      int chunkX = (int)Math.floor(mc.thePlayer.getPositionVector().xCoord / 16.0D);
	      int chunkZ = (int)Math.floor(mc.thePlayer.getPositionVector().zCoord / 16.0D);
	      if(mc.theWorld.getChunkFromChunkCoords(chunkX, chunkZ).isLoaded()) {
	         mc.thePlayer.getPositionVector();
	         this.lastPos = mc.thePlayer.getPositionVector();
	         if(targetVec.distanceTo(mc.thePlayer.getPositionVector()) < 0.5D) {
	        //    Modules.queueToggle(this.getId());
	         }

	         Vec3d vec;
	         if(targetVec.distanceTo(mc.thePlayer.getPositionVector()) >= this.blocksPerTeleport) {
	            vec = tpDirectionVec.scale(this.blocksPerTeleport);
	            mc.thePlayer.setPosition(mc.thePlayer.posX + vec.xCoord, mc.thePlayer.posY + vec.yCoord, mc.thePlayer.posZ + vec.zCoord);
	         } else {
	            vec = tpDirectionVec.scale(targetVec.distanceTo(mc.thePlayer.getPositionVector()));
	            mc.thePlayer.setPosition(mc.thePlayer.posX + vec.xCoord, mc.thePlayer.posY + vec.yCoord, mc.thePlayer.posZ + vec.zCoord);
	         }

	         this.lastTp = System.currentTimeMillis();
	      } else if(this.lastTp + 2000L < System.currentTimeMillis()) {
	         mc.thePlayer.posX = this.lastPos.xCoord;
	         mc.thePlayer.posY = this.lastPos.yCoord;
	         mc.thePlayer.posZ = this.lastPos.zCoord;
	      }

	   }
	
}