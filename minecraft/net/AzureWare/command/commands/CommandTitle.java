package net.AzureWare.command.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;

import net.AzureWare.Client;
import net.AzureWare.command.Command;
import net.AzureWare.mod.mods.PLAYER.Spammer;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.minecraft.client.Minecraft;

public class CommandTitle extends Command {
   private static String fileDir;
   public static String Title = Client.CLIENT_NAME + " " + Client.CLIENT_VERSION;

   public CommandTitle(String[] commands) {
      super(commands);
      this.setArgs("-title <Text>");
   }

   public void onCmd(String[] args) {
      if(args.length == 1) {
         ClientUtil.sendClientMessage(this.getArgs(), Notification.Type.WARNING);
      } else {
    	  String msg = "";
          int i = 1;
          while (i < args.length) {
              msg = String.valueOf((Object)String.valueOf((Object)msg)) + args[i] + " ";
              ++i;
          }
          msg = msg.substring(0, msg.length() - 1);
         Display.setTitle(msg);
         ClientUtil.sendClientMessage("Changed to " + msg, Notification.Type.SUCCESS);
         this.Title = msg;
         super.onCmd(args);
      }
   }
   


}
