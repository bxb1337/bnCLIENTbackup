package net.AzureWare.command.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

import net.AzureWare.Client;
import net.AzureWare.command.Command;
import net.AzureWare.mod.mods.PLAYER.Spammer;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.minecraft.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;

public class CommandSpammer extends Command {
   private static String fileDir;

   static {
      fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + "AzureWare";
   }

   public CommandSpammer(String[] commands) {
      super(commands);
      this.setArgs("-spammer <Text>");
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
         Spammer.text = msg;
         saveText();
         ClientUtil.sendClientMessage("Changed to " + msg, Notification.Type.SUCCESS);
         super.onCmd(args);
      }
   }

   public static void saveText() {
      File f = new File(fileDir + "/spammer.txt");

      try {
         if(!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         pw.print(Spammer.text);
         pw.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   @SuppressWarnings("resource")
public static void loadText() throws IOException {
      File f = new File(fileDir + "/spammer.txt");
      if(!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            try {
               String text = String.valueOf(line);
               Spammer.text = text;
            } catch (Exception var4) {
               ;
            }
         }
      }

   }
}
