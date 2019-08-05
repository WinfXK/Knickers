package xiaokai.knickers.appliance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import xiaokai.knickers.form.MakeForm;
import xiaokai.knickers.mtp.Kick;
import xiaokai.knickers.mtp.MyPlayer;
import xiaokai.tool.ItemIDSunName;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class Handle {
	private static Kick kick;
	private Player player;
	private Map<String, Object> Item;
	private Item item;

	/**
	 * 开始处理使用快捷工具的玩家对象
	 * 
	 * @param kick   Kick对象
	 * @param player 使用快捷工具的玩家对象
	 * @param Item   使用的跨界工具的项目数据对象
	 * @param item   使用的快捷工具的物品对象
	 */
	public Handle(Kick kick, Player player, Map<String, Object> Item, Item item) {
		Handle.kick = kick;
		this.player = player;
		this.Item = Item;
		this.item = item;
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
		case "open":
		default:

			break;
		}
		return true;
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
			 */
			public Dis(Player player, String FileName, String ItemID, String ItemName, String ItemLore,
					List<String> ToolEnchantString) {
				this.player = player;
				Item.put("Key", getKey(1));
				Item.put("FileName", FileName);
				Item.put("ID", (ItemID == null || ItemID.isEmpty()) ? null : ItemIDSunName.UnknownToID(ItemID));
				Item.put("Name", (ItemName == null || ItemName.isEmpty()) ? kick.Message.getMessage("快捷工具名称",
						new String[] { "{Player}" }, new Object[] { player.getName() }) : ItemName);
				Item.put("Lore", (ItemLore == null || ItemLore.isEmpty()) ? kick.Message.getMessage("快捷工具名称2",
						new String[] { "{Player}" }, new Object[] { player.getName() }) : ItemLore);
				Item.put("Enchant", ToolEnchantString == null ? new ArrayList<String>() : ToolEnchantString);
				Item.put("Player", player.getName());
				Item.put("Time", Tool.getDate() + " " + Tool.getTime());
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
			List<String> list = new ArrayList<String>();
			if (ToolEnchantString != null && !ToolEnchantString.isEmpty())
				if (ToolEnchantString.contains(";"))
					for (String string : ToolEnchantString.split(";"))
						list.add(string);
				else
					list.add(ToolEnchantString);
			return (new Dis(player, FileName, ItemID, ToolName, ToolLore, list)).Open();
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
			List<String> list = new ArrayList<String>();
			if (ToolEnchantString != null && !ToolEnchantString.isEmpty())
				if (ToolEnchantString.contains(";"))
					for (String string : ToolEnchantString.split(";"))
						list.add(string);
				else
					list.add(ToolEnchantString);
			return (new Dis(player, FileName, ItemID, ToolName, ToolLore, list)).Button(ButtonKey);
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
