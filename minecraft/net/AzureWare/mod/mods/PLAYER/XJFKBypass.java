package net.AzureWare.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;
import net.AzureWare.Client;
import net.AzureWare.events.EventUpdate;
import net.AzureWare.mod.Mod;
import net.AzureWare.utils.TimeHelper;
import net.AzureWare.value.Value;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import javax.swing.JOptionPane;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class XJFKBypass extends Mod {
   public XJFKBypass() {
      super("XJFKBypass", Category.PLAYER);
   }

   public void onEnable() {
   }
   
}
