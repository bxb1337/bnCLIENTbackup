package net.AzureWare.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.lwjgl.opengl.Display;

import net.AzureWare.command.Command;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.minecraft.client.Minecraft;

public class CommandIGN extends Command {
	public CommandIGN(String[] commands) {
        super(commands);
        this.setArgs("Copies IngameName to Clipboard");
    }

    @Override
    public void onCmd(String[] args) {
        StringSelection stringSelection = new StringSelection(Minecraft.getMinecraft().session.getUsername());
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        ClientUtil.sendClientMessage("Saved to Clipboard!", Notification.Type.INFO);
        super.onCmd(args);
    }
}
