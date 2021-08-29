package cn.winfxk.knickers.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Utils;
import cn.winfxk.knickers.rec.MyPlayer;
import cn.winfxk.knickers.tool.Config;
import cn.winfxk.knickers.tool.Tool;

/**
 * @Createdate 2021/08/27 22:03:22
 * @author Winfxk
 */
public class AdminCommand extends MyCommand {
	public static AdminCommand main;
	public static final String[] AdminKey = { "{Status}", "{ByPlayer}" };
	public static final String[] LangItem = { "{ID}", "{Language}" };

	public AdminCommand() {
		super("AdminCommand");
		main = this;
		commandParameters.clear();
		commandParameters.put(getString("Help"), new CommandParameter[] { new CommandParameter(getString("Help"), false, new String[] { "help", "帮助" }) });
		commandParameters.put(getString("Lang"), new CommandParameter[] { new CommandParameter(getString("Lang"), false, new String[] { "lang", "语言" }) });
		commandParameters.put(getString("Admin"),
				new CommandParameter[] { new CommandParameter(getString("Admin"), false, new String[] { "admin", "管理员" }), new CommandParameter(getString("InputPlayerName"), CommandParamType.TARGET, false) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args == null || args.length <= 0) {
			sender.sendMessage(Tool.getCommandHelp(this));
			return true;
		}
		int index = 0;
		String s;
		switch (args[0].toLowerCase()) {
		case "lang":
		case "language":
		case "语言":
			if (!isAdmin(sender) && sender.isPlayer()) {
				sender.sendMessage(msg.getMessage("notPermission"));
				return true;
			}
			File[] Files = kis.LanguageFile.listFiles(this);
			if (args.length <= 1) {
				try {
					sender.sendMessage(getString("LangItem", LangItem, new Object[] { 0, getLangName(Utils.readFile(getClass().getResourceAsStream("/resource/Message.yml"))) + getString("DefaultLanguage") }));
				} catch (IOException e) {
				}
				for (File file : Files) {
					try {
						sender.sendMessage(getString("LangItem", LangItem, new Object[] { ++index, getLangName(Utils.readFile(file)) }));
					} catch (IOException e) {
					}
				}
				sender.sendMessage(msg.getSun(CommandMsgKey, CommandKey, "LangDescription", "{CMD}", getCommand()));
				return true;
			}
			s = args[1];
			index = Tool.ObjToInt(s);
			if (s == null || s.isEmpty() || !Tool.isInteger(s) || index < 0) {
				sender.sendMessage(getString("LangIDError"));
				return true;
			}
			if (index > Files.length) {
				sender.sendMessage(getString("NotLangID"));
				return true;
			}
			if (index == 0) {
				try {
					msg.reload(s = Utils.readFile(getClass().getResourceAsStream("/resource/Message.yml")));
					msg.load();
					sender.sendMessage(msg.getSun(CommandMsgKey, CommandKey, "SetLangOK", "{Language}", getLangName(s)));
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(msg.getSun(CommandMsgKey, CommandKey, "SetLangError", "{Error}", e.toString()));
					return true;
				}
			}
			try {
				msg.reload(s = Utils.readFile(Files[index - 1]));
				msg.load();
				sender.sendMessage(msg.getSun(CommandMsgKey, CommandKey, "SetLangOK", "{Language}", getLangName(s)));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				sender.sendMessage(msg.getSun(CommandMsgKey, CommandKey, "SetLangError", "{Error}", e.toString()));
				return true;
			}
		case "admin":
		case "管理员":
			if (sender.isPlayer()) {
				sender.sendMessage(msg.getMessage("notPermission"));
				return true;
			}
			if (args.length < 2) {
				kis.config.set("Whitelist", MyPlayer.Whitelist = !kis.config.getBoolean("Whitelist"));
				kis.config.save();
				sender.sendMessage(msg.getSun(CommandMsgKey, CommandKey, "Switch", "{Status}", getString(kis.config.getBoolean("Whitelist") ? "Open" : "Close")));
				return true;
			}
			s = "";
			for (int i = 1; i < args.length; i++)
				s += (s.isEmpty() ? "" : " ") + args[i];
			MyPlayer.setAdmin(s, !MyPlayer.isWhitelist(s));
			sender.sendMessage(msg.getSun(CommandMsgKey, CommandKey, "AdminSwitch", AdminKey, new Object[] { getString(MyPlayer.isWhitelist(s) ? "Open" : "Close"), s }));
			return true;
		default:
			sender.sendMessage(Tool.getCommandHelp(this));
		}
		return true;
	}

	/**
	 * 返回当前命令的最短命令
	 * 
	 * @return
	 */
	public String getCommand() {
		if (getAliases().length > 0) {
			String string = getAliases()[0];
			for (String s : getAliases())
				if (s.length() < string.length())
					string = s;
			return string;
		}
		return getCommand();
	}

	/**
	 * 返回语言Key
	 * 
	 * @param s
	 * @return
	 */
	private Object getLangName(String s) {
		Map<String, Object> map = Config.yaml.loadAs(s, Map.class);
		return map.get("lang");
	}

	/**
	 * 判断玩家是否是管理员
	 * 
	 * @return
	 */
	public static boolean isAdmin(CommandSender player) {
		if (MyPlayer.Whitelist)
			return MyPlayer.getWhitelists().contains(player.getName());
		return player.isOp();
	}
}
