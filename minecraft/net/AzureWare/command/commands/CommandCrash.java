package net.AzureWare.command.commands;


import net.AzureWare.Client;
import net.AzureWare.command.Command;

public class CommandCrash extends Command {
	public CommandCrash(String[] command) {
        super(command);
        this.setArgs("Crash ur client");
    }

    @Override
    public void onCmd(String[] args) {

    	if (args.length == 2) {
    	}
    	super.onCmd(args);
    }
}
