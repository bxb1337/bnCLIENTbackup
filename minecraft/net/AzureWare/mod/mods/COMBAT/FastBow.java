package net.AzureWare.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;
import com.jcraft.jogg.Packet;

import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastBow extends Mod
{
    public FastBow() {
        super("FastBow", Mod.Category.COMBAT);
    }
    
    @EventTarget
    public void onPre(final EventPreMotion event) {
        if (this.mc.thePlayer.getItemInUse() != null && this.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow && this.mc.thePlayer.getItemInUseDuration() == 5) {
            this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);
        }
        if (this.mc.thePlayer.getItemInUse() != null && this.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) {
            for (int i = 0; i < 25; ++i) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
        }
    }
}