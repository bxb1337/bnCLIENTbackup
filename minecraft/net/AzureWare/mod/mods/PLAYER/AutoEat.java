package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class AutoEat extends Mod {
   private int bestSlot;

public AutoEat() {
      super("AutoEat", Category.PLAYER);
   }

   private int getFoodPos() {
		for (int i = 0; i < 9; i++) {
			ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
			if (item == null) {
				continue;
			}
			float restoration = 0;
			float bestRestoration = 0;
			if (item.getItem() instanceof ItemFood) {
				restoration = ((ItemFood) item.getItem()).getSaturationModifier(item);
			}

			if (restoration > bestRestoration) {
				bestRestoration = restoration;
				bestSlot = i;
			}
		}
		if(this.mc.thePlayer.inventory.MainInventory[bestSlot] == null) {
			return -1;
		}
		return bestSlot;
   }
   
   @EventTarget
   public void onUpdate(EventUpdate event) {
	   if(getFoodPos() == -1) {
		   this.setDisplayName("NoFood");
		   return;
	   }
	   
	   if(mc.thePlayer.getFoodStats().getFoodLevel() <= 18) {
		    this.setDisplayName("Eating");
			this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getFoodPos()));
			this.mc.thePlayer.sendQueue.addToSendQueue(
					new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.MainInventory[getFoodPos()]));
			for (int i = 0; i < 32; i++) {
				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			}
			this.mc.thePlayer.stopUsingItem();
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
	   }else {
		   this.setDisplayName("MaxFoodLevel");
	   }
   }
}
