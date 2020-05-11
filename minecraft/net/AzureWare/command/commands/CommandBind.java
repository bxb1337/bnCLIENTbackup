package net.AzureWare.command.commands;

import java.lang.management.ManagementFactory;

import org.lwjgl.input.Keyboard;

import net.AzureWare.Client;
import net.AzureWare.command.Command;
import net.AzureWare.mod.Mod;
import net.AzureWare.mod.ModManager;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.minecraft.Wrapper;
import net.minecraft.client.main.Main;

public class CommandBind extends Command {
	public CommandBind(String[] command) {
        super(command);
        this.setArgs("-bind <mod> <key>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 3) {
            ClientUtil.sendClientMessage(this.getArgs(), Notification.Type.WARNING);
        } else {   
            String mod = args[1];
            int key = Keyboard.getKeyIndex((String)args[2].toUpperCase());
            for (Mod m : ModManager.getModList()) {
                if (!m.getName().equalsIgnoreCase(mod)) continue;
                m.setKey(key);
                ClientUtil.sendClientMessage(String.valueOf(m.getName()) + " was bound to " + Keyboard.getKeyName((int)key), Keyboard.getKeyName((int)key).equals("NONE") ? Notification.Type.ERROR : Notification.Type.SUCCESS);
                Client.getInstance().getFileUtil().saveKeys();
                return;
            }
            ClientUtil.sendClientMessage("Cannot find Module : " + mod, Notification.Type.ERROR);
        }
    }
}
