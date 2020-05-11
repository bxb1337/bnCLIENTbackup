package net.AzureWare.command.commands;

import net.AzureWare.command.Command;
import net.AzureWare.mod.mods.PLAYER.Teams;
import net.AzureWare.mod.mods.WORLD.IRC;
import net.AzureWare.ui.notification.Notification;
import net.AzureWare.utils.ClientUtil;
import net.AzureWare.utils.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CommandWDR extends Command {
	public CommandWDR(String[] commands) {
        super(commands);
        this.setArgs("Fast Watchdog Report");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length != 2) {
            ClientUtil.sendClientMessage(this.getArgs(), Notification.Type.INFO);
            return;
        }

        PlayerUtil.tellPlayer("\247b[AzureWare]\247a举报失败，暂时不可用。");
        super.onCmd(args);
    }
}
