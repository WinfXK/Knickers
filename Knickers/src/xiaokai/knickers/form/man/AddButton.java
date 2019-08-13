package xiaokai.knickers.form.man;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.CustomForm;
import xiaokai.tool.ItemIDSunName;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class AddButton {
	/**
	 * 添加按钮的时候提示要创建那种按钮的界面
	 * 
	 * @param player
	 * @param file   要添加按钮的配置文件对象
	 * @return
	 */
	public static boolean addButton(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player, Kick.kick.Message.getMessage("权限不足"));
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheFile = file;
		myPlayer.isAlter = false;
		Config config = new Config(file, Config.YAML);
		SimpleForm form = new SimpleForm(Kick.kick.formID.getID(1), Kick.kick.Message.getText(config.getString("Title"),
				new String[] { "{Player}" }, new Object[] { player.getName() }) + Tool.getRandColor() + " 添加按钮");
		for (String s : Kick.ButtonTypeList)
			form.addButton(Tool.getRandColor() + s);
		form.sendPlayer(player);
		Kick.kick.PlayerDataMap.put(player.getName(), myPlayer);
		return true;
	}

	/**
	 * 添加各种按钮
	 * 
	 * @author Winfxk
	 *
	 */
	public static class Add {
		private Player player;
		private Config config;
		private String ButtonText;
		private Map<String, Object> map;
		private Map<String, Object> Buttons;
		private boolean isAlter;

		/**
		 * 给菜单添加按钮
		 * 
		 * @param Key            按钮的Key
		 * @param player         添加按钮的玩家对象
		 * @param file           要添加按钮的配置文件对象
		 * @param ButtonText     按钮的文本内容
		 * @param Command        点击按钮后会执行的命令内容
		 * @param Money          点击后会扣除的金币数量
		 * @param IconType       按钮的图标类型
		 * @param IconPath       按钮的贴图路径
		 * @param FilteredModel  过滤玩家的模式
		 * @param FilteredPlayer 过滤的玩家列表
		 * @param isAlter        是修改还是创建
		 */
		public Add(String Key, Player player, File file, String ButtonText, String Command, double Money, int IconType,
				String IconPath, int FilteredModel, List<String> FilteredPlayer, boolean isAlter) {
			this.player = player;
			this.config = new Config(file, Config.YAML);
			Command = Command == null ? "" : Command;
			IconType = IconType >= 0 || IconType <= 2 ? IconType : 0;
			IconPath = (IconType != 1 && IconType != 2) ? null
					: ((IconPath == null || IconPath.isEmpty() || IconPath.equals("null")) ? null : IconPath);
			Money = Money < 0 ? 0 : Money;
			map = new HashMap<String, Object>();
			Buttons = (config.get("Buttons") == null || !(config.get("Buttons") instanceof Map))
					? new HashMap<String, Object>()
					: (HashMap<String, Object>) config.get("Buttons");
			map.put("Text", ButtonText.contains("\n") ? ButtonText.replace("\n", "{n}") : ButtonText);
			map.put("Command", Command.contains("\n") ? Command.replace("\n", "{n}") : Command);
			map.put("Money", Money);
			map.put("IconType", IconType);
			map.put("IconPath", IconPath != null ? ItemIDSunName.UnknownToPath(IconPath) : IconPath);
			map.put("FilteredModel", FilteredModel);
			map.put("FilteredPlayer", FilteredPlayer);
			map.put("Key", Key == null || Key.isEmpty() ? getKey(1) : Key);
			this.isAlter = isAlter;
			if (isAlter) {
				map.put("AlterTime", Tool.getDate() + " " + Tool.getTime());
				map.put("AlterPlayer", player.getName());
			} else {
				map.put("creaTime", Tool.getDate() + " " + Tool.getTime());
				map.put("Player", player.getName());
			}
		}

		/**
		 * 给菜单添加按钮
		 * 
		 * @param player         添加按钮的玩家对象
		 * @param file           要添加按钮的配置文件对象
		 * @param ButtonText     按钮的文本内容
		 * @param Command        点击按钮后会执行的命令内容
		 * @param Money          点击后会扣除的金币数量
		 * @param IconType       按钮的图标类型
		 * @param IconPath       按钮的贴图路径
		 * @param FilteredModel  过滤玩家的模式
		 * @param FilteredPlayer 过滤的玩家列表
		 */
		public Add(Player player, File file, String ButtonText, String Command, double Money, int IconType,
				String IconPath, int FilteredModel, List<String> FilteredPlayer, boolean isAlter) {
			this.player = player;
			this.config = new Config(file, Config.YAML);
			Command = Command == null ? "" : Command;
			IconType = IconType >= 0 || IconType <= 2 ? IconType : 0;
			IconPath = (IconType != 1 && IconType != 2) ? null
					: ((IconPath == null || IconPath.isEmpty()) ? null : IconPath);
			Money = Money < 0 ? 0 : Money;
			map = new HashMap<String, Object>();
			Buttons = (config.get("Buttons") == null || !(config.get("Buttons") instanceof Map))
					? new HashMap<String, Object>()
					: (HashMap<String, Object>) config.get("Buttons");
			map.put("Text", ButtonText.contains("\n") ? ButtonText.replace("\n", "{n}") : ButtonText);
			map.put("Command", Command.contains("\n") ? Command.replace("\n", "{n}") : Command);
			map.put("Money", Money);
			map.put("IconType", IconType);
			map.put("IconPath", IconPath != null ? ItemIDSunName.UnknownToPath(IconPath) : IconPath);
			if (isAlter) {
				map.put("AlterTime", Tool.getDate() + " " + Tool.getTime());
				map.put("AlterPlayer", player.getName());
				map.put("Key", Kick.kick.PlayerDataMap.get(player.getName()).Key);
			} else {
				map.put("creaTime", Tool.getDate() + " " + Tool.getTime());
				map.put("Player", player.getName());
				map.put("Key", getKey(1));
			}
			map.put("FilteredModel", FilteredModel);
			map.put("FilteredPlayer", FilteredPlayer);
			this.isAlter = isAlter;
		}

		/**
		 * 获取随机Key
		 * 
		 * @param JJLength
		 * @return
		 */
		public String getKey(int JJLength) {
			String string = "";
			for (int i = 0; i < JJLength; i++)
				string += Tool.getRandString();
			if (Buttons.containsKey(string))
				return getKey(JJLength++);
			return string;
		}

		/**
		 * 保存配置文件
		 * 
		 * @return
		 * 
		 * @return
		 */
		public boolean save() {
			Buttons.put(map.get("Key").toString(), map);
			config.set("Buttons", Buttons);
			boolean isok;
			if (isok = config.save())
				if (isAlter)
					player.sendMessage("§6成功修改了这个按钮！");
				else
					player.sendMessage("§6成功创建这个页面的第§4" + Buttons.size() + "§6个按钮！");
			else
				player.sendMessage("§4创建可能出现了一些问题！如果这个问题经常存在，请联系作者！");
			return isok;
		}

		/**
		 * 添加一个类型为点击后弹出弹窗的按钮
		 * 
		 * @param Title   弹窗的标题
		 * @param Content 弹窗的文本内容
		 * @param TipType 弹窗的类型
		 * @return
		 */
		public boolean addTip(String Title, String Content, String TipType) {
			Title = Title == null ? ButtonText : Title;
			Content = Content == null ? ButtonText : Content;
			map.put("Title", Title.contains("\n") ? Title.replace("\n", "{n}") : Title);
			map.put("Content", Content.contains("\n") ? Content.replace("\n", "{n}") : Content);
			map.put("TipType", TipType);
			map.put("Type", "Tip");
			return save();
		}

		/**
		 * 添加一个点击后弹出服务器在线玩家列表，供玩家传送的按钮
		 * 
		 * @param Title    界面的标题
		 * @param Content  界面的文本内容
		 * @param isAffirm 是否需要目标玩家确定才传送
		 * @return
		 */
		public boolean addTeleport(String Title, String Content, boolean isAffirm) {
			Title = Title == null ? ButtonText : Title;
			Content = Content == null ? ButtonText : Content;
			map.put("Title", Title.contains("\n") ? Title.replace("\n", "{n}") : Title);
			map.put("Content", Content.contains("\n") ? Content.replace("\n", "{n}") : Content);
			map.put("Type", "Tpa");
			map.put("isAffirm", isAffirm);
			return save();
		}

		/**
		 * 添加一个点击后传送到指定位置的按钮
		 * 
		 * @param World 要传送到的世界
		 * @param x     要传送到的X坐标
		 * @param y     要传送到的Y坐标
		 * @param z     要传送到的Z坐标
		 * @return
		 */
		public boolean addTransfer(String World, double x, double y, double z) {
			map.put("World", World);
			map.put("X", Tool.Double2(x));
			map.put("Y", Tool.Double2(y));
			map.put("Z", Tool.Double2(z));
			map.put("Type", "TP");
			return save();
		}

		/**
		 * 添加一个点击后执行命令的按钮
		 * 
		 * @param Msg        若按钮存在自定义参数，这个为自定义参数输入框的标题，多个使用;分割
		 * @param Hint       若按钮存在自定义参数，这个为自定义参数输入框的提示，多个使用;分割
		 * @param playerType 执行命令的对象 [<b>Console</b>|<b>PlayerByOp</b>|<b>Player</b>]
		 * @return
		 */
		public boolean addCommand(String Msg, String Hint, String playerType) {
			String[] msgLists = Msg != null && Msg.contains(";") && !Msg.isEmpty() ? Msg.split(";")
					: new String[] { Msg };
			String[] hintMsgs = Hint != null && !Hint.isEmpty() && Hint.contains(";") ? Hint.split(";")
					: new String[] { Hint };
			playerType = (playerType.toLowerCase().equals("console") ? "Console"
					: (playerType.toLowerCase().equals("playerbyop") ? "PlayerByOp" : "Player"));
			List<String> msgList = new ArrayList<String>();
			List<String> hintMsg = new ArrayList<String>();
			for (String Mag : msgLists)
				if (Mag != null && !Mag.isEmpty())
					msgList.add(Mag.contains("\n") ? Mag.replace("\n", "{n}") : Mag);
			for (String Mag : hintMsgs)
				if (Mag != null && !Mag.isEmpty())
					hintMsg.add(Mag.contains("\n") ? Mag.replace("\n", "{n}") : Mag);
			map.put("Msg", msgList);
			map.put("Hint", hintMsg);
			map.put("Commander", playerType);
			map.put("Type", "Command");
			return save();
		}

		/**
		 * 创建一个点击后会打开一个界面的按钮
		 * 
		 * @param Title      打开的界面的标题
		 * @param Content    打开的界面的界面文本内容
		 * @param ConfigName 打开的界面的配置文件
		 * @return
		 */
		public boolean addOpen(String Title, String Content, String ConfigName) {
			ConfigName = (ConfigName.lastIndexOf("yml") == ConfigName.length() - 3) ? ConfigName : ConfigName + ".yml";
			File file = new File(new File(Kick.kick.mis.getDataFolder(), Kick.MenuConfigPath), ConfigName);
			if (!file.exists()) {
				Config config = new Config(file, Config.YAML);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				Title = Title == null ? ButtonText : Title;
				Content = Content == null ? ButtonText : Content;
				map.put("Title", Title.contains("\n") ? Title.replace("\n", "{n}") : Title);
				map.put("Content", Content.contains("\n") ? Content.replace("\n", "{n}") : Content);
				map.put("creaTime", Tool.getDate() + " " + Tool.getTime());
				map.put("Player", player.getName());
				map.put("Buttons", new HashMap<String, Object>());
				map.put("FilteredModel", this.map.get("FilteredModel"));
				map.put("FilteredPlayer", this.map.get("FilteredPlayer"));
				config.setAll(map);
				config.save();
			}
			this.map.put("Config", ConfigName);
			this.map.put("Type", "Open");
			return save();
		}
	}

	/**
	 * 处理玩家添加按钮的界面传回的数据
	 * 
	 * @author Winfxk
	 */
	public static class Start {
		private Kick kick;
		private Player player;
		private MyPlayer my;
		private FormResponseCustom data;
		private String ButtonText;

		/**
		 * 监听创建按钮填写按钮数据的类
		 * 
		 * @param k Kick对象
		 * @param p 添加按钮的玩家对象
		 */
		public Start(Kick k, Player p) {
			this.kick = k;
			this.player = p;
			my = kick.PlayerDataMap.get(player.getName());
		}

		/**
		 * 开始引导数据
		 * 
		 * @param data 传回的数据
		 * @return
		 */
		public boolean Switch(FormResponseCustom data) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			ButtonText = data.getInputResponse(0);
			if (ButtonText == null || ButtonText.isEmpty())
				return MakeForm.Tip(player, "请输入按钮文本内容");
			this.data = data;
			switch (my.AddButtonType) {
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
		 * 创建一个点击后会打开一个界面的按钮
		 * 
		 * @return
		 */
		private boolean Open() {
			String Title = data.getInputResponse(1);
			String Content = data.getInputResponse(2);
			String SB = data.getInputResponse(4);
			Title = (Title == null || Title.isEmpty()) ? ButtonText : Title;
			String ConfigName = (SB != null && !SB.isEmpty()) ? SB : data.getDropdownResponse(3).getElementContent();
			if (ConfigName == null || ConfigName.isEmpty())
				return MakeForm.Tip(player, "§4请输入要打开的界面的名字！\n\n不知道该怎么输入的话请瞎几把输入");
			String s = data.getInputResponse(6);
			s = (s == null || s.isEmpty()) ? "0" : s;
			if (!Tool.isInteger(s))
				return MakeForm.Tip(player, "§4扣除的费用数必须为大于零的纯数字！");
			double Money = Double.valueOf(s);
			return (new AddButton.Add(player, my.CacheFile, ButtonText, data.getInputResponse(5), Money,
					data.getStepSliderResponse(7).getElementID(), data.getInputResponse(8),
					data.getStepSliderResponse(9).getElementID(), getPlayers(data.getInputResponse(10)),
					Kick.kick.PlayerDataMap.get(player.getName()).isAlter)).addOpen(Title, Content, ConfigName);
		}

		/**
		 * 添加一个点击后执行命令的按钮
		 * 
		 * @return
		 */
		private boolean Command() {
			String Cmd = data.getInputResponse(1);
			int id = data.getDropdownResponse(4).getElementID();
			String PlayerType = (id == 0 ? "Player" : (id == 1 ? "PlayerByOp" : "Console"));
			if (Cmd == null || Cmd.isEmpty())
				return MakeForm.Tip(player, "§4请输入想要执行的命令");
			String s = data.getInputResponse(5);
			s = (s == null || s.isEmpty()) ? "0" : s;
			if (!Tool.isInteger(s))
				return MakeForm.Tip(player, "§4扣除的费用数必须为大于零的纯数字！");
			double Money = Double.valueOf(s);
			return (new AddButton.Add(player, my.CacheFile, ButtonText, Cmd, Money,
					data.getStepSliderResponse(6).getElementID(), data.getInputResponse(7),
					data.getStepSliderResponse(8).getElementID(), getPlayers(data.getInputResponse(9)),
					Kick.kick.PlayerDataMap.get(player.getName()).isAlter)).addCommand(data.getInputResponse(2),
							data.getInputResponse(3), PlayerType);
		}

		/**
		 * 添加一个点击后传送到指定位置的按钮
		 * 
		 * @return
		 */
		private boolean Transfer() {
			String xString = data.getInputResponse(2);
			xString = (xString == null || xString.isEmpty()) ? String.valueOf(player.getX()) : xString;
			String yString = data.getInputResponse(3);
			yString = (yString == null || yString.isEmpty()) ? String.valueOf(player.getY()) : yString;
			String zString = data.getInputResponse(4);
			zString = (zString == null || zString.isEmpty()) ? String.valueOf(player.getZ()) : zString;
			String s = data.getInputResponse(6);
			s = (s == null || s.isEmpty()) ? "0" : s;
			if (!Tool.isInteger(s))
				return MakeForm.Tip(player, "§4扣除的费用数必须为大于零的纯数字！");
			double Money = Double.valueOf(s);
			double x, y, z;
			if (Tool.isInteger(xString) && Tool.isInteger(yString) && Tool.isInteger(zString)) {
				x = Double.valueOf(xString);
				y = Double.valueOf(yString);
				z = Double.valueOf(zString);
			} else
				return MakeForm.Tip(player, "§4X、Y、Z坐标必须为纯数字！");
			return (new AddButton.Add(player, my.CacheFile, ButtonText, data.getInputResponse(5), Money,
					data.getStepSliderResponse(7).getElementID(), data.getInputResponse(8),
					data.getStepSliderResponse(9).getElementID(), getPlayers(data.getInputResponse(10)),
					Kick.kick.PlayerDataMap.get(player.getName()).isAlter))
							.addTransfer(data.getDropdownResponse(1).getElementContent(), x, y, z);
		}

		/**
		 * 添加一个点击后弹出服务器在线玩家列表，供玩家传送的按钮
		 * 
		 * @return
		 */
		private boolean Teleport() {
			String s = data.getInputResponse(5);
			s = (s == null || s.isEmpty()) ? "0" : s;
			if (!Tool.isInteger(s))
				return MakeForm.Tip(player, "§4扣除的费用数必须为大于零的纯数字！");
			double Money = Double.valueOf(s);
			return (new AddButton.Add(player, my.CacheFile, ButtonText, data.getInputResponse(4), Money,
					data.getStepSliderResponse(6).getElementID(), data.getInputResponse(7),
					data.getStepSliderResponse(8).getElementID(), getPlayers(data.getInputResponse(9)),
					Kick.kick.PlayerDataMap.get(player.getName()).isAlter)).addTeleport(data.getInputResponse(1),
							data.getInputResponse(2), data.getStepSliderResponse(3).getElementID() == 1);
		}

		/**
		 * 监听玩家添加按钮传回的按钮数据，这个按钮时点击弹出弹窗的界面
		 * 
		 * @return
		 */
		private boolean Tip() {
			String s = data.getInputResponse(6);
			s = (s == null || s.isEmpty()) ? "0" : s;
			if (!Tool.isInteger(s))
				return MakeForm.Tip(player, "§4扣除的费用数必须为大于零的纯数字！");
			double Money = Double.valueOf(s);
			return (new AddButton.Add(player, my.CacheFile, ButtonText, data.getInputResponse(3), Money,
					data.getStepSliderResponse(6).getElementID(), data.getInputResponse(7),
					data.getStepSliderResponse(8).getElementID(), getPlayers(data.getInputResponse(9)),
					Kick.kick.PlayerDataMap.get(player.getName()).isAlter)).addTip(data.getInputResponse(1),
							data.getInputResponse(2), data.getDropdownResponse(4).getElementContent());
		}

		/**
		 * 获取被屏蔽的玩家列表
		 * 
		 * @param string
		 * @return
		 */
		public static List<String> getPlayers(String string) {
			List<String> list = new ArrayList<String>();
			if (string == null || string.isEmpty())
				return list;
			if (string.contains(";")) {
				String[] s = string.split(";");
				for (String player : s)
					if (player != null && !player.isEmpty())
						list.add(player);
			} else
				list.add(string);
			return list;
		}
	}

	/**
	 * 添加按钮时创建界面给管理员写入按钮数据的类
	 * 
	 * @author Winfxk
	 */
	public static class Dispose {
		private Kick kick;
		private Player player;
		private MyPlayer my;
		private Config config;

		public Dispose(Kick k, Player p) {
			this.kick = k;
			this.player = p;
			my = kick.PlayerDataMap.get(player.getName());
			config = new Config(my.CacheFile, Config.YAML);
		}

		/**
		 * 开始引导数据
		 * 
		 * @param data
		 * @return
		 */
		public boolean start(FormResponseSimple data) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			my.AddButtonType = Kick.ButtonTypeList[data.getClickedButtonId()];
			my.isAlter = false;
			kick.PlayerDataMap.put(player.getName(), my);
			switch (Kick.ButtonTypeList[data.getClickedButtonId()]) {
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
					new String[] { "{Player}" }, new Object[] { player.getName() }));
			form.addInput("请输入按钮显示的文字", "§6点击变丑", "§e例如：§a点击变丑");
			form.addInput("请输入打开的界面的标题");
			form.addInput("请输入打开的界面的文字内容");
			form.addStepSlider("是否需要目标玩家确定才能传送", new String[] { "不需要", "需要" });
			form.addInput("请输入执行后将会执行的命令(若不想启用该功能请留空本项)");
			form.addInput(getMoneyString(), "0", "0");
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" });
			form.addInput("请输入图标的路径", getHandItemID(player));
			form.addStepSlider("过滤模式", Kick.FilteredModel);
			form.addInput("黑/白 名单列表，多个使用;分割", "");
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
					new String[] { "{Player}" }, new Object[] { player.getName() }));
			List<String> list = new ArrayList<String>();
			int i = 0;
			boolean isSB = false;
			Map<Integer, Level> as = Server.getInstance().getLevels();
			for (Integer is : as.keySet()) {
				list.add(as.get(is).getFolderName());
				if (as.get(is).getFolderName().equals(player.getLevel().getFolderName()))
					isSB = true;
				if (isSB)
					continue;
				i++;
			}
			form.addInput("请输入按钮显示的文字", "§6点击变丑", "§e例如：§a点击变丑");
			form.addDropdown("请输入您想要传送到的世界", list, i);
			form.addInput("请输入X坐标", Tool.Double2(player.getX(), 1));
			form.addInput("请输入Y坐标", Tool.Double2(player.getY(), 1));
			form.addInput("请输入Z坐标", Tool.Double2(player.getZ(), 1));
			form.addInput("请输入执行后将会执行的命令(若不想启用该功能请留空本项)");
			form.addInput(getMoneyString(), "0", "0");
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" });
			form.addInput("请输入图标的路径", getHandItemID(player));
			form.addStepSlider("过滤模式", Kick.FilteredModel);
			form.addInput("黑/白 名单列表，多个使用;分割", "");
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
					new String[] { "{Player}" }, new Object[] { player.getName() }));
			form.addInput("请输入按钮显示的文字", "§6点击变丑", "§e例如：§a点击变丑");
			form.addInput("请输入要执行的命令\n若需玩家输入参数，请使用{msg}变量\n\n其他变量支持：\n玩家名：{Player}\n玩家的余额（未安装对应插件时为0)：{Money}\n全局变量");
			form.addInput("请输入变量的标签（未使用{msg}变量请忽略本项)");
			form.addInput("请输入变量的Hint（未使用{msg}变量请忽略本项)");
			form.addDropdown("执行命令的权限", new String[] { "玩家", "玩家管理员", "控制台" }, 0);
			form.addInput(getMoneyString(), "0", "0");
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" });
			form.addInput("请输入图标的路径", getHandItemID(player));
			form.addStepSlider("过滤模式", Kick.FilteredModel);
			form.addInput("黑/白 名单列表，多个使用;分割", "");
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
					new String[] { "{Player}" }, new Object[] { player.getName() }));
			form.addInput("请输入按钮显示的文字", "§6点击变丑", "§e例如：§a点击变丑");
			form.addInput("请输入打开的界面的标题");
			form.addInput("请输入弹窗的内容");
			String[] Files = new File(kick.mis.getDataFolder(), Kick.MenuConfigPath).list();
			form.addDropdown("想要打开的菜单的配置文件名", Files.length < 1 ? new String[] { "啥玩意都没有" } : Files);
			form.addInput("请输入想要打开的菜单的配置文件名", "", "当该项不为空时上方设置将会失效");
			form.addInput("请输入执行后将会执行的命令(若不想启用该功能请留空本项)");
			form.addInput(getMoneyString(), "0", "0");
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" });
			form.addInput("请输入图标的路径", getHandItemID(player));
			form.addStepSlider("过滤模式", Kick.FilteredModel);
			form.addInput("黑/白 名单列表，多个使用;分割", "");
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
					new String[] { "{Player}" }, new Object[] { player.getName() }));
			form.addInput("请输入按钮显示的文字", "§6点击变丑", "§e例如：§a点击变丑");
			form.addInput("请输入打开的界面的标题", "§a恭喜", "§e例如： §a恭喜");
			form.addInput("请输入弹窗的内容", "§a恭喜您！您已经成功变成丑比！{n}{n}{n}", "§e例如：§a恭喜您！您已经成功变成丑比！{n}{n}{n}");
			form.addInput("请输入执行后将会执行的命令(若不想启用该功能请留空本项)",
					"me 大家好，经过§e§l§n不§6§l§n懈§a§l§n努§4§l§n力，我终于变成了§e§l§n丑§9§l§n逼§r！！！",
					"§e例如： §ame 大家好，经过§e§l§n不§6§l§n懈§a§l§n努§4§l§n力，我终于变成了§e§l§n丑§9§l§n逼§r！！！");
			form.addDropdown("请选择弹窗的类型", new String[] { "Simple", "Modal" }, 0);
			form.addInput(getMoneyString(), "0");
			form.addStepSlider("按钮的图标类型", new String[] { "无图标", "本地资源", "网络资源" });
			form.addInput("请输入图标的路径", getHandItemID(player));
			form.addStepSlider("过滤模式", Kick.FilteredModel);
			form.addInput("黑/白 名单列表，多个使用;分割", "");
			form.sendPlayer(player);
			return true;
		}

		public static String getMoneyString() {
			Plugin plugin = Kick.kick.mis.getServer().getPluginManager().getPlugin("EconomyAPI");
			return "请输入执行后将会扣除的费用\n若不想启用该功能请留空或将本项设置为小于等于0的数"
					+ ((plugin == null || !plugin.isEnabled()) ? "\n§4注意！检测到§6EconomyAPI§4插件可能未启用或未安装，请检查，否则将忽略扣费"
							: "");
		}

		public static String getHandItemID(Player player) {
			Item item = player.getInventory().getItemInHand();
			return item == null ? "0:0" : (item.getId() != 0 ? (item.getId() + ":" + item.getDamage()) : "1:0");
		}
	}
}