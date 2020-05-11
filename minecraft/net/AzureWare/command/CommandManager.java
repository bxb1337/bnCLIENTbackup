package net.AzureWare.command;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import net.AzureWare.Client;
import net.AzureWare.command.commands.CommandBind;
import net.AzureWare.command.commands.CommandConfig;
import net.AzureWare.command.commands.CommandCrash;
import net.AzureWare.command.commands.CommandFcmd;
import net.AzureWare.command.commands.CommandIGN;
import net.AzureWare.command.commands.CommandIRC;
import net.AzureWare.command.commands.CommandPrefix;
import net.AzureWare.command.commands.CommandSay;
import net.AzureWare.command.commands.CommandSpammer;
import net.AzureWare.command.commands.CommandTeleport;
import net.AzureWare.command.commands.CommandTitle;
import net.AzureWare.command.commands.CommandToggle;
import net.AzureWare.command.commands.CommandUser;
import net.AzureWare.command.commands.CommandWDR;
import net.minecraft.Wrapper;
import net.minecraft.client.main.Main;

public class CommandManager {
	private static ArrayList<Command> commands = new ArrayList();
	
	public CommandManager() {
		commands.add(new CommandBind(new String[]{"bind"}));
		commands.add(new CommandToggle(new String[]{"toggle", "t"}));
		commands.add(new CommandIGN(new String[]{"ign"}));
		commands.add(new CommandSpammer(new String[]{"spammer"}));
		commands.add(new CommandPrefix(new String[]{"prefix"}));
		commands.add(new CommandTitle(new String[]{"title"}));
		commands.add(new CommandSay(new String[]{"say"}));
		commands.add(new CommandIRC(new String[]{"irc"}));
		commands.add(new CommandConfig(new String[]{"config", "setting", "autoconfig"}));
		commands.add(new CommandWDR(new String[]{"wdr", "watchdogreport"}));
		commands.add(new CommandFcmd(new String[]{"fcmd"}));
		commands.add(new CommandCrash(new String[]{"crash"}));
		commands.add(new CommandUser(new String[]{"checkuser"}));
		commands.add(new CommandTeleport(new String[]{"tp"}));

	}
	
	public static ArrayList<Command> getCommands() {
        return commands;
    }
}
