package net.AzureWare.utils;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventBlockRender;
import net.AzureWare.events.EventUpdate;
import net.minecraft.block.BlockBed;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

public class NukerUtil {
	public static ArrayList<BlockPos> list = new ArrayList<BlockPos>();

	public static void update() {
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
		list.clear();
	}

	@EventTarget
	public void onRenderBlock(EventBlockRender e) {
		BlockPos pos = new BlockPos(e.x, e.y, e.z);
		if (!list.contains(pos) && e.block instanceof BlockBed) {
			list.add(pos);
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		list.removeIf(pos -> !(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockBed));
	}
}
