package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.Client;
import net.AzureWare.events.EventChat;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.events.EventWorldLoaded;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.mods.COMBAT.Killaura;
import net.AzureWare.mod.mods.MOVEMENT.Fly;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.CombatUtil;
import net.AzureWare.utils.RenderUtil;
import net.AzureWare.utils.TimeHelper;

import net.AzureWare.value.Value;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3i;

public class Fucker extends Mod {
	ArrayList positions = null;
	private TimeHelper timer2 = new TimeHelper();
	private TimeHelper timer = new TimeHelper();
	public static Value mode = new Value("Fucker", "Mode", 0);
	private Value reach = new Value("Fucker_Reach", Double.valueOf(6.0D), Double.valueOf(1.0D), Double.valueOf(6.0D),
			0.1D);
	private Value<Boolean> mineplex = new Value("Fucker_CakeProtect", false);
	private Value delay = new Value("Fucker_Delay", Double.valueOf(120.0D), Double.valueOf(0.0D),
			Double.valueOf(1000.0D), 10.0D);
	private Value teleport = new Value("Fucker_Teleport", Boolean.valueOf(false));
	private BlockPos breakingCake;
	private BlockPos yourCake;

	public Fucker() {
		super("Fucker", Category.PLAYER);
		mode.mode.add("Bed");
		mode.mode.add("Egg");
		mode.mode.add("Cake");
	}

	@EventTarget
	public void onWorldLoaded(EventWorldLoaded e) {
		if (yourCake != null) {
			ClientUtil.sendClientMessage("Pos Clear!", Notification.Type.SUCCESS);
			yourCake = null;
		}
	}

	@EventTarget
	public void onChat(EventChat e) {
		if (e.getMessage().contains("You cannot eat your own cake") || e.getMessage().contains("你不能摧毁自己的床！")) {
			ClientUtil.sendClientMessage("Stop Fuck your own thing!", Notification.Type.SUCCESS);
			yourCake = breakingCake;
		}
	}

	@EventTarget
	public void onPre(EventPreMotion event) {
		if (Killaura.target == null)
			this.standartDestroyer(event);
	}

	private void standartDestroyer(EventPreMotion event) {
		Iterator positions = BlockPos.getAllInBox(
				this.mc.thePlayer.getPosition()
						.subtract(new Vec3i(((Double) this.reach.getValueState()).doubleValue(),
								((Double) this.reach.getValueState()).doubleValue(),
								((Double) this.reach.getValueState()).doubleValue())),
				this.mc.thePlayer.getPosition()
						.add(new Vec3i(((Double) this.reach.getValueState()).doubleValue(),
								((Double) this.reach.getValueState()).doubleValue(),
								((Double) this.reach.getValueState()).doubleValue())))
				.iterator();
		BlockPos bedPos = null;

		while ((bedPos = (BlockPos) positions.next()) != null
				&& (!(this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockBed)
						|| !mode.isCurrentMode("Bed"))
				&& (!(this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockDragonEgg)
						|| !mode.isCurrentMode("Egg"))
				&& (!(this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockCake)
						|| !mode.isCurrentMode("Cake"))) {
			;
		}

		if (bedPos instanceof BlockPos) {
			if (Killaura.target != null)
				return;
			breakingCake = bedPos;

			if (yourCake != null && bedPos.x == yourCake.x && bedPos.y == yourCake.y && bedPos.z == yourCake.z)
				return;

			float[] rot = CombatUtil.getRotationsNeededBlock(bedPos.getX(), bedPos.getY(), bedPos.getZ());
			event.yaw = rot[0];
			event.pitch = rot[1];

			if (this.timer.isDelayComplete((long) ((Double) this.delay.getValueState()).intValue())) {
				this.mc.thePlayer.sendQueue.addToSendQueue(
						new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
				this.mc.thePlayer.sendQueue
						.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
				this.mc.thePlayer.sendQueue.addToSendQueue(
						new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
				this.mc.thePlayer.swingItem();
				if (((Boolean) this.teleport.getValueState()).booleanValue()) {
					this.mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition((double) bedPos.getX(),
							(double) (bedPos.getY() + 1), (double) bedPos.getZ(), true));
				}

				this.timer.reset();
			}

		}
	}
}
