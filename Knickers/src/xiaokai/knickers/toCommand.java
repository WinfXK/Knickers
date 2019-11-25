package xiaokai.knickers;

import xiaokai.knickers.appliance.EstablishForm;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Belle;
import xiaokai.knickers.mtp.Kick;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

/**
 * @author Winfxk
 */
public class toCommand extends Command implements FilenameFilter {
	private final String Permission = "Knickers.Command.main";
	private Kick kick;

	public toCommand() {
		super("mis", "§9打开菜单主页", "§b/§6mis §chelp", new String[] { "菜单", "knickers", "内裤", "胖次", "kis" });
		kick = Kick.kick;
		this.setPermission(Permission);
		this.commandParameters.clear();
		commandParameters.put("添加或删除管理员权限",
				new CommandParameter[] { new CommandParameter("添加或删除管理员权限", false, new String[] { "admin", "管理" }),
						new CommandParameter("要添加或删除管理员权限的玩家名称", CommandParamType.TARGET, false) });
		commandParameters.put("打开一个界面",
				new CommandParameter[] { new CommandParameter("打开一个界面", false, new String[] { "show", "open" }),
						new CommandParameter("要打开的界面的名字[文件名支持只写部分]", true,
								new File(kick.mis.getDataFolder(), Kick.MenuConfigPath).list(this)) });
		commandParameters.put("打开命令帮助",
				new CommandParameter[] { new CommandParameter("打开命令帮助", false, new String[] { "help", "帮助" }) });
		commandParameters.put("打开主页面",
				new CommandParameter[] { new CommandParameter("打开主页面", false, new String[] { "ui", "主页" }) });
		commandParameters.put("查看快捷工具页面",
				new CommandParameter[] { new CommandParameter("查看快捷工具页面", false, new String[] { "tool", "快捷工具" }) });
	}

	@Override
	public boolean execute(CommandSender player, String commandLabel, String[] args) {
		if (!Kick.kick.mis.isEnabled())
			return false;
		if (!player.hasPermission(Permission)) {
			player.sendMessage(kick.Message.getMessage("权限不足"));
			return true;
		}
		if (player.isPlayer()) {
			new Thread() {
				@Override
				public void run() {
					Belle.exMaterials((Player) player);
				}
			}.start();
			if (args.length < 1)
				return MakeForm.Main((Player) player);
		} else if (args.length < 1)
			return false;
		switch (args[0]) {
		case "tool":
		case "快捷工具":
			if (!player.isPlayer()) {
				player.sendMessage("§4请在游戏内执行此命令！");
				return true;
			} else
				return EstablishForm.Main((Player) player);
		case "help":
		case "h":
		case "帮助":
			player.sendMessage(
					"§4=====§6=====§a=====§e=====§f[§9命令帮助§f]§e=====§a=====§6=====§4=====\n§f/§bmis §dhelp §c:§9打开命令帮助\n§f/§bmis §dadmin §2<§3玩家名§2> §c:§9添加或删除管理员权限\n§f/§bmis §dshow §2<§3文件名§2> §c:§9打开一个界面[文件名支持只写部分]\n§f/§bmis §dui §c:§9打开主页面\n§f/§bmis §c:§9打开主页并且检查是否拥有快捷工具\n§f/§bmis §dtool §c:§9查看快捷工具页面");
			return true;
		case "admin":
		case "a":
		case "管理":
			if (!Kick.isAdmin(player)) {
				player.sendMessage(kick.Message.getMessage("权限不足"));
				return true;
			}
			if (args.length < 2 || args[1] == null || args[1].isEmpty()) {
				player.sendMessage("§4请输入想要添加或删除管理员的玩家名称！");
				return true;
			}
			List<Object> list = kick.config.getList("白名单") == null ? new ArrayList<>() : kick.config.getList("白名单");
			if (list.contains(args[1])) {
				for (int i = 0; i < list.size(); i++)
					if (list.get(i).equals(args[1]))
						list.remove(i);
			} else
				list.add(args[1]);
			kick.config.set("白名单", list);
			if (kick.config.save())
				player.sendMessage("§6您已" + (list.contains(args[1]) ? "§e添加§9" : "§4删除§9") + args[1] + "§6的管理员权限");
			else
				player.sendMessage("§4设置异常！");
			return true;
		case "show":
		case "打开":
		case "open":
			if (!player.isPlayer()) {
				player.sendMessage("§4请在游戏内执行此命令！");
				return true;
			}
			if (args.length < 2 || args[1] == null || args[1].isEmpty()) {
				player.sendMessage(kick.Message.getSon("命令", "未输入想要打开的界面的配置文件名", new String[] { "{Player}" },
						new String[] { player.getName() }));
				return true;
			}
			String ConfigName = args[1];
			File dFile = new File(kick.mis.getDataFolder(), Kick.MenuConfigPath);
			File file = new File(dFile, ConfigName);
			boolean isOK = false;
			if (!file.exists()) {
				String ConfigNames = (ConfigName.lastIndexOf("yml") == ConfigName.length() - 3) ? ConfigName
						: ConfigName + ".yml";
				file = new File(dFile, ConfigNames);
				if (!file.exists()) {
					for (String FileN : dFile.list()) {
						file = new File(dFile, FileN);
						if (file.isFile() && FileN.contains(ConfigName)) {
							isOK = true;
							break;
						}
					}
				} else
					isOK = file.isFile();
			} else
				isOK = file.isFile();
			if (isOK)
				return MakeForm.OpenMenu((Player) player, file, true, false);
			else
				player.sendMessage(kick.Message.getSon("命令", "无法打开界面", new String[] { "{Error}", "{Player}" },
						new Object[] { "找不到该界面", player.getName() }));
			return true;
		case "ui":
		case "主页":
			if (!player.isPlayer()) {
				player.sendMessage("§4请在游戏内执行此命令！");
				return true;
			}
			return MakeForm.Main((Player) player);
		}
		return false;
	}

	@Override
	public boolean accept(File arg0, String arg1) {
		return new File(arg0, arg1).isFile();
	}
}
