package xiaokai.knickers.appliance;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.form.OpenButton;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.ItemIDSunName;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Handle {
	private static Kick kick = Kick.kick;
	private Player player;
	private Map<String, Object> Item;

	/**
	 * 处理快捷工具页面的数据
	 * 
	 * @author Winfxk
	 */
	public static class disTool {
		private Player player;

		/**
		 * 处理快捷工具页面的数据
		 * 
		 * @param player 玩家对象
		 */
		public disTool(Player player) {
			this.player = player;
		}

		/**
		 * 玩家点击的是获取快捷工具得到按钮
		 * 
		 * @return
		 */
		public boolean Price() {
			return true;
		}

		/**
		 * 玩家点击的是写入数据的按钮
		 * 
		 * @return
		 */
		public boolean Write() {
			return true;
		}
	}

	/**
	 * 处理玩家查看工具信息页面返回的数据
	 * 
	 * @param player 触发这个事件的玩家对象
	 * @param data   返回的数据对象
	 * @return
	 */
	public static boolean disToolListIsItem(Player player, FormResponseSimple data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		switch (myPlayer.keyList.get(data.getClickedButtonId())) {
		case "get":
			return (new disTool(player)).Price();
		case "put":
			return (new disTool(player)).Write();
		case "ok":
		default:
			return EstablishForm.showToolList(player);
		}
	}

	/**
	 * 处理玩家点击工具主页的数据
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean Switch(Player player, FormResponseSimple data) {
		switch (data.getClickedButtonId()) {
		case 0:
			return EstablishForm.showToolList(player);
		case 1:
			return EstablishForm.AddItemByStyle(player);
		case 2:
			return EstablishForm.delTool(player);
		default:
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "无法获取返回数据！" }));
		}
	}

	/**
	 * 开始处理使用快捷工具的玩家对象
	 * 
	 * @param kick   Kick对象
	 * @param player 使用快捷工具的玩家对象
	 * @param Item   使用的跨界工具的项目数据对象
	 * @param item   使用的快捷工具的物品对象
	 */
	public Handle(Player player, Map<String, Object> Item) {
		this.player = player;
		this.Item = Item;
	}

	/**
	 * 开始判断玩家使用的快捷工具的类型
	 * 
	 * @return
	 */
	public boolean start() {
		String Type = Item.get("Type") == null ? null : String.valueOf(Item.get("Type"));
		if (Type == null || Type.isEmpty())
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "无法解析工具类型！" }));
		switch (Type.toLowerCase()) {
		case "button":
			return isButton();
		case "open":
		default:
			return isOpen();
		}
	}

	/**
	 * 如果使用的工具是模拟点击按钮
	 * 
	 * @return
	 */
	public boolean isButton() {
		Object obj = Item.get("FileName");
		String FileName = obj == null || String.valueOf(obj).isEmpty() ? null : String.valueOf(obj);
		if (FileName == null)
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "无法获取想要打开的文件名！" }));
		String Key = Item.get("Key") != null ? String.valueOf(Item.get("Key")) : null;
		if (Key == null || Key.isEmpty())
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "无法获取按钮的Key" }));
		File file = new File(new File(kick.mis.getDataFolder(), Kick.MenuConfigPath), FileName);
		if (!file.exists())
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "要打开的界面文件不存在！" }));
		Config config = new Config(file, Config.YAML);
		Map<String, Object> Buttons = (config.get("Buttons") != null && (config.get("Buttons") instanceof Map))
				? (HashMap<String, Object>) config.get("Buttons")
				: new HashMap<String, Object>();
		if (Buttons.size() < 1)
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "打开的界面不存在任何按钮！" }));
		if (Buttons.containsKey(Key))
			return (new OpenButton(kick, player, file, Key)).start();
		else
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "要打开的按钮不存在！" }));
	}

	/**
	 * 如果使用的工具按钮是打开界面型的
	 * 
	 * @return
	 */
	public boolean isOpen() {
		Object obj = Item.get("FileName");
		String FileName = obj == null || String.valueOf(obj).isEmpty() ? null : String.valueOf(obj);
		if (FileName == null)
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "无法获取想要打开的文件名！" }));
		File file = new File(new File(kick.mis.getDataFolder(), Kick.MenuConfigPath), FileName);
		if (!file.exists())
			return MakeForm.Tip(player, kick.Message.getSon("快捷工具", "自定义工具打开失败提示",
					new String[] { "{Player}", "{Error}" }, new Object[] { player.getName(), "要打开的界面文件不存在！" }));
		return MakeForm.OpenMenu(player, file, false, false);
	}

	/**
	 * 自定义工具的创建
	 * 
	 * @author Winfxk
	 */
	public static class Make {
		/**
		 * 快捷工具的添加
		 * 
		 * @author Winfxk
		 */
		public static class Dis {
			private Player player;
			private Map<String, Object> Item = new HashMap<String, Object>();

			/**
			 * 添加一个自定义快捷工具工具
			 * 
			 * @param player       要添加这个工具的玩家对象
			 * @param FileName     要打开的界面的文件名
			 * @param ItemID       快捷工具的物品ID（可留空）
			 * @param ItemName     快捷工具的名字
			 * @param ItemLore     快捷工具的Lore
			 * @param enchantments 快捷工具将会有的附魔效果
			 * @param isPrice      是否可以通过快捷工具快速获取
			 * @param isThread     是否异步强制玩家拥有该工具
			 * @param isDrop       是否可以被对丢弃
			 * @param isWrite      玩家是否可以将该数据写入手中的物品中
			 */
			public Dis(Player player, String FileName, String ItemID, String ItemName, String ItemLore,
					List<String> ToolEnchantString, boolean isPrice, boolean isThread, boolean isDrop,
					boolean isWrite) {
				this.player = player;
				ItemLore = (ItemLore == null || ItemLore.isEmpty()) ? kick.Message.getMessage("快捷工具名称2",
						new String[] { "{Player}" }, new Object[] { player.getName() }) : ItemLore;
				ItemName = (ItemName == null || ItemName.isEmpty()) ? kick.Message.getMessage("快捷工具名称",
						new String[] { "{Player}" }, new Object[] { player.getName() }) : ItemName;
				Item.put("Key", getKey(1));
				Item.put("FileName", FileName);
				Item.put("ID", (ItemID == null || ItemID.isEmpty()) ? null : ItemIDSunName.UnknownToID(ItemID));
				Item.put("Name", ItemName.contains("\n") ? ItemName.replace("\n", "{n}") : ItemName);
				Item.put("Lore", ItemLore.contains("\n") ? ItemLore.replace("\n", "{n}") : ItemLore);
				Item.put("Enchant", ToolEnchantString == null ? new ArrayList<String>() : ToolEnchantString);
				Item.put("Player", player.getName());
				Item.put("Time", Tool.getDate() + " " + Tool.getTime());
				Item.put("isPrice", isPrice);
				Item.put("isThread", isThread);
				Item.put("isDrop", isDrop);
				Item.put("isWrite", isWrite);
			}

			/**
			 * 添加的工具为使用后模拟打开一个按钮的工具
			 * 
			 * @param ButtonKey 按钮的Key
			 * @return
			 */
			public boolean Button(String ButtonKey) {
				Item.put("ButtonKey", ButtonKey);
				Item.put("Type", "Button");
				return save();
			}

			/**
			 * 开始添加点击后打开界面的快捷工具数据
			 * 
			 * @return
			 */
			public boolean Open() {
				Item.put("Type", "Open");
				return save();
			}

			/**
			 * 保存数据
			 * 
			 * @return
			 */
			private boolean save() {
				if (Tool.isMateID(Item.get("ID"), kick.config.getString("快捷工具"))) {
					player.sendMessage("§4我们强烈的不建议您将主快捷工具所属的物品设置为自定义快捷工具！");
					return false;
				}
				Appliance.config.set(Item.get("Key").toString(), Item);
				boolean b = Appliance.config.save();
				if (b)
					player.sendMessage("§6您成功添加了一个自定义工具！");
				else
					player.sendMessage("§6您添加了一个自定义工具但保存可能出现了一点问题！");
				Appliance.enclose();
				return b;
			}

			/**
			 * 获取一个随机的Key
			 * 
			 * @param JJLength 要随机获取的Key的最小值
			 * @return
			 */
			public static String getKey(int JJLength) {
				String Key = "";
				for (int i = 0; i < JJLength; i++)
					Key += Tool.getRandString();
				return Appliance.config.exists(Key) ? getKey(JJLength++) : Key;
			}
		}

		private Player player;

		/**
		 * 开始处理玩家添加自定义工具传回的数据
		 * 
		 * @param player 传回数据的玩家对象
		 */
		public Make(Player player) {
			this.player = player;
		}

		/**
		 * 处理来至添加打开界面的快捷工具的数据
		 * 
		 * @param data
		 * @return
		 */
		public boolean addOpen(FormResponseCustom data) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
			String FileName = myPlayer.CacheFile.list()[data.getDropdownResponse(0).getElementID()];
			String ItemID = data.getInputResponse(1);
			String ToolName = data.getInputResponse(2);
			String ToolLore = data.getInputResponse(3);
			String ToolEnchantString = data.getInputResponse(4);
			boolean isPrice = data.getToggleResponse(5);
			boolean isThread = data.getToggleResponse(6);
			boolean isDrop = data.getToggleResponse(7);
			boolean isWrite = data.getToggleResponse(8);
			List<String> list = new ArrayList<String>();
			if (ToolEnchantString != null && !ToolEnchantString.isEmpty())
				if (ToolEnchantString.contains(";"))
					for (String string : ToolEnchantString.split(";"))
						list.add(string);
				else
					list.add(ToolEnchantString);
			return (new Dis(player, FileName, ItemID, ToolName, ToolLore, list, isPrice, isThread, isDrop, isWrite))
					.Open();
		}

		/**
		 * 处理来至添加模拟点击按钮的快捷工具的数据
		 * 
		 * @param data
		 * @return
		 */
		public boolean addButton(FormResponseCustom data) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
			String ButtonKey = myPlayer.keyList.get(data.getDropdownResponse(0).getElementID());
			String FileName = myPlayer.CacheFile.getName();
			String ItemID = data.getInputResponse(1);
			String ToolName = data.getInputResponse(2);
			String ToolLore = data.getInputResponse(3);
			String ToolEnchantString = data.getInputResponse(4);
			boolean isPrice = data.getToggleResponse(5);
			boolean isThread = data.getToggleResponse(6);
			boolean isDrop = data.getToggleResponse(7);
			boolean isWrite = data.getToggleResponse(8);
			List<String> list = new ArrayList<String>();
			if (ToolEnchantString != null && !ToolEnchantString.isEmpty())
				if (ToolEnchantString.contains(";"))
					for (String string : ToolEnchantString.split(";"))
						list.add(string);
				else
					list.add(ToolEnchantString);
			return (new Dis(player, FileName, ItemID, ToolName, ToolLore, list, isPrice, isThread, isDrop, isWrite))
					.Button(ButtonKey);
		}

		/**
		 * 开始判断玩家要添加的按钮是什么类型
		 * 
		 * @param player
		 * @param data
		 * @return
		 */
		public static boolean Switch(Player player, FormResponseSimple data) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, kick.Message.getMessage("权限不足"));
			switch (data.getClickedButtonId()) {
			case 1:
				return EstablishForm.ButtonByPlayer.addByType(player);
			case 0:
			default:
				return EstablishForm.OpenForm(player);
			}
		}
	}
}