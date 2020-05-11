package net.AzureWare.command.commands;


import net.AzureWare.command.Command;
import net.AzureWare.mod.ModManager;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;

public class CommandIRC extends Command {
    public CommandIRC(String[] commands) {
        super(commands);
        this.setArgs("-IRC <Text>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length == 1) {
            ClientUtil.sendClientMessage(this.getArgs(), Notification.Type.INFO);
            return;
        }else {
        	  String msg = "";
              int i = 1;
              while (i < args.length) {
                  msg = String.valueOf((Object)String.valueOf((Object)msg)) + args[i] + " ";
                  ++i;
        }
              if(ModManager.getModByName("IRC").isEnabled()) {
              }else {
            	  ClientUtil.sendClientMessage("Open your IRC first then you can send message!", Notification.Type.ERROR);
              }
      }
    }
}
