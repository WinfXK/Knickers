package cn.winfxk.knickers.cmd;

import java.io.File;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.winfxk.knickers.MakeMenu;
import cn.winfxk.knickers.rec.MyPlayer;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/27 22:03:38
 * @author Winfxk
 */
public class MainCommand extends MyCommand {
	public static final String Permission = "Knickers.Command.Player";
	public static MainCommand main;

	public MainCommand() {
		super("MainCommand");
		main = this;
		commandParameters.clear();
		commandParameters.put(getString("Help"), new CommandParameter[] { new CommandParameter(getString("Help"), false, new String[] { "help", "帮助" }) });
		commandParameters.put(getString("Tool"), new CommandParameter[] { new CommandParameter(getString("Tool"), false, new String[] { "tool", "工具" }) });
		commandParameters.put(getString("Main"), new CommandParameter[] { new CommandParameter(getString("Main"), false, new String[] { "main", "主页" }) });
		commandParameters.put(getString("Open"),
				new CommandParameter[] { new CommandParameter(getString("Open"), false, new String[] { "show", "open" }), new CommandParameter(getString("Files"), true, MakeMenu.MenuFile.list(this)) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.isPlayer()) {
			sender.sendMessage(Tool.getCommandHelp(this, AdminCommand.main));
			sender.sendMessage(getNotPlayer());
			return true;
		}
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(msg.getMessage("权限不足"));
			return true;
		}
		String string;
		MyPlayer myPlayer = kis.MyPlayers.get(sender.getName());
		if (args == null || args.length <= 0)
			return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, MakeMenu.MenuFile));
		switch (args[0].toLowerCase()) {
		case "show":
		case "open":
			if (args.length <= 1) {
				sender.sendMessage(getString("NotInputMenuName"));
				return true;
			}
			String name = args[1];
			if (name == null || name.isEmpty()) {
				sender.sendMessage(getString("NotInputMenuName"));
				return true;
			}
			File[] files = MakeMenu.MenuFile.listFiles(this);
			for (File file : files) {
				string = file.getName();
				if (string.equals(name))
					return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, file));
			}
			for (File file : files) {
				string = file.getName().toLowerCase();
				if (string.equals(name))
					return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, file));
			}
			for (File file : files) {
				string = file.getName().toLowerCase();
				if (string.contains(name))
					return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, file));
			}
			sender.sendMessage(getString("NotMenuFile"));
			return true;
		case "main":
		case "主页":
			return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, MakeMenu.MenuFile));
		case "tool":
		case "工具":
			if (kis.isFastTool(myPlayer.getPlayer())) {
				sender.sendMessage(getString("HoldTool", myPlayer.getPlayer()));
				return true;
			}
			myPlayer.getPlayer().getInventory().addItem(kis.getFastTool());
			sender.sendMessage(kis.message.getMessage("给工具", myPlayer.getPlayer()));
			return true;
		case "help":
		case "帮助":
		default:
			sender.sendMessage(Tool.getCommandHelp(this, AdminCommand.main));
			return true;
		}
	}
}
