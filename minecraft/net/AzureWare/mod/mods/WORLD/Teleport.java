package net.AzureWare.mod.mods.WORLD;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.command.commands.CommandTeleport;
import net.AzureWare.events.EventTick;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3d;

public class Teleport extends Mod{
	public Teleport() {
		super("Teleport",Category.WORLD);
	}
	
	   private double x = 0.0D;
	   private double y = 0.0D;
	   private double z = 0.0D;
	   private double blocksPerTeleport = 100.0D;
	   private long lastTp;
	   private Vec3 lastPos;
	
	   
	
	   
	   
	   
	
	
		   
}
