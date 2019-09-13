package xiaokai.knickers.mtp;

import xiaokai.knickers.Knickers;
import xiaokai.knickers.toCommand;
import xiaokai.knickers.appliance.Appliance;
import xiaokai.knickers.tool.ItemIDSunName;
import xiaokai.knickers.tool.Tool;
import xiaokai.knickers.tool.Update;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */ 
@SuppressWarnings("unchecked")
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
	 * 消息文件存储文件名
	 */
	public String MsgName = "Message.yml";
	/**
	 * 自定义工具的配置文件
	 */
	public String ApplianceName = "Appliance.yml";
	/**
	 * 表单ID存储类
	 */
	public FormID formID;
	/**
	 * 消息文件类
	 */
	public Message Message;
	/**
	 * 要初始化的表单ID键值
	 */
	public String[] FormIDName = { /* 0 */ "主页", /* 1 */ "添加按钮创建界面", /* 2 */"添加按钮输入数据界面", /* 3 */ "删除按钮时显示按钮列表的界面",
			/* 4 */"删除按钮时提示时候删除的界面", /* 5 */"显示在线玩家列表以供玩家传送的界面", /* 6 */ "在Tpa传送的时候，目标玩家点击确定取消的界面",
			/* 7 */ "{Msg}型命令执行界面", /* 8 */ "子页4", /* 9 */"设置页面", /* 10 */ "子页0", /* 11 */ "子页1", /* 12 */ "子页3",
			/* 13 */"子页2", /* 14 */"添加自定义快捷工具的界面的ID", /* 15 */"添加打开界面型的工具页面", /* 16 */"添加一个模拟点击按钮时，显示界面文件列表的界面ID",
			/* 17 */"开始添加点击后模拟点击按钮的工具的界面", /* 18 */"自定义工具主页", /* 19 */"自定义工具列表页ID", /* 20 */"自定义工具删除页面-列表",
			/* 21 */"自定义工具列表打开的界面ID", /* 22 */"自定义工具确认删除界面", /* 23 */"修改按钮的主页", /* 24 */"修改界面", /* 25 */"更多设置页面" };
	/**
	 * 表单ID存储位置
	 */
	public String FormIDConfigName = "FormID.yml";
	/**
	 * 玩家数据库
	 */
	public LinkedHashMap<String, MyPlayer> PlayerDataMap = new LinkedHashMap<>();
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
	public static final String[] ButtonTypeListEng = { "tip", "open", "command", "tp", "tpa" };
	/**
	 * 快捷工具的类型
	 */
	public static final String[] FastToolType = { "打开一个界面", "模拟点击按钮" };
	/**
	 * 添加的菜单的配置文件存储位置
	 */
	public static final String MenuConfigPath = "Menus/";
	/**
	 * 在启动服务器时检查文件夹是否创建，要检查的列表
	 */
	public static final String[] LoadDirList = { MenuConfigPath };
	/**
	 * 自定义可以工具打开界面的处理类</br>
	 * ....</br>
	 * ？？？？？</br>
	 * <b>What</b>？？？</br>
	 * 我咋感觉我有点不会说话了，这特么说的什么鬼！
	 */
	public Appliance App;
	/**
	 * 处理命令的地方
	 */
	public toCommand command;
	/**
	 * 异步线程
	 */
	public startThread sThread;
	/**
	 * 按钮过滤模式
	 */
	public static final String[] FilteredModel = { "无", "黑名单", "白名单" };

	/**
	 * 插件数据集合
	 * 
	 * @param knickers
	 */
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
		Message = new Message(this);
		App = new Appliance(this);
		command = new toCommand(this);
		sThread = new startThread();
		sThread.start();
	}

	/**
	 * 上面太多了看着难受，启动几个线程 </br>
	 * 检测更新间隔 </br>
	 * 定时检查快捷工具间隔
	 * 
	 * @author Winfxk
	 */
	public class startThread extends Thread {
		public int Update;
		public int time;
		public int UpdateAutoTool;

		/**
		 * 上面太多了看着难受，启动几个线程 </br>
		 * 检测更新间隔 </br>
		 * 定时检查快捷工具间隔
		 */
		public startThread() {
			Update = Tool.ObjectToInt(config.get("检测更新间隔"), 500);
			time = Tool.ObjectToInt(config.get("定时检查快捷工具间隔"), 60);
			UpdateAutoTool = Tool.ObjectToInt(config.get("自定义工具异步检查持有间隔"), 0);
		}

		@Override
		public void run() {
			while (true) {
				try {
					sleep(1000);
					if (config.getBoolean("检测更新") && Update-- < 0) {
						(new Update(mis)).start();
						Update = Tool.ObjectToInt(config.get("检测更新间隔"), 500);
					}
					if (time-- < 0 && config.getBoolean("定时检查快捷工具")) {
						Map<UUID, Player> Players = Server.getInstance().getOnlinePlayers();
						for (UUID u : Players.keySet()) {
							Player player = Players.get(u);
							if (player.isOnline())
								Belle.exMaterials(player);
						}
						time = Tool.ObjectToInt(config.get("定时检查快捷工具间隔"), 60);
					}
					if (config.getBoolean("自定义工具异步检查持有") && UpdateAutoTool-- < 0) {
						Map<UUID, Player> Players = Server.getInstance().getOnlinePlayers();
						for (UUID u : Players.keySet()) {
							Player player = Players.get(u);
							if (player.isOnline() && !player.getInventory().isFull()) {
								Map<String, Object> map = Appliance.config.getAll();
								for (String ike : map.keySet()) {
									Map<String, Object> item = (Map<String, Object>) map.get(ike);
									if (Tool.ObjToBool(item.get("isThread"), true)
											&& !Appliance.isAlready(player, ike)) {
										Object obj = item.get("ID");
										if (obj != null) {
											String ID = String.valueOf(obj);
											if (!ID.isEmpty()) {
												int[] IDs = Tool.IDtoFullID(ID);
												if (IDs[0] != 0) {
													Item Item = new Item(IDs[0], IDs[1]);
													Item = Appliance.setData(Item, item);
													player.getInventory().addItem(Item);
													player.sendMessage(kick.Message.getSun("界面", "快捷工具列表页",
															"强制获得一个快捷工具",
															new String[] { "{Player}", "{ItemName}", "{ItemID}" },
															new Object[] { player.getName(),
																	ItemIDSunName.getIDByName(Item.getId(),
																			Item.getDamage()),
																	Item.getId() + ":" + Item.getDamage() }));
												}
											}
										}
									}
								}
							}
						}
						UpdateAutoTool = Tool.ObjectToInt(config.get("自定义工具异步检查持有间隔"), 0);
					}
				} catch (InterruptedException e) {
					mis.getLogger().error("进程更新遇到错误！" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 判断一个逗比是不是管理员
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAdmin(CommandSender player) {
		if (!player.isPlayer())
			return true;
		return isAdmin((Player) player);
	}

	/**
	 * 判断一个逗比是不是管理员
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAdmin(Player player) {
		if (!player.isPlayer())
			return true;
		if (Kick.kick.config.getBoolean("仅允许白名单管理菜单"))
			return Kick.kick.config.getList("白名单").contains(player.getName());
		return player.isOp();
	}
}
