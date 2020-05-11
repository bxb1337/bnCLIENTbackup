package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.PlayerUtil;
import net.AzureWare.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;

public class FastUse extends Mod{
	
	//private Value<Double> delay = new Value("FastUse_Delay", Double.valueOf(15.0D), Double.valueOf(0.0D), Double.valueOf(35.0D), 1.0D);
	private Value<Boolean> invisible = new Value<Boolean>("FastUse_OnGround", false);

	public FastUse() {
		super("FastUse", Category.PLAYER);
	}
	
    @EventTarget
    public void onTick(EventUpdate event) {
    	
    	 if(this.mc.thePlayer.getCurrentEquippedItem() != null && (this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFood || this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemPotion) && this.mc.gameSettings.keyBindDrop.isKeyDown() && (!this.invisible.getValueState().booleanValue() || this.mc.thePlayer.onGround) && this.mc.thePlayer.ticksExisted % 4 == 0) {
             for(int var2 = 0; var2 < 2; ++var2) {
                double var3 = this.mc.thePlayer.posX;
                double var5 = this.mc.thePlayer.posY + 1.0E-9D;
                double var7 = this.mc.thePlayer.posZ;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(var3, var5, var7, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
             }
          }

    }



}
