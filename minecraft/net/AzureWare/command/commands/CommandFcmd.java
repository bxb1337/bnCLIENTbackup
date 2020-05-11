package net.AzureWare.command.commands;


import net.AzureWare.Client;
import net.AzureWare.command.Command;

public class CommandFcmd extends Command {
	public CommandFcmd(String[] command) {
		super(command);
		this.setArgs("nmsl");
	}

	@Override
	public void onCmd(String[] args) {

		if (args.length >= 3) {
			String msg = "";
			int i = 2;
			while (i <= args.length - 1) {
				msg = msg + args[i] + " ";
				++i;
			}
		}
		super.onCmd(args);
	}
}
