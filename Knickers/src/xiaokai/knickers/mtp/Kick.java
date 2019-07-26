package xiaokai.knickers.mtp;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import xiaokai.knickers.Knickers;
import xiaokai.tool.Tool;
import xiaokai.tool.Update;

/**
 * @author Winfxk
 */
public class Kick {
	public static Kick kick;
	/**
	 * 插件主累对象
	 */
	public Knickers mis;
	/**
	 * 插件猪被子文件 <b>config</b></br>
	 * 表单ID配置文件 <b>formIdConfig</b>
	 */
	public Config config, formIdConfig;
	/**
	 * 系统配置文件的文件名
	 */
	public String ConfigName = "Config.yml";
	/**
	 * 菜单主页文件名
	 */
	public String MainFileName = "Main.yml";
	/**
	 * 表单ID存储类
	 */
	public FormID formID;
	/**
	 * 消息文件存储文件名
	 */
	public String MsgName = "Message.yml";
	/**
	 * 消息文件类
	 */
	public Message Message;
	/**
	 * 要初始化的表单ID键值
	 */
	public String[] FormIDName = { "主页", "添加按钮创建界面", "添加按钮输入数据界面", "删除按钮时显示按钮列表的界面", "删除按钮时提示时候删除的界面",
			"显示在线玩家列表以供玩家传送的界面", "在Tpa传送的时候，目标玩家点击确定取消的界面", "{Msg}型命令执行界面", "子页4", "设置页面", "子页0", "子页1", "子页3", "子页2" };
	/**
	 * 表单ID存储位置
	 */
	public String FormIDConfigName = "FormID.yml";
	/**
	 * 玩家数据库
	 */
	public LinkedHashMap<String, MyPlayer> PlayerDataMap = new LinkedHashMap<String, MyPlayer>();
	/**
	 * 要检查默认设置的配置文件
	 */
	public String[] LoadFileName = { ConfigName, MainFileName, MsgName };
	/**
	 * 要检查数据是否匹配的配置文件
	 */
	public String[] isLoadFileName = { ConfigName, MsgName };
	/**
	 * 能创建的按钮的类型
	 */
	public static final String[] ButtonTypeList = { "提示一个窗口", "打开一个新的界面", "执行命令", "定点传送", "传送到在线玩家" };
	/**
	 * 添加的菜单的配置文件存储位置
	 */
	public static final String MenuConfigPath = "Menus/";
	/**
	 * 在启动服务器时检查文件夹是否创建，要检查的列表
	 */
	public static final String[] LoadDirList = { MenuConfigPath };

	public Kick(Knickers knickers) {
		kick = this;
		if (!knickers.getDataFolder().exists())
			knickers.getDataFolder().mkdirs();
		mis = knickers;
		formIdConfig = new Config(new File(knickers.getDataFolder(), FormIDConfigName), Config.YAML);
		(new Belle(this)).start();
		config = new Config(new File(knickers.getDataFolder(), ConfigName), Config.YAML);
		formID = new FormID(this);
		formID.setConfig(formIdConfig.getAll());
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (true) {
					try {
						sleep(Tool.ObjectToInt(kick.config.get("检测更新间隔"), 500) * 1000);
						if (config.getBoolean("检测更新"))
							(new Update(knickers)).start();
					} catch (InterruptedException e) {
						mis.getLogger().warning("自动检查更新遇到错误！" + e.getMessage());
					}
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (true) {
					try {
						Object object = config.get("定时检查快捷工具间隔");
						String s = object == null ? "" : String.valueOf(object);
						int time = Tool.ObjectToInt(s, 60);
						if (time > 0) {
							Map<UUID, Player> Players = Server.getInstance().getOnlinePlayers();
							for (UUID u : Players.keySet()) {
								Player player = Players.get(u);
								if (player.isOnline())
									Belle.exMaterials(player);
							}
						}
						sleep((time < 1 ? 60 : time) * 1000);
					} catch (InterruptedException e) {
						mis.getLogger().warning("自动检查更玩家快捷工具遇到错误！" + e.getMessage());
					}
				}
			}
		}.start();
		Message = new Message(this);
	}

	public static boolean isAdmin(CommandSender player) {
		if (!player.isPlayer())
			return true;
		return isAdmin((Player) player);
	}

	public static boolean isAdmin(Player player) {
		if (!player.isPlayer())
			return true;
		if (Kick.kick.config.getBoolean("仅允许白名单管理菜单"))
			return Kick.kick.config.getList("白名单").contains(player.getName());
		return player.isOp();
	}
}
