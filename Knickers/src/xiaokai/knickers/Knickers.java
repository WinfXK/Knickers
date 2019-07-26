package xiaokai.knickers;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.TextFormat;
import xiaokai.knickers.event.Monitor;
import xiaokai.knickers.event.PlayerEvent;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Belle;
import xiaokai.knickers.mtp.Kick;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Knickers extends PluginBase {
	private Instant loadTime = Instant.now();
	/**
	 * 插件缓存数据集合
	 */
	protected static Kick kick;

	@Override
	public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
		if (player.isPlayer()) {
			new Thread() {
				public void run() {
					Belle.exMaterials((Player) player);
				}
			}.start();
			if (args.length < 1)
				return MakeForm.Main((Player) player);
		} else if (args.length < 1)
			return false;
		switch (args[0]) {
		case "help":
		case "h":
		case "帮助":
			player.sendMessage(
					"§4=====§6=====§a=====§e=====§f[§9命令帮助§f]§e=====§a=====§6=====§4=====\n§f/§bmis §dhelp §c:§9打开命令帮助\n§f/§bmis §dadmin §2<§3玩家名§2> §c:§9添加或删除管理员权限\n§f/§bmis §dshow §2<§3文件名§2> §c:§9打开一个界面[文件名支持只写部分]\n§f/§bmis §dui §c:§9打开主页面\n§f/§bmis §c:§9打开主页并且检查是否拥有快捷工具");
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
			List<Object> list = kick.config.getList("白名单") == null ? new ArrayList<Object>()
					: kick.config.getList("白名单");
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
			File dFile = new File(getDataFolder(), Kick.MenuConfigPath);
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
				return MakeForm.OpenMenu((Player) player, file);
			else
				player.sendMessage(kick.Message.getSon("命令", "无法打开界面", new String[] { "{Error}", "{Player}" },
						new Object[] { "找不到该界面", player.getName() }));
			return true;
		case "ui":
			if (!player.isPlayer()) {
				player.sendMessage("§4请在游戏内执行此命令！");
				return true;
			}
			return MakeForm.Main((Player) player);
		}
		return false;
	}

	/**
	 * 明人不说暗话！这就是插件启动事件
	 */
	@Override
	public void onEnable() {
		super.onEnable();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerEvent(kick), this);
		pm.registerEvents(new Monitor(kick), this);
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "启动！") + "§6耗时：§9"
				+ ((float) (Duration.between(loadTime, Instant.now()).toMillis()) / 1000));
	}

	/**
	 * 返回货币的名称，如“金币”
	 * 
	 * @return
	 */
	public static String getMoneyName() {
		return kick.config.getString("货币单位");
	}

	/**
	 * ????这都看不懂？？这是插件关闭事件
	 */
	@Override
	public void onDisable() {
		this.getServer().getLogger()
				.info(Tool.getColorFont(this.getName() + "关闭！") + TextFormat.GREEN + "本次运行时长" + TextFormat.BLUE
						+ Tool.getTimeBy(((float) (Duration.between(loadTime, Instant.now()).toMillis()) / 1000)));
		super.onDisable();
	}

	/**
	 * PY已准备好！插件加载事件
	 */
	@Override
	public void onLoad() {
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "正在加载..."));
		kick = new Kick(this);
	}

	public static Kick getKick() {
		return kick;
	}

	/**
	 * 快来和本插件PY交易吧~
	 * 
	 * @return 插件主类对象
	 */
	public static Knickers getPY() {
		return kick.mis;
	}
}
