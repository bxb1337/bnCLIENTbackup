package net.AzureWare.command.commands;

import net.AzureWare.Client;
import net.AzureWare.command.Command;
import net.AzureWare.utils.PlayerUtil;

public class CommandUser extends Command {
	public CommandUser(String[] commands) {
        super(commands);
        this.setArgs("Check online user");
    }

    @Override
    public void onCmd(String[] args) {


        super.onCmd(args);
    }
}
