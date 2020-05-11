package net.AzureWare.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.Colors;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class AutoSoup extends Mod {

	private Value delay = new Value("AutoSoup_Delay", Double.valueOf(50.0D), Double.valueOf(0.0D),
			Double.valueOf(1000.0D), 10.0D);
	private Value health = new Value("AutoSoup_Health", Double.valueOf(6.0D), Double.valueOf(0.5D),
			Double.valueOf(9.5D), 0.5D);
	private TimeHelper timer = new TimeHelper();
	private TimeHelper throwTimer = new TimeHelper();
	private boolean nextTick = false;
	public static Value mode = new Value("AutoSoup", "Mode", 0);

	public AutoSoup() {
		super("AutoSoup", Category.COMBAT);
		mode.addValue("Pot");
		// mode.addValue("Soup");

		mode.addValue("Head");
	}

	@EventTarget
	public void onPre(EventPreMotion event) {
		if ((double) (mc.thePlayer.getHealth() / 2.0F) <= ((Double) health.getValueState()).doubleValue()) {
			event.pitch = 90.0F;
			getPotion();
			nextTick = true;
		}

		if (nextTick) {
			event.pitch = 90.0F;
			throwPotion();
			nextTick = false;
		}

	}

	private void getPotion() {
		int slotId = getFreeSlot();
		if (slotId != -1) {
			for (int id = 9; id <= 35; ++id) {
				Slot currentSlot = mc.thePlayer.inventoryContainer.getSlot(id);
				if (currentSlot.getHasStack()) {
					ItemStack currentItem = currentSlot.getStack();
					if (mode.isCurrentMode("Pot")) {
						if (currentItem.getItem() instanceof ItemPotion && isSplashPotion(currentItem)
								&& timer.isDelayComplete((long) ((Double) delay.getValueState()).intValue())) {
							mc.playerController.windowClick(0, id, 0, 1, mc.thePlayer);
							slotId = getFreeSlot();
							timer.reset();
						}
					} else if ((mode.isCurrentMode("Head"))) {
						if (currentItem.getItem() instanceof Item
								&& timer.isDelayComplete((long) ((Double) delay.getValueState()).intValue())) {
							mc.playerController.windowClick(0, id, 0, 1, mc.thePlayer);
							slotId = 397;
							timer.reset();
						}
					}

				}
			}
		}

	}

	private void throwPotion() {
		int slotId = getFreeSlot();
		if (slotId != -1) {
			for (int id = 36; id <= 44; ++id) {
				Slot currentSlot = mc.thePlayer.inventoryContainer.getSlot(id);
				if (currentSlot.getHasStack()) {
					ItemStack currentItem = currentSlot.getStack();
					if (mode.isCurrentMode("Pot")) {
						if (currentItem.getItem() instanceof ItemPotion && isSplashPotion(currentItem)
								&& timer.isDelayComplete((long) ((Double) delay.getValueState()).intValue())) {
							if (currentItem.getItem() instanceof ItemPotion && isSplashPotion(currentItem)
									&& throwTimer.isDelayComplete((long) ((Double) delay.getValueState()).intValue())) {
								int old = mc.thePlayer.inventory.currentItem;
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(id - 36));
								mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
										new BlockPos(-1, -1, -1), -1,
										mc.thePlayer.inventoryContainer.getSlot(id).getStack(), 0.0F, 0.0F, 0.0F));
								mc.thePlayer.inventory.currentItem = id - 36;
								mc.thePlayer.sendQueue.addToSendQueue(
										new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(old));
								mc.thePlayer.inventory.currentItem = old;
								throwTimer.reset();
							}
						}
					} else if ((mode.isCurrentMode("Head"))) {
						if (currentItem.getItem() instanceof Item
								&& throwTimer.isDelayComplete((long) ((Double) delay.getValueState()).intValue())) {
							int old = mc.thePlayer.inventory.currentItem;
							mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(id - 36));
							mc.thePlayer.sendQueue
									.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1,
											mc.thePlayer.inventoryContainer.getSlot(id).getStack(), 0.0F, 0.0F, 0.0F));
							mc.thePlayer.inventory.currentItem = id - 36;
							mc.thePlayer.sendQueue.addToSendQueue(
									new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
							mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(old));
							mc.thePlayer.inventory.currentItem = old;
							throwTimer.reset();
						}
					}

				}
			}
		}

	}

	private boolean isSplashPotion(ItemStack itemStack) {
		return ItemPotion.isSplash(itemStack.getMetadata());
	}

	private int getFreeSlot() {
		for (int id = 36; id < 45; ++id) {
			Slot currentSlot = mc.thePlayer.inventoryContainer.getSlot(id);
			if (!currentSlot.getHasStack()) {
				return 1;
			}
		}

		return -1;
	}

}
