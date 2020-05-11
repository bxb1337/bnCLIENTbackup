package net.AzureWare.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.lwjgl.opengl.Display;

import net.AzureWare.command.Command;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class CommandSay extends Command {
	public CommandSay(String[] commands) {
        super(commands);
    }

    @Override
    public void onCmd(String[] args) {
    	if(args.length == 1) {
            ClientUtil.sendClientMessage("-say <Text>", Notification.Type.WARNING);
         } else {
        	 String msg = "";
             int i = 1;
             while (i < args.length) {
                 msg = String.valueOf((Object)String.valueOf((Object)msg)) + args[i] + " ";
                 ++i;
             }
             msg = msg.substring(0, msg.length() - 1);
             
         	Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C01PacketChatMessage(msg));
         }
    	
    	
        super.onCmd(args);
    }
}
