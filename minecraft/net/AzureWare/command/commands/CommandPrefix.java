package net.AzureWare.command.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.AzureWare.command.Command;
import net.AzureWare.mod.mods.PLAYER.AutoAbuse;
import net.AzureWare.mod.mods.PLAYER.Spammer;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.minecraft.client.Minecraft;

public class CommandPrefix extends Command {
   private static String fileDir;

   static {
      fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + "AzureWare";
   }

   public CommandPrefix(String[] commands) {
      super(commands);
      this.setArgs("-prefix <Text>");
   }

   public void onCmd(String[] args) {
      if(args.length != 2) {
         ClientUtil.sendClientMessage(this.getArgs(), Notification.Type.WARNING);
      } else {
         Spammer.prefix = args[1];
         AutoAbuse.prefix = args[1];
         saveText();
         ClientUtil.sendClientMessage("Changed to " + args[1], Notification.Type.SUCCESS);
         super.onCmd(args);
      }
   }

   public static void saveText() {
      File f = new File(fileDir + "/prefix.txt");

      try {
         if(!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         pw.print(Spammer.prefix);
         pw.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   @SuppressWarnings("resource")
public static void loadText() throws IOException {
      File f = new File(fileDir + "/prefix.txt");
      if(!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            try {
               String text = String.valueOf(line);
               Spammer.prefix = text;
               AutoAbuse.prefix = text;
            } catch (Exception var4) {
               ;
            }
         }
      }

   }
}
