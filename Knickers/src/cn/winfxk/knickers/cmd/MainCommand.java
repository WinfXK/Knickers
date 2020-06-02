package cn.winfxk.knickers.cmd;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Message;
import cn.winfxk.knickers.MyPlayer;
import cn.winfxk.knickers.module.menu.OpenMenu;
import cn.winfxk.knickers.tool.Tool;

/**
 * 主命令类
 * 
 * @Createdate 2020/05/22 19:03:12
 * @author Winfxk
 */
public class MainCommand extends Command {
	public static final String Permission = "Knickers.Command.main";
	private Activate ac;
	private Message msg;

	public MainCommand(Activate ac) {
		super(ac.getPluginBase().getName().toLowerCase(),
				Activate.getActivate().getMessage().getSon("MainCommand", "Description"),
				"/" + ac.getPluginBase().getName().toLowerCase() + " help", ac.getCommand("MainCommand"));
		msg = ac.getMessage();
		this.ac = ac;
		commandParameters.clear();
		commandParameters.put(msg.getSon("MainCommand", "Help"), new CommandParameter[] {
				new CommandParameter(msg.getSon("MainCommand", "Help"), false, new String[] { "help", "帮助" }) });
		commandParameters.put(msg.getSon("MainCommand", "Tool"), new CommandParameter[] {
				new CommandParameter(msg.getSon("MainCommand", "Tool"), false, new String[] { "tool", "工具" }) });
		commandParameters.put(msg.getSon("MainCommand", "Main"), new CommandParameter[] {
				new CommandParameter(msg.getSon("MainCommand", "Main"), false, new String[] { "main", "主页" }) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.isPlayer()) {
			sender.sendMessage(Tool.getCommandHelp(this));
			sender.sendMessage(msg.getMessage("请在游戏内自行命令"));
			return true;
		}
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(msg.getMessage("权限不足"));
			return true;
		}
		MyPlayer myPlayer = ac.getPlayers(sender.getName());
		if (args == null || args.length <= 0)
			return myPlayer.showForm(new OpenMenu(myPlayer.getPlayer(), null, ac.getMainMenu()));
		switch (args[0].toLowerCase()) {
		case "tool":
		case "工具":
			if (ac.isToolItem((Player) sender)) {
				sender.sendMessage(msg.getSon("MainCommand", "PossessTool"));
				return true;
			}
			sender.sendMessage(msg.getMessage("GiveTool", (Player) sender));
			return true;
		case "main":
		case "主页":
			return myPlayer.showForm(new OpenMenu(myPlayer.getPlayer(), null, ac.getMainMenu()));
		default:
			sender.sendMessage(Tool.getCommandHelp(this));
			return true;
		}
	}
}
