package net.AzureWare.events;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventLiquidCollide implements Event {
	private Block block;
	private BlockPos pos;
	private AxisAlignedBB bounds;
	private boolean canceled;
	
	public EventLiquidCollide(Block block, BlockPos pos, AxisAlignedBB bounds) {
		this.block = block;
		this.pos = pos;
		this.bounds = bounds;
	}

	public AxisAlignedBB getBounds() {
		return this.bounds;
	}

	public void setBounds(AxisAlignedBB bounds) {
		this.bounds = bounds;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public Block getBlock() {
		return this.block;
	}

	public void setCancelled(boolean b) {
		this.canceled = b;
	}
	
	public boolean isCancelled() {
		return this.canceled;
	}
}
