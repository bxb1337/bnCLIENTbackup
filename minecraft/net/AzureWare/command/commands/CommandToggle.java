package net.AzureWare.command.commands;

import net.AzureWare.command.Command;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;

public class CommandToggle extends Command {
    public CommandToggle(String[] commands) {
        super(commands);
        this.setArgs("-toggle <mod>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length != 2) {
            ClientUtil.sendClientMessage(this.getArgs(), Notification.Type.INFO);
            return;
        }
        boolean found = false;
        for (Mod mod : ModManager.getModList()) {
            if (!args[1].equalsIgnoreCase(mod.getName())) continue;
            try {
				mod.set(!mod.isEnabled());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            found = true;
            ClientUtil.sendClientMessage(String.valueOf(mod.getName()) + " was toggled", Notification.Type.SUCCESS);
            break;
        }
        if (!found) {
            ClientUtil.sendClientMessage("Cannot find Module : " + args[1], Notification.Type.WARNING);
        }
    }
}
