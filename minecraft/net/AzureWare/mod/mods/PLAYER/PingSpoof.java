/*
 * Decompiled with CFR 0_132.
 */
package net.AzureWare.mod.mods.PLAYER;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.darkmagician6.eventapi.EventTarget;

import net.AzureWare.events.EventPacket;
import net.AzureWare.events.EventPacket.EventPacketType;
import net.AzureWare.events.EventPreMotion;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.Mod.Category;
import net.AzureWare.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.ChatComponentText;


public class PingSpoof
extends Mod {
    private List<Packet> packetList = new CopyOnWriteArrayList<Packet>();
    private TimeHelper Packettimer = new TimeHelper();

    public PingSpoof() {
    	super("PingSpoof", Category.PLAYER);
    }


    


    private final HashMap<Packet<?>, Long> packetsMap = new HashMap<>();


    public void onDisable() {
        packetsMap.clear();
        super.onDisable();
    }

	@EventTarget
    public void onPacket(final EventPacket event) {
		if(event.getType() == EventPacketType.SEND )
		{
	        final Packet packet = event.getPacket();

	        if ((packet instanceof C00PacketKeepAlive || packet instanceof C16PacketClientStatus) && !(mc.thePlayer.isDead || mc.thePlayer.getHealth() <= 0) && !packetsMap.containsKey(packet)) {
	            event.setCancelled(true);

	            synchronized(packetsMap) {
	                packetsMap.put(packet, System.currentTimeMillis() +1000l);
	            }
	        }
		}

    }

	@EventTarget
    public void onUpdate(final EventPreMotion event) {
        try {
            synchronized(packetsMap) {
                for(final Iterator<Map.Entry<Packet<?>, Long>> iterator = packetsMap.entrySet().iterator(); iterator.hasNext(); ) {
                    final Map.Entry<Packet<?>, Long> entry = iterator.next();

                    if(entry.getValue() < System.currentTimeMillis()) {
                        mc.getNetHandler().addToSendQueue(entry.getKey());
                        iterator.remove();
                    }
                }
            }
        }catch(final Throwable t) {
            t.printStackTrace();
        }
    }
}

