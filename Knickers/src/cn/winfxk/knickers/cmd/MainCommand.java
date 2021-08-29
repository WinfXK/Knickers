package cn.winfxk.knickers.cmd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.winfxk.knickers.MakeMenu;
import cn.winfxk.knickers.rec.MyPlayer;
import cn.winfxk.knickers.tool.Config;
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
		commandParameters.put(getString("Open"), new CommandParameter[] { new CommandParameter(getString("Open"), false, new String[] { "show", "open" }),
				new MyParamter(getString("Files"), false, MakeMenu.MenuFile.list(this)), new CommandParameter(getString("Click"), true) });
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
			return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, kis.Main));
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
			File menuFile = null;
			for (File file : files) {
				string = file.getName();
				if (string.equals(name)) {
					menuFile = file;
					break;
				}
			}
			if (menuFile == null) {
				for (File file : files) {
					string = file.getName().toLowerCase();
					if (string.equals(name.toLowerCase())) {
						menuFile = file;
						break;
					}
				}
				if (menuFile == null)
					for (File file : files) {
						string = file.getName().toLowerCase();
						if (string.contains(name.toLowerCase())) {
							menuFile = file;
							break;
						}
					}
			}
			if (menuFile == null) {
				sender.sendMessage(getString("NotMenuFile"));
				return true;
			}
			if (args.length == 2)
				return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, menuFile));
			if (args.length >= 3) {
				String a = args[2];
				if (a == null || a.isEmpty())
					return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, menuFile));
				Config config = new Config(menuFile);
				Object obj = config.get("Buttons");
				LinkedHashMap<String, Object> Buttons = new LinkedHashMap<>(obj == null || !(obj instanceof Map) ? new HashMap<>() : (Map<String, Object>) obj);
				if (Buttons.size() <= 0) {
					sender.sendMessage(getString("EmptyButton"));
					return true;
				}
				List<String> Keys = new ArrayList<>(Buttons.keySet());
				String Key = null;
				for (String s : Keys)
					if (s.equals(a)) {
						Key = s;
						break;
					}
				if (Key == null) {
					for (String s : Keys)
						if (s.toLowerCase().equals(a.toLowerCase())) {
							Key = s;
							break;
						}
					if (Key == null)
						for (String s : Keys)
							if (s.toLowerCase().contains(a.toLowerCase())) {
								Key = s;
								break;
							}
				}
				if (Key == null) {
					sender.sendMessage(getString("NotButtonKey"));
					return true;
				}
				obj = Buttons.get(Key);
				Map<String, Object> map = obj != null && obj instanceof Map ? (HashMap<String, Object>) obj : new HashMap<>();
				if (map.size() <= 0) {
					sender.sendMessage(getString("ButtonError"));
					return true;
				}
				return new MakeMenu(myPlayer.getPlayer(), null, menuFile).onClick(map);
			}
			return true;
		case "main":
		case "主页":
			return myPlayer.show(new MakeMenu(myPlayer.getPlayer(), null, kis.Main));
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
