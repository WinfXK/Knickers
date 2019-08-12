package xiaokai.knickers.form.man;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.CustomForm;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * 修改按钮操作类
 * 
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class AlterButton {
	/**
	 * 开始处理玩家点击的是哪一个按钮
	 * 
	 * @param player 点击的玩家对象
	 * @param data   点击返回的数据
	 * @return
	 */
	public static boolean Dis(Player player, FormResponseSimple data) {
		int ID = data.getClickedButtonId();
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		Config config = new Config(myPlayer.CacheFile, Config.YAML);
		String Key = myPlayer.keyList.get(ID);
		Map<String, Object> map = (Map<String, Object>) ((Map<String, Object>) config.get("Buttons")).get(Key);
		int in = 0;
		for (String s : Kick.ButtonTypeListEng) {
			if (String.valueOf(map.get("Type")).toLowerCase().equals(s))
				break;
			in++;
		}
		myPlayer.Key = Key;
		Kick.kick.PlayerDataMap.put(player.getName(), myPlayer);
		return new Dispose(Kick.kick, player, map).start(in);
	}

	/**
	 * 修改添加按钮时创建界面给管理员写入按钮数据的类
	 * 
	 * @author Winfxk
	 */
	public static class Dispose {
		private Kick kick;
		private Player player;
		private MyPlayer my;
		private Config config;
		private Map<String, Object> map;

		/**
		 * 修改添加按钮时创建界面给管理员写入按钮数据的类
		 * 
		 * @param k Kick对象
		 * @param p 玩家对象
		 */
		public Dispose(Kick k, Player p, Map<String, Object> map) {
			this.kick = k;
			this.player = p;
			this.map = map;
			my = kick.PlayerDataMap.get(player.getName());
			config = new Config(my.CacheFile, Config.YAML);
		}

		/**
		 * 开始引导数据
		 * 
		 * @param data
		 * @return
		 */
		public boolean start(int ID) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			my.AddButtonType = Kick.ButtonTypeList[ID];
			my.isAlter = true;
			kick.PlayerDataMap.put(player.getName(), my);
			switch (Kick.ButtonTypeList[ID]) {
			case "传送到在线玩家":
				return Teleport();
			case "定点传送":
				return Transfer();
			case "执行命令":
				return Command();
			case "打开一个新的界面":
				return Open();
			case "提示一个窗口":
				return Tip();
			default:
				return false;
			}
		}

		/**
		 * 添加的按钮是点击后传送玩家到另一个在线玩家的按钮时要创建的界面
		 * 
		 * @return
		 */
		public boolean Teleport() {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			CustomForm form = new CustomForm(kick.formID.getID(2), kick.Message.getText(config.getString("Title"),
					new String[] { "{Player}" }, new Object[] { player.getName() }) + Tool.getColorFont("-Teleport"));
			form.addInput("请输入按钮显示的文字", map.get("Text"), "§e例如：§a点击变丑");
			form.addInput("请输入打开的界面的标题", map.get("Title"));
			form.addInput("请输入打开的界面的文字内容", map.get("Content"));
			form.addStepSlider("是否需要目标玩家确定才能传送", new String[] { "不需要", "需要" },
					Tool.ObjToBool(map.get("isAffirm")) ? 1 : 0);
			form.addInput("请输入执行后将会执行的命令(若不想启用该功能请留空本项)", map.get("Command"));
			form.addInput(getMoneyString(), map.get("Money"));
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" },
					Tool.ObjectToInt(map.get("IconType")));
			form.addInput("请输入图标的路径", map.get("IconPath"));
			form.addStepSlider("过滤模式", Kick.FilteredModel, Tool.ObjectToInt("FilteredModel"));
			form.addInput("黑名单列表，多个使用;分割", ListToString(map.get("FilteredPlayer")));
			form.sendPlayer(player);
			return true;
		}

		/**
		 * 添加的按钮是点击后传送玩家到一个特定的坐标的按钮时要创建的界面
		 * 
		 * @return
		 */
		public boolean Transfer() {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			CustomForm form = new CustomForm(kick.formID.getID(2), kick.Message.getText(config.getString("Title"),
					new String[] { "{Player}" }, new Object[] { player.getName() }) + Tool.getColorFont("-Transfer"));
			List<String> list = new ArrayList<String>();
			int i = 0;
			boolean isSB = false;
			Map<Integer, Level> as = Server.getInstance().getLevels();
			for (Integer is : as.keySet()) {
				list.add(as.get(is).getFolderName());
				if (as.get(is).getFolderName().equals(map.get("World")))
					isSB = true;
				if (isSB)
					continue;
				i++;
			}
			form.addInput("请输入按钮显示的文字", map.get("Text"), "§e例如：§a点击变丑");
			form.addDropdown("请输入您想要传送到的世界", list, i);
			form.addInput("请输入X坐标", map.get("X"));
			form.addInput("请输入Y坐标", map.get("Y"));
			form.addInput("请输入Z坐标", map.get("Z"));
			form.addInput("请输入执行后将会执行的命令(若不想启用该功能请留空本项)", map.get("Command"));
			form.addInput(getMoneyString(), map.get("Money"));
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" },
					Tool.ObjectToInt(map.get("IconType")));
			form.addInput("请输入图标的路径", map.get("IconPath"));
			form.addStepSlider("过滤模式", Kick.FilteredModel, Tool.ObjectToInt("FilteredModel"));
			form.addInput("黑名单列表，多个使用;分割", ListToString(map.get("FilteredPlayer")));
			form.sendPlayer(player);
			return true;
		}

		/**
		 * 添加的按钮是点击后执行命令的按钮时要创建的界面
		 * 
		 * @return
		 */
		public boolean Command() {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			CustomForm form = new CustomForm(kick.formID.getID(2), kick.Message.getText(config.getString("Title"),
					new String[] { "{Player}" }, new Object[] { player.getName() }) + Tool.getColorFont("-Command"));
			form.addInput("请输入按钮显示的文字", map.get("Text"), "§e例如：§a点击变丑");
			form.addInput("请输入要执行的命令\n若需玩家输入参数，请使用{msg}变量\n\n其他变量支持：\n玩家名：{Player}\n玩家的余额（未安装对应插件时为0)：{Money}\n全局变量",
					map.get("Command"));
			form.addInput("请输入变量的标签（未使用{msg}变量请忽略本项)", map.get("Msg"));
			form.addInput("请输入变量的Hint（未使用{msg}变量请忽略本项)", map.get("Hint"));
			String s = (String) map.get("Commander");
			form.addDropdown("执行命令的权限", new String[] { "玩家", "玩家管理员", "控制台" },
					s.toLowerCase().equals("playerbyop") ? 1 : s.toLowerCase().equals("player") ? 0 : 2);
			form.addInput(getMoneyString(), map.get("Money"));
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" },
					Tool.ObjectToInt(map.get("IconType")));
			form.addInput("请输入图标的路径", map.get("IconPath"));
			form.addStepSlider("过滤模式", Kick.FilteredModel, Tool.ObjectToInt("FilteredModel"));
			form.addInput("黑名单列表，多个使用;分割", ListToString(map.get("FilteredPlayer")));
			form.sendPlayer(player);
			return true;
		}

		/**
		 * 添加的按钮是点击后弹出新窗口的按钮时要创建的界面
		 * 
		 * @return
		 */
		public boolean Open() {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			CustomForm form = new CustomForm(kick.formID.getID(2), kick.Message.getText(config.getString("Title"),
					new String[] { "{Player}" }, new Object[] { player.getName() }) + Tool.getColorFont("-Open"));
			form.addInput("请输入按钮显示的文字", map.get("Text"), "§e例如：§a点击变丑");
			form.addInput("请输入打开的界面的标题", map.get("Title"));
			form.addInput("请输入弹窗的内容", map.get("Content"));
			String[] Files = new File(kick.mis.getDataFolder(), Kick.MenuConfigPath).list();
			form.addDropdown("想要打开的菜单的配置文件名", Files.length < 1 ? new String[] { "啥玩意都没有" } : Files);
			form.addInput("请输入想要打开的菜单的配置文件名", map.get("Config"), "当该项不为空时上方设置将会失效");
			form.addInput("请输入执行后将会执行的命令(若不想启用该功能请留空本项)", map.get("Command"));
			form.addInput(getMoneyString(), map.get("Money"));
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" },
					Tool.ObjectToInt(map.get("IconType")));
			form.addInput("请输入图标的路径", map.get("IconPath"));
			form.addStepSlider("过滤模式", Kick.FilteredModel, Tool.ObjectToInt("FilteredModel"));
			form.addInput("黑名单列表，多个使用;分割", ListToString(map.get("FilteredPlayer")));
			form.sendPlayer(player);
			return true;
		}

		/**
		 * 添加的按钮是点击后弹出提示的按钮时要创建的界面，创建界面给玩家输入按钮信息，下同
		 * 
		 * @return
		 */
		public boolean Tip() {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			CustomForm form = new CustomForm(kick.formID.getID(2), kick.Message.getText(config.getString("Title"),
					new String[] { "{Player}" }, new Object[] { player.getName() }) + Tool.getColorFont("-Tip"));
			form.addInput("请输入按钮显示的文字", map.get("Text"), "§e例如：§a点击变丑");
			form.addInput("请输入打开的界面的标题", map.get("Title"), "§e例如： §a恭喜");
			form.addInput("请输入弹窗的内容", map.get("Content"), "§e例如：§a恭喜您！您已经成功变成丑比！{n}{n}{n}");
			form.addInput("请输入执行后将会执行的命令(若不想启用该功能请留空本项)", map.get("Command"),
					"§e例如： §ame 大家好，经过§e§l§n不§6§l§n懈§a§l§n努§4§l§n力，我终于变成了§e§l§n丑§9§l§n逼§r！！！");
			form.addDropdown("请选择弹窗的类型", new String[] { "Simple", "Modal" },
					String.valueOf(map.get("TipType")).toLowerCase().equals("simple") ? 0 : 1);
			form.addInput(getMoneyString(), map.get("Money"));
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" },
					Tool.ObjectToInt(map.get("IconType")));
			form.addInput("请输入图标的路径", map.get("IconPath"));
			form.addStepSlider("过滤模式", Kick.FilteredModel, Tool.ObjectToInt("FilteredModel"));
			form.addInput("黑名单列表，多个使用;分割", ListToString(map.get("FilteredPlayer")));
			form.sendPlayer(player);
			return true;
		}

		public static String getMoneyString() {
			Plugin plugin = Kick.kick.mis.getServer().getPluginManager().getPlugin("EconomyAPI");
			return "请输入执行后将会扣除的费用\n若不想启用该功能请留空或将本项设置为小于等于0的数"
					+ ((plugin == null || !plugin.isEnabled()) ? "\n§4注意！检测到§6EconomyAPI§4插件可能未启用或未安装，请检查，否则将忽略扣费"
							: "");
		}

		public static String ListToString(Object obj) {
			List<String> l = obj == null || !(obj instanceof List) ? new ArrayList<String>() : (ArrayList<String>) obj;
			String string = "";
			for (int i = 0; i < l.size(); i++)
				string += l.get(i) + ((i + 1 < l.size()) ? ";" : "");
			return string;
		}
	}

	/**
	 * 显示可以修改的按钮列表
	 * 
	 * @param player
	 * @param file
	 * @return
	 */
	public static boolean make(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
		Kick kick = Kick.kick;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheFile = file;
		List<String> Items = new ArrayList<String>();
		Config config = new Config(file, Config.YAML);
		Map<String, Object> Buttons = (config.get("Buttons") != null && (config.get("Buttons") instanceof Map))
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<String, Object>();
		if (Buttons.size() < 1)
			return MakeForm.Tip(player, "§4这个界面没有发现按钮的存在！");
		SimpleForm form = new SimpleForm(kick.formID.getID(23), Kick.kick.Message.getText(config.getString("Title")),
				"§4请点击您想要修改的按钮");
		for (String ike : Buttons.keySet()) {
			Map<String, Object> Item = ((Buttons.get(ike) instanceof Map) && Buttons.get(ike) != null)
					? (HashMap<String, Object>) Buttons.get(ike)
					: new HashMap<String, Object>();
			if (Item.size() > 0) {
				String Path = (String) Item.get("IconPath");
				form.addButton(String.valueOf(Item.get("Text")), !String.valueOf(Item.get("IconPath")).equals("2"),
						Path == null || Path.isEmpty() ? null : Path);
				Items.add(ike);
			}
		}
		myPlayer.keyList = Items;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}
}
