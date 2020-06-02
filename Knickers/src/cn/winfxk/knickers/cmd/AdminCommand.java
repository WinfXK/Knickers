package cn.winfxk.knickers.cmd;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.winfxk.knickers.Activate;
import cn.winfxk.knickers.Message;
import cn.winfxk.knickers.MyPlayer;
import cn.winfxk.knickers.form.AllModule;
import cn.winfxk.knickers.form.Setting;
import cn.winfxk.knickers.module.FunctionBase;
import cn.winfxk.knickers.tool.Tool;

/**
 * 管理员命令
 * 
 * @Createdate 2020/06/03 00:01:50
 * @author Winfxk
 */
public class AdminCommand extends Command {
	private Activate ac;
	private Message msg;

	public AdminCommand(Activate ac) {
		super("Admin" + ac.getPluginBase().getName().toLowerCase(),
				Activate.getActivate().getMessage().getSon("AdminCommand", "Description"),
				"/Admin" + ac.getPluginBase().getName().toLowerCase() + " help", ac.getCommand("AdminCommand"));
		msg = ac.getMessage();
		this.ac = ac;
		commandParameters.clear();
		commandParameters.put(msg.getSon("AdminCommand", "Help"), new CommandParameter[] {
				new CommandParameter(msg.getSon("AdminCommand", "Help"), false, new String[] { "help", "帮助" }) });
		commandParameters.put(msg.getSon("AdminCommand", "Admin"), new CommandParameter[] {
				new CommandParameter(msg.getSon("AdminCommand", "Admin"), false, new String[] { "admin", "管理员" }),
				new CommandParameter(msg.getSon("AdminCommand", "Admin-PlayerName"), CommandParamType.TARGET, false) });
		commandParameters.put(msg.getSon("AdminCommand", "Module"),
				new CommandParameter[] { new CommandParameter(msg.getSon("AdminCommand", "Module"), false,
						new String[] { "module", "mod", "功能" }) });
		commandParameters.put(msg.getSon("AdminCommand", "Setting"),
				new CommandParameter[] { new CommandParameter(msg.getSon("AdminCommand", "Setting"), false,
						new String[] { "setting", "sett", "设置" }) });
		commandParameters.put(msg.getSon("AdminCommand", "OnlyAdmin"), new CommandParameter[] {
				new CommandParameter(msg.getSon("AdminCommand", "OnlyAdmin"), false, new String[] { "oa", "私有管理员" }) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender.isPlayer() && !ac.isAdmin(sender)) {
			sender.sendMessage(msg.getMessage("权限不足"));
			return true;
		}
		if (args.length <= 0) {
			sender.sendMessage(Tool.getCommandHelp(this));
			return true;
		}
		switch (args[0].toLowerCase()) {
		case "oa":
		case "私有管理员":
			if (sender.isPlayer()) {
				sender.sendMessage(msg.getMessage("请在控制台执行命令"));
				return true;
			}
			ac.getConfig().set("OnlyAdmin", !ac.getConfig().getBoolean("OnlyAdmin"));
			sender.sendMessage(msg.getSon("AdminCommand",
					ac.getConfig().getBoolean("OnlyAdmin") ? "OpenOnlyAdmin" : "CloseOnlyAdmin"));
			return ac.getConfig().save();
		case "setting":
		case "sett":
		case "设置":
			if (!sender.isPlayer()) {
				sender.sendMessage(msg.getMessage("请在游戏内自行命令"));
				return true;
			}
			if (!ac.isAdmin(sender)) {
				sender.sendMessage(msg.getMessage("权限不足"));
				return true;
			}
			return ac.getPlayers(sender.getName()).showForm(new Setting((Player) sender, null));
		case "module":
		case "mod":
		case "功能":
			if (sender.isPlayer()) {
				if (!ac.isAdmin(sender)) {
					sender.sendMessage(msg.getMessage("权限不足"));
					return true;
				}
				return ac.getPlayers(sender.getName()).showForm(new AllModule((Player) sender, null));
			}
			for (FunctionBase base : ac.getFunctionMag().getFunction().values())
				sender.sendMessage(msg.getSon("AdminCommand", "ModuleItem",
						new String[] { "{ModuleName}", "{ModuleKey}", "{Enable}" },
						new Object[] { base.getName(), base.getModuleKey(), base.isEnable() }));
			return true;
		case "admin":
		case "管理员":
			if (sender.isPlayer()) {
				sender.sendMessage(msg.getMessage("请在控制台执行命令"));
				return true;
			}
			if (args.length <= 1) {
				sender.sendMessage(msg.getSon("AdminCommand", "NotInputAdmin-PlayerName"));
				return true;
			}
			List<String> list = ac.getConfig().getStringList("Admin");
			if (list.contains(args[1])) {
				list.add(args[1]);
				sender.sendMessage(msg.getSon("AdminCommand", "addAdmin", new String[] { "{ByPlayer}", "{ByMoney}" },
						new Object[] { args[1], MyPlayer.getMoney(args[1]) }));
			} else {
				list.remove(args[1]);
				sender.sendMessage(msg.getSon("AdminCommand", "removeAdmin", new String[] { "{ByPlayer}", "{ByMoney}" },
						new Object[] { args[1], MyPlayer.getMoney(args[1]) }));
			}
			ac.getConfig().set("Admin", list);
			return ac.getConfig().save();
		default:
			sender.sendMessage(Tool.getCommandHelp(this));
			return true;
		}
	}
}
