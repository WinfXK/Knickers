package cn.winfxk.knickers.cmd;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

/**
 * @Createdate 2021/08/27 22:03:22
 * @author Winfxk
 */
public class AdminCommand extends MyCommand {
	public static final String Permission = "Knickers.Command.Admin";
	public static AdminCommand main;

	public AdminCommand() {
		super("AdminCommand");
		main = this;
		commandParameters.clear();
		commandParameters.put(getString("Help"), new CommandParameter[] { new CommandParameter(getString("Help"), false, new String[] { "help", "帮助" }) });
		commandParameters.put(getString("Admin"),
				new CommandParameter[] { new CommandParameter(getString("Admin"), false, new String[] { "admin", "管理员" }), new CommandParameter(getString("InputPlayerName"), CommandParamType.TARGET, false) });
		commandParameters.put(getString("Module"), new CommandParameter[] { new CommandParameter(getString("Help"), false, new String[] { "module", "mod", "功能" }) });
		commandParameters.put(getString("Setting"), new CommandParameter[] { new CommandParameter(getString("Help"), false, new String[] { "setting", "sett", "设置" }) });
		commandParameters.put(getString("oa"), new CommandParameter[] { new CommandParameter(getString("Help"), false, new String[] { "oa", "私有管理员" }) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		return false;
	}
}
